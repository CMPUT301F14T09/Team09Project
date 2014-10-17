package com.team09.qanda;

import java.util.Date;

public class Reply extends QModel<QView> {
	private String text;
	private User author;
	private Date timestamp;
	
	public Reply(User author, String text) {
		this.author=author;
		this.text=text;
		this.timestamp=new Date();
	}
	
	//overloaded constructor to test default order
	public Reply(User author, String text, Date date) {
		this.author=author;
		this.text=text;
		this.timestamp= date;
	}
	
	public String getText() {
		return this.text;
	}
	
	public User getAuthor() {
		return this.author;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}
}
