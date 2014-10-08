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
	
	public void addDown() {
		post.setDowns(post.getDowns()+1);
	}
	
	public int getPostUps() {
		return post.getUps();
	}
	
	public int getPostDowns() {
		return post.getDowns();
	}
}
