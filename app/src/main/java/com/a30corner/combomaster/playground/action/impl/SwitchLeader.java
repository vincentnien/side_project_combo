package com.a30corner.combomaster.playground.action.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;

public class SwitchLeader extends EnemyAction {

	public SwitchLeader(String title, String description, int... val) {
		this(val);
		setDescription(title, description);
	}
	
    public SwitchLeader(int... val) {
        super(Act.SWITCH_LEADER, val);
    }

    @Override
    public void doAction(IEnvironment env, Enemy enemy, ICastCallback callback) {
        super.doAction(env, enemy, callback);
        
        env.toastEnemyAction(this, enemy);
        
        List<Integer> chooseOne = new ArrayList<Integer>();
        for(int i=1; i<=4; ++i) {
        	if(env.getMember(i) != null) {
        		chooseOne.add(i);
        	}
        }
        if(chooseOne.size()>=1) {
	        Collections.shuffle(chooseOne);
	        
	        ActiveSkill skill = new ActiveSkill(SkillType.ST_LEADER_SWITCH);
	        skill.addData(data.get(0), chooseOne.get(0));
	        env.executeLeaderSwitch(skill, callback);
        }
    }

}
