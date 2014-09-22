package com.maxkalavera.ecoar;

import java.util.ArrayList;

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
	LoaderManager.LoaderCallbacks<String[][]> {
			
	ArrayList<String[]> itemValues = new ArrayList<String[]>();
	ResultItemsAdapter adapter;
	
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.adapter = new ResultItemsAdapter(getActivity(), this.itemValues);
        setListAdapter(adapter);
    }
	
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showProductInfo(position);
    }	
	
	void showProductInfo(int productID) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ProductInfo.class);
        intent.putExtra("productID", productID);
        startActivity(intent);
		
	}
	
	void newSearch(String query){
		getLoaderManager().initLoader(0, null, this);
		Log.i("ecoar", "Loader initiated");
	}

	@Override
	public Loader<String[][]> onCreateLoader(int arg0, Bundle arg1) {
		return new ResultProductsLoader(this.getActivity());
	}

	@Override
	public void onLoadFinished(Loader<String[][]> arg0, String[][] loaderRes) {
		for (int i = 0; i < loaderRes.length; i++){
			this.itemValues.add(loaderRes[i]);
		}
		this.adapter.notifyDataSetChanged();
		Log.i("ecoar", "Loader finished");
	}

	@Override
	public void onLoaderReset(Loader<String[][]> arg0) {
		// TODO Auto-generated method stub
		
	}
}


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


class ResultProductsLoader extends AsyncTaskLoader<String[][]> {
	
	  public ResultProductsLoader(Context context) {
		    super(context);
	  }
	
	  public String[][] loadInBackground() {		  
		return new String[][]{{"Cafe",""},{"Queso", ""}};
	  }
}

