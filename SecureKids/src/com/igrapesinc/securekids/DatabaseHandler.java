package com.igrapesinc.securekids;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	// All Static variables
	
    // Database Version
    private static final int DATABASE_VERSION = 1;
    
    // Database Name
    private static final String DATABASE_NAME = "android_api";
    
    // User table name
    private static final String TABLE_LOGIN = "user";
    
    // User Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CREATED_AT = "created_at";
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "(" 
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_FIRST_NAME + " TEXT,"
				+ KEY_LAST_NAME + " TEXT,"
				+ KEY_PHONE + " TEXT UNIQUE,"
				+ KEY_EMAIL + " TEXT UNIQUE,"
				+ KEY_CREATED_AT + " TEXT" + ")";
		db.execSQL(CREATE_LOGIN_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		//Drop older table if it exists
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
		
		//Create table
		onCreate(db);
	}
    
    /**
     * Storing user details in database
     */
	public void addUser(int id, String first_name, String last_name, String phone, String email, String created_at) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_ID, id);
		values.put(KEY_FIRST_NAME, first_name);
		values.put(KEY_LAST_NAME, last_name);
		values.put(KEY_PHONE, phone);
		values.put(KEY_EMAIL, email);
		values.put(KEY_CREATED_AT, created_at);
		
		//Inserting Row
		db.insert(TABLE_LOGIN, null, values);
		
		//Closing db connection
		db.close();
		
	}
	
	/**
	 * Getting user data from database
	 */
	public HashMap<String, String> getUserDetails() {
		
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT * FROM " + TABLE_LOGIN;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		//Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("id", cursor.getString(0));
			user.put("first_name", cursor.getString(1));
			user.put("last_name", cursor.getString(2));
			user.put("phone", cursor.getString(3));
			user.put("email", cursor.getString(4));
			user.put("created_at", cursor.getString(5));
		}
		
		cursor.close();
		db.close();
		
		//Return user
		return user;
		
	}
	
	/**
	 * Getting user login status
	 * Return true if rows are in table
	 */
	public int getRowCount() {
		
		String countQuery = "SELECT * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		
		db.close();
		cursor.close();
		
		//Return row count
		return rowCount;
		
	}
	
	/**
	 * Recreate database
	 * Delete all tables and create them again
	 */
	public void resetTables() {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		//Delete all rows
		db.delete(TABLE_LOGIN, null, null);
		db.close();
		
	}
	
	
}
