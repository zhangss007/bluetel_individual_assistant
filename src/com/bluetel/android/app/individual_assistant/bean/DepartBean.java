package com.bluetel.android.app.individual_assistant.bean;

public class DepartBean {

	private String _id = "" ;
	/**
	 * 唯一标识
	 */
	private String uniqueId = "" ;
	/**
	 * 部门名称
	 */
	private String name = "" ;
	
	public DepartBean(){
		super() ;		
	}
	
	
	
	public DepartBean(String _id, String uniqueId, String name) {
		super();
		this._id = _id;
		this.uniqueId = uniqueId;
		this.name = name;
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
	
}
