package com.bluetel.android.app.individual_assistant.ftp;

/**
 * 执行每一个动作后相应的结果，包括成功的和失败的。
 * @author zss
 *
 * 2013-8-29
 * bluetel-android-app-broadcast
 */
public class Result {
	
	/**
	 *响应的内容 
	 */
	private String reponse;
	
	/**
	 * 响应的结果
	 */
	private boolean succeed;
	
	/**
	 * 响应的时间
	 */
	private String time;
	
	/**
	 * 无参的构造函数
	 */
	public Result(){
		
		
	}
	
	/**
	 * 构造函数
	 * @param res 响应的内容
	 */
	public Result(String res){
		
		this.reponse = res ;
	}
	
	/**
	 * 构造方法
	 * @param suc 响应的结果
	 * @param ti  响应的时间
	 * @param res 响应的内容
	 */
	public Result(boolean suc,String ti,String res){
		
		this.succeed = suc ;
		this.reponse = ti ;
		this.reponse = res ;
	}

	/**
	 * 得到相应内容
	 * @return 相应内容
	 */
	public String getReponse() {
		
		return reponse;
	}

	/**
	 * 设置响应内容
	 * @param reponse 响应内容
	 */
	public void setReponse(String reponse) {
		
		this.reponse = reponse;
	}

	/**
	 * 得到响应结果
	 * @return 响应结果
	 */
	public boolean isSucceed() {
		
		return succeed;
	}
	
	/**
	 * 设置响应结果
	 * @param succeed 响应结果
	 */
	public void setSucceed(boolean succeed) {
		
		this.succeed = succeed;
	}

	/**
	 * 得到响应时间
	 * @return 响应时间
	 */
	public String getTime() {
		
		return time;
	}

	/**
	 * 设置响应时间
	 * @param time 响应时间
	 */
	public void setTime(String time) {
		this.time = time;
	}
	
	
}
