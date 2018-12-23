package com.a30corner.combomaster.playground;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;

import com.a30corner.combomaster.pad.monster.MonsterSkill;
import com.a30corner.combomaster.pad.monster.MonsterVO;
import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
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
import com.a30corner.combomaster.playground.action.impl.DropLockAttack;
import com.a30corner.combomaster.playground.action.impl.DropRateAttack;
import com.a30corner.combomaster.playground.action.impl.Gravity;
import com.a30corner.combomaster.playground.action.impl.IntoVoid;
import com.a30corner.combomaster.playground.action.impl.LineChange;
import com.a30corner.combomaster.playground.action.impl.LockAwoken;
import com.a30corner.combomaster.playground.action.impl.LockOrb;
import com.a30corner.combomaster.playground.action.impl.LockSkill;
import com.a30corner.combomaster.playground.action.impl.MultipleAttack;
import com.a30corner.combomaster.playground.action.impl.RandomChange;
import com.a30corner.combomaster.playground.action.impl.RandomLineChange;
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
import com.a30corner.combomaster.playground.enemy.attack.AttackAction;
import com.a30corner.combomaster.playground.enemy.attack.RandomAttack;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAliveOnly;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAlways;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFindAttr;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFindOrb;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHasBuff;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionHp;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionIf;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionNTurns;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionPercentage;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionSomeoneDied;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionTurns;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsedIf;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.playground.stagedata.Challenge28_Lv10;
import com.a30corner.combomaster.playground.stagedata.Challenge32Lv9;
import com.a30corner.combomaster.playground.stagedata.Challenge34Lv10;
import com.a30corner.combomaster.playground.stagedata.DevilNormalStage;
import com.a30corner.combomaster.playground.stagedata.DevilSuperStage;
import com.a30corner.combomaster.playground.stagedata.DragonDestroyStage;
import com.a30corner.combomaster.playground.stagedata.DragonHellStage;
import com.a30corner.combomaster.playground.stagedata.HanumanStage;
import com.a30corner.combomaster.playground.stagedata.Hera297Stage;
import com.a30corner.combomaster.playground.stagedata.HeraDragonStage;
import com.a30corner.combomaster.playground.stagedata.MachineAthenaStage;
import com.a30corner.combomaster.playground.stagedata.MachineHeraStage;
import com.a30corner.combomaster.playground.stagedata.MachineZeusStage;
import com.a30corner.combomaster.playground.stagedata.SnowRiverStage;
import com.a30corner.combomaster.playground.stagedata.SuperFight;
import com.a30corner.combomaster.playground.stagedata.UltimateArena1;
import com.a30corner.combomaster.playground.stagedata.UltimateArena2;
import com.a30corner.combomaster.playground.stagedata.UltimateArena3;
import com.a30corner.combomaster.playground.stagedata.UraArena;
import com.a30corner.combomaster.playground.stagedata.Zeus297Stage;
import com.a30corner.combomaster.playground.stagedata.ZeusDragonStage;
import com.a30corner.combomaster.provider.LocalDBHelper;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.RandomUtil;
import com.a30corner.combomaster.utils.ZipUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class StageGenerator {

	public static void initWith(IEnvironment env,
			SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
		if ("hanuman".equals(mStage)) {
			new HanumanStage().create(env, mStageEnemies, mStage);
		} else if ("tuesday".equals(mStage)) {
			initTuesdayStage(env, mStageEnemies, mStage);
		} else if ("thursday".equals(mStage)) {
			initThursdayStage(env, mStageEnemies, mStage);
		} else if ("friday".equals(mStage)) {
			initFridayStage(env, mStageEnemies, mStage);
		} else if("sopdet".equals(mStage)) {
			initSopdet(env, mStageEnemies, mStage);
		} else if ("ultimate".equals(mStage)) {
			new UltimateArena1().create(env, mStageEnemies, mStage);
		} else if ("ultimate2".equals(mStage)) {
			new UltimateArena2().create(env, mStageEnemies, mStage);
		} else if ("ultimate3".equals(mStage)) {
			new UltimateArena3().create(env, mStageEnemies, mStage);
		} else if ("28_10".equals(mStage)) {
			new Challenge28_Lv10().create(env, mStageEnemies, mStage);
		} else if ("snow_river".equals(mStage)) {
			new SnowRiverStage().create(env, mStageEnemies, mStage);
		} else if ("zeus297".equals(mStage)) {
			new Zeus297Stage().create(env, mStageEnemies, mStage);
		} else if ("hera297".equals(mStage)) {
			new Hera297Stage().create(env, mStageEnemies, mStage);
		} else if ("machinezeus".equals(mStage)) {
			try {
				new MachineZeusStage().create(env, mStageEnemies, mStage);
			} catch (Exception e) {
				Log.e("Vincent", e.toString(), e);
			}
		} else if ("devilsuper".equals(mStage)) {
			new DevilSuperStage().create(env, mStageEnemies, mStage);
		} else if ("devilnormal".equals(mStage)) {
			new DevilNormalStage().create(env, mStageEnemies, mStage);
		} else if("machinehera".equals(mStage)) {
			new MachineHeraStage().create(env, mStageEnemies, mStage);
		} else if("monday".equals(mStage)) {
			initMonday(env, mStageEnemies, mStage);
		} else if("dragondestroy".equals(mStage)) {
			new DragonDestroyStage().create(env, mStageEnemies, mStage);
		} else if("dragonhell".equals(mStage)) {
			new DragonHellStage().create(env, mStageEnemies, mStage);
		} else if("challenge32_9".equals(mStage)) {
			new Challenge32Lv9().create(env, mStageEnemies, mStage);
		} else if("superfight".equals(mStage)) {
			new SuperFight().create(env, mStageEnemies, mStage);
		} else if("challenge34_10".equals(mStage)) {
			new Challenge34Lv10().create(env, mStageEnemies, mStage);
		} else if("machineathena".equals(mStage)) {
			new MachineAthenaStage().create(env, mStageEnemies, mStage);
		} else if ("zeusdragon".equals(mStage)) {
			new ZeusDragonStage().create(env, mStageEnemies, mStage);
		} else if("heradragon".equals(mStage)) {
			new HeraDragonStage().create(env, mStageEnemies, mStage);
		} else if("201806_10".equals(mStage)) {
			//StageGenerator2.init201806_Lv10(env, mStageEnemies, mStage);
		} else if ("ura".equals(mStage)) {
			new UraArena().create(env, mStageEnemies, mStage);
		}
	}

	
	private static void initMonday(IEnvironment env,
			SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
		ArrayList<Enemy> data;
		Enemy zero = null;
		SequenceAttack seq = null;
		mStageEnemies.clear();

		List<SkillText> list = loadSkillText(env, mStage);
		SkillText[] text = new SkillText[list.size()];
		int len = text.length;
		for (int i = 0; i < len; ++i) {
			text[i] = getSkillText(list, i);
		}
		int stageCounter = 0;
		final int[] ATTRIBUTE = {1,4,5,3,0};
		
		data = new ArrayList<Enemy>();
		{
			data.add(Enemy.create(env, 310, 337433, 10600, 240, 3,
					new Pair<Integer, Integer>(1,4),
					getMonsterTypes(env, 310)));
			zero = data.get(0);
			zero.attackMode = new EnemyAttackMode();
			zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();
			
			seq = new SequenceAttack();
			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[2].title,text[2].description,150)));
			zero.attackMode.addAttack(new ConditionHp(2,50), seq);
			
			seq = new SequenceAttack();
			seq.addAttack(new EnemyAttack().addPair(new ConditionPercentage(50), new Angry(text[1].title,text[1].description,5, 200)));
			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
			zero.attackMode.addAttack(new ConditionHp(1,50), seq);
			
			data.add(Enemy.create(env, 314, 325656, 10082, 1200, 3,
					new Pair<Integer, Integer>(5,1),
					getMonsterTypes(env, 314)));
			zero = data.get(1);
			zero.attackMode = new EnemyAttackMode();
			zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();
			
			seq = new SequenceAttack();
			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[4].title,text[4].description,150)));
			zero.attackMode.addAttack(new ConditionHp(2,50), seq);
			
			seq = new SequenceAttack();
			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new BindPets(text[3].title,text[3].description,5,2,4,1,4)));
			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
			zero.attackMode.addAttack(new ConditionHp(1,50), seq);
		}
		mStageEnemies.put(++stageCounter, data);
		
		data = new ArrayList<Enemy>();
		{
			data.add(Enemy.create(env, 311, 337433, 10600, 240, 3,
					new Pair<Integer, Integer>(1,5),
					getMonsterTypes(env, 311)));
			zero = data.get(0);
			zero.attackMode = new EnemyAttackMode();
			zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();
			
			seq = new SequenceAttack();
			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[5].title,text[5].description,150)));
			zero.attackMode.addAttack(new ConditionHp(2,50), seq);
			
			seq = new SequenceAttack();
			seq.addAttack(new EnemyAttack().addPair(new ConditionPercentage(50), new Angry(text[1].title,text[1].description,5, 200)));
			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
			zero.attackMode.addAttack(new ConditionHp(1,50), seq);
			
			data.add(Enemy.create(env, 313, 355100, 12908, 0, 4,
					new Pair<Integer, Integer>(4,5),
					getMonsterTypes(env, 313)));
			zero = data.get(1);
			zero.attackMode = new EnemyAttackMode();
			zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();
			
			seq = new SequenceAttack();
			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[5].title,text[5].description,150)));
			zero.attackMode.addAttack(new ConditionHp(2,50), seq);
			
			seq = new SequenceAttack();
			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new BindPets(text[6].title,text[6].description,3,10,10,1)));
			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
			zero.attackMode.addAttack(new ConditionHp(1,50), seq);
		}
		mStageEnemies.put(++stageCounter, data);
		
		data = new ArrayList<Enemy>();
		{
			data.add(Enemy.create(env, 312, 355100, 12908, 0, 4,
					new Pair<Integer, Integer>(4,1),
					getMonsterTypes(env, 312)));
			zero = data.get(0);
			zero.attackMode = new EnemyAttackMode();
			zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();
			
			seq = new SequenceAttack();
			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[4].title,text[4].description,150)));
			zero.attackMode.addAttack(new ConditionHp(2,50), seq);
			
			seq = new SequenceAttack();
			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new BindPets(text[6].title,text[6].description,3,10,10,1)));
			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
			zero.attackMode.addAttack(new ConditionHp(1,50), seq);
			
			data.add(Enemy.create(env, 315, 325656, 10082, 1200, 3,
					new Pair<Integer, Integer>(5,1),
					getMonsterTypes(env, 315)));
			zero = data.get(1);
			zero.attackMode = new EnemyAttackMode();
			zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();
			
			seq = new SequenceAttack();
			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[2].title,text[2].description,150)));
			zero.attackMode.addAttack(new ConditionHp(2,50), seq);
			
			seq = new SequenceAttack();
			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new BindPets(text[3].title,text[3].description,5,2,4,1,4)));
			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(100)));
			zero.attackMode.addAttack(new ConditionHp(1,50), seq);
		}
		mStageEnemies.put(++stageCounter, data);
		
		data = new ArrayList<Enemy>();
		{
			int random = 1272 + RandomUtil.getInt(3);
			if(random==1272) {
				data.add(Enemy.create(env, random, 823839, 9529, 990, 1,
						new Pair<Integer, Integer>(1,3),
						getMonsterTypes(env, random)));
				zero = data.get(0);
				zero.attackMode = new EnemyAttackMode();
				seq = new SequenceAttack();
				seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new LockSkill(text[7].title, text[7].description, 3)));
				zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
				
				seq = new SequenceAttack();
				seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Daze(text[8].title, text[8].description, 0)));
				seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(text[9].title, text[9].description, 100)));
				seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(text[10].title, text[10].description, 110)));
				seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Daze(text[11].title, text[11].description, 0)));
				seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(text[12].title, text[12].description, 600)));
				zero.attackMode.addAttack(new ConditionAlways(), seq);
			} else if(random==1273) {
				data.add(Enemy.create(env, random, 1011812, 8412, 990, 1,
						new Pair<Integer, Integer>(4,3),
						getMonsterTypes(env, random)));
				zero = data.get(0);
				zero.attackMode = new EnemyAttackMode();
				seq = new SequenceAttack();
				seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new DropRateAttack(text[13].title, text[13].description, 10, 4, 15)));
				zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
				
				seq = new SequenceAttack();
				seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Daze(text[14].title, text[14].description, 0)));
				seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(text[15].title, text[15].description, 100)));
				seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new BindPets(text[16].title, text[16].description, 2, 2, 3, 1)));
				seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Daze(text[17].title, text[17].description, 0)));
				seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[18].title, text[18].description, 100, 5)));
				zero.attackMode.addAttack(new ConditionAlways(), seq);
			} else if(random==1274) {
				data.add(Enemy.create(env, random, 1192339,  4392, 30000, 1,
						new Pair<Integer, Integer>(5,3),
						getMonsterTypes(env, random)));
				zero = data.get(0);
				zero.attackMode = new EnemyAttackMode();
				seq = new SequenceAttack();
				seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ResistanceShieldAction(text[19].title, text[19].description, 999)));
				zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
				
				seq = new SequenceAttack();
				seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Daze(text[20].title, text[20].description, 0)));
				seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new DropRateAttack(text[21].title, text[21].description, 5, 7, 20)));
				seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new LockOrb(text[22].title, text[22].description, 0,7)));
				seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Daze(text[23].title, text[23].description, 0)));
				seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[24].title, text[24].description, 100, 4)));
				zero.attackMode.addAttack(new ConditionAlways(), seq);
			}
		}
		mStageEnemies.put(++stageCounter, data);
		
		data = new ArrayList<Enemy>();
		{
			for(int i=0; i<3; ++i) {
				int index = RandomUtil.getInt(5);
				int random = 246 + index;
				
				data.add(Enemy.create(env, random, 17, 24444, 433333, 3,
						new Pair<Integer, Integer>(ATTRIBUTE[index],-1),
						getMonsterTypes(env, random)));
				zero = data.get(i);
				zero.attackMode = new EnemyAttackMode();
				zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();
			}
		}
		mStageEnemies.put(++stageCounter, data);
		
		data = new ArrayList<Enemy>();
		{
			data.add(Enemy.create(env, 2414, 840694, 5333, 300000, 1,
					new Pair<Integer, Integer>(0,-1),
					getMonsterTypes(env, 2414)));
			zero = data.get(0);
			zero.attackMode = new EnemyAttackMode();
			seq = new SequenceAttack();
			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new ResistanceShieldAction(text[25].title,text[25].description,999)));
			zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
		
			seq = new SequenceAttack();
			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Gravity(text[26].title,text[26].description,99)));
			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[27].title,text[27].description,999,200)));
			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[28].title,text[28].description,100,3)));
			zero.attackMode.addAttack(new ConditionAlways(), seq);
			
		}
		mStageEnemies.put(++stageCounter, data);
	}


	public static void initSopdet(IEnvironment env,
								  SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
		mStageEnemies.clear();

		ArrayList<Enemy> data = new ArrayList<Enemy>();
		Enemy zero = null;
		SequenceAttack seq = null;

		List<SkillText> list = loadSkillText(env, "ultimate2"); //
		SkillText[] text = new SkillText[list.size()];
		int len = text.length;
		for (int i = 0; i < len; ++i) {
			text[i] = getSkillText(list, i);
		}

		int stageCounter = 0;
		int choose = 1463;
		data = new ArrayList<Enemy>();
		{
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
			zero.attackMode.createEmptyMustAction();
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
		}
		mStageEnemies.put(++stageCounter, data);
		data = new ArrayList<Enemy>();
		{
			data.add(Enemy.create(env, choose, 1685674, 22626, 0, 1,
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
		}
		mStageEnemies.put(++stageCounter, data);
		data = new ArrayList<Enemy>();
		{
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
		}
		mStageEnemies.put(++stageCounter, data);
	}

	// */
	public static void initFridayStage(IEnvironment env,
			SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
		mStageEnemies.clear();

		ArrayList<Enemy> data = new ArrayList<Enemy>();
		Enemy zero = null;
		SequenceAttack seq = null;

		List<SkillText> list = loadSkillText(env, mStage);
		SkillText[] text = new SkillText[14];
		for (int i = 0; i < 14; ++i) {
			text[i] = getSkillText(list, i);
		}

		// level 1
		data = new ArrayList<Enemy>();
		data.add(Enemy.create(env, 160, 75900, 4770, 60, 1,
				new Pair<Integer, Integer>(0, -1), getMonsterTypes(env, 160)));
		zero = data.get(0);
		zero.attackMode = new EnemyAttackMode();
		zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();
		AttackAction action = new RandomAttack();
		action.addAttack(new EnemyAttack().addPair(new ConditionPercentage(90),
				new Attack(100)));
		action.addAttack(new EnemyAttack()
				.addPair(new ConditionPercentage(50), new BindPets(
						text[0].title, text[0].description, 5, 2, 4, 1, 1)));
		action.addAttack(new EnemyAttack()
				.addPair(new ConditionPercentage(50), new BindPets(
						text[1].title, text[1].description, 5, 2, 4, 1, 4)));
		action.addAttack(new EnemyAttack()
				.addPair(new ConditionPercentage(50), new BindPets(
						text[2].title, text[2].description, 5, 2, 4, 1, 5)));
		action.addAttack(new EnemyAttack()
				.addPair(new ConditionPercentage(50), new BindPets(
						text[3].title, text[3].description, 5, 2, 4, 1, 3)));
		action.addAttack(new EnemyAttack()
				.addPair(new ConditionPercentage(50), new BindPets(
						text[4].title, text[4].description, 5, 2, 4, 1, 0)));
		zero.attackMode.addAttack(new ConditionHp(1, 0), action);

		data.add(Enemy.create(env, 155, 16489, 4978, 60, 1,
				new Pair<Integer, Integer>(1, -1), getMonsterTypes(env, 155)));
		zero = data.get(1);
		zero.attackMode = new EnemyAttackMode();
		zero.attackMode.createEmptyFirstStrike().createSimpleAttackAction("",
				"", 100);

		data.add(Enemy.create(env, 156, 17111, 5413, 60, 1,
				new Pair<Integer, Integer>(4, -1), getMonsterTypes(env, 156)));
		zero = data.get(2);
		zero.attackMode = new EnemyAttackMode();
		zero.attackMode.createEmptyFirstStrike().createSimpleAttackAction("",
				"", 100);

		data.add(Enemy.create(env, 160, 75900, 4770, 60, 1,
				new Pair<Integer, Integer>(0, -1), getMonsterTypes(env, 160)));
		zero = data.get(3);
		zero.attackMode = new EnemyAttackMode();
		zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();
		action = new RandomAttack();
		action.addAttack(new EnemyAttack().addPair(new ConditionPercentage(90),
				new Attack(100)));
		action.addAttack(new EnemyAttack()
				.addPair(new ConditionPercentage(50), new BindPets(
						text[0].title, text[0].description, 5, 2, 4, 1, 1)));
		action.addAttack(new EnemyAttack()
				.addPair(new ConditionPercentage(50), new BindPets(
						text[1].title, text[1].description, 5, 2, 4, 1, 4)));
		action.addAttack(new EnemyAttack()
				.addPair(new ConditionPercentage(50), new BindPets(
						text[2].title, text[2].description, 5, 2, 4, 1, 5)));
		action.addAttack(new EnemyAttack()
				.addPair(new ConditionPercentage(50), new BindPets(
						text[3].title, text[3].description, 5, 2, 4, 1, 3)));
		action.addAttack(new EnemyAttack()
				.addPair(new ConditionPercentage(50), new BindPets(
						text[4].title, text[4].description, 5, 2, 4, 1, 0)));
		zero.attackMode.addAttack(new ConditionHp(1, 0), action);

		data.add(Enemy.create(env, 157, 17733, 5911, 60, 1,
				new Pair<Integer, Integer>(5, -1), getMonsterTypes(env, 155)));
		zero = data.get(4);
		zero.attackMode = new EnemyAttackMode();
		zero.attackMode.createEmptyFirstStrike().createSimpleAttackAction("",
				"", 100);

		data.add(Enemy.create(env, 158, 18356, 6347, 60, 1,
				new Pair<Integer, Integer>(3, -1), getMonsterTypes(env, 156)));
		zero = data.get(5);
		zero.attackMode = new EnemyAttackMode();
		zero.attackMode.createEmptyFirstStrike().createSimpleAttackAction("",
				"", 100);

		data.add(Enemy.create(env, 160, 75900, 4770, 60, 1,
				new Pair<Integer, Integer>(0, -1), getMonsterTypes(env, 160)));
		zero = data.get(6);
		zero.attackMode = new EnemyAttackMode();
		zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();
		action = new RandomAttack();
		action.addAttack(new EnemyAttack().addPair(new ConditionPercentage(90),
				new Attack(100)));
		action.addAttack(new EnemyAttack()
				.addPair(new ConditionPercentage(50), new BindPets(
						text[0].title, text[0].description, 5, 2, 4, 1, 1)));
		action.addAttack(new EnemyAttack()
				.addPair(new ConditionPercentage(50), new BindPets(
						text[1].title, text[1].description, 5, 2, 4, 1, 4)));
		action.addAttack(new EnemyAttack()
				.addPair(new ConditionPercentage(50), new BindPets(
						text[2].title, text[2].description, 5, 2, 4, 1, 5)));
		action.addAttack(new EnemyAttack()
				.addPair(new ConditionPercentage(50), new BindPets(
						text[3].title, text[3].description, 5, 2, 4, 1, 3)));
		action.addAttack(new EnemyAttack()
				.addPair(new ConditionPercentage(50), new BindPets(
						text[4].title, text[4].description, 5, 2, 4, 1, 0)));
		zero.attackMode.addAttack(new ConditionHp(1, 0), action);
		mStageEnemies.put(1, data);
		// level 2
		int[] ids = { 246, 247, 248, 249, 250 };
		int[] prop = { 1, 4, 5, 3, 0 };
		data = new ArrayList<Enemy>();
		for (int i = 0; i < 3; ++i) {
			int rnd = RandomUtil.getInt(5);
			int id = ids[rnd];
			data.add(Enemy.create(env, id, 19, 30370, 544444, 3,
					new Pair<Integer, Integer>(prop[rnd], -1),
					getMonsterTypes(env, id)));
			zero = data.get(i);
			zero.attackMode = new EnemyAttackMode();
			zero.attackMode.createEmptyFirstStrike();
			zero.attackMode.createSimpleAttackAction("", "", 100);
		}
		mStageEnemies.put(2, data);
		// level 3
		data = new ArrayList<Enemy>();
		data.add(Enemy.create(env, 251, 24, 60123, 544444, 5,
				new Pair<Integer, Integer>(0, -1), getMonsterTypes(env, 251)));
		zero = data.get(0);
		zero.attackMode = new EnemyAttackMode();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new Attack(text[5].title, text[5].description, 10)));
		zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
		zero.attackMode.createEmptyMustAction();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new Attack(100)));
		zero.attackMode.addAttack(new ConditionPercentage(100), seq);

		data.add(Enemy.create(env, 1002, 20, 240, 600000, 1,
				new Pair<Integer, Integer>(3, -1), getMonsterTypes(env, 1002)));
		zero = data.get(1);
		zero.attackMode = new EnemyAttackMode();
		zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();
		action = new RandomAttack();
		action.addAttack(new EnemyAttack().addPair(new ConditionPercentage(75),
				new Attack(100)));
		action.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new BindPets(text[6].title, text[6].description, 3, 3, 3, 1)));
		zero.attackMode.addAttack(new ConditionHp(1, 0), action);

		data.add(Enemy.create(env, 251, 24, 60123, 544444, 5,
				new Pair<Integer, Integer>(0, -1), getMonsterTypes(env, 251)));
		zero = data.get(2);
		zero.attackMode = new EnemyAttackMode();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new Attack(text[5].title, text[5].description, 10)));
		zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
		zero.attackMode.createEmptyMustAction();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new Attack(100)));
		zero.attackMode.addAttack(new ConditionPercentage(100), seq);

		mStageEnemies.put(3, data);
		// level 4
		data = new ArrayList<Enemy>();
		data.add(Enemy.create(env, 915, 2083889, 9542, 200, 1,
				new Pair<Integer, Integer>(3, -1), getMonsterTypes(env, 915)));
		zero = data.get(0);
		zero.attackMode = new EnemyAttackMode();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new RecoverPlayer(text[7].title, text[7].description, 100)));
		zero.attackMode.addAttack(new ConditionFirstStrike(), seq);

		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(
				text[8].title, text[8].description, 999, 300)));
		zero.attackMode.addAttack(new ConditionAliveOnly(), seq);

		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 20),
				new MultipleAttack(text[9].title, text[9].description, 50, 4)));
		seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
				new BindPets(text[10].title, text[10].description, 1, 4, 4, 0)));
		seq.addAttack(new EnemyAttack().addAction(
				new ColorChange(text[11].title, text[11].description, -1, 2))
				.addPair(new ConditionHp(2, 50), new Attack(100)));
		zero.attackMode.addAttack(new ConditionHp(2, 50), seq);

		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addAction(
				new ColorChange(text[11].title, text[11].description, -1, 2))
				.addPair(new ConditionHp(1, 50), new Attack(100)));
		zero.attackMode.addAttack(new ConditionAlways(), seq);

		data.add(Enemy.create(env, 916, 3056111, 18917, 200, 1,
				new Pair<Integer, Integer>(0, -1), getMonsterTypes(env, 916)));
		zero = data.get(1);
		zero.attackMode = new EnemyAttackMode();
		zero.attackMode.createEmptyFirstStrike();

		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(
				text[8].title, text[8].description, 999, 300)));
		zero.attackMode.addAttack(new ConditionAliveOnly(), seq);

		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 20),
				new MultipleAttack(text[9].title, text[9].description, 50, 4)));
		zero.attackMode.addAttack(new ConditionAlways(), seq);
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new RandomChange(text[12].title, text[12].description, 6, 6)));
		seq.addAttack(new EnemyAttack().addAction(
				new ColorChange(text[13].title, text[13].description, 2, 7))
				.addPair(new ConditionAlways(), new Attack(100)));
		zero.attackMode.addAttack(new ConditionHp(1, 20), seq);

		mStageEnemies.put(4, data);
	}

	public static void initTuesdayStage(IEnvironment env,
			SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
		mStageEnemies.clear();

		List<SkillText> list = loadSkillText(env, mStage);
		SkillText[] text = new SkillText[5];
		for (int i = 0; i < 5; ++i) {
			text[i] = getSkillText(list, i);
		}

		ArrayList<Enemy> data = new ArrayList<Enemy>();
		// level 1
		data.add(Enemy.create(env, 147, 633650, 12764, 600, 3,
				new Pair<Integer, Integer>(1, -1), getMonsterTypes(env, 147)));
		Enemy zero = data.get(0);
		zero.attackMode = new EnemyAttackMode();
		SequenceAttack seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new Attack(text[0].title, text[0].description, 75))); // new
																		// ResistanceShieldAction(99)
		zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
		zero.attackMode.createEmptyMustAction();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 25),
				new Attack(text[1].title, text[1].description, 200)));
		zero.attackMode.addAttack(new ConditionHp(2, 25), seq);
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 101),
				new Attack("", "", 100)));
		zero.attackMode.addAttack(new ConditionHp(2, 101), seq);

		mStageEnemies.put(1, data);
		// level 2
		data = new ArrayList<Enemy>();
		data.add(Enemy.create(env, 148, 633650, 12764, 600, 3,
				new Pair<Integer, Integer>(4, -1), getMonsterTypes(env, 148)));
		zero = data.get(0);
		zero.attackMode = new EnemyAttackMode();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new Attack(text[0].title, text[0].description, 75))); // new
																		// ResistanceShieldAction(99)
		zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
		zero.attackMode.createEmptyMustAction();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 25),
				new Attack(text[1].title, text[1].description, 200)));
		zero.attackMode.addAttack(new ConditionHp(2, 25), seq);
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 101),
				new Attack("", "", 100)));
		zero.attackMode.addAttack(new ConditionHp(2, 101), seq);

		mStageEnemies.put(2, data);

		// level 3
		data = new ArrayList<Enemy>();
		data.add(Enemy.create(env, 149, 633650, 12764, 600, 3,
				new Pair<Integer, Integer>(5, -1), getMonsterTypes(env, 149)));
		zero = data.get(0);
		zero.attackMode = new EnemyAttackMode();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new Attack(text[0].title, text[0].description, 75))); // new
																		// ResistanceShieldAction(99)
		zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
		zero.attackMode.createEmptyMustAction();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 25),
				new Attack(text[1].title, text[1].description, 200)));
		zero.attackMode.addAttack(new ConditionHp(2, 25), seq);
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 101),
				new Attack("", "", 100)));
		zero.attackMode.addAttack(new ConditionHp(2, 101), seq);

		mStageEnemies.put(3, data);

		// level 4
		data = new ArrayList<Enemy>();
		data.add(Enemy.create(env, 150, 1489447, 22505, 900, 3,
				new Pair<Integer, Integer>(3, -1), getMonsterTypes(env, 150)));
		zero = data.get(0);
		zero.attackMode = new EnemyAttackMode();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new Attack(text[0].title, text[0].description, 75))); // new
																		// ResistanceShieldAction(99)
		zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
		zero.attackMode.createEmptyMustAction();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 25),
				new Attack(text[1].title, text[1].description, 200)));
		zero.attackMode.addAttack(new ConditionHp(2, 25), seq);
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 101),
				new Attack("", "", 100)));
		zero.attackMode.addAttack(new ConditionHp(2, 101), seq);

		mStageEnemies.put(4, data);

		// level 5
		data = new ArrayList<Enemy>();
		data.add(Enemy.create(env, 151, 1489447, 22505, 900, 3,
				new Pair<Integer, Integer>(0, -1), getMonsterTypes(env, 151)));
		zero = data.get(0);
		zero.attackMode = new EnemyAttackMode();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new Attack(text[0].title, text[0].description, 75))); // new
																		// ResistanceShieldAction(99)
		zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
		zero.attackMode.createEmptyMustAction();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 25),
				new Attack(text[1].title, text[1].description, 200)));
		zero.attackMode.addAttack(new ConditionHp(2, 25), seq);
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 101),
				new Attack("", "", 100)));
		zero.attackMode.addAttack(new ConditionHp(2, 101), seq);

		mStageEnemies.put(5, data);

		// level 6
		data = new ArrayList<Enemy>();
		data.add(Enemy.create(env, 1176, 7016458, 11194, 9250, 2,
				new Pair<Integer, Integer>(3, 1), getMonsterTypes(env, 1176)));
		zero = data.get(0);
		zero.attackMode = new EnemyAttackMode();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack()
				.addPair(new ConditionAlways(), new AbsorbShieldAction(
						text[2].title, text[2].description, 3, 0))); // new
																		// ResistanceShieldAction(99)
		zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
		zero.attackMode.createEmptyMustAction();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
				new Attack(text[3].title, text[3].description, 100)));
		zero.attackMode.addAttack(new ConditionUsed(), seq);
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(),
				new Attack(text[3].title, text[3].description, 100)));
		zero.attackMode.addAttack(new ConditionUsed(), seq);
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new Attack(text[4].title, text[4].description, 1000)));
		zero.attackMode.addAttack(new ConditionAlways(), seq);

		mStageEnemies.put(6, data);

		if (RandomUtil.getLuck(10)) {
			data = new ArrayList<Enemy>();
			data.add(Enemy.create(env, 321, 1379793, 27030, 1550, 4,
					new Pair<Integer, Integer>(0, -1),
					getMonsterTypes(env, 321)));
			zero = data.get(0);
			zero.attackMode = new EnemyAttackMode();
			zero.attackMode.createEmptyFirstStrike();
			zero.attackMode.createEmptyMustAction();
			seq = new SequenceAttack();
			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
					new MultipleAttack(80, 3)));
			zero.attackMode.addAttack(new ConditionAlways(), seq);

			int stage = RandomUtil.getInt(5) + 1;
			mStageEnemies.put(stage, data);
		}
	}

	public static void initThursdayStage(IEnvironment env,
			SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
		mStageEnemies.clear();

		List<SkillText> list = loadSkillText(env, mStage);
		SkillText[] text = new SkillText[14];
		for (int i = 0; i < 14; ++i) {
			text[i] = getSkillText(list, i);
		}

		ArrayList<Enemy> data = new ArrayList<Enemy>();
		// level 1
		data.add(Enemy.create(env, 152, 13378, 3111, 60, 1,
				new Pair<Integer, Integer>(5, -1), getMonsterTypes(env, 152)));
		Enemy zero = data.get(0);
		zero.attackMode = new EnemyAttackMode();
		zero.attackMode.createSimpleAttackAction(text[0].title,
				text[0].description, 1000);
		data.add(Enemy.create(env, 152, 13378, 3111, 60, 1,
				new Pair<Integer, Integer>(5, -1), getMonsterTypes(env, 152)));
		zero = data.get(1);
		zero.attackMode = new EnemyAttackMode();
		zero.attackMode.createSimpleAttackAction(text[0].title,
				text[0].description, 1000);

		data.add(Enemy.create(env, 227, 462494, 104165, 90, 3,
				new Pair<Integer, Integer>(5, -1), getMonsterTypes(env, 227)));
		zero = data.get(2);
		zero.attackMode = new EnemyAttackMode();
		SequenceAttack seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new ReduceTime(text[1].title, text[1].description, 1, -2000))); // new
																				// ResistanceShieldAction(99)
		zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
		zero.attackMode.createEmptyMustAction();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new MultipleAttack(text[2].title, text[2].description, 80, 2)));
		zero.attackMode.addAttack(new ConditionPercentage(100), seq);

		data.add(Enemy.create(env, 152, 13378, 3111, 60, 1,
				new Pair<Integer, Integer>(5, -1), getMonsterTypes(env, 152)));
		zero = data.get(3);
		zero.attackMode = new EnemyAttackMode();
		zero.attackMode.createSimpleAttackAction(text[0].title,
				text[0].description, 1000);
		data.add(Enemy.create(env, 152, 13378, 3111, 60, 1,
				new Pair<Integer, Integer>(5, -1), getMonsterTypes(env, 152)));
		zero = data.get(4);
		zero.attackMode = new EnemyAttackMode();
		zero.attackMode.createSimpleAttackAction(text[0].title,
				text[0].description, 1000);

		mStageEnemies.put(1, data);

		// level 2
		data = new ArrayList<Enemy>();
		for (int i = 0; i < 4; ++i) {
			data.add(Enemy.create(env, 153, 93144, 19321, 60, 2,
					new Pair<Integer, Integer>(5, -1),
					getMonsterTypes(env, 153)));
			zero = data.get(i);
			zero.attackMode = new EnemyAttackMode();
			zero.attackMode.createEmptyFirstStrike();

			SequenceAttack must = new SequenceAttack();
			must.addAttack(new EnemyAttack()
					.addPair(
							new ConditionPercentage(70),
							new BindPets(text[3].title, text[3].description, 2,
									1, 2, 3)).addCondition(new ConditionUsed()));
			zero.attackMode.addAttack(new ConditionAlways(), must);

			SequenceAttack normal = new SequenceAttack();
			normal.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
					new Attack(text[0].title, text[0].description, 1000)));
			zero.attackMode.addAttack(new ConditionHp(1, 0), normal);
		}
		mStageEnemies.put(2, data);

		// level 3
		data = new ArrayList<Enemy>();
		data.add(Enemy.create(env, 154, 280283, 84113, 90, 3,
				new Pair<Integer, Integer>(5, -1), getMonsterTypes(env, 154)));
		zero = data.get(0);
		zero.attackMode = new EnemyAttackMode();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new Attack(text[4].title, text[4].description, 10)));
		zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
		zero.attackMode.createEmptyMustAction();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new MultipleAttack(text[5].title, text[5].description, 500, 3)));
		zero.attackMode.addAttack(new ConditionPercentage(100), seq);

		data.add(Enemy.create(env, 1002, 20, 240, 600000, 1,
				new Pair<Integer, Integer>(3, -1), getMonsterTypes(env, 1002)));
		zero = data.get(1);
		zero.attackMode = new EnemyAttackMode();
		zero.attackMode.createEmptyFirstStrike().createEmptyMustAction();
		AttackAction action = new RandomAttack();
		action.addAttack(new EnemyAttack().addPair(new ConditionPercentage(75),
				new Attack(100)));
		action.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new BindPets(text[6].title, text[6].description, 3, 3, 3, 1)));
		zero.attackMode.addAttack(new ConditionHp(1, 0), action);

		data.add(Enemy.create(env, 227, 462494, 104165, 78, 3,
				new Pair<Integer, Integer>(5, -1), getMonsterTypes(env, 227)));
		zero = data.get(2);
		zero.attackMode = new EnemyAttackMode();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new ReduceTime(text[1].title, text[1].description, 1, -2000)));
		zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
		zero.attackMode.createEmptyMustAction();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new MultipleAttack(text[2].title, text[2].description, 80, 2)));
		zero.attackMode.addAttack(new ConditionPercentage(100), seq);

		mStageEnemies.put(3, data);

		// level 4
		data = new ArrayList<Enemy>();
		data.add(Enemy.create(env, 1085, 2083819, 8486, 70, 2,
				new Pair<Integer, Integer>(1, 5), getMonsterTypes(env, 1085)));
		zero = data.get(0);
		zero.attackMode = new EnemyAttackMode();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new ResistanceShieldAction(text[7].title, text[7].description,
						999)));
		zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
		zero.attackMode.createEmptyMustAction();
		action = new RandomAttack();
		action.addAttack(new EnemyAttack().addAction(
				new ColorChange(text[8].title, text[8].description, -1, 1))
				.addPair(new ConditionAlways(), new Attack(100)));
		action.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new MultipleAttack(text[2].title, text[2].description, 80, 2)));
		zero.attackMode.addAttack(new ConditionHp(1, 0), action);

		data.add(Enemy.create(env, 1086, 1944931, 6611, 70, 2,
				new Pair<Integer, Integer>(4, 5), getMonsterTypes(env, 1086)));
		zero = data.get(1);
		zero.attackMode = new EnemyAttackMode();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new ResistanceShieldAction(text[7].title, text[7].description,
						999)));
		zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 50),
				new LockSkill(text[10].title, text[10].description, 5))
				.addCondition(new ConditionUsed()));
		zero.attackMode.addAttack(new ConditionAlways(), seq);
		action = new RandomAttack();
		action.addAttack(new EnemyAttack().addAction(
				new ColorChange(text[9].title, text[9].description, -1, 4))
				.addPair(new ConditionAlways(), new Attack(100)));
		action.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new MultipleAttack(text[2].title, text[2].description, 80, 2)));
		zero.attackMode.addAttack(new ConditionHp(1, 0), action);

		data.add(Enemy.create(env, 1087, 3056042, 5361, 70, 1,
				new Pair<Integer, Integer>(5, 3), getMonsterTypes(env, 1087)));
		zero = data.get(2);
		zero.attackMode = new EnemyAttackMode();
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new Daze(text[11].title, text[11].description)));
		zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 101),
				new Daze(text[12].title, text[12].description)).addCondition(
				new ConditionUsed()));
		zero.attackMode.addAttack(new ConditionAlways(), seq);
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
				new Daze(text[11].title, text[11].description)));
		zero.attackMode.addAttack(new ConditionHp(0, 100), seq);
		seq = new SequenceAttack();
		seq.addAttack(new EnemyAttack()
				.addPair(new ConditionAlways(), new MultipleAttack(
						text[13].title, text[13].description, 250, 2)));
		zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
		mStageEnemies.put(4, data);
	}

	public static class SkillText {
		public String title = "";
		public String description = "";
	}

	static List<SkillText> loadSkillText(IEnvironment env, String stage) {
		Activity activity = env.getScene().getActivity();
		try {
			if (ZipUtils.unpackZip(activity,
					activity.getAssets().open("data/" + stage + ".zip"))) {
				Gson gson = new Gson();
				java.lang.reflect.Type listType = new TypeToken<List<SkillText>>() {
				}.getType();
				String loc = Locale.getDefault().getLanguage();
				if (!"ja".equals(loc) && !"zh".equals(loc) && !"en".equals(loc)) {
					loc = "en";
				}
				return gson.fromJson(
						new FileReader(new File(activity.getCacheDir(),
								"skill-" + loc + ".json")), listType);
			}
		} catch (IOException e) {
			Log.e("Vincent", e.toString());
		}
		LogUtil.e("Load skill text failed");
		return new ArrayList<SkillText>();
	}

	static SkillText getSkillText(List<SkillText> data, int index) {
		if (index < data.size()) {
			return data.get(index);
		}
		return new SkillText();
	}

	static MonsterVO getMonster(IEnvironment env, int id) {
		return LocalDBHelper.getMonsterData(env.getScene().getActivity(), id);
	}

	static List<MonsterSkill.MonsterType> getMonsterTypes(MonsterVO vo) {
		List<MonsterSkill.MonsterType> list = new ArrayList<MonsterSkill.MonsterType>();
		Pair<MonsterSkill.MonsterType, MonsterSkill.MonsterType> types = vo
				.getMonsterTypes();
		list.add(types.first);
		if (types.second != MonsterSkill.MonsterType.NONE) {
			list.add(types.second);
		}
		if (vo.getMonsterType3() != MonsterSkill.MonsterType.NONE) {
			list.add(vo.getMonsterType3());
		}
		return list;
	}

	static List<MonsterSkill.MonsterType> getMonsterTypes(IEnvironment env,
			int id) {
		MonsterVO vo = LocalDBHelper.getMonsterData(env.getScene()
				.getActivity(), id);
		List<MonsterSkill.MonsterType> list = new ArrayList<MonsterSkill.MonsterType>();
		Pair<MonsterSkill.MonsterType, MonsterSkill.MonsterType> types = vo
				.getMonsterTypes();
		list.add(types.first);
		if (types.second != MonsterSkill.MonsterType.NONE) {
			list.add(types.second);
		}
		if (vo.getMonsterType3() != MonsterSkill.MonsterType.NONE) {
			list.add(vo.getMonsterType3());
		}
		return list;
	}

}
