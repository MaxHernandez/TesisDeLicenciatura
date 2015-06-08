package com.maxkalavera.utils.httprequest;

import com.squareup.okhttp.Response;
import com.maxkalavera.utils.database.jsonmodels.BaseResponseJsonModel;

public class ResponseBundle {
	private Response response;
	private BaseResponseJsonModel responseJsonObject;
	private String responseBody;
	
	public ResponseBundle(Response response) {
		this.response = response;
		this.responseJsonObject = null;
		this.responseBody = null;
	}
	
	public ResponseBundle(Response response, BaseResponseJsonModel responseJsonObject) {
		this.response = response;
		this.responseJsonObject = responseJsonObject;
		this.responseBody = null;
	}

	public ResponseBundle(Response response, BaseResponseJsonModel responseJsonObject, String responseBody) {
		this.response = response;
		this.responseJsonObject = responseJsonObject;
		this.responseBody = responseBody;
	}
	
	public Response getResponse() {
		return this.response;
	}
		
	public BaseResponseJsonModel getResponseJsonObject() {
		return this.responseJsonObject;
	}
	
	public String getResponseBody() {
		return this.responseBody;
	}

}
