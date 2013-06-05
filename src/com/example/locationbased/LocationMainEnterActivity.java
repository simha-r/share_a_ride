package com.example.locationbased;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.locationbased.ChatFragment.ChatFragmentInterface;
import com.example.locationbased.EnterLocationFragment.EnterLocationFragInterface;
import com.example.locationbased.InboxFragment.InboxFragmentInterface;
import com.example.locationbased.ShowCoPassengersFragment.ShowCoPassengerFragInterface;
import com.example.locationbased.ShowCoPassengersProfileFragment.ShowCoPassengerProfileInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationMainEnterActivity extends SherlockFragmentActivity
		implements EnterLocationFragInterface, InboxFragmentInterface,
		ChatFragmentInterface, ShowCoPassengerFragInterface,ShowCoPassengerProfileInterface {

	private static final int TWO_MINUTES = 1000 * 60 * 2;
	GoogleMap map;
	static final String TAG = "LocationMainEnterActivity";
	Button buttonLogout;
	SharedPreferences prefs;
	TextView displayName;
	ActionBar bar;
	Tab tab1;
	Tab tab2;
	TabListener mtb2;
	FragmentTransaction ft1;
	EnterLocationFragment frag1;
	InboxFragment frag2;
	ShowCoPassengersProfileFragment frag4;
	Fragment fragMap;
	FragmentManager fm;
	Map dataForChat;

	/*
	 * String emailIdOfChat; String firstNameOfChat;
	 */

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		if (LocationData.emailId == null || LocationData.password == null) {
			Intent intent = new Intent(LocationMainEnterActivity.this,
					FirstActivity.class);
			startActivity(intent);
			Log.d(TAG, "going to finish mainenteractivity");
			finish();
			Log.d(TAG, "have finished the mainenteractivity");
		} else {
			setContentView(R.layout.activity_location);
			Log.d(TAG, "now going to initialize fragments");
			frag1 = new EnterLocationFragment();
			frag2 = new InboxFragment();
			fragMap = getSupportFragmentManager().findFragmentById(R.id.map);
			bar = getSupportActionBar();
			bar.setHomeButtonEnabled(true);
			bar.setDisplayShowTitleEnabled(false);
			/*bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			bar.setDisplayOptions(0, ActionBar.NAVIGATION_MODE_TABS);
			tab1 = bar.newTab().setText("Home");
			TabListener mtb1 = new MyTabsListener(frag1, this);
			tab1.setTabListener(mtb1);
			tab2 = bar.newTab().setText("Inbox");
			mtb2 = new MyTabsListener(frag2, this);
			tab2.setTabListener(mtb2);
			bar.addTab(tab1);
			bar.addTab(tab2);*/

			Log.d(TAG, "OncreateActivity");
			displayName = (TextView) findViewById(R.id.displayName);
			displayName.setText("Hi " + LocationData.emailId + " !");

			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			prefs = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());

			fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			// EnterLocationFragment frag1 = new EnterLocationFragment();

			ft.add(R.id.frag_container, frag1, "frag1");

			ft.commit();

		}
	}

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
	 * 
	 * if (keyCode == KeyEvent.KEYCODE_BACK) { int index =
	 * getSupportActionBar().getSelectedNavigationIndex(); switch(index) { case
	 * 0: return super.onKeyDown(keyCode, event);
	 * 
	 * case 1: ft1=fm.beginTransaction(); ft1.detach(frag2);
	 * if(frag1.isDetached()) { ft1.attach(frag2); ft1.commit(); } else {
	 * ft1.add(R.id.frag_container, frag1); ft1.commit(); }
	 * //mtb2.onTabUnselected(tab2, ft1); //mtb2.onTabSelected(tab1, ft1); } }
	 * return true; //return super.onKeyDown(keyCode, event); }
	 */

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_logout:
			Editor editor = prefs.edit();
			editor.clear();
			editor.commit();
			LocationData.emailId = null;
			LocationData.password = null;
			Intent intent = new Intent(LocationMainEnterActivity.this,
					FirstActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;

		case android.R.id.home:
			Log.d(TAG, "Home button clicked");
			frag1 = new EnterLocationFragment();
			 FragmentManager fm1=getSupportFragmentManager();			
             FragmentTransaction ft1 = fm.beginTransaction();
             ft1.replace(R.id.frag_container, frag1);
             ft1.show(fragMap);
             ft1.commit();
			break;
			
		case R.id.action_inbox:
			Log.d(TAG, "Messages button clicked in actionbar");
             FragmentManager fm=getSupportFragmentManager();			
             FragmentTransaction ft = fm.beginTransaction();
              ft.replace(R.id.frag_container, frag2, "frag2");
             ft.addToBackStack(null);
           
             ft.commit();
		}
		return true;

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Log.d(TAG, "OnResume");

	}

	// Callback method implemented for EnterLocationFragment ..It shows current
	// location on map
	@Override
	public void showOnMap(Location location) {

		LatLng latlng = new LatLng(location.getLatitude(),
				location.getLongitude());
		map.clear();

		Marker marker = map.addMarker(new MarkerOptions().position(latlng));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14));
		return;
	}

	public void showMap() {
		if (!fragMap.isVisible()) {
			FragmentTransaction ft1 = getSupportFragmentManager()
					.beginTransaction();
			ft1.show(fragMap);
			ft1.commit();

		}
	}

	// Callback method implemented for EnterLocationFragment ..It executes the 2
	// async tassks needed to get present location and post data to server and
	// get results
	@Override
	public void postAndProcessData(String destination, String source) {
		new ForwardGeocoding().execute(destination, source);

	}

	// Helper Functions To Determine Accurate Location Polling

	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	// Helper Functions regarding Posting data and converting to strings etc

	public String postRequestAndGetResponse(String url, List values) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = null;
		HttpPost httpPost = new HttpPost(url);
		InputStream is = null;
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

	// AsyncTask classes for geocoding,getting location fix etc
	// Async Task to get geocoding data and location fix

	class ForwardGeocoding extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			Geocoder geocoder1 = new Geocoder(LocationMainEnterActivity.this);
			List<Address> addresses = new ArrayList<Address>(1);
			try {
				addresses = geocoder1.getFromLocationName(params[0], 1);
				if (addresses.size() == 0)
					throw new AddressNotFoundException();
				addresses.get(0);
				LocationData.destinationLatitude = addresses.get(0)
						.getLatitude();
				LocationData.destinationLongitude = addresses.get(0)
						.getLongitude();
				LocationData.destinationAddress.setLength(0);
				for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++) {

					LocationData.destinationAddress.append(
							addresses.get(0).getAddressLine(i)).append(" \n");

				}
				if (params[1] != null) {

					addresses = geocoder1.getFromLocationName(params[1], 1);
					addresses.get(0);
					LocationData.sourceLatitude = addresses.get(0)
							.getLatitude();
					LocationData.sourceLongitude = addresses.get(0)
							.getLongitude();
					LocationData.sourceAddress.setLength(0);
					for (int i = 0; i <= addresses.get(0)
							.getMaxAddressLineIndex(); i++) {

						LocationData.sourceAddress.append(
								addresses.get(0).getAddressLine(i)).append(
								" \n");

					}

					Log.d(TAG,
							"Source Latitude is "
									+ Double.toString(LocationData.sourceLatitude));
					Log.d(TAG,
							" Source Longitude is "
									+ Double.toString(LocationData.sourceLongitude));

				} else {
					LocationData.sourceLatitude = LocationData.currentLatitude;
					LocationData.sourceLongitude = LocationData.currentLongitude;

					LocationData.sourceAddress.setLength(0);
					LocationData.sourceAddress
							.append(LocationData.currentAddress.toString());

				}

				Log.d(TAG,
						"Destination Latitude is "
								+ Double.toString(LocationData.destinationLatitude));
				Log.d(TAG,
						" Destination Longitude is "
								+ Double.toString(LocationData.destinationLongitude));
				Log.d(TAG,
						"source Latitude is "
								+ Double.toString(LocationData.sourceLatitude));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LocationData.error = "internet";
				Log.d(TAG, "IO excepton.....");

			} catch (AddressNotFoundException e) {
				Log.d(TAG, "Address not found exception thrown");
				LocationData.error = "address";
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (LocationData.error.equalsIgnoreCase("address")) {
				Toast.makeText(
						LocationMainEnterActivity.this,
						"We couldnt find your address. Please enter your address a little differently",
						Toast.LENGTH_LONG).show();
				LocationData.error = "noerror"; // Resetting the error to no
												// error
				cancel(true);
			} else if (LocationData.error.equalsIgnoreCase("internet")) {
				Toast.makeText(LocationMainEnterActivity.this,
						"Please make sure your internet is turned on",
						Toast.LENGTH_LONG).show();
				LocationData.error = "noerror";
				cancel(true);
			} else {
				new QueryServer().execute();
			}
		}

	}

	// Async Class for posting data to server and getting response
	class QueryServer extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			Log.d(TAG, "Starting QueryServer");

			List<NameValuePair> values = new ArrayList<NameValuePair>();

			values.add(new BasicNameValuePair("email_id", LocationData.emailId));
			values.add(new BasicNameValuePair("password", LocationData.password));
			values.add(new BasicNameValuePair("source_latitude", Double
					.toString(LocationData.sourceLatitude)));
			values.add(new BasicNameValuePair("source_longitude", Double
					.toString(LocationData.sourceLongitude)));
			values.add(new BasicNameValuePair("destination_latitude", Double
					.toString(LocationData.destinationLatitude)));
			values.add(new BasicNameValuePair("destination_longitude", Double
					.toString(LocationData.destinationLongitude)));
			values.add(new BasicNameValuePair("source_address",
					LocationData.sourceAddress.toString()));
			values.add(new BasicNameValuePair("destination_address",
					LocationData.destinationAddress.toString()));
			values.add(new BasicNameValuePair("mode_of_rider", "e"));
			values.add(new BasicNameValuePair("action", "updatelocation"));

			String response = null;
			String url = "http://www.sharearide.net63.net/controller.php";
			response = postRequestAndGetResponse(url, values);
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			LocationData.response = result;
			if (LocationData.response.trim().equalsIgnoreCase("noresults"))
				Toast.makeText(LocationMainEnterActivity.this,
						"Nobody is going in that direction ..Sorry",
						Toast.LENGTH_LONG).show();
			FragmentManager fm = getSupportFragmentManager();
			ShowCoPassengersFragment frag2 = new ShowCoPassengersFragment();
			Fragment frag1 = fm.findFragmentByTag("frag1");
			FragmentTransaction ft = fm.beginTransaction();
			ft.addToBackStack("frag1");
			ft.remove(frag1);

			ft.add(R.id.frag_container, frag2, "frag2");

			ft.commit();

			/*
			 * Intent intent = new Intent(LocationEnterActivity.this,
			 * ShowCoPassengers.class);
			 * intent.putExtra("copassenger_results_string", result);
			 * startActivity(intent);
			 */}

	}

	// InboxFragmentInterface callback method
	@Override
	public void openChatScreen(String emailId, String firstName) {

		Log.d(TAG, "going to open chat screen");
		/*
		 * emailIdOfChat=emailId; firstNameOfChat=firstName;
		 */
		dataForChat = new HashMap<String, String>();
		dataForChat.put("emailId", emailId);
		dataForChat.put("name", firstName);

		FragmentManager fm = getSupportFragmentManager();

		ChatFragment frag3 = new ChatFragment();
		// Fragment frag2=fm.findFragmentByTag("frag2");
		FragmentTransaction ft = fm.beginTransaction();
		ft.hide(fragMap);
		//ft.addToBackStack("frag2");
		//ft.remove(frag2);
        ft.replace(R.id.frag_container, frag3,"frag3");
		ft.addToBackStack(null);
		//ft.add(R.id.frag_container, frag3, "frag3");

		ft.commit();

	}

	@Override
	public Map getData() {

		return dataForChat;

	}

	@Override
	public void hideMap() {
		// TODO Auto-generated method stub
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.hide(fragMap);
		ft.commit();

	}

	@Override
	public void openProfileOfPassenger(PassengerProfile profile) {
		Log.d(TAG, "OpenProfileofPassenger called");
		frag4=new ShowCoPassengersProfileFragment();
		frag4.profile=profile;
		FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.frag_container, frag4);
        ft.addToBackStack(null);
        ft.commit();
	}

}

// Classes for exceptions etc

class AddressNotFoundException extends Exception {
	public AddressNotFoundException() {
		super("Address Not Found");
	}
}

// AsyncTask classes for geocoding and getting location details

