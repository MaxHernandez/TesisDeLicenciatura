package com.maxkalavera.ecoar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.view.View;
import android.view.LayoutInflater;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;

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
import com.maxkalavera.utils.CircleScore;

public class Home extends Activity{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.home);
		
		LastProductsList lastProducts = new LastProductsList(this);
		String [][] lastProductsElements = {{"Trigo", "sirve para tal ..."}, {"Detergente", "Agregado hace 5 min"}};
		lastProducts.setElements(lastProductsElements);
		
		ScrollView someProductsWidget = (ScrollView)this.findViewById(R.id.home_someproducts_scrollview);
		someProductsWidget.addView(lastProducts);

		// Slide Menu
        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu);
		
	}

}