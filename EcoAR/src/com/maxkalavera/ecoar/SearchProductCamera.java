package com.maxkalavera.ecoar;

import android.app.Activity;
import android.os.Bundle;

import com.maxkalavera.utils.SlideMenuBarHandler;

public class SearchProductCamera extends Activity{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.searchproduct);
		
		
		
		SlideMenuBarHandler slideMenu = new SlideMenuBarHandler(this, "Home");
		
	}
}