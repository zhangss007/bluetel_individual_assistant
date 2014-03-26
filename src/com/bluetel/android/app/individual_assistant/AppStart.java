package com.bluetel.android.app.individual_assistant;

import static android.content.Intent.ACTION_MAIN;


import com.bluetel.android.app.individual_assistant.service.MainService;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
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
	private Handler mHandler;
	private ServiceWaitThread mThread;
	
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
		mHandler = new Handler();
		splashIv = (ImageView)findViewById(R.id.spalsh_iv) ;
		
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
//				Intent intent = new Intent(AppStart.this,LoginActivity.class) ;
//				startActivity(intent) ;
//				AppStart.this.finish() ;
				
				
				if (MainService.isReady()) {
					onServiceReady();
				} else {
					// start linphone as background  
					startService(new Intent(ACTION_MAIN).setClass(AppStart.this, MainService.class));
					mThread = new ServiceWaitThread();
					mThread.start();
				}
			}
		}) ;
	}
	
	
	private class ServiceWaitThread extends Thread{

		@Override
		public void run() {
			super.run();
			
			while(!MainService.isReady()){
				
				try {
					sleep(30) ;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					onServiceReady();
				}
			}) ;
			mThread = null ;
		}
	}
	
	protected void onServiceReady() {
		final Class<? extends Activity> classToStart ;
		if (getResources().getBoolean(R.bool.show_tutorials_instead_of_app)) {
			classToStart = LoginActivity.class;
		} else {
			classToStart = MainActivity.class;
		}
		
		MainService.instance().setActivityToLaunchOnIncomingReceived(classToStart);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent().setClass(AppStart.this, classToStart).setData(getIntent().getData()));
				finish();
			}
		}, 1000);
	}

	
}
