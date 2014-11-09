package com.maxkalavera.ecoar.searchcamera;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.utils.SlideMenuBarHandler;

public class SearchCamera extends BaseActivity implements SurfaceHolder.Callback {
    private Camera mCamera;
    private CameraPreview mPreview;
    private Button takePictureButton;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.searchcamera);		
	    this.surfaceView = (SurfaceView) findViewById(R.id.searchcamera_surface);
	    this.surfaceHolder = surfaceView.getHolder();
	    this.surfaceHolder.addCallback(this);
	    this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    this.takePictureButton = (Button) findViewById(R.id.searchcamera_takepicture);
	}
	
	public void startCamera () {
	    try {
	    	this.mCamera = Camera.open(0);
            Camera.Parameters camParam = this.mCamera.getParameters();
            //camParam.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            camParam.setZoom(0);
            this.setCameraDisplayOrientation(this, 0, this.mCamera);
            this.mCamera.setParameters(camParam);
            
	    	if (this.mCamera != null) {
	    		this.mCamera.setPreviewDisplay(this.surfaceHolder);
	    		this.mCamera.startPreview();
	    	}
	    } catch (IOException e) {
	        Log.e("SearchCamera_startCamera", e.getMessage());
	    }
	    setTakePictureButton(true);
	}
	
	public void setTakePictureButton(boolean conf) {
		if (!conf) {
		    this.takePictureButton.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v) {
		    		mCamera.takePicture(null, null, jpgPictureCallback);				
				}
		    });
		} else {
			this.takePictureButton.setOnClickListener(null);
		}
	}
	
	public void stopCamera() {
		this.mCamera.stopPreview();
		this.mCamera.setPreviewCallback(null);
		this.mCamera.release();
	}
	
	PictureCallback jpgPictureCallback = new PictureCallback(){
		 @Override
		 public void onPictureTaken(byte[] data, Camera arg1) {
			  Bitmap bitmapPicture
			   = BitmapFactory.decodeByteArray(data, 0, data.length);
			  stopCamera();
			  setTakePictureButton(false);
		 }};
	
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		startCamera();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}
	
	 public static void setCameraDisplayOrientation(Activity activity,
	         int cameraId, android.hardware.Camera camera) {
	     android.hardware.Camera.CameraInfo info =
	             new android.hardware.Camera.CameraInfo();
	     android.hardware.Camera.getCameraInfo(cameraId, info);
	     int rotation = activity.getWindowManager().getDefaultDisplay()
	             .getRotation();
	     int degrees = 0;
	     switch (rotation) {
	         case Surface.ROTATION_0: degrees = 0; break;
	         case Surface.ROTATION_90: degrees = 90; break;
	         case Surface.ROTATION_180: degrees = 180; break;
	         case Surface.ROTATION_270: degrees = 270; break;
	     }

	     int result;
	     if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	         result = (info.orientation + degrees) % 360;
	         result = (360 - result) % 360;  // compensate the mirror
	     } else {  // back-facing
	         result = (info.orientation - degrees + 360) % 360;
	     }
	     camera.setDisplayOrientation(result);
	 }
	 
	
};