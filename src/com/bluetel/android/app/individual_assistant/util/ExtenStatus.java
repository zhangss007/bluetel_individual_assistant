package com.bluetel.android.app.individual_assistant.util;

/**
 * 获得显示分机状态
 * @author zss
 *
 * 2013-8-21
 * bluetel-android-app-broadcast
 */
public class ExtenStatus {

	public   static String getExtenStatus(int status){
		
		String cStatus = null ;
		switch(status){
		
		case -11: cStatus = "状态" ;
			break ;
		case 0 : cStatus = "离线"; //OFFLINE
			break ;
		case 1 : cStatus = "在线";//ONLINE
			break ;
		case 2 : cStatus = "在线" ;//HANGUP
			break ;
		case 3 : cStatus = "呼出响铃" ; //CALLING_RING
			break ;
		case 4 : cStatus = "呼入响铃" ;//CALLED_RING
			break ;
		case 5 : cStatus = "通话中" ;//TALKING
			break ;
		case 6 : cStatus = "会议中";//MEETING
			break ;
		case 7 : cStatus = "广播中" ;
		 	break ;
		default : cStatus = "其他情况" ;//Defult
			break ;
		}
		return cStatus ;
	}
}
