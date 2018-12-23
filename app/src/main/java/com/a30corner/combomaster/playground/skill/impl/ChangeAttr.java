package com.a30corner.combomaster.playground.skill.impl;

import java.util.List;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.playground.skill.Skill;

public class ChangeAttr extends Skill {

	public static void onFire(IEnvironment env, Member owner, ActiveSkill skill,
			ICastCallback callback) {
		List<Enemy> list = env.getEnemies();
		int attribute = skill.getData().get(0);
		for(Enemy enemy : list) {
			enemy.changeAttribute(attribute);
		}
		callback.onCastFinish(true);
	}

	@Override
	public SkillType getSkillType() {
		return SkillType.ST_CHANGE_ENEMY_ATTR;
	}

}
