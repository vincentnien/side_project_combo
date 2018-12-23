package com.a30corner.combomaster.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.SparseArray;

import com.a30corner.combomaster.provider.table.TableMonster;
import com.a30corner.combomaster.provider.table.TableMonsterData;
import com.a30corner.combomaster.provider.vo.MonsterDO;
import com.a30corner.combomaster.utils.LogUtil;

public abstract class AbstractProvider extends ContentProvider {

	private SparseArray<AbstractTable> mTableMap = new SparseArray<AbstractTable>();
	private AtomicInteger mCounter = new AtomicInteger(1);
	
	protected Context mContext;
	private SQLiteDatabase mDatabase;
	
	private static AbstractProvider sInstance;

	private static Map<Class<?>, String> sClassMaps = new HashMap<Class<?>, String>();
	private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	protected void addTable(AbstractTable table) {
		int index = mCounter.getAndIncrement();
		table.setActionId(index);
		table.setupColumns();

		mTableMap.append(index, table);
		sClassMaps.put(table.getClass(), table.getTableName());

		sUriMatcher.addURI(getAuthority(), table.getTableName(), index);
	}
	
	public static String getTableName(Class<?> table) {
		if ( sClassMaps.containsKey(table) ) {
			return sClassMaps.get(table);
		}
		return null;
	}


    public static Uri getContentUri(Class<?> table) {
        return getContentUri(getTableName(table));
    }
    
    public static Uri getContentUri(String table) {
        return Uri.withAppendedPath(sInstance.getContentUri(), table);
    }
	
	public SQLiteDatabase getDatabase() {
		return mDatabase;
	}

	protected abstract String getDBName();

	protected abstract int getDBVersion();

	protected abstract String getAuthority();

	protected abstract Uri getContentUri();

	public class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase sqlitedb) {
			LogUtil.d("onCreate");
			for (int index = 0; index < mTableMap.size(); ++index) {
				AbstractTable table = mTableMap.valueAt(index);
				sqlitedb.execSQL(table.createTableSQL());
			}
		}

		@Override
		public void onDowngrade(SQLiteDatabase sqlitedb, int oldVersion,
				int newVersion) {
			
			for (int index = 0; index < mTableMap.size(); ++index) {
				AbstractTable table = mTableMap.valueAt(index);
				sqlitedb.execSQL("DROP TABLE IF EXISTS " + table.getTableName());
			}

			onCreate(sqlitedb);
		}

		private void version2Added(SQLiteDatabase sqlitedb) {
			String sql = "ALTER TABLE " + TableMonster.TABLE + " ADD COLUMN " + TableMonster.Columns.MAWOKEN;
			for(int i=1; i<6; ++i) {
				sqlitedb.execSQL(sql + i + " INTEGER DEFAULT(-1)");
			}
		}
		
		private void version3Added(SQLiteDatabase sqlitedb) {
			String sql = "ALTER TABLE " + TableMonster.TABLE + " ADD COLUMN " + TableMonster.Columns.SKILL2;
			sqlitedb.execSQL(sql + " INTEGER DEFAULT(-1)");
		}
		
		private void version4Added(SQLiteDatabase sqlitedb) {
			String sql = "ALTER TABLE " + TableMonsterData.TABLE + " ADD COLUMN " + TableMonsterData.Columns.SKILL_LV1;
			sqlitedb.execSQL(sql + " INTEGER DEFAULT(15)");
			sql = "ALTER TABLE " + TableMonsterData.TABLE + " ADD COLUMN " + TableMonsterData.Columns.SKILL_LVMAX;
			sqlitedb.execSQL(sql + " INTEGER DEFAULT(15)");
			sql = "ALTER TABLE " + TableMonster.TABLE + " ADD COLUMN " + TableMonster.Columns.SKILL1_CD;
			sqlitedb.execSQL(sql + " INTEGER DEFAULT(99)");
			sql = "ALTER TABLE " + TableMonster.TABLE + " ADD COLUMN " + TableMonster.Columns.SKILL2_CD;
			sqlitedb.execSQL(sql + " INTEGER DEFAULT(99)");
		}

		private void version5Added(SQLiteDatabase sqlitedb) {
			try {
				String sql = "ALTER TABLE " + TableMonster.TABLE + " ADD COLUMN " + TableMonster.Columns.MAWOKEN;
				sqlitedb.execSQL(sql + 6 + " INTEGER DEFAULT(-1)");
			} catch(Throwable e) {
			}
		}

		private void version9Added(SQLiteDatabase sqlitedb) {
			try {
				for(int i=1; i<10; ++i) {
					String sql = "ALTER TABLE " + TableMonsterData.TABLE + " ADD COLUMN " + TableMonsterData.Columns.SUPER_AWOKEN;
					sqlitedb.execSQL(sql + i + " INTEGER DEFAULT(-1)");
				}
				String sql = "ALTER TABLE " + TableMonster.TABLE + " ADD COLUMN " + TableMonster.Columns.SUPER_AWOKEN;
				sqlitedb.execSQL(sql + " INTEGER DEFAULT(-1)");
			} catch(Throwable e) {
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase sqlitedb, int oldver, int newver) {
			LogUtil.d("onUpgrade");
			if ( oldver == 2 ) {
				version2Added(sqlitedb);
				version3Added(sqlitedb);
				version4Added(sqlitedb);
				return ;
			}
			
			if (oldver == 3) {
				version3Added(sqlitedb);
				version4Added(sqlitedb);
				return ;
			}
			
			if(oldver == 4) {
				version4Added(sqlitedb);
				return ;
			}
			
			if(oldver==5 || oldver ==6) {
				version5Added(sqlitedb);
				return ;
			}

			if(oldver<9) {
				version9Added(sqlitedb);
				return;
			}
			
			List<MonsterDO> oldData = new ArrayList<MonsterDO>();
			if ( oldver == 1 ) {
				// backup old data
				for(int page=0; page<10; ++page) {
					List<MonsterDO> list = LocalDBHelper.getMonsterBoxRange(sqlitedb, page);
					oldData.addAll(list);
				}
			}
			
			for (int index = 0; index < mTableMap.size(); ++index) {
				AbstractTable table = mTableMap.valueAt(index);
				sqlitedb.execSQL("DROP TABLE IF EXISTS " + table.getTableName());
			}

			onCreate(sqlitedb);
			
			// restore monster box
			if ( oldData.size() > 0 ) {
				LocalDBHelper.addMonsterList(sqlitedb, oldData);
			}
		}
	}

	@Override
	public boolean onCreate() {
		mContext = getContext();
		
		sInstance = this;

		DatabaseHelper dbhelper = new DatabaseHelper(mContext, getDBName(),
				null, getDBVersion());
		mDatabase = dbhelper.getWritableDatabase();
		
		return mDatabase != null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int index = sUriMatcher.match(uri);
		try {
			return mTableMap.get(index).delete(uri, selection, selectionArgs);
		} catch (NullPointerException e) {
			return 0;
		}
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int index = sUriMatcher.match(uri);
		try {
			return mTableMap.get(index).insert(uri, values);
		} catch (NullPointerException e) {
			return null;
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		int index = sUriMatcher.match(uri);
		try {
			return mTableMap.get(index).query(uri, projection, selection,
					selectionArgs, sortOrder);
		} catch (NullPointerException e) {
			return null;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int index = sUriMatcher.match(uri);
		try {
			return mTableMap.get(index).update(uri, values, selection,
					selectionArgs);
		} catch (NullPointerException e) {
			return 0;
		}
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		int index = sUriMatcher.match(uri);
		try {
			return mTableMap.get(index).bulkInsert(uri, values);
		} catch (NullPointerException e) {
			return 0;
		}
	}

}
