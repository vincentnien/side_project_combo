package com.a30corner.combomaster.playground;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.util.Pair;

import com.a30corner.combomaster.pad.DamageCalculator.AttackValue;
import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill;
import com.a30corner.combomaster.pad.monster.Skill;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.scene.PlaygroundGameScene;

import rx.functions.Action0;

public interface IEnvironment {

	public void toastEnemyAction(EnemyAction action, Enemy enemy);
	public void toastEnemyAction(EnemyAction action, Enemy enemy, Action0 todo);
	public void lockSkill(Enemy enemy, int turns, boolean skill, ICastCallback callback);
	public void bindPets(Enemy enemy, List<Pair<Integer, Integer>> bindList, ICastCallback callback);
	public void executeLeaderSwitch(ActiveSkill skill, ICastCallback callback);
	public Member getMember(int index);
	public Team getTeam();
	public PlaygroundGameScene getScene();
	public boolean isSkillLocked();
	public List<Enemy> getEnemies();
	public void recoverPlayerHp(int recovery);
	public void skillBoost(List<Integer> adjust, Member owner,ICastCallback callback);
	public void skillBoost(int adjust, Member owner, ICastCallback callback);
	public void bindRecover(int recover);
	public void awokenRecover(int recover);
	public <T extends Skill> void fireGlobalSkill(String key, T skill, ICastCallback callback);
	public void attackToDirect(AttackValue attack,  Enemy enemy, Member from,
             AtomicInteger counter,  ICastCallback callback);
	public int attackTo(MonsterInfo info, AttackValue attack, Enemy enemy, Member from,
            AtomicInteger counter, ICastCallback callback);
	public int attackVampire(MonsterInfo info, AttackValue attack, Enemy enemy, Member from,
            AtomicInteger counter, ICastCallback callback, int percent);
	public void showSkillDialog(int counter, MonsterInfo info, int cd1,int cd2, ISkillCallback callback);
	public boolean hasDebuff(String skill, int val);
	public boolean isNullAwokenStage();
	public void fireSkill(Member owner, int no);
	public void fireSkill(Member owner); 
	public boolean hasAlreadySwitched();
	public void attackByEnemyMultiple(Enemy enemy, int attackTimes, int damage, ICastCallback callback);
	void removePositiveSkills();
	public boolean hasBuff();
	public void attackByEnemy(Enemy enemy, int damage, ICastCallback callback);
	public void fireSkill(Member caster, ActiveSkill skill, ICastCallback callback);

	boolean hasSkill(String skill);

	void removeSkill(String sk);

	boolean isMultiMode();
	boolean is7x6();

	boolean isAwokenLocked();
	int getTargetAwokenCount(MonsterInfo info,MonsterSkill.AwokenSkill skill);
}
