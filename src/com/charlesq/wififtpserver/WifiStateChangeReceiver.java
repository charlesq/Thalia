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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;


public class WifiStateChangeReceiver extends BroadcastReceiver{
   
	private static List<WifiStateChangeListener> listeners = new ArrayList<WifiStateChangeListener>();
	private static Object o = new Object(); 
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		
		String action = intent.getAction();
		if (!action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION))
		    return;
		int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
		switch(state)
		{
		case WifiManager.WIFI_STATE_ENABLED:
		case WifiManager.WIFI_STATE_DISABLING:
			Iterator<WifiStateChangeListener> it = listeners.iterator();
			while(it.hasNext())
			{
				it.next().stateChanged(state == WifiManager.WIFI_STATE_ENABLED);
			}
			break;
		case WifiManager.WIFI_STATE_DISABLED:
		case WifiManager.WIFI_STATE_ENABLING:
			/* fall through */
		
		default:
			
		}
	}
	public static synchronized void registerListener(WifiStateChangeListener l)
	{
		synchronized (o)
		{
		    listeners.add(l);
		}
	}
	public static synchronized void unregisterListener(WifiStateChangeListener l)
	{
		synchronized (o)
		{
		    listeners.remove(l);
		}
	}
	public interface WifiStateChangeListener
	{
	 	public void stateChanged(boolean up);
	}

}
