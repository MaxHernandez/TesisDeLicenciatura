package com.maxkalavera.ecoar.searchbar;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.id;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.ecoar.login.LoginFragmentLoginLoader;
import com.maxkalavera.ecoar.productinfo.ProductInfo;
import com.maxkalavera.utils.HTTPRequest;
import com.maxkalavera.utils.Product;
import com.maxkalavera.utils.searchobtainers.AmazonSearchObtainer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SearchBarResultsListFragment extends ListFragment implements 
	LoaderManager.LoaderCallbacks<ArrayList<Product>>, OnScrollListener {
			
	ArrayList<Product> itemValues = new ArrayList<Product>();
	SearchBarResultsListFragmentAdapter adapter;
	ProgressBar progressBar;
	View progressBarView;
	String query = null;
	int page = 1;
	boolean scrollListenFlag = true;
	
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
		LayoutInflater inflater = getActivity().getLayoutInflater();
		this.progressBarView = inflater.inflate(R.layout.loading, null);
		this.progressBar = (ProgressBar) progressBarView.findViewById(R.id.progressbar);
		
		this.getListView().addFooterView(this.progressBarView);
        this.adapter = new SearchBarResultsListFragmentAdapter(getActivity(), this.itemValues);
        
        setListAdapter(adapter);
		getLoaderManager().initLoader(0, null, this);
		this.getListView ().setOnScrollListener(this);
		
		Log.i("ecoar", "Loader initiated");
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searchbar_results, container, false);
    	//this.progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    	return view;
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showProductInfo(position);
    }

	@Override
	public Loader<ArrayList<Product>> onCreateLoader(int loaderID, Bundle args) {
		switch (loaderID) {
		case 0:
			return null;
		case 1:
			SearchBarResultsListFragmentLoader loader = new SearchBarResultsListFragmentLoader(this.getActivity(), this.query, this.page);
			loader.forceLoad();
			Log.i("ecoar", "Loader created");
			return loader;
		default:
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Product>> arg0, ArrayList<Product> loaderRes) {
		Log.i("ecoar", "Load finished");
		if (loaderRes != null) {
			if (loaderRes.size() > 0 ) {
				this.page += 1;
				this.getListView ().setOnScrollListener(this);
				this.scrollListenFlag = true;
			}				
			this.itemValues.addAll(loaderRes);
			this.adapter.notifyDataSetChanged();
		}
		//this.progressBarView.setVisibility(View.INVISIBLE);
		this.progressBar.setVisibility(View.GONE);
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Product>> arg0) {
		Log.i("ecoar", "Loader reset finished");
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if ( (this.scrollListenFlag) &&
		( (firstVisibleItem+visibleItemCount) >= totalItemCount ) 
		) {
			this.scrollListenFlag = false;
			this.getListView().setOnScrollListener(null);
			this.loadData();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollstate) {
	}
	
	void newSearch(String query){
		this.getLoaderManager().destroyLoader(0);
		this.itemValues.clear();
		this.adapter.notifyDataSetChanged();
		this.query = query;
		this.page = 1;		
		this.loadData();
		
		Log.i("ecoar", "Loader reseted");
	}
	
	void showProductInfo(int productID) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ProductInfo.class);
        intent.putExtra("productID", productID);
        startActivity(intent);
	}
	
	private void loadData(){
		Log.i("ecoar", "Loading icon visible!");
		this.progressBar.setVisibility(View.VISIBLE);
		//this.progressBar.invalidateDrawable(this.progressBar.getProgressDrawable());
		getLoaderManager().restartLoader(1, null, this);
	}
	
}

