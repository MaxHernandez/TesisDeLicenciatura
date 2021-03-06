package com.maxkalavera.ecoar;

import com.maxkalavera.utils.CircleScore;
import com.maxkalavera.utils.LastProductsList;
import com.maxkalavera.utils.SlideMenuBarHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

public class SearchProduct extends FragmentActivity implements OnClickListener{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		this.setContentView(R.layout.searchproduct);
		SlideMenuBarHandler slideMenu = new SlideMenuBarHandler(this, "SearchProduct");
		
		Button startSearchButton = (Button) findViewById(R.id.searchproduct_searchButton);
		startSearchButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		SearchProductResults fragment = 
				(SearchProductResults) getSupportFragmentManager().findFragmentById(R.id.searchProductResultsFragment);
		EditText queryInputEditText = (EditText) findViewById(R.id.searchproduct_searchTextBar);
		fragment.newSearch(queryInputEditText.getText().toString());
	}
}