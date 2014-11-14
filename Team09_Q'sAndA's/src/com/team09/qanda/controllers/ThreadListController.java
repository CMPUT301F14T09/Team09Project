package com.team09.qanda.controllers;
import java.util.ArrayList;

import com.team09.qanda.esearch.ElasticSearchHandler;
import com.team09.qanda.esearch.SimpleSortFactory;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.ThreadList;


public class ThreadListController {
	//Handles the questions displayed to the user on the main screen of the application
	private ThreadList tl;
	private ElasticSearchHandler esh;
	
	public ThreadListController(ThreadList threadList) {
		this(threadList,new ElasticSearchHandler());
	}
	public ThreadListController(ThreadList threadList, ElasticSearchHandler esh){
		this.tl=threadList;
		this.esh=esh;
	}
	
	public void addThread(QuestionThread thread) {
		ArrayList<QuestionThread> threads=tl.getThreads();
		threads.add(thread);
		tl.setThreads(threads);
	}
	
	public void removeThread(QuestionThread thread) {
		ArrayList<QuestionThread> threads=tl.getThreads();
		for (int i=0;i<threads.size();i++) {
			if (threads.get(i).getId().equals(thread.getId())) {
				threads.remove(i);
				break;
			}
		}
		tl.setThreads(threads);
	}
	
	public void refreshThreads() {
		tl.setThreads(esh.getThreads(tl.getSortType(), tl.getNumThreads()));
	}
	
	public void getMoreThreads() {
		int current=tl.getNumThreads();
		int more=current+10;
		tl.setNumThreads(more);
		tl.setThreads(esh.getThreads(tl.getSortType(), tl.getNumThreads()));
	}
	public void changeSortType(SimpleSortFactory factory){
		tl.setSortType(factory.createSort());
	}
	
	public void clear() {
		tl.setThreads(new ArrayList<QuestionThread>());
	}
	
	public void cleanup() {
		esh.cleanup();
	}
	//For testing purposes only
	public void changeESHHandler(ElasticSearchHandler esh){
		this.esh=esh;
	}
	
}