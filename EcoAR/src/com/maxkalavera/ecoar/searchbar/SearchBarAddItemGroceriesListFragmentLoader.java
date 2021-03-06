package com.maxkalavera.ecoar.searchbar;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.ProgressBar;

import com.maxkalavera.utils.database.GroceriesListDAO;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.searchobtainers.AmazonSearchObtainer;


public class SearchBarAddItemGroceriesListFragmentLoader extends AsyncTaskLoader<ProductModel> {
	ProductModel product;
	
	// Metodo constructor
	public SearchBarAddItemGroceriesListFragmentLoader(Context context, ProductModel product) {
		super(context);
		this.product = product;
	}
		
	public ProductModel loadInBackground() {
		GroceriesListDAO groceriesListDAO = new GroceriesListDAO(getContext());
		groceriesListDAO.open();
		ProductModel product = groceriesListDAO.addProduct(this.product);
		groceriesListDAO.close();
		return product;
	}
}
