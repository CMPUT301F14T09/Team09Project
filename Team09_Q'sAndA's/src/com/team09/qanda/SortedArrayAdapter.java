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
		// TODO Auto-generated method stub
		sort(new Comparator<QuestionThread>(){
			@Override
			public int compare(QuestionThread lhs, QuestionThread rhs) {
				// TODO Auto-generated method stub
				return 0;
			}
			
		});
	}

	public void sortByMostRecent() {
		sort(new Comparator<QuestionThread>(){
			@Override
			public int compare(QuestionThread lhs, QuestionThread rhs) {
				// TODO Auto-generated method stub
				return 0;
			}
			
		});
	}

	public void sortByLeastUpVoted() {
		sort(new Comparator<QuestionThread>(){
			@Override
			public int compare(QuestionThread lhs, QuestionThread rhs) {
				// TODO Auto-generated method stub
				return 0;
			}
			
		});
		
	}

	public void sortByMostUpVoted() {
		sort(new Comparator<QuestionThread>(){
			@Override
			public int compare(QuestionThread lhs, QuestionThread rhs) {
				// TODO Auto-generated method stub
				return 0;
			}
			
		});
	}

	public void sortByOldest() {
		// TODO Auto-generated method stub
		
	}

}
