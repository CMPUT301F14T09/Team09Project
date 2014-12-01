package com.team09.qanda.controllers;

import java.util.ArrayList;

import com.team09.qanda.esearch.ElasticSearchHandler;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;

/**
 * 
 * This class makes updates to a QuestionThread object. The object passed to the
 * constructor will be the one that will be changed.
 * The methods use the getters and setters in QuestionThread to perform more meaningful actions
 *
 */

public class QuestionThreadController {
	
	private QuestionThread thread;
	private ElasticSearchHandler esh;
	
	/**
	 * The constructor accepts a QuestionThread object, which it will use and update.
	 * By default it creates a standard ElasticSearchHandler to communicate with the server.
	 * @param thread The QuestionThread object to be used.
	 */
	public QuestionThreadController(QuestionThread thread) {
		this(thread,new ElasticSearchHandler());
	}
	
	/**
	 * An optional constructor with the choice to add a different ElasticSearchHandler.
	 * This can be used for testing to push questions to a different location than
	 * the main index.
	 * @param thread The QuestionThread object to be used.
	 * @param esh The ElasticSearchHandler to communicate with the server
	 */
	public QuestionThreadController(QuestionThread thread,ElasticSearchHandler esh){
		this.thread=thread;
		this.esh=esh;
	}
	
	/**
	 * Adds an answer to the current list of answers in the QuestionThread
	 * @param answer The answer to be added, in the form of a Post object
	 */
	public void addAnswer(Post answer) {
		ArrayList<Post> answers=thread.getAnswers();
		answers.add(answer);
		thread.setAnswers(answers);
	}
	
	/**
	 * Saves the QuestionThread to the server using the ElasticSearchHandler's settings.
	 */
	public void saveThread() {
		esh.saveThread(this.thread, this.thread.getId());
	}
	
	/**
	 * Saves the QuestionThread to the server using the ElasticSearchHandler's settings, as
	 * well as setting a specific id for the object to be referenced by the application
	 * and elasticsearch.
	 * @param id A string representing a unique id.
	 */
	public void saveThread(String id) {
		esh.saveThread(this.thread, id);
	}

	public void sort() {
		// TODO Auto-generated method stub
		
	}
	
}
