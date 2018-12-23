package com.a30corner.combomaster.playground.stagedata;

import android.util.Pair;
import android.util.SparseArray;

import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.StageGenerator;
import com.a30corner.combomaster.playground.action.impl.AbsorbShieldAction;
import com.a30corner.combomaster.playground.action.impl.Angry;
import com.a30corner.combomaster.playground.action.impl.Attack;
import com.a30corner.combomaster.playground.action.impl.BindPets;
import com.a30corner.combomaster.playground.action.impl.ChangeAttribute;
import com.a30corner.combomaster.playground.action.impl.ColorChange;
import com.a30corner.combomaster.playground.action.impl.ComboShield;
import com.a30corner.combomaster.playground.action.impl.DamageVoidShield;
import com.a30corner.combomaster.playground.action.impl.DarkScreen;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.IntoVoid;
import com.a30corner.combomaster.playground.action.impl.LineChange;
import com.a30corner.combomaster.playground.action.impl.LockAwoken;
import com.a30corner.combomaster.playground.action.impl.LockOrb;
import com.a30corner.combomaster.playground.action.impl.LockSkill;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.ReduceTime;
import com.a30corner.combomaster.playground.action.impl.ResistanceShieldAction;
import com.a30corner.combomaster.playground.action.impl.ShieldAction;
import com.a30corner.combomaster.playground.action.impl.SkillDown;
import com.a30corner.combomaster.playground.action.impl.Transform;
import com.a30corner.combomaster.playground.enemy.EnemyAttack;
import com.a30corner.combomaster.playground.enemy.EnemyAttackMode;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.enemy.attack.FromHeadAttack;
import com.a30corner.combomaster.playground.enemy.attack.RandomAttack;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAlways;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHp;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionIf;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionTurns;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsedIf;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class ZeusDragonStage extends IStage {

    @Override
    public void create(IEnvironment env,
                       SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        ArrayList<Enemy> data;
        Enemy zero;
        SequenceAttack seq;
        mStageEnemies.clear();

        SparseArray<List<StageGenerator.SkillText>> list = loadSkillTextNew(env, mStage, new int[]{2719,2748,2749});
        int stageCounter = 0;

        data = new ArrayList<Enemy>();
        {
            StageGenerator.SkillText[] text = loadSubText(list, 2748);
            data.add(Enemy.create(env, 2748, 27081300, 17640, 840, 1,
                    new Pair<Integer, Integer>(4, 1),
                    getMonsterTypes(env, 2748)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 75);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[0].title, text[0].description, 999)).addPair(new ConditionAlways(), new ShieldAction(text[1].title,text[1].description,1,-1,50)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Transform(text[17].title,text[17].description,1,4,5,3,0,2,7,6,8)).addPair(new ConditionAlways(), new Attack(400)));
            zero.attackMode.addAttack(new ConditionHp(2,25), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new BindPets(text[2].title,text[2].description,2,2,2,1)).addPair(new ConditionTurns(2), new Attack(130)));
            seq.addAttack(new EnemyAttack().addAction(new RandomChange(text[3].title,text[3].description,6,3)).addPair(new ConditionAlways(), new Attack(180)));
            zero.attackMode.addAttack(new ConditionHp(1,75), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Transform(text[4].title,text[4].description,1,3,0)).addAction(new LineChange(text[5].title,text[5].description,1,7)).addPair(new ConditionUsed(), new Attack(100)));
            seq.addAttack(new EnemyAttack().addAction(new RandomChange(text[6].title,text[6].description,7,5)).addPair(new ConditionAlways(), new Attack(180)));
            seq.addAttack(new EnemyAttack().addAction(new RandomChange(text[7].title,text[7].description,1,2,4,2)).addPair(new ConditionAlways(), new Attack(160)));
            zero.attackMode.addAttack(new ConditionHp(1,50), seq);

            seq = new SequenceAttack();
            int[] change = {5,3,0};
            int[] absorb = {1,0,3};
            int random = RandomUtil.getInt(3);
            seq.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[8+random*2].title,text[8+random*2].description,change[random])).addPair(new ConditionUsed(), new AbsorbShieldAction(text[9+random*2].title,text[9+random*2].description,5,absorb[random])));

            seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[14].title,text[14].description,7,8)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB.ordinal(), 7), new Attack(200)));
            seq.addAttack(new EnemyAttack().addAction(new RandomChange(text[15].title,text[15].description,7,5)).addPair(new ConditionTurns(2), new Attack(180)));
            seq.addAttack(new EnemyAttack().addAction(new RandomChange(text[16].title,text[16].description,1,2,4,2)).addPair(new ConditionAlways(), new Attack(160)));
            zero.attackMode.addAttack(new ConditionHp(1,25), seq);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            StageGenerator.SkillText[] text = loadSubText(list, 2749);
            data.add(Enemy.create(env, 2749, 13431300, 28440, 840, 1,
                    new Pair<Integer, Integer>(5, 4),
                    getMonsterTypes(env, 2749)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[0].title, text[0].description, 290)).addPair(new ConditionAlways(), new LockAwoken(text[1].title,text[1].description,5)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Gravity(text[5].title,text[5].description,99)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 1, 50), new ResistanceShieldAction(text[6].title,text[6].description,10)));
            seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[10].title,text[10].description,1,7)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new ReduceTime(text[11].title,text[11].description,1,-2000)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new MultipleAttack(text[12].title,text[12].description,60,3)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[2].title,text[2].description,100,5)));
            zero.attackMode.addAttack(new ConditionHp(2,15), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new LockSkill(text[3].title,text[3].description,5)).addPair(new ConditionUsed(), new Angry(text[4].title,text[4].description,99,200)));
            zero.attackMode.addAttack(new ConditionHp(2,20), seq);

            RandomAttack rnd = new RandomAttack();
            rnd.addAttack(new EnemyAttack().addAction(new SkillDown(text[7].title,text[7].description,0,0,2)).addPair(new ConditionUsed(), new Attack(120)));
            rnd.addAttack(new EnemyAttack().addAction(new LineChange(text[8].title,text[8].description,1,5)).addPair(new ConditionUsed(), new Attack(130)));
            rnd.addAttack(new EnemyAttack().addAction(new LockOrb(text[9].title,text[9].description,2,5)).addPair(new ConditionUsed(), new Attack(120)));
            zero.attackMode.addAttack(new ConditionHp(1,50), rnd);

            rnd = new RandomAttack();
            rnd.addAttack(new EnemyAttack().addAction(new SkillDown(text[7].title,text[7].description,0,0,2)).addPair(new ConditionUsed(), new Attack(120)));
            rnd.addAttack(new EnemyAttack().addAction(new LineChange(text[8].title,text[8].description,1,5)).addPair(new ConditionUsed(), new Attack(130)));
            rnd.addAttack(new EnemyAttack().addAction(new LockOrb(text[9].title,text[9].description,2,5)).addPair(new ConditionUsed(), new Attack(120)));
            zero.attackMode.addAttack(new ConditionHp(2,50), rnd);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            StageGenerator.SkillText[] text = loadSubText(list, 2719);
            data.add(Enemy.create(env, 2719, 100000000, 10320, 2180, 1,
                    new Pair<Integer, Integer>(3,-1),
                    getMonsterTypes(env, 2719)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new DamageVoidShield(text[48].title,text[48].description,99, 20000000)).addAction(new ResistanceShieldAction(text[49].title,text[49].description,999)).addPair(new ConditionAlways(), new Gravity(text[50].title,text[50].description,35)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new IntoVoid(text[51].title,text[51].description,0)).addPair(new ConditionAlways(), new Gravity(text[52].title,text[52].description,500)));
            zero.attackMode.addAttack(new ConditionHp(1,90), seq);

            FromHeadAttack head = new FromHeadAttack();
            head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[53].title,text[53].description,1,4,5,3,0)).addAction(new Attack(text[54].title,text[54].description,280)).addPair(new ConditionHp(2,60), new ColorChange(2,7)));
            head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[53].title,text[53].description,1,4,5,3,0)).addAction(new Attack(text[55].title,text[55].description,260)).addPair(new ConditionHp(2,70), new RandomChange(6,3)));
            head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[53].title,text[53].description,1,4,5,3,0)).addAction(new Attack(text[56].title,text[56].description,240)).addPair(new ConditionHp(2,80), new DarkScreen(0)));
            head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[53].title,text[53].description,1,4,5,3,0)).addAction(new Attack(text[57].title,text[57].description,200)).addPair(new ConditionHp(2,90), new BindPets(3,2,2,1)));
            zero.attackMode.addAttack(new ConditionHp(1,50), head);

            head = new FromHeadAttack();
            head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[53].title,text[53].description,1,4,5,3,0)).addAction(new ComboShield(text[58].title,text[58].description,3,7)).addPair(new ConditionUsed(), new MultipleAttack(text[59].title,text[59].description,150,4)));
            head.addAttack(new EnemyAttack().addAction(new IntoVoid(text[51].title,text[51].description,0)).addPair(new ConditionHp(2,20), new Gravity(text[52].title,text[52].description,500)));
            head.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[60].title,text[60].description,130,6)).addPair(new ConditionHp(2,30), new LockOrb(text[61].title,text[61].description,1,15,1,4,5,3,0,2)));
            head.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[62].title,text[62].description,90,5)).addPair(new ConditionHp(2,40), new Transform(text[63].title,text[63].description,2,6)));
            head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[53].title,text[53].description,1,4,5,3,0)).addAction(new Attack(text[64].title,text[64].description,340)).addPair(new ConditionHp(2,50), new LineChange(8,7)));
            zero.attackMode.addAttack(new ConditionHp(2,50), head);

        }
        mStageEnemies.put(++stageCounter, data);
    }
}
