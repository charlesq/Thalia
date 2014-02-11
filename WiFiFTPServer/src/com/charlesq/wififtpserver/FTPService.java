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
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent; 
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager; 
public class FTPService extends Service { 
	ServiceBinder mBinder = new ServiceBinder();
	NotificationManager mNotifyManager;
    NotificationCompat.Builder mNotificationBuilder;
    TheServer server;
   
    @Override
    public void onCreate()
    {
        Intent in=new Intent(getString(R.string.Service_Event)); 
        LocalBroadcastManager.getInstance(this).sendBroadcast(in);
        mNotifyManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationBuilder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.drawable.ic_launcher);
		sendNotification("FTP Server is up", "Awaiting connection", null, 0);/* Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | 
				Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);*/
		Settings.setContext(this);
		
		server = TheServer.getInstance(this);
		
		
		
    }
    @Override
    public void onDestroy()
    {
	    Intent in=new Intent(getString(R.string.Service_Event)); 
	    LocalBroadcastManager.getInstance(this).sendBroadcast(in);
		mNotificationBuilder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.drawable.ic_launcher);
		sendNotification("FTP Server down", "going away", null, 0);
		super.onDestroy();

    }
    private void sendNotification(String title, String text, String info, int flags)
	{
		Intent intent =  new Intent();//, ControlActivity.class);
	    intent.setFlags(flags);
	    PendingIntent pending = PendingIntent.getActivity(this, 0, intent, 0);
	    mNotificationBuilder.setContentIntent(pending);
	    mNotificationBuilder.setContentTitle(title).setContentText(text).setContentInfo(info).setWhen((new Date()).getTime());
		Notification notice = mNotificationBuilder.build();
		notice.flags |= flags;
		mNotifyManager.notify(0, notice);
	}

    @Override
    public IBinder onBind(Intent intent) 
    {
    	TheServer.halt(false);
    	new Thread(server).start();
	    return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent)
    {
    	TheServer.halt(true);
    	return false;
    }
   
    
    
    public void informSettingsChanged()
    {
    	
    }
    public void setFolder(String newFolder)
    {
    	mNotificationBuilder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.drawable.ic_launcher);
		//sendNotification("New FTP Folder set", newFolder, null, 0);
		// Intent in=new Intent(getString(R.string.Service_Event)); 
		// in.putExtra(getString(R.string.FTPFolder), newFolder);
	 //    LocalBroadcastManager.getInstance(this).sendBroadcast(in);
    }
    public class ServiceBinder extends Binder
    {
    	public FTPService getService()
    	{
    		return FTPService.this;
    	}
    }
	public void setFTPURL(InetAddress addr)
	{
		StringBuilder d = new StringBuilder();
		if (addr == null)
		{
			d.append("not available");
		}
		else
		{
			d.append("ftp:/");
			d.append(Settings.ipAddr.toString());
		//	d.append(Settings.getPreference(getString(R.string.FTPFolder)));
		}
		Intent intent = new Intent(getString(R.string.Service_Event));
		intent.putExtra(getString(R.string.FTP_URL), d.toString());
	    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}
	
	
    
}

