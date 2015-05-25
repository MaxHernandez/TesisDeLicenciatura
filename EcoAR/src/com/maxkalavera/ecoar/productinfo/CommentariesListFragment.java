package com.maxkalavera.ecoar.productinfo;

import java.util.ArrayList;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.home.HomeLastProductsFragmentAdapter;
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
	ArrayList<CommentModel> valuesList = new ArrayList<CommentModel>();
	ProductModel product;
	int page = 1; 
	
	
	public static final int GET_COMMENTLIST = 1;
	public static final int POST_COMMENT = 2;
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);        
		this.adapter = new CommentariesListFragmentAdapter(this.getActivity(), this.valuesList);
        this.setListAdapter(this.adapter);
        
		try{
			this.product =  
					(ProductModel) this.getActivity().getIntent().getParcelableExtra("product");
		} catch(Exception e){
			Log.e("ProductInfo_create:", e.toString());
		}
		
        if (this.product != null)
        	getLoaderManager().initLoader(GET_COMMENTLIST, null, this);
    }

	/************************************************************
	 * 
	 ************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.productinfo_commentarieslist, container, false);
    	return view;
    }
    
    public void setUp() {    	
		Button sendCommentButton = (Button) getView().findViewById(R.id.productinfo_commentarieslist_sendcomment);
		sendCommentButton.setOnClickListener(this);
    }

	/************************************************************
	 * Listener del boton para enviar comentario 
	 ************************************************************/    
	@Override
	public void onClick(View view) {
		enableCommentaryCapability(false);
    	getLoaderManager().initLoader(GET_COMMENTLIST, null, this);
	}
	
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
				//ERROR: Aun hace falta agregar paginacion a los parametros
				RequestParamsBundle paramsBundleGetProductInfo = new RequestParamsBundle();
				paramsBundleGetProductInfo.addURIParam("general_id", this.product.generalID);
				paramsBundleGetProductInfo.addURIParam("page", String.valueOf(this.page));
				
				GetCommentListHTTPLoader getCommentListHTTPLoader = 
						new GetCommentListHTTPLoader(this.getActivity(), paramsBundleGetProductInfo, this.product, this.page);
				getCommentListHTTPLoader.forceLoad();
				return getCommentListHTTPLoader;
				
			case POST_COMMENT:
				BaseActivity baseActivity = (BaseActivity)this.getActivity();
				UserDataDAO userDataDAO = baseActivity.getUserDataManager();
				
				EditText commentEditText = (EditText) getView().findViewById(R.id.productinfo_commentarieslist_comment);
				CommentModel newComennt = 
						new CommentModel(commentEditText.getText().toString(), userDataDAO.getUsername());
				
				RequestParamsBundle paramsBundlePostUserScore = new RequestParamsBundle();
				paramsBundlePostUserScore.addJSONParam("general_id", this.product.generalID);
				paramsBundlePostUserScore.AddJsonModel("comment", newComennt);
				
				PostCommentHTTPLoader postCommentHTTPLoader = 
						new PostCommentHTTPLoader(this.getActivity(), paramsBundlePostUserScore, this.product);
				postCommentHTTPLoader.forceLoad();
				return postCommentHTTPLoader;
				
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
				
			default:
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<ResponseBundle> loader) {
	}
    
};
