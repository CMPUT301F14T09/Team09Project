package com.team09.qanda.test;

import com.team09.qanda.MainActivity;
import com.team09.qanda.Post;
import com.team09.qanda.QuestionThread;
import com.team09.qanda.SortedArrayAdapter;
import com.team09.qanda.ThreadList;
import com.team09.qanda.User;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ArrayAdapter;

public class sortableArrayAdapterTest extends ActivityInstrumentationTestCase2<MainActivity> {
	ThreadList questions=new ThreadList();
	SortedArrayAdapter srt;
	MainActivity testAct;
	ArrayAdapter<CharSequence> spinner;
	public sortableArrayAdapterTest() {
		super(MainActivity.class);
		testAct=getActivity();
		srt=new SortedArrayAdapter(testAct,android.R.layout.simple_list_item_1);
		spinner=testAct.getSpinnerAdapter();
	}
	public void testSortbyHasPictures(){
		questions.clear();
		srt.clear();
		//get the position of the  "HasPictures" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Has Pictures");
		
		QuestionThread NoPicture=new QuestionThread(new Post(new User(),"No picture?"));
		Post pic1=new Post(new User(),"Hello?");
		pic1.setImage();
		Post pic2=new Post(new User(),"Does this work?");
		pic2.setImage();
		questions.addThread(new QuestionThread(pic1),new QuestionThread(pic2),NoPicture);
		//choose Sorting Option
		testAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(srt.getPosition(NoPicture),0);
	}
	public void testsortByMostRecent(){
		questions.clear();
		srt.clear();
		//get the position of the  "HasPictures" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Most Recent");
		
		QuestionThread first=new QuestionThread(new Post(new User(),"am I first?"));
		QuestionThread second=new QuestionThread(new Post(new User(),"am I second?"));
		QuestionThread third=new QuestionThread(new Post(new User(),"am I third?"));
		questions.addThread(third,second,first);
		//choose Sorting Option
		testAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(srt.getPosition(third),2);
		
	}
	public void testsortByOldest(){
		questions.clear();
		srt.clear();
		//get the position of the  "Oldest" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Oldest");
		QuestionThread first=new QuestionThread(new Post(new User(),"am I first?"));
		QuestionThread second=new QuestionThread(new Post(new User(),"am I second?"));
		QuestionThread third=new QuestionThread(new Post(new User(),"am I third?"));
		questions.addThread(first,second,third);
	    
		//choose Sorting Option
		testAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(srt.getPosition(third),0);
		
	}
	public void testsortByMostUpVotes(){
		questions.clear();
		srt.clear();
		//get the position of the  "Most Upvoted" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Most Upvoted");
		
		Post txt=new Post(new User(),"Do upvotes work?");
		txt.setUps(2);
		Post txt2=new Post(new User(),"Do upvotes work?");
		txt2.setUps(1);
		Post txt3=new Post(new User(),"Do upvotes work?");
		QuestionThread most=new QuestionThread(txt);
		QuestionThread middle=new QuestionThread(txt);
		QuestionThread least=new QuestionThread(txt);
		questions.addThread(least,middle,most);

		//choose Sorting Option
		testAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(srt.getPosition(most),0);
	}
	public void testsortByLeastUpvotes(){
		questions.clear();
		srt.clear();
		//get the position of the  "Least Upvoted" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Least Upvoted");
		
		Post txt=new Post(new User(),"Do upvotes work?");
		txt.setUps(2);
		Post txt2=new Post(new User(),"Do upvotes work?");
		txt2.setUps(1);
		Post txt3=new Post(new User(),"Do upvotes work?");
		QuestionThread most=new QuestionThread(txt);
		QuestionThread middle=new QuestionThread(txt);
		QuestionThread least=new QuestionThread(txt);
		questions.addThread(most,middle,least);
		srt.sortByMostUpVoted();
		assertEquals(srt.getPosition(least),0);
		
		//choose Sorting Option
		testAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(srt.getPosition(most),0);
	}
}
