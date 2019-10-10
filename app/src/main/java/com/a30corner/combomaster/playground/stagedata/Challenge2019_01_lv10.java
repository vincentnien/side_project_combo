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
import com.a30corner.combomaster.playground.action.impl.RandomLineChange;
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
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAtTurn;
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

public class Challenge2019_01_lv10 extends IStage {

    @Override
    public void create(IEnvironment env,
                       SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        mStageEnemies.clear();

        ArrayList<Enemy> data;
        Enemy zero;
        SequenceAttack seq;

        SparseArray<List<StageGenerator.SkillText>> list = loadSkillTextNew(env, mStage, new int[]{761,189,2294,3251,4744}); //
        StageGenerator.SkillText[] text;// = new StageGenerator.SkillText[list.size()];
		data = new ArrayList<Enemy>();
        {
            text = loadSubText(list, 761);
            data.add(Enemy.create(env, 761, 6579900, 31836, 720, 1,
                    new Pair<Integer, Integer>(0, 4),
                    getMonsterTypes(env, 761)));
            zero = data.get(0);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new ReduceTime(text[2].title, text[2].description, 10, -2000)).addPair(new ConditionAlways(), new RandomChange(text[1].title, text[1].description, 9, 3)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new ShieldAction(text[3].title, text[3].description, 4, -1, 50)).addPair(new ConditionUsed(), new ResistanceShieldAction(text[4].title, text[4].description, 4)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[5].title, text[5].description, 100)).addAction(new LineChange(3, 4)).addAction(new RandomLineChange(2, 4, 0, 1, 11)).addPair(new ConditionAlways(), new LockOrb(text[6].title, text[6].description, 0, 4)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[5].title, text[5].description, 100)).addAction(new RandomLineChange(2, 4, 0, 1, 14)).addAction(new LineChange(4, 0)).addPair(new ConditionAlways(), new LockOrb(text[8].title, text[8].description, 0, 0)));
            seq.addAttack(new EnemyAttack().addAction(new Attack(text[9].title, text[9].description, 100)).addAction(new LineChange(8, 0)).addAction(new LineChange(9, 4)).addPair(new ConditionAlways(), new LockOrb(text[10].title, text[10].description, 2, 12)));
            seq.addAttack(new EnemyAttack()
                    .addAction(new LineChange(3, 4)).addAction(new RandomLineChange(2, 4, 0, 1, 11)).addAction(new Attack(text[5].title, text[5].description, 100))
                    .addAction(new RandomLineChange(2, 4, 0, 1, 14)).addAction(new LineChange(4, 0)).addAction(new Attack(text[7].title, text[7].description, 100))
                    .addAction(new LineChange(8, 0)).addAction(new LineChange(9, 4)).addPair(new ConditionAlways(), new Attack(text[9].title, text[9].description, 100))
            );
            zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
		}
		mStageEnemies.put(1, data);
        {

            text = loadSubText(list, 189);
            data = new ArrayList<Enemy>();
            data.add(Enemy
                    .create(env, 189, 4104237, 36838, 744, 1,
                            new Pair<Integer, Integer>(0, -1),
                            getMonsterTypes(env, 189)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new ComboShield(text[0].title,
                                    text[0].description, 99, 6)).addPair(
                            new ConditionAlways(),
                            new Attack(text[1].title, text[1].description,
                                    100)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                    new LockAwoken(text[4].title, text[4].description, 1)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
                    new Attack(text[5].title, text[5].description, 600)));
            zero.attackMode.addAttack(new ConditionHp(2, 25), seq);

            RandomAttack random = new RandomAttack();
            random.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new ColorChange(text[2].title, text[2].description, -1,
                            7, -1, 7)));
            random.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 60),
                    new MultipleAttack(text[3].title, text[3].description,
                            40, 3)));
            random.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 25), random);
        }
        mStageEnemies.put(2, data);

        {

            text = loadSubText(list, 2294);
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 2294, 6789600, 36372, 1160, 1,
                    new Pair<Integer, Integer>(1, 5),
                    getMonsterTypes(env, 2294)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new AbsorbShieldAction(text[0].title,
                                    text[0].description, 5, 0))
                    .addAction(new AbsorbShieldAction(5, 3))
                    .addAction(
                            new ColorChange(text[1].title, text[1].description,
                                    2, 1))
                    .addPair(new ConditionAlways(), new Attack(90)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
            zero.attackMode.createEmptyMustAction();

            RandomAttack rndAttack = new RandomAttack();
            rndAttack.addAttack(new EnemyAttack()
                    .addAction(
                            new ChangeAttribute(text[2].title,
                                    text[2].description, 1, 5))
                    .addAction(
                            new ColorChange(text[3].title, text[3].description,
                                    4, 5))
                    .addPair(new ConditionAlways(), new Attack(110)));
            rndAttack.addAttack(new EnemyAttack()
                    .addAction(
                            new ChangeAttribute(text[2].title,
                                    text[2].description, 1, 5))
                    .addAction(
                            new ColorChange(text[4].title, text[4].description,
                                    2, 1))
                    .addPair(new ConditionAlways(), new Attack(90)));
            rndAttack.addAttack(new EnemyAttack()
                    .addAction(
                            new ChangeAttribute(text[2].title,
                                    text[2].description, 1, 5))
                    .addAction(
                            new LineChange(text[5].title, text[5].description,
                                    1, 5))
                    .addPair(new ConditionAlways(), new Attack(100)));
            zero.attackMode.addAttack(new ConditionHp(1, 50), rndAttack);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new ResistanceShieldAction(text[6].title,
                            text[6].description, 1))
                    .addPair(
                            new ConditionAlways(),
                            new RandomChange(text[7].title, text[7].description,
                                    7,15)));
            seq.addAttack(new EnemyAttack().addAction(
                    new LineChange(text[8].title, text[8].description, 0, 1, 1,
                            1, 2, 1, 5, 5, 6, 5)).addPair(
                    new ConditionAlways(), new Attack(220)));
            zero.attackMode.addAttack(new ConditionHp(2, 50), seq);
        }
        mStageEnemies.put(3, data);

        {

            text = loadSubText(list, 3251);
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 3251, 9570977, 24851, 1372, 1, getMonsterAttrs(env, 3251), getMonsterTypes(env, 3251)));
            zero = data.get(0);
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
        mStageEnemies.put(4, data);

        {

            text = loadSubText(list, 4744);
            data = new ArrayList<Enemy>();
            data.add(Enemy.create(env, 4744, 333600000, 28745, 564, 1,getMonsterAttrs(env,4744),getMonsterTypes(env, 4744)));
            zero = data.get(0);

            zero.attackMode = new EnemyAttackMode();
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 5, 50);
            zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);


            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[3].title,text[3].description, 999)).addAction(new DropLockAttack(text[5].title, text[5].description, 15,-1,10)).addPair(new ConditionAlways(),new LockOrb(text[7].title, text[7].description, 0, 1,4,5,3,0,2,6,7,8)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(new SkillDown(text[8].title, text[8].description, 0, 0, 3)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new MultipleAttack(text[9].title,text[9].description,20,4)));
            seq.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[10].title, text[10].description, 1,4,5,3,0)).addPair(new ConditionHp( 2, 10), new MultipleAttack(text[11].title,text[11].description,500,5)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            RandomAttack rndAtk = new RandomAttack();
            rndAtk.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[16].title,text[16].description,20,4)).addAction(new DarkScreen(text[18].title, text[18].description, 0)).addPair(new ConditionAlways(), new Attack(60)));
            rndAtk.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[16].title,text[16].description,20,4)).addAction(new BindPets(text[19].title, text[19].description, 2, 1, 1, 1)).addPair(new ConditionAlways(), new Attack(60)));

            rndAtk.addAttack(new EnemyAttack().addAction(new Gravity(text[17].title, text[17].description, 75)).addAction(new DarkScreen(text[18].title, text[18].description, 0)).addPair(new ConditionAlways(), new Attack(60)));
            rndAtk.addAttack(new EnemyAttack().addAction(new Gravity(text[17].title, text[17].description, 75)).addAction(new BindPets(text[19].title, text[19].description, 2, 1, 1, 1)).addPair(new ConditionAlways(), new Attack(60)));
            zero.attackMode.addAttack(new ConditionHp(2,50), rndAtk);

            rndAtk = new RandomAttack();
            rndAtk.addAttack(new EnemyAttack().addAction(new ColorChange(text[12].title,text[12].description,-1,2)).addAction(new Attack(90)).addAction(new DarkScreen(text[14].title, text[14].description, 0)).addPair(new ConditionAlways(), new Attack(60)));
            rndAtk.addAttack(new EnemyAttack().addAction(new ColorChange(text[12].title,text[12].description,-1,2)).addAction(new Attack(90)).addAction(new BindPets(text[15].title, text[15].description, 2, 1, 1, 1)).addPair(new ConditionAlways(), new Attack(60)));

            rndAtk.addAttack(new EnemyAttack().addAction(new RandomChange(text[13].title,text[13].description,6,3)).addAction(new Attack(65)).addAction(new DarkScreen(text[14].title, text[14].description, 0)).addPair(new ConditionAlways(), new Attack(60)));
            rndAtk.addAttack(new EnemyAttack().addAction(new RandomChange(text[13].title,text[13].description,6,3)).addAction(new Attack(65)).addAction(new BindPets(text[15].title, text[15].description, 2, 1, 1, 1)).addPair(new ConditionAlways(), new Attack(60)));
            zero.attackMode.addAttack(new ConditionHp(1,50), rndAtk);

        }
        mStageEnemies.put(5, data);

    }
}
