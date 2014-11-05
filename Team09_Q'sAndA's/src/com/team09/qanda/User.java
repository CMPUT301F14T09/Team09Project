package com.team09.qanda;

import java.io.Serializable;

import android.provider.Settings;

public class User extends QModel<QView> implements Serializable {
	private String name;
	private String deviceID;
	
	public User() {
		this.name="User"+System.currentTimeMillis();
		this.deviceID = Settings.Secure.ANDROID_ID;
	}
	
	public User(String name) {
		this.name=name;
		this.deviceID = Settings.Secure.ANDROID_ID;
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