package com.igrapesinc.securekids;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

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

public class Register extends Activity {
	
	private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
	
	//JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_ID = "parent_id";
	private static String KEY_FIRST_NAME = "parent_first_name";
	private static String KEY_LAST_NAME = "parent_last_name";
	private static String KEY_PHONE = "parent_phone";
	private static String KEY_EMAIL = "parent_email";
	private static String KEY_PIN = "family_pin";
	private static String KEY_CREATED_AT = "created_at";
	private static String KEY_UPDATED_AT = "updated_at";
	
	EditText inputFirstName;
	EditText inputLastName;
	EditText inputPhone;
	EditText inputEmail;
	EditText inputPin;
	EditText inputPassword;
	
	TextView registerErrorMsg;
	
	Button register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		inputFirstName = (EditText) findViewById(R.id.edFirstName);
		inputLastName = (EditText) findViewById(R.id.edLastName);
		inputPhone = (EditText) findViewById(R.id.edPhone);
		inputEmail = (EditText) findViewById(R.id.edEmail);
		inputPin = (EditText) findViewById(R.id.edPin);
		inputPassword = (EditText) findViewById(R.id.edPassword);
		
		registerErrorMsg = (TextView) findViewById(R.id.txtErrorMsg);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    registerErrorMsg.setText(extras.getString("error"));
		}
		
		register = (Button) findViewById(R.id.btnRegister);
		register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//Creating new user in background thread
				new CreateNewUser().execute();
				
			}
		});
	}
	
	//Background Async Task to create new parent user
	class CreateNewUser extends AsyncTask<String, String, String> {
		
		//Before starting background thread, show Progress Dialog
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			super.onPreExecute();

			pDialog = new ProgressDialog(Register.this);
			pDialog.setMessage("Creating User...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			
		}
		
		//Creating user
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			
			String firstname = inputFirstName.getText().toString();
			String lastname = inputLastName.getText().toString();
			String phone = inputPhone.getText().toString();
			String email = inputEmail.getText().toString();
			String pin = inputPin.getText().toString();
			String password = inputPassword.getText().toString();
			
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.registerUser(firstname, lastname, phone, email, pin, password);
			
			//Check LogCat for response
			//Log.d("Secure Kids", json.toString());
			
			//Check for registration response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					
					//registerErrorMsg.setText("");
					String res = json.getString(KEY_SUCCESS);
					
					if (Integer.parseInt(res) == 1) {
						
						//User successfully registered
						Log.d("Secure Kids", "User successfully registered");
						
						//Store user details in SQLite Database
						DatabaseHandler db = new DatabaseHandler(getApplicationContext());
						JSONObject json_user = json.getJSONObject("parent_user");
						//Log.d("Secure Kids", "Created json_user");
						
						//Clear all previous data in database
						userFunction.logoutUser(getApplicationContext());
						db.addUser(json_user.getInt(KEY_ID), json_user.getString(KEY_FIRST_NAME), json_user.getString(KEY_LAST_NAME), 
									json_user.getString(KEY_PHONE), json_user.getString(KEY_EMAIL), json_user.getString(KEY_CREATED_AT));
						Log.d("Secure Kids", "Added user to SQLite database");
						
						
						//Launch Add Child activity
						Intent add_child = new Intent("com.igrapesinc.securekids.ADD_CHILD");
						Log.d("Secure Kids", "Create Add Child intent");
						
						//Close all views before launching Add Child
						add_child.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(add_child);
						
						//Close registration screen
						finish();
					} else {
						
						//Error in registration
						
						Intent register = new Intent("com.igrapesinc.securekids.REGISTER");
						
						res = json.getString(KEY_ERROR);
						
						if (Integer.parseInt(res) == 1) {
							register.putExtra("error", "We're sorry!  An error occurred during registration.  Please try again.");
						} else if (Integer.parseInt(res) == 2) {
							register.putExtra("error", "A user already exists with these credentials.");
						} else {
							register.putExtra("error", "Fill in all required fields.");
						}
						//registerErrorMsg.setText("We're sorry!  An error occurred during registration.  Please try again.");
						startActivity(register);
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
