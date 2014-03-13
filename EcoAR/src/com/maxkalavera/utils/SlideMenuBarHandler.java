package com.maxkalavera.utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.maxkalavera.ecoar.Home;
import com.maxkalavera.ecoar.R;
import java.util.Hashtable;

public class SlideMenuBarHandler implements OnClickListener, OnTouchListener{	
	SlidingMenu menu;
	View slideMenuView;
	Activity activity;
	Hashtable<View, String> dict = new Hashtable<View, String>();
	String callerClass;
	
	public SlideMenuBarHandler(Activity activity, String callerClass) {
		this.activity = activity;
		this.callerClass = callerClass;
		// SlidingMenu obtenido del repositorio 
        menu = new SlidingMenu(activity);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.commentlist_item);
        //menu.setBehindOffsetRes(R.dimen.slidemenu_slide_offset);
        menu.setBehindWidthRes(R.dimen.slidemenu_slide_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        
	    LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(this.activity.LAYOUT_INFLATER_SERVICE);
		View slideMenuView = inflater.inflate(R.layout.slidemenu, null);
		this.slideMenuView = slideMenuView;
        menu.setMenu(slideMenuView);
        
        this.setButtons();
        }
	
	public View addElement(String nameStr){
		LinearLayout slideMenuLinearLayout = (LinearLayout)this.slideMenuView.findViewById(R.id.slidemenu_linearlayout);
		
		// Para cargar el Layout que servira como plantilla para cada elemento
	    LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(this.activity.LAYOUT_INFLATER_SERVICE);
		View itemPattern = inflater.inflate(R.layout.slidemenu_item, null);
		
		// Se cambia el nombre del producto en la plantilla
		TextView name = (TextView)itemPattern.findViewById(R.id.slidemenu_item_name);
		name.setText(nameStr);
		
		slideMenuLinearLayout.addView(itemPattern);
		return itemPattern;
	}
	
	private void setButtons() {
		// El boton que despliega el SlideMenu 
		Button displayButton = (Button)this.activity.findViewById(R.id.slidemenu_topbar_displaybutton);
        displayButton.setOnClickListener(this);
        
        View item = this.addElement("Home");
		item.setOnTouchListener(this);
		this.dict.put(item, "Home");
		
		item = this.addElement("Login");
		item.setOnTouchListener(this);
		this.dict.put(item, "Login");
		
		item = this.addElement("Buscar");
		item.setOnTouchListener(this);
		this.dict.put(item, "SearchProduct");
		
		item = this.addElement("Buscar por cámara");
		item.setOnTouchListener(this);
		
		item = this.addElement("Lista del super");
		item.setOnTouchListener(this);
	}
	
	public boolean onTouch(View item, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
        	item.setBackgroundResource(R.drawable.someproducts_item_onselect);
        	return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
        	item.setBackgroundResource(R.drawable.someproducts_item);
        	
        	String className = dict.get(item);
        	if (this.callerClass != className) {
        		Intent intent = new Intent();
        		intent.setClassName("com.maxkalavera.ecoar", "com.maxkalavera.ecoar."+className);
        		this.activity.startActivity(intent);
        	}
        	
        	return false;
        }	
		return false;
	}
	
	
	public void onClick(View view){
		switch(view.getId()) {
		case R.id.slidemenu_topbar_displaybutton:
			this.menu.showMenu();
			break;
		}
	}
	
}