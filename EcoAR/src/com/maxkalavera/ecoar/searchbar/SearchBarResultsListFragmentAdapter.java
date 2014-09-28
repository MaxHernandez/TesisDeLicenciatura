package com.maxkalavera.ecoar.searchbar;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.Product;


public class SearchBarResultsListFragmentAdapter extends ArrayAdapter<ArrayList<Product>> {
	private final Activity context;
	private final ArrayList productList;
	
	public SearchBarResultsListFragmentAdapter(Activity context, ArrayList productList) {
		super(context, R.layout.searchbar_results_item, productList);
	    this.context = context;
	    this.productList = productList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("ecoar", "");
		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.searchbar_results_item, null);
		}
		Product pdata = (Product) this.productList.get(position);
		TextView productName = (TextView) convertView.findViewById(R.id.searchproduct_item_name);
		productName.setText(pdata.productName);
		
		ImageView productImage = (ImageView) convertView.findViewById(R.id.searchproduct_item_image);
		productImage.setImageBitmap(pdata.image);		
		return convertView;
	}
}