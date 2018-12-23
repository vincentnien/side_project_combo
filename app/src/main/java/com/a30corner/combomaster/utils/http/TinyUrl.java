package com.a30corner.combomaster.utils.http;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public interface TinyUrl {
	@GET("/api-create.php")
	void getTinyUrl(@Query("url") String url, Callback<Response> callback);
}
