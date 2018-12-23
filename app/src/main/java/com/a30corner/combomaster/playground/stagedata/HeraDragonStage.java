package com.a30corner.combomaster.playground.stagedata;

import android.util.Pair;
import android.util.SparseArray;

import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.StageGenerator;
import com.a30corner.combomaster.playground.action.impl.AbsorbShieldAction;
import com.a30corner.combomaster.playground.action.impl.Attack;
import com.a30corner.combomaster.playground.action.impl.BindPets;
import com.a30corner.combomaster.playground.action.impl.ColorChange;
import com.a30corner.combomaster.playground.action.impl.ComboShield;
import com.a30corner.combomaster.playground.action.impl.DamageAbsorbShield;
import com.a30corner.combomaster.playground.action.impl.DamageVoidShield;
import com.a30corner.combomaster.playground.action.impl.Daze;
import com.a30corner.combomaster.playground.action.impl.DropRateAttack;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.IntoVoid;
import com.a30corner.combomaster.playground.action.impl.LineChange;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.RecoverPlayer;
import com.a30corner.combomaster.playground.action.impl.RecoverSelf;
import com.a30corner.combomaster.playground.action.impl.ResistanceShieldAction;
import com.a30corner.combomaster.playground.action.impl.SkillDown;
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
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsedIf;
import com.a30corner.combomaster.playground.entity.Enemy;

import java.util.ArrayList;
import java.util.List;

public class HeraDragonStage extends IStage {

    @Override
    public void create(IEnvironment env,
                        SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        ArrayList<Enemy> data;
        Enemy zero = null;
        SequenceAttack seq = null;
        mStageEnemies.clear();

        SparseArray<List<StageGenerator.SkillText>> list = loadSkillTextNew(env, mStage, new int[]{825,393,2720});
        int stageCounter = 0;

        data = new ArrayList<Enemy>();
        {
            StageGenerator.SkillText[] text = loadSubText(list, 825);
            data.add(Enemy.create(env, 825, 10003778, 42000, 1200, 1,
                    new Pair<Integer, Integer>(3,-1),
                    getMonsterTypes(env, 825)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1,1, 0, EnemyCondition.Type.FIND_ATTR.ordinal(), 0), new BindPets(text[1].title,text[1].description,5,10,10,1,0)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1,1, 1, EnemyCondition.Type.FIND_ATTR.ordinal(), 0), new SkillDown(text[2].title,text[2].description,0,3,3)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2,50), new AbsorbShieldAction(text[3].title,text[3].description,5,0)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2,70), new DamageAbsorbShield(text[4].title,text[4].description,999, 1000000)));
            seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[5].title,text[5].description,7,3)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB.ordinal(), 7), new Attack(220)));
            seq.addAttack(new EnemyAttack().addAction(new ColorChange(text[6].title,text[6].description,0,7)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.FIND_ORB.ordinal(), 0), new Attack(110)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new RandomChange(text[7].title,text[7].description,3,4)).addPair(new ConditionAlways(), new Attack(90)));
            zero.attackMode.addAttack(new ConditionHp(1,0), seq);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            StageGenerator.SkillText[] text = loadSubText(list, 393);
            data.add(Enemy.create(env, 393, 9394350, 10710, 0, 1,
                    new Pair<Integer, Integer>(5,0),
                    getMonsterTypes(env, 393)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.KONJO, 30);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[1].title, text[1].description, 999,6)).addPair(new ConditionAlways(), new ResistanceShieldAction(text[2].title,text[2].description,999)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new RecoverSelf(text[3].title,text[3].description,50)).addPair(new ConditionHp(2,1), new Gravity(text[4].title,text[4].description,500)));
            seq.addAttack(new EnemyAttack().addAction(new DamageVoidShield(text[10].title,text[10].description,99,500000)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 30), new AbsorbShieldAction(text[11].title,text[11].description,1,5)));
            seq.addAttack(new EnemyAttack().addAction(new DropRateAttack(text[7].title,text[7].description,5,3,20)).addAction(new Transform(1,4,5,3,0,2)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new Attack(text[8].title,text[8].description,340)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Transform(1,4,5,3,0,2)).addPair(new ConditionAlways(), new Attack(text[5].title,text[5].description,240)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new AbsorbShieldAction(text[6].title,text[6].description,1,3)));
            zero.attackMode.addAttack(new ConditionHp(1,50), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Transform(1,4,5,3,0,2)).addPair(new ConditionAlways(), new Attack(text[8].title,text[8].description,340)));
            zero.attackMode.addAttack(new ConditionHp(2,50), seq);

            RandomAttack rnd = new RandomAttack();
            rnd.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[12].title,text[12].description,1,1)).addAction(new LineChange(2,2)).addPair(new ConditionAlways(), new Attack(text[16].title,text[16].description,1000)));
            rnd.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[13].title,text[13].description,1,4)).addAction(new LineChange(2,2)).addPair(new ConditionAlways(), new Attack(text[16].title,text[16].description,1000)));
            rnd.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[14].title,text[14].description,1,5)).addAction(new LineChange(2,2)).addPair(new ConditionAlways(), new Attack(text[16].title,text[16].description,1000)));
            rnd.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[15].title,text[15].description,1,0)).addAction(new LineChange(2,2)).addPair(new ConditionAlways(), new Attack(text[16].title,text[16].description,1000)));
            zero.attackMode.addAttack(new ConditionHp(2,30), rnd);
        }
        mStageEnemies.put(++stageCounter, data);

        data = new ArrayList<Enemy>();
        {
            StageGenerator.SkillText[] text = loadSubText(list, 2720);
            data.add(Enemy.create(env, 2720, 50000000, 10985, 0, 1,
                    new Pair<Integer, Integer>(0,-1),
                    getMonsterTypes(env, 2720)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new DamageAbsorbShield(text[2].title,text[2].description,999, 2000000)).addAction(new ResistanceShieldAction(text[1].title,text[1].description,999)).addPair(new ConditionAlways(), new MultipleAttack(text[3].title,text[3].description,100,4)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Daze(text[4].title,text[4].description,0)).addPair(new ConditionUsed(), new RecoverPlayer(text[5].title,text[5].description, 100)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[6].title,text[6].description, 500, 2)));

            seq.addAttack(new EnemyAttack().addAction(new Daze(text[4].title,text[4].description,0)).addPair(new ConditionUsed(), new RecoverPlayer(text[5].title,text[5].description, 100)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[7].title,text[7].description, 250, 5)));

            seq.addAttack(new EnemyAttack().addAction(new Daze(text[4].title,text[4].description,0)).addPair(new ConditionUsed(), new RecoverPlayer(text[5].title,text[5].description, 100)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[8].title,text[8].description, 500, 3)));

            seq.addAttack(new EnemyAttack().addAction(new Daze(text[4].title,text[4].description,0)).addPair(new ConditionUsed(), new RecoverPlayer(text[5].title,text[5].description, 100)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[9].title,text[9].description, 440, 4)));

            seq.addAttack(new EnemyAttack().addAction(new Daze(text[4].title,text[4].description,0)).addPair(new ConditionUsed(), new RecoverPlayer(text[5].title,text[5].description, 100)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[10].title,text[10].description, 350, 6)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Daze(text[11].title,text[11].description,0)));
            seq.addAttack(new EnemyAttack().addAction(new IntoVoid(text[12].title,text[12].description,0)).addPair(new ConditionAlways(), new MultipleAttack(text[13].title,text[13].description, 300,10)));
            zero.attackMode.addAttack(new ConditionHp(1,0), seq);
        }
        mStageEnemies.put(++stageCounter, data);
    }
}
