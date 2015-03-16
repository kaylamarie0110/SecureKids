package com.igrapesinc.securekids;

import org.json.JSONException;
import org.json.JSONObject;

import com.igrapesinc.securekids.ParentLogin.LoginUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChildLogin extends Activity {
	
private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
	
	//JSON Response node names
		private static String KEY_SUCCESS = "success";
		private static String KEY_ERROR = "error";
		private static String KEY_ERROR_MSG = "error_msg";
		private static String KEY_ID = "child_id";
		private static String KEY_FIRST_NAME = "child_first_name";
		private static String KEY_LAST_NAME = "child_last_name";
		private static String KEY_PHONE = "child_phone";
		private static String KEY_EMAIL = "child_email";
		private static String KEY_CREATED_AT = "created_at";
		private static String KEY_UPDATED_AT = "updated_at";
	
	EditText inputPhone;
	EditText inputPin;
	
	TextView loginErrorMsg;
	
	Button login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.child_login);
		
		inputPhone = (EditText) findViewById(R.id.edPhone);
		inputPin = (EditText) findViewById(R.id.edPin);
		
		loginErrorMsg = (TextView) findViewById(R.id.txtErrorMsg);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    loginErrorMsg.setText(extras.getString("error"));
		}
		
		login = (Button) findViewById(R.id.btnLogin);
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//Creating new user in background thread
				new LoginUser().execute();
				
			}
		});
	}

	//Background Async Task to create new parent user
		class LoginUser extends AsyncTask<String, String, String> {
			
			//Before starting background thread, show Progress Dialog
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				
				super.onPreExecute();

				pDialog = new ProgressDialog(ChildLogin.this);
				pDialog.setMessage("Logging in...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				
			}
			
			//Logging in user
			@Override
			protected String doInBackground(String... args) {
				
				String phone = inputPhone.getText().toString();
				String pin = inputPin.getText().toString();
				
				UserFunctions userFunction = new UserFunctions();
				JSONObject json = userFunction.loginChild(phone, pin);
				
				//Check for child login response
				try {
					
					if (json.getString(KEY_SUCCESS) != null) {
						
						//registerErrorMsg.setText("");
						String res = json.getString(KEY_SUCCESS);
						
						if (Integer.parseInt(res) == 1) {
							
							//User successfully logged in
							Log.d("Secure Kids", "User successfully logged in");
						
							//Store user details in SQLite Database
							DatabaseHandler db = new DatabaseHandler(getApplicationContext());
							JSONObject json_user = json.getJSONObject("child_user");
							
							//Clear all previous data in database
							userFunction.logoutUser(getApplicationContext());
							db.addUser(json_user.getInt(KEY_ID), json_user.getString(KEY_FIRST_NAME), json_user.getString(KEY_LAST_NAME), 
										json_user.getString(KEY_PHONE), json_user.getString(KEY_EMAIL), json_user.getString(KEY_CREATED_AT));
							Log.d("Secure Kids", "Added user to SQLite database");
							
							//Launch Report activity
							Intent child_main = new Intent("com.igrapesinc.securekids.CHILD_MAIN");
							
							//Close all views before launching Add Child
							child_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(child_main);
							
							//Close registration screen
							finish();
							
						} else {
							
							//Error logging in
							
							Intent child_login = new Intent("com.igrapesinc.securekids.CHILD_LOGIN");
							
							res = json.getString(KEY_ERROR);
							
							if (Integer.parseInt(res) == 1) {
								child_login.putExtra("error", "Incorrect phone number or pin.");
							} else {
								child_login.putExtra("error", "Fill in all required fields.");
							}
							//registerErrorMsg.setText("We're sorry!  An error occurred during registration.  Please try again.");
							startActivity(child_login);
							
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				return null;
				
			}
			
			//After completing background, dismiss the progress dialog
			protected void onPostExecute(String file_url) {
				// TODO Auto-generated method stub
				
				//Dismiss the dialog
				pDialog.dismiss();
				
			}
			
		}
	
}
