package com.a30corner.combomaster.playground.stagedata;

import android.util.Pair;
import android.util.SparseArray;

import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.StageGenerator;
import com.a30corner.combomaster.playground.StageGenerator;
import com.a30corner.combomaster.playground.action.impl.AbsorbShieldAction;
import com.a30corner.combomaster.playground.action.impl.Angry;
import com.a30corner.combomaster.playground.action.impl.Attack;
import com.a30corner.combomaster.playground.action.impl.BindPets;
import com.a30corner.combomaster.playground.action.impl.ChangeAttribute;
import com.a30corner.combomaster.playground.action.impl.ColorChange;
import com.a30corner.combomaster.playground.action.impl.ComboShield;
import com.a30corner.combomaster.playground.action.impl.DamageAbsorbShield;
import com.a30corner.combomaster.playground.action.impl.Daze;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.LineChange;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.RandomLineChange;
import com.a30corner.combomaster.playground.action.impl.ReduceTime;
import com.a30corner.combomaster.playground.action.impl.ResistanceShieldAction;
import com.a30corner.combomaster.playground.action.impl.SkillDown;
import com.a30corner.combomaster.playground.enemy.EnemyAttack;
import com.a30corner.combomaster.playground.enemy.EnemyAttackMode;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.enemy.attack.RandomAttack;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
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

public class Challenge34Lv10 extends IStage {
    
    @Override
    public void create(IEnvironment env,
                       SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        ArrayList<Enemy> data;
        Enemy zero;
        SequenceAttack seq ;
        mStageEnemies.clear();

        SparseArray<List<StageGenerator.SkillText>> list = loadSkillTextNew(env, mStage, new int[]{2127, 2294, 2296, 2298, 2528});
        {
            StageGenerator.SkillText[] text = loadSubText(list, 2127);

            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 2127, 7071496, 18774, 20000, 1,
                    new Pair<Integer, Integer>(3, 5),
                    getMonsterTypes(env, 2127)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionAlways(), new SkillDown(
                            text[1].title, text[1].description, 0, 1, 3)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[2].title, text[2].description, 150))
                    .addPair(new ConditionHp(2, 1), new ReduceTime(10, -2000)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 10),
                    new Attack(text[7].title, text[7].description, 500)));
            seq.addAttack(new EnemyAttack().addAction(
                    new Angry(text[3].title, text[3].description, 999, 200))
                    .addPair(
                            new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                                    .ordinal(), 2, 50),
                            new Gravity(text[4].title, text[4].description,
                                    99)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            RandomAttack rattack = new RandomAttack();
            rattack.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[5].title, text[5].description,
                            20, 6)));
            rattack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[6].title, text[6].description, 100))
                    .addPair(new ConditionAlways(), new LineChange(7, 7, 9, 6)));
            rattack.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 10), rattack);
        }
        mStageEnemies.put(1, data);

        {
            int random = RandomUtil.chooseOne(new int[]{2294,2296,2298});

            data = new ArrayList<Enemy>();
            if(random == 2298) {
                StageGenerator.SkillText[] text = loadSubText(list, 2298);
                data.add(Enemy.create(env, 2298, 7226373, 20187, 928, 1,
                        new Pair<Integer, Integer>(5, 4),
                        getMonsterTypes(env, 2298)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new AbsorbShieldAction(text[0].title,
                                        text[0].description, 5, 0))
                        .addAction(new AbsorbShieldAction(5, 3))
                        .addAction(
                                new ColorChange(text[1].title, text[1].description,
                                        2, 5))
                        .addPair(new ConditionAlways(), new Attack(90)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new ChangeAttribute(text[2].title,
                                        text[2].description, 4, 5))
                        .addAction(
                                new ColorChange(text[3].title, text[3].description,
                                        1, 4))
                        .addPair(new ConditionAlways(), new Attack(110)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new ChangeAttribute(text[2].title,
                                        text[2].description, 4, 5))
                        .addAction(
                                new ColorChange(text[4].title, text[4].description,
                                        2, 5))
                        .addPair(new ConditionAlways(), new Attack(90)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new ChangeAttribute(text[2].title,
                                        text[2].description, 4, 5))
                        .addAction(
                                new LineChange(text[5].title, text[5].description,
                                        1, 4))
                        .addPair(new ConditionAlways(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), rndAttack);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ResistanceShieldAction(text[6].title,
                                text[6].description, 1))
                        .addPair(
                                new ConditionAlways(),
                                new ReduceTime(text[7].title, text[7].description,
                                        1, -3000)));
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[8].title, text[8].description, 0, 5, 1,
                                5, 2, 5, 5, 4, 6, 4)).addPair(
                        new ConditionAlways(), new Attack(220)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);
            } else if(random == 2296) {
                StageGenerator.SkillText[] text = loadSubText(list, 2296);
                data.add(Enemy.create(env, 2296, 7343916, 20399, 928, 1,
                        new Pair<Integer, Integer>(4, 1),
                        getMonsterTypes(env, 2296)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new AbsorbShieldAction(text[0].title,
                                        text[0].description, 5, 0))
                        .addAction(new AbsorbShieldAction(5, 3))
                        .addAction(
                                new ColorChange(text[1].title, text[1].description,
                                        2, 4))
                        .addPair(new ConditionAlways(), new Attack(90)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new ChangeAttribute(text[2].title,
                                        text[2].description, 4, 1))
                        .addAction(
                                new ColorChange(text[3].title, text[3].description,
                                        5, 1))
                        .addPair(new ConditionAlways(), new Attack(110)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new ChangeAttribute(text[2].title,
                                        text[2].description, 4, 1))
                        .addAction(
                                new ColorChange(text[4].title, text[4].description,
                                        2, 4))
                        .addPair(new ConditionAlways(), new Attack(90)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new ChangeAttribute(text[2].title,
                                        text[2].description, 4, 1))
                        .addAction(
                                new LineChange(text[5].title, text[5].description,
                                        1, 1))
                        .addPair(new ConditionAlways(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), rndAttack);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ResistanceShieldAction(text[6].title,
                                text[6].description, 1))
                        .addPair(
                                new ConditionAlways(),
                                new BindPets(text[7].title, text[7].description,
                                        1,1,1,0)));
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[8].title, text[8].description, 0, 4, 1,
                                4, 2, 4, 5, 1, 6, 1)).addPair(
                        new ConditionAlways(), new Attack(220)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);
            } else {
                StageGenerator.SkillText[] text = loadSubText(list, 2294);
                data.add(Enemy.create(env, 2294, 7108124, 20399, 928, 1,
                        new Pair<Integer, Integer>(1, 5),
                        getMonsterTypes(env, 2294)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new AbsorbShieldAction(text[0].title,
                                        text[0].description, 5, 0))
                        .addAction(new AbsorbShieldAction(5, 3))
                        .addAction(
                                new ColorChange(text[1].title, text[1].description,
                                        2, 1))
                        .addPair(new ConditionAlways(), new Attack(90)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new ChangeAttribute(text[2].title,
                                        text[2].description, 1, 5))
                        .addAction(
                                new ColorChange(text[3].title, text[3].description,
                                        4, 5))
                        .addPair(new ConditionAlways(), new Attack(110)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new ChangeAttribute(text[2].title,
                                        text[2].description, 1, 5))
                        .addAction(
                                new ColorChange(text[4].title, text[4].description,
                                        2, 1))
                        .addPair(new ConditionAlways(), new Attack(90)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new ChangeAttribute(text[2].title,
                                        text[2].description, 1, 5))
                        .addAction(
                                new LineChange(text[5].title, text[5].description,
                                        1, 5))
                        .addPair(new ConditionAlways(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), rndAttack);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ResistanceShieldAction(text[6].title,
                                text[6].description, 1))
                        .addPair(
                                new ConditionAlways(),
                                new RandomChange(text[7].title, text[7].description,
                                        7,15)));
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[8].title, text[8].description, 0, 1, 1,
                                1, 2, 1, 5, 5, 6, 5)).addPair(
                        new ConditionAlways(), new Attack(220)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);
            }
        }
        mStageEnemies.put(2, data);

        {
            StageGenerator.SkillText[] text = loadSubText(list, 2528);
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 2528, 49452000, 17100, 3160, 1,
                    new Pair<Integer, Integer>(3, -1),
                    getMonsterTypes(env, 2528)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new ResistanceShieldAction(text[2].title,
                            text[2].description, 999)).addPair(
                    new ConditionAlways(),
                    new ComboShield(text[1].title, text[1].description, 999,
                            7)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 20),
                    new MultipleAttack(text[3].title, text[3].description,
                            800, 8)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new DamageAbsorbShield(text[4].title,
                            text[4].description, 5, 2000000)).addPair(
                    new ConditionUsed(),
                    new Daze(text[5].title, text[5].description, 0)));
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new SkillDown(text[6].title, text[6].description,
                                    0, 3, 3)).addPair(
                            new ConditionTurns(3),
                            new MultipleAttack(text[7].title,
                                    text[7].description, 30, 6)));
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new RandomLineChange(text[8].title, text[8].description,3,3,7,8,2,0,4))
                    .addPair(
                            new ConditionAlways(),
                            new Attack(180)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[9].title,
                    text[9].description, 30, 6)));

            zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1,
                            0, EnemyCondition.Type.FIND_TYPE.ordinal(), 1, 6),
                    new BindPets(text[10].title, text[10].description, 4, 10,
                            10, 1, 6)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1,
                            1, EnemyCondition.Type.FIND_TYPE.ordinal(), 1, 6),
                    new BindPets(text[11].title, text[11].description, 3, 10,
                            10, 2)));
            zero.attackMode.addAttack(new ConditionUsedIf(1, 1, 0,
                    EnemyCondition.Type.HP.ordinal(), 2, 70), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[13].title, text[13].description, 190))
                    .addPair(new ConditionAlways(), new RandomChange(8, 5)));
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[13].title, text[13].description, 190))
                    .addPair(new ConditionAlways(), new RandomChange(8, 5)));
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[12].title, text[12].description, 90))
                    .addPair(new ConditionAlways(), new BindPets(2, 3, 3, 1)));
            zero.attackMode.addAttack(new ConditionHp(1, 50), seq);
        }
        mStageEnemies.put(3, data);
    }
}
