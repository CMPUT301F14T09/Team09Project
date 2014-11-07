package com.team09.qanda.esearch;


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
