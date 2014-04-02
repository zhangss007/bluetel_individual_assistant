package com.bluetel.android.app.individual_assistant;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CaptureActivity extends Activity{

	
	private static final String TAG = "TAG" ;
	private Camera mCamera = null ;
	private CameraPreview mPreview;
	private FrameLayout preview  = null ;
	private MediaRecorder mMediaRecorder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
		setContentView(R.layout.capture_layout) ;
		startPreviewCamera() ;
		Log.i(TAG, "onCreate............") ;
	}

	
	/**
	 * Check if this device has a camera 
	 * @param context
	 * @return
	 */
	private boolean checkCameraHardware(Context context){
		
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
		
			//this device has a camera 
			return true ;
		}else {
			
			//no camera on this device
			return false ;
		}
	}
	/**
	 * 启动摄像头显示
	 */
	private void startPreviewCamera(){
		
		if (checkCameraHardware(this)){
			
			Log.i(TAG, getResources().getString(R.string.haved_camera)) ;
			mCamera = getCameraInstance() ;
			if (mCamera != null ){
				
				mPreview = new CameraPreview(this, mCamera);
		        preview = (FrameLayout) findViewById(R.id.camera_preview);
		        preview.addView(mPreview);
		        
		        
			}else{
				
				Toast.makeText(CaptureActivity.this, "相机初始化失败....", Toast.LENGTH_SHORT).show() ;
				releaseCamera() ;
				CaptureActivity.this.finish() ;
			}

		}else {
			
			Log.i(TAG, getResources().getString(R.string.no_camera)) ;                                             
		}
	}
	
	public static Camera getCameraInstance(){
		
		Camera c = null ;
		try{
			//attempt to get a Camera instance 
			c = Camera.open() ;
		}catch(Exception e){
			
			//Camera is not available(in use dose not exit)
		}
		return c ;
	}
	
	
	private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }


	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestory............") ;
		releaseMediaRecorder() ;
		releaseCamera() ;
	}


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
		Log.i(TAG, "onRestart............") ;
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume............") ;
		//startPreviewCamera() ;
		
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause............") ;
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG, "onStart............") ;
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "onStop............") ;
	}
    
    
    
	
}
