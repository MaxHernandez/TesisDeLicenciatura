package com.maxkalavera.ecoar.productinfo;

import java.util.ArrayList;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.ecoar.searchbar.SearchBarResultsListFragmentLoader;
import com.maxkalavera.utils.SlideMenuBarHandler;
import com.maxkalavera.utils.datamodels.ProductInfoModel;
import com.maxkalavera.utils.datamodels.ProductModel;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.BitmapFactory;
import 	android.graphics.Bitmap;
import android.util.Log;

public class ProductInfo extends BaseActivity implements 
LoaderManager.LoaderCallbacks<ProductInfoModel>{
	private ProductModel product;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.productinfo);
		try{
			this.product =  
					(ProductModel) getIntent().getSerializableExtra("product");
       } catch(Exception e){
              Log.e("ProductInfo_create:", e.toString());
       }
	}
	
	@Override
	public Loader<ProductInfoModel> onCreateLoader(int loaderID, Bundle args) {
		switch (loaderID) {
			case 0:
				return null;
			case 1:
				ProductInfoLoader loader = new ProductInfoLoader(this, this.product);
				loader.forceLoad();
				return loader;
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<ProductInfoModel> arg0, ProductInfoModel loaderRes) {
		setData(loaderRes);
	}

	@Override
	public void onLoaderReset(Loader<ProductInfoModel> arg0) {
	}
	
	public void setData(ProductInfoModel info) {
		((TextView) findViewById(R.id.productinfo_productname)).setText(info.product.productName);
		((ImageView) findViewById(R.id.productinfo_productimage)).setImageBitmap(info.product.image);
		((TextView) findViewById(R.id.productinfo_productqualification)).setText(info.qualification);

		Bitmap aproved = BitmapFactory.decodeResource(getResources(), R.drawable.aproved);
		Bitmap notAproved = BitmapFactory.decodeResource(getResources(), R.drawable.notaproved);		
		
		boolean categoriesBooleanList[] = new boolean[] {
				info.transport,
				info.energy,
				info.water,
				info.society,
				info.recyclable
		};
		int categoriesIDList[] = new int[] {
				R.id.productinfo_transport,
				R.id.productinfo_energy,
				R.id.productinfo_water,
				R.id.productinfo_society,
				R.id.productinfo_recyclable
		};
		
		for (int i = 0; i < categoriesBooleanList.length; i++ ) {
			if (categoriesBooleanList[i]) {
				((ImageView) findViewById(categoriesIDList[i])).setImageBitmap(aproved);
			} else {
				((ImageView) findViewById(categoriesIDList[i])).setImageBitmap(notAproved);
			}
		}

	}
	

}