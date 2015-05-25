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
	SearchBarResultsListFragmentAdapter searchBarResultsListFragmentAdapter;
	private int elementListPosition;
	private View elementView;
	private ListFragment fragment;
	
	public static final int IS_ITEM_ON_LIST = 1;
	public static final int ADD_ELEMENT = 2;
	public static final int REMOVE_ELEMENT = 3;
	
	public FloatingMenuListener (SearchBarResultsListFragmentAdapter searchBarResultsListFragmentAdapter,
			ListFragment fragment) {
		this.searchBarResultsListFragmentAdapter = searchBarResultsListFragmentAdapter;
		this.context = searchBarResultsListFragmentAdapter.getContext();
		this.elementListPosition = -1;
		this.elementView = null;
		this.fragment = fragment;
	}
	
	private Context getContext() {
		return this.context;
	}
	
	public void onClick(View view) {  
		this.elementListPosition = (Integer)view.getTag();
		this.elementView = view;
		this.fragment.
			getLoaderManager().initLoader(FloatingMenuListener.IS_ITEM_ON_LIST, null, this);
	}
	
	public void showPopUpMenu(ProductModel pModel) {
		PopupMenu popup = new PopupMenu(getContext(), this.elementView);  		
		popup.inflate(R.menu.searchbar_list_floatingmenu);
		popup.setOnMenuItemClickListener(this);
		popup.getMenu();

		Menu menu = popup.getMenu();
	
		if (pModel.getGroceriesID() == -1)
			menu.removeItem(R.id.searchbar_list_floatingmenu_addtogroceries);
		else
			menu.removeItem(R.id.searchbar_list_floatingmenu_removefromgroceries);
		
		popup.show();
	}
	
	public boolean onMenuItemClick(MenuItem item) {  
		switch(item.getItemId()){
			case R.id.searchbar_list_floatingmenu_addtogroceries:
				this.fragment.
					getLoaderManager().initLoader(FloatingMenuListener.ADD_ELEMENT, null, this);
     			break;
			case R.id.searchbar_list_floatingmenu_removefromgroceries:
				searchBarResultsListFragmentAdapter.getItem(this.elementListPosition);
				this.fragment.
					getLoaderManager().initLoader(FloatingMenuListener.REMOVE_ELEMENT, null, this);
     			break;
     	}
		return true;
	} 
	
	/************************************************************
	 * Loaders's methods 
	 ************************************************************/
	@Override
	public Loader<ProductModel> onCreateLoader(int loaderID, Bundle args) {
		ProductModel product = searchBarResultsListFragmentAdapter.getItem(this.elementListPosition);
		switch (loaderID) {
			case FloatingMenuListener.IS_ITEM_ON_LIST:
				IsItemOnListSearchBarResultsListFragmentLoader loaderIsElementOnlist = 
					new IsItemOnListSearchBarResultsListFragmentLoader(this.getContext(), product);
				loaderIsElementOnlist.forceLoad();
				return loaderIsElementOnlist;
				
			case FloatingMenuListener.ADD_ELEMENT: 
				AddItemSearchBarResultsListFragmentLoader loaderAddItem = 
				new AddItemSearchBarResultsListFragmentLoader(this.getContext(), product);				
				return loaderAddItem;
				
			case FloatingMenuListener.REMOVE_ELEMENT: 
				RemoveItemSearchBarResultsListFragmentLoader loaderRemoveItem = 
				new RemoveItemSearchBarResultsListFragmentLoader(this.getContext(), product);				
				return loaderRemoveItem;
				
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<ProductModel> loader, ProductModel loaderRes) {
		switch (loader.getId()) {
		case FloatingMenuListener.IS_ITEM_ON_LIST:
			this.showPopUpMenu(loaderRes);
			break;
		case FloatingMenuListener.ADD_ELEMENT: 
			break;
		case FloatingMenuListener.REMOVE_ELEMENT: 
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
		this.floatingMenuListener = new FloatingMenuListener(this, fragment);
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