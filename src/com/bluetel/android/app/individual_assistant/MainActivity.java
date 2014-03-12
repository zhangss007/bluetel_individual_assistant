package com.bluetel.android.app.individual_assistant;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MainActivity extends ActivityGroup implements OnClickListener{

	private RelativeLayout contact , capture , record , set ;
	private FrameLayout container ;
	
	private static MainActivity instance ;
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
		instance = this ;
	}
	
	static final boolean isInStance(){
		
		return instance!=null ;
	}
	
	public static MainActivity getInstance(){			
	
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
	
	
}
