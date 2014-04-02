package com.bluetel.android.app.individual_assistant.data;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * 部门信息类
 * @author zss
 *
 * 2013-8-2
 * bluetel-android-app-broadcast
 */
public class Depart {
	
	
	private List<Exten> cDepartList = new ArrayList<Exten>() ;
	private int count ;
	
	
	public Depart() {
		
		super();
		count = 0 ;
	}

	
	public boolean isHaveExten(String extenNumber){
		
		boolean isvalue = false ;
		for(Exten exten:cDepartList){
			
			if (exten.getNumber().equals(extenNumber)){
				
				isvalue = true ;
				break ;
			}
		}
		return isvalue ;
	}
	/**
	 * 返回当前部门的分机信息
	 * @return
	 */
	public List<Exten> getcDepartList() {
		
		return cDepartList;
	}



	public int getCount() {
		
		return count;
	}

	/**
	 * 通过分机号返回当前分机信息
	 * @param number
	 * @return
	 */
	public Exten getCurDepartExten(String number){
		
		Exten cExten = null ;
		
		for(Exten exten:cDepartList){
			
			if (number.equals(exten.getNumber())){
				
				cExten = exten ;
				break ;
			}
			
		}
		
		return cExten ;		
	}

	//存储接收分机信息
	public void addExtens(String exten,String name , String depart ,String status){
		
		int seats_state = -1 ;
		if (!status.contains("OK") || status.contains("UNKNOWN")){
			
			seats_state = 0 ;
		}else{
			
			seats_state = 1 ;
		}
		count ++ ;
		createExten(exten,name,depart,seats_state) ;    
	}

	
	//创建坐席用户信息
	
	private void createExten (String exten , String name , String depart , int status){
		
		Exten cExten = new Exten() ;
	    cExten.setNumber(exten) ;
	    cExten.setName(name) ;
	    cExten.setDepart(depart) ;
	    cExten.setStatus(status) ;
	    if (!cDepartList.contains(cExten)){
	    	
	    	cDepartList.add(cExten) ;
	    }
	    
	}
	
	public void updateExtenStatus(String exten,String status ){
		
		int seats_state = -1 ;
		if (!status.contains("OK") || status.contains("UNKNOWN")){
			
			seats_state = 0 ;
		}else{
			
			seats_state = 1 ;
		}
		
		if (cDepartList != null){
			
		Log.i("TAG", "部门名称为--》" + cDepartList.get(0).getDepart()) ;
			for (Exten cExten:cDepartList){
				
				if (cExten!=null&&cExten.getNumber().equals(exten)){
					
					cExten.setStatus(seats_state) ;
					break ;
				}
			}
		}
	}
	
	//更新分机状态信息
	public void updateExtenInfo(String exten , int status ,String num,String channel , String bchannel , String uid ){
		
		
		Exten mExten = null ;
		for (int  i= 0 ; i < cDepartList.size() ; i++){
			int oldSta; 
			if (exten.equals(cDepartList.get(i).getNumber())){

				mExten = cDepartList.get(i) ;
				oldSta = mExten.getStatus() ;
				if (oldSta == Exten.USER_STATUS_CALLED_RING ||oldSta == Exten.USER_STATUS_CALLING_RING ||
					oldSta == Exten.USER_STATUS_TALKING || oldSta == Exten.USER_STATUS_BROADCAST){
					
					if (status == Exten.USER_STATUS_HANGUP || status == Exten.USER_STATUS_OFFLINE ||
						status == Exten.USER_STATUS_CALLED_RING ||status == Exten.USER_STATUS_CALLING_RING ||
						status == Exten.USER_STATUS_TALKING || status == Exten.USER_STATUS_BROADCAST){
						
						
						if(mExten.getExtenNum()== null ){
							mExten.setExtenNum(num) ;
						if(status == Exten.USER_STATUS_HANGUP || status == Exten.USER_STATUS_OFFLINE){
							
							mExten.setExtenNum("") ;
						}
						}
						
						if (num!=null&&num.equals("000")){
						
							mExten.setStatus(Exten.USER_STATUS_BROADCAST) ;
							Log.i("TAG", "&&&&&&&&&&&&&&"+"----" + mExten.getExtenNum()+ "----") ;
						
						}else {
						
							mExten.setStatus(status) ;
						}
						//mExten.setStatus(status) ;
						mExten.setChan(channel) ;
					}
					
				}else{
					
					mExten.setExtenNum(num) ;
					mExten.setStatus(status) ;
					mExten.setChan(channel) ;
				}
				mExten.setBchan(bchannel) ;
				mExten.setUid(uid) ;
				break ;
			}
		}
	}
}
