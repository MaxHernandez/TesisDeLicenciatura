package com.maxkalavera.ecoar.home;

import java.util.ArrayList;
import java.util.List;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.productinfo.ProductInfo;
import com.maxkalavera.ecoar.searchbar.SearchBarResultsListFragmentAdapter;
import com.maxkalavera.ecoar.searchbar.SearchBarResultsListFragmentLoader;
import com.maxkalavera.utils.database.models.ProductModel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

public class HomeLastProductsFragment extends ListFragment implements LoaderCallbacks<List<ProductModel>> {
	HomeLastProductsFragmentAdapter adapter;
	ArrayList<ProductModel> valuesList = new ArrayList<ProductModel>();
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);        
		this.adapter = new HomeLastProductsFragmentAdapter(getActivity(), this.valuesList);
        this.setListAdapter(this.adapter);
		getLoaderManager().initLoader(0, null, this);
    }
    
	/************************************************************
	 * 
	 ************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lastproducts, container, false);
    	return view;
    }
    
	/************************************************************
	 * 
	 ************************************************************/
    @Override
    public void onListItemClick(ListView l, View v, int position, long id)  {
    	this.showProductInfo(position);
    }

	void showProductInfo(int position) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ProductInfo.class);
        intent.putExtra("product", valuesList.get(position));
        startActivity(intent);
	}
    
	/************************************************************
	 * Loder methods
	 ************************************************************/
	
	@Override
	public Loader<List<ProductModel>> onCreateLoader(int loaderID, Bundle args) {
		switch (loaderID) {
		case 0:
			return null;
		case 1:
			HomeLastProductsFragmentLoader loader = new HomeLastProductsFragmentLoader(this.getActivity());
			loader.forceLoad();
			return loader;
		default:
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<List<ProductModel>> loader, List<ProductModel> data) {		
		this.valuesList.addAll(data);
		this.adapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<List<ProductModel>> loader) {		
	}

	
}
