package com.maxkalavera.ecoar;

import com.maxkalavera.utils.SlideMenuBarHandler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public class Login extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);
		
		SlideMenuBarHandler slideMenu = new SlideMenuBarHandler(this, "Login");
		
	}
}