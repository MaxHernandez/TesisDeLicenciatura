package com.maxkalavera.ecoar.groceries;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.maxkalavera.utils.database.GroceriesListDAO;
import com.maxkalavera.utils.database.productmodel.ProductModel;

public class GroceriesListFragmentClearListLoader  extends AsyncTaskLoader<List<ProductModel>> {
	
	public GroceriesListFragmentClearListLoader(Context context) {
		super(context);
	}

	public List<ProductModel> loadInBackground() {
		try {
			GroceriesListDAO groceriesListDAO = new GroceriesListDAO(this.getContext()); 
			groceriesListDAO.removeAllProducts();
			return new ArrayList<ProductModel>();
		} catch (Exception e ) {
			e.printStackTrace();
		}
		return null;
	}
	
}
