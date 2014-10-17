package com.team09.qanda;

import java.util.ArrayList;


public class QuestionThreadController {
	//Handles individual question threads and its answers
	private QuestionThread thread;
	
	public QuestionThreadController(QuestionThread thread) {
		this.thread=thread;
	}
	
	public void addAnswer(Post answer) {
		ArrayList<Post> answers=thread.getAnswers();
		answers.add(answer);
		thread.setAnswers(answers);
	}
	
	public ArrayList<Post> getAnswers() {
		return thread.getAnswers();
	}

	public void attachImage() {
		// TODO Auto-generated method stub
		thread.setImage();
	}

	public void sort() {
		// TODO Auto-generated method stub
		
	}

	public int answerCount() {
		return getAnswers().size();
	}
	public void addUp(){
		thread.getQuestion().setUps(thread.getQuestion().getUps()+1);
	}
	
}
