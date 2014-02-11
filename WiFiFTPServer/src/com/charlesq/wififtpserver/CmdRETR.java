package com.charlesq.wififtpserver;
/* Modified from SwiFTP code */
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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class CmdRETR extends FtpCmd implements Runnable {
	//public static final String message = "TEMPLATE!!";
	protected String input;
	
	public CmdRETR(SessionContext sessionContext, String input) {
		super(sessionContext);
		this.input = input;
	}
	
	public void run() {
		String param = getParameter(input);
		File fileToRetr;
		String errString = null;
		
		mainblock: {
			fileToRetr = inputPathToChrootedFile(sessionContext.getWorkingDir(), param);
			if(violatesChroot(fileToRetr)) {
				errString = "550 Invalid name or chroot violation\r\n";
				break mainblock;
			} else if(fileToRetr.isDirectory()) {
				errString = "550 Can't RETR a directory\r\n";
				break mainblock;
			} else if(!fileToRetr.exists()) {
				errString = "550 File does not exist\r\n";
				break mainblock;
			} else if(!fileToRetr.canRead()) {
				errString = "550 No read permissions\r\n";
				break mainblock;
			} /*else if(!sessionContext.isBinaryMode()) {
				myLog.l(Log.INFO, "Failed RETR in text mode");
				errString = "550 Text mode RETR not supported\r\n";
				break mainblock;
			}*/
			try {
				FileInputStream in = new FileInputStream(fileToRetr);
				byte[] buffer = new byte[Settings.dataChunkSize];
				int bytesRead;
				if(sessionContext.startUsingDataSocket()) {
				} else {
					errString = "425 Error opening socket\r\n";
					break mainblock;
				}
				sessionContext.writeString("150 Sending file\r\n");
				if(sessionContext.isBinaryMode()) {
					while((bytesRead = in.read(buffer)) != -1) {
						//myLog.l(Log.DEBUG,
						//		String.format("CmdRETR sending %d bytes", bytesRead));
						if(sessionContext
						   .sendViaDataSocket(buffer, bytesRead) == false) 
						{
							errString = "426 Data socket error\r\n";
							break mainblock;
						}
					}
				} else { // We're in ASCII mode
					// We have to convert all solitary \n to \r\n
					boolean lastBufEndedWithCR = false;
					while((bytesRead = in.read(buffer)) != -1) {
						int startPos = 0, endPos = 0;
						byte[] crnBuf = {'\r','\n'};
						for(endPos = 0; endPos<bytesRead; endPos++) {
							if(buffer[endPos] == '\n') {
								// Send bytes up to but not including the newline
								sessionContext.sendViaDataSocket(buffer, 
										startPos, endPos-startPos);
								if(endPos == 0) {
									// handle special case where newline occurs at
									// the beginning of a buffer
									if(!lastBufEndedWithCR) {
										// Send an \r only if the the previous
										// buffer didn't end with an \r
										sessionContext.sendViaDataSocket(crnBuf, 1);
									}
								} else if(buffer[endPos-1] != '\r') {
									// The file did not have \r before \n, add it
									sessionContext.sendViaDataSocket(crnBuf, 1);
								} else {
									// The file did have \r before \n, don't change
								}
								startPos = endPos;
							}
						}
						// Now endPos has finished traversing the array, send remaining
						// data as-is
						sessionContext.sendViaDataSocket(buffer, startPos, 
								endPos-startPos);
						if(buffer[bytesRead-1] == '\r') {
							lastBufEndedWithCR = true;
						} else {
							lastBufEndedWithCR = false;
						}
					}
				}
			} catch (FileNotFoundException e) {
				errString = "550 File not found\r\n";
				break mainblock;
			} catch(IOException e) {
				errString = "425 Network error\r\n";
				break mainblock;
			}
		}
		sessionContext.closeDataSocket();
		if(errString != null) {
			sessionContext.writeString(errString);
		} else {
			sessionContext.writeString("226 Transmission finished\r\n");
		}
	}
}
