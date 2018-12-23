package com.a30corner.combomaster.playground.action.impl;

import java.util.List;

import rx.functions.Action0;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;

public class RecoverDead extends EnemyAction {

    public RecoverDead(int... val) {
        super(Act.RESURRECTION, val);
    }

    public RecoverDead(String title, String desc, int... val) {
        super(Act.RESURRECTION, val);
        setDescription(title, desc);
    }
    
    @Override
    public void doAction(final IEnvironment env, final Enemy enemy, final ICastCallback callback) {
        super.doAction(env, enemy, callback);

        env.toastEnemyAction(this, enemy, new Action0() {

			@Override
			public void call() {
				List<Enemy> enemies = env.getEnemies();
				for(Enemy target : enemies) {
					if(target.dead() || target.diedThisTurn()) {
						target.recoverHp(data.get(0), callback);
						break;
					}
				}
			}
        });
    	
    }

}
