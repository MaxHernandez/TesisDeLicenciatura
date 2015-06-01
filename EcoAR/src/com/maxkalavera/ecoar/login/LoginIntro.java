package com.maxkalavera.ecoar.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieSyncManager;
import android.widget.Button;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.home.Home;
import com.maxkalavera.ecoar.main.Main;
import com.maxkalavera.utils.database.UserSessionDAO;

public class LoginIntro extends BaseActivity implements OnClickListener{
	UserSessionDAO userSession;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.loginintro);
		this.userSession = new UserSessionDAO(this);
		this.setOptionsMenuFlagOff();
		
		if (this.userSession.checkSessionStatus()){
            Intent intent = new Intent();
            intent.setClass(this, Home.class);
            startActivity(intent);
		}
		Button button = (Button) findViewById(R.id.loginintro_skiplogin);
		button.setOnClickListener(this);
	}
	
    @Override
    public void onClick(View button) {
        Intent intent = new Intent(LoginIntro.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
	
}
