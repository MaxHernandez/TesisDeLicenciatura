package com.maxkalavera.ecoar.searchcamera;

import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.view.Surface;

import com.maxkalavera.utils.database.productmodel.ProductModel;

public class CameraOptionsSearchCameraLoader extends AsyncTaskLoader<Camera> {
	
	private Camera camera;
	private int option;
	private Context context;
	
	public static final int GET_CAMERA = 0;
	public static final int RELEASE_CAMERA = 1;
	//private static final int CAMERA_ID = Camera.CameraInfo.CAMERA_FACING_BACK;
	private static final int CAMERA_ID = Camera.CameraInfo.CAMERA_FACING_FRONT;
	
	public CameraOptionsSearchCameraLoader(Context context, Camera camera, int option) {
		super(context);
		this.camera = camera;
		this.option = option;
		this.context = context;
	}
	
	public void setCameraDisplayOrientation(Camera camera) {		
		Camera.CameraInfo info =
	             new android.hardware.Camera.CameraInfo();
	     android.hardware.Camera.getCameraInfo(CAMERA_ID, info);
	     
	     int rotation = ((FragmentActivity) this.context).getWindowManager().getDefaultDisplay()
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
	     
	     Parameters params=camera.getParameters();
	     params.setZoom(0);
	     if (params.getSupportedFocusModes().contains(Parameters.FOCUS_MODE_MACRO)) {
	    	 params.setFocusMode(Parameters.FOCUS_MODE_MACRO);
	     } else if (params.getSupportedFocusModes().contains(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
	    	 params.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
	     camera.setParameters(params);		
	}

	public Camera loadInBackground() {
		switch(this.option) {
			case GET_CAMERA:
			    if (this.camera != null) {
			        this.camera.release();
			        this.camera = null;
			    }
			    //try {
					this.camera = Camera.open(CAMERA_ID);
			    //} catch (Exception e) {
			        //e.printStackTrace();
			    //}
			    if(this.camera != null)
			    	this.setCameraDisplayOrientation(this.camera);
				return this.camera;
				
			case RELEASE_CAMERA:
			    if (this.camera != null) {
			        this.camera.release();
			        this.camera = null;
			    }
				return this.camera;
				
			default:
				break;
		}
		return null;
	}
}
