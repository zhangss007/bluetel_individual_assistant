package com.bluetel.android.app.individual_assistant.adapter;

import java.util.List;

import com.bluetel.android.app.individual_assistant.R;
import com.bluetel.android.app.individual_assistant.bean.ChatMsgEntity;
import com.bluetel.android.app.individual_assistant.linphone.ChatMessage;

import android.R.bool;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatMsgAdapter extends BaseAdapter{

	
	public static interface IMsgViewType
	{
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}
	
	private List<ChatMessage> coo ;
	private Context context ;
	private LayoutInflater inflater = null ;
	
	public ChatMsgAdapter(Context context, List<ChatMessage> coo){
		
		this.context  = context ;
		inflater = LayoutInflater.from(context) ;
		this.coo = coo ;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return coo.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return coo.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ChatMessage entity = coo.get(arg0) ;
		boolean isComMsg = entity.isIncoming() ;
		ViewHoder vHoder = null ;
		if (vHoder == null){
			vHoder = new ViewHoder() ;
			if (isComMsg){
				
				arg1 = inflater.inflate(R.layout.chatting_item_msg_left, null) ;
			}else {
				
				arg1 = inflater.inflate(R.layout.chatting_item_msg_right, null) ;
			}
			arg1.setTag(vHoder) ;
			
		}else {
			
			vHoder = (ViewHoder) arg1.getTag() ;
		}
		
		vHoder.chatMsg = (TextView)arg1.findViewById(R.id.tv_chat_content) ;
		
		vHoder.chatMsg.setText(entity.getMessage()) ;
		
		return arg1;
	}
	
	static class ViewHoder {
		
		TextView chatMsg ;
	}

	
}
