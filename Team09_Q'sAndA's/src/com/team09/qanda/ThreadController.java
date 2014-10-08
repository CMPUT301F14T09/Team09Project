package com.team09.qanda;


public class ThreadController {
	//Handles individual question threads and its answers
	private QuestionThread thread;
	
	public ThreadController(QuestionThread thread) {
		this.thread=thread;
	}
	
	public void addAnswer(Post answer) {
		thread.addAnswer(answer);
	}
	
}