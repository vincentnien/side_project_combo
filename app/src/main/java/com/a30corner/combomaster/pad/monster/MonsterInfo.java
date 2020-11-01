package com.a30corner.combomaster.pad.monster;

import android.content.Context;
import android.util.Pair;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.pad.monster.MonsterSkill.AwokenSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MoneyAwokenSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MonsterType;
import com.a30corner.combomaster.provider.LocalDBHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MonsterInfo {

	private MonsterVO mMonsterVo, mMonsterVo2 = null;
	private int index = 0;
	private int mNo;
	private int mLv;
	private int mEgg1;
	private int mEgg2;
	private int mEgg3;
	private int mAwoken;
	private int mCd = 5;
	private int mCd2 = 5;
	private int mNo2 = 0;

	private int mHpAdd, mAtkAdd, mRcvAdd;
	private List<MoneyAwokenSkill> mPotentialList;
	private AwokenSkill mSuperAwoken = null;

	private MonsterInfo(int no, MonsterVO vo) {
		mNo = no;
		mMonsterVo = vo;
	}

	public static MonsterInfo load(Context context, int no, int lv, int egg1,
			int egg2, int egg3, int awoken, List<MoneyAwokenSkill> list,
			int cd1, int cd2, int hpa, int atka, int rcva, MonsterVO vo2, AwokenSkill superAwoken) throws IOException {
		MonsterInfo info = new MonsterInfo(no, LocalDBHelper.getMonsterData(context, no));
		info.setHpEgg(egg1);
		info.setAtkEgg(egg2);
		info.setRcvEgg(egg3);
		info.setLv(lv);
		info.setAwoken(awoken);
		info.setPotentialAwokenList(list);
		info.mCd = cd1;
		info.mCd2 = cd2;
		info.mHpAdd = hpa;
		info.mAtkAdd = atka;
		info.mRcvAdd = rcva;
		info.mMonsterVo2 = vo2;
		info.mSuperAwoken = superAwoken;
		return info;
	}

	public void henshin(Context context, int no) {
		mMonsterVo = LocalDBHelper.getMonsterData(context, no);
		mNo = no;
		setAwoken(9);
		mCd = mMonsterVo.slvmax;
	}
	
	public List<ActiveSkill> getActiveSkill2() {
		if(mMonsterVo2 != null) {
			return mMonsterVo2.getActiveSkill();
		}
		return null;
	}

	public int getAssistantNo() {
		if(mMonsterVo2 != null) {
			return mMonsterVo2.getNo();
		}
		return mNo2;
	}
	
	public static MonsterInfo create(int index, int no, int lv, int egg1,
            int egg2, int egg3, int awoken, List<MoneyAwokenSkill> list, int super_awoken, MonsterVO vo, int no2) {
	    MonsterInfo info = new MonsterInfo(no, vo);
        info.setHpEgg(egg1);
        info.setAtkEgg(egg2);
        info.setRcvEgg(egg3);
        info.setLv(lv);
        info.setAwoken(awoken);
        info.setPotentialAwokenList(list);
        info.index = index;
		info.mSuperAwoken = AwokenSkill.get(super_awoken);
		info.mNo2 = no2;
        return info;
	}
	
	public static MonsterInfo empty(int index) {
	    return create(index, -1, 0, 0, 0, 0, 0, new ArrayList<MonsterSkill.MoneyAwokenSkill>(0), -1, MonsterVO.empty(), 0);
	}

	private void setHpEgg(int egg) {
		mEgg1 = egg;
	}

	private void setAtkEgg(int egg) {
		mEgg2 = egg;
	}

	private void setRcvEgg(int egg) {
		mEgg3 = egg;
	}

	private void setAwoken(int awoken) {
		mAwoken = awoken;
	}

	// private -> public for special mode .. well.. any good idea?
	public void setLv(int lv) {
		mLv = lv;
	}

	public int getIndex() {
	    return index;
	}
	
	public int getLv() {
		return mLv;
	}

	public int getAwoken() {
		return mAwoken;
	}
	
	public void setPotentialAwokenList(List<MoneyAwokenSkill> list) {
		mPotentialList = list;
	}
	
	public List<MoneyAwokenSkill> getPotentialAwokenList() {
		return mPotentialList;
	}

	public boolean isAwokenMax() {
		return (mAwoken != 0) && mAwoken >= mMonsterVo.getAwokenList().size();
	}

	public int getEgg1() {
		return mEgg1;
	}

	public int getEgg2() {
		return mEgg2;
	}

	public int getEgg3() {
		return mEgg3;
	}

	public int get297() {
		return mEgg1 + mEgg2 + mEgg3;
	}

	public int getNo() {
		return mNo;
	}
	
	public int getCd() {
	    return mCd;
	}
	
	public int getCd2() {
		return mCd2;
	}

	public int getRecovery() {
		return getRecovery(false);
	}

	//public AwokenSkill getSuperAwoken() { return mSuperAwoken; }
	
	public int getRecovery(boolean isMultiMode) {
		int addition = 200 * getTargetAwokenCount(AwokenSkill.ENHANCE_HEAL, isMultiMode);
		int rcv = mMonsterVo.getRecovery(mLv);
		double potentialRcv = getTargetPotentialAwokenCount(MoneyAwokenSkill.ENHANCE_RCV) *Math.ceil(rcv * 0.1) +
				getTargetPotentialAwokenCount(MoneyAwokenSkill.ENHANCE_RCV_PLUS) *Math.ceil(rcv * 0.3) +
				getTargetPotentialAwokenCount(MoneyAwokenSkill.ALL_UP) * Math.ceil(rcv * 0.2);
		int total = rcv + mEgg3 * 3 + addition + (int)potentialRcv + mRcvAdd;
		
		if(isMultiMode) {
			int matchCount = getTargetAwokenCount(AwokenSkill.MATCH_BOOST, true);
			if (matchCount > 0) {
				total *= Math.pow(1.5, matchCount);
			}
		}
		int rcvDown = getTargetAwokenCount(AwokenSkill.RCV_DOWN, isMultiMode);
		total = total - 2000 * rcvDown;
		return total;
	}

	public int getHP() {
		return getHP(false);
	}
	
	public int getHP(boolean isMultiMode) {
		int addition = 500 * getTargetAwokenCount(AwokenSkill.ENHANCE_HP, isMultiMode);
		int hp = mMonsterVo.getHp(mLv);
		double potentialHp = getTargetPotentialAwokenCount(MoneyAwokenSkill.ENHANCE_HP) * Math.ceil(hp * 0.015) +
				getTargetPotentialAwokenCount(MoneyAwokenSkill.ENHANCE_HP_PLUS) * Math.ceil(hp * 0.045) +
				getTargetPotentialAwokenCount(MoneyAwokenSkill.ALL_UP) * Math.ceil(hp * 0.03);
		int total = hp + mEgg1 * 10 + addition + (int)potentialHp + mHpAdd;
		
		if(isMultiMode) {
			int matchCount = getTargetAwokenCount(AwokenSkill.MATCH_BOOST, true);
			if (matchCount > 0) {
				total *= Math.pow(1.5, matchCount);
			}
		}
		int hpDown = getTargetAwokenCount(AwokenSkill.HP_DOWN, isMultiMode);
		total = total - 2000 * hpDown;
		return total>0 ? total:1;
	}

	public int getAtk() {
		return getAtk(false);
	}
	
	public int getAtk(boolean isMultiMode) {
		int addition = 100 * getTargetAwokenCount(AwokenSkill.ENHANCE_ATK, isMultiMode);
		int atk = mMonsterVo.getAtk(mLv);
		double potentialAtk = getTargetPotentialAwokenCount(MoneyAwokenSkill.ENHANCE_ATK) * Math.ceil(atk * 0.01) +
				getTargetPotentialAwokenCount(MoneyAwokenSkill.ENHANCE_ATK_PLUS) * Math.ceil(atk * 0.03) +
				getTargetPotentialAwokenCount(MoneyAwokenSkill.ALL_UP) * Math.ceil(atk * 0.02);
		int total = atk + mEgg2 * 5 + addition + (int)potentialAtk + mAtkAdd;
		if(isMultiMode) {
			int matchCount = getTargetAwokenCount(AwokenSkill.MATCH_BOOST, true);
			if (matchCount > 0) {
				// I don't think there will be more than 1 of this
				//total *= Math.pow(1.5, matchCount);
				// but it happened .. XD , 2017/12/17
				// 
				total *= Math.pow(1.5, matchCount);
			}
			
		}
		int atkDown = getTargetAwokenCount(AwokenSkill.ATK_DOWN, isMultiMode);
		total = total - 1000 * atkDown;
		return total>0 ? total:1;
	}
	
	public int getCost() {
	    if ( mMonsterVo != null ) {
	        return mMonsterVo.getCost();
	    }
	    return 1;
	}
	
	public int getRare() {
	    if ( mMonsterVo != null ) {
	        return mMonsterVo.getRare();
	    }
	    return 1;
	}

	public List<LeaderSkill> getLeaderSkill() {
		return mMonsterVo.getLeaderSkill();
	}

	public List<ActiveSkill> getActiveSkill() {
		return mMonsterVo.getActiveSkill();
	}

	public List<AwokenSkill> getAwokenSkills(boolean isMultiMode) {
		List<AwokenSkill> addition = new ArrayList<AwokenSkill>();
		if(mMonsterVo2 != null && mMonsterVo2.getAwokenList().contains(AwokenSkill.ASSISTANT)) {
			addition.addAll(mMonsterVo2.getAwokenList());
		}
		if (mAwoken > 0) {
			List<AwokenSkill> list = mMonsterVo.getAwokenList();
			int size = list.size();
			int end = (mAwoken >= size) ? size : mAwoken;
			addition.addAll(list.subList(0, end));
		}

		if(!isMultiMode && mSuperAwoken != null && mSuperAwoken != AwokenSkill.SKILL_NONE) {
			addition.add(mSuperAwoken);
		}
		return addition;
	}

	public int getTargetPotentialAwokenCount(MoneyAwokenSkill skill) {
		if ( skill == null  || ComboMasterApplication.getsInstance().isAwokenNulled() ) {
			return 0;
		}
		int count = 0;
		List<MoneyAwokenSkill> list = getPotentialAwokenList();
		if ( list != null && list.size() > 0 ) {
			for(MoneyAwokenSkill s : list) {
				if (skill.ordinal() == s.ordinal()) {
					++count;
				}
			}
		}
		return count;
	}

	public int getTargetMoneyAwokenCount(MoneyAwokenSkill skill) {
		if(skill == null || ComboMasterApplication.getsInstance().isAwokenNulled()) {
			return 0;
		}

		int count = 0;
		List<MoneyAwokenSkill> list = mPotentialList;
		for(MoneyAwokenSkill s : list) {
			if(s == skill) {
				++count;
			}
		}
		return count;
	}

		public int getTargetAwokenCount(AwokenSkill skill, boolean isMultiMode) {
			if (skill == null || ComboMasterApplication.getsInstance().isAwokenNulled()) {
				return 0;
			}
			int count = 0;
			List<AwokenSkill> list = getAwokenSkills(isMultiMode);//mMonsterVo.getAwokenList();

			int size = list.size();
			for (int i = 0; i < size; ++i) {
				if (skill == list.get(i)) {
					++count;
				}
			}

		return count;
	}

	public Pair<Integer, Integer> getProps() {
		return mMonsterVo.getMonsterProps();
	}

	public Pair<MonsterType, MonsterType> getMonsterTypes() {
		return mMonsterVo.getMonsterTypes();
	}

	public MonsterType getMonsterType3() {
		return mMonsterVo.getMonsterType3();
	}
	
	public final MonsterVO getMonsterVO() {
		return mMonsterVo;
	}
}
