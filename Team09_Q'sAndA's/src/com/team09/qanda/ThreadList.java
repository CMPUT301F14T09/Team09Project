package com.team09.qanda;

import java.util.ArrayList;

public class ThreadList extends QModel<QView> {
	private ElasticSearchHandler esh;
	private ArrayList<QuestionThread> threads;
	
	public ThreadList() {
		//TODO: need to fix refresh because of design change
		refresh(0, 10);
	}
	
	public void refresh(int sortStyle, int numQuestions) {
		this.threads=esh.getThreads(sortStyle, numQuestions);
	}

	public ArrayList<QuestionThread> getThreads() {
		return this.threads;
	}
	
	public ArrayList<Post> getQuestions() {
		ArrayList<Post> questions=new ArrayList<Post>();
		for (QuestionThread t:threads) {
			questions.add(t.getQuestion());
		}
		return questions;
	}
	
	public void setThreads(ArrayList<QuestionThread> threads) {
		this.threads=threads;
		notifyViews();
	}

	public QuestionThread get(int i) {
		return threads.get(i);
	}

}
