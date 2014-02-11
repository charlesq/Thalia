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
 *//*
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


public class CmdDELE extends FtpCmd implements Runnable {
	protected String input; 
	
	public CmdDELE(SessionContext sessionContext, String input) {
		super(sessionContext);
		this.input = input;
	}
	
	public void run() {
		String param = getParameter(input);
		File storeFile = inputPathToChrootedFile(sessionContext.getWorkingDir(), param);
		String errString = null;
		if(violatesChroot(storeFile)) {
			errString = "550 Invalid name or chroot violation\r\n";
		} else if(storeFile.isDirectory()) {
			errString = "550 Can't DELE a directory\r\n";
		} else if(!storeFile.delete()) {
			errString = "450 Error deleting file\r\n";
		}
		
		if(errString != null) {
			sessionContext.writeString(errString);
		} else {
			sessionContext.writeString("250 File successfully deleted\r\n");
		}
	}

}
