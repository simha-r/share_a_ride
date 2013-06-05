package com.example.locationbased;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MyTabsListener implements TabListener{

	public Fragment fragment;
	public SherlockFragmentActivity mactivity;
	 
	public MyTabsListener(Fragment fragment,SherlockFragmentActivity mactivity) {
	this.fragment = fragment;
	this.mactivity=mactivity;
	}
	 
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	//Toast.makeText(, "Reselected!", Toast.LENGTH_LONG).show();
	}
	 
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	ft.replace(R.id.frag_container, fragment);
//FragmentTransaction ft1=mactivity.getSupportFragmentManager().beginTransaction();
/*if(fragment.isDetached())
	ft.attach(fragment);
else
	ft.add(R.id.frag_container, fragment);
		
		*/
		
		
	}
	 
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	ft.remove(fragment);
	//ft.detach(fragment);
	
	}

}
