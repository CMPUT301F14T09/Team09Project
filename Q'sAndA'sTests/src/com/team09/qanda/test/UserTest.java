package com.team09.qanda.test;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.team09.qanda.models.User;

import junit.framework.TestCase;

public class UserTest extends InstrumentationTestCase {

	private Context context;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		context = getInstrumentation().getContext();
	}

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
