package com.a30corner.combomaster.playground.action.impl;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.scene.PlaygroundGameScene;

public class RandomChangeExcept extends EnemyAction {

	public RandomChangeExcept(String title, String description, int... val) {
		this(val);
		setDescription(title, description);
	}

    public RandomChangeExcept(int... val) {
        super(Act.RANDOM_CHANGE, val);
    }

    @Override
    public void doAction(IEnvironment env, Enemy enemy, ICastCallback callback) {
        super.doAction(env, enemy, callback);
        
        env.toastEnemyAction(this, enemy);
        
    	PlaygroundGameScene scene = env.getScene();
    	ActiveSkill skill = new ActiveSkill(SkillType.ST_RANDOM_CHANGE_RESTRICT);
    	skill.setData(data); // color, cnt, except
    	scene.skillFired(skill, callback, true);
    }

}
