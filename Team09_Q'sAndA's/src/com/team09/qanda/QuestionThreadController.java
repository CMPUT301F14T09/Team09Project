package com.team09.qanda;


public class QuestionThreadController {
	//Handles individual question threads and its answers
	private QuestionThread thread;
	
	public QuestionThreadController(QuestionThread thread) {
		this.thread=thread;
	}
	
	public void addAnswer(Post answer) {
		thread.addAnswer(answer);
	}
	
}
