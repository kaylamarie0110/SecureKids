package com.igrapesinc.securekids;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {

	private final Context mContext;
	
	//Flag for GPS status
	boolean isGPSEnabled = false;
	
	//Flag for network status
	boolean isNetworkEnabled = false;
	
	boolean canGetLocation = false;
	
	Location location;	
	double latitude;	
	double longitude;
	
	//Minimum distance to change updates, in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;	//10 meters
	//Minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;	//1 minutes
	
	//Declaring a location manager
	protected LocationManager locationManager;
	
	public GPSTracker(Context context) {
		this.mContext = context;
		getLocation();
	}
	
	public Location getLocation() {
		
		try {
			
			locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
			
			//Getting GPS status
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			
			//Getting network status
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			if (!isGPSEnabled && !isNetworkEnabled) {
				//No network provider is enabled
			} else {
				
				this.canGetLocation = true;
				
				//First get location from Network Provider
				if (isNetworkEnabled) {
					
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Secure Kids", "Network");
					
					if (locationManager != null) {
						
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							
							latitude = location.getLatitude();
							longitude = location.getLongitude();
							
						}
					}
				}
				//If GPS is enabled, get lat/long using GPS Services
				if (isGPSEnabled) {
					
					if (location == null) {
						
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("Secure Kids", "GPS Enabled");
						
						if (locationManager != null) {
							
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							
							if (location != null) {
								
								latitude = location.getLatitude();
								longitude = location.getLongitude();
								
							}	
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return location;
		
	}
	
	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 */
	public void stopUsingGPS() {
		
		if (locationManager != null) {
			locationManager.removeUpdates(GPSTracker.this);
		}
		
	}
	
	/**
	 * Function to get latitude
	 */
	public double getLatitude() {
		
		if (location != null) {
			latitude = location.getLatitude();
		}
		
		return latitude;
		
	}
	
	/**
	 * Function to get longitude
	 */
	public double getLongitude() {
			
		if (location != null) {
			longitude = location.getLongitude();
		}
		
		return longitude;
		
	}
	
	/**
	 * Function to check if GPS/wifi is enabled
	 */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}
	
	/**
	 * Function to show settings alert dialog
	 * Pressing Settings button will launch Settings Options
	 */
	public void showSettingsAlert() {
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
		
		//Setting Dialog title
		alertDialog.setTitle("GPS Settings");
		
		//Setting Dialog message
		alertDialog.setMessage("GPS is not enabled.  Would you like to go to the settings menu to enable GPS?");
		
		//On pressing Settings button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(intent);
				
			}
		});
		
		//On pressing Cancel button
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				dialog.cancel();
				
			}
		});
		
		//Showing alert message
		alertDialog.show();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
