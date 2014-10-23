package com.maxkalavera.utils;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.home.Home;
import com.maxkalavera.ecoar.main.Main;

import com.maxkalavera.utils.CircleScore;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.maxkalavera.utils.CircleScore;

public class LastProductsList extends LinearLayout implements OnTouchListener{
	Context context;
	
	public LastProductsList(Context context){
		super(context);
		this.context = context;
		// Fijar la orientación de la lista en vertical es decir que se iran agregando cosas de arriba hacia abajo
		this.setOrientation(LinearLayout.VERTICAL);	
		
		// Este grupo de lineas sirven para definir el (Width, Height 1.0f) del widget
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, 1.0f);
		this.setLayoutParams(param);

		this.setGravity(Gravity.CENTER);
	}
	
	public void setElements(String[][] elements){
		for(int i = 0; i < elements.length; i++){
			this.addElement(elements[i][0], elements[i][1]);
		}
	}
	
	public void addElement(String nameStr, String descriptStr){
		
		// Para cargar el Layout que servira como plantilla para cada elemento
	    LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemPattern = inflater.inflate(R.layout.someproducts_item, null);
		
		float circleSize = (float)48.0;
		float textSize = 20;
		float score = (float)9.8;
		
		// Se agrega el icono de calificacion ambiental que cambia de color
		// dependiendo de la calificacion
		LinearLayout circleIconSocket = (LinearLayout)itemPattern.findViewById(R.id.someproducts_item_socket);
		circleIconSocket.addView(
					new CircleScore(this.context, circleSize, textSize, score)
					, 0);
		
		// Se cambia el nombre del producto en la plantilla
		TextView name = (TextView)itemPattern.findViewById(R.id.someproducts_name);
		name.setText(nameStr);
		
		// Se cambia el nombre del producto en la plantilla
		TextView descript = (TextView)itemPattern.findViewById(R.id.someproducts_description);
		descript.setText(descriptStr);

		
		// Se usara este metodo para agregar listeners el item de la lista puesto
		// que estos metodos listeners podrian tomar mucho espacio.
		itemPattern.setOnTouchListener(this);
		
		this.addView(itemPattern);
	}
	
	public void removeElement(View item){
		this.removeView(item);
		//this.removeViewInLayout(item);
	}
	
	public void removeAllElements() {
		this.removeAllViews();
	}
	
	public boolean onTouch(View item, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
        	item.setBackgroundResource(R.drawable.someproducts_item_onselect); 
        	return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
        	item.setBackgroundResource(R.drawable.someproducts_item);
    		Intent intent = new Intent();
    		intent.setClassName("com.maxkalavera.ecoar", "com.maxkalavera.ecoar.ShowProductInfo");
    		this.context.startActivity(intent);
        	return false;
        }	
		return false;
	}
	

}