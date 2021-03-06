package com.a30corner.combomaster.playground.stagedata;

import android.util.Pair;
import android.util.SparseArray;

import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.StageGenerator;
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
import com.a30corner.combomaster.playground.action.impl.LockOrb;
import com.a30corner.combomaster.playground.action.impl.LockSkill;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.RecoverSelf;
import com.a30corner.combomaster.playground.action.impl.ReduceTime;
import com.a30corner.combomaster.playground.action.impl.ResistanceShieldAction;
import com.a30corner.combomaster.playground.action.impl.ShieldAction;
import com.a30corner.combomaster.playground.action.impl.SkillDown;
import com.a30corner.combomaster.playground.action.impl.Transform;
import com.a30corner.combomaster.playground.enemy.EnemyAttack;
import com.a30corner.combomaster.playground.enemy.EnemyAttackMode;
import com.a30corner.combomaster.playground.enemy.attack.RandomAttack;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAlways;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFindOrb;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHasBuff;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHp;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionPercentage;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionTurns;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.entity.Enemy;

import java.util.ArrayList;
import java.util.List;

public class DragonDestroyStage extends IStage {

    @Override
    public void create(IEnvironment env,
                       SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        ArrayList<Enemy> data;
        Enemy zero = null;
        SequenceAttack seq = null;
        mStageEnemies.clear();

        List<StageGenerator.SkillText> list = loadSkillText(env, mStage);
        StageGenerator.SkillText[] text = new StageGenerator.SkillText[list.size()];
        int len = text.length;
        for (int i = 0; i < len; ++i) {
            text[i] = getSkillText(list, i);
        }
        int stageCounter = 0;

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 253, 692, 10744, 147222, 1,
                    new Pair<Integer, Integer>(1,-1),
                    getMonsterTypes(env, 253)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

            data.add(Enemy.create(env, 682, 4000000, 100000, 10000, 3,
                    new Pair<Integer, Integer>(0,-1),
                    getMonsterTypes(env, 682)));
            zero = data.get(1);
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();

            data.add(Enemy.create(env, 256, 692, 10744, 147222, 1,
                    new Pair<Integer, Integer>(4,-1),
                    getMonsterTypes(env, 256)));
            zero = data.get(2);
            zero.attackMode = new EnemyAttackMode();
            zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 1225, 14280784, 33969, 2016, 1,
                    new Pair<Integer, Integer>(1,-1),
                    getMonsterTypes(env, 1225)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ResistanceShieldAction(text[1].title,text[1].description,5)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[2].title,text[2].description, 10)).addPair(new ConditionAlways(), new ColorChange(-1,1)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[3].title,text[3].description, 25)).addPair(new ConditionAlways(), new ColorChange(-1,1)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[4].title,text[4].description, 50)).addPair(new ConditionAlways(), new ColorChange(-1,1)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[5].title,text[5].description, 100)).addPair(new ConditionAlways(), new ColorChange(-1,1)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[6].title,text[6].description, 500)).addPair(new ConditionAlways(), new ColorChange(-1,1)));
            zero.attackMode.addAttack(new ConditionHp(1,0), seq);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 1227, 19041867, 98560, 2240, 5,
                    new Pair<Integer, Integer>(4,-1),
                    getMonsterTypes(env, 1227)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new DropRateAttack(text[7].title,text[7].description,5,4,15)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[8].title,text[8].description, 500)).addPair(new ConditionAlways(), new ColorChange(-1,4)));
            zero.attackMode.addAttack(new ConditionHp(1,0), seq);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 1472, 14320857, 29512, 0, 1,
                    new Pair<Integer, Integer>(5,3),
                    getMonsterTypes(env, 1472)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ResistanceShieldAction(text[9].title,text[9].description,4)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new ColorChangeNew(text[10].title,
                            text[10].description, 1, 7)).addPair(
                    new ConditionUsed(), new Attack(100)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                    new MultipleAttack(text[12].title,
                            text[12].description, 50, 4)));
            seq.addAttack(new EnemyAttack().addAction(
                    new BindPets(text[13].title, text[13].description, 2,
                            1, 2, 4)).addPair(new ConditionUsed(),
                    new Attack(80)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                    new Daze(text[14].title, text[14].description)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[15].title,
                            text[15].description, 500, 5)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);
        }
        mStageEnemies.put(++stageCounter, data);


        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 1631, 16843467, 34998, 0, 1,
                    new Pair<Integer, Integer>(3,0),
                    getMonsterTypes(env, 1631)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new ComboShield(text[16].title, text[16].description,
                            99, 5)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new DarkScreen(text[17].title, text[17].description,
                            0)).addPair(new ConditionTurns(7),
                    new Attack(1000)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            RandomAttack rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new ColorChange(text[18].title, text[18].description,
                            -1, 3)).addPair(new ConditionAlways(),
                    new Attack(80)));
            rndAttack
                    .addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new MultipleAttack(
                                    text[19].title, text[19].description,
                                    50, 3)));
            zero.attackMode.addAttack(new ConditionHp(1, 50), rndAttack);

            rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addAction(
                    new ColorChange(text[20].title, text[20].description,
                            -1, 0)).addPair(new ConditionAlways(),
                    new Attack(80)));
            rndAttack
                    .addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new MultipleAttack(
                                    text[19].title, text[19].description,
                                    50, 3)));
            zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);

            rndAttack = new RandomAttack();
            rndAttack
                    .addAttack(new EnemyAttack().addPair(
                            new ConditionAlways(), new MultipleAttack(
                                    text[21].title, text[21].description,
                                    50, 5)));
            zero.attackMode.addAttack(new ConditionHp(2, 30), rndAttack);

        }
        mStageEnemies.put(++stageCounter, data);


        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 2092, 8734110, 22159, 1044, 1,
                    new Pair<Integer, Integer>(1,3),
                    getMonsterTypes(env, 1472)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new ResistanceShieldAction(text[22].title,
                            text[22].description, 5))
                    .addPair(
                            new ConditionAlways(),
                            new Attack(text[23].title,
                                    text[23].description, 110)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new ComboShield(text[24].title, text[24].description,
                            5, 5)).addPair(
                    new ConditionUsed(),
                    new MultipleAttack(text[25].title,
                            text[25].description, 40, 3)));
            seq.addAttack(new EnemyAttack().addAction(
                    new DamageAbsorbShield(text[26].title,
                            text[26].description, 5, 500000)).addPair(
                    new ConditionUsed(),
                    new MultipleAttack(text[27].title,
                            text[27].description, 20, 6)));
            seq.addAttack(new EnemyAttack().addAction(
                    new BindPets(text[28].title, text[28].description, 3,
                            5, 5, 5)).addPair(new ConditionUsed(),
                    new Attack(150)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[29].title,
                            text[29].description, 1000, 5)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);
        }
        mStageEnemies.put(++stageCounter, data);

        //7
        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 2277, 9240684, 27361, 1044, 1,
                    new Pair<Integer, Integer>(4,0),
                    getMonsterTypes(env, 2277)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 30);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new ResistanceShieldAction(text[30].title,
                            text[30].description, 999)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[31].title, text[31].description, 80))
                    .addPair(new ConditionHp(2, 1), new RecoverSelf(50)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new Attack(text[35].title, text[35].description,
                                    100))
                    .addAction(new Transform(4, 2, 7))
                    .addPair(
                            new ConditionUsed(),
                            new LockOrb(text[36].title, text[36].description,
                                    0, 7)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[37].title, text[37].description,
                            100, 5)));
            zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

            RandomAttack rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionAlways(), new Gravity(text[32].title,
                            text[32].description, 99)));
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionPercentage(80), new Attack(text[33].title,
                            text[33].description, 120)));
            rndAttack.addAttack(new EnemyAttack().addPair(
                    new ConditionPercentage(40), new MultipleAttack(
                            text[34].title, text[34].description, 70, 2)));
            zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 1846, 32, 17894, 10000000, 1,
                    new Pair<Integer, Integer>(0,-1),
                    getMonsterTypes(env, 1846)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(new SkillDown(text[38].title,text[38].description, 0, 2, 2))
                    .addPair(new ConditionAlways(),new ComboShield(text[39].title,text[39].description, 1,5)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionHp(0,100),new MultipleAttack(text[40].title,text[40].description, 200,3)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(new ComboShield(text[41].title,text[41].description, 1,5))
                    .addPair(new ConditionUsed(),new ReduceTime(text[42].title,text[42].description, 1,-1000)));
            seq.addAttack(new EnemyAttack()
                    .addAction(new Attack(text[43].title,text[43].description, 600))
                    .addPair(new ConditionAlways(),new DarkScreen(0)));
            zero.attackMode.addAttack(new ConditionHp(1,50), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionUsed(),new Angry(text[44].title,text[44].description, 999,200)));
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionAlways(),new MultipleAttack(text[45].title,text[45].description, 35,3)));
            seq.addAttack(new EnemyAttack()
                    .addAction(new Attack(text[46].title,text[46].description, 60))
                    .addPair(new ConditionAlways(),new BindPets(3, 2, 2, 1)));
            zero.attackMode.addAttack(new ConditionHp(2,50), seq);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 1847, 16419498, 37028, 2568, 1,
                    new Pair<Integer, Integer>(0,4),
                    getMonsterTypes(env, 1847)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(new ResistanceShieldAction(text[47].title,text[47].description, 999))
                    .addPair(new ConditionAlways(),new ShieldAction(text[48].title,text[48].description, 1,-1,75)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionHasBuff(),new IntoVoid(text[49].title,text[49].description, 0)));
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionAlways(),new BindPets(text[50].title,text[50].description, 3,2,2,3)));
            zero.attackMode.addAttack(new ConditionUsed(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(new LockSkill(text[51].title,text[51].description, 5))
                    .addPair(new ConditionUsed(),new RecoverSelf(text[52].title,text[52].description, 100)));
            zero.attackMode.addAttack(new ConditionHp(2,50), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(new Attack(text[53].title,text[53].description,200))
                    .addPair(new ConditionAlways(),new ColorChange(6,8)));
            zero.attackMode.addAttack(new ConditionFindOrb(6), seq);

            RandomAttack ra = new RandomAttack();
            ra.addAttack(new EnemyAttack()
                    .addAction(new RandomChange(text[55].title,text[55].description,6,4))
                    .addPair(new ConditionAlways(),new RecoverSelf(text[54].title,text[54].description,10)));
            ra.addAttack(new EnemyAttack()
                    .addAction(new Attack(text[56].title,text[56].description,120))
                    .addPair(new ConditionAlways(), new DarkScreen(0)));
            ra.addAttack(new EnemyAttack()
                    .addPair(new ConditionAlways(), new MultipleAttack(text[57].title,text[57].description,50,3)));
            zero.attackMode.addAttack(new ConditionHp(1,30), ra);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionAlways(),new Gravity(text[58].title,text[58].description,100)));
            seq.addAttack(new EnemyAttack()
                    .addPair(new ConditionAlways(),new MultipleAttack(text[59].title,text[59].description,1000,3)));
            zero.attackMode.addAttack(new ConditionHp(2,30), seq);
        }
        mStageEnemies.put(++stageCounter, data);
    }
}
