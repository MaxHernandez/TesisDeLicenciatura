package com.maxkalavera.ecoar;

import com.maxkalavera.utils.SlideMenuBarHandler;

import android.app.Activity;
import android.os.Bundle;


public class SearchProduct extends Activity{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.searchproduct);
			
		SlideMenuBarHandler slideMenu = new SlideMenuBarHandler(this, "SearchProduct");
		
	}
}