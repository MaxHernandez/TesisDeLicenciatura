package com.maxkalavera.ecoar.productinfo;

import java.util.ArrayList;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.ecoar.searchbar.SearchBarResultsListFragmentHTTPLoader;
import com.maxkalavera.utils.InternetStatusChecker;
import com.maxkalavera.utils.SlideMenuBarHandler;
import com.maxkalavera.utils.database.productmodel.ProductInfoModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

import android.net.Uri;
import android.os.Bundle;
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

public class ProductInfo extends BaseActivity implements 
	LoaderManager.LoaderCallbacks<ResponseBundle>, 
	OnRatingBarChangeListener{
	
	private ProductModel product;
	private ProductInfoModel productInfo;
	
	private final static int GET_PRODUCT_INFO = 1;
	private final static int POST_USER_SCORE = 2;
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.productinfo);
		
		this.product = null;
		this.productInfo = null;
		
		try{
			this.product =  
					(ProductModel) getIntent().getParcelableExtra("product");
		} catch(Exception e){
			Log.e("EcoAr:", e.toString());
		}
		
		if (this.product != null)
			getSupportLoaderManager().initLoader(GET_PRODUCT_INFO, null, this);
	}
	
	
	
	/************************************************************
	 * 
	 ************************************************************/
	public void setUp() {
		if (this.product == null)
			return;
		
		// profuctinfo_basicinfo.xml
		((TextView) findViewById(R.id.productinfo_productname))
			.setText(product.name);
		((TextView) findViewById(R.id.productinfo_productdescription))
			.setText(product.description);
		((ImageView) findViewById(R.id.productinfo_productimage))
			.setImageBitmap(product.image);
		((TextView) findViewById(R.id.productinfo_shopingservice))
		.setText(product.shopingService);
		((TextView) findViewById(R.id.productinfo_referenceurl))
		.setText(product.url);
		
		if (this.productInfo == null)
			return;
		
		// productinfo_ecologicalscore.xml
		((TextView) findViewById(R.id.productinfo_ecologicalscore))
			.setText(String.valueOf(this.productInfo.ecologicalScore));
		
		// productinfo_userscore.xml
		((TextView) findViewById(R.id.productinfo_usersscore))
			.setText(String.valueOf(this.productInfo.usersScore.usersScore));
		
		RatingBar ratingBar = (RatingBar) findViewById(R.id.productinfo_ratingbar);
		
		if (this.productInfo.usersScore.ownScore == null) {
			ratingBar.setOnRatingBarChangeListener(this);
		} else {
			ratingBar.setRating(this.productInfo.usersScore.ownScore);
			
			//TextView ratingBarText = (TextView) findViewById(R.id.productinfo_ratingbartext);
			//ratingBarText.setText(
			//		String.valueOf(this.productInfo.usersScore.ownScore.intValue())+"/5");
		}
		
	}
	
	/************************************************************
	 * Access to the Product reference URL
	 ************************************************************/
	public void showProductReferenceURL() {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(this.product.url));
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
		this.productInfo.usersScore.ownScore = rating;
		getSupportLoaderManager().initLoader(POST_USER_SCORE, null, this);
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
				paramsBundleGetProductInfo.addURIParam("general_id", this.product.generalId);
				GetProductInfoHTTPLoader getProductInfoHTTPLoader = 
						new GetProductInfoHTTPLoader(this, paramsBundleGetProductInfo, this.product);
				getProductInfoHTTPLoader.forceLoad();
				return getProductInfoHTTPLoader;
				
			case POST_USER_SCORE:
				if(!InternetStatusChecker.checkInternetStauts(this))
					return null;
				
				RequestParamsBundle paramsBundlePostUserScore = new RequestParamsBundle();
				paramsBundlePostUserScore.addJSONParam("general_id", this.product.generalId);
				paramsBundlePostUserScore.addJSONParam("own_score", this.productInfo.usersScore.ownScore.toString());
				PostUserScoreHTTPLoader postUserScoreHTTPLoader = new PostUserScoreHTTPLoader(this, paramsBundlePostUserScore, this.product);
				postUserScoreHTTPLoader.forceLoad();
				return postUserScoreHTTPLoader;
				
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<ResponseBundle> loader, ResponseBundle responseBundle) {
		switch (loader.getId()) {
			case GET_PRODUCT_INFO:
				if (responseBundle.getResponse().isSuccessful()) {
					CommentariesListFragment commentariesFragment = (CommentariesListFragment) this.getSupportFragmentManager().
							findFragmentById(R.id.productinfo_commentaries);
					commentariesFragment.setUp(this.product);
					
					if (responseBundle.getResponseJsonObject() != null) {
						this.productInfo = 
								(ProductInfoModel) responseBundle.getResponseJsonObject();
						this.setUp();
					} else {
						RatingBar ratingBar = (RatingBar) findViewById(R.id.productinfo_ratingbar);
						ratingBar.setOnRatingBarChangeListener(null);
					}
				}
				break;
				
			case POST_USER_SCORE:
				if (responseBundle.getResponse().isSuccessful() && 
					responseBundle.getResponseJsonObject() != null) {
					this.productInfo.usersScore = 
							(UsersScoreModel) responseBundle.getResponseJsonObject();
					
					RatingBar ratingBar = (RatingBar) findViewById(R.id.productinfo_ratingbar);
					ratingBar.setRating(this.productInfo.usersScore.ownScore);
					
					//TextView ratingBarText = (TextView) findViewById(R.id.productinfo_ratingbartext);
					//ratingBarText.setText(String.valueOf(this.productInfo.usersScore.ownScore.intValue())+"/5");
					
					((TextView) findViewById(R.id.productinfo_usersscore))
						.setText(String.valueOf(this.productInfo.usersScore.usersScore));
					ratingBar.setOnRatingBarChangeListener(null);
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