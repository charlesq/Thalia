package com.charlesq.wififtpserver;
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
import java.io.File;
import java.net.InetAddress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class Settings {
    private static Context context;
	private static SharedPreferences mPrefernces;
	public static InetAddress ipAddr;
	protected static int inputBufferSize = 256;
	protected static int dataChunkSize = 65536;  
	public static final boolean stayAwake = false;
	public static final String STRING_ENCODING = "UTF-8";
	public static final String SESSION_ENCODING = "UTF-8"; 
	public static final int SO_TIMEOUT_MS = 30000;
	public static final int backlog = 5;
	static WakeLock mWakeLock;
	static WifiLock mWifiLock;
	public static int concurrentClients = 5;
	
	public static File chrootDir;
	public static String version = "1.0";
	
	public static synchronized void setContext(Context ctx)
	{
		if (context != null)
			return;
		Settings.context = ctx.getApplicationContext();
		mPrefernces = Settings.context.getSharedPreferences(Settings.context.getString(R.string.Preference_Store), 0);
	}
	public static Context getContext()
	{
		return context;
	} 
	public static synchronized void commitPreference(String key, String value)
	{
		SharedPreferences.Editor editor = mPrefernces.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public static String getPreference(String key)
	{
		return mPrefernces.getString(key, null);
	}
	public static synchronized void setChrootDir()
	{
		Settings.chrootDir = new File(getPreference(context.getString(R.string.FTPFolder)));
	}
	 static void acquireReleaseWifiLock(boolean lock)
	    {
	    	if (lock)
	    	{
	    		if (mWifiLock == null)
	    		{
	    			
	    			mWifiLock = ((WifiManager)context.getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL,  context.getString(R.string.app_name));
	    			mWifiLock.setReferenceCounted(false);
	    		}
	    		mWifiLock.acquire();
	    	}
	    	else
	    	{
	    		if (mWifiLock != null)
	    			mWifiLock.release();
	    	}
	    }
	    @SuppressLint("Wakelock")
		static void acquireReleaseWakeLock(boolean lock)
	    {
	    	if (lock)
	    	{
	    		if (mWakeLock == null)
	    		{
	    			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	    			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, context.getString(R.string.app_name));
	    		//	mWakeLock = ((PowerManager)context.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP, context.getString(R.string.app_name));
	    			mWakeLock.setReferenceCounted(false);
	    		}
	    		mWakeLock.acquire();
	    	}
	    	else
	    	{
	    		if (mWakeLock != null)
	    			mWakeLock.release();
	    	}
	    }
	    
}
