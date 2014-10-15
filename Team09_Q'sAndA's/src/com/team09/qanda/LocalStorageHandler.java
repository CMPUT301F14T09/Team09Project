package com.team09.qanda;

public class LocalStorageHandler {
	//For local data storage using GSON/JSON

	private ThreadList tl;

	public LocalStorageHandler(ThreadList threadList) {
		this.tl=threadList;
	}

	public ThreadList getThreadList() {
		// TODO Auto-generated method stub
		return tl;
	}

}
