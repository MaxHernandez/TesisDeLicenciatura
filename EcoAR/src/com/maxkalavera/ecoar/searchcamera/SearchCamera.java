package com.maxkalavera.ecoar.searchcamera;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.R.layout;
import com.maxkalavera.utils.SlideMenuBarHandler;

public class SearchCamera extends BaseActivity implements SurfaceHolder.Callback {
    private Camera mCamera;
    private CameraPreview mPreview;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.searchcamera);	
        // Create an instance of Camera
	    try {
	        this.mCamera = Camera.open();
	    } catch (RuntimeException e) {
	        Log.e("SearchCamera_startCamera", e.getMessage());
	    }
	    
	    this.surfaceView = (SurfaceView) findViewById(R.id.searchcamera_surface);
	    this.surfaceHolder = surfaceView.getHolder();
	    this.surfaceHolder.addCallback(this);
	    
	    try {
	        this.mCamera.setPreviewDisplay(this.surfaceHolder);
	        this.mCamera.startPreview();
	    } catch (IOException e) {
	        Log.e("SearchCamera_startCamera", e.getMessage());
	    }
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}
};