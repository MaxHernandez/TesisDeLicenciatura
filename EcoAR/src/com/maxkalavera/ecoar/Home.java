package com.maxkalavera.ecoar;

import java.io.IOException;


import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
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
import com.maxkalavera.utils.LastProductsList;
import com.maxkalavera.utils.SlideMenuBarHandler;
import com.maxkalavera.utils.HTTPRequest;

public class Home extends FragmentActivity{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.home);
		
		LastProductsList lastProducts = new LastProductsList(this);
		String [][] lastProductsElements = {{"Trigo", "sirve para tal ..."}, {"Detergente", "Agregado hace 5 min"}};
		lastProducts.setElements(lastProductsElements);
		
		ScrollView someProductsWidget = (ScrollView)this.findViewById(R.id.home_someproducts_scrollview);
		someProductsWidget.addView(lastProducts);
		
		SlideMenuBarHandler slideMenu = new SlideMenuBarHandler(this, "Home");
		
		HTTPRequest requestHandler = new HTTPRequest();
		requestHandler.execute(new String[] {"http://elisa.dyndns-web.com/"});

		//TextView usernameText = (TextView)this.findViewById(R.id.home_wellcomeuser_usernametext);
		//usernameText.setText("Pablo");
		
	}

}