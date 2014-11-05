package com.team09.qanda;

public class ESResult {
	private String _id;
	private QuestionThread _source;
	
	public String getId() {
		return _id;
	}
	
	public QuestionThread getThread() {
		return _source;
	}
	
	public void setId(String id) {
		this._id=id;
	}
	
	public void setThread(QuestionThread thread) {
		this._source=thread;
	}
}
