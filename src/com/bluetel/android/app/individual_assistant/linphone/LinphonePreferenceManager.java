package com.bluetel.android.app.individual_assistant.linphone;

import com.bluetel.android.app.individual_assistant.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LinphonePreferenceManager {

	private static LinphonePreferenceManager instance ;
	private Context c ;
	private SharedPreferences p ;
	
	public LinphonePreferenceManager(Context context){
		
		c = context.getApplicationContext() ;
		p = PreferenceManager.getDefaultSharedPreferences(c) ;
	}
	
	public String getString(int key){
		
		return c.getString(key) ;
	}
	
	public boolean useSoftvolume(){
		
		return p.getBoolean(getString(R.string.pref_audio_soft_volume_key),false) ;
	}
	
	public boolean useAudioRoutingAPIHack(){
		
		return p.getBoolean(
				getString(R.string.pref_audio_hacks_use_routing_api_key), false) ;
	}
	
	public boolean useGalaxySHack() {
		return p.getBoolean(
				getString(R.string.pref_audio_hacks_use_galaxys_hack_key), false);
	}

	public int useSpecificAudioModeHack() {
		return Integer.parseInt(p.getString(getString(R.string.pref_audio_use_specific_mode_key), "0"));
	}

	public static final synchronized LinphonePreferenceManager getInstance(Context c) {
		if (instance == null) {
			instance = new LinphonePreferenceManager(c.getApplicationContext());
		}
		return instance;
	}
}







