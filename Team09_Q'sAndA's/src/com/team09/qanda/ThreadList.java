package com.team09.qanda;

import java.util.ArrayList;

public class ThreadList {
	private ElasticSearchHandler esh;
	private ArrayList<Thread> threads;
	
	public ThreadList() {
		retrieveThreads();
	}
	
	private void retrieveThreads() {
		this.threads=esh.getThreads();
	}
	
	public ArrayList<Thread> getThreads() {
		return this.threads;
	}

}
