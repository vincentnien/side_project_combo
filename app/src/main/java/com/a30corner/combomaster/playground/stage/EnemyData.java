package com.a30corner.combomaster.playground.stage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnemyData {

	@SerializedName("id")
	@Expose
	public Integer id;
	@SerializedName("size")
	@Expose
	public Integer size;
	@SerializedName("hp")
	@Expose
	public Integer hp;
	@SerializedName("cd")
	@Expose
	public Integer cd;
	@SerializedName("atk")
	@Expose
	public Integer atk;
	@SerializedName("def")
	@Expose
	public Integer def;
	@SerializedName("rare")
	@Expose
	public Integer rare = 0;
	

	public int prop1;
	public int prop2 = -1;
}
