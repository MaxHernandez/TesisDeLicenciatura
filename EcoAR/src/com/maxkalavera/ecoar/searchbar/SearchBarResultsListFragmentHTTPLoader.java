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
import com.maxkalavera.utils.searchobtainers.ServerBarcodeObtainer;
import com.maxkalavera.utils.searchobtainers.ServerQueryObtainer;


public class SearchBarResultsListFragmentHTTPLoader extends AsyncTaskLoader<ArrayList<ProductModel>> {
	String query;
	int page;
	String obtainer;
	
	// Metodo constructor
	public SearchBarResultsListFragmentHTTPLoader(Context context, String query, int page, String obtainer) {
		super(context);
		this.query = query;
		this.page = page;
		this.obtainer = obtainer;
	}
		
	public ArrayList<ProductModel> loadInBackground() {
		if (query != null) {
			
			ArrayList<ProductModel> data = null;
			
			if (this.obtainer.equals(AmazonSearchObtainer.NAME)) 
				data = new AmazonSearchObtainer(getContext()).getData(query, page);
			
			else if (this.obtainer.equals(ServerBarcodeObtainer.NAME))
				data = new ServerBarcodeObtainer(getContext()).getData(query, page);
			
			else if (this.obtainer.equals(ServerQueryObtainer.NAME))
				data = new ServerQueryObtainer(getContext()).getData(query, page);
			
			if (data == null) return null;
			
			GroceriesListDAO groceriesListDAO = new GroceriesListDAO(getContext());
			groceriesListDAO.open();
			for(int i=0; i < data.size(); i++)
				groceriesListDAO.isProductInGroceries(data.get(i));
			groceriesListDAO.close();
			return data;	
			
		}
		return null;
	}
}
