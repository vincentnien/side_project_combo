package com.a30corner.combomaster.playground.stagedata;

import android.util.Pair;
import android.util.SparseArray;

import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.StageGenerator;
import com.a30corner.combomaster.playground.action.impl.AbsorbShieldAction;
import com.a30corner.combomaster.playground.action.impl.Attack;
import com.a30corner.combomaster.playground.action.impl.BindPets;
import com.a30corner.combomaster.playground.action.impl.ColorChange;
import com.a30corner.combomaster.playground.action.impl.DamageAbsorbShield;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.LineChange;
import com.a30corner.combomaster.playground.action.impl.LockOrb;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RecoverSelf;
import com.a30corner.combomaster.playground.action.impl.ResistanceShieldAction;
import com.a30corner.combomaster.playground.action.impl.SkillDown;
import com.a30corner.combomaster.playground.action.impl.Transform;
import com.a30corner.combomaster.playground.enemy.EnemyAttack;
import com.a30corner.combomaster.playground.enemy.EnemyAttackMode;
import com.a30corner.combomaster.playground.enemy.attack.RandomAttack;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAlways;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHp;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionPercentage;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionTurns;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.entity.Enemy;

import java.util.ArrayList;
import java.util.List;

public class SnowRiverStage extends IStage {

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
            data.add(Enemy.create(env, 148, 1117225, 22505, 900, 3,
                    new Pair<Integer, Integer>(4, -1),
                    getMonsterTypes(env, 148)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(text[0].title, text[0].description, 75)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 25),
                    new Attack(text[1].title, text[1].description, 200)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 25), seq);
        }
        mStageEnemies.put(1, data);

        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 247, 19, 30370, 544444, 3,
                    new Pair<Integer, Integer>(4, -1),
                    getMonsterTypes(env, 247)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createSimpleAttackAction("", "", 100);

            data.add(Enemy.create(env, 251, 24, 60123, 544444, 5,
                    new Pair<Integer, Integer>(0, -1),
                    getMonsterTypes(env, 251)));
            zero = data.get(1);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(text[2].title, text[2].description, 10)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            data.add(Enemy.create(env, 247, 19, 30370, 544444, 3,
                    new Pair<Integer, Integer>(4, -1),
                    getMonsterTypes(env, 247)));
            zero = data.get(2);
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createSimpleAttackAction("", "", 100);
        }
        mStageEnemies.put(2, data);

        {
            data = new ArrayList<Enemy>();
            data.add(Enemy
                    .create(env, 758, 4099028, 10250, 360, 1,
                            new Pair<Integer, Integer>(4, 1),
                            getMonsterTypes(env, 758)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new AbsorbShieldAction(text[3].title, text[3].description,
                            5, 5)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new BindPets(text[4].title, text[4].description, 3,
                                    2, 2, 1)).addPair(new ConditionTurns(5),
                            new Attack(320)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[5].title, text[5].description, 80))
                    .addPair(new ConditionUsed(), new LockOrb(0, 1, 4)));
            zero.attackMode.addAttack(new ConditionHp(2, 60), seq);

            RandomAttack rndAtk = new RandomAttack();
            rndAtk.addAttack(new EnemyAttack().addAction(
                    new Attack(text[6].title, text[6].description, 120))
                    .addPair(new ConditionAlways(), new ColorChange(-1, 4)));
            rndAtk.addAttack(new EnemyAttack().addPair(new ConditionPercentage(
                    40), new MultipleAttack(text[7].title, text[7].description,
                    70, 2)));
            rndAtk.addAttack(new EnemyAttack().addPair(new ConditionPercentage(
                    15), new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), rndAtk);
        }
        mStageEnemies.put(3, data);

        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 234, 20, 5778, 200000, 1,
                    new Pair<Integer, Integer>(3, -1),
                    getMonsterTypes(env, 234)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createEmptyFirstStrike();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 50),
                    new Attack(text[8].title, text[8].description, 200)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), seq);

            data.add(Enemy.create(env, 1229, 24, 60123, 544444, 5,
                    new Pair<Integer, Integer>(4, -1),
                    getMonsterTypes(env, 1229)));
            zero = data.get(1);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionAlways(), new BindPets(text[9].title,
                            text[9].description, 3, 3, 3, 1)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[10].title, text[10].description, 90))
                    .addPair(new ConditionAlways(), new ColorChange(-1, 0)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), seq);

            data.add(Enemy.create(env, 234, 20, 5778, 200000, 1,
                    new Pair<Integer, Integer>(3, -1),
                    getMonsterTypes(env, 234)));
            zero = data.get(2);
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createEmptyFirstStrike();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 50),
                    new Attack(text[8].title, text[8].description, 200)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
        }
        mStageEnemies.put(4, data);

        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 1602, 5001806, 9319, 570, 1,
                    new Pair<Integer, Integer>(4, 5),
                    getMonsterTypes(env, 1602)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new DamageAbsorbShield(text[11].title,
                            text[11].description, 99, 400000)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[12].title, text[12].description, 300))
                    .addPair(new ConditionHp(1, 100), new Transform(4, 6)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[12].title, text[12].description, 300))
                    .addPair(new ConditionAlways(), new Transform(4, 6)));
            zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

            RandomAttack rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new SkillDown(text[13].title,
                            text[13].description, 0, 0, 2)));
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[14].title, text[14].description, 120))
                    .addPair(new ConditionAlways(), new LockOrb(0, 5)));
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[15].title, text[15].description, 80))
                    .addPair(new ConditionAlways(),
                            new LineChange(3, 4, 7, 5, 8, 6)));
            zero.attackMode.addAttack(new ConditionHp(1, 20), rndAttack);
        }
        mStageEnemies.put(5, data);

        {
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 2277, 7500556, 22208, 870, 1,
                    new Pair<Integer, Integer>(4, 0),
                    getMonsterTypes(env, 2277)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 30);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new ResistanceShieldAction(text[16].title,
                            text[16].description, 999)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[17].title, text[17].description, 80))
                    .addPair(new ConditionHp(2, 1), new RecoverSelf(50)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new Attack(text[21].title, text[21].description,
                                    100))
                    .addAction(new Transform(4, 2, 7))
                    .addPair(
                            new ConditionUsed(),
                            new LockOrb(text[22].title, text[22].description,
                                    0, 7)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[23].title, text[23].description,
                            100, 5)));
            zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

            RandomAttack rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new Gravity(text[18].title,
                            text[18].description, 99)));
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionPercentage(80), new Attack(text[19].title,
                            text[19].description, 120)));
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionPercentage(40), new MultipleAttack(
                            text[20].title, text[20].description, 70, 2)));
            zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
        }
        mStageEnemies.put(6, data);
    }
}
