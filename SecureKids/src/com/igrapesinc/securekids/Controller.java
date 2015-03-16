package com.igrapesinc.securekids;

import android.app.Application;

public class Controller extends Application {

	public String callType = "";
	public String phoneNumber = "";
	public long startTime;
	
	public String getCallType() {
		return callType;
	}
	
	public void setCallType(String type) {
		this.callType = type;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public long getStartTime() {
		return startTime;
	}
	
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
}
