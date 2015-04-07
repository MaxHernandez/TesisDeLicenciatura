package com.maxkalavera.utils.httprequest;

import com.squareup.okhttp.Response;
import com.maxkalavera.utils.database.jsonmodels.BaseResponseJsonModel;

public class ResponseBundle {
	private Response response;
	private BaseResponseJsonModel responseJsonObject;
	
	public ResponseBundle(Response response) {
		this.response = response;
		this.responseJsonObject = null;
	}
	
	public ResponseBundle(Response response, BaseResponseJsonModel responseJsonObject) {
		this.response = response;
		this.responseJsonObject = responseJsonObject;
	}
	
	public void response(Response response) {
		this.response = response;
	}
	
	public Response getResponse() {
		return this.response;
	}
	
	public void responseJsonObject(BaseResponseJsonModel responseJsonObject) {
		this.responseJsonObject = responseJsonObject;
	}
	
	public BaseResponseJsonModel getResponseJsonObject() {
		return this.responseJsonObject;
	}

}
