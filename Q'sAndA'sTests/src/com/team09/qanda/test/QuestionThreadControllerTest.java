package com.team09.qanda.test;

import java.util.ArrayList;
import java.util.Date;

import junit.framework.TestCase;

import com.team09.qanda.Post;
import com.team09.qanda.PostController;
import com.team09.qanda.QuestionThread;
import com.team09.qanda.QuestionThreadController;
import com.team09.qanda.Reply;
import com.team09.qanda.User;

public class QuestionThreadControllerTest extends TestCase {
	
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
		qctl.attachImage();
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
			qctl.attachImage();
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue("The image is too big!", thrown.equals(false));
		assertTrue("There is no image!", thrown.equals(false));
	}

	// Use Case #13.2: Most upvoted answers
	public void testsortAnswersByMostUpVotes(){
		Post questionText=new Post(new User(),"This is a question.");
		QuestionThread qThread=new QuestionThread(questionText);
		QuestionThreadController qctl = new QuestionThreadController(qThread);
		Post answer1=new Post(new User(),"Second Best Answer");
		answer1.setUps(1);
		Post answer2=new Post(new User(),"Best Answer");
		answer2.setUps(2);
		Post answer3=new Post(new User(),"Worst Answer");
		qctl.addAnswer(answer1);
		qctl.addAnswer(answer2);
		qctl.addAnswer(answer3);
		qctl.sort();
		assertEquals(qctl.getAnswers().get(0),answer2);
	}

	// Use Case #14: Most upvoted answers
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
		assertEquals(qctl.answerCount(),3);
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
	//Use Case 11: Upvote a Question
	public void testUpVotes(){
		Post txt=new Post(new User(),"Do upvotes work?");
		QuestionThreadController cn1=new QuestionThreadController(new QuestionThread(txt));
		cn1.addUp();
		assertTrue(txt.getUps()==1);
		cn1.addUp();
		assertTrue(txt.getUps()==2);
	}
}
