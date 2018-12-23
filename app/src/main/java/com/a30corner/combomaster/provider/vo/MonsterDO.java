package com.a30corner.combomaster.provider.vo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.a30corner.combomaster.pad.monster.MonsterSkill.MoneyAwokenSkill;
import com.a30corner.combomaster.provider.table.TableMonster;

public class MonsterDO {

	public final int index;
	public final int no;
	public final int lv;
	public final int egg1;
	public final int egg2;
	public final int egg3;
	public final int awoken;
	public final boolean maxAwoken;
	public final int active2;
	public List<MoneyAwokenSkill> potentialList;
	public final int skill1_cd;
	public final int skill2_cd;
	public final int super_awoken;
	
	private MonsterDO(int index, int no, int lv, int egg1, int egg2, int egg3,
			int awoken, boolean maxAwoken, List<MoneyAwokenSkill> list, int active2,
			int cd1, int cd2, int sawoken) {
		this.index = index;
		this.no = no;
		this.lv = lv;
		this.egg1 = egg1;
		this.egg2 = egg2;
		this.egg3 = egg3;
		this.awoken = awoken;
		this.maxAwoken = maxAwoken;
		this.potentialList = list;
		this.active2 = active2;
		this.skill1_cd = cd1;
		this.skill2_cd = cd2;
		this.super_awoken = sawoken;
	}

	public static MonsterDO fromData(int index, int no, int lv, int egg1,
			int egg2, int egg3, int awoken, boolean maxAwoken, List<MoneyAwokenSkill> list, int active2,
			int cd1, int cd2, int super_awoken) {
		return new MonsterDO(index, no, lv, egg1, egg2, egg3, awoken, maxAwoken, list, active2, cd1, cd2, super_awoken);
	}
	
	public static MonsterDO empty(int index) {
		return new MonsterDO(index, -1, 0, 0, 0, 0, 0, false, new ArrayList<MoneyAwokenSkill>(0), -1, 1, 1, -1);
	}

	private static int idx_index = -1;
	private static int idx_no;
	private static int idx_lv;
	private static int idx_egg1;
	private static int idx_egg2;
	private static int idx_egg3;
	private static int idx_awoken;
	private static int idx_awokenMax;
	private static int idx_active2;
	private static int idx_cd1;
	private static int idx_cd2;
	private static int idx_super_awoken;

	public static MonsterDO fromCursor(Cursor cursor) {
		if (cursor == null) {
			return null;
		}
        idx_index = cursor
                .getColumnIndexOrThrow(TableMonster.Columns.INDEX);
        idx_no = cursor.getColumnIndexOrThrow(TableMonster.Columns.NO);
        idx_lv = cursor.getColumnIndexOrThrow(TableMonster.Columns.LV);
        idx_egg1 = cursor.getColumnIndexOrThrow(TableMonster.Columns.HP);
        idx_egg2 = cursor.getColumnIndexOrThrow(TableMonster.Columns.ATK);
        idx_egg3 = cursor.getColumnIndexOrThrow(TableMonster.Columns.RCV);
        idx_awoken = cursor
                .getColumnIndexOrThrow(TableMonster.Columns.AWOKEN);
        idx_awokenMax = cursor
                .getColumnIndexOrThrow(TableMonster.Columns.MAX_AWOKEN);
        idx_active2 = cursor.getColumnIndexOrThrow(TableMonster.Columns.SKILL2);
        idx_cd1 = cursor.getColumnIndexOrThrow(TableMonster.Columns.SKILL1_CD);
        idx_cd2 = cursor.getColumnIndexOrThrow(TableMonster.Columns.SKILL2_CD);
		idx_super_awoken = cursor.getColumnIndexOrThrow(TableMonster.Columns.SUPER_AWOKEN);
		int index = cursor.getInt(idx_index);
		int no = cursor.getInt(idx_no);
		int egg1 = cursor.getInt(idx_egg1);
		int egg2 = cursor.getInt(idx_egg2);
		int egg3 = cursor.getInt(idx_egg3);
		int lv = cursor.getInt(idx_lv);
		int awoken = cursor.getInt(idx_awoken);
		boolean maxAwoken = cursor.getInt(idx_awokenMax) == 1;
		int active2 = cursor.getInt(idx_active2);
		int cd1 = cursor.getInt(idx_cd1);
		int cd2 = cursor.getInt(idx_cd2);
		
		List<MoneyAwokenSkill> list = new ArrayList<MoneyAwokenSkill>();
		for(int i=1; i<7; ++i) {
			int idx = cursor.getColumnIndexOrThrow(TableMonster.Columns.MAWOKEN+i);
			int potentialId = cursor.getInt(idx);
			if ( potentialId != -1 ) {
				list.add(MoneyAwokenSkill.get(potentialId));
			}
		}
		int super_awoken = cursor.getInt(idx_super_awoken);

		return new MonsterDO(index, no, lv, egg1, egg2, egg3, awoken, maxAwoken, list, active2, cd1, cd2, super_awoken);
	}

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(TableMonster.Columns.INDEX, index);
		values.put(TableMonster.Columns.NO, no);
		values.put(TableMonster.Columns.LV, lv);
		values.put(TableMonster.Columns.HP, egg1);
		values.put(TableMonster.Columns.ATK, egg2);
		values.put(TableMonster.Columns.RCV, egg3);
		values.put(TableMonster.Columns.AWOKEN, awoken);
		values.put(TableMonster.Columns.MAX_AWOKEN, maxAwoken ? 1 : 0);
		values.put(TableMonster.Columns.SKILL2, active2);
		values.put(TableMonster.Columns.SKILL1_CD, skill1_cd);
		values.put(TableMonster.Columns.SKILL2_CD, skill2_cd);
		values.put(TableMonster.Columns.SUPER_AWOKEN, super_awoken);
		int size = potentialList.size();
		for(int i=0; i<size; ++i) {
			MoneyAwokenSkill s = potentialList.get(i);
			if ( s != MoneyAwokenSkill.SKILL_NONE ) {
				values.put(TableMonster.Columns.MAWOKEN+(i+1), s.ordinal());
			}
		}
		
		return values;
	}
}
