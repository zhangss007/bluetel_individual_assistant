package com.bluetel.android.app.individual_assistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity1 extends Activity implements OnClickListener{

	private Button connects , capture , record ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.acticity_main1) ;
		findViews() ;
	}

	private void findViews(){
		
		connects = (Button)findViewById(R.id.connect_btn) ;
		capture = (Button)findViewById(R.id.capture_upload_btn) ;
		record = (Button)findViewById(R.id.record_upload_btn) ;
		
		connects.setOnClickListener(this) ;
		capture.setOnClickListener(this) ;
		record.setOnClickListener(this) ;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.connect_btn:
			startActivity(ContacterActivity.class) ;
			break;
		case R.id.capture_upload_btn:
			startActivity(CaptureActivity.class) ;
			break ;
		case R.id.record_upload_btn:
			startActivity(RecordActivity.class) ;
			break ;
		default:
			break;
		}
	}
	
	private void startActivity(Class<?> clazz){
		
		Intent intent = new Intent(MainActivity1.this,clazz) ;
		startActivity(intent) ;
	}
}
