package com.maxkalavera.ecoar.main;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.id;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.ecoar.home.Home;
import com.maxkalavera.ecoar.login.Login;
import com.maxkalavera.ecoar.login.LoginFragmentHTTPLoader;
import com.maxkalavera.ecoar.login.LoginIntro;
import com.maxkalavera.utils.database.CacheProductDAO;
import com.maxkalavera.utils.database.UserSessionDAO;
import com.maxkalavera.utils.httprequest.InternetStatus;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;
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


public class Main extends BaseActivity implements LoaderCallbacks<ResponseBundle>{
	UserSessionDAO userSession;
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);	
		
		this.initCoockieSyncManager();
		
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowTitleEnabled(false); 
		actionBar.setDisplayShowHomeEnabled(false);
		
		this.userSession = new UserSessionDAO(this);		
		getSupportLoaderManager().initLoader(1, null, this);
	}

	/************************************************************
	 * Loading HTTP requests Methods
	 ************************************************************/
	@Override
	public Loader<ResponseBundle> onCreateLoader(int loaderID, Bundle args) {
		InternetStatus internetStatus = new InternetStatus(this);
		if (!internetStatus.isOnline()){
			// Aqui hay que imprimir un aviso en la pantalla de que es necesaria conexion a internet
			return null;
		}
		
		switch (loaderID) {
			case 0:
				return null;
			case 1:
				MainSetUpAndCheckSessionHTTPLoader loader = new MainSetUpAndCheckSessionHTTPLoader(this, null);
				loader.forceLoad();
				return loader;
			default:
				return null;
		}
	}
	
	public void startRequestLoader(int id, RequestParamsBundle bundle) {
		
	}

	@Override
	public void onLoadFinished(Loader<ResponseBundle> loader, ResponseBundle responseBundle) {
		switch(loader.getId()) {
			case 1:
				checkSessionInResponse(responseBundle);
				break;
			default:
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<ResponseBundle> arg0) {		
	}
	
	/************************************************************
	 * Check the status of the user session with the server
	 ************************************************************/
	public void checkSessionInResponse(ResponseBundle responseBundle) {
		if (responseBundle.getResponse() != null) {
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
	
}