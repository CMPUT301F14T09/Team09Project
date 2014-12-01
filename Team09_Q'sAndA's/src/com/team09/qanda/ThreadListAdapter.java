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

/**
 * This is a custom adapter for displaying a list of questions.
 * 
 * It is used in the MainActivity for the main list of questions
 * and in the UserThreadsActivity to display locally stored questions
 * with a few changes.
 *
 * This class extends ArrayAdapter<QuestionThread> and uses an ArrayList
 * of QuestionThread objects to display the summary of a list of questions using
 * a custom layout.
 *
 */

public class ThreadListAdapter extends ArrayAdapter<QuestionThread> {

	private ArrayList<String> ids;
	private LocalStorageHandler lsh;
	private ArrayList<QuestionThread> threads;
	private Context context;
	private int resId;
	private boolean isMain;
	
	
	/**
	 * The constructor takes the context of the activity using the adapter, the
	 * resource id of the custom row layout and the QuestionThread objects to be displayed
	 * in the form of an array list.
	 * 
	 * @param context The context of the activity where the adapter is being used
	 * @param layoutResourceId The resource id for the custom row layout that is to be used
	 * @param threads The list of question threads whose summary is to be displayed
	 * @param isMain A boolean to identify if the adapter is being used in the main activity or not
	 */
	public ThreadListAdapter(Context context, int layoutResourceId, ArrayList<QuestionThread> threads, boolean isMain) {
		super(context, layoutResourceId, threads);
		this.threads = threads;
		this.context = context;
		this.isMain=isMain;
		this.resId=layoutResourceId;
		this.lsh=new LocalStorageHandler();
		this.ids=lsh.getIds(context, Constants.LATER_IDS_FILENAME);
	}
	
	/**
	 * Returns a View that displays a row in the list of questions' summary
	 * 
	 * It inflates a custom layout that consists of various text views to display the summary
	 * of a question. It grabs all the information (text, author, location,
	 * number of points, and the number of answers) from the QuestionThread object
	 * at the index in the threads ArrayList that the current position in the list corresponds to.
	 * 
	 * This method also implements onClickListeners for the button that is used to save questions
	 * for later.
	 * 
	 * @param position The index of the row in the list being displayed
	 * @param convertView View used in the previous row
	 * @param parent The parent that this view will be attached to
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(this.resId, parent, false);
		}
		final ListView list=(ListView)parent.findViewById(R.id.MainListView);
		TextView question = (TextView) convertView.findViewById(R.id.questionMain);
		TextView author = (TextView) convertView.findViewById(R.id.authorMain);
		TextView location = (TextView) convertView.findViewById(R.id.locMain);
		TextView points = (TextView) convertView.findViewById(R.id.numOfPoints);
		TextView answers = (TextView) convertView.findViewById(R.id.numOfAnswers);
		CheckBox later = (CheckBox) convertView.findViewById(R.id.saveQuestion);
		
		final QuestionThread thread = threads.get(position);
		
		if (isMain) {
			later.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ids=lsh.getIds(context, Constants.LATER_IDS_FILENAME);
					if (ids.contains(thread.getId())) {
						list.performItemClick(null, position, Constants.REMOVE_READ_LATER);
					}
					else {
						list.performItemClick(null, position, Constants.ADD_READ_LATER);
					}
					ids=lsh.getIds(context, Constants.LATER_IDS_FILENAME);
				}
			});
			if (this.ids.contains(thread.getId())) {
				later.setChecked(true);
			}
			else {
				later.setChecked(false);
			}
		}
		
		question.setText(thread.getQuestion().getText());
		author.setText(" - " + thread.getQuestion().getAuthor().getName());
		location.setText("Posted from: " + thread.getQuestion().getCity());
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
