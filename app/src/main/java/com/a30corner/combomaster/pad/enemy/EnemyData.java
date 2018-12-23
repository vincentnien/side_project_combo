package com.a30corner.combomaster.pad.enemy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Pair;

public class EnemyData {

	private int mNo;
	private int mBasicAtk;
	private int mAttackTurns;
	private int mHp;
	private int mDefence;

	private int mHpWidth;

	private int mEnemySize;

	private Pair<Integer, Integer> mProps;
	private List<Pair<SkillCondition, EnemySkill>> mSkillList;

	public void addSkill(Pair<SkillCondition, EnemySkill> skill) {
		if (mSkillList == null) {
			mSkillList = new ArrayList<Pair<SkillCondition, EnemySkill>>();
		}
		mSkillList.add(skill);
	}

	public EnemyData() {
		mEnemySize = 1;
	}

	public EnemyData(int no, int atk, int turns, int hp, int defence,
			Pair<Integer, Integer> props,
			List<Pair<SkillCondition, EnemySkill>> skills) {
		mNo = no;
		mBasicAtk = atk;
		mAttackTurns = turns;
		mHp = hp;
		mDefence = defence;
		mProps = props;
		mSkillList = skills;
		mEnemySize = 1;
	}

	public int getNo() {
		return mNo;
	}

	public Pair<Integer, Integer> getProps() {
		return mProps;
	}

	public void setSize(int size) {
		mEnemySize = size;
	}

	public int getSize() {
		return mEnemySize;
	}

	public int getHpWidth() {
		return mHpWidth;
	}

	public void setProps(Pair<Integer, Integer> props) {
		mProps = props;
	}

	public int getAttackTurns() {
		return mAttackTurns;
	}

	public int getAtk() {
		return mBasicAtk;
	}

	public List<Pair<SkillCondition, EnemySkill>> getSkillList() {
		if (mSkillList != null) {
			return Collections.unmodifiableList(mSkillList);
		} else {
			return new ArrayList<Pair<SkillCondition, EnemySkill>>(0);
		}
	}

	public int getDefence() {
		return mDefence;
	}

	public int getHp() {
		return mHp;
	}
}
