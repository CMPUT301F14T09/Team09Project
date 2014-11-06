package com.team09.qanda;

import java.util.ArrayList;


public class PostController {
	//Handles individual posts
	private Post post;
	
	public PostController(Post post) {
		this.post=post;
	}
	
	public void addUp() {
		ArrayList<User> ups = post.getUpsList();
		ApplicationState state = new ApplicationState();
		User user = state.getUser();
		if (alreadyUpvoted() == false) {
			ups.add(user);
			post.setUps(ups);
		}
	}
	
	public boolean alreadyUpvoted() {
		ArrayList<User> ups = post.getUpsList();
		ApplicationState state = new ApplicationState();
		User user = state.getUser();
		for (int i = 0; i< post.getUps(); i++) {
			if (ups.get(i).getID() == user.getID()) {
				return true;
			}
		}
		return false;
	}
	
	public void addReply(Reply reply) {
		ArrayList<Reply> replies=post.getReplies();
		replies.add(reply);
		post.setReplies(replies);
	}
	
	public void attachImage() {
		post.setImage();
	}
}
