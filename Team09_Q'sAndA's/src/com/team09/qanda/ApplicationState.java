package com.team09.qanda;

import com.team09.qanda.models.User;

/**
 * 
 * Stores any user preferences.
 * 
 */

public class ApplicationState {
	private static ApplicationState appState = null;
	private User user;
	
	protected ApplicationState(){
		
	}
	
	public static ApplicationState getInstance() {
		if(appState == null) {
			appState = new ApplicationState();
		}
		return appState;
	}
   
	public User getUser(){
		return user;
	}
   
	public void setUser(User user){
		this.user = user;
	}
}
