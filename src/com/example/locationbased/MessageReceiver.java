package com.example.locationbased;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MessageReceiver extends BroadcastReceiver {

	static String TAG="MessageReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "OnReceive");
		
		Log.d(TAG, intent.getStringExtra("email_id_receiver"));
		Log.d(TAG, intent.getStringExtra("first_name"));
		Log.d(TAG, intent.getStringExtra("message"));
		Log.d(TAG, intent.getStringExtra("direction"));
		Log.d(TAG, "time is "+intent.getLongExtra("time_of_message", 1));
		
		
		
		
		Intent intent1=new Intent("com.example.locationbased.action.insertmessage");
		intent1.putExtras(intent);
		Log.d(TAG, "Action of intent1 is "+intent1.getAction());
		Log.d(TAG, "email in intnt1 is"+intent1.getStringExtra("email_id_receiver"));
     context.startService(intent1);
	
		
	}

}
