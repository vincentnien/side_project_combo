package com.a30corner.combomaster.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.a30corner.combomaster.activity.ui.FilterDialog;
import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.LeaderSkill;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterVO;
import com.a30corner.combomaster.provider.table.TableActiveSkill;
import com.a30corner.combomaster.provider.table.TableLeaderSkill;
import com.a30corner.combomaster.provider.table.TableMonster;
import com.a30corner.combomaster.provider.table.TableMonsterData;
import com.a30corner.combomaster.provider.vo.MonsterDO;
import com.a30corner.combomaster.provider.vo.MonsterDO2;
import com.a30corner.combomaster.provider.vo.MonsterData;
import com.a30corner.combomaster.utils.EasyUtil;

public class LocalDBHelper {

	public static void addMonster(Context context, MonsterDO2 mdo) {
		Uri monsterUri = ComboMasterProvider.getMonsterUri();

		context.getContentResolver().insert(monsterUri, mdo.toContentValues());
	}
	
	public static void addMonster(Context context, MonsterDO mdo) {
		Uri monsterUri = ComboMasterProvider.getMonsterUri();

		context.getContentResolver().insert(monsterUri, mdo.toContentValues());
	}
	
	protected static void addMonsterList(SQLiteDatabase db, List<MonsterDO> data) {
		if ( data.size() > 0 ) {
			db.beginTransaction();
			
			try {
				for(MonsterDO mdo : data) {
					db.insert(TableMonster.TABLE, null, mdo.toContentValues());
				}
				
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
		}
	}
//	
	@Deprecated
	public static void random300Data(Context context) {
		//List<MonsterDO> data = new ArrayList<MonsterDO>();
//		for(int i=0; i<300; ++i) {
//			int no = (int)(Math.random()*1500);
//			int lv = (int)(Math.random()*10);
//			int egg1 = (int)(Math.random()*99);
//			int egg2 = (int)(Math.random()*99);
//			int egg3 = (int)(Math.random()*99);
//			MonsterDO mdo = MonsterDO.fromData(i, no, lv, egg1, egg2, egg3, 0, false);
//			addMonster(context, mdo);
//		}
		
	}

	public static MonsterDO getMonsterBox(Context context, int index) {
		Uri monsterUri = ComboMasterProvider.getMonsterUri();

		Cursor cursor = context.getContentResolver().query(monsterUri, null,
				TableMonster.Columns.INDEX + "=?",
				new String[] { String.valueOf(index) }, null);
		if (cursor != null) {
			try {
				if (cursor.moveToFirst()) {
					return MonsterDO.fromCursor(cursor);
				}
			} finally {
				EasyUtil.close(cursor);
			}
		}
		return null;
	}

	protected static List<MonsterDO> getMonsterBoxRange(SQLiteDatabase db, int page) {
		List<MonsterDO> data = new ArrayList<MonsterDO>(30);

		if (db == null) {
			return data;
		}

		int from = page * 30;
		int end = (page + 1) * 30;
		 Cursor cursor = db.query(TableMonster.TABLE, null, 
				TableMonster.Columns.INDEX + ">=? AND "
						+ TableMonster.Columns.INDEX + "<?", 
						new String[] { String.valueOf(from), String.valueOf(end)},
						null, null, null, null);
		if (cursor != null) {
			try {
				while (cursor.moveToNext()) {
					data.add(MonsterDO.fromCursor(cursor));
				}
			} finally {
				EasyUtil.close(cursor);
			}
		}
		return data;
	}
	
	public static final int MAX_BOX = 600;
	
	public static void removeMonsterFromBox(Context context, int index) {
		Uri monsterUri = ComboMasterProvider.getMonsterUri();
		context.getContentResolver().delete(monsterUri, TableMonster.Columns.INDEX+"=?", 
				new String[]{String.valueOf(index)});
	}
	
	public static List<MonsterData> getMonsterList(Context context, Map<String, Object> filter) {
		List<Boolean> attr;// = (ArrayList<Boolean>) filter.get(FilterDialog.FILTER_ATTR);
		List<Boolean> type;// = (ArrayList<Boolean>) filter.get(FilterDialog.FILTER_TYPE);
		Integer rareFrom = 1;// = (Integer)filter.get(FilterDialog.FILTER_RARE_FROM);
		Integer rareEnd = 10;//(Integer)filter.get(FilterDialog.FILTER_RARE_END);
		boolean includeAttr = false;//(Boolean)filter.get(FilterDialog.FILTER_INCLUDE_ATTR2);
		
		// init
		if ( filter.containsKey(FilterDialog.FILTER_ATTR) ) {
			attr = (ArrayList<Boolean>) filter.get(FilterDialog.FILTER_ATTR);
		} else {
			attr = new ArrayList<Boolean>(0);
		}
		if ( filter.containsKey(FilterDialog.FILTER_TYPE) ) {
			type = (ArrayList<Boolean>) filter.get(FilterDialog.FILTER_TYPE);
		} else {
			type = new ArrayList<Boolean>(0);
		}
		if ( filter.containsKey(FilterDialog.FILTER_RARE_FROM) ) {
			rareFrom = (Integer)filter.get(FilterDialog.FILTER_RARE_FROM);
		}
		if ( filter.containsKey(FilterDialog.FILTER_RARE_END) ) {
			rareEnd = (Integer)filter.get(FilterDialog.FILTER_RARE_END);
		}
		if ( filter.containsKey(FilterDialog.FILTER_INCLUDE_ATTR2) ) {
			includeAttr = (Boolean)filter.get(FilterDialog.FILTER_INCLUDE_ATTR2);
		}
		
		// generate sql query string
		StringBuilder sb = new StringBuilder();
		int max = Math.max(rareFrom, rareEnd);
		int min = Math.min(rareFrom, rareEnd);
		List<String> args = new ArrayList<String>();
		int len;
		
		args.add(String.valueOf(min));
		args.add(String.valueOf(max));
		sb.append(TableMonsterData.Columns.RARE).append(">=? AND ").append(TableMonsterData.Columns.RARE).append("<=? ");
		
		List<Integer> attrFilter = new ArrayList<Integer>(5);
		len = attr.size();
		
		for(int i=0; i<len; ++i) {
			if ( attr.get(i) ) {
				if (i<2) {
					attrFilter.add(i);
				} else {
					attrFilter.add(i+1);
				}
			}
		}
		if ( attrFilter.size() > 0 ) {
			StringBuilder attrSb = new StringBuilder();
			attrSb.append(" IN ('").append(TextUtils.join("','", attrFilter)).append("')");
			
			String query = attrSb.toString();
			sb.append(" AND (").append(TableMonsterData.Columns.PROP1).append(query);
			if ( includeAttr ) {
				sb.append(" OR ").append(TableMonsterData.Columns.PROP2).append(query);
			}
			sb.append(")");
		}
		List<Integer> typeFilter = new ArrayList<Integer>();
		len = type.size();
		for(int i=0; i<len; ++i) {
			if ( type.get(i) ) {
				typeFilter.add(i);
			}
		}
		if ( typeFilter.size() > 0 ) {
			StringBuilder typeSb = new StringBuilder();
			typeSb.append(" IN ('").append(TextUtils.join("','", typeFilter)).append("')");
			
			String query = typeSb.toString();
			sb.append(" AND (").append(TableMonsterData.Columns.TYPE1).append(query)
				.append(" OR ").append(TableMonsterData.Columns.TYPE2).append(query)
				.append(" OR ").append(TableMonsterData.Columns.TYPE3).append(query)
				.append(")");
		}
		
		return queryMonsterData(context,
				sb.toString(), args.toArray(new String[args.size()]), TableMonsterData.Columns.ID + " DESC");
	}
	
	public static List<MonsterInfo> getMonsterBoxFilter(Context context, Map<String, Object> filter) {
		List<Boolean> attr;// = (ArrayList<Boolean>) filter.get(FilterDialog.FILTER_ATTR);
		List<Boolean> type;// = (ArrayList<Boolean>) filter.get(FilterDialog.FILTER_TYPE);
		Integer rareFrom = 1;// = (Integer)filter.get(FilterDialog.FILTER_RARE_FROM);
		Integer rareEnd = 10;//(Integer)filter.get(FilterDialog.FILTER_RARE_END);
		boolean includeAttr = false;//(Boolean)filter.get(FilterDialog.FILTER_INCLUDE_ATTR2);
		
		// init
		if ( filter.containsKey(FilterDialog.FILTER_ATTR) ) {
			attr = (ArrayList<Boolean>) filter.get(FilterDialog.FILTER_ATTR);
		} else {
			attr = new ArrayList<Boolean>(0);
		}
		if ( filter.containsKey(FilterDialog.FILTER_TYPE) ) {
			type = (ArrayList<Boolean>) filter.get(FilterDialog.FILTER_TYPE);
		} else {
			type = new ArrayList<Boolean>(0);
		}
		if ( filter.containsKey(FilterDialog.FILTER_RARE_FROM) ) {
			rareFrom = (Integer)filter.get(FilterDialog.FILTER_RARE_FROM);
		}
		if ( filter.containsKey(FilterDialog.FILTER_RARE_END) ) {
			rareEnd = (Integer)filter.get(FilterDialog.FILTER_RARE_END);
		}
		if ( filter.containsKey(FilterDialog.FILTER_INCLUDE_ATTR2) ) {
			includeAttr = (Boolean)filter.get(FilterDialog.FILTER_INCLUDE_ATTR2);
		}
		
		// generate sql query string
		StringBuilder sb = new StringBuilder();
		int max = Math.max(rareFrom, rareEnd);
		int min = Math.min(rareFrom, rareEnd);
		List<String> args = new ArrayList<String>();
		int len;
		
		args.add(String.valueOf(min));
		args.add(String.valueOf(max));
		sb.append("b.").append(TableMonsterData.Columns.RARE).append(">=? AND ")
			.append("b.").append(TableMonsterData.Columns.RARE).append("<=? ");
		
		List<Integer> attrFilter = new ArrayList<Integer>(5);
		len = attr.size();
		boolean isDefault = true;
		if ( min != 1 || max != 9 ) {
			isDefault = false;
		}
		
		for(int i=0; i<len; ++i) {
			if ( attr.get(i) ) {
				if (i<2) {
					attrFilter.add(i);
				} else {
					attrFilter.add(i+1);
				}
			}
		}
		
		if ( attrFilter.size() > 0 ) {
			StringBuilder attrSb = new StringBuilder();
			attrSb.append(" IN ('").append(TextUtils.join("','", attrFilter)).append("')");
			
			String query = attrSb.toString();
			sb.append(" AND (b.").append(TableMonsterData.Columns.PROP1).append(query);
			if ( includeAttr ) {
				sb.append(" OR b.").append(TableMonsterData.Columns.PROP2).append(query);
			}
			sb.append(")");
			isDefault = false;
		}
		List<Integer> typeFilter = new ArrayList<Integer>();
		len = type.size();
		for(int i=0; i<len; ++i) {
			if ( type.get(i) ) {
				typeFilter.add(i);
			}
		}
		if ( typeFilter.size() > 0 ) {
			StringBuilder typeSb = new StringBuilder();
			typeSb.append(" IN ('").append(TextUtils.join("','", typeFilter)).append("')");
			
			String query = typeSb.toString();
			sb.append(" AND (b.").append(TableMonsterData.Columns.TYPE1).append(query)
				.append(" OR b.").append(TableMonsterData.Columns.TYPE2).append(query)
				.append(" OR b.").append(TableMonsterData.Columns.TYPE3).append(query)
				.append(")");
			isDefault = false;
		}
		if ( isDefault ) {
			return getMonsterBox(context);
		}

		List<MonsterInfo> data = new ArrayList<MonsterInfo>();

		String selection = sb.toString();
		SQLiteDatabase db = context.openOrCreateDatabase(ComboMasterProvider.DB_NAME, Context.MODE_PRIVATE, null);
		String query = String.format("SELECT a.*,b.* FROM %s as a,%s as b WHERE %s AND b.monster_no=a.mno", TableMonster.TABLE, TableMonsterData.TABLE, selection);
		Cursor cursor = db.rawQuery(query, args.toArray(new String[args.size()]));
		if ( cursor != null ) {
			try {
				while(cursor.moveToNext()) {
					MonsterDO mdo = MonsterDO.fromCursor(cursor);
					MonsterVO mvo = MonsterVO.fromCursor(cursor);
					
					MonsterInfo info = MonsterInfo.create(mdo.index, mdo.no, 
					        mdo.lv, mdo.egg1, mdo.egg2, mdo.egg3, mdo.awoken, mdo.potentialList, mdo.super_awoken, mvo);
					data.add(info);
				}
			} finally {
				EasyUtil.close(cursor);
			}
		}
		return data;
	}
	
	public static List<MonsterInfo> getMonsterBox(Context context) {
		List<MonsterInfo> data = new ArrayList<MonsterInfo>(MAX_BOX);

		for(int i=0; i<MAX_BOX; ++i) {
		    data.add(MonsterInfo.empty(i));
		}

        SQLiteDatabase db = context.openOrCreateDatabase(ComboMasterProvider.DB_NAME, Context.MODE_PRIVATE, null);
        String query = String.format("SELECT a.*,b.* FROM %s as a,%s as b WHERE b.monster_no=a.mno AND a.boxindex<%d", TableMonster.TABLE, TableMonsterData.TABLE, MAX_BOX);
        Cursor cursor = db.rawQuery(query, null);

		if ( cursor != null ) {
			try {
				while(cursor.moveToNext()) {
					MonsterDO mdo = MonsterDO.fromCursor(cursor);
	                MonsterVO mvo = MonsterVO.fromCursor(cursor);

                    MonsterInfo info = MonsterInfo.create(mdo.index, mdo.no,
                            mdo.lv, mdo.egg1, mdo.egg2, mdo.egg3, mdo.awoken, mdo.potentialList, mdo.super_awoken, mvo);
                    data.set(mdo.index, info);
				}
			} finally {
				EasyUtil.close(cursor);
			}
		}
		return data;
	}
	
    public static List<MonsterDO> getMonsterDO(Context context) {
    	int max = MAX_BOX+20*6;
        List<MonsterDO> data = new ArrayList<MonsterDO>(max);//
        
        for(int i=0; i<max; ++i) {
            data.add(MonsterDO.empty(i));
        }

        Uri monsterUri = ComboMasterProvider.getMonsterUri();
        Cursor cursor = context.getContentResolver().query(
                monsterUri, null, null, null, null);
        if ( cursor != null ) {
            try {
                while(cursor.moveToNext()) {
                    MonsterDO mdo = MonsterDO.fromCursor(cursor);
                    data.set(mdo.index, mdo);
                }
            } finally {
                EasyUtil.close(cursor);
            }
        }
      return data;
    }
	
	public static List<MonsterDO> getMonsterBoxRange(Context context, int page) {
		List<MonsterDO> data = new ArrayList<MonsterDO>(30);

		if (context == null) {
			return data;
		}

		Uri monsterUri = ComboMasterProvider.getMonsterUri();
		int from = page * 30;
		int end = (page + 1) * 30;
		Cursor cursor = context.getContentResolver().query(
				monsterUri,
				null,
				TableMonster.Columns.INDEX + ">=? AND "
						+ TableMonster.Columns.INDEX + "<?",
				new String[] { String.valueOf(from), String.valueOf(end) },
				null);
		if (cursor != null) {
			try {
				while (cursor.moveToNext()) {
					data.add(MonsterDO.fromCursor(cursor));
				}
			} finally {
				EasyUtil.close(cursor);
			}
		}
		return data;
	}
	
	public static int getMaxIndexFromBox(Context context) {
		SQLiteDatabase db = context.openOrCreateDatabase(ComboMasterProvider.DB_NAME, Context.MODE_PRIVATE, null);
		String query = "SELECT MAX(boxindex) FROM " + TableMonster.TABLE;
		Cursor cursor = db.rawQuery(query, null);
		if(cursor != null) {
			try {
				if(cursor.moveToFirst()) {
					return cursor.getInt(0);
				}
			} finally {
				EasyUtil.close(cursor);
			}
		}
		return 0;
	}
	
	public static List<Integer> getSixEmptyBox(Context context) {
		SQLiteDatabase db = context.openOrCreateDatabase(ComboMasterProvider.DB_NAME, Context.MODE_PRIVATE, null);
		List<Integer> box = new ArrayList<Integer>();
		String query = "SELECT MAX(boxindex) FROM " + TableMonster.TABLE;
		Cursor cursor = db.rawQuery(query, null);
		int from = 0;
		
		if(cursor != null) {
			try {
				if(cursor.moveToFirst()) {
					int index = cursor.getInt(0);
					if (index >= MAX_BOX) {
						from = (int)(MAX_BOX+Math.ceil((index-MAX_BOX+1)/6.0)*6.0);
					} else if ( index < MAX_BOX && index+6 >= MAX_BOX ) {
						// there is no free space in BOX... , place them to outside of BOX
						from = MAX_BOX;
					} else {
						from = (index!=0)? index + 1:0;
					}
				} else {
					from = 0;
				}
			} finally {
				EasyUtil.close(cursor);
			}
		}

		for(int i=0; i<6; ++i) {
			box.add(from+i);
		}
		
		return box;
	}

	public static int getMonsterBoxNo(Context context, int index) {
		Uri monsterUri = ComboMasterProvider.getMonsterUri();
		Cursor cursor = context.getContentResolver().query(monsterUri,
				new String[] { TableMonster.Columns.NO },
				TableMonster.Columns.INDEX + "=?",
				new String[] { String.valueOf(index) }, null);
		if (cursor != null) {
			try {
				if (cursor.moveToFirst()) {
					return cursor.getInt(cursor
							.getColumnIndex(TableMonster.Columns.NO));
				}
			} finally {
				EasyUtil.close(cursor);
			}
		}
		return -1;
	}
	
	public static void clearMonsterSkill(Context context) {
		ContentResolver resolver = context.getContentResolver();
		
		resolver.delete(ComboMasterProvider.getContentUri(TableActiveSkill.class), null, null);
		resolver.delete(ComboMasterProvider.getContentUri(TableLeaderSkill.class), null, null);
	}
	
	public static void clearMonsterSkill(Context context, List<MonsterVO> list) {
		ContentResolver resolver = context.getContentResolver();
		List<Integer> data = new ArrayList<Integer>(list.size());
		for(MonsterVO vo : list) {
			int no = vo.getNo();
			data.add(no);
		}
		String where = String.format("%s IN ('%s')", TableActiveSkill.Columns.OWNER, TextUtils.join("','", data));
		resolver.delete(ComboMasterProvider.getContentUri(TableActiveSkill.class), where, null);
		resolver.delete(ComboMasterProvider.getContentUri(TableLeaderSkill.class), where, null);
	}

	public static void addMonsterSkill(Context context, MonsterVO data) {
		int no = data.getNo();
		List<ContentValues> aValues = new ArrayList<ContentValues>();
		List<ContentValues> lValues = new ArrayList<ContentValues>();
		List<LeaderSkill> lList = data.getLeaderSkill();
		for(LeaderSkill ls : lList) {
			lValues.add(ls.toContentValues(no));
		}
		List<ActiveSkill> aList = data.getActiveSkill();
		for(ActiveSkill as : aList) {
			aValues.add(as.toContentValues(no));
		}
		ContentResolver resolver = context.getContentResolver();
		resolver.bulkInsert(ComboMasterProvider.getContentUri(TableLeaderSkill.class), lValues.toArray(new ContentValues[lValues.size()]));
		resolver.bulkInsert(ComboMasterProvider.getContentUri(TableActiveSkill.class), aValues.toArray(new ContentValues[aValues.size()]));
	}
	
	public static void addMonsterSkill(Context context, List<MonsterVO> data) {
		int size = data.size();
		List<ContentValues> aValues = new ArrayList<ContentValues>();
		List<ContentValues> lValues = new ArrayList<ContentValues>();
		for(int i=0; i<size; ++i) {
			MonsterVO vo = data.get(i);
			int no = vo.getNo();
			List<LeaderSkill> lList = vo.getLeaderSkill();
			for(LeaderSkill ls : lList) {
				lValues.add(ls.toContentValues(no));
			}
			List<ActiveSkill> aList = vo.getActiveSkill();
			for(ActiveSkill as : aList) {
				aValues.add(as.toContentValues(no));
			}
		}
		ContentResolver resolver = context.getContentResolver();
		resolver.bulkInsert(ComboMasterProvider.getContentUri(TableLeaderSkill.class), lValues.toArray(new ContentValues[lValues.size()]));
		resolver.bulkInsert(ComboMasterProvider.getContentUri(TableActiveSkill.class), aValues.toArray(new ContentValues[aValues.size()]));
	}
	
	public static void addMonsterData(Context context, MonsterVO data) {
		ContentResolver resolver = context.getContentResolver();
		resolver.insert(ComboMasterProvider.getContentUri(TableMonsterData.class), data.toContentValues());
	}
	
	public static void addMonsterData(Context context, List<MonsterVO> data) {
		ContentResolver resolver = context.getContentResolver();
		ContentValues[] values = new ContentValues[data.size()];
		int size = data.size();
		for(int i=0; i<size; ++i) {
			values[i] = data.get(i).toContentValues();
		}
		resolver.bulkInsert(ComboMasterProvider.getContentUri(TableMonsterData.class), values);
	}
	
	private static String[] MONSTER_DATA_PROJECTION = {
		TableMonsterData.Columns.ID,TableMonsterData.Columns.COST,TableMonsterData.Columns.RARE,
		TableMonsterData.Columns.TYPE1,TableMonsterData.Columns.TYPE2,TableMonsterData.Columns.TYPE3,
		TableMonsterData.Columns.PROP1,TableMonsterData.Columns.PROP2,TableMonsterData.Columns.URL,
		TableMonsterData.Columns.SKILL_LV1,TableMonsterData.Columns.SKILL_LVMAX,TableMonsterData.Columns.OVERPOWER
	};
	
	public static int queryMonsterRarity(Context context, int id) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(ComboMasterProvider.getContentUri(TableMonsterData.class), 
				new String[]{TableMonsterData.Columns.RARE}, 
				TableMonsterData.Columns.ID + "=" + id, null, null);
		if ( cursor != null ) {
			try {
				while(cursor.moveToNext()) {
					return cursor.getInt(0);
				}
			} finally {
				EasyUtil.close(cursor);
			}
		}
		return 1;
	}
	
//	public static List<Pair<Integer,Integer>> queryMonsterRarity(Context context, List<Integer> monsterNo) {
//		List<Pair<Integer,Integer>> data = new ArrayList<Pair<Integer,Integer>>();
//		ContentResolver resolver = context.getContentResolver();
//		String query = "(" + TextUtils.join(",", monsterNo) +  ")";
//		
//		Cursor cursor = resolver.query(ComboMasterProvider.getContentUri(TableMonsterData.class), 
//				new String[]{TableMonsterData.Columns.ID, TableMonsterData.Columns.RARE}, 
//				TableMonsterData.Columns.ID + " IN " + query, null, TableMonsterData.Columns.RARE + " DESC");
//		if ( cursor != null ) {
//			try {
//				while(cursor.moveToNext()) {
//					data.add(new Pair<Integer, Integer>(
//							cursor.getInt(cursor.getColumnIndex(TableMonsterData.Columns.ID)), 
//							cursor.getInt(cursor.getColumnIndex(TableMonsterData.Columns.RARE))));
//				}
//			} finally {
//				EasyUtil.close(cursor);
//			}
//		}
//		return data;
//	}
	
	public static List<MonsterData> queryMonsterData(Context context, String selection, String[] selectionArgs, String order) {
		if ( order == null ) {
			order = TableMonsterData.Columns.ID + " DESC";
		}
		
		List<MonsterData> list = new ArrayList<MonsterData>();
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(ComboMasterProvider.getContentUri(TableMonsterData.class), 
				MONSTER_DATA_PROJECTION, selection, selectionArgs, order);
		if ( cursor != null ) {
			try {
				while(cursor.moveToNext()) {
					list.add(MonsterData.fromCursor(cursor));
				}
			} finally {
				EasyUtil.close(cursor);
			}
		}
		return list;
	}
	
	public static MonsterVO getMonsterData(Context context, int no) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(ComboMasterProvider.getContentUri(TableMonsterData.class),
				null, TableMonsterData.Columns.ID + "=?", 
				new String[]{String.valueOf(no)}, null);
		if ( cursor != null ) {
			try {
			    if ( cursor.moveToFirst() ) {
			        MonsterVO vo = MonsterVO.fromCursor(cursor);
			        // get leader skill and active skill
			        String[] owner = new String[]{String.valueOf(no)};
			        List<ActiveSkill> aList = new ArrayList<ActiveSkill>();
			        Cursor acursor = resolver.query(ComboMasterProvider.getContentUri(TableActiveSkill.class), 
			                null, TableActiveSkill.Columns.OWNER + "=?",
			                owner, null);
			        try {
			            if ( acursor != null ) {
			                while(acursor.moveToNext()) {
			                    ActiveSkill as = ActiveSkill.fromCursor(acursor);
			                    if ( as != null ) {
			                        aList.add(as);
			                    }
			                }
			            }
			            if ( aList.size() == 0 ) {
	                        aList.add(ActiveSkill.empty());
	                    }
			        } finally {
			            EasyUtil.close(acursor);
			        }
			        
			        List<LeaderSkill> lList = new ArrayList<LeaderSkill>();
			        Cursor lcursor = resolver.query(ComboMasterProvider.getContentUri(TableLeaderSkill.class),
			                null, TableLeaderSkill.Columns.OWNER + "=?",
			                owner, null);
			        try {
			            if ( lcursor != null ) {
			                while(lcursor.moveToNext()) {
			                    LeaderSkill ls = LeaderSkill.fromCursor(lcursor);
			                    if ( ls != null ) {
			                        lList.add(ls);
			                    }
			                }
			            }
			            if ( lList.size() == 0 ) {
			                lList.add(LeaderSkill.empty());
	                    }
			        } finally {
			            EasyUtil.close(lcursor);
			        }
			        
			        vo.setActiveSkill(aList);
			        vo.setLeaderSkill(lList);
			        return vo;
			    }
			} finally {
				EasyUtil.close(cursor);
			}
		}
		return null;
	}


	

}
