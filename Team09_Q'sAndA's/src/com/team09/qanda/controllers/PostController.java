package com.team09.qanda.controllers;

import java.util.ArrayList;

import com.team09.qanda.ApplicationState;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.Reply;
import com.team09.qanda.models.User;

/**
 * This class is used to make updates to an instance of a Post.
 * It is initialized with a Post object, and then it can operate on that Post
 * The methods use the getters and setters in Post to perform more meaningful actions
 * 
 */

public class PostController {
	
	private Post post;
	
	/**
	 * The constructor takes in a Post object, which it will act upon
	 * @param post The Post that will be updated
	 */
	public PostController(Post post) {
		this.post=post;
	}
	
	/**
	 * Adds an upvote to the post. Performs a check to see if the user
	 * has already upvoted the post, so each user can only upvote an
	 * individual post once.
	 */
	public void addUp() {
		ArrayList<User> ups = post.getUpsList();
		ApplicationState state = ApplicationState.getInstance();
		User user = state.getUser();
		if (alreadyUpvoted() == false) {
			ups.add(user);
			post.setUps(ups);
		}
	}
	
	/**
	 * Performs a check to see if the user attempting to upvote
	 * a post has previously upvoted it.
	 * @return True if they have, false otherwise
	 */
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
	
	/**
	 * Adds a reply to the current list of replies for a single post
	 * @param reply The reply to be added
	 */
	public void addReply(Reply reply) {
		ArrayList<Reply> replies=post.getReplies();
		replies.add(reply);
		post.setReplies(replies);
	}
	
	// Sets the posts image
	public void attachImage(String image) {
		post.setImage(image);
		post.setHasPicture(true);
	}
	
	public void addCity(String city) {
		post.setCity(city);
	}
}
