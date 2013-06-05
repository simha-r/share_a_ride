package com.example.locationbased;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MessageInsertService extends IntentService {

	static String TAG = "MessageInsertService";

	public MessageInsertService() {
		super(TAG);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "OnHandleIntent");
		String emailId = intent.getStringExtra("email_id_receiver");
		String firstName = intent.getStringExtra("first_name");
		String message = intent.getStringExtra("message");
		long timeOfMessage = intent.getLongExtra("time_of_message", 1);
		String direction=intent.getStringExtra("direction");

		((MyApplication) getApplication()).chatData.insert(emailId, firstName,message, timeOfMessage,direction);
		
		Log.d(TAG, "Have finished inserting data");
		
		sendBroadcast(new Intent("com.example.locationbased.action.updateui"));
		
		/*try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//com.example.locationbased//databases//alldata.db";
                String backupDBPath = "alldatabackup.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
            Log.d(TAG, "Message Service finished");
		} catch (Exception e) {

        }*/

	}

}
