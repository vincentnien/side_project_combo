package com.a30corner.combomaster.playground.action.impl;

import rx.functions.Action0;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;

public class Daze extends EnemyAction {

	public Daze(String title, String description, int... val) {
		this(val);
		setDescription(title, description);
	}
	
    public Daze(int... val) {
        super(Act.DAZE, val);
    }

    @Override
    public void doAction(IEnvironment env, Enemy enemy, final ICastCallback callback) {
        super.doAction(env, enemy, callback);
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
				callback.onCastFinish(true);
			}
		});
        
    }

}
