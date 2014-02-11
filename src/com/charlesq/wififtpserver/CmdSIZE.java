package com.charlesq.wififtpserver;
/* ported from Swiftp.CmdSIZE.java with minor changes */
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

public class CmdSIZE extends FtpCmd {
	protected String input;
	
	public CmdSIZE(SessionContext sessionContext, String input) {
		super(sessionContext);
		this.input = input;
	}
	
	public void run() {
		
		String errString = null;
		String param = getParameter(input);
		long size = 0;
		mainblock: {
			File currentDir = sessionContext.getWorkingDir();
			if(param.contains(File.separator)) {
				errString = "550 No directory traversal allowed in SIZE param\r\n";
				break mainblock;
			}
			File target = new File(currentDir, param);

			// We should have caught any invalid location access before now, but
			// here we check again, just to be explicitly sure.
			if(violatesChroot(target)) {
				errString = "550 SIZE target violates chroot\r\n";
				break mainblock;
			}
			if(!target.exists()) {
				
				break mainblock;
			}
			if(!target.isFile()) {
				errString = "550 Cannot get the size of a non-file\r\n";
				break mainblock;
			}
			size = target.length(); 
		}
		if(errString != null) {
			sessionContext.writeString(errString);
		} else {
			sessionContext.writeString("213 " + size + "\r\n");
		}
		
	}

}
