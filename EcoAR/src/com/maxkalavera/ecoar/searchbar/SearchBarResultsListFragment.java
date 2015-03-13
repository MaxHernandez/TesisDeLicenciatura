package com.maxkalavera.ecoar.searchbar;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.id;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.ecoar.login.LoginFragmentHTTPLoader;
import com.maxkalavera.ecoar.productinfo.ProductInfo;
import com.maxkalavera.utils.HTTPRequestTemp;
import com.maxkalavera.utils.databasemodels.ProductModel;
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
	LoaderManager.LoaderCallbacks<ArrayList<ProductModel>>, OnScrollListener {
			
	ArrayList<ProductModel> valuesList = new ArrayList<ProductModel>();
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
		this.getListView ().setOnScrollListener(this);
        
		this.adapter = new SearchBarResultsListFragmentAdapter(getActivity(), this.valuesList);
        setListAdapter(adapter);
		getLoaderManager().initLoader(0, null, this);
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searchbar_results, container, false);
    	return view;
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showProductInfo(position);
    }

	@Override
	public Loader<ArrayList<ProductModel>> onCreateLoader(int loaderID, Bundle args) {
		switch (loaderID) {
			case 0:
				return null;
			case 1:
				SearchBarResultsListFragmentLoader loader = new SearchBarResultsListFragmentLoader(this.getActivity(), this.query, this.page);
				loader.forceLoad();
				return loader;
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<ProductModel>> arg0, ArrayList<ProductModel> loaderRes) {
		if (loaderRes != null) {
			if (loaderRes.size() > 0 ) {
				this.page += 1;
				this.getListView ().setOnScrollListener(this);
				this.scrollListenFlag = true;
			}				
			this.valuesList.addAll(loaderRes);
			this.adapter.notifyDataSetChanged();
		}
		this.progressBar.setVisibility(View.GONE);
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<ProductModel>> arg0) {
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
		this.valuesList.clear();
		this.adapter.notifyDataSetChanged();
		this.query = query;
		this.page = 1;		
		this.loadData();
	}
	
	void showProductInfo(int position) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ProductInfo.class);
        intent.putExtra("product", valuesList.get(position));
        startActivity(intent);
	}
	
	private void loadData(){
		this.progressBar.setVisibility(View.VISIBLE);
		//this.progressBar.invalidateDrawable(this.progressBar.getProgressDrawable());
		getLoaderManager().restartLoader(1, null, this);
	}
	
}

