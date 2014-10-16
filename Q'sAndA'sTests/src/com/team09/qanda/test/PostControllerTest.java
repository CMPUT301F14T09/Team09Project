package com.team09.qanda.test;

import junit.framework.TestCase;

import com.team09.qanda.Post;
import com.team09.qanda.PostController;
import com.team09.qanda.QuestionThread;
import com.team09.qanda.QuestionThreadController;
import com.team09.qanda.Reply;
import com.team09.qanda.User;

public class PostControllerTest extends TestCase {

	// Use Case #6 : As an author, I want to reply to questions and answers to clarify things.
	public void testAddReplytoAnswer() {
		String message = "What is this?";
		String answer = "This is Sparta.";
		String reply = "This is cool!";
		String reply2 = "Your face is cool.";
		String name = "John";
		String auth = "Pete";
		String replyName = "Luna";
		User a = new User(name);
		User a2 = new User(auth);
		User a3 = new User(replyName);
		Post q = new Post(a, message);
		Post ans = new Post(a2, answer);
		// Make a question thread
		QuestionThread qThread = new QuestionThread(q);
		// Create a question thread controller
		QuestionThreadController qctl = new QuestionThreadController(qThread);
		// Create a post controller
		PostController pctlq = new PostController(q);
		qctl.addAnswer(ans);
		assertTrue("This is not answered", !qThread.getAnswers().isEmpty());
		Reply r = new Reply(a3,reply);
		pctlq.addReply(r);
		assertTrue("There is 1 reply in the question.", !q.getReplies().isEmpty());
		Reply r2 = new Reply(a3, reply2);
		// Make a Post Controller for answer
		PostController pctla = new PostController(ans);
		pctla.addReply(r2);
		assertTrue("THere is 1 reply in the answer.", !ans.getReplies().isEmpty());
	}
	
	// Use Case #6 : As an author, I want to reply to questions and answers to clarify things.
	public void testAddReplytoQuestion() {
		String message = "What is this?";
		String answer = "This is Sparta.";
		String reply = "This is cool!";
		String name = "John";
		String auth = "Pete";
		String replyName = "Luna";
		User a = new User(name);
		User a2 = new User(auth);
		User a3 = new User(replyName);
		Post q = new Post(a, message);
		Post ans = new Post(a2, answer);
		// Make a question thread
		QuestionThread qThread = new QuestionThread(q);
		// Create a question thread controller
		QuestionThreadController qctl = new QuestionThreadController(qThread);
		// Create a post controller
		PostController pctl = new PostController(q);
		qctl.addAnswer(ans);
		assertTrue("This is not answered", !qThread.getAnswers().isEmpty());
		Reply r = new Reply(a3,reply);
		pctl.addReply(r);
		assertTrue("There is 1 reply", !q.getReplies().isEmpty());
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
		// Create a post (answer) controller
		PostController pctl = new PostController(ans);
		try {
			pctl.attachImage();
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue("The image is too big!", thrown.equals(false));
		assertTrue("There is no image!", thrown.equals(false));
	}


}