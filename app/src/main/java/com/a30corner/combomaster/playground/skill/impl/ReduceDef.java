package com.a30corner.combomaster.playground.skill.impl;

import java.util.List;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.playground.skill.Skill;
import com.a30corner.combomaster.utils.Constants;

public class ReduceDef extends Skill {

	public static void onFire(IEnvironment env, Member owner, ActiveSkill skill,
			ICastCallback callback) {
		List<Enemy> list = env.getEnemies();
		for(Enemy enemy : list) {
			enemy.addDebuff(owner, Type.REDUCE_DEF, skill.getData());
		}
		env.fireGlobalSkill(Constants.SK_REDUCE_DEF, skill, callback);
		//callback.onCastFinish(true);
	}

	@Override
	public SkillType getSkillType() {
		return SkillType.ST_REDUCE_DEF;
	}

}
