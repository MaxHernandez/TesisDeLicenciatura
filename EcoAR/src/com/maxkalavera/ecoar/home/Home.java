package com.maxkalavera.ecoar.home;

import java.io.IOException;


import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import android.graphics.Canvas;
import android.graphics.Paint;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.graphics.Color;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.ecoar.login.Login;
import com.maxkalavera.ecoar.searchbar.SearchBar;
import com.maxkalavera.utils.ActionBarHandler;
import com.maxkalavera.utils.LastProductsList;
import com.maxkalavera.utils.SlideMenuBarHandler;
import com.maxkalavera.utils.HTTPRequest;

public class Home extends BaseActivity {//FragmentActivity {
	//private ActionBarHandler actionBarHandler;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, this);
		this.setContentView(R.layout.home);
	
		/*
		LastProductsList lastProducts = new LastProductsList(this);		
		lastProducts.setElements(lastProductsElements);
		ScrollView someProductsWidget = (ScrollView)this.findViewById(R.id.home_someproducts_scrollview);
		someProductsWidget.addView(lastProducts);
		*/
		//this.actionBarHandler = new ActionBarHandler(this);
	}
	/*
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
	*/
}