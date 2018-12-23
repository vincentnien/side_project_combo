package com.a30corner.combomaster.playground.action.impl;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;

import rx.functions.Action0;

public class RecoveryUpAction extends EnemyAction {

	public RecoveryUpAction(String title, String description, int... val) {
		this(val);
		setDescription(title, description);
	}

    public RecoveryUpAction(int... val) {
        super(Act.RECOVERY_UP, val);
    }

    @Override
    public void doAction(final IEnvironment env, final Enemy enemy, final ICastCallback callback) {
        super.doAction(env, enemy, callback);
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
				ActiveSkill skill = new ActiveSkill(ActiveSkill.SkillType.ST_RECOVER_UP);
				skill.setData(data);
				env.fireSkill(null, skill, callback);
			}
		});
        
    }

}
