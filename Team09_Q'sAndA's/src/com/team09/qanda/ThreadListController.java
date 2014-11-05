package com.team09.qanda;
import java.util.ArrayList;


public class ThreadListController {
	//Handles the questions displayed to the user on the main screen of the application
	private ThreadList tl;
	private ElasticSearchHandler esh;
	
	public ThreadListController(ThreadList threadList) {
		this.tl=threadList;
		this.esh=new ElasticSearchHandler();
	}
	
	public void addThread(QuestionThread thread) {
		ArrayList<QuestionThread> threads=tl.getThreads();
		threads.add(thread);
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
	public void sortThreads(String sortType){
		tl.setSortType(sortType);
		refreshThreads();
	}
	
	public void saveThread(QuestionThread thread) {
		esh.saveThread(thread);
	}
	
	public void clear() {
		tl.setThreads(new ArrayList<QuestionThread>());
	}
	
	public void cleanup() {
		esh.cleanup();
	}
	
}