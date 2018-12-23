package com.a30corner.combomaster.playground.stage;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StageData {

	@SerializedName("stage")
	@Expose
	public String stage;
	@SerializedName("enname")
	@Expose
	public String enname;
	@SerializedName("jpname")
	@Expose
	public String jpname;
	@SerializedName("type")
	@Expose
	public Integer type;
	@SerializedName("enemies")
	@Expose
	public List<EnemyData> enemies = new ArrayList<EnemyData>();
	@SerializedName("floor")
	@Expose
	public Integer floor;
	@SerializedName("detail")
	@Expose
	public List<Floor> detail = new ArrayList<Floor>();
}
