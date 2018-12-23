package com.a30corner.combomaster.padherder;

import retrofit.http.GET;
import retrofit.http.Path;

import com.a30corner.combomaster.padherder.vo.PadHerder;

public interface IPadHerder {

	@GET("/user-api/user/{user}/")
	PadHerder getUserData(@Path("user") String user);
	
}
