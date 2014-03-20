package com.bluetel.android.app.individual_assistant.service;

import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCall.State;
import org.linphone.core.LinphoneCore.GlobalState;
import org.linphone.core.LinphoneCore.RegistrationState;

import com.bluetel.android.app.individual_assistant.MainActivity;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneManager;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneSimpleListener.LinphoneServiceListener;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;

public class MainService extends Service implements LinphoneServiceListener{

	
	public Handler mHandler = new Handler();
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		LinphoneManager.createAndStart(this, this) ;
		
		
		
	}

	@Override
	public void onGlobalStateChanged(GlobalState arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCallStateChanged(LinphoneCall arg0, State arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCallEncryptionChanged(LinphoneCall arg0, boolean arg1,
			String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisplayStatus(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegistrationStateChanged(RegistrationState arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRingerPlayerCreated(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tryingNewOutgoingCallButAlreadyInCall() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tryingNewOutgoingCallButCannotGetCallParameters() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tryingNewOutgoingCallButWrongDestinationAddress() {
		// TODO Auto-generated method stub
		
	}

	
}
