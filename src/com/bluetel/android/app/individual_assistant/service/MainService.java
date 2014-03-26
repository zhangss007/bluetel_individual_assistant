package com.bluetel.android.app.individual_assistant.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.core.OnlineStatus;
import org.linphone.core.LinphoneCall.State;
import org.linphone.core.LinphoneCore.GlobalState;
import org.linphone.core.LinphoneCore.RegistrationState;
import org.linphone.mediastream.Log;
import org.linphone.mediastream.Version;

import com.bluetel.android.app.individual_assistant.ChatActivity;
import com.bluetel.android.app.individual_assistant.MainActivity;
import com.bluetel.android.app.individual_assistant.R;
import com.bluetel.android.app.individual_assistant.compatibility.Compatibility;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneManager;
import com.bluetel.android.app.individual_assistant.linphone.LinphonePreferenceManager;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneSimpleListener.LinphoneServiceListener;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
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
	
	
	private static final int IC_LEVEL_ORANGE=0;
	
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
	
	
	private Method mSetForeground;
	private Method mStartForeground;
	private Method mStopForeground;
	private Object[] mSetForegroundArgs = new Object[1];
	private Object[] mStartForegroundArgs = new Object[2];
	private Object[] mStopForegroundArgs = new Object[1];
	private Class<? extends Activity> incomingReceivedActivity = MainActivity.class;
	
	
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
	
	private static final Class<?>[] mSetFgSign = new Class[] {boolean.class};
	private static final Class<?>[] mStartFgSign = new Class[] {
		int.class, Notification.class};
	private static final Class<?>[] mStopFgSign = new Class[] {boolean.class};
	

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
		mNM.cancel(INCALL_NOTIF_ID); // in case of crash the icon is not removed
		
		mNotif = new Notification();
		mNotif.icon = R.drawable.ic_launcher;
		mNotif.when = System.currentTimeMillis();
		mNotif.iconLevel=IC_LEVEL_ORANGE;
		mNotif.flags |= Notification.FLAG_ONGOING_EVENT;
		
		
		Intent notifIntent = new Intent(this, incomingReceivedActivity);
		notifIntent.putExtra("Notification", true);
		mNotifContentIntent = PendingIntent.getActivity(this, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mWifiLock = mWifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, this.getPackageName()+"-wifi-call-lock");
		mWifiLock.setReferenceCounted(false);
		
		Compatibility.setNotificationLatestEventInfo(mNotif, this, mNotificationTitle, "", mNotifContentIntent);
		
		// In case restart after a crash. Main in LinphoneActivity
		LinphonePreferenceManager.getInstance(this);

		// Set default preferences
		PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
		
		LinphoneManager.createAndStart(this, this) ;
		
		instance = this; // instance is ready once linphone manager has been created
		
		// Retrieve methods to publish notification and keep Android
		// from killing us and keep the audio quality high.
				if (Version.sdkStrictlyBelow(Version.API05_ECLAIR_20)) {
					try {
						mSetForeground = getClass().getMethod("setForeground", mSetFgSign);
					} catch (NoSuchMethodException e) {
						Log.e(e, "Couldn't find foreground method");
					}
				} else {
					try {
						mStartForeground = getClass().getMethod("startForeground", mStartFgSign);
						mStopForeground = getClass().getMethod("stopForeground", mStopFgSign);
					} catch (NoSuchMethodException e) {
						Log.e(e, "Couldn't find startGoreground or stopForeground");
					}
				}

		startForegroundCompat(NOTIF_ID, mNotif);

		
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

	
	public void setActivityToLaunchOnIncomingReceived(Class<? extends Activity> activity) {
		incomingReceivedActivity = activity;
		resetIntentLaunchedOnNotificationClick();
	}
	
	private void resetIntentLaunchedOnNotificationClick() {
		Intent notifIntent = new Intent(this, incomingReceivedActivity);
		mNotifContentIntent = PendingIntent.getActivity(this, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		Compatibility.setNotificationLatestEventInfo(mNotif, this, mNotificationTitle, "", mNotifContentIntent);
	}
	
	/**
	 * This is a wrapper around the new startForeground method, using the older
	 * APIs if it is not available.
	 */
	void startForegroundCompat(int id, Notification notification) {
		// If we have the new startForeground API, then use it.
		if (mStartForeground != null) {
			mStartForegroundArgs[0] = Integer.valueOf(id);
			mStartForegroundArgs[1] = notification;
			invokeMethod(mStartForeground, mStartForegroundArgs);
			return;
		}

		// Fall back on the old API.
		if (mSetForeground != null) {
			mSetForegroundArgs[0] = Boolean.TRUE;
			invokeMethod(mSetForeground, mSetForegroundArgs);
			// continue
		}

		notifyWrapper(id, notification);
	}
	
	/**
	 * Wrap notifier to avoid setting the linphone icons while the service
	 * is stopping. When the (rare) bug is triggered, the linphone icon is
	 * present despite the service is not running. To trigger it one could
	 * stop linphone as soon as it is started. Transport configured with TLS.
	 */
	private synchronized void notifyWrapper(int id, Notification notification) {
		if (instance != null) {
			mNM.notify(id, notification);
		} else {
			Log.i("Service not ready, discarding notification");
		}
	}
	
	void invokeMethod(Method method, Object[] args) {
		try {
			method.invoke(this, args);
		} catch (InvocationTargetException e) {
			// Should not happen.
			Log.w(e, "Unable to invoke method");
		} catch (IllegalAccessException e) {
			// Should not happen.
			Log.w(e, "Unable to invoke method");
		}
	}

}
