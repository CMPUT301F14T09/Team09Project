package com.team09.qanda.test;

import com.team09.qanda.MainActivity;
import com.team09.qanda.Post;
import com.team09.qanda.QuestionThread;
import com.team09.qanda.SortedArrayAdapter;
import com.team09.qanda.ThreadList;
import com.team09.qanda.User;

import android.test.ActivityInstrumentationTestCase2;

public class sortableArrayAdapterTest extends ActivityInstrumentationTestCase2<MainActivity> {
	ThreadList questions=new ThreadList();
	SortedArrayAdapter srt=new SortedArrayAdapter(getActivity(),android.R.layout.activity_list_item);
	public sortableArrayAdapterTest() {
		super(MainActivity.class);
	}
	public void testSortbyHasPictures(){
		questions.clear();
		QuestionThread NoPicture=new QuestionThread(new Post(new User(),"No picture?"));
		Post pic1=new Post(new User(),"Hello?");
		pic1.setImage();
		Post pic2=new Post(new User(),"Does this work?");
		pic2.setImage();
		questions.addThread(new QuestionThread(pic1),new QuestionThread(pic2),NoPicture);
		assertEquals(srt.getPosition(NoPicture),0);
	}
	public void testsortByMostRecent(){
		questions.clear();
		QuestionThread first=new QuestionThread(new Post(new User(),"am I first?"));
		QuestionThread second=new QuestionThread(new Post(new User(),"am I second?"));
		QuestionThread third=new QuestionThread(new Post(new User(),"am I third?"));
		questions.addThread(third,second,first);
		srt.sortByMostRecent(questions);
		assertEquals(srt.getPosition(third),2);
		
	}
	public void testsortByOldest(){
		questions.clear();
		QuestionThread first=new QuestionThread(new Post(new User(),"am I first?"));
		QuestionThread second=new QuestionThread(new Post(new User(),"am I second?"));
		QuestionThread third=new QuestionThread(new Post(new User(),"am I third?"));
		questions.addThread(first,second,third);
		srt.sortByMostRecent(questions);
		assertEquals(srt.getPosition(third),0);
		
	}
	public void testsortByMostUpVotes(){
		questions.clear();
		Post txt=new Post(new User(),"Do upvotes work?");
		txt.setUps(2);
		Post txt2=new Post(new User(),"Do upvotes work?");
		txt2.setUps(1);
		Post txt3=new Post(new User(),"Do upvotes work?");
		QuestionThread most=new QuestionThread(txt);
		QuestionThread middle=new QuestionThread(txt);
		QuestionThread least=new QuestionThread(txt);
		questions.addThread(least,middle,most);
		srt.sortByMostUpVoted();
		assertEquals(srt.getPosition(most),0);
	}
	public void testsortByLeastUpvotes(){
		questions.clear();
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
	}
}
