package com.team09.qanda.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.team09.qanda.views.QView;

/**
 * 
 * This class is used to represent a thread consisting of a question
 * and its answers, as well as all of their contained information.
 *
 */

public class QuestionThread extends QModel<QView> implements Serializable {
	private String id;
	private Post question;
	private ArrayList<Post> answers;
	
	/**
	 * A QuestionThread features a Question (a Post object) and any of its related answers (also a Post object),
	 * and replies 
	 * @param question, the main Post of the QuestionThread(ie. the question)
	 */
	public QuestionThread(Post question) {
		this.question=question;
		answers=new ArrayList<Post>();
	}
	/**
	 * Returns the id of the QuestionThread.
	 * This id used to save QuestionThreads locally (via the Local Storage handler)
	 * and as an identifier for the Elastic Search Handler.
	 * @return a String representation of the QuestionThread's id
	 * 
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Sets the id of the QuestionThread.
	 * This id used to save QuestionThreads locally (via the Local Storage handler)
	 * and as an identifier for the Elastic Search Handler.
	 */
	public void setId(String id) {
		this.id=id;
	}
	/**
	 * Get the question (the main Post object) of this QuestionThread
	 * @return A Post object  which represents the Question
	 */
	public Post getQuestion() {
		return this.question;
	}
	/**
	 * Get the answers to the question of the QuestionThread, if any
	 * @return An ArrayList of Posts containing all the answers relating to this QuestionThreads' question
	 */
	public ArrayList<Post> getAnswers() {
		return this.answers;
	}
	/**
	 * Find the number answers that this QuestionThreads' question has.
	 * It uses the size() method of an ArrayList.Meaning, a call to this function is equivalent to calling getAnwers().size()
	 * @return an int representing the total number of answers
	 */
	public int answerCount() {
		return this.answers.size();
	}
	/**
	 * Set the answers to the question of the QuestionThread.
	 * A call to this function will delete this QuestionThreads' answers.
	 * If previous answers need to be preserved, one should first make a call
	 * to getAnswers() and the answers to the corresponding ArrayList.
	 * @return An ArrayList of Posts containing all the answers relating to this QuestionThreads' question
	 */
	public void setAnswers(ArrayList<Post> answers) {
		this.answers=answers;
		notifyViews();
	}
	/**
	 * Set the question (the main Post object) of this QuestionThread
	 */
	public void setQuestion(Post question) {
		this.question = question;
	}
}