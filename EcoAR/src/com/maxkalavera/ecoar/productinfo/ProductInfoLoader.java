package com.maxkalavera.ecoar.productinfo;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.maxkalavera.utils.datamodels.ProductModel;
import com.maxkalavera.utils.datamodels.ProductInfoModel;

public class ProductInfoLoader extends AsyncTaskLoader<ProductInfoModel> {
	private ProductModel product;
	
	// Metodo constructor
	public ProductInfoLoader(Context context, ProductModel product) {
		super(context);
		this.product = product;
	}
	
	public ProductInfoModel loadInBackground() {
		ProductInfoModel info = new ProductInfoModel(this.product);
		
		
		
		return info;
	}
}
