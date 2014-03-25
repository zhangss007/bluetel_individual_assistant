package com.bluetel.android.app.individual_assistant.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Sip消息接收以及Ami命令控制Socket通信类
 * @author zss
 *
 * 2013-8-5
 * bluetel-android-app-broadcast
 */
public class SipQuery extends Thread {


	private final String TAG = "TAG" ;
	public static final int SIP_THREAD_ID = 0x123 ;
	
	private Handler handler ;
	
	/**
	 * 声明一个Socket，用于进行与服务端建立连接,获得分机状态和发送Ami命令
	 */
	private Socket sipSocket ;
	/**
	 * 数据输出定义
	 */
	private OutputStream os = null ;
	/**
	 * 数据输入流定义
	 */
	private BufferedReader br = null ;
	/**
	 * 服务端连接IP
	 */
	private String serIp ;
	/**
	 * 连接服务端端口
	 */
	private int serPort ;
	
	/**
	 * 网络连接状态
	 */
	private boolean netErrorStatus = false;
	
	private Map<String,String> mMap = new HashMap<String,String>() ;
	
	public SipQuery(Handler handler) {
		
		super();
		this.handler = handler;
	}
	
	/**
	 * 设置连接IP,连接端口
	 * @param serverIp   连接IP
	 * @param serverPort 连接端口
	 */
	public void setConnectInfo(String serverIp , int serverPort){
		
		this.serIp = serverIp ;
		this.serPort = serverPort ;
	}

	
	
	public boolean getNetStatus() {
		
		return netErrorStatus;
	}

	@Override
	public void run() {
		
		super.run();
		
		try {
			
			sipSocket = new Socket(serIp, serPort) ;
			os = sipSocket.getOutputStream() ;
			br = new BufferedReader( new InputStreamReader(sipSocket.getInputStream()),8192*2);
			
		} catch (NumberFormatException e) {
			
			//网络连接错误,登录过程失败
			handlerMessage(SipDataManager.NET_CONNECTED_ERROR) ;
			netErrorStatus = true ;
			e.printStackTrace();
			
		} catch (UnknownHostException e) {
			
			//网络连接错误,登录过程失败
			handlerMessage(SipDataManager.NET_CONNECTED_ERROR) ;
			netErrorStatus = true ;
			e.printStackTrace();
			
		} catch (IOException e) {
			
			//网络连接错误,登录过程是吧
			handlerMessage(SipDataManager.NET_CONNECTED_ERROR) ;
			netErrorStatus  = true ;
			e.printStackTrace();
			
			
		}
		
		if (!netErrorStatus){
			
			try {
				String content = null ;
				//建立常连接，循环接收服务端分机状态信息
				while((content = br.readLine()) != null){
					
					if (content.contains("Asterisk Call Manager")){
						
							os.write(("Action: login\r\nUsername: phpagi\r\nSecret: phpagi\r\n\r\n")
									.getBytes("utf-8"));
						
					}else if (content.contains("Authentication accepted")){
						
							os.write(("Action: sippeers\r\n\r\naction: CoreShowChannels\r\n\r\n")
									.getBytes("utf-8"));
					}
					
					//Log.i("TAG", "接收数据为------>" + content ) ;
					dealTheExtenInfoStatus(content) ;
				}
			} catch (IOException e) {
				
				//网络连接失败
				handlerMessage(SipDataManager.NET_CONNECTED_ERROR) ;
				netErrorStatus = true ;
			}
			
		}
	}
	
	/**
	 * sip消息数据解析
	 * @param content
	 */
	private void dealTheExtenInfoStatus(String content) {

		String name , num , status , bchan;
		String uid = null ;
		if (!content.equals("")){
			
			String key = null ;
			String value = null ;
			int index = 0 ;
			index = content.indexOf(":") ;
			if (index != 0 && index != -1){
				
				key = content.substring(0, index) ;
				value = content.substring(index + 2, content.length()) ;
				
//				Log.i(TAG,"KEY ------->" + key ) ;
//				Log.i(TAG, "VALUE------>" + value) ;
//				
				
				mMap.put(key, value) ;
				
			}
		}else {
			
			if (mMap.containsKey("Event")){
				
				if (mMap.containsValue("PeerStatus")){
					                         
					name = mMap.get("Peer") ;
					status = mMap.get("PeerStatus") ;
					uid = mMap.get("Uniqueid") ;
					setStatusByChannel(name,status,"","",uid) ;
					//Log.i("TAG", "Peer-->" + name + "PeerStatus-->" + status) ;
				}else if (mMap.containsValue("PeerEntry")) {
					
					name = mMap.get("ObjectName") ;
					status = mMap.get("Status") ;
					addSip(name,status) ;
					//Log.i("TAG", "PeerEntry-->" + name + "Status-->" + status) ;
				}else if (mMap.containsValue("PeerlistComplete")){
					
					handlerMessage(SipDataManager.SIP_DATA_GET_COMPLETED) ;
				}else if (mMap.containsValue("CoreShowChannelsComplete")){
					
					Log.i("TAG","Co reShowChannelsComplete") ;
					//handlerMessage(SipDataManager.SIP_DATA_GET_COMPLETED) ;

				}else if (mMap.containsValue("Newstate") ||mMap.containsValue("CoreShowChannel")){
					
					String bridgeChan = null ;
					num = mMap.get("ConnectedLineNum") ;
					if (0 == num.length())
						num = mMap.get("Extension") ;
					name = mMap.get("Channel") ;
					status = mMap.get("ChannelStateDesc") ;
					uid = mMap.get("Uniqueid") ;
					if (uid == null){ 
						
						uid = mMap.get("UniqueID") ;
					}
					bridgeChan = mMap.get("BridgedChannel") ;
					if (bridgeChan == null){
						
						setStatusByChannel(name, status, num, "", uid) ;
					}else {
						
						setStatusByChannel(name, status, num, bridgeChan, uid) ;
					}
					
				}else if (mMap.containsValue("Hangup")){
					
					name = mMap.get("Channel") ;
					uid = mMap.get("Uniqueid") ;
					setStatusByChannel(name,"Hangup","","",uid) ;
				}else if (mMap.containsValue("Bridge")){
					
					name = mMap.get("Channel1") ;
					status = mMap.get("Channel2") ;
					uid = mMap.get("Uniqueid1") ;
					String cid1 , cid2 ;
					cid1 = mMap.get("CallerID1") ;
					cid2 = mMap.get("CallerID2") ;
					setStatusByChannel(name,"Up",cid2,status,uid) ;
					uid = mMap.get("Uniqueid2") ;
					setStatusByChannel(status,"Up",cid1,name,uid) ;
				}else if (mMap.containsValue("Dial")){

					name = mMap.get("Channel") ;
					status = mMap.get("Dialstring") ;
					uid = mMap.get("Uniqueid") ;
					if (mMap.containsKey("Destination")){
						bchan = mMap.get("Destination") ;
						setStatusByChannel(name,"Ring",status,"",uid) ;
						status = mMap.get("CallerIDNum") ;
						setStatusByChannel(bchan,"Ringing",status,"",uid) ;
						//Log.i("TAG", "Bridge-->" + name + ":" + "Ring") ;
						//Log.i("TAG", "Dial-->" + bchan + ":" + "Ringing") ;
					}

				}else if (mMap.containsValue("Destination")){
					
					
				}else if (mMap.containsValue("Destination")){
					
					
				}else if (mMap.containsValue("MeetmeLeave")){
					
					
				}else if (mMap.containsValue("MeetmeEnd")){
					
					
				}else if (mMap.containsValue("MeetmeList")){
					
					
				}
			}
			mMap.clear() ;
		}
	}

	private void addSip(String number, String status) {
		
		Exten mExten = findCurrentExten(number) ;
		if (mExten != null){
			
			Data.getInstance().setDate(mExten, status) ;
		}
	}

	private void setStatusByChannel(String channel , String status , String number ,
			String bridgeChan , String uid) {
		
		
		String chan , mDepart;
    	int pos = channel.indexOf("/") ;
    	chan = channel.substring(pos + 1, channel.length()) ;
    	if (chan.contains("-")){
    		
    		pos = chan.indexOf("-") ;
    		chan = chan.substring(0, pos) ;
    	}
    	Data data = Data.getInstance() ;
    	mDepart = data.getDepartByNumber(chan) ;
    	if (mDepart != null){
    		
    		//获得更新前的状态，通过部门，分机号获得更新前状态
    		int old_sta = data.getCurrentDepartExtenStatus(mDepart, chan) ;
    		int sta = data.getStatus(status) ;
			//更新当前分机状态
			data.updateCurrentExtenInfo(chan, sta,number ,mDepart,channel,bridgeChan,uid) ;
    		//进行比较原来分机状态与当前分机状态是否相同，分机状态相同则不更新状态
    		//不相同则更新当前状态
    		if(old_sta != sta){
    		
    			handlerMessage(SipDataManager.EXTEN_STATUS_UPDATE) ;
    		}
    	}
	}

	private void handlerMessage(int status){
		
		Message message = handler.obtainMessage() ;
		message.what = SipDataManager.AMI_CONNECT ;
		message.obj = status ;
		message.sendToTarget() ;
	}
	
	
    private Exten findCurrentExten(String exten){
    	
    	Exten cExten = null ;
    	Data data = Data.getInstance() ;
    	for (int i = 0 ; i < data.getExtenInfoList().size() ; i++){
    		
    		if (exten.equals(data.getExtenInfoList().get(i).getNumber())){
    			
    			cExten =  data.getExtenInfoList().get(i) ;
    			break ;
    		}
    	}
    	
    	return cExten ;
    }
    
   /**
    * 判断sipSocket连接状态
    * @return
    */
    public boolean isconnect(){
    	
    	return sipSocket.isConnected() ;
    }
   
    /**
     * 关闭Socket连接
     */
    public void closeConnect() throws IOException{
    	  	
    	sipSocket.close() ;
    }
    
    
    
    
}
