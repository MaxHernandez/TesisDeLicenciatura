package com.maxkalavera.ecoar.main;

import java.net.CookieStore;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.id;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.ecoar.home.Home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.widget.Button;


public class Main extends  FragmentActivity implements OnClickListener{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		
		 CookieManager cookieManager = CookieManager.getInstance();
		
		Button button = (Button) findViewById(R.id.skip_login_button);
		button.setOnClickListener(this);
	}
	
    @Override
    public void onClick(View button) {
        Intent intent = new Intent(Main.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
	
}