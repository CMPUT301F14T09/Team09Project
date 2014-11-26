package com.team09.qanda;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.team09.qanda.models.Post;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//http://www.javacodegeeks.com/2010/09/android-location-based-services.html Nov 22, 2014

public class GPSHandler {
	 	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
	    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
	    
	    private LocationManager locationManager;
	    private Context c;

		public GPSHandler(Context ctx) {
			c = ctx;
			locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
	        
	        locationManager.requestLocationUpdates(
	                LocationManager.GPS_PROVIDER, 
	                MINIMUM_TIME_BETWEEN_UPDATES, 
	                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
	                new MyLocationListener()
	        );
		}
		
		public AlertDialog getLocationPrompt(Post p) {
			final Post post = p;
			LayoutInflater layoutInflater = LayoutInflater.from(c);
			View promptView = layoutInflater.inflate(R.layout.loc_prompt, null);
			
		    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
		
		    // set loc_prompt.xml to be the layout file of the alertdialog builder
		    alertDialogBuilder.setView(promptView);
		    
		    // setup a dialog window
		    alertDialogBuilder
		        .setCancelable(false)
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		                post.setCity(getCity());
		            }
		        })
		        .setNegativeButton("No", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		            	dialog.cancel();
		            }
		        });
		    return alertDialogBuilder.create();
		}
		
		public Location getLocation() {
			return (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
		}
		
		public String getCity() {
			Location location = getLocation();

	        if (location != null) {
	        	Geocoder gcd = new Geocoder(c, Locale.getDefault());
	        	List<Address> addresses;
				try	{
					addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
					if (addresses.size() > 0) 
		                return(addresses.get(0).getLocality());
				} catch (IOException e)	{
					e.printStackTrace();
				}
	        } 
	        return "N/A";
		}
		
		private class MyLocationListener implements LocationListener {
	    	
			//TODO: Following toasts were for testing, remove when everything confirmed to work
	        public void onLocationChanged(Location location) {
	            String message = String.format(
	                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
	                    location.getLongitude(), location.getLatitude()
	            );
	            Toast.makeText(c, message, Toast.LENGTH_LONG).show();
	        }

	        public void onStatusChanged(String s, int i, Bundle b) {
	            Toast.makeText(c, "Provider status changed",
	                    Toast.LENGTH_LONG).show();
	        }

	        public void onProviderDisabled(String s) {
	            Toast.makeText(c,
	                    "Provider disabled by the user. GPS turned off",
	                    Toast.LENGTH_LONG).show();
	        }

	        public void onProviderEnabled(String s) {
	            Toast.makeText(c,
	                    "Provider enabled by the user. GPS turned on",
	                    Toast.LENGTH_LONG).show();
	        }

	    }
}
