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
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchProductResults extends ListFragment implements 
	LoaderManager.LoaderCallbacks<ArrayList<Product>> {
			
	ArrayList<Product> itemValues = new ArrayList<Product>();
	ResultItemsAdapter adapter;
	Bundle loaderArgs = new Bundle();
	
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.adapter = new ResultItemsAdapter(getActivity(), this.itemValues);
        setListAdapter(adapter);
		getLoaderManager().initLoader(0, this.loaderArgs, this);
		Log.i("ecoar", "Loader initiated");
    }
	
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showProductInfo(position);
    }

	@Override
	public Loader<ArrayList<Product>> onCreateLoader(int arg0, Bundle args) {
		String query = this.loaderArgs.getString("query");
		ResultProductsLoader loader = new ResultProductsLoader(this.getActivity(), query);
		loader.forceLoad();
		Log.i("ecoar", "Loader created");
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Product>> arg0, ArrayList<Product> loaderRes) {
		Log.i("ecoar", "Load finished");
		if (loaderRes != null) {
			//this.itemValues.addAll(loaderRes);
			this.itemValues = loaderRes;
			this.adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Product>> arg0) {
		Log.i("ecoar", "Loader reset finished");
		
	}
	
	void newSearch(String query){
		this.loaderArgs.putString("query", query);
		getLoaderManager().restartLoader(0, null, this);
		//getLoaderManager().initLoader(0, this.loaderArgs, this);
		Log.i("ecoar", "Loader reseted");
	}
	
	void showProductInfo(int productID) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ProductInfo.class);
        intent.putExtra("productID", productID);
        startActivity(intent);
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
	
	// Metodo constructor
	public ResultProductsLoader(Context context, String query) {
		super(context);
		this.query = query;
	}
	
	public ArrayList<Product> loadInBackground() {
		//String query = this.args.getString("query");
		if (query != null) {
			AmazonSearchObtainer dataObtainer = new AmazonSearchObtainer();
			ArrayList<Product> data = dataObtainer.getData(query);
			return data;
		}
		return null;
	}
}

