package com.team09.qanda;

import java.io.Serializable;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.provider.Settings.Secure;

/**
 * 
 * Class to store the devices user. Stores username and deviceID to be used for creating posts and upvoting.
 * If no name provided, username is generated.
 * 
 */

public class User extends QModel<QView> implements Serializable {
	private String name;
	private String deviceID;
	
	public User() {
		this.name="User"+System.currentTimeMillis();
//		String deviceID = Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
	}
	
	public User(String name) {
		this.name=name;
		this.deviceID = android.provider.Settings.Secure.ANDROID_ID;
//		String deviceID = Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);


	}
	
	public void setName(String name) {
		this.name=name;
		notifyViews();
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getID() {
		return this.deviceID;
	}
}