package com.igrapesinc.securekids;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

public class CallHelper {
	
	private Context context;
	private TelephonyManager tm;
	private CallStateListener callStateListener;
	
	private OutgoingReceiver outgoingReceiver;
	
	String phoneNumber;
	
	static long startTime = -1;
	static long endTime = -1;
	
	Date startDate;
	String startDateString;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yy HH:mm:ss");
	
	String duration = "";
	String callType = "Outgoing";
	
	String user_id;
	
	public CallHelper(Context context) {
		this.context = context;
		
		callStateListener = new CallStateListener();
		outgoingReceiver = new OutgoingReceiver();
	}
	
	//Start call detection
	public void start() {
		
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		context.registerReceiver(outgoingReceiver, intentFilter);		
		
	}
	
	//Stop call detection
	public void stop() {
		
		tm.listen(callStateListener, PhoneStateListener.LISTEN_NONE);
		
	}
	
	//Listener to detect incoming calls
	private class CallStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				//When idle, i.e no call
				if (startTime != -1) {
					endTime = System.currentTimeMillis();
					
					long totalTime = endTime - startTime;
					
					int seconds = (int) ((totalTime/1000)%60);
					int minutes = (int) ((totalTime/(1000*60))%60);
					int hours = (int) ((totalTime/(1000*60*60))%24);
					
					duration = "";
					
					if (hours > 0)
						duration += hours + ":";
					if (minutes < 10)
						duration += "0" + minutes + ":";
					else
						duration += minutes + ":";
					if (seconds < 10)
						duration += "0" + seconds;
					else
						duration += "" + seconds;
					Log.d("Secure Kids", "Call Duration: " + duration);
	
					Log.d("Secure Kids", "Call Type: " + callType);
					
					Toast.makeText(context, "Duration: " + duration, Toast.LENGTH_LONG).show();
					
					DatabaseHandler db = new DatabaseHandler(context);
					HashMap<String, String> user = db.getUserDetails();
					user_id = (String) user.get("id");
					
					new AddPhoneCall().execute();
					
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				//When off hook, i.e in call
				startTime = System.currentTimeMillis();
				startDate = new Date(startTime);
				startDateString = dateFormat.format(startDate);
				Toast.makeText(context, "Start time: " + startTime, Toast.LENGTH_LONG).show();
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				//When ringing
				phoneNumber = incomingNumber;
				//new AddIncomingCall().execute();
				callType = "Incoming";
				Toast.makeText(context, "Incoming: " + phoneNumber, Toast.LENGTH_LONG).show();
				
				break;
				
			}
		}
	}
	
	//Broadcast receiver to detect outgoing calls
	public class OutgoingReceiver extends BroadcastReceiver {
		
		public OutgoingReceiver() {
			
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			//new AddOutgoingCall().execute();
			
			Toast.makeText(context, "Outgoing: " + phoneNumber, Toast.LENGTH_LONG).show();
			
		}	
	}
	
	//Background Async Task to add phone call
	class AddPhoneCall extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.addPhoneCall(phoneNumber, startDateString, duration, callType, user_id);
			
			return null;
		}
	}
}
