package com.a30corner.combomaster.playground.action.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Pair;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.scene.PlaygroundGameScene;

public class ColorChangeNew extends EnemyAction {

	public ColorChangeNew(String title, String description, int... val) {
		this(val);
		setDescription(title, description);
	}
	
    public ColorChangeNew(int... val) {
        super(Act.COLOR_CHANGE, val);
    }

    @Override
    public void doAction(IEnvironment env, Enemy enemy, ICastCallback callback) {
        super.doAction(env, enemy, callback);

        env.toastEnemyAction(this, enemy);
        
    	PlaygroundGameScene scene = env.getScene();
    	ActiveSkill skill = new ActiveSkill(SkillType.ST_COLOR_CHANGE);
    	boolean needRandom = false;
    	int dsize = data.size();
    	int check = -1;
    	for(int i=0; i<dsize; i+=2) {
    		int orb = data.get(i);
    		if(orb == -1) {
    			needRandom = true;
    		} else {
    			check = orb;
    		}
    	}
		Set<Integer> set = new HashSet<Integer>();
		int[][] board = scene.getBoard();
		Pair<Integer, Integer> pair = scene.getSize();
		for(int i=0; i<pair.first; ++i) {
			for(int j=0; j<pair.second; ++j) {
				set.add(board[i][j]%10);
			}
		}
		List<Integer> newdata = new ArrayList<Integer>(data);
    	if (check != -1) {
    		if(!set.contains(check)) {
    			needRandom = true;
    			newdata.set(0, -1);
    		}
    	}
    	if (needRandom) {
    		List<Integer> contains = new ArrayList<Integer>(set);
    		Collections.shuffle(contains);
    		int index = 0;
    		int size = newdata.size();
    		for(int i=0; i<size; ++i, ++index) {
    			if(newdata.get(i) == -1) {
    				if(index>=contains.size()) {
    					break;
    				}
    				newdata.set(i, contains.get(index));
    			}
    		}
    		skill.setData(newdata);
    	} else {
    		skill.setData(data);
    	}
    	scene.skillFired(skill, callback, true);
    }

}
