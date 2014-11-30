package com.team09.qanda.test;

import java.util.ArrayList;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.team09.qanda.controllers.PostController;
import com.team09.qanda.controllers.QuestionThreadController;
import com.team09.qanda.controllers.ThreadListController;
import com.team09.qanda.esearch.ElasticSearchHandler;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.ThreadList;
import com.team09.qanda.models.User;
import com.team09.qanda.views.MainActivity;

import junit.framework.TestCase;


public class ESHTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public ESHTest() {
		super(MainActivity.class);
		// TODO Auto-generated constructor stub
	}

	private Context context;

	@Override
	protected void setUp() throws Exception {
		context = getActivity().getApplicationContext();
		super.setUp();
	}
	
	//Use case #15: Search for questions and answers
	public void testNumberOfAnswers(){
		ElasticSearchHandler handler=new ElasticSearchHandler("http://cmput301.softwareprocess.es:8080", "cmput301f14t09","searchTests");
		ThreadListController  threadController = new ThreadListController(new ThreadList(),handler);
		
		Post questionText=new Post(new User(context),"This is a question.");
		QuestionThread qThread=new QuestionThread(questionText);
		QuestionThreadController qctl = new QuestionThreadController(qThread,handler);
		Post answer1=new Post(new User(context),"Do upvotes work?");
		qctl.addAnswer(answer1);
		qctl.saveThread();
		
		Post questionText2=new Post(new User(context),"Does this work?");
		QuestionThread qThread2=new QuestionThread(questionText2);
		QuestionThreadController qct2 = new QuestionThreadController(qThread2,handler);
		qct2.saveThread();
		
		QuestionThreadController qct3 = new QuestionThreadController(new QuestionThread(new Post(new User(context),"This should not match?")),handler);
		qct3.saveThread();
		
		ArrayList<QuestionThread> searchResults = handler.search("do");
		assertEquals(searchResults.size(),2);
	}
	
	//Use case 21: As an author, I want to push my replies, questions and answers online once I get connectivity.
	//test save works by removing(if exists) and re-adding a test post
	public void testSavetoServer(){
		Post testp = new Post(new User(context,"test"), "testq");
		ThreadList thread = new ThreadList();
		ThreadListController  threadController = new ThreadListController(thread);
		
		//connecting to local test elastic search environment
		ElasticSearchHandler esh = new ElasticSearchHandler("http://localhost:9200/test","cmput301f14t09", "qthread");

		QuestionThread q1 = new QuestionThread(testp);
		QuestionThreadController qtc1 = new QuestionThreadController(q1);
		
		qtc1.addAnswer(new Post(new User(context,"test2"), "testa"));
		threadController.addThread(q1);
		
		try {
			esh.saveThreads(thread);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		
		//confirm questionthread object was added
		assertNotNull(esh.getThreads().indexOf(q1));
	}
	
	//Use case 21(kinda?):  As an author, I want to push my replies, questions and answers online once I get connectivity.
	//testing load thread from server
	public void testLoadfromServer(){
		ElasticSearchHandler esh = new ElasticSearchHandler();
		assertTrue(esh.getThreads()!=null);
	}
}

