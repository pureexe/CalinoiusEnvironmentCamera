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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pureexe.calinoius.environment.camera.utility.CameraPreview;
import com.pureexe.calinoius.environment.camera.utility.DataManager;
import com.pureexe.calinoius.environment.camera.utility.EnvSensorJSON;
import com.pureexe.calinoius.environment.camera.R;

public class EnvironmentCameraFragment extends Fragment implements SensorEventListener {
	public EnvironmentCameraFragment() {
	}

	private SensorManager sensorManager;
	private Sensor sensorAcceleration;
	private Sensor sensorGravity;
	private Sensor sensorGyroscope;
	private Sensor sensorMagneticField;
	private Sensor sensorOrientation;
	private Sensor sensorLight;
	private Sensor sensorPressure;
	private Sensor sensorTempurature;
	private Sensor sensorHumidity;
	private boolean hasAcceleration;
	
	private boolean hasGravity;
	private boolean hasGyroscope;
	private boolean hasMagneticField;
	private boolean hasOrientation;
	private boolean hasLight;
	private boolean hasPressure;
	private boolean hasTempurature;
	private boolean hasHumidity;
	
	
	private float[] Acceleration;
	private float[] Gravity;
	private float[] Gyroscope;
	private float[] MagneticField;
	private float[] Orientation;
	private float[] Light;
	private float[] Pressure;
	private float[] Tempurature;
	private float 	BatteryTempurature;
	private float[] Humidity;
	
	private Vector AccelerationList;
	private Vector GravityList;
	private Vector GyroscopeList;
	private Vector MagneticFieldList;
	private Vector OrientationList;
	private Vector LightList;
	private Vector PressureList;
	private Vector TempuratureList;
	private Vector BatteryTempuratureList;
	private Vector HumidityList;
	
	private float[] AccelerationSum;
	private float[] GravitySum;
	private float[] GyroscopeSum;
	private float[] MagneticFieldSum;
	private float[] OrientationSum;
	private float[] LightSum;
	private float[] PressureSum;
	private float[] TempuratureSum;
	private float[]	BatteryTempuratureSum;
	private float[] HumiditySum;
	private int sensorRepeat;
	
	private TextView displayTxt;
	private CameraPreview mPreview;
	private FrameLayout cameraFramePreview;
	private Camera mCamera;
	private PictureCallback jpgePictureCallback;
	private LocationManager locationManager;
	private LocationListener locationListener;
	protected long currentTime;
	
	
	private DataManager dataManager;
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	private static File getOutputMediaFile(int type){
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "Calinoius");
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	        	return null;
	        }
	    }

	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }
	    return mediaFile;
	}
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			  // Low sensitive so i'm doesn't add sensor repeat
		      BatteryTempurature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)/10.0f;
		}
	  };
	
	  public void shootSound()
	  {
	      AudioManager meng = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
	      int volume = meng.getStreamVolume( AudioManager.STREAM_NOTIFICATION);
	      if (volume != 0)
	      {
	    	  MediaPlayer _shootMP = null;
			if (_shootMP == null)
	              _shootMP = MediaPlayer.create(getActivity(), Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
	          if (_shootMP != null)
	              _shootMP.start();
	      }
	  }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		sensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
		sensorAcceleration = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorGravity =  sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sensorOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorLight  = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		sensorPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
		sensorTempurature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
	
		// Init vector for storage sensor values history
		AccelerationList = new Vector();
		GravityList = new Vector();
		GyroscopeList = new Vector();
		MagneticFieldList = new Vector();
		OrientationList = new Vector();
		LightList = new Vector();
		PressureList = new Vector();
		TempuratureList = new Vector();
		BatteryTempuratureList = new Vector();
		HumidityList = new Vector();

		//Init Sum of vector for use sliding windows Algorithm
		AccelerationSum = new float[3];
		GravitySum = new float[3];
		GyroscopeSum = new float[3];
		MagneticFieldSum = new float[3];
		OrientationSum = new float[3];
		LightSum = new float[3];
		PressureSum = new float[3];
		TempuratureSum = new float[3];
		BatteryTempuratureSum = new float[3];
		HumiditySum = new float[3];
		
		//Init Sensor result array
		Acceleration = new float[3];
		Gravity = new float[3];
		Gyroscope = new float[3];
		MagneticField = new float[3];
		Orientation = new float[3];
		Light = new float[3];
		Pressure = new float[3];
		Tempurature = new float[3];
		Humidity = new float[3];
		
		// DataManager is Utility class for fetch sharepreference data
		dataManager = new DataManager(getActivity()); 
		// Callback When take picture
		jpgePictureCallback = new PictureCallback() {
		    @Override
		    public void onPictureTaken(byte[] data, Camera camera) {
		    	shootSound();
		    	
		       File pictureFile = (File)getOutputMediaFile(MEDIA_TYPE_IMAGE);
		        if (pictureFile == null){
		         	Toast.makeText(getActivity(), "NULL Eject", Toast.LENGTH_LONG).show();
		        	return;
		        }
		        
		        try {
		            FileOutputStream fos = new FileOutputStream(pictureFile);
		            fos.write(data);
		            fos.close();
		            addImageGallery(pictureFile);
		            EnvSensorJSON envJson = new EnvSensorJSON();
		            ExifInterface exif = new ExifInterface(pictureFile.toString());
		            envJson.setSoftware();
		            envJson.setSoftwareVersion();
		            envJson.setResearcher(dataManager.getString("Researcher"));
		            if(hasAcceleration)
		            	envJson.setAcceleration(Acceleration);
		            if(hasGravity)
		            	envJson.setGravity(Gravity);
		            if(hasGyroscope)
		            	envJson.setGyroscope(Gyroscope);
		            if(hasMagneticField)
		            	envJson.setMagneticField(MagneticField);
		            if(hasOrientation)
		            	envJson.setOrientation(Orientation);
		            if(hasHumidity)
		            	envJson.setHumidity(Humidity);
		            if(hasLight)
		            	envJson.setLight(Light);
		            if(hasLight)
		            	envJson.setPressure(Pressure);
		            if(hasTempurature){
		            	envJson.setTempurature(Tempurature);
		            }else {
		            	if(dataManager.getSettingBooleanTrue(SettingPreferenceFragment.THERMISTER_INTEGRATION)){
		            		float[] setTempuratureC=new float[1];
		            		setTempuratureC[0]=BatteryTempurature;
		            		envJson.setTempurature(setTempuratureC);
		            	}
		            }
		            exif.setAttribute("UserComment", envJson.getJSON().toString());
		            exif.saveAttributes();
		            
		            mCamera.startPreview();
		        } catch (FileNotFoundException e) {
		        } catch (IOException e) {
		        }

		    }
		};
		// Location Listener
		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		    	
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}

		  };
		super.onCreate(savedInstanceState);
	}

	private void addImageGallery( File file ) {
	    ContentValues values = new ContentValues();
	    values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
	    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
	    getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}
	@Override
	public void onPause() {
		getActivity().unregisterReceiver(this.mBatInfoReceiver);
		sensorManager.unregisterListener(this);
		cameraFramePreview.removeAllViews();
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
		//locationManager.removeUpdates(locationListener);
		super.onPause();
	}

	@Override
	public void onResume() {
		getActivity().registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		hasAcceleration = sensorManager.registerListener(this, sensorAcceleration,SensorManager.SENSOR_DELAY_NORMAL);
		hasGravity = sensorManager.registerListener(this, sensorGravity,SensorManager.SENSOR_DELAY_NORMAL);
		hasGyroscope = sensorManager.registerListener(this, sensorGyroscope,SensorManager.SENSOR_DELAY_NORMAL);
		hasMagneticField = sensorManager.registerListener(this, sensorMagneticField,SensorManager.SENSOR_DELAY_NORMAL);
		hasOrientation = sensorManager.registerListener(this, sensorOrientation,SensorManager.SENSOR_DELAY_NORMAL);
		hasLight = sensorManager.registerListener(this, sensorLight,SensorManager.SENSOR_DELAY_NORMAL);
		hasPressure = sensorManager.registerListener(this, sensorPressure,SensorManager.SENSOR_DELAY_NORMAL);
		hasTempurature = sensorManager.registerListener(this, sensorTempurature,SensorManager.SENSOR_DELAY_NORMAL);
		hasHumidity = sensorManager.registerListener(this, sensorHumidity,SensorManager.SENSOR_DELAY_NORMAL);
		sensorRepeat = dataManager.getInt("SesnorRepeat", 5);
		 mCamera = CameraPreview.getCameraInstance();
	     if(mCamera==null){
	    	 Toast.makeText(getActivity(), "Can\'t connect camera", Toast.LENGTH_LONG).show();
	    	 getActivity().finish();

	     }
	     Camera.Parameters camParam = mCamera.getParameters();
	     List<Camera.Size> picture_size= camParam.getSupportedPictureSizes();    
	 
	     camParam.setPictureSize(picture_size.get(0).width, picture_size.get(0).height);
	     camParam.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
	     mCamera.setParameters(camParam);
	     mPreview = new CameraPreview(getActivity(), mCamera);
	     cameraFramePreview.addView(mPreview);
	     if(dataManager.getSettingBoolean(SettingPreferenceFragment.AIM_POINT)){
	    	 getView().findViewById(R.id.aim_point).setVisibility(View.VISIBLE);
	     } else {
	    	 getView().findViewById(R.id.aim_point).setVisibility(View.GONE);
	     }
	     //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		super.onResume();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View decorView = getActivity().getWindow().getDecorView();
		decorView.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {
			public void onSystemUiVisibilityChange(int visibility) {
				if(visibility == View.SYSTEM_UI_FLAG_VISIBLE) {
					currentTime = System.currentTimeMillis();
				}
			}
		});
		View rootView = inflater.inflate(R.layout.fragment_environmentcamera, container,
				false);
		getActivity().setRequestedOrientation(0);
		cameraFramePreview = (FrameLayout) rootView.findViewById(R.id.camera_preview);
		cameraFramePreview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mCamera.autoFocus(null);
				return false;
			}
		});
		
		
		ImageView shutter = (ImageView) rootView.findViewById(R.id.imageView1);
		shutter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCamera.takePicture(null, null, jpgePictureCallback);
				mCamera.autoFocus(null);
			}
		});
		
		
		
		return rootView;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			Acceleration = event.values;
			/*
			AccelerationSum[0] +=event.values[0];
			AccelerationSum[1] +=event.values[1];
			AccelerationSum[2] +=event.values[2];
			AccelerationList.add(event.values);
			if(AccelerationList.size()>sensorRepeat){
				float[] prevVar =(float[]) AccelerationList.remove(0);
				AccelerationSum[0]-=prevVar[0];
				AccelerationSum[1]-=prevVar[1];
				AccelerationSum[2]-=prevVar[2];
			}
			Acceleration[0] = AccelerationSum[0]/(float)AccelerationList.size();
			Acceleration[1] = AccelerationSum[1]/(float)AccelerationList.size();
			Acceleration[2] = AccelerationSum[2]/(float)AccelerationList.size();
			*/
		}
		if(event.sensor.getType() == Sensor.TYPE_GRAVITY){
			Gravity = event.values;
			/*
			GravitySum[0] +=event.values[0];
			GravitySum[1] +=event.values[1];
			GravitySum[2] +=event.values[2];
			GravityList.add(event.values);
			if(GravityList.size()>sensorRepeat){
				float[] prevVar =(float[]) GravityList.remove(0);
				GravitySum[0]-=prevVar[0];
				GravitySum[1]-=prevVar[1];
				GravitySum[2]-=prevVar[2];
			}
			Gravity[0] = GravitySum[0]/(float)GravityList.size();
			Gravity[1] = GravitySum[1]/(float)GravityList.size();
			Gravity[2] = GravitySum[2]/(float)GravityList.size();
			*/
		}
		if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
			Gyroscope = event.values;
			/*
			GyroscopeSum[0] +=event.values[0];
			GyroscopeSum[1] +=event.values[1];
			GyroscopeSum[2] +=event.values[2];
			GyroscopeList.add(event.values);
			if(GyroscopeList.size()>sensorRepeat){
				float[] prevVar =(float[]) GyroscopeList.remove(0);
				GyroscopeSum[0]-=prevVar[0];
				GyroscopeSum[1]-=prevVar[1];
				GyroscopeSum[2]-=prevVar[2];
			}
			Gyroscope[0] = GyroscopeSum[0]/(float)GyroscopeList.size();
			Gyroscope[1] = GyroscopeSum[1]/(float)GyroscopeList.size();
			Gyroscope[2] = GyroscopeSum[2]/(float)GyroscopeList.size();
			*/
		}
		if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
			MagneticField = event.values;
			/*
			MagneticFieldSum[0] +=event.values[0];
			MagneticFieldSum[1] +=event.values[1];
			MagneticFieldSum[2] +=event.values[2];
			MagneticFieldList.add(event.values);
			if(MagneticFieldList.size()>sensorRepeat){
				float[] prevVar =(float[]) MagneticFieldList.remove(0);
				MagneticFieldSum[0]-=prevVar[0];
				MagneticFieldSum[1]-=prevVar[1];
				MagneticFieldSum[2]-=prevVar[2];
			}
			MagneticField[0] = MagneticFieldSum[0]/(float)MagneticFieldList.size();
			MagneticField[1] = MagneticFieldSum[1]/(float)MagneticFieldList.size();
			MagneticField[2] = MagneticFieldSum[2]/(float)MagneticFieldList.size();
			*/
		}
		if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){
			Orientation = event.values;
			/*
			OrientationSum[0] +=event.values[0];
			OrientationSum[1] +=event.values[1];
			OrientationSum[2] +=event.values[2];
			OrientationList.add(event.values);
			if(OrientationList.size()>sensorRepeat){
				float[] prevVar =(float[]) OrientationList.remove(0);
				OrientationSum[0]-=prevVar[0];
				OrientationSum[1]-=prevVar[1];
				OrientationSum[2]-=prevVar[2];
			}
			Orientation[0] = OrientationSum[0]/(float)OrientationList.size();
			Orientation[1] = OrientationSum[1]/(float)OrientationList.size();
			Orientation[2] = OrientationSum[2]/(float)OrientationList.size();
			*/
		}
		if(event.sensor.getType() == Sensor.TYPE_PRESSURE){
			Pressure = event.values;
			/*
			PressureSum[0] +=event.values[0];
			PressureList.add(event.values);
			if(PressureList.size()>sensorRepeat){
				float[] prevVar =(float[]) PressureList.remove(0);
				PressureSum[0]-=prevVar[0];
			}
			Pressure[0] = PressureSum[0]/(float)PressureList.size();
			*/
		}
		if(event.sensor.getType() == Sensor.TYPE_LIGHT){
			Light = event.values;
			/*
			LightSum[0] +=event.values[0];
			LightList.add(event.values);
			if(LightList.size()>sensorRepeat){
				float[] prevVar =(float[]) LightList.remove(0);
				LightSum[0]-=prevVar[0];
			}
			Light[0] = LightSum[0]/(float)LightList.size();
			*/
		}
		if(event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
			Tempurature = event.values;
			/*
			TempuratureSum[0] +=event.values[0];
			TempuratureList.add(event.values);
			if(TempuratureList.size()>sensorRepeat){
				float[] prevVar =(float[]) TempuratureList.remove(0);
				TempuratureSum[0]-=prevVar[0];
			}
			Tempurature[0] = TempuratureSum[0]/(float)TempuratureList.size();
			*/
		}
		if(event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY){
			Humidity = event.values;
			/*
			HumiditySum[0] +=event.values[0];
			HumidityList.add(event.values);
			if(HumidityList.size()>sensorRepeat){
				float[] prevVar =(float[]) HumidityList.remove(0);
				HumiditySum[0]-=prevVar[0];
			}
			Humidity[0] = HumiditySum[0]/(float)HumidityList.size();
			*/
		}	
	}
	
	
	
}