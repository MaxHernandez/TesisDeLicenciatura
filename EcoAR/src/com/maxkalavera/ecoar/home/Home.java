package com.maxkalavera.ecoar.home;


import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.UserDataDAO;
import com.maxkalavera.utils.httprequest.ResponseBundle;


public class Home extends BaseActivity{
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
	public void onCreate(Bundle savedInstanceState) {
		// Se define el layout del activity con setContentView() en el 
		// Activity padre para poder llevar a cabo su confiuracion.
		super.onCreate(savedInstanceState, R.layout.home);
	}
}