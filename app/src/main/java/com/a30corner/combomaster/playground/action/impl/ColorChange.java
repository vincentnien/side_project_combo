package com.a30corner.combomaster.playground.action.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Pair;

import com.a30corner.combomaster.pad.PadBoardAI;
import com.a30corner.combomaster.pad.mode7x6.PadBoardAI7x6;
import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.scene.PlaygroundGameScene;

public class ColorChange extends EnemyAction {

	public ColorChange(String title, String description, int... val) {
		this(val);
		setDescription(title, description);
	}
	
    public ColorChange(int... val) {
        super(Act.COLOR_CHANGE, val);
    }

    @Override
    public void doAction(IEnvironment env, Enemy enemy, ICastCallback callback) {
        super.doAction(env, enemy, callback);

		boolean is7x6 = env.is7x6();
		final int ROWS = (is7x6)? PadBoardAI7x6.ROWS:PadBoardAI.ROWS;
		final int COLS = (is7x6)? PadBoardAI7x6.COLS:PadBoardAI.COLS;
        env.toastEnemyAction(this, enemy);
        
    	PlaygroundGameScene scene = env.getScene();
    	ActiveSkill skill = new ActiveSkill(SkillType.ST_COLOR_CHANGE);
    	boolean needRandom = false;
    	for(Integer d : data) {
    		if (d == -1) {
    			needRandom = true;
    			break;
    		}
    	}
    	List<Integer> newdata = new ArrayList<Integer>(data);
    	int[][] board = scene.getBoard();
    	if (needRandom) {
    		Set<Integer> set = new HashSet<Integer>();
    		Pair<Integer, Integer> pair = scene.getSize();
    		for(int i=0; i<pair.first; ++i) {
    			for(int j=0; j<pair.second; ++j) {
    				set.add(board[i][j]%10);
    			}
    		}
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
    		Set<Integer> set = new HashSet<Integer>();
    		int size = data.size();
    		for(int i=0; i<size; i+=2) {
    			set.add(data.get(i));
    		}
    		boolean findOrb = false;
    		for(int i=0; i<ROWS; ++i) {
    			for(int j=0; j<COLS; ++j) {
    				if(set.contains(board[i][j]%10)) {
    					findOrb = true;
    					break;
    				}
    			}
    		}
    		if(!findOrb) {
				//FIXME: need if else action
    			new Attack(25).doAction(env, enemy, callback);
    			return ;
    		} 
    	}
    	scene.skillFired(skill, callback, true);
    }

}
