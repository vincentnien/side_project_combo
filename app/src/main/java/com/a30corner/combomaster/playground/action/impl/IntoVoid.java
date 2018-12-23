package com.a30corner.combomaster.playground.action.impl;

import rx.functions.Action0;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;

public class IntoVoid extends EnemyAction {

	public IntoVoid(String title, String description, int... val) {
		this(val);
		setDescription(title, description);
	}
	
    public IntoVoid(int... val) {
        super(Act.INTO_VOID, val);
    }

    @Override
    public void doAction(final IEnvironment env, Enemy enemy, final ICastCallback callback) {
        super.doAction(env, enemy, callback);
        
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
				env.removePositiveSkills();
				callback.onCastFinish(true);
			}
		});
        
    }

}
