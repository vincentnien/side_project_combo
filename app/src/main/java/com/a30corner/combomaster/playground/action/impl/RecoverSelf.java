package com.a30corner.combomaster.playground.action.impl;

import rx.functions.Action0;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

public class RecoverSelf extends EnemyAction {

    public RecoverSelf(int... val) {
        super(Act.RECOVER_SELF, val);
    }

    public RecoverSelf(String title, String desc, int... val) {
        super(Act.RECOVER_SELF, val);
        setDescription(title, desc);
    }
    
    @Override
    public void doAction(IEnvironment env, final Enemy enemy, final ICastCallback callback) {
        super.doAction(env, enemy, callback);

        env.toastEnemyAction(this, enemy, new Action0() {

			@Override
			public void call() {
				int value = data.get(0);
				if(data.size()>1) {
					value = RandomUtil.range(value, data.get(1));
				}
				enemy.recoverHp(value, callback);
			}
        });
    	
    }

}
