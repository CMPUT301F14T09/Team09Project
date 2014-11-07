package com.team09.qanda;

import java.util.ArrayList;

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
		this.thread=thread;
		this.esh=new ElasticSearchHandler();
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
