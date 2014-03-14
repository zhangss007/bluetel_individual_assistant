package com.bluetel.android.app.individual_assistant;

import java.util.ArrayList;
import java.util.List;

import com.bluetel.android.app.individual_assistant.adapter.RecordListAdapter;
import com.bluetel.android.app.individual_assistant.bean.Record;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

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
	private List<Record> recordList  = new ArrayList<Record>() ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_list_layout) ;
		findViews() ;
	}

	private void findViews(){
		
		leftbackBtn = (Button)findViewById(R.id.left_btn) ;
		records = (ListView)findViewById(R.id.records) ;
		leftbackBtn.setOnClickListener(backBtnListener) ;
		
		for (int i = 0 ; i < 10 ;i ++){
			
			Record record = new Record() ;
			record.setTimer("2014-03-12 14:53:59") ;
			record.setRecordTimer("00:00") ;
			
			recordList.add(record) ;
		}
		RecordListAdapter adapter  = new RecordListAdapter(RecordListActivity.this, recordList) ;
		records.setAdapter(adapter) ;
		adapter.notifyDataSetInvalidated() ;
	}
	
	OnClickListener backBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			MainActivity.getInstance().startViewByActivity(RecordActivity.class, 0) ;
		}
	};
	
}
