package com.a30corner.combomaster.playground.action.impl;

import java.util.List;

import rx.functions.Action0;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.BuffOnEnemy;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.playground.entity.impl.Angry;

public class Attack extends EnemyAction {

	public Attack(String title, String desc, int... val) {
		super(Act.ATTACK, val);
		
		setDescription(title, desc);
	}
	
    public Attack(int... val) {
        super(Act.ATTACK, val);
    }

    @Override
    public void doAction(final IEnvironment env, final Enemy enemy, final ICastCallback callback) {
        super.doAction(env, enemy, callback);
		List<BuffOnEnemy> buffList = enemy.getBuffList();
		float angry = 1.0f;
		for(BuffOnEnemy buff : buffList) {
			if(buff instanceof Angry) {
				angry = buff.getData().get(1) / 100f;
				break;
			}
		}
        final int damage = (int)(data.get(0) * enemy.atk * angry / 100.0f);
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
				env.attackByEnemy(enemy, damage, callback);
			}
		});
    	
    }

}
