package com.bluetel.android.app.individual_assistant.data;

/**
 * Ami命令字符信息类
 * @author zss
 *
 * 2013-8-5
 * bluetel-android-app-broadcast
 */
public class Ami {

	//用key-value标记AMI命令
	
	/**
	 * Key 表示Ami命令的key值
	 * @author zss
	 *
	 * 2013-8-5
	 * bluetel-android-app-broadcast
	 */
	public static class Key {
		
		public static final String ACTION = "Action: " ;
		public static final String CHANNEL = "Channel: " ;
		public static final String EXTEN = "Exten: " ;
		public static final String CALLERID = "Callerid: " ;
		public static final String APPLICATION = "Application: " ;
		public static final String DATA = "Data: " ;
		public static final String PRIORITY = "Priority: " ;
		public static final String CONTEXT = "Context: " ;
		public static final String VARIABLE = "Variable: " ;
	}
	

	/**
	 * Value 表示AMI命令的value值
	 * @author zss
	 *
	 * 2013-8-5
	 * bluetel-android-app-broadcast
	 */
	public static class Value{
		
		public static final String ORIGINATE = "Originate" ;
		public static final String SIP = "SIP/" ;
		public static final String CHANSPY = "Chanspy" ;
		public static final String BRIDGE = "Bridge" ;
		public static final String HANDUP = "Hangup" ;
		public static final String ONE = "1" ;
		public static final String PAGE = "Page" ;
		public static final String BROADCAST = "broadcast" ;
		public static final String EXTEN_INTERCOM = "exten_intercom" ;
		/**
		 * 插讲标志
		 */
		public static final String BQES = ",BqES" ;
		/**
		 * 监听标志
		 */
		public static final String DQES = ",dqES" ;
		/**
		 * 拆讲标志
		 */
		public static final String  TX= ",Tx" ;
		/**
		 * 分区喊话
		 */
		public static final String SR = ",sr," ;
		/**
		 * 分区对讲
		 */
		public static final String  DSR= ",dsr," ;
		
		public static final String ZZZ = "000" ;
		/**
		 * 广播音乐文件名称
		 */
		public static final String SOUND = "SOUND=" ;
		/**
		 * 广播播放时间
		 */
		public static final String PLAY_TIMES = ",PLAY_TIMES=" ;
		
		/**
		 * 换行符
		 */
		public static final String LINEFEED = "\r\n" ;
		public static final String SR_ = ",sr" ;
		public static final String LOCAL = "LOCAL/000@broadcastfiles" ;
		public static final String ID = "ID=" ;
	}
}
