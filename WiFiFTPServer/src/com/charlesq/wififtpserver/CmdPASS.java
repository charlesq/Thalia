package com.charlesq.wififtpserver;

import android.util.Log;
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


public class CmdPASS extends FtpCmd implements Runnable {
	String input;
	
	public CmdPASS(SessionContext sessionContext, String input) {
		// We can just discard the password for now. We're just
		// following the expected dialogue, we're going to allow
		// access in any case.
		super(sessionContext);
		this.input = input;
	}
	
	public void run() {
		// User must have already executed a USER command to
		// populate the Account object's username
	
		String attemptPassword = getParameter(input, true); // silent
		String attemptUsername = sessionContext.account.getUsername();
		Log.e(Settings.getContext().getString(R.string.app_name), "attempt usename is " + attemptUsername);
		Log.e(Settings.getContext().getString(R.string.app_name), "attempt password is " + attemptPassword);
		if(attemptUsername == null) {
			sessionContext.writeString("503 Must send USER first\r\n");
			return;
		}
		String password;
		String username;
		username = Settings.getPreference(Settings.getContext().getString(R.string.UserName));
        password = Settings.getPreference(Settings.getContext().getString(R.string.PassWord));
        Log.e(Settings.getContext().getString(R.string.app_name), "retrieved usename is " + username);
		Log.e(Settings.getContext().getString(R.string.app_name), "retrieved password is " + password);
		if(username == null || password == null) {
			sessionContext.writeString("500 Internal error during authentication");
		} else if(username.equals(attemptUsername) && 
				password.equals(attemptPassword)) {
			sessionContext.writeString("230 Access granted\r\n");
			sessionContext.authAttempt(true);
		} else {
			try {
				// If the login failed, sleep for one second to foil
				// brute force attacks
				Thread.sleep(1000);
			} catch(InterruptedException e) {}
			sessionContext.writeString("530 Login incorrect.\r\n");
			sessionContext.authAttempt(false);
		}
	}
}
