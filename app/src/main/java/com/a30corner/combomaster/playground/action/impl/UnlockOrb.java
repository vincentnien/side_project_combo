package com.a30corner.combomaster.playground.action.impl;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnlockOrb extends EnemyAction {

	public UnlockOrb(String title, String description, int... val) {
		this(val);
		setDescription(title, description);
	}

    public UnlockOrb(int... val) {
        super(Act.UNLOCK_ORB, val);
    }

    @Override
    public void doAction(IEnvironment env, Enemy enemy, ICastCallback callback) {
        super.doAction(env, enemy, callback);
        
        env.toastEnemyAction(this, enemy);
        
    	PlaygroundGameScene scene = env.getScene();
		boolean is7x6 = env.is7x6();
		final int ROWS = (is7x6)? PadBoardAI7x6.ROWS:PadBoardAI.ROWS;
		final int COLS = (is7x6)? PadBoardAI7x6.COLS:PadBoardAI.COLS;

    	int[][] board = scene.getBoard();
    	int type = data.get(0);
    	if(type == 0) { // lock some type orb
    		// find orb
    		List<Integer> newData = data.subList(1, data.size());
    		Set<Integer> set = new HashSet<Integer>(newData);
    		
    		boolean find = false;
    		for(int i=0; i<ROWS; ++i) {
    			for(int j=0; j<COLS; ++j) {
    				if(set.contains(board[i][j])) {
    					find = true;
    					break;
    				}
    			}
    		}
    		if(find) {
	    		ActiveSkill skill = new ActiveSkill(SkillType.ST_ADD_LOCK);
	    		skill.setData(newData);
	    		
	    		scene.skillFired(skill, callback, true);
    		} else {
    			new Attack(100).doAction(env, enemy, callback);
    		}
    	} else if(type == 1) { // lock x orbs
    		Set<Integer> set = new HashSet<Integer>(data.subList(2, data.size()));
    		
    		List<Pair<Integer, Integer>> container = new ArrayList<Pair<Integer, Integer>>();
    		for(int i=0; i<ROWS; ++i) {
    			for(int j=0; j<COLS; ++j) {
    				if(set.contains(board[i][j])) {
    					container.add(new Pair<Integer, Integer>(i, j));
    				}
    			}
    		}
    		int csize = container.size();
    		if(csize>0) {
	    		Collections.shuffle(container);
	    		int count = data.get(1);
	    		if(count>csize) {
	    			count = csize;
	    		}
	    		int[][] newboard =  (is7x6)? PadBoardAI7x6.copy_board(board):PadBoardAI.copy_board(board);
	    		for(int i=0; i<count; ++i) {
	    			Pair<Integer, Integer> target = container.get(i);
	    			if(newboard[target.first][target.second]<20) {
	    				newboard[target.first][target.second] += 20;
	    			}
	    		}
	    		scene.setChangeColorBoard(newboard, callback, false);
    		} else {
    			new Attack(100).doAction(env, enemy, callback);
    		}
    	} else if(type == 2) {
    		int count = data.get(1);
    		int[][] newboard = (is7x6)? PadBoardAI7x6.copy_board(board):PadBoardAI.copy_board(board);

    		List<Integer> randomPos = new ArrayList<Integer>(30);
    		for(int i=0; i<30; ++i) {
    			randomPos.add(i);
    		}
    		Collections.shuffle(randomPos);
    		
    		int lockedCnt = 0;
    		for(int i=0; i<30; ++i) {
    			int pos = randomPos.get(i);
    			int x = pos % ROWS;
    			int y = pos % COLS;
    			if(newboard[x][y]<20) {
    				newboard[x][y] += 20;
    				if(++lockedCnt >= count) {
    					break;
    				}
    			}
    		}
    		scene.setChangeColorBoard(newboard, callback, false);
    	}
    	
    }

}
