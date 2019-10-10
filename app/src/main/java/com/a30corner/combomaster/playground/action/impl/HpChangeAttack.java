package com.a30corner.combomaster.playground.action.impl;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.Constants;

import rx.functions.Action0;

public class HpChangeAttack extends EnemyAction {

    public HpChangeAttack(int... val) {
        super(Act.HP_CHANGE, val);
    }

    public HpChangeAttack(String title, String desc, int... val) {
        super(Act.HP_CHANGE, val);
        setDescription(title, desc);
    }
    
    @Override
    public void doAction(final IEnvironment env, final Enemy enemy,
                         final ICastCallback callback) {
        super.doAction(env, enemy, callback);
        
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
		        ActiveSkill skill = new ActiveSkill(ActiveSkill.SkillType.ST_HP_CHANGE);
		        skill.setData(data);
                env.fireGlobalSkill(Constants.SK_HP_CHANGE, skill, callback);
		        //env.fireSkill(null, skill, callback);
//                callback.onCastFinish(true);
			}
        });

    }

}
