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
	private  int[] recordStartImages = {
			R.drawable.record_img_rec1,
			R.drawable.record_img_rec2,
			R.drawable.record_img_rec3,
			R.drawable.record_img_rec4,
			R.drawable.record_img_rec5,
			R.drawable.record_img_rec6,
		} ;
	private int[] recordPauseImages = {
			R.drawable.record_img_pause1,
			R.drawable.record_img_pause2,
			R.drawable.record_img_pause3,
			R.drawable.record_img_pause4,
			R.drawable.record_img_pause5,
			R.drawable.record_img_pause6,
		} ;
	private int[] currentRecordBtnSelectors = {
			R.drawable.record_btn_begin,
			R.drawable.record_btn_stop,
			R.drawable.record_btn_play
		} ; 
	
	private int startRecordingImage = 0 ;
	private static final int RECORD_IMAGE = 6 ;
	
	private Timer recordImTimer = null  ;
	private Handler handler = null ;
	private int currentRecordBtnSelector = 0 ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_layout) ;
		findViews() ;
		init() ;
		
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 0x952700){

					Log.i("TAG", "kkkk" + startRecordingImage) ;
					
					if (msg.obj.equals(currentRecordBtnSelectors[0])){
						
						recordImage.setBackgroundResource(recordStartImages[startRecordingImage++]) ;
						if (startRecordingImage ==  RECORD_IMAGE)
							startRecordingImage = 0 ;
					}else if (msg.obj.equals(currentRecordBtnSelectors[2])){
						
						recordImage.setBackgroundResource(recordPauseImages[startRecordingImage++]) ;
						if (startRecordingImage ==  RECORD_IMAGE)
							startRecordingImage = 0 ;
					}
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
		int select = currentRecordBtnSelector ;
		switch (v.getId()) {
		case R.id.record_begin_btn:{
			
				changeRecordBtnResource(select) ;
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
	
	
	private void init(){
		
		currentRecordBtnSelector = currentRecordBtnSelectors[0] ;
	}
	
	/**
	 * 改变录音开始录音按钮、播放按钮、停止播放按钮的切换效果以及录音状态图片的显示效果
	 */
	private void changeRecordBtnResource(int select){
		
		if (currentRecordBtnSelector == currentRecordBtnSelectors[0]){
			
			startTheRecordTimer(select) ;
			currentRecordBtnSelector = currentRecordBtnSelectors[1] ;
			
		}else if (currentRecordBtnSelector == currentRecordBtnSelectors[1]){
			
			currentRecordBtnSelector = currentRecordBtnSelectors[2] ;
			recordImTimer.cancel() ;
			recordImage.setBackgroundResource(R.drawable.record_img_rec) ;
		}else if (currentRecordBtnSelector == currentRecordBtnSelectors[2]){
			
			startRecordingImage = 0 ; 
			currentRecordBtnSelector = currentRecordBtnSelectors[1] ;
			startTheRecordTimer(select) ;
		}
		recordBegin.setBackgroundResource(currentRecordBtnSelector) ;
	}
	/**
	 * 启动一个定时器，来改变录音显示状态
	 * @param select
	 */
	private void startTheRecordTimer(final int select){
		
		recordImTimer = new Timer() ;
		recordImTimer.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.i("TAG", "kkkkkkkkk" + currentRecordBtnSelector) ;
				Message message = handler.obtainMessage() ;
				message.what = 0x952700 ;
				message.obj = select ;
				message.sendToTarget() ;
			}
			
			
		},500,500) ;
	}
	
}
