package com.team09.qanda;

import java.util.ArrayList;

public class QuestionThread {
	private Post question;
	private ArrayList<Post> answers;
	
	public QuestionThread(Post question) {
		this.question=question;
		answers=new ArrayList<Post>();
	}
	
	public Post getQuestion() {
		return this.question;
	}
	
	public ArrayList<Post> getAnswers() {
		return this.answers;
	}
	
	public int getNumOfAnswers() {
		return this.answers.size();
	}
	
	public void addAnswer(Post answer) {
		this.answers.add(answer);
	}

	public void attachImage() {
		// TODO
	}

}