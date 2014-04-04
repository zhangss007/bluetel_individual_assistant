package com.bluetel.android.app.individual_assistant.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;



import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 远程数据 获得 管理类
 * @author zss
 *
 * 2014-3-18
 * bluetel-android-app-individual-assistant
 */
public class SipDataManager {


	private Handler handler ;
	private String serverIp ;
	/**
	 * 数据库连接查询端口
	 */
	private static final  int SER_SQL_CON_PORT  = 15038 ;
	/**
	 * AMI连接分机状态刷新端口
	 */
    private static final int SER_AMI_CON_PORT = 5038 ;	
    //网络错误连接状态标志
    private boolean netErrorStatus = false ; 
    
    
    //网络开始连接错误
    public static final int NET_CONNECTING_ERROR = 0x4001 ;
    //网络连接过程中断，需要重新启动AMI连接线程刷新获得实时分机状态
    public static final int NET_CONNECTED_ERROR = 0x4002 ;
    //分机信息获得完毕，用于前台UI显示
    public static final int SIP_DATA_GET_COMPLETED = 0x4003 ;
    //分机信息sql查询完成
    public static final int EXTEN_DATA_QUERY_COMPLETED = 0x4004;
   //刷新分机状态标志
    public static final int EXTEN_STATUS_UPDATE = 0x4005 ;
    
    //数据库信息查询
    public static final int SQL_QUERY = 0x5000 ; 
    //AMI连接
    public static final int AMI_CONNECT = 0x5001 ;
	
    
    private Socket sqlQuery = null;
    private OutputStream sqlQueryOutput = null ;
    private BufferedReader sqlQueryBuffer = null ;   
    

    private Socket sipQuery = null;
    private OutputStream sipQueryOutput = null ;
    private BufferedReader sipQueryBuffer = null ; 
    
	public SipDataManager(Handler handler,String serverIp){
		
		String ser = serverIp ;
		if (ser.contains(":")){
			
			this.serverIp = ser.substring(0,ser.indexOf(":")) ;
		}else{
			this.serverIp = serverIp ; 
		}
		
		this.handler = handler ;
	}
	
	public void startGetExtenInfoByQuery(){
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				try {
				
					sqlQuery = new Socket(serverIp, SER_SQL_CON_PORT) ;
					sqlQueryOutput = sqlQuery.getOutputStream() ;
					sqlQueryBuffer = new BufferedReader(new InputStreamReader(sqlQuery.getInputStream(),"gb2312"),8192*2) ;
					
				} catch (UnknownHostException e) {
					
					netErrorStatus = true ;
					handerMessage(SQL_QUERY, NET_CONNECTING_ERROR);
				} catch (IOException e) {
					
					netErrorStatus = true ;
					handerMessage(SQL_QUERY, NET_CONNECTING_ERROR) ;
				}
				
				if (!netErrorStatus){
					//网络连接正常
					//建立连接
					initQueryMysql() ;
					
					try {
						Thread.sleep(500) ;
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					String content = null ;
					
					
					try {
						while((sqlQueryBuffer !=null)&&(content = sqlQueryBuffer.readLine())!= null){
							
							
							if (content.equals("Connect success!")){
								
								//如果SQL连接成功，则进行发送分机查询命令
								queryMysql(MySqlInfo.Sql.SELECT_EXTEN_ALL_INFO_SQL) ;
							}else if (!content.equals("Connect success!")&&!content.equals("")&&!content.equals("Query success! 1 row is effected!/n")){
								
								//获得分机信息
								Log.d("TAG", content) ;
								Data.getInstance().setExtenInfo(content) ;
							}else if (content.equals("")){
								
								handerMessage(SQL_QUERY,EXTEN_DATA_QUERY_COMPLETED) ;
								//启动AMI分机状态查询线程
								startGetExtenStatusByAMi() ;
								
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}).start() ;
	}
	
	/**
	 * 初始化连接查询，建立连接
	 */
	private void initQueryMysql(){
		
		try {
			
			sqlQueryOutput.write(MySqlInfo.Sql.MYSQL.getBytes("utf-8")) ;
		} catch (Exception e) {
			
			//网络连接失败
			handerMessage(SQL_QUERY,NET_CONNECTING_ERROR) ;
			netErrorStatus = true ;
		}
	}
	
	
	/**
	 * 根据sql进行相应的数据库查询
	 * @param sql
	 */
	public void queryMysql(String sql){
		
		try {
			
			sqlQueryOutput.write(sql.getBytes("gb2312")) ;
			byte bytes[] = sql.getBytes("utf-8") ;
			Log.i("TAG", "EnCode---------->" + bytes) ;
			
		} catch (Exception e) {
			
			//网络连接失败
			handerMessage(SQL_QUERY,NET_CONNECTED_ERROR) ;
			netErrorStatus = true  ;
		}
	}
	
	//用于刷新前端数据
	private void handerMessage(int what , int obg){
		
		Message msg = handler.obtainMessage() ;
		msg.what = what ;
		msg.obj = obg ;
		msg.sendToTarget() ;
	}
	
	/**
	 * 启动获取AMI分机实时状态的函数
	 */
	private void startGetExtenStatusByAMi(){
		
		SipQuery amiQuery = new SipQuery(handler) ;
		amiQuery.setConnectInfo(serverIp, SER_AMI_CON_PORT) ;
		amiQuery.start() ;
	}
	
	
	
}
