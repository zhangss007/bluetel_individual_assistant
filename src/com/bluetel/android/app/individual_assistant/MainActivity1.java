package com.bluetel.android.app.individual_assistant;

import static android.content.Intent.ACTION_MAIN;

import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCall.State;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.core.LinphoneCore.RegistrationState;

import com.bluetel.android.app.individual_assistant.linphone.LinphoneManager;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneSimpleListener.LinphoneOnCallStateChangedListener;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneSimpleListener.LinphoneOnMessageReceivedListener;
import com.bluetel.android.app.individual_assistant.linphone.LinphoneSimpleListener.LinphoneOnRegistrationStateChangedListener;
import com.bluetel.android.app.individual_assistant.service.MainService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity1 extends Activity implements OnClickListener{

	private Button connects , capture , record ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.acticity_main1) ;
		findViews() ;
	}

	private void findViews(){
		
		connects = (Button)findViewById(R.id.connect_btn) ;
		capture = (Button)findViewById(R.id.capture_upload_btn) ;
		record = (Button)findViewById(R.id.record_upload_btn) ;
		
		connects.setOnClickListener(this) ;
		capture.setOnClickListener(this) ;
		record.setOnClickListener(this) ;
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.connect_btn:
			startActivity(ContacterActivity.class) ;
			break;
		case R.id.capture_upload_btn:
			startActivity(CaptureActivity.class) ;
			break ;
		case R.id.record_upload_btn:
			startActivity(RecordActivity.class) ;
			break ;
		default:
			break;
		}
	}
	
	private void startActivity(Class<?> clazz){
		
		Intent intent = new Intent(MainActivity1.this,clazz) ;
		startActivity(intent) ;
	}

}
