package com.team09.qanda.controllers;

import java.util.ArrayList;

import com.team09.qanda.esearch.ElasticSearchHandler;
import com.team09.qanda.esearch.SimpleSortFactory;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.ThreadList;

/**
 * This class makes updates to a ThreadList object. The object passed to the
 * constructor will be the one that will be changed.
 * The methods use the getters and setters in ThreadList to perform more meaningful actions
 *
 */
public class ThreadListController {
	
	private ThreadList tl;
	private ElasticSearchHandler esh;
	
	/**
	 * The constructor accepts a ThreadList object, which it will use and update.
	 * By default it creates a standard ElasticSearchHandler to communicate with the server.
	 * @param threadList The ThreadList object to be used.
	 */
	public ThreadListController(ThreadList threadList) {
		this(threadList,new ElasticSearchHandler());
	}
	
	/**
	 * An optional constructor with the choice to add a different ElasticSearchHandler.
	 * This can be used for testing to push questions to a different location than
	 * the main index.
	 * @param threadList The ThreadList object to be used.
	 * @param esh The ElasticSearchHandler to communicate with the server
	 */
	public ThreadListController(ThreadList threadList, ElasticSearchHandler esh){
		this.tl=threadList;
		this.esh=esh;
	}
	
	/**
	 * Appends a QuestionThread to the end of the 
	 * current list of QuestionThreads.
	 * @param thread The QuestionThread to be added.
	 */
	public void addThread(QuestionThread thread) {
		ArrayList<QuestionThread> threads=tl.getThreads();
		threads.add(thread);
		tl.setThreads(threads);
	}
	
	/**
	 * Removes a given QuestionThread from the current list of QuestionThreads.
	 * It does so by comparing the two ids. If there is no match, there will
	 * be no change to the ThreadList
	 * @param thread The QuestionThread to be removed.
	 */
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
	
	/**
	 * Gets the most current data from the server.
	 * Uses the values in the ThreadList to determine how many QuestionThreads
	 * to request and in what sorting order to return them.
	 */
	public void refreshThreads() {
		tl.setThreads(esh.getThreads(tl.getSortType(), tl.getNumThreads()));
	}
	
	/**
	 * Requests 10 additional QuestionThreads from the server. This increments
	 * the value for number of threads in the ThreadList, 
	 * and uses its value for sorting.
	 */
	public void getMoreThreads() {
		int current=tl.getNumThreads();
		int more=current+10;
		tl.setNumThreads(more);
		tl.setThreads(esh.getThreads(tl.getSortType(), tl.getNumThreads()));
	}
	
	/**
	 * 
	 * @param factory
	 */
	public void changeSortType(SimpleSortFactory factory){
		tl.setSortType(factory.createSort());
	}
	
	/**
	 * 
	 * @param query
	 */
	public void search(String query){
		tl.setThreads(esh.search(query));
	}
	
	/**
	 * This clears the current list of QuestionThreads. It has no effect
	 * on the sort type or number of threads.
	 */
	public void clear() {
		tl.setThreads(new ArrayList<QuestionThread>());
	}
	
	/**
	 * This function shuts down the connection to the elasticsearch server
	 */
	public void cleanup() {
		esh.cleanup();
	}
	
	/**
	 * 
	 * @param esh
	 */
	public void changeESHHandler(ElasticSearchHandler esh){
		this.esh=esh;
	}
	
}