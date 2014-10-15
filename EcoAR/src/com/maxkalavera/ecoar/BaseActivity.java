package com.maxkalavera.ecoar;

import com.maxkalavera.utils.ActionBarHandler;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseActivity extends FragmentActivity  {
	public ActionBarHandler actionBarHandler;
	
	public void onCreate(Bundle savedInstanceState, Activity activity) {
		super.onCreate(savedInstanceState);
		this.actionBarHandler = new ActionBarHandler(activity);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Boolean rval = this.actionBarHandler.onOptionsItemSelected(item);
		if (rval != null) 
			return (boolean)rval;
		else
			return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.mainmenu, menu);
	    return true;
	}
	
};
