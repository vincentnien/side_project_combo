package com.a30corner.combomaster.playground.action.impl;

import rx.functions.Action0;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.scene.PlaygroundGameScene;

public class LineChange extends EnemyAction {

    public LineChange(int... val) {
        super(Act.LINE_CHANGE, val);
    }

    public LineChange(String title, String desc, int... val) {
    	super(Act.LINE_CHANGE, val);
    	setDescription(title, desc);
	}
    
    @Override
    public void doAction(final IEnvironment env, Enemy enemy, final ICastCallback callback) {
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
		        PlaygroundGameScene scene = env.getScene();
		        ActiveSkill skill = new ActiveSkill(SkillType.ST_ONE_LINE_TRANSFORM);
		        skill.setData(data);
		        scene.skillFired(skill, callback, true);
			}
		});

    }

}
