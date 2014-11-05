package com.team09.qanda;

import java.util.ArrayList;


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

	public void sort() {
		// TODO Auto-generated method stub
		
	}
	
}
