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
		UserState state = new UserState();
		User user = state.getUser();
		for (int i = 1; i<= post.getUps(); i++) {
			if (ups.get(i).getName() == user.getName() && ups.get(i).getID() == user.getID()) {
				ups.add(user);
				post.setUps(ups);
				break;
			}
		}
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
