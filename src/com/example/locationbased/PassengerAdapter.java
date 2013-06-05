package com.example.locationbased;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PassengerAdapter extends ArrayAdapter {
	public static String TAG = "locationbasedactivity";

	int resource;
	List list;

	public PassengerAdapter(Context context, int textViewResourceId,
			List<PassengerProfile> list) {
		super(context, textViewResourceId);
		resource = textViewResourceId;
		this.list = list;
		Log.d("locationbasedactivity", "PassengerAdapter constructor is called");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Log.d("locationbasedactivity", "getView is called");

		Log.d(TAG, "position is " + position);
		PassengerProfile profile = (PassengerProfile) list.get(position);
		if (convertView == null) {

			LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(resource, parent, false);
			holder = new ViewHolder();
			holder.emailView = (TextView) convertView
					.findViewById(R.id.emailIdPassenger);
			holder.sourceAddressView = (TextView) convertView
					.findViewById(R.id.sourceAddressPassenger);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.emailView.setText(profile.emailIdPassenger);
		holder.sourceAddressView.setText(profile.sourceAddressPassenger);
		return convertView;
	}

	static class ViewHolder {
		TextView emailView;
		TextView sourceAddressView;
	}

}
