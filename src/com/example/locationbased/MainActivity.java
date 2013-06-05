package com.example.locationbased;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.urbanairship.push.PushManager;

public class MainActivity extends Activity {
	static final String TAG = "locationbasedactivity";
	public static SharedPreferences prefs;

	private static EditText firstName, lastName, emailID, phone, password;
	public static String firstNameValue, lastNameValue, emailIDValue,
			phoneValue, apid, gcm_regid, passwordValue;
	private static RadioButton male, female;
	private static Button buttonSignup;
	String gender;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(TAG, "on created MainActivity");
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	

			setContentView(R.layout.activity_registration);
			apid = PushManager.shared().getAPID();
			gcm_regid = PushManager.shared().getGcmId();
			firstName = (EditText) findViewById(R.id.fieldFirstName);
			lastName = (EditText) findViewById(R.id.fieldLastName);
			password = (EditText) findViewById(R.id.fieldPassword);
			emailID = (EditText) findViewById(R.id.fieldEmailID);
			phone = (EditText) findViewById(R.id.fieldPhone);
			male = (RadioButton) findViewById(R.id.radio0);
			female = (RadioButton) findViewById(R.id.radio1);
			buttonSignup = (Button) findViewById(R.id.button_signup);
			buttonSignup.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					firstNameValue = firstName.getText().toString();
					lastNameValue = lastName.getText().toString();
					emailIDValue = emailID.getText().toString();
					phoneValue = phone.getText().toString();
					passwordValue = password.getText().toString();

					if (male.isChecked())
						gender = "Male";
					else if (female.isChecked())
						gender = "Female";
					new SignUp().execute();

				}
			});

		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "On Paused MainActivity");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "On Resumed MainActivity");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "On Stopped MainActivity");
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append((line + "\n"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public class SignUp extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			if (apid == null) {
				apid = PushManager.shared().getAPID();
				gcm_regid = PushManager.shared().getGcmId();
			}
			HttpClient client = new DefaultHttpClient();
			List<NameValuePair> pair = new ArrayList<NameValuePair>();
			pair.add(new BasicNameValuePair("uairship_id", apid));
			pair.add(new BasicNameValuePair("first_name", firstNameValue));
			pair.add(new BasicNameValuePair("email_id", emailIDValue));
			pair.add(new BasicNameValuePair("last_name", lastNameValue));
			pair.add(new BasicNameValuePair("phone_number", phoneValue));
			pair.add(new BasicNameValuePair("gcm_reg_id", gcm_regid));
			pair.add(new BasicNameValuePair("gender", gender));
			pair.add(new BasicNameValuePair("action", "registration"));
			pair.add(new BasicNameValuePair("password", passwordValue));
			HttpPost httpPost = new HttpPost(
					"http://www.sharearide.net63.net/controller.php");

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(pair));
				HttpResponse httpResponse = null;
				httpResponse = client.execute(httpPost);
				int responseCode = httpResponse.getStatusLine().getStatusCode();
				switch (responseCode) {
				case 200:
					HttpEntity entity = httpResponse.getEntity();
					if (entity != null) {
						InputStream is = entity.getContent();
						String responseString = convertStreamToString(is);
						Editor editor = prefs.edit();
						if (responseString.trim().equalsIgnoreCase(
								"Succesfully Registered")) {
							
							editor.putBoolean("registered", true);  // Indicates that we have registered at least one time
							editor.putBoolean("loggedin", true);  //Indicates that user is signed in
							editor.putString("uairship_id", apid);
							editor.putString("emailId", emailIDValue);
							editor.putString("password", passwordValue);
							editor.putString("phone", phoneValue);
							editor.putString("gcm_regid", gcm_regid);
							editor.putString("firstName", firstNameValue);
							editor.putString("lastName", lastNameValue);
							editor.putString("gender",gender);
							editor.commit();
							return "You have successfully Registered";

						} else if (responseString.trim().equalsIgnoreCase(
								"Registration Failed"))
							return "Registration Failed";

					}
					break;
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
			Intent intent = new Intent(MainActivity.this, LocationMainEnterActivity.class); 
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);	
			finish();

			

		}

	}

}
