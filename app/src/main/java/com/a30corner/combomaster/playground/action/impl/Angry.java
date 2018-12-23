package com.a30corner.combomaster.playground.action.impl;

import rx.functions.Action0;

import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;

public class Angry extends EnemyAction {

	public Angry(String title, String description, int... val) {
		this(val);
		setDescription(title, description);
	}
	
    public Angry(int... val) {
        super(Act.ANGRY, val);
    }

    @Override
    public void doAction(IEnvironment env, final Enemy enemy, final ICastCallback callback) {
        super.doAction(env, enemy, callback);
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
				enemy.addBuff(Type.ANGRY, data);
				callback.onCastFinish(true);
			}
		});
        
    }

}
