package com.bluetel.android.app.individual_assistant;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class CodecSetActivity extends PreferenceActivity{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences) ;
		
	}

	
}
