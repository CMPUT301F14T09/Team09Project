package com.team09.qanda.test;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.test.InstrumentationTestCase;

public class GPSTest extends InstrumentationTestCase {

	private Context context;

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		context = getInstrumentation().getContext();
	}
	
	//Use case #24-27: test Location manager
	public void testGPS() {
	    LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

	    lm.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
	    lm.setTestProviderStatus(LocationManager.GPS_PROVIDER, 
	                             LocationProvider.AVAILABLE,
	                             null, System.currentTimeMillis());

	    Location location = new Location(LocationManager.GPS_PROVIDER);
	    location.setLatitude(1.0);
	    location.setLongitude(2.0);
	    location.setTime(System.currentTimeMillis());
	    lm.setTestProviderLocation(LocationManager.GPS_PROVIDER, location);

	    try {
	        Thread.sleep(2000);
	        assertTrue(location.getLatitude() == 1.0 && location.getLongitude() == 2.0);      
	    } catch(InterruptedException e) {
	    }
	}
	
}

