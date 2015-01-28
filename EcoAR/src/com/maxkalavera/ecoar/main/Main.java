package com.maxkalavera.ecoar.main;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.id;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.ecoar.home.Home;
import com.maxkalavera.ecoar.login.Login;
import com.maxkalavera.ecoar.login.LoginFragmentLoginLoader;
import com.maxkalavera.ecoar.login.LoginIntro;
import com.maxkalavera.utils.UserSession;
import com.maxkalavera.utils.HTTPR.InternetStatus;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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


public class Main extends BaseActivity implements LoaderCallbacks<Response>{
	UserSession userSession;
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);	
		
		this.userSession = new UserSession(this);		
		getSupportLoaderManager().initLoader(1, null, this);
		
		this.initCoockieSyncManager();
		
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowTitleEnabled(false); 
		actionBar.setDisplayShowHomeEnabled(false);
		
	}

	/************************************************************
	 * Loading HTTP requests Methods
	 ************************************************************/
	@Override
	public Loader<Response> onCreateLoader(int loaderID, Bundle args) {
		InternetStatus internetStatus = new InternetStatus(this);
		if (!internetStatus.isOnline()){
			// Aqui hay que imprimir un aviso en la pantalla de que es necesaria conexion a internet
			return null;
		}
		
		switch (loaderID) {
			case 0:
				return null;
			case 1:
				MainCheckSessionLoader loader = new MainCheckSessionLoader(this, args);
				loader.forceLoad();
				return loader;
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Response> arg0, Response response) {
		if (response != null) {
			this.userSession.setSessionStatus(true);
			Intent intent = new Intent();
			intent.setClass(this, Home.class);
			startActivity(intent);
		}else{
			this.userSession.setSessionStatus(false);
			Intent intent = new Intent();
			intent.setClass(this, LoginIntro.class);
			startActivity(intent);		
		}
	}

	@Override
	public void onLoaderReset(Loader<Response> arg0) {		
	}
}