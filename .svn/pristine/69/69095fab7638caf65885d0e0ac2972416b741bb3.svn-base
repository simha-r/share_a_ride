/*package com.example.locationbased;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShowCoPassengers extends Activity {
     PassengerAdapter passengerAdapter;
	String result;
	ArrayList<PassengerProfile> copassengersList;
	String from[]={"email_id","source_address"};
	int to[]={R.id.emailIdPassenger,R.id.sourceAddressPassenger};
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_copassengers);
                listView=(ListView) findViewById(R.id.listview1);
		copassengersList = new ArrayList<PassengerProfile>();
	Intent intent=getIntent();
	result=intent.getStringExtra("copassenger_results_string");
	if(result.trim().equalsIgnoreCase("noresults"))
		Log.d("locationbasedactivity","There are no results");
		
		Log.d("locationbasedactivity", result.toString());
		
		
		if (!result.trim().equalsIgnoreCase("noresults")) 
		{ Log.d("locationbasedactivity", "have inside if loop");
			
		try {
			JSONObject jsonObject =new JSONObject(result);
			JSONArray  usersArray = jsonObject.getJSONArray("users");
			for (int i = 0; i < usersArray.length(); i++) {
		    JSONObject jsonObject1 = usersArray.getJSONObject(i);
		    HashMap passengerDetails =new HashMap();
		    Log.d("locationbasedactivity", "inside THE showcopassengers");
		    Log.d("locationbasedactivity",jsonObject1.get("email_id").toString());
		    passengerDetails.put("email_id", jsonObject1.get("email_id").toString());
		    passengerDetails.put("source_address", jsonObject1.get("source_address").toString());
		    PassengerProfile profile =new PassengerProfile();
		   profile.emailIdPassenger=jsonObject1.getString("email_id");
		   profile.firstNamePassenger=jsonObject1.getString("first_name");
		   profile.sourceLatitudePassenger=jsonObject1.getDouble("source_latitude");
		   profile.sourceLongitudePassenger=jsonObject1.getDouble("source_longitude");
		   profile.destinationLatitudePassenger=jsonObject1.getDouble("destination_latitude");
		   profile.destinationLongitudePassenger=jsonObject1.getDouble("destination_longitude");
		   profile.sourceAddressPassenger=jsonObject1.getString("source_address");
		   profile.destinationAddressPassenger=jsonObject1.getString("destination_address");
		   profile.modeOfPassenger=jsonObject1.getString("mode_of_rider");
		   profile.timeOfRidePassenger=jsonObject1.getString("time_of_ride");
		   profile.destinationDistance=jsonObject1.getDouble("distanced");
		   profile.sourceDistance=jsonObject1.getDouble("distances");
		   copassengersList.add(profile);	
		   Log.d("locationbasedactivity", "Name is"+ profile.firstNamePassenger);
		}
		
		
		
		
		passengerAdapter = new PassengerAdapter(ShowCoPassengers.this, R.layout.row_passenger, copassengersList);

	     listView.setAdapter(passengerAdapter);   
	     Log.d("locationbasedactivity", "size is "+copassengersList.size());
           //Log.d("locationbasedactivity","Passengeradapter item 1 is "+ passengerAdapter.getItem(0).toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("locationbasedactivity", "JsonException has occured");
			e.printStackTrace();
		}	
		
		}
		else
		{setContentView(R.layout.no_passengers_found);
			
			
		}
                
	
	
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
	}

}
*/