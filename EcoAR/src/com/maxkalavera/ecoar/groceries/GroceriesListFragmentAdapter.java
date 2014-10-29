package com.maxkalavera.ecoar.groceries;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.datamodels.ProductModel;

public class GroceriesListFragmentAdapter extends ArrayAdapter<ArrayList<ProductModel>> {
	private final Activity context;
	private final ArrayList<ProductModel> elementList;
	
	public GroceriesListFragmentAdapter(Activity context, ArrayList<ProductModel> productList) {
		super(context, R.layout.searchbar_results_item, (ArrayList)productList);
	    this.context = context;
	    this.elementList = productList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.groceries_list_item, null);
		}
		
		ProductModel pdata = (ProductModel) this.elementList.get(position);
		
		
		return convertView;
	}
	
};
