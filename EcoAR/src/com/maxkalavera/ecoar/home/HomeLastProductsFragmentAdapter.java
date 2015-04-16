package com.maxkalavera.ecoar.home;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.jsonmodels.UserDataJsonModel;
import com.maxkalavera.utils.database.models.ProductModel;

public class HomeLastProductsFragmentAdapter extends ArrayAdapter<List<ProductModel>> {
	private final List<ProductModel> elementList;
	private final Activity activity;
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
	public HomeLastProductsFragmentAdapter(Activity activity, List<ProductModel> productList) {
		super(activity, R.layout.searchbar_results_item, (List)productList);
		this.activity = activity;
	    this.elementList = productList;
	}
	
	/************************************************************
	 * For every element in the list this method defines an 
	 * element view with a layout.
	 ************************************************************/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = this.activity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.lastproducts_item, null);
		}
		
		ProductModel pData = (ProductModel) this.elementList.get(position);
		TextView productName = (TextView) convertView.findViewById(R.id.lastproducts_itemname);
		productName.setText(pData.name);
		
		return convertView;
	}
	
}
