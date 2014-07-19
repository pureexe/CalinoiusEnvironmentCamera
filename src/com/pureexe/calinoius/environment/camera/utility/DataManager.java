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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DataManager {
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor editor;
	private SharedPreferences defaultPref;
	private SharedPreferences.Editor defaultEditor;
	private static Context context;
	public DataManager(Context activity)
	{ 
		sharedPref = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		context = activity;
		defaultPref = PreferenceManager.getDefaultSharedPreferences(context);
		defaultEditor = defaultPref.edit();
	}
	/*** Easy Define ***/
	public void setBool(String name,boolean in)
	{
		editor.putBoolean(name, in);
		editor.commit();
	}
	public boolean getBool(String name)
	{
		return sharedPref.getBoolean(name, false);
	}
	public boolean getBool(String name,boolean defaultVar)
	{
		return sharedPref.getBoolean(name, defaultVar);
	}
	public void setInt(String name,int in)
	{
		editor.putInt(name, in);
		editor.commit();
	}
	public int getInt(String name)
	{
		return sharedPref.getInt(name,0);
	}
	public int getInt(String name,int defaultVar)
	{
		return sharedPref.getInt(name,defaultVar);
	}
	public void setFloat(String name,float in)
	{
		editor.putFloat(name, in);
		editor.commit();
	}
	public float getFloat(String name)
	{
		return sharedPref.getFloat(name,0);
	}
	public float getFloat(String name,float defaultVar)
	{
		return sharedPref.getFloat(name,defaultVar);
	}
	public void setString(String name,String in)
	{
		editor.putString(name, in);
		editor.commit();
	}
	public String getString(String name)
	{
		return sharedPref.getString(name,null);
	}
	public String getString(String name,String defaultVar)
	{
		return sharedPref.getString(name,defaultVar);
	}
	public void setLong(String name,long in)
	{
		editor.putLong(name, in);
		editor.commit();
	}
	public long getLong(String name){
		return sharedPref.getLong(name, 0);
	}
	public long getLong(String name,long defaultVar){
		return sharedPref.getLong(name, defaultVar);
	}
	

	
	/** get Setting **/
	public String getSettingString(String key){
        return defaultPref.getString(key, "");
    }
	public boolean getSettingBoolean(String key){
        return defaultPref.getBoolean(key,false);
    }
	public boolean getSettingBooleanTrue(String key){
        return defaultPref.getBoolean(key,true);
    }
	public boolean getSettingBooleanFalse(String key){
        return defaultPref.getBoolean(key,false);
    }

	
}
