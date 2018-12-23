package com.a30corner.combomaster.playground.action.impl;

import rx.functions.Action0;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

public class ChangeAttribute extends EnemyAction {

	public ChangeAttribute(String title, String description, int... val) {
		this(val);
		setDescription(title, description);
	}
	
    public ChangeAttribute(int... val) {
        super(Act.CHANGE_ATTRIBUTE, val);
    }

    @Override
    public void doAction(final IEnvironment env, final Enemy enemy, final ICastCallback callback) {
        super.doAction(env, enemy, callback);
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
				ActiveSkill skill = new ActiveSkill(SkillType.ST_CHANGE_ENEMY_ATTR);
				int choose = RandomUtil.getInt(data.size());
				skill.addData(data.get(choose));
				//env.getScene().skillFired(skill, callback);
				env.fireSkill(null, skill, callback);
			}
		});
        
    }

}
