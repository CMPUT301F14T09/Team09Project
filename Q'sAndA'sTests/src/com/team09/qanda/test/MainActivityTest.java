package com.team09.qanda.test;

import java.util.ArrayList;

import com.team09.qanda.R;
import com.team09.qanda.ThreadListAdapter;
import com.team09.qanda.controllers.PostController;
import com.team09.qanda.controllers.QuestionThreadController;
import com.team09.qanda.controllers.ThreadListController;
import com.team09.qanda.esearch.ElasticSearchHandler;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.ThreadList;
import com.team09.qanda.models.User;
import com.team09.qanda.views.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.widget.ArrayAdapter;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
	ThreadListAdapter adapter;
	MainActivity mainAct;
	ArrayAdapter<String> spinner;
	private Context context;
	ElasticSearchHandler esh;
	QuestionThread one;
	QuestionThread two;
	QuestionThread three;
	public MainActivityTest() {
		super(MainActivity.class);
		
	}
	@Override
	public void setUp() throws Exception{
		super.setUp();
		esh=new ElasticSearchHandler("http://cmput301.softwareprocess.es:8080", "cmput301f14t09","testq");
		mainAct=getActivity();
		spinner=mainAct.getSpinnerAdapter();
		createSampleList();
	}
	//Use Case 9
	public void testSortbyHasPictures() throws Throwable{
		//get the position of the  "HasPictures" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition(mainAct.getString(R.string.sort_HasPicture));
		//choose Sorting Option
		mainAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		//need time for an asynchronous thread to finish executing
		Thread.sleep(1000);
		//Did we get all the items in the list (or did my crude timer not work?)
		assertEquals(mainAct.getThreadList().getThreads().size(),3);
		QuestionThread q=mainAct.getThreadList().getThreads().get(0);
		//QuestionThread three should be at the top of the list
		//Just make sure its the same question
		assertTrue(q.getQuestion().isImageSet());
		assertTrue(q.getQuestion().getText().equals(three.getQuestion().getText()));
		assertEquals(q.answerCount(),three.answerCount());
	}
	
	//Use Case 10.1
	public void testsortByMostRecent() throws InterruptedException{
		//get the position of the  "HasPictures" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition(mainAct.getString(R.string.sort_MostRecent));
		
		//choose Sorting Option
		mainAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		//need time for an asynchronous thread to finish executing
		Thread.sleep(1000);
		
		//Did we get all the items in the list (or did my crude timer not work?)
		assertEquals(mainAct.getThreadList().getThreads().size(),3);
		
		//QuestionThread three should be at the bottom of the list
		QuestionThread q=mainAct.getThreadList().getThreads().get(2);
		
		//Just make sure its the same question
		assertTrue(q.getQuestion().getText().equals(three.getQuestion().getText()));
		assertEquals(q.answerCount(),three.answerCount());
	}
	
	//Use Case 10.2
	public void testsortByOldest() throws InterruptedException{
		//get the position of the  "Oldest" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition(mainAct.getString(R.string.sort_Oldest));
		mainAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		
		//need time for an asynchronous thread to finish executing
		Thread.sleep(1000);
		//Did we get all the items in the list (or did my crude timer not work?)
		assertEquals(mainAct.getThreadList().getThreads().size(),3);
		
		//QuestionThread three should be at the bottom of the list
		QuestionThread q=mainAct.getThreadList().getThreads().get(0);
		
		//Just make sure its the same question

		assertTrue(q.getQuestion().getText().equals(three.getQuestion().getText()));
		assertEquals(q.answerCount(),three.answerCount());

	}
	
	//Use Case 10.3
	public void testsortByMostUpVotes() throws InterruptedException{
		//get the position of the  "Most Upvoted" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition(mainAct.getString(R.string.sort_MostUpvotes));
		//choose Sorting Option
		mainAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));

		//need time for an asynchronous thread to finish executing
		Thread.sleep(1000);
		
		//Did we get all the items in the list (or did my crude timer not work?)
		assertEquals(mainAct.getThreadList().getThreads().size(),3);
		
		//QuestionThread one should be at the at the top of the list
		QuestionThread q=mainAct.getThreadList().getThreads().get(0);
		//Just make sure its the same question
	
		assertTrue(q.getQuestion().getText().equals(one.getQuestion().getText()));
		assertEquals(q.answerCount(),one.answerCount());
	}
	
	//Use Case 10.4
	public void testsortByLeastUpvotes() throws InterruptedException{
		//get the position of the  "Least Upvoted" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition(mainAct.getString(R.string.sort_LeastUpvoted));
		//choose Sorting Option
		mainAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		
		//need time for an asynchronous thread to finish executing
		Thread.sleep(1000);
		
		//Did we get all the items in the list (or did my crude timer not work?)
		assertEquals(mainAct.getThreadList().getThreads().size(),3);
				
		//QuestionThread one should be at the at the bottom of the list
		QuestionThread q=mainAct.getThreadList().getThreads().get(2);
				
		//Just make sure its the same question

		assertTrue(q.getQuestion().getText().equals(one.getQuestion().getText()));
		assertEquals(q.answerCount(),one.answerCount());
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
	private void createSampleList() throws InterruptedException {
			PostController controller;
			Post txt=new Post(new User(mainAct),"Hello?");
			Post txt2=new Post(new User(mainAct),"Do upvotes work?");
			Post txt3=new Post(new User(mainAct),"Why?");
			ArrayList<User> txtUps = txt.getUpsList();
			User user1 = new User(mainAct);
			User user2 = new User(mainAct);
			txtUps.add(user1);
			txtUps.add(user2);
			txt.setUps(txtUps);
			
		    
		    controller=new PostController(txt2);
		    controller.addUp();
		    
			txt3.setImage();
			
			one=new QuestionThread(txt);
			two=new QuestionThread(txt2);
			three=new QuestionThread(txt3);
			if(esh.getThreads().isEmpty()){
				QuestionThreadController qtc=new QuestionThreadController(one,esh);
				qtc.saveThread();
				
			    qtc=new QuestionThreadController(two,esh);
				qtc.saveThread();
				
			    qtc=new QuestionThreadController(three,esh);
				qtc.saveThread();
			}
	}
}
