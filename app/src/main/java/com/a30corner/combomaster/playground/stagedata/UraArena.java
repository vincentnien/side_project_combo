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
import com.a30corner.combomaster.playground.action.impl.CloudAttack;
import com.a30corner.combomaster.playground.action.impl.ColorChange;
import com.a30corner.combomaster.playground.action.impl.ComboShield;
import com.a30corner.combomaster.playground.action.impl.DamageAbsorbShield;
import com.a30corner.combomaster.playground.action.impl.DamageVoidShield;
import com.a30corner.combomaster.playground.action.impl.DarkScreen;
import com.a30corner.combomaster.playground.action.impl.Daze;
import com.a30corner.combomaster.playground.action.impl.DropLockAttack;
import com.a30corner.combomaster.playground.action.impl.DropRateAttack;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.LineChange;
import com.a30corner.combomaster.playground.action.impl.LockAwoken;
import com.a30corner.combomaster.playground.action.impl.LockOrb;
import com.a30corner.combomaster.playground.action.impl.LockRemoveAttack;
import com.a30corner.combomaster.playground.action.impl.LockSkill;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.PositionChange;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.RandomChangeExcept;
import com.a30corner.combomaster.playground.action.impl.RandomLineChange;
import com.a30corner.combomaster.playground.action.impl.RecoverDead;
import com.a30corner.combomaster.playground.action.impl.RecoverPlayer;
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
import com.a30corner.combomaster.playground.enemy.attack.FromHeadAttack;
import com.a30corner.combomaster.playground.enemy.attack.RandomAttack;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAlways;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAtTurn;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFindDeadBody;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFindOrb;
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

public class UraArena extends IStage {

    @Override
    public void create(IEnvironment env, SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        mStageEnemies.clear();

        ArrayList<Enemy> data;
        Enemy zero;
        SequenceAttack seq;
        SparseArray<List<StageGenerator.SkillText>> list = loadSkillTextNew(env, mStage, new int[]{ 1458, 1602, 2130, 1526, 1527, 1528, 1529, 1530, 3721, 3722, 3723, 3724, 3725, 2739, 2740, 2741, 2742, 2743,
                3741, 3742, 3743, 3744, 3745, 234, 3000, 3001, 3002, 3726, 3727, 3728, 3729, 3730, 1206, 1207, 310, 311, 312, 313, 314, 315,
                1294, 1295, 1463, 1465, 1837, 2008, 2738, 2180, 2664, 2006, 2391, 2939, 2940, 2941, 1955, 1726, 1956, 1954,
                3829, 3832, 3881, 2092, 2277, 2754, 3013, 3245, 985, 1536, 1737, 2807, 2946, 1217, 1644,
                1747, 2078});
        StageGenerator.SkillText[] text;
        int stageCounter = 0;

        data = new ArrayList<Enemy>();
        for (int i = 0; i < 2; ++i) {
            int rndId = RandomUtil.range(1526, 1530);
            text = loadSubText(list, rndId);
            if (rndId == 1526) {
                data.add(Enemy.create(env, 1526, 4718311, 24827, 3200, 1,
                        new Pair<Integer, Integer>(1, -1),
                        getMonsterTypes(env, 1526)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createEmptyFirstStrike()
                        .createEmptyMustAction();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[1].title, text[1].description, 9, 3))
                        .addPair(new ConditionAlways(), new Attack(100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[2].title, text[2].description,
                                70, 5, 8)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

            } else if (rndId == 1527) {
                data.add(Enemy.create(env, rndId, 3162756, 29493, 3200, 1,
                        new Pair<Integer, Integer>(4, -1),
                        getMonsterTypes(env, rndId)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                int[] absorb = {1, 4, 5, 3};
                int index = RandomUtil.getInt(absorb.length);
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[5].title, text[5].description, 2, 1,
                                2, 3)).addAction(
                        new Attack(80))
                        .addAction(new AbsorbShieldAction(text[index + 1].title,
                                text[index + 1].description, 5, 0))
                        .addPair(new ConditionAlways(),
                                new AbsorbShieldAction(text[index + 1].title,
                                        text[index + 1].description, 5, absorb[index])));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[5].title, text[5].description, 2, 1,
                                2, 3)).addPair(new ConditionAlways(),
                        new Attack(80)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(2, 75), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 75), seq);

            } else if (rndId == 1528) {
                data.add(Enemy.create(env, rndId, 1607200, 26071, 160000, 1,
                        new Pair<Integer, Integer>(5, -1),
                        getMonsterTypes(env, rndId)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new ComboShield(text[1].title, text[1].description, 3, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[2].title, text[2].description, 100))
                        .addPair(new ConditionUsed(), new SuperDark(1, 1, 0, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Angry(text[3].title, text[3].description, 2, 1000)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[4].title, text[4].description, 300, 2)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            } else if (rndId == 1529) {
                data.add(Enemy.create(env, rndId, 33716411, 27049, 0, 4,
                        new Pair<Integer, Integer>(3, -1),
                        getMonsterTypes(env, rndId)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Angry(text[1].title, text[1].description, 5, 2100)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Angry(text[1].title, text[1].description, 5, 2100)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            } else if (rndId == 1530) {
                data.add(Enemy.create(env, rndId, 75, 36960, 600000, 2,
                        new Pair<Integer, Integer>(0, -1),
                        getMonsterTypes(env, rndId)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new LockAwoken(text[1].title, text[1].description, 3)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[2].title, text[2].description, -1,
                                7)).addPair(new ConditionAlways(),
                        new Attack(110)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data);
// done
        data = new ArrayList<Enemy>();
        {
            int choose = 3721 + RandomUtil.getInt(5);
            text = loadSubText(list, choose);
            if (choose == 3721) {
                data.add(Enemy.create(env, choose, 39357111, 29524, 1080, 1,
                        new Pair<Integer, Integer>(1, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 5, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[2].title, text[2].description, 1, 6)).addAction(new Attack(140)).addPair(new ConditionAlways(), new RandomLineChange(text[1].title, text[1].description, 2, 1, 7, 3, 13, 14, 15)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[4].title, text[4].description, 10)).addPair(new ConditionHp(1, 99), new ShieldAction(text[3].title, text[3].description, 4, -1, 75)));
                seq.addAttack(new EnemyAttack().addAction(new ReduceTime(text[6].title, text[6].description, 5, 1500, 0)).addPair(new ConditionHp(2, 99), new ShieldAction(text[3].title, text[3].description, 4, -1, 75)));
                zero.attackMode.addAttack(new ConditionAtTurn(1), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new LockOrb(text[12].title, text[12].description, 0, 1, 4, 5, 3, 0, 2, 6, 7, 8, 9)));
                seq.addAttack(new EnemyAttack().addAction(new Transform(text[13].title, text[13].description, 1, 7)).addPair(new ConditionAlways(), new Attack(3000)));
                zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[7].title, text[7].description, 999, 150)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ShieldAction(text[8].title, text[8].description, 4, -1, 75)));
                zero.attackMode.addAttack(new ConditionNTurns(4, 1), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[9].title, text[9].description, 130)).addPair(new ConditionAlways(), new LineChange(1, 7)));
                seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[10].title, text[10].description, 2, 1)).addPair(new ConditionAlways(), new Attack(130)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[11].title, text[11].description, 90)));
                zero.attackMode.addAttack(new ConditionHp(1, 20), seq);

            } else if (choose == 3722) {
                data.add(Enemy.create(env, choose, 39734000, 28787, 1080, 1,
                        new Pair<Integer, Integer>(4, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 1, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new RandomChange(text[2].title, text[2].description, 7, 11)).addAction(new CloudAttack(text[8].title, text[8].description, 1, 6, 2)).addPair(new ConditionAlways(), new ShieldAction(text[1].title, text[1].description, 5, -1, 50)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[4].title, text[4].description, 7, 6)).addAction(new Attack(250)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.FIND_ORB.ordinal(), 7), new ComboShield(text[3].title, text[3].description, 10, 6)));
                seq.addAttack(new EnemyAttack().addAction(new Attack(100)).addPair(new ConditionUsedIf(1, 1, 1, EnemyCondition.Type.FIND_ORB.ordinal(), 7), new ComboShield(text[3].title, text[3].description, 10, 6)));
                zero.attackMode.addAttack(new ConditionAtTurn(1), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new LockAwoken(text[10].title, text[10].description, 5)));
                seq.addAttack(new EnemyAttack().addAction(new Transform(text[11].title, text[11].description, 4, 7)).addPair(new ConditionAlways(), new Attack(3000)));
                zero.attackMode.addAttack(new ConditionHp(2, 20), seq);


                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[5].title, text[5].description, 999, 150)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ShieldAction(text[6].title, text[6].description, 4, -1, 75)));
                zero.attackMode.addAttack(new ConditionNTurns(4, 1), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(120)).addPair(new ConditionAlways(), new SkillDown(text[7].title, text[7].description, 0, 2, 2)));
                seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[8].title, text[8].description, 2, 4)).addPair(new ConditionAlways(), new Attack(130)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[9].title, text[9].description, 90)));
                zero.attackMode.addAttack(new ConditionHp(1, 20), seq);
            } else if (choose == 3723) {
                data.add(Enemy.create(env, choose, 39545556, 29156, 1080, 1,
                        new Pair<Integer, Integer>(5, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[2].title, text[2].description, 1, 6)).addPair(new ConditionAlways(), new Daze(text[13].title, text[13].description, 0)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[3].title, text[3].description, 4, 3)).addAction(new AbsorbShieldAction(text[3].title, text[3].description, 4, 0)).addPair(new ConditionHp(1, 99), new ShieldAction(text[2].title, text[2].description, 4, -1, 75)));
                seq.addAttack(new EnemyAttack().addAction(new DropRateAttack(text[5].title, text[5].description, 99, 5, 25)).addPair(new ConditionHp(2, 99), new ShieldAction(text[4].title, text[4].description, 4, -1, 75)));
                zero.attackMode.addAttack(new ConditionAtTurn(1), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new ReduceTime(text[11].title, text[11].description, 5, 500, 0)));
                seq.addAttack(new EnemyAttack().addAction(new Transform(text[12].title, text[12].description, 5, 7)).addPair(new ConditionAlways(), new Attack(3000)));
                zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[6].title, text[6].description, 999, 150)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ShieldAction(text[7].title, text[7].description, 4, -1, 75)));
                zero.attackMode.addAttack(new ConditionNTurns(4, 1), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new LockOrb(text[8].title, text[8].description, 1, 9, 1, 4, 5, 3, 0, 2, 6, 7, 8)).addPair(new ConditionAlways(), new Attack(120)));
                seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[9].title, text[9].description, 2, 5)).addPair(new ConditionAlways(), new Attack(130)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[10].title, text[10].description, 90)));
                zero.attackMode.addAttack(new ConditionHp(1, 20), seq);
            } else if (choose == 3724) {
                data.add(Enemy.create(env, choose, 39921556, 29371, 1080, 1,
                        new Pair<Integer, Integer>(3, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new LockOrb(text[1].title, text[1].description, 0, 3, 2)).addPair(new ConditionAlways(), new Transform(text[2].title, text[2].description, 7)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(280)).addPair(new ConditionFindOrb(7), new ColorChange(text[3].title, text[3].description, 7, 2)));
                seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[5].title, text[5].description, 10)).addPair(new ConditionUsed(), new ShieldAction(text[4].title, text[4].description, 10, -1, 35)));
                zero.attackMode.addAttack(new ConditionUsed(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new SkillDown(text[11].title, text[11].description, 0, 5, 5)));
                seq.addAttack(new EnemyAttack().addAction(new Transform(text[12].title, text[12].description, 3, 7)).addPair(new ConditionAlways(), new Attack(3000)));
                zero.attackMode.addAttack(new ConditionHp(2, 20), seq);


                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[6].title, text[6].description, 999, 150)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new DamageAbsorbShield(text[7].title, text[7].description, 4, 200000)));
                zero.attackMode.addAttack(new ConditionNTurns(4, 1), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new RecoveryUpAction(2, 50)).addPair(new ConditionAlways(), new Attack(text[8].title, text[8].description, 130)));
                seq.addAttack(new EnemyAttack().addAction(new RandomChange(text[9].title, text[9].description, 8, 3)).addPair(new ConditionAlways(), new Attack(110)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[10].title, text[10].description, 90)));
                zero.attackMode.addAttack(new ConditionHp(1, 20), seq);
            } else if (choose == 3725) {
                data.add(Enemy.create(env, choose, 40109556, 28453, 1080, 1,
                        new Pair<Integer, Integer>(0, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new SuperDark(text[1].title, text[1].description, 1, 2, 1, 5, 1, 2, 2, 2, 3, 1, 3, 5, 4, 4, 4, 4, 5, 4, 6, 1, 6)).addPair(new ConditionAlways(), new RandomChange(text[2].title, text[2].description, 29, 6)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new DropRateAttack(text[4].title, text[4].description, 7, 2, 20, 7, 20)).addPair(new ConditionHp(2, 70), new ShieldAction(text[3].title, text[3].description, 4, -1, 75)));
                seq.addAttack(new EnemyAttack().addAction(new DropLockAttack(text[4].title, text[4].description, 7, -1, 25)).addPair(new ConditionHp(1, 70), new ShieldAction(text[3].title, text[3].description, 4, -1, 75)));
                zero.attackMode.addAttack(new ConditionAtTurn(1), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Transform(text[15].title, text[15].description, 7)));
                seq.addAttack(new EnemyAttack().addAction(new Transform(text[16].title, text[16].description, 0, 7)).addPair(new ConditionAlways(), new Attack(3000)));
                zero.attackMode.addAttack(new ConditionHp(2, 20), seq);


                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[7].title, text[7].description, 999, 150)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ShieldAction(text[8].title, text[8].description, 4, -1, 75)));
                zero.attackMode.addAttack(new ConditionNTurns(4, 1), seq);

                FromHeadAttack fh = new FromHeadAttack();
                fh.addAttack(new EnemyAttack().addAction(new ColorChange(text[9].title, text[9].description, 7, 6)).addPair(new ConditionFindOrb(7), new Attack(150)));
                fh.addAttack(new EnemyAttack().addAction(new RandomChange(text[10].title, text[10].description, 6, 4)).addPair(new ConditionAlways(), new Attack(110)));
                zero.attackMode.addAttack(new ConditionModeSelection(3, 0), fh);

                fh = new FromHeadAttack();
                fh.addAttack(new EnemyAttack().addAction(new ColorChange(text[11].title, text[11].description, 6, 7)).addPair(new ConditionFindOrb(6), new Attack(130)));
                fh.addAttack(new EnemyAttack().addAction(new ColorChange(text[12].title, text[12].description, 2, 7)).addPair(new ConditionAlways(), new Attack(130)));
                zero.attackMode.addAttack(new ConditionModeSelection(3, 1), fh);

                fh = new FromHeadAttack();
                fh.addAttack(new EnemyAttack().addAction(new LockOrb(0, 7)).addPair(new ConditionFindOrb(7), new Attack(text[13].title, text[13].description, 100)));
                fh.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Gravity(text[14].title, text[14].description, 90)));
                zero.attackMode.addAttack(new ConditionModeSelection(3, 2), fh);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 2++stageCounter

        data = new ArrayList<Enemy>();
        {
            int[] ids = {2739, 2740, 2741, 2742, 2743};
            int choose = ids[RandomUtil.getInt(ids.length)];
            text = loadSubText(list, choose);
            if (choose == 2739) {
                data.add(Enemy.create(env, choose, 18327320, 33576, 656, 1,
                        new Pair<Integer, Integer>(1, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 76);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new DamageVoidShield(text[2].title, text[2].description, 999, 5000000)).addPair(new ConditionAlways(), new ComboShield(text[1].title, text[1].description, 99, 4)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(1, 75), new MultipleAttack(text[5].title, text[5].description, 50, 3)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1, 1, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 4, 0), new LockAwoken(text[3].title, text[3].description, 3)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[4].title, text[4].description, 300)));
                zero.attackMode.addAttack(new ConditionHp(2, 1), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new ShieldAction(text[8].title, text[8].description, 3, -1, 75)));
                seq.addAttack(new EnemyAttack().addAction(new RandomChangeExcept(7, 5, 1)).addPair(new ConditionAlways(), new Attack(text[9].title, text[9].description, 50)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                RandomAttack rnd = new RandomAttack();
                rnd.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[5].title, text[5].description, 50, 3)));
                rnd.addAttack(new EnemyAttack().addAction(new Gravity(text[7].title, text[7].description, 99)).addPair(new ConditionAlways(), new RecoverSelf(text[6].title, text[6].description, 10)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), rnd);
            } else if (choose == 2740) {
                data.add(Enemy.create(env, choose, 28366373, 32940, 656, 1,
                        new Pair<Integer, Integer>(4, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new LockSkill(text[0].title, text[0].description, 5)).addPair(new ConditionIf(1, 1, 1, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 0, 0), new ReduceTime(text[1].title, text[1].description, 5, -3000)));
                seq.addAttack(new EnemyAttack().addAction(new LockSkill(text[0].title, text[0].description, 5)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 0, 0), new Attack(100)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(text[2].title, text[2].description, 50)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[3].title, text[3].description, 50, 2)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[4].title, text[4].description, 50, 3)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[5].title, text[5].description, 50, 4)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[6].title, text[6].description, 200, 4)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

            } else if (choose == 2741) {
                data.add(Enemy.create(env, choose, 90, 32775, 3000000, 1,
                        new Pair<Integer, Integer>(5, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(new DamageVoidShield(text[0].title, text[0].description, 10, 100))
                        .addPair(new ConditionAlways(), new MultipleAttack(text[1].title, text[1].description, 50, 3))
                );
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(
                        1, 1, 0, EnemyCondition.Type.ENEMY_DEBUFF.ordinal(), 0, 0), new Angry(text[2].title, text[2].description, 999, 200)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);


                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[4].title, text[4].description, 300)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB.ordinal(), 7), new ColorChange(7, 5)));
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[3].title, text[3].description, 70)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB.ordinal(), 2), new ColorChange(2, 7)));
                zero.attackMode.addAttack(new ConditionHp(2, 10), seq);
                //FIXME: add if and else
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[5].title, text[5].description, 30)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB.ordinal(), 7), new ColorChange(7, 2)));
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[3].title, text[3].description, 70)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB.ordinal(), 2), new ColorChange(2, 7)));
                zero.attackMode.addAttack(new ConditionHp(1, 10), seq);


            } else if (choose == 2742) {
                data.add(Enemy.create(env, choose, 42370740, 29194, 656, 1,
                        new Pair<Integer, Integer>(3, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new DropRateAttack(text[1].title, text[1].description, 5, 3, 25, 2, 25)).addPair(new ConditionAlways(), new ResistanceShieldAction(text[0].title, text[0].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                // check which one first
                seq.addAttack(new EnemyAttack().addAction(new Gravity(text[2].title, text[2].description, 60)).addAction(new RandomChange(text[3].title, text[3].description, 8, 1)).addPair(new ConditionAlways(), new Attack(40)));
                seq.addAttack(new EnemyAttack().addAction(new Gravity(text[2].title, text[2].description, 60)).addAction(new RandomChange(text[3].title, text[3].description, 8, 1)).addPair(new ConditionAlways(), new Attack(40)));
                seq.addAttack(new EnemyAttack().addAction(new Gravity(text[2].title, text[2].description, 60)).addAction(new RandomChange(text[3].title, text[3].description, 8, 1)).addPair(new ConditionAlways(), new Attack(40)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1, 1, EnemyCondition.Type.FIND_ORB.ordinal(), 8), new RecoverPlayer(text[5].title, text[5].description, 100)));
                seq.addAttack(new EnemyAttack().addAction(new ColorChange(8, 3)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB.ordinal(), 8), new Attack(text[4].title, text[4].description, 5000)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (choose == 2743) {
                data.add(Enemy.create(env, choose, 10824366, 31663, 656, 1,
                        new Pair<Integer, Integer>(0, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new LineChange(10, 0)).addAction(new Attack(text[1].title, text[1].description, 150)).addPair(new ConditionAlways(), new AbsorbShieldAction(text[0].title, text[0].description, 4, 0)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 10), new MultipleAttack(text[2].title, text[2].description, 100, 8)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(0, 100), new Attack(text[4].title, text[4].description, 200)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new BindPets(text[3].title, text[3].description, 5, 5, 10, 1, 0)));
                zero.attackMode.addAttack(new ConditionNTurns(3, 1), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[5].title, text[5].description, -1, 0)).addPair(new ConditionAlways(), new Attack(80)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 3
        // done
        data = new ArrayList<Enemy>();
        {
            int[] ids = {3741, 3742, 3743, 3744, 3745};
            for (int x = 0; x < 3; ++x) {
                if (x == 1) {
                    text = loadSubText(list, 234);
                    data.add(Enemy.create(env, 234, 110, 37778, 2000000, 1,
                            new Pair<Integer, Integer>(3, -1),
                            getMonsterTypes(env, 234)));
                    zero = data.get(x);
                    zero.setHasResurrection();
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SkillDown(text[0].title, text[0].description, 0, 1, 3)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionFindDeadBody(), new RecoverDead(text[1].title, text[1].description, 100)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[2].title, text[2].description, 200)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
                    zero.attackMode.addAttack(new ConditionHp(1, 50), seq);
                } else {
                    int choose = RandomUtil.chooseOne(ids);
                    text = loadSubText(list, choose);
                    if (choose == 3741) {
                        data.add(Enemy.create(env, choose, 15150000, 14000, 260, 1,
                                new Pair<Integer, Integer>(1, -1),
                                getMonsterTypes(env, choose)));
                        zero = data.get(x);
                        zero.setHasResurrection();
                        zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 80);
                        zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 5, 50);
                        zero.attackMode = new EnemyAttackMode();
                        seq = new SequenceAttack();
                        seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ComboShield(text[2].title, text[2].description, 3, 5)));
                        zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                        seq = new SequenceAttack();
                        seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new RandomLineChange(text[3].title, text[3].description, 2, 1, 7, 2, 13, 12)));
                        seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Gravity(text[4].title, text[4].description, 90)));
                        zero.attackMode.addAttack(new ConditionAlways(), seq);

                        seq = new SequenceAttack();
                        seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[5].title, text[5].description, 100, 2)));
                        seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new RecoverSelf(text[6].title, text[6].description, 50)));
                        zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                    } else if (choose == 3742) {
                        data.add(Enemy.create(env, choose, 15070000, 12550, 260, 1,
                                new Pair<Integer, Integer>(4, -1),
                                getMonsterTypes(env, choose)));
                        zero = data.get(x);
                        zero.setHasResurrection();
                        zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 80);
                        zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 1, 50);
                        zero.attackMode = new EnemyAttackMode();
                        seq = new SequenceAttack();
                        seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1, 1, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 0, 0), new ReduceTime(text[2].title, text[2].description, 3, -2 * 1000)));
                        seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 0, 0), new Attack(100)));
                        zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                        seq = new SequenceAttack();
                        seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new RandomLineChange(text[3].title, text[3].description, 2, 4, 7, 2, 13, 12)));
                        seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Gravity(text[4].title, text[4].description, 90)));
                        zero.attackMode.addAttack(new ConditionAlways(), seq);

                        seq = new SequenceAttack();
                        seq.addAttack(new EnemyAttack().addAction(new LockOrb(1, 8, 1, 4, 5, 3, 0, 2, 6, 7, 8)).addPair(new ConditionAlways(), new Attack(text[5].title, text[5].description, 160)));
                        seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new RecoverSelf(text[6].title, text[6].description, 50)));
                        zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                    } else if (choose == 3743) {
                        data.add(Enemy.create(env, choose, 15000000, 10650, 260, 1,
                                new Pair<Integer, Integer>(5, -1),
                                getMonsterTypes(env, choose)));
                        zero = data.get(x);
                        zero.setHasResurrection();
                        zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 80);
                        zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);
                        zero.attackMode = new EnemyAttackMode();
                        seq = new SequenceAttack();
                        seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new LockSkill(text[2].title, text[2].description, 3)));
                        zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                        seq = new SequenceAttack();
                        seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new RandomLineChange(text[3].title, text[3].description, 2, 5, 7, 2, 13, 12)));
                        seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Gravity(text[4].title, text[4].description, 90)));
                        zero.attackMode.addAttack(new ConditionAlways(), seq);

                        seq = new SequenceAttack();
                        seq.addAttack(new EnemyAttack().addAction(new Attack(160)).addPair(new ConditionAlways(), new SkillDown(text[5].title, text[5].description, 0, 1, 1)));
                        seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new RecoverSelf(text[6].title, text[6].description, 50)));
                        zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                    } else if (choose == 3744) {
                        data.add(Enemy.create(env, choose, 15300000, 12760, 260, 1,
                                new Pair<Integer, Integer>(3, -1),
                                getMonsterTypes(env, choose)));
                        zero = data.get(x);
                        zero.setHasResurrection();
                        zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 80);
                        zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
                        zero.attackMode = new EnemyAttackMode();
                        seq = new SequenceAttack();
                        seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SkillDown(text[2].title, text[2].description, 0, 3, 3)));
                        zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                        seq = new SequenceAttack();
                        seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new RandomLineChange(text[3].title, text[3].description, 2, 3, 7, 2, 13, 12)));
                        seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Gravity(text[4].title, text[4].description, 90)));
                        zero.attackMode.addAttack(new ConditionAlways(), seq);

                        seq = new SequenceAttack();
                        seq.addAttack(new EnemyAttack().addAction(new Attack(text[5].title, text[5].description, 170)).addPair(new ConditionAlways(), new BindPets(text[5].title, text[5].description, 2, 1, 1, 1)));
                        seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new RecoverSelf(text[6].title, text[6].description, 50)));
                        zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                    } else if (choose == 3745) {
                        data.add(Enemy.create(env, choose, 15300000, 14450, 260, 1,
                                new Pair<Integer, Integer>(0, -1),
                                getMonsterTypes(env, choose)));
                        zero = data.get(x);
                        zero.setHasResurrection();
                        zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 80);
                        zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
                        zero.attackMode = new EnemyAttackMode();
                        seq = new SequenceAttack();
                        seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Daze(text[2].title, text[2].description, 0)));
                        zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                        seq = new SequenceAttack();
                        seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new RandomLineChange(text[3].title, text[3].description, 2, 0, 7, 2, 13, 12)));
                        seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Gravity(text[4].title, text[4].description, 90)));
                        zero.attackMode.addAttack(new ConditionAlways(), seq);

                        seq = new SequenceAttack();
                        seq.addAttack(new EnemyAttack().addAction(new DarkScreen(0)).addPair(new ConditionAlways(), new Attack(text[5].title, text[5].description, 170)));
                        seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new RecoverSelf(text[6].title, text[6].description, 50)));
                        zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                    }
                }

            }

        }
        mStageEnemies.put(++stageCounter, data); // 4

        //done
        data = new ArrayList<Enemy>();
        {
            for (int i = 0; i < 1; ++i) {
                int idx = RandomUtil.getInt(9);
                int choose = 3317 + idx;
                data.add(Enemy.create(env, choose, 50, 4500, 5000000, 1,
                        new Pair<Integer, Integer>(3, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new SkillDown("キラキラ光るたまぁ~☆", "", 0, 3, 3)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack("たまぁ~☆", "", 1000)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            }

        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {1458, 1602, 2130};
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            text = loadSubText(list, choose);
            if (choose == 1458) {
                data.add(Enemy.create(env, 1458, 131940000, 22740, 840, 1,
                        new Pair<Integer, Integer>(4, 1),
                        getMonsterTypes(env, 1458)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[0].title, text[0].description, 999)).addAction(new Attack(text[1].title, text[1].description, 100)).addPair(new ConditionAlways(), new LockAwoken(6)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 20), new MultipleAttack(text[3].title, text[3].description, 100, 3)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new ReduceTime(text[2].title, text[2].description, 5, -2000)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[4].title, text[4].description, 200)).addAction(new DarkScreen(0)).addPair(new ConditionAlways(), new ChangeAttribute(text[5].title, text[5].description, 1, 4, 5, 3, 0)));
                zero.attackMode.addAttack(new ConditionTurns(2), seq);

                RandomAttack ra = new RandomAttack();
                ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[6].title, text[6].description, 99)));
                ra.addAttack(new EnemyAttack().addAction(new Attack(text[7].title, text[7].description, 80)).addPair(new ConditionAlways(), new ColorChange(-1, 7)));
                ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[8].title, text[8].description, 60, 2)));
                ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[9].title, text[9].description, 20, 7)));
                zero.attackMode.addAttack(new ConditionHp(1, 20), ra);
            } else if (choose == 1602) {
                data = new ArrayList<Enemy>();
                data.add(Enemy.create(env, 1602, 78000000, 20130, 1140, 1,
                        new Pair<Integer, Integer>(4, 5),
                        getMonsterTypes(env, 1602)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[0].title,
                        text[0].description, 999)).addPair(new ConditionAlways(),
                        new DamageAbsorbShield(text[1].title,
                                text[1].description, 99, 4000000)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[2].title, text[2].description, 300))
                        .addPair(new ConditionHp(0, 100), new Transform(4, 6)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);


                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new SkillDown(text[3].title,
                                text[3].description, 0, 0, 2)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new Attack(text[4].title, text[4].description, 120))
                        .addPair(new ConditionAlways(), new LockOrb(0, 5)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new Attack(text[5].title, text[5].description, 80))
                        .addPair(new ConditionAlways(),
                                new LineChange(3, 4, 7, 5, 8, 6)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), rndAttack);
            } else if (choose == 2130) {
                data.add(Enemy.create(env, choose, 109900000, 21690, 150000, 1,
                        new Pair<Integer, Integer>(0, 1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(new DropLockAttack(text[2].title, text[2].description, 10, -1, 50))
                        .addAction(new RandomLineChange(2, 1, 8, 4, 10, 11, 14, 15))
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

        }
        mStageEnemies.put(++stageCounter, data); // 6

        data = new ArrayList<Enemy>();
        {
            int[] ids = {3000, 3001, 3002};
            int choose = RandomUtil.chooseOne(ids);
            text = loadSubText(list, choose);
            if (choose == 3000) {
                data.add(Enemy.create(env, choose, 57527047, 31418, 780, 1, new Pair<Integer, Integer>(1, -1), getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                zero.attackMode = new EnemyAttackMode();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[2].title, text[2].description, 160)).addPair(new ConditionAlways(), new SkillDown(text[1].title, text[1].description, 0, 0, 2)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ShieldAction(text[4].title, text[4].description, 7, -1, 50)).addPair(new ConditionHp(1, 50), new ResistanceShieldAction(text[3].title, text[3].description, 999)));
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[5].title, text[5].description, 160)).addAction(new ShieldAction(text[4].title, text[4].description, 7, -1, 50)).addPair(new ConditionHp(2, 50), new ResistanceShieldAction(text[3].title, text[3].description, 999)));
                zero.attackMode.addAttack(new ConditionAtTurn(1), seq);

                seq = new SequenceAttack();
                for (int i = 0; i < 7; ++i) {
                    seq.addAttack(new EnemyAttack().addAction(new RandomChange(text[6].title, text[6].description, 1, 3, 3, 3)).addPair(new ConditionUsed(), new Attack(100)));
                }
                seq.addAttack(new EnemyAttack().addAction(new RandomChange(text[7].title, text[7].description, 1, 9, 3, 9)).addPair(new ConditionAlways(), new Attack(300)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (choose == 3001) {
                data.add(Enemy.create(env, choose, 55196491, 31464, 780, 1, new Pair<Integer, Integer>(4, -1), getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                zero.attackMode = new EnemyAttackMode();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[2].title, text[2].description, -1, 7, -1, 7, -1, 7)).addPair(new ConditionAlways(), new SkillDown(text[1].title, text[1].description, 0, 0, 2)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 20), new Attack(text[7].title, text[7].description, 400)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[5].title, text[5].description, 140)).addAction(new ColorChange(-1, 2)).addPair(new ConditionHp(1, 50), new ResistanceShieldAction(text[4].title, text[4].description, 999)));
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[6].title, text[6].description, 50)).addAction(new Transform(1, 4, 5, 3, 0, 7)).addAction(new Attack(text[5].title, text[5].description, 140)).addAction(new ColorChange(-1, 2)).addPair(new ConditionHp(2, 50), new ResistanceShieldAction(text[4].title, text[4].description, 999)));
                zero.attackMode.addAttack(new ConditionAtTurn(1), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(new RandomChangeExcept(text[6].title, text[6].description, 4, 3, 7, 2, 3, 7)).addPair(new ConditionAlways(), new Attack(30)));
                rndAttack.addAttack(new EnemyAttack().addAction(new RandomChangeExcept(text[7].title, text[7].description, 4, 3, 2, 7, 3, 2)).addPair(new ConditionAlways(), new Attack(160)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), rndAttack);
            } else if (choose == 3002) {
                data.add(Enemy.create(env, choose, 54683769, 31556, 780, 1, new Pair<Integer, Integer>(5, -1), getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                zero.attackMode = new EnemyAttackMode();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new LockOrb(text[2].title, text[2].description, 0, 5, 2)).addAction(new Attack(80)).addPair(new ConditionAlways(), new SkillDown(text[1].title, text[1].description, 0, 0, 2)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[4].title, text[4].description, 60)).addAction(new Transform(7)).addPair(new ConditionHp(1, 50), new ResistanceShieldAction(text[3].title, text[3].description, 999)));
                seq.addAttack(new EnemyAttack().addAction(new ShieldAction(text[5].title, text[5].description, 1, -1, 75)).addAction(new Attack(text[4].title, text[4].description, 60)).addAction(new Transform(7)).addPair(new ConditionHp(2, 50), new ResistanceShieldAction(text[3].title, text[3].description, 999)));
                zero.attackMode.addAttack(new ConditionAtTurn(1), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ShieldAction(text[5].title, text[5].description, 1, -1, 75)));
                seq.addAttack(new EnemyAttack().addAction(new Transform(text[10].title, text[10].description, 5, 7, 6)).addPair(new ConditionAlways(), new Attack(200)));
                seq.addAttack(new EnemyAttack().addAction(new LockOrb(text[11].title, text[11].description, 1, 18, 1, 4, 5, 3, 0, 2, 6, 7, 8)).addPair(new ConditionAlways(), new Attack(80)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new RandomChange(text[6].title, text[6].description, 5, 3, 2, 3)).addPair(new ConditionAlways(), new Attack(140)));
                seq.addAttack(new EnemyAttack().addAction(new LockOrb(text[2].title, text[2].description, 0, 5, 2)).addPair(new ConditionAlways(), new Attack(80)));
                seq.addAttack(new EnemyAttack().addAction(new Transform(text[8].title, text[8].description, 7)).addPair(new ConditionAlways(), new Attack(60)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 7

        data = new ArrayList<Enemy>();
        {
            for (int i = 0; i < 3; ++i) {
                int rnd = RandomUtil.getInt(5);
                int choose = 3726 + rnd;
                text = loadSubText(list, choose);
                if (choose == 3726) {
                    data.add(Enemy.create(env, choose, 10500000, 49780, 140, 1, new Pair<Integer, Integer>(1, -1), getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    int turns = RandomUtil.range(2, 6);
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new AbsorbShieldAction(text[0].title, text[0].description, turns, 1)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.ALIVE_ONLY.ordinal(), 0), new Angry(text[2].title, text[2].description, 999, 150)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockOrb(text[1].title, text[1].description, 0, 1)).addPair(new ConditionUsed(), new Attack(50)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new PositionChange(text[3].title, text[3].description, 9, 0, 1, 0, 1, 2, 5, 0, 5, 2)).addPair(new ConditionAlways(), new Attack(80)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[4].title, text[4].description, 50, 2)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Daze(text[5].title, text[5].description, 0)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);

                } else if (choose == 3727) {
                    data.add(Enemy.create(env, choose, 10650000, 48880, 140, 1, new Pair<Integer, Integer>(4, -1), getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    int turns = RandomUtil.range(2, 6);
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new AbsorbShieldAction(text[0].title, text[0].description, turns, 4)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.ALIVE_ONLY.ordinal(), 0), new Angry(text[2].title, text[2].description, 999, 150)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockOrb(text[1].title, text[1].description, 0, 4)).addPair(new ConditionUsed(), new Attack(50)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[3].title, text[3].description, 80)).addPair(new ConditionAlways(), new BindPets(3, 1, 1, 1)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[4].title, text[4].description, 50, 2)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Daze(text[5].title, text[5].description, 0)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (choose == 3728) {
                    data.add(Enemy.create(env, choose, 10530000, 49330, 140, 1, new Pair<Integer, Integer>(5, -1), getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    int turns = RandomUtil.range(2, 6);
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new AbsorbShieldAction(text[0].title, text[0].description, turns, 5)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.ALIVE_ONLY.ordinal(), 0), new Angry(text[2].title, text[2].description, 999, 150)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockOrb(text[1].title, text[1].description, 0, 5)).addPair(new ConditionUsed(), new Attack(50)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new SwitchLeader(text[3].title, text[3].description, 1)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.LEADER_SWITCH.ordinal()), new Attack(80)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[4].title, text[4].description, 50, 2)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Daze(text[5].title, text[5].description, 0)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (choose == 3729) {
                    data.add(Enemy.create(env, choose, 10570000, 49090, 140, 1, new Pair<Integer, Integer>(3, -1), getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    int turns = RandomUtil.range(2, 6);
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new AbsorbShieldAction(text[0].title, text[0].description, turns, 3)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.ALIVE_ONLY.ordinal(), 0), new Angry(text[2].title, text[2].description, 999, 150)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockOrb(text[1].title, text[1].description, 0, 3)).addPair(new ConditionUsed(), new Attack(50)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[3].title, text[3].description, 80)).addPair(new ConditionAlways(), new ShieldAction(2, -1, 35)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[4].title, text[4].description, 50, 2)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Daze(text[5].title, text[5].description, 0)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (choose == 3730) {
                    data.add(Enemy.create(env, choose, 10500000, 49540, 140, 1, new Pair<Integer, Integer>(0, -1), getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    int turns = RandomUtil.range(2, 6);
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new AbsorbShieldAction(text[0].title, text[0].description, turns, 0)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.ALIVE_ONLY.ordinal(), 0), new Angry(text[2].title, text[2].description, 999, 150)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockOrb(text[1].title, text[1].description, 0, 0)).addPair(new ConditionUsed(), new Attack(50)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[3].title, text[3].description, -1, 7, -1, 7)).addPair(new ConditionAlways(), new Attack(80)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[4].title, text[4].description, 50, 2)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Daze(text[5].title, text[5].description, 0)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                }
            }
        }
        mStageEnemies.put(++stageCounter, data); // 8


        data = new ArrayList<Enemy>();
        {
            int[] ids = {1206, 1207};
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            text = loadSubText(list, choose);
            if (choose == ids[0]) {
                data = new ArrayList<Enemy>();
                data.add(Enemy.create(env, 1206, 18029303, 30116, 2680, 1,
                        new Pair<Integer, Integer>(5, 3),
                        getMonsterTypes(env, 1206)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 5, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(new LockOrb(text[2].title,
                                text[2].description, 0, 1, 4, 5, 3, 0, 2, 6, 7, 8))
                        .addPair(new ConditionAlways(),
                                new ResistanceShieldAction(text[1].title,
                                        text[1].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Angry(text[8].title, text[8].description, 999,
                                200)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(text[9].title, text[9].description, 200)));
                zero.attackMode.addAttack(new ConditionHp(2, 25), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[3].title, text[3].description, 125))
                        .addPair(new ConditionAlways(), new ColorChange(-1, 7)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[4].title,
                                text[4].description, 35, 5)));
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[5].title, text[5].description, 100))
                        .addPair(new ConditionAlways(),
                                new BindPets(2, 2, 3, 1)));
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[6].title, text[6].description, 150))
                        .addPair(new ConditionAlways(), new ColorChange(-1, 1)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Gravity(text[7].title, text[7].description, 99)));
                zero.attackMode.addAttack(new ConditionHp(1, 25), seq);
            } else if (choose == ids[1]) {
                data = new ArrayList<Enemy>();
                data.add(Enemy.create(env, choose, 20129303, 27004, 2680, 1,
                        new Pair<Integer, Integer>(0, 1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 1, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(new RecoveryUpAction(text[2].title,
                                text[2].description, 4, 50))
                        .addPair(new ConditionAlways(),
                                new ResistanceShieldAction(text[1].title,
                                        text[1].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Daze(text[8].title, text[8].description, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[9].title,
                                text[9].description, 300, 3)));
                zero.attackMode.addAttack(new ConditionHp(2, 25), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[3].title, text[3].description, 100))
                        .addPair(new ConditionAlways(), new RandomChange(6, 6)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[4].title,
                                text[4].description, 25, 6, 7)));
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[5].title, text[5].description, 125))
                        .addPair(new ConditionAlways(), new DarkScreen(0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[6].title,
                                text[6].description, 100, 2)));
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[7].title, text[7].description, 75))
                        .addPair(new ConditionAlways(),
                                new BindPets(2, 2, 2, 4)));
                zero.attackMode.addAttack(new ConditionHp(1, 25), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data); // ++stageCounter 9

        // ok, text not ok
        data = new ArrayList<Enemy>();
        {
            for (int i = 0; i < 2; ++i) {
                int rnd = RandomUtil.getInt(5);
                int choose = 310 + rnd;
                text = loadSubText(list, choose);
                if (choose == 310) {
                    data.add(Enemy.create(env, choose, 51420000, 55500, 300, 3, new Pair<Integer, Integer>(1, 4), getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[1].title, text[1].description, 30, 2)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 50), new Attack(text[2].title, text[2].description, 150)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Daze(text[3].title, text[3].description, 0)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(200)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (choose == 311) {
                    data.add(Enemy.create(env, choose, 51420000, 53500, 300, 3, new Pair<Integer, Integer>(1, 5), getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[1].title, text[1].description, 30, 2)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 50), new Attack(text[2].title, text[2].description, 150)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Daze(text[3].title, text[3].description, 0)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(200)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (choose == 312) {
                    data.add(Enemy.create(env, choose, 51450000, 56000, 0, 4, new Pair<Integer, Integer>(4, 1), getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[1].title, text[1].description, 30, 2)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 50), new Attack(text[2].title, text[2].description, 150)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new BindPets(text[3].title, text[3].description, 3, 10, 10, 1)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
                    zero.attackMode.addAttack(new ConditionHp(1, 50), seq);
                } else if (choose == 313) {
                    data.add(Enemy.create(env, choose, 51450000, 56400, 0, 4, new Pair<Integer, Integer>(4, 5), getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new CloudAttack(text[1].title, text[1].description, 3, 3, 3)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 50), new Attack(text[2].title, text[2].description, 150)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new BindPets(text[3].title, text[3].description, 3, 10, 10, 1)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
                    zero.attackMode.addAttack(new ConditionHp(1, 50), seq);
                } else if (choose == 314) {
                    data.add(Enemy.create(env, choose, 51410000, 52800, 1500, 3, new Pair<Integer, Integer>(5, 1), getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[1].title, text[1].description, 30, 2)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 50), new Attack(text[2].title, text[2].description, 150)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new BindPets(text[3].title, text[3].description, 5, 2, 4, 1, 4)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (choose == 315) {
                    data.add(Enemy.create(env, choose, 51400000, 52800, 1500, 3, new Pair<Integer, Integer>(5, 4), getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[1].title, text[1].description, 30, 2)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 50), new Attack(text[2].title, text[2].description, 150)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new BindPets(text[3].title, text[3].description, 5, 2, 4, 1, 4)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                }

            }
        }
        mStageEnemies.put(++stageCounter, data);// 10 ++stageCounter

        data = new ArrayList<Enemy>();
        {
            text = loadSubText(list, 1294);
            data.add(Enemy.create(env, 1294, 12000000, 20556, 100000, 1, new Pair<Integer, Integer>(4, -1), getMonsterTypes(env, 1294)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ComboShield(text[0].title, text[0].description, 999, 6)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.ALIVE_ONLY.ordinal(), 0), new Angry(text[1].title, text[1].description, 999, 1000)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);


            text = loadSubText(list, 1295);
            data.add(Enemy.create(env, 1295, 525, 19556, 15000000, 1, new Pair<Integer, Integer>(5, -1), getMonsterTypes(env, 1295)));
            zero = data.get(1);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Gravity(text[0].title, text[0].description, 99)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.ALIVE_ONLY.ordinal(), 0), new Angry(text[1].title, text[1].description, 999, 1000)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);
        }
        mStageEnemies.put(++stageCounter, data);// 11

        data = new ArrayList<Enemy>();
        {
            int[] ids = {1463, 1465, 1837, 2008};
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            text = loadSubText(list, choose);
            if (choose == ids[0]) { // this is 1663
                data.add(Enemy.create(env, choose, 2599002, 34886, 0, 1,
                        new Pair<Integer, Integer>(3, 0),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new DamageAbsorbShield(text[0].title,
                                text[0].description, 99, 200000)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Daze(text[1].title, text[1].description)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Daze("2", "")));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Daze("1", "")));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[2].title,
                                text[2].description, 200, 7)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (choose == ids[1]) {
                data.add(Enemy.create(env, choose, 26417469, 33128, 1072, 1,
                        new Pair<Integer, Integer>(0, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 80);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new SkillDown(text[0].title, text[0].description,
                                0, 5, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[4].title,
                                text[4].description, 50, 4)));
                zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[1].title, text[1].description,
                                -1, 7)).addPair(new ConditionAlways(),
                        new Attack(50)));
                seq.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[2].title,
                                text[2].description, 6, 4)).addPair(
                        new ConditionAlways(), new Attack(50)));
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[3].title, text[3].description, 2,
                                4, 4, 1)).addPair(new ConditionAlways(),
                        new Attack(50)));
                zero.attackMode.addAttack(new ConditionHp(1, 20), seq);
            } else if (choose == ids[2]) {
                data.add(Enemy.create(env, choose, 70292441, 34541, 752, 1,
                        new Pair<Integer, Integer>(5, 1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(new LockOrb(text[1].title,
                                text[1].description, 1, 8, 1, 4, 5, 3, 0, 2, 6, 7, 8))
                        .addAction(new Attack(100))
                        .addPair(new ConditionIf(1, 1,
                                        0, EnemyCondition.Type.LEADER_SWITCH.ordinal()),
                                new SwitchLeader(text[0].title,
                                        text[0].description, 0)));
                seq.addAttack(new EnemyAttack()
                        .addAction(new LockOrb(text[1].title,
                                text[1].description, 1, 8, 1, 4, 5, 3, 0, 2, 6, 7, 8))
                        .addAction(new Attack(100))
                        .addPair(new ConditionIf(1, 1,
                                        1, EnemyCondition.Type.LEADER_SWITCH.ordinal()),
                                new Attack(100)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new ComboShield(text[2].title, text[2].description,
                                99, 4)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 30),
                        new MultipleAttack(text[8].title,
                                text[8].description, 80, 3)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[3].title, text[3].description,
                                2, 7)).addPair(new ConditionTurns(3),
                        new Attack(25)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[4].title, text[4].description,
                                -1, 0)).addPair(new ConditionAlways(),
                        new Attack(100)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new Gravity(text[5].title,
                                text[5].description, 99)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new DarkScreen(text[6].title, text[6].description,
                                0)).addPair(new ConditionAlways(),
                        new Attack(75)));
                rndAttack.addAttack(new EnemyAttack().addPair(new ConditionIf(
                                1, 1, 0, EnemyCondition.Type.LEADER_SWITCH.ordinal()),
                        new SwitchLeader(text[7].title,
                                text[7].description, 3)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
            } else if (choose == ids[3]) {
                data.add(Enemy.create(env, choose, 59610384, 29548, 736, 1,
                        new Pair<Integer, Integer>(1, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ComboShield(text[0].title, text[0].description,
                                5, 7)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 80),
                        new SkillDown(text[2].title, text[2].description,
                                0, 2, 2)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                        new SwitchLeader(text[1].title,
                                text[1].description, 2)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 10),
                        new MultipleAttack(text[4].title,
                                text[4].description, 100, 12)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[3].title, text[3].description,
                                -1, 7)).addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 10), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 12

        // ok
        data = new ArrayList<Enemy>();
        {
            int[] ids = {2738, 2180, 2664, 2006, 2391};
            int choose = RandomUtil.chooseOne(ids);
            text = loadSubText(list, choose);
            if (choose == 2738) {
                data.add(Enemy.create(env, choose, 800000, 1, 40000000, 3, new Pair<Integer, Integer>(5, -1), getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
                zero.attackMode = new EnemyAttackMode();

                RandomAttack rnd = new RandomAttack();
                rnd.addAttack(new EnemyAttack().addAction(new LockRemoveAttack(text[2].title, text[2].description, 3, 4)).addPair(new ConditionAlways(), new ChangeAttribute(text[1].title, text[1].description, 1)));
                rnd.addAttack(new EnemyAttack().addAction(new LockRemoveAttack(text[4].title, text[4].description, 3, 5)).addPair(new ConditionAlways(), new ChangeAttribute(text[3].title, text[3].description, 4)));
                rnd.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new LockRemoveAttack(text[5].title, text[5].description, 3, 1)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), rnd);

                rnd = new RandomAttack();
                rnd.addAttack(new EnemyAttack().addAction(new Gravity(text[7].title, text[7].description, 300)).addPair(new ConditionAlways(), new ChangeAttribute(text[6].title, text[6].description, 1)));
                rnd.addAttack(new EnemyAttack().addAction(new Gravity(text[7].title, text[7].description, 300)).addPair(new ConditionAlways(), new ChangeAttribute(text[6].title, text[6].description, 4)));
                rnd.addAttack(new EnemyAttack().addAction(new Gravity(text[7].title, text[7].description, 300)).addPair(new ConditionAlways(), new ChangeAttribute(text[6].title, text[6].description, 5)));
                zero.attackMode.addAttack(new ConditionAlways(), rnd);
            } else if (choose == 2180) {
                data.add(Enemy.create(env, choose, 27686964, 24027, 1248, 1, new Pair<Integer, Integer>(0, 1), getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 1, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 88);
                zero.attackMode = new EnemyAttackMode();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ReduceTime(text[4].title, text[4].description, 10, 500, 0)).addAction(new ShieldAction(text[3].title, text[3].description, 3, -1, 50)).addPair(new ConditionAlways(), new ResistanceShieldAction(text[2].title, text[2].description, 10)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Angry(text[8].title, text[8].description, 999, 400)).addAction(new SuperDark(text[7].title, text[7].description, 4, 1, 0, 0)).addAction(new ComboShield(text[6].title, text[6].description, 999, 7)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 1), new RecoverSelf(text[5].title, text[5].description, 4)));
                seq.addAttack(new EnemyAttack().addAction(new Angry(text[8].title, text[8].description, 999, 400)).addAction(new SuperDark(text[7].title, text[7].description, 4, 1, 0, 0)).addPair(new ConditionUsedIf(1, 1, 1, EnemyCondition.Type.ENEMY_BUFF.ordinal(), BuffOnEnemySkill.Type.COMBO_SHIELD.ordinal()), new ComboShield(text[6].title, text[6].description, 999, 7)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 5), new MultipleAttack(text[9].title, text[9].description, 30, 5)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                FromHeadAttack head = new FromHeadAttack();
                head.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1, 1, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 0, 0), new ReduceTime(text[16].title, text[16].description, 10, 50, 0)));
                head.addAttack(new EnemyAttack().addAction(new RandomChange(text[11].title, text[11].description, 6, 11)).addPair(new ConditionAlways(), new LockOrb(text[12].title, text[12].description, 0, 1, 4, 5, 3, 0, 2, 6, 7, 8)));
                zero.attackMode.addAttack(new ConditionModeSelection(3, 0), head);

                head = new FromHeadAttack();
                head.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1, 1, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 0, 0), new ReduceTime(text[16].title, text[16].description, 10, 50, 0)));
                head.addAttack(new EnemyAttack().addAction(new ColorChange(text[14].title, text[14].description, 2, 7)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB.ordinal(), 2), new Attack(100)));
                head.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[15].title, text[15].description, 30, 5)));
                zero.attackMode.addAttack(new ConditionModeSelection(3, 1), head);

                head = new FromHeadAttack();
                head.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1, 1, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 0, 0), new ReduceTime(text[16].title, text[16].description, 10, 50, 0)));
                head.addAttack(new EnemyAttack().addAction(new Gravity(text[18].title, text[18].description, 100)).addPair(new ConditionAlways(), new ShieldAction(text[17].title, text[17].description, 3, -1, 50)));
                zero.attackMode.addAttack(new ConditionModeSelection(3, 2), head);
            } else if (choose == 2664) {
                data.add(Enemy.create(env, choose, 21739187, 23037, 1248, 1, new Pair<Integer, Integer>(3, 4), getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[1].title, text[1].description, 10))
                        .addAction(new ShieldAction(text[2].title, text[2].description, 1, -1, 75))
                        .addPair(new ConditionAlways(), new BindPets(text[3].title, text[3].description, 3, 1, 3, 6)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new LockSkill(text[5].title, text[5].description, 10)).addPair(new ConditionUsed(), new ColorChange(text[4].title, text[4].description, -1, 7, -1, 7, -1, 7)));
                seq.addAttack(new EnemyAttack().addAction(new LockAwoken(text[7].title, text[7].description, 1)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new ShieldAction(text[6].title, text[6].description, 1, -1, 75)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 1, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 1, 0), new LockSkill(text[8].title, text[8].description, 10)));
                seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[4].title, text[4].description, -1, 7, -1, 7, -1, 7)).addPair(new ConditionUsed(), new LockOrb(text[9].title, text[9].description, 0, 1, 4, 5, 3, 0, 2, 6, 7, 8)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 30), new MultipleAttack(text[10].title, text[10].description, 100, 5)));
                seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[11].title, text[11].description, 7, 2)).addPair(new ConditionFindOrb(7), new Attack(360)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[11].title, text[11].description, 7, 2)).addPair(new ConditionFindOrb(7), new Attack(360)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[12].title, text[12].description, -1, 7)).addAction(new Attack(100)).addPair(new ConditionAlways(), new PositionChange(text[13].title, text[13].description, 9, 0, 1, 0, 0, 0, 2, 0, 3, 0)));
                seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[4].title, text[4].description, -1, 7, -1, 7, -1, 7)).addPair(new ConditionAlways(), new ShieldAction(text[14].title, text[14].description, 1, -1, 75)));
                seq.addAttack(new EnemyAttack().addAction(new SkillDown(text[16].title, text[16].description, 0, 0, 4)).addAction(new ColorChange(text[15].title, text[15].description, -1, 7)).addPair(new ConditionAlways(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), seq);
            } else if (choose == 2006) {
                data.add(Enemy.create(env, choose, 17207420, 37227, 1008, 1, new Pair<Integer, Integer>(1, 3), getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[2].title, text[2].description, 130, 3)).addPair(new ConditionAlways(), new ResistanceShieldAction(text[1].title, text[1].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 1), new RecoverSelf(text[3].title, text[3].description, 100)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[8].title, text[8].description, 300)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[4].title, text[4].description, 200)));
                zero.attackMode.addAttack(new ConditionHp(1, 80), seq);

                RandomAttack rnd = new RandomAttack();
                rnd.addAttack(new EnemyAttack().addAction(new DarkScreen(text[5].title, text[5].description, 0)).addPair(new ConditionAlways(), new Attack(130)));
                rnd.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
                rnd.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[6].title, text[6].description, 30, 2, 6)));
                rnd.addAttack(new EnemyAttack().addPair(new ConditionTurns(2), new BindPets(text[7].title, text[7].description, 3, 2, 4, 3)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rnd);
            } else if (choose == 2391) {
                data.add(Enemy.create(env, 2391, 73500000, 39974, 672, 2,
                        new Pair<Integer, Integer>(4, 3),
                        getMonsterTypes(env, 2391)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 76);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new RecoveryUpAction(text[1].title, text[1].description, 6, 50)).addAction(new ShieldAction(text[2].title, text[2].description, 6, -1, 75)).addPair(new ConditionIf(1, 1, 1, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 3, 0), new Gravity(text[3].title, text[3].description, 99)));
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[1].title, text[1].description, 100)).addAction(new ShieldAction(text[2].title, text[2].description, 6, -1, 75)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 3, 0), new Gravity(text[3].title, text[3].description, 99)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[4].title, text[4].description, 999)).addAction(new Attack(100)).addAction(new Transform(text[5].title, text[5].description, 4)).addPair(new ConditionUsed(), new LineChange(text[6].title, text[6].description, 1, 3)));
                seq.addAttack(new EnemyAttack().addAction(new Transform(text[5].title, text[5].description, 4)).addAction(new Attack(100)).addPair(new ConditionUsed(), new DropRateAttack(text[7].title, text[7].description, 3, 7, 20)));
                seq.addAttack(new EnemyAttack().addAction(new Transform(text[5].title, text[5].description, 4)).addAction(new Attack(100)).addPair(new ConditionUsed(), new DropLockAttack(text[8].title, text[8].description, 1, -1, 100)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[9].title, text[9].description, 1000, 2)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 13 ++stageCounter

        // not ok
        data = new ArrayList<Enemy>();
        {
            int[] ids = {2939, 2940, 2941};
            int choose = RandomUtil.chooseOne(ids);
            text = loadSubText(list, choose);
            if (choose == 2939) {
                data.add(Enemy.create(env, choose, 35410000, 22210, 100000, 1, new Pair<Integer, Integer>(1, 5), getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 1, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 5, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[3].title, text[3].description, 999)).addAction(new AbsorbShieldAction(text[2].title, text[2].description, 5, 5)).addPair(new ConditionAlways(), new AbsorbShieldAction(5, 1)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new LineChange(text[4].title, text[4].description, 3, 1, 4, 5, 7, 5, 8, 1, 9, 5, 10, 1)).addPair(new ConditionHp(2, 15), new Attack(750)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack(); // check at turn 1 can work or not
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 50), new ShieldAction(text[5].title, text[5].description, 3, -1, 75)));
                zero.attackMode.addAttack(new ConditionAtTurn(1), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 1, EnemyCondition.Type.ENEMY_BUFF.ordinal(), 5), new ShieldAction(text[6].title, text[6].description, 3, -1, 50)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[7].title, text[7].description, 99)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SuperDark(text[8].title, text[8].description, 3, 2, 1, 0)));
                seq.addAttack(new EnemyAttack().addAction(new SkillDown(text[9].title, text[9].description, 0, 1, 1)).addPair(new ConditionAlways(), new Attack(150)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[10].title, text[10].description, 35, 5)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (choose == 2940) {
                data.add(Enemy.create(env, choose, 82690000, 20260, 2140, 1, new Pair<Integer, Integer>(4, 3), getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[1].title, text[1].description, 20, 6)).addPair(new ConditionAlways(), new SkillDown(text[0].title, text[0].description, 0, 5, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new LineChange(8, 4, 9, 4)).addPair(new ConditionHp(2, 15), new Attack(text[4].title, text[4].description, 750)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[3].title, text[3].description, 2, 250)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(2, 70), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Gravity(text[4].title, text[4].description, 99)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                RandomAttack rnd = new RandomAttack();
                rnd.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[5].title, text[5].description, 50, 3)));
                rnd.addAttack(new EnemyAttack().addAction(new DarkScreen(text[6].title, text[6].description, 0)).addPair(new ConditionAlways(), new Attack(130)));
                rnd.addAttack(new EnemyAttack().addAction(new ReduceTime(text[7].title, text[7].description, 1, 500, 0)).addPair(new ConditionAlways(), new Attack(140)));
                rnd.addAttack(new EnemyAttack().addAction(new RandomChange(text[8].title, text[8].description, 4, 3)).addPair(new ConditionAlways(), new Attack(130)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), rnd);
            } else if (choose == 2941) {
                data.add(Enemy.create(env, choose, 58140000, 21220, 2140, 1, new Pair<Integer, Integer>(5, 4), getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new LockSkill(text[0].title, text[0].description, 5)).addPair(new ConditionAlways(), new LockAwoken(text[1].title, text[1].description, 6)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 20), new MultipleAttack(text[2].title, text[2].description, 150, 5)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(0, 100), new SkillDown(text[3].title, text[3].description, 0, 2, 2)));
                seq.addAttack(new EnemyAttack().addAction(new ShieldAction(text[5].title, text[5].description, 1, -1, 99)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new ChangeAttribute(text[4].title, text[4].description, 0)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(new DarkScreen(text[6].title, text[6].description, 0)).addAction(new Attack(100))
                        .addAction(new RandomChange(text[7].title, text[7].description, 6, 5)).addAction(new Attack(100))
                        .addAction(new Attack(text[8].title, text[8].description, 100)).addPair(new ConditionModeSelection(4, 0, 3), new BindPets(3, 3, 3, 3)));
                seq.addAttack(new EnemyAttack()
                        .addAction(new RandomChange(text[7].title, text[7].description, 6, 5)).addAction(new Attack(100))
                        .addAction(new Attack(text[8].title, text[8].description, 100)).addPair(new ConditionModeSelection(4, 1, 3), new BindPets(3, 3, 3, 3)));
                seq.addAttack(new EnemyAttack().addAction(new Attack(text[8].title, text[8].description, 100)).addPair(new ConditionModeSelection(4, 2, 3), new BindPets(3, 3, 3, 3)));
                zero.attackMode.addAttack(new ConditionHp(2, 40), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(new DarkScreen(text[6].title, text[6].description, 0)).addAction(new Attack(100))
                        .addAction(new RandomChange(text[7].title, text[7].description, 6, 5)).addPair(new ConditionModeSelection(4, 0, 2), new Attack(100)));
                seq.addAttack(new EnemyAttack()
                        .addAction(new RandomChange(text[7].title, text[7].description, 6, 5)).addPair(new ConditionModeSelection(4, 1, 2), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(2, 60), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new DarkScreen(text[6].title, text[6].description, 0)).addPair(new ConditionModeSelection(4, 0, 1), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(2, 80), seq);

                RandomAttack rnd = new RandomAttack();
                rnd.addAttack(new EnemyAttack().addAction(new LineChange(text[9].title, text[9].description, 3, 4)).addPair(new ConditionAlways(), new Attack(140)));
                rnd.addAttack(new EnemyAttack().addAction(new LineChange(text[10].title, text[10].description, 4, 5)).addPair(new ConditionAlways(), new Attack(140)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), rnd);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 14 ++stageCounter

        //for(int x = 0; x<4; ++x)
        {
            data = new ArrayList<Enemy>();
            {
                int[] ids = {1955, 1726, 1956, 1954};
                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == ids[0]) {
                    data.add(Enemy.create(env, choose, 100000000, 11111, 100000000,
                            1, new Pair<Integer, Integer>(4, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(0);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addAction(new Transform(text[2].title, text[2].description, 1, 4, 5, 3, 0, 2))
                            .addAction(new LockAwoken(text[0].title, text[0].description, 6))
                            .addPair(
                                    new ConditionAlways(),
                                    new LockSkill(text[1].title,
                                            text[1].description, 6)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addAction(
                                    new Attack(text[3].title,
                                            text[3].description, 90000)).addPair(
                                    new ConditionIf(1, 1, 0,
                                            EnemyCondition.Type.FIND_ORB.ordinal(),
                                            7), new ColorChange(7, 4)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new RandomLineChange(text[4].title, text[4].description, 3, 4, 2, 7, 2, 0, 4)).addPair(new ConditionAlways(), new LineChange(1, 7)));
                    seq.addAttack(new EnemyAttack().addAction(new RandomLineChange(text[5].title, text[5].description, 3, 4, 2, 7, 3, 10, 13, 15)).addPair(new ConditionAlways(), new LineChange(8, 7)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new RandomChange(text[6].title, text[6].description, 7, 13)));
                    seq.addAttack(new EnemyAttack().addAction(new SuperDark(text[7].title, text[7].description, 1, -1)).addPair(new ConditionAlways(), new RandomChange(text[8].title, text[8].description, 7, 16)));
                    seq.addAttack(new EnemyAttack().addAction(new Daze(text[11].title, text[11].description, 0)).addAction(new RandomChange(text[10].title, text[10].description, 7, 4)).addPair(new ConditionAlways(), new ReduceTime(text[9].title, text[9].description, 1, -2000)));

                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                            new RecoverSelf(text[12].title, text[12].description,
                                    -100)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);

                } else if (choose == ids[1]) {
                    data.add(Enemy.create(env, choose, 7910093, 20032, 504, 1, new Pair<Integer, Integer>(1, 0), getMonsterTypes(env, choose)));
                    zero = data.get(0);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                            new SkillDown(text[1].title, text[1].description,
                                    0, 0, 7)));
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
                } else if (choose == ids[3]) {
                    data.add(Enemy.create(env, choose, 29408982, 22775, 0, 1, new Pair<Integer, Integer>(1, -1), getMonsterTypes(env, choose)));
                    zero = data.get(0);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 1, 50);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addAction(
                                    new SkillDown(text[1].title,
                                            text[1].description, 0, 2, 5))
                            .addAction(
                                    new ComboShield(text[2].title,
                                            text[2].description, 99, 5))
                            .addPair(
                                    new ConditionAlways(),
                                    new Gravity(text[3].title,
                                            text[3].description, 99)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addAction(
                                    new Gravity(text[9].title,
                                            text[9].description, 75))
                            .addAction(
                                    new Transform(text[10].title,
                                            text[10].description, 1))
                            .addPair(new ConditionHp(2, 30), new Attack(300)));
                    seq.addAttack(new EnemyAttack().addAction(
                            new ColorChange(text[4].title, text[4].description,
                                    6, 2)).addPair(
                            new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                                    .ordinal(), 6), new Attack(200)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    RandomAttack rndAttack = new RandomAttack();
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new LineChange(text[5].title, text[5].description,
                                    4, 1, 7, 1, 9, 1)).addPair(
                            new ConditionAlways(), new Attack(100)));
                    rndAttack
                            .addAttack(new EnemyAttack().addPair(
                                    new ConditionAlways(), new MultipleAttack(
                                            text[6].title, text[6].description,
                                            40, 3)));
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new RandomChange(text[7].title,
                                    text[7].description, 6, 7)).addPair(
                            new ConditionAlways(), new Attack(50)));
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new DarkScreen(text[8].title, text[8].description,
                                    0)).addPair(new ConditionAlways(),
                            new Attack(75)));
                    zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
                } else if (choose == ids[2]) {
                    data.add(Enemy.create(env, choose, 2570260, 21007, 0, 1, new Pair<Integer, Integer>(5, -1), getMonsterTypes(env, choose)));
                    zero = data.get(0);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 1);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                            new DamageAbsorbShield(text[1].title,
                                    text[1].description, 99, 300000)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new RandomChangeExcept(text[2].title,
                                    text[2].description, 7, 15, 2)).addPair(
                            new ConditionHp(2, 1),
                            new RecoverSelf(text[3].title, text[3].description,
                                    -2)));
                    seq.addAttack(new EnemyAttack().addAction(
                            new LineChange(text[7].title, text[7].description,
                                    0, 7)).addPair(new ConditionHp(2, 30),
                            new Attack(200)));
                    seq.addAttack(new EnemyAttack().addAction(
                            new LineChange(text[4].title, text[4].description,
                                    0, 2, 2, 2, 1, 5)).addPair(
                            new ConditionTurns(2), new Attack(100)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    RandomAttack rndAttack = new RandomAttack();
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new LineChange(text[5].title, text[5].description,
                                    3, 1, 8, 1, 10, 1)).addPair(
                            new ConditionAlways(), new Attack(100)));
                    rndAttack.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new MultipleAttack(
                                    text[6].title, text[6].description, 30, 3,
                                    4)));
                    zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
                }
            }
            mStageEnemies.put(++stageCounter, data); // 15 ++stageCounter
        }

//        for (int x = 0; x<3; ++x)
        {
            // ok
            data = new ArrayList<Enemy>();
            {
                int[] ids = {3829, 3832, 3881};
                int choose = RandomUtil.chooseOne(ids);
                text = loadSubText(list, choose);
                if (choose == ids[0]) {
                    data.add(Enemy.create(env, choose, 150000000, 12400, 400000, 1, new Pair<Integer, Integer>(1, 5), getMonsterTypes(env, choose)));
                    zero = data.get(0);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockAwoken(text[2].title, text[2].description, 4)).addPair(new ConditionAlways(), new ResistanceShieldAction(text[1].title, text[1].description, 999)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[4].title, text[4].description, 1000, 10)).addPair(new ConditionHp(2, 10), new ChangeAttribute(text[3].title, text[3].description, 1, 5)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[6].title, text[6].description, 999, 150)).addAction(new LineChange(text[5].title, text[5].description, 1, 8)).addPair(new ConditionUsed(), new RandomLineChange(2, 2, 6, 2, 0, 4)));
                    zero.attackMode.addAttack(new ConditionTurns(5), seq);

                    RandomAttack rnd = new RandomAttack();
                    rnd.addAttack(new EnemyAttack().addAction(new RandomChange(text[7].title, text[7].description, 29, 4)).addPair(new ConditionAlways(), new Attack(300)));
                    rnd.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[8].title, text[8].description, 300)));
                    zero.attackMode.addAttack(new ConditionHp(1, 10), rnd);
                } else if (choose == ids[1]) {
                    data.add(Enemy.create(env, choose, 300000000, 13250, 200000, 1, new Pair<Integer, Integer>(4, 5), getMonsterTypes(env, choose)));
                    zero = data.get(0);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[2].title, text[2].description, 5)).addAction(new Attack(text[1].title, text[1].description,400)).addAction(new LineChange(1, 8)).addPair(new ConditionAlways(), new RandomLineChange( 6, 1, 4, 5, 3, 0, 2, 4, 0, 1, 3, 4)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new RecoverSelf(text[4].title, text[4].description, 15)).addPair(new ConditionUsedIf(1,1,0,EnemyCondition.Type.TURN.ordinal(), 5), new DamageVoidShield(text[3].title, text[3].description, 99, 10000000)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 5), new MultipleAttack(text[5].title, text[5].description, 100, 10)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    RandomAttack rnd = new RandomAttack();
                    rnd.addAttack(new EnemyAttack().addAction(new SuperDark(text[8].title, text[8].description, 1, 1, 4, 99)).addPair(new ConditionAlways(), new Attack(220)));
                    rnd.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[10].title, text[10].description, 120, 2)));
                    rnd.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[11].title, text[11].description, 65, 4)));
                    zero.attackMode.addAttack(new ConditionHp(2, 33), rnd);

                    rnd = new RandomAttack();
                    rnd.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[6].title, text[6].description, 120, 2)));
                    rnd.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[7].title, text[7].description, 65, 4)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), rnd);
                } else if (choose == ids[2]) {
                    data.add(Enemy.create(env, choose, 200000000, 12140, 5000000, 1, new Pair<Integer, Integer>(3, 0), getMonsterTypes(env, choose)));
                    zero = data.get(0);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[2].title, text[2].description, 5)).addAction(new AbsorbShieldAction(text[1].title, text[1].description, 5, 0)).addPair(new ConditionAlways(), new AbsorbShieldAction(5, 3)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,1,0,EnemyCondition.Type.ENEMY_DEBUFF.ordinal(), 0, 0), new Angry(text[4].title, text[4].description, 999, 200)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 5), new MultipleAttack(text[3].title, text[3].description, 1000, 5)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ShieldAction(text[6].title, text[6].description, 7, -1, 75)).addPair(new ConditionAlways(), new RecoveryUpAction(text[5].title, text[5].description, 7, 50)));
                    zero.attackMode.addAttack(new ConditionAtTurn(6), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockAwoken(text[7].title, text[7].description, 1)).addPair(new ConditionAlways(), new MultipleAttack(text[8].title, text[8].description, 1000, 7)));
                    zero.attackMode.addAttack(new ConditionAtTurn(13), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[9].title, text[9].description,270)).addPair(new ConditionAlways(), new SkillDown( 0, 0, 1)));
                    seq.addAttack(new EnemyAttack().addAction(new LockOrb(text[10].title, text[10].description, 1, 6,1,4,5,3,0,2,6,7,8)).addPair(new ConditionAlways(), new Attack(270)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[11].title, text[11].description, 300)));
                    zero.attackMode.addAttack(new ConditionHp(1, 5), seq);

                }
            }
            mStageEnemies.put(++stageCounter, data); // 16 ++stageCounter
        }

//        for(int x = 0; x<5; ++x)
        {
            // ok
            data = new ArrayList<Enemy>();
            {
                int[] ids = {2092, 2277, 2754, 3013, 3245};
                int choose = RandomUtil.chooseOne(ids);
                text = loadSubText(list, choose);
                if (choose == ids[0]) {
                    data.add(Enemy.create(env, 2092, 75310000, 38850, 1740, 1,
                            new Pair<Integer, Integer>(1, 3),
                            getMonsterTypes(env, 2092)));
                    zero = data.get(0);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 1, 50);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new ResistanceShieldAction(text[1].title,
                                    text[1].description, 5))
                            .addPair(
                                    new ConditionAlways(),
                                    new Attack(text[2].title,
                                            text[2].description, 200)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new ComboShield(text[3].title, text[3].description,
                                    5, 5)).addPair(
                            new ConditionUsed(),
                            new MultipleAttack(text[4].title,
                                    text[4].description, 40, 3)));
                    seq.addAttack(new EnemyAttack().addAction(
                            new DamageAbsorbShield(text[5].title,
                                    text[5].description, 5, 5000000)).addPair(
                            new ConditionUsed(),
                            new MultipleAttack(text[6].title,
                                    text[6].description, 20, 6)));
                    seq.addAttack(new EnemyAttack().addAction(
                            new BindPets(text[7].title, text[7].description, 3,
                                    5, 5, 5)).addPair(new ConditionUsed(),
                            new Attack(150)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                            new MultipleAttack(text[8].title,
                                    text[8].description, 1000, 5)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);
                } else if (choose == ids[1]) {
                    data.add(Enemy.create(env, 2277, 76201200, 47970, 1740, 1,
                            new Pair<Integer, Integer>(4, 0),
                            getMonsterTypes(env, 2277)));
                    zero = data.get(0);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 30);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                            new ResistanceShieldAction(text[2].title,
                                    text[2].description, 999)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new Attack(text[3].title, text[3].description, 80))
                            .addPair(new ConditionHp(2, 1), new RecoverSelf(50)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addAction(
                                    new Attack(text[7].title, text[7].description,
                                            100))
                            .addAction(new Transform(4, 2, 7))
                            .addPair(
                                    new ConditionUsed(),
                                    new LockOrb(text[8].title, text[8].description,
                                            0, 7)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                            new MultipleAttack(text[9].title, text[9].description,
                                    100, 5)));
                    zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                    RandomAttack rndAttack = new RandomAttack();
                    rndAttack.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new Gravity(text[4].title,
                                    text[4].description, 99)));
                    rndAttack.addAttack(new EnemyAttack().addPair(
                            new ConditionPercentage(80), new Attack(text[5].title,
                                    text[5].description, 120)));
                    rndAttack.addAttack(new EnemyAttack().addPair(
                            new ConditionPercentage(40), new MultipleAttack(
                                    text[6].title, text[6].description, 70, 2)));
                    zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
                } else if (choose == ids[2]) {
                    data.add(Enemy.create(env, choose, 76800000, 49230, 1740, 1, new Pair<Integer, Integer>(5, 0), getMonsterTypes(env, choose)));
                    zero = data.get(0);
                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 5, 50);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addAction(new ResistanceShieldAction(text[1].title,text[1].description, 7))
                            .addAction(new ShieldAction(text[2].title, text[2].description, 7,-1, 75))
                            .addAction(new RandomChange(text[3].title, text[3].description,6, 4)).addPair(new ConditionAlways(),new Attack(70)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new BindPets(text[4].title, text[4].description, 2,7,7, 1)).addAction(new RandomChange(text[5].title, text[5].description,6, 14)).addPair(new ConditionHp(2, 16),new Attack(140)));
                    seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[6].title, text[6].description,6, 7)).addPair(new ConditionFindOrb(6),new Attack(210)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new DamageVoidShield(text[7].title,text[7].description, 1,100000)).addAction(new RandomChange(text[8].title, text[8].description,6,7)).addPair(new ConditionUsed(),new Attack(70)));
                    zero.attackMode.addAttack(new ConditionHp(2,86), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[9].title,text[9].description, 1,4,5,3,0)).addAction(new RandomChange(text[10].title, text[10].description,8,4)).addPair(new ConditionUsed(),new Attack(70)));
                    zero.attackMode.addAttack(new ConditionHp(2,72), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new BindPets(text[11].title, text[11].description, 2,7,7, 1)).addAction(new RandomChange(text[12].title, text[12].description,8,5)).addPair(new ConditionUsed(),new Attack(70)));
                    zero.attackMode.addAttack(new ConditionHp(2,58), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new DamageVoidShield(text[7].title,text[7].description, 1,100000)).addAction(new RandomChange(text[14].title, text[14].description,6,4)).addPair(new ConditionUsed(),new Attack(70)));
                    zero.attackMode.addAttack(new ConditionHp(2,44), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[9].title,text[9].description, 1,4,5,3,0)).addAction(new RandomChange(text[16].title, text[16].description,6,7)).addPair(new ConditionUsed(),new Attack(70)));
                    zero.attackMode.addAttack(new ConditionHp(2,30), seq);

                    RandomAttack rnd = new RandomAttack();
                    rnd.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[17].title,text[17].description, 1,4,5)).addPair(new ConditionAlways(),new MultipleAttack(text[18].title, text[18].description,15, 7)));
                    rnd.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[17].title,text[17].description, 1,4,5)).addPair(new ConditionAlways(),new Gravity(text[20].title, text[20].description, 77)));
                    rnd.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[17].title,text[17].description, 1,4,5)).addAction(new Transform(text[22].title, text[22].description, 1,4, 5, 3, 0,2,6)).addPair(new ConditionAlways(),new Attack(70)));
                    rnd.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[17].title,text[17].description, 1,4,5)).addAction(new RandomChange(text[24].title, text[24].description,6,5)).addPair(new ConditionAlways(),new Attack(70)));
                    rnd.addAttack(new EnemyAttack().addAction(new RecoverSelf(7)).addPair(new ConditionHp(2,93),new Attack(text[25].title, text[25].description,70)));
                    rnd.addAttack(new EnemyAttack().addAction(new RandomChange(text[26].title, text[26].description,8,3)).addPair(new ConditionAlways(),new Attack(70)));
                    seq.addAttack(new EnemyAttack().addAction(new LockOrb(text[27].title, text[27].description, 1,7)).addPair(new ConditionAlways(),new Attack(70)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), rnd);
                } else if (choose == ids[3]) {
                    data.add(Enemy.create(env, choose, 48750000, 35650, 8888888, 1,
                            new Pair<Integer, Integer>(3, 4),
                            getMonsterTypes(env, choose)));
                    zero = data.get(0);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 75);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),new ShieldAction(text[2].title, text[2].description, 3,-1, 75)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new RecoverSelf(text[3].title, text[3].description,100)).addPair(new ConditionUsedIf(1,1,0, EnemyCondition.Type.HP.ordinal(), 2, 1),new ComboShield(text[4].title, text[4].description,999,7)));
                    seq.addAttack(new EnemyAttack().addAction(new RecoverSelf(text[3].title, text[3].description,100)).addPair(new ConditionHp(2, 1),new Attack(100)));
                    seq.addAttack(new EnemyAttack().addAction(new Transform(text[5].title, text[5].description, 6)).addPair(new ConditionHp(2,15),new Attack(600)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Gravity(text[7].title, text[7].description, 99)).addPair(new ConditionUsed(),new ReduceTime(text[6].title, text[6].description,3, -1000)));
                    zero.attackMode.addAttack(new ConditionHp(2, 70), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[8].title, text[8].description,3,5)).addAction(new Attack(text[9].title, text[9].description,100)).addPair(new ConditionUsed(),new BindPets(text[9].title, text[9].description, 2,5, 5, 1)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1, 1, EnemyCondition.Type.ENEMY_BUFF.ordinal(), BuffOnEnemySkill.Type.SHIELD.ordinal()),new ShieldAction(text[10].title, text[10].description, 3,-1, 75)));
                    seq.addAttack(new EnemyAttack().addAction(new DropRateAttack(text[11].title, text[11].description,1, 8, 20)).addPair(new ConditionAlways(),new MultipleAttack(text[12].title, text[12].description,70, 2)));
                    seq.addAttack(new EnemyAttack().addAction(new Daze(text[13].title,text[13].description, 0)).addAction(new LockOrb(text[14].title, text[14].description, 1,8,1,4,5,3,0,2,6,7,8)).addPair(new ConditionAlways(),new Attack(140)));
                    seq.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[15].title,text[15].description, 3, 5)).addPair(new ConditionAlways(),new AbsorbShieldAction(3, 0)));
                    seq.addAttack(new EnemyAttack().addAction(new DropRateAttack(text[11].title, text[11].description,1, 8, 20)).addPair(new ConditionAlways(),new MultipleAttack(text[12].title, text[12].description,70, 2)));
                    seq.addAttack(new EnemyAttack().addAction(new Daze(text[13].title,text[13].description, 0)).addAction(new LockOrb(text[14].title, text[14].description, 1,8,1,4,5,3,0,2,6,7,8)).addPair(new ConditionAlways(),new Attack(140)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (choose == ids[4]) {
                    data.add(Enemy.create(env, choose, 71940000, 24630, 99999, 1, new Pair<Integer, Integer>(0, 1), getMonsterTypes(env, choose)));
                    zero = data.get(0);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockOrb(text[2].title, text[2].description,0, 1,4,5,3,0,2,6,7,8)).addAction(new ResistanceShieldAction(text[3].title,text[3].description, 999)).addPair(new ConditionUsed(),new Angry(text[4].title, text[4].description, 2,900)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new RecoverSelf(text[5].title, text[5].description,100)).addAction(new Attack(text[6].title, text[6].description,200)).addPair(new ConditionUsedIf(1,1,0,EnemyCondition.Type.HP.ordinal(),2,1),new BindPets(text[6].title, text[6].description, 3,9, 9, 1)));
                    seq.addAttack(new EnemyAttack().addAction(new RecoverSelf(text[5].title, text[5].description,100)).addAction(new Attack(text[6].title, text[6].description,200)).addPair(new ConditionUsedIf(1,1,0,EnemyCondition.Type.HP.ordinal(),2,1),new BindPets(text[6].title, text[6].description, 3,9, 9, 1)));
                    seq.addAttack(new EnemyAttack().addAction(new PositionChange(text[7].title, text[7].description,9,0,0,0,0,2,0,4,2,0,2,2,2,4,4,0,4,2,4,4,6,1,6,3,6,5,8,1,8,3,8,5)).addPair(new ConditionHp(2,20),new Attack(5000)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[8].title, text[8].description,20)).addPair(new ConditionUsed(),new ComboShield(9,8)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new DarkScreen(text[9].title, text[9].description,0)).addAction(new Attack(50)).addPair(new ConditionUsed(),new LockOrb(text[10].title, text[10].description,0, 1,4,5,3,0,2,6,7,8)));
                    seq.addAttack(new EnemyAttack().addAction(new PositionChange(text[11].title, text[11].description,9, 0, 1, 0, 0, 0, 2, 0, 3, 0)).addPair(new ConditionAlways(),new Attack(120)));
                    seq.addAttack(new EnemyAttack().addAction(new LineChange(text[12].title, text[12].description,7,7,10,7)).addPair(new ConditionAlways(),new Attack(150)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                }
            }
            mStageEnemies.put(++stageCounter, data); // 17
        }

//        for (int y = 0; y<5; ++y)
        {
			data = new ArrayList<Enemy>();
			{
				int[] ids = {985, 1536, 1737, 2807, 2946};
				int choose = RandomUtil.chooseOne(ids);
                text = loadSubText(list, choose);
				if (choose == ids[0]) {
					data.add(Enemy.create(env, choose, 90087000, 34110, 1160, 1,
							new Pair<Integer, Integer>(0, -1),
							getMonsterTypes(env, choose)));
					zero = data.get(0);
					zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 90);
					zero.attackMode = new EnemyAttackMode();
					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack()
							.addAction(new BindPets(text[1].title, text[1].description,3,3,3,1))
							.addPair(new ConditionAlways(), new Attack(400)));
					zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

					zero.attackMode.createEmptyMustAction();

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
									1, 0, EnemyCondition.Type.FIND_TYPE.ordinal(), 1, 6),
							new BindPets(text[2].title, text[2].description, 4,
									5, 5, 1, 6)));
					seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
									1, 1, EnemyCondition.Type.FIND_TYPE.ordinal(), 1, 6),
							new Attack(100)));
					zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack()
							.addPair(new ConditionUsed(), new Gravity(
									text[4].title, text[4].description, 99)));
					seq.addAttack(new EnemyAttack()
							.addPair(new ConditionAlways(), new Attack(
									text[5].title, text[5].description, 500)));
					zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addAction(
							new ColorChange(text[3].title, text[3].description,
									3, 7)).addPair(
							new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
									.ordinal(), 3), new Attack(90)));

					zero.attackMode.addAttack(new ConditionHp(1, 30), seq);
				} else if (choose == ids[1]) {
					data.add(Enemy.create(env, choose, 108110000, 50260, 7800, 1,
							new Pair<Integer, Integer>(1, -1),
							getMonsterTypes(env, choose)));
					zero = data.get(0);
					zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 90);
					zero.attackMode = new EnemyAttackMode();
					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addAction(
							new DropRateAttack(text[1].title,
									text[1].description, 99, 1, 10)).addPair(
							new ConditionAlways(), new DropRateAttack(99, 7, 10)));
					zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
									1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
							new ComboShield(text[2].title, text[2].description,
									999, 7)));
					zero.attackMode.addAttack(new ConditionAlways(), seq);

					RandomAttack rndAttack = new RandomAttack();
					rndAttack.addAttack(new EnemyAttack().addAction(
							new ColorChange(text[3].title, text[3].description,
									4, 7)).addPair(
							new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
									.ordinal(), 4), new Attack(100)));
					rndAttack.addAttack(new EnemyAttack().addAction(
							new ColorChange(text[4].title, text[4].description,
									-1, 1)).addPair(new ConditionAlways(),
							new Attack(100)));
					rndAttack
							.addAttack(new EnemyAttack().addPair(
									new ConditionAlways(), new MultipleAttack(
											text[5].title, text[5].description,
											50, 3)));
					zero.attackMode.addAttack(new ConditionHp(2, 50), rndAttack);

					rndAttack = new RandomAttack();
					rndAttack.addAttack(new EnemyAttack().addAction(
							new ColorChange(text[6].title, text[6].description,
									4, 7)).addPair(
							new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
									.ordinal(), 4), new Attack(100)));
					rndAttack.addAttack(new EnemyAttack().addAction(
							new ColorChange(text[7].title, text[7].description,
									-1, 1)).addPair(new ConditionAlways(),
							new Attack(100)));
					zero.attackMode.addAttack(new ConditionHp(1, 50), rndAttack);
				} else if (choose == ids[2]) {
					data.add(Enemy.create(env, choose, 106840000, 50010, 39000, 1,
							new Pair<Integer, Integer>(4, -1),
							getMonsterTypes(env, choose)));
					zero = data.get(0);
					zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 90);
					zero.attackMode = new EnemyAttackMode();
					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack()
							.addPair(new ConditionAlways(), new LockSkill(
									text[1].title, text[1].description, 5)));
					zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
									1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
							new BindPets(text[2].title, text[2].description, 3,
									1, 3, 5)));
					zero.attackMode.addAttack(new ConditionAlways(), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addAction(
							new ColorChange(text[3].title, text[3].description,
									-1, 4)).addPair(new ConditionAlways(),
							new Attack(100)));
					zero.attackMode.addAttack(new ConditionHp(1, 75), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addAction(
							new BindPets(text[4].title, text[4].description, 3,
									2, 4, 1)).addPair(new ConditionTurns(2),
							new Attack(85)));
					seq.addAttack(new EnemyAttack().addAction(
							new ColorChange(text[5].title, text[5].description,
									-1, 4)).addPair(new ConditionAlways(),
							new Attack(100)));
					zero.attackMode.addAttack(new ConditionHp(1, 30), seq);

					RandomAttack rnd = new RandomAttack();
					rnd.addAttack(new EnemyAttack().addPair(
							new ConditionPercentage(50), new Gravity(
									text[6].title, text[6].description, 500)));
					rnd.addAttack(new EnemyAttack().addAction(
							new BindPets(text[7].title, text[7].description, 3,
									2, 4, 1)).addPair(new ConditionPercentage(50),
							new Attack(85)));
					rnd.addAttack(new EnemyAttack().addAction(
							new ColorChange(text[8].title, text[8].description,
									-1, 4)).addPair(new ConditionAlways(),
							new Attack(100)));
					zero.attackMode.addAttack(new ConditionHp(2, 30), rnd);
				} else if (choose == ids[3]) {
					data.add(Enemy.create(env, choose, 70000000, 10100, 1560, 1, new Pair<Integer, Integer>(5, 0), getMonsterTypes(env, choose)));
					zero = data.get(0);
					zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 90);
					zero.attackMode = new EnemyAttackMode();
					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addAction(new DamageVoidShield(text[2].title, text[2].description, 999, 6000000)).addPair(new ConditionAlways(), new AbsorbShieldAction(text[1].title, text[1].description, 10, 3)));
					zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addAction(new RandomChange(text[4].title, text[4].description, 7, 10)).addPair(new ConditionUsed(), new ResistanceShieldAction(text[3].title, text[3].description, 999)));
					seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 10), new MultipleAttack(text[6].title, text[6].description, 2000, 6)));
					seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[5].title, text[5].description, 7, 5)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB.ordinal(), 7), new Attack(10000)));
					zero.attackMode.addAttack(new ConditionAlways(), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addAction(new Daze(text[8].title, text[8].description, 0)).addAction(new Transform(text[7].title, text[7].description, 5,2,7,6)).addPair(new ConditionUsed(), new Attack(350)));
					zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addAction(new RandomChange(text[9].title, text[9].description, 7, 5)).addPair(new ConditionAlways(), new Attack(280)));
					seq.addAttack(new EnemyAttack().addAction(new Attack(text[10].title, text[10].description, 280)).addPair(new ConditionAlways(), new BindPets(text[10].title, text[10].description, 3, 5, 5, 1)));
					zero.attackMode.addAttack(new ConditionHp(1, 10), seq);


				} else if (choose == ids[4]) {
					data.add(Enemy.create(env, choose, 100000000, 10610, 1560, 1, new Pair<Integer, Integer>(3, 0), getMonsterTypes(env, choose)));
					zero = data.get(0);
					zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 90);
					zero.attackMode = new EnemyAttackMode();
					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[2].title, text[2].description, 999)).addPair(new ConditionAlways(), new LockAwoken(text[1].title, text[1].description, 7)));
					zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

					zero.attackMode.createEmptyMustAction();


					RandomAttack rnd = new RandomAttack();
					rnd.addAttack(new EnemyAttack().addAction(new Attack(text[4].title, text[4].description, 100)).addPair(new ConditionAlways(), new BindPets(1, 10, 10, 0)));
					rnd.addAttack(new EnemyAttack().addAction(new Attack(text[3].title, text[3].description, 100)).addPair(new ConditionAlways(), new BindPets(2, 10, 10, 1)));
					zero.attackMode.addAttack(new ConditionAtTurn(1), rnd);

					seq = new SequenceAttack();
					int randOne = RandomUtil.getInt(3);
					if (randOne == 0) {
						seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[5].title, text[5].description, 0, 3)).addPair(new ConditionHp(1, 50), new Attack(280)));
					} else if (randOne == 1) {
						seq.addAttack(new EnemyAttack().addAction(new ReduceTime(1, -1000)).addPair(new ConditionHp(1, 50), new Attack(text[6].title, text[6].description, 280)));
					} else {
						seq.addAttack(new EnemyAttack().addPair(new ConditionHp(1, 50), new MultipleAttack(text[7].title, text[7].description, 150, 2)));
					}
					zero.attackMode.addAttack(new ConditionAtTurn(2), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new ComboShield(text[8].title, text[8].description, 8, 8)));
					for (int x = 0; x < 8; ++x) {
						seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(text[9].title, text[9].description, 300)));
					}
					seq.addAttack(new EnemyAttack().addAction(new Gravity(text[10].title, text[10].description, 99)).addPair(new ConditionAlways(), new MultipleAttack(text[11].title, text[11].description, 3000, 5)));
					zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
				}
			}
			mStageEnemies.put(++stageCounter, data); // 18 ++stageCounter
        }
			data = new ArrayList<Enemy>();
			{
				int[] ids = {1217, 1644};
				int choose = RandomUtil.chooseOne(ids);
                text = loadSubText(list, choose);
				if (choose == ids[0]) {
					data.add(Enemy.create(env, choose, 100000000, 20230, 20000000, 2, new Pair<Integer, Integer>(3, 4), getMonsterTypes(env, choose)));
					zero = data.get(0);
					zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
					zero.addOwnAbility(BuffOnEnemySkill.Type.CHANGE_TURN, 70, 1);
					zero.attackMode = new EnemyAttackMode();

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[3].title, text[3].description, 999, 5)).addPair(new ConditionAlways(), new RecoveryUpAction(text[2].title, text[2].description, 10, 50)));
					zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addPair(new ConditionHp(1, 71), new Attack(text[4].title, text[4].description, 1000)));
					seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 10), new MultipleAttack(text[5].title, text[5].description, 3000, 10)));
					seq.addAttack(new EnemyAttack().addAction(new LockAwoken(text[7].title, text[7].description, 3)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new Angry(text[6].title, text[6].description, 999, 150)));
					zero.attackMode.addAttack(new ConditionAlways(), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addAction(new CloudAttack(text[9].title, text[9].description, 1, 6, 1)).addPair(new ConditionAlways(), new Attack(200)));
					seq.addAttack(new EnemyAttack().addAction(new CloudAttack(text[8].title, text[8].description, 1, 1, 5)).addPair(new ConditionAlways(), new Attack(200)));
					zero.attackMode.addAttack(new ConditionHp(1, 10), seq);
				} else if (choose == ids[1]) {
					data.add(Enemy.create(env, choose, 102000000, 21610, 20000000, 2, new Pair<Integer, Integer>(0, 4), getMonsterTypes(env, choose)));
					zero = data.get(0);
					zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
					zero.addOwnAbility(BuffOnEnemySkill.Type.CHANGE_TURN, 70, 1);
					zero.attackMode = new EnemyAttackMode();

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[3].title, text[3].description, 999, 4)).addPair(new ConditionAlways(), new LockOrb(text[2].title, text[2].description, 0, 1, 4, 5, 3, 0, 2, 6, 7, 8)));
					zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addPair(new ConditionHp(1, 71), new Attack(text[4].title, text[4].description, 1000)));
					seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 10), new MultipleAttack(text[5].title, text[5].description, 3000, 10)));
					seq.addAttack(new EnemyAttack().addAction(new LockOrb(text[7].title, text[7].description, 0, 1, 4, 5, 3, 0, 2, 6, 7, 8)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new Angry(text[6].title, text[6].description, 999, 150)));
					zero.attackMode.addAttack(new ConditionAlways(), seq);

                    RandomAttack rnd = new RandomAttack();
                    rnd.addAttack(new EnemyAttack().addAction(new SuperDark(text[8].title, text[8].description, 1, 2, 0, 1)).addPair(new ConditionAlways(), new Attack(200)));
                    rnd.addAttack(new EnemyAttack().addAction(new SuperDark(text[9].title, text[9].description, 1, 2, 1, 1)).addPair(new ConditionAlways(), new Attack(200)));
                    rnd.addAttack(new EnemyAttack().addAction(new SuperDark(text[10].title, text[10].description, 1, 2, 2, 1)).addPair(new ConditionAlways(), new Attack(200)));
                    rnd.addAttack(new EnemyAttack().addAction(new SuperDark(text[11].title, text[11].description, 1, 3, 2, 1)).addPair(new ConditionAlways(), new Attack( 200)));
                    rnd.addAttack(new EnemyAttack().addAction(new SuperDark(text[12].title, text[12].description, 1, 3, 1, 1)).addPair(new ConditionAlways(), new Attack( 200)));
                    rnd.addAttack(new EnemyAttack().addAction(new SuperDark(text[13].title, text[13].description, 1, 3, 0, 1)).addPair(new ConditionAlways(), new Attack(200)));
                    rnd.addAttack(new EnemyAttack().addAction(new SuperDark(text[14].title, text[14].description, 1, 0, 0, 1)).addPair(new ConditionAlways(), new Attack(200)));
                    rnd.addAttack(new EnemyAttack().addAction(new SuperDark(text[15].title, text[15].description, 1, 0, 1, 1)).addPair(new ConditionAlways(), new Attack(200)));
                    rnd.addAttack(new EnemyAttack().addAction(new SuperDark(text[16].title, text[16].description, 1, 0, 2, 1)).addPair(new ConditionAlways(), new Attack(200)));
                    rnd.addAttack(new EnemyAttack().addAction(new SuperDark(text[17].title, text[17].description, 1, 1, 1, 1)).addPair(new ConditionAlways(), new Attack(200)));
                    rnd.addAttack(new EnemyAttack().addAction(new SuperDark(text[18].title, text[18].description, 1, 1, 0, 1)).addPair(new ConditionAlways(), new Attack( 200)));
					zero.attackMode.addAttack(new ConditionHp(1, 10), rnd);
				}
			}
			mStageEnemies.put(++stageCounter, data); // 19 ++stageCounter
			//done
			data = new ArrayList<Enemy>();
			{
				int[] ids = {1747, 2078};
				int choose = RandomUtil.chooseOne(ids);
                text = loadSubText(list, choose);
				if (choose == ids[0]) {
					data.add(Enemy.create(env, choose, 200000000, 21930, 0, 1,
							new Pair<Integer, Integer>(3, 1),
							getMonsterTypes(env, choose)));
					zero = data.get(0);
					zero.attackMode = new EnemyAttackMode();
					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addAction(
							new ResistanceShieldAction(text[0].title,
									text[0].description, 99)).addPair(
							new ConditionAlways(),
							new ShieldAction(text[1].title,
									text[1].description, 2, -1, 75)));
					zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addPair(new ConditionHp(1, 65),
							new MultipleAttack(text[2].title,
									text[2].description, 200, 6)));
					zero.attackMode.addAttack(new ConditionAlways(), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack()
							.addPair(new ConditionAlways(), new Attack(
									text[10].title, text[10].description, 1500)));
					zero.attackMode.addAttack(new ConditionHp(2, 5), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack()
							.addPair(new ConditionUsed(), new DamageVoidShield(text[3].title, text[3].description, 999, 10000000)));
					zero.attackMode.addAttack(new ConditionTurns(3), seq);

					RandomAttack rndAttack = new RandomAttack();
					rndAttack.addAttack(new EnemyAttack().addAction(
							new MultipleAttack(text[4].title,
									text[4].description, 90, 2)).addPair(
							new ConditionAlways(),
							new BindPets(text[5].title, text[5].description, 3,
									2, 2, 1)));
					rndAttack.addAttack(new EnemyAttack().addAction(
							new MultipleAttack(text[4].title,
									text[4].description, 90, 2)).addPair(
							new ConditionAlways(),
							new DarkScreen(text[6].title, text[6].description,
									0)));
					rndAttack.addAttack(new EnemyAttack().addAction(
							new MultipleAttack(text[4].title,
									text[4].description, 90, 2))
							.addPair(
									new ConditionAlways(),
									new Gravity(text[8].title,
											text[8].description, 99)));
					rndAttack.addAttack(new EnemyAttack().addAction(
							new MultipleAttack(text[4].title,
									text[4].description, 90, 2)).addPair(
							new ConditionAlways(),
							new RandomChange(text[7].title,
									text[7].description, 6, 3)));
					rndAttack.addAttack(new EnemyAttack().addAction(
							new LockSkill(text[9].title, text[9].description,
									10)).addPair(new ConditionAlways(),
							new Attack(150)));
					zero.attackMode.addAttack(new ConditionHp(1, 5), rndAttack);
				} else {
					data.add(Enemy.create(env, choose, 200000000, 22410, 0, 1,
							new Pair<Integer, Integer>(0, 1),
							getMonsterTypes(env, choose)));
					zero = data.get(0);
					zero.attackMode = new EnemyAttackMode();
					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addAction(
							new ResistanceShieldAction(text[0].title,
									text[0].description, 99)).addPair(
							new ConditionAlways(),
							new ShieldAction(text[1].title,
									text[1].description, 2, -1, 75)));
					zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack().addPair(new ConditionHp(1, 65),
							new MultipleAttack(text[2].title,
									text[2].description, 200, 6)));
					zero.attackMode.addAttack(new ConditionAlways(), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack()
							.addPair(new ConditionAlways(), new Attack(
									text[8].title, text[8].description, 1500)));
					zero.attackMode.addAttack(new ConditionHp(2, 5), seq);

					seq = new SequenceAttack();
					seq.addAttack(new EnemyAttack()
							.addPair(new ConditionUsed(), new DamageVoidShield(text[3].title, text[3].description, 999, 10000000)));
					zero.attackMode.addAttack(new ConditionTurns(3), seq);

					RandomAttack rndAttack = new RandomAttack();
					rndAttack
							.addAttack(new EnemyAttack()
									.addAction(
											new DarkScreen(text[4].title,
													text[4].description, 0))
									.addAction(new Attack(text[5].title,
                                            text[5].description,90))
									.addAction(
											new Transform( 1, 4, 5,
													3, 0, 2))
									.addPair(new ConditionAlways(), new Attack(90)));
					rndAttack
							.addAttack(new EnemyAttack()
									.addAction(
											new Transform(text[5].title,
													text[5].description, 1, 4, 5,
													3, 0, 2))
									.addAction(new Attack(90))
									.addAction(
											new SkillDown(text[6].title,
													text[6].description, 0, 0, 1))
									.addPair(new ConditionAlways(), new Attack(90)));
					rndAttack.addAttack(new EnemyAttack()
							.addAction(
									new SkillDown(text[6].title,
											text[6].description, 0, 0, 1))
							.addAction(new Attack(90))
							.addAction(
									new DarkScreen(text[4].title,
											text[4].description, 0))
							.addPair(new ConditionAlways(), new Attack(90)));
					rndAttack.addAttack(new EnemyAttack().addAction(
							new LockSkill(text[7].title, text[7].description,
									10)).addPair(new ConditionAlways(),
							new Attack(150)));
					zero.attackMode.addAttack(new ConditionHp(1, 5), rndAttack);
				}

			}
			mStageEnemies.put(++stageCounter, data); // 20 ++stageCounter

			// done
			data = new ArrayList<Enemy>();
			{
				int[] prop = {1, 4, 5, 3, 0};
				for (int i = 0; i < 3; ++i) {
                    int idx = RandomUtil.getInt(6);
                    if (idx < 5) {
                        int choose = 1547 + idx;
                        data.add(Enemy.create(env, choose, 24, 7707, 6000000, 1,
                                new Pair<Integer, Integer>(prop[idx], -1),
                                getMonsterTypes(env, choose)));
                        zero = data.get(i);
                        zero.attackMode = new EnemyAttackMode();
                        zero.attackMode.createSimpleAttackAction("", "", 100);
                    } else {
                        int choose = 3891;
                        data.add(Enemy.create(env, choose, 48, 15414, 12000000, 1,
                                new Pair<Integer, Integer>(3, 0),
                                getMonsterTypes(env, choose)));
                        zero = data.get(i);
                        zero.attackMode = new EnemyAttackMode();
                        zero.attackMode.createSimpleAttackAction("", "", 100);
                    }
				}
			}
			mStageEnemies.put(++stageCounter, data); // 21

		}
    }
