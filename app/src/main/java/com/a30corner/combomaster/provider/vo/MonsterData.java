package com.a30corner.combomaster.provider.vo;

import android.database.Cursor;
import android.util.Pair;

import com.a30corner.combomaster.pad.monster.MonsterSkill.MonsterType;
import com.a30corner.combomaster.provider.table.TableMonsterData;

public class MonsterData {

	public int id;
	public String url;
	public Pair<MonsterType, MonsterType> mTypes;
	public Pair<Integer, Integer> mProps;
	public int cost;
	public int rare;
	public int lv1;
	public int lvmax;
	public int op;
	
	private static int idx_id = -1;
	private static int idx_url;
	private static int idx_type1;
	private static int idx_type2;
	private static int idx_type3;
	private static int idx_prop1;
	private static int idx_prop2;
	private static int idx_cost;
	private static int idx_rare;
	private static int idx_lv1;
	private static int idx_lvmax;
	private static int idx_op;
	
	private MonsterData(int id, String url, int type1, int type2, int type3, int prop1, int prop2, int cost, int rare, int lv1, int lvmax, int op) {
		this.id = id;
		this.url = url;
		this.cost = cost;
		this.rare = rare;
		mTypes = new Pair<MonsterType, MonsterType>(MonsterType.get(type1), MonsterType.get(type2));
		mProps = new Pair<Integer, Integer>(prop1, prop2);
		this.lv1 = lv1;
		this.lvmax = lvmax;
		this.op = op;
	}
	
	public static MonsterData fromCursor(Cursor cursor) {
		if ( idx_id == -1 ) {
			idx_id = cursor.getColumnIndex(TableMonsterData.Columns.ID);
			idx_url = cursor.getColumnIndex(TableMonsterData.Columns.URL);
			idx_type1 = cursor.getColumnIndex(TableMonsterData.Columns.TYPE1);
			idx_type2 = cursor.getColumnIndex(TableMonsterData.Columns.TYPE2);
			idx_type3 = cursor.getColumnIndex(TableMonsterData.Columns.TYPE3);
			idx_prop1 = cursor.getColumnIndex(TableMonsterData.Columns.PROP1);
			idx_prop2 = cursor.getColumnIndex(TableMonsterData.Columns.PROP2);
			idx_cost = cursor.getColumnIndex(TableMonsterData.Columns.COST);
			idx_rare = cursor.getColumnIndex(TableMonsterData.Columns.RARE);
			idx_lv1 = cursor.getColumnIndex(TableMonsterData.Columns.SKILL_LV1);
			idx_lvmax = cursor.getColumnIndex(TableMonsterData.Columns.SKILL_LVMAX);
			idx_op = cursor.getColumnIndex(TableMonsterData.Columns.OVERPOWER);
		}
		int id = cursor.getInt(idx_id);
		String url = cursor.getString(idx_url);
		int type1 = cursor.getInt(idx_type1);
		int type2 = cursor.getInt(idx_type2);
		int type3 = cursor.getInt(idx_type3);
		int prop1 = cursor.getInt(idx_prop1);
		int prop2 = cursor.getInt(idx_prop2);
		int cost = cursor.getInt(idx_cost);
		int rare = cursor.getInt(idx_rare);
		int lv1 = cursor.getInt(idx_lv1);
		int lvmax = cursor.getInt(idx_lvmax);
		int op = cursor.getInt(idx_op);
		return new MonsterData(id, url, type1, type2, type3, prop1, prop2, cost, rare, lv1, lvmax, op);
	}
}
