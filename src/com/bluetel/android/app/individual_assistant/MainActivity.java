package com.bluetel.android.app.individual_assistant;

import static android.content.Intent.ACTION_MAIN;

import java.util.List;

import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCallParams;
import org.linphone.core.LinphoneCore;
import org.linphone.core.OnlineStatus;
import org.linphone.core.LinphoneCall.State;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.core.LinphoneCore.RegistrationState;
import org.linphone.core.LinphoneProxyConfig;
import org.linphone.mediastream.Log;

import com.bluetel.android.app.individual_assistant.bean.UploadFile;
import com.bluetel.android.app.individual_assistant.linphone.ChatMessage;
import com.bluetel.android.app.individual_assistant.linphone.ChatStorage;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneManager;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneSimpleListener.LinphoneOnCallStateChangedListener;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneSimpleListener.LinphoneOnMessageReceivedListener;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneSimpleListener.LinphoneOnRegistrationStateChangedListener;
import com.bluetel.android.app.individual_assistant.service.MainService;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.view.Menu;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends ActivityGroup implements OnClickListener,								
									LinphoneOnCallStateChangedListener,
									LinphoneOnMessageReceivedListener,
									LinphoneOnRegistrationStateChangedListener{

	private RelativeLayout contact , capture , record , set ;
	private FrameLayout container ;
	
	private LinearLayout  menuBottom ;
	
	
	private static MainActivity instance ;
	
	private SharedPreferences mPref;
	private boolean accountCreated = false;
	
	public static final String PREF_FIRST_LAUNCH = "pref_first_launch";
	private static final int SETTINGS_ACTIVITY = 123;
	private static final int FIRST_LOGIN_ACTIVITY = 101;
	
	//分机注册成功
	private static final int REGISTER_OK = 1 ;
	//分机注册失败
	private static final int REGISTER_FAILURE = 2 ;
	//通话中
	private static final int CALL_OK = 3  ;
	//通话结束
	private static final int CALL_END = 4 ;
	//来电。。。
	private static final int INCOMING_RECEIVED = 5 ;
	
	private Handler handler;
	
	private int currentMenuSelect = 0 ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
		//设置Activity横屏显示
	//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) ;
		setContentView(R.layout.activity_main);
		
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 0x5000){
					
					int type = (Integer) msg.obj ;
					switch (type) {
					case REGISTER_OK:{
						
							Toast.makeText(MainActivity.this, "注册成功。。。", Toast.LENGTH_SHORT).show() ;
							if (ContacterActivity.isInstance()){
								
								ContacterActivity.getInstance().startGetContacters() ;
							}
						}
						break;
					case REGISTER_FAILURE:
						Toast.makeText(MainActivity.this, "注册失败。。。", Toast.LENGTH_SHORT).show() ;
						break ;
					case CALL_OK:{
						startOrientationSensor() ;
						resetMenuButtonSelector() ;
						capture.setSelected(true) ;
						startViewByActivity(VideoActivity.class,0) ;
						Toast.makeText(MainActivity.this, "服务端启动了远程视频监控任务。。。", Toast.LENGTH_SHORT).show() ;
						
						
						}
						break ;
					case CALL_END:{
						
							if (currentMenuSelect == R.id.capture){
								
								if(VideoActivity.isInstance()){
								
									VideoActivity.getInstance().startPreviewCamera() ;
								}
							}

							Toast.makeText(MainActivity.this, "服务器远程监控结束", Toast.LENGTH_SHORT).show();
						}
						break ;
					case INCOMING_RECEIVED:{
						
							if(VideoActivity.isInstance()){
								
								Toast.makeText(MainActivity.this, "关闭当前已经开启的摄像头", Toast.LENGTH_SHORT).show();
								VideoActivity.getInstance().releaseCamera() ;
							}
						}
						break ;
					default:
						break;
					}
				}
				
			}
			
			
		};

		
		if (!LinphoneManager.isInstanciated()) {
			Log.e("No service running: avoid crash by starting the launcher", this.getClass().getName());
			// super.onCreate called earlier
			finish();
			startActivity(getIntent().setClass(this, AppStart.class));
			return;
		}
		
		boolean useFirstLoginActivity = getResources().getBoolean(R.bool.display_account_wizard_at_first_start);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		if (useFirstLoginActivity && !pref.getBoolean(getString(R.string.first_launch_suceeded_once_key), false)) {
			if (pref.getInt(getString(R.string.pref_extra_accounts), -1) > -1) {
				pref.edit().putBoolean(getString(R.string.first_launch_suceeded_once_key), true);
			} else {
				startActivityForResult(new Intent().setClass(this, LoginActivity.class), FIRST_LOGIN_ACTIVITY);
			}
		}
		
		findViews() ;
		mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		resetMenuButtonSelector() ;
		contact.setSelected(true) ;
		startViewByActivity(ContacterActivity.class,R.id.contacts) ;
		instance = this ;
		Log.i("TAG","。。。。。。。创建了一次MainActivity了。。。。。") ;
	}
	
	static final boolean isInStance(){
		
		return instance!=null ;
	}
	
	public static synchronized MainActivity getInstance(){			
	
		if(instance != null) 
			return instance ;
		throw new RuntimeException("MainActivity is not instantiated yet.") ;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
	//设置横屏显示
	public void setActivityLandScape(){
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) ;
	}
	
	//设置竖屏显示
	public void setActivityPortRair(){
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) ;
	}
	
	private void findViews(){
		
		container = (FrameLayout)findViewById(R.id.container) ;
		contact = (RelativeLayout)findViewById(R.id.contacts) ;
		capture = (RelativeLayout)findViewById(R.id.capture) ;
		record = (RelativeLayout)findViewById(R.id.record) ;
		set    = (RelativeLayout)findViewById(R.id.set) ;
		contact.setOnClickListener(this) ;
		capture.setOnClickListener(this) ;
		record.setOnClickListener(this) ;
		set.setOnClickListener(this) ;
		contact.setSelected(true) ;
		
		menuBottom = (LinearLayout)findViewById(R.id.memu_bottom) ;
		
	}
	
	public void hideMenuBottom (){
		
		if (menuBottom.isShown()){
			
			menuBottom.setVisibility(View.GONE) ;
		}
	}

	
	
	private void resetMenuButtonSelector(){
		
		contact.setSelected(false) ;
		capture.setSelected(false) ;
		record.setSelected(false) ;
		set.setSelected(false) ;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		resetMenuButtonSelector() ;
		int checkId = v.getId() ;
		currentMenuSelect = checkId ;
		switch (checkId) {
		case R.id.contacts:
			contact.setSelected(true) ;
			startViewByActivity(ContacterActivity.class,checkId) ;
			break;
		case R.id.capture:
			capture.setSelected(true) ;
			startViewByActivity(VideoActivity.class,checkId) ;
			break ;
		case R.id.record:
			record.setSelected(true) ;
			startViewByActivity(RecordActivity.class,checkId) ;
			break ;
		case R.id.set:
			set.setSelected(true);
			startViewByActivity(SetActivity.class,checkId) ;
			break ;
		default:
			break;
		}
	}

	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!MainService.isReady())  {
			startService(new Intent(ACTION_MAIN).setClass(this, MainService.class));
		}
		// Remove to avoid duplication of the listeners
		LinphoneManager.removeListener(this);
		LinphoneManager.addListener(this);
	}

	
	
	protected void startViewByActivity(Class<?> clazz, int checkid) {
		Intent intent = new Intent(this, clazz);
		startViewByActivity(clazz, intent);
	}

	public void startViewByActivity(Class<?> clazz, Intent intent) {
		@SuppressWarnings("deprecation")
		View v = getLocalActivityManager().startActivity(clazz.getName(), intent).getDecorView();
		container.removeAllViews();
		container.addView(v);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		instance = null ;
	}
	
	public void logIn(String username, String password, String domain, boolean sendEcCalibrationResult) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null && getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}

        saveCreatedAccount(username, password, domain);
		LinphoneManager.getInstance().initializePayloads();

		try {
			LinphoneManager.getInstance().initFromConf();
		} catch (Throwable e) {
			Log.e(e, "Error while initializing from config in first login activity");
			//Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
		}

		if (LinphoneManager.getLc().getDefaultProxyConfig() != null) {
			//launchEchoCancellerCalibration(sendEcCalibrationResult);
		}
	}
	
	public SharedPreferences getSharePreferences(){
		
		return mPref ;
	}
	
	
	public void saveCreatedAccount(String username, String password, String domain) {
		if (accountCreated)
			return;
		
		int newAccountId = mPref.getInt(getString(R.string.pref_extra_accounts), 0);
		if (newAccountId == -1)
			newAccountId = 0;
		writePreference(R.string.pref_extra_accounts, newAccountId+1);
		
		if (newAccountId == 0) {
			writePreference(R.string.pref_username_key, username);
			writePreference(R.string.pref_passwd_key, password);
			writePreference(R.string.pref_domain_key, domain);
			
			boolean isMainAccountLinphoneDotOrg = domain.equals(getString(R.string.default_domain));
			if (isMainAccountLinphoneDotOrg) {
				if (getResources().getBoolean(R.bool.disable_all_security_features_for_markets)) {
					writePreference(R.string.pref_proxy_key, domain + ":5228");
					writePreference(R.string.pref_transport_key, getString(R.string.pref_transport_tcp_key));
				}
				else {
					writePreference(R.string.pref_proxy_key, domain + ":5223");
					writePreference(R.string.pref_transport_key, getString(R.string.pref_transport_tls_key));
				}
				
				writePreference(R.string.pref_expire_key, "604800"); // 3600*24*7
				writePreference(R.string.pref_enable_outbound_proxy_key, true);
				writePreference(R.string.pref_stun_server_key, getString(R.string.default_stun));
				writePreference(R.string.pref_ice_enable_key, true);
				writePreference(R.string.pref_push_notification_key, true);
			}
		} else {
			writePreference(getString(R.string.pref_username_key) + newAccountId, username);
			writePreference(getString(R.string.pref_passwd_key) + newAccountId, password);
			writePreference(getString(R.string.pref_domain_key) + newAccountId, domain);
//			writePreference(R.string.pref_username_key, username);
//			writePreference(R.string.pref_passwd_key, password);
//			writePreference(R.string.pref_domain_key, domain);
		}
		String forcedProxy=getResources().getString(R.string.setup_forced_proxy);
		if (!TextUtils.isEmpty(forcedProxy)) {
			writePreference(R.string.pref_enable_outbound_proxy_key, true);
			writePreference(R.string.pref_proxy_key, forcedProxy);
		}
		accountCreated = true;
	}
	
	
	
	private void writePreference(int key, String value) {
		mPref.edit().putString(getString(key), value).commit();
	}
	
	private void writePreference(String key, String value) {
		mPref.edit().putString(key, value).commit();
	}
	
	private void writePreference(int key, int value) {
		mPref.edit().putInt(getString(key), value).commit();
	}
	
	private void writePreference(int key, boolean value) {
		mPref.edit().putBoolean(getString(key), value).commit();
	}
	
	
	public void startChatRoomActivity(String extenName, String extenNumber, String departName){
		
		String numberOrAdress = extenNumber ;
		String sipUri = numberOrAdress ;
		LinphoneProxyConfig lpc = LinphoneManager.getLc().getDefaultProxyConfig();
		if (lpc != null){
			
			if (!numberOrAdress.startsWith("sip:")){
				
				numberOrAdress = "sip:" + numberOrAdress ;
			}
			
			if (!sipUri.contains("@"))
				sipUri = numberOrAdress + "@" + lpc.getDomain();
		}
		
		
		Intent intent = new Intent(MainActivity.this, ChatActivity.class) ;
		Bundle bundle = new Bundle() ;
		bundle.putString("ExtenName", extenName) ;
		bundle.putString("ExtenNumber", extenNumber) ;
		bundle.putString("SipUri", sipUri) ;
		bundle.putString("Depart", departName) ;
		intent.putExtras(bundle) ;
		startActivity(intent) ;
	}
	
	public List<ChatMessage> getChatMessages(String correspondent) {
		return getChatStorage().getMessages(correspondent);
	}

	public void removeFromChatList(String sipUri) {
		getChatStorage().removeDiscussion(sipUri);
	}

	public void removeFromDrafts(String sipUri) {
		getChatStorage().deleteDraft(sipUri);
	}
	
	
	public ChatStorage getChatStorage() {
		if (LinphoneManager.getInstance().chatStorage == null) {
			return new ChatStorage(this);
		}
		return LinphoneManager.getInstance().chatStorage;
	}

	public int onMessageSent(String number,String to, String message) {
		getChatStorage().deleteDraft(to);

		return getChatStorage().saveTextMessage(number,"", to, message, System.currentTimeMillis());
	}
	
	public int  onSaveUploadFile(String userNumber, String fileName,int fileType,String time, int status ){

		return  getChatStorage().saveUploadFileInfo(userNumber, fileName, fileType, time, status);
		
	}
	
	public List<UploadFile> getUploadFiles(String user) {
		
		return getChatStorage().getUpLoadFile(user);
	}
	
	@Override
	public void onMessageReceived(final LinphoneAddress from,
			final LinphoneChatMessage message, final int id) {
		// TODO Auto-generated method stub
		Log.i("TAG","来短信了。；   。。。。。") ;
		if (MainService.isReady()){
			
			MainService.instance().onMessageReceived(from, message, id) ;
		}
		
	}

	@Override
	public void onRegistrationStateChanged(RegistrationState state) {
		// TODO Auto-generated method stub
		
		Log.i("TAG","注册状态。。。。。1321231321233") ;
		if (state == RegistrationState.RegistrationFailed){
			
			handMessage(REGISTER_FAILURE) ;
		}else if(state == RegistrationState.RegistrationOk){
			
			handMessage(REGISTER_OK) ;
		} 
	}

	@Override
	public void onCallStateChanged(LinphoneCall call, State state,
			String message) {
		// TODO Auto-generated method stub
		
		
		
		Log.i("TAG","通话状态。。。。。。。。。。。。。。。" + state) ;
		if (state == LinphoneCall.State.Connected){
			
			handMessage(CALL_OK) ;
		}else if (state == LinphoneCall.State.CallEnd){
			//通话结束
			handMessage(CALL_END) ;
		}else if (state == LinphoneCall.State.IncomingReceived){
			
			handMessage(INCOMING_RECEIVED) ;
			//来电自动接听
			Log.i("new state [",state,"]" + "来电话了 。。。。");
			LinphoneCallParams params = LinphoneManager.getLc().createDefaultCallParameters();
			if (call != null)
				LinphoneManager.getInstance().acceptCallWithParams(call, params) ;
		}
		
		
	}
	
	public void exit() {
		//refreshStatus(OnlineStatus.Offline);
		finish();
		stopService(new Intent(ACTION_MAIN).setClass(this, MainService.class));
	}
	
	
	private void handMessage(int type) {
		
		Message msg = handler.obtainMessage() ;
		msg.what = 0x5000 ;
		msg.obj = type ;
		msg.sendToTarget() ;
	}
	
	
	
	

	/**
	 * Register a sensor to track phoneOrientation changes
	 */
	private synchronized void startOrientationSensor() {
		if (mOrientationHelper == null) {
			mOrientationHelper = new LocalOrientationEventListener(this);
		}
		mOrientationHelper.enable();
	}

	private OrientationEventListener mOrientationHelper;
	private int mAlwaysChangingPhoneAngle = -1;
	//private AcceptNewFriendDialog acceptNewFriendDialog;

	private class LocalOrientationEventListener extends OrientationEventListener {
		public LocalOrientationEventListener(Context context) {
			super(context);
		}

		@Override
		public void onOrientationChanged(final int o) {
			if (o == OrientationEventListener.ORIENTATION_UNKNOWN) {
				return;
			}

			Log.i("TAG","设备的方向为----------->" + o ) ;
			int degrees = 270;
//			if (o < 45 || o > 315)
//				degrees = 0;
//			else if (o < 135)
//				degrees = 90;
//			else if (o < 225)
//				degrees = 180;
			
//			 int degrees = 0;
//		     switch (o) {
//		         case Surface.ROTATION_0: degrees = 0; break;
//		         case Surface.ROTATION_90: degrees = 90; break;
//		         case Surface.ROTATION_180: degrees = 180; break;
//		         case Surface.ROTATION_270: degrees = 270; break;
//		     }

			if (mAlwaysChangingPhoneAngle == degrees) {
				return;
			}
			mAlwaysChangingPhoneAngle = degrees;

			Log.d("Phone orientation changed to ", degrees);
			int rotation = (360 - degrees) % 360;
			LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
			if (lc != null) {
				lc.setDeviceRotation(rotation);
				LinphoneCall currentCall = lc.getCurrentCall();
				if (currentCall != null && currentCall.cameraEnabled() && currentCall.getCurrentParamsCopy().getVideoEnabled()) {
					lc.updateCall(currentCall, null);
				}
			}
		}
	}
	//检测屏幕是否反转
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		
		int degrees = 270;
		if (mAlwaysChangingPhoneAngle == degrees) {
			return;
		}
		mAlwaysChangingPhoneAngle = degrees;

		Log.d("Phone orientation changed to ", degrees);
		int rotation = (360 - degrees) % 360;
		LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
		if (lc != null) {
			lc.setDeviceRotation(rotation);
			LinphoneCall currentCall = lc.getCurrentCall();
			if (currentCall != null && currentCall.cameraEnabled() && currentCall.getCurrentParamsCopy().getVideoEnabled()) {
				lc.updateCall(currentCall, null);
			}
		}
		
	}
	
	
	
	
	
}
