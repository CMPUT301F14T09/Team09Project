package com.team09.qanda;

import java.util.ArrayList;

public class ThreadController {
	//Handles individual question threads and its answers
	private QuestionThread thread;
	private ArrayList<Post> answers;
	
	public ThreadController(QuestionThread thread) {
		this.thread=thread;
		loadAnswers();
	}
	
	private void loadAnswers() {
		this.answers=thread.getAnswers();
	}
	
	public void addAnswer(Post answer) {
		answers.add(answer);
	}
	
}
