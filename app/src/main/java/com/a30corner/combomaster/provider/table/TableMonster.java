package com.a30corner.combomaster.provider.table;

import com.a30corner.combomaster.provider.AbstractProvider;
import com.a30corner.combomaster.provider.AbstractTable;

public class TableMonster extends AbstractTable {

	public static final String TABLE = "monster";

	public TableMonster(AbstractProvider provider) {
		super(provider);
	}

	@Override
	public String getTableName() {
		return TABLE;
	}

	@Override
	public void setupColumns() {
		addColumn(Columns.INDEX, "INTEGER PRIMARY KEY");
		addColumn(Columns.NO, "INTEGER");
		addColumn(Columns.LV, "INTEGER");
		addColumn(Columns.HP, "INTEGER");
		addColumn(Columns.ATK, "INTEGER");
		addColumn(Columns.RCV, "INTEGER");
		addColumn(Columns.AWOKEN, "INTEGER");
		addColumn(Columns.MAX_AWOKEN, "INTEGER");
		for(int i=1; i<7; ++i) {
			addColumn(Columns.MAWOKEN +i, "INTEGER DEFAULT(-1)");
		}
		addColumn(Columns.SKILL2, "INTEGER DEFAULT(-1)");
		addColumn(Columns.SKILL1_CD, "INTEGER DEFAULT(99)");
		addColumn(Columns.SKILL2_CD, "INTEGER DEFAULT(99)");
		addColumn(Columns.SUPER_AWOKEN, "INTEGER DEFAULT(-1)");
	}

	public static final class Columns {
		public static final String INDEX = "boxindex";
		public static final String NO = "mno";
		public static final String LV = "lv";
		public static final String HP = "hp_plus";
		public static final String ATK = "atk_plus";
		public static final String RCV = "rcv_plus";
		public static final String AWOKEN = "awoken_count";
		public static final String MAX_AWOKEN = "awoken_max";
		public static final String MAWOKEN = "mawoken_";
		public static final String SKILL2 = "skill2";
		public static final String SKILL1_CD = "skill1_cd";
		public static final String SKILL2_CD = "skill2_cd";
		public static final String SUPER_AWOKEN = "super_awoken";
	}

}
