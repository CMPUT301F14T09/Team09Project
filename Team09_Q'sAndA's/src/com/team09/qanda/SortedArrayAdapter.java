package com.team09.qanda;

import java.util.Comparator;

import android.content.Context;
import android.widget.ArrayAdapter;

public class SortedArrayAdapter extends ArrayAdapter<QuestionThread> {

	public SortedArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
	}

	public void sortByHasPictures() {
		sort(new Comparator<QuestionThread>(){
			@Override
			public int compare(QuestionThread lhs, QuestionThread rhs) {
				if(lhs.getQuestion().isImageSet() && rhs.getQuestion().isImageSet()){
					return 0;
				}
				else if(lhs.getQuestion().isImageSet() && !rhs.getQuestion().isImageSet()){
					return -1;
				}
				return 1;
			}
			
		});
	}

	public void sortByMostRecent() {
		sort(new Comparator<QuestionThread>(){
			@Override
			public int compare(QuestionThread lhs, QuestionThread rhs) {
				if(lhs.getQuestion().getUps()>rhs.getQuestion().getUps()){
					return -1;
				}
				else if(lhs.getQuestion().getUps()<rhs.getQuestion().getUps()){
					return 1;
				}
				return 0;
			}
			
		});
	}

	public void sortByLeastUpVoted() {
		sort(new Comparator<QuestionThread>(){
			@Override
			public int compare(QuestionThread lhs, QuestionThread rhs) {
				return Integer.valueOf(lhs.getQuestion().getUps()).compareTo(rhs.getQuestion().getUps());
			}
			
		});
		
	}

	public void sortByMostUpVoted() {
		sort(new Comparator<QuestionThread>(){
			@Override
			public int compare(QuestionThread lhs, QuestionThread rhs) {
				if(lhs.getQuestion().getUps()>rhs.getQuestion().getUps()){
					return -1;
				}
				else if(lhs.getQuestion().getUps()<rhs.getQuestion().getUps()){
					return 1;
				}
				return 0;
			}
			
		});
	}

	public void sortByOldest() {
		sort(new Comparator<QuestionThread>(){
			@Override
			public int compare(QuestionThread lhs, QuestionThread rhs) {
				return lhs.getQuestion().getTimestamp().compareTo(rhs.getQuestion().getTimestamp());
			}
			
		});
		
	}

}
