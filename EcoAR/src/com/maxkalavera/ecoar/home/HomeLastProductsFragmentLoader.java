package com.maxkalavera.ecoar.home;

import java.util.ArrayList;
import java.util.List;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.ProductCacheDAO;
import com.maxkalavera.utils.database.productmodel.ProductModel;

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
		ProductCacheDAO productCacheDAO = new ProductCacheDAO(this.getContext());
		// ERROR: Esta variable debe ser movida a la lista de valores en los archivos de recursos
		productCacheDAO.open();
		List<ProductModel> temp = productCacheDAO.getLastProducts(this.numberOfProducts);
		productCacheDAO.close();
		return temp;
	}
	

}
