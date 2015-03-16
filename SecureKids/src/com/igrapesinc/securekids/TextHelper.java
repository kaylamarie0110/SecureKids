package com.igrapesinc.securekids;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.json.JSONObject;

import com.igrapesinc.securekids.CallHelper.AddPhoneCall;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;


public class TextHelper {
	
	private Context context;
	
	private IncomingReceiver incomingReceiver;
	
	String message;
	String phoneNumber;
	long timestamp;
	
	Date date;
	String dateString;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yy HH:mm:ss");
	
	String messageType;
	
	String user_id;
	
	public TextHelper(Context context) {
		this.context = context;
		
//		callStateListener = new CallStateListener();
		incomingReceiver = new IncomingReceiver();
		
		Log.d("Secure Kids", "Text Helper initialized");
	}
	
	//Start call detection
	public void start() {
		
//		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//		tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
//		
		
//		IntentFilter intentFilter = new IntentFilter(Intent.);
//		context.registerReceiver(incomingReceiver, intentFilter);
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		context.registerReceiver(incomingReceiver, intentFilter);		
		
		ContentObserver observer=new OutgoingHandler(new Handler(), context);
		context.getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, observer);
		
	}
	
	//Stop call detection
	public void stop() {
		
//		tm.listen(callStateListener, PhoneStateListener.LISTEN_NONE);
		
	}
	
	//Broadcast receiver to detect received sms
	public class IncomingReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			Bundle bundle = intent.getExtras();
			
			Object[] pdus = (Object[]) bundle.get("pdus");
			
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[0]);
			
			message = sms.getMessageBody();
			phoneNumber = sms.getOriginatingAddress();
			timestamp = sms.getTimestampMillis();
			
			date = new Date(timestamp);
			dateString = dateFormat.format(date);
			
			messageType = "Incoming";
			
			DatabaseHandler db = new DatabaseHandler(context);
			HashMap<String, String> user = db.getUserDetails();
			user_id = (String) user.get("id");
			
			Log.d("Secure Kids", "Message Received");
			Log.d("Secure Kids", "Message from: " + phoneNumber);
			Log.d("Secure Kids", "Message time: " + dateString);
			Log.d("Secure Kids", "Message Body: " + message);
			
			new AddSMSMessage().execute();
		}
	}
	
	//Content Observer to detect sent sms
	public class OutgoingHandler extends ContentObserver {
		
		private Context mContext;
		
//	    public OutgoingHandler(Context context){
//	    	super(context);
//	        mContext=context;
//	    } 
		
		public OutgoingHandler(Handler handler, Context context) {
	        super(handler);
	        this.mContext = context;
	    }
		
		public void onChange(boolean selfChange) {
			
			Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
			if (cursor.moveToNext()) {
				
				String protocol = cursor.getString(cursor.getColumnIndex("protocol"));
				int type = cursor.getInt(cursor.getColumnIndex("type"));
				
				//Only processing outgoing sms event and only successfully sent (available in inbox)
				if (protocol != null || type != 2) {
					return;
				}
				
				int dateColumn = cursor.getColumnIndex("date");
				int bodyColumn = cursor.getColumnIndex("body");
				int addressColumn = cursor.getColumnIndex("address");
				
				phoneNumber = cursor.getString(addressColumn);
				
				date = new Date(cursor.getLong(dateColumn));
				dateString = dateFormat.format(date);
				message = cursor.getString(bodyColumn); 
				
				messageType = "Outgoing";
				
				DatabaseHandler db = new DatabaseHandler(context);
				HashMap<String, String> user = db.getUserDetails();
				user_id = (String) user.get("id");
				
				Log.d("Secure Kids", "Sent message");
				Log.d("Secure Kids", "Message to: " + phoneNumber);
				Log.d("Secure Kids", "Message time: " + timestamp);
				Log.d("Secure Kids", "Message body: " + message);
				
				new AddSMSMessage().execute();
				
			}
		}
	}

	//Background Async Task to add phone call
	class AddSMSMessage extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.addSMSMessage(phoneNumber, dateString, message, messageType, user_id);
			
			return null;
		}
	}
	
}
