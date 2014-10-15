package com.maxkalavera.ecoar.productinfo;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.utils.ActionBarHandler;
import com.maxkalavera.utils.SlideMenuBarHandler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public class ProductInfo extends Activity{
	private ActionBarHandler actionBarHandler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.productinfo);
		
		this.actionBarHandler = new ActionBarHandler(this);	
	}	
}