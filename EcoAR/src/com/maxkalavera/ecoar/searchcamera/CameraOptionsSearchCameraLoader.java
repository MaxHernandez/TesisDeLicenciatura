package com.maxkalavera.ecoar.searchcamera;

import android.content.Context;
import android.hardware.Camera;
import android.support.v4.content.AsyncTaskLoader;

import com.maxkalavera.utils.database.productmodel.ProductModel;

public class CameraOptionsSearchCameraLoader extends AsyncTaskLoader<Camera> {
	
	Camera camera;
	int option;
	
	public static final int GET_CAMERA = 0;
	public static final int RELEASE_CAMERA = 1;
	private static final int CAMERA_ID = Camera.CameraInfo.CAMERA_FACING_BACK;
	
	public CameraOptionsSearchCameraLoader(Context context, Camera camera, int option) {
		super(context);
		this.camera = camera;
		this.option = option;
	}

	public Camera loadInBackground() {
		switch(this.option) {
			case GET_CAMERA:
			    if (this.camera != null) {
			        this.camera.release();
			        this.camera = null;
			    }
			    try {
					this.camera = Camera.open(CAMERA_ID);
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
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
