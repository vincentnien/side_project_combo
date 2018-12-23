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
import com.a30corner.combomaster.playground.action.impl.ColorChangeNew;
import com.a30corner.combomaster.playground.action.impl.ComboShield;
import com.a30corner.combomaster.playground.action.impl.DamageAbsorbShield;
import com.a30corner.combomaster.playground.action.impl.DamageVoidShield;
import com.a30corner.combomaster.playground.action.impl.DarkScreen;
import com.a30corner.combomaster.playground.action.impl.Daze;
import com.a30corner.combomaster.playground.action.impl.DropRateAttack;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.IntoVoid;
import com.a30corner.combomaster.playground.action.impl.LineChange;
import com.a30corner.combomaster.playground.action.impl.LockAwoken;
import com.a30corner.combomaster.playground.action.impl.LockOrb;
import com.a30corner.combomaster.playground.action.impl.LockSkill;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.RecoverDead;
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
import com.a30corner.combomaster.playground.enemy.attack.FromHeadAttack;
import com.a30corner.combomaster.playground.enemy.attack.RandomAttack;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAliveOnly;
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

public class UltimateArena3 extends IStage {

    @Override
    public void create(IEnvironment env,
                       SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        mStageEnemies.clear();

        ArrayList<Enemy> data;
        Enemy zero;
        SequenceAttack seq;

        List<StageGenerator.SkillText> list = loadSkillText(env, mStage); //
        StageGenerator.SkillText[] text = new StageGenerator.SkillText[list.size()];
        int len = text.length;
        for (int i = 0; i < len; ++i) {
            text[i] = getSkillText(list, i);
        }
        List<StageGenerator.SkillText> s23list = loadSkillText(env, "floor23");
        StageGenerator.SkillText[] s23text = new StageGenerator.SkillText[s23list.size()];
        int len23 = s23text.length;
        for (int i = 0; i < len23; ++i) {
            s23text[i] = getSkillText(s23list, i);
        }

        int stageCounter = 0;

        // level1
        //done
        data = new ArrayList<Enemy>();
        for (int i = 0; i < 2; ++i) {
            int rndId = 2305 + RandomUtil.getInt(5) * 2;
            if (rndId == 2305) {
                data.add(Enemy.create(env, rndId, 856303, 10465, 330, 1,
                        new Pair<Integer, Integer>(1, -1),
                        getMonsterTypes(env, rndId)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new SkillDown(text[0].title, text[0].description, 0, 1,
                                4)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new Gravity(text[1].title, text[1].description, 80)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[2].title, text[2].description, 150))
                        .addPair(new ConditionAlways(), new LineChange(7, 1)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);

            } else if (rndId == 2307) {
                data.add(Enemy.create(env, rndId, 916103, 10133, 330, 1,
                        new Pair<Integer, Integer>(4, -1),
                        getMonsterTypes(env, rndId)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[3].title, text[3].description, 7,
                                10)).addPair(new ConditionAlways(),
                        new Attack(50)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new DropRateAttack(text[4].title, text[4].description,
                                99, 4, 15)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[5].title, text[5].description, 150))
                        .addPair(new ConditionAlways(), new LineChange(9, 4)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);

            } else if (rndId == 2309) {
                data.add(Enemy.create(env, rndId, 769925, 9169, 330, 1,
                        new Pair<Integer, Integer>(5, -1),
                        getMonsterTypes(env, rndId)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ComboShield(text[6].title, text[6].description, 5,
                                4)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new ShieldAction(text[7].title, text[7].description, 1,
                                -1, 15)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[8].title, text[8].description,
                                80, 2)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (rndId == 2311) {
                data.add(Enemy.create(env, rndId, 1165269, 13222, 330, 2,
                        new Pair<Integer, Integer>(3, -1),
                        getMonsterTypes(env, rndId)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new BindPets(text[395].title, text[395].description, 2,
                                2, 3, 1)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[396].title, text[396].description, 80))
                        .addPair(new ConditionUsed(),
                                new LineChange(7, 3, 9, 3)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[397].title,
                                        text[397].description, 100))
                        .addPair(new ConditionAlways(),
                                new BindPets(3, 1, 1, 1)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (rndId == 2313) {
                data.add(Enemy.create(env, rndId, 751653, 9435, 330, 1,
                        new Pair<Integer, Integer>(0, -1),
                        getMonsterTypes(env, rndId)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Attack(
                                text[398].title, text[398].description, 100)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[399].title, text[399].description, 70))
                        .addPair(new ConditionUsed(), new DarkScreen(0)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[400].title,
                                        text[400].description, 150)).addPair(
                                new ConditionAlways(), new LineChange(5, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 1

        // done
        data = new ArrayList<Enemy>();
        {
            int[] L2 = { 812, 814, 1307, 1945, 2549 };
            int choose = L2[RandomUtil.getInt(L2.length)];
            if (choose == 812) {
                data.add(Enemy.create(env, choose, 4408978, 19134, 938, 1,
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
                data.add(Enemy.create(env, choose, 6442312, 17304, 938, 1,
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
                data.add(Enemy.create(env, choose, 4478112, 16694, 658, 1,
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
                                -1, 7)));
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
                data.add(Enemy.create(env, choose, 4427278, 15169, 658, 1,
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
            } else if (choose == 2549) {
                data.add(Enemy.create(env, choose, 6003010, 17731, 644, 1,
                        new Pair<Integer, Integer>(5, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ResistanceShieldAction(text[401].title,
                                text[401].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Gravity(
                                text[408].title, text[408].description, 100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[409].title,
                                text[409].description, 160, 2)));
                zero.attackMode.addAttack(new ConditionHp(2, 20), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new RecoverSelf(text[406].title, text[406].description,
                                30)));
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[407].title,
                                        text[407].description, 160)).addPair(
                                new ConditionAlways(), new ColorChange(-1, 4)));
                zero.attackMode.addAttack(new ConditionHp(2, 40), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[402].title,
                                        text[402].description, 140)).addPair(
                                new ConditionAlways(), new ColorChange(1, 7)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[403].title,
                                        text[403].description, 140)).addPair(
                                new ConditionAlways(),
                                new LineChange(8, 7, 9, 7)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[404].title,
                                        text[404].description, 100)).addPair(
                                new ConditionAlways(),
                                new LockOrb(1, 6, 5, 1, 4, 5, 3, 0)));
                rndAttack
                        .addAttack(new EnemyAttack().addPair(
                                new ConditionAlways(), new MultipleAttack(
                                        text[405].title, text[405].description,
                                        35, 3)));
                zero.attackMode.addAttack(new ConditionHp(1, 40), rndAttack);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 2++stageCounter
        data = new ArrayList<Enemy>();
        {
            int[] prop = { 1, 4, 5, 3, 0 };
            for (int i = 0; i < 4; ++i) {
                int idx = RandomUtil.getInt(5);
                int choose = 2723 + idx;
                data.add(Enemy.create(env, choose, 20, 11167, 3488889, 1,
                        new Pair<Integer, Integer>(prop[idx], -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createEmptyFirstStrike();

                String[] title = {
                        "エンソウルキャリー","スイソウルキャリー","ジュソウルキャリー",
                        "コウソウルキャリー","メイソウルキャリー"
                };
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(new RandomChange(title[idx], "", prop[idx], 1))
                        .addPair(new ConditionAlways(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 3
        // done
        data = new ArrayList<Enemy>();
        {
            int[] ids = { 823, 826, 1258, 1062 };
            int choose = ids[RandomUtil.getInt(ids.length)];
            if (choose == 823) {
                data.add(Enemy.create(env, choose, 2748050, 8540, 4200, 1,
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
                data.add(Enemy.create(env, choose, 3154717, 16165, 4200, 1,
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
                data.add(Enemy.create(env, choose, 6109862, 25844, 0, 2,
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
                data.add(Enemy.create(env, choose, 3055795, 32859, 0, 2,
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
        mStageEnemies.put(++stageCounter, data); // 4

        //done
        data = new ArrayList<Enemy>();
        {
            int[] ids = { 475, 476, 477, 478, 479 };
            int[] hp = { 2001947, 1989613, 1977280, 2026613, 2038947 };
            int[] atk = { 24124, 23779, 23384, 24519, 24864 };
            int[] bind = { 4, 5, 1, 0, 3 };
            int[] prop = { 1, 4, 5, 3, 0 };
            for (int i = 0; i < 2; ++i) {
                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];

                data.add(Enemy.create(env, choose, hp[rnd], atk[rnd], 2560, 1,
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
                data.add(Enemy.create(env, choose, 2560373, 11102, 0, 1,
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
                data.add(Enemy.create(env, choose, 2804373, 29402, 0, 2,
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
                data.add(Enemy.create(env, choose, 2036790, 17812, 0, 1,
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
                data.add(Enemy.create(env, choose, 1833457, 16287, 0, 1,
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
                data.add(Enemy.create(env, choose, 1543707, 25437, 408, 1,
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
        mStageEnemies.put(++stageCounter, data); // 6

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1228, 1229, 1230, 1466, 1468, 1469 };

            for (int i = 0; i < 3; ++i) {
                int rnd = RandomUtil.getInt(ids.length);
                int choose = ids[rnd];
                if (choose == 1228) {
                    data.add(Enemy.create(env, choose, 37, 14811, 600000, 2,
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
                    data.add(Enemy.create(env, choose, 37, 14811, 600000, 1,
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
                    data.add(Enemy.create(env, choose, 37, 14811, 600000, 1,
                            new Pair<Integer, Integer>(1, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createEmptyFirstStrike();
                    zero.attackMode.createSimpleAttackAction("", "", 100);
                } else if (choose == 1466) {
                    data.add(Enemy.create(env, choose, 37, 7777, 600000, 2,
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
                    data.add(Enemy.create(env, choose, 30, 13000, 600000, 2,
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
                    data.add(Enemy.create(env, choose, 37, 13000, 600000, 1,
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
        mStageEnemies.put(++stageCounter, data); // 7

        data = new ArrayList<Enemy>();
        {
            for (int i = 0; i < 1; ++i) {
                int idx = RandomUtil.getInt(9);
                int choose = 3317 + idx;
                data.add(Enemy.create(env, choose, 35, 3017, 5000000, 1,
                        new Pair<Integer, Integer>(3, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new SkillDown("キラキラ光るたまぁ~☆","", 0, 3, 3)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack("たまぁ~☆", "", 1000)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 8

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 2182, 1425, 1590, 1711, 2104, 2184 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];

            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 1266458, 15778, 0, 1,
                        new Pair<Integer, Integer>(4, 0),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 75);

                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Attack(text[410].title, text[410].description, 75)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Gravity(text[416].title,
                                        text[416].description, 99)).addPair(
                                new ConditionHp(2, 1),
                                new MultipleAttack(text[417].title,
                                        text[417].description, 60, 2)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new ComboShield(text[411].title, text[411].description,
                                99, 4)));
                seq.addAttack(new EnemyAttack().addPair(
                        new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                                .ordinal(), 2, 50),
                        new LockSkill(text[412].title, text[412].description, 5)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack
                        .addAttack(new EnemyAttack().addAction(
                                new Attack(text[413].title,
                                        text[413].description, 85)).addPair(
                                new ConditionAlways(), new RandomChange(0, 5)));
                rndAttack
                        .addAttack(new EnemyAttack().addPair(
                                new ConditionAlways(), new MultipleAttack(
                                        text[414].title, text[414].description,
                                        60, 2)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new Attack(text[415].title,
                                text[415].description, 100)));
                zero.attackMode.addAttack(new ConditionHp(1, 1), rndAttack);
            } else if (choose == ids[5]) {
                data.add(Enemy.create(env, choose, 6322345, 17914, 0, 1,
                        new Pair<Integer, Integer>(1, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Gravity(
                                text[416].title, text[416].description, 99)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                        new ShieldAction(text[419].title,
                                text[419].description, 1, -1, 75)));
                seq.addAttack(new EnemyAttack().addPair(
                        new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                                .ordinal(), 2, 50),
                        new Gravity(text[420].title, text[420].description, 100)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                RandomAttack rndAtk = new RandomAttack();
                rndAtk.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[421].title,
                                        text[421].description, 120)).addPair(
                                new ConditionAlways(),
                                new LineChange(3, 1, 8, 1, 10, 1)));
                rndAtk.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new Attack(text[422].title,
                                text[422].description, 100)));
                rndAtk.addAttack(new EnemyAttack()
                        .addAction(
                                new RecoverSelf(text[423].title,
                                        text[423].description, 10))
                        .addAction(
                                new Attack(text[424].title,
                                        text[424].description, 50))
                        .addPair(new ConditionHp(2, 80), new LineChange(2, 1)));
                rndAtk.addAttack(new EnemyAttack().addAction(
                        new Attack(text[425].title, text[425].description, 75))
                        .addPair(new ConditionHp(2, 80),
                                new BindPets(5, 2, 2, 1, 4)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rndAtk);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new LineChange(3, 1, 8, 1, 10, 1, 4, 3, 7, 3, 9, 3))
                        .addPair(
                                new ConditionAlways(),
                                new Attack(text[426].title,
                                        text[426].description, 200)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);
            } else if (choose == ids[1]) {
                data.add(Enemy.create(env, choose, 5380459, 25546, 648, 2,
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
                data.add(Enemy.create(env, choose, 4039963, 16227, 2160, 1,
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
                data.add(Enemy.create(env, choose, 5916695, 22184, 658, 1,
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
                data.add(Enemy.create(env, choose, 5086993, 22145, 504, 1,
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
            }
        }
        mStageEnemies.put(++stageCounter, data); // ++stageCounter 9

        // not ok
        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1533, 1534, 1535, 1748, 2320, 2398, 2400, 2402 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 1544012, 10289, 518, 1,
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
                data.add(Enemy.create(env, choose, 766262, 17914, 366667, 1,
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
                data.add(Enemy.create(env, choose, 2240428, 14864, 0, 1,
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
                data.add(Enemy.create(env, choose, 6506667, 65067, 0, 5,
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
                data.add(Enemy.create(env, choose, 9055043, 17019, 644, 1,
                        new Pair<Integer, Integer>(5, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new SkillDown(text[427].title, text[427].description,
                                0, 2, 2)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionTurns(6),
                        new MultipleAttack(text[431].title,
                                text[431].description, 150, 2)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
                        new ShieldAction(text[432].title,
                                text[432].description, 1, -1, 75)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1,
                        0, EnemyCondition.Type.FIND_PET.ordinal(), 3, 363, 362,
                        1423), new RandomChange(text[428].title,
                        text[428].description, 4, 1)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1,
                                0, EnemyCondition.Type.FIND_PET.ordinal(), 1, 1347),
                        new RecoverPlayer(text[429].title,
                                text[429].description, 100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1, 1,
                        0, EnemyCondition.Type.FIND_PET.ordinal(), 3, 374, 375,
                        655), new BindPets(text[430].title,
                        text[430].description, 0, 1, 1)));
                zero.attackMode.addAttack(new ConditionUsed(), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new Attack(text[433].title, text[433].description, 90))
                        .addPair(new ConditionAlways(), new LockOrb(0, 3)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[434].title,
                                        text[434].description, 100)).addPair(
                                new ConditionAlways(), new RandomChange(3, 1)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[435].title,
                                        text[435].description, 120)).addPair(
                                new ConditionAlways(),
                                new LineChange(2, 3, 6, 5)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[436].title,
                                        text[436].description, 150)).addPair(
                                new ConditionHp(2, 50),
                                new LineChange(7, 5, 8, 7)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), rndAttack);
            } else if (choose == ids[5]) {
                data.add(Enemy.create(env, choose, 6613010, 16389, 644, 1,
                        new Pair<Integer, Integer>(1, 0),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 1, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ResistanceShieldAction(text[437].title,
                                text[437].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                        1, 0, EnemyCondition.Type.FIND_PET.ordinal(), 4, 368,
                        369, 652, 1344), new Attack(text[438].title,
                        text[438].description, 130)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 2, 80),
                        new SkillDown(text[439].title, text[439].description,
                                0, 0, 2)));
                seq.addAttack(new EnemyAttack().addAction(
                        new DropRateAttack(text[440].title,
                                text[440].description, 99, 1, 25)).addPair(
                        new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                                .ordinal(), 2, 50),
                        new ChangeAttribute(text[441].title,
                                text[441].description, 1)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[442].title,
                                        text[442].description, 150)).addPair(
                                new ConditionAlways(), new Transform(1)));
                zero.attackMode.addAttack(new ConditionTurns(4), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[443].title,
                                        text[443].description, 120)).addPair(
                                new ConditionAlways(), new ColorChange(1, 7)));
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[444].title,
                                        text[444].description, 130)).addPair(
                                new ConditionAlways(), new RandomChange(1, 6)));
                zero.attackMode.addAttack(new ConditionHp(1, 20), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[445].title,
                                text[445].description, 70, 4)));
                zero.attackMode.addAttack(new ConditionHp(2, 20), seq);
            } else if (choose == ids[6]) {
                data.add(Enemy.create(env, choose, 6968843, 19439, 644, 1,
                        new Pair<Integer, Integer>(3, 4),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 5, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);

                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new AbsorbShieldAction(text[446].title,
                                text[446].description, 5, 0)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                        1, 0, EnemyCondition.Type.FIND_PET.ordinal(), 5, 376,
                        377, 656, 1348, 2396), new BindPets(text[447].title,
                        text[447].description, 5, 1, 1, 1, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 1, 70),
                        new ResistanceShieldAction(text[448].title,
                                text[448].description, 999)));
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[449].title,
                                        text[449].description, 100))
                        .addPair(
                                new ConditionUsedIf(1, 1, 0,
                                        EnemyCondition.Type.HP.ordinal(), 2, 70),
                                new DarkScreen(0)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[450].title, text[450].description, 80))
                        .addPair(new ConditionPercentage(60),
                                new SkillDown(0, 1, 1)));
                seq.addAttack(new EnemyAttack()
                        .addPair(new ConditionAlways(), new Attack(
                                text[451].title, text[451].description, 110)));
                zero.attackMode.addAttack(new ConditionHp(1, 50), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                        new ShieldAction(text[452].title,
                                text[452].description, 2, -1, 50)));
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[453].title,
                                        text[453].description, 130)).addPair(
                                new ConditionAlways(), new LineChange(8, 3)));
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[453].title,
                                        text[453].description, 130)).addPair(
                                new ConditionAlways(), new LineChange(8, 3)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[454].title,
                                text[454].description, 120, 4)));
                zero.attackMode.addAttack(new ConditionHp(2, 50), seq);
            } else if (choose == ids[7]) {
                data.add(Enemy.create(env, choose, 9968010, 16958, 644, 1,
                        new Pair<Integer, Integer>(0, 3),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[455].title,
                                        text[455].description, 150)).addPair(
                                new ConditionAlways(), new RandomChange(6, 4)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                                1, 0, EnemyCondition.Type.HP.ordinal(), 1, 100),
                        new ResistanceShieldAction(text[456].title,
                                text[456].description, 999)));
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[463].title, text[463].description,
                                2, 0)).addPair(
                        new ConditionIf(2, 1, 0, EnemyCondition.Type.HP
                                .ordinal(), 2, 20, 1, 0,
                                EnemyCondition.Type.FIND_ORB.ordinal(), 2),
                        new Attack(300)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
                        1, 0, EnemyCondition.Type.FIND_PET.ordinal(), 3, 1423,
                        1925, 2179), new BindPets(text[457].title,
                        text[457].description, 4, 5, 5, 1, 6)));
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[458].title, text[458].description, 4,
                                5, 5, 1, 6)).addPair(
                        new ConditionUsedIf(1, 1, 1,
                                EnemyCondition.Type.FIND_PET.ordinal(), 3,
                                1423, 1925, 2179),
                        new RandomChange(text[459].title,
                                text[459].description, 6, 4)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addPair(new ConditionIf(
                                1, 1, 0, EnemyCondition.Type.HP.ordinal(), 1, 20),
                        new ShieldAction(text[460].title,
                                text[460].description, 1, -1, 50)));
                rndAttack.addAttack(new EnemyAttack().addPair(new ConditionIf(
                                1, 1, 0, EnemyCondition.Type.HP.ordinal(), 1, 20),
                        new DropRateAttack(text[461].title,
                                text[461].description, 10, 6, 10)));
                rndAttack.addAttack(new EnemyAttack().addPair(new ConditionIf(
                                1, 1, 0, EnemyCondition.Type.HP.ordinal(), 1, 20),
                        new SkillDown(text[462].title, text[462].description,
                                0, 1, 2)));
                zero.attackMode.addAttack(new ConditionUsedIf(1, 1, 0,
                        EnemyCondition.Type.HP.ordinal(), 2, 50), rndAttack);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[463].title,
                                        text[463].description, 120)).addPair(
                                new ConditionAlways(), new LockOrb(0, 6, 8)));
                zero.attackMode.addAttack(new ConditionTurns(3), seq);

                rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[464].title,
                                        text[464].description, 120)).addPair(
                                new ConditionAlways(), new RandomChange(6, 2)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new Attack(100)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), rndAttack);
            }

        }
        mStageEnemies.put(++stageCounter, data);// 10 ++stageCounter

        // check it, ok
        data = new ArrayList<Enemy>();
        {
            for (int i = 0; i < 2; ++i) {
                int rnd = RandomUtil.getInt(6);
                int choose = 2333 + rnd;
                if (choose == 2333) {
                    data.add(Enemy.create(env, choose, 2333031, 7342, 550, 1,
                            new Pair<Integer, Integer>(1, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.setHasResurrection();
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new AbsorbShieldAction(
                                    text[466].title, text[466].description, 5,
                                    1)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addPair(new ConditionAliveOnly(), new RecoverDead(
                                    text[467].title, text[467].description, 45)));
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                                    .ordinal(), 2, 80), new SkillDown(
                                    text[468].title, text[468].description, 0,
                                    1, 1)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    RandomAttack rndAttack = new RandomAttack();
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new ColorChange(text[469].title,
                                    text[469].description, -1, 1)).addPair(
                            new ConditionAlways(), new Attack(120)));
                    rndAttack.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new MultipleAttack(
                                    text[470].title, text[470].description, 50,
                                    3)));
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new Attack(text[471].title, text[471].description,
                                    120)).addPair(new ConditionAlways(),
                            new ColorChange(1, 6)));
                    zero.attackMode
                            .addAttack(new ConditionHp(1, 10), rndAttack);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new MultipleAttack(
                                    text[472].title, text[472].description,
                                    500, 2)));
                    zero.attackMode.addAttack(new ConditionHp(2, 10), seq);
                } else if (choose == 2334) {
                    data.add(Enemy.create(env, choose, 2479208, 6977, 550, 1,
                            new Pair<Integer, Integer>(4, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.setHasResurrection();
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new AbsorbShieldAction(
                                    text[473].title, text[473].description, 5,
                                    4)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addPair(new ConditionAliveOnly(), new RecoverDead(
                                    text[467].title, text[467].description, 45)));
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                                    .ordinal(), 2, 80), new SkillDown(
                                    text[468].title, text[468].description, 0,
                                    1, 1)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    RandomAttack rndAttack = new RandomAttack();
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new Attack(text[474].title, text[474].description,
                                    120)).addPair(new ConditionAlways(),
                            new RandomChange(4, 4)));
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new Attack(text[475].title, text[475].description,
                                    120)).addPair(new ConditionAlways(),
                            new LineChange(8, 4)));
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new Attack(text[476].title, text[476].description,
                                    80)).addPair(new ConditionAlways(),
                            new ColorChange(4, 7)));
                    zero.attackMode
                            .addAttack(new ConditionHp(1, 10), rndAttack);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new MultipleAttack(
                                    text[472].title, text[472].description,
                                    500, 2)));
                    zero.attackMode.addAttack(new ConditionHp(2, 10), seq);
                } else if (choose == 2335) {
                    data.add(Enemy.create(env, choose, 2778208, 7741, 550, 1,
                            new Pair<Integer, Integer>(3, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.setHasResurrection();
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new Attack(text[477].title, text[477].description,
                                    60)).addPair(new ConditionAlways(),
                            new BindPets(0, 2, 2, 0)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addPair(new ConditionAliveOnly(), new RecoverDead(
                                    text[467].title, text[467].description, 45)));
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                                    .ordinal(), 2, 80), new SkillDown(
                                    text[468].title, text[468].description, 0,
                                    1, 1)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    RandomAttack rndAttack = new RandomAttack();
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new Attack(text[478].title, text[478].description,
                                    120)).addPair(new ConditionAlways(),
                            new LockOrb(0, 3)));
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new Attack(text[479].title, text[479].description,
                                    100)).addPair(new ConditionAlways(),
                            new RandomChange(3, 5)));
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new Attack(text[480].title, text[480].description,
                                    80)).addPair(new ConditionHp(2, 70),
                            new ColorChange(3, 6)));
                    zero.attackMode
                            .addAttack(new ConditionHp(1, 10), rndAttack);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new MultipleAttack(
                                    text[472].title, text[472].description,
                                    500, 2)));
                    zero.attackMode.addAttack(new ConditionHp(2, 10), seq);
                } else if (choose == 2336) {
                    data.add(Enemy.create(env, choose, 2479208, 6977, 12375, 1,
                            new Pair<Integer, Integer>(5, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.setHasResurrection();
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new AbsorbShieldAction(
                                    text[481].title, text[481].description, 5,
                                    5)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addPair(new ConditionAliveOnly(), new RecoverDead(
                                    text[467].title, text[467].description, 45)));
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                                    .ordinal(), 2, 80), new SkillDown(
                                    text[468].title, text[468].description, 0,
                                    1, 1)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    RandomAttack rndAttack = new RandomAttack();
                    rndAttack.addAttack(new EnemyAttack().addPair(
                            new ConditionIf(1, 1, 1,
                                    EnemyCondition.Type.HAS_DEBUFF.ordinal(),
                                    2, 5), new DropRateAttack(text[482].title,
                                    text[482].description, 99, 5, 20)));
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new Attack(text[483].title, text[483].description,
                                    100)).addPair(new ConditionAlways(),
                            new LineChange(6, 5)));
                    rndAttack.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new MultipleAttack(
                                    text[484].title, text[484].description, 60,
                                    2)));
                    zero.attackMode
                            .addAttack(new ConditionHp(1, 10), rndAttack);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new MultipleAttack(
                                    text[472].title, text[472].description,
                                    500, 2)));
                    zero.attackMode.addAttack(new ConditionHp(2, 10), seq);
                } else if (choose == 2337) {
                    data.add(Enemy.create(env, choose, 2911097, 6279, 12375, 1,
                            new Pair<Integer, Integer>(3, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.setHasResurrection();
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(),
                            new ResistanceShieldAction(text[485].title,
                                    text[485].description, 999)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addPair(new ConditionAliveOnly(), new RecoverDead(
                                    text[467].title, text[467].description, 45)));
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                                    .ordinal(), 2, 80), new SkillDown(
                                    text[468].title, text[468].description, 0,
                                    1, 1)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    RandomAttack rndAttack = new RandomAttack();
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new Attack(text[486].title, text[486].description,
                                    100)).addPair(new ConditionAlways(),
                            new DarkScreen(0)));
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new Attack(text[487].title, text[487].description,
                                    100)).addPair(new ConditionAlways(),
                            new BindPets(3, 4, 4, 1)));
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new Attack(text[488].title, text[488].description,
                                    120)).addPair(new ConditionAlways(),
                            new ColorChange(3, 7)));
                    zero.attackMode
                            .addAttack(new ConditionHp(1, 10), rndAttack);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new MultipleAttack(
                                    text[472].title, text[472].description,
                                    500, 2)));
                    zero.attackMode.addAttack(new ConditionHp(2, 10), seq);
                } else if (choose == 2338) {
                    data.add(Enemy.create(env, choose, 2678542, 7043, 12375, 1,
                            new Pair<Integer, Integer>(0, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.setHasResurrection();
                    zero.attackMode = new EnemyAttackMode();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new ShieldAction(
                                    text[489].title, text[489].description, 3,
                                    -1, 30)));
                    zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack()
                            .addPair(new ConditionAliveOnly(), new RecoverDead(
                                    text[467].title, text[467].description, 45)));
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                                    .ordinal(), 2, 80), new SkillDown(
                                    text[468].title, text[468].description, 0,
                                    1, 1)));
                    zero.attackMode.addAttack(new ConditionAlways(), seq);

                    RandomAttack rndAttack = new RandomAttack();
                    rndAttack.addAttack(new EnemyAttack().addAction(
                            new Attack(text[490].title, text[490].description,
                                    100)).addPair(new ConditionAlways(),
                            new DarkScreen(0)));
                    rndAttack.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new RandomChange(
                                    text[491].title, text[491].description, 6,
                                    4)));
                    rndAttack
                            .addAttack(new EnemyAttack().addPair(
                                    new ConditionHp(2, 70), new Gravity(
                                            text[492].title,
                                            text[492].description, 75)));
                    zero.attackMode
                            .addAttack(new ConditionHp(1, 10), rndAttack);

                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new MultipleAttack(
                                    text[472].title, text[472].description,
                                    500, 2)));
                    zero.attackMode.addAttack(new ConditionHp(2, 10), seq);
                }
            }
        }
        mStageEnemies.put(++stageCounter, data);// 11

        // ok
        data = new ArrayList<Enemy>();
        {
            int rnd = RandomUtil.getInt(5);
            int choose = 746 + 2 * rnd;
            if (choose == 746) {
                data.add(Enemy.create(env, choose, 3419207, 14534, 496, 1,
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
                data.add(Enemy.create(env, choose, 3419207, 14534, 496, 1,
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
                data.add(Enemy.create(env, choose,  3419207, 14534, 496, 1,
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
                data.add(Enemy.create(env, choose,  3419207, 14534, 496, 1,
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
                data.add(Enemy.create(env, choose,  3419207, 14534, 496, 1,
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
        mStageEnemies.put(++stageCounter, data); // 12

        // ok
        data = new ArrayList<Enemy>();
        {
            for (int i = 0; i < 2; ++i) {
                int choose = 2299 + RandomUtil.getInt(5);
                if (choose == 2299) {
                    data.add(Enemy.create(env, choose, 55, 9000, 10000000, 1,
                            new Pair<Integer, Integer>(1, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createEmptyFirstStrike()
                            .createEmptyMustAction();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new Attack(text[493].title, text[493].description,
                                    300)).addPair(new ConditionAlways(),
                            new RandomChange(1, 5)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (choose == 2300) {
                    data.add(Enemy.create(env, choose, 55, 9000, 10000000, 1,
                            new Pair<Integer, Integer>(4, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createEmptyFirstStrike()
                            .createEmptyMustAction();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new Attack(text[494].title, text[494].description,
                                    300)).addPair(new ConditionAlways(),
                            new RandomChange(4, 5)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (choose == 2301) {
                    data.add(Enemy.create(env, choose, 55, 9000, 10000000, 1,
                            new Pair<Integer, Integer>(5, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createEmptyFirstStrike()
                            .createEmptyMustAction();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new Attack(text[495].title, text[495].description,
                                    300)).addPair(new ConditionAlways(),
                            new RandomChange(5, 5)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (choose == 2302) {
                    data.add(Enemy.create(env, choose, 55, 9000, 10000000, 1,
                            new Pair<Integer, Integer>(3, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createEmptyFirstStrike()
                            .createEmptyMustAction();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new Attack(text[496].title, text[496].description,
                                    300)).addPair(new ConditionAlways(),
                            new RandomChange(3, 5)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                } else if (choose == 2303) {
                    data.add(Enemy.create(env, choose, 55, 9000, 10000000, 1,
                            new Pair<Integer, Integer>(0, -1),
                            getMonsterTypes(env, choose)));
                    zero = data.get(i);
                    zero.attackMode = new EnemyAttackMode();
                    zero.attackMode.createEmptyFirstStrike()
                            .createEmptyMustAction();
                    seq = new SequenceAttack();
                    seq.addAttack(new EnemyAttack().addAction(
                            new Attack(text[497].title, text[497].description,
                                    300)).addPair(new ConditionAlways(),
                            new RandomChange(0, 5)));
                    zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
                }
            }
        }
        mStageEnemies.put(++stageCounter, data); // 13 ++stageCounter

        // not ok
        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1922, 1760 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 11799128, 75, 3990, 1,
                        new Pair<Integer, Integer>(3, 0),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);

                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new ResistanceShieldAction(text[498].title,
                                text[498].description, 2)).addPair(
                        new ConditionAlways(),
                        new ComboShield(text[499].title, text[499].description,
                                999, 4)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Gravity(text[511].title,
                                        text[511].description, 95)).addPair(
                                new ConditionAlways(),
                                new RecoverSelf(text[512].title,
                                        text[512].description, 50)));
                zero.attackMode.addAttack(new ConditionHp(2, 10), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[502].title, text[502].description, 4,
                                2, 4, 1, 6)).addPair(new ConditionUsed(),
                        new Daze(text[508].title, text[508].description)));
                seq.addAttack(new EnemyAttack().addAction(
                        new MultipleAttack(text[509].title,
                                text[509].description, 4500, 10)).addPair(
                        new ConditionAlways(),
                        new Attack(text[510].title, text[510].description,
                                80000)));
                zero.attackMode.addAttack(new ConditionHp(2, 40), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[500].title,
                                        text[500].description, 12000))
                        .addAction(new LineChange(8, 3, 9, 3))
                        .addPair(
                                new ConditionAlways(),
                                new ResistanceShieldAction(text[503].title,
                                        text[503].description, 2)));
                zero.attackMode.addAttack(new ConditionTurns(3), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[500].title,
                                        text[500].description, 12000))
                        .addAction(new LineChange(8, 3, 9, 3))
                        .addPair(
                                new ConditionAlways(),
                                new MultipleAttack(text[504].title,
                                        text[504].description, 6000, 3)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[501].title,
                                        text[501].description, 15000))
                        .addAction(new LineChange(6, 3))
                        .addAction(
                                new Attack(text[505].title,
                                        text[505].description, 7500))
                        .addPair(new ConditionAlways(), new ColorChange(3, 7)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[501].title,
                                        text[501].description, 15000))
                        .addAction(new LineChange(6, 3))
                        .addPair(
                                new ConditionAlways(),
                                new MultipleAttack(text[504].title,
                                        text[504].description, 6000, 3)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[500].title,
                                        text[500].description, 12000))
                        .addAction(new LineChange(8, 3, 9, 3))
                        .addAction(
                                new Attack(text[506].title,
                                        text[506].description, 10000))
                        .addPair(new ConditionAlways(),
                                new BindPets(2, 1, 2, 1)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[501].title,
                                        text[501].description, 15000))
                        .addAction(new LineChange(6, 3))
                        .addPair(
                                new ConditionAlways(),
                                new Gravity(text[507].title,
                                        text[507].description, 99)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new BindPets(text[502].title, text[502].description, 4,
                                2, 4, 1, 6))
                        .addPair(
                                new ConditionAlways(),
                                new Gravity(text[507].title,
                                        text[507].description, 99)));
                zero.attackMode.addAttack(new ConditionHp(1, 40), rndAttack);

            } else if (choose == ids[1]) {
                data.add(Enemy.create(env, choose, 8139230, 15189, 4060, 1,
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
        mStageEnemies.put(++stageCounter, data); // 14 ++stageCounter

        // ok
        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 1324, 66673, 5500, 1500000, 1,
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
        mStageEnemies.put(++stageCounter, data); // 15 ++stageCounter

        // ok
        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1663, 1463, 1465, 1837, 2008 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 3052033, 22570, 1400, 1,
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
                data.add(Enemy.create(env, choose, 1887954, 25342, 0, 1,
                        new Pair<Integer, Integer>(3, 0),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new DamageAbsorbShield(text[228].title,
                                text[228].description, 99, 200000)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();
//				seq = new SequenceAttack();
//
//				zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Daze(text[229].title, text[229].description)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Daze("3", "")));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Daze("2", "")));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new Daze("1", "")));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[230].title,
                                text[230].description, 200, 7)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if (choose == ids[2]) {
                data.add(Enemy.create(env, choose, 2299903, 15170, 0, 1,
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
                data.add(Enemy.create(env, choose, 6120028, 16389, 658, 1,
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
                data.add(Enemy.create(env, choose, 5189677, 12078, 644, 1,
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
        mStageEnemies.put(++stageCounter, data); // 16 ++stageCounter

        // ok
        data = new ArrayList<Enemy>();
        {
            int[] ids = { 757, 1095, 1092, 760 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 2545327, 18036, 504, 1,
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
                data.add(Enemy.create(env, choose, 3562298, 14925, 546, 1,
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
                data.add(Enemy.create(env, choose, 3664168, 43961, 8610, 3,
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
            } else if (choose == ids[3]) {
                data.add(Enemy.create(env, choose, 6103660, 16348, 504, 1,
                        new Pair<Integer, Integer>(3, 5),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 5, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ResistanceShieldAction(text[543].title,
                                text[543].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[547].title,
                                        text[547].description, 200)).addPair(
                                new ConditionHp(2, 30),
                                new BindPets(3, 1, 3, 0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionTurns(3),
                        new ShieldAction(text[544].title,
                                text[544].description, 1, -1, 50)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                RandomAttack attack = new RandomAttack();
                attack.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[545].title,
                                        text[545].description, 150)).addPair(
                                new ConditionAlways(), new ColorChange(0, 2)));
                attack.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[546].title,
                                        text[546].description, 150)).addPair(
                                new ConditionAlways(),
                                new LineChange(2, 3, 6, 5)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), attack);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 157

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1955, 1726, 1956, 2075 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 10000000, 99999, 10000000,
                        1, new Pair<Integer, Integer>(4, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new LockAwoken(text[513].title, text[513].description,
                                6))
                        .addPair(
                                new ConditionAlways(),
                                new LockSkill(text[514].title,
                                        text[514].description, 6)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[515].title,
                                        text[515].description, 1000)).addPair(
                                new ConditionIf(1, 1, 0,
                                        EnemyCondition.Type.FIND_ORB.ordinal(),
                                        7), new ColorChange(7, 4)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new RandomChange(text[516].title,
                                text[516].description, 7, 5)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new RandomChange(text[517].title,
                                text[517].description, 7, 6)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new RandomChange(text[518].title,
                                text[518].description, 7, 10)));
                seq.addAttack(new EnemyAttack().addAction(
                        new DarkScreen(text[519].title, text[519].description,
                                0)).addPair(
                        new ConditionAlways(),
                        new RandomChange(text[520].title,
                                text[520].description, 7, 15)));
                seq.addAttack(new EnemyAttack().addAction(
                        new ReduceTime(text[521].title, text[521].description,
                                1, -1000)).addPair(
                        new ConditionAlways(),
                        new RandomChange(text[522].title,
                                text[522].description, 7, 9)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new RecoverSelf(text[523].title, text[523].description,
                                -100)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);

            } else if (choose == ids[1]) {
                data.add(Enemy.create(env, choose, 4782603, 17202, 588, 1,
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
            } else if (choose == ids[2]) {
                data.add(Enemy.create(env, choose, 3054270, 18361, 0, 1,
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
                data.add(Enemy.create(env, choose, 366667, 24319, 1666667, 1,
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
        mStageEnemies.put(++stageCounter, data); // 18 ++stageCounter

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 1849, 10279110, 15433, 0, 1,
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
        mStageEnemies.put(++stageCounter, data); // 19 ++stageCounter

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1536, 1737, 1954, 2081 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == 1536) {
                data.add(Enemy.create(env, choose, 7327930, 25254, 5460, 1,
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
            } else if (choose == 1737) {
                data.add(Enemy.create(env, choose, 7241513, 23729, 27300, 1,
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
            } else if (choose == 1954) {
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
                                new SkillDown(text[278].title,
                                        text[278].description, 0, 2, 2))
                        .addAction(
                                new ComboShield(text[279].title,
                                        text[279].description, 99, 4))
                        .addPair(
                                new ConditionAlways(),
                                new Gravity(text[280].title,
                                        text[280].description, 75)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Gravity(text[280].title,
                                        text[280].description, 75))
                        .addAction(
                                new Transform(text[286].title,
                                        text[286].description, 1))
                        .addPair(new ConditionHp(2, 30), new Attack(300)));
                seq.addAttack(new EnemyAttack().addAction(
                        new ColorChange(text[281].title, text[281].description,
                                6, 2)).addPair(
                        new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB
                                .ordinal(), 6), new Attack(200)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new LineChange(text[282].title, text[282].description, 4,
                                1, 7, 1, 9, 1)).addPair(new ConditionAlways(),
                        new Attack(100)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new MultipleAttack(
                                text[283].title, text[283].description, 40, 3)));
                rndAttack.addAttack(new EnemyAttack().addAction(
                        new RandomChange(text[284].title, text[284].description,
                                6, 7)).addPair(new ConditionAlways(),
                        new Attack(50)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new DarkScreen(text[285].title,
                                        text[285].description, 0)).addPair(
                                new ConditionAlways(), new Attack(75)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
            } else if (choose == 2081) {
                data.add(Enemy.create(env, choose, 9414704, 31878, 0, 1,
                        new Pair<Integer, Integer>(5, -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new DropRateAttack(text[524].title,
                                        text[524].description, 99, 7, 15))
                        .addAction(new DropRateAttack(99, 2, 15))
                        .addPair(
                                new ConditionAlways(),
                                new DamageAbsorbShield(text[525].title,
                                        text[525].description, 999, 1000000)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[528].title,
                                        text[528].description, 70))
                        .addAction(new BindPets(3, 2, 2, 5))
                        .addAction(
                                new Attack(text[529].title,
                                        text[529].description, 10))
                        .addPair(
                                new ConditionUsedIf(1, 1, 0,
                                        EnemyCondition.Type.HP.ordinal(), 2, 50),
                                new RecoverSelf(10)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new Daze(text[526].title, text[526].description))
                        .addPair(
                                new ConditionIf(1, 1, 0,
                                        EnemyCondition.Type.FIND_PET.ordinal(),
                                        5, 238, 239, 1114, 1115, 1955),
                                new ResistanceShieldAction(text[527].title,
                                        text[527].description, 999)));
                zero.attackMode.addAttack(new ConditionUsed(), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[530].title,
                                        text[530].description, 80))
                        .addAction(new ColorChange(2, 7))
                        .addAction(
                                new Attack(text[529].title,
                                        text[529].description, 10))
                        .addPair(new ConditionAlways(), new RecoverSelf(10)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new MultipleAttack(text[531].title,
                                        text[531].description, 35, 4))
                        .addAction(
                                new Attack(text[529].title,
                                        text[529].description, 10))
                        .addPair(new ConditionAlways(), new RecoverSelf(10)));
                rndAttack.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[532].title,
                                        text[532].description, 120))
                        .addAction(new LineChange(0, 2, 2, 5))
                        .addAction(
                                new Attack(text[529].title,
                                        text[529].description, 10))
                        .addPair(new ConditionAlways(), new RecoverSelf(10)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new DarkScreen(text[533].title, text[533].description,
                                0)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[534].title,
                                text[534].description, 1000, 3)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);
            }

        }
        mStageEnemies.put(++stageCounter, data); // 20 ++stageCounter

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1215, 1472, 1631, 2092, 2277 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 9156913, 22977, 9520, 1,
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
                data.add(Enemy.create(env, choose, 7340943, 15128, 0, 1,
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
                data.add(Enemy.create(env, choose, 7427360, 15433, 0, 1,
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
                data.add(Enemy.create(env, choose, 10378845, 26332, 1218, 1,
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
                data.add(Enemy.create(env, choose, 10890813, 32513, 1218, 1,
                        new Pair<Integer, Integer>(4, 0),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 30);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new ResistanceShieldAction(text[535].title,
                                text[535].description, 999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(
                        new Attack(text[536].title, text[536].description, 80))
                        .addPair(new ConditionHp(2, 1), new RecoverSelf(50)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack()
                        .addAction(
                                new Attack(text[540].title,
                                        text[540].description, 100))
                        .addAction(new Transform(4, 2, 7))
                        .addPair(
                                new ConditionUsed(),
                                new LockOrb(text[541].title,
                                        text[541].description, 0, 7)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                        new MultipleAttack(text[542].title,
                                text[542].description, 100, 5)));
                zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

                RandomAttack rndAttack = new RandomAttack();
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionAlways(), new Gravity(text[537].title,
                                text[537].description, 99)));
                rndAttack.addAttack(new EnemyAttack().addPair(
                        new ConditionPercentage(80), new Attack(
                                text[538].title, text[538].description, 120)));
                rndAttack
                        .addAttack(new EnemyAttack().addPair(
                                new ConditionPercentage(40),
                                new MultipleAttack(text[539].title,
                                        text[539].description, 70, 2)));
                zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 21 ++stageCounter

        data = new ArrayList<Enemy>();
        {
            int[] ids = { 985, 1422, 1371 };
            int rnd = RandomUtil.getInt(ids.length);
            int choose = ids[rnd];
            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 6105897, 23119, 812, 1,
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
                data.add(Enemy.create(env, choose, 10174597, 28304, 5460, 1,
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
            } else if (choose == ids[2]) {
                data.add(Enemy.create(env, choose, 7802307, 29829, 5460, 1,
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
        mStageEnemies.put(++stageCounter, data); // 22 ++stageCounter

        // u3 stage 23 very hard
        data = new ArrayList<Enemy>();
        {
            int[] prop = { 1, 4, 5, 3, 0 };
            int idx = RandomUtil.getInt(5);
            int choose = 2716 + idx;
            if(choose == 2716) {
                data.add(Enemy.create(env, choose, 78000000, 11744, 1744, 1,
                        new Pair<Integer, Integer>(prop[idx], -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);
                zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 50);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction( new ResistanceShieldAction(s23text[65].title,s23text[65].description,999)).addPair(new ConditionAlways(), new SkillDown(s23text[66].title,s23text[66].description,0,15,15)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 1), new RecoverSelf(s23text[67].title,s23text[67].description, 50)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(s23text[68].title,s23text[68].description, 101)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(s23text[69].title,s23text[69].description, 102, 2)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(s23text[70].title,s23text[70].description, 103, 3)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(s23text[71].title,s23text[71].description, 104, 4)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(s23text[72].title,s23text[72].description, 105, 5)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(s23text[73].title,s23text[73].description, 106, 6)));
                zero.attackMode.addAttack(new ConditionHp(1,1), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(s23text[74].title,s23text[74].description,517, 7)));
                zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
            } else if(choose == 2717) {
                data.add(Enemy.create(env, choose, 31150000, 11342, 1962, 1,
                        new Pair<Integer, Integer>(prop[idx], -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                int[][] absorb = {{1,3},{1,0}, {4,3},{4,0}, {5,3},{5,0}};
                int rnd = RandomUtil.getInt(6);
                seq.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(s23text[3+rnd].title,s23text[3+rnd].description,1,absorb[rnd][0])).addAction(new AbsorbShieldAction(1,absorb[rnd][1])).addAction( new DamageVoidShield(s23text[2].title,s23text[2].description,99, 5000000)).addPair(new ConditionAlways(), new ResistanceShieldAction(s23text[1].title,s23text[1].description,999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(s23text[11].title,s23text[11].description, 1000,3)).addAction(new IntoVoid(s23text[10].title,s23text[10].description, 0)).addPair(new ConditionHp(2, 10), new ShieldAction(s23text[9].title,s23text[9].description, 1,-1,75)));
                seq.addAttack(new EnemyAttack().addAction(new Attack(s23text[13].title,s23text[13].description, 450)).addAction(new Transform(1,4,5,3,0)).addAction(new LockSkill(s23text[12].title,s23text[12].description, 10)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 90), new ShieldAction(s23text[9].title,s23text[9].description, 1,-1,75)));
                seq.addAttack(new EnemyAttack().addAction(new Attack(s23text[15].title,s23text[15].description, 600)).addAction(new Transform(1,4,5,3,0)).addAction(new DarkScreen(s23text[14].title,s23text[14].description, 0)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 70), new ShieldAction(s23text[9].title,s23text[9].description, 1,-1,75)));
                seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(s23text[17].title,s23text[17].description, 400,3)).addAction(new SkillDown(s23text[16].title,s23text[16].description, 0,5,5)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new ShieldAction(s23text[9].title,s23text[9].description, 1,-1,75)));
                seq.addAttack(new EnemyAttack().addAction(new Attack(s23text[19].title,s23text[19].description, 2000)).addAction(new Transform(1,4,5,3,0)).addAction(new LockAwoken(s23text[18].title,s23text[18].description, 2)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 30), new ShieldAction(s23text[9].title,s23text[9].description, 1,-1,75)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(s23text[26].title,s23text[26].description,150)).addAction(new ColorChange(-1,7)).addAction(new Attack(s23text[25].title,s23text[25].description,50)).addAction(new BindPets(2,1,1,1)).addPair(new ConditionAlways(), new Gravity(s23text[27].title,s23text[27].description,99)));
                zero.attackMode.addAttack(new ConditionHp(2,50), seq);

                RandomAttack ra = new RandomAttack();
                int[][] colorIndex = {{20,23},{20,24},{21,23},{21,24},{22,23},{22,24}};
                for(int i=0; i<6; ++i) {
                    for(int j=0; j<2; ++j) {
                        ra.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(s23text[3+i].title,s23text[3+i].description,1,absorb[i][0])).addAction(new AbsorbShieldAction(1,absorb[i][1])).addAction(new Attack(s23text[colorIndex[i][j]].title,s23text[colorIndex[i][j]].description,100)).addAction(new ColorChange(-1, absorb[i][j])).addAction(new Attack(s23text[25].title,s23text[25].description,50)).addPair(new ConditionAlways(), new BindPets(2,1,1,1)));
                    }
                }
                zero.attackMode.addAttack(new ConditionHp(1,50), ra);
            } else if(choose == 2718) {
                data.add(Enemy.create(env, choose, 39000000, 9892, 50000, 1,
                        new Pair<Integer, Integer>(prop[idx], -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction( new DamageVoidShield(s23text[30].title,s23text[30].description,99, 6000000)).addAction(new ShieldAction(s23text[29].title,s23text[29].description,7,-1,75)).addPair(new ConditionAlways(), new ResistanceShieldAction(s23text[28].title,s23text[28].description,999)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new RandomChange(s23text[31].title,s23text[31].description, 7,3)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(s23text[32].title,s23text[32].description, 1,690)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(690)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new RandomChange(s23text[33].title,s23text[33].description, 7,3)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(s23text[34].title,s23text[34].description, 1,690)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(690)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new DarkScreen(s23text[35].title,s23text[35].description, 1)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                int[][] absorb = {{1,4},{4,5}, {4,3},{3,0}, {1,0}};
                int rnd = RandomUtil.getInt(5);
                seq.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(s23text[37+rnd].title,s23text[37+rnd].description,3,absorb[rnd][0])).addAction(new AbsorbShieldAction(3,absorb[rnd][1])).addAction(new Attack(s23text[36].title,s23text[36].description,450)).addPair(new ConditionHp(1,50), new LineChange(8,5,9,5)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2,50), new Gravity(s23text[42].title,s23text[42].description,500)));
                zero.attackMode.addAttack(new ConditionUsed(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(s23text[42].title,s23text[42].description,500)));
                zero.attackMode.addAttack(new ConditionHp(2,20), seq);

                RandomAttack rndAtk = new RandomAttack();
                rndAtk.addAttack(new EnemyAttack().addAction(new Attack(s23text[44].title,s23text[44].description,420)).addPair(new ConditionAlways(), new RecoverSelf(10)));
                rndAtk.addAttack(new EnemyAttack().addAction(new Attack(s23text[45].title,s23text[45].description,410)).addPair(new ConditionAlways(), new BindPets(3,5,5,1)));
                rndAtk.addAttack(new EnemyAttack().addAction(new Attack(s23text[46].title,s23text[46].description,340)).addPair(new ConditionAlways(), new LockOrb(1,6,1,4,5,3,0,2,6,7,8)));
                rndAtk.addAttack(new EnemyAttack().addAction(new Attack(s23text[47].title,s23text[47].description,360)).addPair(new ConditionAlways(), new DarkScreen(0)));
                zero.attackMode.addAttack(new ConditionHp(2,30), rndAtk);

                rndAtk = new RandomAttack();
                rndAtk.addAttack(new EnemyAttack().addAction(new Attack(s23text[46].title,s23text[46].description,340)).addPair(new ConditionAlways(), new LockOrb(1,6,1,4,5,3,0,2,6,7,8)));
                rndAtk.addAttack(new EnemyAttack().addAction(new Attack(s23text[47].title,s23text[47].description,360)).addPair(new ConditionAlways(), new DarkScreen(0)));
                zero.attackMode.addAttack(new ConditionHp(2,50), rndAtk);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Attack(s23text[43].title,s23text[43].description,260)).addPair(new ConditionAlways(), new SkillDown(0,3,5)));
                zero.attackMode.addAttack(new ConditionHp(1,50), seq);
            } else if(choose == 2719) {
                data.add(Enemy.create(env, choose, 78000000, 8103, 1744, 1,
                        new Pair<Integer, Integer>(prop[idx], -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new DamageVoidShield(s23text[48].title,s23text[48].description,99, 20000000)).addAction(new ResistanceShieldAction(s23text[49].title,s23text[49].description,999)).addPair(new ConditionAlways(), new Gravity(s23text[50].title,s23text[50].description,35)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
                zero.attackMode.createEmptyMustAction();

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new IntoVoid(s23text[51].title,s23text[51].description,0)).addPair(new ConditionAlways(), new Gravity(s23text[52].title,s23text[52].description,500)));
                zero.attackMode.addAttack(new ConditionHp(1,90), seq);

                FromHeadAttack head = new FromHeadAttack();
                head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(s23text[53].title,s23text[53].description,1,4,5,3,0)).addAction(new Attack(s23text[54].title,s23text[54].description,280)).addPair(new ConditionHp(2,60), new ColorChange(2,7)));
                head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(s23text[53].title,s23text[53].description,1,4,5,3,0)).addAction(new Attack(s23text[55].title,s23text[55].description,260)).addPair(new ConditionHp(2,70), new RandomChange(6,3)));
                head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(s23text[53].title,s23text[53].description,1,4,5,3,0)).addAction(new Attack(s23text[56].title,s23text[56].description,240)).addPair(new ConditionHp(2,80), new DarkScreen(0)));
                head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(s23text[53].title,s23text[53].description,1,4,5,3,0)).addAction(new Attack(s23text[57].title,s23text[57].description,200)).addPair(new ConditionHp(2,90), new BindPets(3,2,2,1)));
                zero.attackMode.addAttack(new ConditionHp(1,50), head);

                head = new FromHeadAttack();
                head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(s23text[53].title,s23text[53].description,1,4,5,3,0)).addAction(new ComboShield(s23text[58].title,s23text[58].description,3,7)).addPair(new ConditionUsed(), new MultipleAttack(s23text[59].title,s23text[59].description,150,4)));
                head.addAttack(new EnemyAttack().addAction(new IntoVoid(s23text[51].title,s23text[51].description,0)).addPair(new ConditionHp(2,20), new Gravity(s23text[52].title,s23text[52].description,500)));
                head.addAttack(new EnemyAttack().addAction(new MultipleAttack(s23text[60].title,s23text[60].description,130,6)).addPair(new ConditionHp(2,30), new LockOrb(s23text[61].title,s23text[61].description,1,15,1,4,5,3,0,2)));
                head.addAttack(new EnemyAttack().addAction(new MultipleAttack(s23text[62].title,s23text[62].description,90,5)).addPair(new ConditionHp(2,40), new Transform(s23text[63].title,s23text[63].description,2,6)));
                head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(s23text[53].title,s23text[53].description,1,4,5,3,0)).addAction(new Attack(s23text[64].title,s23text[64].description,340)).addPair(new ConditionHp(2,50), new LineChange(8,7)));
                zero.attackMode.addAttack(new ConditionHp(2,50), head);
            } else if(choose == 2720) {
                data.add(Enemy.create(env, choose, 39000000, 8620, 0, 1,
                        new Pair<Integer, Integer>(prop[idx], -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(0);
                zero.attackMode = new EnemyAttackMode();
                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new DamageAbsorbShield(s23text[76].title,s23text[76].description,999, 2000000)).addAction(new ResistanceShieldAction(s23text[75].title,s23text[75].description,999)).addPair(new ConditionAlways(), new MultipleAttack(s23text[77].title,s23text[77].description,100,4)));
                zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addAction(new Daze(s23text[78].title,s23text[78].description,0)).addPair(new ConditionUsed(), new RecoverPlayer(s23text[79].title,s23text[79].description, 100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(s23text[80].title,s23text[80].description, 500, 2)));

                seq.addAttack(new EnemyAttack().addAction(new Daze(s23text[78].title,s23text[78].description,0)).addPair(new ConditionUsed(), new RecoverPlayer(s23text[79].title,s23text[79].description, 100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(s23text[81].title,s23text[81].description, 250, 5)));

                seq.addAttack(new EnemyAttack().addAction(new Daze(s23text[78].title,s23text[78].description,0)).addPair(new ConditionUsed(), new RecoverPlayer(s23text[79].title,s23text[79].description, 100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(s23text[82].title,s23text[82].description, 500, 3)));

                seq.addAttack(new EnemyAttack().addAction(new Daze(s23text[78].title,s23text[78].description,0)).addPair(new ConditionUsed(), new RecoverPlayer(s23text[79].title,s23text[79].description, 100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(s23text[83].title,s23text[83].description, 440, 4)));

                seq.addAttack(new EnemyAttack().addAction(new Daze(s23text[78].title,s23text[78].description,0)).addPair(new ConditionUsed(), new RecoverPlayer(s23text[79].title,s23text[79].description, 100)));
                seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(s23text[84].title,s23text[84].description, 350, 6)));
                zero.attackMode.addAttack(new ConditionAlways(), seq);

                seq = new SequenceAttack();
                seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Daze(s23text[85].title,s23text[85].description,0)));
                seq.addAttack(new EnemyAttack().addAction(new IntoVoid(s23text[86].title,s23text[86].description,0)).addPair(new ConditionAlways(), new MultipleAttack(s23text[87].title,s23text[87].description, 300,10)));
                zero.attackMode.addAttack(new ConditionHp(1,0), seq);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 23 ++stageCounter
        //done
        data = new ArrayList<Enemy>();
        {
            int[] ids = { 1747, 2078 };
            int choose = RandomUtil.chooseOne(ids);

            if (choose == ids[0]) {
                data.add(Enemy.create(env, choose, 37408889, 12508, 0, 1,
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
                data.add(Enemy.create(env, choose, 37408889, 12782, 0, 1,
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
        mStageEnemies.put(++stageCounter, data); // 24 ++stageCounter

        // done
        data = new ArrayList<Enemy>();
        {
            int[] prop = { 1, 4, 5, 3, 0 };
            for (int i = 0; i < 3; ++i) {
                int idx = RandomUtil.getInt(5);
                int choose = 1547 + idx;
                data.add(Enemy.create(env, choose, 24, 7707, 6000000, 1,
                        new Pair<Integer, Integer>(prop[idx], -1),
                        getMonsterTypes(env, choose)));
                zero = data.get(i);
                zero.attackMode = new EnemyAttackMode();
                zero.attackMode.createSimpleAttackAction("", "", 100);
            }
        }
        mStageEnemies.put(++stageCounter, data); // 22

    }
}
