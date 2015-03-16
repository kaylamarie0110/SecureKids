package com.igrapesinc.securekids;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ChildMain extends Activity {
	
	private PendingIntent pendingIntent;
	private AlarmManager alarmManager;
	
	//private TelephonyManager telephonyManager;
	
	private boolean detectCallEnabled;
	
	TextView greeting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.child_main);
		
		//Get child name
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		HashMap<String, String> user = db.getUserDetails();
		String name = (String) user.get("first_name");
		
		greeting = (TextView) findViewById(R.id.txtHello);
		greeting.setText("Hello, " + name + "!");
		
		//GET LOCATIONS
		Intent alarmIntent = new Intent(this, AlarmReceiverGPS.class);
		pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
		
		startAlarm();
		
		//GET CALLS
		setCallDetectEnabled(true);
//		Intent incomingCallIntent = new Intent(this, IncomingCallReceiver.class);
//		Intent outgoingCallIntent = new Intent(this, OutgoingCallReceiver.class);
//		Intent callDurationIntent = new Intent(this, CallDurationReceiver.class);
//		PendingIntent incomingCallPendingIntent = PendingIntent.getBroadcast(this, 0, incomingCallIntent, 0);
//		PendingIntent outgoingCallPendingIntent = PendingIntent.getBroadcast(this, 0, outgoingCallIntent, 0);
//		PendingIntent callDurationPendingIntent = PendingIntent.getBroadcast(this, 0, callDurationIntent, 0);
		
		//GET TEXTS
		setTextDetectEnabled(true);
		
	}
	
	public void startAlarm() {
		
		alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	    int interval = 1000 * 60 * 30;

	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
	    //Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
		
	}
	
//	public void listenForCalls() {
//		
//		telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//		
//	}
	
	public void setCallDetectEnabled(boolean enable) {
		
		//detectCallEnabled = enable;
		
		Intent intent = new Intent(this, CallDetectService.class);
		if (enable) {
			
			//Start detect service
			startService(intent);
			
		} else {
			
			//Stop detect service
			stopService(intent);
			
		}
		
	}
	
	public void setTextDetectEnabled(boolean enable) {
		
		//detectEnabled = enable;
		
		Intent intent = new Intent(this, TextDetectService.class);
		if (enable) {
			
			//Start detect service
			startService(intent);
			
			Log.d("Secure Kids", "Started service");
			
		} else {
			
			//Stop detect service
			stopService(intent);
			
		}
		
	}
	
	

}
