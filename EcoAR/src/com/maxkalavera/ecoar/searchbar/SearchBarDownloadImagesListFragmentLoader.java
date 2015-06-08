package com.maxkalavera.ecoar.searchbar;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.ProgressBar;

import com.maxkalavera.utils.database.GroceriesListDAO;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.httprequest.ImageDownloader;
import com.maxkalavera.utils.searchobtainers.AmazonSearchObtainer;


public class SearchBarDownloadImagesListFragmentLoader extends AsyncTaskLoader<ArrayList<ProductModel>> {
	ArrayList<ProductModel> productList;
	
	// Metodo constructor
	public SearchBarDownloadImagesListFragmentLoader(Context context, ArrayList<ProductModel> productList) {
		super(context);
		this.productList = productList;
	}
		
	public ArrayList<ProductModel> loadInBackground() {
		for (int i = 0; i < this.productList.size(); i++) {
			ProductModel pdata = this.productList.get(i);
			
			if (pdata.shopingService.equals(AmazonSearchObtainer.SHOPING_SERVICE))
				pdata.image = ImageDownloader.downloadImage(pdata.imageURL);
		}
		
		return productList;
	}
}
