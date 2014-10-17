package com.team09.qanda;

import java.util.ArrayList;

public class QModel<V extends QView> {
	private ArrayList<V> views;
	
	public QModel() {
		views=new ArrayList<V>();
	}
	
	public void addView(V view) {
		if (!views.contains(view)) {
			views.add(view);
		}
	}
	
	public void deleteView(V view) {
		views.remove(view);
	}
	
	public void notifyViews() {
		for (V view: views) {
			view.update(this);
		}
	}
}