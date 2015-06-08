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
import android.webkit.CookieSyncManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;



public class CommentariesPrefaceListFragment extends ListFragment implements 
	LoaderManager.LoaderCallbacks<ResponseBundle>, 
	View.OnClickListener
	{
	
	private CommentariesListFragmentAdapter adapter;
	private ArrayList<CommentModel> valuesList;
	
	private ProductModel product; 
	private CommentModel deleteComment;
	
	
	public static final int GET_COMMENTLIST = 1;
	public static final int DELETE_COMMENT = 2;
	
	private static final int MAX_NUMBER_COMMENTS = 3;
	private static final int PAGE = 1;
	public static final int DELAY = 10000;
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
    	this.valuesList = new ArrayList<CommentModel>();
    	this.deleteComment = null;
        
        BaseActivity baseActivity = (BaseActivity) this.getActivity();
        String username = baseActivity.getUserDataManager().getUsername();
		
		this.adapter = new CommentariesListFragmentAdapter(this, this.valuesList, username);
        this.setListAdapter(this.adapter);
        
        this.getListView().setScrollContainer(false);
    }

	/************************************************************
	 * 
	 ************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.productinfo_commentarieslist_preface, container, false);

    	return view;
    }	
    
	@Override
	public void onResume() {
		super.onResume();
	}
    
	/************************************************************
	 * 
	 ************************************************************/
    public void setUp(ProductModel product) {
    	if (product != null) {
    		this.product = product;
    		
    		this.getView().findViewById(R.id.productinfo_commentarieslist_preface_more).setOnClickListener(this);
    		getLoaderManager().initLoader(GET_COMMENTLIST, null, this);
    	}
    }

	/************************************************************
	 * 
	 ************************************************************/    
	@Override
	public void onClick(View view) {
		ProductInfo productInfoActivity = (ProductInfo) this.getActivity();
		new Handler().post(productInfoActivity);
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
	 * Reference "http://www.java2s.com/Code/Android/UI/setListViewHeightBasedOnChildren.htm"
	 ************************************************************/      
	public void setListViewHeightBasedOnChildren() {
    	ListView listView =  this.getListView();

    	ListAdapter listAdapter = listView.getAdapter(); 
    	if (listAdapter == null) {
    		return;
    	}

    	int totalHeight = 0;
    	for (int i = 0; i < listAdapter.getCount(); i++) {
    		View listItem = listAdapter.getView(i, null, listView);
    		listItem.measure(0, 0);
    		totalHeight += listItem.getMeasuredHeight();
    	}

    	ViewGroup.LayoutParams params = listView.getLayoutParams();
    	params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    	listView.setLayoutParams(params);
    	listView.requestLayout();
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
				paramsBundleGetProductInfo.addUriParam("page", String.valueOf(PAGE));
				
				CommentariesListGetCommentListHTTPLoader commentariesListGetCommentListHTTPLoader = 
						new CommentariesListGetCommentListHTTPLoader(this.getActivity(), paramsBundleGetProductInfo, this.product, PAGE);
				commentariesListGetCommentListHTTPLoader.forceLoad();
				return commentariesListGetCommentListHTTPLoader;
				
			case DELETE_COMMENT:
				if(!InternetStatusChecker.checkInternetStauts(this.getActivity()))
					return null;
				
				RequestParamsBundle paramsBundleDeleteComment = new RequestParamsBundle();
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
						this.valuesList.clear();
						for (int i = 0; i < MAX_NUMBER_COMMENTS; i++) {
							this.valuesList.add(commentariesListJsonModel.commentaries.get(i));
						}
						//this.valuesList.addAll(commentariesListJsonModel.commentaries);
						this.adapter.notifyDataSetChanged();
						setListViewHeightBasedOnChildren();
					}
					
				} else {
					// Error al recuperar los datos del servidor
					
					// Vuelve a a activar el listener del scroll 
					// despues de 10s. 
					new Handler().postDelayed(new Runnable(){
						CommentariesPrefaceListFragment commentariesPreface;
						
						public Runnable setParameter(CommentariesPrefaceListFragment commentariesPreface){
							this.commentariesPreface = commentariesPreface;
							return this;
						}
						public void run() {
							commentariesPreface.getLoaderManager().restartLoader(CommentariesPrefaceListFragment.GET_COMMENTLIST, null, commentariesPreface);
						}						
					}.setParameter(this), DELAY);
				}
				
				break;
			
			case DELETE_COMMENT:
				if ( loaderRes.getResponse() != null && loaderRes.getResponse().isSuccessful() && loaderRes.getResponseJsonObject() != null ) { 
					CommentModel comment = (CommentModel) loaderRes.getResponseJsonObject();
					this.valuesList.remove(comment);
					this.adapter.notifyDataSetChanged();
					this.getLoaderManager().restartLoader(GET_COMMENTLIST, null, this);
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
