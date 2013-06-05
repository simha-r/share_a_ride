package com.example.locationbased;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class ChatFragment extends SherlockFragment {

	public static String TAG = "ChatFragment";
	
	static Cursor messagesCursor;
	static List<Map> list;
	Map dataOfChat;
	TextView chatMessage;
	TextView timeOfMessage;
	Button sendMessage;
	EditText message;
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("emailId", emailId);
		outState.putString("firstName", firstName);
		
	}



	ChatFragmentInterface mcallback;
	ListView listView;
	
	String emailId;
	String firstName;
	SimpleAdapter adapter;
	String[] from={"message","time"};
	int[] to={R.id.chat_message,R.id.time_of_chat_message};
	
	IntentFilter notificationFilter = new IntentFilter(
			"com.example.locationbased.action.updateui");


	
	
			
	public interface ChatFragmentInterface
	{ 
		public Map getData();
		
	}
	
	
	BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			//Log.d(TAG, "Testing notification service in LocationEnterActivity "
				//	+ intent.getStringExtra("first_name"));
			Log.d(TAG, "received broadcast ..going to notify adapter");
			new GetMessages().execute(emailId,firstName);
			//playSound(); // To play a sound similar to notification sound
							// because we have disabled notifications
			//addButton(intent.getStringExtra("first_name"),
				//	intent.getStringExtra("message"));
		}
	};
	
	
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		mcallback=(ChatFragmentInterface) getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		notificationFilter.addAction(LocationData.ACTION_SEND_MESSAGE);
		notificationFilter.setPriority(1);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.show_chat_screen, null,false);
		chatMessage=(TextView) view.findViewById(R.id.chat_message);
		timeOfMessage=(TextView) view.findViewById(R.id.time_of_chat_message);
		sendMessage=(Button) view.findViewById(R.id.buttonSend);
	    listView=(ListView) view.findViewById(R.id.list_messages);
	    message=(EditText) view.findViewById(R.id.message);
	    
	    
	    
	     dataOfChat=mcallback.getData(); // have not initialized hashmap !!
	
	     if(dataOfChat==null)
	    	 {emailId=savedInstanceState.getString("emailId");
		    firstName = savedInstanceState.getString("firstName");
	    	 }
	     else
	     {  emailId=(String) dataOfChat.get("emailId");
		firstName=(String) dataOfChat.get("name");
	     }
		Log.d(TAG, "OnCreateView");
sendMessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG, "OnButtonClicked");
				String messageValue=message.getText().toString();
				
			hideKeyboard(message);
				Intent intent=new Intent("com.example.locationbased.action.sendmessage");
				intent.putExtra("email_id_receiver", emailId);
				intent.putExtra("message", messageValue);
				intent.putExtra("time_of_message", NetworkHelper.getCurrentTimeStamp());
				intent.putExtra("direction", "out");
			intent.putExtra("first_name", firstName);
			intent.putExtra("action", "inviteforride");
			
			/*Intent intentDb=new Intent("com.example.locationbased.action.message");
			intentDb.putExtra("email_id_receiver", emailId);
			intentDb.putExtra("message", messageValue);
			intentDb.putExtra("time_of_message", NetworkHelper.getCurrentTimeStamp());
			intentDb.putExtra("direction", "out");
		intentDb.putExtra("first_name", firstName);
		intentDb.putExtra("action", "inviteforride");
	*/
	getActivity().sendBroadcast(intent);	
		
	//	getActivity().startService(intentDb);  // intent to store sent msg in local database
	
			//getActivity().startService(intent);  // intent to send message to server to send to other user
			
				
						
			}
		});
		new GetMessages().execute(emailId,firstName);	
		return view;
	}
	
	
	

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().unregisterReceiver(notificationReceiver);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
		
		getActivity().registerReceiver(notificationReceiver, notificationFilter);
	}

	public static List<Map> getList(Cursor c) {
		list = new ArrayList<Map>();
		 Map map;
		 messagesCursor.moveToPosition(-1);
		 while(messagesCursor.moveToNext())
		 {  map = new HashMap();
		    Log.d(TAG, "Converting cursor to list");
			 map.put("message", messagesCursor.getString(0));
		 map.put("time", messagesCursor.getString(messagesCursor.getColumnIndex(ChatData.TIME_OF_MESSAGE)));
		 map.put("directtion", messagesCursor.getString(messagesCursor.getColumnIndex(ChatData.DIRECTION)));
			 list.add(map);
			
		 }
		 Log.d(TAG, "Sixe of list is"+list.size());
		 return list;
		 
	}

	class GetMessages extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			Log.d(TAG, "DoinBackground of GetUsers");
			if(messagesCursor!=null) messagesCursor=null;
			messagesCursor = ((MyApplication) getActivity().getApplication()).chatData.getMessagesOfUser(params[0]);
			ChatFragment.getList(messagesCursor);
			
			return "hi";

		}

		@Override
		protected void onPostExecute(String result) {
			Log.d(TAG, "OnPostExecute");
			super.onPostExecute(result);
			
		adapter = new SimpleAdapter(getSherlockActivity(), (List<? extends Map<String, ?>>) list, R.layout.row_message, from, to);
		listView.setAdapter(adapter);
		listView.setSelection(adapter.getCount()-1);
		
		
		
		}
		
		

	}
	
	
	
	//Intent Service for sending message
	
	
	
	public void hideKeyboard(EditText editConfirmPass)
	{       message.setText("");
	
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); imm.hideSoftInputFromWindow(editConfirmPass.getWindowToken(), 0);
	   
	}
	
	
	
	
	
	
}