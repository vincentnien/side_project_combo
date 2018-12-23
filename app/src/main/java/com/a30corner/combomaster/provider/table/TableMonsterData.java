package com.a30corner.combomaster.provider.table;

import com.a30corner.combomaster.provider.AbstractProvider;
import com.a30corner.combomaster.provider.AbstractTable;

public class TableMonsterData extends AbstractTable {

	public static final String TABLE = "monster_data";
	
	public TableMonsterData(AbstractProvider provider) {
		super(provider);
	}

	@Override
	public void setupColumns() {
		addColumn(Columns.ID, "INTEGER PRIMARY KEY");
		addColumn(Columns.LV, "INTEGER");
		addColumn(Columns.HP, "INTEGER");
		addColumn(Columns.ATK, "INTEGER");
		addColumn(Columns.RCV, "INTEGER");
		addColumn(Columns.MAX_LV, "INTEGER");
		addColumn(Columns.MAX_HP, "INTEGER");
		addColumn(Columns.MAX_ATK, "INTEGER");
		addColumn(Columns.MAX_RCV, "INTEGER");
		addColumn(Columns.MAX_AWOKEN, "INTEGER");
		for(int i=1; i<10; ++i) {
			addColumn(Columns.AWOKEN + i, "INTEGER DEFAULT(-1)");
		}
		addColumn(Columns.TYPE1, "INTEGER");
		addColumn(Columns.TYPE2, "INTEGER DEFAULT(-1)");
		addColumn(Columns.TYPE3, "INTEGER DEFAULT(-1)");
		addColumn(Columns.PROP1, "INTEGER");
		addColumn(Columns.PROP2, "INTEGER DEFAULT(-1)");
		addColumn(Columns.COST, "INTEGER");
		addColumn(Columns.RARE, "INTEGER");
		addColumn(Columns.SCALE_HP, "VARCHAR(5)");
		addColumn(Columns.SCALE_ATK, "VARCHAR(5)");
		addColumn(Columns.SCALE_RCV, "VARCHAR(5)");
		addColumn(Columns.URL, "TEXT");
		addColumn(Columns.SKILL_LV1, "INTEGER DEFAULT(15)");
		addColumn(Columns.SKILL_LVMAX, "INTEGER DEFAULT(15)");
		addColumn(Columns.OVERPOWER, "INTEGER DEFAULT(0)");
		for(int i=1; i<10; ++i) {
			addColumn(Columns.SUPER_AWOKEN+i, "INTEGER DEFAULT(-1)");
		}
	}

	@Override
	public String getTableName() {
		return TABLE;
	}

	public static final class Columns {
		public static final String ID = "monster_no";
		public static final String LV = "lvmax";
		public static final String HP = "hp";
		public static final String ATK = "atk";
		public static final String RCV = "rcv";
		public static final String MAX_LV = "max_lv";
		public static final String MAX_HP = "max_hp";
		public static final String MAX_ATK = "max_atk";
		public static final String MAX_RCV = "max_rcv";
		public static final String MAX_AWOKEN = "max_awoken";
		public static final String AWOKEN = "awoken_";
		public static final String TYPE1 = "type_1";
		public static final String TYPE2 = "type_2";
		public static final String TYPE3 = "type_3";
		public static final String PROP1 = "prop1";
		public static final String PROP2 = "prop2";
		public static final String COST = "cost";
		public static final String RARE = "rare";
		public static final String SCALE_HP = "scale_hp";
		public static final String SCALE_ATK = "scale_atk";
		public static final String SCALE_RCV = "scale_rcv";
		public static final String URL = "image_url";
		public static final String SKILL_LV1 = "skill_lv1";
		public static final String SKILL_LVMAX = "skill_lvmax";
		public static final String OVERPOWER = "overpower";
		public static final String SUPER_AWOKEN = "super_awoken_";
	}
}
