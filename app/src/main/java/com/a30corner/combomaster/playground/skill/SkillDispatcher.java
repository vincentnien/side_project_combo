package com.a30corner.combomaster.playground.skill;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.playground.skill.impl.AddCombo;
import com.a30corner.combomaster.playground.skill.impl.Attack;
import com.a30corner.combomaster.playground.skill.impl.AttackFix;
import com.a30corner.combomaster.playground.skill.impl.AwokenRecover;
import com.a30corner.combomaster.playground.skill.impl.BindRecover;
import com.a30corner.combomaster.playground.skill.impl.ChangeAttr;
import com.a30corner.combomaster.playground.skill.impl.CounterAttack;
import com.a30corner.combomaster.playground.skill.impl.DamageHp;
import com.a30corner.combomaster.playground.skill.impl.Delay;
import com.a30corner.combomaster.playground.skill.impl.DirectAttack;
import com.a30corner.combomaster.playground.skill.impl.DropLock;
import com.a30corner.combomaster.playground.skill.impl.DropRate;
import com.a30corner.combomaster.playground.skill.impl.EnhanceOrbs;
import com.a30corner.combomaster.playground.skill.impl.Gravity;
import com.a30corner.combomaster.playground.skill.impl.Gravity100;
import com.a30corner.combomaster.playground.skill.impl.HpAttack;
import com.a30corner.combomaster.playground.skill.impl.LeaderSwitch;
import com.a30corner.combomaster.playground.skill.impl.NoDrop;
import com.a30corner.combomaster.playground.skill.impl.Poison;
import com.a30corner.combomaster.playground.skill.impl.PowerUp;
import com.a30corner.combomaster.playground.skill.impl.RecoverHp;
import com.a30corner.combomaster.playground.skill.impl.RecoverHpByTurn;
import com.a30corner.combomaster.playground.skill.impl.RecoveryUp;
import com.a30corner.combomaster.playground.skill.impl.ReduceDef;
import com.a30corner.combomaster.playground.skill.impl.ReduceShield;
import com.a30corner.combomaster.playground.skill.impl.ReduceShieldByAwoken;
import com.a30corner.combomaster.playground.skill.impl.SkillBoost;
import com.a30corner.combomaster.playground.skill.impl.TeamHpAttack;
import com.a30corner.combomaster.playground.skill.impl.TimeExtend;
import com.a30corner.combomaster.playground.skill.impl.TimeExtendX;
import com.a30corner.combomaster.playground.skill.impl.TypeUp;
import com.a30corner.combomaster.playground.skill.impl.VoidAttrShield;
import com.a30corner.combomaster.playground.skill.impl.VoidDamageShield;

public class SkillDispatcher {

    public static void fire(IEnvironment env, Member owner, ActiveSkill skill, ICastCallback callback) {
        SkillType type = skill.getType();
        switch(type) {
			case ST_NO_DROP:
				NoDrop.onFire(env, owner, skill, callback);
				return ;
			case ST_ENHANCE_ORB: {
				EnhanceOrbs.onFire(env, owner, skill, callback);
				return ;
			}
			case ST_TURN_RECOVER: {
				RecoverHpByTurn.onFire(env, owner, skill, callback);
				return ;
			}
        case ST_RECOVER_UP: {
        	RecoveryUp.onFire(env, owner, skill, callback);
        	return ;
        }
		case ST_VOID_ATTR: {
			VoidAttrShield.onFire(env, owner, skill, callback);
			return;
		}
		case ST_VOID: {
			VoidDamageShield.onFire(env, owner, skill, callback);
			return ;
		}
        case ST_LEADER_SWITCH:
        	LeaderSwitch.onFire(env, owner, skill, callback);
        	return ;
        case ST_ADD_COMBO:
        	AddCombo.onFire(env, owner, skill, callback);
        	return ;
        case ST_DROP_LOCK:
        	DropLock.onFire(env, owner, skill, callback);
        	return ;
        case ST_RECOVER_HP:
        	RecoverHp.onFire(env, owner, skill, callback);
        	return ;
        case ST_HP_1:
        	DamageHp.onFire(env, owner, skill, callback);
        	return ;
        case ST_SKILL_BOOST:
        	SkillBoost.onFire(env, owner, skill, callback);
        	return ;
		case ST_AWOKEN_RECOVER:
			AwokenRecover.onFire(env, owner, skill, callback);
			return ;
        case ST_BIND_RECOVER:
        	BindRecover.onFire(env, owner, skill, callback);
        	return ;
		case ST_DEF_UP_BY_AWOKEN:
			ReduceShieldByAwoken.onFire(env, owner, skill, callback);
			return ;
		case ST_REDUCE_SHIELD:
        	ReduceShield.onFire(env, owner, skill, callback);
        	return ;
        case ST_GRAVITY:
            Gravity.onFire(env, owner, skill, callback);
            return ;
        case ST_GRAVITY_100:
            Gravity100.onFire(env, owner, skill, callback);
            return ;
        case ST_ATTACK_FIXED:
        	AttackFix.onFire(env, owner, skill, callback);
        	return ;
		case ST_HP_ATTACK:
			HpAttack.onFire(env, owner, skill, callback);
			return ;
		case ST_TEAM_HP_ATTACK:
			TeamHpAttack.onFire(env, owner, skill, callback);
			return ;
        case ST_ATTACK:
            Attack.onFire(env, owner, skill, callback);
            return ;
        case ST_DIRECT_ATTACK:
        	DirectAttack.onFire(env, owner, skill, callback);
        	return ;
        case ST_REDUCE_DEF:
        	ReduceDef.onFire(env, owner, skill, callback);
        	return ;
        case ST_DELAY:
        	Delay.onFire(env, owner, skill, callback);
        	return ;
        case ST_POISON:
        	Poison.onFire(env, owner, skill, callback);
        	return ;
        case ST_MOVE_TIME_X:
        	TimeExtendX.onFire(env, owner, skill, callback);
        	return ;        	
        case ST_TIME_EXTEND:
        	TimeExtend.onFire(env, owner, skill, callback);
        	return ;
        case ST_DROP_RATE:
        	DropRate.onFire(env, owner, skill, callback);
        	return ;
        case ST_POWER_UP:
        	PowerUp.onFire(env, owner, skill, callback);
        	return ;
        case ST_POWER_UP_BY_AWOKEN:
        	PowerUp.onFire(env, owner, skill, callback);
        	return ;
        case ST_TYPE_UP:
        	TypeUp.onFire(env, owner, skill, callback);
        	return ;
        case ST_CHANGE_ENEMY_ATTR:
        	ChangeAttr.onFire(env, owner, skill, callback);
        	return ;
        case ST_COUNTER_ATTACK:
        	CounterAttack.onFire(env, owner, skill, callback);
        	return ;
        default:
            break;
        }
        callback.onCastFinish(false);
    }
}
