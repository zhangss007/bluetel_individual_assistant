package com.bluetel.android.app.individual_assistant.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

public class FileManager {

	/**
	 * 图片文件格式标志
	 */
	public static final int MEDIA_TYPE_IMAGE = 1;
	/**
	 * 视频文件格式标志
	 */
	public static final int MEDIA_TYPE_VIDEO = 2;
	/**
	 * 录音文件格式标志
	 */
	public static final int MEDIA_TYPE_RECORD = 3 ;
	
	private static  FileManager instance = null  ;
	
	private File mediaStorageDir = null ;
	
	private static final String TAG = "TAG" ;
	
	private static boolean isInstance(){
		
		if (instance!= null)
			return true ;
		return false ;
	}
	
	protected FileManager(){
		
		mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
		//创建一个文件存储目录，如果该存储目录不存在
		if (!mediaStorageDir.exists()){
			
			if (!mediaStorageDir.mkdir()){
				
				Log.i(TAG, "文件存储目录创建失败") ;
			}
		}
	}
	
	
	
	public static FileManager getInstance() {
		
		if (!isInstance())
			instance = new FileManager() ;		
		return instance ;
	}
	
	
	public File getOutPutMediaFile(int type) {
		
		// Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
//	        currentUploadFileName = mediaStorageDir.getPath() + File.separator +
//	    	        "IMG_"+ timeStamp + ".jpg" ;
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
//	        currentUploadFileName = mediaStorageDir.getPath() + File.separator +
//	    	        "VID_"+ timeStamp + ".mp4" ;
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	
	
}
