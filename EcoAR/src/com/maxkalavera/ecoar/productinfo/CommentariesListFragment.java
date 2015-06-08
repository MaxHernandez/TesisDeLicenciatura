package com.maxkalavera.ecoar.productinfo;

import java.util.ArrayList;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.home.HomeLastProductsFragmentAdapter;
import com.maxkalavera.utils.InternetStatusChecker;
import com.maxkalavera.utils.database.UserDataDAO;
import com.maxkalavera.utils.database.jsonmodels.CommentariesListJsonModel;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.ProductInfoModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class CommentariesListFragment extends ListFragment implements 
	LoaderManager.LoaderCallbacks<ResponseBundle>,
	OnScrollListener,
	View.OnClickListener,
	Runnable {
	
	private CommentariesListFragmentAdapter adapter;
	private ArrayList<CommentModel> valuesList;
	private ProgressBar progressBar;
	private ProductModel product;
	private int page; 
	private CommentModel deleteComment;
	
	
	public static final int GET_COMMENTLIST = 1;
	public static final int POST_COMMENT = 2;
	public static final int DELETE_COMMENT = 3;
	
	public static final int DELAY = 10000;
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
    	this.valuesList = new ArrayList<CommentModel>();
    	this.page = 1;
    	this.deleteComment = null;
        
        BaseActivity baseActivity = (BaseActivity) this.getActivity();
        String username = baseActivity.getUserDataManager().getUsername();
        
		// El orden es muy importante, el Footer View debe ser agregado antes 
		// que se establezca un adapter en el listFragment.
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View progressBarView = inflater.inflate(R.layout.loading, null);
		this.getListView().addFooterView(progressBarView);
		this.progressBar = (ProgressBar) progressBarView.findViewById(R.id.loading_progressbar);
		this.progressBar.setVisibility(View.GONE);
		
		this.adapter = new CommentariesListFragmentAdapter(this, this.valuesList, username);
        this.setListAdapter(this.adapter);

        this.enableCommentCapability(false);
        
		try{
	        Bundle paramsBundle = this.getArguments();
	        ProductModel product = null;
			product =  
					(ProductModel) paramsBundle.getParcelable(ProductInfo.PRODUCT_KEYWORD);
			this.setUp(product);
		} catch(Exception e){
			e.printStackTrace();
		}
    }

	/************************************************************
	 * 
	 ************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.productinfo_commentarieslist, container, false);

    	return view;
    }	
    
	/************************************************************
	 * 
	 ************************************************************/
    public void setUp(ProductModel product) {
    	if (product != null) {
    		this.product = product;
    		
    		Button sendCommentButton = 
    				(Button) getView().findViewById(
    						R.id.productinfo_commentarieslist_sendcomment);
    		sendCommentButton.setOnClickListener(this);
    		
    		getLoaderManager().initLoader(GET_COMMENTLIST, null, this);
    	}
    }
    
	/************************************************************
	 * On Scroll listener, para cargar automaticamente mas 
	 * elementos a la lista.
	 ************************************************************/	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if ( (firstVisibleItem+visibleItemCount) >= totalItemCount ) {
			this.getListView().setOnScrollListener(null);
			getLoaderManager().restartLoader(GET_COMMENTLIST, null, this);
		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollstate) {
	}
	
	@Override
	public void run() {
		this.getListView().setOnScrollListener(this);
	}
	/************************************************************
	 * Listener del boton para enviar comentario 
	 ************************************************************/    
	@Override
	public void onClick(View view) {
		enableCommentCapability(false);
    	getLoaderManager().restartLoader(POST_COMMENT, null, this);
	}
    
	/************************************************************
	 * 
	 ************************************************************/    
    public void deleteComment(int position) {
    	this.deleteComment = this.valuesList.get(position);
    	getLoaderManager().restartLoader(DELETE_COMMENT, null, this);
    }
	
	/************************************************************
	 * 
	 ************************************************************/    
	public void enableCommentCapability(boolean option) {
		EditText commentEditText = 
				(EditText) getView().findViewById(
						R.id.productinfo_commentarieslist_comment);
		Button sendCommentButton = 
				(Button) getView().findViewById(
						R.id.productinfo_commentarieslist_sendcomment);

		if (option) {
			//commentEditText.setFocusable(true);
			commentEditText.setEnabled(true);
			//commentEditText.setCursorVisible(true);
			//commentEditText.setKeyListener(null);
			//commentEditText.setBackgroundColor(Color.TRANSPARENT);
			commentEditText.getText().clear();
			
			sendCommentButton.setEnabled(true);		
		} else {
			//commentEditText.setFocusable(false);
			commentEditText.setEnabled(false);
			//commentEditText.setCursorVisible(false);

			sendCommentButton.setEnabled(false); 
		}
	}
    
	/************************************************************
	 * Loading HTTP requests Methods
	 ************************************************************/
	@Override
	public Loader<ResponseBundle> onCreateLoader(int loaderID, Bundle args) {
		switch (loaderID) {
			case GET_COMMENTLIST:
				if(!InternetStatusChecker.checkInternetStauts(this.getActivity()))
					return null;
				
				RequestParamsBundle paramsBundleGetProductInfo = new RequestParamsBundle();
				paramsBundleGetProductInfo.addUriParam("general_id", this.product.generalId);
				paramsBundleGetProductInfo.addUriParam("page", String.valueOf(this.page));
				
				CommentariesListGetCommentListHTTPLoader commentariesListGetCommentListHTTPLoader = 
						new CommentariesListGetCommentListHTTPLoader(this.getActivity(), paramsBundleGetProductInfo, this.product, this.page);
				commentariesListGetCommentListHTTPLoader.forceLoad();
				return commentariesListGetCommentListHTTPLoader;
				
			case POST_COMMENT:
				if(!InternetStatusChecker.checkInternetStauts(this.getActivity()))
					return null;
				
				BaseActivity baseActivity = (BaseActivity)this.getActivity();
				UserDataDAO userDataDAO = baseActivity.getUserDataManager();
				
				EditText commentEditText = (EditText) getView().findViewById(R.id.productinfo_commentarieslist_comment);
				CommentModel newComennt = 
						new CommentModel(commentEditText.getText().toString(), userDataDAO.getUsername());
				
				RequestParamsBundle paramsBundlePostUserScore = new RequestParamsBundle();
				paramsBundlePostUserScore.addJsonParam("general_id", this.product.generalId);
				paramsBundlePostUserScore.AddJsonModel("comment", newComennt);
				
				CommentariesListPostCommentHTTPLoader commentariesListPostCommentHTTPLoader = 
						new CommentariesListPostCommentHTTPLoader(this.getActivity(), paramsBundlePostUserScore, this.product);
				commentariesListPostCommentHTTPLoader.forceLoad();
				return commentariesListPostCommentHTTPLoader;
				
			case DELETE_COMMENT:
				if(!InternetStatusChecker.checkInternetStauts(this.getActivity()))
					return null;
				
				RequestParamsBundle paramsBundleDeleteComment = new RequestParamsBundle();
				//paramsBundleDeleteComment.addJsonParam("id", String.valueOf(this.deleteComment.id));
				paramsBundleDeleteComment.AddJsonModel("id", this.deleteComment);
				
				CommentariesListDeleteCommentHTTPLoader commentariesListDeleteCommentHTTPLoader = 
				new CommentariesListDeleteCommentHTTPLoader(this.getActivity(), paramsBundleDeleteComment, this.deleteComment);
				commentariesListDeleteCommentHTTPLoader.forceLoad();
				return commentariesListDeleteCommentHTTPLoader;
				
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<ResponseBundle> loader, ResponseBundle loaderRes) {
		switch (loader.getId()) {
			case GET_COMMENTLIST:				
				if (loaderRes.getResponse() != null && loaderRes.getResponse().isSuccessful() && loaderRes.getResponseJsonObject() != null) {
					CommentariesListJsonModel commentariesListJsonModel = 
							(CommentariesListJsonModel) loaderRes.getResponseJsonObject();
					
					if (!commentariesListJsonModel.commentaries.isEmpty()) {
						this.valuesList.addAll(commentariesListJsonModel.commentaries);
						this.adapter.notifyDataSetChanged();
						this.getListView().setOnScrollListener(this);
					} else {
						this.getListView().setOnScrollListener(null);
					}
					this.page += 1;
					
				} else {
					// Error al recuperar los datos del servidor
					
					// Vuelve a a activar el listener del scroll 
					// despues de 10s. 
					new Handler().postDelayed(this, DELAY);
					
					/*
					if (loaderRes.getResponseJsonObject() != null) {
						CommentariesListJsonModel commentariesListJsonModel = 
								(CommentariesListJsonModel) loaderRes.getResponseJsonObject();
						if (!commentariesListJsonModel.commentaries.isEmpty()) {
							this.valuesList.addAll(commentariesListJsonModel.commentaries);
							this.adapter.notifyDataSetChanged();
							this.page += 1;
						}						
					} else {
						// Error al recuperar los datos del servidor
					}
					*/
				}
				
				this.enableCommentCapability(true);
				
				break;
				
			case POST_COMMENT:
				if ( loaderRes.getResponse() != null && loaderRes.getResponse().isSuccessful() && loaderRes.getResponseJsonObject() != null ) {
					CommentModel newComennt = 
							(CommentModel) loaderRes.getResponseJsonObject();
					this.valuesList.add(0, newComennt);
					this.adapter.notifyDataSetChanged();
					this.enableCommentCapability(true);
					this.getListView().smoothScrollToPosition(0);
				} else {
					this.enableCommentCapability(true);
					// Error al mandar la peticion
				}
				break;
			
			case DELETE_COMMENT:
				if ( loaderRes.getResponse() != null && loaderRes.getResponse().isSuccessful() && loaderRes.getResponseJsonObject() != null ) { 
					CommentModel comment = (CommentModel) loaderRes.getResponseJsonObject();
					this.valuesList.remove(comment);
					this.adapter.notifyDataSetChanged();
				} else {
					// Error al enviar el http request 
				}
				break;
				
			default:
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<ResponseBundle> loader) {
	}
    
};
