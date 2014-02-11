package com.charlesq.wififtpserver;
/* Modified from SwiFTP.FtpCmd.java
 * 1. Substituted Globals with Settings.
 * 2. Substituted SessionThread with the new SessionContext
 * 3. Remvoed all loggings
 * 4. Merged CmdMap.java
 * 
 * 
 */
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
import java.lang.reflect.Constructor;

public abstract class FtpCmd implements Runnable {

	protected SessionContext sessionContext;	
	protected static CmdMap[] cmdClasses = {
			new CmdMap("SYST", CmdSYST.class),
			new CmdMap("USER", CmdUSER.class),
			new CmdMap("PASS", CmdPASS.class),
			new CmdMap("TYPE", CmdTYPE.class),
			new CmdMap("CWD",  CmdCWD.class),
			new CmdMap("PWD",  CmdPWD.class),
			new CmdMap("LIST", CmdLIST.class),
			new CmdMap("PASV", CmdPASV.class),
			new CmdMap("RETR", CmdRETR.class),
			new CmdMap("NLST", CmdNLST.class),
			new CmdMap("NOOP", CmdNOOP.class),
			new CmdMap("STOR", CmdSTOR.class),
			new CmdMap("DELE", CmdDELE.class),
			new CmdMap("RNFR", CmdRNFR.class),
			new CmdMap("RNTO", CmdRNTO.class),
			new CmdMap("RMD",  CmdRMD.class),
			new CmdMap("MKD",  CmdMKD.class),
			new CmdMap("OPTS", CmdOPTS.class),
			new CmdMap("PORT", CmdPORT.class),
			new CmdMap("QUIT", CmdQUIT.class),
			new CmdMap("FEAT", CmdFEAT.class),
			new CmdMap("SIZE", CmdSIZE.class),
			new CmdMap("CDUP", CmdCDUP.class),
			new CmdMap("APPE", CmdAPPE.class),
			new CmdMap("XCUP", CmdCDUP.class), // synonym
			new CmdMap("XPWD", CmdPWD.class),  // synonym
			new CmdMap("XMKD", CmdMKD.class),  // synonym
			new CmdMap("XRMD", CmdRMD.class)   // synonym
	};
	
	public FtpCmd(SessionContext session) {
		this.sessionContext= session;

	}
	
	@Override
	abstract public void run();
	
	protected static void dispatchCommand(SessionContext session, 
	                                      String inputString) {
		String[] strings = inputString.split(" ");
		String unrecognizedCmdMsg = "502 Command not recognized\r\n";
		if(strings == null) {
			return;
		}
		if(strings.length < 1) {
			
			session.writeString(unrecognizedCmdMsg);
			return;
		}
		String verb = strings[0];
		if(verb.length() < 1) {
			session.writeString(unrecognizedCmdMsg);
			return;
		}
		FtpCmd cmdInstance = null;
		verb = verb.trim();
		verb = verb.toUpperCase();
		for(int i=0; i<cmdClasses.length; i++) {
			
			if(cmdClasses[i].getName().equals(verb)) {
				// We found the correct command. We retrieve the corresponding
				// Class object, get the Constructor object for that Class, and 
				// and use that Constructor to instantiate the correct FtpCmd 
				// subclass. Yes, I'm serious.
				Constructor<? extends FtpCmd> constructor; 
				try {
					constructor = cmdClasses[i].getCommand().getConstructor(
							new Class[] {SessionContext.class, String.class});
				} catch (NoSuchMethodException e) {
					
					return;
				}
				try {
					cmdInstance = constructor.newInstance(
							new Object[] {session, inputString});
				} catch(Exception e) {
			
					return;
				}
			}
		}
		if(cmdInstance == null) {
			session.writeString(unrecognizedCmdMsg);
			return;
		} else if(session.isAuthenticated() 
				|| cmdInstance.getClass().equals(CmdUSER.class)
				|| cmdInstance.getClass().equals(CmdPASS.class)
				|| cmdInstance.getClass().equals(CmdUSER.class))
		{
			// Unauthenticated users can run only USER, PASS and QUIT 
			cmdInstance.run();
		} else {
			session.writeString("530 Login first with USER and PASS\r\n");
		}
	}
		
	/**
	 * An FTP parameter is that part of the input string that occurs
	 * after the first space, including any subsequent spaces. Also,
	 * we want to chop off the trailing '\r\n', if present.
	 * 
	 * Some parameters shouldn't be logged or output (e.g. passwords),
	 * so the caller can use silent==true in that case.
	 */
	static public String getParameter(String input, boolean silent) {
		if(input == null) {
			return "";
		}
		int firstSpacePosition = input.indexOf(' ');
		if(firstSpacePosition == -1) {
			return "";
		}
		String retString = input.substring(firstSpacePosition+1);
		
		// Remove trailing whitespace
		// todo: trailing whitespace may be significant, just remove \r\n
		retString = retString.replaceAll("\\s+$", "");
		
		if(!silent) {
			
		}
		return retString; 
	}
	
	/**
	 * A wrapper around getParameter, for when we don't want it to be silent.
	 */
	static public String getParameter(String input) {
		return getParameter(input, false);
	}

	public static File inputPathToChrootedFile(File existingPrefix, String param) {
		try {
			if(param.charAt(0) == '/') {
				// The STOR contained an absolute path
				File chroot = Settings.chrootDir;
				return new File(chroot, param);
			}
		} catch (Exception e) {} 
		
		// The STOR contained a relative path
		return new File(existingPrefix, param); 
	}
	
	public boolean violatesChroot(File file) {
		File chroot = Settings.chrootDir;
		try {
			String canonicalPath = file.getCanonicalPath();
			if(!canonicalPath.startsWith(chroot.toString())) {
				
				return true; // the path must begin with the chroot path
			}
			return false;
		} catch(Exception e) 
		{
			return true;  // for security, assume violation
		}
	}

    public static class CmdMap {
    	protected Class<? extends FtpCmd> cmdClass;
    	String name;
    	public CmdMap(String name, Class<? extends FtpCmd> cmdClass) 
    	{
    		this.name = name;
    		this.cmdClass = cmdClass;
    	}
    	public Class<? extends FtpCmd> getCommand() 
    	{
    		return cmdClass;
    	}

	    public void setCommand(Class<? extends FtpCmd> cmdClass) 
	    {
		    this.cmdClass = cmdClass;
	    }

	    public String getName() {
		    return name;
	    }

	    public void setName(String name) 
	    {
		    this.name = name;
	    }
}


}
