package com.team09.qanda;

import java.util.ArrayList;

public class ThreadController {
	//Handles individual question threads and its answers
	private Thread thread;
	private Post question;
	private ArrayList<Post> answers;
	
	public ThreadController(Thread thread) {
		this.thread=thread;
		loadQuestion();
		loadAnswers();
	}
	
	private void loadQuestion() {
		this.question=thread.getQuestion();
	}
	
	private void loadAnswers() {
		this.answers=thread.getAnswers();
	}
	
	public Post getQuestion() {
		return question;
	}
	
	public ArrayList<Post> getAnswers() {
		return answers;
	}
	
	public void addAnswer(Post answer) {
		answers.add(answer);
	}
	
}
