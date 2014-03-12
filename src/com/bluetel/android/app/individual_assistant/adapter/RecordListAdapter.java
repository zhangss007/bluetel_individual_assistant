package com.bluetel.android.app.individual_assistant.adapter;

import java.util.List;

import com.bluetel.android.app.individual_assistant.R;
import com.bluetel.android.app.individual_assistant.bean.Record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 录音记录适配器 
 * @author zss
 *
 * 2014-3-12
 * bluetel-android-app-individual-assistant
 */
public class RecordListAdapter extends BaseAdapter{

	
	private List<Record> lists ;
	private LayoutInflater inflater ;
	public RecordListAdapter(Context context, List<Record> lists){
		
		inflater = LayoutInflater.from(context) ;
		this.lists = lists ;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		ViewHoder vHoder = null ;
		if (arg1 == null){
			
			vHoder = new ViewHoder() ;
			arg1 = inflater.inflate(R.layout.record_list_item_layout, null) ;
			arg1.setTag(vHoder) ;
		}else {
			
			vHoder = (ViewHoder)arg1.getTag() ;
		}
		return arg1;
	}

	static class ViewHoder {
		
		TextView recordTimer ;
	}
	
}
