package com.a30corner.combomaster.playground.action.impl;

import rx.functions.Action0;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;

public class Gravity extends EnemyAction {

    public Gravity(int... val) {
        super(Act.GRAVITY, val);
    }
    public Gravity(String title, String desc, int... val) {
        super(Act.GRAVITY, val);
        setDescription(title, desc);
    }


    @Override
    public void doAction(final IEnvironment env,final Enemy enemy, final ICastCallback callback) {
        super.doAction(env, enemy, callback);

        env.toastEnemyAction(this, enemy, new Action0() {

			@Override
			public void call() {
		    	int percent = data.get(0);
		    	int damage = (int)(env.getTeam().getCurrentHp() * percent / 100.0);
		    	env.attackByEnemy(enemy, damage, callback);
			}
        	
        });

    }

}
