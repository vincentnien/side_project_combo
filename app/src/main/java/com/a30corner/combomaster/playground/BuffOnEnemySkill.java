package com.a30corner.combomaster.playground;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BuffOnEnemySkill {

    public enum Type {
        REDUCE_DEF,
        ANGRY,
        RESISTANCE_SHIELD,
        ABSORB_SHIELD,
        COMBO_SHIELD,
        SHIELD,
        DELAY,
        POISON,
        DAMAGE_ABSORB_SHIELD,
        DAMAGE_VOID_SHIELD,
        KONJO,
        CHANGE_ATTR,
        CHANGE_TURN
    }
    
    public Type type;
    public LinkedList<Integer> data;
    
    private BuffOnEnemySkill(Type t) {
        type = t;
    }
    
    public static BuffOnEnemySkill create(Type type) {
        return new BuffOnEnemySkill(type);
    }
    
    public static BuffOnEnemySkill create(Type type, int... data) {
        BuffOnEnemySkill skill = new BuffOnEnemySkill(type);
        skill.setData(data);
        return skill;
    }
    
    public static BuffOnEnemySkill create(Type type, List<Integer> data) {
        BuffOnEnemySkill skill = new BuffOnEnemySkill(type);
        skill.setData(data);
        return skill;
    }
    
    public void setData(int... data) {
        this.data = new LinkedList<Integer>();
        for(int d : data) {
            this.data.add(d);
        }
    }
    
    public void setData(List<Integer> data) {
        this.data = new LinkedList<Integer>(data);
    }
    
    public List<Integer> getData() {
        return Collections.unmodifiableList(data);
    }
}
