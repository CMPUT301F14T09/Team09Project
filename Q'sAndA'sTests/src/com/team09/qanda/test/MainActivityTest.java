package com.team09.qanda.test;

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
	ThreadList questions;
	ThreadListController tlc;
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
		adapter=mainAct.getAdapter();
		spinner=mainAct.getSpinnerAdapter();
		questions=new ThreadList();
		tlc = new ThreadListController(questions,esh);
		createSampleList();
	}
	//Use Case 9
	public void testSortbyHasPictures(){
		//get the position of the  "HasPictures" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Has Pictures");
		//choose Sorting Option
		mainAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(adapter.getPosition(three),0);
	}
	
	//Use Case 10.1
	public void testsortByMostRecent(){
		//get the position of the  "HasPictures" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Most Recent");
		//choose Sorting Option
		mainAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(adapter.getPosition(three),2);
	}
	
	//Use Case 10.2
	public void testsortByOldest(){
		//get the position of the  "Oldest" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Oldest");
		QuestionThread first=new QuestionThread(new Post(new User(context),"am I first?"));
		QuestionThread second=new QuestionThread(new Post(new User(context),"am I second?"));
		QuestionThread third=new QuestionThread(new Post(new User(context),"am I third?"));
		// Make ThreadListController
		tlc.addThread(first);
		tlc.addThread(second);
		tlc.addThread(third);

		//choose Sorting Option
		mainAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(adapter.getPosition(three),0);

	}
	
	//Use Case 10.3
	public void testsortByMostUpVotes(){
		PostController controller;
		//get the position of the  "Most Upvoted" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Most Upvoted");
		//choose Sorting Option
		mainAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(adapter.getPosition(one),0);
	}
	
	//Use Case 10.4
	public void testsortByLeastUpvotes(){
		//get the position of the  "Least Upvoted" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Least Upvoted");
		//choose Sorting Option
		mainAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(adapter.getPosition(one),2);
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
	private void createSampleList() {
		if(esh.getThreads().isEmpty()){
			PostController controller;
			Post txt=new Post(new User(mainAct),"Hello?");
			
			controller=new PostController(txt);
		    controller.addUp();
		    controller.addUp();
			
		    Post txt2=new Post(new User(mainAct),"Do upvotes work?");
		    controller=new PostController(txt2);
		    controller.addUp();
		    
			Post txt3=new Post(new User(mainAct),"Why?");
			txt.setHasPicture(true);
			
			one=new QuestionThread(txt);
			two=new QuestionThread(txt2);
			three=new QuestionThread(txt3);
			
			for(QuestionThread question:new QuestionThread[]{one,two,three}){
				QuestionThreadController qtc=new QuestionThreadController(question,esh);
				qtc.saveThread();
				tlc.addThread(question);
			}
		}
		
	}
}
