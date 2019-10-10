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
import com.a30corner.combomaster.playground.action.impl.DarkScreen;
import com.a30corner.combomaster.playground.action.impl.Daze;
import com.a30corner.combomaster.playground.action.impl.DropLockAttack;
import com.a30corner.combomaster.playground.action.impl.DropRateAttack;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.LineChange;
import com.a30corner.combomaster.playground.action.impl.LockAwoken;
import com.a30corner.combomaster.playground.action.impl.LockOrb;
import com.a30corner.combomaster.playground.action.impl.LockSkill;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.PositionChange;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.RecoverSelf;
import com.a30corner.combomaster.playground.action.impl.RecoveryUpAction;
import com.a30corner.combomaster.playground.action.impl.ReduceTime;
import com.a30corner.combomaster.playground.action.impl.ResistanceShieldAction;
import com.a30corner.combomaster.playground.action.impl.ShieldAction;
import com.a30corner.combomaster.playground.action.impl.SkillDown;
import com.a30corner.combomaster.playground.action.impl.SuperDark;
import com.a30corner.combomaster.playground.action.impl.SwitchLeader;
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

public class Challenge2019_7years_Lv30 extends IStage {

    @Override
    public void create(IEnvironment env,
                       SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        mStageEnemies.clear();

        ArrayList<Enemy> data;
        Enemy zero;
        SequenceAttack seq;

        SparseArray<List<StageGenerator.SkillText>> list = loadSkillTextNew(env, mStage, new int[]{4834,1726,2749,1668,2081,2983}); //
        StageGenerator.SkillText[] text;// = new StageGenerator.SkillText[list.size()];

        // ok!
        data = new ArrayList<Enemy>();
        {
            int[] ids = {1726};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                data.add(Enemy.create(env, choose, 7056300, 25380, 840, 1, new Pair<Integer, Integer>(1, 0), getMonsterTypes(env, choose)));
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

        }
        mStageEnemies.put(1, data);

        // OK!
        data = new ArrayList<Enemy>();
        {
            int[] ids = {2749};
            for (int i = 0; i < 1; ++i) {

                int choose = ids[0];
                text = loadSubText(list, choose);
                if (choose == 2749) {
                    data.add(Enemy.create(env, 2749, 13431300, 28440, 840, 1,getMonsterAttrs(env,2749),getMonsterTypes(env, 2749)));

                    zero = data.get(0);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[0].title, text[0].description, 290)).addPair(new ConditionAlways(), new LockAwoken(text[1].title,text[1].description,5)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Gravity(text[5].title,text[5].description,99)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 1, 50), new ResistanceShieldAction(text[6].title,text[6].description,10)));
                    seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[10].title,text[10].description,1,7)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new ReduceTime(text[11].title,text[11].description,1,-2000)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new MultipleAttack(text[12].title,text[12].description,60,3)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[2].title,text[2].description,100,5)));
                    zero.attackMode.addAttack(new ConditionHp(2,15), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockSkill(text[3].title,text[3].description,5)).addPair(new ConditionUsed(), new Angry(text[4].title,text[4].description,99,200)));
                    zero.attackMode.addAttack(new ConditionHp(2,20), seq);

                    RandomAttack rnd = new RandomAttack();
                    rnd.addAttack(new EnemyAttack().addAction(new SkillDown(text[7].title,text[7].description,0,0,2)).addPair(new ConditionAlways(), new Attack(120)));
                    rnd.addAttack(new EnemyAttack().addAction(new LineChange(text[8].title,text[8].description,1,5)).addPair(new ConditionAlways(), new Attack(130)));
                    rnd.addAttack(new EnemyAttack().addAction(new LockOrb(text[9].title,text[9].description,2,5)).addPair(new ConditionAlways(), new Attack(120)));
                    zero.attackMode.addAttack(new ConditionHp(1,50), rnd);

                    rnd = new RandomAttack();
                    rnd.addAttack(new EnemyAttack().addAction(new SkillDown(text[7].title,text[7].description,0,0,2)).addPair(new ConditionAlways(), new Attack(120)));
                    rnd.addAttack(new EnemyAttack().addAction(new LineChange(text[8].title,text[8].description,1,5)).addPair(new ConditionAlways(), new Attack(130)));
                    rnd.addAttack(new EnemyAttack().addAction(new LockOrb(text[9].title,text[9].description,2,5)).addPair(new ConditionAlways(), new Attack(120)));
                    zero.attackMode.addAttack(new ConditionHp(2,50), rnd);


                }

            }

        }
        mStageEnemies.put(2, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {1668};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 1668) {
                    data.add(Enemy.create(env, 1668, 35000000, 28350, 500000, 1,getMonsterAttrs(env,1668),getMonsterTypes(env, 1668)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new SuperDark(text[3].title, text[3].description, 3, 0, 0, 1, 0, 2, 0, 3, 0, 0)).addPair(new ConditionAlways(),new BindPets(text[5].title, text[5].description, 4, 10, 10, 1, 6)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[7].title,text[7].description,100,4)).addPair(new ConditionHp(2,1), new RecoverSelf(text[6].title, text[6].description, 50)));
                    seq.addAttack(new EnemyAttack().addAction(new Transform(text[8].title, text[8].description, 1, 0, 6)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new PositionChange(text[9].title, text[9].description,9,0,0,0,1,0,2,0,3,0,4,2,4,3)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[17].title,text[17].description,1,0)).addPair(new ConditionAlways(), new AbsorbShieldAction(1,1)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[18].title, text[18].description, 300)));
                    zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new ColorChange(text[10].title, text[10].description, 1,6)).addPair(new ConditionAlways(), new Attack(105))); // FIXME: color -> 2 colors
                    rndAtk.addAttack(new EnemyAttack().addAction(new RandomChange(text[11].title,text[11].description,9,8)).addPair(new ConditionAlways(), new Attack(60)));
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[12].title,text[12].description,40,3)));
                    zero.attackMode.addAttack(new ConditionModeSelection(3,0), rndAtk);

                    rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new ColorChange(text[10].title, text[10].description, 1,6)).addPair(new ConditionAlways(), new Attack(105))); // FIXME: color -> 2 colors
                    rndAtk.addAttack(new EnemyAttack().addAction(new RandomChange(text[11].title,text[11].description,9,8)).addPair(new ConditionAlways(), new Attack(60)));
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[12].title,text[12].description,40,3)));
                    zero.attackMode.addAttack(new ConditionModeSelection(3,1), rndAtk);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new SuperDark(text[3].title, text[3].description, 3, 2,2,2,3,2,4,2,5,3,2,3,3,3,4,3,5,4,2,4,3,4,4,4,5)).addPair(new ConditionAlways(), new RandomChange(text[14].title,text[14].description,6,6)));
                    seq.addAttack(new EnemyAttack().addAction(new SuperDark(text[3].title, text[3].description, 3, 0, 0, 1, 0, 2, 0, 3, 0, 0)).addPair(new ConditionAlways(), new RandomChange(text[16].title,text[16].description,6,6)));
                    zero.attackMode.addAttack(new ConditionModeSelection(3,2), seq);

                }

            }

        }
        mStageEnemies.put(3, data);

        // OK!
        data = new ArrayList<Enemy>();
        {
            int[] ids = {2081};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 2081) {
                    data.add(Enemy.create(env, 2081, 9414704, 31878, 0, 1,getMonsterAttrs(env,2081),getMonsterTypes(env, 2081)));

                    zero = data.get(0);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addAction(
                                    new DropRateAttack(text[1].title,
                                            text[1].description, 99, 7, 15))
                            .addAction(new DropRateAttack(99, 2, 15))
                            .addPair(
                                    new ConditionAlways(),
                                    new DamageAbsorbShield(text[3].title,
                                            text[3].description, 999, 1000000)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addAction(
                                    new Attack(text[6].title,
                                            text[6].description, 70))
                            .addAction(new BindPets(3, 2, 2, 5))
                            .addAction(
                                    new Attack(text[7].title,
                                            text[7].description, 10))
                            .addPair(
                                    new ConditionUsedIf(1, 1, 0,
                                            EnemyCondition.Type.HP.ordinal(), 2, 50),
                                    new RecoverSelf(10)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new Daze(text[4].title, text[4].description))
                            .addPair(
                                    new ConditionIf(1, 1, 0,
                                            EnemyCondition.Type.FIND_PET.ordinal(),
                                            5, 238, 239, 1114, 1115, 1955),
                                    new ResistanceShieldAction(text[5].title,
                                            text[5].description, 999)));
                    zero.attackMode.addAttack(new ConditionUsed(), seq);

                    RandomAttack rndAttack = new RandomAttack();
                    rndAttack.addAttack(new EnemyAttack()
                            .addAction(
                                    new Attack(text[8].title,
                                            text[8].description, 80))
                            .addAction(new ColorChange(2, 7))
                            .addAction(
                                    new Attack(text[9].title,
                                            text[9].description, 10))
                            .addPair(new ConditionAlways(), new RecoverSelf(10)));
                    rndAttack.addAttack(new EnemyAttack()
                            .addAction(
                                    new MultipleAttack(text[10].title,
                                            text[10].description, 35, 4))
                            .addAction(
                                    new Attack(text[11].title,
                                            text[11].description, 10))
                            .addPair(new ConditionAlways(), new RecoverSelf(10)));
                    rndAttack.addAttack(new EnemyAttack()
                            .addAction(
                                    new Attack(text[12].title,
                                            text[12].description, 120))
                            .addAction(new LineChange(0, 2, 2, 5))
                            .addAction(
                                    new Attack(text[13].title,
                                            text[13].description, 10))
                            .addPair(new ConditionAlways(), new RecoverSelf(10)));
                    zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                            new DarkScreen(text[14].title, text[14].description,
                                    0)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                            new MultipleAttack(text[15].title,
                                    text[15].description, 1000, 3)));
                    zero.attackMode.addAttack(new ConditionHp(2, 30), seq);


                }

            }

        }
        mStageEnemies.put(4, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {2983};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 2983) {
                    data.add(Enemy.create(env, 2983, 30006300, 33120, 500000, 2,getMonsterAttrs(env,2983),getMonsterTypes(env, 2983)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.CHANGE_TURN, 50);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[8].title, text[8].description, 999, 6)).addAction(new ResistanceShieldAction(text[10].title,text[10].description, 999)).addPair(new ConditionAlways(),new ReduceTime(text[12].title, text[12].description, 6, 250, 0)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SwitchLeader(text[18].title, text[18].description, 1)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[19].title, text[19].description, 300)));
                    zero.attackMode.addAttack(new ConditionHp(2,25), seq);

                    seq = new SequenceAttack();

                    seq.addAttack(new EnemyAttack().addAction(new LineChange(text[14].title, text[14].description,1,2)).addAction(new Attack(100)).addAction(new ColorChange(text[17].title, text[17].description, 2,6)).addPair(new ConditionAlways(), new Attack(100)));
                    zero.attackMode.addAttack(new ConditionHp(2,50), seq);


                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ShieldAction(text[13].title, text[13].description, 1, -1, 75)).addAction(new LineChange(text[14].title, text[14].description,1,2)).addAction(new Attack(100)).addAction(new ColorChange(text[15].title, text[15].description, 2,7)).addPair(new ConditionAlways(), new Attack(100)));
                    zero.attackMode.addAttack(new ConditionHp(1,50), seq);

                }

            }

        }
        mStageEnemies.put(5, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {4834};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 4834) {
                    data.add(Enemy.create(env, 4834, 800000000, 40400, 0, 1,getMonsterAttrs(env,4834),getMonsterTypes(env, 4834)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[3].title,text[3].description, 999)).addAction(new DamageAbsorbShield(text[5].title,text[5].description, 999, 30000000)).addAction(new RecoveryUpAction(text[7].title, text[7].description, 10, 50)).addPair(new ConditionAlways(),new SkillDown(text[9].title, text[9].description, 0, 10, 10)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new RecoverSelf(text[10].title, text[10].description, 100)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new DropLockAttack(text[11].title, text[11].description, 99, -1, 100)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 8), new Angry(text[12].title,text[12].description, 99,200)));
                    seq.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[13].title, text[13].description, 1,4,5,3,0)).addPair(new ConditionHp(2,8), new MultipleAttack(text[14].title,text[14].description,500,4,5)));

                    seq.addAttack(new EnemyAttack().addAction(new RecoveryUpAction(text[15].title, text[15].description, 10, 50)).addPair(new ConditionTurns(10), new Gravity(text[16].title, text[16].description, 75)));

                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(1,50), new MultipleAttack(text[17].title,text[17].description,50,2)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2,50), new MultipleAttack(text[18].title,text[18].description,50,3)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);


                }

            }

        }
        mStageEnemies.put(6, data);


    }
}
