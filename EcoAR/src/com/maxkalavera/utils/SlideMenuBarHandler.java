package com.maxkalavera.utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.maxkalavera.ecoar.R;
import java.util.Hashtable;

public class SlideMenuBarHandler implements OnTouchListener{
	
	Context context; 
	private SlidingMenu menu;
	View slideMenuView;
//	Activity activity;
	String localClassName;
	Hashtable<View, String> dict = new Hashtable<View, String>();
	
	public SlideMenuBarHandler(Activity activity) {
		this.context = activity.getApplicationContext();
		this.localClassName = activity.getComponentName().getClassName();

		// Se llevan a cabo configuraciones en SlidingMenu obtenido del repositorio 
        menu = new SlidingMenu(this.getContext());
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.commentlist_item);
        //menu.setBehindOffsetRes(R.dimen.slidemenu_slide_offset);
        menu.setBehindWidthRes(R.dimen.slidemenu_slideoffset);
        TypedValue fadeDegreeOutValue = new TypedValue();
        this.getContext().getResources().getValue(R.dimen.slidemenu_fadedegree, fadeDegreeOutValue, true);
        menu.setFadeDegree(fadeDegreeOutValue.getFloat()); 
        menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        
	    LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View slideMenuView = inflater.inflate(R.layout.slidemenu, null);
		this.slideMenuView = slideMenuView;
        menu.setMenu(slideMenuView);
        
        //this.setButtons();
        }
	
	public Context getContext() {
		return this.context;
	}
	
	public void showMenu() {
		this.menu.toggle();
	}
	
	public void addButton(SlideMenuBarHandlerButton button, OnTouchListener listener) {
		View item = this.addElement(button);
		
		if (listener != null) 
			item.setOnTouchListener(listener);
		else
			item.setOnTouchListener(this);
		
		this.dict.put(item, button.className);
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
	    LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemPattern = inflater.inflate(R.layout.slidemenu_item, null);
		
		// Se cambia el nombre del producto en la plantilla
		TextView name = (TextView)itemPattern.findViewById(R.id.slidemenu_item_name);
		name.setText(button.text);
		
		if (button.iconID != null) {
			ImageView icon = (ImageView)itemPattern.findViewById(R.id.slidemenu_item_image);
			icon.setImageResource(button.iconID.intValue());
		}
		
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
        		this.getContext().startActivity(intent);
        	}
        	
        	return false;
        }	
		return false;
	}
	
}