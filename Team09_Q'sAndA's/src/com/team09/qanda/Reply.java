package com.team09.qanda;

public class Reply {
	private String text;
	private User author;
	
	public Reply(User author, String text) {
		this.author=author;
		this.text=text;
	}
	
	public String getText() {
		return this.text;
	}
	
	public User getAuthor() {
		return this.author;
	}

}
