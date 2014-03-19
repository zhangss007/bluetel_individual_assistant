package com.bluetel.android.app.individual_assistant.util;


/**
 * sql查询字符串数据解析基类
 * @author zss
 *
 * 2013-8-15
 * bluetel-android-app-broadcast
 */
public class MString {

	private String mStr ;

	public String getmStr() {
	
		return mStr;
	}

	public void setmStr(String mStr) {
		
		this.mStr = mStr;
	}
	
	public  String substring(){

		String str = null ;
		
		if (mStr.contains("!")){
			
			int pos = mStr.indexOf("!") ;
			str = mStr.substring(0, pos) ;
			mStr = mStr.substring(pos+1, mStr.length()) ;
			return str ;
			
		}else {
			
			return mStr ;
		}

	}
	
}
