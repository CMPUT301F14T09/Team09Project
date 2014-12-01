package com.team09.qanda.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.team09.qanda.views.QView;

/**
 * 
 * This is the Post class. 
 * Posts are used to represent both questions and answers.
 * They are differentiated at a higher level in the QuestionThread class.
 * This class stores the name of the author of the post, the text of the post,
 * a list of all replies, and the number of upvotes that the post has, including a list of
 * everyone who upvoted the post, to prevent a single user from upvoting a post twice.
 * 
 */

public class Post extends QModel<QView> implements Serializable {
	private String text;
	private User author;
	private ArrayList<User> ups;
	//This is needed as the ArrayList shows up as an empty array on elastic search (unable to sort it)
	private int upVotes;
	private boolean hasPictures;
	private String image;
	private ArrayList<Reply> replies;
	private Date timestamp;
	//Strings are a little hard to sort on elastic search, so I'll use time in milliseconds
	private long relativeDate;
	private String city = "N/A"; //defaults to N/A
	
	/**
	 * Get the name of the city in which the Post was created.
	 * A call to this method should be preceded a call to the setCity() method
	 * @return the name of the corresponding city. 
	 * If the name of the city has not been set (by calling setCity()), the string "N\A" will be returned
	 * @see setCity(String city)
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * Set the name of the city in which the Post was created.
	 * This method should be invoked when a call to the GPS Handler 
	 * (which attempts to get the real-world location of the end user making the post) is made.
	 * @param city the name of the city where the post will be created
	 */
	public void setCity(String city)
	{
		this.city = city;
	}
	/**
	 * A representation of a Question or Answer of a User.
	 * In addition to questions/answers, Posts can have pictures,
	 * upvotes, and replies (comments)
	 * @param author, the User responsible for the post
	 * @param text, a String which represents a question or an answer 
	 */
	public Post(User author, String text) {
		this.author=author;
		this.text=text;
		this.ups=new ArrayList<User>();
		this.upVotes=ups.size();
		this.replies=new ArrayList<Reply>();
		this.timestamp=new Date();
		relativeDate=timestamp.getTime();
	}
	/**
	 * Get the total number of upvotes of this post.
	 * Note: this method has the same effect has calling getUpsList.size()
	 * @return an int, which represents the total number of upvotes that
	 * this Post has.
	 * @see getUpsList()
	 */
	public int getUps() {
		upVotes = ups.size();
		return upVotes;
	}
	/**
	 *  Get the list that have upvoted this post.
	 *  This method should not be invoked directly. Rather
	 *  it should be invoked by the addUp() method of this Post's corresponding PostController.
	 *  A call to this method should preceded by a call to setUps()
	 *  Instead of simply returning the Post's number of upvotes, this method returns a list of Users
	 *  who have upvoted this Post (so that one check for duplicate upvotes). If you just want the number of upvotes,
	 *  a call to getUps() should be made instead.
	 * @return the list (of type ArrayList) of users that have upvoted this post.
	 * @see getUps(),setUps(ArrayList<User> ups)
	 */
	public ArrayList<User> getUpsList() {
		return this.ups;
	}
	/**
	 * 
	 *  Set the list of users who have upvoted this post.
	 *  This method should not be invoked directly. Rather
	 *  it should be invoked by the addUp() method of this Post's corresponding PostController.
	 *  The intent of this method is set the total number of upvotes. A list of users is required so
	 *  that duplicate upvotes (multiple upvotes by the same user) can be found
	 * @param ups, the ArrayList of Users of who have upvoted this post
	 * @see getUpsList()
	 */
	public void setUps(ArrayList<User> ups) {
		this.ups=ups;
		this.upVotes=ups.size();
		notifyViews();
	}
	/**
	 * Get the User that is responsible for this post.
	 * @return the Author, the User object which is aggregated by this post 
	 */
	public User getAuthor() {
		return this.author;
	}
	/**
	 * Get the textual of representation the question (or answer) of this Post
	 * @return  the text representing a  question/answer 
	 */
	public String getText() {
		return this.text;
	}
	/**
	 * Get all the replies (excluding the actual answers) to this Post.
	  A  Reply represents a comment to a question or answer
	 * @return  an ArrayList of Replies associated with this Post.
	 * @see setReplies(ArrayList<Reply> replies);
	 */
	public ArrayList<Reply> getReplies() {
		return this.replies;
	}
	/**
	 *Set all the replies (excluding the actual answers) to this Post.
	  A  Reply represents a comment to a question or answer.
	  This method also notifies any views associated with this model (ie. this Post).
	  Note: invoking this method will completely overwrite previous replies. 
	  @param replies, an ArrayList of new re
	 * @return  an ArrayList of Replies associated with this Post.
	 * @see setReplies(ArrayList<Reply> replies);
	 */
	public void setReplies(ArrayList<Reply> replies) {
		this.replies=replies;
		notifyViews();
	}
	/**
	 * The timestamp of this Post refers to
	 * to the Date and time (represented by the Java Date class) when the Post object was first instantiated
	 * @return the Date object associated with the particular date and time when the Post was first created. 
	 */
	public Date getTimestamp() {
		return this.timestamp;
	}
	/**
	 * The String representation of this Post object.
	 * Since the Post represents a question/answer, this method returns
	 * the String representaion of the actual question/answer of this post.
	 * A call to this method is equivalent to calling getText()
	 * @return A String representation of the question/answer of this Post
	 * @see getText()
	 */
	public String toString() {
		return this.text;
	}
	/** 
	 * Get the image (if any) associated with this post.
	 * The image returned by this method was formally a byte array which has been encoded to a String (for compressions reasons)
	 * If you want to get the image back, you have to make call to Base64.decode() to decode the String and then;
		BitmapFactory.decodeByteArray() to make it into a Bitmap;
	 * @return the byte array (in the form a String) of the image or null if the Post has no images
	 */
	public String getImage() {
		return this.image;
	}
	/** 
	 * Set the image to be associated with this post.
	 * The image string should be a Base64 byte array (representing an image) which has been encoded to a String
	 * Note: only one image is allowed per Post
	 * 
	 */
	public void setImage(String image) {
		this.image = image;
		setHasPicture(true);
	}
	/** 
	 * This method determines whether this Post has a picture or not.
	 * It should not be invoked directly (it is left public for testing purposes only).
	 * Rather, it should be invoked by the SetImage method when an actual image gets attached to this Post. 
	 * Note: only one image is allowed per Post
	 * 
	 */
	public void setHasPicture(boolean result) {
		hasPictures=result;
	}
	/**
	 * This method checks to see whether this object has a Post
	 * @return true, if this Post has an image or false otherwise
	 */
	public Boolean isImageSet() {
		return hasPictures;
	}

}
