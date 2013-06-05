/*package com.example.locationbased;

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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationEnterActivity extends SherlockFragmentActivity {
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	GoogleMap map;
	static final String TAG = "locationbasedactivity";

	AsyncTask<String, String, String> GetLocationDetails; // first async task to
															// do geocoding and
															// get loc. details
	// AsyncTask<String, String, String> QueryServer; //2nd async task to submit
	// details to server and query server for nearby people
	// has to be performed after execution of 1st async task
	RelativeLayout layout;
	SharedPreferences prefs;

	TextView displayName;
	Button buttonLogout;
	Button buttonSubmit; // Button to submit info
	Button buttonCurrentLocation;// button to populate sourceField with
									// currentAddress
	EditText destinationField; // Edit field of destination
	EditText sourceField; // Edit field of source
	LocationManager locationManager;
	LocationListener locationListener;
	// <2>
	Geocoder geocoder; // Geocoder used in Reverse Geocoding..ie from address to
						// lat/long
	TextView locationText; // shows current location ...set by location listener
	
	 * MapView map; MapController mapController;
	 

	IntentFilter notificationFilter = new IntentFilter(
			"com.example.locationbased.action.message");
	// To get notifications ( mainly messages from other users)
	BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "Testing notification service in LocationEnterActivity "
					+ intent.getStringExtra("first_name"));
			playSound(); // To play a sound similar to notification sound
							// because we have disabled notifications
			addButton(intent.getStringExtra("first_name"),
					intent.getStringExtra("message"));
		}
	};

	public void addButton(String name, String message) {
		Log.d(TAG, "addButton called" + name);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.notify_new_message, null);

		TextView tv = (TextView) view.findViewById(R.id.notifyMessageName);
		tv.setText("  " + name + " has sent u a message");
		// Button notificationButton=(Button)
		// findViewById(R.id.buttonShowMessage);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ABOVE, R.id.map);
		layout.addView(view, params);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		if (!prefs.getBoolean("loggedin", false)) {
			Intent intent = new Intent(LocationEnterActivity.this,
					FirstActivity.class);
			startActivity(intent);
			finish();
		}

		setContentView(R.layout.activity_location);
		layout = (RelativeLayout) findViewById(R.id.activity_location);
		Log.d(TAG, "On created LocationEnterActivity");
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		emailId = prefs.getString("emailId", null);
		password = prefs.getString("password", null);

		displayName = (TextView) findViewById(R.id.displayName);
		displayName.setText("Hi  " + prefs.getString("emailId", "Hi") + " !"); // Display
																				// name
																				// of
																				// user
																				// on
																				// screen
		locationText = (TextView) findViewById(R.id.text1);
		destinationField = (EditText) findViewById(R.id.destinationField);
		sourceField = (EditText) findViewById(R.id.sourceField);
		buttonCurrentLocation = (Button) findViewById(R.id.buttonCurrentLocation);
		buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
		buttonLogout = (Button) findViewById(R.id.buttonLogout);
		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		
		 * map = (MapView) findViewById(R.id.map1);
		 * map.setBuiltInZoomControls(true); mapController =
		 * map.getController(); // <4> mapController.setZoom(16);
		 
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			@Override
			public void onProviderEnabled(String provider) {

				Log.d(TAG, "OnProvider Enabled");
			}

			@Override
			public void onProviderDisabled(String provider) {

				Log.d(TAG, "OnProvider Disabled");
			}

			@Override
			public void onLocationChanged(Location location) {
				Log.d(TAG, "onLocationChanged");
				makeUseOfNewLocation(location);

			}

			private void makeUseOfNewLocation(Location location) {

				Log.d(TAG,
						"onLocationChanged with location "
								+ location.toString());
				if (isBetterLocation(location,
						LocationEnterActivity.currentBestLocation)) {
					LocationEnterActivity.currentBestLocation = location;
					showOnMap(location);
					getAndSetAddressFromLocation(location);

				}

			}

			private void getAndSetAddressFromLocation(Location location) {
				geocoder = new Geocoder(getApplicationContext());
				currentLatitude = location.getLatitude();
				currentLongitude = location.getLongitude();
				List<Address> addresses;
				try {
					addresses = geocoder.getFromLocation(
							location.getLatitude(), location.getLongitude(), 5);

					currentAddress = new StringBuilder("");
					if (addresses.size() > 0) {

						Address address = addresses.get(0);
						currentAddress.setLength(0);
						for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
							currentAddress.append(address.getAddressLine(i))
									.append(" \n");

						}
					}
					locationText.setText(currentAddress); // set current address
															// in a textview
					Log.d(TAG, currentAddress.toString());

				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.d(TAG, "couldnt get addreses from geocoder");
					e.printStackTrace();
				}
			}

			private void showOnMap(Location location) {
				// int latitude = (int) (location.getLatitude() * 1000000);
				// int longitude = (int) (location.getLongitude() * 1000000);
				LatLng latlng = new LatLng(location.getLatitude(),
						location.getLongitude());
				// GeoPoint point = new GeoPoint(latitude, longitude);

				Marker currentLocationMarker = map
						.addMarker(new MarkerOptions().position(latlng));
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14));

			}

		};

		buttonLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor editor = prefs.edit();
				editor.clear();
				editor.commit();
				Intent intent = new Intent(LocationEnterActivity.this,
						FirstActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

			}
		});

		buttonCurrentLocation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sourceField.setText(currentAddress);
			}
		});

		buttonSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				String destination = destinationField.getText().toString();
				String source = sourceField.getText().toString();
				if (currentAddress != null) {
					if (source.equalsIgnoreCase(currentAddress.toString())) {
						// do nothing..have to send current location itself as
						// source location to server
						Log.d(TAG, "Your source is same as current address");
						source = null;
					}
				}

				GetLocationDetails = new ForwardGeocoding().execute(
						destination, source); // get address and coordinates by
												// geocoding

			}

			class QueryServer extends AsyncTask<String, String, String> {

				@Override
				protected String doInBackground(String... params) {
					Log.d(TAG, "Starting QueryServer");

					List<NameValuePair> values = new ArrayList<NameValuePair>();

					values.add(new BasicNameValuePair("email_id", emailId));
					values.add(new BasicNameValuePair("password", password));
					values.add(new BasicNameValuePair("source_latitude", Double
							.toString(sourceLatitude)));
					values.add(new BasicNameValuePair("source_longitude",
							Double.toString(sourceLongitude)));
					values.add(new BasicNameValuePair("destination_latitude",
							Double.toString(destinationLatitude)));
					values.add(new BasicNameValuePair("destination_longitude",
							Double.toString(destinationLongitude)));
					values.add(new BasicNameValuePair("source_address",
							sourceAddress.toString()));
					values.add(new BasicNameValuePair("destination_address",
							destinationAddress.toString()));
					values.add(new BasicNameValuePair("mode_of_rider", "e"));
					values.add(new BasicNameValuePair("action",
							"updatelocation"));

					String response = null;
					String url = "http://www.sharearide.net63.net/controller.php";
					response = postRequestAndGetResponse(url, values);
					return response;
				}

				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					Intent intent = new Intent(LocationEnterActivity.this,
							ShowCoPassengers.class);
					intent.putExtra("copassenger_results_string", result);
					startActivity(intent);

				}

			}

			class ForwardGeocoding extends AsyncTask<String, String, String> {

				@Override
				protected String doInBackground(String... params) {

					// double destinationLatitude,
					// destinationLongitude,sourceLatitude,sourceLongitude;
					Geocoder geocoder1 = new Geocoder(getApplicationContext());
					List<Address> addresses = new ArrayList<Address>(1);
					try {
						addresses = geocoder1.getFromLocationName(params[0], 1);
						if (addresses.size() == 0)
							throw new AddressNotFoundException();
						addresses.get(0);
						destinationLatitude = addresses.get(0).getLatitude();
						destinationLongitude = addresses.get(0).getLongitude();
						destinationAddress.setLength(0);
						for (int i = 0; i <= addresses.get(0)
								.getMaxAddressLineIndex(); i++) {

							destinationAddress.append(
									addresses.get(0).getAddressLine(i)).append(
									" \n");

						}
						if (params[1] != null) {

							addresses = geocoder1.getFromLocationName(
									params[1], 1);
							addresses.get(0);
							sourceLatitude = addresses.get(0).getLatitude();
							sourceLongitude = addresses.get(0).getLongitude();
							sourceAddress.setLength(0);
							for (int i = 0; i <= addresses.get(0)
									.getMaxAddressLineIndex(); i++) {

								sourceAddress.append(
										addresses.get(0).getAddressLine(i))
										.append(" \n");

							}

							Log.d(TAG,
									"Source Latitude is "
											+ Double.toString(sourceLatitude));
							Log.d(TAG,
									" Source Longitude is "
											+ Double.toString(sourceLongitude));

						} else {
							sourceLatitude = currentLatitude;
							sourceLongitude = currentLongitude;

							sourceAddress.setLength(0);
							sourceAddress.append(currentAddress.toString());

						}

						Log.d(TAG,
								"Destination Latitude is "
										+ Double.toString(destinationLatitude));
						Log.d(TAG,
								" Destination Longitude is "
										+ Double.toString(destinationLongitude));
						Log.d(TAG,
								"source Latitude is "
										+ Double.toString(sourceLatitude));

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						error = "internet";
						Log.d(TAG, "IO excepton.....");

					} catch (AddressNotFoundException e) {
						Log.d(TAG, "Address not found exception thrown");
						error = "address";
						e.printStackTrace();
					}

					return null;
				}

				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					if (error.equalsIgnoreCase("address")) {
						Toast.makeText(
								LocationEnterActivity.this,
								"We couldnt find your address. Please enter your address a little differently",
								Toast.LENGTH_LONG).show();
						error = "noerror"; // Resetting the error to no error
						cancel(true);
					} else if (error.equalsIgnoreCase("internet")) {
						Toast.makeText(LocationEnterActivity.this,
								"Please make sure your internet is turned on",
								Toast.LENGTH_LONG).show();
						error = "noerror";
						cancel(true);
					} else {
						// showOnMapDestination();
						new QueryServer().execute();
					}
				}

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	private void showOnMapDestination() {
		// int latitude = (int) (location.getLatitude() * 1000000);
		// int longitude = (int) (location.getLongitude() * 1000000);
		LatLng latlng = new LatLng(destinationLatitude, destinationLongitude);
		// GeoPoint point = new GeoPoint(latitude, longitude);

		Marker currentLocationMarker = map.addMarker(new MarkerOptions()
				.position(latlng));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12));

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MyApplication.running = true; // used to tell notification builder that
										// we dont want to build a notification
										// as we r inside the app
		registerReceiver(notificationReceiver, notificationFilter);
		Log.d(TAG, "OnResume LocationEnterActivity");
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 10, locationListener);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		View v = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(event);

		if (v instanceof EditText) {
			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
			float y = event.getRawY() + w.getTop() - scrcoords[1];

			Log.d("Activity",
					"Touch event " + event.getRawX() + "," + event.getRawY()
							+ " " + x + "," + y + " rect " + w.getLeft() + ","
							+ w.getTop() + "," + w.getRight() + ","
							+ w.getBottom() + " coords " + scrcoords[0] + ","
							+ scrcoords[1]);
			if (event.getAction() == MotionEvent.ACTION_UP
					&& (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
							.getBottom())) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
						.getWindowToken(), 0);
			}
		}
		return ret;
	}

	public void playSound() {
		Uri notification = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
				notification);
		r.play();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onPause  LocationEnterActivity");
		if (this.hasWindowFocus())
			Log.d(TAG, "It has window focus");

		locationManager.removeUpdates(locationListener);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		Log.d(TAG, "On Stopped LocationEnterActivity");
		if (!this.hasWindowFocus()) // To check if activity is in
									// foreground...even if screen is off
		{
			MyApplication.running = false;
			unregisterReceiver(notificationReceiver);

		}
	}

}
*/