package com.team09.qanda;

import java.util.ArrayList;

public class ESResultList {
	private int total;
	private double max_score;
	private ArrayList<ESResult> hits;
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public ArrayList<ESResult> getHits() {
		return hits;
	}
	public void setHits(ArrayList<ESResult> hits) {
		this.hits = hits;
	}
	
}
