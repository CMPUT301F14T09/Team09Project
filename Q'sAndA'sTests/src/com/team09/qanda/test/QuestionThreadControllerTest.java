package com.team09.qanda.test;

import java.util.ArrayList;
import java.util.Date;

import junit.framework.TestCase;

import android.content.Context;

import com.team09.qanda.LocalStorageHandler;
import com.team09.qanda.Post;
import com.team09.qanda.PostController;
import com.team09.qanda.QuestionThread;
import com.team09.qanda.QuestionThreadController;
import com.team09.qanda.Reply;
import com.team09.qanda.ThreadList;
import com.team09.qanda.User;

public class QuestionThreadControllerTest extends TestCase {
	private Context context;
	
	// Use Case #2 : View a question and its answers
	public void testViewThread() {
		Post qpost1 = new Post(new User("John"), "Question 1?");
		PostController pc1 = new PostController(qpost1);
		
		QuestionThread q1 = new QuestionThread(qpost1);
		QuestionThreadController qtc1 = new QuestionThreadController(q1);
		
		pc1.addUp();
		pc1.addUp();
		Post apost1 = new Post(new User("Lia"), "Answer 1.");
		qtc1.addAnswer(apost1);
		Post apost2 = new Post(new User("Liam"), "Answer 2.");
		qtc1.addAnswer(apost2);
		
		// Testing functionality to be used to display all the components
		// of a question thread
		assertEquals("Question 1?", q1.getQuestion().getText());
		assertEquals("John", q1.getQuestion().getAuthor());
		assertEquals(2,q1.getAnswers().size());
		assertEquals(0,q1.getAnswers().get(0).getUps());
	}
	
	// Use Case #3 : View replies to a question/answer
	public void testViewReplies() {
		Post qpost1 = new Post(new User("John"), "Question 1?");
		PostController pc1 = new PostController(qpost1);
		
		QuestionThread q1 = new QuestionThread(qpost1);
		QuestionThreadController qtc1 = new QuestionThreadController(q1);
		
		pc1.addReply(new Reply(new User("Brent"),"Reply 1"));
		
		assertEquals(1,qpost1.getReplies().size());
		assertEquals("Reply 1",qpost1.getReplies().get(0).getText());
		assertEquals("Brent",qpost1.getReplies().get(0).getAuthor());
		
	}
	
	// Use Case #5 : As an author, I want to answer questions by making an answer.
	public void testAddAnswer() {
		String message = "What is this?";
		String answer = "This is Sparta.";
		String name = "John";
		String auth = "Pete";
		User a = new User(name);
		User a2 = new User(auth);
		Post q = new Post(a, message);
		Post ans = new Post(a2, answer);
		// Make a question thread
		QuestionThread qThread = new QuestionThread(q);
		// Create a question thread controller
		QuestionThreadController qctl = new QuestionThreadController(qThread);
		qctl.addAnswer(ans);
		assertTrue("This is not answered", !qThread.getAnswers().isEmpty());
	}

	// Use Case #7 : As an author, I want to attach a picture to my questions or my answers.
	public void testInsertImageInQuestion() {
		String message = "What is this?";
		String answer = "This is Sparta.";
		String name = "John";
		String auth = "Pete";
		User a = new User(name);
		User a2 = new User(auth);
		Post q = new Post(a, message);
		Post ans = new Post(a2, answer);
		// Make a question thread
		QuestionThread qThread = new QuestionThread(q);
		// Create a question thread controller
		QuestionThreadController qctl = new QuestionThreadController(qThread);
		qctl.addAnswer(ans);
		// Attach image in question using question thread controller
		q.setImage();
		assertTrue("There is no image!", q.isImageSet());
	}
	
	// Use Case #8 : As a sysadmin, I do not want the pictures to be large (> 64kb).
	public void testSysadminCheckImageOnQuestionThread() {
		String message = "What is this?";
		String answer = "This is Sparta.";
		String name = "John";
		String auth = "Pete";
		User a = new User(name);
		User a2 = new User(auth);
		Post q = new Post(a, message);
		Post ans = new Post(a2, answer);
		// Make a question thread
		QuestionThread qThread = new QuestionThread(q);
		// Create a question thread controller
		QuestionThreadController qctl = new QuestionThreadController(qThread);
		qctl.addAnswer(ans);
		assertTrue("This is not answered", !qThread.getAnswers().isEmpty());
		Boolean thrown = false;
		try {
			q.setImage();
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue("The image is too big!", thrown.equals(false));
		assertTrue("There is no image!", q.isImageSet().equals(true));
	}
	
	// Use Case #13.2: Most upvoted answers
	public void testsortAnswersByMostUpVotes(){
		Post questionText=new Post(new User(),"This is a question.");
		QuestionThread qThread=new QuestionThread(questionText);
		QuestionThreadController qctl = new QuestionThreadController(qThread);
		Post answer1=new Post(new User(),"Second Best Answer");
		ArrayList<User> answer1Ups = answer1.getUpsList();
		User user1 = new User();
		User user2 = new User();
		answer1Ups.add(user1);
		answer1.setUps(answer1Ups);
		Post answer2=new Post(new User(),"Best Answer");
		ArrayList<User> answer2Ups = answer2.getUpsList();
		answer2Ups.add(user1);
		answer2Ups.add(user2);
		answer2.setUps(answer2Ups);
		Post answer3=new Post(new User(),"Worst Answer");
		qctl.addAnswer(answer1);
		qctl.addAnswer(answer2);
		qctl.addAnswer(answer3);
		assertEquals(qThread.getAnswers().get(0),answer2);
	}
	
	// Test for someone upvoting same question twice
	public void testUpvoteTwice(){
		Post questionText=new Post(new User(),"This is a question.");
		QuestionThread qThread=new QuestionThread(questionText);
		QuestionThreadController qctl = new QuestionThreadController(qThread);
		Post answer1=new Post(new User(),"Second Best Answer");
		ArrayList<User> answer1Ups = answer1.getUpsList();
		User user1 = new User();
		answer1Ups.add(user1);
		answer1Ups.add(user1);
		answer1.setUps(answer1Ups);
		qctl.addAnswer(answer1);
		assertEquals(qThread.getAnswers().get(0).getUps(),1);
	}

	// Use Case #14: Number of answers
	public void testNumberOfAnswers(){
		Post questionText=new Post(new User(),"This is a question.");
		QuestionThread qThread=new QuestionThread(questionText);
		QuestionThreadController qctl = new QuestionThreadController(qThread);
		Post answer1=new Post(new User(),"Here is an answer.");
		Post answer2=new Post(new User(),"Another answer.");
		Post answer3=new Post(new User(),"A third answer.");
		qctl.addAnswer(answer1);
		qctl.addAnswer(answer2);
		qctl.addAnswer(answer3);
		assertEquals(qThread.answerCount(),3);
	}
	
	//Use case 17: Read later
	public void testReadLater() {
		LocalStorageHandler handler=new LocalStorageHandler();
		Post question=new Post(new User(),"Can I read these things later?");
		QuestionThread qt=new QuestionThread(question);
		handler.saveQuestionThread(context, qt, "Later.txt");
		ThreadList laters=handler.getThreadList(context, "Later.txt");
		ArrayList<String> qs=new ArrayList<String>();
		for (Post q:laters.getQuestions()) {
			qs.add(q.getText());
		}
		assertTrue(qs.contains("Can I read these things later?"));
	}
		
	//Use case 18: Favourite questions
	public void testFavourite() {
		LocalStorageHandler handler=new LocalStorageHandler();
		Post question=new Post(new User(),"This is my favourite");
		QuestionThread qt=new QuestionThread(question);
		handler.saveQuestionThread(context, qt, "Favourite.txt");
		ThreadList favs=handler.getThreadList(context, "Favourite.txt");
		ArrayList<String> qs=new ArrayList<String>();
		for (Post q:favs.getQuestions()) {
			qs.add(q.getText());
		}
		assertTrue(qs.contains("This is my favourite"));
	}
	
	// Use Case #22: As a user, by default, I should see the most fresh comments
	public void testDefaultCommentOrder(){
		Post p1 = new Post(new User("test"), "q1");
		PostController pc1 = new PostController(p1);
		
		Reply reply = new Reply(new User("test1"),"r2");
		pc1.addReply(reply);
		pc1.addReply(new Reply(new User("test1"),"r1", new Date(reply.getTimestamp().getTime()-1000)));
		pc1.addReply(new Reply(new User("test1"),"r3", new Date(reply.getTimestamp().getTime()+1000)));
			
	    ArrayList<Reply> comm = p1.getReplies();
	    for(int i = 1; i < comm.size(); i++){
	    	if(comm.get(i-1).getTimestamp().compareTo(comm.get(i).getTimestamp()) > 1) {
	    		fail();
		    }
	    }
	}

}
