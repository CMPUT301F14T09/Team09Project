package com.team09.qanda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Post extends QModel<QView> implements Serializable {
	private String text;
	private User author;
	private ArrayList<User> ups;
	private boolean hasPictures;
	private ArrayList<Reply> replies;
	private Date timestamp;
	
	public Post(User author, String text) {
		this.author=author;
		this.text=text;
		this.ups=new ArrayList<User>();
		this.replies=new ArrayList<Reply>();
		this.timestamp=new Date();
	}
	
	public int getUps() {
		return this.ups.size();
	}
	
	public ArrayList<User> getUpsList() {
		return this.ups;
	}
	
	public void setUps(ArrayList<User> ups) {
		this.ups=ups;
		notifyViews();
	}
	
	public User getAuthor() {
		return this.author;
	}
	
	public String getText() {
		return this.text;
	}
	
	public ArrayList<Reply> getReplies() {
		return this.replies;
	}
	
	public void setReplies(ArrayList<Reply> replies) {
		this.replies=replies;
		notifyViews();
	}
	
	public Date getTimestamp() {
		return this.timestamp;
	}
	
	public String toString() {
		return this.text;
	}
	
	public void setImage() {
		// TODO
		hasPictures=true;
	}

	public Boolean isImageSet() {
		return hasPictures;
	}

}
