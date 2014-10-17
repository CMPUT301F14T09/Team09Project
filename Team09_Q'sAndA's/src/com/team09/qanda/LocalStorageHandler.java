package com.team09.qanda;

public class LocalStorageHandler {
	//For local data storage using GSON/JSON

	/*private ThreadList tl;

	public LocalStorageHandler(ThreadList threadList) {
		this.tl=threadList;
	}*/

	public ThreadList getThreadList(String filename) {
		// TODO Auto-generated method stub
		return new ThreadList();
	}
	
	public void saveQuestionThread(QuestionThread qt, String filename) {
		ThreadList tl=this.getThreadList(filename);
		ThreadListController cont=new ThreadListController(tl);
		cont.addThread(qt);
	}

}
