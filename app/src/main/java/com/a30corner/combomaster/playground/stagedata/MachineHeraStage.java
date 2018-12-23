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
import com.a30corner.combomaster.playground.action.impl.ColorChange;
import com.a30corner.combomaster.playground.action.impl.DamageAbsorbShield;
import com.a30corner.combomaster.playground.action.impl.DarkScreen;
import com.a30corner.combomaster.playground.action.impl.Daze;
import com.a30corner.combomaster.playground.action.impl.DropRateAttack;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.LineChange;
import com.a30corner.combomaster.playground.action.impl.LockAwoken;
import com.a30corner.combomaster.playground.action.impl.LockOrb;
import com.a30corner.combomaster.playground.action.impl.LockSkill;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.RandomLineChange;
import com.a30corner.combomaster.playground.action.impl.RecoverDead;
import com.a30corner.combomaster.playground.action.impl.RecoverSelf;
import com.a30corner.combomaster.playground.action.impl.ReduceTime;
import com.a30corner.combomaster.playground.action.impl.ResistanceShieldAction;
import com.a30corner.combomaster.playground.action.impl.ShieldAction;
import com.a30corner.combomaster.playground.action.impl.SkillDown;
import com.a30corner.combomaster.playground.enemy.EnemyAttack;
import com.a30corner.combomaster.playground.enemy.EnemyAttackMode;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.enemy.attack.RandomAttack;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAliveOnly;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAlways;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHp;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionTurns;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsedIf;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class MachineHeraStage extends IStage {

    @Override
    public void create(IEnvironment env,
                       SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        ArrayList<Enemy> data;
        Enemy zero = null;
        SequenceAttack seq = null;
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
            data.add(Enemy.create(env, 85, 1613833, 94170, 10400, 3,
                    new Pair<Integer, Integer>(3,-1),
                    getMonsterTypes(env, 85)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SkillDown(text[1].title,text[1].description,0, 1, 1)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[2].title, text[2].description, 20, 3)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            data.add(Enemy.create(env, 87, 1677722, 98388, 10400, 3,
                    new Pair<Integer, Integer>(0,-1),
                    getMonsterTypes(env, 87)));
            zero = data.get(1);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SkillDown(text[1].title,text[1].description,0, 1, 1)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[3].title, text[3].description, 60)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);
        }
        mStageEnemies.put(++stageCounter, data);

        final int[] ATTRIBUTE = {1,4,5,3,0};
        data = new ArrayList<Enemy>();
        {
            for(int i=0; i<3; ++i) {
                int index = RandomUtil.getInt(5);
                int random = 2195 + index;

                data.add(Enemy.create(env, random, 38, 4362, 933333, 1,
                        new Pair<Integer, Integer>(ATTRIBUTE[index],-1),
                        getMonsterTypes(env, random)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SkillDown(text[4].title,text[4].description,0,1,1)));
                zero.attackMode.addAttack(new ConditionHp(0, 100), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[5].title,text[5].description, 999, 200)));
                if(random==2195) {
                    seq.addAttack(new EnemyAttack().addAction(new Attack(280)).addPair(new ConditionAlways(), new RandomChange(text[6].title,text[6].description,1,3)));
                } else if(random==2196) {
                    seq.addAttack(new EnemyAttack().addAction(new Attack(280)).addPair(new ConditionAlways(), new RandomChange(text[7].title,text[7].description,4,3)));
                } else if(random==2197) {
                    seq.addAttack(new EnemyAttack().addAction(new Attack(280)).addPair(new ConditionAlways(), new RandomChange(text[8].title,text[8].description,5,3)));
                } else if(random==2198) {
                    seq.addAttack(new EnemyAttack().addAction(new Attack(200)).addPair(new ConditionAlways(), new BindPets(text[9].title,text[9].description,3,2,2,1)));
                } else if(random==2199) {
                    seq.addAttack(new EnemyAttack().addAction(new Attack(140)).addPair(new ConditionAlways(), new RandomChange(text[10].title,text[10].description,6,3)));
                }
                zero.attackMode.addAttack(new ConditionHp(2, 100), seq);

            }
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            int random = 897 + RandomUtil.getInt(5) * 2;

            if(random == 897) {
                data.add(Enemy.create(env, random, 5514889, 51162, 38400, 1,
                        new Pair<Integer, Integer>(1,-1),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[13].title,text[13].description,5, 200)));
                zero.attackMode.addAttack(new ConditionHp(2,50), seq);

                RandomAttack ra = new RandomAttack();
                ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[11].title, text[11].description, 50, 2)));
                ra.addAttack(new EnemyAttack().addAction(new Attack(text[12].title,text[12].description,80)).addPair(new ConditionTurns(2), new ColorChange(-1,1)));
                zero.attackMode.addAttack(new ConditionHp(1,25), ra);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[14].title,text[14].description,160)).addPair(new ConditionAlways(), new ColorChange(-1,1)));
                zero.attackMode.addAttack(new ConditionHp(2,25), seq);
            } else if(random == 899) {
                data.add(Enemy.create(env, random, 5578778, 48916, 38400, 1,
                        new Pair<Integer, Integer>(4,-1),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[13].title,text[13].description,5, 200)));
                zero.attackMode.addAttack(new ConditionHp(2,50), seq);

                RandomAttack ra = new RandomAttack();
                ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[16].title, text[16].description, 25, 4)));
                ra.addAttack(new EnemyAttack().addAction(new Attack(text[15].title,text[15].description,80)).addPair(new ConditionTurns(2), new ColorChange(-1,4)));
                zero.attackMode.addAttack(new ConditionHp(1,25), ra);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[17].title,text[17].description,160)).addPair(new ConditionAlways(), new ColorChange(-1,4)));
                zero.attackMode.addAttack(new ConditionHp(2,25), seq);
            } else if (random==901) {
                data.add(Enemy.create(env, random, 5546833, 50320, 38400, 1,
                        new Pair<Integer, Integer>(5,-1),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[13].title,text[13].description,5, 200)));
                zero.attackMode.addAttack(new ConditionHp(2,50), seq);

                RandomAttack ra = new RandomAttack();
                ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[19].title, text[19].description, 20, 5)));
                ra.addAttack(new EnemyAttack().addAction(new Attack(text[18].title,text[18].description,80)).addPair(new ConditionTurns(2), new ColorChange(-1,5)));
                zero.attackMode.addAttack(new ConditionHp(1,25), ra);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[20].title,text[20].description,160)).addPair(new ConditionAlways(), new ColorChange(-1,5)));
                zero.attackMode.addAttack(new ConditionHp(2,25), seq);
            } else if(random==903) {
                data.add(Enemy.create(env, random, 5578778, 48914, 38400, 1,
                        new Pair<Integer, Integer>(3,-1),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[13].title,text[13].description,5, 200)));
                zero.attackMode.addAttack(new ConditionHp(2,50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[22].title,text[22].description,160)).addPair(new ConditionAlways(), new ColorChange(-1,3)));
                zero.attackMode.addAttack(new ConditionHp(2,25), seq);

                RandomAttack ra = new RandomAttack();
                ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[21].title, text[21].description, 110)));
                ra.addAttack(new EnemyAttack().addAction(new Attack(text[23].title,text[23].description,80)).addPair(new ConditionAlways(), new ColorChange(-1,3)));
                zero.attackMode.addAttack(new ConditionHp(1,25), ra);

            } else if(random==905) {
                data.add(Enemy.create(env, random, 5578778, 48916, 38400, 1,
                        new Pair<Integer, Integer>(0,-1),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[13].title,text[13].description,5, 200)));
                zero.attackMode.addAttack(new ConditionHp(2,50), seq);

                RandomAttack ra = new RandomAttack();
                ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[25].title, text[25].description, 25, 4)));
                ra.addAttack(new EnemyAttack().addAction(new Attack(text[24].title,text[24].description,80)).addPair(new ConditionTurns(2), new ColorChange(-1,0)));
                zero.attackMode.addAttack(new ConditionHp(1,25), ra);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[26].title,text[26].description,160)).addPair(new ConditionAlways(), new ColorChange(-1,0)));
                zero.attackMode.addAttack(new ConditionHp(2,25), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data);

        //4
        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 2186, 6461319, 20187, 750, 1,
                    new Pair<Integer, Integer>(4,-1),
                    getMonsterTypes(env, 2186)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new DamageAbsorbShield(text[27].title,text[27].description,10,1000000)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[28].title,text[28].description,100)).addPair(new ConditionUsed(), new LockOrb(1,15,1,4,5,3,0,2,7)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(360)).addPair(new ConditionTurns(9), new RandomLineChange(text[29].title,text[29].description,3,4,7,6,5,0,1,2,3,4)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            RandomAttack ra = new RandomAttack();
            ra.addAttack(new EnemyAttack().addAction(new SkillDown(text[30].title,text[30].description,0,2,5)).addPair(new ConditionAlways(), new MultipleAttack(text[34].title,text[34].description,30,2)));
            ra.addAttack(new EnemyAttack().addAction(new Gravity(text[31].title,text[31].description,70)).addPair(new ConditionAlways(), new MultipleAttack(text[34].title,text[34].description,30,2)));
            ra.addAttack(new EnemyAttack().addAction(new ShieldAction(text[32].title,text[32].description,1,-1,50)).addPair(new ConditionAlways(), new MultipleAttack(text[34].title,text[34].description,30,2)));
            ra.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[33].title,text[33].description,70,2)).addPair(new ConditionAlways(), new MultipleAttack(text[34].title,text[34].description,30,2)));
            zero.attackMode.addAttack(new ConditionHp(1,0), ra);
        }
        mStageEnemies.put(++stageCounter, data);

        //5
        data = new ArrayList<Enemy>();
        {
            int random = 1272 + RandomUtil.getInt(3);
            if(random==1272) {
                data.add(Enemy.create(env, random, 5102886, 64924, 4950, 1,
                        new Pair<Integer, Integer>(1,3),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new LockSkill(text[35].title, text[35].description, 3)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Daze(text[36].title, text[36].description, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(text[37].title, text[37].description, 100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(text[38].title, text[38].description, 110)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Daze(text[39].title, text[39].description, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[40].title, text[40].description, 600)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            } else if(random==1273) {
                data.add(Enemy.create(env, random, 6267192, 57317, 4950, 1,
                        new Pair<Integer, Integer>(4,3),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new DropRateAttack(text[41].title, text[41].description, 10, 4, 15)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Daze(text[42].title, text[42].description, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(text[43].title, text[43].description, 100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new BindPets(text[44].title, text[44].description, 2, 2, 3, 1)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Daze(text[45].title, text[45].description, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[46].title, text[46].description, 100, 5)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            } else if(random==1274) {
                data.add(Enemy.create(env, random, 7385386, 29927, 30000, 1,
                        new Pair<Integer, Integer>(5,3),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ResistanceShieldAction(text[47].title, text[47].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Daze(text[48].title, text[48].description, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new DropRateAttack(text[49].title, text[49].description, 5, 7, 20)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new LockOrb(text[50].title, text[50].description, 0,7)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Daze(text[51].title, text[51].description, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[52].title, text[52].description, 100, 4)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data);

        //6
        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 2334, 5845625, 18095, 1250, 1,
                    new Pair<Integer, Integer>(4, -1),
                    getMonsterTypes(env, 2334)));
            zero = data.get(0);
            zero.setHasResurrection();
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new AbsorbShieldAction(
                            text[53].title, text[53].description, 5,
                            4)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionAliveOnly(), new RecoverDead(
                            text[54].title, text[54].description, 45)));
            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                            .ordinal(), 2, 80), new SkillDown(
                            text[55].title, text[55].description, 0,
                            1, 1)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            RandomAttack rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[56].title, text[56].description,
                            120)).addPair(new ConditionAlways(),
                    new RandomChange(4, 4)));
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[57].title, text[57].description,
                            120)).addPair(new ConditionAlways(),
                    new LineChange(8, 4)));
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[58].title, text[58].description,
                            80)).addPair(new ConditionAlways(),
                    new ColorChange(4, 7)));
            zero.attackMode
                    .addAttack(new ConditionHp(1, 10), rndAttack);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new MultipleAttack(
                            text[59].title, text[59].description,
                            500, 2)));
            zero.attackMode.addAttack(new ConditionHp(2, 10), seq);

            data.add(Enemy.create(env, 2337, 6863958, 16286, 28125, 1,
                    new Pair<Integer, Integer>(3, -1),
                    getMonsterTypes(env, 2337)));
            zero = data.get(1);
            zero.setHasResurrection();
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(),
                    new ResistanceShieldAction(text[60].title,
                            text[60].description, 999)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionAliveOnly(), new RecoverDead(
                            text[54].title, text[54].description, 45)));
            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                            .ordinal(), 2, 80), new SkillDown(
                            text[55].title, text[55].description, 0,
                            1, 1)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[61].title, text[61].description,
                            100)).addPair(new ConditionAlways(),
                    new DarkScreen(0)));
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[62].title, text[62].description,
                            100)).addPair(new ConditionAlways(),
                    new BindPets(3, 4, 4, 1)));
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[63].title, text[63].description,
                            120)).addPair(new ConditionAlways(),
                    new ColorChange(3, 7)));
            zero.attackMode
                    .addAttack(new ConditionHp(1, 10), rndAttack);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new MultipleAttack(
                            text[59].title, text[59].description,
                            500, 2)));
            zero.attackMode.addAttack(new ConditionHp(2, 10), seq);
        }
        mStageEnemies.put(++stageCounter, data);

        //7
        data = new ArrayList<Enemy>();
        {
            int random = RandomUtil.getInt(2) + 1206;
            if(random == 1206) {
                data = new ArrayList<Enemy>();
                data.add(Enemy.create(env, 1206, 11420942, 21119, 1742, 1,
                        new Pair<Integer, Integer>(5, 3),
                        getMonsterTypes(env, 1206)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ResistanceShieldAction(text[64].title,
                                text[64].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Angry(text[70].title, text[70].description, 999,
                                200)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(text[71].title, text[71].description, 200)));
                zero.attackMode.addAttack(new ConditionHp(2, 25), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[65].title, text[65].description, 125))
                        .addPair(new ConditionAlways(), new ColorChange(-1, 7)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[66].title,
                                text[66].description, 35, 5)));
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[67].title, text[67].description, 100))
                        .addPair(new ConditionAlways(),
                                new BindPets(2, 2, 3, 1)));
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[68].title, text[68].description, 150))
                        .addPair(new ConditionAlways(), new ColorChange(-1, 1)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Gravity(text[69].title, text[69].description, 99)));
                zero.attackMode.addAttack(new ConditionHp(1, 25), seq);
            } else {
                data = new ArrayList<Enemy>();
                data.add(Enemy.create(env, random, 12750942, 18937, 1742, 1,
                        new Pair<Integer, Integer>(0, 1),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ResistanceShieldAction(text[72].title,
                                text[72].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Daze(text[78].title, text[78].description, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[79].title,
                                text[79].description, 300, 3)));
                zero.attackMode.addAttack(new ConditionHp(2, 25), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[73].title, text[73].description, 100))
                        .addPair(new ConditionAlways(), new RandomChange(6, 6)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[74].title,
                                text[74].description, 25, 6, 7)));
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[75].title, text[75].description, 125))
                        .addPair(new ConditionAlways(), new DarkScreen(0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[76].title,
                                text[76].description, 100, 2)));
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[77].title, text[77].description, 75))
                        .addPair(new ConditionAlways(),
                                new BindPets(2, 2, 2, 4)));
                zero.attackMode.addAttack(new ConditionHp(1, 25), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data);

        //8
        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 2530, 5222708, 22199, 850, 1,
                    new Pair<Integer, Integer>(0, 1),
                    getMonsterTypes(env, 2530)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 1);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new DropRateAttack(
                            text[80].title, text[80].description, 15, 7, 10)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new ReduceTime(text[81].title,
                            text[81].description, 7, -3000)).addPair(
                    new ConditionHp(2, 1), new RecoverSelf(-2)));

            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                            .ordinal(), 2, 80), new SkillDown(
                            text[55].title, text[55].description, 0,
                            1, 1)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            RandomAttack rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new RandomChange(text[82].title, text[82].description,6, 5)));
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[83].title, text[83].description,
                            140)).addPair(new ConditionAlways(),
                    new DarkScreen(0)));
            zero.attackMode
                    .addAttack(new ConditionHp(1, 10), rndAttack);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new MultipleAttack(
                            text[59].title, text[59].description,
                            500, 2)));
            zero.attackMode.addAttack(new ConditionHp(2, 10), seq);

            /////////
            data.add(Enemy.create(env, 2531, 5672058, 19598, 850, 1,
                    new Pair<Integer, Integer>(1,5),
                    getMonsterTypes(env, 2531)));
            zero = data.get(1);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new MultipleAttack(
                            text[84].title, text[84].description, 120, 2)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.ALIVE_ONLY
                            .ordinal(), 1), new Angry(text[85].title, text[85].description,999,200)));

            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                            .ordinal(), 2, 80), new SkillDown(
                            text[55].title, text[55].description, 0,
                            1, 1)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[86].title, text[86].description,
                            150)).addPair(new ConditionAlways(),
                    new ColorChange(6,5)));
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[87].title, text[87].description,
                            130)).addPair(new ConditionAlways(),
                    new LineChange(0,1,2,5)));
            zero.attackMode.addAttack(new ConditionHp(1, 10), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new MultipleAttack(
                            text[59].title, text[59].description,
                            500, 2)));
            zero.attackMode.addAttack(new ConditionHp(2, 10), seq);
        }
        mStageEnemies.put(++stageCounter, data);
        //9
        data = new ArrayList<Enemy>();
        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 2128, 10686150, 31350, 820, 2,
                    new Pair<Integer, Integer>(0, 1),
                    getMonsterTypes(env, 2128)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ShieldAction(text[88].title, text[88].description, 5,-1,50)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);


            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[89].title, text[89].description, 150)).addPair(new ConditionUsed(), new ColorChange(-1, 1)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[90].title, text[90].description, 180)).addPair(new ConditionUsed(), new BindPets(2, 5, 5, 2)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[91].title, text[91].description, 500)).addPair(new ConditionAlways(), new LineChange(8,6,9,6)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);
        }
        mStageEnemies.put(++stageCounter, data);
        //10
        data = new ArrayList<Enemy>();
        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 2526, 41873700, 37125, 10000, 1,
                    new Pair<Integer, Integer>(0, -1),
                    getMonsterTypes(env, 2526)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(new AbsorbShieldAction(text[92].title, text[92].description, 2, 3))
                    .addAction(new AbsorbShieldAction(2, 0))
                    .addPair(new ConditionAlways(), new ResistanceShieldAction(text[93].title, text[93].description,999)
                    ));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 20),
                    new Gravity(text[94].title, text[94].description,1000)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 30),
                    new RecoverSelf(text[108].title, text[108].description,50)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            for(int i=0; i<3; ++i) {
                int which = RandomUtil.getInt(2);
                if(which==0) {
                    seq.addAttack(new EnemyAttack()
                            .addAction(new RandomChange(text[109].title, text[109].description,7,12))
                            .addPair(new ConditionAlways(), new LockAwoken(text[110].title, text[110].description,2)));
                } else {
                    seq.addAttack(new EnemyAttack()
                            .addAction(new BindPets(text[111].title, text[111].description,3,3,3,4))
                            .addPair(new ConditionAlways(), new Angry(text[112].title, text[112].description,2, 200)));
                }
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new MultipleAttack(text[113].title, text[113].description,30, 5, 8)));
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new MultipleAttack(text[113].title, text[113].description,30, 5, 8)));
            }
            zero.attackMode.addAttack(new ConditionHp(2,50), seq);

            RandomAttack ra = new RandomAttack();
            ra.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[98].title,text[98].description,2,1)).addAction(new Attack(text[99].title,text[99].description,130)).addPair(new ConditionAlways(), new ColorChange(-1,1)));
            ra.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[100].title,text[100].description,2,4)).addAction(new Attack(text[101].title,text[101].description,130)).addPair(new ConditionAlways(), new ColorChange(-1,4)));
            ra.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[102].title,text[102].description,2,5)).addAction(new Attack(text[103].title,text[103].description,130)).addPair(new ConditionAlways(), new ColorChange(-1,5)));
            ra.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[104].title,text[104].description,2,3)).addAction(new Attack(text[105].title,text[105].description,130)).addPair(new ConditionAlways(), new ColorChange(-1,3)));
            ra.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[106].title,text[106].description,2,0)).addAction(new Attack(text[107].title,text[107].description,130)).addPair(new ConditionAlways(), new ColorChange(-1,0)));
            zero.attackMode.addAttack(new ConditionTurns(2), ra);

            ra = new RandomAttack();
            ra.addAttack(new EnemyAttack().addAction(new Attack(text[95].title,text[95].description,130)).addPair(new ConditionAlways(), new SkillDown(0,3,3)));
            ra.addAttack(new EnemyAttack().addAction(new Attack(text[96].title,text[96].description,130)).addPair(new ConditionAlways(), new RandomChange(8,3)));
            ra.addAttack(new EnemyAttack().addAction(new Attack(text[97].title,text[97].description,130)).addPair(new ConditionAlways(), new LockOrb(1,8,1,4,5,3,0,2,7)));
            zero.attackMode.addAttack(new ConditionHp(1, 50), ra);
        }
        mStageEnemies.put(++stageCounter, data);
    }
}
