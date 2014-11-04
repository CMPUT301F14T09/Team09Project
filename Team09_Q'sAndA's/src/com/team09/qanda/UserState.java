package com.team09.qanda;

public class UserState {
	private static UserState userState = null;
	private static User user;
	
	protected UserState(){
		
	}
	
   public static UserState getInstance() {
	      if(userState == null) {
	    	  userState = new UserState();
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
