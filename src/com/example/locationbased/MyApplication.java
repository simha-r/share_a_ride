package com.example.locationbased;

import java.util.Map;

import android.app.Application;
import android.app.Notification;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.UAirship;
import com.urbanairship.push.BasicPushNotificationBuilder;
import com.urbanairship.push.PushManager;

public class MyApplication extends Application {

	public static boolean running;
	SharedPreferences prefs;
	ChatData chatData;

	@Override
	public void onCreate() {

		super.onCreate();
		
		

		AirshipConfigOptions options = AirshipConfigOptions
				.loadDefaultOptions(this);
		UAirship.takeOff(this, options);
		PushManager.enablePush();

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		LocationData.emailId = prefs.getString("emailId", null);
		LocationData.password = prefs.getString("password", null);


		chatData=new ChatData(this);
		
		// customize the sound played when a push is received
		// nb.soundUri =
		// Uri.parse("android.resource://"+this.getPackageName()+"/"
		// +R.raw.cat);
		BasicPushNotificationBuilder nb = new BasicPushNotificationBuilder() {
			@Override
			public Notification buildNotification(String alert,
					Map<String, String> extras) {
				Notification notification = super.buildNotification(alert,
						extras);
				// The icon displayed in the status bar
				notification.icon = R.drawable.icon;

				// The icon displayed within the notification content
				notification.contentView.setImageViewResource(
						android.R.id.icon, R.drawable.icon);
				if (!running)
					return notification;
				else
					return null;
			}
		};
		// Set the custom notification builder
		PushManager.shared().setNotificationBuilder(nb);

		PushManager.shared().setIntentReceiver(IntentReceiver.class);

	}
}
