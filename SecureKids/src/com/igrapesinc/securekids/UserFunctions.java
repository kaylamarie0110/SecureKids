package com.igrapesinc.securekids;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class UserFunctions {
	
	private JSONParser jsonParser;
	
	private static String parentLoginURL = "http://192.168.0.12/android_connect/verify_parent_user.php";
	private static String childLoginURL = "http://192.168.0.12/android_connect/verify_child_user.php";
	private static String registerURL = "http://192.168.0.12/android_connect/create_parent_user.php";
	private static String addChildURL = "http://192.168.0.12/android_connect/create_child_user.php";
	private static String addLocationURL = "http://192.168.0.12/android_connect/add_location.php";
	private static String addCallURL = "http://192.168.0.12/android_connect/add_call.php";
	private static String addMessageURL = "http://192.168.0.12/android_connect/add_message.php";

//	private static String parent_login_tag = "parent_login";
//	private static String register_tag = "register";
//	private static String add_child_tag = "add_child";
	
	//Constructor
	public UserFunctions() {
		jsonParser = new JSONParser();
	}
	
	/**
	 * Function to make login request for parent user
	 */
	public JSONObject loginParent(String phone, String password) {
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		//params.add(new BasicNameValuePair("tag", parent_login_tag));
		params.add(new BasicNameValuePair("parent_phone", phone));
		params.add(new BasicNameValuePair("parent_password", password));
		
		JSONObject json = jsonParser.makeHttpRequest(parentLoginURL, "POST", params);
		
		return json;
		
	}
	
	/**
	 * Function to make login request for child user
	 */
	public JSONObject loginChild(String phone, String pin) {
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		//params.add(new BasicNameValuePair("tag", parent_login_tag));
		params.add(new BasicNameValuePair("child_phone", phone));
		params.add(new BasicNameValuePair("child_pin", pin));
		
		JSONObject json = jsonParser.makeHttpRequest(childLoginURL, "POST", params);
		
		return json;
		
	}
	
	/**
	 * Function to register user
	 */
	public JSONObject registerUser(String firstname, String lastname, String phone, String email, String pin, String password) {
		
		//Building parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		//params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("parent_first_name", firstname));
		params.add(new BasicNameValuePair("parent_last_name", lastname));
		params.add(new BasicNameValuePair("parent_phone", phone));
		params.add(new BasicNameValuePair("parent_email", email));
		params.add(new BasicNameValuePair("family_pin", pin));
		params.add(new BasicNameValuePair("parent_password", password));
		
		//Log.d("Secure Kids", "Set params");
		
		//Getting JSON object
		//Note that create parent user url accepts POST method
		JSONObject json = jsonParser.makeHttpRequest(registerURL, "POST", params);
		//JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		
		//Log.d("Secure Kids", "Created json object");
		
		return json;
		
	}
	
	/**
	 * Function to add child
	 */
	public JSONObject addChild(String firstname, String lastname, String phone, String email, String parent_id) {
		
		//Building parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		//params.add(new BasicNameValuePair("tag", add_child_tag));
		params.add(new BasicNameValuePair("child_first_name", firstname));
		params.add(new BasicNameValuePair("child_last_name", lastname));
		params.add(new BasicNameValuePair("child_phone", phone));
		params.add(new BasicNameValuePair("child_email", email));
		params.add(new BasicNameValuePair("parent_id", parent_id));
		
		//Log.d("Secure Kids", "Set params");
		
		//Getting JSON object
		//Note that create parent user url accepts POST method
		JSONObject json = jsonParser.makeHttpRequest(addChildURL, "POST", params);
		//JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		
		//Log.d("Secure Kids", "Created json object");
		
		return json;
		
	}
	
	/**
	 * Function to logout user
	 * Reset the database
	 */
	public boolean logoutUser(Context context) {
		
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		
		return true;
		
	}
	
	/**
	 * Function to add location
	 */
	public JSONObject addLocation(String latitude, String longitude, String child_id) {
		
		//Building parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("latitude", latitude));
		params.add(new BasicNameValuePair("longitude", longitude));
		params.add(new BasicNameValuePair("child_id", child_id));
		
		//Note that create parent user url accepts POST method
		JSONObject json = jsonParser.makeHttpRequest(addLocationURL, "POST", params);
		
		return json;
		
	}
	
	/**
	 * Function to add phone call
	 */
	public JSONObject addPhoneCall(String phone_number, String start_time, String duration, String type, String child_id) {
		
		//Building parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phone_number", phone_number));
		params.add(new BasicNameValuePair("start_time", start_time));
		params.add(new BasicNameValuePair("duration", duration));
		params.add(new BasicNameValuePair("type", type));
		params.add(new BasicNameValuePair("child_id", child_id));
		
		//Note that create parent user url accepts POST method
		JSONObject json = jsonParser.makeHttpRequest(addCallURL, "POST", params);
		
		return json;
		
	}
	
	/**
	 * Function to add phone call
	 */
	public JSONObject addSMSMessage(String phone_number, String timestamp, String message, String type, String child_id) {
		
		//Building parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phone_number", phone_number));
		params.add(new BasicNameValuePair("timestamp", timestamp));
		params.add(new BasicNameValuePair("message", message));
		params.add(new BasicNameValuePair("type", type));
		params.add(new BasicNameValuePair("child_id", child_id));
		
		//Note that create parent user url accepts POST method
		JSONObject json = jsonParser.makeHttpRequest(addMessageURL, "POST", params);
		
		return json;
		
	}
}
