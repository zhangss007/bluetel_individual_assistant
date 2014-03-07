package com.bluetel.android.app.individual_assistant;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
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
	
	//语音操作对象
	private MediaPlayer mPlayer = null ;
	private MediaRecorder mRecorder = null ;
	//语音文件路径
	private String fileName = null ;
	
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
		
        //设置sdcard的路径  
		fileName = Environment.getExternalStorageDirectory().getAbsolutePath();  
		fileName += "/audiorecordtest.3gp"; 
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
			stratRecord();
			currentRecordBtnSelector = currentRecordBtnSelectors[1] ;
			
		}else if (currentRecordBtnSelector == currentRecordBtnSelectors[1]){
			
			stopRecord() ;
			stopPlay() ;
			currentRecordBtnSelector = currentRecordBtnSelectors[2] ;
			recordImTimer.cancel() ;
			recordImage.setBackgroundResource(R.drawable.record_img_play) ;
		}else if (currentRecordBtnSelector == currentRecordBtnSelectors[2]){
			
			startRecordingImage = 0 ; 
			currentRecordBtnSelector = currentRecordBtnSelectors[1] ;
			startTheRecordTimer(select) ;
			startPlay() ;
		}
		recordBegin.setBackgroundResource(currentRecordBtnSelector) ;
	}
	/**
	 * 启动一个定时器，来改变录音渐变显示效果
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
	
	/**
	 * 开始录音
	 */
	private void stratRecord(){
		
		//TODO start record 
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC) ;
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP) ;
		mRecorder.setOutputFile(fileName) ;
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) ;
		try {
			mRecorder.prepare() ;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mRecorder.start() ;
	}
	
	/**
	 * 停止录音
	 */
	private void stopRecord(){
		
		
		if (mRecorder != null){
			
			mRecorder.stop() ;
			mRecorder.release() ;
			mRecorder = null ;
		}

	}
	
	/**
	 * 播放录音
	 */
	private void startPlay(){
		
		mPlayer = new MediaPlayer() ;
		//录音播放完毕的监听事件
		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				recordImTimer.cancel() ;
				recordImage.setBackgroundResource(R.drawable.record_img_play) ;
				changeRecordBtnResource(currentRecordBtnSelectors[1]) ;
			}
		}) ;
		try{  
            mPlayer.setDataSource(fileName);  
            mPlayer.prepare();  
            mPlayer.start();  
        }catch(IOException e){  
            Log.e("TAG","播放失败");  
        }  
	}
	
	/**
	 * 停止播放录音
	 */
	private void stopPlay(){
		
		if (mPlayer != null){
			
			mPlayer.stop() ;
			mPlayer.release() ;
			mPlayer = null ;
		}
	}
}
