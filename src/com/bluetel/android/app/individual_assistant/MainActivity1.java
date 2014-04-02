package com.bluetel.android.app.individual_assistant;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity1 extends TabActivity implements OnClickListener{

	
	private TabHost tabHost = null  ;
	private TabSpec tabSpec = null ;
	
	private RelativeLayout contact , capture , record , set ;
	//顶层标题栏
	private LinearLayout bottom ;
	
	private final String CONTACTER = "CONTACTER" ;
	private final String CAPTURE = "CAPTURE" ;
	private final String RECORD= "RECORD" ;
	private final String SET = "SET" ;
	
	
	private static MainActivity1 instance ;
	
	public static boolean isInstance(){
		
		return instance != null;
	}
	
	public static synchronized MainActivity1 getInstance(){			
		
		if(instance != null) 
			return instance ;
		throw new RuntimeException("MainActivity is not instantiated yet.") ;
	}
	
	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.acticity_main1) ;
		initTabHost() ;
		findViews() ;
		instance = this ;
	}

	
	
	
	
	private void initTabHost(){
		
		tabHost = super.getTabHost() ;
		tabSpec = tabHost.newTabSpec(CONTACTER).setIndicator(CONTACTER).setContent(new Intent(MainActivity1.this,ContacterActivity.class)) ;
		tabHost.addTab(tabSpec) ;
		tabSpec = tabHost.newTabSpec(CAPTURE).setIndicator(CAPTURE).setContent(new Intent(MainActivity1.this, VideoActivity.class)) ;
		tabHost.addTab(tabSpec) ;
		tabSpec = tabHost.newTabSpec(RECORD).setIndicator(RECORD).setContent(new Intent(MainActivity1.this, RecordListActivity.class)) ;
		tabHost.addTab(tabSpec) ;
		tabSpec = tabHost.newTabSpec(SET).setIndicator(SET).setContent(new Intent(MainActivity1.this, SetActivity.class)) ;
		tabHost.addTab(tabSpec) ;
		tabHost.setCurrentTab(0) ;
	}
	
	
	
	
	private void findViews(){

		contact = (RelativeLayout)findViewById(R.id.contacts) ;
		capture = (RelativeLayout)findViewById(R.id.capture) ;
		record = (RelativeLayout)findViewById(R.id.record) ;
		set    = (RelativeLayout)findViewById(R.id.set) ;
		contact.setOnClickListener(this) ;
		capture.setOnClickListener(this) ;
		record.setOnClickListener(this) ;
		set.setOnClickListener(this) ;
		contact.setSelected(true) ;
		
		bottom = (LinearLayout)findViewById(R.id.bottom) ;
		
	}

	/**
	 * 隐藏底部标题栏
	 */
	public void hideBottom(){
		
		if (bottom.isShown()){
			
			bottom.setVisibility(View.GONE) ;
			contact.setVisibility(View.GONE) ;
			capture.setVisibility(View.GONE) ;
			record.setVisibility(View.GONE) ;
			set.setVisibility(View.GONE) ;
		}
	}
	
	/**
	 * 显示底部标题栏
	 */
	public void showBottom(){
		
		if(!bottom.isShown()){
			
			bottom.setVisibility(View.VISIBLE) ;
			contact.setVisibility(View.VISIBLE) ;
			capture.setVisibility(View.VISIBLE) ;
			record.setVisibility(View.VISIBLE) ;
			set.setVisibility(View.VISIBLE) ;
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	@Override
	public void onClick(View v) {
	
		
		resetMenuButtonSelector() ;
		switch (v.getId()) {
		
		case R.id.contacts:
			contact.setSelected(true) ;
			tabHost.setCurrentTabByTag(CONTACTER) ;	
			break;
		case R.id.capture:
			capture.setSelected(true) ;
			tabHost.setCurrentTabByTag(CAPTURE) ;
			break ;
		case R.id.record:
			record.setSelected(true) ;
			tabHost.setCurrentTabByTag(RECORD);
			break ;
		case R.id.set:
			set.setSelected(true) ;
			tabHost.setCurrentTabByTag(SET) ;
			break ;
		default:
			break;
		}
	}

	
	private void resetMenuButtonSelector(){
		
		contact.setSelected(false) ;
		capture.setSelected(false) ;
		record.setSelected(false) ;
		set.setSelected(false) ;
	}
	
	
	
	
}
