package com.a30corner.combomaster.playground;

import java.util.ArrayList;
import java.util.List;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.pad.enemy.EnemySkill;
import com.a30corner.combomaster.pad.enemy.EnemySkill.TYPE;
import com.a30corner.combomaster.pad.monster.LeaderSkill;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill.AwokenSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MoneyAwokenSkill;
import com.a30corner.combomaster.pad.monster.TeamInfo;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.RandomUtil;

public class Team {

	private TeamInfo team;
	private int mCurrentHp = 1000;
	private boolean[] mBindStatus = new boolean[6];
	private boolean isCopMode = false;

	private int mChangedHp = -1;
	
	public Team() {
		ComboMasterApplication instance = ComboMasterApplication.getsInstance();
		team = instance.getTargetTeam();
		isCopMode = instance.isCopMode();
		mCurrentHp = team.getHp();
		for (int i = 0; i < 6; ++i) {
			mBindStatus[i] = false;
		}
	}

	public MonsterInfo getMember(int index) {
		return team.getMember(index);
	}

	public TeamInfo team() {
		return team;
	}
	
	public void switchMember(int index) {
		team.switchMember(index);
		team.refresh(new boolean[]{false, false, false, false, false, false});
	}

	public void henshin(int i, int index) {
		team.henshin(i, index, mBindStatus);
	}

	public int getRealHp() {
		return team.getHp();
	}

	public int getHp() {
		if (mChangedHp == -1) {
			return team.getHp();
		} else {
			return mChangedHp;
		}
	}

	public void setMaxHp(int hp) {
		mChangedHp = hp;
		if (hp != -1 && (team.getHp() == mCurrentHp || mCurrentHp > hp)) {
			mCurrentHp = hp;
		}
		if (hp == -1 && mCurrentHp > team.getHp()) {
			mCurrentHp = team.getHp();
		}
	}

	public int getCurrentHp() {
		return mCurrentHp;
	}

	public void setCurrentHp(int currentHp) {
		mCurrentHp = currentHp;
	}

//	public boolean isLockedSkill() {
//		int count = 0;
//		for (int i = 0; i < 6; ++i) {
//			MonsterInfo info = getMember(i);
//			if (info == null || mBindStatus[i]) {
//				continue;
//			}
//			count += info
//					.getTargetAwokenCount(AwokenSkill.RESISTANCE_SKILL_LOCK);
//		}
//		return !RandomUtil.getLuck(count * 20);
//	}

	public interface IDamageCallback {
		public void onResult(double damage, List<Boolean> reduced);
	}


	public double reduceDamage(Enemy enemy, int damage, IDamageCallback callback) {
		double reduced = 0.0;
		List<Boolean> reduceList = new ArrayList<Boolean>();
		for (int i = 0; i < 6; ++i) {
			reduceList.add(false);
		}
		reduced = reduceFromLeader(enemy, damage, 1, reduceList);
		// first - reduce from awoken
		reduced = reduceFromAwoken(enemy, reduced, reduceList);

		callback.onResult(reduced, reduceList);
		return reduced;
	}
//
//	@Deprecated
//	public double reduceDamage(Enemy enemy, EnemySkill skill,
//			IDamageCallback callback) {
//		List<Boolean> reduceList = new ArrayList<Boolean>();
//		for (int i = 0; i < 6; ++i) {
//			reduceList.add(false);
//		}
//		int damage = 0;
//		double reduced = 0.0;
//		List<Integer> data = skill.getData();
//		if (skill.getSkillType() == TYPE.MULTI_HIT) {
//			damage = data.get(1);
//			int hit = data.get(0);
//
//			reduced = reduceFromLeader(enemy, damage, hit, reduceList);
//		} else if (skill.getSkillType() == TYPE.STRIKE) {
//			damage = data.get(0);
//
//			reduced = reduceFromLeader(enemy, damage, 1, reduceList);
//		}
//
//		// first - reduce from awoken
//		reduced = reduceFromAwoken(enemy, reduced, reduceList);
//		return reduced;
//	}

	private double reduceFromLeader(Enemy enemy, double damage, int hit,
			List<Boolean> reduceList) {
		MonsterInfo leader = getMember(0);
		MonsterInfo friend = getMember(5);

		double reduce = damage;
		if (leader != null && !mBindStatus[0]) {
			List<LeaderSkill> list = leader.getLeaderSkill();

			for (LeaderSkill ls : list) {
				reduce = reduceFromLeader(ls, enemy, reduce, hit);
			}
			if (damage != reduce) {
				reduceList.set(0, true);
			}
		}
		if (friend != null && !mBindStatus[5]) {
			double before = reduce;
			List<LeaderSkill> list = friend.getLeaderSkill();
			for (LeaderSkill ls : list) {
				reduce = reduceFromLeader(ls, enemy, reduce, hit);
			}
			if (before != reduce) {
				reduceList.set(5, true);
			}
		}

		return reduce;
	}

	private double reduceFromLeader(LeaderSkill skill, Enemy enemy,
			double damage, int hit) {
		List<Integer> data = skill.getData();
		double split = damage / hit;
		switch (skill.getType()) {
		case LST_DO_KONJO: {
			int percent = data.get(0);
			int hp = team.getHp();
			int currentHp = mCurrentHp;
			for (int i = 0; i < hit; ++i) {
				double hpPercent = currentHp * 100.0 / (double) hp;
				if (hpPercent >= percent && split >= currentHp) {
					currentHp = 1;
				} else {
					currentHp -= split;
				}
			}
			return mCurrentHp - currentHp;
		}
		case LST_REDUCE_BY_HP_CONDITION: {
			// format: [hpCondition, percent, typeCount, orbType1, orbType2,
			// ...etc]
			double remain = damage - split;
			boolean bigger = (data.size()>2);

			int percent = data.get(1);
			int hpCondition = data.get(0);

			int hp = team.getHp();
			if (hpCondition == 100) {
				if (hp == mCurrentHp) {
					return Math.ceil(split * (100 - percent) / 100.0) + remain;
				}
			} else if(!bigger) {// only 50 currently
				double hpPercent = mCurrentHp * 100.0 / hp;
				if (hpPercent <= hpCondition) {
					return Math.ceil(split * (100 - percent) / 100.0) + remain;
				}
			} else {
				double hpPercent = mCurrentHp * 100.0 / hp;
				if (hpPercent >= hpCondition) {
					return Math.ceil(split * (100 - percent) / 100.0) + remain;
				}
			}

			break;
		}
		case LST_REDUCE_DAMAGE: {
			// format: [percent, typeCount, orbType1, orbType2, ...etc]
			double remain = damage - split;

			int percent = data.get(0);
			int typeCount = data.get(1);
			List<Integer> types = data.subList(2, 2 + typeCount);
			if (isSameProp(enemy, types)) {
				return Math.ceil(split * (100 - percent) / 100.0) + remain;
			}
			break;
		}
		default:
		}
		return damage;
	}

	private boolean isSameProp(Enemy enemy, List<Integer> types) {
		// if is all props
		if (types.size() == 1 && types.get(0) == -1) {
			return true;
		}
		for (Integer type : types) {
			if (enemy.currentProp() == type) {
				return true;
			}
		}
		return false;
	}

	private double reduceFromAwoken(Enemy enemy, double damage,
			List<Boolean> reduceList) {
		if (ComboMasterApplication.getsInstance().isAwokenNulled()) {
			return damage;
		}
		int prop = enemy.currentProp();
		int totalCount = 0;
		int moneyCount = 0;
		int moneyCount_plus = 0;
		AwokenSkill[] mapping = { AwokenSkill.REDUCE_DARK,
				AwokenSkill.REDUCE_FIRE, AwokenSkill.AUTO_RECOVER,
				AwokenSkill.REDUCE_LIGHT, AwokenSkill.REDUCE_WATER,
				AwokenSkill.REDUCE_WOOD };
		MoneyAwokenSkill[] moneyMapping = { MoneyAwokenSkill.REDUCE_DARK,
				MoneyAwokenSkill.REDUCE_FIRE, MoneyAwokenSkill.AUTO_RECOVERY,
				MoneyAwokenSkill.REDUCE_LIGHT, MoneyAwokenSkill.REDUCE_WATER,
				MoneyAwokenSkill.REDUCE_WOOD };
		MoneyAwokenSkill[] moneyPlus = {
				MoneyAwokenSkill.REDUCE_DARK_PLUS,
				MoneyAwokenSkill.REDUCE_FIRE_PLUS, MoneyAwokenSkill.AUTO_RECOVERY,
				MoneyAwokenSkill.REDUCE_LIGHT_PLUS, MoneyAwokenSkill.REDUCE_WATER_PLUS,
				MoneyAwokenSkill.REDUCE_WOOD_PLUS
		};
		for (int i = 0; i < 6; ++i) {
			MonsterInfo info = getMember(i);
			if (info == null || mBindStatus[i]) {
				continue;
			}
			int count = info.getTargetAwokenCount(mapping[prop], isCopMode);
			int mcount = info.getTargetPotentialAwokenCount(moneyMapping[prop]);
			int mcount_plus = info.getTargetPotentialAwokenCount(moneyPlus[prop]);
			if (count != 0) {
				reduceList.set(i, true);
			}
			if (mcount != 0) {
				reduceList.set(i, true);
			}
			if (mcount_plus != 0) {
				reduceList.set(i, true);
			}
			totalCount += count;
			moneyCount += mcount;
			moneyCount_plus += mcount_plus;
		}

		double reduced = (1.0 - totalCount * 0.07 - moneyCount * 0.01 - moneyCount_plus * 0.025);
		if (reduced < 0.0) {
			reduced = 0.0;
		}
		return Math.ceil(damage * reduced);
	}

	public boolean[] bindStatus() {
		return mBindStatus;
	}

	public boolean bindPet(int index, boolean binded) {
		mBindStatus[index] = binded;
		if (index == 0 || index == 5) {
			team.refresh(mBindStatus);
			int hp = team.getHp();
			if (mCurrentHp > hp) {
				mCurrentHp = hp;
			}
		}
		return false;
	}

}
