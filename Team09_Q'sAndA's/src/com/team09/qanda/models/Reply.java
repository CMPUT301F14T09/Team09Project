package com.team09.qanda.models;

import java.io.Serializable;
import java.util.Date;

import com.team09.qanda.views.QView;

/**
 * 
 * This class allows creation of a reply. The constructor takes in the User that created the reply,
 * as well as the reply text. A timestamp is then generated.
 *
 */

public class Reply extends QModel<QView> implements Serializable {
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
