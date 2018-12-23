package com.a30corner.combomaster.provider.table;

import com.a30corner.combomaster.provider.AbstractProvider;
import com.a30corner.combomaster.provider.AbstractTable;

public class TableActiveSkill extends AbstractTable {

	private static final String TABLE = "askill";
	
	public TableActiveSkill(AbstractProvider provider) {
		super(provider);
	}

	@Override
	public void setupColumns() {
		addColumn(Columns.ID, "INTEGER PRIMARY KEY");
		addColumn(Columns.OWNER, "INTEGER");
		addColumn(Columns.TYPE, "INTEGER");
		addColumn(Columns.DATA, "TEXT");
	}

	@Override
	public String getTableName() {
		return TABLE;
	}

	public static final class Columns {
		public static final String ID = "_id";
		public static final String OWNER = "owner_id";
		public static final String TYPE = "skill_type";
		public static final String DATA = "data";
	}
}
