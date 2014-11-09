package com.team09.qanda.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.ViewAsserts;

import com.team09.qanda.ThreadAdapter;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.User;
import com.team09.qanda.views.QuestionThreadActivity;

public class QuestionThreadActivityTest extends ActivityInstrumentationTestCase2<QuestionThreadActivity> {
	ThreadAdapter adapter;
	QuestionThreadActivity ta;
	QuestionThread thread;
	private Context context;

	
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		context = getInstrumentation().getContext();
	}

	public QuestionThreadActivityTest() {
		super(QuestionThreadActivity.class);
		
	}
	/*
	@Override
	public void setUp() throws Exception{
		super.setUp();
		//threadAct=getActivity();
		//adapter=threadAct.getAdapter();
				
	}
	
	@Override
	public void tearDown(){
		adapter.clear();
	}
	*/
	
	// Use Case #2 : View a question and its answers
	public void testDisplayQuestion() {
		Post question = new Post(new User(context), "Question 1");
		thread = new QuestionThread(question);
		
		Intent intent = new Intent();
    	intent.putExtra("Selected Thread", thread);
    	
    	setActivityIntent(intent);
		
		ta = getActivity();
		int oldLength = ta.getAdapter().getCount();
		
		assertEquals(2, oldLength);
		
		ViewAsserts.assertOnScreen(ta.getWindow().getDecorView(), ta.findViewById(com.team09.qanda.R.id.ThreadPostsView));
		
	}
}
