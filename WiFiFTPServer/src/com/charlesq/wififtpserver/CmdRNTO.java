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

public class CmdRNTO extends FtpCmd implements Runnable {
	protected String input;

	public CmdRNTO(SessionContext sessionContext, String input) {
		super(sessionContext);
		this.input = input;
	}
	
	public void run() {
		String param = getParameter(input);
		String errString = null;
		File toFile = null;
		mainblock: {
			toFile = inputPathToChrootedFile(sessionContext.getWorkingDir(), param);
			if(violatesChroot(toFile)) {
				errString = "550 Invalid name or chroot violation\r\n";
				break mainblock;
			}
			File fromFile = sessionContext.getRenameFrom();
			if(fromFile == null) {
				errString = "550 Rename error, maybe RNFR not sent\r\n";
				break mainblock;
			}
			if(!fromFile.renameTo(toFile)) {
				errString = "550 Error during rename operation\r\n";
				break mainblock;
			}
		}
		if(errString != null) {
			sessionContext.writeString(errString);
		} else {
			sessionContext.writeString("250 rename successful\r\n");
		}
		sessionContext.setRenameFrom(null);
	}
}
