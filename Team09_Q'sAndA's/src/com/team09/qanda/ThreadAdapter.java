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

	private ArrayList<Post> threadPosts;
	private Context context;
	
	public ThreadAdapter(Context context, int layoutResourceId, ArrayList<Post> threadPosts) {
		super(context, layoutResourceId, threadPosts);
		
		this.threadPosts = threadPosts;
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.thread_row_layout, parent, false);
		}	
		/*if (position == 1) {
			convertView = inflater.inflate(R.layout.answers_heading_row, parent, false);
		}
		if (position == 2) {
			convertView = inflater.inflate(R.layout.thread_row_layout, parent, false);
		}
		
		if (position == 1) {
			TextView text = (TextView) convertView.findViewById(R.id.answersHeading);
			int numAnswers = threadPosts.size() - 2;
			text.setText(numAnswers + " Answers");
		}*/
		//else {
			TextView text = (TextView) convertView.findViewById(R.id.post);
			TextView author = (TextView) convertView.findViewById(R.id.postAuthor);
			TextView upvotes = (TextView) convertView.findViewById(R.id.postUpvotes);
			
			Post post = threadPosts.get(position);
			
			text.setText(post.getText());
			author.setText(" - " + post.getAuthor().getName());
			upvotes.setText(post.getUps() + " Point(s)");
		//}
		
				
				
		return convertView;
	}

	
	

}
