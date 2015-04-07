package com.maxkalavera.ecoar;

import com.maxkalavera.ecoar.searchbar.SearchBar;
import com.maxkalavera.utils.SlideMenuBarHandler;
import com.maxkalavera.utils.SlideMenuBarHandlerButton;
import com.maxkalavera.utils.UserSession;
import com.maxkalavera.utils.database.UserDataDAO;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.CookieSyncManager;

public class BaseActivity extends FragmentActivity  {
	private SlideMenuBarHandler slideMenu;
	
	public void onCreate(Bundle savedInstanceState, int contentViewLayout) {
		super.onCreate(savedInstanceState);
		this.setContentView(contentViewLayout);
				
		boolean sessionFlag = new UserSession(this).checkSessionStatus();

		// Configuracion del SlideMenuBarHandler
		this.slideMenu = new SlideMenuBarHandler(this);
		if (sessionFlag) {
			SlideMenuBarHandlerButton[] slideMenuButtonsDefault = {
					new SlideMenuBarHandlerButton(
							getResources().getString(R.string.actionbar_login),
							"com.maxkalavera.ecoar.login.Login",
							R.drawable.home),
					new SlideMenuBarHandlerButton(
							getResources().getString(R.string.actionbar_search),
							"com.maxkalavera.ecoar.searchbar.SearchBar",
							R.drawable.search),
					new SlideMenuBarHandlerButton(
							getResources().getString(R.string.actionbar_searchcamera),
							"com.maxkalavera.ecoar.searchcamera.SearchCamera",
							R.drawable.searchcamera),
					new SlideMenuBarHandlerButton(
							getResources().getString(R.string.actionbar_groceries),
							"com.maxkalavera.ecoar.groceries.Groceries",
							R.drawable.groceries),
					new SlideMenuBarHandlerButton(
						getResources().getString(R.string.actionbar_logout),
						"",
						null),
					};
			this.slideMenu.setButtons(slideMenuButtonsDefault);
		}else {
			SlideMenuBarHandlerButton[] slideMenuButtonsDefault = {
					new SlideMenuBarHandlerButton(
							getResources().getString(R.string.actionbar_home),
							"com.maxkalavera.ecoar.login.Login",
							R.drawable.home),
					new SlideMenuBarHandlerButton(
							getResources().getString(R.string.actionbar_search),
							"com.maxkalavera.ecoar.searchbar.SearchBar",
							R.drawable.search),
					new SlideMenuBarHandlerButton(
							getResources().getString(R.string.actionbar_searchcamera),
							"com.maxkalavera.ecoar.searchcamera.SearchCamera",
							R.drawable.searchcamera),
					new SlideMenuBarHandlerButton(
							getResources().getString(R.string.actionbar_groceries),
							"com.maxkalavera.ecoar.groceries.Groceries",
							R.drawable.groceries),
					new SlideMenuBarHandlerButton(
							getResources().getString(R.string.actionbar_logout),
							"",
							R.drawable.logout),
					};
			this.slideMenu.setButtons(slideMenuButtonsDefault);			
		}
		
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}
	
	
	/*************************************************************
	 *  Configuracion de Cookies
	 *************************************************************/
	
	@Override
	public void onResume() {
		super.onResume();
		CookieSyncManager.getInstance().startSync();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		 CookieSyncManager.getInstance().stopSync();	
	}

	
	public void initCoockieSyncManager(){
		CookieSyncManager.createInstance(this);
	}
	
	/*************************************************************
	 * Configuracion del SlideMenu
	 *************************************************************/
	
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
	
	/*************************************************************
	 * Revisión de la sesión de usuario por medio del 
	 * UserDataManager
	 *************************************************************/
	UserDataDAO userDataManager = null;
	
	public UserDataDAO getUserDataManager(){
		if ( this.userDataManager == null )
			this.userDataManager = new UserDataDAO(this);
		return this.userDataManager;
	}
	
};
