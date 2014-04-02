package com.bluetel.android.app.individual_assistant.bean;

public class UploadFile {

	private int id ;
	private String user ;
	private String fileName ;
	private int fileType ;
	private String filePath ;
	private String time ;
	private int status ;
	
	public UploadFile() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UploadFile(int id, String user, String fileName, int fileType,
			String filePath, String time, int status) {
		super();
		this.id = id;
		this.user = user;
		this.fileName = fileName;
		this.fileType = fileType;
		this.filePath = filePath;
		this.time = time;
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getFileType() {
		return fileType;
	}
	public void setFileType(int fileType) {
		this.fileType = fileType;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}
