package com.a30corner.combomaster.playground.stagedata;

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
import com.a30corner.combomaster.playground.action.impl.HpChangeAttack;
import com.a30corner.combomaster.playground.action.impl.IntoVoid;
import com.a30corner.combomaster.playground.action.impl.LineChange;
import com.a30corner.combomaster.playground.action.impl.LockAwoken;
import com.a30corner.combomaster.playground.action.impl.LockOrb;
import com.a30corner.combomaster.playground.action.impl.LockRemoveAttack;
import com.a30corner.combomaster.playground.action.impl.LockSkill;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.NoDropAttack;
import com.a30corner.combomaster.playground.action.impl.PositionChange;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.RandomChangeExcept;
import com.a30corner.combomaster.playground.action.impl.RandomLineChange;
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
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFindOrb;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHasBuff;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHp;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionIf;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionModeSelection;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionNTurns;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionPercentage;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionTurnsOffset;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsedIf;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class UraUltimateArena5 extends IStage {

    @Override
    public void create(IEnvironment env,
                       SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        ArrayList<Enemy> data;
        Enemy zero;
        SequenceAttack seq;
        mStageEnemies.clear();

        SparseArray<List<StageGenerator.SkillText>> list = loadSkillTextNew(env, mStage, new int[]{3318,3319, 3320, 3321, 3322, 3323, 3324, 3325,2551,3259,3208,3838,363,365,1108,2989,3017,3251,2896,3457,3327,2206,2524,1920,1921,2393,2395,3074,4417,1338,4358,908,1514,2560,3200,3903,4013,3638,3640,3642,4585,4742,2098,1885,5175,5177,5179,5181,5183,5297,3891,1547,1548,1549,1550,1551});
        StageGenerator.SkillText[] text;// = new StageGenerator.SkillText[list.size()];

        data = new ArrayList<Enemy>();// checked
        {
            int[] ids = {161, 162, 163, 164, 165};
            int[] atk = {51887, 51849, 51812, 51792, 51774};
            for (int i = 0; i < 4; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                data.add(Enemy.create(env, choose, 8, atk[rnd], 133332666, 1, getMonsterAttrs(env, choose), getMonsterTypes(env, choose)));
                zero = data.get(i);

                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createSimpleAttackAction("", "", 100);

            }

        }
        mStageEnemies.put(1, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {2551, 3259};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 2551) {// checked
                    data.add(Enemy.create(env, 2551, 11501556, 22973, 2024, 1, getMonsterAttrs(env, 2551), getMonsterTypes(env, 2551)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new LockOrb(text[3].title, text[3].description, 2, 10)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[4].title, text[4].description, 999, 200))
                            .addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 1),
                                    new RecoverSelf(text[5].title, text[5].description, 100)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new RandomChange(text[6].title, text[6].description, 6, 8)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 30), new DarkScreen(text[11].title, text[11].description, 0)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[7].title, text[7].description,240)).addPair(new ConditionAlways(), new BindPets( 3, 3, 3, 1)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[8].title, text[8].description, 240)).addPair(new ConditionAlways(), new SkillDown(0, 1, 1)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[9].title, text[9].description, 240)).addPair(new ConditionAlways(), new LineChange(8, 3, 9, 3)));
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[10].title, text[10].description, 300)));
                    zero.attackMode.addAttack(new ConditionHp(1, 30), rndAtk);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[12].title, text[12].description,1000)).addPair(new ConditionAlways(), new LineChange( 3, 7, 4, 7, 7, 7, 10, 7)));
                    zero.attackMode.addAttack(new ConditionHp(2, 30), seq);
                }
                if (choose == 3259) { // checked
                    data.add(Enemy.create(env, 3259, 36433333, 38970, 2508, 1, getMonsterAttrs(env, 3259), getMonsterTypes(env, 3259)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ShieldAction(text[2].title, text[2].description, 3, -1, 75)).addPair(new ConditionAlways(), new ResistanceShieldAction(text[4].title, text[4].description, 3)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[5].title, text[5].description,600)).addPair(new ConditionHp(2, 20), new LineChange( 3, 6, 8, 6, 10, 6, 7, 8, 9, 8, 11, 8)));

                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[6].title, text[6].description,100)).addPair(new ConditionUsed(), new RandomChange( 6, 6)));
                    seq.addAttack(new EnemyAttack().addAction(new Daze(text[8].title, text[8].description, 0)).addPair(new ConditionUsed(), new ComboShield(text[7].title, text[7].description, 2, 6)));

                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[9].title, text[9].description,200)).addPair(new ConditionUsed(), new DarkScreen( 0)));
                    seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[11].title, text[11].description, -1, 8)).addPair(new ConditionUsed(), new ReduceTime(text[10].title, text[10].description, 10, -1000)));

                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();

                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[12].title, text[12].description, 140)).addPair(new ConditionAlways(), new LineChange(3, 6)));

                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[13].title, text[13].description, 80, 2)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                }

            }

        }
        mStageEnemies.put(2, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {3208}; //3838
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 3208) {// checked
                    data.add(Enemy.create(env, 3208, 21119333, 36680, 2400, 1, getMonsterAttrs(env, 3208), getMonsterTypes(env, 3208)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ShieldAction(text[2].title, text[2].description, 2, -1, 75)).addAction(new Transform(text[4].title, text[4].description, 1, 4, 5, 2)).addPair(new ConditionAlways(), new Attack(150)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(1, 90), new Angry(text[5].title, text[5].description, 8, 200)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 90), new Daze(text[6].title, text[6].description, 0)));
                    zero.attackMode.addAttack(new ConditionAtTurn(1), seq);


                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Transform(text[13].title, text[13].description, 1, 4, 5, 2)).addAction(new Attack(150)).addPair(new ConditionUsed(), new ShieldAction(text[12].title, text[12].description, 2, -1, 75)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[14].title, text[14].description, 100, 8)));
                    zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new ComboShield(text[7].title, text[7].description, 6, 6)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[8].title, text[8].description, 50)).addPair(new ConditionAlways(), new RandomChange(8, 6)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[9].title, text[9].description, 100)).addPair(new ConditionAlways(), new LineChange(0, 4, 2, 4)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[10].title, text[10].description, 99)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[11].title, text[11].description, 50, 4)));
                    zero.attackMode.addAttack(new ConditionHp(1, 20), seq);
                }

//                if (choose == 3838) { // not ready FIXME:
//                    data.add(Enemy.create(env, 3838, 16627444, -1, 2100, 1, getMonsterAttrs(env, 3838), getMonsterTypes(env, 3838)));
//                    zero = data.get(i);
//
//                    zero.attackMode = new EnemyAttackMode();
//
//                    seq = new SequenceAttack();
//                    seq.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[2].title, text[2].description, 5, 3)).addAction(new BindPets(text[4].title, text[4].description, 3, 3, 3, 2)).addPair(new ConditionAlways(), new Attack(200)));
//                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
//
//                    seq = new SequenceAttack();
//                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[5].title, text[5].description, 90, 2)));
//                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ResistanceShieldAction(text[6].title, text[6].description, 999)));
//                    seq.addAttack(new EnemyAttack().addAction(new Attack(200)).addPair(new ConditionAlways(), new BindPets(text[7].title, text[7].description, 2, 3, 3, 1)));
//                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ResistanceShieldAction(text[8].title, text[8].description, 999)));
//                    seq.addAttack(new EnemyAttack().addAction(new Attack(220)).addPair(new ConditionAlways(), new BindPets(text[9].title, text[9].description, 2, 4, 4, 1)));
//                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ResistanceShieldAction(text[10].title, text[10].description, 999)));
//                    seq.addAttack(new EnemyAttack().addAction(new Attack(240)).addPair(new ConditionAlways(), new BindPets(text[11].title, text[11].description, 2, 5, 5, 1)));
//                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ResistanceShieldAction(text[12].title, text[12].description, 999)));
//                    seq.addAttack(new EnemyAttack().addAction(new Attack(260)).addPair(new ConditionAlways(), new BindPets(text[13].title, text[13].description, 2, 6, 6, 1)));
//                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ResistanceShieldAction(text[14].title, text[14].description, 999)));
//                    seq.addAttack(new EnemyAttack().addAction(new Attack(280)).addPair(new ConditionAlways(), new BindPets(text[15].title, text[15].description, 2, 7, 7, 1)));
//                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ResistanceShieldAction(text[16].title, text[16].description, 999)));
//                    seq.addAttack(new EnemyAttack().addAction(new Attack(300)).addPair(new ConditionAlways(), new BindPets(text[17].title, text[17].description, 2, 8, 8, 1)));
//                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[18].title, text[18].description, 90, 3)));
//                    seq.addAttack(new EnemyAttack().addAction(new Attack(1000)).addPair(new ConditionAlways(), new BindPets(text[19].title, text[19].description, 2, 4, 4, 3)));
//                    seq.addAttack(new EnemyAttack().addAction(new Attack(3000)).addPair(new ConditionAlways(), new BindPets(text[20].title, text[20].description, 3, 5, 5, 6)));
//                    seq.addAttack(new EnemyAttack().addAction(new Attack(5000)).addPair(new ConditionAlways(), new BindPets(text[21].title, text[21].description, 3, 6, 6, 5)));
//                    zero.attackMode.addAttack(new ConditionAlways(), seq);
//
//                }

            }

        }
        mStageEnemies.put(3, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {363, 365, 1108};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 363) { // checked
                    data.add(Enemy.create(env, 363, 44480000, 34290, 30000000, 1, getMonsterAttrs(env, 363), getMonsterTypes(env, 363)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new BindPets(text[1].title, text[1].description, 3, 5, 5, 1)).addAction(new Attack(120)).addPair(new ConditionAlways(), new ShieldAction(text[0].title, text[0].description, 1, -1, 80)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ReduceTime(text[6].title, text[6].description, 7, -1000)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new Gravity(text[5].title, text[5].description, 99)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 15), new MultipleAttack(text[8].title, text[8].description, 100, 5)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);


                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new BindPets(text[1].title, text[1].description, 3, 5, 5, 1)).addAction(new Attack(120)).addPair(new ConditionAlways(), new ShieldAction(text[0].title, text[0].description, 1, -1, 80)));
                    zero.attackMode.addAttack(new ConditionHp(1, 99), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[9].title, text[9].description, 80, 2)).addPair(new ConditionAlways(), new RecoverSelf(text[10].title, text[10].description, 10)));
                    seq.addAttack(new EnemyAttack().addAction(new LineChange(text[11].title, text[11].description, 1, 5)).addPair(new ConditionAlways(), new Attack(120)));
                    zero.attackMode.addAttack(new ConditionHp(1, 50), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[12].title, text[12].description, 60, 3)).addPair(new ConditionAlways(), new RecoverSelf(text[10].title, text[10].description, 10)));
                    seq.addAttack(new EnemyAttack().addAction(new RandomLineChange(text[14].title, text[14].description, 2, 5, 6, 1,2)).addPair(new ConditionAlways(), new Attack(150)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                }
                if (choose == 365) { // checked
                    data.add(Enemy.create(env, 365, 48800000, 29790, 30000000, 1, getMonsterAttrs(env, 365), getMonsterTypes(env, 365)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[2].title, text[2].description, 5, 4)).addAction(new LockOrb(text[4].title, text[4].description, 0, 0, 4)).addPair(new ConditionAlways(), new Attack(120)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Gravity(text[6].title, text[6].description, 99)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new DropRateAttack(text[5].title, text[5].description, 1, 8, 15)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[7].title, text[7].description, 100, 5)));
                    zero.attackMode.addAttack(new ConditionHp(2, 15), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[8].title, text[8].description, 80, 2)).addPair(new ConditionAlways(), new SkillDown(text[9].title, text[9].description, 0, 1, 1)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[10].title, text[10].description,120)).addPair(new ConditionAlways(), new LineChange( 1, 4)));
                    zero.attackMode.addAttack(new ConditionHp(1, 50), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[11].title, text[11].description, 60, 3)).addPair(new ConditionAlways(), new SkillDown(text[12].title, text[12].description, 0, 1, 1)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[13].title, text[13].description, 150)).addPair(new ConditionAlways(), new RandomLineChange(2, 4, 6, 1, 2)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                }
                if (choose == 1108) { // checked
                    data.add(Enemy.create(env, 1108, 42190000, 39015, 30000000, 1, getMonsterAttrs(env, 1108), getMonsterTypes(env, 1108)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[2].title, text[2].description, 5, 1)).addAction(new DropRateAttack(text[4].title, text[4].description, 1, 8, 15)).addPair(new ConditionAlways(), new Attack(120)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockOrb(text[5].title, text[5].description, 0, 0, 1)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new Gravity(text[6].title, text[6].description, 99)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[7].title, text[7].description, 100, 5)));
                    zero.attackMode.addAttack(new ConditionHp(2, 15), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new BindPets(text[12].title, text[12].description, 5, 1, 1, 1, 1)).addPair(new ConditionAlways(), new MultipleAttack(text[11].title, text[11].description, 60, 3)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[13].title, text[13].description,150)).addPair(new ConditionAlways(), new RandomLineChange( 2, 1, 6, 1, 2)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new BindPets(text[9].title, text[9].description, 5, 1, 1, 1, 1)).addPair(new ConditionAlways(), new MultipleAttack(text[8].title, text[8].description, 80, 2)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[10].title, text[10].description, 120)).addPair(new ConditionAlways(), new LineChange(1, 1)));
                    zero.attackMode.addAttack(new ConditionHp(1, 50), seq);

                }

            }

        }
        mStageEnemies.put(4, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {3318, 3319, 3320, 3321, 3322, 3323, 3324, 3325};
            text = loadSubText(list, 3318);
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                //text = loadSubText(list, choose);
                data.add(Enemy.create(env, choose, 50, 6750, 30000000, 1, getMonsterAttrs(env, choose), getMonsterTypes(env, choose)));
                zero = data.get(i);

                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SkillDown(text[0].title, text[0].description, 0, 3, 3)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[1].title, text[1].description, 1000)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            }

        }
        mStageEnemies.put(5, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {2989, 3017};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 2989) { // checked
                    data.add(Enemy.create(env, 2989, 20557500, 46800, 10000000, 1, getMonsterAttrs(env, 2989), getMonsterTypes(env, 2989)));
                    zero = data.get(i);

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
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[11].title, text[11].description, 100)).addPair(new ConditionAlways(), new RandomLineChange(2,4,6,1,1)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[13].title, text[13].description, 99)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[15].title, text[15].description, 35, 4)));

                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                }
                if (choose == 3017) {// checked, some error but dont mind
                    data.add(Enemy.create(env, 3017, 18399150, 37350, 10000000, 1, getMonsterAttrs(env, 3017), getMonsterTypes(env, 3017)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[3].title, text[3].description, 99, 5)).addPair(new ConditionAlways(), new LockOrb(text[5].title, text[5].description, 0, 0, 3)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();

                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Gravity(text[7].title, text[7].description, 99)).addPair(new ConditionHp(2, 70), new DamageAbsorbShield(text[6].title, text[6].description, 3, 500000)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(1, 70), new ShieldAction(text[8].title, text[8].description, 3, -1, 75)));
                    zero.attackMode.addAttack(new ConditionAtTurn(1), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[9].title, text[9].description, 1000)).addPair(new ConditionUsed(), new LockOrb(0, 1, 4, 5, 3, 0, 2, 6, 7, 8)));
                    zero.attackMode.addAttack(new ConditionHp(2, 15), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[12].title, text[12].description, 999, 200)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[10].title, text[10].description, 100)).addPair(new ConditionUsed(), new LockOrb(2, 8)));
                    zero.attackMode.addAttack(new ConditionHp(2, 70), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[11].title, text[11].description,200)).addPair(new ConditionUsed(), new DarkScreen( 0)));

                    zero.attackMode.addAttack(new ConditionHp(1, 70), seq);

                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[13].title, text[13].description,130)).addPair(new ConditionHp(2, 30), new RandomChange( 6, 4, 7, 4)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[14].title, text[14].description,130)).addPair(new ConditionAlways(), new ColorChange(-1, 6)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[15].title, text[15].description,130)).addPair(new ConditionAlways(), new ColorChange(-1, 7)));
                    zero.attackMode.addAttack(new ConditionModeSelection(3, 0, 1), rndAtk);

                    rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[17].title, text[17].description,140)).addPair(new ConditionAlways(), new LockOrb( 0, 6, 7)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[18].title, text[18].description, 140)).addPair(new ConditionAlways(), new LockOrb(2, 10)));
                    zero.attackMode.addAttack(new ConditionModeSelection(3, 1, 2), rndAtk);

                    rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 30), new MultipleAttack(text[20].title, text[20].description, 50, 4)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[21].title, text[21].description, 140)).addPair(new ConditionAlways(), new LockSkill(5)));
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[20].title, text[20].description, 40, 4)));
                    zero.attackMode.addAttack(new ConditionModeSelection(3, 2, 0), rndAtk);
                }

            }

        }
        mStageEnemies.put(6, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {3251, 2896};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 3251) {// checked
                    data.add(Enemy.create(env, 3251, 18828150, 39285, 3920, 1, getMonsterAttrs(env, 3251), getMonsterTypes(env, 3251)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new SkillDown(text[0].title, text[0].description, 0, 3, 5)).addPair(new ConditionAlways(), new DropRateAttack(text[1].title, text[1].description, 15, 6, 20)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ShieldAction(text[2].title, text[2].description, 10, -1, 75)).addAction(new Attack(90)).addPair(new ConditionAtTurn(1), new SuperDark(text[3].title, text[3].description, 1, 0, 0, 1, 0, 2, 0, 3, 0, 0)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 30), new DamageAbsorbShield(text[11].title, text[11].description, 10, 800000)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[12].title, text[12].description, 600)).addPair(new ConditionHp(2, 30), new SuperDark(1, 0, 0, 0, 2, 1, 0, 0)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[4].title, text[4].description,100)).addPair(new ConditionAlways(), new SuperDark( 1, 3, 0, 0)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[5].title, text[5].description,100)).addPair(new ConditionAlways(), new SuperDark( 1, 2, 0, 0)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[6].title, text[6].description,90)).addPair(new ConditionAlways(), new SuperDark(1, 0, 0, 0)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[7].title, text[7].description,90)).addPair(new ConditionAlways(), new SuperDark(1, 1, 0, 0)));

                    zero.attackMode.addAttack(new ConditionModeSelection(3, 0), rndAtk);

                    rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[8].title, text[8].description, 120)).addPair(new ConditionAlways(), new RandomLineChange(2, 1, 6, 1, 2)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[9].title, text[9].description, 120)).addPair(new ConditionAlways(), new BindPets(3, 1, 1, 2)));

                    zero.attackMode.addAttack(new ConditionModeSelection(3, 1), rndAtk);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[10].title, text[10].description, 60, 3)));

                    zero.attackMode.addAttack(new ConditionModeSelection(3, 2), seq);
                }
                if (choose == 2896) { // checked
                    data.add(Enemy.create(env, 2896, 21562500, 41040, 3920, 1, getMonsterAttrs(env, 2896), getMonsterTypes(env, 2896)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 5, 50);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);


                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Gravity(text[3].title, text[3].description, 99)).addPair(new ConditionAlways(), new SwitchLeader(text[5].title, text[5].description, 2)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[6].title, text[6].description,120)).addPair(new ConditionAlways(), new RandomLineChange( 3, 4, 0, 6, 2, 11,14)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[7].title, text[7].description, 50, 3)));
                    zero.attackMode.addAttack(new ConditionHp(1, 35), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new SkillDown(text[8].title, text[8].description, 0, 2, 4)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[9].title, text[9].description, 300, 6)));
                    zero.attackMode.addAttack(new ConditionHp(2, 35), seq);
                }
            }

        }
        mStageEnemies.put(7, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {3327, 3457};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 3457) { // checked , FIXME: "add bind move" action and check hp restore or not
                    data.add(Enemy.create(env, 3457, 39200000, 42210, 2280, 1, getMonsterAttrs(env, 3457), getMonsterTypes(env, 3457)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 1, 50);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);


                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Daze(text[3].title, text[3].description, 0)).addPair(new ConditionAlways(), new ResistanceShieldAction(text[5].title, text[5].description, 999)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[7].title, text[7].description, 200)).addAction(new ShieldAction(99, -1, 75)).addPair(new ConditionUsed(), new RecoverSelf(text[6].title, text[6].description, 50)));
                    zero.attackMode.addAttack(new ConditionHp(2, 40), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[8].title, text[8].description, 100, 7)));
                    zero.attackMode.addAttack(new ConditionHp(2, 15), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[10].title, text[10].description, 25, 7)).addPair(new ConditionUsed(), new SkillDown(text[9].title, text[9].description, 0, 4, 4)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[13].title, text[13].description, 110)).addPair(new ConditionAlways(), new LockOrb(0, 0, 5, 6, 7)));
                    zero.attackMode.addAttack(new ConditionModeSelection(2, 0, 1), seq);

                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[14].title, text[14].description, 50)).addPair(new ConditionAlways(), new RandomChangeExcept(6, 12, 2)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[15].title, text[15].description,150)).addAction(new RandomLineChange(3, 0, 2, 7, 2, 14, 15)).addAction(new RandomLineChange(3, 5, 3, 7, 2, 12, 13)).addPair(new ConditionAlways(), new RandomLineChange( 3, 0, 4, 7, 2, 10, 11)));
                    zero.attackMode.addAttack(new ConditionModeSelection(2, 1, 0), rndAtk);

                }
                if (choose == 3327) { // checked
                    data.add(Enemy.create(env, 3327, 39400000, 42285, 20000000, 1, getMonsterAttrs(env, 3327), getMonsterTypes(env, 3327)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[4].title, text[4].description, 999, 7)).addPair(new ConditionAlways(), new ReduceTime(text[2].title, text[2].description, 20, 2000, 0)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[21].title, text[21].description, 999, 200)).addPair(new ConditionUsed(), new ShieldAction(text[20].title, text[20].description, 3, -1, 75)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[23].title, text[23].description, 1000, 3)));
                    zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[23].title, text[23].description, 1000, 3)));
                    zero.attackMode.addAttack(new ConditionHp(2, 10), seq);


                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[7].title, text[7].description, 999)).addAction(new Attack(150)).addPair(new ConditionUsed(), new BindPets(text[8].title, text[8].description, 2, 4, 4, 1)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[9].title, text[9].description, 140)).addPair(new ConditionUsed(), new CloudAttack(1, 2, 2)));
                    seq.addAttack(new EnemyAttack().addAction(new SuperDark(text[10].title, text[10].description, 1, 2, 1, 4, 1, 2, 2, 4, 2, 2, 5, 2, 6, 4, 5, 4, 6)).addPair(new ConditionUsed(), new Attack(140)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new RecoverSelf(text[11].title, text[11].description, 100)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new RecoverSelf(text[12].title, text[12].description, 20)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new RecoverSelf(text[13].title, text[13].description, 20)));
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[15].title, text[15].description, 999, 150)).addPair(new ConditionUsed(), new Daze(text[14].title, text[14].description, 0)));
                    zero.attackMode.addAttack(new ConditionHp(1, 20), seq);


                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[9].title, text[9].description,140)).addPair(new ConditionAlways(), new CloudAttack( 1, 2, 2)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new SuperDark(text[10].title, text[10].description, 1, 2, 1, 4, 1, 2, 2, 4, 2, 2, 5, 2, 6, 4, 5, 4, 6)).addPair(new ConditionAlways(), new Attack(140)));
                    zero.attackMode.addAttack(new ConditionHp(1, 10), rndAtk);
                }

            }

        }
        mStageEnemies.put(8, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {2206, 1921};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 2206) { // checked
                    data.add(Enemy.create(env, 2206, 79333333, 62481, 24444358, 1, getMonsterAttrs(env, 2206), getMonsterTypes(env, 2206)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[2].title, text[2].description, 999)).addPair(new ConditionAlways(), new ReduceTime(text[4].title, text[4].description, 99, -1000)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[11].title, text[11].description, 999, 200)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new BindPets(text[10].title, text[10].description, 1, 1, 1, 2)));
                    seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[15].title, text[15].description, 60, 6)).addAction(new Attack(text[14].title, text[14].description, 20)).addPair(new ConditionHp(2, 20), new Transform(0, 1, 6, 7)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[13].title, text[13].description, 100)).addPair(new ConditionHp(2, 50), new ShieldAction(text[12].title, text[12].description, 1, -1, 50)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[5].title, text[5].description,150)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB.ordinal(), 7), new ColorChange( 7, 1)));
                    seq.addAttack(new EnemyAttack().addAction(new SkillDown(text[7].title, text[7].description, 0, 0, 2)).addAction(new Attack(100)).addPair(new ConditionHp(2,80), new RandomLineChange(3, 1, 0, 7, 3, 0, 2, 4)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[9].title, text[9].description, 100)).addPair(new ConditionAlways(), new RandomChange(text[8].title, text[8].description, 6, 5)));
                    zero.attackMode.addAttack(new ConditionHp(1, 50), seq);
                }
                else { // checked
                    data.add(Enemy.create(env, 1921, 41640000, 57489, 1092, 1, getMonsterAttrs(env, 1921), getMonsterTypes(env, 1921)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new DropRateAttack(text[2].title, text[2].description, 10, 2, 20)).addPair(new ConditionAlways(), new RecoveryUpAction(text[4].title, text[4].description, 10, 50)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 20), new Angry(text[5].title, text[5].description, 999, 200)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[6].title, text[6].description, 90)).addPair(new ConditionAlways(), new RandomChange(7, 4)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[7].title, text[7].description,80)).addPair(new ConditionAlways(), new ColorChange( 2, 7)));
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[8].title, text[8].description, 35, 3)));
                    zero.attackMode.addAttack(new ConditionHp(1,0), rndAtk);
                }
            }

        }
        mStageEnemies.put(9, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {2098, 1885};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 2098) { // checked
                    data.add(Enemy.create(env, 2098, 50000000, 59025, 760, 1,getMonsterAttrs(env,2098),getMonsterTypes(env, 2098)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[2].title, text[2].description, 10, 7)).addPair(new ConditionAlways(),new Daze(text[4].title, text[4].description, 10, 2, 500)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ShieldAction(text[5].title, text[5].description, 10, -1, 75)).addPair(new ConditionAtTurn(10), new SkillDown(text[6].title, text[6].description, 0, 10, 10)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(300)).addPair(new ConditionHp(2, 10), new Transform(text[7].title, text[7].description, 5,0)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(110)).addAction(new LineChange(text[8].title, text[8].description, 0, 6, 2, 6, 1, 8)).addAction(new Attack(10)).addPair(new ConditionUsed(), new NoDropAttack(text[9].title, text[9].description, 6)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    RandomAttack ra = new RandomAttack();

                    ra.addAttack(new EnemyAttack().addAction(new Attack(100)).addPair(new ConditionAlways(), new ColorChange(-1,5)));
                    ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[11].title,text[11].description,40,3)));
                    ra.addAttack(new EnemyAttack().addAction(new Attack(110)).addPair(new ConditionAlways(), new LockOrb(text[12].title, text[12].description, 0, 0, 5)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), ra);


                }
                if (choose == 1885) { // checked
                    data.add(Enemy.create(env, 1885, 60000000, 57360, 800, 1,getMonsterAttrs(env,1885),getMonsterTypes(env, 1885)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();

                    int x = RandomUtil.getInt(3);
                    int[] absorb = {1, 4, 5};
                    int[] target = {4, 5, 1};
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[2].title,text[2].description, 999)).addAction(new AbsorbShieldAction(text[4].title,text[4].description,2, absorb[x])).addAction(new LockRemoveAttack(text[6].title, text[6].description, 2, target[x])).addPair(new ConditionAlways(), new Attack(100)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockAwoken(text[7].title, text[7].description, 1)).addAction(new Attack(130)).addPair(new ConditionHp(0, 100), new LockRemoveAttack(text[8].title, text[8].description, 1, 2)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 10), new Gravity(text[9].title, text[9].description, 400)));

                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[10].title,text[10].description, 99,150)).addPair(new ConditionUsed(), new Gravity(text[11].title, text[11].description, 99)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    RandomAttack ra = new RandomAttack();
                    ra.addAttack(new EnemyAttack().addAction(new Attack(95)).addPair(new ConditionAlways(), new DarkScreen(text[14].title, text[14].description, 0)));
                    ra.addAttack(new EnemyAttack().addAction(new Attack(100)).addPair(new ConditionAlways(), new BindPets(text[15].title, text[15].description, 3, 5, 5, 1)));
                    ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ComboShield(text[16].title, text[16].description, 1, 9)));
                    zero.attackMode.addAttack(new ConditionModeSelection(2, 0), ra);

                    ra = new RandomAttack();
                    for(x=0; x<3; ++x) {
                        ra.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[4].title, text[4].description, 2, absorb[x])).addAction(new LockRemoveAttack(text[6].title, text[6].description, 2, target[x])).addPair(new ConditionAlways(), new Attack(100)));
                    }
                    zero.attackMode.addAttack(new ConditionModeSelection(2, 1), ra);


                }

            }

        }
        mStageEnemies.put(10, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {2393, 2395};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 2393) {
                    data.add(Enemy.create(env, 2393, 75300000, 56910, 1880, 1, getMonsterAttrs(env, 2393), getMonsterTypes(env, 2393)));
                    zero = data.get(i);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);

                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    // FIXME: heart bomb
                    seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[2].title, text[2].description, 999)).addPair(new ConditionAlways(), new RandomChange(text[4].title, text[4].description, 9, 12)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new DarkScreen(text[14].title, text[14].description, 0)).addPair(new ConditionNTurns(5, 1), new RandomChange(text[4].title, text[4].description, 9, 12)));

                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new RecoverSelf(text[5].title, text[5].description, 50)).addPair(new ConditionUsed(), new LockAwoken(text[6].title, text[6].description, 1)));
                    seq.addAttack(new EnemyAttack().addAction(new RecoverSelf(text[5].title, text[5].description, 50)).addPair(new ConditionUsed(), new Transform(text[8].title, text[8].description, 8)));
                    zero.attackMode.addAttack(new ConditionHp(2, 1), seq);


                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(text[9].title, text[9].description, 150)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(text[9].title, text[9].description, 150)));
                    zero.attackMode.addAttack(new ConditionHp(2, 15), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new RecoverPlayer(text[11].title, text[11].description, 100)).addAction(new Daze(text[12].title, text[12].description, 0)).addPair(new ConditionUsed(), new Transform(text[13].title, text[13].description, 6)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[17].title, text[17].description, 50, 2)).addPair(new ConditionAlways(), new RandomChange(text[16].title, text[16].description, 0, 3, 4, 3)));
                    seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[17].title, text[17].description, 50, 2)).addPair(new ConditionAlways(), new LineChange(text[18].title, text[18].description, 3, 4, 4, 0)));
                    seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[17].title, text[17].description, 50, 2)).addPair(new ConditionAlways(), new ShieldAction(text[20].title, text[20].description, 1, -1, 50)));

                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                }
                if (choose == 2395) { // checked
                    data.add(Enemy.create(env, 2395, 60000000, 55500, 1880, 1, getMonsterAttrs(env, 2395), getMonsterTypes(env, 2395)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[2].title, text[2].description, 999, 5)).addAction(new CloudAttack(text[4].title, text[4].description, 1, 6, 2)).addPair(new ConditionAlways(), new DropLockAttack(text[6].title, text[6].description, 10, 4, 100, 3, 100)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new SkillDown(text[8].title, text[8].description, 0, 3, 3)).addPair(new ConditionHp(0, 100), new MultipleAttack(text[7].title, text[7].description, 55, 2)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[9].title, text[9].description, 99, 200)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[10].title, text[10].description, 500)).addPair(new ConditionAlways(), new Transform(7)));
                    zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ShieldAction(text[12].title, text[12].description, 10, -1, 50)).addPair(new ConditionUsed(), new ResistanceShieldAction(text[11].title, text[11].description, 999)));

                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[14].title, text[14].description,95)).addAction(new LineChange( 7, 3)).addPair(new ConditionAlways(), new CloudAttack(text[13].title, text[13].description, 1, 6, 2)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[15].title, text[15].description,95)).addAction(new LineChange( 8, 3)).addPair(new ConditionAlways(), new CloudAttack(text[13].title, text[13].description, 1, 6, 2)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[16].title, text[16].description,95)).addAction(new LineChange( 9, 3)).addPair(new ConditionAlways(), new CloudAttack(text[13].title, text[13].description, 1, 6, 2)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[17].title, text[17].description,95)).addAction(new LineChange( 10, 3)).addPair(new ConditionAlways(), new CloudAttack(text[13].title, text[13].description, 1, 6, 2)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[18].title, text[18].description,100)).addAction(new LockOrb( 2, 10)).addPair(new ConditionAlways(), new CloudAttack(text[13].title, text[13].description, 1, 6, 2)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[19].title, text[19].description, 35, 3)).addPair(new ConditionAlways(), new CloudAttack(text[13].title, text[13].description, 1, 6, 2)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), rndAtk);
                }

            }

        }
        mStageEnemies.put(11, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {3074, 4417};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 3074) {
                    data.add(Enemy.create(env, 3074, 42000000, 17280, 10000000, 1, getMonsterAttrs(env, 3074), getMonsterTypes(env, 3074)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new SkillDown(text[0].title, text[0].description, 0, 4, 6)).addAction(new ResistanceShieldAction(text[1].title, text[1].description, 999)).addPair(new ConditionAlways(), new DamageAbsorbShield(text[2].title, text[2].description, 99, 4000000)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(
                            1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new ShieldAction(text[3].title, text[3].description, 2, -1, 99)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(
                            1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new MultipleAttack(text[4].title, text[4].description, 200, 6)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(
                            1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 20), new RecoverSelf(text[10].title, text[10].description, 50)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 20), new MultipleAttack(text[11].title, text[11].description, 1000, 10)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    RandomAttack ra = new RandomAttack();
                    ra.addAttack(new EnemyAttack().addPair(new ConditionPercentage(40), new Gravity(text[5].title, text[5].description, 150)));
                    ra.addAttack(new EnemyAttack()
                            .addAction(new SuperDark(text[6].title, text[6].description, 3, 1, 4, 2, 3, 2, 6, 3, 2, 3, 5, 4, 1, 4, 4, 5, 3)) //SuperDark
                            .addAction(new Attack(text[7].title, text[7].description, 420))
                            .addPair(new ConditionAlways(), new BindPets(3, 3, 3, 1)));
                    zero.attackMode.addAttack(new ConditionTurnsOffset(2, 4), ra);

                    ra = new RandomAttack();
                    ra.addAttack(new EnemyAttack()
                            .addAction(new Attack(text[8].title, text[8].description, 380))
                            .addPair(new ConditionAlways(), new LineChange(10, 3, 7, 5)));
                    ra.addAttack(new EnemyAttack()
                            .addAction(new ChangeAttribute(text[9].title, text[9].description, 5, 3))
                            .addPair(new ConditionAlways(), new Attack(380)));
                    zero.attackMode.addAttack(new ConditionHp(1, 20), ra);

                }
                if (choose == 4417) { // checked
                    data.add(Enemy.create(env, 4417, 88900000, 43904, 1656, 1, getMonsterAttrs(env, 4417), getMonsterTypes(env, 4417)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);


                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[3].title, text[3].description, 30)).addPair(new ConditionAlways(), new ShieldAction(text[5].title, text[5].description, 3, -1, 95)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();
                    //zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new SwitchLeader(text[6].title, text[6].description, 5)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[7].title, text[7].description, 40, 8)));
                    zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[8].title, text[8].description, 90)).addPair(new ConditionAlways(), new RandomChange(7, 8)));
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[9].title, text[9].description, 25, 4)));
                    zero.attackMode.addAttack(new ConditionModeSelection(3, 0, 1), rndAtk);

                    rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[8].title, text[8].description, 90)).addPair(new ConditionAlways(), new RandomChange(7, 8)));
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[9].title, text[9].description, 25, 4)));
                    zero.attackMode.addAttack(new ConditionModeSelection(3, 1, 2), rndAtk);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[10].title, text[10].description, 3, 120)).addAction(new Attack(110)).addPair(new ConditionUsed(), new ShieldAction(text[11].title, text[11].description, 3, -1, 90)));
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[12].title, text[12].description, 3, 140)).addAction(new Attack(110)).addPair(new ConditionUsed(), new ShieldAction(text[13].title, text[13].description, 3, -1, 80)));
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[14].title, text[14].description, 3, 160)).addAction(new Attack(110)).addPair(new ConditionUsed(), new ShieldAction(text[15].title, text[15].description, 3, -1, 70)));
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[16].title, text[16].description, 3, 180)).addAction(new Attack(110)).addPair(new ConditionUsed(), new ShieldAction(text[17].title, text[17].description, 3, -1, 60)));
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[18].title, text[18].description, 3, 200)).addAction(new Attack(110)).addPair(new ConditionUsed(), new ShieldAction(text[19].title, text[19].description, 3, -1, 50)));
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[20].title, text[20].description, 3, 220)).addAction(new Attack(110)).addPair(new ConditionUsed(), new ShieldAction(text[21].title, text[21].description, 3, -1, 40)));
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[22].title, text[22].description, 3, 240)).addAction(new Attack(110)).addPair(new ConditionUsed(), new ShieldAction(text[23].title, text[23].description, 3, -1, 30)));
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[24].title, text[24].description, 3, 260)).addAction(new Attack(110)).addPair(new ConditionUsed(), new ShieldAction(text[25].title, text[25].description, 3, -1, 20)));
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[26].title, text[26].description, 3, 280)).addAction(new Attack(110)).addPair(new ConditionUsed(), new ShieldAction(text[27].title, text[27].description, 3, -1, 10)));
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[28].title, text[28].description, 999, 300)).addPair(new ConditionUsed(), new CloudAttack(text[29].title, text[29].description, 10, 5, 4)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[30].title, text[30].description,90)).addPair(new ConditionAlways(), new RandomChange( 7, 8)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[9].title, text[9].description, 25, 4)));
                    zero.attackMode.addAttack(new ConditionModeSelection(3, 2, 0), seq);
                }
            }

        }
        mStageEnemies.put(12, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {1338, 4358};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 1338) { // checked
                    data.add(Enemy.create(env, 1338, 50700000, 42525, 800, 2, getMonsterAttrs(env, 1338), getMonsterTypes(env, 1338)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 75);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.CHANGE_TURN, 99, 1);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new BindPets(text[5].title, text[5].description, 1, 4, 4, 2)).addPair(new ConditionAlways(), new DamageAbsorbShield(text[7].title, text[7].description, 10, 5000000)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockAwoken(text[8].title, text[8].description, 2)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 1), new BindPets(text[9].title, text[9].description, 3, 2, 2, 6)));

                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 10), new MultipleAttack(text[10].title, text[10].description, 100, 3)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(1, 99), new LockAwoken(text[11].title, text[11].description, 2)));

                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[17].title, text[17].description,100)).addPair(new ConditionHp(2, 50), new ColorChange( 7, 8)));
                    zero.attackMode.addAttack(new ConditionFindOrb(7), seq);

                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[16].title, text[16].description, 100)).addAction(new ColorChange(0, 7)).addPair(new ConditionUsed(), new ShieldAction(text[15].title, text[15].description, 999, -1, 50)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[18].title, text[18].description,100)).addPair(new ConditionAlways(), new ColorChange( 0, 7)));
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[19].title, text[19].description, 99)));
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[20].title, text[20].description, 60, 2)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), rndAtk);

                    seq = new SequenceAttack();

                    seq.addAttack(new EnemyAttack().addAction(new Gravity(text[13].title, text[13].description, 99)).addPair(new ConditionUsed(), new LockSkill(text[12].title, text[12].description, 5)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[14].title, text[14].description,150)).addPair(new ConditionAlways(), new RandomChange( 7, 3)));

                    zero.attackMode.addAttack(new ConditionHp(1, 50), seq);
                }
                if (choose == 4358) {
                    data.add(Enemy.create(env, 4358, 106800000, 31242, 17777800, 2, getMonsterAttrs(env, 4358), getMonsterTypes(env, 4358)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.CHANGE_TURN, 20, 1);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[5].title, text[5].description, 10, 7)).addAction(new ReduceTime(text[7].title, text[7].description, 10, 1500, 0)).addPair(new ConditionAlways(), new Daze(text[9].title, text[9].description, 0)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);



                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[11].title, text[11].description, 99, 120)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 1), new RecoverSelf(text[10].title, text[10].description, 50)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[11].title, text[11].description, 100)).addPair(new ConditionHp(2,1), new RecoverSelf(text[10].title, text[10].description, 50)));
                    seq.addAttack(new EnemyAttack().addAction(new DamageVoidShield(text[13].title, text[13].description, 10, 5000000)).addPair(new ConditionAtTurn(4), new SwitchLeader(text[12].title, text[12].description, 10)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[15].title, text[15].description, 210)).addAction(new DarkScreen(0)).addPair(new ConditionUsed(), new ResistanceShieldAction(text[14].title, text[14].description, 4)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[16].title, text[16].description, 120)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new CloudAttack(4, 2, 3)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 20), new Daze(text[17].title, text[17].description, 0)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);


                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[18].title, text[18].description, 120, 10)));
                    zero.attackMode.addAttack(new ConditionHp(2,20), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[19].title, text[19].description,220)).addPair(new ConditionAlways(), new RandomChangeExcept( 6, 3, 2)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[20].title, text[20].description, 60, 4)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);

                }

            }

        }
        mStageEnemies.put(13, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {908, 1514};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 908) { // FIXME: add own ability for type reduce damage
                    data.add(Enemy.create(env, 908, 80000000, 21150, 300000, 1, getMonsterAttrs(env, 908), getMonsterTypes(env, 908)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockAwoken(text[3].title, text[3].description, 3)).addPair(new ConditionAlways(), new ResistanceShieldAction(text[5].title, text[5].description, 999)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new BindPets(text[6].title, text[6].description, 2, 5, 5, 1)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new LockSkill(text[7].title, text[7].description, 5)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[8].title, text[8].description, 120, 3)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[9].title, text[9].description, 150, 2)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);

                }
                if (choose == 1514) { // FIXME: ConditionCombo
                    data.add(Enemy.create(env, 1514, 40500000, 58155, 10000000, 1, getMonsterAttrs(env, 1514), getMonsterTypes(env, 1514)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new CloudAttack(text[2].title, text[2].description, 1, 6, 3)).addAction(new ComboShield(text[1].title, text[1].description, 999, 4)).addPair(new ConditionAlways(), new ResistanceShieldAction(text[0].title, text[0].description, 999)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();

                    FromHeadAttack fh = new FromHeadAttack();
                    fh.addAttack(new EnemyAttack().addPair(new ConditionHp(1, 99), new ReduceTime(text[3].title, text[3].description, 1, 250, 0)));
                    fh.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Daze(text[4].title, text[4].description, 0)));
                    zero.attackMode.addAttack(new ConditionModeSelection(4, 0), fh);

                    fh = new FromHeadAttack();
                    fh.addAttack(new EnemyAttack().addAction(new Attack(text[5].title, text[5].description,100)).addPair(new ConditionAlways(), new CloudAttack( 1, 4, 5)));
                    zero.attackMode.addAttack(new ConditionModeSelection(4, 1), fh);

                    fh = new FromHeadAttack();
                    fh.addAttack(new EnemyAttack().addPair(new ConditionHp(1, 99), new ReduceTime(text[3].title, text[3].description, 1, 250, 0)));
                    fh.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Daze(text[4].title, text[4].description, 0)));
                    zero.attackMode.addAttack(new ConditionModeSelection(4, 2), fh);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[8].title, text[8].description, 40, 5)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[9].title, text[9].description, 120)).addPair(new ConditionAlways(), new CloudAttack(1, 6, 3)));
                    zero.attackMode.addAttack(new ConditionModeSelection(4, 3), seq);
                }

            }

        }
        mStageEnemies.put(14, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {3318, 3319, 3320, 3321, 3322, 3323, 3324, 3325};
            text = loadSubText(list, 3318);
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                //
                data.add(Enemy.create(env, choose, 50, 67500, 30000000, 1, getMonsterAttrs(env, choose), getMonsterTypes(env, choose)));
                zero = data.get(i);


                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new SkillDown(text[0].title, text[0].description, 0, 3, 3)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[1].title, text[1].description, 1000)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

            }

        }
        mStageEnemies.put(15, data);
        data = new ArrayList<Enemy>();
        {
            int[] ids = {2560, 3200};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 2560) { // checked
                    data.add(Enemy.create(env, 2560, 61764244, 42356, 960, 1, getMonsterAttrs(env, 2560), getMonsterTypes(env, 2560)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();

                    seq = new SequenceAttack();
                    int[] color = {1, 4, 5, 3};
                    int attr = RandomUtil.chooseOne(color);
                    int[] inv = {3,4,0,0,5,1};
                    int[] tid = {0,2,0,8,4,6};
                    int[] aid = {0,10,0,16,12,14};
                    seq.addAttack(new EnemyAttack()
                            .addAction(new ChangeAttribute(text[tid[attr]].title, text[tid[attr]].description, attr))
                            .addAction(new AbsorbShieldAction(text[aid[attr]].title, text[aid[attr]].description, 3, inv[attr]))
                            .addPair(new ConditionAlways(), new LockOrb(text[18].title, text[18].description, 0, 2)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[19].title, text[19].description, 140)).addPair(new ConditionAtTurn(1), new Transform(6)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);


                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[20].title, text[20].description,680)).addPair(new ConditionAlways(), new Transform( 6)));
                    zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new DropRateAttack(text[23].title, text[23].description, 5, 6, 15)).addAction(new Angry(text[22].title, text[22].description, 999, 150)).addPair(new ConditionUsed(), new ChangeAttribute(text[21].title, text[21].description, 0)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[25].title, text[25].description, 50, 3)).addPair(new ConditionAlways(), new CloudAttack(text[24].title, text[24].description, 2, 2, 2)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[26].title, text[26].description, 140)).addPair(new ConditionAlways(), new BindPets(3, 1, 1, 1)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[27].title, text[27].description, 140)).addPair(new ConditionAlways(), new BindPets(3, 1, 1, 1)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                }
                if (choose == 3200) { // checked
                    data.add(Enemy.create(env, 3200, 120000000, 91250, 1440, 1, getMonsterAttrs(env, 3200), getMonsterTypes(env, 3200)));
                    zero = data.get(i);


                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[0].title, text[0].description, 100)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[1].title, text[1].description, 100)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);
                }
            }
        }
        mStageEnemies.put(16, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {3903, 4013};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 3903) { // checked
                    data.add(Enemy.create(env, 3903, 110000000, 18720, 4360, 1, getMonsterAttrs(env, 3903), getMonsterTypes(env, 3903)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[3].title, text[3].description, 999)).addAction(new DamageVoidShield(text[5].title, text[5].description, 999, 30000000)).addPair(new ConditionAlways(), new Attack(text[7].title, text[7].description, 700)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[9].title, text[9].description, 999, 150)).addPair(new ConditionUsed(), new RecoverSelf(text[8].title, text[8].description, 100)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(100)).addPair(new ConditionAlways(), new RecoverSelf(text[8].title, text[8].description, 100)));
                    zero.attackMode.addAttack(new ConditionHp(2, 1), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Daze(text[10].title, text[10].description, 0)).addAction(new Gravity(text[11].title, text[11].description, 99)).addAction(new DarkScreen(0)).addPair(new ConditionUsed(), new Attack(text[12].title, text[12].description, 100)));
                    seq.addAttack(new EnemyAttack().addAction(new ReduceTime(text[14].title, text[14].description, 10, -1000)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new ComboShield(text[13].title, text[13].description, 999, 7)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[15].title, text[15].description,350)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new SuperDark( 2, -1, 6, 6)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new MultipleAttack(text[16].title, text[16].description, 80, 6)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new Attack(text[17].title, text[17].description, 700)));
                    zero.attackMode.addAttack(new ConditionHp(1,10), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[23].title, text[23].description, 6000, 10)).addPair(new ConditionUsed(), new ChangeAttribute(text[22].title, text[22].description, 1, 4, 5, 3, 0)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[24].title, text[24].description, 5000, 10)));
                    zero.attackMode.addAttack(new ConditionHp(2, 10), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[18].title, text[18].description, 110, 3)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[19].title, text[19].description, 280)).addPair(new ConditionAlways(), new SkillDown(0, 0, 3)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[20].title, text[20].description, 80, 5)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[21].title, text[21].description, 300)).addPair(new ConditionAlways(), new Daze(0)));
                    zero.attackMode.addAttack(new ConditionHp(1, 9), seq);
                }
                if (choose == 4013) { // checked
                    data.add(Enemy.create(env, 4013, 800000000, 45015, 4360, 1, getMonsterAttrs(env, 4013), getMonsterTypes(env, 4013)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[3].title, text[3].description, 999)).addPair(new ConditionAlways(), new DamageVoidShield(text[5].title, text[5].description, 999, 100000000)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockAwoken(text[7].title, text[7].description, 10)).addPair(new ConditionUsed(), new Angry(text[6].title, text[6].description, 999, 1000)));
                    zero.attackMode.addAttack(new ConditionHp(2, 10), seq);


                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LineChange(text[9].title, text[9].description, 8, 0, 9, 3)).addAction(new Attack(250)).addPair(new ConditionUsed(), new ChangeAttribute(text[8].title, text[8].description, 1, 4, 5)));
                    seq.addAttack(new EnemyAttack().addAction(new ReduceTime(text[11].title, text[11].description, 10, -1000)).addAction(new Attack(150)).addPair(new ConditionUsed(), new ChangeAttribute(text[10].title, text[10].description, 0)));

                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[12].title, text[12].description, 16, 6, 10)));
                    zero.attackMode.addAttack(new ConditionModeSelection(6, 0, 1), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[12].title, text[12].description, 16, 6, 10)));
                    zero.attackMode.addAttack(new ConditionModeSelection(6, 1, 2), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[12].title, text[12].description, 16, 6, 10)));
                    zero.attackMode.addAttack(new ConditionModeSelection(6, 2, 3), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[12].title, text[12].description, 16, 6, 10)));
                    zero.attackMode.addAttack(new ConditionModeSelection(6, 3, 4), seq);
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[12].title, text[12].description, 16, 6, 10)));
                    zero.attackMode.addAttack(new ConditionModeSelection(6, 4, 5), seq);
                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[13].title, text[13].description, 140)).addPair(new ConditionAlways(), new SuperDark(3, -1, 5, 5)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[14].title, text[14].description, 140)).addPair(new ConditionAlways(), new RandomChange(8, 3)));
                    zero.attackMode.addAttack(new ConditionModeSelection(6, 5, 0), rndAtk);

                }

            }

        }
        mStageEnemies.put(17, data);


        data = new ArrayList<Enemy>();
        {
            int[] ids = {5175, 5177, 5179, 5181, 5183};
            int choose = RandomUtil.chooseOne(ids);
            text = loadSubText(list, choose);
            if (choose == 5175) { // ok
                data.add(Enemy.create(env, 5175, 500000000, 52785, 4400, 1,getMonsterAttrs(env,5175),getMonsterTypes(env, 5175)));
                zero = data.get(0);

                zero.attackMode = new EnemyAttackMode();
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 90);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 1, 50);


                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[4].title,text[4].description, 999)).addAction(new ComboShield(text[6].title, text[6].description, 1, 9)).addPair(new ConditionAlways(),new DamageVoidShield(text[8].title,text[8].description, 999, 50000000)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[9].title, text[9].description, 99, 7)).addAction(new Daze(text[10].title, text[10].description, 0)).addAction(new Attack(100)).addPair(new ConditionUsed(), new Daze(text[11].title, text[11].description, 0)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[12].title, text[12].description, 1,4,5,3,0)).addPair(new ConditionAlways(), new MultipleAttack(text[13].title,text[13].description,300,5)));
                zero.attackMode.addAttack(new ConditionHp(2,10), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new HpChangeAttack(text[14].title, text[14].description, 1, 3, 50)).addPair(new ConditionAlways(), new Attack(110)));
                zero.attackMode.addAttack(new ConditionHp(0,100), seq);


                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new LockAwoken(text[15].title, text[15].description, 10)).addPair(new ConditionUsed(), new Angry(text[16].title,text[16].description, 999,200)));
                zero.attackMode.addAttack(new ConditionHp(2,50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ReduceTime(text[17].title, text[17].description, 99, -2000)).addAction(new HpChangeAttack(text[14].title, text[14].description, 1, 3, 50)).addPair(new ConditionUsed(), new Attack(110)));
                zero.attackMode.addAttack(new ConditionHp(2, 75), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Daze(text[19].title, text[19].description, 0)).addAction(new Attack(100)).addPair(new ConditionAlways(), new Daze(text[20].title, text[20].description, 0)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);


            }else
            if (choose == 5177) { // ok
                data.add(Enemy.create(env, 5177, 450000000, 60690, 4400, 1,getMonsterAttrs(env,5177),getMonsterTypes(env, 5177)));
                zero = data.get(0);

                zero.attackMode = new EnemyAttackMode();
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 90);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);

                int color = RandomUtil.chooseOne(new int[]{1, 4, 5, 3, 0});
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[4].title,text[4].description, 999)).addAction(new DamageVoidShield(text[6].title,text[6].description, 999, 25000000)).addAction(new LockRemoveAttack(text[8].title, text[8].description, 1, color)).addPair(new ConditionAlways(),new Attack(120)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);


                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new IntoVoid(text[9].title,text[9].description,0)).addPair(new ConditionHp(2, 10), new MultipleAttack(text[10].title,text[10].description,400,4)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new SuperDark(text[11].title, text[11].description, 5, -1, 15, 15)).addPair(new ConditionUsed(), new CloudAttack(text[12].title,text[12].description, 10, 2, 5)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                RandomAttack ra = new RandomAttack();
                ra.addAttack(new EnemyAttack().addAction(new Attack(50)).addAction(new RandomLineChange(text[14].title, text[14].description, 3, 4, 6, 7, 2, 10, 15)).addAction(new Attack(50)).addPair(new ConditionAlways(), new ShieldAction(text[15].title, text[15].description, 1, -1, 50)));
                ra.addAttack(new EnemyAttack().addAction(new Attack(50)).addAction(new RandomLineChange(text[14].title, text[14].description, 3, 4, 6, 7, 2, 4, 0)).addAction(new Attack(50)).addPair(new ConditionAlways(), new BindPets(text[17].title, text[17].description, 2, 1, 2, 2)));
                zero.attackMode.addAttack(new ConditionModeSelection(2, 0), ra);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new LockRemoveAttack(text[8].title, text[8].description, 1, color)).addPair(new ConditionAlways(),new Attack(120)));
                zero.attackMode.addAttack(new ConditionModeSelection(2, 1), seq);
            } else
            if (choose == 5179) { // ok
                data.add(Enemy.create(env, 5179, 250000000, 57000, 13800000, 1,getMonsterAttrs(env,5179),getMonsterTypes(env, 5179)));
                zero = data.get(0);

                zero.attackMode = new EnemyAttackMode();
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 90);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 5, 50);


                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new PositionChange(text[3].title, text[3].description, 29, 1, 1, 1, 2, 1, 4, 1, 1, 3, 3, 3, 4, 3)).addAction(new DamageVoidShield(text[6].title,text[6].description, 999, 25000000)).addPair(new ConditionAlways(),new SwitchLeader(text[8].title, text[8].description, 8)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new SwitchLeader(text[9].title, text[9].description, 1)).addPair(new ConditionAlways(), new MultipleAttack(text[10].title,text[10].description,300,6,9)));
                zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ShieldAction(text[11].title, text[11].description, 8, -1, 50)).addPair(new ConditionUsed(), new ResistanceShieldAction(text[12].title,text[12].description, 999)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                RandomAttack ra = new RandomAttack();
                ra.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[16].title,text[16].description,20,3)).addAction(new Attack(50)).addPair(new ConditionAlways(), new BindPets(text[17].title, text[17].description, 3, 1, 2, 2)));
                ra.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[16].title,text[16].description,20,3)).addAction(new Attack(45)).addPair(new ConditionAlways(), new SkillDown(text[19].title, text[19].description, 0, 1, 2)));
                ra.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[16].title,text[16].description,20,3)).addAction(new Attack(40)).addPair(new ConditionAlways(), new LockOrb(text[21].title, text[21].description, 2, 15)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), ra);

            } else
            if (choose == 5181) { // ok
                data.add(Enemy.create(env, 5181, 400000000, 52530, 4400, 1,getMonsterAttrs(env,5181),getMonsterTypes(env, 5181)));
                zero = data.get(0);

                zero.attackMode = new EnemyAttackMode();
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 90);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);


                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[4].title,text[4].description, 999)).addAction(new DamageVoidShield(text[6].title,text[6].description, 999, 25000000)).addPair(new ConditionAlways(),new ShieldAction(text[8].title, text[8].description, 4, -1, 90)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();

                seq.addAttack(new EnemyAttack().addAction(new DarkScreen(text[9].title, text[9].description, 0)).addPair(new ConditionUsed(), new Daze(text[10].title, text[10].description, 0)));
                seq.addAttack(new EnemyAttack().addAction(new Attack(800)).addPair(new ConditionAlways(), new Transform(text[11].title, text[11].description, 3)));

                zero.attackMode.addAttack(new ConditionHp(2, 15), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new LockAwoken(text[12].title, text[12].description, 2)).addPair(new ConditionAlways(), new Attack(80)));
                zero.attackMode.addAttack(new ConditionModeSelection(5, 0), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(80)).addPair(new ConditionAlways(), new CloudAttack(text[13].title,text[13].description, 4, 3, 2)));
                zero.attackMode.addAttack(new ConditionModeSelection(5, 1), seq);

                RandomAttack ra = new RandomAttack();
                ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new IntoVoid(text[14].title,text[14].description,0)));
                ra.addAttack(new EnemyAttack().addAction(new HpChangeAttack(text[15].title, text[15].description, 1, 1, 50000)).addPair(new ConditionAlways(), new MultipleAttack(text[16].title,text[16].description,30,3)));
                ra.addAttack(new EnemyAttack().addAction(new Attack(80)).addPair(new ConditionAlways(), new RecoveryUpAction(text[17].title, text[17].description, 2, 50)));
                ra.addAttack(new EnemyAttack().addAction(new Attack(84)).addPair(new ConditionAlways(), new ChangeAttribute(text[18].title, text[18].description, 1,4,5,3,0)));
                zero.attackMode.addAttack(new ConditionModeSelection(5, 2), ra);

                ra = new RandomAttack();
                ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new IntoVoid(text[14].title,text[14].description,0)));
                ra.addAttack(new EnemyAttack().addAction(new HpChangeAttack(text[15].title, text[15].description, 1, 1, 50000)).addPair(new ConditionAlways(), new MultipleAttack(text[16].title,text[16].description,30,3)));
                ra.addAttack(new EnemyAttack().addAction(new Attack(80)).addPair(new ConditionAlways(), new RecoveryUpAction(text[17].title, text[17].description, 2, 50)));
                ra.addAttack(new EnemyAttack().addAction(new Attack(84)).addPair(new ConditionAlways(), new ChangeAttribute(text[18].title, text[18].description, 1,4,5,3,0)));
                zero.attackMode.addAttack(new ConditionModeSelection(5, 3), ra);

                ra = new RandomAttack();
                ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new IntoVoid(text[14].title,text[14].description,0)));
                ra.addAttack(new EnemyAttack().addAction(new HpChangeAttack(text[15].title, text[15].description, 1, 1, 50000)).addPair(new ConditionAlways(), new MultipleAttack(text[16].title,text[16].description,30,3)));
                ra.addAttack(new EnemyAttack().addAction(new Attack(80)).addPair(new ConditionAlways(), new RecoveryUpAction(text[17].title, text[17].description, 2, 50)));
                ra.addAttack(new EnemyAttack().addAction(new Attack(84)).addPair(new ConditionAlways(), new ChangeAttribute(text[18].title, text[18].description, 1,4,5,3,0)));
                zero.attackMode.addAttack(new ConditionModeSelection(5, 4), ra);


            }
            else if (choose == 5183) { // ok
                data.add(Enemy.create(env, 5183, 600000000, 58050, 4400, 1,getMonsterAttrs(env,5183),getMonsterTypes(env, 5183)));
                zero = data.get(0);

                zero.attackMode = new EnemyAttackMode();
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 90);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);


                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[4].title,text[4].description, 999)).addAction(new DamageVoidShield(text[6].title,text[6].description, 999, 60000000)).addPair(new ConditionAlways(),new DropRateAttack(text[8].title, text[8].description, 1, 8,60)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new ReduceTime(text[9].title, text[9].description, 1, 250, 0)).addAction(new Attack(800)).addPair(new ConditionHp(2,10),new Transform(text[10].title, text[10].description, 8)));
                seq.addAttack(new EnemyAttack().addAction(new Attack(30)).addAction(new ShieldAction(text[12].title, text[12].description, 1, -1, 75)).addPair(new ConditionUsed(), new DropRateAttack(text[11].title, text[11].description, 30, 6,15)));

                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(150)).addAction(new Transform(text[14].title, text[14].description, 6)).addPair(new ConditionUsed(), new BindPets(text[13].title, text[13].description, 3, 5, 5, 6)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                RandomAttack ra = new RandomAttack();
                ra.addAttack(new EnemyAttack().addAction(new Attack(130)).addPair(new ConditionAlways(), new SuperDark(text[15].title, text[15].description, 3, -1, 3, 3)));
                ra.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[16].title,text[16].description,45,3)));
                zero.attackMode.addAttack(new ConditionHp(1,10), ra);

            }

        }
        mStageEnemies.put(18, data);


        data = new ArrayList<Enemy>();
        {
            int[] ids = {3638, 3640, 3642};
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 3638) { // checked
                    data.add(Enemy.create(env, 3638, 312000000, 18425, 5056, 1, getMonsterAttrs(env, 3638), getMonsterTypes(env, 3638)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 5, 50);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[4].title, text[4].description, 999)).addPair(new ConditionAlways(), new CloudAttack(text[6].title, text[6].description, 30, -1, 1, 0)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[10].title, text[10].description,550)).addPair(new ConditionUsed(), new DropRateAttack( 15, 8, 15)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new ComboShield(text[11].title, text[11].description, 999, 6)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[8].title, text[8].description, 3000, 10)).addPair(new ConditionHp(2,1), new RecoverSelf(text[7].title, text[7].description, 100)));
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2,10), new MultipleAttack(text[9].title, text[9].description, 3000, 10)));

                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[12].title, text[12].description, 340)).addPair(new ConditionAlways(), new RandomChange(4, 9)));
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[13].title, text[13].description, 50, 8)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[14].title, text[14].description,360)).addPair(new ConditionAlways(), new ReduceTime( 1, 250, 0)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[15].title, text[15].description, 380)).addPair(new ConditionAlways(), new LockOrb(2, 15)));
                    zero.attackMode.addAttack(new ConditionHp(1,0), rndAtk);


                }
                if (choose == 3640) { // checked
                    data.add(Enemy.create(env, 3640, 468000000, 19902, 5056, 1, getMonsterAttrs(env, 3640), getMonsterTypes(env, 3640)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 1, 50);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 5, 50);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[3].title, text[3].description, 999)).addPair(new ConditionAlways(), new SuperDark(text[5].title, text[5].description, 30, -1)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2,10), new MultipleAttack(text[6].title, text[6].description, 3000, 10)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[7].title, text[7].description, 99)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[8].title, text[8].description,320)).addPair(new ConditionAlways(), new ChangeAttribute( 1, 4, 5, 3, 0)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[9].title, text[9].description, 310)).addPair(new ConditionAlways(), new BindPets(3, 10, 10, 3)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[10].title, text[10].description,60)).addPair(new ConditionAlways(), new LockRemoveAttack( 2, 2)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[11].title, text[11].description,380)).addPair(new ConditionAlways(), new AbsorbShieldAction( 5, 1)));
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[12].title, text[12].description, 150, 3)));

                    zero.attackMode.addAttack(new ConditionHp(1, 0), rndAtk);
                }
                if (choose == 3642) {
                    data.add(Enemy.create(env, 3642, 780000000, 82607, 6688, 4, getMonsterAttrs(env, 3642), getMonsterTypes(env, 3642)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.CHANGE_TURN, 70);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new DamageVoidShield(text[5].title, text[5].description, 999, 30000000)).addPair(new ConditionAlways(), new DropLockAttack(text[7].title, text[7].description, 99, -1, 100)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[8].title, text[8].description,100)).addPair(new ConditionHp(1,70), new Daze( 0)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[11].title, text[11].description, 999, 1000)));
                    zero.attackMode.addAttack(new ConditionHp(2, 10), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new LockSkill(text[10].title, text[10].description, 10)).addPair(new ConditionUsed(), new ResistanceShieldAction(text[9].title, text[9].description, 999)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[12].title, text[12].description,120)).addPair(new ConditionAlways(), new DropRateAttack( 1, 7, 30)));
                    seq.addAttack(new EnemyAttack().addAction(new LockAwoken(text[13].title, text[13].description,1)).addPair(new ConditionAlways(), new Attack( 110)));
                    seq.addAttack(new EnemyAttack().addAction(new SuperDark(text[14].title, text[14].description, 1, 0, 0, 0, 2, 1, 0, 0)).addPair(new ConditionAlways(), new Attack(90)));
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[15].title, text[15].description,100)).addPair(new ConditionAlways(), new Daze( 0)));

                    zero.attackMode.addAttack(new ConditionHp(2, 70), seq);
                }

            }

        }
        mStageEnemies.put(19, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {4585, 4742}; // TODO: add 5297, 5297
            for (int i = 0; i < 1; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                text = loadSubText(list, choose);
                if (choose == 4585) { // checked
                    data.add(Enemy.create(env, 4585, 1500000000, 120300, 3120, 2, getMonsterAttrs(env, 4585), getMonsterTypes(env, 4585)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.CHANGE_TURN, 50, 1);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);


                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ResistanceShieldAction(text[5].title, text[5].description, 999)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHasBuff(), new IntoVoid(text[13].title, text[13].description, 0)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    int x = RandomUtil.getInt(5);
                    int[] attr1 = {1, 5, 4, 3, 1};
                    int[] attr2 = {4, 0, 3, 0, 5};
                    seq.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[6 + x].title, text[6 + x].description, 5, attr2[x])).addPair(new ConditionUsed(), new AbsorbShieldAction(5, attr1[x])));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new LockAwoken(text[11].title, text[11].description, 5)));
                    zero.attackMode.addAttack(new ConditionHp(2, 15), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[12].title, text[12].description, 500, 6)));
                    zero.attackMode.addAttack(new ConditionHp(2, 10), seq);

                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[14].title, text[14].description, 50, 2)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[15].title, text[15].description, 90)).addPair(new ConditionAlways(), new Transform(3, 2, 6, 7)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[16].title, text[16].description,90)).addPair(new ConditionAlways(), new Daze( 0)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[17].title, text[17].description, 95)).addPair(new ConditionAlways(), new SuperDark(2, -1, 5, 5)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), rndAtk);

                } else if (choose == 4742) { // checked
                    data.add(Enemy.create(env, 4742, 800000000, 112560, 2280, 1, getMonsterAttrs(env, 4742), getMonsterTypes(env, 4742)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);
                    zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 1, 50);


                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[4].title, text[4].description, 999)).addPair(new ConditionAlways(), new DamageVoidShield(text[6].title, text[6].description, 999, 20000000)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(new ConditionHasBuff(), new IntoVoid(text[10].title, text[10].description, 0)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Angry(text[8].title, text[8].description, 99, 200)).addPair(new ConditionUsed(), new RecoverSelf(text[7].title, text[7].description, 50)));
                    zero.attackMode.addAttack(new ConditionHp(2, 10), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(new Attack(text[9].title, text[9].description, 140)).addPair(new ConditionUsed(), new Transform(1)));
                    zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                    RandomAttack rndAtk = new RandomAttack();
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[11].title, text[11].description, 99)));
                    rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[12].title, text[12].description,70)).addPair(new ConditionAlways(), new SuperDark( 4, -1, 1, 3)));
                    rndAtk.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[13].title, text[13].description, 15, 5)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), rndAtk);
                } else {

                }

            }

        }
        mStageEnemies.put(20, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = {3891, 1547, 1548, 1549, 1550, 1551}; // checked
            for (int i = 0; i < 3; ++i) {

                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                if(choose == 3891) {
                    data.add(Enemy.create(env, choose, 48, 23121, 24000000, 1, getMonsterAttrs(env, choose), getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                } else {
                    data.add(Enemy.create(env, choose, 24, 11561, 12000000, 1, getMonsterAttrs(env, choose), getMonsterTypes(env, choose)));
                    zero = data.get(i);

                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                }
            }

        }
        mStageEnemies.put(21, data);


    }
}
