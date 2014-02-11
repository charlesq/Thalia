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



public class CmdOPTS extends FtpCmd implements Runnable {
	private String input;
	
	public CmdOPTS(SessionContext sessionContext, String input) {
		super(sessionContext);
		this.input = input;
	}
	
	public void run() {
		String param = getParameter(input);
		String errString = null;
		
		mainBlock: {
			if(param == null) {
				errString = "550 Need argument to OPTS\r\n";
				break mainBlock;
			}
			String[] splits = param.split(" ");
			if(splits.length != 2) {
				errString = "550 Malformed OPTS command\r\n";
				break mainBlock;
			}
			String optName = splits[0].toUpperCase();
			String optVal = splits[1].toUpperCase();
			if(optName.equals("UTF8")) {
				// OK, whatever. Don't really know what to do here. We
				// always operate in UTF8 mode.
				if(optVal.equals("ON")) {
					sessionContext.setEncoding("UTF-8");
				} else 
				break mainBlock;
			} else {
				errString = "502 Unrecognized option\r\n";
				break mainBlock;
			}
		}
		if(errString != null) {
			sessionContext.writeString(errString);
		} else {
			sessionContext.writeString("200 OPTS accepted\r\n");
		}
	}

}
