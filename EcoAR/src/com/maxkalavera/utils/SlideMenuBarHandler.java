package com.maxkalavera.utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.home.Home;
import java.util.Hashtable;

public class SlideMenuBarHandler implements OnTouchListener{
	private SlidingMenu menu;
	View slideMenuView;
	Activity activity;
	String localClassName;
	Hashtable<View, String> dict = new Hashtable<View, String>();
	
	public SlideMenuBarHandler(Activity activity) {
		this.activity = activity;
		this.localClassName = this.activity.getComponentName(). getClassName();

		// Se llevan a cabo configuraciones en SlidingMenu obtenido del repositorio 
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
        
        //this.setButtons();
        }
	
	public void showMenu() {
		this.menu.toggle();
	}
	
	public void setButtons(SlideMenuBarHandlerButton[] buttons) {
		for (int i = 0; i < buttons.length; i++) {
			View item = this.addElement(buttons[i]);
			item.setOnTouchListener(this);
			this.dict.put(item, buttons[i].className);
		}
	}
	
	public View addElement(SlideMenuBarHandlerButton button){
		LinearLayout slideMenuLinearLayout = (LinearLayout)this.slideMenuView.findViewById(R.id.slidemenu_linearlayout);
		
		// Para cargar el Layout que servira como plantilla para cada elemento
	    LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(this.activity.LAYOUT_INFLATER_SERVICE);
		View itemPattern = inflater.inflate(R.layout.slidemenu_item, null);
		
		// Se cambia el nombre del producto en la plantilla
		TextView name = (TextView)itemPattern.findViewById(R.id.slidemenu_item_name);
		name.setText(button.text);
		
		ImageView icon = (ImageView)itemPattern.findViewById(R.id.slidemenu_item_image);
		icon.setImageResource(button.iconID);
		
		slideMenuLinearLayout.addView(itemPattern);
		return itemPattern;
	}
	
	public boolean onTouch(View item, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
        	item.setBackgroundResource(R.drawable.someproducts_item_onselect);
        	return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
        	item.setBackgroundResource(R.drawable.someproducts_item);
        	
        	String className = dict.get(item);
        	if (!this.localClassName.equals(className)) {
        		Intent intent = new Intent();
        		intent.setClassName("com.maxkalavera.ecoar", className);
        		this.activity.startActivity(intent);
        	}
        	
        	return false;
        }	
		return false;
	}
	
}