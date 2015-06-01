package com.maxkalavera.ecoar.productinfo;

import java.util.ArrayList;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.home.HomeLastProductsFragmentAdapter;
import com.maxkalavera.utils.InternetStatusChecker;
import com.maxkalavera.utils.database.UserDataDAO;
import com.maxkalavera.utils.database.jsonmodels.CommentariesListModel;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.ProductInfoModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class CommentariesListFragment extends ListFragment implements 
	LoaderManager.LoaderCallbacks<ResponseBundle>,
	View.OnClickListener {
	
	CommentariesListFragmentAdapter adapter;
	ArrayList<CommentModel> valuesList;
	ProductModel product;
	int page; 
	CommentModel deleteComment;
	
	
	public static final int GET_COMMENTLIST = 1;
	public static final int POST_COMMENT = 2;
	public static final int DELETE_COMMENT = 3;
	
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
		this.adapter = new CommentariesListFragmentAdapter(this, this.valuesList, username);
        this.setListAdapter(this.adapter);
        
    }
    
    public void setUp(ProductModel product) {
    	if (product != null) {
    		this.product = product;
    		getLoaderManager().initLoader(GET_COMMENTLIST, null, this);
    		
    		Button sendCommentButton = (Button) getView().findViewById(R.id.productinfo_commentarieslist_sendcomment);
    		sendCommentButton.setOnClickListener(this);
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
    public void deleteComment(int position) {
    	this.deleteComment = this.valuesList.get(position);
    }
    
	/************************************************************
	 * Listener del boton para enviar comentario 
	 ************************************************************/    
	@Override
	public void onClick(View view) {
		enableCommentaryCapability(false);
    	getLoaderManager().initLoader(GET_COMMENTLIST, null, this);
	}
	
	/************************************************************
	 * 
	 ************************************************************/    
	public void enableCommentaryCapability(boolean option) {
		EditText commentEditText = 
				(EditText) getView().findViewById(
						R.id.productinfo_commentarieslist_comment);
		Button sendCommentButton = 
				(Button) getView().findViewById(
						R.id.productinfo_commentarieslist_sendcomment);

		if (option) {
			commentEditText.setFocusable(true);
			commentEditText.setEnabled(true);
			commentEditText.setCursorVisible(true);
			//commentEditText.setKeyListener(null);
			//commentEditText.setBackgroundColor(Color.TRANSPARENT);
			commentEditText.getText().clear();
			
			sendCommentButton.setEnabled(true);			
		} else {
			commentEditText.setFocusable(false);
			commentEditText.setEnabled(false);
			commentEditText.setCursorVisible(false);

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
				paramsBundleGetProductInfo.addURIParam("general_id", this.product.generalId);
				paramsBundleGetProductInfo.addURIParam("page", String.valueOf(this.page));
				
				GetCommentListHTTPLoader getCommentListHTTPLoader = 
						new GetCommentListHTTPLoader(this.getActivity(), paramsBundleGetProductInfo, this.product, this.page);
				getCommentListHTTPLoader.forceLoad();
				return getCommentListHTTPLoader;
				
			case POST_COMMENT:
				if(!InternetStatusChecker.checkInternetStauts(this.getActivity()))
					return null;
				
				BaseActivity baseActivity = (BaseActivity)this.getActivity();
				UserDataDAO userDataDAO = baseActivity.getUserDataManager();
				
				EditText commentEditText = (EditText) getView().findViewById(R.id.productinfo_commentarieslist_comment);
				CommentModel newComennt = 
						new CommentModel(commentEditText.getText().toString(), userDataDAO.getUsername());
				
				RequestParamsBundle paramsBundlePostUserScore = new RequestParamsBundle();
				paramsBundlePostUserScore.addJSONParam("general_id", this.product.generalId);
				paramsBundlePostUserScore.AddJsonModel("comment", newComennt);
				
				PostCommentHTTPLoader postCommentHTTPLoader = 
						new PostCommentHTTPLoader(this.getActivity(), paramsBundlePostUserScore, this.product);
				postCommentHTTPLoader.forceLoad();
				return postCommentHTTPLoader;
				
			case DELETE_COMMENT:
				if(!InternetStatusChecker.checkInternetStauts(this.getActivity()))
					return null;
				
				RequestParamsBundle paramsBundleDeleteComment = new RequestParamsBundle();
				paramsBundleDeleteComment.addJSONParam("id", String.valueOf(this.deleteComment.getServerId()));
				
				DeleteCommentHTTPLoader deleteCommentHTTPLoader = 
				new DeleteCommentHTTPLoader(this.getActivity(), paramsBundleDeleteComment, this.deleteComment);
				deleteCommentHTTPLoader.forceLoad();
				return deleteCommentHTTPLoader;
				
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<ResponseBundle> loader, ResponseBundle loaderRes) {
		switch (loader.getId()) {
			case GET_COMMENTLIST:
				if(loaderRes.getResponseJsonObject() != null) {
					CommentariesListModel commentariesListModel = 
							(CommentariesListModel) loaderRes.getResponseJsonObject();
					if (!commentariesListModel.commentaries.isEmpty()) {
						this.valuesList.addAll(commentariesListModel.commentaries);
						this.adapter.notifyDataSetChanged();
						this.page += 1;
					}
				}
				break;
				
			case POST_COMMENT:
				if(loaderRes.getResponseJsonObject() != null) {
					CommentModel newComennt = 
							(CommentModel) loaderRes.getResponseJsonObject();
					this.valuesList.add(newComennt);
					this.adapter.notifyDataSetChanged();
					enableCommentaryCapability(true);
				}
				break;
			
			case DELETE_COMMENT:
				if(loaderRes.getResponse().isSuccessful()) {
					CommentModel comment = (CommentModel) loaderRes.getResponseJsonObject();
					for(int i = 0; i < this.valuesList.size(); i++){
						if (comment.getId() == this.valuesList.get(i).getId())
							this.valuesList.remove(i);
					}
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
