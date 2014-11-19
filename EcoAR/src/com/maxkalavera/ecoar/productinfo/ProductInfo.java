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
import android.graphics.BitmapFactory;
import 	android.graphics.Bitmap;

public class ProductInfo extends BaseActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.productinfo);		
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