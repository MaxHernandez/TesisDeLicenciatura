package com.maxkalavera.ecoar;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

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

public class SearchProductResults extends ListFragment implements 
	LoaderManager.LoaderCallbacks<ArrayList<Product>>, OnScrollListener {
			
	ArrayList<Product> itemValues = new ArrayList<Product>();
	ResultItemsAdapter adapter;
	ProgressBar progressBar;
	View progressBarView;
	String query = null;
	int page = 1;
	boolean scrollListenFlag = true;
	
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
		LayoutInflater inflater = getActivity().getLayoutInflater();
		this.progressBarView = inflater.inflate(R.layout.loadingscreen, null);
		this.progressBar = (ProgressBar) progressBarView.findViewById(R.id.progress_bar);
		//this.progressBar.setVisibility(View.GONE);
		
		this.getListView().addFooterView(this.progressBarView);
        this.adapter = new ResultItemsAdapter(getActivity(), this.itemValues);
        
        setListAdapter(adapter);
		getLoaderManager().initLoader(0, null, this);
		this.getListView ().setOnScrollListener(this);
		
		Log.i("ecoar", "Loader initiated");
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searchbar_results_fragment, container, false);
    	//this.progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    	return view;
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showProductInfo(position);
    }

	@Override
	public Loader<ArrayList<Product>> onCreateLoader(int arg0, Bundle args) {
		ResultProductsLoader loader = new ResultProductsLoader(this.getActivity(), this.query, this.page, 
				this.progressBar);
		loader.forceLoad();
		Log.i("ecoar", "Loader created");
		return loader;
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
		getLoaderManager().restartLoader(0, null, this);
	}
	
}

/*
 *  ADAPTER 
 */

class ResultItemsAdapter extends ArrayAdapter<ArrayList<Product>> {
	private final Activity context;
	private final ArrayList productList;
	
	public ResultItemsAdapter(Activity context, ArrayList productList) {
		super(context, R.layout.searchproduct_results_item, productList);
	    this.context = context;
	    this.productList = productList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("ecoar", "");
		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.searchproduct_results_item, null);
		}
		Product pdata = (Product) this.productList.get(position);
		TextView productName = (TextView) convertView.findViewById(R.id.searchproduct_item_name);
		productName.setText(pdata.productName);
		
		ImageView productImage = (ImageView) convertView.findViewById(R.id.searchproduct_item_image);
		productImage.setImageBitmap(pdata.image);		
		return convertView;
	}
}

/*
 *  LOADER
 */

class ResultProductsLoader extends AsyncTaskLoader<ArrayList<Product>> {
	String query;
	int page;
	
	// Metodo constructor
	public ResultProductsLoader(Context context, String query, int page, ProgressBar progressBar) {
		super(context);
		this.query = query;
		this.page = page;
	}
		
	public ArrayList<Product> loadInBackground() {
		if (query != null) {
			AmazonSearchObtainer dataObtainer = new AmazonSearchObtainer();
			ArrayList<Product> data = dataObtainer.getData(query, page);
			return data;
		}
		return null;
	}
}

