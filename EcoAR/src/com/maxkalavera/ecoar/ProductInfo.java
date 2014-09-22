package com.maxkalavera.ecoar;

import com.maxkalavera.utils.SlideMenuBarHandler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public class ProductInfo extends Activity{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.showproductinfo);
		
		SlideMenuBarHandler slideMenu = new SlideMenuBarHandler(this, "ShowProductInfo");	
	}	
}