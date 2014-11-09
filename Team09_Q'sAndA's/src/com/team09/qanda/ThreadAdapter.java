package com.team09.qanda;

import java.util.ArrayList;

import com.team09.qanda.controllers.PostController;
import com.team09.qanda.controllers.QuestionThreadController;
import com.team09.qanda.esearch.ElasticSearchHandler;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.views.PictureViewActivity;
import com.team09.qanda.views.QuestionThreadActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ThreadAdapter extends ArrayAdapter<Post> {

	private QuestionThread thread;
	private Context context;
	
	public ThreadAdapter(Context context, int layoutResourceId, QuestionThread thread) {
		super(context, layoutResourceId);
		
		this.thread = thread;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return thread.getAnswers().size() + 2;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.thread_row_layout, parent, false);
		}	
		if (position == 1) {
			convertView = inflater.inflate(R.layout.answers_heading_row, parent, false);
			convertView.setOnClickListener(null);
		} else {
			convertView = inflater.inflate(R.layout.thread_row_layout, parent, false);
		}
		
		if (position == 1) {
			TextView text = (TextView) convertView.findViewById(R.id.answersHeading);
			int numAnswers = thread.getAnswers().size();
			if (numAnswers == 1) {
				text.setText(numAnswers + " Answer");		}
			else { 
				text.setText(numAnswers + " Answers");		}
			
		}
		else {
			TextView text = (TextView) convertView.findViewById(R.id.post);
			TextView author = (TextView) convertView.findViewById(R.id.postAuthor);
			TextView upvotes = (TextView) convertView.findViewById(R.id.postUpvotes);
			ImageButton attachmentButton = (ImageButton) convertView.findViewById(R.id.attachmentButton);
			
			Post post;
			
			if (position == 0) {
				post = thread.getQuestion();
			}
			else {
				post = thread.getAnswers().get(position-2);
			}
			
			text.setText(post.getText());
			
			if (post.getUps() == 1) {
				upvotes.setText(post.getUps() + " Point  ");
			}
			else { 
				upvotes.setText(post.getUps() + " Points"); 
			}
			
			try {
				author.setText("- "+post.getAuthor().getName());
			}
			catch (NullPointerException e) {
				author.setText("- Null");
			}
				
			if (post.isImageSet() == false) {
				attachmentButton.setVisibility(View.INVISIBLE);
			}
			else {
				final Post post_copy = post;
				attachmentButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, PictureViewActivity.class);
						intent.putExtra("Selected Post", post_copy);
						context.startActivity(intent);
					}
				});
			}
			
			PostController qpc = new PostController(post);
			CheckBox upvoteBox = (CheckBox) convertView.findViewById(R.id.upvoteCheckbox);
			upvoteBox.setChecked(qpc.alreadyUpvoted());
			
			final int position_copy = position;

			upvoteBox.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					PostController qpc;
					QuestionThreadController qtc = new QuestionThreadController(thread);
					if (position_copy == 0) {
						Post post = thread.getQuestion();
						qpc = new PostController(post);
						qpc.addUp();
						thread.setQuestion(post);
					}
					else {
						Post post = thread.getAnswers().get(position_copy-2);
						qpc = new PostController(post);
						qpc.addUp();
						ArrayList<Post> answers = thread.getAnswers();
						answers.set(position_copy-2, post);
						thread.setAnswers(answers);
					}
					AsyncSave task=new AsyncSave();
					task.execute(new QuestionThreadController[] {qtc});
					notifyDataSetChanged();
				}
			});
			
			
		}
		
				
				
		return convertView;
	}
	
	private class AsyncSave extends AsyncTask<QuestionThreadController, Void, Void> {

		@Override
		protected Void doInBackground(QuestionThreadController... params) {
			for (QuestionThreadController qtc:params) {
		    	qtc.saveThread(thread.getId());
			}
			return null;
		}
	}
}
