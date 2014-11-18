package com.team09.qanda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.team09.qanda.controllers.PostController;
import com.team09.qanda.controllers.QuestionThreadController;
import com.team09.qanda.esearch.ElasticSearchHandler;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.Reply;
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
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ThreadAdapter extends BaseExpandableListAdapter {

	private QuestionThread thread;
	private Context context;
	
	public ThreadAdapter(Context context, int layoutResourceId, QuestionThread thread) {
		
		this.thread = thread;
		this.context = context;
		
//		PostController pc = new PostController(thread.getQuestion());
//		Reply reply = new Reply());
//		pc.addReply(reply)
	}

	

	
	protected void sortAnswers() {
		Collections.sort(thread.getAnswers(), new Comparator<Post>(){

			@Override
			public int compare(Post lhs, Post rhs) {
				return new Integer(lhs.getUps()).compareTo(rhs.getUps());
			}
			
		});
		Collections.reverse(thread.getAnswers());
		notifyDataSetChanged();
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

	@Override
	public int getGroupCount() {
		return thread.getAnswers().size() + 2;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition == 0) {
			return thread.getQuestion().getReplies().size()+1;
		}
		else if (groupPosition == 1) {
			return 0;
		}
		else {
			return thread.getAnswers().get(groupPosition - 2).getReplies().size()+1;
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		if (groupPosition == 0) {
			return thread.getQuestion();
		}
		else if (groupPosition == 1) {
			return 0;
		}
		else {
			return thread.getAnswers().get(groupPosition - 2);
		}
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if (groupPosition == 0) {
			return thread.getQuestion().getReplies().get(childPosition);
		}
		else if (groupPosition == 1) {
			return 0;
		}
		else {
			return thread.getAnswers().get(groupPosition - 2).getReplies().get(childPosition);
		}
//		if (childPosition == 0){
//			return "looool";
//		}
//		else {
//			return "haha";
//		}
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.thread_row_layout, parent, false);
		}	
		if (groupPosition == 1) {
			convertView = inflater.inflate(R.layout.answers_heading_row, parent, false);
			convertView.setOnClickListener(null);
		} else {
			convertView = inflater.inflate(R.layout.thread_row_layout, parent, false);
		}
		
		if (groupPosition == 1) {
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
			
			if (groupPosition == 0) {
				post = thread.getQuestion();
			}
			else {
				post = thread.getAnswers().get(groupPosition-2);
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
			
			final int position_copy = groupPosition;

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
					sortAnswers();
					AsyncSave task=new AsyncSave();
					task.execute(new QuestionThreadController[] {qtc});
					notifyDataSetChanged();
				}
			});
			
			CheckBox repliesButton = (CheckBox) convertView.findViewById(R.id.repliesButton);
			
			
			
//			
//			final ListView listView = (ListView) convertView.findViewById(R.id.RepliesView);
//			 
//			String[] values = new String[] {
//                    "Adapter implementation",
//                    "Simple List View In Android",
//                    "Create List View Android", 
//                    "Android Example", 
//                    "List View Source Code", 
//                    "List View Array Adapter", 
//                    "Android Example List View" 
//                   };
//			
//			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
//		              android.R.layout.simple_list_item_1, values);
//			
//			listView.setAdapter(adapter);
//			
//			repliesButton.setOnClickListener(new View.OnClickListener() {
//				
//				public void onClick(View v) {
//					Toast.makeText(context, "loool", Toast.LENGTH_SHORT).show();
//					
//					if (listView.getVisibility() == View.VISIBLE) {
//						listView.setVisibility(View.GONE);
//					}
//					else {
//						listView.setVisibility(View.VISIBLE);
//					}
//				}
//			});
		}
		
				
				
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		//final String childText = (String) getChild(groupPosition, childPosition);
		
		LayoutInflater inflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (convertView == null) {
            convertView = inflater.inflate(R.layout.replies_layout, null);
        }
		
		if (isLastChild) {
			convertView = inflater.inflate(R.layout.replies_layout, null);
			
		}
		
		TextView txtListChild = (TextView) convertView
	                .findViewById(R.id.lblListItem);
		
		if (groupPosition != 2) {
			txtListChild.setText("WHaaaa");
		}
			
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}
