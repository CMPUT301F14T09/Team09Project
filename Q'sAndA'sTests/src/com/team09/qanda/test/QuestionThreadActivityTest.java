package com.team09.qanda.test;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.team09.qanda.ThreadAdapter;
import com.team09.qanda.controllers.PostController;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.Reply;
import com.team09.qanda.models.User;
import com.team09.qanda.views.QuestionThreadActivity;

public class QuestionThreadActivityTest extends ActivityInstrumentationTestCase2<QuestionThreadActivity> {
	private ThreadAdapter adapter;
	private QuestionThreadActivity ta;
	private QuestionThread thread;
	private EditText textInput;
	private Context context;

	
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		context = this.getInstrumentation().getTargetContext();
		
		Post question = new Post(new User(context), "Question 1");
		thread = new QuestionThread(question);
		
		Intent intent = new Intent();
    	intent.putExtra("Selected Thread", thread);
    	
    	setActivityIntent(intent);
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
	
	private void makeAnswer(String text) {
		ta = getActivity();
		assertNotNull(ta.findViewById(com.team09.qanda.R.id.answerSubmissionButton));
		textInput = ((EditText) ta.findViewById(com.team09.qanda.R.id.editAnswerText));
		textInput.setText(text);
		((ImageButton) ta.findViewById(com.team09.qanda.R.id.answerSubmissionButton)).performClick();
		DialogFragment dialog = (DialogFragment) ta.getSupportFragmentManager().findFragmentByTag("LocDialogFragment");
		dialog.getDialog().cancel();
	}
	
	
	// Use Case #2 : View a question and its answers
	public void testDisplayQuestion() {
		Post question = new Post(new User(context), "Question 1");
		thread = new QuestionThread(question);
		
		Intent intent = new Intent();
    	intent.putExtra("Selected Thread", thread);
    	
    	setActivityIntent(intent);
		
		ta = getActivity();
		int oldLength = ta.getAdapter().getGroupCount();
		
		assertEquals(2, oldLength);
		
		ViewAsserts.assertOnScreen(ta.getWindow().getDecorView(), ta.findViewById(com.team09.qanda.R.id.postAuthor));
		ViewAsserts.assertOnScreen(ta.getWindow().getDecorView(), ta.findViewById(com.team09.qanda.R.id.post));
		ViewAsserts.assertOnScreen(ta.getWindow().getDecorView(), ta.findViewById(com.team09.qanda.R.id.ThreadPostsView));
		
	}
	
	// Use Case #5: Add an answer
	@UiThreadTest
	public void testAddAnswer() {
		
		QuestionThreadActivity activity = getActivity();
		ThreadAdapter adapter = activity.getAdapter();
		
		int oldLength = adapter.getGroupCount();
		
		makeAnswer("Answer 1");
		
		assertEquals(oldLength+1,adapter.getGroupCount());
		
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(), activity.findViewById(com.team09.qanda.R.id.post));
	}
	
	@UiThreadTest
	// Use Case #3 : View replies
	public void testDisplayReplies() {
		Post question = new Post(new User(context), "Question 1");
		Reply reply = new Reply(new User(context), "reply");
		PostController pc = new PostController(question);
		pc.addReply(reply);
		thread = new QuestionThread(question);

		Intent intent = new Intent();
		intent.putExtra("Selected Thread", thread);

		setActivityIntent(intent);

		ta = getActivity();

		assertEquals(2, ta.getAdapter().getGroupCount());
		assertEquals(1, ta.getAdapter().getChildrenCount(0));

		((CheckBox) ta.findViewById(com.team09.qanda.R.id.repliesButton)).performClick();


		ViewAsserts.assertOnScreen(ta.getWindow().getDecorView(), ta.findViewById(com.team09.qanda.R.id.replyAuthor));
		ViewAsserts.assertOnScreen(ta.getWindow().getDecorView(), ta.findViewById(com.team09.qanda.R.id.reply));
		//ViewAsserts.assertGroupContains((ViewGroup) ta.findViewById(com.team09.qanda.R.id.ThreadPostsView), ta.findViewById(com.team09.qanda.R.id.postText));
	}
	
}
