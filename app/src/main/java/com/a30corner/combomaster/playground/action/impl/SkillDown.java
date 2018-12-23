package com.a30corner.combomaster.playground.action.impl;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action0;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

public class SkillDown extends EnemyAction {

	public SkillDown(String title, String description, int... val) {
		this(val);
		setDescription(title, description);
	}
	
    public SkillDown(int... val) {
        super(Act.ADD_SKILL_TURN, val);
    }

    @Override
    public void doAction(final IEnvironment env, Enemy enemy, final ICastCallback callback) {
        super.doAction(env, enemy, callback);
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
		        if(data.get(0) == 0) { // all
		        	List<Integer> reduced = new ArrayList<Integer>();
		        	for(int i=0; i<6; ++i) {
				        int cd = -RandomUtil.range(data.get(1), data.get(2));
				        reduced.add(cd);
		        	}
		        	env.skillBoost(reduced, null, callback);
		        }
			}
		});
        
    }

}
