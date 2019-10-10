package com.a30corner.combomaster.pad.monster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.a30corner.combomaster.provider.table.TableLeaderSkill;

public class LeaderSkill {

	public enum LeaderSkillType {
		LST_NOT_SUPPORT(-1), LST_FACTOR(0), LST_COLOR_FACTOR(1), LST_HP_FACTOR(
				2), LST_COMBO_FACTOR(3), LST_MULTI_ORB_COMBO(4), LST_TARGET_ORB_COMBO(
				5), LST_FACTOR_AND_COMBO_FACTOR(6), LST_FACTOR_AND_HP_FACTOR(7),
				LST_REDUCE_DAMAGE(8), LST_REDUCE_BY_HP_CONDITION(9), LST_DO_KONJO(10)
				, LST_HEAL(11), LST_USED_SKILL(12), LST_FIVE_ORB(13),
				LST_MULTI_ORB_COMBO_MULTI(14), LST_MULTI_PLAY(15),
				LST_CROSS(16), LST_EXTEND_TIME(17),
				LST_COUNT_ORB(18), LST_CROSS_P(19), LST_HP_RECOVER(20),
				LST_CROSS_ATTACK(21), LST_MORE_REMOVE(22), LST_ATTACK(23),
				LST_NO_DROP(24), LST_COMBO_RCV(25),LST_BOARD(26),
		LST_COLOR_SHIELD(27),LST_COMBO_SHIELD(28),LST_TARGET_ORB_SHIELD(29),
		LST_TIME_FIXED(30), LST_DROP_NO_MORE(31), LST_MULTI_ORB_SHIELD(32),
		LST_MULTI_ORB_SHIELD_MULTI(33), LST_MULTI_ORB_RCV(34),LST_L_ATTACK(35),
		LST_COMBO_ATTACK(36), LST_COLOR_COMBO(37), LST_COUNTER_STRIKE(38),
		LST_NO_POISON(39), LST_HEART_FACTOR(40), LST_COLOR_DIRECT_ATTACK(41), LST_MULTI_ORB_DIRECT_ATTACK(42);

		private final int value;

		LeaderSkillType(int v) {
			value = v;
		}

		public int value() {
			return value;
		}

		public static LeaderSkillType parse(int value) {
			LeaderSkillType[] values = LeaderSkillType.values();
			if ( value < 0 || value >= values.length ) {
				return LeaderSkillType.LST_NOT_SUPPORT;
			}
			return values[value];
		}
	}

	private LeaderSkillType mType;
	private List<Integer> mData = new ArrayList<Integer>();

	public LeaderSkill(LeaderSkillType type) {
		mType = type;
	}

	public LeaderSkill() {
		mType = LeaderSkillType.LST_NOT_SUPPORT;
	}

	public void addData(int... data) {
		for (int d : data) {
			mData.add(d);
		}
	}

	public void setData(List<Integer> data) {
		mData.addAll(data);
	}

	public List<Integer> getData() {
		return Collections.unmodifiableList(mData);
	}

	public void setType(LeaderSkillType type) {
		mType = type;
	}

	public static LeaderSkill create(LeaderSkillType type) {
		return new LeaderSkill(type);
	}

	public static LeaderSkill empty() {
		return new LeaderSkill(LeaderSkillType.LST_NOT_SUPPORT);
	}

	public boolean notSupport() {
		return LeaderSkillType.LST_NOT_SUPPORT == mType;
	}

	public ContentValues toContentValues(int owner) {
		ContentValues values = new ContentValues();
		values.put(TableLeaderSkill.Columns.OWNER, owner);
		int type = (mType!=null)? mType.ordinal():LeaderSkillType.LST_NOT_SUPPORT.ordinal();
		values.put(TableLeaderSkill.Columns.TYPE, type);
		values.put(TableLeaderSkill.Columns.DATA, TextUtils.join(",", mData));
		return values;
	}
	

    static int idx_owner = -1;
    static int idx_type;
    static int idx_data;
    
    public static LeaderSkill fromCursor(Cursor cursor) {
        if ( cursor == null ) {
            return null;
        }
        if ( idx_owner == -1 ) {
            idx_owner = cursor.getColumnIndex(TableLeaderSkill.Columns.OWNER);
            idx_type = cursor.getColumnIndex(TableLeaderSkill.Columns.TYPE);
            idx_data = cursor.getColumnIndex(TableLeaderSkill.Columns.DATA);
        }
        int typeId = cursor.getInt(idx_type);
        String data = cursor.getString(idx_data);
        LeaderSkill ls = new LeaderSkill();
        ls.setType(LeaderSkillType.parse(typeId));
        List<Integer> list = new ArrayList<Integer>();
        
        if ( !TextUtils.isEmpty(data) ) {
            String[] split = data.split(",");
            if ( split != null ) {
                for(String s : split) {
                    try {
                        list.add(Integer.parseInt(s));
                    } catch(Exception e) {
                        // skip
                    }
                }
            }
        }
        ls.setData(list);
        
        return ls;
    }
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(mType.toString());
		sb.append("-");
		for (Integer d : mData) {
			sb.append(d);
			sb.append(",");
		}
		return sb.toString();
	}

	public LeaderSkillType getType() {
		return mType;
	}
}
