package com.maxkalavera.ecoar.home;

import java.util.ArrayList;

import com.maxkalavera.utils.datamodels.ProductModel;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.Loader;

public class HomeLastProductsFragmentLoader extends Loader<ArrayList<ProductModel>> {

	public HomeLastProductsFragmentLoader(Context context) {
		super(context);
	}
	

}
