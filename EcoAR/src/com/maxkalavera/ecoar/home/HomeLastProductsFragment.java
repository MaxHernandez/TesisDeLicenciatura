package com.maxkalavera.ecoar.home;

import java.util.ArrayList;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.productinfo.ProductInfo;
import com.maxkalavera.ecoar.searchbar.SearchBarResultsListFragmentAdapter;
import com.maxkalavera.ecoar.searchbar.SearchBarResultsListFragmentLoader;
import com.maxkalavera.utils.datamodels.ProductModel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

public class HomeLastProductsFragment extends ListFragment implements LoaderCallbacks<ArrayList<ProductModel>> {
	HomeLastProductsFragmentAdapter adapter;
	ArrayList<ProductModel> valuesList = new ArrayList<ProductModel>();
	
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);        
		this.adapter = new HomeLastProductsFragmentAdapter(getActivity(), this.valuesList);
        this.setListAdapter(this.adapter);
		getLoaderManager().initLoader(0, null, this);
    }
	
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
    
	@Override
	public Loader<ArrayList<ProductModel>> onCreateLoader(int loaderID, Bundle args) {
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
	public void onLoadFinished(Loader<ArrayList<ProductModel>> loader, ArrayList<ProductModel> data) {		
		this.valuesList.addAll(data);
		this.adapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<ProductModel>> loader) {		
	}

	
}
