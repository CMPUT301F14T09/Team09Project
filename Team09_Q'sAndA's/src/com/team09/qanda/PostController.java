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
		User user = new User();
		ups.add(user);
		post.setUps(ups);
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
