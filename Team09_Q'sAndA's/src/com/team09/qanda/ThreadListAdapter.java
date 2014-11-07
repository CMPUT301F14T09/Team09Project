package com.team09.qanda;

import java.util.ArrayList;
import java.util.Comparator;

import com.team09.qanda.models.QuestionThread;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ThreadListAdapter extends ArrayAdapter<QuestionThread> {

	private ArrayList<QuestionThread> threads;
	private Context context;
	
	public ThreadListAdapter(Context context, int layoutResourceId, ArrayList<QuestionThread> threads) {
		super(context, layoutResourceId, threads);
		this.threads = threads;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.main_row_layout, parent, false);
		}
		TextView question = (TextView) convertView.findViewById(R.id.questionMain);
		TextView author = (TextView) convertView.findViewById(R.id.authorMain);
		TextView points = (TextView) convertView.findViewById(R.id.numOfPoints);
		TextView answers = (TextView) convertView.findViewById(R.id.numOfAnswers);
		
		QuestionThread thread = threads.get(position);
		
		question.setText(thread.getQuestion().getText());
		//author.setText(" - " + thread.getQuestion().getAuthor().getName());
		points.setText(thread.getQuestion().getUps() + " Point(s)");
		answers.setText(thread.getAnswers().size() + " Answer(s)");
		
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
