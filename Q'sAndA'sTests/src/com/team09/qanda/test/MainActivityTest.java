package com.team09.qanda.test;

import java.util.ArrayList;
import java.util.Arrays;

import com.team09.qanda.MainActivity;
import com.team09.qanda.Post;
import com.team09.qanda.PostController;
import com.team09.qanda.QuestionThread;
import com.team09.qanda.QuestionThreadController;
import com.team09.qanda.ThreadListAdapter;
import com.team09.qanda.ThreadList;
import com.team09.qanda.ThreadListController;
import com.team09.qanda.User;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
	ThreadListAdapter adapter;
	MainActivity mainAct;
	ArrayAdapter<String> spinner;
	
	public MainActivityTest() {
		super(MainActivity.class);
		
	}
	@Override
	public void setUp() throws Exception{
		super.setUp();
		mainAct=getActivity();
		adapter=mainAct.getAdapter();
		spinner=mainAct.getSpinnerAdapter();
	}
	@Override
	public void tearDown(){
		adapter.clear();
	}
	//Use Case 9
	public void testSortbyHasPictures(){
		ThreadList questions=new ThreadList();
		//get the position of the  "HasPictures" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Has Pictures");
		QuestionThread NoPicture=new QuestionThread(new Post(new User(),"No picture?"));
		// Make ThreadListController
		ThreadListController tlc = new ThreadListController(questions);
		Post pic1=new Post(new User(),"Hello?");
		pic1.setImage();
		Post pic2=new Post(new User(),"Does this work?");
		pic2.setImage();
		// Add threads using controller
		tlc.addThread(new QuestionThread(pic1));
		tlc.addThread(new QuestionThread(pic2));
		tlc.addThread(NoPicture);
		//choose Sorting Option
		mainAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(adapter.getPosition(NoPicture),0);
	}
	
	//Use Case 10.1
	public void testsortByMostRecent(){
		ThreadList questions=new ThreadList();
		//get the position of the  "HasPictures" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Most Recent");
		// Make ThreadListController
		ThreadListController tlc = new ThreadListController(questions);
		QuestionThread first=new QuestionThread(new Post(new User(),"am I first?"));
		QuestionThread second=new QuestionThread(new Post(new User(),"am I second?"));
		QuestionThread third=new QuestionThread(new Post(new User(),"am I third?"));
		tlc.addThread(third);
		tlc.addThread(second);
		tlc.addThread(first);
		//choose Sorting Option
		mainAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(adapter.getPosition(third),2);
	}
	
	//Use Case 10.2
	public void testsortByOldest(){
		ThreadList questions=new ThreadList();
		//get the position of the  "Oldest" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Oldest");
		QuestionThread first=new QuestionThread(new Post(new User(),"am I first?"));
		QuestionThread second=new QuestionThread(new Post(new User(),"am I second?"));
		QuestionThread third=new QuestionThread(new Post(new User(),"am I third?"));
		// Make ThreadListController
		ThreadListController tlc = new ThreadListController(questions);
		tlc.addThread(first);
		tlc.addThread(second);
		tlc.addThread(third);

		//choose Sorting Option
		mainAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(adapter.getPosition(third),0);

	}
	
	//Use Case 10.3
	public void testsortByMostUpVotes(){
		ThreadList questions=new ThreadList();
		PostController controller;
		
		//get the position of the  "Most Upvoted" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Most Upvoted");
		
		Post txt=new Post(new User(),"Do upvotes work?");
		controller=new PostController(txt);
	    controller.addUp();
	    controller.addUp();
		
	    Post txt2=new Post(new User(),"Do upvotes work?");
	    controller=new PostController(txt2);
	    controller.addUp();
		Post txt3=new Post(new User(),"Do upvotes work?");
		QuestionThread most=new QuestionThread(txt);
		QuestionThread middle=new QuestionThread(txt2);
		QuestionThread least=new QuestionThread(txt3);
		// Make ThreadListController
		ThreadListController tlc = new ThreadListController(questions);
		tlc.addThread(least);
		tlc.addThread(middle);
		tlc.addThread(most);

		//choose Sorting Option
		mainAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(adapter.getPosition(most),0);
	}
	
	//Use Case 10.4
	public void testsortByLeastUpvotes(){
		ThreadList questions=new ThreadList();
		//get the position of the  "Least Upvoted" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Least Upvoted");
		Post txt=new Post(new User(),"Do upvotes work?");
		
		ArrayList<User> txtUps = txt.getUpsList();
		User user1 = new User();
		User user2 = new User();
		txtUps.add(user1);
		txtUps.add(user2);
		txt.setUps(txtUps);
		
		Post txt2=new Post(new User(),"Do upvotes work?");
		
		ArrayList<User> txt2Ups = txt2.getUpsList();
		txt2Ups.add(user1);
		txt2.setUps(txt2Ups);
		
		Post txt3=new Post(new User(),"Do upvotes work?");
		
		QuestionThread most=new QuestionThread(txt);
		QuestionThread middle=new QuestionThread(txt2);
		QuestionThread least=new QuestionThread(txt3);
		
		// Make ThreadListController
		ThreadListController tlc = new ThreadListController(questions);
		tlc.addThread(most);
		tlc.addThread(middle);
		tlc.addThread(least);

		//choose Sorting Option
		mainAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(adapter.getPosition(most),0);
	}
	
	@UiThreadTest
	public void testListQuestions() {
		MainActivity ma = getActivity();
		int oldLength = ma.getAdapter().getCount();
		
		assertEquals(2, oldLength);
		
		ViewAsserts.assertOnScreen(ma.getWindow().getDecorView(), ma.findViewById(com.team09.qanda.R.id.MainListView));
		
	}
	
	@UiThreadTest
	public void testSpinnerAdapter() {
		//TODO
	}
	
	//Use Case #23: As an author, I set my username.
	//ensures the alertdialog actually shows up
	public void testSetUsername() throws Throwable{
		
		Intent intent = new Intent();
		setActivityIntent(intent);
		
		runTestOnUiThread(new Runnable() {
			
			public void run() {
				mainAct.setUsername();
				assertNotNull(mainAct.findViewById(com.team09.qanda.R.id.usernameInput));
				assertNotNull(mainAct.findViewById(com.team09.qanda.R.id.usernamePrompt));
			}
		});
		
	}
}
