package com.a30corner.combomaster.playground.stagedata;

import android.util.Pair;
import android.util.SparseArray;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.StageGenerator;
import com.a30corner.combomaster.playground.action.impl.AbsorbShieldAction;
import com.a30corner.combomaster.playground.action.impl.Angry;
import com.a30corner.combomaster.playground.action.impl.Attack;
import com.a30corner.combomaster.playground.action.impl.BindPets;
import com.a30corner.combomaster.playground.action.impl.ChangeAttribute;
import com.a30corner.combomaster.playground.action.impl.ColorChange;
import com.a30corner.combomaster.playground.action.impl.ComboShield;
import com.a30corner.combomaster.playground.action.impl.DamageVoidShield;
import com.a30corner.combomaster.playground.action.impl.DarkScreen;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.IntoVoid;
import com.a30corner.combomaster.playground.action.impl.LineChange;
import com.a30corner.combomaster.playground.action.impl.LockAwoken;
import com.a30corner.combomaster.playground.action.impl.LockOrb;
import com.a30corner.combomaster.playground.action.impl.LockSkill;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.RecoverSelf;
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
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFindOrb;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHp;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsedIf;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class SuperFight extends IStage {

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
            data.add(Enemy.create(env, 2717, 27300000, 9967, 1744, 1,
                    new Pair<Integer, Integer>(4,-1),
                    getMonsterTypes(env, 2717)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            int[][] absorb = {{1,3},{1,0}, {4,3},{4,0}, {5,3},{5,0}};
            int rnd = RandomUtil.getInt(6);
            seq.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[3+rnd].title,text[3+rnd].description,1,absorb[rnd][0])).addAction(new AbsorbShieldAction(1,absorb[rnd][1])).addAction( new DamageVoidShield(text[2].title,text[2].description,99, 5000000)).addPair(new ConditionAlways(), new ResistanceShieldAction(text[1].title,text[1].description,999)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[11].title,text[11].description, 1000,3)).addAction(new IntoVoid(text[10].title,text[10].description, 0)).addPair(new ConditionHp(2, 10), new ShieldAction(text[9].title,text[9].description, 1,-1,75)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[13].title,text[13].description, 450)).addAction(new Transform(1,4,5,3,0)).addAction(new LockSkill(text[12].title,text[12].description, 10)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 90), new ShieldAction(text[9].title,text[9].description, 1,-1,75)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[15].title,text[15].description, 600)).addAction(new Transform(1,4,5,3,0)).addAction(new DarkScreen(text[14].title,text[14].description, 0)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 70), new ShieldAction(text[9].title,text[9].description, 1,-1,75)));
            seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[17].title,text[17].description, 400,3)).addAction(new SkillDown(text[16].title,text[16].description, 0,5,5)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new ShieldAction(text[9].title,text[9].description, 1,-1,75)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[19].title,text[19].description, 2000)).addAction(new Transform(1,4,5,3,0)).addAction(new LockAwoken(text[18].title,text[18].description, 2)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 30), new ShieldAction(text[9].title,text[9].description, 1,-1,75)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[26].title,text[26].description,150)).addAction(new ColorChange(-1,7)).addAction(new Attack(text[25].title,text[25].description,50)).addAction(new BindPets(2,1,1,1)).addPair(new ConditionAlways(), new Gravity(text[27].title,text[27].description,99)));
            zero.attackMode.addAttack(new ConditionHp(2,50), seq);

            RandomAttack ra = new RandomAttack();
            int[][] colorIndex = {{20,23},{20,24},{21,23},{21,24},{22,23},{22,24}};
            for(int i=0; i<6; ++i) {
                for(int j=0; j<2; ++j) {
                    ra.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[3+i].title,text[3+i].description,1,absorb[i][0])).addAction(new AbsorbShieldAction(1,absorb[i][1])).addAction(new Attack(text[colorIndex[i][j]].title,text[colorIndex[i][j]].description,100)).addAction(new ColorChange(-1, absorb[i][j])).addAction(new Attack(text[25].title,text[25].description,50)).addPair(new ConditionAlways(), new BindPets(2,1,1,1)));
                }
            }
            zero.attackMode.addAttack(new ConditionHp(1,50), ra);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 2718, 33500000, 8528, 50000, 1,
                    new Pair<Integer, Integer>(5,-1),
                    getMonsterTypes(env, 2718)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction( new DamageVoidShield(text[30].title,text[30].description,99, 6000000)).addAction(new ShieldAction(text[29].title,text[29].description,7,-1,75)).addPair(new ConditionAlways(), new ResistanceShieldAction(text[28].title,text[28].description,999)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new RandomChange(text[31].title,text[31].description, 7,3)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[32].title,text[32].description, 1,690)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(100)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new RandomChange(text[33].title,text[33].description, 7,3)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[34].title,text[34].description, 1,690)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(100)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new DarkScreen(text[35].title,text[35].description, 1)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            int[][] absorb = {{1,4},{4,5}, {4,3},{3,0}, {1,0}};
            int rnd = RandomUtil.getInt(5);
            seq.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[37+rnd].title,text[37+rnd].description,3,absorb[rnd][0])).addAction(new AbsorbShieldAction(3,absorb[rnd][1])).addAction(new Attack(text[36].title,text[36].description,450)).addPair(new ConditionHp(1,50), new LineChange(8,5,9,5)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2,50), new Gravity(text[42].title,text[42].description,500)));
            zero.attackMode.addAttack(new ConditionUsed(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[42].title,text[42].description,500)));
            zero.attackMode.addAttack(new ConditionHp(2,20), seq);

            RandomAttack rndAtk = new RandomAttack();
            rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[44].title,text[44].description,420)).addPair(new ConditionAlways(), new RecoverSelf(10)));
            rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[45].title,text[45].description,410)).addPair(new ConditionAlways(), new BindPets(3,5,5,1)));
            rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[46].title,text[46].description,340)).addPair(new ConditionAlways(), new LockOrb(1,6,1,4,5,3,0,2,6,7,8)));
            rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[47].title,text[47].description,360)).addPair(new ConditionAlways(), new DarkScreen(0)));
            zero.attackMode.addAttack(new ConditionHp(2,30), rndAtk);

            rndAtk = new RandomAttack();
            rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[46].title,text[46].description,340)).addPair(new ConditionAlways(), new LockOrb(1,6,1,4,5,3,0,2,6,7,8)));
            rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[47].title,text[47].description,360)).addPair(new ConditionAlways(), new DarkScreen(0)));
            zero.attackMode.addAttack(new ConditionHp(2,50), rndAtk);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[43].title,text[43].description,260)).addPair(new ConditionFindOrb(1), new SkillDown(0,3,5)));
            zero.attackMode.addAttack(new ConditionHp(1,50), seq);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 2719, 67000000, 6995, 1526, 1,
                    new Pair<Integer, Integer>(3,-1),
                    getMonsterTypes(env, 2719)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new DamageVoidShield(text[48].title,text[48].description,99, 20000000)).addAction(new ResistanceShieldAction(text[49].title,text[49].description,999)).addPair(new ConditionAlways(), new Gravity(text[50].title,text[50].description,35)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new IntoVoid(text[51].title,text[51].description,0)).addPair(new ConditionAlways(), new Gravity(text[52].title,text[52].description,500)));
            zero.attackMode.addAttack(new ConditionHp(1,90), seq);

            FromHeadAttack head = new FromHeadAttack();
            head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[53].title,text[53].description,1,4,5,3,0)).addAction(new Attack(text[54].title,text[54].description,280)).addPair(new ConditionHp(2,60), new ColorChange(2,7)));
            head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[53].title,text[53].description,1,4,5,3,0)).addAction(new Attack(text[55].title,text[55].description,260)).addPair(new ConditionHp(2,70), new RandomChange(6,3)));
            head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[53].title,text[53].description,1,4,5,3,0)).addAction(new Attack(text[56].title,text[56].description,240)).addPair(new ConditionHp(2,80), new DarkScreen(0)));
            head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[53].title,text[53].description,1,4,5,3,0)).addAction(new Attack(text[57].title,text[57].description,200)).addPair(new ConditionHp(2,90), new BindPets(3,2,2,1)));
            zero.attackMode.addAttack(new ConditionHp(1,50), head);

            head = new FromHeadAttack();
            head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[53].title,text[53].description,1,4,5,3,0)).addAction(new ComboShield(text[58].title,text[58].description,3,7)).addPair(new ConditionUsed(), new MultipleAttack(text[59].title,text[59].description,150,4)));
            head.addAttack(new EnemyAttack().addAction(new IntoVoid(text[51].title,text[51].description,0)).addPair(new ConditionHp(2,20), new Gravity(text[52].title,text[52].description,500)));
            head.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[60].title,text[60].description,130,6)).addPair(new ConditionHp(2,30), new LockOrb(text[61].title,text[61].description,1,15,1,4,5,3,0,2)));
            head.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[62].title,text[62].description,90,5)).addPair(new ConditionHp(2,40), new Transform(text[63].title,text[63].description,2,6)));
            head.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[53].title,text[53].description,1,4,5,3,0)).addAction(new Attack(text[64].title,text[64].description,340)).addPair(new ConditionHp(2,50), new LineChange(8,7)));
            zero.attackMode.addAttack(new ConditionHp(2,50), head);

        }
        mStageEnemies.put(++stageCounter, data);

    }
}
