package com.maxkalavera.ecoar.home;

import java.util.ArrayList;
import java.util.List;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.CacheProductDAO;
import com.maxkalavera.utils.database.models.ProductModel;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

public class HomeLastProductsFragmentLoader extends AsyncTaskLoader<List<ProductModel>> {
	int numberOfProducts; 
	
	public HomeLastProductsFragmentLoader(Context context) {
		super(context);
		this.numberOfProducts = this.getContext().
				getResources().
				getInteger(R.dimen.home_lastproducts_numberofproducts);
	}

	@Override
	public List<ProductModel> loadInBackground() {
		CacheProductDAO cacheProductDAO = new CacheProductDAO(this.getContext());
		// ERROR: Esta variable debe ser movida a la lista de valores en los archivos de recursos
		cacheProductDAO.open();
		List<ProductModel> temp = cacheProductDAO.getLastProducts(this.numberOfProducts);
		cacheProductDAO.close();
		return temp;
	}
	

}
