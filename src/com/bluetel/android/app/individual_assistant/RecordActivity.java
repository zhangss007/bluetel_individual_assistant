package com.bluetel.android.app.individual_assistant;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class RecordActivity extends Activity{

	private ImageView recordImage ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_layout) ;
		findViews() ;
	}

	private void findViews(){
	
		recordImage = (ImageView)findViewById(R.id.record_image) ;
		
	}
	
}
