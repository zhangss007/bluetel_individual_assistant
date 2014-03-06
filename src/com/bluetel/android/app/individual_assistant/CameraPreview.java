package com.bluetel.android.app.individual_assistant;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

	
	private SurfaceHolder mHolder ;
	private Camera mCamera ;
	private static final String TAG = "TAG" ;
	
	public CameraPreview(Context context) {
		super(context);
		
	}

	public CameraPreview(Context context,Camera camera) {
		super(context);
		mCamera = camera ;
		//Install a SurfaceHoder.Callback so we get notified when the 
		//underlying surface is create and destroyed.
		this.mHolder = getHolder() ;
		mHolder.addCallback(this) ;
		//deprecated setting ,but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS) ;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		// If your preview can change  or rotate , take care of those events here 
		//Make sure to stop the preview before resizing or reformatting it .
		if (mHolder.getSurface() == null ){
					
			return ;
		}
		//stop preview before making changes
		try{
					
			mCamera.stopPreview() ;
		}catch(Exception e){
					
			//ignore: tried to stop a non-existent preview 
		}
				
		//set preview size and make any resize,rotate or
		//reformatting changes here 
				
		//start preview with new setting
		try{
					
			mCamera.setPreviewDisplay(holder) ;
			mCamera.startPreview() ;
		}catch(Exception e){
					
			Log.d(TAG, "Error starting camera preview:" + e.getMessage()) ;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//The Surface has been created ,now tell the camera where to draw the preview
		try{
					
			if (mCamera != null ){
						
			mCamera.setPreviewDisplay(holder) ;
			mCamera.startPreview() ;
			}

		}catch(IOException e){
					
					
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		//releaseCamera() ;
	}
	
    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

}
