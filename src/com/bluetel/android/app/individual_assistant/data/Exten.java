package com.bluetel.android.app.individual_assistant.data;

/**
 * 分机信息类
 * @author zss
 *
 * 2013-8-2
 * bluetel-android-app-broadcast
 */
public class Exten {

	
	//通话分机状态
	public static final int USER_STATUS_OFFLINE = 0 ;
	public static final int USER_STATUS_ONLINE = 1 ;
	public static final int USER_STATUS_HANGUP = 2 ;
	public static final int USER_STATUS_CALLING_RING = 3 ;
	public static final int USER_STATUS_CALLED_RING = 4 ;
	public static final int USER_STATUS_TALKING = 5 ;
	public static final int USER_STATUS_MEETING = 6 ;
	public static final int USER_STATUS_BROADCAST = 7 ; //广播状态
	
	
	private String number ;
	private String extenNum  ;
	private String name ;
	private String depart ;
	private String chan ;
	private String bchan ;
	private String uid ;
	private int status ;
	private String statusStr ;
	
	
	
	public Exten() {
		
		super();
	}

	public Exten(String number, String name, String depart, String chan,
			String bchan, String uid, int status, String statusStr) {
		
		super();
		this.number = number;
		this.name = name;
		this.depart = depart;
		this.chan = chan;
		this.bchan = bchan;
		this.uid = uid;
		this.status = status;
		this.statusStr = statusStr;
	}

	public String getNumber() {
		
		return number;
	}
	
	public void setNumber(String number) {
		
		this.number = number;
	}
	
	
	
	public String getExtenNum() {
		
		return extenNum;
	}

	public void setExtenNum(String extenNum) {
		
		this.extenNum = extenNum;
	}

	public String getName() {
		
		return name;
	}
	
	public void setName(String name) {
		
		this.name = name;
	}
	
	public String getDepart() {
		
		return depart;
	}
	
	public void setDepart(String depart) {
		
		this.depart = depart;
	}
	
	public String getChan() {
		
		return chan;
	}
	
	public void setChan(String chan) {
		
		this.chan = chan;
	}
	
	public String getBchan() {
		
		return bchan;
	}
	
	public void setBchan(String bchan) {
		
		this.bchan = bchan;
	}
	
	public String getUid() {
		
		return uid;
	}
	
	public void setUid(String uid) {
		
		this.uid = uid;
	}
	
	public int getStatus() {
		
		return status;
	}
	
	public void setStatus(int status) {
		
		this.status = status;
	}
	
	public String getStatusStr() {
		
		return statusStr;
	}
	
	public void setStatusStr(String statusStr) {
		
		this.statusStr = statusStr;
	}

	/**
	 * 设置分机信息
	 * @param exten  分机号
	 * @param name   坐席姓名
	 * @param depart 所属部门
	 * @param status 当前分机状态
	 */
	public void setExtenInfo(String exten, String name, String depart,
			int status) {
		
		this.number = exten ;
		this.name = name ;
		this.depart = depart ;
		this.status = status ;
	}
	
}
