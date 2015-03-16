package com.igrapesinc.securekids;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TextDetectService extends Service {
	
	private TextHelper textHelper;
	
	public TextDetectService() {
		
	}
	
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		textHelper = new TextHelper(this);
		//Log.d("Secure Kids", "Initialized textHelper");
		int res = super.onStartCommand(intent, flags, startId);
		textHelper.start();
		//Log.d("Secure Kids", "Started textHelper");
		return res;
		
	}



	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		super.onDestroy();
		textHelper.stop();
		
	}



	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
