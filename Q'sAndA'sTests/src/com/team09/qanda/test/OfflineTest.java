package com.team09.qanda.test;

import java.util.ArrayList;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.test.ActivityInstrumentationTestCase2;

import com.team09.qanda.LocalStorageHandler;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.ThreadList;
import com.team09.qanda.models.User;
import com.team09.qanda.views.QuestionThreadActivity;

public class OfflineTest extends ActivityInstrumentationTestCase2<QuestionThreadActivity> {
	
	private Context context;
	
	public OfflineTest() {
		super(QuestionThreadActivity.class);
		this.context=getActivity().getApplicationContext();
	}
	
	//Use case 19: Offline viewing
	public void testViewOffline() {
		LocalStorageHandler handler=new LocalStorageHandler();
		Post question=new Post(new User(context), "Offline? No way");
		QuestionThread qt=new QuestionThread(question);
		handler.saveQuestionThread(context, qt, "Favourite.txt");
		handler.saveQuestionThread(context, qt, "Later.txt");
		
		WifiManager wifi=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(false);
		
		ThreadList favs=handler.getThreadList(context, "Favourite.txt");
		ThreadList laters=handler.getThreadList(context, "Later.txt");
		ArrayList<String> favQs=new ArrayList<String>();
		ArrayList<String> laterQs=new ArrayList<String>();
		for (Post q:favs.getQuestions()) {
			favQs.add(q.getText());
		}
		for (Post q:laters.getQuestions()) {
			laterQs.add(q.getText());
		}
		
		assertTrue(favQs.contains("Offline? No way"));
		assertTrue(laterQs.contains("Offline? No way"));
	}
	
	//Use case 20: Author offline
	public void testOfflineAuthor() {
		WifiManager wifi=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(false);
		
		LocalStorageHandler handler=new LocalStorageHandler();
		String userQuestion="I'm writing this offline";
		handler.saveText(context, userQuestion, "Offline.txt");
		String stillQuestion=handler.getText(context, "Offline.txt");
		assertEquals(userQuestion, stillQuestion);
	}
}
