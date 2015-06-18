package com.maxkalavera.ecoar.brandinfo;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.productmodel.ProductModel;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class CommentariesDialogFragment extends DialogFragment {
	
    //static CommentariesDialogFragment newInstance() {
    //    return new CommentariesDialogFragment();
    //}

    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
		CommentariesListFragment commentariesListFragment = new CommentariesListFragment();
		commentariesListFragment.setArguments(this.getArguments());
		
        getChildFragmentManager().beginTransaction()
        	.add(R.id.productinfo_commentaries_conteiner, commentariesListFragment).commit();
        getChildFragmentManager().executePendingTransactions();
    }
    

    @Override
    public void onDismiss(final DialogInterface dialog) {
    	
		CommentariesPrefaceListFragment commentariesPrefaceFragment = (CommentariesPrefaceListFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(R.id.productinfo_commentaries_preface);
		commentariesPrefaceFragment.getLoaderManager()
			.restartLoader(CommentariesPrefaceListFragment.GET_COMMENTLIST, null, commentariesPrefaceFragment);

    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.productinfo_commentaries_dialog, container, false);
		
		getDialog().setTitle("Commentarios");
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		// Do something else
		return rootView;
		//return inflater.inflate(R.layout.productinfo_commentaries_dialog, null);
	}
}
