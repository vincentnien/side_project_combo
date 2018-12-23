package com.a30corner.combomaster.playground.stagedata;

import android.util.Pair;
import android.util.SparseArray;

import com.a30corner.combomaster.pad.monster.MonsterVO;
import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.StageGenerator;
import com.a30corner.combomaster.playground.action.impl.Angry;
import com.a30corner.combomaster.playground.action.impl.Attack;
import com.a30corner.combomaster.playground.action.impl.BindPets;
import com.a30corner.combomaster.playground.action.impl.ColorChange;
import com.a30corner.combomaster.playground.action.impl.ComboShield;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.LineChange;
import com.a30corner.combomaster.playground.action.impl.LockAwoken;
import com.a30corner.combomaster.playground.action.impl.LockSkill;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.RecoverSelf;
import com.a30corner.combomaster.playground.action.impl.ReduceTime;
import com.a30corner.combomaster.playground.action.impl.ResistanceShieldAction;
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
import com.a30corner.combomaster.playground.enemy.conditions.ConditionTurns;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsedIf;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class Hera297Stage extends IStage {

    @Override
    public void create(IEnvironment env,
                 SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        ArrayList<Enemy> data = new ArrayList<Enemy>();
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
            data = new ArrayList<Enemy>();
            int[] stage5 = { 98, 100, 102, 104, 108, 191, 192, 193, 194, 195,
                    196, 197, 198, 199, 200 };
            int[] weight = { 2, 2, 2, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2 };

            for (int i = 0; i < 4; ++i) {
                int size = RandomUtil.range(4, 6);

                int count = 0;
                data = new ArrayList<Enemy>();
                while (size > 0) {
                    int index = RandomUtil.getInt(stage5.length);
                    if (size >= weight[index]) {
                        size -= weight[index];
                        int id = stage5[index];

                        if (id == 98) {
                            MonsterVO vo = getMonster(env, id);
                            data.add(Enemy.create(env, id, 844092, 19104,
                                    15750, 3, vo.getMonsterProps(),
                                    getMonsterTypes(vo)));
                            zero = data.get(count);
                            zero.attackMode = new EnemyAttackMode();
                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionFindOrb(4), new ColorChange(
                                            text[0].title, text[0].description,
                                            4, 1)));
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionUsed(), new BindPets(
                                            text[1].title, text[1].description,
                                            5, 2, 4, 1, 4)));
                            zero.attackMode.addAttack(
                                    new ConditionFirstStrike(), seq);

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionUsed(), new BindPets(
                                            text[1].title, text[1].description,
                                            5, 2, 4, 1, 4)));
                            zero.attackMode.addAttack(new ConditionAlways(),
                                    seq);

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionAlways(), new Attack(100)));
                            zero.attackMode.addAttack(new ConditionHp(1, 0),
                                    seq);
                        } else if (id == 100) {
                            MonsterVO vo = getMonster(env, id);
                            data.add(Enemy.create(env, id, 844092, 19281,
                                    15750, 3, vo.getMonsterProps(),
                                    getMonsterTypes(vo)));
                            zero = data.get(count);
                            zero.attackMode = new EnemyAttackMode();
                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionFindOrb(5), new ColorChange(
                                            text[2].title, text[2].description,
                                            5, 4)));
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionUsed(), new BindPets(
                                            text[3].title, text[3].description,
                                            5, 2, 4, 1, 5)));
                            zero.attackMode.addAttack(
                                    new ConditionFirstStrike(), seq);

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionUsed(), new BindPets(
                                            text[3].title, text[3].description,
                                            5, 2, 4, 1, 5)));
                            zero.attackMode.addAttack(new ConditionAlways(),
                                    seq);

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionAlways(), new Attack(100)));
                            zero.attackMode.addAttack(new ConditionHp(1, 0),
                                    seq);
                        } else if (id == 102) {
                            MonsterVO vo = getMonster(env, id);
                            data.add(Enemy.create(env, id, 844092, 19546,
                                    15750, 3, vo.getMonsterProps(),
                                    getMonsterTypes(vo)));
                            zero = data.get(count);
                            zero.attackMode = new EnemyAttackMode();
                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionFindOrb(1), new ColorChange(
                                            text[4].title, text[4].description,
                                            1, 5)));
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionUsed(), new BindPets(
                                            text[5].title, text[5].description,
                                            5, 2, 4, 1, 1)));
                            zero.attackMode.addAttack(
                                    new ConditionFirstStrike(), seq);

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionUsed(), new BindPets(
                                            text[5].title, text[5].description,
                                            5, 2, 4, 1, 1)));
                            zero.attackMode.addAttack(new ConditionAlways(),
                                    seq);

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionAlways(), new Attack(100)));
                            zero.attackMode.addAttack(new ConditionHp(1, 0),
                                    seq);
                        } else if (id == 104) {
                            MonsterVO vo = getMonster(env, id);
                            data.add(Enemy.create(env, id, 844092, 19546,
                                    15750, 3, vo.getMonsterProps(),
                                    getMonsterTypes(vo)));
                            zero = data.get(count);
                            zero.attackMode = new EnemyAttackMode();
                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionFindOrb(0), new ColorChange(
                                            text[6].title, text[6].description,
                                            0, 3)));
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionUsed(), new BindPets(
                                            text[7].title, text[7].description,
                                            5, 2, 4, 1, 0)));
                            zero.attackMode.addAttack(
                                    new ConditionFirstStrike(), seq);

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionUsed(), new BindPets(
                                            text[7].title, text[7].description,
                                            5, 2, 4, 1, 0)));
                            zero.attackMode.addAttack(new ConditionAlways(),
                                    seq);

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionAlways(), new Attack(100)));
                            zero.attackMode.addAttack(new ConditionHp(1, 0),
                                    seq);
                        } else if (id == 108) {
                            MonsterVO vo = getMonster(env, id);
                            data.add(Enemy.create(env, id, 844092, 19546,
                                    15750, 3, vo.getMonsterProps(),
                                    getMonsterTypes(vo)));
                            zero = data.get(count);
                            zero.attackMode = new EnemyAttackMode();
                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionFindOrb(3), new ColorChange(
                                            text[8].title, text[8].description,
                                            3, 0)));
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionUsed(), new BindPets(
                                            text[9].title, text[9].description,
                                            5, 2, 4, 1, 3)));
                            zero.attackMode.addAttack(
                                    new ConditionFirstStrike(), seq);

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionUsed(), new BindPets(
                                            text[9].title, text[9].description,
                                            5, 2, 4, 1, 3)));
                            zero.attackMode.addAttack(new ConditionAlways(),
                                    seq);

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionAlways(), new Attack(100)));
                            zero.attackMode.addAttack(new ConditionHp(1, 0),
                                    seq);
                        } else if (id == 191) {
                            MonsterVO vo = getMonster(env, id);
                            data.add(Enemy.create(env, id, 425451, 20726,
                                    49000, 1, vo.getMonsterProps(),
                                    getMonsterTypes(vo)));
                            zero = data.get(count);
                            zero.attackMode = new EnemyAttackMode();
                            zero.attackMode.createEmptyFirstStrike()
                                    .createEmptyMustAction();

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionUsed(), new Angry(
                                            text[10].title,
                                            text[10].description, 3, 130)));
                            zero.attackMode.addAttack(new ConditionHp(2, 75),
                                    seq);

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionAlways(), new Attack(100)));
                            zero.attackMode.addAttack(new ConditionHp(1, 0),
                                    seq);
                        } else if (id == 193) {
                            MonsterVO vo = getMonster(env, id);
                            data.add(Enemy.create(env, id, 428799, 20904,
                                    49000, 1, vo.getMonsterProps(),
                                    getMonsterTypes(vo)));
                            zero = data.get(count);
                            zero.attackMode = new EnemyAttackMode();
                            zero.attackMode.createEmptyFirstStrike()
                                    .createEmptyMustAction();

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionUsed(), new Angry(
                                            text[10].title,
                                            text[10].description, 3, 130)));
                            zero.attackMode.addAttack(new ConditionHp(2, 75),
                                    seq);

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionAlways(), new Attack(100)));
                            zero.attackMode.addAttack(new ConditionHp(1, 0),
                                    seq);
                        } else if (id == 195) {
                            MonsterVO vo = getMonster(env, id);
                            data.add(Enemy.create(env, id, 432150, 21262,
                                    49000, 1, vo.getMonsterProps(),
                                    getMonsterTypes(vo)));
                            zero = data.get(count);
                            zero.attackMode = new EnemyAttackMode();
                            zero.attackMode.createEmptyFirstStrike()
                                    .createEmptyMustAction();

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionUsed(), new Angry(
                                            text[10].title,
                                            text[10].description, 3, 130)));
                            zero.attackMode.addAttack(new ConditionHp(2, 75),
                                    seq);

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionAlways(), new Attack(100)));
                            zero.attackMode.addAttack(new ConditionHp(1, 0),
                                    seq);
                        } else if (id == 197) {
                            MonsterVO vo = getMonster(env, id);
                            data.add(Enemy.create(env, id, 438849, 21440,
                                    49000, 1, vo.getMonsterProps(),
                                    getMonsterTypes(vo)));
                            zero = data.get(count);
                            zero.attackMode = new EnemyAttackMode();
                            zero.attackMode.createEmptyFirstStrike()
                                    .createEmptyMustAction();

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionUsed(), new Angry(
                                            text[10].title,
                                            text[10].description, 3, 130)));
                            zero.attackMode.addAttack(new ConditionHp(2, 75),
                                    seq);

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionAlways(), new Attack(100)));
                            zero.attackMode.addAttack(new ConditionHp(1, 0),
                                    seq);
                        } else if (id == 199) {
                            MonsterVO vo = getMonster(env, id);
                            data.add(Enemy.create(env, id, 442200, 21976,
                                    49000, 1, vo.getMonsterProps(),
                                    getMonsterTypes(vo)));
                            zero = data.get(count);
                            zero.attackMode = new EnemyAttackMode();
                            zero.attackMode.createEmptyFirstStrike()
                                    .createEmptyMustAction();

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionUsed(), new Angry(
                                            text[10].title,
                                            text[10].description, 3, 130)));
                            zero.attackMode.addAttack(new ConditionHp(2, 75),
                                    seq);

                            seq = new SequenceAttack();
                            seq.addAttack(new EnemyAttack().addPair(
                                    new ConditionAlways(), new Attack(100)));
                            zero.attackMode.addAttack(new ConditionHp(1, 0),
                                    seq);
                        } else if (id == 192) {
                            MonsterVO vo = getMonster(env, id);
                            data.add(Enemy.create(env, id, 741216, 23134,
                                    55000, 1, vo.getMonsterProps(),
                                    getMonsterTypes(vo)));
                            zero = data.get(count);
                            zero.attackMode = new EnemyAttackMode();
                            zero.attackMode.createEmptyFirstStrike()
                                    .createEmptyMustAction();

                            RandomAttack random = new RandomAttack();
                            random.addAttack(new EnemyAttack().addPair(
                                    new ConditionAlways(), new Attack(100)));
                            random.addAttack(new EnemyAttack()
                                    .addPair(new ConditionAlways(),
                                            new BindPets(text[11].title,
                                                    text[11].description, 5, 2,
                                                    4, 1, 4)));
                            zero.attackMode.addAttack(new ConditionHp(1, 0),
                                    random);
                        } else if (id == 194) {
                            MonsterVO vo = getMonster(env, id);
                            data.add(Enemy.create(env, id, 765051, 23262,
                                    55000, 1, vo.getMonsterProps(),
                                    getMonsterTypes(vo)));
                            zero = data.get(count);
                            zero.attackMode = new EnemyAttackMode();
                            zero.attackMode.createEmptyFirstStrike()
                                    .createEmptyMustAction();

                            RandomAttack random = new RandomAttack();
                            random.addAttack(new EnemyAttack().addPair(
                                    new ConditionAlways(), new Attack(100)));
                            random.addAttack(new EnemyAttack()
                                    .addPair(new ConditionAlways(),
                                            new BindPets(text[12].title,
                                                    text[12].description, 5, 2,
                                                    4, 1, 5)));
                            zero.attackMode.addAttack(new ConditionHp(1, 0),
                                    random);
                        } else if (id == 196) {
                            MonsterVO vo = getMonster(env, id);
                            data.add(Enemy.create(env, id, 788883, 24024,
                                    55000, 1, vo.getMonsterProps(),
                                    getMonsterTypes(vo)));
                            zero = data.get(count);
                            zero.attackMode = new EnemyAttackMode();
                            zero.attackMode.createEmptyFirstStrike()
                                    .createEmptyMustAction();

                            RandomAttack random = new RandomAttack();
                            random.addAttack(new EnemyAttack().addPair(
                                    new ConditionAlways(), new Attack(100)));
                            random.addAttack(new EnemyAttack()
                                    .addPair(new ConditionAlways(),
                                            new BindPets(text[13].title,
                                                    text[13].description, 5, 2,
                                                    4, 1, 1)));
                            zero.attackMode.addAttack(new ConditionHp(1, 0),
                                    random);
                        } else if (id == 198) {
                            MonsterVO vo = getMonster(env, id);
                            data.add(Enemy.create(env, id, 812718, 23515,
                                    55000, 1, vo.getMonsterProps(),
                                    getMonsterTypes(vo)));
                            zero = data.get(count);
                            zero.attackMode = new EnemyAttackMode();
                            zero.attackMode.createEmptyFirstStrike()
                                    .createEmptyMustAction();

                            RandomAttack random = new RandomAttack();
                            random.addAttack(new EnemyAttack().addPair(
                                    new ConditionAlways(), new Attack(100)));
                            random.addAttack(new EnemyAttack()
                                    .addPair(new ConditionAlways(),
                                            new BindPets(text[14].title,
                                                    text[14].description, 5, 2,
                                                    4, 1, 0)));
                            zero.attackMode.addAttack(new ConditionHp(1, 0),
                                    random);
                        } else if (id == 200) {
                            MonsterVO vo = getMonster(env, id);
                            data.add(Enemy.create(env, id, 836550, 23642,
                                    55000, 1, vo.getMonsterProps(),
                                    getMonsterTypes(vo)));
                            zero = data.get(count);
                            zero.attackMode = new EnemyAttackMode();
                            zero.attackMode.createEmptyFirstStrike()
                                    .createEmptyMustAction();

                            RandomAttack random = new RandomAttack();
                            random.addAttack(new EnemyAttack().addPair(
                                    new ConditionAlways(), new Attack(100)));
                            random.addAttack(new EnemyAttack()
                                    .addPair(new ConditionAlways(),
                                            new BindPets(text[15].title,
                                                    text[15].description, 5, 2,
                                                    4, 1, 3)));
                            zero.attackMode.addAttack(new ConditionHp(1, 0),
                                    random);
                        }
                        ++count;

                    }

                }
                mStageEnemies.put(i + 1, data);
            }
        }
        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 1928, 4005921, 7646, 0, 1,
                    new Pair<Integer, Integer>(3, 4),
                    getMonsterTypes(env, 1928)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new ReduceTime(text[16].title, text[16].description, 5,
                            -2000)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1, 1,
                            EnemyCondition.Type.HAS_DEBUFF.ordinal(), 0, -1),
                    new ReduceTime(text[17].title, text[17].description, 3,
                            -3000)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1,
                    0, EnemyCondition.Type.HP.ordinal(), 2, 30), new BindPets(
                    text[20].title, text[20].description, 2, 2, 3, 4)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new BindPets(text[21].title, text[21].description, 3, 1, 2,
                            5)).addPair(new ConditionAlways(), new Attack(150)));
            zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new BindPets(text[18].title, text[18].description, 3, 1, 2,
                            2)).addPair(new ConditionAlways(), new Attack(75)));
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new RandomChange(text[19].title,
                                    text[19].description, 7, 3)).addPair(
                            new ConditionAlways(), new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 30), seq);
        }
        mStageEnemies.put(5, data);
        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 1533, 3163959, 5622, 1850, 1,
                    new Pair<Integer, Integer>(1, -1),
                    getMonsterTypes(env, 1533)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionAlways(), new ComboShield(
                            text[22].title, text[22].description, 5, 5)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();
            seq = new SequenceAttack();
            for (int i = 0; i < 5; ++i) {
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[23].title, text[23].description, 1,
                                1)).addPair(new ConditionUsed(),
                        new Attack(140)));
            }
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                    new Angry(text[24].title, text[24].description, 999, 300)));
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new ColorChange(text[25].title,
                                    text[25].description, 0, 1)).addPair(
                            new ConditionIf(1, 1, 0,
                                    EnemyCondition.Type.FIND_ORB.ordinal(), 0),
                            new Attack(200)));
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new ColorChange(text[26].title,
                                    text[26].description, -1, 1)).addPair(
                            new ConditionIf(1, 1, 1,
                                    EnemyCondition.Type.FIND_ORB.ordinal(), 0),
                            new Attack(200)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
        }
        mStageEnemies.put(6, data);
        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 1535, 4591041, 8122, 0, 1,
                    new Pair<Integer, Integer>(5, -1),
                    getMonsterTypes(env, 1535)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new LockSkill(text[27].title, text[27].description, 5)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Transform(text[28].title, text[28].description, 5, 2))
                    .addPair(new ConditionTurns(6), new Attack(300)));
            // TODO: ADD damage if hp < 7615
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 50),
                    new RecoverSelf(text[29].title, text[29].description, 100)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            RandomAttack rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new BindPets(text[30].title, text[30].description, 0, 1, 2,
                            1)).addPair(new ConditionAlways(), new Attack(75)));
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new BindPets(text[31].title, text[31].description, 2, 1, 2,
                            3)).addPair(new ConditionAlways(), new Attack(50)));
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new MultipleAttack(text[32].title,
                            text[32].description, 40, 3, 4)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), rndAttack);
        }
        mStageEnemies.put(7, data);

        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 1534, 1570209, 9789, 1388890, 1,
                    new Pair<Integer, Integer>(4, -1),
                    getMonsterTypes(env, 1534)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new ResistanceShieldAction(text[33].title,
                            text[33].description, 4)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new LineChange(text[34].title, text[34].description, 0, 6,
                            2, 2)).addPair(new ConditionAlways(),
                    new Attack(25)));
            seq.addAttack(new EnemyAttack().addAction(
                    new LineChange(text[35].title, text[35].description, 3, 6,
                            4, 2)).addPair(new ConditionAlways(),
                    new Attack(50)));
            seq.addAttack(new EnemyAttack().addAction(
                    new LineChange(text[36].title, text[36].description, 0, 2,
                            2, 6)).addPair(new ConditionAlways(),
                    new Attack(75)));
            seq.addAttack(new EnemyAttack().addAction(
                    new LineChange(text[37].title, text[37].description, 3, 2,
                            4, 6)).addPair(new ConditionAlways(),
                    new Attack(100)));
            seq.addAttack(new EnemyAttack().addAction(
                    new LineChange(text[38].title, text[38].description, 0, 6,
                            2, 6)).addPair(new ConditionAlways(),
                    new Attack(200)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
        }
        mStageEnemies.put(8, data);

        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 1748, 5475556, 54756, 0, 5,
                    new Pair<Integer, Integer>(0, 5),
                    getMonsterTypes(env, 1748)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 5, 50);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Gravity(text[39].title, text[39].description, 99)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new LineChange(text[40].title, text[40].description, 1, 0))
                    .addPair(new ConditionAlways(), new Attack(1000)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);
        }
        mStageEnemies.put(9, data);

        {
            data = new ArrayList<Enemy>();
            data.add(Enemy
                    .create(env, 189, 35286426, 45246, 7750, 1,
                            new Pair<Integer, Integer>(0, -1),
                            getMonsterTypes(env, 189)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new ComboShield(text[41].title,
                                    text[41].description, 99, 6)).addPair(
                            new ConditionAlways(),
                            new Attack(text[42].title, text[42].description,
                                    100)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                    new LockAwoken(text[45].title, text[45].description, 1)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                    new Attack(text[46].title, text[46].description, 600)));
            zero.attackMode.addAttack(new ConditionHp(2, 25), seq);

            RandomAttack random = new RandomAttack();
            random.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new ColorChange(text[43].title, text[43].description, -1,
                            7, -1, 7)));
            random.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 60),
                    new MultipleAttack(text[44].title, text[44].description,
                            40, 3)));
            random.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 25), random);
        }
        mStageEnemies.put(10, data);
    }
}
