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
import com.a30corner.combomaster.playground.action.impl.ColorChangeNew;
import com.a30corner.combomaster.playground.action.impl.ComboShield;
import com.a30corner.combomaster.playground.action.impl.DamageAbsorbShield;
import com.a30corner.combomaster.playground.action.impl.DarkScreen;
import com.a30corner.combomaster.playground.action.impl.Daze;
import com.a30corner.combomaster.playground.action.impl.DropRateAttack;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.IntoVoid;
import com.a30corner.combomaster.playground.action.impl.LineChange;
import com.a30corner.combomaster.playground.action.impl.LockSkill;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.RecoverPlayer;
import com.a30corner.combomaster.playground.action.impl.RecoverSelf;
import com.a30corner.combomaster.playground.action.impl.ReduceTime;
import com.a30corner.combomaster.playground.action.impl.ResistanceShieldAction;
import com.a30corner.combomaster.playground.action.impl.ShieldAction;
import com.a30corner.combomaster.playground.action.impl.SkillDown;
import com.a30corner.combomaster.playground.action.impl.SwitchLeader;
import com.a30corner.combomaster.playground.action.impl.Transform;
import com.a30corner.combomaster.playground.enemy.EnemyAttack;
import com.a30corner.combomaster.playground.enemy.EnemyAttackMode;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.enemy.attack.RandomAttack;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAlways;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFindAttr;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHp;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionIf;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionPercentage;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionTurns;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsedIf;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class UltimateArena1 extends IStage {

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

        int stageCounter = 0;

        // level1
        data = new ArrayList<Enemy>();
        for (int i = 0; i < 2; ++i) {
            int rndId = RandomUtil.range(1526, 1530);
            if (rndId == 1526) {
                data.add(Enemy.create(env, 1526, 1297536, 6827, 960, 1,
                        new Pair<Integer, Integer>(1, -1),
                        getMonsterTypes(env, 1526)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createEmptyFirstStrike()
                        .createEmptyMustAction();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[1].title, text[1].description, -1,
                                7)).addPair(new ConditionAlways(),
                        new Attack(80)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[0].title, text[0].description,
                                20, 5, 8)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

            } else if (rndId == 1527) {
                data.add(Enemy.create(env, rndId, 869758, 8111, 960, 1,
                        new Pair<Integer, Integer>(4, -1),
                        getMonsterTypes(env, rndId)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new AbsorbShieldAction(text[2].title,
                                text[2].description, 5, 0)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[3].title, text[3].description, 2, 1,
                                2, 3)).addPair(new ConditionAlways(),
                        new Attack(50)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(2, 75), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 75), seq);

            } else if (rndId == 1528) {
                data.add(Enemy.create(env, rndId, 441980, 7170, 48000, 1,
                        new Pair<Integer, Integer>(5, -1),
                        getMonsterTypes(env, rndId)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new DarkScreen(text[4].title, text[4].description, 0))
                        .addPair(new ConditionAlways(), new Attack(50)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[5].title, text[5].description,
                                20, 6)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            } else if (rndId == 1529) {
                data.add(Enemy.create(env, rndId, 1725313, 13244, 0, 4,
                        new Pair<Integer, Integer>(3, -1),
                        getMonsterTypes(env, rndId)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Angry(text[6].title, text[6].description, 5, 600)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Angry(text[6].title, text[6].description, 5, 600)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            } else if (rndId == 1530) {
                data.add(Enemy.create(env, rndId, 1725313, 13244, 0, 4,
                        new Pair<Integer, Integer>(0, -1),
                        getMonsterTypes(env, rndId)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new BindPets(text[7].title, text[7].description, 2, 2,
                                4, 1)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[8].title, text[8].description, -1,
                                7)).addPair(new ConditionAlways(),
                        new Attack(110)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data);
        data = new ArrayList<Enemy>();
        {
            int[] L2 = { 812, 814, 1307, 1945 };
            int choose = L2[RandomUtil.getInt(L2.length)];
            if (choose == 812) {
                data.add(Enemy.create(env, choose, 3710288, 16102, 804, 1,
                        new Pair<Integer, Integer>(3, 4),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new LockSkill(text[9].title, text[9].description, 10)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new ColorChange(text[13].title, text[13].description,
                                -1, 7, -1, 7, -1, 7)));
                // 森羅万象
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[14].title,
                                text[14].description, 50, 5)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                seq = new SequenceAttack();
                // 母なる大海
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[12].title, text[12].description,
                                1, 4)).addPair(new ConditionAlways(),
                        new Attack(110)));
                // 幼龍神の息吹
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[10].title, text[10].description,
                                5, 1)).addPair(new ConditionAlways(),
                        new Attack(90)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                seq = new SequenceAttack();
                // 大いなる神光
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[11].title, text[11].description,
                                0, 3)).addPair(new ConditionAlways(),
                        new Attack(100)));
                // 幼龍神の息吹
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[10].title, text[10].description,
                                5, 1)).addPair(new ConditionAlways(),
                        new Attack(90)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (choose == 814) {
                data.add(Enemy.create(env, choose, 5421399, 14562, 804, 1,
                        new Pair<Integer, Integer>(0, 1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1,
                                1, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 0, 0),
                        new ReduceTime(text[46].title, text[46].description,
                                10, -2000)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1,
                                0, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 0, 0),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1,
                                1, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 0, 0),
                        new ReduceTime(text[46].title, text[46].description,
                                10, -2000)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new BindPets(text[50].title, text[50].description, 4,
                                1, 3, 1, 6)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new RandomChange(text[47].title, text[47].description,
                                6, 8)));
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[48].title, text[48].description,
                                -1, 7)).addPair(new ConditionAlways(),
                        new Attack(100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[49].title,
                                text[49].description, 30, 5)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (choose == 1307) {
                data.add(Enemy.create(env, choose, 3768466, 14048, 564, 1,
                        new Pair<Integer, Integer>(4, 0),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();

                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ComboShield(text[15].title, text[15].description,
                                999, 4)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 25),
                        new Attack(text[19].title, text[19].description, 400)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new ColorChange(text[18].title, text[18].description,
                                -1, 7, -1, 7, -1, 7)));
                zero.attackMode.addAttack(new ConditionHp(2, 75), seq);

                RandomAttack rndAtk = new RandomAttack();
                rndAtk.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[16].title, text[16].description,
                                5, 4)).addPair(
                        new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                                .ordinal(), 5), new Attack(100)));
                rndAtk.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[17].title, text[17].description,
                                4, 2)).addPair(
                        new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                                .ordinal(), 4), new Attack(150)));
                rndAtk.addAttack(new EnemyAttack().addPair(
                        new ConditionPercentage(50), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), rndAtk);
            } else if (choose == 1945) {
                data.add(Enemy.create(env, choose, 3725688, 12765, 564, 1,
                        new Pair<Integer, Integer>(5, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 75);

                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new RecoverPlayer(text[20].title, text[20].description,
                                100)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 1),
                        new RecoverSelf(text[21].title, text[21].description,
                                100)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                // 夜穿天昇
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new LineChange(text[27].title,
                                        text[27].description, 8, 3))
                        .addAction(new LineChange(9, 3))
                        .addPair(new ConditionAlways(), new Attack(200)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.FIND_TYPE.ordinal(), 1, 1),
                        new BindPets(text[22].title, text[22].description, 4,
                                2, 3, 1, 1)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 1, EnemyCondition.Type.FIND_TYPE.ordinal(), 1, 1),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(2, 75), seq);

                RandomAttack rnd = new RandomAttack();
                rnd.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[23].title, text[23].description,
                                0, 2)).addPair(new ConditionAlways(),
                        new Attack(125)));
                rnd.addAttack(new EnemyAttack().addAction(
                        new Transform(text[24].title, text[24].description, 1,
                                4, 5, 3, 0)).addPair(new ConditionAlways(),
                        new Attack(75)));
                rnd.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[25].title, text[25].description,
                                2, 1)).addPair(new ConditionAlways(),
                        new Attack(75)));
                rnd.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Gravity(text[26].title, text[26].description, 99)));
                rnd.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rnd);
            }
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 823, 826, 1258, 1062 };
            int choose = ids[RandomUtil.getInt(ids.length)];
            if (choose == 823) {
                data.add(Enemy.create(env, choose, 2312567, 7188, 3600, 1,
                        new Pair<Integer, Integer>(4, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                // TODO: if can not work, attack !
                seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1,
                                1, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 0, 0),
                        new ReduceTime(text[28].title, text[28].description, 3,
                                -1000)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1,
                                0, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 0, 0),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(text[30].title, text[30].description, 80)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[31].title,
                                text[31].description, 80, 2)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[32].title,
                                text[32].description, 80, 3)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[33].title,
                                text[33].description, 80, 4)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[34].title,
                                text[34].description, 500, 4)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (choose == 826) {
                data.add(Enemy.create(env, choose, 2654789, 13603, 3600, 1,
                        new Pair<Integer, Integer>(0, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new AbsorbShieldAction(text[35].title,
                                text[35].description, 3, 0)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[36].title, text[36].description,
                                7, 0)).addPair(
                        new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                                .ordinal(), 7), new Attack(200)));
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[37].title, text[37].description,
                                -1, 7)).addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            } else if (choose == 1258) {
                data.add(Enemy.create(env, choose, 5141632, 21748, 0, 2,
                        new Pair<Integer, Integer>(4, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Gravity(text[38].title, text[38].description, 99)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Angry(text[39].title, text[39].description, 999,
                                300)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[40].title, text[40].description,
                                -1, 7)).addPair(new ConditionAlways(),
                        new Attack(80)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[41].title,
                                text[41].description, 50, 3)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (choose == 1062) {
                data.add(Enemy.create(env, choose, 2571543, 27652, 0, 2,
                        new Pair<Integer, Integer>(0, 5),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new DropRateAttack(text[42].title,
                                text[42].description, 10, 6, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.FIND_TYPE.ordinal(), 1, 0),
                        new BindPets(text[44].title, text[44].description, 4,
                                3, 3, 1, 0)));
                seq.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[45].title, text[45].description,
                                6, 3)).addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[43].title, text[43].description,
                                -1, 7)).addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 475, 476, 477, 478, 479 };
            int[] hp = { 1871189, 1859661, 1848133, 1894244, 1905772 };
            int[] atk = { 22548, 22226, 21857, 22917, 23240 };
            int[] bind = { 4, 5, 1, 0, 3 };
            int[] prop = { 1, 4, 5, 3, 0 };
            for (int i = 0; i < 2; ++i) {
                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];

                data.add(Enemy.create(env, choose, hp[rnd], atk[rnd], 2400, 1,
                        new Pair<Integer, Integer>(prop[rnd], -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createEmptyFirstStrike();
                zero.attackMode.createEmptyMustAction();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[51].title,
                                text[51].description, 30, 6)));
                zero.attackMode.addAttack(new ConditionHp(2, 35), seq);
                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionPercentage(80), new BindPets(
                                text[52 + rnd].title,
                                text[52 + rnd].description, 5, 2, 4, 1,
                                bind[rnd])));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), rndAttack);
            }

        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1210, 1211, 1212, 1213, 1214 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == 1210) {
                data.add(Enemy.create(env, choose, 2154631, 9343, 0, 1,
                        new Pair<Integer, Integer>(1, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new RecoverPlayer(text[57].title, text[57].description,
                                100)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionTurns(6),
                        new MultipleAttack(text[61].title,
                                text[61].description, 80, 5)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 50),
                        new RecoverSelf(text[59].title, text[59].description,
                                100)));
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[58].title, text[58].description,
                                4, 2)).addPair(
                        new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                                .ordinal(), 4), new Attack(75)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (choose == 1211) {
                data.add(Enemy.create(env, choose, 2359964, 24743, 0, 2,
                        new Pair<Integer, Integer>(4, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.FIND_ATTR.ordinal(), 5),
                        new BindPets(text[62].title, text[62].description, 5,
                                3, 3, 1, 5)));
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new DarkScreen(text[63].title,
                                        text[63].description, 0))
                        .addPair(
                                new ConditionUsedIf(
                                        1,
                                        1,
                                        1,
                                        EnemyCondition.Type.FIND_ATTR.ordinal(),
                                        5), new Attack(25)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 30),
                        new MultipleAttack(text[65].title,
                                text[65].description, 50, 3)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[64].title, text[64].description, 2,
                                2, 2, 2)).addPair(new ConditionAlways(),
                        new Attack(75)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), seq);
            } else if (choose == 1212) {
                data.add(Enemy.create(env, choose, 1714020, 14990, 0, 1,
                        new Pair<Integer, Integer>(5, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Gravity(text[66].title, text[66].description, 99)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(
                        new ConditionPercentage(50), new BindPets(
                                text[67].title, text[67].description, 3, 2, 3,
                                1)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[68].title,
                                text[68].description, 20, 5)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            } else if (choose == 1213) {
                data.add(Enemy.create(env, choose, 1542909, 13706, 0, 1,
                        new Pair<Integer, Integer>(3, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new DamageAbsorbShield(text[69].title,
                                text[69].description, 3, 200000)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(
                        new ConditionFindAttr(0), new BindPets(text[70].title,
                                text[70].description, 5, 3, 3, 1, 0)));
                zero.attackMode.addAttack(new ConditionTurns(4), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[71].title, text[71].description,
                                3, 4)).addPair(new ConditionAlways(),
                        new Attack(100)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new Transform(text[72].title, text[72].description, 1,
                                4, 5, 3, 0, 2)).addPair(new ConditionAlways(),
                        new Attack(125)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), rndAttack);
            } else if (choose == 1214) {
                data.add(Enemy.create(env, choose, 1299076, 21408, 408, 1,
                        new Pair<Integer, Integer>(0, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ColorChange(text[73].title, text[73].description,
                                -1, 7, -1, 7)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 25),
                        new MultipleAttack(text[75].title,
                                text[75].description, 50, 3)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[74].title, text[74].description,
                                6, 3)).addPair(new ConditionAlways(),
                        new Attack(25)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            }

        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1228, 1229, 1230, 1466, 1468, 1469 };

            for (int i = 0; i < 3; ++i) {
                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                if (choose == 1228) {
                    data.add(Enemy.create(env, choose, 34, 13578, 600000, 2,
                            new Pair<Integer, Integer>(5, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new BindPets(text[76].title,
                                    text[76].description, 3, 3, 3, 1)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new ColorChange(text[77].title,
                                    text[77].description, -1, 3)).addPair(
                            new ConditionAlways(), new Attack(200)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                } else if (choose == 1229) {
                    data.add(Enemy.create(env, choose, 34, 13578, 600000, 1,
                            new Pair<Integer, Integer>(4, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new BindPets(text[76].title,
                                    text[76].description, 3, 3, 3, 1)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new ColorChange(text[78].title,
                                    text[78].description, -1, 0)).addPair(
                            new ConditionAlways(), new Attack(90)));
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new Attack(100)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);
                } else if (choose == 1230) {
                    data.add(Enemy.create(env, choose, 34, 13578, 600000, 1,
                            new Pair<Integer, Integer>(1, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createEmptyFirstStrike();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                } else if (choose == 1466) {
                    data.add(Enemy.create(env, choose, 34, 7777, 600000, 2,
                            new Pair<Integer, Integer>(3, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    // FIXME:
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new LockSkill(
                                    text[79].title, text[79].description, 5)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new Attack(text[81].title,
                                    text[81].description, 100)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);
                } else if (choose == 1468) {
                    data.add(Enemy.create(env, choose, 28, 12000, 600000, 2,
                            new Pair<Integer, Integer>(0, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createEmptyFirstStrike();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionUsed(), new Daze(text[82].title,
                                    text[82].description, 0)));
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new MultipleAttack(
                                    text[83].title, text[83].description, 100,
                                    12)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);
                } else if (choose == 1469) {
                    data.add(Enemy.create(env, choose, 34, 12000, 600000, 1,
                            new Pair<Integer, Integer>(3, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new RecoverPlayer(
                                    text[84].title, text[84].description, 100)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new BindPets(text[85].title, text[85].description,
                                    3, 2, 2, 1)).addPair(new ConditionAlways(),
                            new Attack(100)));
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new BindPets(text[76].title,
                                    text[76].description, 3, 3, 3, 1)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);
                }
            }

        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1223, 1425, 1590, 1711, 2104, 1648 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];

            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 2228002, 17926, 8889, 1,
                        new Pair<Integer, Integer>(5, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new AbsorbShieldAction(text[86].title,
                                text[86].description, 5, 1)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[87].title, text[87].description, 3,
                                2, 2, 1)).addPair(new ConditionAlways(),
                        new Attack(text[31].title, text[31].description, 75)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[88].title,
                                text[88].description, 40, 3)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[89].title, text[89].description,
                                -1, 2)).addPair(new ConditionAlways(),
                        new Attack(100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Gravity(text[90].title, text[90].description, 99)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[91].title,
                                text[91].description, 30, 2)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), seq);
            } else if (choose == ids[1]) {
                data.add(Enemy.create(env, choose, 4733018, 22472, 576, 2,
                        new Pair<Integer, Integer>(5, 1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ReduceTime(text[92].title, text[92].description, 3,
                                -1000)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Daze(text[93].title, text[93].description, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(text[94].title, text[94].description, 200)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[95].title, text[95].description,
                                -1, 0)).addPair(new ConditionAlways(),
                        new Attack(90)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[96].title,
                                text[96].description, 30, 4)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), seq);
            } else if (choose == ids[2]) {
                data.add(Enemy.create(env, choose, 3553827, 14275, 1920, 1,
                        new Pair<Integer, Integer>(1, 5),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ComboShield(text[97].title, text[97].description,
                                999, 3)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(2,
                        1, 0, EnemyCondition.Type.FIND_ATTR.ordinal(), 1, 0,
                        EnemyCondition.Type.HP.ordinal(), 2, 50), new BindPets(
                        text[98].title, text[98].description, 5, 3, 3, 1, 1)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                        new RecoverSelf(text[99].title, text[99].description,
                                50)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 30),
                        new MultipleAttack(text[101].title,
                                text[101].description, 50, 3)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[100].title, text[100].description,
                                -1, 5)).addPair(new ConditionAlways(),
                        new Attack(text[0].title, text[0].description, 90)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[102].title, text[102].description,
                                -1, 1)).addPair(new ConditionAlways(),
                        new Attack(90)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), rndAttack);
            } else if (choose == ids[3]) {
                data.add(Enemy.create(env, choose, 4979077, 18668, 564, 1,
                        new Pair<Integer, Integer>(5, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ResistanceShieldAction(text[103].title,
                                text[103].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new ComboShield(text[104].title, text[104].description,
                                99, 5)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[108].title,
                                text[108].description, 60, 4)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[105].title, text[105].description,
                                -1, 3)).addPair(new ConditionAlways(),
                        new Attack(75)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new Gravity(text[106].title,
                                text[106].description, 99)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new DarkScreen(text[107].title, text[107].description,
                                0)).addPair(new ConditionAlways(),
                        new Attack(75)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
            } else if (choose == ids[4]) {
                data.add(Enemy.create(env, choose, 4280858, 18635, 432, 1,
                        new Pair<Integer, Integer>(4, 5),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ShieldAction(text[109].title,
                                text[109].description, 5, -1, 50)).addPair(
                        new ConditionAlways(),
                        new DarkScreen(text[110].title, text[110].description,
                                0)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ResistanceShieldAction(text[111].title,
                                text[111].description, 999)).addPair(
                        new ConditionUsed(),
                        new MultipleAttack(text[112].title,
                                text[112].description, 20, 5)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[118].title,
                                text[118].description, 50, 6)));
                zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new LineChange(text[116].title,
                                        text[116].description, 3, 1, 4, 1))
                        .addAction(new Attack(60))
                        .addAction(
                                new LineChange(text[117].title,
                                        text[117].description, 0, 1, 2, 1))
                        .addPair(new ConditionTurns(3), new Attack(60)));
                zero.attackMode.addAttack(new ConditionHp(2, 75), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[114].title,
                                text[114].description, 7, 5)).addPair(
                        new ConditionAlways(), new Attack(120)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[115].title,
                                text[115].description, 6, 4)).addPair(
                        new ConditionAlways(), new Attack(80)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new MultipleAttack(
                                text[113].title, text[113].description, 26, 3,
                                5)));
                zero.attackMode.addAttack(new ConditionHp(1, 20), rndAttack);
            } else if (choose == ids[5]) {
                data.add(Enemy.create(env, choose, 4170977, 23973, 3640, 1,
                        new Pair<Integer, Integer>(0, 1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ResistanceShieldAction(text[119].title,
                                text[119].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.FIND_ATTR.ordinal(), 3),
                        new BindPets(text[120].title, text[120].description, 5,
                                3, 3, 1, 3)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[124].title,
                                text[124].description, 60, 3)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[121].title,
                                text[121].description, 6, 3)).addPair(
                        new ConditionAlways(), new Attack(60)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[122].title, text[122].description,
                                3, 1)).addPair(new ConditionAlways(),
                        new Attack(100)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new DarkScreen(text[123].title, text[123].description,
                                0)).addPair(new ConditionAlways(),
                        new Attack(80)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
            }
        }
        mStageEnemies.put(++stageCounter, data); // ++stageCounter 7

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1533, 1534, 1535, 1748, 889, 890, 891, 892 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 1299332, 8658, 444, 1,
                        new Pair<Integer, Integer>(1, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ComboShield(text[125].title, text[125].description,
                                5, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();
                seq = new SequenceAttack();
                for (int i = 0; i < 5; ++i) {
                    seq.addAttack(new EnemyAttack().addAction(
                            new LineChange(text[126].title,
                                    text[126].description, 1, 1)).addPair(
                            new ConditionUsed(), new Attack(140)));
                }
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Angry(text[127].title, text[127].description, 999,
                                300)));
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[128].title, text[128].description,
                                0, 1)).addPair(
                        new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                                .ordinal(), 0), new Attack(200)));
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[129].title, text[129].description,
                                -1, 1)).addPair(
                        new ConditionIf(1, 1, 1, EnemyCondition.Type.FIND_ORB
                                .ordinal(), 0), new Attack(200)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (choose == ids[1]) {
                data.add(Enemy.create(env, choose, 644832, 15075, 322222, 1,
                        new Pair<Integer, Integer>(4, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ResistanceShieldAction(text[130].title,
                                text[130].description, 4)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[131].title, text[131].description,
                                0, 6, 2, 2)).addPair(new ConditionAlways(),
                        new Attack(25)));
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[132].title, text[132].description,
                                3, 6, 4, 2)).addPair(new ConditionAlways(),
                        new Attack(50)));
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[133].title, text[133].description,
                                0, 2, 2, 6)).addPair(new ConditionAlways(),
                        new Attack(75)));
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[134].title, text[134].description,
                                3, 2, 4, 6)).addPair(new ConditionAlways(),
                        new Attack(100)));
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[135].title, text[135].description,
                                0, 6, 2, 6)).addPair(new ConditionAlways(),
                        new Attack(200)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (choose == ids[2]) {
                data.add(Enemy.create(env, choose, 1885388, 12508, 0, 1,
                        new Pair<Integer, Integer>(5, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new LockSkill(
                                text[136].title, text[136].description, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new Transform(text[137].title, text[137].description,
                                5, 2)).addPair(new ConditionTurns(6),
                        new Attack(300)));
                // TODO: ADD damage if hp < 7615
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 50),
                        new RecoverSelf(text[138].title, text[138].description,
                                100)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[139].title, text[139].description, 0,
                                1, 2, 1)).addPair(new ConditionAlways(),
                        new Attack(75)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[140].title, text[140].description, 2,
                                1, 2, 3)).addPair(new ConditionAlways(),
                        new Attack(50)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new MultipleAttack(
                                text[141].title, text[141].description, 40, 3,
                                4)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), rndAttack);
            } else if (choose == ids[3]) {
                data.add(Enemy.create(env, choose, 5475556, 54756, 0, 5,
                        new Pair<Integer, Integer>(0, 5),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 5, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Gravity(
                                text[142].title, text[142].description, 99)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[143].title, text[143].description,
                                1, 0)).addPair(new ConditionAlways(),
                        new Attack(1000)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            } else if (choose == ids[4]) {
                data.add(Enemy.create(env, choose, 2570260, 12885, 2520, 1,
                        new Pair<Integer, Integer>(1, 0),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new DarkScreen(text[144].title, text[144].description,
                                0)).addPair(new ConditionAlways(),
                        new Attack(50)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new DarkScreen(text[144].title, text[144].description,
                                0)).addPair(new ConditionAlways(),
                        new Attack(50)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[147].title,
                                text[147].description, 75, 2)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Gravity(
                                text[145].title, text[145].description, 99)));
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[146].title, text[146].description,
                                -1, 7)).addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), seq);
            } else if (choose == ids[5]) {
                data.add(Enemy.create(env, choose, 2399149, 11345, 1260, 1,
                        new Pair<Integer, Integer>(4, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[148].title, text[148].description,
                                -1, 7)).addPair(new ConditionAlways(),
                        new Attack(50)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new DropRateAttack(text[149].title,
                                text[149].description, 5, 7, 25)));
                zero.attackMode.addAttack(new ConditionHp(2, 75), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[154].title,
                                text[154].description, 50, 4)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new DarkScreen(text[150].title, text[150].description,
                                0)).addPair(new ConditionAlways(),
                        new Attack(75)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[151].title, text[151].description,
                                -1, 2)).addPair(new ConditionAlways(),
                        new Attack(150)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[152].title, text[152].description, 3,
                                1, 2, 4)).addPair(new ConditionAlways(),
                        new Attack(50)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[153].title, text[153].description,
                                -1, 7)).addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
            } else if (choose == ids[6]) {
                data.add(Enemy.create(env, choose, 2142482, 12628, 5040, 1,
                        new Pair<Integer, Integer>(5, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new DropRateAttack(text[155].title,
                                text[155].description, 10, 7, 10)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[156].title, text[156].description, 0,
                                2, 2, 1)).addPair(new ConditionTurns(3),
                        new Attack(50)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Attack(
                                text[158].title, text[158].description, 150)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[157].title,
                                text[157].description, 20, 4, 6)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), seq);
            } else if (choose == ids[7]) {
                data.add(Enemy.create(env, choose, 2056927, 13911, 3780, 1,
                        new Pair<Integer, Integer>(3, 1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1,
                                0, EnemyCondition.Type.FIND_ATTR.ordinal(), 5),
                        new BindPets(text[159].title, text[159].description, 5,
                                3, 3, 1, 5)));
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[160].title, text[160].description,
                                -1, 3)).addPair(
                        new ConditionIf(1, 1, 1, EnemyCondition.Type.FIND_ATTR
                                .ordinal(), 5), new Attack(75)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new BindPets(text[161].title, text[161].description, 4,
                                3, 3, 1, 1)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Daze(text[163].title, text[163].description, 10, 6,
                                5)));
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Attack(
                                text[164].title, text[164].description, 1000)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[162].title, text[162].description,
                                -1, 1)).addPair(new ConditionAlways(),
                        new Attack(125)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), seq);
            }

        }
        mStageEnemies.put(++stageCounter, data);// 8

        data = new ArrayList<Enemy>();
        {
            for (int i = 0; i < 3; ++i) {
                int rnd = RandomUtil.getInt(5);
                int choose = 1538 + 2 * rnd;
                if (choose == 1538) {
                    data.add(Enemy.create(env, choose, 302697, 8200, 30000, 1,
                            new Pair<Integer, Integer>(1, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new AbsorbShieldAction(
                                    text[165].title, text[165].description, 10,
                                    1)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new ColorChange(text[166].title,
                                    text[166].description, -1, 1)).addPair(
                            new ConditionAlways(), new Attack(75)));
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new Attack(text[167].title,
                                    text[167].description, 100)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (choose == 1540) {
                    data.add(Enemy.create(env, choose, 302697, 8200, 30000, 1,
                            new Pair<Integer, Integer>(4, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new AbsorbShieldAction(
                                    text[168].title, text[168].description, 10,
                                    4)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new ColorChange(text[169].title,
                                    text[169].description, -1, 4)).addPair(
                            new ConditionAlways(), new Attack(75)));
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new Attack(text[167].title,
                                    text[167].description, 100)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (choose == 1542) {
                    data.add(Enemy.create(env, choose, 302697, 8200, 30000, 1,
                            new Pair<Integer, Integer>(5, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new AbsorbShieldAction(
                                    text[170].title, text[170].description, 10,
                                    5)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new ColorChange(text[171].title,
                                    text[171].description, -1, 5)).addPair(
                            new ConditionAlways(), new Attack(75)));
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new Attack(text[167].title,
                                    text[167].description, 100)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (choose == 1544) {
                    data.add(Enemy.create(env, choose, 302697, 8200, 30000, 1,
                            new Pair<Integer, Integer>(3, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new AbsorbShieldAction(
                                    text[172].title, text[172].description, 10,
                                    3)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new ColorChange(text[173].title,
                                    text[173].description, -1, 3)).addPair(
                            new ConditionAlways(), new Attack(75)));
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new Attack(text[167].title,
                                    text[167].description, 100)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (choose == 1546) {
                    data.add(Enemy.create(env, choose, 302697, 8200, 30000, 1,
                            new Pair<Integer, Integer>(0, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new AbsorbShieldAction(
                                    text[174].title, text[174].description, 10,
                                    0)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    zero.attackMode.createEmptyMustAction();

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new ColorChange(text[175].title,
                                    text[175].description, -1, 0)).addPair(
                            new ConditionAlways(), new Attack(75)));
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new Attack(text[167].title,
                                    text[167].description, 100)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                }
            }
        }
        mStageEnemies.put(++stageCounter, data);// 9

        data = new ArrayList<Enemy>();
        {
            int rnd = RandomUtil.getInt(5);
            int choose = 746 + 2 * rnd;
            if (choose == 746) {
                data.add(Enemy.create(env, choose, 2951485, 12546, 434, 1,
                        new Pair<Integer, Integer>(1, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new LockSkill(
                                text[176].title, text[176].description, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[177].title,
                                text[177].description, 80, 7)));
                zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 20), seq);
            } else if (choose == 748) {
                data.add(Enemy.create(env, choose, 2951485, 12546, 434, 1,
                        new Pair<Integer, Integer>(4, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new LockSkill(
                                text[176].title, text[176].description, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[178].title,
                                text[178].description, 70, 7)));
                zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 20), seq);
            } else if (choose == 750) {
                data.add(Enemy.create(env, choose, 2951485, 12546, 434, 1,
                        new Pair<Integer, Integer>(5, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new LockSkill(
                                text[176].title, text[176].description, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[179].title,
                                text[179].description, 30, 7)));
                zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 20), seq);
            } else if (choose == 752) {
                data.add(Enemy.create(env, choose, 2951485, 12546, 434, 1,
                        new Pair<Integer, Integer>(3, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new LockSkill(
                                text[176].title, text[176].description, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[180].title,
                                text[180].description, 200, 4)));
                zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 20), seq);
            } else if (choose == 754) {
                data.add(Enemy.create(env, choose, 2951485, 12546, 434, 1,
                        new Pair<Integer, Integer>(0, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new LockSkill(
                                text[176].title, text[176].description, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[181].title,
                                text[181].description, 70, 7)));
                zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 20), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 10

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1342, 1343, 1645, 914, 1089 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 4968040, 20995, 816, 2,
                        new Pair<Integer, Integer>(3, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new LockSkill(
                                text[182].title, text[182].description, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[184].title,
                                text[184].description, 300, 4)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[183].title,
                                text[183].description, 100, 2)));
                zero.attackMode.addAttack(new ConditionHp(2, 70), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 70), seq);
            } else if (choose == ids[1]) {
                data.add(Enemy.create(env, choose, 4968040, 12012, 816, 1,
                        new Pair<Integer, Integer>(0, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ReduceTime(text[185].title, text[185].description,
                                5, -2000)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[188].title, text[188].description,
                                -1, 0)).addPair(new ConditionAlways(),
                        new Attack(300)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[187].title,
                                text[187].description, 100, 2)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(2, 70), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[186].title, text[186].description,
                                -1, 0)).addPair(new ConditionAlways(),
                        new Attack(150)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 70), seq);
            } else if (choose == ids[2]) {
                data.add(Enemy.create(env, choose, 3855732, 20208, 0, 1,
                        new Pair<Integer, Integer>(1, 0),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ComboShield(text[189].title, text[189].description,
                                99, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Daze(text[193].title, text[193].description, 0)));
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[194].title, text[194].description,
                                -1, 1)).addPair(new ConditionAlways(),
                        new Attack(500)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[190].title, text[190].description,
                                -1, 1)).addPair(new ConditionAlways(),
                        new Attack(100)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[191].title, text[191].description, 3,
                                2, 2, 1)).addPair(new ConditionHp(2, 75),
                        new Attack(50)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new DarkScreen(text[192].title, text[192].description,
                                0)).addPair(new ConditionHp(2, 50),
                        new Attack(75)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
            } else if (choose == ids[3]) {
                data.add(Enemy.create(env, choose, 2401288, 17642, 804, 1,
                        new Pair<Integer, Integer>(5, 0),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Angry(text[195].title, text[195].description, 1,
                                300)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionUsed(), new Attack(
                                text[196].title, text[196].description, 100)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionUsed(), new LockSkill(
                                text[197].title, text[197].description, 5)));
                zero.attackMode.addAttack(new ConditionHp(2, 75), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Daze(text[199].title, text[199].description, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[200].title,
                                text[200].description, 300, 2)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[198].title,
                                text[198].description, 40, 2, 4)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), seq);
            } else if (choose == ids[4]) {
                data.add(Enemy.create(env, choose, 2743510, 15845, 12060, 1,
                        new Pair<Integer, Integer>(4, 0),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ResistanceShieldAction(text[201].title,
                                text[201].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Daze(text[204].title, text[204].description, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[223].title,
                                text[223].description, 100, 5)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[202].title, text[202].description,
                                -1, 4)).addPair(new ConditionAlways(),
                        new Attack(100)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[203].title, text[203].description, 3,
                                1, 3, 2)).addPair(new ConditionHp(2, 75),
                        new Attack(85)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 11 ++stageCounter

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 1324, 55564, 5500, 1500000, 1,
                    new Pair<Integer, Integer>(0, -1),
                    getMonsterTypes(env, 1324)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Angry(text[205].title, text[205].description, 5, 200)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionTurns(6),
                    new Attack(text[207].title, text[207].description, 5000)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            RandomAttack rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new ColorChange(text[206].title, text[206].description, -1,
                            7)).addPair(new ConditionPercentage(80),
                    new Attack(80)));
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), rndAttack);
        }
        mStageEnemies.put(++stageCounter, data); // 12 ++stageCounter

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1663, 1463, 1465, 1837, 2008 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 2568378, 18994, 1200, 1,
                        new Pair<Integer, Integer>(5, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new DropRateAttack(text[224].title,
                                text[224].description, 99, 5, 25, 0, 25)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[227].title,
                                text[227].description, 100, 2)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[225].title, text[225].description,
                                1, 7)).addPair(new ConditionAlways(),
                        new Attack(50)));
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[226].title, text[226].description,
                                7, 2, 10, 2)).addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), seq);
            } else if (choose == ids[1]) {
                data.add(Enemy.create(env, choose, 1532431, 20569, 0, 1,
                        new Pair<Integer, Integer>(3, 0),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new DamageAbsorbShield(text[228].title,
                                text[228].description, 99, 200000)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Daze(text[229].title, text[229].description)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Daze("3", "")));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Daze("2", "")));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Daze("1", "")));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[230].title,
                                text[230].description, 200, 7)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (choose == ids[2]) {
                data.add(Enemy.create(env, choose, 1935438, 12766, 0, 1,
                        new Pair<Integer, Integer>(0, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new SkillDown(text[231].title, text[231].description,
                                0, 5, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[235].title,
                                text[235].description, 50, 4)));
                zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[232].title, text[232].description,
                                -1, 7)).addPair(new ConditionAlways(),
                        new Attack(50)));
                seq.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[233].title,
                                text[233].description, 6, 4)).addPair(
                        new ConditionAlways(), new Attack(50)));
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[234].title, text[234].description, 2,
                                4, 4, 1)).addPair(new ConditionAlways(),
                        new Attack(50)));
                zero.attackMode.addAttack(new ConditionHp(1, 20), seq);
            } else if (choose == ids[3]) {
                data.add(Enemy.create(env, choose, 5150188, 13792, 564, 1,
                        new Pair<Integer, Integer>(5, 1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1,
                                0, EnemyCondition.Type.LEADER_SWITCH.ordinal()),
                        new SwitchLeader(text[236].title,
                                text[236].description, 3)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new ComboShield(text[237].title, text[237].description,
                                99, 4)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 30),
                        new MultipleAttack(text[243].title,
                                text[243].description, 80, 3)));
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[238].title, text[238].description,
                                2, 7)).addPair(new ConditionTurns(3),
                        new Attack(25)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[239].title, text[239].description,
                                -1, 0)).addPair(new ConditionAlways(),
                        new Attack(100)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new Gravity(text[240].title,
                                text[240].description, 99)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new DarkScreen(text[241].title, text[241].description,
                                0)).addPair(new ConditionAlways(),
                        new Attack(75)));
                rndAttack.addAttack(new EnemyAttack().addPair(new ConditionIf(
                                1, 1, 0, EnemyCondition.Type.LEADER_SWITCH.ordinal()),
                        new SwitchLeader(text[242].title,
                                text[242].description, 3)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
            } else if (choose == ids[4]) {
                data.add(Enemy.create(env, choose, 4367269, 10164, 552, 1,
                        new Pair<Integer, Integer>(1, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ComboShield(text[244].title, text[244].description,
                                5, 4)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 80),
                        new SkillDown(text[246].title, text[246].description,
                                0, 2, 2)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                        new SwitchLeader(text[245].title,
                                text[245].description, 2)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 10),
                        new MultipleAttack(text[248].title,
                                text[248].description, 100, 12)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[247].title, text[247].description,
                                -1, 7)).addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 10), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 13 ++stageCounter

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 757, 1095, 1092 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 2141969, 15178, 432, 1,
                        new Pair<Integer, Integer>(1, 5),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 1, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 5, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Gravity(
                                text[259].title, text[259].description, 80)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[263].title, text[263].description,
                                3, 1, 7, 1, 4, 5, 10, 5)).addPair(
                        new ConditionAlways(), new Attack(150)));
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Gravity(
                                text[259].title, text[259].description, 80)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[262].title, text[262].description,
                                7, 5, 10, 5)).addPair(new ConditionAlways(),
                        new Attack(100)));
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Gravity(
                                text[259].title, text[259].description, 80)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[260].title, text[260].description,
                                7, 1, 10, 1)).addPair(new ConditionAlways(),
                        new Attack(100)));
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Gravity(
                                text[259].title, text[259].description, 80)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), seq);
            } else if (choose == ids[1]) {
                data.add(Enemy.create(env, choose, 2997781, 12560, 468, 1,
                        new Pair<Integer, Integer>(4, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new LockSkill(text[252].title, text[252].description,
                                10)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(text[253].title, text[253].description, 25)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(text[254].title, text[254].description, 50)));
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Attack(
                                text[255].title, text[255].description, 100)));
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Attack(
                                text[256].title, text[256].description, 200)));
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Attack(
                                text[257].title, text[257].description, 1000)));
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Attack(
                                text[258].title, text[258].description, 1000)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (choose == ids[2]) {
                data.add(Enemy.create(env, choose, 3083508, 36994, 7380, 3,
                        new Pair<Integer, Integer>(5, 1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Gravity(
                                text[249].title, text[249].description, 99)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[251].title,
                                text[251].description, 75, 2)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[250].title, text[250].description,
                                -1, 1)).addPair(new ConditionAlways(),
                        new Attack(85)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 14

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1928, 1726, 1956, 2075 }; //1954
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 2141969, 15178, 432, 1,
                        new Pair<Integer, Integer>(3, 4),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ReduceTime(text[264].title, text[264].description,
                                5, -2000)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1,
                                1, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 0, -1),
                        new ReduceTime(text[265].title, text[265].description,
                                3, -3000)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 30),
                        new BindPets(text[268].title, text[268].description, 2,
                                2, 3, 4)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[269].title, text[269].description, 3,
                                1, 2, 5)).addPair(new ConditionAlways(),
                        new Attack(150)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[266].title, text[266].description, 3,
                                1, 2, 2)).addPair(new ConditionAlways(),
                        new Attack(75)));
                seq.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[267].title,
                                text[267].description, 7, 3)).addPair(
                        new ConditionAlways(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), seq);
            } else if (choose == ids[1]) {
                data.add(Enemy.create(env, choose, 4024704, 14476, 504, 1,
                        new Pair<Integer, Integer>(1, 0),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new SkillDown(text[270].title, text[270].description,
                                0, 0, 4)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 1),
                        new SkillDown(text[271].title, text[271].description,
                                0, 99, 99)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                        new BindPets(text[272].title, text[272].description, 3,
                                2, 2, 6)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 30),
                        new MultipleAttack(text[276].title,
                                text[276].description, 1000, 4)));
                seq.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[273].title,
                                text[273].description, 1, 4, 0, 4)).addPair(
                        new ConditionTurns(2), new Attack(100)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[275].title, text[275].description,
                                1, 7)).addPair(
                        new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                                .ordinal(), 1), new Attack(75)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[276].title, text[276].description,
                                0, 6)).addPair(
                        new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                                .ordinal(), 0), new Attack(50)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionPercentage(75), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
//			} else if (choose == ids[2]) {
//				data.add(Enemy.create(env, choose, 7190260, 22775, 0, 1,
//						new Pair<Integer, Integer>(1, -1),
//						getMonsterTypes(env, choose)));
//				zero = data.get(0);
//				zero.addOwnAbility(Type.SHIELD, -1, 1, 50);
//				zero.addOwnAbility(Type.SHIELD, -1, 4, 50);
//				zero.attackMode = new EnemyAttackMode();
//				seq = new SequenceAttack();
//				seq.addAttack(new EnemyAttack()
//						.addAction(
//								new SkillDown(text[277].title,
//										text[277].description, 0, 2, 2))
//						.addAction(
//								new ComboShield(text[278].title,
//										text[278].description, 99, 4))
//						.addPair(
//								new ConditionAlways(),
//								new Gravity(text[279].title,
//										text[279].description, 75)));
//				zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
//
//				seq = new SequenceAttack();
//				seq.addAttack(new EnemyAttack()
//						.addAction(
//								new Gravity(text[279].title,
//										text[279].description, 75))
//						.addAction(
//								new Transform(text[285].title,
//										text[285].description, 1))
//						.addPair(new ConditionHp(2, 30), new Attack(300)));
//				seq.addAttack(new EnemyAttack().addAction(
//						new ColorChange(text[280].title, text[280].description,
//								6, 2)).addPair(
//						new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
//								.ordinal(), 6), new Attack(200)));
//				zero.attackMode.addAttack(new ConditionAlways(), seq);
//
//				RandomAttack rndAttack = new RandomAttack();
//				rndAttack.addAttack(new EnemyAttack().addAction(
//						new LineChange(text[281].title, text[281].description,
//								4, 1, 7, 1, 9, 1)).addPair(
//						new ConditionAlways(), new Attack(75)));
//				rndAttack
//						.addAttack(new EnemyAttack().addPair(
//								new ConditionAlways(), new MultipleAttack(
//										text[282].title, text[282].description,
//										40, 3)));
//				rndAttack.addAttack(new EnemyAttack().addAction(
//						new RandomChange(text[283].title,
//								text[283].description, 6, 7)).addPair(
//						new ConditionAlways(), new Attack(50)));
//				rndAttack.addAttack(new EnemyAttack().addAction(
//						new DarkScreen(text[284].title, text[284].description,
//								0)).addPair(new ConditionAlways(),
//						new Attack(75)));
//				zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
            } else if (choose == ids[2]) {
                data.add(Enemy.create(env, choose, 2570260, 15451, 0, 1,
                        new Pair<Integer, Integer>(5, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 1);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new DamageAbsorbShield(text[286].title,
                                text[286].description, 99, 300000)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[291].title,
                                text[291].description, 7, 10)).addPair(
                        new ConditionHp(2, 1),
                        new RecoverSelf(text[292].title, text[292].description,
                                -2)));
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[290].title, text[290].description,
                                0, 7)).addPair(new ConditionHp(2, 30),
                        new Attack(200)));
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[287].title, text[287].description,
                                0, 2, 2, 2, 1, 5)).addPair(
                        new ConditionTurns(2), new Attack(100)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[288].title, text[288].description,
                                3, 1, 8, 1, 10, 1)).addPair(
                        new ConditionAlways(), new Attack(100)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new MultipleAttack(
                                text[289].title, text[289].description, 30, 3,
                                4)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
            } else if (choose == ids[3]) {
                data.add(Enemy.create(env, choose, 322222, 20465, 1555556, 1,
                        new Pair<Integer, Integer>(5, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 5, 30);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 30);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 30);

                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new RecoverPlayer(text[293].title,
                                text[293].description, 100)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new Transform(text[294].title, text[294].description,
                                5, 3, 0)).addPair(new ConditionAlways(),
                        new Attack(100)));
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[295].title, text[295].description,
                                0, 2)).addPair(new ConditionAlways(),
                        new Attack(150)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[296].title,
                                text[296].description, 25, 5, 7)));
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new SkillDown(text[297].title,
                                        text[297].description, 0, 1, 1))
                        .addAction(
                                new Transform(text[298].title,
                                        text[298].description, 5, 3, 0))
                        .addPair(new ConditionAlways(), new Attack(300)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 15 ++stageCounter

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 1849, 8650180, 12987, 0, 1,
                    new Pair<Integer, Integer>(0, -1),
                    getMonsterTypes(env, 1849)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();

            seq.addAttack(new EnemyAttack().addAction(
                    new IntoVoid(text[299].title, text[299].description))
                    .addPair(
                            new ConditionAlways(),
                            new RandomChange(text[300].title,
                                    text[300].description, 8, 2)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Gravity(text[301].title, text[301].description, 99)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new RandomChange(text[302].title, text[302].description, 8,
                            5)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new RandomChange(text[303].title, text[303].description, 8,
                            5)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new RandomChange(text[304].title, text[304].description, 8,
                            5)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[305].title, text[305].description,
                            1000, 2)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
        }
        mStageEnemies.put(++stageCounter, data); // 16

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 651, 917, 918, 1252, 1532 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 3205510, 17385, 8040, 1,
                        new Pair<Integer, Integer>(5, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Attack(
                                text[306].title, text[306].description, 130)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new ResistanceShieldAction(text[307].title,
                                text[307].description, 99)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[308].title, text[308].description,
                                1, 7)).addPair(
                        new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                                .ordinal(), 1), new Attack(80)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[309].title, text[309].description,
                                0, 7)).addPair(
                        new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                                .ordinal(), 0), new Attack(90)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(
                        new ConditionPercentage(75), new Attack(
                                text[310].title, text[310].description, 200)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[309].title, text[309].description,
                                0, 7)).addPair(
                        new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                                .ordinal(), 0), new Attack(90)));
                zero.attackMode.addAttack(new ConditionHp(2, 31), seq);
            } else if (choose == ids[1]) {
                data.add(Enemy.create(env, choose, 4112484, 19336, 8160, 1,
                        new Pair<Integer, Integer>(3, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createEmptyFirstStrike();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                        new BindPets(text[312].title, text[312].description, 4,
                                2, 4, 1, 6)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 20),
                        new MultipleAttack(text[313].title,
                                text[313].description, 25, 10)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(
                        new ConditionPercentage(70), new Attack(
                                text[311].title, text[311].description, 150)));
                zero.attackMode.addAttack(new ConditionHp(2, 75), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (choose == ids[2]) {
                data.add(Enemy.create(env, choose, 6080262, 19336, 8160, 1,
                        new Pair<Integer, Integer>(0, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ComboShield(text[314].title, text[314].description,
                                999, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                        new BindPets(text[312].title, text[312].description, 4,
                                2, 4, 1, 6)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 20),
                        new MultipleAttack(text[313].title,
                                text[313].description, 25, 10)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[315].title, text[315].description,
                                -1, 7)).addPair(new ConditionAlways(),
                        new Attack(80)));
                zero.attackMode.addAttack(new ConditionHp(1, 20), seq);
            } else if (choose == ids[3]) {
                data.add(Enemy.create(env, choose, 5566843, 14562, 4020, 1,
                        new Pair<Integer, Integer>(1, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ComboShield(text[316].title, text[316].description,
                                999, 4)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 30),
                        new MultipleAttack(text[318].title,
                                text[318].description, 50, 3)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[317].title, text[317].description,
                                4, 1)).addPair(new ConditionTurns(2),
                        new Attack(120)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), seq);
            } else if (choose == ids[4]) {

                data.add(Enemy.create(env, choose, 6165732, 17128, 4020, 1,
                        new Pair<Integer, Integer>(4, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ResistanceShieldAction(text[319].title,
                                text[319].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new DropRateAttack(text[320].title,
                                text[320].description, 99, 4, 25)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[321].title, text[321].description,
                                -1, 4)).addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 75), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[321].title, text[321].description,
                                -1, 4)).addPair(new ConditionAlways(),
                        new Attack(100)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[322].title, text[322].description, 2,
                                1, 2, 3)).addPair(new ConditionPercentage(50),
                        new Attack(75)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), rndAttack);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new ComboShield(text[323].title, text[323].description,
                                1, 5)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new MultipleAttack(text[324].title,
                                text[324].description, 35, 5)));
            }

        }
        mStageEnemies.put(++stageCounter, data); // 17 ++stageCounter

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1215, 1472, 1631, 2092, 1760 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 7705818, 19336, 8160, 1,
                        new Pair<Integer, Integer>(0, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new AbsorbShieldAction(
                                text[325].title, text[325].description, 4, 3)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new AbsorbShieldAction(
                                text[326].title, text[326].description, 4, 0)));
                zero.attackMode
                        .addAttack(new ConditionFirstStrike(), rndAttack);

                zero.attackMode.createEmptyMustAction();

                rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new AbsorbShieldAction(
                                text[325].title, text[325].description, 4, 3)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new AbsorbShieldAction(
                                text[326].title, text[326].description, 4, 0)));
                zero.attackMode.addAttack(new ConditionTurns(4), rndAttack);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Gravity(
                                text[327].title, text[327].description, 100)));
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[329].title, text[329].description, 2,
                                1, 2, 1)).addPair(new ConditionAlways(),
                        new Attack(100)));
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[330].title, text[330].description,
                                -1, 7)).addPair(new ConditionAlways(),
                        new Attack(150)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Gravity(
                                text[328].title, text[328].description, 99)));
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[329].title, text[329].description, 2,
                                1, 2, 1)).addPair(new ConditionAlways(),
                        new Attack(100)));
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[330].title, text[330].description,
                                -1, 7)).addPair(new ConditionAlways(),
                        new Attack(150)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), seq);
            } else if (choose == ids[1]) {
                data.add(Enemy.create(env, choose, 6177624, 12731, 0, 1,
                        new Pair<Integer, Integer>(5, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ResistanceShieldAction(text[331].title,
                                text[331].description, 4)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChangeNew(text[332].title,
                                text[332].description, 1, 7)).addPair(
                        new ConditionUsed(), new Attack(100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new MultipleAttack(text[334].title,
                                text[334].description, 50, 4)));
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[335].title, text[335].description, 2,
                                1, 2, 4)).addPair(new ConditionUsed(),
                        new Attack(80)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Daze(text[336].title, text[336].description)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[337].title,
                                text[337].description, 500, 5)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            } else if (choose == ids[2]) {
                data.add(Enemy.create(env, choose, 6250347, 12987, 0, 1,
                        new Pair<Integer, Integer>(3, 0),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ComboShield(text[338].title, text[338].description,
                                99, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new DarkScreen(text[339].title, text[339].description,
                                0)).addPair(new ConditionTurns(7),
                        new Attack(1000)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[340].title, text[340].description,
                                -1, 3)).addPair(new ConditionAlways(),
                        new Attack(80)));
                rndAttack
                        .addAttack(new EnemyAttack().addPair(
                                new ConditionAlways(), new MultipleAttack(
                                        text[341].title, text[341].description,
                                        50, 3)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), rndAttack);

                rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[342].title, text[342].description,
                                -1, 0)).addPair(new ConditionAlways(),
                        new Attack(80)));
                rndAttack
                        .addAttack(new EnemyAttack().addPair(
                                new ConditionAlways(), new MultipleAttack(
                                        text[341].title, text[341].description,
                                        50, 3)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);

                rndAttack = new RandomAttack();
                rndAttack
                        .addAttack(new EnemyAttack().addPair(
                                new ConditionAlways(), new MultipleAttack(
                                        text[344].title, text[344].description,
                                        50, 5)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), rndAttack);
            } else if (choose == ids[3]) {
                data.add(Enemy.create(env, choose, 8734110, 22159, 1044, 1,
                        new Pair<Integer, Integer>(1, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ResistanceShieldAction(text[345].title,
                                text[345].description, 5))
                        .addPair(
                                new ConditionAlways(),
                                new Attack(text[346].title,
                                        text[346].description, 110)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ComboShield(text[347].title, text[347].description,
                                5, 5)).addPair(
                        new ConditionUsed(),
                        new MultipleAttack(text[348].title,
                                text[348].description, 40, 3)));
                seq.addAttack(new EnemyAttack().addAction(
                        new DamageAbsorbShield(text[349].title,
                                text[349].description, 5, 500000)).addPair(
                        new ConditionUsed(),
                        new MultipleAttack(text[350].title,
                                text[350].description, 20, 6)));
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[351].title, text[351].description, 3,
                                5, 5, 5)).addPair(new ConditionUsed(),
                        new Attack(150)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[352].title,
                                text[352].description, 1000, 5)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            } else if (choose == ids[4]) {
                data.add(Enemy.create(env, choose, 6849407, 12782, 3480, 1,
                        new Pair<Integer, Integer>(3, 0),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ResistanceShieldAction(text[353].title,
                                text[353].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 1),
                        new RecoverSelf(text[354].title, text[354].description,
                                100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                        new Angry(text[355].title, text[355].description, 999,
                                250)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Daze(text[356].title, text[356].description)));
                seq.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[357].title,
                                text[357].description, 6, 9)).addPair(
                        new ConditionAlways(), new Attack(100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Daze(text[358].title, text[358].description)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[359].title,
                                text[359].description, 50, 5)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Daze(text[360].title, text[360].description)));
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Gravity(
                                text[361].title, text[361].description, 100)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 18 ++stageCounter

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 985, 1536, 1737, 1422, 1371 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 5138296, 19455, 696, 1,
                        new Pair<Integer, Integer>(0, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Attack(
                                text[362].title, text[362].description, 200)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.FIND_TYPE.ordinal(), 1, 6),
                        new BindPets(text[364].title, text[364].description, 4,
                                5, 5, 1, 6)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 1, EnemyCondition.Type.FIND_TYPE.ordinal(), 1, 6),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionUsed(), new Gravity(
                                text[365].title, text[365].description, 99)));
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Attack(
                                text[366].title, text[366].description, 500)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[363].title, text[363].description,
                                3, 7)).addPair(
                        new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                                .ordinal(), 3), new Attack(90)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), seq);
            } else if (choose == ids[1]) {
                data.add(Enemy.create(env, choose, 6166673, 21252, 4680, 1,
                        new Pair<Integer, Integer>(1, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new DropRateAttack(text[367].title,
                                text[367].description, 99, 1, 10)).addPair(
                        new ConditionAlways(), new DropRateAttack(99, 7, 10)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                        new ComboShield(text[368].title, text[368].description,
                                999, 5)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[369].title, text[369].description,
                                4, 7)).addPair(
                        new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                                .ordinal(), 4), new Attack(100)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[370].title, text[370].description,
                                -1, 1)).addPair(new ConditionAlways(),
                        new Attack(100)));
                rndAttack
                        .addAttack(new EnemyAttack().addPair(
                                new ConditionAlways(), new MultipleAttack(
                                        text[371].title, text[371].description,
                                        50, 3)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), rndAttack);

                rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[369].title, text[369].description,
                                4, 7)).addPair(
                        new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                                .ordinal(), 4), new Attack(100)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[370].title, text[370].description,
                                -1, 1)).addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), rndAttack);
            } else if (choose == ids[2]) {
                data.add(Enemy.create(env, choose, 6093951, 19969, 23400, 1,
                        new Pair<Integer, Integer>(4, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new LockSkill(
                                text[374].title, text[374].description, 5)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                        new BindPets(text[375].title, text[375].description, 3,
                                1, 3, 5)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[376].title, text[376].description,
                                -1, 4)).addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 75), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[377].title, text[377].description, 3,
                                2, 4, 1)).addPair(new ConditionPercentage(75),
                        new Attack(85)));
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[378].title, text[378].description,
                                -1, 4)).addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(
                        new ConditionPercentage(75), new Gravity(
                                text[379].title, text[379].description, 100)));
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[380].title, text[380].description, 3,
                                2, 4, 1)).addPair(new ConditionPercentage(75),
                        new Attack(85)));
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[381].title, text[381].description,
                                -1, 4)).addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);
            } else if (choose == ids[3]) {
                data.add(Enemy.create(env, choose, 8562229, 23819, 4680, 1,
                        new Pair<Integer, Integer>(3, 5),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new RecoverPlayer(text[382].title,
                                text[382].description, 100)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 50),
                        new RecoverSelf(text[383].title, text[383].description,
                                100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 75),
                        new BindPets(text[384].title, text[384].description, 5,
                                10, 10, 1, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 90),
                        new ResistanceShieldAction(text[385].title,
                                text[385].description, 999)));
                zero.attackMode.addAttack(new ConditionUsedIf(1, 1, 0,
                        EnemyCondition.Type.HP.ordinal(), 2, 90), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[386].title, text[386].description,
                                -1, 3)).addPair(new ConditionAlways(),
                        new Attack(80)));
                rndAttack
                        .addAttack(new EnemyAttack().addPair(
                                new ConditionAlways(), new MultipleAttack(
                                        text[387].title, text[387].description,
                                        20, 6)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionPercentage(65), new Gravity(
                                text[388].title, text[388].description, 99)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[389].title,
                                text[389].description, 100, 10)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);
            } else if (choose == ids[4]) {
                data.add(Enemy.create(env, choose, 6565876, 25102, 4680, 1,
                        new Pair<Integer, Integer>(0, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Transform(text[390].title,
                                        text[390].description, 6)).addPair(
                                new ConditionAlways(), new Attack(10)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[391].title, text[391].description,
                                -1, 7)).addPair(new ConditionAlways(),
                        new Attack(100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[392].title,
                                text[392].description, 30, 5)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Transform(text[393].title,
                                        text[393].description, 6)).addPair(
                                new ConditionUsed(), new Attack(10)));
                seq.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[394].title,
                                text[394].description, 6, 6)).addPair(
                        new ConditionAlways(), new Attack(125)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 19 ++stageCounter

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
                        new ResistanceShieldAction(text[208].title,
                                text[208].description, 99)).addPair(
                        new ConditionAlways(),
                        new ShieldAction(text[209].title,
                                text[209].description, 1, -1, 50)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(1, 65),
                        new MultipleAttack(text[210].title,
                                text[210].description, 200, 6)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Attack(
                                text[217].title, text[217].description, 1500)));
                zero.attackMode.addAttack(new ConditionHp(2, 5), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new MultipleAttack(text[211].title,
                                text[211].description, 90, 2)).addPair(
                        new ConditionAlways(),
                        new BindPets(text[212].title, text[212].description, 3,
                                2, 2, 1)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new MultipleAttack(text[211].title,
                                text[211].description, 90, 2)).addPair(
                        new ConditionAlways(),
                        new DarkScreen(text[213].title, text[213].description,
                                0)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new MultipleAttack(text[211].title,
                                text[211].description, 90, 2))
                        .addPair(
                                new ConditionAlways(),
                                new Gravity(text[215].title,
                                        text[215].description, 99)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new MultipleAttack(text[211].title,
                                text[211].description, 90, 2)).addPair(
                        new ConditionAlways(),
                        new RandomChange(text[214].title,
                                text[214].description, 6, 3)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new LockSkill(text[216].title, text[216].description,
                                10)).addPair(new ConditionAlways(),
                        new Attack(150)));
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
                        new ResistanceShieldAction(text[208].title,
                                text[208].description, 99)).addPair(
                        new ConditionAlways(),
                        new ShieldAction(text[209].title,
                                text[209].description, 1, -1, 50)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(1, 65),
                        new MultipleAttack(text[218].title,
                                text[218].description, 200, 6)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Attack(
                                text[222].title, text[222].description, 1500)));
                zero.attackMode.addAttack(new ConditionHp(2, 5), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack
                        .addAttack(new EnemyAttack()
                                .addAction(
                                        new DarkScreen(text[219].title,
                                                text[219].description, 0))
                                .addAction(new Attack(90))
                                .addAction(
                                        new Transform(text[220].title,
                                                text[220].description, 1, 4, 5,
                                                3, 0, 2))
                                .addPair(new ConditionAlways(), new Attack(90)));
                rndAttack
                        .addAttack(new EnemyAttack()
                                .addAction(
                                        new Transform(text[220].title,
                                                text[220].description, 1, 4, 5,
                                                3, 0, 2))
                                .addAction(new Attack(90))
                                .addAction(
                                        new SkillDown(text[221].title,
                                                text[221].description, 0, 0, 1))
                                .addPair(new ConditionAlways(), new Attack(90)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new SkillDown(text[221].title,
                                        text[221].description, 0, 0, 1))
                        .addAction(new Attack(90))
                        .addAction(
                                new DarkScreen(text[219].title,
                                        text[219].description, 0))
                        .addPair(new ConditionAlways(), new Attack(90)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new LockSkill(text[216].title, text[216].description,
                                10)).addPair(new ConditionAlways(),
                        new Attack(150)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 5), rndAttack);
            }

        }
        mStageEnemies.put(++stageCounter, data); // 20 ++stageCounter

        data = new ArrayList<Enemy>();
        {
            int[] prop = { 1, 4, 5, 3, 0 };
            int idx = RandomUtil.getInt(5);
            int choose = 1547 + idx;
            data.add(Enemy.create(env, choose, 18, 3821, 6000000, 1,
                    new Pair<Integer, Integer>(prop[idx], -1),
                    getMonsterTypes(env, choose)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createSimpleAttackAction("", "", 100);
        }
        mStageEnemies.put(++stageCounter, data); // 21
    }
}
