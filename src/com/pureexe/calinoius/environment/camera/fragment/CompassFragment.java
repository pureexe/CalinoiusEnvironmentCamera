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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.pureexe.calinoius.environment.camera.activity.FragmentDisplayActivity;
import com.pureexe.calinoius.environment.camera.activity.MainActivity;
import com.pureexe.calinoius.environment.camera.utility.CameraPreview;
import com.pureexe.calinoius.environment.camera.utility.EnvSensorJSON;
import com.pureexe.calinoius.environment.camera.utility.HomePageAdapter;
import com.pureexe.calinoius.environment.camera.R;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Camera.PictureCallback;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CompassFragment extends Fragment implements SensorEventListener{
	private Sensor sensorOrientation;
	private SensorManager sensorManager;
	private boolean hasOrientation;
	
	public void onCreate(Bundle savedInstanceState) {
		sensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE); 
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_compass, container,
				false);
		return rootView;
	}
	
	@Override
	public void onResume() {
		hasOrientation = sensorManager.registerListener(this, sensorOrientation,SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            Intent intent = new Intent(getActivity(),MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

}
