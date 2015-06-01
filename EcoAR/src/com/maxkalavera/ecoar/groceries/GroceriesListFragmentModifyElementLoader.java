package com.maxkalavera.ecoar.groceries;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.maxkalavera.utils.database.GroceriesListDAO;
import com.maxkalavera.utils.database.productmodel.ProductModel;

public class GroceriesListFragmentModifyElementLoader  extends AsyncTaskLoader<List<ProductModel>> {
	List<ProductModel> valuesList;
	Bundle params;
	
	final static String POSITION = "position";
	
	public GroceriesListFragmentModifyElementLoader(Context context, 
			List<ProductModel> valuesList, 
			Bundle params) {
		super(context);
		this.valuesList=valuesList;
		this.params=params;
	}

	public List<ProductModel> loadInBackground() {
		int position = 0;
		
		if (params.containsKey(POSITION)) {
			position = params.getInt("position");
			
			GroceriesListDAO groceriesListDAO = new GroceriesListDAO(this.getContext());
			groceriesListDAO.updateScoreOnProduct(this.valuesList.get(position));
			
		}
		
		return null;
	}
	
};
