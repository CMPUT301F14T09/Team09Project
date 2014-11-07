package com.team09.qanda.test;

import android.content.Context;

import com.team09.qanda.models.User;

import junit.framework.TestCase;

public class UserTest extends TestCase {

	private Context context;

	//Use Case #23: As an author, I set my username.
	public void testSetUsernameDefault(){
		User user = new User(context);
		assertTrue(!user.getName().equalsIgnoreCase(""));	
	}
	
	//Use Case #23: As an author, I set my username.
	public void testSetUsername(){
		//TODO: modify user class to check for existing file containing username
		User user = new User(context,"testuser");
		assertTrue(user.getName().equals("testuser"));
	}
}
