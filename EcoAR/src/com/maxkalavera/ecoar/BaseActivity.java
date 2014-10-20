package com.maxkalavera.ecoar;

import java.util.LinkedList;

import com.maxkalavera.ecoar.searchbar.SearchBar;
import com.maxkalavera.utils.SlideMenuBarHandler;
import com.maxkalavera.utils.SlideMenuBarHandlerButton;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseActivity extends FragmentActivity  {
	private SlideMenuBarHandler slideMenu;
	
	public void onCreate(Bundle savedInstanceState, int contentViewLayout) {
		super.onCreate(savedInstanceState);
		this.setContentView(contentViewLayout);
				
		boolean sessionFlag = this.getSharedPreferences("Session_prefs", Context.MODE_PRIVATE).getBoolean("sessionAuthenticated", false);
		this.slideMenu = new SlideMenuBarHandler(this);
		
		if (sessionFlag) {
			SlideMenuBarHandlerButton[] slideMenuButtonsDefault = {
					new SlideMenuBarHandlerButton("Login", "com.maxkalavera.ecoar.login.Login", 0),
					new SlideMenuBarHandlerButton("Buscar", "com.maxkalavera.ecoar.searchbar.SearchBar", 0),
					new SlideMenuBarHandlerButton("Buscar por cámara", "", 0),
					new SlideMenuBarHandlerButton("Lista del super", "", 0),
					};
			this.slideMenu.setButtons(slideMenuButtonsDefault);
		}else {
			SlideMenuBarHandlerButton[] slideMenuButtonsDefault = {
					new SlideMenuBarHandlerButton("Home", "com.maxkalavera.ecoar.home.Home", 0),
					new SlideMenuBarHandlerButton("Buscar", "com.maxkalavera.ecoar.searchbar.SearchBar", 0),
					new SlideMenuBarHandlerButton("Buscar por cámara", "", 0),
					new SlideMenuBarHandlerButton("Lista del super", "", 0),
					};
			this.slideMenu.setButtons(slideMenuButtonsDefault);			
		}
		
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
