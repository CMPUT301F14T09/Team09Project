package com.team09.qanda.test;

import com.team09.qanda.LocalStorageHandler;
import com.team09.qanda.controllers.PostController;
import com.team09.qanda.controllers.QuestionThreadController;
import com.team09.qanda.controllers.ThreadListController;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.ThreadList;
import com.team09.qanda.models.User;
import com.team09.qanda.views.MainActivity;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

public class ThreadListControllerTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	
	private Context context = getActivity().getApplicationContext();

	public ThreadListControllerTest() {
		super(MainActivity.class);
	}



	// Use Case #1 : Browse questions
	public void testBrowseQuestions() {
		Post qpost1 = new Post(new User(context,"John"), "Question 1?");
		PostController pc1 = new PostController(qpost1);
		
		QuestionThread q1 = new QuestionThread(qpost1);
		QuestionThreadController qtc1 = new QuestionThreadController(q1);
		
		pc1.addUp();
		pc1.addUp();
		qtc1.addAnswer(new Post(new User(context), "Answer 1."));
		
		Post qpost2 = new Post(new User(context), "Question 2?");
		PostController pc2 = new PostController(qpost2);
		
		QuestionThread q2 = new QuestionThread(qpost2);
		QuestionThreadController qtc2 = new QuestionThreadController(q2);
		
		pc2.addUp();
		pc2.addUp();
		qtc2.addAnswer(new Post(new User(context), "Answer 2."));
		qtc2.addAnswer(new Post(new User(context), "Answer 3."));
		
		ThreadList questions = new ThreadList();
		ThreadListController cn1=new ThreadListController(questions);
		cn1.addThread(q1);
		cn1.addThread(q2);
		
		// Testing functionality to be used in implementing getView() method
		// of our custom adapter for displaying the main questions' list
		assertEquals("Question 1?", questions.get(0).getQuestion().getText());
		assertEquals("John", questions.get(0).getQuestion().getAuthor().getName());
		assertEquals(2, questions.get(1).answerCount());
		assertEquals(2, questions.get(1).getQuestion().getUps());		
	}
	
	// Use Case #4 : Add a question
	public void testAddQuestion() {
		Post qpost1 = new Post(new User(context, "John"), "Question 1?");
		PostController pc1 = new PostController(qpost1);
		
		QuestionThread q1 = new QuestionThread(qpost1);
		QuestionThreadController qtc1 = new QuestionThreadController(q1);
		ThreadList questions = new ThreadList();
		ThreadListController cn1=new ThreadListController(questions);
		cn1.addThread(q1);
		
		assertTrue("Question not added", !questions.getQuestions().isEmpty());
	}
	
	// Use Case #13.1: Most upvoted questions
	public void testsortQuestionsByMostUpVotes(){
		Post qpost1 = new Post(new User(context, "John"), "Question 1?");
		PostController pc1 = new PostController(qpost1);
		Post qpost2 = new Post(new User(context, "John"), "Question 2?");
		PostController pc2 = new PostController(qpost2);
		pc2.addUp();
		pc2.addUp();
		Post qpost3 = new Post(new User(context, "Bob"), "Question 3?");
		PostController pc3 = new PostController(qpost3);
		pc3.addUp();

		QuestionThread q1 = new QuestionThread(qpost1);
		QuestionThread q2 = new QuestionThread(qpost2);
		QuestionThread q3 = new QuestionThread(qpost3);
		
		ThreadList myQuestions = new ThreadList();
		ThreadListController cn1=new ThreadListController(myQuestions);
		cn1.addThread(q1);
		cn1.addThread(q2);
		cn1.addThread(q3);
	
		assertSame(myQuestions.getThreads().get(0), q2);
		assertSame(myQuestions.getThreads().get(1), q3);
		assertSame(myQuestions.getThreads().get(2), q1);
	}
	
	// Use Case #16: Remember which questions I asked
	public void testStoreMyQuestionsLocally() {
		Post qpost1 = new Post(new User(context, "John"), "Question 1?");
		
		QuestionThread q1 = new QuestionThread(qpost1);
		ThreadList tl = new ThreadList();
		ThreadListController tlc = new ThreadListController(tl);
		tlc.addThread(q1);
		
		LocalStorageHandler lsh = new LocalStorageHandler();
		lsh.saveQuestionThread(context, q1, "MyQuestions.txt");
	
		assertTrue(lsh.getThreadList(context, "MyQuestions.txt").equals(tl)); 
	}
}
