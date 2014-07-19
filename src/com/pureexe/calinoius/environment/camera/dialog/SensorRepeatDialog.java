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

package com.pureexe.calinoius.environment.camera.dialog;

import com.pureexe.calinoius.environment.camera.R;
import com.pureexe.calinoius.environment.camera.utility.DataManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

public class SensorRepeatDialog extends DialogPreference{

	private static DataManager dataManager;
	private Context mContext;
	public SensorRepeatDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
		dataManager = new DataManager(context);		
		setSummary(dataManager.getInt("SensorRepeat",5)+" "+context.getString(R.string.sensor_repeat_des));
		mContext = context;
	}
	
	@Override
	public void onClick() {
		setSensorRepeat(mContext,this);
		//Toast.makeText(getContext(), "Click", Toast.LENGTH_LONG).show();
	}

	public static void setSensorRepeat(final Context context,final DialogPreference dialogPref){
		AlertDialog.Builder	builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.sensor_repeat));
		//builder.setMessage(context.getString(R.string.set_data_plan_box_des));
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rootView = inflater.inflate(R.layout.dialog_sensorrepeat, null);
		
		final NumberPicker SensorRepeatPicker = (NumberPicker)rootView.findViewById(R.id.sensorrepeat_picker);
	
        final String[] SensorRepeatDisplay = new String[100];
        for(int i=0; i<SensorRepeatDisplay.length; i++) {
        		SensorRepeatDisplay[i] = ""+(i+1);
        }
        SensorRepeatPicker.setMaxValue(99);
        SensorRepeatPicker.setDisplayedValues(SensorRepeatDisplay);
		dataManager = new DataManager(context);
        int repeattime = dataManager.getInt("SensorRepeat",5);
		SensorRepeatPicker.setValue(repeattime-1);
		SensorRepeatPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		builder.setView(rootView);

		
		builder.setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
		
		public void onClick(DialogInterface dialog, int whichButton) {
						int value = 0;
						value = SensorRepeatPicker.getValue();
						dataManager.setInt("SensorRepeat", value+1);
						if(dialogPref!=null)
							dialogPref.setSummary(dataManager.getInt("SensorRepeat",5)+" "+context.getString(R.string.sensor_repeat_des));
			}
		});
		builder.show();
	}
}
	
