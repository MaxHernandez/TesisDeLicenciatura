package com.maxkalavera.ecoar.searchcamera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.LoaderManager.LoaderCallbacks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import com.maxkalavera.ecoar.brandinfo.BrandInfo;
import com.maxkalavera.ecoar.productinfo.ProductInfo;
import com.maxkalavera.ecoar.searchbar.SearchBar;
import com.maxkalavera.utils.ErrorMesages;
import com.maxkalavera.utils.ImageStringConverter;
import com.maxkalavera.utils.LogoutChecker;
import com.maxkalavera.utils.SlideMenuBarHandler;
import com.maxkalavera.utils.database.jsonmodels.SearchCameraResponseJsonModel;
import com.maxkalavera.utils.database.productmodel.BrandModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.Config;


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
    	this.callback.takePictureCallback(data);
    }

};


/*****************************************************************************
 *****************************************************************************
 *
 *
 *
 *****************************************************************************
 ****************************************************************************/
class Preview implements SurfaceHolder.Callback, View.OnClickListener, 
	LoaderManager.LoaderCallbacks<ResponseBundle>,
	Camera.PreviewCallback {
    
	private Context context;
	private SurfaceHolder surfaceHolder;
    private Camera camera;
    private TakePictureCallback takePictureCallback;
	private Button shutterButton;
	private RequestParamsBundle requestParamsBundle; 
	 
	private List<String> barcodeIgnoredList;
	
	// Zbar
	private ImageScanner barCodeScanner;
	
	
	private static final int SEND_IMAGE = 1; 
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
    Preview(Context context, SurfaceView surfaceView) {
    	this.context = context;
    	this.surfaceHolder = surfaceView.getHolder();
    	this.surfaceHolder.addCallback(this);
    	this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    	this.takePictureCallback = new TakePictureCallback(context, this);
    	
    	barCodeScanner = new ImageScanner();
    	barCodeScanner.setConfig(0, Config.X_DENSITY, 3);
    	barCodeScanner.setConfig(0, Config.Y_DENSITY, 3);
    	barCodeScanner.setConfig(Symbol.QRCODE, Config.ENABLE, 0); 
    	
    	this.barcodeIgnoredList = new ArrayList<String>();
    	
    	shutterButton =  
    			(Button) ((FragmentActivity) context).findViewById(R.id.searchcamera_takepicture);
    }
    
    private Context getContext(){
    	return this.context;
    }    
    
	/************************************************************
	 * Send Pictures to the server callback
	 ************************************************************/
    public void onClick(View view) {
    	if (camera == null) return;
    	
    	this.camera.takePicture( null, this.takePictureCallback, this.takePictureCallback);
    	this.shutterButton.setOnClickListener(null);
    	
    }

    public void takePictureCallback(byte[] picture) {
    	Bitmap bitmapPicture
		   = BitmapFactory.decodeByteArray(picture, 0, picture.length);
    	
    	bitmapPicture = this.fixOrientation(bitmapPicture);
    	
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmapPicture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		
		this.requestParamsBundle = new RequestParamsBundle();
		requestParamsBundle.addPart(baos.toByteArray(),
				"image/jpg",
				"product_image",
				"product_image.jpg");
		
		((BaseActivity) this.getContext()).getSupportLoaderManager().restartLoader(SEND_IMAGE, null, this);
		//this.callback.takePictureCallback(bitmapPicture);
		//ImageStringConverter.ArrayToString(picture);
    	
    }
    
    // reference "http://stackoverflow.com/questions/6069122/camera-orientation-issue-in-android"
    public Bitmap fixOrientation(Bitmap bitmap) {
        if (bitmap.getWidth() > bitmap.getHeight()) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return rotatedBitmap;
        }
        
        return bitmap;
    }
    
	/************************************************************
	 * Get frames from the camera callback
	 ************************************************************/
    
	public void onPreviewFrame(byte[] data, Camera camera) {
    	Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getPreviewSize();
        
        Image zbarImage = new Image(size.width, size.height, "Y800");
        zbarImage.setData(data);
        int result = barCodeScanner.scanImage(zbarImage);

        if (result > 0) {
            SymbolSet symbolSet = barCodeScanner.getResults();
            for (Symbol symbol : symbolSet) {
                String barcode = symbol.getData();
                if (barcode != null && !barcode.equals("")) {
                	
                	new AlertDialog.Builder(this.getContext())
                    	.setTitle("Codigo de barras encontrado")
                    	.setMessage(barcode)
                    	.setPositiveButton("Ver",            
                    		new DialogInterface.OnClickListener() {
                    			Context context;
                    			String barcode;
                    		
                    			public DialogInterface.OnClickListener setParams(Context context, String barcode) {
                    				this.context = context;
                    				this.barcode = barcode;
                    				return this;
                    			}
                    		
                    			public void onClick(DialogInterface dialog, int id) {
        					        Intent intent = new Intent();
        					       	intent.setClass(this.context, SearchBar.class);
        					        intent.putExtra("barcode", this.barcode);
        					        this.context.startActivity(intent);
                    			}
                        	}.setParams(this.context, barcode))
                    	.setNeutralButton("Ignorar",
                        		new DialogInterface.OnClickListener() {
                    		
                    		List<String> barcodeIgnoredList;
                    		String barcode;
                    		
                			public DialogInterface.OnClickListener setParams(List<String> barcodeIgnoredList, String barcode) {
                				this.barcodeIgnoredList = barcodeIgnoredList;
                				this.barcode = barcode;
                				return this;
                			}
                		
                			public void onClick(DialogInterface dialog, int id) {
                				this.barcodeIgnoredList.add(barcode);
                			}
                    	}.setParams(barcodeIgnoredList, barcode))
                    	.setNegativeButton("Cancelar", null)
                    	.show();
                	
                	break;
                }
            }
        }
    }
    
	/************************************************************
	 * Camera lifecycle methods
	 ************************************************************/
    
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
            camera.setPreviewCallback(this);
            camera.startPreview();
            shutterButton.setOnClickListener(this);
        }
    }
    
    private void stopPreviewAndFreeCamera() {
        if (this.camera != null) {
        	this.camera.setPreviewCallback(null);
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
	        camera.setPreviewCallback(null);
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
	public void onLoadFinished(Loader<ResponseBundle> loader, ResponseBundle responseBundle) {
		switch(loader.getId()) {
			case SEND_IMAGE:
				
				if (responseBundle.getResponse() != null && responseBundle.getResponse().isSuccessful()) {
					SearchCameraResponseJsonModel searchCamerResponse = 
							(SearchCameraResponseJsonModel) responseBundle.getResponseJsonObject();
				
					if (searchCamerResponse != null) {
						
						if (searchCamerResponse.brand != null) {
					        Intent intent = new Intent();
					       	intent.setClass(this.getContext(), BrandInfo.class);
					        intent.putExtra("brand", searchCamerResponse.brand);
					        this.getContext().startActivity(intent);
							
						} else if (searchCamerResponse.query != null) {							
					        Intent intent = new Intent();
					       	intent.setClass(this.getContext(), SearchBar.class);
					        intent.putExtra("query", searchCamerResponse.query);
					        this.getContext().startActivity(intent);
					        
						} else {
							AlertDialog.Builder dialog = new AlertDialog.Builder(context);
						    dialog.setTitle(
						    		context.getResources().getString(R.string.searchcamera_product_not_found_title));
						    dialog.setMessage(
						    		context.getResources().getString(R.string.searchcamera_product_not_found_message));
						    dialog.setPositiveButton(
						    		context.getResources().getString(R.string.error_afirmative_button),null);
						    dialog.show();
						}
						
					} else {
						ErrorMesages.errorRetrievingJsonData(this.getContext());
					}
				} else {
					ErrorMesages.errorSendingHttpRequest(this.getContext());
					LogoutChecker.checkSessionOnResponse(this.getContext(), responseBundle.getResponse());
				}

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
	
	static {
	    System.loadLibrary("iconv");
	}
	
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
	}
	
	public void startCamera() {
		this.preview.setCamera(null);
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