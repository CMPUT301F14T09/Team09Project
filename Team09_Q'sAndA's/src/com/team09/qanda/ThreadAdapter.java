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
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			//if (position == 1) {
				//convertView = inflater.inflate(R.layout.answers_heading_row, parent, false);
			//}
			//else {
				convertView = inflater.inflate(R.layout.thread_row_layout, parent, false);
			//}
		}
		
		//if (position == 1) {
			//TextView text = (TextView) convertView.findViewById(R.id.answersHeading);
			//text.setText("Answers (" + thread.getAnswers().size() + ")");
		//}
		//else {
			TextView text = (TextView) convertView.findViewById(R.id.post);
			TextView author = (TextView) convertView.findViewById(R.id.postAuthor);
			TextView upvotes = (TextView) convertView.findViewById(R.id.postUpvotes);
			
			//if (position == 0) {
				//text.setText(threadPosts.getQuestion().getText());
				//author.setText(" - " + thread.getQuestion().getAuthor().getName());
				//upvotes.setText(thread.getQuestion().getUps() + " Point(s)");
			//}
			//else {
				Post post = threadPosts.get(position);
				
				text.setText(post.getText());
				author.setText(" - " + post.getAuthor().getName());
				upvotes.setText(post.getUps() + " Point(s)");
			//}
		//}
		
		return convertView;
	}

	
	

}
