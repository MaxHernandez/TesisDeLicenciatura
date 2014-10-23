package com.maxkalavera.ecoar.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieSyncManager;
import android.widget.Button;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.home.Home;
import com.maxkalavera.ecoar.main.Main;
import com.maxkalavera.utils.UserSession;

public class LoginIntro extends FragmentActivity implements OnClickListener{
	UserSession userSession;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.userSession = new UserSession(this);
		
		if (this.userSession.checkSessionStatus()){
            Intent intent = new Intent();
            intent.setClass(this, Home.class);
            startActivity(intent);
		}
			
		this.setContentView(R.layout.loginintro);
		
		CookieSyncManager.createInstance(this);
		
		Button button = (Button) findViewById(R.id.skip_login_button);
		button.setOnClickListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		CookieSyncManager.getInstance().startSync();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		 CookieSyncManager.getInstance().stopSync();
		
	}
	
    @Override
    public void onClick(View button) {
        Intent intent = new Intent(LoginIntro.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
	
}
