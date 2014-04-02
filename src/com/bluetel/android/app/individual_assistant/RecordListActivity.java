package com.bluetel.android.app.individual_assistant;

import java.util.ArrayList;
import java.util.List;

import com.bluetel.android.app.individual_assistant.adapter.RecordListAdapter;
import com.bluetel.android.app.individual_assistant.bean.Record;
import com.bluetel.android.app.individual_assistant.bean.UploadFile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 录音记录列表Activity
 * @author zss
 *
 * 2014-3-12
 * bluetel-android-app-individual-assistant
 */
public class RecordListActivity extends Activity{

	
	private Button leftbackBtn ;
	private ListView records ;
	private List<UploadFile> recordList = null;
	private String TAG = "TAG" ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_list_layout) ;
		findViews() ;
	}
	
	private void initViews(){
		
		recordList = MainActivity.getInstance().getUploadFiles("625") ;
		
		if (recordList != null){
			
			RecordListAdapter adapter  = new RecordListAdapter(RecordListActivity.this, recordList) ;
			records.setAdapter(adapter) ;
			adapter.notifyDataSetInvalidated() ;
		}else {
			
			Toast.makeText(RecordListActivity.this, "录音记录为空", Toast.LENGTH_SHORT).show();
		}
	}

	private void findViews(){
		
		leftbackBtn = (Button)findViewById(R.id.left_btn) ;
		records = (ListView)findViewById(R.id.records) ;
		leftbackBtn.setOnClickListener(backBtnListener) ;

	}
	
	OnClickListener backBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			MainActivity.getInstance().startViewByActivity(RecordActivity.class, 0) ;
		}
	};
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "------onDestory---------") ;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "------onPause---------") ;
		
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i(TAG, "------onRestart---------") ;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "------onResume---------") ;
		initViews() ;
		
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG, "------onStart---------") ;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "------onStop---------") ;
	}
	
	
	
}
