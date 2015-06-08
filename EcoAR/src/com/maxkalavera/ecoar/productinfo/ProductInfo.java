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

public class ProductInfo extends BaseActivity implements 
	LoaderManager.LoaderCallbacks<ResponseBundle>, 
	OnRatingBarChangeListener,
	Runnable
	{
	
	private ProductModel product;
	private ProductInfoModel productInfo;
	private int requestOwnScore = -1;
	
	private final static int GET_PRODUCT_INFO = 1;
	private final static int POST_USER_SCORE = 2;
	
	public static final String PRODUCT_KEYWORD = "product";
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.productinfo);
		
		this.product = null;
		this.productInfo = null;
		
		try{
			this.product =  
					(ProductModel) getIntent().getParcelableExtra(PRODUCT_KEYWORD);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		//setUp(); // ELIMINAR
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
		
		RatingBar ratingBar = (RatingBar) findViewById(R.id.productinfo_ratingbar);
		
		// productinfo_ecologicalscore.xml
		((TextView) findViewById(R.id.productinfo_ecologicalscore))
			.setText(String.valueOf(this.productInfo.ecological_score));

		if (this.productInfo.usersScore == null)
			return;
		
		// productinfo_userscore.xml
		((TextView) findViewById(R.id.productinfo_usersscore))
			.setText(String.valueOf(this.productInfo.usersScore.users_score));
		
		if (this.productInfo.usersScore.own_score != -1)
			return;
		
		ratingBar.setRating(this.productInfo.usersScore.own_score);
		ratingBar.setOnRatingBarChangeListener(null);
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
		RatingBar ratingBarView = (RatingBar) findViewById(R.id.productinfo_ratingbar);
		ratingBarView.setOnRatingBarChangeListener(null);
		
		this.requestOwnScore = (int) rating;
		getSupportLoaderManager().initLoader(POST_USER_SCORE, null, this);
	}	
	
	/************************************************************
	 * Run
	 ************************************************************/
    public void run() {
		//CommentariesDialogFragment commentariesDialogFragment = new CommentariesDialogFragment();
    	CommentariesDialogFragment commentariesDialogFragment = new CommentariesDialogFragment();
    	commentariesDialogFragment.setRetainInstance(true);

    	Bundle paramsBundle = new Bundle();
    	paramsBundle.putParcelable(PRODUCT_KEYWORD, this.product);
    	
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
				paramsBundleGetProductInfo.addUriParam("general_id", this.product.generalId);
				ProductInfoGetInfoHTTPLoader productInfoGetInfoHTTPLoader = 
						new ProductInfoGetInfoHTTPLoader(this, paramsBundleGetProductInfo, this.product);
				
				productInfoGetInfoHTTPLoader.forceLoad();
				return productInfoGetInfoHTTPLoader;
				
			case POST_USER_SCORE:
				if(!InternetStatusChecker.checkInternetStauts(this))
					return null;
				
				RequestParamsBundle paramsBundlePostUserScore = new RequestParamsBundle();
				paramsBundlePostUserScore.addJsonParam("general_id", this.product.generalId);
				paramsBundlePostUserScore.addJsonParam("own_score", String.valueOf(this.requestOwnScore));
				ProductInfoPostUserScoreHTTPLoader productInfoPostUserScoreHTTPLoader = new ProductInfoPostUserScoreHTTPLoader(this, paramsBundlePostUserScore, this.product);
				productInfoPostUserScoreHTTPLoader.forceLoad();
				return productInfoPostUserScoreHTTPLoader;
				
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
							this.productInfo = 
									(ProductInfoModel) responseBundle.getResponseJsonObject();
						} else {
							// Error al deserializar el json
						}
				} else {
					if (responseBundle.getResponseJsonObject() != null) {
						this.productInfo = 
							(ProductInfoModel) responseBundle.getResponseJsonObject();
					} else {
						// Error al mandar la petición
					}
				}
				
				this.setUp();
				
				//CommentariesListFragment commentariesFragment = (CommentariesListFragment) this.getSupportFragmentManager().
				//		findFragmentById(R.id.productinfo_commentaries);
				//commentariesFragment.setUp(this.product); //ERROR, Descomentar esta linea cuando sea necesario
				
				CommentariesPrefaceListFragment commentariesPrefaceFragment = (CommentariesPrefaceListFragment) this.getSupportFragmentManager().
						findFragmentById(R.id.productinfo_commentaries_preface);
				commentariesPrefaceFragment.setUp(this.product); //ERROR, Descomentar esta linea cuando sea necesario
				
			    //new Handler().post(this);
				
				
				break;
				
			case POST_USER_SCORE:
				if (responseBundle.getResponse() != null && 
					responseBundle.getResponse().isSuccessful() && 
					responseBundle.getResponseJsonObject() != null) {
					
					this.productInfo.usersScore = 
							(UsersScoreModel) responseBundle.getResponseJsonObject();
					
					TextView ecologicalScore = (TextView) findViewById(R.id.productinfo_ecologicalscore);
					ecologicalScore.setText(String.valueOf(this.productInfo.ecological_score));
					
					TextView usersScore = (TextView) findViewById(R.id.productinfo_usersscore);
					usersScore.setText(String.valueOf(this.productInfo.usersScore.users_score));
					
					RatingBar ratingBar = (RatingBar) findViewById(R.id.productinfo_ratingbar);
					ratingBar.setRating(this.productInfo.usersScore.own_score);
					
					ratingBar.setOnRatingBarChangeListener(null);
				} else {
					RatingBar ratingBar = (RatingBar) findViewById(R.id.productinfo_ratingbar);
					ratingBar.setRating(0.0f);
					ratingBar.setOnRatingBarChangeListener(this);
					
					// Error al recuperar la informacion del servidor.
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