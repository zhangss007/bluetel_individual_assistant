package com.bluetel.android.app.individual_assistant;

import java.util.List;
import java.util.Map;

import com.bluetel.android.app.individual_assistant.data.Data;
import com.bluetel.android.app.individual_assistant.data.SipDataManager;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContacterActivity extends Activity{

	private ContactersAdapter contactsAdapter = null ;
	private ExpandableListView contactsView ;
	private TextView title , netStatus ;
	
	private List<Map<String,Object>> mdepartList ;
	/**
	 * 模拟分机数据信息
	 */
	private Map<String,com.bluetel.android.app.individual_assistant.data.Depart> mDepartMap ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
//		//设置Activity全屏显示
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN) ;
		//设置Activity竖屏显示
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) ;
		setContentView(R.layout.contacts_layout) ;
		findView() ;
		
		final Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				if (msg.what == SipDataManager.SQL_QUERY){
					
					if ((Integer)msg.obj == SipDataManager.EXTEN_DATA_QUERY_COMPLETED){
						
						//获得部门信息
						mdepartList = Data.getInstance().getMdepartList() ;
					}else if ((Integer)msg.obj == SipDataManager.NET_CONNECTING_ERROR){
						//网络连接错误
						netStatus.setText("网络连接错误") ;
					}
				}else if (msg.what == SipDataManager.AMI_CONNECT){
					
					if ((Integer)msg.obj == SipDataManager.SIP_DATA_GET_COMPLETED){
						
					    //获得部门分机信息
						netStatus.setText("网络连接ok...") ;
						Log.i("TAG", "分机信息获得完毕了") ;
						mDepartMap = Data.getInstance().getmDepartMap() ;
						if (mDepartMap != null && mdepartList != null){
							
							contactsAdapter= new ContactersAdapter(ContacterActivity.this) ;
							contactsView.setAdapter(contactsAdapter) ;
							contactsAdapter.notifyDataSetInvalidated() ;
							expandGroup(mdepartList.size()) ;
							
						}
					}else if ((Integer)msg.obj == SipDataManager.EXTEN_STATUS_UPDATE){
						
						if (contactsAdapter != null){
							
							contactsAdapter.notifyDataSetChanged() ;
							expandGroup(mdepartList.size()) ;
						}

					}else if ((Integer)msg.obj == SipDataManager.NET_CONNECTED_ERROR){
						
						//网络连接错误
						netStatus.setText("网络连接错误") ;
					}
				}
			}
			
			
		} ;
		
		
		SipDataManager dataManager = new SipDataManager(handler) ;
		dataManager.startGetExtenInfoByQuery() ;
		
//		new Thread(){
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				super.run();
//				
//				Cursor cur = getContentResolver().query(  
//		                ContactsContract.Contacts.CONTENT_URI,  
//		                null,  
//		                null,  
//		                null,  
//		                ContactsContract.Contacts.DISPLAY_NAME  
//		                        + " COLLATE LOCALIZED ASC");  
//		        // 循环遍历  
//		        if (cur.moveToFirst()) {  
//		            int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);  
//		  
//		            int displayNameColumn = cur  
//		                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);  
//		  
//		            do {  
//		            	Contact contact = new Contact() ;
//		                // 获得联系人的ID号  
//		                String contactId = cur.getString(idColumn);  
//		                // 获得联系人姓名  
//		                String disPlayName = cur.getString(displayNameColumn);  
//		                  
//		               
//		                
//		                // 查看该联系人有多少个电话号码。如果没有这返回值为0  
//		                int phoneCount = cur  
//		                        .getInt(cur  
//		                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));  
//		                Log.i("username", disPlayName);  
//		                contact.setName(disPlayName) ;
//		                if (phoneCount > 0) {  
//		                    // 获得联系人的电话号码  
//		                    Cursor phones = getContentResolver().query(  
//		                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
//		                            null,  
//		                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID  
//		                                    + " = " + contactId, null, null);  
//		                    if (phones.moveToFirst()) {  
//		                        do {  
//		                            // 遍历所有的电话号码  
//		                            String phoneNumber = phones  
//		                                    .getString(phones  
//		                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));  
//		                            String phoneType = phones  
//		                                    .getString(phones  
//		                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));  
//		                            Log.i("phoneNumber", phoneNumber);  
//		                            Log.i("phoneType", phoneType);  
//		                            contact.setNumber(phoneNumber) ;
//		                        } while (phones.moveToNext());   
//		                    }  
//		                } 
//		                if (contact.getName() != null && contact.getNumber() != null){
//		                	
//		                	contacts.add(contact) ;
//		                }
//		                
//		                Log.i("TAG", ".......................................................") ;
//		            } while (cur.moveToNext());  
//		            
//		        }  
//		        if (!contacts.isEmpty()){
//		        	
//		        	Log.i("TAG", "联系人已经遍历完成。。。。 人数为--------" + contacts.size()) ;
//		        	Message message = handler.obtainMessage() ;
//		        	message.what = 0x0234 ;
//		        	message.obj = CONTACT_OK ;
//		        	message.sendToTarget();
//		        }else {
//		        	
//		        	Log.i("TAG", "联系人已经遍历完成。。。。 人数为00000--------") ;
//		        }
//			}
//		
//			
//		}.start() ;
	
		
		
	
    }  

	
	//设置默认展开
	private void expandGroup(int size){
		
		for (int i = 0 ; i < size ; i++)
			contactsView.expandGroup(i);
	}
	
	private void findView(){
		
		contactsView = (ExpandableListView)findViewById(R.id.contact_view) ;
		title = (TextView)findViewById(R.id.title) ;
		netStatus = (TextView)findViewById(R.id.net_status) ;	
		
		title.setText(getResources().getString(R.string.menu_title_contacter)) ;
		
		contactsView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				//expandGroup(mdepartList.size()) ;
				return false;
			}
		}) ;
	}
	
	
	public class ContactersAdapter extends BaseExpandableListAdapter{

		private LayoutInflater inflater ;
		public ContactersAdapter(Context context) {
			// TODO Auto-generated constructor stub
			inflater = LayoutInflater.from(context) ;
		}
		
		
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			if (mDepartMap.get(mdepartList.get(groupPosition).get("Depart")) == null)
				return null ;
			return mDepartMap.get(mdepartList.get(groupPosition).get("Depart")).getcDepartList().get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public View getChildView(final int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewChildHoder childHoder = null ;
			if (convertView == null){
				
				childHoder = new ViewChildHoder() ;
				convertView = inflater.inflate(R.layout.contact_child_item, null) ;
				convertView.setTag(childHoder) ;
			}else {
				
				childHoder = (ViewChildHoder) convertView.getTag() ;
			}
			childHoder.extenName = (TextView)convertView.findViewById(R.id.exten_name) ;
			childHoder.extenNumber = (TextView)convertView.findViewById(R.id.exten_number) ;
			childHoder.chat = (ImageView)convertView.findViewById(R.id.chat_right) ;
			if(mDepartMap.get(mdepartList.get(groupPosition).get("Depart")) != null){
				
				final String extenName = mDepartMap.get(mdepartList.get(groupPosition).get("Depart")).getcDepartList().get(childPosition).getName() ;
				final String extenNumber = mDepartMap.get(mdepartList.get(groupPosition).get("Depart")).getcDepartList().get(childPosition).getNumber();
				String status = com.bluetel.android.app.individual_assistant.util.ExtenStatus.getExtenStatus(mDepartMap.get(mdepartList.get(groupPosition).get("Depart")).getcDepartList().get(childPosition).getStatus()) ;
				
				childHoder.extenName.setText(extenName) ;
				childHoder.extenNumber.setText(extenNumber + "[" + status +"]") ;
				
				childHoder.chat.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(ContacterActivity.this, extenName + "[" + extenNumber + "]", Toast.LENGTH_SHORT).show() ;
						Intent intent = new Intent(ContacterActivity.this, ChatActivity.class) ;
						Bundle bundle = new Bundle() ;
						bundle.putString("ExtenName", extenName) ;
						bundle.putString("ExtenNumber", extenNumber) ;
						bundle.putString("Depart", mdepartList.get(groupPosition).get("Depart").toString()) ;
						intent.putExtras(bundle) ;
						startActivity(intent) ;
					}
				}) ;
				
			}
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			if (mDepartMap.get(mdepartList.get(groupPosition).get("Depart")) == null)
				return 0 ;
			return mDepartMap.get(mdepartList.get(groupPosition).get("Depart")).getcDepartList().size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return mdepartList.get(groupPosition).get("Depart");
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return mdepartList.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			
			ViewGroupHoder groupHoder = null ;
			if (convertView == null){
				
				groupHoder = new ViewGroupHoder() ;
				convertView = inflater.inflate(R.layout.connect_group_item, null) ;
				convertView.setTag(groupHoder) ;
			}else {
				
			    groupHoder = (ViewGroupHoder) convertView.getTag() ;
			}
			
			groupHoder.departName = (TextView)convertView.findViewById(R.id.depart) ;
			groupHoder.departName.setText((String)mdepartList.get(groupPosition).get("Depart")) ;
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return false;
		}
		
		
	}
	
	
	static class ViewGroupHoder {
		
		TextView departName ;
	}
	
	static class ViewChildHoder {
		
		TextView extenName ;
		TextView extenNumber ;
		ImageView chat ;
	}
	
	

	
}
