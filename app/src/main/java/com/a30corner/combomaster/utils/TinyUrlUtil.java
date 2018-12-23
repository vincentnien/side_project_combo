package com.a30corner.combomaster.utils;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;

import com.a30corner.combomaster.utils.http.TinyUrl;

public class TinyUrlUtil {

	private static final String TINY_URL_COM = "http://tinyurl.com";
	
	public static void getTinyUrl(String url, Callback<Response> callback) throws Exception {
		LogUtil.e(url);
		RestAdapter adapter = new RestAdapter.Builder().setEndpoint(
				TINY_URL_COM).build();
		adapter.create(TinyUrl.class).getTinyUrl(url, callback);
	}
}


