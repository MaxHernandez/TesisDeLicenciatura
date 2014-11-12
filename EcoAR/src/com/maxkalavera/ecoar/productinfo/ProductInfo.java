package com.maxkalavera.ecoar.productinfo;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.utils.SlideMenuBarHandler;
import com.maxkalavera.utils.datamodels.ProductInfoModel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ProductInfo extends BaseActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.productinfo);		
	}
	
	public void setData(ProductInfoModel info) {
		((TextView) findViewById(R.id.productinfo_productname)).setText(info.product.productName);
		((ImageView) findViewById(R.id.productinfo_productimage)).setImageBitmap(info.product.image);
		((TextView) findViewById(R.id.productinfo_productqualification)).setText(info.qualification);
	}
	

}