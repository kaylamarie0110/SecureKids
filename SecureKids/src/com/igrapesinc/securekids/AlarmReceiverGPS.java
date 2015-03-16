package com.igrapesinc.securekids;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.igrapesinc.securekids.Register.CreateNewUser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiverGPS extends BroadcastReceiver {
	
	String longitude;
	String latitude;
	String user_id;
	
	//JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		//new AddLocation().execute();
		
		GPSTracker gps = new GPSTracker(context);
		
		if (gps.canGetLocation()) {

			//Can get location
			latitude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());
            
            
            DatabaseHandler db = new DatabaseHandler(context);
			HashMap<String, String> user = db.getUserDetails();
			user_id = (String) user.get("id");
			
            
            new AddLocation().execute();
            
		}
	}
	
	//Background Async Task to add location
	class AddLocation extends AsyncTask<String, String, String>  {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			Log.d("Secure Kids", "latitude: " + latitude + " longitude: " + longitude);
			Log.d("Secure Kids", "user_id: " + user_id);
			
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.addLocation(latitude, longitude, user_id);
			
			//Check for location response
			try {
				
				if (json.getString(KEY_SUCCESS) != null) {
					
					//registerErrorMsg.setText("");
					String res = json.getString(KEY_SUCCESS);
					
					if (Integer.parseInt(res) == 1) {
						
						Log.d("Secure Kids", "Location successfully added");
						
					} else {
						
						Log.d("Secure Kids", "Error while adding location");
						
					}
				}
			} catch (JSONException e){
				e.printStackTrace();
			}
			
			return null;
		}
		
		
		
	}
	

}
