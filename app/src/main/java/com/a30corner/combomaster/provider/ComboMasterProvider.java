package com.a30corner.combomaster.provider;

import android.net.Uri;

import com.a30corner.combomaster.provider.table.TableActiveSkill;
import com.a30corner.combomaster.provider.table.TableLeaderSkill;
import com.a30corner.combomaster.provider.table.TableMonster;
import com.a30corner.combomaster.provider.table.TableMonsterData;

public class ComboMasterProvider extends AbstractProvider {

	public static final String AUTHORITY = "com.a30corner.combomaster.provider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

	public static final String DB_NAME = "cm.db";
	protected static final int DB_VERSION = 9;


	@Override
	public boolean onCreate() {
		addTable(new TableMonster(this));
		addTable(new TableMonsterData(this));
		addTable(new TableActiveSkill(this));
		addTable(new TableLeaderSkill(this));

		return super.onCreate();
	}

	public static Uri getMonsterUri() {
		return getContentUri(TableMonster.TABLE);
	}

	@Override
	protected String getDBName() {
		return DB_NAME;
	}

	@Override
	protected int getDBVersion() {
		return DB_VERSION;
	}

	@Override
	protected String getAuthority() {
		return AUTHORITY;
	}

	@Override
	protected Uri getContentUri() {
		return CONTENT_URI;
	}

}
