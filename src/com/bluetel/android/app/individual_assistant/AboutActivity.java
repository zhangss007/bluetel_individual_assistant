package com.bluetel.android.app.individual_assistant;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * AboutActivity
 * @author zss
 *
 * 2014-3-27
 * bluetel-android-app-individual-assistant
 */
public class AboutActivity extends Activity{

	private TextView title ;
	private Button backBtn ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
		//设置Activity竖屏显示
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) ;
		
		setContentView(R.layout.about_activity_layout) ;
		findviews() ;
	}
	
	private void findviews(){
		
		title = (TextView)findViewById(R.id.title) ;
		backBtn = (Button)findViewById(R.id.left_btn) ;
		backBtn.setVisibility(View.VISIBLE) ;
		title.setText(getResources().getString(R.string.about)) ;
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish() ;
			}
		}) ;
		
	}

	
}
