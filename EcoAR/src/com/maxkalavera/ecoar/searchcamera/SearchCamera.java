package com.maxkalavera.ecoar.searchcamera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.LoaderManager.LoaderCallbacks;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.ecoar.productinfo.ProductInfo;
import com.maxkalavera.ecoar.searchbar.SearchBar;
import com.maxkalavera.utils.ImageStringConverter;
import com.maxkalavera.utils.SlideMenuBarHandler;
import com.maxkalavera.utils.database.jsonmodels.SearchCameraResponseJsonModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

class TakePictureCallback implements Camera.PictureCallback {
	Context context;
	Preview callback;
	
	public TakePictureCallback(Context context, Preview callback) {
		this.context = context;
		this.callback = callback;
	}
	
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
    	if (camera == null || data == null ||data.length == 0) return;
    	Log.i("TakePictureCallback", "PictureTaken");
    	this.callback.takePictureCallback(data);
    }

};


/*****************************************************************************
 * 
 * ***************************************************************************/
class Preview implements SurfaceHolder.Callback, View.OnClickListener, 
	LoaderManager.LoaderCallbacks<ResponseBundle> {
    
	private Context context;
	private SurfaceHolder surfaceHolder;
    private Camera camera;
    private TakePictureCallback takePictureCallback;
	private Button shutterButton;
	private RequestParamsBundle requestParamsBundle; 
	
	private static final int SEND_IMAGE = 1; 
	
    Preview(Context context, SurfaceView surfaceView) {
    	this.context = context;
    	this.surfaceHolder = surfaceView.getHolder();
    	this.surfaceHolder.addCallback(this);
    	this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    	this.takePictureCallback = new TakePictureCallback(context, this);
    	
    	shutterButton =  
    			(Button) ((FragmentActivity) context).findViewById(R.id.searchcamera_takepicture);
    	shutterButton.setOnClickListener(this);
    }
    
    private Context getContext(){
    	return this.context;
    }
        
    public void onClick(View view) {
    	if (camera == null) return;
    	
    	this.camera.takePicture( null, this.takePictureCallback, this.takePictureCallback);
    	this.shutterButton.setOnClickListener(null);
    	
    }

    public void takePictureCallback(byte[] picture) {
    	Bitmap bitmapPicture
		   = BitmapFactory.decodeByteArray(picture, 0, picture.length);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmapPicture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		
		this.requestParamsBundle = new RequestParamsBundle();
		requestParamsBundle.addPart(baos.toByteArray(),
				"image/jpg",
				"product_image",
				"product_image.png");
		
		((BaseActivity) this.getContext()).getSupportLoaderManager().restartLoader(SEND_IMAGE, null, this);
		//this.callback.takePictureCallback(bitmapPicture);
		//ImageStringConverter.ArrayToString(picture);
    	
    }
    
    public void setCamera(Camera camera) {
        if (this.camera == camera) { return; }
        this.stopPreviewAndFreeCamera();
        this.camera = camera;
        
        if (camera != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();   
        }
    }
    
    private void stopPreviewAndFreeCamera() {
        if (this.camera != null) {
        	this.camera.stopPreview();        
        	this.camera.release();
        	this.camera = null;
        }
    }

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}
    
	@Override
	public void surfaceChanged(SurfaceHolder holder, 
			int format, 
			int width,
			int height) {
		if (camera != null) {
			camera.startPreview();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	    if (camera != null) {
	        camera.stopPreview();
	    }
	}

	/************************************************************
	 * Loaders
	 ************************************************************/
	@Override
	public Loader<ResponseBundle> onCreateLoader(int id, Bundle args) {
		switch(id) {
			case SEND_IMAGE:
				SendImageHTTPLoader sendImageLoader
				= new SendImageHTTPLoader(this.getContext(), this.requestParamsBundle);
				sendImageLoader.forceLoad();
				return sendImageLoader;
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<ResponseBundle> loader, ResponseBundle response) {
		switch(loader.getId()) {
			case SEND_IMAGE:
				
				/*
				SearchCameraResponseJsonModel searchCamerResponse = 
							(SearchCameraResponseJsonModel) response.getResponseJsonObject();
				
				if (searchCamerResponse != null) {
					if (searchCamerResponse.success) {
				        Intent intent = new Intent();
				        intent.setClass(this.getContext(), SearchBar.class);
				        intent.putExtra("query", searchCamerResponse.query);
				        this.getContext().startActivity(intent);
					} else {
						// mostrar que no se encontro nada en la imagen
					}
				} else {
					// error al enviar el paquete
				}
				*/
				((SearchCamera) this.getContext()).startCamera();
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<ResponseBundle> loader) {
		
	}
};

/*****************************************************************************
 * 
 * ***************************************************************************/
public class SearchCamera extends BaseActivity implements
	LoaderManager.LoaderCallbacks<Camera> {
	
    private Camera camera;
    private Preview preview; 
    
	/************************************************************
	 * Constructor Method
	 ************************************************************/
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.searchcamera);	
		setUp();
	}
	
	public void setUp() {
		this.preview = new Preview(this,
        		(SurfaceView) findViewById(R.id.searchcamera_surface));
		//this.startCamera();
	}
	
	public void startCamera() {
		this.preview.setCamera(null);
		Log.i("StartCamera", "----->B");
		this.getSupportLoaderManager().restartLoader(
				CameraOptionsSearchCameraLoader.GET_CAMERA, null, this);
	}
	
	public void releaseCamera() {
	    this.preview.setCamera(null);
		this.getSupportLoaderManager().restartLoader(
				CameraOptionsSearchCameraLoader.RELEASE_CAMERA, null, this);
	}

	/************************************************************
	 * Loaders
	 ************************************************************/
	@Override
	public Loader<Camera> onCreateLoader(int id, Bundle args) {
		switch(id) {
			case CameraOptionsSearchCameraLoader.GET_CAMERA:
				CameraOptionsSearchCameraLoader getCameraLoader
				= new CameraOptionsSearchCameraLoader(this, this.camera, 
						CameraOptionsSearchCameraLoader.GET_CAMERA);
				getCameraLoader.forceLoad();
				return getCameraLoader;
				
			case CameraOptionsSearchCameraLoader.RELEASE_CAMERA:
				CameraOptionsSearchCameraLoader releaseCameraLoader
				= new CameraOptionsSearchCameraLoader(this, this.camera, 
						CameraOptionsSearchCameraLoader.RELEASE_CAMERA);
				releaseCameraLoader.forceLoad();
				return releaseCameraLoader;
				
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Camera> loader, Camera camera) {
		switch(loader.getId()) {
			case CameraOptionsSearchCameraLoader.GET_CAMERA:
				if (camera != null) {
					this.camera = camera;
					this.preview.setCamera(this.camera);
				}
				break;
			
			case CameraOptionsSearchCameraLoader.RELEASE_CAMERA:
				this.camera = camera;
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Camera> loader) {
		
	}
		
	@Override
	public void onResume() {
		super.onResume();
		this.startCamera();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		this.releaseCamera();
	}	
	
};