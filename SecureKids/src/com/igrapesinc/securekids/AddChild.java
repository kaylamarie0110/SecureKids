package com.igrapesinc.securekids;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.igrapesinc.securekids.Register.CreateNewUser;

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

public class AddChild extends Activity {
	
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
	
	EditText inputFirstName;
	EditText inputLastName;
	EditText inputPhone;
	EditText inputEmail;
	
	TextView addChildErrorMsg;
	
	Button finished;
	Button add_another;
	
	Integer add = 0;	//Flag to add another child or not

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_child);
		
		inputFirstName = (EditText) findViewById(R.id.edFirstName);
		inputLastName = (EditText) findViewById(R.id.edLastName);
		inputPhone = (EditText) findViewById(R.id.edPhone);
		inputEmail = (EditText) findViewById(R.id.edEmail);
		
		addChildErrorMsg = (TextView) findViewById(R.id.txtErrorMsg);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    addChildErrorMsg.setText(extras.getString("error"));
		}
		
		finished = (Button) findViewById(R.id.btnAddChild);
		finished.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//Creating new user in background thread
				new CreateNewUser().execute();
				
			}
		});
		
		add_another = (Button) findViewById(R.id.btnAddChildPlus);
		add_another.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				add = 1;
				
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

			pDialog = new ProgressDialog(AddChild.this);
			pDialog.setMessage("Adding Child...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			
		}
		
		//Creating child
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			
			String firstname = inputFirstName.getText().toString();
			String lastname = inputLastName.getText().toString();
			String phone = inputPhone.getText().toString();
			String email = inputEmail.getText().toString();
			
			//Get parent id from SQLite db
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			HashMap<String, String> user = db.getUserDetails();
			String user_id = (String) user.get("id");
			
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.addChild(firstname, lastname, phone, email, user_id);
			
			//Check LogCat for response
			//Log.d("Secure Kids", json.toString());
			
			//Check for add child response
			try {
				
				if (json.getString(KEY_SUCCESS) != null) {
					
					String res = json.getString(KEY_SUCCESS);
					
					if (Integer.parseInt(res) == 1) {
						
						//User successfully registered
						Log.d("Secure Kids", "Successfully added child");
						
						//Store user details in SQLite Database
//						DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//						JSONObject json_user = json.getJSONObject("child_user");
						
						//Clear all previous data in database
//						userFunction.logoutUser(getApplicationContext());
//						db.addUser(json_user.getString(KEY_FIRST_NAME), json_user.getString(KEY_LAST_NAME), json_user.getString(KEY_PHONE), 
//									json_user.getString(KEY_EMAIL), json_user.getString(KEY_CREATED_AT));
						
						if (add == 1) {
							
							//Add another child
							
							//Launch Add Child activity
							Intent add_child = new Intent("com.igrapesinc.securekids.ADD_CHILD");
							
							//Close all views before launching Add Child
							add_child.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(add_child);
							
						} else {
							
							//Go to main page
							
							//Launch Report activity
							Intent report = new Intent("com.igrapesinc.securekids.REPORT");
							
							//Close all views before launching Add Child
							report.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(report);
							
						}
						
						//Close registration screen
						finish();
						
					} else {
						
						//Error adding child
						
						Intent add_child = new Intent("com.igrapesinc.securekids.ADD_CHILD");
						
						res = json.getString(KEY_ERROR);
						
						if (Integer.parseInt(res) == 1) {
							add_child.putExtra("error", "We're sorry!  An error occurred while adding new child.  Please try again.");
						} else if (Integer.parseInt(res) == 2) {
							add_child.putExtra("error", "A child already exists with these credentials.");
						} else {
							add_child.putExtra("error", "Fill in all required fields.");
						}
						//addChildErrorMsg.setText("We're sorry!  An error occurred during registration.  Please try again.");
						startActivity(add_child);
						
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
