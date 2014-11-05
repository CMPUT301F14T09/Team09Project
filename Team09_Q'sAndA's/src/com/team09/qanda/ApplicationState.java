package com.team09.qanda;

public class ApplicationState {
	private static ApplicationState userState = null;
	private static User user;
	
	protected ApplicationState(){
		
	}
	
   public static ApplicationState getInstance() {
	      if(userState == null) {
	    	  userState = new ApplicationState();
	      }
	      return userState;
   }
   
   public User getUser(){
	   return user;
   }
   
   public void setUser(User user){
	   this.user = user;
   }
}
