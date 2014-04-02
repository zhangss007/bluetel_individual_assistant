package com.bluetel.android.app.individual_assistant;

import com.bluetel.android.app.individual_assistant.linphone.LinphoneUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SetActivity extends Activity implements OnClickListener{


	private TextView title ;
	private TextView userNumber , userUri ;
	private TextView softVersion ;
	private Button exit ;
	
	private SharedPreferences mPref ;
	
	private RelativeLayout checkNewer, serverSet, codecSet, aboutBlutel ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) ;
		setContentView(R.layout.set_layout) ;
		mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		//serverIp = mPref.getString(getString(R.string.pref_domain_key), "");
		findView() ;
		
	}
	
	private void findView(){
		
		title = (TextView)findViewById(R.id.title) ;
		exit  = (Button)findViewById(R.id.exit_soft) ;
		softVersion = (TextView)findViewById(R.id.vision) ;
		
		userNumber = (TextView)findViewById(R.id.user_number) ;
		userUri = (TextView)findViewById(R.id.user_uri) ;
		
		checkNewer = (RelativeLayout)findViewById(R.id.check_newer) ;
		serverSet = (RelativeLayout)findViewById(R.id.server_set) ;
		codecSet = (RelativeLayout)findViewById(R.id.codec_set) ;
		aboutBlutel = (RelativeLayout)findViewById(R.id.about_bluetel) ;
		
		title.setText(getResources().getString(R.string.menu_title_set)) ;
		
		
		checkNewer.setOnClickListener(this) ;
		serverSet.setOnClickListener(this) ;
		codecSet.setOnClickListener(this) ;
		aboutBlutel.setOnClickListener(this) ;
		
		initLoginInfo() ;
		
		exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (getResources().getBoolean(R.bool.enable_log_collect)) {
					
					LinphoneUtils.collectLogs(getString(R.string.app_name), getString(R.string.about_bugreport_email));
				} else {
					
					MainActivity.getInstance().exit();
				}
			}
		}) ;
	}

	
	private void initLoginInfo(){
		
		String number , domain ;
		number = mPref.getString(getResources().getString(R.string.pref_username_key), "") ;
		domain = mPref.getString(getResources().getString(R.string.pref_domain_key), "") ;
		userNumber.setText(number) ;
		userUri.setText(number + "@" + domain) ;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (MainActivity.isInStance()) {

			int id =v.getId() ;
			switch (id) {
			case R.id.check_newer:{
				
					if (softVersion.getText().equals(getSoftVersion())){
						
						Toast.makeText(SetActivity.this, "当前软件为最新版本。。。", Toast.LENGTH_SHORT).show() ;
					}else{
						
						softVersion.setText(getSoftVersion()) ;
					}
				}	
				break;
			case R.id.server_set:{
				
					startTheActivity(LoginActivity.class) ;
				}
				break ;
			case R.id.codec_set :{
				
					startTheActivity(CodecSetActivity.class) ; 
				}
				break ;
			case R.id.about_bluetel:{
					
					startTheActivity(AboutActivity.class) ; 
				}	
				break ;
			default:
				break;
			}

		}
	}

	protected void startTheActivity(Class<?> clazz){
	
		Intent intent = new Intent(SetActivity.this, clazz) ;
		startActivity(intent) ;
	}
	
	private String getSoftVersion(){
		
		String  bRet = "1.0" ;
		
		return bRet ;
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	
	
}
