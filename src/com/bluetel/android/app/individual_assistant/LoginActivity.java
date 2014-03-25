package com.bluetel.android.app.individual_assistant;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 登录界面
 * @author zss
 *
 * 2014-3-25
 * bluetel-android-app-individual-assistant
 */
public class LoginActivity extends Activity implements OnClickListener{

	private TextView title ;
	private EditText loginUser , loginPasswd , loginServerIp ;
	private CheckBox rememberPasswd ;
	private Button login ;
	
	private SharedPreferences sp = null ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
//		//设置Activity全屏显示
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN) ;
		//设置Activity竖屏显示
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) ;
		setContentView(R.layout.login_layout) ;
				
		//设置隐藏键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) ;
		//获得实例对象
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		
		findViews() ;


	}

	
	private void findViews(){
		
		title = (TextView)findViewById(R.id.title) ;
		loginUser = (EditText)findViewById(R.id.login_user_edit) ;
		loginPasswd = (EditText)findViewById(R.id.login_passwd_edit) ;
		loginServerIp = (EditText)findViewById(R.id.login_server_ip_edit) ;
		rememberPasswd = (CheckBox)findViewById(R.id.remember_passwd) ;
		login =(Button)findViewById(R.id.login_login_btn) ;
		login.setOnClickListener(this) ;
		title.setText(getResources().getString(R.string.menu_title_login)) ;
		
		if(sp.getBoolean("ISCHECK", false))
        {
    	  //设置默认是记录密码状态
          rememberPasswd.setChecked(true);
       	  loginUser.setText(sp.getString("USER_NAME", ""));
       	  loginPasswd.setText(sp.getString("PASSWORD", ""));
       	  loginServerIp.setText(sp.getString("SERVER_IP", "")) ;
        }
		
		
		rememberPasswd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (rememberPasswd.isChecked()){
					
					sp.edit().putBoolean("ISCHECK", true).commit();
				}else {
					
					sp.edit().putBoolean("ISCHECK", false).commit();
				}
			}
		}) ;
	}


	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (!loginUser.getText().toString().isEmpty()&&
			 !loginPasswd.getText().toString().isEmpty()&&
			 !loginServerIp.getText().toString().isEmpty()){
			
			//记住密码检测
			if (rememberPasswd.isChecked()){
				
				Log.i("TAG", "记住密码了。。。。。") ;
				  Editor editor = sp.edit();
				  editor.putString("USER_NAME",loginUser.getText().toString());
				  editor.putString("PASSWORD",loginPasswd.getText().toString());
				  editor.putString("SERVER_IP",loginServerIp.getText().toString()) ;
				  editor.commit();
			}
			
			
		}else {
		
			
			Toast.makeText(LoginActivity.this,getResources().getString(R.string.login_info_is_emplty), Toast.LENGTH_SHORT).show();
		}
		
	}	
	
	
}
