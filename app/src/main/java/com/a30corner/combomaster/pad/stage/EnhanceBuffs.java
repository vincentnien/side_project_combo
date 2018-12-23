package com.a30corner.combomaster.pad.stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.util.Pair;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.utils.LogUtil;

public class EnhanceBuffs {
	private List<Pair<ActiveSkill, AtomicInteger>> mBuffs;

	public EnhanceBuffs() {
		mBuffs = new ArrayList<Pair<ActiveSkill, AtomicInteger>>();
	}

	public boolean hasBuff() {
		return mBuffs.size() > 0;
	}

	public void clearBuff() {
		mBuffs.clear();
	}

	public void addBuff(ActiveSkill skill) {
		mBuffs.add(new Pair<ActiveSkill, AtomicInteger>(skill,
				new AtomicInteger(0)));
	}

	public ActiveSkill getPowerUpSkill() {
		int size = mBuffs.size();
		for (int i = 0; i < size; ++i) {
			Pair<ActiveSkill, AtomicInteger> pair = mBuffs.get(i);
			SkillType type = pair.first.getType();
			if (type == SkillType.ST_POWER_UP || type == SkillType.ST_TYPE_UP) {
				LogUtil.d("find power up skill=", pair.first.toString());
				return pair.first;
			}
		}
		return null;
	}

	public void oneTurnPassed() {
		List<Integer> removeList = new ArrayList<Integer>();
		int size = mBuffs.size();
		for (int i = 0; i < size; ++i) {
			Pair<ActiveSkill, AtomicInteger> pair = mBuffs.get(i);
			int fired = pair.second.incrementAndGet();
			if (fired >= pair.first.getData().get(0)) {
				removeList.add(i);
			}
		}
		int rsize = removeList.size();
		for (int i = rsize - 1; i >= 0; --i) {
			mBuffs.remove((int) removeList.get(i));
		}
	}
}
