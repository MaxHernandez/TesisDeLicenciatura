package com.maxkalavera.ecoar.searchbar;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.ProgressBar;

import com.maxkalavera.utils.database.models.ProductModel;
import com.maxkalavera.utils.searchobtainers.AmazonSearchObtainer;


public class SearchBarResultsListFragmentLoader extends AsyncTaskLoader<ArrayList<ProductModel>> {
	String query;
	int page;
	
	// Metodo constructor
	public SearchBarResultsListFragmentLoader(Context context, String query, int page) {
		super(context);
		this.query = query;
		this.page = page;
	}
		
	public ArrayList<ProductModel> loadInBackground() {
		if (query != null) {
			AmazonSearchObtainer dataObtainer = new AmazonSearchObtainer(getContext());
			ArrayList<ProductModel> data = dataObtainer.getData(query, page);
			return data;
		}
		return null;
	}
}
