package com.maxkalavera.ecoar.brandinfo;

import java.util.ArrayList;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.ecoar.searchbar.SearchBarResultsListFragmentHTTPLoader;
import com.maxkalavera.utils.ErrorMesages;
import com.maxkalavera.utils.InternetStatusChecker;
import com.maxkalavera.utils.LogoutChecker;
import com.maxkalavera.utils.SlideMenuBarHandler;
import com.maxkalavera.utils.database.UserSessionDAO;
import com.maxkalavera.utils.database.productmodel.BrandModel;
import com.maxkalavera.utils.database.productmodel.ExtraInfoModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.BitmapFactory;
import 	android.graphics.Bitmap;
import android.util.Log;

public class BrandInfo extends BaseActivity implements 
	LoaderManager.LoaderCallbacks<ResponseBundle>, 
	OnRatingBarChangeListener,
	Runnable
	{
	
	private BrandModel brand;
	private ExtraInfoModel brandInfo;
	private int requestOwnScore = -1;
	
	private final static int GET_PRODUCT_INFO = 1;
	private final static int POST_USER_SCORE = 2;
	
	public static final String BRAND_KEYWORD = "brand";
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.productinfo);
		
		this.brand = null;
		this.brandInfo = null;
		
		try{
			this.brand =  
					(BrandModel) getIntent().getParcelableExtra(BRAND_KEYWORD);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		//setUp(); // ELIMINAR
		if (this.brand != null)
			getSupportLoaderManager().initLoader(GET_PRODUCT_INFO, null, this);
	}
	
	
	
	/************************************************************
	 * 
	 ************************************************************/
	public void setUp() {
		if (this.brand == null)
			return;
		
		// profuctinfo_basicinfo.xml
		((TextView) findViewById(R.id.productinfo_productname))
			.setText(brand.name);
		((TextView) findViewById(R.id.productinfo_productdescription))
			.setText(brand.description);
		((ImageView) findViewById(R.id.productinfo_productimage))
			.setImageBitmap(brand.image);
		
		((TextView) findViewById(R.id.productinfo_shopingservice))
		.setText(this.getResources().getString(R.string.webservice_name));
		
		((TextView) findViewById(R.id.productinfo_referenceurl))
		.setText(brand.webpage);
			
		if (this.brandInfo == null)
			return;
		
		RatingBar ratingBar = (RatingBar) findViewById(R.id.productinfo_ratingbar);
		
		// productinfo_ecologicalscore.xml
		((TextView) findViewById(R.id.productinfo_ecologicalscore))
			.setText(String.valueOf(this.brandInfo.ecological_score));

		if (this.brandInfo.usersScore == null)
			return;
		
		// productinfo_userscore.xml
		((TextView) findViewById(R.id.productinfo_usersscore))
			.setText(String.valueOf(this.brandInfo.usersScore.users_score));
		
		if (this.brandInfo.usersScore.own_score != -1) {
			ratingBar.setRating(this.brandInfo.usersScore.own_score);
			ratingBar.setOnRatingBarChangeListener(null);
		} else {
			ratingBar.setOnRatingBarChangeListener(this);
		}
	}
	
	/************************************************************
	 * Access to the Product reference URL
	 ************************************************************/
	public void showProductReferenceURL() {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(this.brand.webpage));
			startActivity(intent);
		} catch(Exception e){
			Log.e("EcoAr:", e.toString());
		}
	}

	
	/************************************************************
	 * RatingBar listener
	 ************************************************************/
	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
		UserSessionDAO userSessionDAO = new UserSessionDAO(this);
		if (userSessionDAO.checkSessionStatus()) {
			RatingBar ratingBarView = (RatingBar) findViewById(R.id.productinfo_ratingbar);
			ratingBarView.setOnRatingBarChangeListener(null);
		
			this.requestOwnScore = (int) rating;
			getSupportLoaderManager().initLoader(POST_USER_SCORE, null, this);
		} else {
			findViewById(R.id.productinfo_ratingbar_not_session).setVisibility(View.VISIBLE);
		}
	}	
	
	/************************************************************
	 * Run
	 ************************************************************/
    public void run() {
		//CommentariesDialogFragment commentariesDialogFragment = new CommentariesDialogFragment();
    	CommentariesDialogFragment commentariesDialogFragment = new CommentariesDialogFragment();
    	commentariesDialogFragment.setRetainInstance(true);

    	Bundle paramsBundle = new Bundle();
    	paramsBundle.putParcelable(BRAND_KEYWORD, this.brand);
    	
    	commentariesDialogFragment.setArguments(paramsBundle);
		commentariesDialogFragment.show(getSupportFragmentManager(), 
				getResources().getString(R.string.productinfo_commentarieslist_title));
    }
	
	/************************************************************
	 * Loading HTTP requests Methods
	 ************************************************************/
	@Override
	public Loader<ResponseBundle> onCreateLoader(int loaderID, Bundle args) {
		switch (loaderID) {
			case GET_PRODUCT_INFO:
				if(!InternetStatusChecker.checkInternetStauts(this))
					return null;
				
				RequestParamsBundle paramsBundleGetProductInfo = new RequestParamsBundle();
				paramsBundleGetProductInfo.addUriParam("id", String.valueOf(this.brand.id));
				BrandInfoGetInfoHTTPLoader brandInfoGetInfoHTTPLoader = 
						new BrandInfoGetInfoHTTPLoader(this, paramsBundleGetProductInfo, this.brand);
				
				brandInfoGetInfoHTTPLoader.forceLoad();
				return brandInfoGetInfoHTTPLoader;
				
			case POST_USER_SCORE:
				if(!InternetStatusChecker.checkInternetStauts(this))
					return null;
				
				RequestParamsBundle paramsBundlePostUserScore = new RequestParamsBundle();
				paramsBundlePostUserScore.addJsonParam("id", String.valueOf(this.brand.id));
				paramsBundlePostUserScore.addJsonParam("own_score", String.valueOf(this.requestOwnScore));
				BrandInfoPostUserScoreHTTPLoader brandInfoPostUserScoreHTTPLoader = 
						new BrandInfoPostUserScoreHTTPLoader(this, paramsBundlePostUserScore, this.brand);
				brandInfoPostUserScoreHTTPLoader.forceLoad();
				return brandInfoPostUserScoreHTTPLoader;
				
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<ResponseBundle> loader, ResponseBundle responseBundle) {
		switch (loader.getId()) {
			case GET_PRODUCT_INFO:
				if (responseBundle.getResponse() != null && responseBundle.getResponse().isSuccessful()) {
						if (responseBundle.getResponseJsonObject() != null) {
							this.brandInfo = 
									(ExtraInfoModel) responseBundle.getResponseJsonObject();
						} else {
							ErrorMesages.errorRetrievingJsonData(this);
						}
				} else {
					if (responseBundle.getResponseJsonObject() != null) {
						this.brandInfo = 
							(ExtraInfoModel) responseBundle.getResponseJsonObject();
					} else {
						ErrorMesages.errorSendingHttpRequest(this);
						LogoutChecker.checkSessionOnResponse(this, responseBundle.getResponse());
					}
				}
				
				this.setUp();
				
				CommentariesPrefaceListFragment commentariesPrefaceFragment = (CommentariesPrefaceListFragment) this.getSupportFragmentManager().
						findFragmentById(R.id.productinfo_commentaries_preface);
				commentariesPrefaceFragment.setUp(this.brand);
				
				break;
				
			case POST_USER_SCORE:
				if (responseBundle.getResponse() != null && 
					responseBundle.getResponse().isSuccessful() && 
					responseBundle.getResponseJsonObject() != null) {
					
					this.brandInfo.usersScore = 
							(UsersScoreModel) responseBundle.getResponseJsonObject();
					
					TextView ecologicalScore = (TextView) findViewById(R.id.productinfo_ecologicalscore);
					ecologicalScore.setText(String.valueOf(this.brandInfo.ecological_score));
					
					TextView usersScore = (TextView) findViewById(R.id.productinfo_usersscore);
					usersScore.setText(String.valueOf(this.brandInfo.usersScore.users_score));
					
					RatingBar ratingBar = (RatingBar) findViewById(R.id.productinfo_ratingbar);
					ratingBar.setRating(this.brandInfo.usersScore.own_score);
					
					ratingBar.setOnRatingBarChangeListener(null);
				} else {
					RatingBar ratingBar = (RatingBar) findViewById(R.id.productinfo_ratingbar);
					ratingBar.setRating(0.0f);
					ratingBar.setOnRatingBarChangeListener(this);
					
					ErrorMesages.errorRetrievingData(this);
					LogoutChecker.checkSessionOnResponse(this, responseBundle.getResponse());
				}
				break;
				
			default:
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<ResponseBundle> loader) {
	}
	
}