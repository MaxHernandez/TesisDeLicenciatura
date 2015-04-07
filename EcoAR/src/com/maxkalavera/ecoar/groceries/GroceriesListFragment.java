package com.maxkalavera.ecoar.groceries;

import java.util.ArrayList;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.searchbar.SearchBarResultsListFragmentAdapter;
import com.maxkalavera.utils.database.models.ProductModel;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class GroceriesListFragment extends ListFragment {
	ArrayList<ProductModel> valuesList = new ArrayList<ProductModel>();
	GroceriesListFragmentAdapter adapter;
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
		this.adapter = new GroceriesListFragmentAdapter(getActivity(), this.valuesList);
        setListAdapter(adapter);
    }

	/************************************************************
	 * OnCreateView 
	 ************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.groceries_list, container, false);
    	return view;
    }
    
}
