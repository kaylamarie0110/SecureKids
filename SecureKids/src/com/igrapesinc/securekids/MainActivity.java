package com.igrapesinc.securekids;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	Button parent_login;
	Button child_login;
	Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        parent_login = (Button) findViewById(R.id.btnParentLogin);
        parent_login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent register = new Intent ("com.igrapesinc.securekids.PARENT_LOGIN");
		        startActivity(register);
				
			}
		});
        
        child_login = (Button) findViewById(R.id.btnChildLogin);
        child_login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent register = new Intent ("com.igrapesinc.securekids.CHILD_LOGIN");
		        startActivity(register);
				
			}
		});
        
        register = (Button) findViewById(R.id.btnRegister);
        register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent register = new Intent ("com.igrapesinc.securekids.REGISTER");
		        startActivity(register);
				
			}
		});
        
    }
}
