package com.example.locationbased;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class SendMessageService extends IntentService {

	public static String TAG="SendMessageService";
	String emailIdOfReceiver;
	String message;
	long timeOfMessage;
	public SendMessageService() {
		super(TAG);

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		Log.d(TAG, "OnHandleIntent");
emailIdOfReceiver=intent.getStringExtra("email_id_receiver");
message=intent.getStringExtra("message");
timeOfMessage=intent.getLongExtra("time_of_message", 0);
String action=intent.getStringExtra("action");
Log.d(TAG, "Emailid is"+LocationData.emailId+"  passwrd is"+LocationData.password);
Log.d(TAG, "Receiver emailid is"+emailIdOfReceiver+"  message is"+message);
List<NameValuePair> values =new ArrayList<NameValuePair>();
values.add(new BasicNameValuePair("email_id", LocationData.emailId));
values.add(new BasicNameValuePair("password",LocationData.password));
values.add(new BasicNameValuePair("email_id_receiver", emailIdOfReceiver));
values.add(new BasicNameValuePair("message",message));
values.add(new BasicNameValuePair("action",action));
String response = NetworkHelper.postRequestAndGetResponse(LocationData.url, values);
		if(response==null)
			Log.d(TAG, "Could'nt send message");
		else if (response=="invalid")
			Log.d(TAG, "Invalid username/password");
		else
			{Log.d(TAG, "Message has been sent");
			Log.d(TAG, "Response is"+response);
			}
	}

}
