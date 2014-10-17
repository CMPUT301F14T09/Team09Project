package com.team09.qanda.test;

import com.team09.qanda.MainActivity;
import com.team09.qanda.Post;
import com.team09.qanda.QuestionThread;
import com.team09.qanda.SortedArrayAdapter;
import com.team09.qanda.ThreadList;
import com.team09.qanda.ThreadListController;
import com.team09.qanda.User;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ArrayAdapter;

public class SortedArrayAdapterTest extends ActivityInstrumentationTestCase2<MainActivity> {
	SortedArrayAdapter srt;
	MainActivity testAct;
	ArrayAdapter<CharSequence> spinner;
	public SortedArrayAdapterTest() {
		super(MainActivity.class);
		testAct=getActivity();
		srt=new SortedArrayAdapter(testAct,android.R.layout.simple_list_item_1);
		spinner=testAct.getSpinnerAdapter();
	}
	public void testSortbyHasPictures(){
		ThreadList questions=new ThreadList();
		srt.clear();
		//get the position of the  "HasPictures" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Has Pictures");

		QuestionThread NoPicture=new QuestionThread(new Post(new User(),"No picture?"));
		// Make ThreadListController
		ThreadListController tlc = new ThreadListController(questions);
		Post pic1=new Post(new User(),"Hello?");
		pic1.setImage();
		Post pic2=new Post(new User(),"Does this work?");
		pic2.setImage();
		// Add threads using controller
		tlc.addThread(new QuestionThread(pic1));
		tlc.addThread(new QuestionThread(pic2));
		tlc.addThread(NoPicture);
		//choose Sorting Option
		testAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(srt.getPosition(NoPicture),0);
	}
	public void testsortByMostRecent(){
		ThreadList questions=new ThreadList();
		srt.clear();
		//get the position of the  "HasPictures" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Most Recent");
		// Make ThreadListController
		ThreadListController tlc = new ThreadListController(questions);
		QuestionThread first=new QuestionThread(new Post(new User(),"am I first?"));
		QuestionThread second=new QuestionThread(new Post(new User(),"am I second?"));
		QuestionThread third=new QuestionThread(new Post(new User(),"am I third?"));
		tlc.addThread(third);
		tlc.addThread(second);
		tlc.addThread(first);
		//choose Sorting Option
		testAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(srt.getPosition(third),2);

	}
	public void testsortByOldest(){
		ThreadList questions=new ThreadList();
		srt.clear();
		//get the position of the  "Oldest" sorting option in the Drop Down List of ActionBar
		int selection=spinner.getPosition("Oldest");
		QuestionThread first=new QuestionThread(new Post(new User(),"am I first?"));
		QuestionThread second=new QuestionThread(new Post(new User(),"am I second?"));
		QuestionThread third=new QuestionThread(new Post(new User(),"am I third?"));
		// Make ThreadListController
		ThreadListController tlc = new ThreadListController(questions);
		tlc.addThread(first);
		tlc.addThread(second);
		tlc.addThread(third);

		//choose Sorting Option
		testAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(srt.getPosition(third),0);

	}
	public void testsortByMostUpVotes(){
		ThreadList questions=new ThreadList();
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
		// Make ThreadListController
		ThreadListController tlc = new ThreadListController(questions);
		tlc.addThread(least);
		tlc.addThread(middle);
		tlc.addThread(most);

		//choose Sorting Option
		testAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(srt.getPosition(most),0);
	}
	public void testsortByLeastUpvotes(){
		ThreadList questions=new ThreadList();
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
		// Make ThreadListController
		ThreadListController tlc = new ThreadListController(questions);
		tlc.addThread(most);
		tlc.addThread(middle);
		tlc.addThread(least);
		srt.sortByMostUpVoted();
		assertEquals(srt.getPosition(least),0);

		//choose Sorting Option
		testAct.getNavigationListener().onNavigationItemSelected(selection,spinner.getItemId(selection));
		assertEquals(srt.getPosition(most),0);
	}
}
