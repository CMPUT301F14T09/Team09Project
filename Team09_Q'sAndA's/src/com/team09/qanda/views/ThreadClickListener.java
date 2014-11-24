package com.team09.qanda.views;
import com.team09.qanda.Constants;
import com.team09.qanda.LocalStorageHandler;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.ThreadList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;;

public class ThreadClickListener implements OnItemClickListener {
	private LocalStorageHandler lsh;
	private Context context;
	private ThreadList threads;
	
	public ThreadClickListener(LocalStorageHandler lsh,Context ctx,ThreadList threads){
		this.lsh=lsh;
		this.context=ctx;
		this.threads=threads;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (id==Constants.REMOVE_READ_LATER) {
			lsh.deleteQuestionThread(context, threads.get(position), Constants.READ_LATER_FILENAME, Constants.LATER_IDS_FILENAME);
		}
		else if (id==Constants.ADD_READ_LATER) {
			lsh.saveQuestionThread(context, threads.get(position), Constants.READ_LATER_FILENAME, Constants.LATER_IDS_FILENAME);
		}
		
		else {
			QuestionThread selectedThread = (QuestionThread) parent.getItemAtPosition(position);
			displayThread(selectedThread);
		}
		
	}
	private void displayThread(QuestionThread thread) {
		Intent intent = new Intent(context, QuestionThreadActivity.class);
		intent.putExtra("Selected Thread", thread);
		intent.putExtra("main", true);
		context.startActivity(intent);
	}

}
