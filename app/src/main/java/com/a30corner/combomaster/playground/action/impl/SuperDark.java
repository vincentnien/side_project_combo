package com.a30corner.combomaster.playground.action.impl;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.scene.PlaygroundGameScene;

import rx.functions.Action0;

public class SuperDark extends EnemyAction {

    public SuperDark(int... val) {
        super(Act.DARK_SCREEN, val);
    }

    public SuperDark(String title, String desc, int... val) {
        super(Act.DARK_SCREEN, val);
        setDescription(title, desc);
    }
    
    @Override
    public void doAction(final IEnvironment env, Enemy enemy,
    		final ICastCallback callback) {
        super.doAction(env, enemy, callback);
        
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
		        ActiveSkill skill = new ActiveSkill(SkillType.ST_SUPER_DARK);
                skill.setData(data);
		        //scene.skillFired(skill, callback, true);
                env.fireSkill(null, skill, callback);
			}
        });

    }

}
