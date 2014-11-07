package com.team09.qanda;

import java.util.ArrayList;

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
		ApplicationState state = new ApplicationState();
		User user = state.getUser();
		if (alreadyUpvoted() == false) {
			ups.add(user);
			post.setUps(ups);
		}
	}
	
	// Checks to see if user has already voted
	public boolean alreadyUpvoted() {
		ArrayList<User> ups = post.getUpsList();
		ApplicationState state = new ApplicationState();
		User user = state.getUser();
		for (int i = 0; i< post.getUps(); i++) {
			if (ups.get(i).getName() == user.getName()) {
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
	public void attachImage() {
		post.setImage();
	}
}
