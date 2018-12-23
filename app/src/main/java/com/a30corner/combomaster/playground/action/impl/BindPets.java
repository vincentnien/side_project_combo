package com.a30corner.combomaster.playground.action.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Pair;

import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MonsterType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.Team;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

public class BindPets extends EnemyAction {

	public BindPets(String title, String description, int... val) {
		this(val);
		setDescription(title, description);
	}
	
    public BindPets(int... val) {
        super(Act.LOCK, val);
    }

    @Override
    public void doAction(IEnvironment env, Enemy enemy, ICastCallback callback) {
        super.doAction(env, enemy, callback);
        
        //*****
        // index 0: type [0-leader, 1-leader&f 2-memberOnly 3-all 4-types 5 attr]
        // index 1,2: lock turn range
        // index 3: addition data(len: types or attr)
        
        Team team = env.getTeam();
        int type = data.get(0);
        int from = data.get(1);
        int end = data.get(2);
        int count = data.get(3);
        List<Pair<Integer, Integer>> lockList = new ArrayList<Pair<Integer, Integer>>();
        
        switch(type) {
        case 0:
        	lockList.add(new Pair<Integer, Integer>(0, RandomUtil.range(from, end)));
        	break;
        case 1:
        	lockList.add(new Pair<Integer, Integer>(0, RandomUtil.range(from, end)));
        	if (team.getMember(5) != null) {
        		lockList.add(new Pair<Integer, Integer>(5, RandomUtil.range(from, end)));
        	}
        	break;
        case 2: {
        	List<Integer> rand = new ArrayList<Integer>();
        	for(int i=1; i<5; ++i) {
        		if (team.getMember(i) != null) {
        			rand.add(i);
        		}
        	}
        	Collections.shuffle(rand);
        	int size = Math.min(rand.size(), count);
        	for(int i=0; i < size; ++i) {
        		lockList.add(new Pair<Integer, Integer>(rand.get(i), RandomUtil.range(from, end)));
        	}
        	break;
        }
        case 3: {
        	List<Integer> rand = new ArrayList<Integer>();
        	for(int i=0; i<6; ++i) {
        		if(team.getMember(i) != null) {
        			rand.add(i);
        		}
        	}
        	Collections.shuffle(rand);
        	int size = Math.min(rand.size(), count);
        	for(int i=0; i<size; ++i) {
        		lockList.add(new Pair<Integer, Integer>(rand.get(i), RandomUtil.range(from, end)));
        	}
        	break;
        }
        case 4: {
        	List<Integer> types = data.subList(4, 4+count);
        	Set<Integer> set = new HashSet<Integer>(types);
        	for(int i=0; i<6; ++i) {
        		MonsterInfo info = team.getMember(i);
        		if (info != null ) {
        			Pair<MonsterType,MonsterType> pair = info.getMonsterTypes();
        			if (set.contains(pair.first.ordinal()) || set.contains(pair.second.ordinal()) ||
        					set.contains(info.getMonsterType3().ordinal())) {
        				lockList.add(new Pair<Integer, Integer>(i, RandomUtil.range(from, end)));
        			}
        		}
        	}
        	break;
        }
        	
        case 5: {
        	List<Integer> attrs = data.subList(4, 4+count);
        	Set<Integer> set = new HashSet<Integer>(attrs);
        	//int attr = data.get(4);
        	for(int i=0; i<6; ++i) {
        		MonsterInfo info = team.getMember(i);
        		if (info != null ) {
        			Pair<Integer, Integer> pair = info.getProps();
        			if (set.contains(pair.first) || set.contains(pair.second)) {
        			//if(attr == pair.first || attr == pair.second) {
        				lockList.add(new Pair<Integer, Integer>(i, RandomUtil.range(from, end)));
        			}
        		}
        	}
        	break;
        }
        default: 
        	break;
        }
        env.toastEnemyAction(this, enemy);
        if(lockList.size() == 0) {
        	new Attack(100).doAction(env, enemy, callback);
        } else {
        	env.bindPets(enemy, lockList, callback);
        }
    }

}
