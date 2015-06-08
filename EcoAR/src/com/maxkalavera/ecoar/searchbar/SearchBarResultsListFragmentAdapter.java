package com.maxkalavera.ecoar.searchbar;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.groceries.GroceriesListFragment;
import com.maxkalavera.ecoar.groceries.GroceriesListFragmentAdapter;
import com.maxkalavera.ecoar.productinfo.ProductInfo;
import com.maxkalavera.ecoar.searchbar.*;
import com.maxkalavera.utils.database.productmodel.ProductModel;


/************************************************************
 * 
 ************************************************************/
class FloatingMenuListener 	implements View.OnClickListener,
		PopupMenu.OnMenuItemClickListener, 
		LoaderManager.LoaderCallbacks<ProductModel> {
	
	private Context context;
	ArrayList<ProductModel> productList;
	private int elementListPosition;
	private ListFragment fragment;
	
	public static final int IS_ITEM_ON_LIST = 1;
	public static final int ADD_ELEMENT = 2;
	public static final int REMOVE_ELEMENT = 3;
	
	public FloatingMenuListener (ArrayList<ProductModel> productList,
			ListFragment fragment) {
		this.productList = productList;
		this.context = fragment.getActivity();
		this.elementListPosition = -1;
		this.fragment = fragment;
	}
	
	private Context getContext() {
		return this.context;
	}
	
	public void onClick(View view) {  
		this.elementListPosition = (Integer)view.getTag();
		ProductModel pModel = 
				this.productList.get(this.elementListPosition);

		PopupMenu popup = new PopupMenu(getContext(), view);
		popup.inflate(R.menu.searchbar_list_floatingmenu);
		popup.setOnMenuItemClickListener(this);
		popup.getMenu();

		Menu menu = popup.getMenu();
		if (pModel.getGroceriesId() == -1)
			menu.removeItem(R.id.searchbar_list_floatingmenu_removefromgroceries);
		else
			menu.removeItem(R.id.searchbar_list_floatingmenu_addtogroceries);
		
		popup.show();
	}

	public boolean onMenuItemClick(MenuItem item) {  				
		switch(item.getItemId()){
			case R.id.searchbar_list_floatingmenu_addtogroceries:
				this.fragment.
					getLoaderManager().restartLoader(FloatingMenuListener.ADD_ELEMENT, null, this);
     			break;
			case R.id.searchbar_list_floatingmenu_removefromgroceries:
				this.fragment.
					getLoaderManager().restartLoader(FloatingMenuListener.REMOVE_ELEMENT, null, this);
     			break;
     	}
		return true;
	} 
	
	/************************************************************
	 * Loaders's methods 
	 ************************************************************/
	@Override
	public Loader<ProductModel> onCreateLoader(int loaderID, Bundle args) {
		ProductModel product = this.productList.get(this.elementListPosition);
		switch (loaderID) {				
			case FloatingMenuListener.ADD_ELEMENT: 
				Log.i("SearchBarAddedToGroceries", "Add Groceries Loader.");
				SearchBarAddItemGroceriesListFragmentLoader loaderAddItem = 
					new SearchBarAddItemGroceriesListFragmentLoader(this.getContext(), product);	
				loaderAddItem.forceLoad();
				return loaderAddItem;
				
			case FloatingMenuListener.REMOVE_ELEMENT: 
				SearchBarRemoveItemListFragmentLoader loaderRemoveItem = 
					new SearchBarRemoveItemListFragmentLoader(this.getContext(), product);
				loaderRemoveItem.forceLoad();
				return loaderRemoveItem;
				
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<ProductModel> loader, ProductModel loaderRes) {
		switch (loader.getId()) {
			case FloatingMenuListener.ADD_ELEMENT: 
				this.productList.set(this.productList.indexOf(loaderRes), loaderRes);
				break;
			case FloatingMenuListener.REMOVE_ELEMENT: 
				this.productList.set(this.productList.indexOf(loaderRes), loaderRes);
				break;
			default:
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<ProductModel> loader) {
		
	}
	
};

/************************************************************
 * 
 ************************************************************/
class ProductNameClickListener implements View.OnClickListener {
	SearchBarResultsListFragmentAdapter searchBarResultsListFragmentAdapter;
	
	public ProductNameClickListener(SearchBarResultsListFragmentAdapter searchBarResultsListFragmentAdapter) {
		this.searchBarResultsListFragmentAdapter = searchBarResultsListFragmentAdapter;
	}
	
	@Override
	public void onClick(View view) {
		ProductModel pModel = searchBarResultsListFragmentAdapter.getItem((Integer)view.getTag());
		showProductInfo(pModel);
	}
	
	
	void showProductInfo(ProductModel pModel) {
        Intent intent = new Intent();
        intent.setClass(searchBarResultsListFragmentAdapter.getContext(), ProductInfo.class);
        intent.putExtra("product", pModel);
        searchBarResultsListFragmentAdapter.getContext().startActivity(intent);
	}
	
};


/************************************************************
 * 
 ************************************************************/
public class SearchBarResultsListFragmentAdapter extends ArrayAdapter<ProductModel> {
	
	private ProductNameClickListener productNameClickListener;
	private FloatingMenuListener floatingMenuListener;
	
	public SearchBarResultsListFragmentAdapter(ListFragment fragment, Context context, ArrayList<ProductModel> productList) {
		super(context, R.layout.searchbar_results_item, productList);
		this.productNameClickListener = new ProductNameClickListener(this);
		this.floatingMenuListener = new FloatingMenuListener(productList, fragment);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(this.getContext());
			convertView = inflater.inflate(R.layout.searchbar_results_item, null);
		}
		ProductModel pData = (ProductModel) this.getItem(position);
		
		ImageView productImage = (ImageView) convertView.findViewById(R.id.searchbar_item_image);
		productImage.setImageBitmap(pData.image);
		
		TextView name = (TextView) convertView.findViewById(R.id.searchbar_item_name);
		name.setText(pData.name);
		name.setTag(Integer.valueOf(position));
		name.setOnClickListener(this.productNameClickListener);
				
		TextView description = (TextView) convertView.findViewById(R.id.searchbar_item_description);
		description.setText(pData.description);
		
		Button menuButton = (Button) convertView.findViewById(R.id.searchbar_item_menu);
		menuButton.setTag(Integer.valueOf(position));
		menuButton.setOnClickListener(this.floatingMenuListener);
		
		return convertView;
	}
	
};