package com.team09.qanda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.team09.qanda.controllers.PostController;
import com.team09.qanda.controllers.QuestionThreadController;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.Reply;
import com.team09.qanda.views.PictureViewActivity;

public class ThreadAdapter extends BaseExpandableListAdapter {

	private QuestionThread thread;
	private Context context;
	private ApplicationState curState = ApplicationState.getInstance();
	
	public ThreadAdapter(Context context, int layoutResourceId, QuestionThread thread) {
		
		this.thread = thread;
		this.context = context;
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
	public Reply getChild(int groupPosition, int childPosition) {
		if (groupPosition == 0) {
			return thread.getQuestion().getReplies().get(childPosition);
		}
		else if (groupPosition == 1) {
			return null;
		}
		else {
			return thread.getAnswers().get(groupPosition - 2).getReplies().get(childPosition);
		}
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
	public View getGroupView(final int groupPosition, final boolean isExpanded,
			View convertView, final ViewGroup parent) {
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
			TextView location = (TextView) convertView.findViewById(R.id.postLoc);
			TextView upvotes = (TextView) convertView.findViewById(R.id.postUpvotes);
			TextView replyCount = (TextView) convertView.findViewById(R.id.replyCount);
			ImageButton attachmentButton = (ImageButton) convertView.findViewById(R.id.attachmentButton);
			
			Post post;
			
			if (groupPosition == 0) {
				post = thread.getQuestion();
			}
			else {
				post = thread.getAnswers().get(groupPosition-2);
			}
			
			text.setText(post.getText());
			replyCount.setText(String.valueOf(post.getReplies().size()));
			
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
			
			location.setText("Posted from: " + post.getCity());
			
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
			int imageResourceId = isExpanded ? R.drawable.replies_on : R.drawable.replies_off;
		    repliesButton.setButtonDrawable(imageResourceId);
			
			repliesButton.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {					
					if(isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
		            else ((ExpandableListView) parent).expandGroup(groupPosition, true);
				}
			});
		}
		
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (convertView == null) {
            convertView = inflater.inflate(R.layout.replies_layout, null);
        }
		
		if (!isLastChild) {
			convertView = inflater.inflate(R.layout.replies_layout, null);
			TextView replyText = (TextView) convertView.findViewById(R.id.reply);
			TextView replyAuthor = (TextView) convertView.findViewById(R.id.replyAuthor);
						
			if (groupPosition != 1) {
				Reply reply = getChild(groupPosition, childPosition);
				System.out.println(reply.getText() + "Reply is not null!");
				System.out.println(replyText);
				replyText.setText(reply.getText());
				replyAuthor.setText("- " + reply.getAuthor().getName());
			}
			
		}
		else {
			convertView = inflater.inflate(R.layout.add_reply_layout, null);
			
			ImageButton submitReplyButton = (ImageButton) convertView.findViewById(R.id.submitReplyBtn);
			
			final EditText replyTextField = (EditText) convertView.findViewById(R.id.editReplyText);
			
			final int position_copy = groupPosition;
			
			//replyTextField.setOnClickListener(l)
			/*
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					if (replyTextField != null) {
						replyTextField.requestFocus();
					}
					
				}
			}, 100);
			*/
			
			submitReplyButton.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					String replyText = replyTextField.getText().toString();
					Reply reply = new Reply(curState.getUser(), replyText);
					PostController pc;
					QuestionThreadController qtc = new QuestionThreadController(thread);
					if (position_copy == 0) {
						Post post = thread.getQuestion();
						pc = new PostController(post);
						pc.addReply(reply);
						thread.setQuestion(post);
					}
					else {
						Post post = thread.getAnswers().get(position_copy-2);
						pc = new PostController(post);
						pc.addReply(reply);
						ArrayList<Post> answers = thread.getAnswers();
						answers.set(position_copy-2, post);
						thread.setAnswers(answers);
					}
					
					AsyncSave task=new AsyncSave();
					task.execute(new QuestionThreadController[] {qtc});
					notifyDataSetChanged();
					System.out.println("whaaaaaa");
					replyTextField.setText("");
				}
			});
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}
