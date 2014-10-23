package com.maxkalavera.ecoar.home;

import com.maxkalavera.ecoar.productinfo.ProductInfo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

public class HomeLastProductsFragment extends ListFragment implements LoaderCallbacks<Cursor> {

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)  {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ProductInfo.class);
        intent.putExtra("index", position);
        startActivity(intent);
    }

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new HomeLastProductsFragmentLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {		
	}

	
}
