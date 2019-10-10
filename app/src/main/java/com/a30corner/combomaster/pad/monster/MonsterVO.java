package com.a30corner.combomaster.pad.monster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.a30corner.combomaster.pad.monster.MonsterSkill.AwokenSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MonsterType;
import com.a30corner.combomaster.provider.LocalDBHelper;
import com.a30corner.combomaster.provider.table.TableMonsterData;
import com.a30corner.combomaster.utils.EasyUtil;
import com.a30corner.combomaster.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MonsterVO {
	private final String mImageUrl;
	public final int mNo;
	private final int mLv; // max
	private final int mHp; // min
	private final int mAtk; // min
	private final int mRecovery; // min
	// private final Pair<String, String> mSkill;
	// private final Pair<String, String> mLeaderSkill;
	// private final List<String> mAwokens;

	public List<LeaderSkill> mLeaderSkill;
	private List<ActiveSkill> mActiveSkill;

	private List<AwokenSkill> mAwokenList;
	private List<AwokenSkill> super_awoken;

	private Pair<MonsterType, MonsterType> mMonsterTypes;
	private MonsterType mMonsterType3 = MonsterType.NONE;
	
	private Pair<Integer, Integer> mMonsterProps;
	

	private final int mHpMax;
	private final int mAtkMax;
	private final int mRcvMax;

	private final float hpScale;
	private final float atkScale;
	private final float rcvScale;

	private final int mRare;
	private final int mCost;
	public final int slv1;
	public final int slvmax;

	public final int overpower;
	
	@Override
	public String toString() {
		return mNo + ":" + mImageUrl;
	}

	public List<AwokenSkill> getAwokenList() {
		return Collections.unmodifiableList(mAwokenList);
	}

	public List<AwokenSkill> getSuperAwokenList() {
		return Collections.unmodifiableList(super_awoken);
	}

	public static MonsterVO fromDB(Context context, int no) {
		return LocalDBHelper.getMonsterData(context, no);
	}


    static int idx_no = -1;
    static int idx_lv;
    static int idx_url;
    static int idx_hp;
    static int idx_atk;
    static int idx_rcv;
    static int idx_hpmax;
    static int idx_atkmax;
    static int idx_rcvmax;
//  static int idx_skill;
//  static int idx_skill_title;
//  static int idx_lskill;
//  static int idx_lskill_title;
    static int idx_type1;
    static int idx_type2;
    static int idx_type3;
    static int idx_prop1;
    static int idx_prop2;
    static int[] idx_awoken = new int[10];
    static int idx_hpscale;
    static int idx_atkscale;
    static int idx_rcvscale;
    static int idx_max_awoken;
    static int idx_cost;
    static int idx_rare;
    static int idx_skill_lv1;
    static int idx_skill_lvmax;
	static int idx_op;
	static int[] idx_super_awoken = new int[10];

    public static MonsterVO fromCursor(Cursor cursor) {
        idx_no = cursor.getColumnIndexOrThrow(TableMonsterData.Columns.ID);
        idx_lv = cursor.getColumnIndex(TableMonsterData.Columns.LV);
        idx_url = cursor.getColumnIndex(TableMonsterData.Columns.URL);
        idx_hp = cursor.getColumnIndex(TableMonsterData.Columns.HP);
        idx_atk = cursor.getColumnIndex(TableMonsterData.Columns.ATK);
        idx_rcv = cursor.getColumnIndex(TableMonsterData.Columns.RCV);
        idx_hpmax = cursor.getColumnIndex(TableMonsterData.Columns.MAX_HP);
        idx_atkmax = cursor.getColumnIndex(TableMonsterData.Columns.MAX_ATK);
        idx_rcvmax = cursor.getColumnIndex(TableMonsterData.Columns.MAX_RCV);
        for(int i=1; i<10; ++i) {
            idx_awoken[i-1] = cursor.getColumnIndex(TableMonsterData.Columns.AWOKEN+i);
        }
		for(int i=1; i<10; ++i) {
			idx_super_awoken[i-1] = cursor.getColumnIndex(TableMonsterData.Columns.SUPER_AWOKEN+i);
		}
        idx_max_awoken = cursor.getColumnIndex(TableMonsterData.Columns.MAX_AWOKEN);
        idx_type1 = cursor.getColumnIndex(TableMonsterData.Columns.TYPE1);
        idx_type2 = cursor.getColumnIndex(TableMonsterData.Columns.TYPE2);
        idx_type3 = cursor.getColumnIndex(TableMonsterData.Columns.TYPE3);
        idx_prop1 = cursor.getColumnIndex(TableMonsterData.Columns.PROP1);
        idx_prop2 = cursor.getColumnIndex(TableMonsterData.Columns.PROP2);
        idx_cost = cursor.getColumnIndex(TableMonsterData.Columns.COST);
        idx_rare = cursor.getColumnIndex(TableMonsterData.Columns.RARE);
        idx_hpscale = cursor.getColumnIndex(TableMonsterData.Columns.SCALE_HP);
        idx_atkscale = cursor.getColumnIndex(TableMonsterData.Columns.SCALE_ATK);
        idx_rcvscale = cursor.getColumnIndex(TableMonsterData.Columns.SCALE_RCV);
        idx_skill_lv1 = cursor.getColumnIndex(TableMonsterData.Columns.SKILL_LV1);
        idx_skill_lvmax = cursor.getColumnIndex(TableMonsterData.Columns.SKILL_LVMAX);
		idx_op = cursor.getColumnIndex(TableMonsterData.Columns.OVERPOWER);
        
        
        int no = cursor.getInt(idx_no);
        int lv = cursor.getInt(idx_lv);
        int hp = cursor.getInt(idx_hp);
        int atk = cursor.getInt(idx_atk);
        int rcv = cursor.getInt(idx_rcv);
        int hpmax = cursor.getInt(idx_hpmax);
        int atkmax = cursor.getInt(idx_atkmax);
        int rcvmax = cursor.getInt(idx_rcvmax);
        String url = cursor.getString(idx_url);
        List<AwokenSkill> awokens = new ArrayList<MonsterSkill.AwokenSkill>();
        for(int i=1; i<10; ++i) {
            int oid = cursor.getInt(idx_awoken[i-1]);
            if ( oid != -1 ) {
                awokens.add(AwokenSkill.get(oid));
            }
        }
		List<AwokenSkill> sawokens = new ArrayList<MonsterSkill.AwokenSkill>();
		for(int i=1; i<10; ++i) {
			int oid = cursor.getInt(idx_super_awoken[i-1]);
			if ( oid != -1) {
				sawokens.add(AwokenSkill.get(oid));
			}
		}
        
        int awokenmax = awokens.size();
        Pair<MonsterType, MonsterType> types = null;
        int type1 = cursor.getInt(idx_type1);
        int type2 = cursor.getInt(idx_type2);
        int type3 = cursor.getInt(idx_type3);
        types = new Pair<MonsterSkill.MonsterType, MonsterSkill.MonsterType>(
                MonsterType.get(type1), MonsterType.get(type2));
        MonsterType mtype3 = MonsterType.get(type3);
        int prop1 = cursor.getInt(idx_prop1);
        int prop2 = cursor.getInt(idx_prop2);
        Pair<Integer, Integer> props = new Pair<Integer, Integer>(prop1,prop2);
        int cost = cursor.getInt(idx_cost);
        int rare = cursor.getInt(idx_rare);
        float hpscale = Float.parseFloat(cursor.getString(idx_hpscale));
        float atkscale = Float.parseFloat(cursor.getString(idx_atkscale));
        float rcvscale = Float.parseFloat(cursor.getString(idx_rcvscale));
        int lv1 = cursor.getInt(idx_skill_lv1);
        int lvmax = cursor.getInt(idx_skill_lvmax);
		int op = cursor.getInt(idx_op);
        return new MonsterVO(no, hp, lv, atk, rcv, hpmax, atkmax, rcvmax, hpscale, atkscale, rcvscale, awokens, types, mtype3, props, rare, cost, url, lv1, lvmax, op, sawokens);
    }
	
	
	@Deprecated
	public static MonsterVO fromAsset(Context context, int no)
			throws IOException {
		Gson gson = new Gson();
		Type type = new TypeToken<MonsterVO>() {
		}.getType();

		String json = null;
		InputStream is = null;
		BufferedReader reader = null;
		try {
			String filename = String.format("data/%04d.json", no);
			// load from /data/data/ first...
			File f = new File(context.getFilesDir(), filename);
			if (f.exists()) {
				is = new FileInputStream(f);
			} else {
				// if not exists, load from assets
				is = context.getAssets().open(filename);
			}
			reader = new BufferedReader(new InputStreamReader(is));
			json = reader.readLine();
		} catch (Exception e) {
			LogUtil.e(e, e.getMessage());
		} finally {
			EasyUtil.close(is);
			EasyUtil.close(reader);
		}
		if (TextUtils.isEmpty(json)) {
			throw new IOException("load failed.");
		}

		return gson.fromJson(json, type);
	}

	public int getNo() {
		return mNo;
	}
	
	public int getMaxLv() {
		if(overpower == 0) {
			return mLv;
		} else {
			return mLv + 11;
		}
	}

	public int getAtk(int lv) {
		long llv;
		if(mLv < lv && overpower > 0) {
			llv = lv;
		} else {
			llv = (mLv < lv) ? mLv : lv;
		}
		long current_atk;

		if (mLv == 1) {
			current_atk = mAtkMax;
		} else if(llv <= 99){
			current_atk = Math.round(mAtk + (mAtkMax - mAtk)
					* Math.pow((llv - 1) / (double) (mLv - 1), atkScale));
		} else {
			current_atk = (int)Math.round(mAtkMax * (1.0+ overpower /100.0*(llv-99)/11.0));
		}
		return (int) current_atk;
	}

	public int getHp(int lv) {
		long llv;
		if(mLv < lv && overpower > 0) {
			llv = lv;
		} else {
			llv = (mLv < lv) ? mLv : lv;
		}

		long current_hp;

		if (mLv == 1) {
			current_hp = mHpMax;
		} else if(llv <= 99){
			current_hp = Math.round(mHp + (mHpMax - mHp)
					* Math.pow((llv - 1) / (double) (mLv - 1), hpScale));
		} else {
			current_hp = (int)Math.round(mHpMax * (1.0+ overpower /100.0*(llv-99)/11.0));
		}
		return (int) current_hp;
	}

	public int getRecovery(int lv) {
		long llv;
		if(mLv < lv && overpower > 0) {
			llv = lv;
		} else {
			llv = (mLv < lv) ? mLv : lv;
		}
		long current_rec;

		if (mLv == 1) {
			current_rec = mRcvMax;
		} else if(llv <= 99){
			current_rec = Math.round(mRecovery + (mRcvMax - mRecovery)
					* Math.pow((llv - 1) / (double) (mLv - 1), rcvScale));
		} else {
			current_rec = (int)Math.round(mRcvMax * (1.0+ overpower /100.0*(llv-99)/11.0));
		}
		return (int) current_rec;
	}

	public Pair<Integer, Integer> getMonsterProps() {
		return mMonsterProps;
	}

	public Pair<MonsterType, MonsterType> getMonsterTypes() {
		return mMonsterTypes;
	}
	
	public MonsterType getMonsterType3() {
		if ( mMonsterType3 == null ) {
			return MonsterType.NONE;
		}
		return mMonsterType3;
	}

	public void setLeaderSkill(List<LeaderSkill> list) {
	    mLeaderSkill = list;
	}
	
	public void setActiveSkill(List<ActiveSkill> list) {
	    mActiveSkill = list;
	}
	
	public List<LeaderSkill> getLeaderSkill() {
		return mLeaderSkill;
	}

	public List<ActiveSkill> getActiveSkill() {
		return mActiveSkill;
	}

	public MonsterVO(int no, int hp, int lv, int atk, int rcv,
			int hpmax, int atkmax, int rcvmax, float scalehp, float scaleatk, float scalercv,
			List<AwokenSkill> awokens,
			Pair<MonsterType, MonsterType> monsterType,
			MonsterType monsterType3,
			Pair<Integer, Integer> props,
			int rare, int cost,
			String url, int lv1, int lvmax, int op,List<AwokenSkill> super_awokens) {
		mImageUrl = url;
		mNo = no;
		mHp = hp;
		mLv = lv;
		mAtk = atk;
		mRecovery = rcv;
		mHpMax = hpmax;
		mAtkMax = atkmax;
		mRcvMax = rcvmax;
		hpScale = scalehp;
		atkScale = scaleatk;
		rcvScale = scalercv;
		mAwokenList = new ArrayList<MonsterSkill.AwokenSkill>(awokens);
		super_awoken = new ArrayList<AwokenSkill>(super_awokens);
		mMonsterProps = props;
		mMonsterTypes = monsterType;
		mMonsterType3 = monsterType3;
		mRare = rare;
		mCost = cost;
		slv1 = lv1;
		slvmax = lvmax;
		this.overpower = op;
	}
	
	public int getCost() {
	    return mCost;
	}
	
	public int getRare() {
	    return mRare;
	}
	
	public String getImageUrl() {
		return mImageUrl;
	}

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(TableMonsterData.Columns.ID, mNo);
		values.put(TableMonsterData.Columns.LV, mLv);
		values.put(TableMonsterData.Columns.HP, mHp);
		values.put(TableMonsterData.Columns.ATK, mAtk);
		values.put(TableMonsterData.Columns.RCV, mRecovery);
		values.put(TableMonsterData.Columns.MAX_LV, mLv);
		values.put(TableMonsterData.Columns.MAX_HP, mHpMax);
		values.put(TableMonsterData.Columns.MAX_ATK, mAtkMax);
		values.put(TableMonsterData.Columns.MAX_RCV, mRcvMax);
		int maxAwoken = (mAwokenList!=null)? mAwokenList.size():0;
		values.put(TableMonsterData.Columns.MAX_AWOKEN, maxAwoken);
		//LogUtil.e("id = " + mNo);

		for(int i=1; i<=maxAwoken; ++i) {
			values.put(TableMonsterData.Columns.AWOKEN+i, mAwokenList.get(i-1).ordinal());
		}
		int maxSAwoken = (super_awoken!=null)? super_awoken.size():0;
		for(int i=1; i<=maxSAwoken; ++i) {
			values.put(TableMonsterData.Columns.SUPER_AWOKEN+i, super_awoken.get(i-1).ordinal());
		}
		values.put(TableMonsterData.Columns.TYPE1, mMonsterTypes.first.ordinal());
		values.put(TableMonsterData.Columns.TYPE2, mMonsterTypes.second.ordinal());
		int t3 = -1;
		if ( mMonsterType3 != null && mMonsterType3 != MonsterType.NONE ) {
			t3 = mMonsterType3.ordinal();
		}
		values.put(TableMonsterData.Columns.TYPE3, t3);
		values.put(TableMonsterData.Columns.PROP1, mMonsterProps.first);
		values.put(TableMonsterData.Columns.PROP2, mMonsterProps.second);
		values.put(TableMonsterData.Columns.COST, mCost);
		values.put(TableMonsterData.Columns.RARE, mRare);
		values.put(TableMonsterData.Columns.SCALE_HP, hpScale);
		values.put(TableMonsterData.Columns.SCALE_ATK, atkScale);
		values.put(TableMonsterData.Columns.SCALE_RCV, rcvScale);
		values.put(TableMonsterData.Columns.URL, mImageUrl);
		values.put(TableMonsterData.Columns.SKILL_LV1, slv1);
		values.put(TableMonsterData.Columns.SKILL_LVMAX, slvmax);
		values.put(TableMonsterData.Columns.OVERPOWER, overpower);
		return values;
	}

	public static MonsterVO empty() {
		return new MonsterVO(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, new ArrayList<MonsterSkill.AwokenSkill>(), new Pair<MonsterSkill.MonsterType, MonsterSkill.MonsterType>(MonsterSkill.MonsterType.AWOKEN, MonsterSkill.MonsterType.NONE), MonsterSkill.MonsterType.NONE, new Pair<Integer, Integer>(0, -1), 1, 1, "", 1, 1, 0,new ArrayList<MonsterSkill.AwokenSkill>());
	}

}
