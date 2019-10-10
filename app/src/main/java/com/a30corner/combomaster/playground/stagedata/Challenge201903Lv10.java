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
import com.a30corner.combomaster.playground.action.impl.ComboShield;
import com.a30corner.combomaster.playground.action.impl.DamageAbsorbShield;
import com.a30corner.combomaster.playground.action.impl.DarkScreen;
import com.a30corner.combomaster.playground.action.impl.DropLockAttack;
import com.a30corner.combomaster.playground.action.impl.DropRateAttack;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.IntoVoid;
import com.a30corner.combomaster.playground.action.impl.LineChange;
import com.a30corner.combomaster.playground.action.impl.LockAwoken;
import com.a30corner.combomaster.playground.action.impl.LockOrb;
import com.a30corner.combomaster.playground.action.impl.LockRemoveAttack;
import com.a30corner.combomaster.playground.action.impl.LockSkill;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.RandomChangeExcept;
import com.a30corner.combomaster.playground.action.impl.RandomLineChange;
import com.a30corner.combomaster.playground.action.impl.RecoverSelf;
import com.a30corner.combomaster.playground.action.impl.ReduceTime;
import com.a30corner.combomaster.playground.action.impl.ResistanceShieldAction;
import com.a30corner.combomaster.playground.action.impl.ShieldAction;
import com.a30corner.combomaster.playground.action.impl.SkillDown;
import com.a30corner.combomaster.playground.action.impl.Transform;
import com.a30corner.combomaster.playground.enemy.EnemyAttack;
import com.a30corner.combomaster.playground.enemy.EnemyAttackMode;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.enemy.attack.FromHeadAttack;
import com.a30corner.combomaster.playground.enemy.attack.RandomAttack;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAlways;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAtTurn;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFindOrb;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHasBuff;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHp;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionIf;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionModeSelection;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionNTurns;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionPercentage;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionTurns;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsedIf;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class Challenge201903Lv10 extends IStage {
    
    @Override
    public void create(IEnvironment env,
                       SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        ArrayList<Enemy> data;
        Enemy zero;
        SequenceAttack seq;
        mStageEnemies.clear();

        data = new ArrayList<Enemy>();
        SparseArray<List<StageGenerator.SkillText>> list = loadSkillTextNew(env, mStage, new int[]{2239, 1726, 2130, 2191, 2641, 2989, 2809});
        StageGenerator.SkillText[] text;// = new StageGenerator.SkillText[list.size()];

        {
            int[] ids = {2239};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 2239) {
                    data.add(Enemy.create(env, 2239, 16815649, 26314, 1008, 1, getMonsterAttrs(env, 2239), getMonsterTypes(env, 2239)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ShieldAction(text[2].title, text[2].description, 10, -1, 50)).addPair(new ConditionAlways(), new ReduceTime(text[4].title, text[4].description, 10, -3000)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockAwoken(text[5].title, text[5].description, 2)).addAction(new Attack(text[6].title, text[6].description, 50)).addPair(new ConditionUsed(), new LineChange(2, 8)));
                    seq.addAttack(new EnemyAttack().addAction(new SkillDown(text[7].title, text[7].description, 0, 2, 2)).addAction(new Attack(text[6].title, text[6].description, 50)).addPair(new ConditionUsed(), new LineChange(2, 8)));
                    seq.addAttack(new EnemyAttack().addAction(new LockSkill(text[9].title, text[9].description, 2)).addAction(new Attack(text[6].title, text[6].description, 50)).addPair(new ConditionUsed(), new LineChange(2, 8)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[11].title, text[11].description, 400)).addPair(new ConditionAlways(), new RandomChange(8, 9)));
                    zero.attackMode.addAttack(new ConditionModeSelection(3, 0, 1), seq);

                    FromHeadAttack fh = new FromHeadAttack();
                    fh.addAttack(new EnemyAttack().addAction(new Attack(text[12].title, text[12].description, 400)).addPair(new ConditionFindOrb(8), new ColorChange(8, 2)));
                    fh.addAttack(new EnemyAttack().addAction(new Attack(text[13].title, text[13].description, 100)).addPair(new ConditionFindOrb(2), new ColorChange(2, 8)));
                    fh.addAttack(new EnemyAttack().addAction(new Attack(text[14].title, text[14].description, 50)).addPair(new ConditionAlways(), new LineChange(2, 8)));
                    zero.attackMode.addAttack(new ConditionModeSelection(3, 1, 2), fh);

                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new BindPets(text[15].title, text[15].description, 2, 2, 2, 2)));
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new BindPets(text[16].title, text[16].description, 3, 1, 1, 6)));

                    zero.attackMode.addAttack(new ConditionModeSelection(3, 2, 0), rndAtk);
                }
            }

        }
        mStageEnemies.put(1, data);
        data = new ArrayList<Enemy>();
        {
            int choose = 1726;
            data.add(Enemy.create(env, choose, 8572098, 30832, 1008, 1,
                    new Pair<Integer, Integer>(1, 0),
                    getMonsterTypes(env, choose)));
            text = loadSubText(list, choose);
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new SkillDown(text[1].title, text[1].description,
                            0, 0, 4)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                            1, 0, EnemyCondition.Type.HP.ordinal(), 2, 1),
                    new SkillDown(text[2].title, text[2].description,
                            0, 99, 99)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                            1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                    new BindPets(text[3].title, text[3].description, 3,
                            2, 2, 6)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 30),
                    new MultipleAttack(text[7].title,
                            text[7].description, 1000, 4)));
            seq.addAttack(new EnemyAttack().addAction(
                    new RandomChange(text[4].title,
                            text[4].description, 1, 4, 0, 4)).addPair(
                    new ConditionTurns(2), new Attack(100)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            RandomAttack rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new ColorChange(text[5].title, text[5].description,
                            1, 7)).addPair(
                    new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                            .ordinal(), 1), new Attack(75)));
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new ColorChange(text[6].title, text[6].description,
                            0, 6)).addPair(
                    new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                            .ordinal(), 0), new Attack(50)));
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionPercentage(75), new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
        }
        mStageEnemies.put(2, data);

        data = new ArrayList<Enemy>();
        {
            int choose = 2130;
            data.add(Enemy.create(env, choose, 13356160, 26349, 180000, 1,
                    new Pair<Integer, Integer>(0, 1),
                    getMonsterTypes(env, choose)));
            text = loadSubText(list, choose);
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(new DropLockAttack(text[2].title, text[2].description, 10, -1, 50))
                    .addAction(new RandomLineChange(2, 1, 6, 4, 10, 11, 14, 15))
                    .addAction(new Attack(text[1].title, text[1].description, 100))
                    .addPair(new ConditionAlways(), new ResistanceShieldAction(text[0].title, text[0].description, 999)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[4].title, text[4].description, 2, 4)).addAction(new AbsorbShieldAction(text[4].title, text[4].description, 2, 5)).addPair(new ConditionUsed(), new AbsorbShieldAction(text[4].title, text[4].description, 2, 1)));
            zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new LockOrb(text[3].title, text[3].description, 0, 1, 4, 5, 3, 0, 2, 6, 7, 8)).addPair(new ConditionAlways(), new Attack(800)));
            zero.attackMode.addAttack(new ConditionNTurns(5, 1), seq);

            RandomAttack rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addAction(new ColorChange(text[5].title, text[5].description, -1, 7)).addPair(new ConditionAlways(), new Attack(170)));
            rndAttack.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[6].title, text[6].description, 95, 2)));
            rndAttack.addAttack(new EnemyAttack().addAction(new ComboShield(text[7].title, text[7].description, 1, 6)).addPair(new ConditionAlways(), new Attack(180)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), rndAttack);

        }
        mStageEnemies.put(3, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {2191};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 2191) {
                    data.add(Enemy.create(env, 2191, 8776500, 19500, 600, 1, getMonsterAttrs(env, 2191), getMonsterTypes(env, 2191)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 76);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[3].title, text[3].description, 5)).addPair(new ConditionAlways(), new Transform(text[5].title, text[5].description, 4, 5, 3, 0)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[6].title, text[6].description, 300)).addPair(new ConditionFindOrb(7), new ColorChange(7, 4)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[7].title, text[7].description, 250)).addPair(new ConditionFindOrb(6), new ColorChange(6, 4)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Transform(text[8].title, text[8].description, 4)).addAction(new Attack(600)).addPair(new ConditionAlways(), new RecoverSelf(text[9].title, text[9].description, 76)));
                    zero.attackMode.addAttack(new ConditionHp(2, 1), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Transform(text[8].title, text[8].description, 4)).addPair(new ConditionAlways(), new Attack(600)));
                    zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LineChange(text[12].title, text[12].description, 4, 4)).addAction(new LineChange(10, 4)).addPair(new ConditionUsed(), new AbsorbShieldAction(text[11].title, text[11].description, 3, 4)));
                    zero.attackMode.addAttack(new ConditionHp(2, 75), seq);

                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new LineChange(text[13].title, text[13].description, 4, 4)).addAction(new Attack(100)).addPair(new ConditionAlways(), new DarkScreen(text[14].title, text[14].description, 0)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new LineChange(text[13].title, text[13].description, 4, 4)).addAction(new Attack(100)).addPair(new ConditionAlways(), new RandomChange(text[16].title, text[16].description, 6, 3)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new LineChange(text[13].title, text[13].description, 4, 4)).addAction(new Attack(100)).addPair(new ConditionAlways(), new RandomChangeExcept(text[18].title, text[18].description, 7, 3, 4)));

                    zero.attackMode.addAttack(new ConditionHp(1, 0), rndAtk);
                }
            }
        }
        mStageEnemies.put(4, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {2641};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 2641) {
                    data.add(Enemy.create(env, 2641, 81400000, 24750, 0, 1, getMonsterAttrs(env, 2641), getMonsterTypes(env, 2641)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockRemoveAttack(text[2].title, text[2].description, 3, 2)).addPair(new ConditionAlways(), new ComboShield(text[4].title, text[4].description, 999, 7)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Gravity(text[5].title, text[5].description, 99)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Gravity(text[5].title, text[5].description, 99)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[6].title, text[6].description, 999, 200)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(100)).addPair(new ConditionAlways(), new ColorChange(text[7].title, text[7].description, 2, 7)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[8].title, text[8].description, 40, 3)));
                    zero.attackMode.addAttack(new ConditionHp(1,0), seq);


                }

            }
        }
        mStageEnemies.put(5, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 2989, 20557500, 31200, 1960, 1, getMonsterAttrs(env, 2989), getMonsterTypes(env, 2989)));
            zero = data.get(0);
            text = loadSubText(list, 2989);

            zero.attackMode = new EnemyAttackMode();
            zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[3].title, text[3].description, 999)).addPair(new ConditionAlways(), new SkillDown(text[5].title, text[5].description, 0, 3, 5)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAtTurn(7), new Angry(text[6].title, text[6].description, 999, 200)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 15), new MultipleAttack(text[7].title, text[7].description, 200, 8)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionHasBuff(), new IntoVoid(text[14].title, text[14].description, 0)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new ShieldAction(text[8].title, text[8].description, 3, -1, 75)));
            zero.attackMode.addAttack(new ConditionHp(2, 70), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[9].title, text[9].description, 120)).addPair(new ConditionUsed(), new DropRateAttack(5, 4, 15, 6, 15, 7, 15)));
            zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[11].title, text[11].description, 100)).addPair(new ConditionAlways(), new RandomLineChange(2, 4, 6, 1, 1)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[13].title, text[13].description, 99)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[15].title, text[15].description, 35, 4)));

            zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
        }
        mStageEnemies.put(6, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {2809};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 2809) {
                    data.add(Enemy.create(env, 2809, 200000000, 31920, 1140, 1, getMonsterAttrs(env, 2809), getMonsterTypes(env, 2809)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 90);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[3].title, text[3].description, 999)).addAction(new DamageAbsorbShield(text[5].title, text[5].description, 999, 8000000)).addPair(new ConditionAlways(), new LockAwoken(text[7].title, text[7].description, 10)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[8].title, text[8].description, 999, 200)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[9].title, text[9].description, 50, 6)));

                    zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new BindPets(text[11].title, text[11].description, 3, 10, 10, 6)).addPair(new ConditionAlways(), new LockOrb(text[10].title, text[10].description, 0, 1, 4, 5, 3, 0, 2, 6, 7, 8, 9)));
                    zero.attackMode.addAttack(new ConditionAtTurn(10), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new DropRateAttack(text[13].title, text[13].description, 10, 6, 15)).addPair(new ConditionUsed(), new Gravity(text[12].title, text[12].description, 99)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    RandomAttack rndatk = new RandomAttack();

                    rndatk.addAttack(new EnemyAttack().addAction(new Attack(text[14].title, text[14].description, 100)).addPair(new ConditionAlways(), new DarkScreen(0)));
                    rndatk.addAttack(new EnemyAttack().addAction(new Attack(text[15].title, text[15].description, 95)).addPair(new ConditionAlways(), new BindPets(2, 2, 2, 1)));
                    rndatk.addAttack(new EnemyAttack().addAction(new Attack(text[16].title, text[16].description, 95)).addPair(new ConditionAlways(), new RandomChange(9, 5)));
                    rndatk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[17].title, text[17].description, 55, 2)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), rndatk);

                }
            }
            mStageEnemies.put(7, data);
        }
    }
}
