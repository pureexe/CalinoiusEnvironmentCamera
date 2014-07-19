/*
* Copyright (C) 2014 Pakkapon Phongthawee
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.pureexe.calinoius.environment.camera.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.pureexe.calinoius.environment.camera.activity.EXIFreadActivity;
import com.pureexe.calinoius.environment.camera.activity.EnvironmentCameraActivity;
import com.pureexe.calinoius.environment.camera.activity.FragmentDisplayActivity;
import com.pureexe.calinoius.environment.camera.activity.MainActivity;
import com.pureexe.calinoius.environment.camera.utility.DataManager;
import com.pureexe.calinoius.environment.camera.utility.EnvSensorJSON;
import com.pureexe.calinoius.environment.camera.utility.HomePageAdapter;
import com.pureexe.calinoius.environment.camera.R;

public class MainFragment extends Fragment  {
	DataManager dataManager;
	public MainFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		dataManager = new DataManager(getActivity());
		
		try{
			// Use Try catch Exception to avoid force close with CM's Privacy Guard
			Cursor c = getActivity().getApplication().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null); 
			c.moveToFirst();
			dataManager.setString("Researcher", c.getString(c.getColumnIndex("display_name")));
		}
		catch(Exception e) {
			dataManager.setString("Researcher","Unknown");
		}
		GridView gridview = (GridView) rootView.findViewById(R.id.gridView1);
	    gridview.setAdapter(new HomePageAdapter(getActivity()));
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	if(position==0){
	        		Toast.makeText(getActivity(), "No implement yet :P", Toast.LENGTH_SHORT).show();
	        	}
	        	if(position==1){
	    			Intent intent = new Intent(getActivity(), EnvironmentCameraActivity.class);
	    	        startActivity(intent);
	            }
	        	if(position==2){
	            	Intent beepActivity = new Intent(getActivity(),FragmentDisplayActivity.class);
	            	beepActivity.setAction(Intent.ACTION_SEND);
	            	beepActivity.putExtra(Intent.EXTRA_TEXT,"EXIFreadFragment");
	            	beepActivity.setType("beepActivity");
	            	startActivity(beepActivity);
	            }
	        	if(position==3){
	            	Intent beepActivity = new Intent(getActivity(),FragmentDisplayActivity.class);
	            	beepActivity.setAction(Intent.ACTION_SEND);
	            	beepActivity.putExtra(Intent.EXTRA_TEXT,"SettingPreferenceFragment");
	            	beepActivity.setType("beepActivity");
	            	startActivity(beepActivity);
	            }
	        }
	    });

	    
		return rootView;
	}
	
	


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_environmentcamera) {
			Intent intent = new Intent(getActivity(), EnvironmentCameraActivity.class);
	        startActivity(intent);
		}
		if (id == R.id.action_exifread) {
			Intent intent = new Intent(getActivity(), EXIFreadActivity.class);
	        startActivity(intent);
		}
		if (id == R.id.action_compass) {
		}
		return super.onOptionsItemSelected(item);
	}

	
}
