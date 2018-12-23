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
import com.a30corner.combomaster.playground.action.impl.DamageAbsorbShield;
import com.a30corner.combomaster.playground.action.impl.DamageVoidShield;
import com.a30corner.combomaster.playground.action.impl.DarkScreen;
import com.a30corner.combomaster.playground.action.impl.DropRateAttack;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.IntoVoid;
import com.a30corner.combomaster.playground.action.impl.LineChange;
import com.a30corner.combomaster.playground.action.impl.LockAwoken;
import com.a30corner.combomaster.playground.action.impl.LockOrb;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.RandomLineChange;
import com.a30corner.combomaster.playground.action.impl.RecoverSelf;
import com.a30corner.combomaster.playground.action.impl.ReduceTime;
import com.a30corner.combomaster.playground.action.impl.ResistanceShieldAction;
import com.a30corner.combomaster.playground.action.impl.ShieldAction;
import com.a30corner.combomaster.playground.action.impl.SkillDown;
import com.a30corner.combomaster.playground.action.impl.SuperDark;
import com.a30corner.combomaster.playground.enemy.EnemyAttack;
import com.a30corner.combomaster.playground.enemy.EnemyAttackMode;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.enemy.attack.RandomAttack;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAlways;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHp;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionIf;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionPercentage;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionTurns;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionTurnsOffset;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsedIf;
import com.a30corner.combomaster.playground.entity.Enemy;

import java.util.ArrayList;
import java.util.List;

public class MachineAthenaStage extends IStage {
    @Override
    public void create(IEnvironment env,
                       SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        ArrayList<Enemy> data;
        Enemy zero = null;
        SequenceAttack seq = null;
        mStageEnemies.clear();

        SparseArray<List<StageGenerator.SkillText>> list = loadSkillTextNew(env, mStage, new int[]{82,1458,2187,2195,2196,2199,2298,2529,2531,2652,2653,2655,2741,2987,3074});
        int stageCounter = 0;

        StageGenerator.SkillText[] text;
        data = new ArrayList<Enemy>();
        {
            text = loadSubText(list, 2196);
            data.add(Enemy.create(env, 2196, 38, 3965, 933333, 1,
                    new Pair<Integer, Integer>(4,-1),
                    getMonsterTypes(env, 2196)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SkillDown(text[0].title,text[0].description,0,1,1)));
            zero.attackMode.addAttack(new ConditionHp(0, 100), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[1].title,text[1].description, 999, 200)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(280)).addPair(new ConditionAlways(), new RandomChange(text[2].title,text[2].description,4,3)));
            zero.attackMode.addAttack(new ConditionHp(2, 100), seq);

            text = loadSubText(list, 82);
            data.add(Enemy.create(env, 82, 2070000, 14800, 600000, 4,
                    new Pair<Integer, Integer>(5,-1),
                    getMonsterTypes(env, 82)));
            zero = data.get(1);
            zero.attackMode = new EnemyAttackMode();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SuperDark(text[0].title,text[0].description,3,1,3,1,5,2,3,2,5,3,3,3,4,3,5)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(400)).addPair(new ConditionAlways(), new RandomChange(text[1].title,text[1].description,5,1)));
            zero.attackMode.addAttack(new ConditionHp(2, 100), seq);

            text = loadSubText(list, 2655);
            data.add(Enemy.create(env, 2655, 414658, 17078, 500, 1,
                    new Pair<Integer, Integer>(3,-1),
                    getMonsterTypes(env, 2655)));
            zero = data.get(2);
            zero.attackMode = new EnemyAttackMode();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(110)).addPair(new ConditionAlways(), new RandomChange(text[0].title,text[0].description,3,1)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(70)).addPair(new ConditionAlways(), new LockOrb(text[1].title,text[1].description,0,3)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(2, 100), seq);

        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            text = loadSubText(list, 2531);
            data.add(Enemy.create(env, 2531, 5672058, 17816, 850, 1,
                    new Pair<Integer, Integer>(1,5),
                    getMonsterTypes(env, 2531)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new MultipleAttack(
                            text[0].title, text[0].description, 120, 2)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.ALIVE_ONLY
                            .ordinal(), 1), new Angry(text[1].title, text[1].description,999,200)));

            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                            .ordinal(), 2, 80), new SkillDown(
                            text[2].title, text[2].description, 0,
                            1, 1)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[3].title, text[3].description,
                            150)).addPair(new ConditionAlways(),
                    new ColorChange(6,5)));
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[4].title, text[4].description,
                            130)).addPair(new ConditionAlways(),
                    new LineChange(0,1,2,5)));
            zero.attackMode.addAttack(new ConditionHp(1, 10), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new MultipleAttack(
                            text[5].title, text[5].description,
                            500, 2)));
            zero.attackMode.addAttack(new ConditionHp(2, 10), seq);

            text = loadSubText(list, 2529);
            data.add(Enemy.create(env, 2529, 4873214, 19340, 850, 1,
                    new Pair<Integer, Integer>(5, 4),
                    getMonsterTypes(env, 2529)));
            zero = data.get(1);
            zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 1);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new DropRateAttack(text[0].title, text[0].description,
                            10, 7, 15)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new RandomChange(text[5].title,
                                    text[5].description, 8, 6)).addPair(
                            new ConditionHp(2, 1), new RecoverSelf(-2)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            RandomAttack rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionUsedIf(1, 1, 0,EnemyCondition.Type.HP.ordinal(), 2, 80), new SkillDown(text[1].title,
                            text[1].description, 0, 1, 1)));
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[2].title, text[2].description, 90))
                    .addPair(new ConditionAlways(), new BindPets(3, 3, 3, 1)));
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new Attack(text[3].title, text[3].description, 100))
                    .addPair(new ConditionHp(2, 70), new RandomLineChange(2,4,5,2,12,13)));
            zero.attackMode.addAttack(new ConditionHp(1, 10), rndAttack);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[4].title, text[4].description,
                            500, 2)));
            zero.attackMode.addAttack(new ConditionHp(2, 10), seq);

        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            text = loadSubText(list, 2195);
            data.add(Enemy.create(env, 2195, 38, 3965, 933333, 1,
                    new Pair<Integer, Integer>(1,-1),
                    getMonsterTypes(env, 2195)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SkillDown(text[0].title,text[0].description,0,1,1)));
            zero.attackMode.addAttack(new ConditionHp(0, 100), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[1].title,text[1].description, 999, 200)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(280)).addPair(new ConditionAlways(), new RandomChange(text[2].title,text[2].description,1,3)));
            zero.attackMode.addAttack(new ConditionHp(2, 100), seq);

            text = loadSubText(list, 82);
            data.add(Enemy.create(env, 82, 2070000, 14800, 600000, 4,
                    new Pair<Integer, Integer>(5,-1),
                    getMonsterTypes(env, 82)));
            zero = data.get(1);
            zero.attackMode = new EnemyAttackMode();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SuperDark(text[0].title,text[0].description,3,1,3,1,5,2,3,2,5,3,3,3,4,3,5)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(400)).addPair(new ConditionAlways(), new RandomChange(text[1].title,text[1].description,5,1)));
            zero.attackMode.addAttack(new ConditionHp(2, 100), seq);

            text = loadSubText(list, 2653);
            data.add(Enemy.create(env, 2653, 439808, 14492, 500, 1,
                    new Pair<Integer, Integer>(4,-1),
                    getMonsterTypes(env, 2653)));
            zero = data.get(2);
            zero.attackMode = new EnemyAttackMode();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(110)).addPair(new ConditionAlways(), new RandomChange(text[0].title,text[0].description,6,3)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(70)).addPair(new ConditionAlways(), new LockOrb(text[1].title,text[1].description,0,4)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(2, 100), seq);

        }
        mStageEnemies.put(++stageCounter, data); // 3

        data = new ArrayList<Enemy>();
        {
            text = loadSubText(list, 1458);
            data.add(Enemy.create(env, 1458, 13194300, 22740, 840, 1,
                    new Pair<Integer, Integer>(4,1),
                    getMonsterTypes(env, 1458)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[0].title,text[0].description,70)).addPair(new ConditionAlways(), new LockAwoken(3)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2,20), new MultipleAttack(text[8].title,text[8].description,100,3)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new ReduceTime(text[1].title,text[1].description,5,-2000)));
            zero.attackMode.addAttack(new ConditionHp(2,50), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[2].title,text[2].description,200)).addAction(new DarkScreen(0)).addPair(new ConditionAlways(), new ChangeAttribute(text[3].title,text[3].description,1,4,5,3,0)));
            zero.attackMode.addAttack(new ConditionTurns(2), seq);

            RandomAttack ra = new RandomAttack();
            ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[4].title,text[4].description,99)));
            ra.addAttack(new EnemyAttack().addAction(new Attack(text[5].title,text[5].description,80)).addPair(new ConditionAlways(), new ColorChange(-1,7)));
            ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[6].title,text[6].description,60,2)));
            ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[7].title,text[7].description,20,7)));
            zero.attackMode.addAttack(new ConditionHp(1, 20), ra);

        }
        mStageEnemies.put(++stageCounter, data); // 4

        data = new ArrayList<Enemy>();
        {
            text = loadSubText(list, 2199);
            data.add(Enemy.create(env, 2199, 38, 3965, 933333, 1,
                    new Pair<Integer, Integer>(0,-1),
                    getMonsterTypes(env, 2199)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SkillDown(text[0].title,text[0].description,0,1,1)));
            zero.attackMode.addAttack(new ConditionHp(0, 100), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[1].title,text[1].description, 999, 200)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(280)).addPair(new ConditionAlways(), new RandomChange(text[2].title,text[2].description,6,3)));
            zero.attackMode.addAttack(new ConditionHp(2, 100), seq);

            text = loadSubText(list, 82);
            data.add(Enemy.create(env, 82, 2070000, 14800, 600000, 4,
                    new Pair<Integer, Integer>(5,-1),
                    getMonsterTypes(env, 82)));
            zero = data.get(1);
            zero.attackMode = new EnemyAttackMode();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SuperDark(text[0].title,text[0].description,3,1,3,1,5,2,3,2,5,3,3,3,4,3,5)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(400)).addPair(new ConditionAlways(), new RandomChange(text[1].title,text[1].description,5,1)));
            zero.attackMode.addAttack(new ConditionHp(2, 100), seq);

            text = loadSubText(list, 2652);
            data.add(Enemy.create(env, 2652, 394717, 17860, 500, 1,
                    new Pair<Integer, Integer>(1,-1),
                    getMonsterTypes(env, 2652)));
            zero = data.get(2);
            zero.attackMode = new EnemyAttackMode();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(110)).addPair(new ConditionAlways(), new RandomChange(text[0].title,text[0].description,7,3)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(70)).addPair(new ConditionAlways(), new LockOrb(text[1].title,text[1].description,0,1)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(2, 100), seq);

        }
        mStageEnemies.put(++stageCounter, data); // 5

        data = new ArrayList<Enemy>();
        {
            text = loadSubText(list, 2187);
            data.add(Enemy.create(env, 2187, 5290500, 21480, 500, 1,
                    new Pair<Integer, Integer>(5,-1),
                    getMonsterTypes(env, 2187)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[0].title,text[0].description,999)).addPair(new ConditionAlways(), new ComboShield(text[1].title,text[1].description,4,6)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[3].title,text[3].description,150)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB.ordinal(), 2), new ColorChange(2,5)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[4].title,text[4].description,150)).addPair(new ConditionAlways(), new RandomChange(5,6)));
            zero.attackMode.addAttack(new ConditionUsedIf(1, 1, 0,EnemyCondition.Type.TURN.ordinal(), 3), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[2].title,text[2].description,150)).addPair(new ConditionUsed(), new LineChange(0,5)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[2].title,text[2].description,150)).addPair(new ConditionUsed(), new LineChange(0,5)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[5].title,text[5].description,300,4)));
            zero.attackMode.addAttack(new ConditionHp(1,0), seq);
        }
        mStageEnemies.put(++stageCounter, data); // 6

        data = new ArrayList<Enemy>();
        {
            text = loadSubText(list, 2741);
            data.add(Enemy.create(env, 2741, 110, 21930, 3000000, 1,
                    new Pair<Integer, Integer>(5, -1),
                    getMonsterTypes(env, 2741)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(new DamageVoidShield(text[0].title, text[0].description,10,100))
                    .addPair(new ConditionAlways(), new MultipleAttack(text[1].title, text[1].description,35, 3))
            );
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(
                    1, 1, 0, EnemyCondition.Type.ENEMY_DEBUFF.ordinal(), 0, 0), new Angry(text[2].title, text[2].description,999,200)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            //FIXME: add if and else
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[5].title, text[5].description,30)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB.ordinal(), 7), new ColorChange(7,2)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[3].title, text[3].description,70)).addPair(new ConditionAlways(), new ColorChange(2,7)));
            zero.attackMode.addAttack(new ConditionHp(1,10), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[4].title, text[4].description,300)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB.ordinal(), 7), new ColorChange(7,5)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[3].title, text[3].description,70)).addPair(new ConditionAlways(), new ColorChange(2,7)));
            zero.attackMode.addAttack(new ConditionHp(2,10), seq);
        }
        mStageEnemies.put(++stageCounter, data); // 7

        data = new ArrayList<Enemy>();
        {
            text = loadSubText(list, 2987);
            data.add(Enemy.create(env, 2987, 18856650, 27870, 1140, 1,
                    new Pair<Integer, Integer>(5, 3),
                    getMonsterTypes(env, 2987)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(new ResistanceShieldAction(text[0].title, text[0].description,999))
                    .addPair(new ConditionAlways(), new SkillDown(text[1].title, text[1].description,0,4,4))
            );
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(
                    1, 1,0, EnemyCondition.Type.TURN.ordinal(), 7), new Angry(text[2].title, text[2].description,999,200)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2,15), new MultipleAttack(text[3].title, text[3].description,100,8)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(
                    1,1, 0, EnemyCondition.Type.HP.ordinal(),2,70), new ShieldAction(text[4].title, text[4].description,6,-1,50)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[5].title, text[5].description,100)).addPair(new ConditionUsedIf(
                    1,1, 0, EnemyCondition.Type.HP.ordinal(),2,50), new DropRateAttack(5,6,10,7,10,5,10)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(
                    1,1, 0, EnemyCondition.Type.HAS_BUFF.ordinal()), new IntoVoid(text[6].title, text[6].description,0)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(
                    1,1, 0, EnemyCondition.Type.HAS_BUFF.ordinal()), new IntoVoid(text[6].title, text[6].description,0)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(
                    1,1, 0, EnemyCondition.Type.HAS_BUFF.ordinal()), new IntoVoid(text[6].title, text[6].description,0)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[7].title, text[7].description,100)).addPair(new ConditionAlways(), new LineChange(1,5)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[8].title, text[8].description,99)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[9].title, text[9].description,40,3)));
            zero.attackMode.addAttack(new ConditionHp(1,0), seq);
        }
        mStageEnemies.put(++stageCounter, data); // 8

        {
            data = new ArrayList<Enemy>();
            text = loadSubText(list, 2298);
            data.add(Enemy.create(env, 2298, 11180427, 31233, 1392, 1,
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
        mStageEnemies.put(++stageCounter, data); // 9

        data = new ArrayList<Enemy>();
        {
            text = loadSubText(list, 3074);
            data.add(Enemy.create(env, 3074, 42000000, 11520, 200000, 1,
                    new Pair<Integer, Integer>(5, -1),
                    getMonsterTypes(env, 3074)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(new SkillDown(text[0].title, text[0].description,0,4,4))
                    .addAction(new ResistanceShieldAction(text[1].title, text[1].description,999))
                    .addPair(new ConditionAlways(), new DamageAbsorbShield(text[2].title, text[2].description,99,4000000))
            );
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(
                    1,1, 0, EnemyCondition.Type.HP.ordinal(),2,50), new ShieldAction(text[3].title, text[3].description,2,-1,99)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(
                    1,1, 0, EnemyCondition.Type.HP.ordinal(),2,50), new MultipleAttack(text[4].title, text[4].description,200,6)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(
                    1,1, 0, EnemyCondition.Type.HP.ordinal(),2,20), new RecoverSelf(text[10].title, text[10].description,50)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2,20), new MultipleAttack(text[11].title, text[11].description,1000,10)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            RandomAttack ra = new RandomAttack();
            ra.addAttack(new EnemyAttack().addPair(new ConditionPercentage(40), new Gravity(text[5].title, text[5].description,150)));
            ra.addAttack(new EnemyAttack()
                    .addAction(new SuperDark(text[6].title, text[6].description,3,1,4,2,3,2,6,3,2,3,5,4,1,4,4,5,3)) //SuperDark
                    .addAction(new Attack(text[7].title, text[7].description,420))
                    .addPair(new ConditionAlways(), new BindPets(3,3,3,1)));
            zero.attackMode.addAttack(new ConditionTurnsOffset(2,4), ra);

            ra = new RandomAttack();
            ra.addAttack(new EnemyAttack()
                    .addAction(new Attack(text[8].title, text[8].description,380))
                    .addPair(new ConditionAlways(), new LineChange(10,3,7,5)));
            ra.addAttack(new EnemyAttack()
                    .addAction(new ChangeAttribute(text[9].title, text[9].description,5,3))
                    .addPair(new ConditionAlways(), new Attack(380)));
            zero.attackMode.addAttack(new ConditionHp(1,20), ra);
        }
        mStageEnemies.put(++stageCounter, data); // 10
    }
}
