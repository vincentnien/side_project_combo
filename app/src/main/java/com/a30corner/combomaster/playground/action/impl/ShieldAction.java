package com.a30corner.combomaster.playground.action.impl;

import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;

public class ShieldAction extends EnemyAction {

    public ShieldAction(int... val) {
        super(Act.SHIELD, val);
    }
    
    public ShieldAction(String title, String desc, int... val) {
        super(Act.SHIELD, val);
        setDescription(title, desc);
    }

    @Override
    public void doAction(IEnvironment env, Enemy enemy, ICastCallback callback) {
        super.doAction(env, enemy, callback);
        
        env.toastEnemyAction(this, enemy);
        
        enemy.addBuff(Type.SHIELD, data);
        callback.onCastFinish(true);
    }

}
