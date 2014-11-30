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
	
	/**
	 * A Reply is used to represent a response to a Post 
	 * @param author, the User responsible for the reply
	 * @param text, the String representation of the reply
	 */
	public Reply(User author, String text) {
		this.author=author;
		this.text=text;
		this.timestamp=new Date();
	}
	
	/**
	 * This constructor allows for a REply to be instantiated with a
	 * custom date. It is intended for testing purposes only
	 * @param author, the User responsible for the reply
	 * @param text, the String representation of the reply
	 * @param date, The date that this object was/will be created
	 */
	public Reply(User author, String text, Date date) {
		this.author=author;
		this.text=text;
		this.timestamp= date;
	}
	/**
	 * Get the String representation of the Reply
	 * @return the String representation of the Reply
	 */
	public String getText() {
		return this.text;
	}
	/**
	 * Get the User that is responsible for this Reply.
	 * @return the Author, the User object which is associated with this Reply
	 */
	public User getAuthor() {
		return this.author;
	}
	/**
	 * The timestamp of this Post refers to
	 * to the Date and time (represented by the Java Date class) when the Reply object was first instantiated
	 * @return the Date object associated with the particular date and time when the Reply was first created. 
	 */
	public Date getTimestamp() {
		return this.timestamp;
	}
}
