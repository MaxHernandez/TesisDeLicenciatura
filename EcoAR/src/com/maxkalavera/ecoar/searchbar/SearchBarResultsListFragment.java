package com.maxkalavera.ecoar.searchbar;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.id;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.ecoar.login.LoginFragmentHTTPLoader;
import com.maxkalavera.ecoar.productinfo.ProductInfo;
import com.maxkalavera.utils.ErrorMesages;
import com.maxkalavera.utils.InternetStatusChecker;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.searchobtainers.AmazonSearchObtainer;
import com.maxkalavera.utils.searchobtainers.ServerBarcodeObtainer;
import com.maxkalavera.utils.searchobtainers.ServerQueryObtainer;

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
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SearchBarResultsListFragment extends ListFragment implements 
	LoaderManager.LoaderCallbacks<ArrayList<ProductModel>>, 
	OnScrollListener,
	OnClickListener {
			
	public ArrayList<ProductModel> valuesList;
	private SearchBarResultsListFragmentAdapter adapter;
	private ProgressBar progressBar;
	
	private String query;
	private Button startSearchButton;
	
	private int page = 1;
	private boolean scrollListenFlag = true;
	
	public static final String[] OBTAINERS_LIST = new String[]{AmazonSearchObtainer.NAME};
	private String selectedObtainer = null;
	private String selectedOneUseObtainer = null;
	
	public static final int SEND_REQUEST = 1;
	public static final int DOWNLOAD_IMAGES = 2;
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        this.setUp();		
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.searchbar_results, container, false);
    	return view;
    }
    
	/************************************************************
	 * 
	 ************************************************************/
    public void setUp() {
        this.valuesList = new ArrayList<ProductModel>();
		this.startSearchButton = (Button) getView().findViewById(R.id.searchproduct_searchButton);
		this.startSearchButton.setOnClickListener(this);
		
		// El orden es muy importante, el Footer View debe ser agregado antes 
		// que se establezca un adapter en el listFragment.
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View progressBarView = inflater.inflate(R.layout.loading, null);
		this.getListView().addFooterView(progressBarView);
		
		this.adapter = new SearchBarResultsListFragmentAdapter(
				this,
				this.getActivity(), 
				this.valuesList);
        this.setListAdapter(adapter);

		this.progressBar = (ProgressBar) progressBarView.findViewById(R.id.loading_progressbar);
		this.progressBar.setVisibility(View.GONE);
		
		
		// funcionalidad para cambiar de servicio al que se hace la busqueda
        this.selectedObtainer = OBTAINERS_LIST[0];
        
        Intent intent = this.getActivity().getIntent(); 
        String barcode = intent.getStringExtra("barcode");
        String query = intent.getStringExtra("query"); 
        
        if (barcode != null) {
        	this.selectedOneUseObtainer = ServerBarcodeObtainer.NAME; 
        	this.query = barcode;
        	
    		// Es importante desactivar el ScrollListener antes de limpiar la lista del Fragment, 
    		// puesto que se activa automanticamente al limpiar la lista del fragmento y provoca
    		// que se carguen multiples loaders y esto provoca errores.
    		this.getListView().setOnScrollListener(null);
    		this.valuesList.clear();
    		this.adapter.notifyDataSetChanged();
    		this.page = 1;
    		
    		this.loadData();
    		
        } else if (query != null) {
        	this.selectedOneUseObtainer = ServerQueryObtainer.NAME; 
        	this.query = query;
        	
    		// Es importante desactivar el ScrollListener antes de limpiar la lista del Fragment, 
    		// puesto que se activa automanticamente al limpiar la lista del fragmento y provoca
    		// que se carguen multiples loaders y esto provoca errores.
    		this.getListView().setOnScrollListener(null);
    		this.valuesList.clear();
    		this.adapter.notifyDataSetChanged();
    		this.page = 1;
    		
    		this.loadData();
        }
    }
    
	/************************************************************
	 * On Scroll listener, para cargar automaticamente mas 
	 * elementos a la lista.
	 ************************************************************/	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if ( this.scrollListenFlag && (firstVisibleItem+visibleItemCount) >= totalItemCount ) {
			this.scrollListenFlag = false;
			this.getListView().setOnScrollListener(null);
			this.loadData();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollstate) {
	}
	
	/************************************************************
	 * Metodos para realizar busquedas 
	 ************************************************************/
	
	@Override
	public void onClick(View view) {
		this.startSearchButton.setOnClickListener(null);
		newSearch();
	}
	
	public void newSearch(String query){
		if (query == null) {
			EditText queryInputEditText = 
					(EditText) getView().findViewById(R.id.searchproduct_searchTextBar);
			this.query = queryInputEditText.getText().toString();
			if (this.query == null) this.query = "";
		} else
			this.query = query;
		
		if (query == "") return;
		
		this.selectedOneUseObtainer = null;
		// Es importante desactivar el ScrollListener antes de limpiar la lista del Fragment, 
		// puesto que se activa automanticamente al limpiar la lista del fragmento y provoca
		// que se carguen multiples loaders y esto provoca errores.
		this.getListView().setOnScrollListener(null);
		this.valuesList.clear();
		this.adapter.notifyDataSetChanged();
		this.page = 1;
		
		this.loadData();
	}
	
	public void newSearch() {
		this.newSearch(null);
	}
	
	private void loadData(){
		this.progressBar.setVisibility(View.VISIBLE);
		//this.getLoaderManager().destroyLoader(SEND_REQUEST);
		getLoaderManager().restartLoader(SEND_REQUEST, null, this);
	}
    
	/************************************************************
	 * Loaders's methods 
	 ************************************************************/
	@Override
	public Loader<ArrayList<ProductModel>> onCreateLoader(int loaderID, Bundle args) {
		switch (loaderID) {
			case SEND_REQUEST:
				if(!InternetStatusChecker.checkInternetStauts(this.getActivity()))
					return null;
				
				String obtainer = null;
				if (this.selectedOneUseObtainer != null ) obtainer = this.selectedOneUseObtainer;
				else obtainer = this.selectedObtainer;
				
				SearchBarResultsListFragmentHTTPLoader searchBarResultsListFragmentHTTPLoader = 
						new SearchBarResultsListFragmentHTTPLoader(this.getActivity(), this.query, this.page, obtainer);
				
				searchBarResultsListFragmentHTTPLoader.forceLoad();				
				return searchBarResultsListFragmentHTTPLoader;
				
			case DOWNLOAD_IMAGES:
				if(!InternetStatusChecker.checkInternetStauts(this.getActivity()))
					return null;
				
				SearchBarDownloadImagesListFragmentLoader searchBarDownloadImagesListFragmentLoader = 
					new SearchBarDownloadImagesListFragmentLoader(this.getActivity(), this.valuesList);
				searchBarDownloadImagesListFragmentLoader.forceLoad();
				return searchBarDownloadImagesListFragmentLoader;
				
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<ProductModel>> loader, ArrayList<ProductModel> loaderRes) {
		switch (loader.getId()) {
			case SEND_REQUEST:
				if (loaderRes != null && !loaderRes.isEmpty()) {
					this.page += 1;
					
					this.valuesList.addAll(loaderRes);
					this.adapter.notifyDataSetChanged();
					
					this.getListView().setOnScrollListener(this);
					this.scrollListenFlag = true;
					
					getLoaderManager().restartLoader(DOWNLOAD_IMAGES, null, this);
				}
				
				if (loaderRes == null) 
					ErrorMesages.errorRetrievingData(this.getActivity());
				
				this.progressBar.setVisibility(View.GONE);
				this.startSearchButton.setOnClickListener(this);
				
				break;
				
			case DOWNLOAD_IMAGES:
				this.adapter.notifyDataSetChanged();
				break;
				
			default:
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<ProductModel>> arg0) {
	}	
	
};

