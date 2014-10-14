package com.team09.qanda.test;

import junit.framework.TestCase;

import com.team09.qanda.Post;
import com.team09.qanda.QuestionThread;
import com.team09.qanda.QuestionThreadController;
import com.team09.qanda.User;

public class QuestionThreadControllerTest extends TestCase {

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
	// Use Case #8 : As a sysadmin, I do not want the pictures to be large (> 64kb).
	public void testInsertImageInAnswer() {
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
	
	// Use Case #2 : View a question and its answers
	public void testViewThread() {
		
	}
	
	// Use Case #3 : View replies to a question/answer
	public void testViewReplies() {
		
	}

}
