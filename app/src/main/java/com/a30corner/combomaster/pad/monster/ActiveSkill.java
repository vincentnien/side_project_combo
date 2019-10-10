package com.a30corner.combomaster.pad.monster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.a30corner.combomaster.provider.table.TableActiveSkill;

public class ActiveSkill extends Skill {

	public static enum SkillType {
		ST_NOT_SUPPORT(-1), ST_TRANSFORM(0), ST_COLOR_CHANGE(1), ST_CHANGE_THE_WORLD(
				2), ST_ADD_PLUS(3), ST_GRAVITY(4), ST_REDUCE_DEF(5), ST_POWER_UP(
				6), ST_TYPE_UP(7), ST_ONE_LINE_TRANSFORM(
				9), ST_POISON(10), ST_DELAY(11), ST_ATTACK(14), ST_DIRECT_ATTACK(15), ST_RANDOM_CHANGE(
				18), ST_TIME_EXTEND(19), ST_RANDOM_CHANGE_RESTRICT(20),
				ST_DROP_RATE(21), ST_ADD_LOCK(22), ST_CHANGE_SELF_ATTR(23),
				ST_TARGET_RANDOM(24), ST_CHANGE_ENEMY_ATTR(25), ST_DARK_SCREEN(26),
				ST_CLEAR_BOARD(27), ST_POWER_UP_BY_AWOKEN(28),
				ST_BIND_RECOVER(29), ST_REDUCE_SHIELD(30),
				ST_SKILL_BOOST(31), ST_MOVE_TIME_X(32),
				ST_RECOVER_HP(33), ST_COUNTER_ATTACK(34),
				ST_LEADER_SWITCH(35), ST_DISABLE_AWOKEN(36),
				ST_HP_1(37), ST_ATTACK_FIXED(38), ST_DROP_LOCK(39),
				ST_LINE_RANDOM_CHANGE(40), ST_ADD_COMBO(41), ST_GRAVITY_100(42),
				ST_RECOVER_UP(43), ST_SUPER_DARK(44),ST_AWOKEN_RECOVER(45),
				ST_DEF_UP_BY_AWOKEN(46),ST_VOID(47),ST_UNLOCK(48),
		ST_VOID_ATTR(49), ST_TURN_RECOVER(50), ST_ENHANCE_ORB(51),
		ST_NO_DROP(52), ST_HP_ATTACK(53), ST_TEAM_HP_ATTACK(54), ST_PERIOD_CHANGE(55),
		ST_POSITION_CHANGE(56), ST_CLOUD(57), ST_LOCK_REMOVE(58), ST_RANDOM_CHANGE_MULTIPLE(59),
		ST_RANDOM_CHANGE_RESTRICT_MORE(60), ST_L_FORMAT(61), ST_CROSS_FORMAT(62),
		ST_SQUARE_FORMAT(63), ST_DROP_ONLY(64), ST_VOID_0(65), ST_RECOVER_LOCK_REMOVE(66),
		ST_HP_CHANGE(67);

		private final int value;

		private SkillType(int v) {
			value = v;
		}

		public int value() {
			return value;
		}

		public static SkillType parse(int value) {
			return SkillType.values()[value];
		}
	}

	private SkillType mType;
	private List<Integer> mData = new ArrayList<Integer>();
	private int countDown = 0;
	private boolean firstEnemys = false;

	public ActiveSkill(SkillType type) {
		mType = type;
	}

	public ActiveSkill() {
		mType = SkillType.ST_NOT_SUPPORT;
	}
	
	public void setTurn(int index, boolean enemy) {
		countDown = mData.get(index);
		firstEnemys = enemy;
	}
	
	public boolean countDown() {
		if(firstEnemys && mType != SkillType.ST_SUPER_DARK) {
			firstEnemys = false;
			return false;
		}
		return --countDown == 0;
	}

	public int turns() {
		return countDown;
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

	public void setType(SkillType type) {
		mType = type;
	}

	public static ActiveSkill create(SkillType type) {
		return new ActiveSkill(type);
	}

	public static ActiveSkill empty() {
		return new ActiveSkill(SkillType.ST_NOT_SUPPORT);
	}

	public boolean notSupport() {
		return SkillType.ST_NOT_SUPPORT == mType;
	}

	public ContentValues toContentValues(int owner) {
		ContentValues values = new ContentValues();
		values.put(TableActiveSkill.Columns.OWNER, owner);
		int type = (mType!=null)? mType.ordinal():SkillType.ST_NOT_SUPPORT.ordinal();
		values.put(TableActiveSkill.Columns.TYPE, type);
		values.put(TableActiveSkill.Columns.DATA, TextUtils.join(",", mData));
		return values;
	}
	
	static int idx_owner = -1;
	static int idx_type;
	static int idx_data;
	
	public static ActiveSkill fromCursor(Cursor cursor) {
	    if ( cursor == null ) {
	        return null;
	    }
	    if ( idx_owner == -1 ) {
	        idx_owner = cursor.getColumnIndex(TableActiveSkill.Columns.OWNER);
	        idx_type = cursor.getColumnIndex(TableActiveSkill.Columns.TYPE);
	        idx_data = cursor.getColumnIndex(TableActiveSkill.Columns.DATA);
	    }
	    int typeId = cursor.getInt(idx_type);
	    String data = cursor.getString(idx_data);
	    ActiveSkill as = new ActiveSkill();
	    as.setType(SkillType.parse(typeId));
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
	    as.setData(list);
	    
	    return as;
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

	public SkillType getType() {
		return mType;
	}
}
