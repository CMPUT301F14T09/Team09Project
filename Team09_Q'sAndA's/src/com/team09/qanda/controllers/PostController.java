package com.team09.qanda.controllers;

import java.util.ArrayList;

import android.graphics.Bitmap;

import com.team09.qanda.ApplicationState;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.Reply;
import com.team09.qanda.models.User;

/**
 * 
 * This is the PostController class.
 * This class makes changes to the Post class.
 * When a post is upvoted, this class checks for duplicates and then adds or does not add an upvote based on the result.
 * This class also adds replies to a post and attaches an image to a post.
 * 
 */

public class PostController {
	//Handles individual posts
	private Post post;
	
	public PostController(Post post) {
		this.post=post;
	}
	
	// Called to add an upvote. Checks alreadyUpvoted() to see if user has already voted.
	public void addUp() {
		ArrayList<User> ups = post.getUpsList();
		ApplicationState state = ApplicationState.getInstance();
		User user = state.getUser();
		if (alreadyUpvoted() == false) {
			ups.add(user);
			post.setUps(ups);
		}
	}
	
	// Checks to see if user has already voted
	public boolean alreadyUpvoted() {
		ArrayList<User> ups = post.getUpsList();
		ApplicationState state = ApplicationState.getInstance();
		User user = state.getUser();
		for (int i = 0; i< post.getUpsList().size(); i++) {
			if (ups.get(i).getID().equals(user.getID())) {
				return true;
			}
		} 
		return false;
	}
	
	// Adds a reply to a post
	public void addReply(Reply reply) {
		ArrayList<Reply> replies=post.getReplies();
		replies.add(reply);
		post.setReplies(replies);
	}
	
	// Sets the posts image
	public void attachImage(byte[] image) {
		post.setImage(image);
		post.setHasPicture(true);
	}
}
