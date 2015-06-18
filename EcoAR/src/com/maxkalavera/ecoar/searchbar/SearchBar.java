package com.maxkalavera.ecoar.searchbar;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import android.os.Bundle;


public class SearchBar extends BaseActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.searchbar);
		
		String query = getIntent().getStringExtra("query");
		if (query != null) {
			SearchBarResultsListFragment searchBarResultsListFragment = 
				(SearchBarResultsListFragment) this.getSupportFragmentManager().findFragmentById(R.id.searchProductResultsFragment);
			searchBarResultsListFragment.newSearch(query);
		}
	}

}