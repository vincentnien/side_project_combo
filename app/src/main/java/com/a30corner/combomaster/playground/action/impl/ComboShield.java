package com.a30corner.combomaster.playground.action.impl;

import rx.functions.Action0;

import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;

public class ComboShield extends EnemyAction {

	public ComboShield(String title, String description, int... val) {
		this(val);
		setDescription(title, description);
	}
	
    public ComboShield(int... val) {
        super(Act.COMBO_SHIELD, val);
    }

    @Override
    public void doAction(IEnvironment env, final Enemy enemy, final ICastCallback callback) {
        super.doAction(env, enemy, callback);
        
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
		        enemy.addBuff(Type.COMBO_SHIELD, data);
		        callback.onCastFinish(true);
			}
		});
    }

}
