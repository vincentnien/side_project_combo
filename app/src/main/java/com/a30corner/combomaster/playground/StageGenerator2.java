package com.a30corner.combomaster.playground;

public class StageGenerator2 {


//	public static SparseArray<List<SkillText>> loadSkillTextNew(IEnvironment env, String stage, int[] ids) {
//		Activity activity = env.getScene().getActivity();
//		SparseArray<List<SkillText>> arr = new SparseArray<List<SkillText>>();
//		try {
//			if (ZipUtils.unpackZip(activity,
//					activity.getAssets().open("data/" + stage + ".zip"))) {
//				Gson gson = new Gson();
//				java.lang.reflect.Type listType = new TypeToken<List<SkillText>>() {
//				}.getType();
//				String loc = Locale.getDefault().getLanguage();
//				if (!"ja".equals(loc) && !"zh".equals(loc) && !"en".equals(loc)) {
//					loc = "en";
//				}
//				for(int id :ids) {
//					List<SkillText> list = gson.fromJson(new FileReader(new File(activity.getCacheDir(),
//								loc + "_" + id +".json")), listType);
//					arr.append(id, list);
//				}
//			}
//		} catch (IOException e) {
//			Log.e("Vincent", e.toString());
//		}
//		return arr;
//	}
//
//	private static SkillText[] loadSubText(SparseArray<List<SkillText>> list, int id) {
//		List<SkillText> sublist = list.get(id);
//		int size = sublist.size();
//		SkillText[] text = new SkillText[size];
//		for(int i=0; i<size; ++i) {
//			text[i] = getSkillText(sublist, i);
//		}
//		return text;
//	}
//
////
////	static void initChallenge44_Lv10(IEnvironment env,
////									 SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
////		ArrayList<Enemy> data;
////		Enemy zero = null;
////		SequenceAttack seq = null;
////		mStageEnemies.clear();
////
////		SparseArray<List<SkillText>> list = loadSkillTextNew(env, mStage, new int[]{2180,1954,1843,1619,2092});
////		int stageCounter = 0;
////
////		SkillText[] text = null;
////
////
////		data = new ArrayList<Enemy>();
////		{
////			data.add(Enemy.create(env, 2180, 25074986, 10880, 624, 1,
////					new Pair<Integer, Integer>(0,1),
////					StageGenerator.getMonsterTypes(env, 2180)));
////			zero = data.get(0);
////			zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
////			zero.attackMode = new EnemyAttackMode();
////			seq = new SequenceAttack();
////			seq.addAttack(new EnemyAttack().addAction(
////					new ResistanceShieldAction(text[22].title,
////							text[22].description, 10))
////					.addAction(new ReduceTime(text[0].title, text[0].description, 10, 500, 1))
////					.addPair(
////							new ConditionAlways(),
////							new ShieldAction(text[23].title,
////									text[23].description, 3,-1,50)));
////			zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
////
////			seq = new SequenceAttack();
////			seq.addAttack(new EnemyAttack().addAction(
////					new ComboShield(text[24].title, text[24].description,
////							5, 5)).addPair(
////					new ConditionUsed(),
////					new MultipleAttack(text[25].title,
////							text[25].description, 40, 3)));
////			zero.attackMode.addAttack(new ConditionAlways(), seq);
////		}
////		mStageEnemies.put(++stageCounter, data);
////
////		data = new ArrayList<Enemy>();
////		{
////			data.add(Enemy.create(env, 1954, 17088540, 27064, 0, 1,
////					new Pair<Integer, Integer>(1, -1),
////					StageGenerator.getMonsterTypes(env, 1954)));
////			zero = data.get(0);
////			zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 1, 50);
////			zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);
////			zero.attackMode = new EnemyAttackMode();
////			seq = new SequenceAttack();
////			seq.addAttack(new EnemyAttack()
////					.addAction(
////							new SkillDown(text[9].title, text[9].description,
////									0, 2, 2))
////					.addAction(
////							new ComboShield(text[10].title,
////									text[10].description, 99, 4))
////					.addPair(
////							new ConditionAlways(),
////							new Gravity(text[11].title, text[11].description,
////									75)));
////			zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
////
////			seq = new SequenceAttack();
////			seq.addAttack(new EnemyAttack()
////					.addAction(
////							new Gravity(text[11].title, text[11].description,
////									75))
////					.addAction(
////							new Transform(text[16].title, text[16].description,
////									1))
////					.addPair(new ConditionHp(2, 30), new Attack(300)));
////			seq.addAttack(new EnemyAttack()
////					.addAction(
////							new ColorChange(text[17].title,
////									text[17].description, 6, 2)).addPair(
////							new ConditionIf(1, 1, 0,
////									EnemyCondition.Type.FIND_ORB.ordinal(), 6),
////							new Attack(200)));
////			zero.attackMode.addAttack(new ConditionAlways(), seq);
////
////			RandomAttack rndAttack = new RandomAttack();
////			rndAttack.addAttack(new EnemyAttack().addAction(
////					new LineChange(text[12].title, text[12].description, 4, 1,
////							7, 1, 9, 1)).addPair(new ConditionAlways(),
////					new Attack(100)));
////			rndAttack.addAttack(new EnemyAttack().addPair(
////					new ConditionAlways(), new MultipleAttack(text[13].title,
////							text[13].description, 40, 3)));
////			rndAttack.addAttack(new EnemyAttack()
////					.addAction(
////							new RandomChange(text[14].title,
////									text[14].description, 6, 7)).addPair(
////							new ConditionAlways(), new Attack(50)));
////			rndAttack.addAttack(new EnemyAttack().addAction(
////					new DarkScreen(text[15].title, text[15].description, 0))
////					.addPair(new ConditionAlways(), new Attack(75)));
////			zero.attackMode.addAttack(new ConditionHp(1, 30), rndAttack);
////		}
////		mStageEnemies.put(++stageCounter, data);
////
////
////		data = new ArrayList<Enemy>();
////		{
////			data.add(Enemy.create(env, 2092, 40494510, 51368, 870, 1,
////					new Pair<Integer, Integer>(1,3),
////					StageGenerator.getMonsterTypes(env, 2092)));
////			zero = data.get(0);
////			zero.attackMode = new EnemyAttackMode();
////			seq = new SequenceAttack();
////			seq.addAttack(new EnemyAttack().addAction(
////					new ResistanceShieldAction(text[22].title,
////							text[22].description, 5))
////					.addPair(
////							new ConditionAlways(),
////							new Attack(text[23].title,
////									text[23].description, 110)));
////			zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
////
////			seq = new SequenceAttack();
////			seq.addAttack(new EnemyAttack().addAction(
////					new ComboShield(text[24].title, text[24].description,
////							5, 5)).addPair(
////					new ConditionUsed(),
////					new MultipleAttack(text[25].title,
////							text[25].description, 40, 3)));
////			seq.addAttack(new EnemyAttack().addAction(
////					new DamageAbsorbShield(text[26].title,
////							text[26].description, 5, 500000)).addPair(
////					new ConditionUsed(),
////					new MultipleAttack(text[27].title,
////							text[27].description, 20, 6)));
////			seq.addAttack(new EnemyAttack().addAction(
////					new BindPets(text[28].title, text[28].description, 3,
////							5, 5, 5)).addPair(new ConditionUsed(),
////					new Attack(150)));
////			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
////					new MultipleAttack(text[29].title,
////							text[29].description, 1000, 5)));
////			zero.attackMode.addAttack(new ConditionAlways(), seq);
////		}
////		mStageEnemies.put(++stageCounter, data);
////	}
//
//
//
//	static void init201806_Lv10(IEnvironment env,
//							   SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
//		ArrayList<Enemy> data;
//		Enemy zero = null;
//		SequenceAttack seq = null;
//		mStageEnemies.clear();
//
//		SparseArray<List<SkillText>> list = loadSkillTextNew(env, mStage, new int[]{3541, 761, 1105, 2008, 1190, 2742, 2391});
//		int stageCounter = 0;
//
//		data = new ArrayList<Enemy>();
//		{
//			SkillText[] text = loadSubText(list, 3541);
//			data.add(Enemy.create(env, 3541, 12250000, 9133, 13500, 1,
//					new Pair<Integer, Integer>(4, 0),
//					StageGenerator.getMonsterTypes(env, 3541)));
//			zero = data.get(0);
//			zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);
//			zero.attackMode = new EnemyAttackMode();
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[2].title,text[2].description,4)).addPair(new ConditionAlways(), new DropLockAttack(text[1].title,text[1].description,10,-1,20)));
//			zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new SuperDark(text[3].title,text[3].description,1, 2, 1, 3, 1, 0)).addPair(new ConditionUsed(), new Attack(300))); // turns, even if target, odd if line
//			seq.addAttack(new EnemyAttack().addAction(new SuperDark(text[4].title,text[4].description,1, 0, 1, 1, 1, 0)).addPair(new ConditionUsed(), new Attack(300)));
//			seq.addAttack(new EnemyAttack().addAction(new BindPets(text[5].title,text[5].description,2, 3, 3, 2)).addAction(new Attack(300)).addPair(new ConditionUsed(), new Angry(text[6].title,text[6].description,2, 300)));
//			seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[8].title,text[8].description,3, 150)).addPair(new ConditionUsed(), new ShieldAction(text[7].title,text[7].description,3, -1, 50)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[9].title,text[9].description,999, 200)));
//			zero.attackMode.addAttack(new ConditionAlways(), seq);
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new ChangeAttribute(text[11].title,text[11].description,4, 0)).addPair(new ConditionAlways(), new MultipleAttack(text[10].title,text[10].description,2, 160)));
//			zero.attackMode.addAttack(new ConditionHp(2,15), seq);
//
//			RandomAttack rnd = new RandomAttack();
//			rnd.addAttack(new EnemyAttack().addAction(new SuperDark(text[3].title,text[3].description,1, 2, 1, 3, 1, 0)).addPair(new ConditionAlways(), new Attack(300))); // turns, even if target, odd if line
//			rnd.addAttack(new EnemyAttack().addAction(new SuperDark(text[4].title,text[4].description,1, 0, 1, 1, 1, 0)).addPair(new ConditionAlways(), new Attack(300)));
//			rnd.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[10].title,text[10].description,2, 160)));
//			zero.attackMode.addAttack(new ConditionHp(1,15), rnd);
//		}
//		mStageEnemies.put(++stageCounter, data);
//
//		data = new ArrayList<Enemy>();
//		{
//			SkillText[] text = loadSubText(list, 761);
//			data.add(Enemy.create(env, 761, 6141240, 27288, 720, 1,
//					new Pair<Integer, Integer>(0, 4),
//					StageGenerator.getMonsterTypes(env, 761)));
//			zero = data.get(0);
//			zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 0, 50);
//			zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 3, 50);
//			zero.addOwnAbility(BuffOnEnemySkill.Type.SHIELD, -1, 4, 50);
//			zero.attackMode = new EnemyAttackMode();
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new ReduceTime(text[2].title,text[2].description,10, -2000)).addPair(new ConditionAlways(), new RandomChange(text[1].title,text[1].description,9, 3)));
//			zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new ShieldAction(text[3].title,text[3].description,4, -1, 50)).addPair(new ConditionUsed(), new ResistanceShieldAction(text[4].title,text[4].description,4)));
//			zero.attackMode.addAttack(new ConditionAlways(), seq);
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new Attack(text[5].title,text[5].description,100)).addAction(new LineChange(3,4)).addAction(new RandomLineChange(2,4,0,1,11)).addPair(new ConditionAlways(), new LockOrb(text[6].title,text[6].description,0,4)));
//			seq.addAttack(new EnemyAttack().addAction(new Attack(text[5].title,text[5].description,100)).addAction(new RandomLineChange(2, 4, 0, 1, 14)).addAction(new LineChange(4,0)).addPair(new ConditionAlways(), new LockOrb(text[8].title,text[8].description,0,0)));
//			seq.addAttack(new EnemyAttack().addAction(new Attack(text[9].title,text[9].description,100)).addAction(new LineChange(8,0)).addAction(new LineChange(9,4)).addPair(new ConditionAlways(), new LockOrb(text[10].title,text[10].description,2,12)));
//			seq.addAttack(new EnemyAttack()
//					.addAction(new LineChange(3,4)).addAction(new RandomLineChange(2,4,0,1,11)).addAction(new Attack(text[5].title,text[5].description,100))
//					.addAction(new RandomLineChange(2, 4, 0, 1,14)).addAction(new LineChange(4,0)).addAction(new Attack(text[7].title,text[7].description,100))
//					.addAction(new LineChange(8,0)).addAction(new LineChange(9,4)).addPair(new ConditionAlways(), new Attack(text[9].title,text[9].description,100))
//			);
//			zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
//		}
//		mStageEnemies.put(++stageCounter, data);
//
//		data = new ArrayList<Enemy>();
//		{
//			SkillText[] text = loadSubText(list, 1105);
//			data.add(Enemy.create(env, 1105, 2640120, 15192, 580, 1,
//					new Pair<Integer, Integer>(0,3),
//					StageGenerator.getMonsterTypes(env, 1105)));
//			zero = data.get(0);
//			zero.attackMode = new EnemyAttackMode();
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new ComboShield(text[2].title,text[2].description,999, 4)).addPair(new ConditionAlways(), new SuperDark(text[1].title,text[1].description,10, -1)));
//			zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
//
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new Attack(text[3].title,text[3].description,100)).addPair(new ConditionUsed(), new LineChange(2, 3)));
//			seq.addAttack(new EnemyAttack().addAction(new Attack(text[4].title,text[4].description,100)).addPair(new ConditionUsed(), new LineChange(1, 3)));
//			seq.addAttack(new EnemyAttack().addAction(new Attack(text[5].title,text[5].description,100)).addPair(new ConditionUsed(), new LineChange(0, 3)));
//			zero.attackMode.addAttack(new ConditionAlways(), seq);
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new Angry(text[7].title, text[7].description,99, 300)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 99), new DropRateAttack(text[6].title,text[6].description,10, 6, 10)));
//			seq.addAttack(new EnemyAttack().addAction(new DropRateAttack(text[8].title,text[8].description,10, 8, 10)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 1, 99), new RecoverSelf(text[9].title, text[9].description, -100)));
//			zero.attackMode.addAttack(new ConditionUsed(), seq);
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new Attack(text[3].title,text[3].description,100)).addPair(new ConditionUsed(), new LineChange(2, 3)));
//			seq.addAttack(new EnemyAttack().addAction(new Attack(text[4].title,text[4].description,100)).addPair(new ConditionUsed(), new LineChange(1, 3)));
//			seq.addAttack(new EnemyAttack().addAction(new Attack(text[5].title,text[5].description,100)).addPair(new ConditionUsed(), new LineChange(0, 3)));
//			zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
//		}
//		mStageEnemies.put(++stageCounter, data);
//
//		data = new ArrayList<Enemy>();
//		{
//			SkillText[] text = loadSubText(list, 2008);
//			data.add(Enemy.create(env, 2008, 29856238, 25530, 552, 1,
//					new Pair<Integer, Integer>(1, 3),
//					getMonsterTypes(env, 2008)));
//			zero = data.get(0);
//			zero.attackMode = new EnemyAttackMode();
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(),
//					new ComboShield(text[1].title, text[1].description,
//							5, 7)));
//			zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
//							1, 0, EnemyCondition.Type.HP.ordinal(), 2, 80),
//					new SkillDown(text[3].title, text[3].description,
//							0, 2, 2)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,
//							1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50),
//					new SwitchLeader(text[2].title,
//							text[2].description, 2)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2, 10),
//					new MultipleAttack(text[5].title,
//							text[5].description, 100, 12)));
//			zero.attackMode.addAttack(new ConditionAlways(), seq);
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(
//					new ColorChange(text[4].title, text[4].description,
//							-1, 7)).addPair(new ConditionAlways(),
//					new Attack(100)));
//			zero.attackMode.addAttack(new ConditionHp(1, 10), seq);
//		}
//		mStageEnemies.put(++stageCounter, data);
//
//		data = new ArrayList<Enemy>();
//		{
//			SkillText[] text = loadSubText(list, 1190);
//			data.add(Enemy.create(env, 1190, 3500000, 30500, 300, 1,
//					new Pair<Integer, Integer>(5,-1),
//					StageGenerator.getMonsterTypes(env, 1190)));
//			zero = data.get(0);
//			zero.attackMode = new EnemyAttackMode();
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new RecoveryUpAction(text[1].title,text[1].description,10, 50)).addPair(new ConditionAlways(), new ShieldAction(text[2].title,text[2].description,5, -1, 99)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1,1, 0, EnemyCondition.Type.HAS_BUFF.ordinal(), 0), new IntoVoid(text[3].title,text[3].description,0)));
//			zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,1,1,EnemyCondition.Type.ENEMY_BUFF.ordinal(),5), new ShieldAction(text[5].title,text[5].description,5, -1, 75)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsedIf(1,1,1,EnemyCondition.Type.ENEMY_BUFF.ordinal(),5), new ShieldAction(text[8].title,text[8].description,5, -1, 50)));
//			zero.attackMode.addAttack(new ConditionAlways(), seq);
//
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new ResistanceShieldAction(text[4].title,text[4].description,999)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[6].title,text[6].description, 2,50)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[6].title,text[6].description, 2,50)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[6].title,text[6].description, 2,50)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new ReduceTime(text[7].title,text[7].description,10,-3000)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[6].title,text[6].description, 2,50)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[6].title,text[6].description, 2,50)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new MultipleAttack(text[6].title,text[6].description, 2,50)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[9].title,text[9].description, 999, 300)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[6].title,text[6].description, 2,50)));
//			zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
//		}
//		mStageEnemies.put(++stageCounter, data);
//
//		data = new ArrayList<Enemy>();
//		{
//			SkillText[] text = loadSubText(list, 2742);
//			data.add(Enemy.create(env, 2742, 21281036, 25223, 492, 1,
//					new Pair<Integer, Integer>(3,-1),
//					StageGenerator.getMonsterTypes(env, 2742)));
//			zero = data.get(0);
//			zero.attackMode = new EnemyAttackMode();
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new DropRateAttack(text[2].title,text[2].description,5, 3, 25, 2, 25)).addPair(new ConditionAlways(), new ResistanceShieldAction(text[1].title,text[1].description,999)));
//			zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
//
//			zero.attackMode.createEmptyMustAction();
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new Gravity(text[3].title,text[3].description, 60)).addAction(new RandomChange(text[4].title,text[4].description,8, 1)).addPair(new ConditionAlways(), new Attack(40)));
//			seq.addAttack(new EnemyAttack().addAction(new Gravity(text[3].title,text[3].description, 60)).addAction(new RandomChange(text[4].title,text[4].description,8, 1)).addPair(new ConditionAlways(), new Attack(40)));
//			seq.addAttack(new EnemyAttack().addAction(new Gravity(text[3].title,text[3].description, 60)).addAction(new RandomChange(text[4].title,text[4].description,8, 1)).addPair(new ConditionAlways(), new Attack(40)));
//            seq.addAttack(new EnemyAttack().addPair(new ConditionIf(1,1,1, EnemyCondition.Type.FIND_ORB.ordinal(), 8), new RecoverPlayer(text[6].title,text[6].description, 100)));
//			seq.addAttack(new EnemyAttack().addAction(new ColorChange(8, 3)).addPair(new ConditionIf(1,1,0, EnemyCondition.Type.FIND_ORB.ordinal(), 8), new Attack(text[5].title,text[5].description, 5000)));
//			zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
//		}
//		mStageEnemies.put(++stageCounter, data);
//
//		data = new ArrayList<Enemy>();
//		{
//			SkillText[] text = loadSubText(list, 2391);
//			data.add(Enemy.create(env, 2391, 73500000, 41407, 672, 2,
//					new Pair<Integer, Integer>(4, 3),
//					StageGenerator.getMonsterTypes(env, 2391)));
//			zero = data.get(0);
//			zero.attackMode = new EnemyAttackMode();
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new RecoveryUpAction(text[1].title,text[1].description,6,50)).addAction(new ShieldAction(text[2].title,text[2].description,6, -1, 75)).addPair(new ConditionIf(1, 1, 1, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 3, 0), new Gravity(text[3].title,text[3].description,99)));
//            seq.addAttack(new EnemyAttack().addAction(new Attack(text[1].title,text[1].description,100)).addAction(new ShieldAction(text[2].title,text[2].description,6, -1, 75)).addPair(new ConditionIf(1, 1, 0, EnemyCondition.Type.HAS_DEBUFF.ordinal(), 3, 0), new Gravity(text[3].title,text[3].description,99)));
//			zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new ResistanceShieldAction(text[4].title,text[4].description, 999)).addAction(new Attack(100)).addAction(new Transform(text[5].title,text[5].description,4)).addPair(new ConditionUsed(), new LineChange(text[6].title,text[6].description,1, 3)));
//			seq.addAttack(new EnemyAttack().addAction(new Transform(text[5].title,text[5].description,4)).addAction(new Attack(100)).addPair(new ConditionUsed(), new DropRateAttack(text[7].title,text[7].description, 3, 7, 20)));
//			seq.addAttack(new EnemyAttack().addAction(new Transform(text[5].title,text[5].description,4)).addAction(new Attack(100)).addPair(new ConditionUsed(), new DropLockAttack(text[8].title,text[8].description, 1, -1, 100)));
//			zero.attackMode.addAttack(new ConditionAlways(), seq);
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new MultipleAttack(text[9].title,text[9].description, 2,1000)));
//			zero.attackMode.addAttack(new ConditionHp(1, 0), seq);
//		}
//		mStageEnemies.put(++stageCounter, data);
//	}
//
//
//	public static class SkillText {
//		public String title = "";
//		public String description = "";
//	}
//
//	static List<SkillText> loadSkillText(IEnvironment env, String stage) {
//		Activity activity = env.getScene().getActivity();
//		try {
//			if (ZipUtils.unpackZip(activity,
//					activity.getAssets().open("data/" + stage + ".zip"))) {
//				Gson gson = new Gson();
//				java.lang.reflect.Type listType = new TypeToken<List<SkillText>>() {
//				}.getType();
//				String loc = Locale.getDefault().getLanguage();
//				if (!"ja".equals(loc) && !"zh".equals(loc) && !"en".equals(loc)) {
//					loc = "en";
//				}
//				return gson.fromJson(
//						new FileReader(new File(activity.getCacheDir(),
//								"skill-" + loc + ".json")), listType);
//			}
//		} catch (IOException e) {
//			Log.e("Vincent", e.toString());
//		}
//		LogUtil.e("Load skill text failed");
//		return new ArrayList<SkillText>();
//	}
//
//	static SkillText getSkillText(List<SkillText> data, int index) {
//		if (index < data.size()) {
//			return data.get(index);
//		}
//		return new SkillText();
//	}
//
//
//	static List<MonsterSkill.MonsterType> getMonsterTypes(IEnvironment env,
//														  int id) {
//		MonsterVO vo = LocalDBHelper.getMonsterData(env.getScene()
//				.getActivity(), id);
//		List<MonsterSkill.MonsterType> list = new ArrayList<MonsterSkill.MonsterType>();
//		Pair<MonsterSkill.MonsterType, MonsterSkill.MonsterType> types = vo
//				.getMonsterTypes();
//		list.add(types.first);
//		if (types.second != MonsterSkill.MonsterType.NONE) {
//			list.add(types.second);
//		}
//		if (vo.getMonsterType3() != MonsterSkill.MonsterType.NONE) {
//			list.add(vo.getMonsterType3());
//		}
//		return list;
//	}


//	static void initGaiaDragon(IEnvironment env,
//							   SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
//		ArrayList<Enemy> data;
//		Enemy zero = null;
//		SequenceAttack seq = null;
//		mStageEnemies.clear();
//
//		List<SkillText> list = loadSkillText(env, mStage);
//		SkillText[] text = new SkillText[list.size()];
//		int len = text.length;
//		for (int i = 0; i < len; ++i) {
//			text[i] = getSkillText(list, i);
//		}
//		int stageCounter = 0;
//
//
//
//		data = new ArrayList<Enemy>();
//		{
//			data.add(Enemy.create(env, 2718, 33500000, 8528, 50000, 1,
//					new Pair<Integer, Integer>(5,-1),
//					StageGenerator.getMonsterTypes(env, 2718)));
//			zero = data.get(0);
//			zero.attackMode = new EnemyAttackMode();
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction( new DamageVoidShield(text[30].title,text[30].description,99, 6000000)).addAction(new ShieldAction(text[29].title,text[29].description,7,-1,75)).addPair(new ConditionAlways(), new ResistanceShieldAction(text[28].title,text[28].description,999)));
//			zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new RandomChange(text[31].title,text[31].description, 7,3)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[32].title,text[32].description, 1,690)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(100)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new RandomChange(text[33].title,text[33].description, 7,3)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Angry(text[34].title,text[34].description, 1,690)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new Attack(100)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionUsed(), new DarkScreen(text[35].title,text[35].description, 1)));
//			zero.attackMode.addAttack(new ConditionAlways(), seq);
//
//			seq = new SequenceAttack();
//			int[][] absorb = {{1,4},{4,5}, {4,3},{3,0}, {1,0}};
//			int rnd = RandomUtil.getInt(5);
//			seq.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[37+rnd].title,text[37+rnd].description,3,absorb[rnd][0])).addAction(new AbsorbShieldAction(3,absorb[rnd][1])).addAction(new Attack(text[36].title,text[36].description,450)).addPair(new ConditionHp(1,50), new LineChange(8,5,9,5)));
//			seq.addAttack(new EnemyAttack().addPair(new ConditionHp(2,50), new Gravity(text[42].title,text[42].description,500)));
//			zero.attackMode.addAttack(new ConditionUsed(), seq);
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Gravity(text[42].title,text[42].description,500)));
//			zero.attackMode.addAttack(new ConditionHp(2,20), seq);
//
//			RandomAttack rndAtk = new RandomAttack();
//			rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[44].title,text[44].description,420)).addPair(new ConditionAlways(), new RecoverSelf(10)));
//			rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[45].title,text[45].description,410)).addPair(new ConditionAlways(), new BindPets(3,5,5,1)));
//			rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[46].title,text[46].description,340)).addPair(new ConditionAlways(), new LockOrb(1,6,1,4,5,3,0,2,6,7,8)));
//			rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[47].title,text[47].description,360)).addPair(new ConditionAlways(), new DarkScreen(0)));
//			zero.attackMode.addAttack(new ConditionHp(2,30), rndAtk);
//
//			rndAtk = new RandomAttack();
//			rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[46].title,text[46].description,340)).addPair(new ConditionAlways(), new LockOrb(1,6,1,4,5,3,0,2,6,7,8)));
//			rndAtk.addAttack(new EnemyAttack().addAction(new Attack(text[47].title,text[47].description,360)).addPair(new ConditionAlways(), new DarkScreen(0)));
//			zero.attackMode.addAttack(new ConditionHp(2,50), rndAtk);
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new Attack(text[43].title,text[43].description,260)).addPair(new ConditionFindOrb(1), new SkillDown(0,3,5)));
//			zero.attackMode.addAttack(new ConditionHp(1,50), seq);
//		}
//		mStageEnemies.put(++stageCounter, data);
//	}


//	static void init2716Dragon(IEnvironment env,
//							  SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
//		ArrayList<Enemy> data;
//		Enemy zero = null;
//		SequenceAttack seq = null;
//		mStageEnemies.clear();
//
//		List<SkillText> list = loadSkillText(env, mStage);
//		SkillText[] text = new SkillText[list.size()];
//		int len = text.length;
//		for (int i = 0; i < len; ++i) {
//			text[i] = getSkillText(list, i);
//		}
//		int stageCounter = 0;
//
//
//		data = new ArrayList<Enemy>();
//		{
//			data.add(Enemy.create(env, 2716, 78000000, 11744, 1744, 1,
//					new Pair<Integer, Integer>(1,-1),
//					StageGenerator.getMonsterTypes(env, 2716)));
//			zero = data.get(0);
//			zero.attackMode = new EnemyAttackMode();
//			seq = new SequenceAttack();
////			seq.addAttack(new EnemyAttack().addAction(new ShieldAction(text[0].title,text[0].description,1,4,50)).addAction( new SkillDown(text[2].title,text[2].description,1,15,15)).addPair(new ConditionAlways(), new ResistanceShieldAction(text[1].title,text[1].description,999)));
////			zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
////			seq = new SequenceAttack();
////			seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[11].title,text[11].description, 1000,3)).addAction(new IntoVoid(text[10].title,text[10].description, 0)).addPair(new ConditionHp(2, 10), new ShieldAction(text[9].title,text[9].description, 1,-1,75)));
////			seq.addAttack(new EnemyAttack().addAction(new Attack(text[13].title,text[13].description, 450)).addAction(new Transform(1,4,5,3,0)).addAction(new LockSkill(text[12].title,text[12].description, 10)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 90), new ShieldAction(text[9].title,text[9].description, 1,-1,75)));
////			seq.addAttack(new EnemyAttack().addAction(new Attack(text[15].title,text[15].description, 600)).addAction(new Transform(1,4,5,3,0)).addAction(new DarkScreen(text[14].title,text[14].description, 0)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 70), new ShieldAction(text[9].title,text[9].description, 1,-1,75)));
////			seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[17].title,text[17].description, 400,3)).addAction(new SkillDown(text[16].title,text[16].description, 0,5,5)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new ShieldAction(text[9].title,text[9].description, 1,-1,75)));
////			seq.addAttack(new EnemyAttack().addAction(new Attack(text[19].title,text[19].description, 2000)).addAction(new Transform(1,4,5,3,0)).addAction(new LockAwoken(text[18].title,text[18].description, 2)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 30), new ShieldAction(text[9].title,text[9].description, 1,-1,75)));
////			zero.attackMode.addAttack(new ConditionAlways(), seq);
////
////			seq = new SequenceAttack();
////			seq.addAttack(new EnemyAttack().addAction(new Attack(text[26].title,text[26].description,150)).addAction(new ColorChange(-1,7)).addAction(new Attack(text[25].title,text[25].description,50)).addAction(new BindPets(2,1,1,1)).addPair(new ConditionAlways(), new Gravity(text[27].title,text[27].description,99)));
////			zero.attackMode.addAttack(new ConditionHp(2,50), seq);
////
////			RandomAttack ra = new RandomAttack();
////			int[][] colorIndex = {{20,23},{20,24},{21,23},{21,24},{22,23},{22,24}};
////			for(int i=0; i<6; ++i) {
////				for(int j=0; j<2; ++j) {
////					ra.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[3+i].title,text[3+i].description,1,absorb[i][0])).addAction(new AbsorbShieldAction(1,absorb[i][1])).addAction(new Attack(text[colorIndex[i][j]].title,text[colorIndex[i][j]].description,100)).addAction(new ColorChange(-1, absorb[i][j])).addAction(new Attack(text[25].title,text[25].description,50)).addPair(new ConditionAlways(), new BindPets(2,1,1,1)));
////				}
////			}
////			zero.attackMode.addAttack(new ConditionHp(1,50), ra);
//		}
//		mStageEnemies.put(++stageCounter, data);
//	}
//
//	static void initNoaDragon(IEnvironment env,
//							   SparseArray<ArrayList<Enemy>> mStageEnemies, String mStage) {
//		ArrayList<Enemy> data;
//		Enemy zero = null;
//		SequenceAttack seq = null;
//		mStageEnemies.clear();
//
//		List<SkillText> list = loadSkillText(env, mStage);
//		SkillText[] text = new SkillText[list.size()];
//		int len = text.length;
//		for (int i = 0; i < len; ++i) {
//			text[i] = getSkillText(list, i);
//		}
//		int stageCounter = 0;
//
//
//		data = new ArrayList<Enemy>();
//		{
//			data.add(Enemy.create(env, 2717, 31150000, 11342, 1962, 1,
//					new Pair<Integer, Integer>(4, -1),
//					getMonsterTypes(env, 2717)));
//			zero = data.get(0);
//			zero.attackMode = new EnemyAttackMode();
//			seq = new SequenceAttack();
//			int[][] absorb = {{1,3},{1,0}, {4,3},{4,0}, {5,3},{5,0}};
//			int rnd = RandomUtil.getInt(6);
//			seq.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[3+rnd].title,text[3+rnd].description,1,absorb[rnd][0])).addAction(new AbsorbShieldAction(1,absorb[rnd][1])).addAction( new DamageVoidShield(text[2].title,text[2].description,99, 5000000)).addPair(new ConditionAlways(), new ResistanceShieldAction(text[1].title,text[1].description,999)));
//			zero.attackMode.addAttack(new ConditionFirstStrike(), seq);
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[11].title,text[11].description, 1000,3)).addAction(new IntoVoid(text[10].title,text[10].description, 0)).addPair(new ConditionHp(2, 10), new ShieldAction(text[9].title,text[9].description, 1,-1,75)));
//			seq.addAttack(new EnemyAttack().addAction(new Attack(text[13].title,text[13].description, 450)).addAction(new Transform(1,4,5,3,0)).addAction(new LockSkill(text[12].title,text[12].description, 10)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 90), new ShieldAction(text[9].title,text[9].description, 1,-1,75)));
//			seq.addAttack(new EnemyAttack().addAction(new Attack(text[15].title,text[15].description, 600)).addAction(new Transform(1,4,5,3,0)).addAction(new DarkScreen(text[14].title,text[14].description, 0)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 70), new ShieldAction(text[9].title,text[9].description, 1,-1,75)));
//			seq.addAttack(new EnemyAttack().addAction(new MultipleAttack(text[17].title,text[17].description, 400,3)).addAction(new SkillDown(text[16].title,text[16].description, 0,5,5)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 50), new ShieldAction(text[9].title,text[9].description, 1,-1,75)));
//			seq.addAttack(new EnemyAttack().addAction(new Attack(text[19].title,text[19].description, 2000)).addAction(new Transform(1,4,5,3,0)).addAction(new LockAwoken(text[18].title,text[18].description, 2)).addPair(new ConditionUsedIf(1, 1, 0, EnemyCondition.Type.HP.ordinal(), 2, 30), new ShieldAction(text[9].title,text[9].description, 1,-1,75)));
//			zero.attackMode.addAttack(new ConditionAlways(), seq);
//
//			seq = new SequenceAttack();
//			seq.addAttack(new EnemyAttack().addAction(new Attack(text[26].title,text[26].description,150)).addAction(new ColorChange(-1,7)).addAction(new Attack(text[25].title,text[25].description,50)).addAction(new BindPets(2,1,1,1)).addPair(new ConditionAlways(), new Gravity(text[27].title,text[27].description,99)));
//			zero.attackMode.addAttack(new ConditionHp(2,50), seq);
//
//			RandomAttack ra = new RandomAttack();
//			int[][] colorIndex = {{20,23},{20,24},{21,23},{21,24},{22,23},{22,24}};
//			for(int i=0; i<6; ++i) {
//				for(int j=0; j<2; ++j) {
//					ra.addAttack(new EnemyAttack().addAction(new AbsorbShieldAction(text[3+i].title,text[3+i].description,1,absorb[i][0])).addAction(new AbsorbShieldAction(1,absorb[i][1])).addAction(new Attack(text[colorIndex[i][j]].title,text[colorIndex[i][j]].description,100)).addAction(new ColorChange(-1, absorb[i][j])).addAction(new Attack(text[25].title,text[25].description,50)).addPair(new ConditionAlways(), new BindPets(2,1,1,1)));
//				}
//			}
//			zero.attackMode.addAttack(new ConditionHp(1,50), ra);
//		}
//		mStageEnemies.put(++stageCounter, data);
//	}

}
