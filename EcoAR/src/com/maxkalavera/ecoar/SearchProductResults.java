package com.maxkalavera.ecoar;

import java.io.IOException;
import java.util.ArrayList;

import com.maxkalavera.utils.HTTPRequest;
import com.maxkalavera.utils.searchobtainers.AmazonSearchObtainer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

public class SearchProductResults extends ListFragment implements 
	LoaderManager.LoaderCallbacks<ArrayList<String[]>> {
			
	ArrayList<String[]> itemValues = new ArrayList<String[]>();
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
	public Loader<ArrayList<String[]>> onCreateLoader(int arg0, Bundle args) {
		String query = this.loaderArgs.getString("query");
		ResultProductsLoader loader = new ResultProductsLoader(this.getActivity(), query);
		loader.forceLoad();
		Log.i("ecoar", "Loader created");
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<String[]>> arg0, ArrayList<String[]> loaderRes) {
		Log.i("ecoar", "Load finished");
		if (loaderRes != null) {
			this.itemValues.addAll(loaderRes);
			Log.i("ecoar", this.itemValues.get(0)[0]);
			this.adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<String[]>> arg0) {
		Log.i("ecoar", "Loader reset finished");
		
	}
	
	void newSearch(String query){
		this.loaderArgs.putString("query", query);
		getLoaderManager().restartLoader(0, null, this);
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

class ResultItemsAdapter extends ArrayAdapter<ArrayList<String[]>> {
	private final Activity context;
	private final ArrayList values;
	
	public ResultItemsAdapter(Activity context, ArrayList values) {
		super(context, R.layout.searchproduct_results_item, values);
	    this.context = context;
	    this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.searchproduct_results_item, null);
		}		
		String[] itemValArray = (String[]) this.values.get(position);
		
		TextView productName = (TextView) convertView.findViewById(R.id.searchproduct_item_name);
		productName.setText(itemValArray[0]);
		
		return convertView;
	}
}

/*
 *  LOADER
 */

class ResultProductsLoader extends AsyncTaskLoader<ArrayList<String[]>> {
	String query;
	
	// Metodo constructor
	public ResultProductsLoader(Context context, String query) {
		super(context);
		this.query = query;
	}
	
	public ArrayList<String[]> loadInBackground() {
		//String query = this.args.getString("query");
		if (query != null) {
			AmazonSearchObtainer dataObtainer = new AmazonSearchObtainer();
			ArrayList<String[]> data = dataObtainer.getData(query);
			return data;
		}
		return null;
	}
}

