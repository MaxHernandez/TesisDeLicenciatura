package com.maxkalavera.ecoar.productinfo;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.maxkalavera.utils.databasemodels.ProductInfoModel;
import com.maxkalavera.utils.databasemodels.ProductModel;

public class RatingBarLoader extends AsyncTaskLoader<ProductInfoModel> {
	private ProductInfoModel productInfo;
	private int method;
	private int GET  = 0;
	private int POST = 1;
	
	// Metodo constructor
	public RatingBarLoader(Context context, ProductInfoModel product, int method) {
		super(context);
		this.productInfo = product;
		this.method = method;
	}
	
	public ProductInfoModel loadInBackground() {		
		if (this.method == this.GET) {
			
		}
		
		if (this.method == this.POST) {
			
		}
		
		return this.productInfo;
	}
}
