package com.a30corner.combomaster.pad.enemy;

import java.util.ArrayList;
import java.util.List;

import com.a30corner.combomaster.utils.RandomUtil;

public class SkillCondition {

	public enum CONDITION {
		HP, BUFF, PERCENTAGE, CONSUMABLE, NO_CONDITION, FIRST_STRIKE, DEFAULT,
	}

	private List<CONDITION> conditions;
	private List<List<Integer>> data;

	private int fireCount = 0;

	public SkillCondition() {
		conditions = new ArrayList<SkillCondition.CONDITION>();
		data = new ArrayList<List<Integer>>();
	}

	public void addCondition(CONDITION cond, List<Integer> data) {
		conditions.add(cond);
		this.data.add(data);
	}

	public static SkillCondition newCondition(CONDITION cond, int... data) {
		SkillCondition condition = new SkillCondition();
		List<Integer> listData = new ArrayList<Integer>();
		for (int d : data) {
			listData.add(d);
		}
		condition.addCondition(cond, listData);
		return condition;
	}

	public boolean isFirstStrike() {
		return conditions.get(0) == CONDITION.FIRST_STRIKE;
	}

	public boolean matchCondition(Object someobj, EnemyData enemy,
			double damaged) {
		int count = 0;
		boolean success = true;
		for (CONDITION c : conditions) {
			switch (c) {
			case BUFF:
				break;
			case CONSUMABLE:
				success = success && (fireCount < data.get(count).get(0));
				break;
			case FIRST_STRIKE:
				success = success && (fireCount == 1);
				break;
			case HP: {
				int percent = data.get(count).get(0);
				double current = (1.0 - (damaged / enemy.getHp())) * 100;
				success = (current <= percent);
				break;
			}
			case PERCENTAGE: {
				int percent = data.get(count).get(0);
				success = success && RandomUtil.getLuck(percent);
				break;
			}
			case DEFAULT:
			case NO_CONDITION:
			default:
				break;

			}
			++count;
		}
		if (success) {
			++fireCount;
		}
		return success;
	}
}
