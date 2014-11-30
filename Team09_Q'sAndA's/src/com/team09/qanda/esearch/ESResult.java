package com.team09.qanda.esearch;

/**
 * This class contains an individual result returned from a query
 * to the elasticsearch server.
 */
import com.team09.qanda.models.QuestionThread;

public class ESResult {
	private String _index;
	private String _type;
	private String _id;
	private double _score;
	private QuestionThread _source;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public QuestionThread get_source() {
		return _source;
	}
	public void set_source(QuestionThread _source) {
		this._source = _source;
	}
	
}
