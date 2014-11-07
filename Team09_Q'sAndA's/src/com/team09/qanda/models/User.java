package com.team09.qanda.models;

import java.io.Serializable;

import com.team09.qanda.views.QView;

import android.content.Context;
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
	
	public User(Context context) {
		this.name="User"+System.currentTimeMillis();
		this.deviceID = Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
	}
	
	public User(Context context, String name) {
		this.name=name;
		this.deviceID = Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);


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