package com.a30corner.combomaster.utils.http;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

import com.a30corner.combomaster.pad.monster.MonsterVO;
import com.a30corner.combomaster.utils.UpdaterUtil.HttpResponse;

public interface NewPets {

	@GET("/combomaster/update0/checkUpdate.php")
	HttpResponse getPetsCount0(@Query("version") String version, @Query("lastUpdate") String modifiedTime);

	@GET("/combomaster/update0/update.php")
	List<MonsterVO> getPetsJson0(@Query("version") String version, @Query("lastUpdate") String modifiedTime);

	@GET("/combomaster/update0/lastModified.php")
	HttpResponse getModifiedTime0();

	@GET("/combomaster/update/checkUpdate.php")
	HttpResponse getPetsCount(@Query("version") String version, @Query("lastUpdate") String modifiedTime);

	@GET("/combomaster/update/update.php")
	List<MonsterVO> getPetsJson(@Query("version") String version, @Query("lastUpdate") String modifiedTime);

	@GET("/combomaster/update/lastModified.php")
	HttpResponse getModifiedTime();
}
