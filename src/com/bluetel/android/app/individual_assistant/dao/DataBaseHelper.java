package com.bluetel.android.app.individual_assistant.dao;

import com.bluetel.android.app.individual_assistant.constant.FileNameConstant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * @author zss
 *
 * 2014-3-19
 * bluetel-android-app-individual-assistant
 */
public class DataBaseHelper extends SQLiteOpenHelper{

	private static final String name = "IndividualAssistant.db" ;  //数据库名称
	private static final int version = 1 ; //数据库版本
	
	//数据库的使用线程数量
	private int openCount = 0;
	
	private static DataBaseHelper dataBaseHelper = null ;
	
	/**
	 * 单例模式实现
	 * @param context
	 * @return
	 */
	public static synchronized DataBaseHelper getInstance(Context context){
		
		if (dataBaseHelper == null){
			
			dataBaseHelper = new DataBaseHelper(context) ;
		}
		return dataBaseHelper ;		
	}
	
	private DataBaseHelper(Context context){
		super(context, name, null, version);
		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		createDepartInfoTable(db) ;
		createExtensInfoTable(db) ;
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	/**
	 * 创建部门表
	 * @param db
	 */
	private void createDepartInfoTable(SQLiteDatabase db){
		
		String sql = "CREATE TABLE[" + FileNameConstant.TB_DEPART_INFO + "](" +
                     "_id interger primary key autoincrement," + 
                     "uniqueId text," + 
				     "name text" +
                     ")";
		db.execSQL(sql) ;
	}
	
	/**
	 * 创建分机信息表
	 * @param db
	 */
	private void createExtensInfoTable(SQLiteDatabase db){
		
		String sql = "CREATE TABLE[" + FileNameConstant.TB_EXTENS_INFO + "](" +
		             "_id interger primary key autoincrement," +
		             "uniqueId text," +
				     "name text," +   //字段  分机
		             "number text," + //字段 分机号码
				     "exten text," +
		             "depart text," + //字段 分机所属部门
				     "chan text," +  //字段  通道
		             "bchan text," + 
				     "uid text," +
		             "status interger" +
				     ")"; 
		db.execSQL(sql) ;
	}

	
	
	
	/**
	 * 关闭数据库不使用db.close() 
	 */
	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		if (openCount > 0){
			
			openCount -- ;			
		}else {
			
			dataBaseHelper = null ;
			super.close();
		}		
	}

	@Override
	public SQLiteDatabase getReadableDatabase() {
		// TODO Auto-generated method stub
		openCount ++ ;
		return super.getReadableDatabase();
	}

	@Override
	public SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
		openCount++ ;
		return super.getWritableDatabase();
	}
	
	
	
	
}
