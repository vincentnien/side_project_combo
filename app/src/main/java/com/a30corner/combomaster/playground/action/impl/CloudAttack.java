package com.a30corner.combomaster.playground.action.impl;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.scene.PlaygroundGameScene;
import com.a30corner.combomaster.utils.Constants;

import rx.functions.Action0;

public class CloudAttack extends EnemyAction {

	public CloudAttack(String title, String description, int... val) {
		this(val);
		setDescription(title, description);
	}

    public CloudAttack(int... val) {
        super(Act.CLOUD, val);
    }

    @Override
    public void doAction(IEnvironment env, Enemy enemy, final ICastCallback callback) {
        super.doAction(env, enemy, callback);

		env.toastEnemyAction(this, enemy);

		ActiveSkill skill = new ActiveSkill(ActiveSkill.SkillType.ST_CLOUD);
		skill.setData(data);

		env.fireGlobalSkill(Constants.SK_CLOUD, skill, callback);

    }

}
