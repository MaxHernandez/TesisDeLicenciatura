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
import com.maxkalavera.utils.datamodels.ProductModel;


public class SearchBarResultsListFragmentAdapter extends ArrayAdapter<ArrayList<ProductModel>> {
	private final Activity context;
	private final ArrayList<ProductModel> productList;
	
	public SearchBarResultsListFragmentAdapter(Activity context, ArrayList<ProductModel> productList) {
		super(context, R.layout.searchbar_results_item, (ArrayList)productList);
	    this.context = context;
	    this.productList = productList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.searchbar_results_item, null);
		}
		ProductModel pdata = (ProductModel) this.productList.get(position);
		TextView productName = (TextView) convertView.findViewById(R.id.searchproduct_itemname);
		productName.setText(pdata.productName);
		
		ImageView productImage = (ImageView) convertView.findViewById(R.id.searchproduct_item_image);
		productImage.setImageBitmap(pdata.image);		
		return convertView;
	}
}