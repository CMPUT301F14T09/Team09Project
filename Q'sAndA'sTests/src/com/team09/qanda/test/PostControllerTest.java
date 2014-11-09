package com.team09.qanda.test;

import junit.framework.TestCase;

import android.content.Context;

import com.team09.qanda.controllers.PostController;
import com.team09.qanda.controllers.QuestionThreadController;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.Reply;
import com.team09.qanda.models.User;

public class PostControllerTest extends TestCase {

	private Context context;

	// Use Case #6 : As an author, I want to reply to questions and answers to clarify things.
	public void testAddReplytoAnswer() {
		String message = "What is this?";
		String answer = "This is Sparta.";
		String reply2 = "Your face is cool.";
		String name = "John";
		String auth = "Pete";
		String replyName = "Luna";
		User a = new User(context,name);
		User a2 = new User(context,auth);
		User a3 = new User(context,replyName);
		Post q = new Post(a, message);
		Post ans = new Post(a2, answer);
		// Make a question thread
		QuestionThread qThread = new QuestionThread(q);
		// Create a question thread controller
		QuestionThreadController qctl = new QuestionThreadController(qThread);
		// Create a post controller
		qctl.addAnswer(ans);
		assertTrue("This is not answered", !qThread.getAnswers().isEmpty());
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
		User a = new User(context,name);
		User a2 = new User(context,auth);
		User a3 = new User(context,replyName);
		
		Post q = new Post(a, message);
		Post ans = new Post(a2, answer);
		// Make a question thread
		QuestionThread qThread = new QuestionThread(q);
		// Create a question thread controller
		QuestionThreadController qctl = new QuestionThreadController(qThread);
		qctl.addAnswer(ans);
		assertTrue("This is not answered", !qThread.getAnswers().isEmpty());
		Reply r = new Reply(a3,reply);
		PostController p=new PostController(q);
		p.addReply(r);
		assertTrue("There is 1 reply", !q.getReplies().isEmpty());
	}

	// Use Case #7 : As an author, I want to attach a picture to my questions or my answers.
	public void testInsertImageInAnswer() {
		String message = "What is this?";
		String answer = "This is Sparta.";
		String name = "John";
		String auth = "Pete";
		User a = new User(context,name);
		User a2 = new User(context,auth);
		Post q = new Post(a, message);
		Post ans = new Post(a2, answer);
		// Make a question thread
		QuestionThread qThread = new QuestionThread(q);
		// Create a question thread controller
		QuestionThreadController qctl = new QuestionThreadController(qThread);
		qctl.addAnswer(ans);
		assertTrue("This is not answered", !qThread.getAnswers().isEmpty());
		// Create a post (answer) controller
		PostController pctl = new PostController(ans);
		ans.setHasPicture(true);
		assertTrue("Image is not set!", ans.isImageSet());
	}
	
	// Use Case #8 : As a sysadmin, I do not want the pictures to be large (> 64kb).
	public void testSysadminCheckImageOnAnswer() {
		String message = "What is this?";
		String answer = "This is Sparta.";
		String name = "John";
		String auth = "Pete";
		User a = new User(context,name);
		User a2 = new User(context,auth);
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
			ans.setHasPicture(true);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue("The image is too big!", thrown.equals(false));
		assertTrue("There is no image!", ans.isImageSet().equals(true));
	}
	
	//Use Case 11, 12: Upvote a Question/Answer
	public void testUpVotes(){
		User user1 = new User(context);
		Post txt=new Post(user1,"Do upvotes work?");
		PostController cn1=new PostController(txt);
		cn1.addUp();
		assertTrue(txt.getUps()==1);
	}
}
