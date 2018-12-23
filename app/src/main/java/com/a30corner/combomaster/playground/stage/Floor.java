package com.a30corner.combomaster.playground.stage;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Floor {

	@SerializedName("must")
	@Expose
	public List<Integer> must = new ArrayList<Integer>();
	@SerializedName("index")
	@Expose
	public List<Integer> index = new ArrayList<Integer>();
	@SerializedName("size")
	@Expose
	public List<Integer> size = new ArrayList<Integer>();
}
