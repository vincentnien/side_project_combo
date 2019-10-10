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
import com.a30corner.combomaster.playground.action.impl.DropLockAttack;
import com.a30corner.combomaster.playground.action.impl.DropRateAttack;
import com.a30corner.combomaster.playground.action.impl.Gravity;
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
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAtTurn;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFindOrb;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHp;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionModeSelection;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsedIf;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class Challenge2019_01_lv9 extends IStage {

    @Override
    public void create(IEnvironment env,
                       SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        mStageEnemies.clear();

        ArrayList<Enemy> data;
        Enemy zero;
        SequenceAttack seq;

        SparseArray<List<StageGenerator.SkillText>> list = loadSkillTextNew(env, mStage, new int[]{2718}); //
        StageGenerator.SkillText[] text;// = new StageGenerator.SkillText[list.size()];

        data = new ArrayList<Enemy>();
        {
            data.add(Enemy.create(env, 2718, 150000000, 13120, 500000, 1,
                    new Pair<Integer, Integer>(5,-1),
                    getMonsterTypes(env, 2718)));
            text = loadSubText(list, 2718);
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction( new DamageVoidShield(text[2].title,text[2].description,99, 20000000)).addAction(new ShieldAction(text[1].title,text[1].description,7,-1,75)).addPair(new ConditionAlways(), new ResistanceShieldAction(text[0].title,text[0].description,999)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new RandomChange(text[3].title,text[3].description, 7,3)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[4].title,text[4].description, 2,690)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(100)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new RandomChange(text[5].title,text[5].description, 7,3)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[6].title,text[6].description, 2,690)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(100)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new DarkScreen(text[7].title,text[7].description, 1)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            int[][] absorb = {{1,4},{4,5}, {4,3},{3,0}, {1,0}};
            int rnd = RandomUtil.getInt(5);
            seq.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[9+rnd].title,text[9+rnd].description,3,absorb[rnd][0])).addAction(new AbsorbShieldAction(3,absorb[rnd][1])).addAction(new Attack(text[8].title,text[8].description,450)).addPair(new ConditionHp(1,50), new LineChange(8,5,9,5)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2,50), new Gravity(text[14].title,text[14].description,500)));
            zero.attackMode.addAttack(new ConditionUsed(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[14].title,text[14].description,500)));
            zero.attackMode.addAttack(new ConditionHp(2,20), seq);

            RandomAttack rndAtk = new RandomAttack();
            rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[16].title,text[16].description,420)).addPair(new ConditionAlways(), new RecoverSelf(10)));
            rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[17].title,text[17].description,410)).addPair(new ConditionAlways(), new BindPets(3,5,5,1)));
            rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[18].title,text[18].description,340)).addPair(new ConditionAlways(), new LockOrb(1,6,1,4,5,3,0,2,6,7,8)));
            rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[19].title,text[19].description,360)).addPair(new ConditionAlways(), new DarkScreen(0)));
            zero.attackMode.addAttack(new ConditionHp(2,30), rndAtk);

            rndAtk = new RandomAttack();
            rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[18].title,text[18].description,340)).addPair(new ConditionAlways(), new LockOrb(1,6,1,4,5,3,0,2,6,7,8)));
            rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[19].title,text[19].description,360)).addPair(new ConditionAlways(), new DarkScreen(0)));
            zero.attackMode.addAttack(new ConditionHp(2,50), rndAtk);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[15].title,text[15].description,260)).addPair(new ConditionFindOrb(1), new SkillDown(0,3,5)));
            zero.attackMode.addAttack(new ConditionHp(1,50), seq);
        }

        mStageEnemies.put(1, data);

    }
}
