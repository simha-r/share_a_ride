package com.example.locationbased;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MessageSender extends BroadcastReceiver {

	static String TAG="MessageSender";
	@Override
	public void onReceive(Context context, Intent intent) {
      
		Log.d(TAG, "OnReceive");
		
		Intent intent1=new Intent("com.example.locationbased.action.insertmessage");
		intent1.putExtras(intent);
		Intent intent2=new Intent("com.example.locationbased.action.sendmessage");
		intent2.putExtras(intent);
		context.startService(intent1);
		context.startService(intent2);
		
	}
	

}
