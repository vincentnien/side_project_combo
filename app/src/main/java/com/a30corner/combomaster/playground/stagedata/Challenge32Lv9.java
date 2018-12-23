package com.a30corner.combomaster.playground.stagedata;

import android.util.Pair;
import android.util.SparseArray;

import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.StageGenerator;
import com.a30corner.combomaster.playground.action.impl.Angry;
import com.a30corner.combomaster.playground.action.impl.Attack;
import com.a30corner.combomaster.playground.action.impl.BindPets;
import com.a30corner.combomaster.playground.action.impl.ColorChange;
import com.a30corner.combomaster.playground.action.impl.ComboShield;
import com.a30corner.combomaster.playground.action.impl.DamageVoidShield;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.LineChange;
import com.a30corner.combomaster.playground.action.impl.LockOrb;
import com.a30corner.combomaster.playground.action.impl.LockSkill;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RecoverSelf;
import com.a30corner.combomaster.playground.action.impl.ReduceTime;
import com.a30corner.combomaster.playground.action.impl.ResistanceShieldAction;
import com.a30corner.combomaster.playground.action.impl.SkillDown;
import com.a30corner.combomaster.playground.action.impl.Transform;
import com.a30corner.combomaster.playground.enemy.EnemyAttack;
import com.a30corner.combomaster.playground.enemy.EnemyAttackMode;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.enemy.attack.RandomAttack;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAlways;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFindOrb;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHp;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionIf;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionPercentage;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionTurns;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsedIf;
import com.a30corner.combomaster.playground.entity.Enemy;

import java.util.ArrayList;
import java.util.List;

public class Challenge32Lv9 extends IStage {

    @Override
    public void create(IEnvironment env,
                                   SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        ArrayList<Enemy> data;
        Enemy zero;
        SequenceAttack seq;
        mStageEnemies.clear();

        List<StageGenerator.SkillText> list = loadSkillText(env, mStage);
        StageGenerator.SkillText[] text = new StageGenerator.SkillText[list.size()];
        int len = text.length;
        for (int i = 0; i < len; ++i) {
            text[i] = getSkillText(list, i);
        }
        int stageCounter = 0;

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 1957, 5767808, 8807, 510, 1,
                    new Pair<Integer, Integer>(1,-1),
                    getMonsterTypes(env, 1957)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new LockSkill(text[1].title,text[1].description,2)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[5].title,text[5].description,200)).addPair(new ConditionUsed(), new BindPets(3,1,1,6)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[6].title,text[6].description,1000)));
            zero.attackMode.addAttack(new ConditionHp(2,1), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[2].title,text[2].description,300)).addPair(new ConditionTurns(2), new LineChange(4,1,9,1)));
            zero.attackMode.addAttack(new ConditionHp(2,50), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[4].title,text[4].description,200)).addPair(new ConditionAlways(), new Transform(1)));
            zero.attackMode.addAttack(new ConditionHp(2,30), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[3].title,text[3].description,150)).addPair(new ConditionFindOrb(1), new ColorChange(1,7)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1, 1, EnemyCondition.Type.FIND_ORB.ordinal(), 0),new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1,30), seq);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 2740, 3656302, 12628, 492, 1,
                    new Pair<Integer, Integer>(4,-1),
                    getMonsterTypes(env, 2740)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new ReduceTime(text[8].title,text[8].description,5,-1000)).addPair(new ConditionAlways(), new LockSkill(text[7].title,text[7].description,5)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(text[9].title,text[9].description,50)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[10].title,text[10].description,50,2)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[11].title,text[11].description,50,3)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[12].title,text[12].description,50,4)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[13].title,text[13].description,200,4)));
            zero.attackMode.addAttack(new ConditionHp(1,0), seq);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 2198, 34, 3337, 911111, 1,
                    new Pair<Integer, Integer>(3,-1),
                    getMonsterTypes(env, 2198)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SkillDown(text[14].title,text[14].description,0,1,1)));
            zero.attackMode.addAttack(new ConditionHp(0, 100), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[15].title,text[15].description, 999, 200)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(200)).addPair(new ConditionAlways(), new BindPets(text[16].title,text[16].description,3,2,2,1)));
            zero.attackMode.addAttack(new ConditionHp(2, 100), seq);

            data.add(Enemy.create(env, 1470, 22, 10000, 50000000, 1,
                    new Pair<Integer, Integer>(0,3),
                    getMonsterTypes(env, 1470)));
            zero = data.get(1);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new DamageVoidShield(text[17].title,text[17].description,99,50)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new ResistanceShieldAction(text[18].title,text[18].description,999)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[19].title,text[19].description,250,4)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            data.add(Enemy.create(env, 2198, 34, 3337, 911111, 1,
                    new Pair<Integer, Integer>(3,-1),
                    getMonsterTypes(env, 2198)));
            zero = data.get(2);
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SkillDown(text[14].title,text[14].description,0,1,1)));
            zero.attackMode.addAttack(new ConditionHp(0, 100), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[15].title,text[15].description, 999, 200)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(200)).addPair(new ConditionAlways(), new BindPets(text[16].title,text[16].description,3,2,2,1)));
            zero.attackMode.addAttack(new ConditionHp(2, 100), seq);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 918, 6080262, 19336, 8160, 1,
                    new Pair<Integer, Integer>(0, 3),
                    getMonsterTypes(env, 918)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new ComboShield(text[20].title, text[20].description,
                            999, 5)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                            1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                    new BindPets(text[22].title, text[22].description, 4,
                            2, 4, 1, 6)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 20),
                    new MultipleAttack(text[23].title,
                            text[23].description, 25, 10)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new ColorChange(text[21].title, text[21].description,
                            -1, 7)).addPair(new ConditionAlways(),
                    new Attack(80)));
            zero.attackMode.addAttack(new ConditionHp(1, 20), seq);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 2277, 7500556, 22208, 870, 1,
                    new Pair<Integer, Integer>(4,0),
                    getMonsterTypes(env, 2277)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 30);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new ResistanceShieldAction(text[24].title,
                            text[24].description, 999)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[25].title, text[25].description, 80))
                    .addPair(new ConditionHp(2, 1), new RecoverSelf(50)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new Attack(text[29].title, text[29].description,
                                    100))
                    .addAction(new Transform(4, 2, 7))
                    .addPair(
                            new ConditionUsed(),
                            new LockOrb(text[30].title, text[30].description,
                                    0, 7)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[31].title, text[31].description,
                            100, 5)));
            zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

            RandomAttack rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new Gravity(text[26].title,
                            text[26].description, 99)));
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionPercentage(80), new Attack(text[27].title,
                            text[27].description, 120)));
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionPercentage(40), new MultipleAttack(
                            text[28].title, text[28].description, 70, 2)));
            zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
        }
        mStageEnemies.put(++stageCounter, data);
    }
}
