package com.a30corner.combomaster.playground.action.impl;

import rx.functions.Action0;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.Constants;

import java.util.ArrayList;

public class ReduceTime extends EnemyAction {

	public ReduceTime(String title, String description, int... val) {
		this(val);
		
		setDescription(title, description);
	}
	
    public ReduceTime(int... val) {
        super(Act.REDUCE_MOVE_TIME, val);
    }

    @Override
    public void doAction(final IEnvironment env, Enemy enemy, final ICastCallback callback) {
        super.doAction(env, enemy, callback);
        
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
				if(data.size() <= 2) {
					ActiveSkill skill = new ActiveSkill(SkillType.ST_TIME_EXTEND);
					skill.setData(data);
					env.fireGlobalSkill(Constants.SK_TIME_CHANGE, skill, callback);
				} else {
					ActiveSkill skill = new ActiveSkill(SkillType.ST_MOVE_TIME_X);
					skill.setData(data.subList(0, 2));
					env.fireGlobalSkill(Constants.SK_TIME_CHANGE_X, skill, callback);
				}
			}
		});

    }

}
