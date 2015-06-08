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
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ProgressBar;

public class GroceriesListFragment extends ListFragment implements 
	LoaderManager.LoaderCallbacks<List<ProductModel>>,
	OnClickListener {
	
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
        this.setUp();
    }
    
    public void setUp(){
        this.valuesList = new ArrayList<ProductModel>();
        
		this.adapter = new GroceriesListFragmentAdapter(this,
				(ArrayList<ProductModel>) this.valuesList);
        this.setListAdapter(this.adapter);
        
        getView().findViewById(R.id.groseries_keepsearching).setOnClickListener(this);
        getView().findViewById(R.id.groseries_clear).setOnClickListener(this);
        
        // recupera los productos de la base de datos 
        this.getProducts();
    	
    }

	/************************************************************
	 * OnClickListener
	 ************************************************************/
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.groseries_keepsearching:
				this.keepSearching();
				break;
			
			case R.id.groseries_clear:
				this.clearList();
				break;
				
			default:
				break;
		}
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
	 *  
	 ************************************************************/    
    public void keepSearching() {
    	Intent intent = new Intent();
    	intent.setClass(this.getActivity(), SearchBar.class);
    	this.startActivity(intent);
    }
    
	/************************************************************
	 *  
	 ************************************************************/ 
    public void removeProduct(int position) {
    	Bundle params = new Bundle();
    	params.putInt(GroceriesListFragmentRemoveElementLoader.POSITION,
    			position);
    	this.getLoaderManager()
    		.restartLoader(GroceriesListFragment.LOADER_REMOVE, params, this);
    }
    
	/************************************************************
	 *  
	 ************************************************************/
    public void modifyElement(int position) {    	
    	Bundle params = new Bundle();
    	params.putInt(GroceriesListFragmentModifyElementLoader.POSITION,
    			position);
    	this.getLoaderManager()
    		.restartLoader(GroceriesListFragment.LOADER_MODIFY_ELEMENT, params, this);
    }
    
	/************************************************************
	 *  
	 ************************************************************/ 
    public void getProducts() {
    	this.getLoaderManager()
    		.restartLoader(GroceriesListFragment.LOADER_GET, null, this);
    }

	/************************************************************
	 *  
	 ************************************************************/    
    public void clearList() {
    	getView().findViewById(R.id.groseries_clear).setOnClickListener(null);
    	this.getLoaderManager()
    		.restartLoader(GroceriesListFragment.LOADER_CLEAR, null, this);
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
					new GroceriesListFragmentClearListLoader(this.getActivity(), this.valuesList);
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
		switch (loader.getId()) {
	        case GroceriesListFragment.LOADER_GET:
				//this.valuesList = loaderRes;
	        	if (loaderRes != null) {
	        		this.valuesList.addAll(loaderRes);
					this.adapter.notifyDataSetChanged();
	        	}
	            break;
	            
	        case GroceriesListFragment.LOADER_REMOVE:
	        	//this.valuesList = loaderRes;
				this.adapter.notifyDataSetChanged();
	            break;
	            
	        case GroceriesListFragment.LOADER_CLEAR:
	        	getView().findViewById(R.id.groseries_clear).setOnClickListener(this);
				this.adapter.notifyDataSetChanged();
	            break;
	            
	        case GroceriesListFragment.LOADER_MODIFY_ELEMENT:
				this.adapter.notifyDataSetChanged();
	            break;
	            
	        default:
	            break;
		}
	}

	@Override
	public void onLoaderReset(Loader<List<ProductModel>> arg0) {
	}
    
}
