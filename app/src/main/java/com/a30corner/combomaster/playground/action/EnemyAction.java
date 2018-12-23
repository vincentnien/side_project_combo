package com.a30corner.combomaster.playground.action;

import java.util.LinkedList;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.LogUtil;

public abstract class EnemyAction extends Action {

    public enum Act {
        ATTACK,
        MULTIPLE_ATTACK,
        GRAVITY,
        RESURRECTION,
        RECOVER_SELF,
        RECOVERY_PLAYER,
        COLOR_CHANGE,
        LINE_CHANGE,
        RANDOM_LINE_CHANGE,
        ADD_DROP_RATE,
        ADD_DROP_LOCK,
        REDUCE_MOVE_TIME,
        TRANSFORM,
        LOCK,
        LOCK_SKILL,
        LOCK_AWOKEN,
        INTO_VOID,
        LOCK_ORB,
        UNLOCK_ORB,
        ADD_BUFF,
        BIND_MONSTER,
        ADD_SKILL_TURN,
        DARK_SCREEN,
        RANDOM_CHANGE,
        ABSORB_SHIELD,
        DAMAGE_ABSORB_SHIELD,
        DAMAGE_VOID_SHIELD,
        COMBO_SHIELD,
        RESISTANCE_SHIELD,
        SHIELD,
        DAZE,
        ANGRY,
        SWITCH_LEADER,
        CHANGE_ATTRIBUTE,
        RECOVERY_UP,
        POSITION_CHANGE,
        CLOUD,
        LOCK_REMOVE
    }
    
    public String title = "";
    public String description = "";
    
    public Act action;
    public LinkedList<Integer> data;
    
    public EnemyAction(Act type, int... val) {
        action = type;
        data = new LinkedList<Integer>();
        if (val != null) {
            for(int v : val) {
                data.add(v);
            }
        }
        setDescription("", "");//getClass().getSimpleName()
    }
    
    public void setDescription(String title, String desc) {
        this.title = title;
        this.description = desc;
    }
    
//    public static EnemyAction create(Act prop, float value) {
//        return new EnemyAction(prop, value);
//    }
    
    public void doAction(IEnvironment env, Enemy enemy, ICastCallback callback) {
        LogUtil.d("execute enemy's action=", title);
    }
}
