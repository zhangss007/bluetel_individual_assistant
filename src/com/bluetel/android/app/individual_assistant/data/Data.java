package com.bluetel.android.app.individual_assistant.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.bluetel.android.app.individual_assistant.util.ExtenString;


/**
 * 数据存储类   单例模式实现
 * @author zss
 *
 * 2014-3-18
 * bluetel-android-app-individual-assistant
 */
public class Data {

	private static Data instance  = null ;
	
	/**
	 * 部门数据缓冲区
	 */
	private List<Map<String,Object>> mdepartList = new ArrayList<Map<String,Object>>() ;
	
	/**
	 * 分机信息数据库缓冲区
	 */
	private List<Exten> extenInfoList = new ArrayList<Exten>() ;
	
	private int startCreate = 0  ;  
	
	/**
	 * 当前全部分机状态集合
	 */
	private Map<String,Depart> mDepartMap ;
	
	
	
	
	
	public Map<String, Depart> getmDepartMap() {
		
		return mDepartMap;
	}

	public void setmDepartMap(Map<String, Depart> mDepartMap) {
		this.mDepartMap = mDepartMap;
	}

	protected Data(){		
		
	}
	
  public static synchronized Data getInstance(){
		
		if (instance == null){
			
			instance  = new Data() ;
		}
		return instance ;
	}
  
  
  /**
	 * 获取从MySql查询出来的数据，信息包括 分机名、分机号码，分机所属部门
	 * @param extenInfo
	 */
	public void setExtenInfo(String extenInfo){
		
		//684!客户端广播!684     解析字符串获得  分机名，分机号 ， 分机所属部门
		String name , depart , exten ;
		ExtenString extenString = new ExtenString() ;
		extenString.setExtenString(extenInfo) ;
		name = extenString.getSubStringName() ;
		depart = extenString.getSubStringDepart() ;
		exten = extenString.getSubStringNumber() ;
		creatExtenInfo(exten, name , depart , -1) ;
		if (!checkTheDepartValue(depart)){
			
			Map<String,Object> mDepartMap = new HashMap<String,Object>() ;
			mDepartMap.put("Depart", depart) ;
			mdepartList.add(mDepartMap) ;
		}
	}
  
	
	/**
	 * 创建数据库分机查询信息
	 * @param exten
	 * @param name
	 * @param depart
	 * @param status
	 */
	private void creatExtenInfo(String exten, String name, String depart, int status) {
		
		Exten extenItem = new Exten() ;
		extenItem.setExtenInfo(exten, name, depart, status) ;
		extenInfoList.add(extenItem) ;
	}
	
	
	/**
	 * 检测部门是否重复
	 * @param depart
	 * @return
	 */
	private boolean checkTheDepartValue(String depart) {
		
		boolean status = false ;
		for (int i = 0 ; i < mdepartList.size() ; i++){
			
			if (mdepartList.get(i).containsValue(depart)){
				
				status = true ;
				break ;
			}
		}
		return status ;
	}

	public List<Map<String, Object>> getMdepartList() {
		return mdepartList;
	}

	public void setMdepartList(List<Map<String, Object>> mdepartList) {
		this.mdepartList = mdepartList;
	}

	public List<Exten> getExtenInfoList() {
		return extenInfoList;
	}

	public void setExtenInfoList(List<Exten> extenInfoList) {
		this.extenInfoList = extenInfoList;
	}
	
	
	public void setDate(Exten mExten, String status) {
		if (startCreate == 0 ){
			
			mDepartMap = new HashMap<String,Depart>() ;
			creatDepart(mExten.getNumber(),mExten.getName(),mExten.getDepart(),status) ;
	        startCreate = 1 ;
	    }else{

	        Depart depart= null ;
	        if (mDepartMap.containsKey(mExten.getDepart())){
	        	
	        	depart = mDepartMap.get(mExten.getDepart());
	        	if (!depart.isHaveExten(mExten.getNumber())){
	        		
	        		 depart.addExtens(mExten.getNumber(),mExten.getName(),mExten.getDepart(),status) ;
	        	}else {
	        		
	        		depart.updateExtenStatus(mExten.getNumber(), status);
	        	}
	           
	        }else{

	        	creatDepart(mExten.getNumber(),mExten.getName(),mExten.getDepart(),status) ;
	        }
	    }
	}
	
	
	/**
	 * 创建部门信息
	 * @param num
	 * @param name
	 * @param depart
	 * @param status
	 */
	private void creatDepart(String exten , String name , String depart,String status ){
		
        Depart departItem = new Depart() ;
        departItem.addExtens(exten,name,depart,status) ;
        mDepartMap.put(depart,departItem) ;
	}
	
	/**
	 * 通过分机号码查找该分机所在的部门
	 * @param number 查找分机分号码
	 * @return       部门
	 */
	public String getDepartByNumber(String number){
		
    	String cDepart = null;
    	for (int i = 0 ; i < extenInfoList.size() ; i++){
    		
    		if (number.equals(extenInfoList.get(i).getNumber())){
    			
    			cDepart = extenInfoList.get(i).getDepart() ;
    			break ;
    		}
    	}
    	
    	return cDepart ;
	}
	
	
	/**
	 * 获得当前部门中选中分机的当前状态
	 * @param depart
	 * @param exten
	 * @return
	 */
	public int getCurrentDepartExtenStatus(String depart , String exten){
		
		int status = -1 ; 
		Depart cDepart = null ;
		//Log.i("TAG", "部门---->" + depart) ;
		if (mDepartMap.containsKey(depart)&&mDepartMap!= null){
			
			cDepart = mDepartMap.get(depart) ;
			if (cDepart!=null&&cDepart.getCurDepartExten(exten) != null){
				
				status = cDepart.getCurDepartExten(exten).getStatus();
			}	
			
		}
		return status ;
	}
	
	
    /**
	 *获取分机状态
	 * @param status
	 * @return
	 */
    public int getStatus(String status){
    	
    	int sta= -1 ;
        if(status.equals("Ring")){

            sta = Exten.USER_STATUS_CALLING_RING;
        }else if(status.equals("Ringing")){

            sta = Exten.USER_STATUS_CALLED_RING;
        }else if(status.equals("Up")){

            sta = Exten.USER_STATUS_TALKING;
        }else if(status.equals("Hangup")){

            sta = Exten.USER_STATUS_HANGUP;
        }else if(status.equals("Registered")||status.equals("Reachable")){

            sta = Exten.USER_STATUS_ONLINE;
        }else if(status.equals("Unreachable") || status.equals("Unregistered")){

            sta = Exten.USER_STATUS_OFFLINE;
        }else if(status.equals("Meetme")){

            sta = Exten.USER_STATUS_MEETING;
        }
    	return sta ;
    }
    
    /**
     *更新当前分机状态
     * @param chan
     * @param sta
     * @param number
     * @param mDepart
     * @param channel
     * @param bridgeChan
     * @param uid
     */
	public void updateCurrentExtenInfo(String chan, int sta, String number,
			String mDepart, String channel, String bridgeChan, String uid) {
		
    	Depart cDepart = null ;
    	if (mDepartMap.containsKey(mDepart)){
    		
    		cDepart = mDepartMap.get(mDepart) ;
    		cDepart.updateExtenInfo(chan, sta , number,channel , bridgeChan , uid) ;
    	}
		
	}
}
