package com.a30corner.combomaster.pad.monster;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.util.Pair;

import com.a30corner.combomaster.pad.monster.MonsterSkill.AwokenSkill;

@Deprecated
public class MonsterData {

	private final String mImageUrl;
	private final int mNo;
	private final int mLv; // max
	private final int mHp; // min
	private final int mAtk; // min
	private final int mRecovery; // min
	private final Pair<String, String> mSkill;
	private final Pair<String, String> mLeaderSkill;
	private final List<String> mAwokens;
	private final String mProps;
	private final String mTypes;
	private final int mHpMax;
	private final int mAtkMax;
	private final int mRcvMax;

	private final float hpScale;
	private final float atkScale;
	private final float rcvScale;

	public static enum MonsterType {
		DRAGON, DEVIL, BALANCED, ATTACKER, PHYSICAL, HEALER, GOD, EVO_MATERIAL, ENHANCE_MATERIAL, SPECIAL_PROTECTED
	}

	private static final String[] TYPES = { "龍", "惡魔", "平衡", "攻擊", "體力", "回復",
			"神", "進化用", "強化合成用", "特別保護", "無" };

	private static final String[] AWOKENS = { "HP強化", "攻撃強化", "回復強化", "火傷害減輕",
			"水傷害減輕", "木傷害減輕", "光傷害減輕", "暗傷害減輕", "自動回復", "封鎖耐性", "黑暗耐性",
			"妨礙珠耐性", "毒耐性", "火＋珠強化", "水＋珠強化", "木＋珠強化", "光＋珠強化", "暗＋珠強化",
			"轉珠時間延長", "封鎖回復", "技能冷卻提升", "火屬性強化", "水屬性強化", "木屬性強化", "光屬性強化",
			"暗屬性強化", "二體攻撃", "封印耐性" };

	private static final String[] PROPS = { "暗", "火", "回復", "光", "水", "木" };

	private int getPropId(String prop) {
		if (TextUtils.isEmpty(prop)) {
			return -1;
		}
		prop = prop.trim();
		for (int i = 0; i < PROPS.length; ++i) {
			if (PROPS[i].equals(prop)) {
				return i;
			}
		}
		return -1;
	}

	private int getTypeId(String type) {
		if (TextUtils.isEmpty(type)) {
			return -1;
		}
		type = type.trim();
		for (int i = 0; i < TYPES.length; ++i) {
			if (TYPES[i].equals(type)) {
				return i;
			}
		}
		return -1;
	}

	private AwokenSkill getAwoken(String awoken) {

		return AwokenSkill.get(getAwokenId(awoken));
	}

	private int getAwokenId(String awoken) {
		if (TextUtils.isEmpty(awoken)) {
			return -1;
		}
		awoken = awoken.trim();
		for (int i = 0; i < AWOKENS.length; ++i) {
			if (AWOKENS[i].equals(awoken)) {
				return i;
			}
		}
		return -1;
	}

	protected MonsterData(int no, String image, int lv, int hp, int atk,
			int rec, Pair<String, String> skill, Pair<String, String> leader,
			List<String> awokens, String props, String types, int hpMax,
			int atkMax, int rcvMax, float hpScale, float atkScale,
			float rcvScale) {
		mImageUrl = image;
		mNo = no;
		mLv = lv;
		mHp = hp;
		mAtk = atk;
		mRecovery = rec;
		mSkill = skill;
		mLeaderSkill = leader;
		mAwokens = awokens;
		mProps = props;
		mTypes = types;
		mHpMax = hpMax;
		mAtkMax = atkMax;
		mRcvMax = rcvMax;
		this.hpScale = hpScale;
		this.atkScale = atkScale;
		this.rcvScale = rcvScale;
	}

	public String getImageUrl() {
		return mImageUrl;
	}

	public int getNo() {
		return mNo;
	}

	public int getLv() {
		return mLv;
	}

	public int getHp() {
		return mHp;
	}

	public int getAtk() {
		return mAtk;
	}

	public int getRecovery() {
		return mRecovery;
	}

	public Pair<String, String> getSkill() {
		return mSkill;
	}

	public Pair<String, String> getLeaderSkill() {
		return mLeaderSkill;
	}

	public List<String> getAwokensRaw() {
		return mAwokens;
	}

	public List<AwokenSkill> getAwokens() {
		List<AwokenSkill> list = new ArrayList<MonsterSkill.AwokenSkill>();
		for (String awoken : mAwokens) {
			list.add(getAwoken(awoken));
		}
		return list;
	}

	public String getProps() {
		return mProps;
	}

	public String getTypes() {
		return mTypes;
	}

	public int getHpMax() {
		return mHpMax;
	}

	public int getAtkMax() {
		return mAtkMax;
	}

	public int getRcvMax() {
		return mRcvMax;
	}

	public float getHpScale() {
		return hpScale;
	}

	public float getAtkScale() {
		return atkScale;
	}

	public float getRcvScale() {
		return rcvScale;
	}
}
