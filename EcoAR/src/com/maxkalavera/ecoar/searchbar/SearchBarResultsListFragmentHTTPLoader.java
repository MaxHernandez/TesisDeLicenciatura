package com.maxkalavera.ecoar.searchbar;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.ProgressBar;

import com.maxkalavera.utils.database.GroceriesListDAO;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.searchobtainers.AmazonSearchObtainer;


public class SearchBarResultsListFragmentHTTPLoader extends AsyncTaskLoader<ArrayList<ProductModel>> {
	String query;
	int page;
	
	// Metodo constructor
	public SearchBarResultsListFragmentHTTPLoader(Context context, String query, int page) {
		super(context);
		this.query = query;
		this.page = page;
	}
		
	public ArrayList<ProductModel> loadInBackground() {
		if (query != null) {
			try{
				ArrayList<ProductModel> data = new AmazonSearchObtainer(getContext()).getData(query, page);
				
				GroceriesListDAO groceriesListDAO = new GroceriesListDAO(getContext());
				groceriesListDAO.open();
				for(int i=0; i < data.size(); i++)
					groceriesListDAO.isProductInGroceries(data.get(i));
				groceriesListDAO.close();
				
				
				return data;	
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
