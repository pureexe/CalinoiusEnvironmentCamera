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
import java.io.IOException;

import com.pureexe.calinoius.environment.camera.activity.EnvironmentCameraActivity;
import com.pureexe.calinoius.environment.camera.utility.EnvSensorJSON;
import com.pureexe.calinoius.environment.camera.R;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnSystemUiVisibilityChangeListener;

public class EXIFreadFragment extends Fragment {
	protected long currentTime;
	private EnvSensorJSON envJSON;
	private Uri imageUri;
	public EXIFreadFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_exifread, container,
				false);
		Intent intent = getActivity().getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		if (Intent.ACTION_SEND.equals(action) && type != null && type.startsWith("image/")) {
				imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		} else {
			pickIntentPicture();
		}
		
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		if(imageUri != null)
			setDataView(imageUri);
		

		super.onResume();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == getActivity().RESULT_CANCELED){
	    	getActivity().finish();
	    }
		if(resultCode == getActivity().RESULT_OK && requestCode == SELECT_PHOTO){
			imageUri = data.getData();
		}
	}

	private static final int SELECT_PHOTO = 100;
	protected void pickIntentPicture() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, SELECT_PHOTO);		
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
		return super.onOptionsItemSelected(item);
	}
	
	private void setDataView(Uri _imageUri){
		String path = null;
		String[] projection = { MediaStore.Images.Media.DATA };
	    Cursor cursor = getActivity().getContentResolver().query(_imageUri, projection, null, null, null);
	    if (cursor.moveToFirst()) {
	        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        path = cursor.getString(columnIndex);
	    }					    
	    cursor.close();
		try {
			ExifInterface exif = new ExifInterface(path);
			envJSON = new EnvSensorJSON(exif.getAttribute("UserComment"));
			TextView tmptxt;
			float[] tf;
			tmptxt = (TextView)getView().findViewById(R.id.researcher);
			tmptxt.setText(envJSON.getResearcher());
			tmptxt = (TextView)getView().findViewById(R.id.acceleration);
			if(envJSON.hasAcceleration()){
				tf=envJSON.getAcceleration();
				tmptxt.setText("[X] "+tf[0]+" m/s²"+"\n[Y] "+tf[1]+" m/s²"+"\n[Z] "+tf[2]+" m/s²");
			} else {
				tmptxt.setText(getActivity().getString(R.string.notavailable));
			}
			tmptxt = (TextView)getView().findViewById(R.id.gravity);
			if(envJSON.hasGravity()){
				tf=envJSON.getGravity();
				tmptxt.setText("[X] "+tf[0]+" m/s²"+"\n[Y] "+tf[1]+" m/s²"+"\n[Z] "+tf[2]+" m/s²");
			} else {
				tmptxt.setText(getActivity().getString(R.string.notavailable));
			}
			tmptxt = (TextView)getView().findViewById(R.id.gyroscope);
			if(envJSON.hasGyroscope()){
				tf=envJSON.getGyroscope();
				
				tmptxt.setText("[X] "+tf[0]+" m/s²"+"\n[Y] "+tf[1]+" m/s²"+"\n[Z] "+tf[2]+" m/s²");
			} else {
				tmptxt.setText(getActivity().getString(R.string.notavailable));
			}
			tmptxt = (TextView)getView().findViewById(R.id.magneticfield);
			if(envJSON.hasMagneticField()){
				tf=envJSON.getMagneticField();
				tmptxt.setText("[X] "+tf[0]+" uT"+"\n[Y] "+tf[1]+" uT"+"\n[Z] "+tf[2]+" uT");
			} else {
				tmptxt.setText(getActivity().getString(R.string.notavailable));
			}
			tmptxt = (TextView)getView().findViewById(R.id.orientation);
			if(envJSON.hasOrientation()){
				tf=envJSON.getOrientation();
				tmptxt.setText("[X] "+tf[0]+"°"+"\n[Y] "+tf[1]+"°"+"\n[Z] "+tf[2]+"°");
			} else {
				tmptxt.setText(getActivity().getString(R.string.notavailable));
			}
			tmptxt = (TextView)getView().findViewById(R.id.light);
			if(envJSON.hasLight()){
				tmptxt.setText(""+envJSON.getLight());
			} else {
				tmptxt.setText(getActivity().getString(R.string.notavailable));
			}
			tmptxt = (TextView)getView().findViewById(R.id.pressure);
			if(envJSON.hasPressure()){
				tmptxt.setText(""+envJSON.getPressure());
			} else {
				tmptxt.setText(getActivity().getString(R.string.notavailable));
			}
			tmptxt = (TextView)getView().findViewById(R.id.tempurature);
			if(envJSON.hasTempurature()){
				tmptxt.setText(""+envJSON.getTempurature());
			} else {
				tmptxt.setText(getActivity().getString(R.string.notavailable));
			}
			tmptxt = (TextView)getView().findViewById(R.id.humidity);
			if(envJSON.hasHumidity()){
				tmptxt.setText(""+envJSON.getHumidity());
			} else {
				tmptxt.setText(getActivity().getString(R.string.notavailable));
			}
			
			ImageView img = (ImageView)getView().findViewById(R.id.imageView1);
			img.setImageURI(_imageUri);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
