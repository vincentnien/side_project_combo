package com.a30corner.combomaster.playground.enemy.conditions;

import android.util.Pair;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;

public class ConditionFindOrb extends EnemyCondition {

	public ConditionFindOrb(int data) {
		super(Type.FIND_ORB, new int[]{data});
	}
	
	public ConditionFindOrb(int[] data) {
		super(Type.FIND_ORB, data);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		int[][] board = env.getScene().getBoard();
		Pair<Integer, Integer> size = env.getScene().getSize();
		int orb = data.get(0);
		
		for(int i=0; i<size.first; ++i) {
			for(int j=0; j<size.second; ++j) {
				if((board[i][j]%10) == orb) {
					return true;
				}
			}
		}
		
		return false;
	}

}
