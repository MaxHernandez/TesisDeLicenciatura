package com.maxkalavera.ecoar.login;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.utils.SlideMenuBarHandler;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;


public class Login extends FragmentActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login_activity);
		
		SlideMenuBarHandler slideMenu = new SlideMenuBarHandler(this, "Login");
		
	}
}