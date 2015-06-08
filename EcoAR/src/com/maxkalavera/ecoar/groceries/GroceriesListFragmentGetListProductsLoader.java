package com.maxkalavera.ecoar.groceries;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.maxkalavera.utils.database.GroceriesListDAO;
import com.maxkalavera.utils.database.productmodel.ProductModel;

public class GroceriesListFragmentGetListProductsLoader  extends AsyncTaskLoader<List<ProductModel>> {
	
	public GroceriesListFragmentGetListProductsLoader(Context context) {
		super(context);
	}

	public List<ProductModel> loadInBackground() {
		GroceriesListDAO groceriesListDAO = new GroceriesListDAO(this.getContext());
		groceriesListDAO.open();
		List<ProductModel> productList = groceriesListDAO.getAllProducts();
		groceriesListDAO.close();
		return productList;
	}
	
}
