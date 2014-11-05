package com.team09.qanda;

import java.io.Serializable;
import java.util.ArrayList;

public class QuestionThread extends QModel<QView> implements Serializable {
	private String id;
	private Post question;
	private ArrayList<Post> answers;
	
	public QuestionThread(Post question) {
		this.question=question;
		answers=new ArrayList<Post>();
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id=id;
	}
	
	public Post getQuestion() {
		return this.question;
	}
	
	public ArrayList<Post> getAnswers() {
		return this.answers;
	}
	
	public int answerCount() {
		return this.answers.size();
	}
	
	public void setAnswers(ArrayList<Post> answers) {
		this.answers=answers;
		notifyViews();
	}

}