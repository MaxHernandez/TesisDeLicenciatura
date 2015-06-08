package com.maxkalavera.ecoar.groceries;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.maxkalavera.utils.database.GroceriesListDAO;
import com.maxkalavera.utils.database.productmodel.ProductModel;

public class GroceriesListFragmentClearListLoader  extends AsyncTaskLoader<List<ProductModel>> {
	List<ProductModel> valuesList;
	
	public GroceriesListFragmentClearListLoader(Context context, List<ProductModel> valuesList) {
		super(context);
		this.valuesList = valuesList;
	}

	public List<ProductModel> loadInBackground() {
		try {
			GroceriesListDAO groceriesListDAO = new GroceriesListDAO(this.getContext()); 
			groceriesListDAO.open();			
			groceriesListDAO.removeAllProducts();
			groceriesListDAO.close();
			
			this.valuesList.clear();
			return this.valuesList;
		} catch (Exception e ) {
			e.printStackTrace();
		}
		return null;
	}
	
}
