package com.a30corner.combomaster.playground.stagedata;

import android.util.Pair;
import android.util.SparseArray;

import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.StageGenerator;
import com.a30corner.combomaster.playground.action.impl.AbsorbShieldAction;
import com.a30corner.combomaster.playground.action.impl.Attack;
import com.a30corner.combomaster.playground.action.impl.BindPets;
import com.a30corner.combomaster.playground.action.impl.ChangeAttribute;
import com.a30corner.combomaster.playground.action.impl.ColorChange;
import com.a30corner.combomaster.playground.action.impl.ComboShield;
import com.a30corner.combomaster.playground.action.impl.DarkScreen;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.LineChange;
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
import com.a30corner.combomaster.playground.enemy.attack.RandomAttack;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAlways;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHp;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionIf;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class Challenge28_Lv10 extends IStage {

    @Override
    public void create(IEnvironment env,
                       SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        mStageEnemies.clear();

        ArrayList<Enemy> data = new ArrayList<Enemy>();
        Enemy zero = null;
        SequenceAttack seq = null;

        List<StageGenerator.SkillText> list = loadSkillText(env, mStage); //
        StageGenerator.SkillText[] text = new StageGenerator.SkillText[list.size()];
        int len = text.length;
        for (int i = 0; i < len; ++i) {
            text[i] = getSkillText(list, i);
        }

        // level1
        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 2298, 6237860, 17426, 812, 1,
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
        }
        mStageEnemies.put(1, data);

        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 1954, 7190260, 22775, 0, 1,
                    new Pair<Integer, Integer>(1, -1),
                    getMonsterTypes(env, 1954)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 1, 50);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new SkillDown(text[9].title, text[9].description,
                                    0, 2, 2))
                    .addAction(
                            new ComboShield(text[10].title,
                                    text[10].description, 99, 4))
                    .addPair(
                            new ConditionAlways(),
                            new Gravity(text[11].title, text[11].description,
                                    75)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new Gravity(text[11].title, text[11].description,
                                    75))
                    .addAction(
                            new Transform(text[16].title, text[16].description,
                                    1))
                    .addPair(new ConditionHp(2, 30), new Attack(300)));
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new ColorChange(text[17].title,
                                    text[17].description, 6, 2)).addPair(
                            new ConditionIf(1, 1, 0,
                                    EnemyCondition.Type.FIND_ORB.ordinal(), 6),
                            new Attack(200)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            RandomAttack rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new LineChange(text[12].title, text[12].description, 4, 1,
                            7, 1, 9, 1)).addPair(new ConditionAlways(),
                    new Attack(100)));
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new MultipleAttack(text[13].title,
                            text[13].description, 40, 3)));
            rndAttack.addAttack(new EnemyAttack()
                    .addAction(
                            new RandomChange(text[14].title,
                                    text[14].description, 6, 7)).addPair(
                            new ConditionAlways(), new Attack(50)));
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new DarkScreen(text[15].title, text[15].description, 0))
                    .addPair(new ConditionAlways(), new Attack(75)));
            zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
        }
        mStageEnemies.put(2, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1747, 2078 };
            int choose = RandomUtil.chooseOne(ids);

            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 30011111, 10153, 0, 1,
                        new Pair<Integer, Integer>(3, 1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ResistanceShieldAction(text[18].title,
                                text[18].description, 999)).addPair(
                        new ConditionAlways(),
                        new ShieldAction(text[19].title, text[19].description,
                                1, -1, 50)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(1, 65),
                        new MultipleAttack(text[20].title,
                                text[20].description, 200, 6)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(text[27].title, text[27].description, 1500)));
                zero.attackMode.addAttack(new ConditionHp(2, 5), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new MultipleAttack(text[21].title,
                                text[21].description, 90, 2)).addPair(
                        new ConditionAlways(),
                        new BindPets(text[22].title, text[22].description, 3,
                                2, 2, 1)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new MultipleAttack(text[21].title,
                                text[21].description, 90, 2))
                        .addPair(
                                new ConditionAlways(),
                                new DarkScreen(text[23].title,
                                        text[23].description, 0)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new MultipleAttack(text[21].title,
                                text[21].description, 90, 2)).addPair(
                        new ConditionAlways(),
                        new Gravity(text[25].title, text[25].description, 99)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new MultipleAttack(text[21].title,
                                text[21].description, 90, 2)).addPair(
                        new ConditionAlways(),
                        new RandomChange(text[24].title, text[24].description,
                                6, 3)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new LockSkill(text[26].title,
                                        text[26].description, 10)).addPair(
                                new ConditionAlways(), new Attack(150)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 5), rndAttack);
            } else {
                data.add(Enemy.create(env, choose, 30011111, 10375, 0, 1,
                        new Pair<Integer, Integer>(0, 1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ResistanceShieldAction(text[18].title,
                                text[18].description, 999)).addPair(
                        new ConditionAlways(),
                        new ShieldAction(text[19].title, text[19].description,
                                1, -1, 50)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(1, 65),
                        new MultipleAttack(text[28].title,
                                text[28].description, 200, 6)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(text[32].title, text[32].description, 1500)));
                zero.attackMode.addAttack(new ConditionHp(2, 5), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack
                        .addAttack(new EnemyAttack()
                                .addAction(
                                        new DarkScreen(text[29].title,
                                                text[29].description, 0))
                                .addAction(new Attack(90))
                                .addAction(
                                        new Transform(text[30].title,
                                                text[30].description, 1, 4, 5,
                                                3, 0, 2))
                                .addPair(new ConditionAlways(), new Attack(90)));
                rndAttack
                        .addAttack(new EnemyAttack()
                                .addAction(
                                        new Transform(text[30].title,
                                                text[30].description, 1, 4, 5,
                                                3, 0, 2))
                                .addAction(new Attack(90))
                                .addAction(
                                        new SkillDown(text[31].title,
                                                text[31].description, 0, 0, 1))
                                .addPair(new ConditionAlways(), new Attack(90)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new SkillDown(text[31].title,
                                        text[31].description, 0, 0, 1))
                        .addAction(new Attack(90))
                        .addAction(
                                new DarkScreen(text[29].title,
                                        text[29].description, 0))
                        .addPair(new ConditionAlways(), new Attack(90)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new LockSkill(text[26].title,
                                        text[26].description, 10)).addPair(
                                new ConditionAlways(), new Attack(150)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 5), rndAttack);
            }

        }
        mStageEnemies.put(3, data);

    }
}
