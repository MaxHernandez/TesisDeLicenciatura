package com.maxkalavera.ecoar.searchcamera;

import android.app.Activity;
import android.os.Bundle;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.utils.SlideMenuBarHandler;

public class SearchCamera extends Activity{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.searchbar);
		
		
		
		SlideMenuBarHandler slideMenu = new SlideMenuBarHandler(this);
		
	}
}