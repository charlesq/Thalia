package com.charlesq.wififtpserver;
/* Modified from Swiftp.SessionThread.java
 * thought the name SessionContext might be more pertinent for its purpose
 * besides removed loggings.
 * 
 */
/*
Copyright 2009 David Revell

This file is part of SwiFTP.

SwiFTP is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SwiFTP is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import android.util.Log;

public class SessionContext implements Runnable
{
	protected boolean shouldExit = false;
	protected Socket cmdSocket;
	protected ByteBuffer buffer = ByteBuffer.allocate(Settings.inputBufferSize);
	protected boolean pasvMode = false;
	protected boolean binaryMode = false;
	protected Account account = new Account();
	protected boolean authenticated = false;
	protected File workingDir = Settings.chrootDir;
	protected Socket dataSocket = null;
	protected File renameFrom = null;
	protected DataSocketFactory dataSocketFactory;
	OutputStream dataOutputStream = null;
	protected String encoding = Settings.SESSION_ENCODING;
	int authFails = 0;
	public static int MAX_AUTH_FAILS = 3;
	
	/**
	 * Sends a string over the already-established data socket
	 * 
	 * @param string
	 * @return Whether the send completed successfully
	 */
	public boolean sendViaDataSocket(String string) {
		try {
			byte[] bytes = string.getBytes(encoding);
			return sendViaDataSocket(bytes, bytes.length);
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}

	public boolean sendViaDataSocket(byte[] bytes, int len) {
		return sendViaDataSocket(bytes, 0, len);
	}

	/**
	 * Sends a byte array over the already-established data socket
	 * 
	 * @param bytes
	 * @param len
	 * @return
	 */
	public boolean sendViaDataSocket(byte[] bytes, int start, int len) {

		if (dataOutputStream == null) {
			
			return false;
		}
		if (len == 0) {
			return true; // this isn't an "error"
		}
		try {
			dataOutputStream.write(bytes, start, len);
		} catch (IOException e) {
			
			return false;
		}
		return true;
	}

	/**
	 * Received some bytes from the data socket, which is assumed to already be
	 * connected. The bytes are placed in the given array, and the number of
	 * bytes successfully read is returned.
	 * 
	 * @param bytes
	 *            Where to place the input bytes
	 * @return >0 if successful which is the number of bytes read, -1 if no
	 *         bytes remain to be read, -2 if the data socket was not connected,
	 *         0 if there was a read error
	 */
	public int receiveFromDataSocket(byte[] buf) {
		int bytesRead;

		if (dataSocket == null) {
			
			return -2;
		}
		if (!dataSocket.isConnected()) {
			
			return -2;
		}
		InputStream in;
		try {
			in = dataSocket.getInputStream();
			// If the read returns 0 bytes, the stream is not yet
			// closed, but we just want to read again.
			while ((bytesRead = in.read(buf, 0, buf.length)) == 0) {
			}
			if (bytesRead == -1) {
				// If InputStream.read returns -1, there are no bytes
				// remaining, so we return 0.
				return -1;
			}
		} catch (IOException e) {
			
			return 0;
		}
		return bytesRead;
	}

	/**
	 * Called when we receive a PASV command.
	 * 
	 * @return Whether the necessary initialization was successful.
	 */
	public int onPasv() {
		return dataSocketFactory.onPasv();
	}

	/**
	 * Called when we receive a PORT command.
	 * 
	 * @return Whether the necessary initialization was successful.
	 */
	public boolean onPort(InetAddress dest, int port) {
		return dataSocketFactory.onPort(dest, port);
	}

	public InetAddress getDataSocketPasvIp() {
		// When the client sends PASV, our reply will contain the address and port
		// of the data connection that the client should connect to. For this purpose
		// we always use the same IP address that the command socket is using.
		return cmdSocket.getLocalAddress();

		// The old code, not totally correct.
		//		return dataSocketFactory.getPasvIp();
	}

	// public int getDataSocketPort() {
	// return dataSocketFactory.getPortNumber();
	// }

	/**
	 * Will be called by (e.g.) CmdSTOR, CmdRETR, CmdLIST, etc. when they are
	 * about to start actually doing IO over the data socket.
	 * 
	 * @return
	 */
	public boolean startUsingDataSocket() {
		try {
			dataSocket = dataSocketFactory.onTransfer();
			if (dataSocket == null) {
				return false;
			}
			dataOutputStream = dataSocket.getOutputStream();
			return true;
		} catch (IOException e) {

			dataSocket = null;
			return false;
		}
	}

	public void quit() {
		closeSocket();
	}

	public void closeDataSocket() {
		
		if (dataOutputStream != null) {
			try {
				dataOutputStream.close();
			} catch (IOException e) {
			}
			dataOutputStream = null;
		}
		if (dataSocket != null) {
			try {
				dataSocket.close();
			} catch (IOException e) {
			}
		}
		dataSocket = null;
	}

	protected InetAddress getLocalAddress() {
		return cmdSocket.getLocalAddress();
	}

	static int numNulls = 0;
	public void run() {
		writeString("220 QFTP " + Settings.version + " ready\r\n");
		// Main loop: read an incoming line and process it
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(cmdSocket
					.getInputStream()), 8192); // use 8k buffer
			while (true) {
				String line;
				line = in.readLine(); // will accept \r\n or \n for terminator
				
				if (line != null) {
					Log.e(Settings.getContext().getString(R.string.app_name), line);
					FtpCmd.dispatchCommand(this, line);
					
				} else {
					break;
				}
			}
		} catch (IOException e) {
		}
		closeSocket();
		TheServer.removeClient(this);
	}

	/**
	 * A static method to check the equality of two byte arrays, but only up to
	 * a given length.
	 */
	public static boolean compareLen(byte[] array1, byte[] array2, int len) {
		for (int i = 0; i < len; i++) {
			if (array1[i] != array2[i]) {
				return false;
			}
		}
		return true;
	}

	public void closeSocket() {
		if (cmdSocket == null) {
			return;
		}
		try {
			cmdSocket.close();
		} catch (IOException e) {}
	}

	public void writeBytes(byte[] bytes) {
		try {
			BufferedOutputStream out = new BufferedOutputStream(cmdSocket
					.getOutputStream(), Settings.dataChunkSize);
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			closeSocket();
			return;
		}
	}

	public void writeString(String str) {
				byte[] strBytes;
		try {
			strBytes = str.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			strBytes = str.getBytes();
		}
		writeBytes(strBytes);
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public boolean isPasvMode() {
		return pasvMode;
	}

	public SessionContext(Socket socket, DataSocketFactory dataSocketFactory
			) {
		this.cmdSocket = socket;
		this.dataSocketFactory = dataSocketFactory;
		
	}

	static public ByteBuffer stringToBB(String s) {
		return ByteBuffer.wrap(s.getBytes());
	}

	public boolean isBinaryMode() {
		return binaryMode;
	}

	public void setBinaryMode(boolean binaryMode) {
		this.binaryMode = binaryMode;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void authAttempt(boolean authenticated) {
		if (authenticated) {
			this.authenticated = true;
		} else {
				authFails++;
			if(authFails > MAX_AUTH_FAILS) {
				
				quit();
			}
		}
		
	}

	public File getWorkingDir() {
		return workingDir;
	}

	public void setWorkingDir(File workingDir) {
		try {
			this.workingDir = workingDir.getCanonicalFile().getAbsoluteFile();
		} catch (IOException e) {
			
		}
	}


	public Socket getDataSocket() {
		return dataSocket;
	}
	
	public File getRenameFrom() {
		return renameFrom;
	}

	public void setRenameFrom(File renameFrom) {
		this.renameFrom = renameFrom;
	}
	
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
