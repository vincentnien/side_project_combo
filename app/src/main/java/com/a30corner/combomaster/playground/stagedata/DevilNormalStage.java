package com.a30corner.combomaster.playground.stagedata;

import android.util.Pair;
import android.util.SparseArray;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.StageGenerator;
import com.a30corner.combomaster.playground.action.impl.Angry;
import com.a30corner.combomaster.playground.action.impl.Attack;
import com.a30corner.combomaster.playground.action.impl.BindPets;
import com.a30corner.combomaster.playground.action.impl.ColorChange;
import com.a30corner.combomaster.playground.action.impl.DarkScreen;
import com.a30corner.combomaster.playground.action.impl.Daze;
import com.a30corner.combomaster.playground.action.impl.DropRateAttack;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.ResistanceShieldAction;
import com.a30corner.combomaster.playground.action.impl.Transform;
import com.a30corner.combomaster.playground.enemy.EnemyAttack;
import com.a30corner.combomaster.playground.enemy.EnemyAttackMode;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.enemy.attack.RandomAttack;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAlways;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHp;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionPercentage;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionTurns;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsedIf;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class DevilNormalStage extends IStage {

    @Override
    public void create(IEnvironment env,
                       SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        ArrayList<Enemy> data;
        Enemy zero = null;
        SequenceAttack seq = null;
        mStageEnemies.clear();

        List<StageGenerator.SkillText> list = loadSkillText(env, "devilsuper");
        StageGenerator.SkillText[] text = new StageGenerator.SkillText[list.size()];
        int len = text.length;
        for (int i = 0; i < len; ++i) {
            text[i] = getSkillText(list, i);
        }
        int stageCounter = 0;
        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 1062, 2571543, 27652, 0, 2,
                    new Pair<Integer, Integer>(0, 5),
                    getMonsterTypes(env, 1062)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new DropRateAttack(text[0].title,
                            text[0].description, 10, 6, 5)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                            1, 0, EnemyCondition.Type.FIND_TYPE.ordinal(), 1, 0),
                    new BindPets(text[2].title, text[2].description, 4,
                            3, 3, 1, 0)));
            seq.addAttack(new EnemyAttack().addAction(
                    new RandomChange(text[1].title, text[1].description,
                            6, 3)).addPair(new ConditionAlways(),
                    new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new ColorChange(text[3].title, text[3].description,
                            -1, 7)).addPair(new ConditionAlways(),
                    new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            int[] random = {895, 1324, 1211, 1214};

            int choose = RandomUtil.chooseOne(random);
            if(choose == 895) {
                data.add(Enemy.create(env, choose, 2570174, 12868, 2460, 1,
                        new Pair<Integer, Integer>(4, 0),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Gravity(text[4].title, text[4].description, 99)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionUsedIf(1,1, 0, EnemyCondition.Type.FIND_ATTR.ordinal(), 0),
                                new BindPets(text[6].title, text[6].description, 5, 3, 3, 1, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Daze(text[7].title, text[7].description, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[8].title, text[8].description, 25, 8)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                RandomAttack rnd = new RandomAttack();
                rnd.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[5].title, text[5].description, -1, 4))
                        .addPair(new ConditionAlways(),new Attack(75)));
                rnd.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), rnd);
            } else if(choose == 1324) {
                data.add(Enemy.create(env, choose, 44456, 5500, 1500000, 1,
                        new Pair<Integer, Integer>(0, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Angry(text[9].title, text[9].description, 5, 200)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionTurns(6),
                        new Attack(text[11].title, text[11].description, 5000)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[10].title, text[10].description, -1,
                                7)).addPair(new ConditionPercentage(50),
                        new Attack(80)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), rndAttack);
            } else if (choose == 1211) {
                data.add(Enemy.create(env, choose, 2359964, 24743, 0, 2,
                        new Pair<Integer, Integer>(4, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.FIND_ATTR.ordinal(), 5),
                        new BindPets(text[12].title, text[12].description, 5,
                                3, 3, 1, 5)));
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new DarkScreen(text[13].title,
                                        text[13].description, 0))
                        .addPair(new ConditionUsedIf(
                                1,1,1,EnemyCondition.Type.FIND_ATTR.ordinal(),5), new Attack(25)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 30),
                        new MultipleAttack(text[15].title,
                                text[15].description, 50, 3)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[14].title, text[14].description, 2,
                                2, 2, 2)).addPair(new ConditionAlways(),
                        new Attack(75)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), seq);
            } else if (choose == 1214) {
                data.add(Enemy.create(env, choose, 1299076, 21408, 408, 1,
                        new Pair<Integer, Integer>(0, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ColorChange(text[16].title, text[16].description,
                                -1, 7, -1, 7)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 25),
                        new MultipleAttack(text[18].title,
                                text[18].description, 50, 3)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[17].title, text[17].description,
                                6, 3)).addPair(new ConditionAlways(),
                        new Attack(25)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 1510, 4634236, 18070, 3350, 1,
                    new Pair<Integer, Integer>(3, 0),
                    getMonsterTypes(env, 1510)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new ResistanceShieldAction(text[19].title, text[19].description, 4)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1,
                            0, EnemyCondition.Type.FIND_TYPE.ordinal(), 1, 6),
                    new BindPets(text[20].title, text[20].description, 4, 3, 3, 1, 6)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1,
                            1, EnemyCondition.Type.FIND_TYPE.ordinal(), 1, 6),
                    new Attack(100)));
            zero.attackMode.addAttack(new ConditionUsedIf(1, 1, 0,EnemyCondition.Type.HP.ordinal(), 2, 75), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[21].title, text[21].description, 50))
                    .addPair(new ConditionAlways(), new DarkScreen(1)));
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionAlways(), new Attack(text[22].title, text[22].description,70)));
            zero.attackMode.addAttack(new ConditionHp(1, 40), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionUsed(), new Daze(text[23].title, text[23].description,0)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[24].title, text[24].description,125))
                    .addPair(new ConditionUsed(), new ColorChange(-1, 7)));
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionAlways(), new MultipleAttack(text[25].title, text[25].description,60,6)));
            zero.attackMode.addAttack(new ConditionHp(2, 40), seq);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 647, 6667787, 6336, 536, 1,
                    new Pair<Integer, Integer>(0, 4),
                    getMonsterTypes(env, 647)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(text[26].title, text[26].description, 100)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                    new ResistanceShieldAction(text[27].title, text[27].description, 999)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[28].title, text[28].description, 100))
                    .addPair(new ConditionAlways(),new BindPets(2,1,1,1)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[28].title, text[28].description, 100))
                    .addPair(new ConditionAlways(),new BindPets(2,1,1,1)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[29].title, text[29].description, 180, 6)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 1371, 5329444, 20375, 3900, 1,
                    new Pair<Integer, Integer>(0, -1),
                    getMonsterTypes(env, 1371)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new Transform(text[30].title,
                                    text[30].description, 6)).addPair(
                            new ConditionAlways(), new Attack(10)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new ColorChange(text[31].title, text[31].description,
                            -1, 7)).addPair(new ConditionAlways(),
                    new Attack(100)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[32].title,
                            text[32].description, 30, 5)));
            zero.attackMode.addAttack(new ConditionHp(1, 50), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new Transform(text[33].title,
                                    text[33].description, 6)).addPair(
                            new ConditionUsed(), new Attack(10)));
            seq.addAttack(new EnemyAttack().addAction(
                    new RandomChange(text[34].title,
                            text[34].description, 6, 6)).addPair(
                    new ConditionAlways(), new Attack(125)));
            zero.attackMode.addAttack(new ConditionHp(2, 50), seq);
        }
        mStageEnemies.put(++stageCounter, data);

    }
}
