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

package com.pureexe.calinoius.environment.camera.utility;

import org.json.JSONException;
import org.json.JSONObject;

public class EnvSensorJSON {
	private JSONObject JSONobj;
	/*
	public float Acceleration[] = new float[3];
	public float Gravity[] = new float[3];
	public float Gyroscope[] = new float[3];
	public float Orientation[] = new float[3];
	public float MagneticField[] = new float[3];
	public float Light;
	public float Pressure;
	public float Humidity;
	public float Temperature;
	*/
	
	public EnvSensorJSON(){
		JSONobj = new JSONObject();
	}
	public EnvSensorJSON(String jsonuri){
		if(jsonuri==null){
			JSONobj = new JSONObject();
			return;
		}
		try {
			JSONobj = new JSONObject(jsonuri);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	public void setSoftware(){
		try {
			JSONobj.put("Software", "Calinoius Physic Environment");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void setSoftwareVersion(){
		try {
			JSONobj.put("SoftwareVersion", "1.0.0");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void setAcceleration(float[] values){
		try {
			JSONobj.put("AccelerationX", values[0]);
			JSONobj.put("AccelerationY", values[1]);
			JSONobj.put("AccelerationZ", values[2]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void setGravity(float[] values){
		try {
			JSONobj.put("GravityX", values[0]);
			JSONobj.put("GravityY", values[1]);
			JSONobj.put("GravityZ", values[2]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void setGyroscope(float[] values){
		try {
			JSONobj.put("GyroscopeX", values[0]);
			JSONobj.put("GyroscopeY", values[1]);
			JSONobj.put("GyroscopeZ", values[2]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void setOrientation(float[] values){
		try {
			JSONobj.put("OrientationX", values[0]);
			JSONobj.put("OrientationY", values[1]);
			JSONobj.put("OrientationZ", values[2]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void setMagneticField(float[] values){
		try {
			JSONobj.put("MagneticFieldX", values[0]);
			JSONobj.put("MagneticFieldY", values[1]);
			JSONobj.put("MagneticFieldZ", values[2]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void setLight(float[] value){
		try {
			JSONobj.put("Light", value[0]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void setPressure(float[] value){
		try {
			JSONobj.put("Pressure", value[0]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void setHumidity(float[] humidity){
		try {
			JSONobj.put("Humidity", humidity);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void setTempurature(float[] value){
		try {
			JSONobj.put("Tempurature", value[0]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void setUseTempuratureFromBattery(boolean value){
		try {
			JSONobj.put("UseTempuratureFromBattery", value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void setResearcher(String name){
		try {
			JSONobj.put("Researcher",name);
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}
	public boolean hasSupportSoftware(){
		try {
			String sw = JSONobj.getString("Software");
			if(sw.equals("Calinoius Physic Environment")){
				return true;
			}
			else {
				return false;
			}
		} catch (JSONException e) {
			return false;
		}
	}
	public boolean hasAcceleration(){
		try {
			JSONobj.get("AccelerationX");
			return true;
		} catch (JSONException e) {
			return false;
		}
	}
	public boolean hasGravity(){
		try {
			JSONobj.get("GravityX");
			return true;
		} catch (JSONException e) {
			return false;
		}
	}
	public boolean hasGyroscope(){
		try {
			JSONobj.get("GyroscopeX");
			return true;
		} catch (JSONException e) {
			return false;
		}
	}
	public boolean hasOrientation(){
		try {
			JSONobj.get("OrientationX");
			return true;
		} catch (JSONException e) {
			return false;
		}
	}
	public boolean hasMagneticField(){
		try {
			JSONobj.get("MagneticFieldX");
			return true;
		} catch (JSONException e) {
			return false;
		}
	}
	public boolean hasLight(){
		try {
			JSONobj.get("Light");
			return true;
		} catch (JSONException e) {
			return false;
		}
	}
	public boolean hasTempurature(){
		try {
			JSONobj.get("Tempurature");
			return true;
		} catch (JSONException e) {
			return false;
		}
	}
	public boolean hasPressure(){
		try {
			JSONobj.get("Pressure");
			return true;
		} catch (JSONException e) {
			return false;
		}
	}
	public boolean hasHumidity(){
		try {
			JSONobj.get("Humidity");
			return true;
		} catch (JSONException e) {
			return false;
		}
	}
	public String getResearcher(){
		String name = "Unknown";
		try {
			name = JSONobj.getString("Researcher");
		} catch (JSONException e) {
					e.printStackTrace();
		}
		return name;
	}
	public float[] getAcceleration(){
		float[] ans = new float[3];
		try {
			ans[0]=(float) JSONobj.getDouble("AccelerationX");
			ans[1]=(float) JSONobj.getDouble("AccelerationY");
			ans[2]=(float) JSONobj.getDouble("AccelerationZ");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public float[] getGravity(){
		float[] ans = new float[3];
		try {
			ans[0]=(float) JSONobj.getDouble("GravityX");
			ans[1]=(float) JSONobj.getDouble("GravityY");
			ans[2]=(float) JSONobj.getDouble("GravityZ");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public float[] getGyroscope(){
		float[] ans = new float[3];
		try {
			ans[0]=(float) JSONobj.getDouble("GyroscopeX");
			ans[1]=(float) JSONobj.getDouble("GyroscopeY");
			ans[2]=(float) JSONobj.getDouble("GyroscopeZ");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public float[] getOrientation(){
		float[] ans = new float[3];
		try {
			ans[0]=(float) JSONobj.getDouble("OrientationX");
			ans[1]=(float) JSONobj.getDouble("OrientationY");
			ans[2]=(float) JSONobj.getDouble("OrientationZ");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public float[] getMagneticField(){
		float[] ans = new float[3];
		try {
			ans[0]=(float) JSONobj.getDouble("MagneticFieldX");
			ans[1]=(float) JSONobj.getDouble("MagneticFieldY");
			ans[2]=(float) JSONobj.getDouble("MagneticFieldZ");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public float getLight(){
		try {
			return (float) JSONobj.getDouble("Light");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public float getPressure(){
		try {
			return (float) JSONobj.getDouble("Pressure");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public float getHumidity(){
		try {
			return (float) JSONobj.getDouble("Humidity");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public float getTempurature(){
		try {
			return (float) JSONobj.getDouble("Tempurature");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public boolean isUseTempuratureFromBattery(){
		try {
			return JSONobj.getBoolean("UseTempuratureFromBattery");
		} catch (JSONException e) {
			return false;
		}
	}
	public String getJSONstr(){
		return getJSON().toString();
	}
	public JSONObject getJSON() {
		return JSONobj;
	}
}
