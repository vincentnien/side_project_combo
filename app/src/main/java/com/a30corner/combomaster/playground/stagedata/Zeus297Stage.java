package com.a30corner.combomaster.playground.stagedata;

import android.util.SparseArray;

import com.a30corner.combomaster.pad.monster.MonsterVO;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.StageGenerator;
import com.a30corner.combomaster.playground.action.impl.Attack;
import com.a30corner.combomaster.playground.action.impl.BindPets;
import com.a30corner.combomaster.playground.action.impl.ColorChange;
import com.a30corner.combomaster.playground.action.impl.DropRateAttack;
import com.a30corner.combomaster.playground.action.impl.LockOrb;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.RecoverDead;
import com.a30corner.combomaster.playground.action.impl.ResistanceShieldAction;
import com.a30corner.combomaster.playground.action.impl.ShieldAction;
import com.a30corner.combomaster.playground.action.impl.SkillDown;
import com.a30corner.combomaster.playground.enemy.EnemyAttack;
import com.a30corner.combomaster.playground.enemy.EnemyAttackMode;
import com.a30corner.combomaster.playground.enemy.attack.RandomAttack;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAlways;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHp;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionNTurns;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionSomeoneDied;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class Zeus297Stage extends IStage {

    @Override
    public void create(IEnvironment env,
                       SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        mStageEnemies.clear();
        ArrayList<Enemy> data = new ArrayList<Enemy>();
        Enemy zero = null;
        SequenceAttack seq = null;

        List<StageGenerator.SkillText> list = loadSkillText(env, mStage);
        StageGenerator.SkillText[] text = new StageGenerator.SkillText[list.size()];
        int len = text.length;
        for (int i = 0; i < len; ++i) {
            text[i] = getSkillText(list, i);
        }

        {
            data = new ArrayList<Enemy>();
            MonsterVO vo = getMonster(env, 262);
            data.add(Enemy.create(env, 262, 651666, 24083, 15000, 2,
                    vo.getMonsterProps(), getMonsterTypes(vo)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

            RandomAttack rand = new RandomAttack();
            rand.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            rand.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[0].title, text[0].description, 70,
                            2)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), rand);

            vo = getMonster(env, 264);
            data.add(Enemy.create(env, 264, 623334, 23422, 15000, 2,
                    vo.getMonsterProps(), getMonsterTypes(vo)));
            zero = data.get(1);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new ShieldAction(text[1].title, text[1].description, 1, -1,
                            99)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[0].title, text[0].description, 70,
                            2)));
            zero.attackMode.addAttack(new ConditionHp(2, 75), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), seq);

            vo = getMonster(env, 266);
            data.add(Enemy.create(env, 266, 595002, 22667, 15000, 2,
                    vo.getMonsterProps(), getMonsterTypes(vo)));
            zero = data.get(2);
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

            rand = new RandomAttack();
            rand.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            rand.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[0].title, text[0].description, 70,
                            2)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), rand);
        }
        mStageEnemies.put(1, data);

        {
            data = new ArrayList<Enemy>();
            MonsterVO vo = getMonster(env, 174);
            data.add(Enemy.create(env, 174, 114, 42218, 1200000, 6,
                    vo.getMonsterProps(), getMonsterTypes(vo)));
            zero = data.get(0);
            zero.setHasResurrection();
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

            RandomAttack rand = new RandomAttack();
            rand.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            rand.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new BindPets(text[2].title, text[2].description, 4, 2, 4,
                            1, 0)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), rand);

            vo = getMonster(env, 234);
            data.add(Enemy.create(env, 234, 300, 16444, 8000000, 1,
                    vo.getMonsterProps(), getMonsterTypes(vo)));
            zero = data.get(1);
            zero.attackMode = new EnemyAttackMode();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new SkillDown(text[3].title, text[3].description, 0, 1, 3)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionSomeoneDied(),
                    new RecoverDead(text[4].title, text[4].description, 100)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(text[5].title, text[5].description, 200)));
            zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), seq);

            vo = getMonster(env, 175);
            data.add(Enemy.create(env, 175, 114, 42218, 1200000, 6,
                    vo.getMonsterProps(), getMonsterTypes(vo)));
            zero = data.get(2);
            zero.setHasResurrection();
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

            rand = new RandomAttack();
            rand.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            rand.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new BindPets(text[6].title, text[6].description, 4, 2, 4,
                            1, 3)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), rand);
        }
        mStageEnemies.put(2, data);

        {
            data = new ArrayList<Enemy>();
            MonsterVO vo = getMonster(env, 192);
            data.add(Enemy.create(env, 192, 1482432, 28918, 110000, 1,
                    vo.getMonsterProps(), getMonsterTypes(vo)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new DropRateAttack(text[7].title, text[7].description, 10,
                            7, 20)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            RandomAttack rand = new RandomAttack();
            rand.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            rand.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new BindPets(text[8].title, text[8].description, 5, 2, 4,
                            1, 4)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), rand);

            vo = getMonster(env, 194);
            data.add(Enemy.create(env, 194, 1530102, 29077, 110000, 1,
                    vo.getMonsterProps(), getMonsterTypes(vo)));
            zero = data.get(1);
            zero.attackMode = new EnemyAttackMode();

            zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

            rand = new RandomAttack();
            rand.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            rand.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new BindPets(text[9].title, text[9].description, 5, 2, 4,
                            1, 5)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), rand);

            vo = getMonster(env, 196);
            data.add(Enemy.create(env, 196, 1577766, 30030, 110000, 1,
                    vo.getMonsterProps(), getMonsterTypes(vo)));
            zero = data.get(2);
            zero.attackMode = new EnemyAttackMode();

            zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

            rand = new RandomAttack();
            rand.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            rand.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new BindPets(text[10].title, text[10].description, 5, 2, 4,
                            1, 1)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), rand);
        }
        mStageEnemies.put(3, data);

        {
            data = new ArrayList<Enemy>();

            int[] monster = { 263, 265, 267 };
            for (int i = 0; i < 2; ++i) {
                int randId = RandomUtil.chooseOne(monster);

                MonsterVO vo;
                if (randId == 263) {
                    vo = getMonster(env, randId);
                    data.add(Enemy.create(env, randId, 10479468, 30240, 28000,
                            2, vo.getMonsterProps(), getMonsterTypes(vo)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addPair(new ConditionAlways(), new MultipleAttack(
                                    text[11].title, text[11].description, 9, 2)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                    zero.attackMode.createEmptyMustAction();

                    RandomAttack rand = new RandomAttack();
                    rand.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new Attack(100)));
                    rand.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(),
                            new MultipleAttack(text[12].title,
                                    text[12].description, 70, 2)));
                    rand.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new BindPets(text[13].title,
                                    text[13].description, 3, 2, 4, 2)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), rand);
                } else if (randId == 265) {

                    vo = getMonster(env, randId);
                    data.add(Enemy.create(env, randId, 10386132, 29804, 28000,
                            2, vo.getMonsterProps(), getMonsterTypes(vo)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addPair(new ConditionAlways(), new MultipleAttack(
                                    text[14].title, text[14].description, 9, 2)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                    zero.attackMode.createEmptyMustAction();

                    RandomAttack rand = new RandomAttack();
                    rand.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new Attack(100)));
                    rand.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(),
                            new MultipleAttack(text[12].title,
                                    text[12].description, 70, 2)));
                    rand.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new BindPets(text[13].title,
                                    text[13].description, 3, 2, 4, 2)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), rand);
                } else {

                    vo = getMonster(env, randId);
                    data.add(Enemy.create(env, randId, 10292802, 29307, 28000,
                            2, vo.getMonsterProps(), getMonsterTypes(vo)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addPair(new ConditionAlways(), new MultipleAttack(
                                    text[15].title, text[15].description, 9, 2)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                    zero.attackMode.createEmptyMustAction();

                    RandomAttack rand = new RandomAttack();
                    rand.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new Attack(100)));
                    rand.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(),
                            new MultipleAttack(text[12].title,
                                    text[12].description, 70, 2)));
                    rand.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new BindPets(text[13].title,
                                    text[13].description, 3, 2, 4, 2)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), rand);
                }
            }
        }
        mStageEnemies.put(4, data);
        {
            data = new ArrayList<Enemy>();
            MonsterVO vo = getMonster(env, 187);
            data.add(Enemy.create(env, 187, 62313636, 49892, 138000, 1,
                    vo.getMonsterProps(), getMonsterTypes(vo)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new LockOrb(text[16].title, text[16].description, 2, 20))
                    .addPair(
                            new ConditionAlways(),
                            new ResistanceShieldAction(text[17].title,
                                    text[17].description, 999)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 20),
                    new MultipleAttack(text[18].title, text[18].description,
                            25, 10)));
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new RandomChange(text[19].title,
                                    text[19].description, 8, 6))
                    .addAction(new Attack(50))
                    .addPair(
                            new ConditionNTurns(3, 0),
                            new LockOrb(text[20].title, text[20].description,
                                    0, 8)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                    new BindPets(text[21].title, text[21].description, 4, 2, 4,
                            1, 6)));
            zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(text[22].title, text[22].description, 150)));
            zero.attackMode.addAttack(new ConditionHp(2, 75), seq);

            RandomAttack rand = new RandomAttack();
            rand.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            rand.addAttack(new EnemyAttack()
                    .addAction(
                            new ColorChange(text[23].title,
                                    text[23].description, 2, 7)).addPair(
                            new ConditionAlways(), new Attack(70)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), rand);
        }
        mStageEnemies.put(5, data);
    }
}
