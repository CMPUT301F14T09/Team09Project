package com.team09.qanda;

import java.util.ArrayList;
import java.util.Date;

public class Post {
	private String text;
	private User author;
	private int ups;
	private ArrayList<Reply> replies;
	private Date timestamp;
	
	public Post(User author, String text) {
		this.author=author;
		this.text=text;
		this.ups=0;
		this.replies=new ArrayList<Reply>();
		this.timestamp=new Date();
	}
	
	public int getUps() {
		return this.ups;
	}
	
	public void setUps(int ups) {
		this.ups=ups;
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
	
	public void setReplies(ArrayList<Reply> replies) {
		this.replies=replies;
	}
	
	public Date getTimestamp() {
		return this.timestamp;
	}
	
	public String toString() {
		return this.text;
	}
	
	public void setImage() {
		// TODO
	}

	public Boolean isImageSet() {
		// TODO Auto-generated method stub
		return false;
	}

}
