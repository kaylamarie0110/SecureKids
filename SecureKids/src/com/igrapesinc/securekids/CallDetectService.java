package com.igrapesinc.securekids;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

//Needed if Activity loses focus, calls will still be detected
public class CallDetectService extends Service {

	private CallHelper callHelper;
	
	public CallDetectService() {
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		callHelper = new CallHelper(this);
		int res = super.onStartCommand(intent, flags, startId);
		callHelper.start();
		return res;
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		super.onDestroy();
		callHelper.stop();
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
