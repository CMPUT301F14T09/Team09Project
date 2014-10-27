package com.team09.qanda.test;



import com.team09.qanda.MainActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.widget.ArrayAdapter;
import android.widget.EditText;


public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>
{
	
	Instrumentation instrumentation;
	Activity activity;

	public MainActivityTest()
	{

		super(MainActivity.class);
		// TODO Auto-generated constructor stub
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		instrumentation = getInstrumentation();
		activity = getActivity();
	}
	
	@UiThreadTest
	public void testListQuestions() {
		MainActivity ma = getActivity();
		int oldLength = ma.getAdapter().getCount();
		
		//makeTweet("Hello");
		//ArrayAdapter<NormalTweetModel> aa = lts.getAdapter();
		assertEquals(2, oldLength);
		
		ViewAsserts.assertOnScreen(ma.getWindow().getDecorView(), ma.findViewById(com.team09.qanda.R.id.MainListView));
		
		//assertTrue(aa.getItem(aa.getCount()-1) instanceof NormalTweetModel);
		
		//NormalTweetModel ntm = aa.getItem(aa.getCount()-1);
		//assertEquals(ntm.getText(), "Hello");
		
	}

}
