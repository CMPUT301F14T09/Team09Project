package com.team09.qanda.test;

import com.team09.qanda.MainActivity;

import android.test.ActivityInstrumentationTestCase2;

public class MyTests extends ActivityInstrumentationTestCase2<MainActivity> {

	public MyTests(){
		super(MainActivity.class);
	}
	
	/*
	public void testPushNew(){
		ElasticSearchHandler esh = new ElasticSearchHandler(“URL”);

		if (Model.getLocalEntries != NULL){
			esh.add(Model.getLocalEntries); //Add to server
			assertTrue(esh.query().contains(Model.getLocalEntries));
		}
	}*/
	
	/*
	public void testDefaultCommentOrder(){
		for (Question q: questions){
			//add some comment not in date order
		}
	
	    for (Question q: questions){
	        comm = q.getAnswers();
	            for(int i = 1; i < comm.size(); i++){
		      if(comm[i-1].getCreateDate().compareTo(comm[i].getCreateDate()) > 1) {
					fail();
		      }
	            }
	        }		
	    }
	}*/

	/*
	public void testSetUsernameDefault(){
		user.setUsername();
		assertTrue(user.getUsername().equals("default"));	
	}*/
	
	/*
	public void testSetUsername(){
		user.setUsername("testuser");
		assertTrue(user.getUsername().equals("testuser"));	
	}*/
}
