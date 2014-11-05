package com.team09.qanda;

public class ApplicationState {
	private static ApplicationState appState = null;
	private static User user;
	
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
