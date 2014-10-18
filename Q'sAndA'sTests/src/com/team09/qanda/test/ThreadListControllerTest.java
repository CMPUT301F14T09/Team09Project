package com.team09.qanda.test;

import com.team09.qanda.LocalStorageHandler;
import com.team09.qanda.MainActivity;
import com.team09.qanda.Post;
import com.team09.qanda.PostController;
import com.team09.qanda.QuestionThread;
import com.team09.qanda.QuestionThreadController;
import com.team09.qanda.ThreadList;
import com.team09.qanda.ThreadListController;
import com.team09.qanda.User;

import android.test.ActivityInstrumentationTestCase2;

public class ThreadListControllerTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	public ThreadListControllerTest() {
		super(MainActivity.class);
	}
	
	// Use Case #1 : Browse questions
	public void testBrowseQuestions() {
		Post qpost1 = new Post(new User("John"), "Question 1?");
		PostController pc1 = new PostController(qpost1);
		
		QuestionThread q1 = new QuestionThread(qpost1);
		QuestionThreadController qtc1 = new QuestionThreadController(q1);
		
		pc1.addUp();
		pc1.addUp();
		qtc1.addAnswer(new Post(new User(), "Answer 1."));
		
		Post qpost2 = new Post(new User(), "Question 2?");
		PostController pc2 = new PostController(qpost2);
		
		QuestionThread q2 = new QuestionThread(qpost2);
		QuestionThreadController qtc2 = new QuestionThreadController(q2);
		
		pc2.addUp();
		pc2.addUp();
		qtc2.addAnswer(new Post(new User(), "Answer 2."));
		qtc2.addAnswer(new Post(new User(), "Answer 3."));
		
		ThreadList questions = new ThreadList();
		ThreadListController cn1=new ThreadListController(questions);
		cn1.addThread(q1);
		cn1.addThread(q2);
		
		assertEquals("Question 1?", questions.get(0).getQuestion().getText());
		assertEquals("John", questions.get(0).getQuestion().getAuthor());
		assertEquals(2, questions.get(1).answerCount());
		assertEquals(2, questions.get(1).getQuestion().getUps());		
	}
	
	// Use Case #4 : Add a question
	public void testAddQuestion() {
		Post qpost1 = new Post(new User("John"), "Question 1?");
		PostController pc1 = new PostController(qpost1);
		
		QuestionThread q1 = new QuestionThread(qpost1);
		QuestionThreadController qtc1 = new QuestionThreadController(q1);
		ThreadList questions = new ThreadList();
		ThreadListController cn1=new ThreadListController(questions);
		cn1.addThread(q1);
		
		assertTrue("Question not added", !questions.getQuestions().isEmpty());
	}
	
	// Use Case #16: Remember which questions I asked
	public void testStoreMyQuestionsLocally() {
		Post qpost1 = new Post(new User("John"), "Question 1?");
		PostController pc1 = new PostController(qpost1);
		
		QuestionThread q1 = new QuestionThread(qpost1);
		QuestionThreadController qtc1 = new QuestionThreadController(q1);			
		
		ThreadList myQuestions = new ThreadList();
		ThreadListController cn1=new ThreadListController(myQuestions);
		cn1.addThread(q1);
		
		//LocalStorageHandler lsh = new LocalStorageHandler(myQuestions);
		LocalStorageHandler lsh = new LocalStorageHandler();
	
		assertSame(lsh.getThreadList("MyQuestions.txt").get(0), q1);
	}
}
