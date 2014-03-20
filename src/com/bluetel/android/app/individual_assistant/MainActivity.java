package com.bluetel.android.app.individual_assistant;

import org.linphone.mediastream.Log;

import com.bluetel.android.app.individual_assistant.linphone.LinphoneManager;

import android.R.string;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MainActivity extends ActivityGroup implements OnClickListener{

	private RelativeLayout contact , capture , record , set ;
	private FrameLayout container ;
	
	private static MainActivity instance ;
	
	private SharedPreferences mPref;
	private boolean accountCreated = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
		//设置Activity全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN) ;
		//设置Activity横屏显示
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) ;
		setContentView(R.layout.activity_main);
		findViews() ;
		mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		instance = this ;
		logIn("629", "629", "192.168.0.140", true) ;
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
		switch (checkId) {
		case R.id.contacts:
			contact.setSelected(true) ;
			startViewByActivity(ContacterActivity.class,checkId) ;
			break;
		case R.id.capture:
			capture.setSelected(true) ;
			startViewByActivity(CaptureActivity.class,checkId) ;
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
		resetMenuButtonSelector() ;
		contact.setSelected(true) ;
		startViewByActivity(ContacterActivity.class,R.id.contacts) ;
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
	
	
}
