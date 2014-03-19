package com.bluetel.android.app.individual_assistant.util;

/**
 * SQL查询分机信息字符串数据解析类
 * @author zss
 *
 * 2013-8-15
 * bluetel-android-app-broadcast
 */
public class ExtenString extends MString{

	public ExtenString() {
		super();
		
	}

	/**
	 *设置分机信息解析字符串
	 * @param str
	 */
	public void setExtenString(String str){
		
		this.setmStr(str) ;
	}
	
	
	////684!客户端广播!684
	/**
	 * 解析获得分机名称
	 * @return
	 */
	public String getSubStringName(){
		
		return this.substring() ;
	}
	
	/**
	 * 解析分机信息字符串获得部门
	 * @return
	 */
	public String getSubStringDepart(){
		
		return this.substring() ;
	}
	
	/**
	 * 解析分机信息字符串获得分机号码
	 * @return
	 */
	public String getSubStringNumber(){
		
		return this.substring() ;
	}
	
}
