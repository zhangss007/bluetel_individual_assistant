package com.bluetel.android.app.individual_assistant;

import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCore;
import org.linphone.mediastream.video.AndroidVideoWindowImpl;
import org.linphone.mediastream.video.capture.hwconf.AndroidCameraConfiguration;

import com.bluetel.android.app.individual_assistant.linphone.CallManager;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneManager;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class VideoActivity extends Activity implements OnClickListener,Callback{

	private SurfaceView mVideoView;
	private SurfaceView mCaptureView;
	private AndroidVideoWindowImpl androidVideoWindowImpl;
	
	private Camera mCamera = null ;
	private CameraPreview mPreview;
	private FrameLayout preview  = null ;
	
    private int numCamera;
    private int cameraId;
    private int cameraCurrentId;
	
    /**
     * 返回主页
     */
	private Button backHomeBtn ;
	
	/**
	 * 摄像头切换
	 */
	private ImageButton switchCamer ;
	
	/**
	 * 拍照 上传
	 */
	private ImageButton shutter ;
	
	
	private static VideoActivity instance ;
	
	//监控状态
	private boolean monitor = false ;
	
	public static boolean isInstance(){
		
		return instance!=null ;
	}
	
	public static VideoActivity getInstance(){
		
		return instance ;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) ;
		setContentView(R.layout.video) ;
		
		mVideoView = (SurfaceView)findViewById(R.id.videoSurface);
		mCaptureView = (SurfaceView)findViewById(R.id.videoCaptureSurface);
		mCaptureView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // Warning useless because value is ignored and automatically set by new APIs.
		
		startSipCapture() ;
		findViews() ;
		
		 numCamera = Camera.getNumberOfCameras();
		 CameraInfo info = new CameraInfo();
		 for(int i = 0;i< numCamera;i++){
			 Camera.getCameraInfo(i, info);
			 if(info.facing == CameraInfo.CAMERA_FACING_BACK){
				 cameraId = i;
			 }
		 }
		 //cameraCurrentId = cameraId ;
		instance = this ;
		//startPreviewCamera() ;
	}

	
	
	private void findViews(){
		
		backHomeBtn = (Button)findViewById(R.id.home) ;
		switchCamer = (ImageButton)findViewById(R.id.switch_camera) ;
		shutter = (ImageButton)findViewById(R.id.shutter) ;
		backHomeBtn.setOnClickListener(this) ;
		switchCamer.setOnClickListener(this) ;
		shutter.setOnClickListener(this) ;
	}
	
	
	private void startSipCapture(){
		
		
		fixZOrder(mVideoView, mCaptureView);
		
		androidVideoWindowImpl = new AndroidVideoWindowImpl(mVideoView, mCaptureView);
		androidVideoWindowImpl.setListener(new AndroidVideoWindowImpl.VideoWindowListener() {
			public void onVideoRenderingSurfaceReady(AndroidVideoWindowImpl vw, SurfaceView surface) {
				LinphoneManager.getLc().setVideoWindow(vw);
				mVideoView = surface;
			}

			public void onVideoRenderingSurfaceDestroyed(AndroidVideoWindowImpl vw) {
				LinphoneCore lc = LinphoneManager.getLc(); 
				if (lc != null) {
					lc.setVideoWindow(null);
				}
			}
			
			public void onVideoPreviewSurfaceReady(AndroidVideoWindowImpl vw, SurfaceView surface) {
				mCaptureView = surface;
				LinphoneManager.getLc().setPreviewWindow(mCaptureView);
			}

			public void onVideoPreviewSurfaceDestroyed(AndroidVideoWindowImpl vw) {
				// Remove references kept in jni code and restart camera
				LinphoneManager.getLc().setPreviewWindow(null);
			}
		});
		
		
		androidVideoWindowImpl.init();	
	}
	
	
	
	
	
	private void releaseTheSipCapture(){
		
		mCaptureView = null;
		if (mVideoView != null) {
			mVideoView.setOnTouchListener(null);
			mVideoView = null;
		}
		if (androidVideoWindowImpl != null) { 
			// Prevent linphone from crashing if correspondent hang up while you are rotating
			androidVideoWindowImpl.release();
			androidVideoWindowImpl = null;
		}
		
		
	}
	
	
	
	
	@Override
	public void onDestroy() {
		//inCallActivity = null;
		
		releaseTheSipCapture() ;
		releaseCamera() ;
		
		super.onDestroy();
	}
	

	@Override
	public void onPause() {	
		
		releaseCamera() ;
		if (androidVideoWindowImpl != null) {
			synchronized (androidVideoWindowImpl) {
				/*
				 * this call will destroy native opengl renderer which is used by
				 * androidVideoWindowImpl
				 */
				LinphoneManager.getLc().setVideoWindow(null);
			}
		}
		
		if (mVideoView != null) {
			((GLSurfaceView) mVideoView).onPause();
		}
		
		
		
		super.onPause();
	}
	
	@Override
	public void onResume() {	
		
		super.onResume();		
		
		boolean isVideoEnabled = false ;
		
		if (LinphoneManager.getLc().getCallsNb() > 0) {
        	LinphoneCall call = LinphoneManager.getLc().getCalls()[0];

        	if (LinphoneUtils.isCallEstablished(call)) {
    			isVideoEnabled = call.getCurrentParamsCopy().getVideoEnabled() && !call.getRemoteParams().isLowBandwidthEnabled();
    			
        	}
        }
		
		if (isVideoEnabled){
			//监控中
			mCaptureView.setVisibility(View.VISIBLE) ;	
			monitor = true ;
		}else{
			
        	mCaptureView.setVisibility(View.INVISIBLE) ;
        	if (mCamera == null){
        		//releaseCamera() ;
        		
        		if (numCamera == 2){
	        		mCamera = Camera.open();
					mPreview = new CameraPreview(this, mCamera);
			        preview = (FrameLayout) findViewById(R.id.camera_preview);
			        preview.addView(mPreview);
	        		cameraCurrentId = cameraId;
        		}else {
					startPreviewCamera() ;
        		}

        	}
        	monitor = false ;
		}
		
		if (mVideoView != null) {
			((GLSurfaceView) mVideoView).onResume();
		}
		
		if (androidVideoWindowImpl != null) {
			synchronized (androidVideoWindowImpl) {
				LinphoneManager.getLc().setVideoWindow(androidVideoWindowImpl);
			}
		}
	}
	
	private void fixZOrder(SurfaceView video, SurfaceView preview) {
		video.setZOrderOnTop(true);
		preview.setZOrderOnTop(true);
		preview.setZOrderMediaOverlay(true); // Needed to be able to display control layout over
	}
	

	
	public boolean onDown(MotionEvent e) {
		
		return true; // Needed to make the GestureDetector working
	}

	public boolean onDoubleTapEvent(MotionEvent e) {
		
		return false;
	}

	public boolean onSingleTapConfirmed(MotionEvent e) {
		
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	public void onLongPress(MotionEvent e) {
		
	}

	public void onShowPress(MotionEvent e) {
		
	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	
	
	
	
    public void releaseCamera(){
        if (mCamera != null){
            mCamera.release();   
            //mCamera.stopPreview() ;
            // release the camera for other applications
            mCamera = null;
            if (mPreview != null)
            	preview.removeView(mPreview) ;
        }
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
	public void startPreviewCamera(){
		
		mCaptureView.setVisibility(View.INVISIBLE) ;
		if (checkCameraHardware(this)){
			
			mCamera = getCameraInstance() ;
			if (mCamera != null ){
				
				mPreview = new CameraPreview(this, mCamera);
		        preview = (FrameLayout) findViewById(R.id.camera_preview);
		        preview.addView(mPreview);
				//cameraCurrentId = cameraId;
			}else{

				Toast.makeText(VideoActivity.this, "相机初始化失败....", Toast.LENGTH_SHORT).show() ;
			}

		}else {
			
			Log.i("TAG", getResources().getString(R.string.no_camera)) ;                                             
		}
	}
	
	
	public Camera getCameraInstance(){
		
		Camera c = null ;
		try{
		
            c = Camera.open((cameraCurrentId + 1) % numCamera);
            cameraCurrentId = (cameraCurrentId + 1)
                    % numCamera;
			 
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return c ;
	}
	
	
	
	
	
	public static void setCameraDisplayOrientation(Activity activity,
	         int cameraId, android.hardware.Camera camera) {
	     android.hardware.Camera.CameraInfo info =
	             new android.hardware.Camera.CameraInfo();
	     android.hardware.Camera.getCameraInfo(cameraId, info);
	     int rotation = activity.getWindowManager().getDefaultDisplay()
	             .getRotation();
	     int degrees = 0;
	     switch (rotation) {
	         case Surface.ROTATION_0: degrees = 0; break;
	         case Surface.ROTATION_90: degrees = 90; break;
	         case Surface.ROTATION_180: degrees = 180; break;
	         case Surface.ROTATION_270: degrees = 270; break;
	     }

	     int result;
	     if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	         result = (info.orientation + degrees) % 360;
	         result = (360 - result) % 360;  // compensate the mirror
	     } else {  // back-facing
	         result = (info.orientation - degrees + 360) % 360;
	     }
	     camera.setDisplayOrientation(result);
	 }
	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 按钮监听事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home:{
				
				if (MainActivity.isInStance()){
					
					MainActivity.getInstance().backHome() ;
				}
			}
			break;
		case R.id.switch_camera:{
			
				if (monitor){
					
					switchCamera() ;
				}else {
					
					releaseCamera();
					startPreviewCamera() ;	
				}
				
			}
			break ;
		case R.id.shutter:{
				
				
			}
			break ;
		default:
			break;
		}
	}
	
	//切换摄像头
	  public void switchCamera() {	
			try {
				int videoDeviceId = LinphoneManager.getLc().getVideoDevice();
				videoDeviceId = (videoDeviceId + 1) % AndroidCameraConfiguration.retrieveCameras().length;
				LinphoneManager.getLc().setVideoDevice(videoDeviceId);
				CallManager.getInstance().updateCall();
				
				// previous call will cause graph reconstruction -> regive preview window
				if (mCaptureView != null) {
					LinphoneManager.getLc().setPreviewWindow(mCaptureView);
				}
			} catch (ArithmeticException ae) {
				//Log.e("Cannot swtich camera : no camera");
			}
		}
}
