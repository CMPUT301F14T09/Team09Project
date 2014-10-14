package com.team09.qanda.test;

import com.team09.qanda.User;

import junit.framework.TestCase;

public class UserTest extends TestCase {

	//Use Case #23: As an author, I set my username.
	public void testSetUsernameDefault(){
		User user = new User();
		assertTrue(!user.getName().equalsIgnoreCase(""));	
	}
	
	//Use Case #23: As an author, I set my username.
	public void testSetUsername(){
		User user = new User("testuser");
		assertTrue(user.getName().equals("testuser"));
	}
}
