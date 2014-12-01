package com.team09.qanda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
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

/**
 * This is a custom adapter for displaying a question thread as a list
 * which includes the question, a heading to display the number of answers
 * followed by a list of answers.
 * 
 * It is used in the QuestionThreadActivity.
 * 
 * It extends BaseExpandableListAdapter so that posts can be display as
 * group items and their replies as the child items which are expanded when
 * a group item is clicked.
 * 
 */


public class ThreadAdapter extends BaseExpandableListAdapter {

	private QuestionThread thread;
	private Context context;
	private ApplicationState curState = ApplicationState.getInstance();
	
	/**
	 * The constructor takes the context of the QuestionThreadActivity using the adapter
	 * and the thread to be displayed.
	 * 
	 * @param context The context of the activity where the adapter is being used
	 * @param thread The question thread to be displayed
	 */
	public ThreadAdapter(Context context, QuestionThread thread) {
		
		this.thread = thread;
		this.context = context;
	}

	
	/**
	 * This method is used to sort answers before they are displayed
	 * It is called every time an answer is upvoted so that the answers 
	 * with most upvotes are displayed first 
	 */
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

	/**
	 * Method to update the thread on the server asynchronously
	 * This method is called every time an answer or a reply is added
	 * or a post is upvoted
	 */
	private class AsyncSave extends AsyncTask<QuestionThreadController, Void, Void> {

		@Override
		protected Void doInBackground(QuestionThreadController... params) {
			for (QuestionThreadController qtc:params) {
		    	qtc.saveThread(thread.getId());
			}
			return null;
		}
	}

	/**
	 * Returns the number of rows to be displayed in the list
	 * i.e. the number of answers plus a row for the question
	 * and a row for displaying the heading for the answers
	 */
	@Override
	public int getGroupCount() {
		return thread.getAnswers().size() + 2;
	}

	/**
	 * Returns the number of children each of the main rows has
	 * i.e. the number of replies for each of the posts
	 * Returns 0 for the second row which is the heading for
	 * the answers
	 * Returns 1 for a post that does not have any replies as a row
	 * is used to display the text box to enter reply with a submit button
	 * 
	 * @param groupPosition The index of the main group row
	 */
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

	/**
	 * Returns the post that needs to be displaying on a certain row
	 * For example getGroup(0) will return the question of the thread
	 * since it is displayed in the first row
	 * getGroup(1) returns 0 since the second row in the list is just
	 * a heading for the answers
	 * 
	 * @param groupPosition The index of the row in the list
	 * 
	 */
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

	/**
	 * Returns the reply given the index of the row in the sub-list 
	 * which is used to display the list of replies for a certain post
	 * 
	 * @param groupPosition The index of the row in the main list which is
	 * displaying the posts
	 * @param childPosition The index of the row in the sub-list expanded by
	 * a row in the main list displaying the replies
	 */
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

	/**
	 * Returns the id of the group given the row index in the main list
	 * In this case it just returns the index
	 * 
	 * @param groupPosition The index of the row in the main list
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * Returns the id of the child given the row index in the child list
	 * In this case it just returns the index
	 * 
	 * @param groupPosition The index of the row in the child list
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/**
	 * Returns if the child and group IDs are stable across changes to
	 * the underlying data.
	 * 
	 * In our case, this is false as the data is subject to change.
	 */
	@Override
	public boolean hasStableIds() {
		return false;
	}

	/**
	 * 
	 */
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

	/**
	 * 
	 */
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

	/**
	 * Indicates that the items in the child lists are not selectable
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}
