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
import java.io.IOException;


public class CmdCWD extends FtpCmd implements Runnable {
	protected String input;
	
	public CmdCWD(SessionContext sessionContext, String input) {
		super(sessionContext);
		this.input = input;
	}
	
	public void run() {
		String param = getParameter(input);
		File newDir;
		String errString = null;
		mainblock: {
			newDir = inputPathToChrootedFile(sessionContext.getWorkingDir(), param);

			// Ensure the new path does not violate the chroot restriction
			if(violatesChroot(newDir)) {
				errString = "550 Invalid name or chroot violation\r\n";
				sessionContext.writeString(errString);
				break mainblock;
			}

			try {
				newDir = newDir.getCanonicalFile();
				if(!newDir.isDirectory()) {
					sessionContext.writeString("550 Can't CWD to invalid directory\r\n");
				} else if(newDir.canRead()) {
					sessionContext.setWorkingDir(newDir);
					sessionContext.writeString("250 CWD successful\r\n");
				} else {
					sessionContext.writeString("550 That path is inaccessible\r\n");
				}
			} catch(IOException e) {
				sessionContext.writeString("550 Invalid path\r\n");
				break mainblock;
			}
		}
	}
}
