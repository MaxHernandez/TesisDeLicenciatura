package com.maxkalavera.utils;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.searchbar.SearchBar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

public class ActionBarHandler {
	private Activity activity;
	private SlideMenuBarHandler slideMenu;
	
	public ActionBarHandler(Activity activity) {
		this.activity = activity;
		this.slideMenu = new SlideMenuBarHandler(this.activity);
		
		ActionBar actionBar = this.activity.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true); 		
	}
	
	public Boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
    		this.slideMenu.showMenu();
    		return true;
		case R.id.slidemenu_searchbutton: 
			Intent intent = new Intent();
			intent.setClass(this.activity, SearchBar.class);
			this.activity.startActivity(intent);
			return true;
		default:
		    return null;		
		}
	}
	
}
