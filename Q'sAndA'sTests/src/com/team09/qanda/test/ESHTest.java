package com.team09.qanda.test;

import java.util.ArrayList;

import com.team09.qanda.ElasticSearchHandler;
import com.team09.qanda.Post;
import com.team09.qanda.PostController;
import com.team09.qanda.QuestionThread;
import com.team09.qanda.QuestionThreadController;
import com.team09.qanda.ThreadList;
import com.team09.qanda.ThreadListController;
import com.team09.qanda.User;

import junit.framework.TestCase;


public class ESHTest extends TestCase
{
	//Use case #15: Search for questions and answers
	public void testNumberOfAnswers(){
		ThreadListController  threadController = new ThreadListController(new ThreadList());
		Post questionText=new Post(new User(),"This is a question.");
		QuestionThread qThread=new QuestionThread(questionText);
		QuestionThreadController qctl = new QuestionThreadController(qThread);
		Post answer1=new Post(new User(),"Do upvotes work?");
		qctl.addAnswer(answer1);
		threadController.addThread(qThread);
		threadController.addThread(new QuestionThread(new Post(new User(),"Does this work?")));
		threadController.addThread(new QuestionThread(new Post(new User(),"This will not be returned.")));
		ElasticSearchHandler esh = new ElasticSearchHandler();
		ThreadList searchResults = esh.search("do");
		assertEquals(searchResults.getThreads().size(),2);
	}
	
	//Use case 21: As an author, I want to push my replies, questions and answers online once I get connectivity.
	//test save works by removing(if exists) and re-adding a test post
	public void testSavetoServer(){
		Post testp = new Post(new User("test"), "testq");
		ThreadList thread = new ThreadList();
		ThreadListController  threadController = new ThreadListController(thread);
		ArrayList<QuestionThread> old = (ArrayList<QuestionThread>)thread.getThreads().clone();
		ElasticSearchHandler esh = new ElasticSearchHandler();
		
//		if (thread.getQuestions().contains(testp)) {
//			esh.delete(testp);
//			thread.refresh(0, 10);
//		}
//		
		QuestionThread q1 = new QuestionThread(testp);
		QuestionThreadController qtc1 = new QuestionThreadController(q1);
		
		qtc1.addAnswer(new Post(new User("test2"), "testa"));
		threadController.addThread(q1);
		
		try {
			esh.saveThreads(thread);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//confirm questionthread object was added
		assertTrue(esh.getThreads().contains(old));
	}
	
	//Use case 21(kinda?):  As an author, I want to push my replies, questions and answers online once I get connectivity.
	//testing load thread from server
	public void testLoadfromServer(){
		ElasticSearchHandler esh = new ElasticSearchHandler();
		assertTrue(esh.getThreads()!=null);
	}
}

