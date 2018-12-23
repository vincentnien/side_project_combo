package com.a30corner.combomaster.playground.action.impl;

import java.util.List;

import rx.functions.Action0;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.BuffOnEnemy;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.playground.entity.impl.Angry;
import com.a30corner.combomaster.utils.RandomUtil;

public class MultipleAttack extends EnemyAction {

	public MultipleAttack(String title, String description, int... val) {
		this(val);
		setDescription(title, description);
	}
	
    public MultipleAttack(int... val) {
        super(Act.ATTACK,  val);
    }

    @Override
    public void doAction(final IEnvironment env, final Enemy enemy, final ICastCallback callback) {

    	env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
				List<BuffOnEnemy> buffList = enemy.getBuffList();
				float angry = 1.0f;
				for(BuffOnEnemy buff : buffList) {
					if(buff instanceof Angry) {
						angry = buff.getData().get(1) / 100f;
						break;
					}
				}
				
		    	float percent = data.get(0);
		    	int hits = data.get(1);
		    	if (data.size()>=3) {
		    	    hits = RandomUtil.range(hits, data.get(2));
		    	}
		    	// TODO: shake multiple times?
		    	//int totalDamage = (int)((enemy.atk*percent/100.0) * hits);
		    	int damage = (int)(enemy.atk * percent * angry / 100.0);
		    	env.attackByEnemyMultiple(enemy, hits, damage, callback);
			}
		});
    }

}
