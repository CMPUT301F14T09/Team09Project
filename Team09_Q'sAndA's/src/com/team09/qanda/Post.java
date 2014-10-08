package com.team09.qanda;

import java.util.ArrayList;

public class Post {
	private String text;
	private User author;
	private int ups;
	private int downs;
	private ArrayList<Reply> replies;
	
	public Post(User author, String text) {
		this.author=author;
		this.text=text;
		this.ups=0;
		this.downs=0;
		this.replies=new ArrayList<Reply>();
	}
	
	public int getUps() {
		return this.ups;
	}
	
	public void setUps(int ups) {
		this.ups=ups;
	}
	
	public int getDowns() {
		return this.downs;
	}
	
	public void setDowns(int downs) {
		this.downs=downs;
	}
	
	public User getAuthor() {
		return this.author;
	}
	
	public void setAuthor(User author) {
		this.author=author;
	}
	
	public String getText() {
		return this.text;
	}
	
	public ArrayList<Reply> getReplies() {
		return this.replies;
	}
	
	public void addReply(Reply reply) {
		this.replies.add(reply);
	}
	
	public String toString() {
		return this.text;
	}

}
