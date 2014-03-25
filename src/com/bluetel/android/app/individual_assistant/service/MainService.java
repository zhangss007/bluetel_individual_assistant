package com.bluetel.android.app.individual_assistant.service;

import java.io.UnsupportedEncodingException;

import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.core.OnlineStatus;
import org.linphone.core.LinphoneCall.State;
import org.linphone.core.LinphoneCore.GlobalState;
import org.linphone.core.LinphoneCore.RegistrationState;
import org.linphone.mediastream.Log;

import com.bluetel.android.app.individual_assistant.ChatActivity;
import com.bluetel.android.app.individual_assistant.R;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneManager;
import com.bluetel.android.app.individual_assistant.linphone.LinphonePreferenceManager;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneSimpleListener.LinphoneServiceListener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class MainService extends Service implements LinphoneServiceListener{

	
	public Handler mHandler = new Handler();
	
	private ChatActivity chatActivity ;
	
	
	private static MainService instance ;
	
//	private boolean mTestDelayElapsed; // add a timer for testing
	private boolean mTestDelayElapsed = true; // no timer
	private WifiManager mWifiManager ;
	private WifiLock mWifiLock ;
	
	
	
	private static final int NOTIF_ID = 1 ;
	private static final int INCALL_NOTIF_ID = 2 ;
	private static final int MESSAGE_NOTIF_ID = 3 ;
	private static final int CUSTOM_NOTIF_ID = 4 ;
	
	//通知
	private Notification mNotif ;
	//来电通知
	private Notification mIncallNotif ;
	//信息通知
	private Notification mMessageNotif ;
	//应用通知
	private Notification mCustomNotif ;
	
	//信息通知个数
	private int mMsgNotifCount ;
	private PendingIntent mNotifContentIntent;
	private String mNotificationTitle ;
	private NotificationManager mNM;
	
	
	
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
	
	
	
	public ChatActivity getChatActivity() {
		return chatActivity;
	}
	public void  setCurrentChatActivityToNull(){
		
		if (chatActivity != null){
			
			chatActivity = null ;
		}
	}
	
	
	public void onMessageReceived(final LinphoneAddress from,
			final LinphoneChatMessage message, final int id){
		
		final String url =from.getUserName() ;
		
		
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(MainService.this, "收到的信息来自----->"+from.asStringUriOnly(), Toast.LENGTH_SHORT).show() ;
				Log.i("TAG","收到了信息了啊  。。。。。。MainService------" + url) ;
				if (chatActivity != null){
						
					if (url!=null&&url.contains(chatActivity.getExtenNumber()))
						try {
							chatActivity.onMessageReceived(id, from, message) ;
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}
				}
			}
		}) ;
		
		
		
		
	}
	

	public void setChatActivity(ChatActivity chatActivity) {
		this.chatActivity = chatActivity;
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
		
		mNotificationTitle = getString(R.string.app_name) ;
		
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE) ;
		
		
		
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
