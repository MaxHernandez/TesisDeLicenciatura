package com.maxkalavera.ecoar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

class LastProducts extends LinearLayout{
	Context context;
	View itemPattern;
	
	LastProducts(Context context){
		super(context);
		this.context = context;
		// Fijar la orientación del widget en vertical es decir que se iran agregando cosas de arriba hacia abajo
		this.setOrientation(LinearLayout.VERTICAL);	
		
		// Este grupo de lineas sirven para definir el (Width, Height 1.0f) del widget
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, 1.0f);
		this.setLayoutParams(param);
		
		// Para cargar el Layout que servira como plantilla para cada elemento
	    LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		itemPattern = inflater.inflate(R.layout.home_someproducts_item, null);
		
		this.addElement("Detergente feliz :)"); //Eliminar
	}
	
	public void addElement(String str){
		TextView name = (TextView)this.itemPattern.findViewById(R.id.someproducts_item_name);
		name.setText(str);
		this.addView(this.itemPattern);
	}
}

public class Home extends Activity{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.home);
		
		LastProducts lastProducts = new LastProducts(this);
		
		ScrollView someProductsWidget = (ScrollView)this.findViewById(R.id.home_someproducts_scrollview);
		someProductsWidget.addView(lastProducts);

	}

}