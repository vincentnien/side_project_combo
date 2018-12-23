package com.a30corner.combomaster.playground.stagedata;

import android.util.SparseArray;

import com.a30corner.combomaster.pad.monster.MonsterVO;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.StageGenerator;
import com.a30corner.combomaster.playground.action.impl.Attack;
import com.a30corner.combomaster.playground.action.impl.BindPets;
import com.a30corner.combomaster.playground.action.impl.ColorChange;
import com.a30corner.combomaster.playground.action.impl.ComboShield;
import com.a30corner.combomaster.playground.action.impl.DamageAbsorbShield;
import com.a30corner.combomaster.playground.action.impl.DarkScreen;
import com.a30corner.combomaster.playground.action.impl.DropRateAttack;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.RecoverSelf;
import com.a30corner.combomaster.playground.action.impl.SkillDown;
import com.a30corner.combomaster.playground.enemy.EnemyAttack;
import com.a30corner.combomaster.playground.enemy.EnemyAttackMode;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAlways;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHp;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionPercentage;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsedIf;
import com.a30corner.combomaster.playground.entity.Enemy;

import java.util.ArrayList;
import java.util.List;

public class HanumanStage extends  IStage {

    @Override
    public void create(IEnvironment env, SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
        mStageEnemies.clear();

        ArrayList<Enemy> data;
        Enemy zero;
        SequenceAttack seq;

        List<StageGenerator.SkillText> list = loadSkillText(env, mStage);
        StageGenerator.SkillText[] text = new StageGenerator.SkillText[list.size()];
        int len = text.length;
        for (int i = 0; i < len; ++i) {
            text[i] = getSkillText(list, i);
        }

        {
            data = new ArrayList<Enemy>();
            MonsterVO vo = getMonster(env, 2838);
            data.add(Enemy.create(env, 2838, 10137000, 12960, 1140, 1,
                    vo.getMonsterProps(), getMonsterTypes(vo)));
            zero = data.get(0);
            zero.attackMode = new EnemyAttackMode();
            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new SkillDown(text[0].title, text[0].description,
                                    0, 10, 10)).addPair(
                            new ConditionAlways(),
                            new DamageAbsorbShield(text[1].title,
                                    text[1].description, 99, 1000000)));
            zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[6].title, text[6].description, 150))
                    .addPair(
                            new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP
                                    .ordinal(), 2, 50),
                            new BindPets(3, 3, 3, 1)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 10),
                    new MultipleAttack(text[2].title, text[2].description, 100,
                            3)));
            zero.attackMode.addAttack(new ConditionAlways(), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack()
                    .addAction(
                            new Attack(text[7].title, text[7].description, 160))
                    .addPair(new ConditionPercentage(15), new ColorChange(2, 7)));
            zero.attackMode.addAttack(new ConditionHp(2, 30), seq);

            seq = new SequenceAttack();
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[3].title, text[3].description, 100))
                    .addPair(new ConditionUsed(), new DropRateAttack(5, 2, 10)));
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[4].title, text[4].description, 130))
                    .addPair(new ConditionUsed(), new RandomChange(2, 3)));
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[5].title, text[5].description, 110))
                    .addPair(new ConditionUsed(), new DarkScreen(0)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new Gravity(text[8].title, text[8].description, 99)));
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[9].title, text[9].description, 100))
                    .addPair(new ConditionAlways(), new RecoverSelf(5, 8)));
            seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
                    new MultipleAttack(text[10].title, text[10].description,
                            44, 3)));
            seq.addAttack(new EnemyAttack().addAction(
                    new Attack(text[11].title, text[11].description, 140))
                    .addPair(new ConditionAlways(), new ComboShield(1, 6)));
            zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
        }
        mStageEnemies.put(1, data);

    }
}
