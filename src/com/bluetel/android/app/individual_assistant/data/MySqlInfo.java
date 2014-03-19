package com.bluetel.android.app.individual_assistant.data;

/**
 * 数据查询SQL语句封装，以及mySql数据库查询状态
 * @author zss
 *
 * 2013-8-5
 * bluetel-android-app-broadcast
 */
public class MySqlInfo {

	/**
	 * 数据库查询状态
	 * @author zss
	 *
	 * 2013-8-5
	 * bluetel-android-app-broadcast
	 */
	public static class QueryStatus {
		
		/**
		 * 数据库连接成功标志
		 */
		public static final String SUCCESS = "SUCCESS" ;
		/**
		 * 数据库查询结束标志
		 */
		public static final String COMPLETE = "COMPLETE" ;
		/**
		 * 数据库连接失败标志
		 */
		public static final String ERROR = "ERROR" ;
		
	}
	
	/**
	 * 数据库SQL数据库查询语句封装 内部类
	 * @author zss
	 *
	 * 2013-8-5
	 * bluetel-android-app-broadcast
	 */
	public static class Sql{
		
		/**
		 * 数据库连接标志
		 */
		public static final String MYSQL = "MYSQL" ;
		
		/**
		 * 查询mysql数据库中分机的信息  ，包括分机号、坐席姓名、分机所属部门
		 */
		public static final String  SELECT_EXTEN_ALL_INFO_SQL = "select name,department,exten from broadcast.txl_employee;" ; 
		
		/**
		 * 获取对讲组号，对讲组名字  对讲组信息
		 */
		public static final String  SELECT_INTERCOM_SQL = "select number, name from broadcast.broadcast_intercom order by id;" ;
		
		/**
		 * 对讲组中分机信息
		 * select exten from broadcast.broadcast_intercom_member where name = '"+intercom+"' order by exten;
		 */
		public static final String SELECT_INTERCOM_NUMBERS_SQL = "select exten from broadcast.broadcast_intercom_member where name = '" ;
	    public static final String ORDER_BY_EXTEN_SQL = "' order by exten" ;
	    /**
	     * 查询摄像头分机信息SQL语句
	     */
	    public static final String QUERY_VIDEO = "select * from broadcast.video_station where termination in (select broadcast.sip_exten.exten from broadcast.sip_exten,broadcast.txl_employee where sip_exten.exten = txl_employee.exten and sip_exten.type=1) order by termination" ;
	    /**
	     * 在分区对讲前进行数据库的插入
	     */
	    public static final String PARTITION_BROAD_INSERT_SQL = "insert into broadcast.broadcastfiles_ontime set braod_time=NOW(),sound='%s1',braod_extens='%s2',adder='%s3'" ;
	    /**
	     * 在分区对讲前获得刚插入对讲分区的信息的id
	     */
	    public static final String PARTITION_BROAD_GET_ID_SQL = "select id from broadcast.broadcastfiles_ontime order by id desc limit 0 , 1 ;" ;
	}
}
