package com.a30corner.combomaster.utils.http;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;

public interface Archive_Test {

	@GET("/test/zip/data.zip")
	public void fetchArchive(Callback<Response> callback);
}
