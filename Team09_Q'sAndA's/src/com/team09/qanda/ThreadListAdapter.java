package com.team09.qanda;

import java.util.ArrayList;
import java.util.Comparator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.team09.qanda.models.QuestionThread;

public class ThreadListAdapter extends ArrayAdapter<QuestionThread> {

	private static final String READ_LATER_FILENAME = "read_later.txt";
	private ArrayList<String> ids;
	private LocalStorageHandler lsh;
	private ArrayList<QuestionThread> threads;
	private Context context;
	private int resId;
	private boolean isMain;
	
	public ThreadListAdapter(Context context, int layoutResourceId, ArrayList<QuestionThread> threads, boolean isMain) {
		super(context, layoutResourceId, threads);
		this.threads = threads;
		this.context = context;
		this.isMain=isMain;
		this.resId=layoutResourceId;
		this.lsh=new LocalStorageHandler();
		this.ids=lsh.getIds(context, "later_ids.txt");
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(this.resId, parent, false);
		}
		final ListView list=(ListView)parent.findViewById(R.id.MainListView);
		TextView question = (TextView) convertView.findViewById(R.id.questionMain);
		TextView author = (TextView) convertView.findViewById(R.id.authorMain);
		TextView points = (TextView) convertView.findViewById(R.id.numOfPoints);
		TextView answers = (TextView) convertView.findViewById(R.id.numOfAnswers);
		CheckBox later = (CheckBox) convertView.findViewById(R.id.saveQuestion);
		
		final QuestionThread thread = threads.get(position);
		
		if (isMain) {
			//ids=lsh.getIds(context, READ_LATER_FILENAME);
			later.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ids=lsh.getIds(context, "later_ids.txt");
					if (ids.contains(thread.getId())) {
						list.performItemClick(null, -1*position, -2);
					}
					else {
						list.performItemClick(null, -1*position, -1);
					}
					//ids=lsh.getIds(context, READ_LATER_FILENAME);
				}
			});
			if (ids.contains(thread.getId())) {
				later.setChecked(true);
			}
			else {
				later.setChecked(false);
			}
		}
		
		question.setText(thread.getQuestion().getText());
		author.setText(" - " + thread.getQuestion().getAuthor().getName());
		if (thread.getQuestion().getUps() == 1) {
			points.setText(thread.getQuestion().getUps() + " Point");		}
		else { 
			points.setText(thread.getQuestion().getUps() + " Points");		}
		
		if (thread.getAnswers().size() == 1) {
			answers.setText(thread.getAnswers().size() + " Answer");		}
		else { 
			answers.setText(thread.getAnswers().size() + " Answers");		}
		
		
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
