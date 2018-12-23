package com.a30corner.combomaster.utils.http;

import retrofit.http.GET;
import retrofit.http.Query;

public interface WebData {

	@GET("/md5.php")
	String getMD5(@Query("ver") String ver);

	@GET("/combomaster/message/update.php")
	String getMessage(@Query("lang") String lang);
}
