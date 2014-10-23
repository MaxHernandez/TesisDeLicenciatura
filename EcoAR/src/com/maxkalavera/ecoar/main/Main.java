package com.maxkalavera.ecoar.main;

import java.net.CookieStore;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.id;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.ecoar.home.Home;
import com.maxkalavera.ecoar.login.Login;
import com.maxkalavera.ecoar.login.LoginFragmentLoginLoader;
import com.maxkalavera.ecoar.login.LoginIntro;
import com.maxkalavera.utils.UserSession;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;


public class Main extends FragmentActivity implements LoaderCallbacks<Boolean>{
	UserSession userSession;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);	
		this.userSession = new UserSession(this);
		
		CookieSyncManager.createInstance(this);
		getSupportLoaderManager().initLoader(1, null, this);
		
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowTitleEnabled(false); 
		actionBar.setDisplayShowHomeEnabled(false);
		
	}
	
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

	@Override
	public Loader<Boolean> onCreateLoader(int loaderID, Bundle args) {
		switch (loaderID) {
			case 0:
				return null;
			case 1:
				MainCheckSessionLoader loader = new MainCheckSessionLoader(this, this.userSession);
				loader.forceLoad();
				return loader;
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Boolean> arg0, Boolean data) {
		Log.i("ecoar-CheckSession-Loader", String.valueOf(data));
		if (data != null) {
			if (this.userSession.checkSessionStatus()){
				Intent intent = new Intent();
				intent.setClass(this, Home.class);
				startActivity(intent);
			}else{
				Intent intent = new Intent();
				intent.setClass(this, Login.class);
				startActivity(intent);			
			}
		}else{
			/*
			 * Esta parte logica dentro del else solo esta para debugear
			 */
			Intent intent = new Intent();
			intent.setClass(this, Home.class);
			startActivity(intent);		
		}
	}

	@Override
	public void onLoaderReset(Loader<Boolean> arg0) {		
	}
}