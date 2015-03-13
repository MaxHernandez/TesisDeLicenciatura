package com.maxkalavera.ecoar.searchbar;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.databasemodels.ProductModel;


public class SearchBarResultsListFragmentAdapter extends ArrayAdapter<ArrayList<ProductModel>> {
	private final Activity context;
	private final ArrayList<ProductModel> elementList;
	
	public SearchBarResultsListFragmentAdapter(Activity context, ArrayList<ProductModel> productList) {
		super(context, R.layout.searchbar_results_item, (ArrayList)productList);
	    this.context = context;
	    this.elementList = productList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.searchbar_results_item, null);
		}
		ProductModel pData = (ProductModel) this.elementList.get(position);
		TextView productName = (TextView) convertView.findViewById(R.id.searchproduct_itemname);
		productName.setText(pData.productName);
		
		ImageView productImage = (ImageView) convertView.findViewById(R.id.searchproduct_item_image);
		productImage.setImageBitmap(pData.image);
		
		Button addShoppingButton = (Button) convertView.findViewById(R.id.searchproduct_item_addshoppinglist);
		addShoppingButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
			}
		});
		
		return convertView;
	}
}