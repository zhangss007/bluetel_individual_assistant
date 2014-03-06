package com.bluetel.android.app.individual_assistant;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class RecordActivity extends Activity implements OnClickListener{

	private ImageView recordImage ;
	private Button recordBegin , uploadRecord ;
	private  int[] startRecordImages = {
			R.drawable.record_img_rec1,
			R.drawable.record_img_rec2,
			R.drawable.record_img_rec3,
			R.drawable.record_img_rec4,
			R.drawable.record_img_rec5,
			R.drawable.record_img_rec6,
		} ;
	private int startRecordingImage = 0 ;
	private static final int RECORD_IMAGE = 6 ;
	private static final String RECORD_BEGIN = "RECORD_BEGIN" ;
	
	private Timer recordImTimer = new Timer()  ;
	private Handler handler = null ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_layout) ;
		findViews() ;
		
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 0x952700){

					Log.i("TAG", "kkkk" + startRecordingImage) ;
					recordImage.setBackgroundResource(startRecordImages[startRecordingImage++]) ;
					if (startRecordingImage ==  RECORD_IMAGE)
						startRecordingImage = 0 ;
					
					

				}
			}
			
			
		} ;
	}

	private void findViews(){
	
		recordImage = (ImageView)findViewById(R.id.record_image) ;
		recordBegin = (Button)findViewById(R.id.record_begin_btn) ;
		uploadRecord = (Button)findViewById(R.id.upload_record_btn) ;
		recordBegin.setOnClickListener(this) ;
		uploadRecord.setOnClickListener(this) ;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.record_begin_btn:{
			
			//recordImage.setBackgroundResource(R.drawable.record_img_rec1) ;
			recordBegin.setSelected(true) ;
				recordImTimer.schedule(new TimerTask(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.i("TAG", "kkkkkkkkk") ;
						Message message = handler.obtainMessage() ;
						message.what = 0x952700 ;
						message.obj = RECORD_BEGIN ;
						message.sendToTarget() ;
					}
					
					
				},500,500) ;
			} 
			
			break;
		case R.id.upload_record_btn:
			break ;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		recordImTimer.cancel() ;
	}
	
	
	
}
