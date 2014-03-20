package com.bluetel.android.app.individual_assistant;

import com.bluetel.android.app.individual_assistant.service.MainService;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

/**
 * 启动界面
 * @author zss
 *
 * 2014-3-20
 * bluetel-android-app-individual-assistant
 */
public class AppStart extends Activity{

	private ImageView splashIv ;
	private static final int ANIMATION = 3000 ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
		//设置Activity全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN) ;
		//设置Activity竖屏显示
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) ;
		setContentView(R.layout.app_start_layout) ;
		splashIv = (ImageView)findViewById(R.id.spalsh_iv) ;
		
		Intent service = new Intent(AppStart.this, MainService.class) ;
		startService(service) ;
		
		//设置一个3秒的渐变
		AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f) ;
		animation.setDuration(ANIMATION) ;
		splashIv.startAnimation(animation) ;
		
		
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AppStart.this, MainActivity.class) ;
				startActivity(intent) ;
				//logIn("629","629","192.168.0.140",true) ;
				AppStart.this.finish() ;
			}
		}) ;
	}
	
	


	
}
