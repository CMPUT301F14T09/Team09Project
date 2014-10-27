package com.team09.qanda;

import java.util.Comparator;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ThreadListAdapter extends ArrayAdapter<QuestionThread> {

	public ThreadListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
	}
	@Override
	//This is need so that we can have a custom textview (to represent questions
	public View getView( int position, View convertView, ViewGroup parent){
		//ToDO
		return convertView;
	}
	@Deprecated
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
	@Deprecated
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
	@Deprecated
	public void sortByLeastUpVoted() {
		sort(new Comparator<QuestionThread>(){
			@Override
			public int compare(QuestionThread lhs, QuestionThread rhs) {
				return Integer.valueOf(lhs.getQuestion().getUps()).compareTo(rhs.getQuestion().getUps());
			}
			
		});
		
	}
	@Deprecated
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
	@Deprecated
	public void sortByOldest() {
		sort(new Comparator<QuestionThread>(){
			@Override
			public int compare(QuestionThread lhs, QuestionThread rhs) {
				return lhs.getQuestion().getTimestamp().compareTo(rhs.getQuestion().getTimestamp());
			}
			
		});
		
	}

}
