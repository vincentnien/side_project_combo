package com.a30corner.combomaster.playground.action.impl;

import rx.functions.Action0;

import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;

public class DamageAbsorbShield extends EnemyAction {

    public DamageAbsorbShield(String title, String description, int... val) {
        super(Act.DAMAGE_ABSORB_SHIELD, val);
        setDescription(title, description);
    }
	
    public DamageAbsorbShield(int... val) {
        super(Act.DAMAGE_ABSORB_SHIELD, val);
    }

    @Override
    public void doAction(IEnvironment env, final Enemy enemy, final ICastCallback callback) {
        super.doAction(env, enemy, callback);
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
		        enemy.addBuff(Type.DAMAGE_ABSORB_SHIELD, data);
		        callback.onCastFinish(true);
			}
		});
    	

    }

}
