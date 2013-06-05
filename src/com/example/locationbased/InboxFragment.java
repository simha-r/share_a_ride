package com.example.locationbased;

import java.util.ArrayList;
import java.util.List;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;



public class InboxFragment extends SherlockFragment {
	
Cursor c;
InboxFragmentInterface mcallback;	
	



@Override
public void onAttach(Activity activity) {
	// TODO Auto-generated method stub
	super.onAttach(activity);
	mcallback=(InboxFragmentInterface) getActivity();
}


String[] names1;
String[] actualNames;

	String from[]={ChatData.EMAIL_ID};
	int to[]={R.id.emailIdInbox};
	ListView listView;
	String TAG="InboxFragment";
	
	View view;
	ArrayAdapter adapter;
	
	
	interface InboxFragmentInterface
	{
		public void openChatScreen(String emailId,String firstName);
		public void hideMap();
	}
	
	IntentFilter notificationFilter = new IntentFilter(
			"com.example.locationbased.action.updateui");
	
	BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			//Log.d(TAG, "Testing notification service in LocationEnterActivity "
				//	+ intent.getStringExtra("first_name"));
			Log.d(TAG, "received broadcast ..going to notify adapter");
			new GetUsers().execute("hi");
			//playSound(); // To play a sound similar to notification sound
							// because we have disabled notifications
			//addButton(intent.getStringExtra("first_name"),
				//	intent.getStringExtra("message"));
		}
	};
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.show_inbox, container, false);
    }

	/*@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG, "OnStarted");
		new GetUsers().execute("hi");
	}*/
 

	@Override
	public void onResume() {
		
		super.onResume();
		mcallback.hideMap();
	   getActivity().registerReceiver(notificationReceiver, notificationFilter);
		Log.d(TAG, "OnResumed");
		new GetUsers().execute("hi");
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		notificationFilter.setPriority(1);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	getActivity().unregisterReceiver(notificationReceiver);
	}


	class GetUsers extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			Log.d(TAG, "DoinBackground of GetUsers");
			 c=((MyApplication)getActivity().getApplication()).chatData.getUsers();
			/* while(c.moveToNext())
			 Log.d(TAG, c.getString(0));*/
return "hi";

		}

		@Override
		protected void onPostExecute(String result) {
		
			super.onPostExecute(result);
			Log.d(TAG, "OnPost executed of GetUsers");
			listView = (ListView) getView().findViewById(R.id.list_inbox);
			
		
			List names=new ArrayList();
			List realNames=new ArrayList();
			int count=0;
			 while(c.moveToNext())
			 {	 count++;
				 Log.d(TAG, c.getString(0));
			     names.add(c.getString(0));
			     realNames.add(c.getString(1));
			 }
			 c.moveToPosition(-1);
			
              names1 =new String[count];
              actualNames=new String[count];
			 int i=0;
			 while(c.moveToNext())
			 {
				 names1[i]=c.getString(0);
				 actualNames[i]=c.getString(1);
				 Log.d(TAG,names1[i]);
				 i++;
			 }
		
			 adapter =new ArrayAdapter(getSherlockActivity(), R.layout.row_inbox, R.id.emailIdInbox,actualNames);
			 listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
				//Log.d(TAG, "Email id is "+listView.getItemAtPosition(position));
				String emailId=names1[position];
				
				Log.d(TAG, "Name is "+actualNames[position]);
				mcallback.openChatScreen(emailId,actualNames[position]);
				
					
				}
				

	        });
	
			
			/*SimpleCursorAdapter adapter=new SimpleCursorAdapter(getSherlockActivity(),R.layout.row_inbox, c, new String[] { "email_id" },new int[] { R.id.emailIdInbox } );
			if(c==null)
				Log.d(TAG, "Cursor is null");
			else
			{adapter=new SimpleCursorAdapter(getActivity(),R.layout.row_inbox, c, from, to);
			listView.setAdapter(adapter);
			}*/
			
		}
		
		
	}
	
	
	
	
	
	
	
	
	
}