package com.maxkalavera.ecoar.home;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.datamodels.ProductModel;

public class HomeLastProductsFragmentAdapter extends ArrayAdapter<ArrayList<ProductModel>> {
	private final ArrayList<ProductModel> elementList;
	private final Activity context;
	
	public HomeLastProductsFragmentAdapter(Activity context, ArrayList<ProductModel> productList) {
		super(context, R.layout.searchbar_results_item, (ArrayList)productList);
		this.context = context;
	    this.elementList = productList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = this.context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.lastproducts_item, null);
		}
		
		ProductModel pData = (ProductModel) this.elementList.get(position);
		TextView productName = (TextView) convertView.findViewById(R.id.lastproducts_itemname);
		productName.setText(pData.productName);
		
		return convertView;
	}
	
}
