package com.maxkalavera.ecoar;

import com.maxkalavera.ecoar.searchbar.SearchBar;
import com.maxkalavera.utils.SlideMenuBarHandler;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseActivity extends FragmentActivity  {
	private SlideMenuBarHandler slideMenu;
	
	public void onCreate(Bundle savedInstanceState, int contentViewLayout) {
		super.onCreate(savedInstanceState);
		this.setContentView(contentViewLayout);
		
		this.slideMenu = new SlideMenuBarHandler(this);
		
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
    		this.slideMenu.showMenu();
    		return true;
		case R.id.slidemenu_searchbutton: 
			Intent intent = new Intent();
			intent.setClass(this, SearchBar.class);
			this.startActivity(intent);
			return true;
		default:
		    return super.onOptionsItemSelected(item);		
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.mainmenu, menu);
	    return true;
	}
	
};
