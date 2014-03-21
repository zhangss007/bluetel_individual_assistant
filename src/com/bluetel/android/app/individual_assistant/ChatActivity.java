package com.bluetel.android.app.individual_assistant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.core.LinphoneChatRoom;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneChatMessage.State;


import com.bluetel.android.app.individual_assistant.adapter.ChatMsgAdapter;
import com.bluetel.android.app.individual_assistant.bean.ChatMsgEntity;
import com.bluetel.android.app.individual_assistant.linphone.ChatMessage;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 聊天界面
 * @author zss
 *
 * 2014-3-20
 * bluetel-android-app-individual-assistant
 */
public class ChatActivity extends Activity implements OnClickListener ,LinphoneChatMessage.StateListener{

	private TextView title ;
	private Button backBtn ;
	private String extenName = "";
	private String sipUri = "" ;
	private String extenNumber = "" ;
	private String depart = "" ;
	
	private Button messageSend ;
	private EditText message ;
	private int messagesFilterLimit = 0;
	
	private ListView chatMsgView ;
	
    private String[]msgArray = new String[]{"有大吗", "有！你呢？", "我也有", "那上吧", 
			"打啊！你放大啊", "你咋不放大呢？留大抢人头那！kkkkkkk",
			"不解释", "尼滚....","jkljkljklj","456456456","5454546","rtyrtrtrtyry"};
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
    
    private List<ChatMessage> messagesList = null; 
    
    private ChatMsgAdapter mChatMsgAdapter = null ;
    private final static int COUNT = 12;
    
    private LinphoneChatRoom chatRoom;
    
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
		setContentView(R.layout.chat_layout) ;
		Intent intent = getIntent() ;
		extenName = intent.getExtras().getString("ExtenName") ;
		extenNumber = intent.getExtras().getString("ExtenNumber") ;
		sipUri = intent.getExtras().getString("SipUri") ;
		depart = intent.getExtras().getString("Depart") ;
		Log.i("TAG", "SipUri--->" + sipUri ) ;
		findViews() ;
		init() ;
		dispalyChat(extenName) ;
		
        LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
		if (lc != null) {
			chatRoom = lc.createChatRoom(sipUri);
		}
	}	
	
	
	
	private void init (){
		
		for(int i = 0; i < COUNT; i++)
    	{
    		ChatMsgEntity entity = new ChatMsgEntity();
    		if (i % 2 == 0)
    		{
    			entity.setName("小黑");
    			entity.setComMsg(true);
    		}else{
    			entity.setName("人马");
    			entity.setComMsg(false);
    		}
    		
    		entity.setTxt(msgArray[i]);
    		mDataArrays.add(entity);
    	}
		mChatMsgAdapter = new ChatMsgAdapter(ChatActivity.this, mDataArrays) ;
		chatMsgView.setAdapter(mChatMsgAdapter) ;
		mChatMsgAdapter.notifyDataSetInvalidated() ;
		
	}
	
	private void findViews(){
		
		title = (TextView)findViewById(R.id.title) ;
		backBtn = (Button)findViewById(R.id.left_btn) ;
		chatMsgView = (ListView)findViewById(R.id.chat_message_list) ;
		
		backBtn.setVisibility(View.VISIBLE) ;
		//启动Activity时不自动弹出软键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) ;
		
		message = (EditText)findViewById(R.id.et_sendmessage) ;
		messageSend = (Button)findViewById(R.id.btn_send) ;
		messageSend.setOnClickListener(this) ;
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChatActivity.this.finish() ;
			}
		}) ;
	}
	
	private void invalidate(){
		
		refreshMessages();
	}
	
	private void refreshMessages() {

		messagesList = MainActivity.getInstance().getChatMessages(sipUri);
		if (messagesList != null && !messagesList.isEmpty()){
			
			Log.i("TAG", " 有聊天记录0哦。。。。。") ;
		}else {
			
			Log.i("TAG", " 暂时没 有聊天记录哦。。。。。") ;
		}
	}



	private void invalidate(int limit){
		
		
	}
	
	private void dispalyChat(String displayName){
		
		title.setText(extenName) ;
		if (messagesFilterLimit == 0){
			
			invalidate() ;
		}else {
			
			invalidate(messagesFilterLimit) ;
		}
	}


	 private  String getUTF8XMLString(String xml) {  
		    // A StringBuffer Object  
		    StringBuffer sb = new StringBuffer();  
		    sb.append(xml);  
		    String xmString = "";  
		    String xmlUTF8="";  
		    try {  
		    xmString = new String(sb.toString().getBytes("UTF-8"));  
		    xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");  
		    System.out.println("utf-8 编码：" + xmlUTF8) ;  
		    } catch (UnsupportedEncodingException e) {  
		    // TODO Auto-generated catch block  
		    e.printStackTrace();  
		    }  
		    // return to String Formed  
		    return xmlUTF8;  
		    }  
	 
	


	@Override
	public void onClick(View v) {

		
		sendTextMessage() ;
	}



	@Override
	public void onLinphoneChatMessageStateChanged(LinphoneChatMessage msg,
			State state) {
		// TODO Auto-generated method stub
		
	}
	
	public void onMessageReceived(final int id, LinphoneAddress from, final LinphoneChatMessage message) throws UnsupportedEncodingException {
		
		Log.i("TAG", "收到短信了 啊 。。。。。。。。。") ;
	}
	
	
	private void sendTextMessage() {
		LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
		boolean isNetworkReachable = lc == null ? false : lc.isNetworkReachable();
		
		if (chatRoom != null && message != null && message.getText().length() > 0 && isNetworkReachable) {
			String messageToSend = message.getText().toString();
			message.setText("");

			LinphoneChatMessage chatMessage = chatRoom.createLinphoneChatMessage(messageToSend);
			chatRoom.sendMessage(chatMessage, this);
		}
			
//			int newId = -1;
//			if (LinphoneActivity.isInstanciated()) {
//				newId = LinphoneActivity.instance().onMessageSent(sipUri, messageToSend);
//			}
//			
//			displayMessage(newId, messageToSend, String.valueOf(System.currentTimeMillis()), false, State.InProgress, messagesLayout);
//			scrollToEnd();
//		} else if (!isNetworkReachable && LinphoneActivity.isInstanciated()) {
//			LinphoneActivity.instance().displayCustomToast(getString(R.string.error_network_unreachable), Toast.LENGTH_LONG);
//		}
	}
}

