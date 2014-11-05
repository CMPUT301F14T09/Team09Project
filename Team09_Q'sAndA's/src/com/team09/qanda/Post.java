package com.team09.qanda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Post extends QModel<QView> implements Serializable {
	private String text;
	private User author;
	private ArrayList<User> ups;
	//This is needed as the ArrayList shows up as an empty array on elastic search (unable to sort it)
	private int upVotes;
	private boolean hasPictures;
	private ArrayList<Reply> replies;
	private Date timestamp;
	//Strings are a little hard to sort on elastic search, so I'll use time in milliseconds
	private long relativeDate;
	
	public Post(User author, String text) {
		this.author=author;
		this.text=text;
		this.ups=new ArrayList<User>();
		this.upVotes=ups.size();
		this.replies=new ArrayList<Reply>();
		this.timestamp=new Date();
		relativeDate=timestamp.getTime();
	}
	
	public int getUps() {
		upVotes = ups.size();
		return upVotes;
	}
	
	public ArrayList<User> getUpsList() {
		return this.ups;
	}
	
	public void setUps(ArrayList<User> ups) {
		this.ups=ups;
		this.upVotes=ups.size();
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
