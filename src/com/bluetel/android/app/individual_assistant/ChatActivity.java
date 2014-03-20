package com.bluetel.android.app.individual_assistant;

import java.util.ArrayList;
import java.util.List;

import com.bluetel.android.app.individual_assistant.adapter.ChatMsgAdapter;
import com.bluetel.android.app.individual_assistant.bean.ChatMsgEntity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 聊天界面
 * @author zss
 *
 * 2014-3-20
 * bluetel-android-app-individual-assistant
 */
public class ChatActivity extends Activity{

	private TextView title ;
	private Button backBtn ;
	private String extenName = "";
	private String extenNumber = "" ;
	private String depart = "" ;
	
	
	private ListView chatMsgView ;
	
    private String[]msgArray = new String[]{"有大吗", "有！你呢？", "我也有", "那上吧", 
			"打啊！你放大啊", "你咋不放大呢？留大抢人头那！kkkkkkk",
			"不解释", "尼滚....","jkljkljklj","456456456","5454546","rtyrtrtrtyry"};
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
    private ChatMsgAdapter mChatMsgAdapter = null ;
    private final static int COUNT = 12;
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
		depart = intent.getExtras().getString("Depart") ;
		findViews() ;
		init() ;
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
		title.setText(extenName) ;
		backBtn.setVisibility(View.VISIBLE) ;
		//启动Activity时不自动弹出软键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) ;
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChatActivity.this.finish() ;
			}
		}) ;
	}
}

