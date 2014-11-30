package com.team09.qanda.esearch;

/**
 * 
 * This class is used to obtain information from the elasticsearch server.
 * It is not used in the context of the application other than to store
 * the result of queries to the server.
 *
 */
public class ESResults {
	private int took;
	private boolean timed_out;
	private Shards _shards;
	private ESResultList hits;
	
	public void setTook(int took) {
		this.took = took;
	}
	public void setHits(ESResultList hits) {
		this.hits = hits;
	}
	
	public int getTook() {
		return took;
	}
	public ESResultList getHits() {
		return hits;
	}
}
