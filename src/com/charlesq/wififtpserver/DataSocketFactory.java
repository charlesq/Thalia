package com.charlesq.wififtpserver;
/* modified from SWiftp.DataSocketFactory.java and Swiftp.NormalDataSocketFactory */
/*
 *  This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class DataSocketFactory {
	ServerSocket server = null;
	InetAddress remoteAddr;
	int remotePort;
	boolean isPasvMode = true;
	private void clearState() {
		if(server != null) {
			try {
				server.close();
			} catch (IOException e) {}
		}
		server = null;
		remoteAddr = null;
		remotePort = 0;
	}
	public int onPasv() {
		clearState();
		try {
		//	int backlog = Integer.parseInt(Settings.getPreference(Settings.getContext().getString(R.string.Backlog)));
			server = new ServerSocket(0,1);
			return server.getLocalPort();
		} catch(IOException e) {
			clearState();
			return 0;
		}
	}

	public boolean onPort(InetAddress remoteAddr, int remotePort) {
		clearState();
		this.remoteAddr = remoteAddr;
		this.remotePort = remotePort;
		return true;
	}
	
	public Socket onTransfer() {
		if(server == null) {
			// We're in PORT mode (not PASV)
			if(remoteAddr == null || remotePort == 0) {
				clearState();
				return null;
			}
			Socket socket;
			try {
				socket = new Socket(remoteAddr, remotePort);
			} catch (IOException e) {
				clearState();
				return null;
			}
			
			// Kill the socket if nothing happens for X milliseconds
			try {
				socket.setSoTimeout(Settings.SO_TIMEOUT_MS);
			} catch (Exception e) {
				clearState();
				return null;
			}
			
			return socket;
		} else {
			// We're in PASV mode (not PORT)
			Socket socket = null;
			try {
				socket = server.accept();
			} catch (Exception e) {
				socket = null;
			}
			clearState();
			return socket;  // will be null if error occurred
		}
	}
	
	/**
	 * Return the port number that the remote client should be informed of (in the body
	 * of the PASV response).
	 * @return The port number, or -1 if error.
	 */
	public int getPortNumber() {
		if(server != null) {
			return server.getLocalPort(); // returns -1 if serversocket is unbound 
		} else {
			return -1;
		}
	}
	
	public InetAddress getPasvIp() {
		return Settings.ipAddr;
	}
}
