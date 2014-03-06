package com.bluetel.android.app.individual_assistant;

import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ContacterActivity extends Activity implements OnClickListener{

	private List<Contact> contacts = new ArrayList<Contact>() ;
	private ContactsAdapter contactsAdapter = null ;
	private ListView contactsView ;
	private Button localContactHead ,sipContactHead ;
	private static final String CONTACT_OK = "CONTACT_OK" ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
		//设置Activity全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN) ;
		//设置Activity竖屏显示
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) ;
		setContentView(R.layout.contacts_layout) ;
		findView() ;
		
		final Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				if (msg.what == 0x0234){
					
					if (msg.obj == CONTACT_OK){
						
			        	contactsAdapter = new ContactsAdapter(ContacterActivity.this) ;
			        	contactsView.setAdapter(contactsAdapter) ;
			            contactsAdapter.notifyDataSetChanged() ;
					}
				}
			}
			
			
		} ;
		
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				
				Cursor cur = getContentResolver().query(  
		                ContactsContract.Contacts.CONTENT_URI,  
		                null,  
		                null,  
		                null,  
		                ContactsContract.Contacts.DISPLAY_NAME  
		                        + " COLLATE LOCALIZED ASC");  
		        // 循环遍历  
		        if (cur.moveToFirst()) {  
		            int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);  
		  
		            int displayNameColumn = cur  
		                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);  
		  
		            do {  
		            	Contact contact = new Contact() ;
		                // 获得联系人的ID号  
		                String contactId = cur.getString(idColumn);  
		                // 获得联系人姓名  
		                String disPlayName = cur.getString(displayNameColumn);  
		                  
		               
		                
		                // 查看该联系人有多少个电话号码。如果没有这返回值为0  
		                int phoneCount = cur  
		                        .getInt(cur  
		                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));  
		                Log.i("username", disPlayName);  
		                contact.setName(disPlayName) ;
		                if (phoneCount > 0) {  
		                    // 获得联系人的电话号码  
		                    Cursor phones = getContentResolver().query(  
		                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
		                            null,  
		                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID  
		                                    + " = " + contactId, null, null);  
		                    if (phones.moveToFirst()) {  
		                        do {  
		                            // 遍历所有的电话号码  
		                            String phoneNumber = phones  
		                                    .getString(phones  
		                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));  
		                            String phoneType = phones  
		                                    .getString(phones  
		                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));  
		                            Log.i("phoneNumber", phoneNumber);  
		                            Log.i("phoneType", phoneType);  
		                            contact.setNumber(phoneNumber) ;
		                        } while (phones.moveToNext());   
		                    }  
		                } 
		                if (contact.getName() != null && contact.getNumber() != null){
		                	
		                	contacts.add(contact) ;
		                }
		                
		                Log.i("TAG", ".......................................................") ;
		            } while (cur.moveToNext());  
		            
		        }  
		        if (!contacts.isEmpty()){
		        	
		        	Log.i("TAG", "联系人已经遍历完成。。。。 人数为--------" + contacts.size()) ;
		        	Message message = handler.obtainMessage() ;
		        	message.what = 0x0234 ;
		        	message.obj = CONTACT_OK ;
		        	message.sendToTarget();
		        }else {
		        	
		        	Log.i("TAG", "联系人已经遍历完成。。。。 人数为00000--------") ;
		        }
			}
		
			
		}.start() ;
	
		
		
	
    }  

	
	private void findView(){
		
		localContactHead = (Button)findViewById(R.id.local_contacter_lv) ;
		sipContactHead   = (Button)findViewById(R.id.sip_contacter_lv) ;
		contactsView = (ListView)findViewById(R.id.contact_view) ;
		
		localContactHead.setOnClickListener(this) ;
		sipContactHead.setOnClickListener(this) ;
		localContactHead.setSelected(true) ;
	}
	
	public class ContactsAdapter extends BaseAdapter{

		private LayoutInflater inflater ;
		
		public ContactsAdapter(Context context){
			
			inflater = LayoutInflater.from(context) ;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return contacts.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return contacts.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHoder vHoder = null ;
			if (convertView == null ){
				vHoder = new ViewHoder() ;
				convertView = inflater.inflate(R.layout.contact_item, null ) ;
				convertView.setTag(vHoder) ;
			}else{
				
				vHoder = (ViewHoder)convertView.getTag() ;
			}
			if (contacts.get(position).getNumber()!= null && contacts.get(position).getName() != null ){
				
				vHoder.contactInfo = (TextView)convertView.findViewById(R.id.contacts_txt) ;
				vHoder.contactAddress = (TextView)convertView.findViewById(R.id.contact_address) ;
				vHoder.contactInfo.setText(contacts.get(position).getName()) ;
				vHoder.contactAddress.setText(contacts.get(position).getNumber()) ;
			}
			//
			return convertView;
		}
		
		
	}
	
	static class ViewHoder {
		
		TextView contactInfo ;
		TextView contactAddress ;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		resetContactHeadSelector() ;
		switch (v.getId()) {
		case R.id.local_contacter_lv:
			localContactHead.setSelected(true) ;
			break;
		case R.id.sip_contacter_lv:
			sipContactHead.setSelected(true) ;
			break ;
		default:
			break;
		}
	}
	
	private void resetContactHeadSelector(){
		
		localContactHead.setSelected(false) ;
		sipContactHead.setSelected(false) ;
	}
	
}
