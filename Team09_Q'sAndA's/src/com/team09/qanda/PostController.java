package com.team09.qanda;


public class PostController {
	//Handles individual posts
	private Post post;
	
	public PostController(Post post) {
		this.post=post;
	}
	
	public void addUp() {
		post.setUps(post.getUps()+1);
	}
	
	public void addReply(Reply reply) {
		post.setReply(reply);
	}
	
	public void attachImage() {
		post.setImage();
	}
}
