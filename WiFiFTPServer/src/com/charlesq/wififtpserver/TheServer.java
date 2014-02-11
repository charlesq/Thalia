package com.charlesq.wififtpserver;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class TheServer implements Runnable,  WifiStateChangeReceiver.WifiStateChangeListener{
    private FTPService host;
	private ServerSocket sock;
	private static TheServer server = null;
	private static boolean halt = false;
	public static CopyOnWriteArrayList<SessionContext> clients =new CopyOnWriteArrayList<SessionContext>();
	private ExecutorService executor = Executors.newFixedThreadPool(Settings.concurrentClients);
	private TheServer(FTPService host)
	{
		
		this.host = host;
		clients.clear();
		try {
			Settings.ipAddr = getIPAddr();
		} catch (UnknownHostException e) {
			Settings.ipAddr = null;
		}
	}
	public static synchronized void halt(boolean stop)
	{
		halt = stop;
	}
	public static TheServer getInstance(FTPService h)
	{
		if (server == null)
		{
			server = new TheServer(h);
		}
		return server;
	}
	
	@Override
	public void run() 
	{
		
		
		WifiStateChangeReceiver.registerListener(this);
		mainblock:
		
		while(!halt)
		{
			if (!isWifiConnected())
				continue;
			Settings.acquireReleaseWakeLock(true);
			Settings.acquireReleaseWifiLock(true);
			
			if (!setupListeningrSocket())
			{
				host.setFTPURL(null);
				continue;
			}
			host.setFTPURL(Settings.ipAddr);
			while (Settings.ipAddr != null && !halt)
			{
				acceptClientConnection();
				
			}
			Settings.acquireReleaseWifiLock(false);
			Settings.acquireReleaseWakeLock(false);
		}
		try {
			sock.close();
		} catch (IOException e) {
		}
		host.setFTPURL(null);
		WifiStateChangeReceiver.unregisterListener(this);
	}
	private synchronized boolean setupListeningrSocket()
	{
		
		try {
			sock = new ServerSocket();
			sock.setReuseAddress(true);
		} catch (IOException e) {
		   return false;
		}
		int backlog = Settings.backlog;
		int port = Integer.parseInt(Settings.getPreference(Settings.getContext().getString(R.string.PortNumber)));
		InetSocketAddress i = new InetSocketAddress(Settings.ipAddr,port);
	//	String s = Settings.getPreference(Settings.getContext().getString(R.string.PortNumber));
		try {
			sock.bind(i, backlog);
		} catch (IOException e) {
			try {
				sock.close();
			} catch (IOException e1) {
			}
			return false;
		}
		
		try {
			sock.setSoTimeout(1000);
		} catch (SocketException e) {
			try {
				sock.close();
			} catch (IOException e1) {
			}
			return false;
		}
		
		return true;
	}
	public boolean isWifiConnected()
    {
    	NetworkInfo w = ((ConnectivityManager) Settings.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    	return w.isConnected();
    }
	private InetAddress getIPAddr() throws UnknownHostException
    {
       if (!isWifiConnected())
    	   return null;
    	int addr = ((WifiManager)Settings.getContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress();
    	ByteBuffer b = ByteBuffer.allocate(4);
    	b.order(ByteOrder.LITTLE_ENDIAN); // optional, the initial order of a byte buffer is always BIG_ENDIAN.
    	b.putInt(addr);

    	byte[] array = b.array();	
    	
    	try
    	{
    		return InetAddress.getByAddress(array);  	
    	}
    	catch (UnknownHostException e)
    	{
    		return null;
    	}
    	
    }
	private void acceptClientConnection()
	{
		try {
			Socket s = sock.accept();
			SessionContext sc = new SessionContext(s, new DataSocketFactory());
			addClient(sc);
			executor.execute(sc);
			
		} catch (IOException e) {
		}
	}
	 @Override
     public void stateChanged(boolean up) {
             if (up)
             {
                     try {
                             Settings.ipAddr = getIPAddr();
                             setupListeningrSocket();
                     } catch (UnknownHostException e) {
                             Settings.ipAddr = null;
                     }
             }
             else
             {
                     Settings.ipAddr = null;
             }
     }
	 public static void removeClient(SessionContext sc)
	 {
	     clients.remove(sc);
	 }
	 public static void addClient(SessionContext sc)
	 {
		 clients.add(sc);
	 }
	 

}
