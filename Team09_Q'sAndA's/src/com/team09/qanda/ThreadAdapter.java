package com.team09.qanda;

import java.util.ArrayList;
import java.util.Comparator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ThreadAdapter extends ArrayAdapter<Post> {

	private QuestionThread thread;
	private Context context;
	
	public ThreadAdapter(Context context, int layoutResourceId, QuestionThread thread) {
		super(context, layoutResourceId, thread.getAnswers());
		this.thread = thread;
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.thread_row_layout, parent, false);
		}
		
		
		TextView text = (TextView) convertView.findViewById(R.id.post);
		TextView author = (TextView) convertView.findViewById(R.id.postAuthor);
		TextView upvotes = (TextView) convertView.findViewById(R.id.postUpvotes);
		
		Post post = thread.getAnswers().get(position);
		
		text.setText(post.getText());
		author.setText(" - " + post.getAuthor().getName());
		upvotes.setText(post.getUps() + " Point(s)");
		
		return convertView;
	}

	
	

}
