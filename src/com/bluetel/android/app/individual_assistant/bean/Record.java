package com.bluetel.android.app.individual_assistant.bean;

/**
 * 录音文件
 * @author zss
 *
 * 2014-3-12
 * bluetel-android-app-individual-assistant
 */
public class Record {

	//录音文件名
	private String fileName ;
	//录音路径
	private String filePath ;
	//录音时间
	private String recordTimer ;
	//录音记录时间
	private String timer ;
	
	public Record() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Record(String fileName, String filePath, String recordTimer, String timer) {
		super();
		this.fileName = fileName;
		this.filePath = filePath;
		this.recordTimer = recordTimer;
		this.timer = timer ;
		
	}

	public String getFileName() {
	
		return fileName;
	}
	
	public void setFileName(String fileName) {
		
		this.fileName = fileName;
	}
	
	public String getFilePath() {
	
		return filePath;
	}
	
	public void setFilePath(String filePath) {
	
		this.filePath = filePath;
	}
	
	public String getRecordTimer() {
	
		return recordTimer;
	}
	
	public void setRecordTimer(String recordTimer) {
		
		this.recordTimer = recordTimer;
	}

	public String getTimer() {
		
		return timer;
	}

	public void setTimer(String timer) {
		
		this.timer = timer;
	}
	
	
}
