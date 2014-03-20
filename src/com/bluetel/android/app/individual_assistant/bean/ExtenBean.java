package com.bluetel.android.app.individual_assistant.bean;

public class ExtenBean {

	private String _id = "" ;
	/**
	 * 唯一表示
	 */
	private String uniqueId = "" ;
	/**
	 * 分机名称
	 */
	private String name = "";
	/**
	 * 分机号码
	 */
	private String number = "" ;
	
	private String exten  = "" ;
	/**
	 * 部门所属分机
	 */
	private String depart = "" ;
	/**
	 * 通道
	 */
	private String chan = "" ;
	
	private String bchan = "" ;
	
	private String uid = "" ;
	
	private String status = "" ;

	public ExtenBean(){
		
		super() ;
	}
	
	public ExtenBean(String _id, String uniqueId, String name, String number,
			String exten, String depart, String chan, String bchan, String uid,
			String status) {
		super();
		this._id = _id;
		this.uniqueId = uniqueId;
		this.name = name;
		this.number = number;
		this.exten = exten;
		this.depart = depart;
		this.chan = chan;
		this.bchan = bchan;
		this.uid = uid;
		this.status = status;
	}

	public String get_id() {
		return _id;
	}
	
	public void set_id(String _id) {
		this._id = _id;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getExten() {
		return exten;
	}

	public void setExten(String exten) {
		this.exten = exten;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		
		this.status = status;
	}
	
	
}
