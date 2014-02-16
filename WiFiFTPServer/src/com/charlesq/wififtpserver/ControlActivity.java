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

import java.util.HashMap;
import java.util.Map;

import com.charlesq.wififtpserver.FTPService.ServiceBinder;

import android.app.Activity; 
import android.content.BroadcastReceiver; 
import android.content.ComponentName;
import android.content.Context; 
import android.content.Intent; 
import android.content.IntentFilter; 
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle; 
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager; 
import android.view.View; 
import android.widget.Button; 
import android.widget.TextView; 
public class ControlActivity extends Activity implements WifiStateChangeReceiver.WifiStateChangeListener{ 
	static Map<String, String> mSettingMap = new HashMap<String, String>();
    TextView mPort, mUsername, mPassword,mWiFi, mURL, mFolder;
    Button mStartStopButton;
    FTPService mService;
    ServiceConnection mConnection;
    private boolean mStoredState = false; /* no service not started */
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    { 
        super.onCreate(savedInstanceState); 
        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.ui_start_stop)))
        {
            String state = savedInstanceState.getCharSequence(getString(R.string.ui_start_stop)).toString();
            if (state.equalsIgnoreCase("Stop"))
        	    mStoredState = true;
        }
        setContentView(R.layout.activity_control);
        mPort = (TextView)findViewById(R.id.port_number);
        mUsername = (TextView)findViewById(R.id.username);
        mPassword = (TextView)findViewById(R.id.passwd);
        mURL = (TextView)findViewById(R.id.url);
        mWiFi = (TextView)findViewById(R.id.wifi_state);
        mFolder = (TextView)findViewById(R.id.ftp_folder);
        mStartStopButton = (Button)findViewById(R.id.start_stop);
        mWiFi.setText(isWifiConnected()?"connected": "disconnected");
        Settings.setContext(this);
        restoreSettings();
        String s = Settings.getPreference(getString(R.string.ui_start_stop));
        
        if (s != null && s.equalsIgnoreCase("Stop"))
        {
        	doBindService();
            mStartStopButton.setText("stop");
            Settings.commitPreference(getString(R.string.ui_start_stop), "start");
        }
        

    }
    @Override
    protected void  onRestoreInstanceState (Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState); 
        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.ui_start_stop)))
        {
            String state = savedInstanceState.getCharSequence(getString(R.string.ui_start_stop)).toString();
            if (state.equalsIgnoreCase("Stop"))
        	    mStoredState = true;
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
      outState.putCharSequence(getString(R.string.ui_start_stop), mStartStopButton.getText());
      Settings.commitPreference(getString(R.string.ui_start_stop), mStartStopButton.getText().toString());
      
    }

    @Override
    protected void onPause() 
    { 
        super.onPause(); 
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onLBMessage); 
        WifiStateChangeReceiver.unregisterListener(this);
    } 
    @Override
    protected void onResume() 
    { 
        super.onResume();
        IntentFilter filter= new IntentFilter(getString(R.string.Service_Event)); 
        LocalBroadcastManager.getInstance(this).registerReceiver(onLBMessage, filter);
        WifiStateChangeReceiver.registerListener(this);
        preserveSharedPreferences();
        loadSharedPreferences();
        if (mStoredState)
        {
        	 doBindService();
             mStartStopButton.setText("Stop");
        	 mStoredState = false;
        }
    } 
    
   
    public void onSettingsClick(View v)
    {

        Intent intent = new Intent(this, ConfigActivity.class);
        startActivity(intent);
    }
    
    public void onStartStopClick(View v) 
    { 
    	 if (new String("start").equalsIgnoreCase(mStartStopButton.getText().toString()) && mService == null)
         {
                doBindService();
                mStartStopButton.setText("Stop");

         }
         else
         {
                 mStartStopButton.setText("Start");
                 unbindService(mConnection);
                 mService = null;
         }

    }
    private BroadcastReceiver onLBMessage= new BroadcastReceiver() 
    { 
        @Override
        public void onReceive(Context context, Intent intent) 
        { 
        	if (intent.getAction().equalsIgnoreCase(getString(R.string.Service_Event)))
        	{
        		String newValue = intent.getStringExtra(getString(R.string.FTPFolder));
        		if (newValue != null)
        		{
        		   // mWiFi.setText(newValue);
        		    return;
        		}
        		newValue = intent.getStringExtra(getString(R.string.FTP_URL));
        		if (newValue != null)
        		{
        		    mURL.setText(newValue);
        		    return;
        		}
        	}
        	
        } 
    }; 
    /* a bind service helper */
    private void doBindService()
    {
    	if (mConnection == null)
    	{
        mConnection = new ServiceConnection()
        {
            @Override
		    public void onServiceConnected(ComponentName name,
						IBinder service) {
					mService = ((ServiceBinder)service).getService();
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
			       mService = null;
			}
		};
    	}
        bindService(new Intent(this, FTPService.class), mConnection, Context.BIND_AUTO_CREATE);
    	
    }
    
    private void preserveSharedPreferences()
    {
             
        String value = mSettingMap.get(getString(R.string.PortNumber));
        
        if (value != null)
        {
              mPort.setText(value);
              Settings.commitPreference(getString(R.string.PortNumber), value);

         }
         value = mSettingMap.get(getString(R.string.UserName));
         if (value != null)
         {
              mUsername.setText(value);
              Settings.commitPreference(getString(R.string.UserName), value);
         }
         value = mSettingMap.get(getString(R.string.PassWord));
         if (value != null)
         {
              mPassword.setText(value);
              Settings.commitPreference(getString(R.string.PassWord), value);
         }
         value = mSettingMap.get(getString(R.string.FTPFolder));
         if (value != null)
         {
              mPassword.setText(value);
              Settings.commitPreference(getString(R.string.FTPFolder), value); 
          }
    }
    public void loadSharedPreferences()
    {
    
        mPort.setText(Settings.getPreference(getString(R.string.PortNumber)));
  	    mUsername.setText(Settings.getPreference(getString(R.string.UserName)));
    	mPassword.setText(Settings.getPreference(getString(R.string.PassWord)));
        mFolder.setText(Settings.getPreference(getString(R.string.FTPFolder)));
        Settings.setChrootDir();
        if (mService != null)
        {
        	 mService.setFolder(Settings.getPreference(getString(R.string.FTPFolder)));
        }
        mSettingMap.clear();

    }
    /* if SharedPreference has no value for a key, load it with from layout settings */
    private void restoreSettings()
    {
    	String value = Settings.getPreference(getString(R.string.UserName));
    	if (Settings.getPreference(getString(R.string.UserName)) == null)
    	{
    		Settings.commitPreference(getString(R.string.UserName), mUsername.getText().toString());
    	}
    	if (Settings.getPreference(getString(R.string.PassWord)) == null)
    	{
    		Settings.commitPreference(getString(R.string.PassWord), mPassword.getText().toString());
    	}
    	if (Settings.getPreference(getString(R.string.FTPFolder)) == null)
    	{
    		Settings.commitPreference(getString(R.string.FTPFolder), mFolder.getText().toString());
    	}
     	value = Settings.getPreference(getString(R.string.PortNumber));
    	if (Settings.getPreference(getString(R.string.PortNumber)) == null)
    	{
    		Settings.commitPreference(getString(R.string.PortNumber), mPort.getText().toString());
    	}
    }
	@Override
	public void stateChanged(boolean up) {
		mWiFi.setText(up? "connected":"disconnected");
		
	}
	 public boolean isWifiConnected()
	 {
	  	NetworkInfo w = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	   	return w.isConnected();
	 }
	
}