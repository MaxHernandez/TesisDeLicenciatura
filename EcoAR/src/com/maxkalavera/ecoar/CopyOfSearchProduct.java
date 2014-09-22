package com.maxkalavera.ecoar;

/*
 * Este archivo es solo una versión antigua del buscador de productos, se ha decidio fabricar una nueva con 
 * las herramientas por defecto de Android.
 */

import com.maxkalavera.utils.CircleScore;
import com.maxkalavera.utils.LastProductsList;
import com.maxkalavera.utils.SlideMenuBarHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

class SBWView extends View {
	int searchBarPosition;
	
	public SBWView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	void setPos(int pos){
		this.searchBarPosition = pos;
	}
	
	int getPos(){
		return this.searchBarPosition;
	}
}

class SearchBarWidget extends LinearLayout implements OnTouchListener, OnClickListener{
	
	Context context;
	Button searchButton;
	EditText searchTextBar;
	int showInfoItemFlag = -1;
	
	// Clase constructora del SearchBarWidget.
	// Recibe el activity de la clase que lo llama para poder modificar y agregar elementos a la interfaz grafica.
	public SearchBarWidget(Activity context){
		super(context);
		this.context = context;
		// Fijar la orientación del widget en vertical es decir que se iran agregando cosas de arriba hacia abajo
		this.setOrientation(LinearLayout.VERTICAL);	
		
		// Este grupo de lineas sirven para definir el (Width, Height 1.0f) del widget
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, 1.0f);
		this.setLayoutParams(param);

		this.setGravity(Gravity.CENTER);
		
		searchButton = (Button) context.findViewById(R.id.searchproduct_searchButton);
		searchButton.setOnClickListener(this);
		searchTextBar = (EditText) context.findViewById(R.id.searchproduct_searchTextBar);
		
		for (int i = 0; i < 20; i += 1)
		{
			this.addElement("A"+Integer.toString(i), "algo");
		}
	}
		
	public void setElements(String[][] elements){
		for(int i = 0; i < elements.length; i++){
			this.addElement(elements[i][0], elements[i][1]);
		}
	}
	
	public void addElement(String nameStr, String descriptStr){
		
		// Para cargar el Layout que servira como plantilla para cada elemento
	    LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemPattern = inflater.inflate(R.layout.searchproduct_results_item, null);
		
		/*
		float circleSize = (float)48.0;
		float textSize = 20;
		float score = (float)9.8;
		
		// Se agrega el icono de calificacion ambiental que cambia de color
		// dependiendo de la calificacion
		LinearLayout circleIconSocket = (LinearLayout)itemPattern.findViewById(R.id.someproducts_item_socket);
		circleIconSocket.addView(
					new CircleScore(this.context, circleSize, textSize, score)
					, 0);
		*/
		
		// Se cambia el nombre del producto en la plantilla
		TextView name = (TextView)itemPattern.findViewById(R.id.someproducts_item_name);
		name.setText(nameStr);
		
		// Se cambia el nombre del producto en la plantilla
		TextView descript = (TextView)itemPattern.findViewById(R.id.someproducts_item_description);
		descript.setText(descriptStr);

		
		// Se usara este metodo para agregar listeners el item de la lista puesto
		// que estos metodos listeners podrian tomar mucho espacio.
		itemPattern.setOnTouchListener(this);
		
		this.addView(itemPattern);
	}
	
	public void addElementInfo(int position) {
		
		// Para cargar el Layout que servira como plantilla para cada elemento
	    LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemPattern = inflater.inflate(R.layout.searchproduct_results_iteminfo, null);
		
		this.addView(itemPattern, position);
	}
	
	public void removeElement(View item){
		this.removeView(item);
		//this.removeViewInLayout(item);
	}
	
	public void removeAllElements() {
		this.removeAllViews();
	}
	
	/*********************************************************
	 * Funciones listener de la clase 
	 *********************************************************/
	
	public boolean onTouch(View item, MotionEvent event) {	
		
		// Motion events
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
        	item.setBackgroundResource(R.drawable.someproducts_item_onselect); 
        	return true;
        	
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
        	item.setBackgroundResource(R.drawable.someproducts_item);
    		//Intent intent = new Intent();
    		//intent.setClassName("com.maxkalavera.ecoar", "com.maxkalavera.ecoar.ShowProductInfo");
    		//this.context.startActivity(intent);
        	
    		int selectedPos = this.indexOfChild(item);
    		
    		// Codigo para eliminar cuadros de información que esten siendo mostrados
    		if (this.showInfoItemFlag >= 0 ) {
    			this.removeViewAt(this.showInfoItemFlag);
    			
    			if (this.showInfoItemFlag == selectedPos+1) {
    				this.showInfoItemFlag = -1;
    			} else {
        			this.showInfoItemFlag = selectedPos+1;
        	   		this.addElementInfo(selectedPos+1);
    			}
    			
    		} else {
    			this.showInfoItemFlag = selectedPos+1;
    	   		this.addElementInfo(selectedPos+1);
    		}
        	
        	return false;
        	
        }  else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
        	item.setBackgroundResource(R.drawable.someproducts_item);
        	
        	return false;
        	
        }
        
		return false;
	}
	
	@Override
	public void onClick(View v) {
		String text = this.searchTextBar.getText().toString();
	}

}

public class CopyOfSearchProduct extends Activity{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.searchproduct);
			
		SlideMenuBarHandler slideMenu = new SlideMenuBarHandler(this, "SearchProduct");
		
		//SearchBarWidget searchBarWidget = new SearchBarWidget(this);
		//ScrollView searchBarWidgetScrollView = (ScrollView)this.findViewById(R.id.seachproduct_results_scrollView);
		//searchBarWidgetScrollView.addView(searchBarWidget);
	}
}