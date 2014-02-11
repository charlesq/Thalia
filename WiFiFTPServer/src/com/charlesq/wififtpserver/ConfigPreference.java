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
import com.charlesq.wififtpserver.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceClickListener;
import android.app.Activity;
import android.preference.EditTextPreference;
import android.preference.*;
import static android.preference.Preference.OnPreferenceChangeListener;
public class ConfigPreference extends PreferenceFragment {
	Activity mInvoker;
	OnPreferenceChangeListener mChangeListener = new ConfigOnClickChangeListener(this);
    public void setInvoker(Activity act)
    {
        mInvoker = act;
    }
   
        @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        Preference button = getPreferenceManager().findPreference("Button");
        button.setOnPreferenceClickListener(new OnPreferenceClickListener ()
        {
            public boolean onPreferenceClick(Preference arg0)
            {
            
                mInvoker.finish();
                return true;
            }
         }
        );
        
        ((EditTextPreference)getPreferenceManager().findPreference(getString(R.string.PortNumber))).setOnPreferenceChangeListener(mChangeListener);
        ((EditTextPreference)getPreferenceManager().findPreference(getString(R.string.UserName))).setOnPreferenceChangeListener(mChangeListener);
        ((EditTextPreference)getPreferenceManager().findPreference(getString(R.string.PassWord))).setOnPreferenceChangeListener(mChangeListener);
        ((EditTextPreference)getPreferenceManager().findPreference(getString(R.string.FTPFolder))).setOnPreferenceChangeListener(mChangeListener);


    }
        
    class ConfigOnClickChangeListener implements OnPreferenceChangeListener
    {
        
        ConfigOnClickChangeListener(ConfigPreference cfp)
        {
            

         }
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue)
        {
            settingsPassthrough(preference, (String)newValue);
            return true;
        }
    }
    /* pass setting changes back to ConfigActivity */
    private void settingsPassthrough(Preference arg, String data)
    {
    	ControlActivity.mSettingMap.put(arg.getKey(), data);
    }

    

}

                                                                                                                                        