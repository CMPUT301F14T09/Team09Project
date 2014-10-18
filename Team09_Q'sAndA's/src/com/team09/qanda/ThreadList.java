package com.team09.qanda;

import java.util.ArrayList;

public class ThreadList extends QModel<QView> {
	private ElasticSearchHandler esh;
	private ArrayList<QuestionThread> threads;
	
	public ThreadList() {
		refresh();
	}
	
	public void refresh() {
		this.threads=esh.getThreads();
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
