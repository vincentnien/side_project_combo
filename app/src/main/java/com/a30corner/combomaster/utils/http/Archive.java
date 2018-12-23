package com.a30corner.combomaster.utils.http;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

public interface Archive {

	@GET("/zip/{version}/data.zip")
	public void fetchArchive(@Path("version") String version, Callback<Response> callback);
}
