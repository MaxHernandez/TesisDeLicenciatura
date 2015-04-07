package com.maxkalavera.ecoar.home;

import java.util.ArrayList;
import java.util.List;

import com.maxkalavera.utils.database.models.ProductModel;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.Loader;

public class HomeLastProductsFragmentLoader extends Loader<List<ProductModel>> {

	public HomeLastProductsFragmentLoader(Context context) {
		super(context);
	}
	

}
