package com.example.locationbased;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class ShowCoPassengersProfileFragment extends SherlockFragment {

	TextView profileName;
	TextView profileSource;
	PassengerProfile profile;
	TextView profileDestination;
	Button buttonChat;
	ShowCoPassengerProfileInterface mcallback;
	
	public interface ShowCoPassengerProfileInterface 
	{
		 public void openChatScreen(String emailId, String firstName);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view= inflater.inflate(R.layout.show_passenger_profile, null,false);
		
		mcallback=(ShowCoPassengerProfileInterface) getActivity();
		profileName=(TextView) view.findViewById(R.id.profile_name);
		profileSource=(TextView) view.findViewById(R.id.profile_source);
		profileDestination=(TextView) view.findViewById(R.id.profile_destination);
		buttonChat=(Button) view.findViewById(R.id.profile_send_message);
				
		profileName.setText(profile.firstNamePassenger);
		profileSource.setText(profile.sourceAddressPassenger);
		profileDestination.setText(profile.destinationAddressPassenger);
		
		buttonChat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			mcallback.openChatScreen(profile.emailIdPassenger, profile.firstNamePassenger);
				
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		return view;
				
	}

	
	
	
	
	
	
	
}
