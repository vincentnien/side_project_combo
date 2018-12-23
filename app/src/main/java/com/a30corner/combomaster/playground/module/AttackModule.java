package com.a30corner.combomaster.playground.module;

import java.util.ArrayList;
import java.util.List;

import com.a30corner.combomaster.pad.DamageCalculator.AttackValue;
import com.a30corner.combomaster.pad.PadBoardAI;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill.AwokenSkill;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.SimulatorConstants;

public class AttackModule {


    public static Enemy getTarget(MonsterInfo monsterInfo, AttackValue attack, List<Enemy> enemies) {
        // find target...
        for(Enemy e : enemies) {
            if(e.isTarget()) {
                return e;
            }
        }
        return getTarget2(monsterInfo, attack, enemies, null);
    }
    
    public static Enemy getTarget2(MonsterInfo monsterInfo, AttackValue attack, List<Enemy> enemies, Enemy first) {
        // find next suitable target
    	
    	
        for(Enemy e : enemies) {
            if (!e.equals(first) && e.getHp() > 0) {
            	if(SimulatorConstants.isKillerTarget(monsterInfo, e)) {
            		return e;
            	}
            	if(PadBoardAI.isRestraint(attack.prop, e.currentProp())) {
            		return e;
            	}
            }
        }
        // find first hp>0 target
        for(Enemy e : enemies) {
            if (!e.equals(first) && e.getHp()>0) {
                return e;
            }
        }
        // find first not dead target
        for(Enemy e : enemies) {
            if (!e.equals(first) && !e.dead()) {
                return e;
            }
        }
        return null;
    }
    

	public static List<Enemy> findSuitableEnemies(MonsterInfo monsterInfo, List<Enemy> stageEnemies, AttackValue attack, boolean isMultiMode) {
        List<Enemy> attackTo = new ArrayList<Enemy>();
        List<Enemy> enemies = stageEnemies;
        
        int type = attack.attackType;
        boolean hasTwoWay = monsterInfo.getTargetAwokenCount(AwokenSkill.TWO_PRONGED_ATTACK, isMultiMode)>0;
        if(!hasTwoWay && attack.attackType == AttackValue.ATTACK_TWO) {
        	type = AttackValue.ATTACK_SINGLE;
        }
        
        if (type == AttackValue.ATTACK_SINGLE) {
            Enemy target = getTarget(monsterInfo, attack, enemies);
            if(target != null) {
                attackTo.add(target);
            }
        } else if(type == AttackValue.ATTACK_MULTIPLE) {
            for(Enemy enemy : enemies) {
                if(!enemy.dead()) {
                    attackTo.add(enemy);
                }
            }
        } else if(type == AttackValue.ATTACK_TWO) {
            Enemy target = getTarget(monsterInfo, attack, enemies);
            if(target != null) {
                attackTo.add(target);
            }
            // find next suitable
            Enemy target2 = getTarget2(monsterInfo, attack, enemies, target);
            if(target2 != null) {
                attackTo.add(target2);
            }
        }
        
        return attackTo;
    }
    
    
}
