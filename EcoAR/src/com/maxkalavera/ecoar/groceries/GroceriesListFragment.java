package com.maxkalavera.ecoar.groceries;

import java.util.ArrayList;
import java.util.List;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.searchbar.SearchBar;
import com.maxkalavera.ecoar.searchbar.SearchBarResultsListFragmentAdapter;
import com.maxkalavera.ecoar.searchbar.SearchBarResultsListFragmentHTTPLoader;
import com.maxkalavera.utils.database.productmodel.ProductModel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ProgressBar;

public class GroceriesListFragment extends ListFragment implements 
LoaderManager.LoaderCallbacks<List<ProductModel>> {
	
	List<ProductModel> valuesList;
	GroceriesListFragmentAdapter adapter;
	
	public static final int LOADER_GET = 1;
	public static final int LOADER_REMOVE = 2;
	public static final int LOADER_CLEAR = 3;
	public static final int LOADER_MODIFY_ELEMENT = 4;
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.valuesList = new ArrayList<ProductModel>();
        
		this.adapter = new GroceriesListFragmentAdapter(this,
				(ArrayList<ProductModel>) this.valuesList);
        this.setListAdapter(this.adapter);
        
        // recupera los productos de la base de datos 
        this.getProducts();
        
        //registerForContextMenu(this.getListView());
    }

    
	/************************************************************
	 * OnCreateView 
	 ************************************************************/
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.groceries_list, container, false);
    	return view;
    }
    
	/************************************************************
	 *  Option Menu in elements of the list
	 ************************************************************/ 
    /*
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		this.getActivity().getMenuInflater().
		inflate(R.menu.groceries_list_floatingmenu , menu);
	}
 
	@Override
	public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch(item.getItemId()){
    		case R.id.groceries_list_floatingmenu_delete:
    			this.removeProduct(info.position);
    			break;
    	}
    	return true;
	}
	*/
    
	/************************************************************
	 *  
	 ************************************************************/ 
    public void removeProduct(int position) {
    	Bundle params = new Bundle();
    	params.putInt(GroceriesListFragmentRemoveElementLoader.POSITION,
    			position);
    	this.getLoaderManager().initLoader(GroceriesListFragment.LOADER_REMOVE, params, this);
    }
    
    public void modifyElement(int position, boolean checkboxValue) {
    	ProductModel product = this.valuesList.get(position);
    	product.setChecked(checkboxValue);
    	
    	Bundle params = new Bundle();
    	params.putInt(GroceriesListFragmentModifyElementLoader.POSITION,
    			position);
    	this.getLoaderManager().initLoader(GroceriesListFragment.LOADER_REMOVE, params, this);
    }
    
	/************************************************************
	 *  
	 ************************************************************/ 
    public void getProducts() {
    	this.getLoaderManager().initLoader(GroceriesListFragment.LOADER_GET, null, this);
    }
    
	/************************************************************
	 *  
	 ************************************************************/    
    public void keepSearching(View v) {
    	Intent intent = new Intent();
    	intent.setClass(this.getActivity(), SearchBar.class);
    	this.startActivity(intent);
    }

	/************************************************************
	 *  
	 ************************************************************/    
    public void clearList(View v) {
    	if (this.valuesList != null){
    		this.getLoaderManager()
    			.initLoader(GroceriesListFragment.LOADER_CLEAR, null, this);
    	}
    }
    
	/************************************************************
	 * Loaders's methods 
	 ************************************************************/
	@Override
	public Loader<List<ProductModel>> onCreateLoader(int loaderID, Bundle args) {
		switch (loaderID) {
			case 0:
				return null;
			case GroceriesListFragment.LOADER_GET:
				GroceriesListFragmentGetListProductsLoader loaderGetList = 
					new GroceriesListFragmentGetListProductsLoader(this.getActivity());
				loaderGetList.forceLoad();
				return loaderGetList;
			case GroceriesListFragment.LOADER_REMOVE:
				GroceriesListFragmentRemoveElementLoader loaderRemove = 
					new GroceriesListFragmentRemoveElementLoader(this.getActivity(), this.valuesList, args);
				loaderRemove.forceLoad();
				return loaderRemove;
			case GroceriesListFragment.LOADER_CLEAR:
				GroceriesListFragmentClearListLoader loaderClean = 
					new GroceriesListFragmentClearListLoader(this.getActivity());
				loaderClean.forceLoad();
				return loaderClean;
			case GroceriesListFragment.LOADER_MODIFY_ELEMENT:
				GroceriesListFragmentModifyElementLoader loaderModifyElement = 
				new GroceriesListFragmentModifyElementLoader(this.getActivity(), this.valuesList, args);
				loaderModifyElement.forceLoad();
				return loaderModifyElement;
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<List<ProductModel>> loader, List<ProductModel> loaderRes) {
		if (loaderRes != null) {
		    switch (loader.getId()) {
	        case GroceriesListFragment.LOADER_GET:
				this.valuesList = loaderRes;
				this.adapter.notifyDataSetChanged();
	            break;
	        case GroceriesListFragment.LOADER_REMOVE:
	        	this.valuesList = loaderRes;
				this.adapter.notifyDataSetChanged();
	            break;
	        case GroceriesListFragment.LOADER_CLEAR:
				this.valuesList.clear();
				this.adapter.notifyDataSetChanged();
	            break;
	        default:
	            break;
	    }

		}
	}

	@Override
	public void onLoaderReset(Loader<List<ProductModel>> arg0) {
	}
    
}
