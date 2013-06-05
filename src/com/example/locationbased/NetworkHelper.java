package com.example.locationbased;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class NetworkHelper {

	public static String TAG="NetworkHelper";
	public static String postRequestAndGetResponse(String url,List values)
	{   Log.d(TAG, "list has "+values.toString());
		
		
		HttpClient httpClient = new DefaultHttpClient();
	HttpResponse httpResponse = null;
	HttpPost httpPost = new HttpPost(url);
		InputStream is=null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(values));
			Log.d(TAG, "going to post params");
			httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			is = entity.getContent();
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
		return convertStreamToString(is);
	}
	
	
	public static long getCurrentTimeStamp()
	{
		long timestamp = System.currentTimeMillis()/1000;
		return timestamp;
	}
	

	public static String convertStreamToString(InputStream is) {

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
			Log.d(TAG, "Response is "+sb.toString());
			return sb.toString();
		}
		
	}
	
	
	
	//function for hiding keyboard
	


	
}
