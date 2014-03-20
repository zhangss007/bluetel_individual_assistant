package com.bluetel.android.app.individual_assistant.bean;

public class ChatMsgEntity {

	
	private String name ;
	private String date ;
	private String txt ;
	private boolean isComMsg = true ;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTxt() {
		return txt;
	}
	public void setTxt(String txt) {
		this.txt = txt;
	}
	public boolean isComMsg() {
		return isComMsg;
	}
	public void setComMsg(boolean isComMsg) {
		this.isComMsg = isComMsg;
	}
	public ChatMsgEntity(String name, String date, String txt, boolean isComMsg) {
		super();
		this.name = name;
		this.date = date;
		this.txt = txt;
		this.isComMsg = isComMsg;
	}
	public ChatMsgEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
