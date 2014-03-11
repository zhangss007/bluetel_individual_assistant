package com.bluetel.android.app.individual_assistant.util;


import com.bluetel.android.app.individual_assistant.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.TextView;

/**
 * 网络连接检测类
 * @author zss
 *
 * 2013-8-2
 * bluetel-android-app-broadcast
 */
public class NetWork {
	
	
	
	/**
	 * 判断网络状态
	 * @param context 上下文
	 * @return true 表示有网络，false表示没有网络
	 */
	public static boolean isNetworkAvailable(Context context) {
		
		
		//获得网络管理器状态
		ConnectivityManager connectivityManager =  (ConnectivityManager)context.
					getSystemService(Context.CONNECTIVITY_SERVICE) ;
		
		if (connectivityManager == null){
			
			return false ;
			
		}else{
			
			NetworkInfo[] info = connectivityManager.getAllNetworkInfo() ;
			
			if(info != null){
				
				for (NetworkInfo netWork : info){
					
					if (netWork.getState() == NetworkInfo.State.CONNECTED){
						
						return true ;
					}
				}
			}
		}
		return false ;
	}

	
	/**
	 * 如果网络连接失败启动网络设置界面
	 * @param context 连接上下文
	 */
	public static void setNetWork(final Context context){
		
		TextView msg = new TextView(context) ;
		msg.setText("当前没有可以使用的网络，请设置网络！") ;
		new AlertDialog.Builder(context)
			.setIcon(R.drawable.not).setTitle("网络提示")
			.setView(msg)
			.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					//跳转到网络设置界面
					context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)) ;
				}
			}).create().show() ;
	}
	

}
