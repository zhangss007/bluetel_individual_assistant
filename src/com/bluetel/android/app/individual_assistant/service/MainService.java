package com.bluetel.android.app.individual_assistant.service;

import org.linphone.core.LinphoneCall;
import org.linphone.core.OnlineStatus;
import org.linphone.core.LinphoneCall.State;
import org.linphone.core.LinphoneCore.GlobalState;
import org.linphone.core.LinphoneCore.RegistrationState;

import com.bluetel.android.app.individual_assistant.R;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneManager;
import com.bluetel.android.app.individual_assistant.linphone.LinphonePreferenceManager;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneSimpleListener.LinphoneServiceListener;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class MainService extends Service implements LinphoneServiceListener{

	
	public Handler mHandler = new Handler();
	
	private static MainService instance ;
	
//	private boolean mTestDelayElapsed; // add a timer for testing
	private boolean mTestDelayElapsed = true; // no timer
	
	public static boolean isReady() {
		return instance!=null && instance.mTestDelayElapsed;
	}
	
	
	/**
	 * @throws RuntimeException service not instantiated
	 */
	public static MainService instance()  {
		if (isReady()) return instance;

		throw new RuntimeException("LinphoneService not instantiated yet");
	}
	
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		
		// In case restart after a crash. Main in LinphoneActivity
		LinphonePreferenceManager.getInstance(this);

		// Set default preferences
		PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
		
		LinphoneManager.createAndStart(this, this) ;
		
		instance = this; // instance is ready once linphone manager has been created
		
		if (!mTestDelayElapsed) {
			// Only used when testing. Simulates a 5 seconds delay for launching service
			mHandler.postDelayed(new Runnable() {
				@Override public void run() {
					mTestDelayElapsed = true;
				}
			}, 5000);
		}
		
		LinphoneManager.getLc().setPresenceInfo(0, "", OnlineStatus.Online);
		
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
