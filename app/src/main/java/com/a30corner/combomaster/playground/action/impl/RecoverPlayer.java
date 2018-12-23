package com.a30corner.combomaster.playground.action.impl;

import rx.functions.Action0;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;

public class RecoverPlayer extends EnemyAction {

    public RecoverPlayer(int... val) {
        super(Act.RECOVERY_PLAYER, val);
    }
    
    public RecoverPlayer(String title, String desc, int... val) {
        super(Act.RECOVERY_PLAYER, val);
        setDescription(title, desc);
    }

    @Override
    public void doAction(final IEnvironment env, Enemy enemy, final ICastCallback callback) {
    	int percent = data.get(0);
    	final int recover = (int)(env.getTeam().getHp() * percent / 100.0);
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
				env.recoverPlayerHp(recover);
				callback.onCastFinish(true);
			}
		});
    }

}
