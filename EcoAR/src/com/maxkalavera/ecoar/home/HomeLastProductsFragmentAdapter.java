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
import com.maxkalavera.utils.database.productmodel.ProductModel;

public class HomeLastProductsFragmentAdapter extends ArrayAdapter<List<ProductModel>> {
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
	public HomeLastProductsFragmentAdapter(Context context, List<ProductModel> productList) {
		super(context, R.layout.searchbar_results_item, (List)productList);
	}
	
	/************************************************************
	 * For every element in the list this method defines an 
	 * element view with a layout.
	 ************************************************************/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(this.getContext());
			convertView = inflater.inflate(R.layout.home_lastproducts_item, null);
		}
		
		ProductModel pData = (ProductModel) this.getItem(position);
		TextView productName = (TextView) convertView.findViewById(R.id.lastproducts_itemname);
		productName.setText(pData.name);
		
		return convertView;
	}
	
}
