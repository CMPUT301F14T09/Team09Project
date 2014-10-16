package com.team09.qanda;

import java.util.ArrayList;


public class QuestionThreadController {
	//Handles individual question threads and its answers
	private QuestionThread thread;
	
	public QuestionThreadController(QuestionThread thread) {
		this.thread=thread;
	}
	
	public void addAnswer(Post answer) {
		thread.setAnswer(answer);
	}
	
	public ArrayList<Post> getAnswers() {
		return thread.getAnswers();
	}

	public void attachImage() {
		// TODO Auto-generated method stub
		thread.attachImage();
	}

	public void sort() {
		// TODO Auto-generated method stub
		
	}
	
}
