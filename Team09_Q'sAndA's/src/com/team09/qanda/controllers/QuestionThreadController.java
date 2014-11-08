package com.team09.qanda.controllers;

import java.util.ArrayList;

import com.team09.qanda.esearch.ElasticSearchHandler;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;

/**
 * 
 * Controller used to update information in questionThread, including adding answers and saving the thread to ElasticSearch.
 *
 */

public class QuestionThreadController {
	//Handles individual question threads and its answers
	private QuestionThread thread;
	private ElasticSearchHandler esh;
	
	public QuestionThreadController(QuestionThread thread) {
		this(thread,new ElasticSearchHandler());
	}
	public QuestionThreadController(QuestionThread thread,ElasticSearchHandler esh){
		this.thread=thread;
		this.esh=esh;
	}
	
	public void addAnswer(Post answer) {
		ArrayList<Post> answers=thread.getAnswers();
		answers.add(answer);
		thread.setAnswers(answers);
	}
	
	public void saveThread() {
		esh.saveThread(this.thread);
	}
	
	public void saveThread(String id) {
		esh.saveThread(this.thread, id);
	}

	public void sort() {
		// TODO Auto-generated method stub
		
	}
	
}
