package com.bluetel.android.app.individual_assistant.bean;

/**
 * 录音文件
 * @author zss
 *
 * 2014-3-12
 * bluetel-android-app-individual-assistant
 */
public class Record {

	
	private String fileName ;
	private String filePath ;
	private String recordTimer ;
	
	
	public Record() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Record(String fileName, String filePath, String recordTimer) {
		super();
		this.fileName = fileName;
		this.filePath = filePath;
		this.recordTimer = recordTimer;
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
	
	
}
