package com.bluetel.android.app.individual_assistant;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SetActivity extends PreferenceActivity{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.set_preference) ;
	}

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.set_layout) ;
//	}
//
//	
	
	
	
}
