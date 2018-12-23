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
import com.a30corner.combomaster.playground.action.impl.DamageAbsorbShield;
import com.a30corner.combomaster.playground.action.impl.DarkScreen;
import com.a30corner.combomaster.playground.action.impl.Daze;
import com.a30corner.combomaster.playground.action.impl.DropLockAttack;
import com.a30corner.combomaster.playground.action.impl.DropRateAttack;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.LineChange;
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

public class MachineZeusStage extends IStage {

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

        {
            int random = RandomUtil.range(319, 320);
            data = new ArrayList<Enemy>();
            if (random == 319) {
                data.add(Enemy.create(env, random, 7614567, 2160, 9200, 3,
                        new Pair<Integer, Integer>(3, -1),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new SkillDown(text[0].title, text[0].description, 0, 2,
                                2)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(text[1].title, text[1].description, 1000)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else {
                data.add(Enemy.create(env, random, 7931233, 103233, 9200, 3,
                        new Pair<Integer, Integer>(0, -1),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new SkillDown(text[0].title, text[0].description, 0, 2,
                                2)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[2].title, text[2].description, 7,
                                5)).addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            }
        }
        mStageEnemies.put(1, data);

        {
            int random = 2582 + RandomUtil.getInt(5) * 2;
            data = new ArrayList<Enemy>();
            if (random == 2582) {
                data.add(Enemy.create(env, random, 5882800, 28200, 800, 1,
                        new Pair<Integer, Integer>(1, -1),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Gravity(text[3].title, text[3].description, 80)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                        new BindPets(text[8].title, text[8].description, 2, 3,
                                3, 2)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[9].title, text[9].description,
                                50, 6)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                RandomAttack rattack = new RandomAttack();
                rattack.addAttack(new EnemyAttack().addAction(
                        new Attack(text[4].title, text[4].description, 90))
                        .addPair(new ConditionAlways(), new DarkScreen(1)));
                rattack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new SkillDown(text[5].title,
                                text[5].description, 0, 1, 1)));
                rattack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new RandomChange(text[6].title,
                                text[6].description, 6, 7)));
                rattack.addAttack(new EnemyAttack().addAction(
                        new Attack(text[7].title, text[7].description, 110))
                        .addPair(new ConditionAlways(), new LockOrb(0, 1)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), rattack);
            } else if (random == 2584) {
                data.add(Enemy.create(env, random, 6043233, 26712, 800, 1,
                        new Pair<Integer, Integer>(4, -1),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Gravity(text[3].title, text[3].description, 80)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                        new BindPets(text[8].title, text[8].description, 2, 3,
                                3, 2)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[9].title, text[9].description,
                                50, 6)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                RandomAttack rattack = new RandomAttack();
                rattack.addAttack(new EnemyAttack().addAction(
                        new Attack(text[4].title, text[4].description, 90))
                        .addPair(new ConditionAlways(), new DarkScreen(1)));
                rattack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new SkillDown(text[5].title,
                                text[5].description, 0, 1, 1)));
                rattack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new RandomChange(text[6].title,
                                text[6].description, 6, 7)));
                rattack.addAttack(new EnemyAttack().addAction(
                        new Attack(text[10].title, text[10].description, 110))
                        .addPair(new ConditionAlways(), new LockOrb(0, 4)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), rattack);
            } else if (random == 2586) {
                data.add(Enemy.create(env, random, 5778250, 29062, 800, 1,
                        new Pair<Integer, Integer>(5, -1),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Gravity(text[3].title, text[3].description, 80)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                        new BindPets(text[8].title, text[8].description, 2, 3,
                                3, 2)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[9].title, text[9].description,
                                50, 6)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                RandomAttack rattack = new RandomAttack();
                rattack.addAttack(new EnemyAttack().addAction(
                        new Attack(text[4].title, text[4].description, 90))
                        .addPair(new ConditionAlways(), new DarkScreen(1)));
                rattack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new SkillDown(text[5].title,
                                text[5].description, 0, 1, 1)));
                rattack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new RandomChange(text[6].title,
                                text[6].description, 6, 7)));
                rattack.addAttack(new EnemyAttack().addAction(
                        new Attack(text[11].title, text[11].description, 110))
                        .addPair(new ConditionAlways(), new LockOrb(0, 5)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), rattack);
            } else if (random == 2588) {
                data.add(Enemy.create(env, random, 6008633, 27652, 800, 1,
                        new Pair<Integer, Integer>(3, -1),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Gravity(text[3].title, text[3].description, 80)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                        new BindPets(text[8].title, text[8].description, 2, 3,
                                3, 2)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[9].title, text[9].description,
                                50, 6)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                RandomAttack rattack = new RandomAttack();
                rattack.addAttack(new EnemyAttack().addAction(
                        new Attack(text[4].title, text[4].description, 90))
                        .addPair(new ConditionAlways(), new DarkScreen(1)));
                rattack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new SkillDown(text[5].title,
                                text[5].description, 0, 1, 1)));
                rattack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new RandomChange(text[6].title,
                                text[6].description, 6, 7)));
                rattack.addAttack(new EnemyAttack().addAction(
                        new Attack(text[12].title, text[12].description, 110))
                        .addPair(new ConditionAlways(), new LockOrb(0, 3)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), rattack);
            } else if (random == 2590) {
                data.add(Enemy.create(env, random, 5957150, 27182, 800, 1,
                        new Pair<Integer, Integer>(0, -1),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Gravity(text[3].title, text[3].description, 80)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                        new BindPets(text[8].title, text[8].description, 2, 3,
                                3, 2)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[9].title, text[9].description,
                                50, 6)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                RandomAttack rattack = new RandomAttack();
                rattack.addAttack(new EnemyAttack().addAction(
                        new Attack(text[4].title, text[4].description, 90))
                        .addPair(new ConditionAlways(), new DarkScreen(1)));
                rattack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new SkillDown(text[5].title,
                                text[5].description, 0, 1, 1)));
                rattack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new RandomChange(text[6].title,
                                text[6].description, 6, 7)));
                rattack.addAttack(new EnemyAttack().addAction(
                        new Attack(text[13].title, text[13].description, 110))
                        .addPair(new ConditionAlways(), new LockOrb(0, 0)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), rattack);
            }
        }
        mStageEnemies.put(2, data);

        {
            int index = RandomUtil.getInt(2);
            int random = 150 + index;
            int color = (index == 0) ? 3 : 0;
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, random, 2778819, 41986, 1500, 3,
                    new Pair<Integer, Integer>(color, -1),
                    getMonsterTypes(env, random)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(text[14].title, text[14].description, 75))); // new
            // ResistanceShieldAction(99)
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 25),
                    new Attack(text[15].title, text[15].description, 200)));
            zero.attackMode.addAttack(new ConditionHp(2, 25), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
        }
        mStageEnemies.put(3, data);

        {
            data = new ArrayList<Enemy>();
            for (int i = 0; i < 3; ++i) {
                int random = 2195 + RandomUtil.getInt(5);
                if (random == 2195) {
                    data.add(Enemy.create(env, random, 38, 3965, 933333, 1,
                            new Pair<Integer, Integer>(1, -1),
                            getMonsterTypes(env, random)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createEmptyFirstStrike()
                            .createEmptyMustAction();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new SkillDown(
                                    text[16].title, text[16].description, 0, 1,
                                    1)));
                    zero.attackMode.addAttack(new ConditionHp(1, 100), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionUsed(), new Angry(text[17].title,
                                    text[17].description, 999, 200)));
                    seq.addAttack(new EnemyAttack().addAction(
                            new Attack(text[18].title, text[18].description,
                                    280)).addPair(new ConditionAlways(),
                            new RandomChange(1, 3)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (random == 2196) {
                    data.add(Enemy.create(env, random, 38, 3965, 933333, 1,
                            new Pair<Integer, Integer>(4, -1),
                            getMonsterTypes(env, random)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createEmptyFirstStrike()
                            .createEmptyMustAction();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new SkillDown(
                                    text[16].title, text[16].description, 0, 1,
                                    1)));
                    zero.attackMode.addAttack(new ConditionHp(1, 100), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionUsed(), new Angry(text[17].title,
                                    text[17].description, 999, 200)));
                    seq.addAttack(new EnemyAttack().addAction(
                            new Attack(text[19].title, text[19].description,
                                    280)).addPair(new ConditionAlways(),
                            new RandomChange(4, 3)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (random == 2197) {
                    data.add(Enemy.create(env, random, 38, 3965, 933333, 1,
                            new Pair<Integer, Integer>(5, -1),
                            getMonsterTypes(env, random)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createEmptyFirstStrike()
                            .createEmptyMustAction();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new SkillDown(
                                    text[16].title, text[16].description, 0, 1,
                                    1)));
                    zero.attackMode.addAttack(new ConditionHp(1, 100), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionUsed(), new Angry(text[17].title,
                                    text[17].description, 999,200)));
                    seq.addAttack(new EnemyAttack().addAction(
                            new Attack(text[20].title, text[20].description,
                                    280)).addPair(new ConditionAlways(),
                            new RandomChange(5, 3)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (random == 2198) {
                    data.add(Enemy.create(env, random, 38, 3965, 933333, 1,
                            new Pair<Integer, Integer>(3, -1),
                            getMonsterTypes(env, random)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createEmptyFirstStrike()
                            .createEmptyMustAction();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new SkillDown(
                                    text[16].title, text[16].description, 0, 1,
                                    1)));
                    zero.attackMode.addAttack(new ConditionHp(1, 100), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionUsed(), new Angry(text[17].title,
                                    text[17].description, 999, 200)));
                    seq.addAttack(new EnemyAttack().addAction(
                            new Attack(text[21].title, text[21].description,
                                    200)).addPair(new ConditionAlways(),
                            new BindPets(3, 2, 2, 1)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (random == 2199) {
                    data.add(Enemy.create(env, random, 38, 3965, 933333, 1,
                            new Pair<Integer, Integer>(0, -1),
                            getMonsterTypes(env, random)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createEmptyFirstStrike()
                            .createEmptyMustAction();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new SkillDown(
                                    text[16].title, text[16].description, 0, 1,
                                    1)));
                    zero.attackMode.addAttack(new ConditionHp(1, 100), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionUsed(), new Angry(text[17].title,
                                    text[17].description, 999, 200)));
                    seq.addAttack(new EnemyAttack().addAction(
                            new Attack(text[22].title, text[22].description,
                                    140)).addPair(new ConditionAlways(),
                            new RandomChange(6, 3)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                }
            }
        }
        mStageEnemies.put(4, data);
        {
            int random = 1206 + RandomUtil.getInt(2);
            if (random == 1206) {
                data = new ArrayList<Enemy>();
                data.add(Enemy.create(env, random, 11420942, 19199, 1742, 1,
                        new Pair<Integer, Integer>(5, 3),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ResistanceShieldAction(text[23].title,
                                text[23].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Angry(text[29].title, text[29].description, 999,
                                200)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(text[30].title, text[30].description, 200)));
                zero.attackMode.addAttack(new ConditionHp(2, 25), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[24].title, text[24].description, 125))
                        .addPair(new ConditionAlways(), new ColorChange(-1, 7)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[25].title,
                                text[25].description, 35, 5)));
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[26].title, text[26].description, 100))
                        .addPair(new ConditionAlways(),
                                new BindPets(2, 2, 3, 1)));
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[27].title, text[27].description, 150))
                        .addPair(new ConditionAlways(), new ColorChange(-1, 1)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Gravity(text[28].title, text[28].description, 99)));
                zero.attackMode.addAttack(new ConditionHp(1, 25), seq);
            } else {
                data = new ArrayList<Enemy>();
                data.add(Enemy.create(env, random, 12750942, 17215, 1742, 1,
                        new Pair<Integer, Integer>(0, 1),
                        getMonsterTypes(env, random)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ResistanceShieldAction(text[31].title,
                                text[31].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Daze(text[37].title, text[37].description, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[38].title,
                                text[38].description, 300, 3)));
                zero.attackMode.addAttack(new ConditionHp(2, 25), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[32].title, text[32].description, 100))
                        .addPair(new ConditionAlways(), new RandomChange(6, 6)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[33].title,
                                text[33].description, 25, 6, 7)));
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[34].title, text[34].description, 125))
                        .addPair(new ConditionAlways(), new DarkScreen(0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[35].title,
                                text[35].description, 100, 2)));
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[36].title, text[36].description, 75))
                        .addPair(new ConditionAlways(),
                                new BindPets(2, 2, 2, 4)));
                zero.attackMode.addAttack(new ConditionHp(1, 25), seq);
            }
        }
        mStageEnemies.put(5, data);

        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 2335, 6550625, 18252, 1250, 1,
                    new Pair<Integer, Integer>(3, -1),
                    getMonsterTypes(env, 2335)));
            zero = data.get(0);
            zero.setHasResurrection();
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[39].title, text[39].description, 60))
                    .addPair(new ConditionAlways(), new BindPets(0, 2, 2, 0)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAliveOnly(),
                    new RecoverDead(text[40].title, text[40].description, 45)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1,
                    0, EnemyCondition.Type.HP.ordinal(), 2, 80), new SkillDown(
                    text[41].title, text[41].description, 0, 1, 1)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            RandomAttack rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[42].title, text[42].description, 120))
                    .addPair(new ConditionAlways(), new LockOrb(0, 3)));
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[43].title, text[43].description, 100))
                    .addPair(new ConditionAlways(), new RandomChange(3, 5)));
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[44].title, text[44].description, 80))
                    .addPair(new ConditionHp(2, 70), new ColorChange(3, 6)));
            zero.attackMode.addAttack(new ConditionHp(1, 10), rndAttack);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[45].title, text[45].description,
                            500, 2)));
            zero.attackMode.addAttack(new ConditionHp(2, 10), seq);

            data.add(Enemy.create(env, 2338, 6315625, 16607, 28125, 1,
                    new Pair<Integer, Integer>(0, -1),
                    getMonsterTypes(env, 2338)));
            zero = data.get(1);
            zero.setHasResurrection();
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new ShieldAction(text[46].title, text[46].description, 3,
                            -1, 30)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAliveOnly(),
                    new RecoverDead(text[40].title, text[40].description, 45)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1,
                    0, EnemyCondition.Type.HP.ordinal(), 2, 80), new SkillDown(
                    text[41].title, text[41].description, 0, 1, 1)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[47].title, text[47].description, 100))
                    .addPair(new ConditionAlways(), new DarkScreen(0)));
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new RandomChange(text[48].title,
                            text[48].description, 6, 4)));
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionHp(2, 70), new Gravity(text[49].title,
                            text[49].description, 75)));
            zero.attackMode.addAttack(new ConditionHp(1, 10), rndAttack);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[45].title, text[45].description,
                            500, 2)));
            zero.attackMode.addAttack(new ConditionHp(2, 10), seq);
        }
        mStageEnemies.put(6, data);

        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 2127, 9006150, 23910, 20000, 1,
                    new Pair<Integer, Integer>(3, 5),
                    getMonsterTypes(env, 2127)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionAlways(), new SkillDown(
                            text[50].title, text[50].description, 0, 1, 3)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[51].title, text[51].description, 150))
                    .addPair(new ConditionHp(2, 1), new ReduceTime(10, -2000)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 10),
                    new Attack(text[56].title, text[56].description, 500)));
            seq.addAttack(new EnemyAttack().addAction(
                    new Angry(text[52].title, text[52].description, 999, 200))
                    .addPair(
                            new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                                    .ordinal(), 2, 50),
                            new Gravity(text[53].title, text[53].description,
                                    99)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            RandomAttack rattack = new RandomAttack();
            rattack.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[54].title, text[54].description,
                            20, 6)));
            rattack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[55].title, text[55].description, 100))
                    .addPair(new ConditionAlways(), new LineChange(7, 7, 9, 6)));
            rattack.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 10), rattack);
        }
        mStageEnemies.put(7, data);

        {
            data = new ArrayList<Enemy>();

            data.add(Enemy.create(env, 2532, 143214, 18550, 1000000, 1,
                    new Pair<Integer, Integer>(4, 3),
                    getMonsterTypes(env, 2532)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new DropLockAttack(text[62].title, text[62].description,
                            99, -1, 15)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAliveOnly(),
                    new Angry(text[63].title, text[63].description, 999, 200)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[65].title, text[65].description, 18))
                    .addPair(new ConditionAlways(), new DarkScreen(0)));
            zero.attackMode.addAttack(new ConditionHp(1, 99), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(text[64].title, text[64].description, 65)));
            zero.attackMode.addAttack(new ConditionHp(2, 99), seq);

            data.add(Enemy.create(env, 2529, 4873214, 19340, 850, 1,
                    new Pair<Integer, Integer>(5, 4),
                    getMonsterTypes(env, 2529)));
            zero = data.get(1);
            zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 1);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new DropRateAttack(text[57].title, text[57].description,
                            10, 7, 15)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new RandomChange(text[61].title,
                                    text[61].description, 8, 6)).addPair(
                            new ConditionHp(2, 1), new RecoverSelf(-2)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            RandomAttack rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionUsedIf(1, 1, 0,EnemyCondition.Type.HP.ordinal(), 2, 80), new SkillDown(text[41].title,
                            text[41].description, 0, 1, 1)));
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[59].title, text[59].description, 90))
                    .addPair(new ConditionAlways(), new BindPets(3, 3, 3, 1)));
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[60].title, text[60].description, 100))
                    .addPair(new ConditionHp(2, 70), new RandomLineChange(2,4,5,2,12,13)));
            zero.attackMode.addAttack(new ConditionHp(1, 10), rndAttack);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[45].title, text[45].description,
                            500, 2)));
            zero.attackMode.addAttack(new ConditionHp(2, 10), seq);

        }
        mStageEnemies.put(8, data);

        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 2189, 10138681, 22502, 750, 1,
                    new Pair<Integer, Integer>(0, -1),
                    getMonsterTypes(env, 2189)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new RandomChange(text[66].title, text[66].description, 7,
                            10)).addPair(new ConditionAlways(),
                    new LockOrb(text[67].title, text[67].description, 0, 7)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[68].title, text[68].description, 100))
                    .addPair(new ConditionUsed(), new BindPets(3, 4, 4, 6)));
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[69].title, text[69].description, 100))
                    .addPair(new ConditionUsed(), new LockSkill(4)));
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[70].title, text[70].description, 100))
                    .addPair(new ConditionUsed(), new DarkScreen(0)));
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionUsed(), new SkillDown(text[71].title,
                            text[71].description, 0, 4, 4)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[72].title, text[72].description,
                            200, 8)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
        }
        mStageEnemies.put(9, data);

        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 2528, 49452000, 17100, 3160, 1,
                    new Pair<Integer, Integer>(3, -1),
                    getMonsterTypes(env, 2528)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new ResistanceShieldAction(text[74].title,
                            text[74].description, 999)).addPair(
                    new ConditionAlways(),
                    new ComboShield(text[73].title, text[73].description, 999,
                            7)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 20),
                    new MultipleAttack(text[75].title, text[75].description,
                            800, 8)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new DamageAbsorbShield(text[76].title,
                            text[76].description, 5, 2000000)).addPair(
                    new ConditionUsed(),
                    new Daze(text[77].title, text[77].description, 0)));
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new SkillDown(text[78].title, text[78].description,
                                    0, 3, 3)).addPair(
                            new ConditionTurns(3),
                            new MultipleAttack(text[79].title,
                                    text[79].description, 30, 6)));
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new RandomLineChange(text[80].title, text[80].description,3,3,7,8,2,0,4))
                    .addPair(
                            new ConditionAlways(),
                            new Attack(180)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[81].title,
                    text[81].description, 30, 6)));

            zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1,
                            0, EnemyCondition.Type.FIND_TYPE.ordinal(), 1, 6),
                    new BindPets(text[82].title, text[82].description, 4, 10,
                            10, 1, 6)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1,
                            1, EnemyCondition.Type.FIND_TYPE.ordinal(), 1, 6),
                    new BindPets(text[83].title, text[83].description, 3, 10,
                            10, 2)));
            zero.attackMode.addAttack(new ConditionUsedIf(1, 1, 0,
                    EnemyCondition.Type.HP.ordinal(), 2, 70), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[85].title, text[85].description, 190))
                    .addPair(new ConditionAlways(), new RandomChange(8, 5)));
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[85].title, text[85].description, 190))
                    .addPair(new ConditionAlways(), new RandomChange(8, 5)));
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[84].title, text[84].description, 90))
                    .addPair(new ConditionAlways(), new BindPets(2, 3, 3, 1)));
            zero.attackMode.addAttack(new ConditionHp(1, 50), seq);
        }
        mStageEnemies.put(10, data);
    }
}
