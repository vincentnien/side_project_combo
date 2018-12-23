package com.a30corner.combomaster.pad;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.pad.monster.LeaderSkill;
import com.a30corner.combomaster.pad.monster.LeaderSkill.LeaderSkillType;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.AwokenSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MonsterType;
import com.a30corner.combomaster.pad.monster.MonsterSkill.SinglePowerUp;
import com.a30corner.combomaster.pad.monster.TeamInfo;
import com.a30corner.combomaster.playground.action.impl.Attack;
import com.a30corner.combomaster.utils.Constants;
import com.a30corner.combomaster.utils.LogUtil;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.a30corner.combomaster.pad.monster.LeaderSkill.LeaderSkillType.*;

public class DamageCalculator {

	public static double calculateBomb(int hp, int bombCount) {
		return hp*bombCount*0.2;
	}

	public static class Damage {
		public double value;
		public int prop;
		public boolean isAttackAll;
		public boolean isTwoWay;
		public boolean isSquare;

		public Damage(double v, int p, boolean atkAll, boolean twoWay, boolean square) {
			value = v;
			prop = p;
			isAttackAll = atkAll;
			isTwoWay = twoWay;
			isSquare = square;
		}

		public static Damage zero(int prop) {
			return new Damage(0.0, prop, false, false, false);
		}

		public static Damage zero() {
			return new Damage(0.0, -1, false, false, false);
		}
	}
//
//	// for simulator, we need to know the attack is attack all or two way or
//	// single target
//	public static List<Pair<Damage, Damage>> calculate(TeamInfo info,
//			List<Match> combos, ActiveSkill skillFired) {
//		if (skillFired == null) {
//			skillFired = ActiveSkill.empty();
//		}
//		List<Pair<Damage, Damage>> monsterAtk = new ArrayList<Pair<Damage, Damage>>();
//		if (combos != null) {
//			double comboFactor = 1 + (0.25 * (combos.size() - 1));
//			// one attack skill count
//			int[] rowSkill = { 0, 0, 0, 0, 0, 0 };
//			int[] plusOSkill = { 0, 0, 0, 0, 0, 0 };
//			for (int i = 0; i < 6; ++i) {
//				MonsterInfo minfo = info.getMember(i);
//				if (minfo == null) {
//					continue;
//				}
//				rowSkill[0] += minfo
//						.getTargetAwokenCount(AwokenSkill.ENHANCE_DARK_ATT, false);
//				rowSkill[1] += minfo
//						.getTargetAwokenCount(AwokenSkill.ENHANCE_FIRE_ATT, false);
//				// rowSkill[2] +=
//				// minfo.getTargetAwokenCount(AwokenSkill.ENHANCE_HEAL);
//				rowSkill[3] += minfo
//						.getTargetAwokenCount(AwokenSkill.ENHANCE_LIGHT_ATT, false);
//				rowSkill[4] += minfo
//						.getTargetAwokenCount(AwokenSkill.ENHANCE_WATER_ATT, false);
//				rowSkill[5] += minfo
//						.getTargetAwokenCount(AwokenSkill.ENHANCE_WOOD_ATT, false);
//
//				plusOSkill[0] += minfo
//						.getTargetAwokenCount(AwokenSkill.ENHANCE_DARK, false);
//				plusOSkill[1] += minfo
//						.getTargetAwokenCount(AwokenSkill.ENHANCE_FIRE, false);
//				plusOSkill[3] += minfo
//						.getTargetAwokenCount(AwokenSkill.ENHANCE_LIGHT, false);
//				plusOSkill[4] += minfo
//						.getTargetAwokenCount(AwokenSkill.ENHANCE_WATER, false);
//				plusOSkill[5] += minfo
//						.getTargetAwokenCount(AwokenSkill.ENHANCE_WOOD, false);
//			}
//
//			// calculate normal attack - atk * (rm orb factor) * (combo factor)
//			boolean[] pattackAll = { false, false, false, false, false, false };
//			boolean[] ptwoWay = { false, false, false, false, false, false };
//			boolean[] psquare = {false, false, false, false, false, false};
//			boolean[] sattackAll = { false, false, false, false, false, false };
//			boolean[] stwoWay = { false, false, false, false, false, false };
//			boolean[] ssquare = {false, false, false, false, false, false};
//			List<List<Pair<Double, Double>>> normalAtk = new ArrayList<List<Pair<Double, Double>>>();
//			for (int i = 0; i < 6; ++i) {
//				MonsterInfo minfo = info.getMember(i);
//				if (minfo == null) {
//					continue;
//				}
//				int square = minfo.getTargetAwokenCount(AwokenSkill.SQUARE_ATTACK, false);
//				int twoway = minfo
//						.getTargetAwokenCount(AwokenSkill.TWO_PRONGED_ATTACK, false);
//				int combo7 = minfo.getTargetAwokenCount(AwokenSkill.COMBO_7, false);
//				Pair<Integer, Integer> props = minfo.getProps();
//				int atk = minfo.getAtk();
//				List<Pair<Double, Double>> memberBasicAtk = new ArrayList<Pair<Double, Double>>();
//
//				int[] rowcount = { 0, 0, 0, 0, 0, 0, 0, 0, 0,0 };
//				for (Match m : combos) {
//					if (m.isOneRow()) {
//						++rowcount[m.type];
//					}
//				}
//				for (Match m : combos) {
//					if (props.first != m.type && props.second != m.type) {
//						continue;
//					}
//
//					double damage = (1.0 + (.25 * (m.count - 3)));
//					if (m.pcount > 0) {
//						damage *= (1.0 + 0.06 * m.pcount)
//								* (1.0 + 0.05 * plusOSkill[m.type]);
//					}
//					if (m.isOneRow() && rowSkill[m.type] > 0) {
//						damage *= (1.0 + 0.1 * rowSkill[m.type]
//								* rowcount[m.type]);
//					}
//					double atk1 = 0.0;
//					double atk2 = 0.0;
//
//					if (props.first == m.type) {
//						atk1 = Math.ceil(damage * atk);
//						if (m.count >= 5) {
//							pattackAll[i] = true;
//						}
//					}
//					if (props.second == m.type) {
//						double calc = damage;
//						if (props.second == props.first) {
//							calc = Math.ceil(calc * atk * 0.1);
//						} else {
//							calc = Math.ceil(calc * atk / 3.0);
//						}
//
//						atk2 = Math.ceil(calc);
//						if (m.count >= 5) {
//							sattackAll[i] = true;
//						}
//					}
//					// not only major prop will be enhanced
//					if (square > 0 && m.isSquare()) {
//						if (props.first == m.type) {
//							atk1 = Math.round(atk1 * Math.pow(2.5, square));
//							psquare[i] = true;
//						}
//						if (props.second == m.type) {
//							atk2 = Math.round(atk2 * Math.pow(2.5, square));
//							ssquare[i] = true;
//						}
//					}
//					if (twoway > 0 && m.isTwoWay()) {
//						if (props.first == m.type) {
//							atk1 = Math.round(atk1 * Math.pow(1.5, twoway));
//							ptwoWay[i] = true;
//						}
//						if (props.second == m.type) {
//							atk2 = Math.round(atk2 * Math.pow(1.5, twoway));
//							stwoWay[i] = true;
//						}
//					}
//					if(combo7 > 0 && combos.size()>=7) {
//						if(props.first == m.type) {
//							atk1 = Math.round(atk1 * Math.pow(2.0, combo7));
//						}
//						if(props.second == m.type) {
//							atk2 = Math.round(atk2 * Math.pow(2.0, combo7));
//						}
//					}
//
//					memberBasicAtk.add(new Pair<Double, Double>(atk1, atk2));
//				}
//				normalAtk.add(memberBasicAtk);
//			}
//			// calculate real atk = atk * normal * combo * prop factor * leader
//			// factor
//			int size = normalAtk.size();
//			MonsterInfo leader = info.getMember(0);
//			MonsterInfo friend = info.getMember(5);
//
//			// assign leader skill factor & friend leader factor, if can't,
//			// assign check later = true
//			for (int i = 0; i < size; ++i) {
//				MonsterInfo minfo = info.getMember(i);
//				if (minfo == null) {
//					LogUtil.d("monster(", i, ") is null");
//					monsterAtk.add(new Pair<Damage, Damage>(Damage.zero(),
//							Damage.zero()));
//					continue;
//				}
//				List<Pair<Double, Double>> basicList = normalAtk.get(i);
//				Pair<Integer, Integer> props = minfo.getProps();
//
//				// * prop factor
//				SkillType stype = skillFired.getType();
//				List<Integer> data = skillFired.getData();
//				double typeFactor = 1.0;
//				double powerFactor1 = 1.0;
//				double powerFactor2 = 1.0;
//				if (stype == SkillType.ST_POWER_UP) {
//					int type = data.get(2);
//					int type1 = 999;
//					if (data.size() == 4) {
//						type1 = data.get(3);
//					}
//					double factor = data.get(1) / 100.0;
//					if (props.first == type || props.first == type1) {
//						powerFactor1 = factor;
//					}
//					if (props.second == type || props.second == type1) {
//						powerFactor2 = factor;
//					}
//				} else if (stype == SkillType.ST_TYPE_UP) {
//					Pair<MonsterType, MonsterType> monsterTypes = minfo
//							.getMonsterTypes();
//					MonsterType type3 = minfo.getMonsterType3();
//					for (int pos = 2; pos < data.size(); ++pos) {
//						int type = data.get(pos);
//						if (monsterTypes.first.ordinal() == type
//								|| monsterTypes.second.ordinal() == type
//								|| type3.ordinal() == type) {
//							typeFactor = data.get(1) / 100.0;
//							break;
//						}
//					}
//				}
//				// * leader factor
//				double f1 = getLeaderFactor(info, leader, minfo, combos, null);
//				double f2 = getLeaderFactor(info, friend, minfo, combos, null);
//				//LogUtil.d(i, " => factor=", f1, ", factor=", f2);
//				double totalf = f1 * f2;
//				double tcf1 = typeFactor * powerFactor1 * comboFactor;
//				double tcf2 = typeFactor * powerFactor2 * comboFactor;
//				double majoratk = 0.0;
//				double minoratk = 0.0;
//				for (Pair<Double, Double> basic : basicList) {
//
//					double atk1 = Math.ceil(basic.first * tcf1);
//					double atk2 = Math.ceil(basic.second * tcf2);
//
//					atk1 = Math.ceil(atk1 * totalf);
//					atk2 = Math.ceil(atk2 * totalf);
//
//					majoratk += atk1;
//					minoratk += atk2;
//				}
//				monsterAtk.add(new Pair<Damage, Damage>(new Damage(majoratk,
//						props.first, pattackAll[i], ptwoWay[i], psquare[i]), new Damage(
//						minoratk, props.second, sattackAll[i], stwoWay[i], ssquare[i])));
//			}
//
//		}
//
//		return monsterAtk;
//	}

	private static double calculateRecovery2(TeamInfo team, List<LeaderSkill> list,
										  List<Match> combos, Map<String, Object> settings,
											 double rcv) {
		double total = rcv;
		for(LeaderSkill lskill: list) {
			List<Integer> data = lskill.getData();
			switch (lskill.getType()) {
				case LST_MULTI_ORB_COMBO_MULTI: {
					int removeN = data.get(0);
					int size = data.get(1);

					Set<Integer> orbTypes = new HashSet<Integer>();
					for(int i=0; i<size; ++i) {
						orbTypes.add(data.get(2+i));
					}

					int maxRemove = 0;
					for (Match m : combos) {
						if (orbTypes.contains(m.type) && m.count > maxRemove) {
							maxRemove = m.count;
						}
					}
					double factor = 1.0;
					if (maxRemove >= removeN) {
						factor = data.get(2+size) / 100.0;
						int maxN = data.get(3+size);
						if (maxN != removeN) {
							if (maxRemove >= maxN) {
								factor = data.get(6+size) / 100.0;
							} else {
								double addfactor = data.get(5 + size) / 100.0;
								int diff = maxN - removeN;
								for (int i = 0; i <= diff; ++i) {
									if (maxRemove == removeN + i) {
										factor = factor + addfactor * i;
										break;
									}
								}
							}
						}
					}
					if(factor > 1.0) {
						float x = 1.0f;
						if(data.size()>7+size) {
							x = data.get(7+size)/100.0f;
						} else if(data.size()>4+size) {
							x = data.get(4+size)/100.0f;
						}
						if(x>1.0f) {
							//FIXME: for 3299 now, FIX while I have free time
							total *= factor;
						}
					}
				}
				break;
				case LST_MULTI_ORB_RCV: {
					//case LST_MULTI_ORB_COMBO: {
					int removeN = data.get(0);
					int orbType = data.get(1);

					int maxRemove = 0;
					for (Match m : combos) {
						if ((orbType == -1 || m.type == orbType) && m.count > maxRemove) {
							maxRemove = m.count;
						}
					}
					double factor = 1.0;
					if (maxRemove >= removeN) {
						factor = data.get(2) / 100.0;
						int maxN = data.get(3);
						if (maxN != removeN) {
							if (maxRemove >= maxN) {
								factor = data.get(6) / 100.0;
							} else {
								double addfactor = data.get(5) / 100.0;
								int diff = maxN - removeN;
								for (int i = 0; i <= diff; ++i) {
									if (maxRemove == removeN + i) {
										factor = factor + addfactor * i;
										break;
									}
								}
							}
						}
					}
					if(factor > 1.0) {
						if(factor > 1.0) {
							float x = 1.0f;
							if(data.size()>7) {
								x = data.get(7)/100.0f;
							} else if(data.size()>4) {
								x = data.get(4)/100.0f;
							}
							if(x>1.0f) {
								total *= x;
							}
						}
					}
				}
				break;
				case LST_COLOR_FACTOR: {
					boolean[] orbset = {false, false, false, false, false, false, false, false, false};
					boolean[] matchColor = {false, false, false, false, false, false, false, false, false};
					boolean[] memberColor = {false, false, false, false, false, false};
					int many = data.get(0);
					List<Boolean> bindStatus = null;
					if (settings != null && settings.containsKey("bindStatus")) {
						bindStatus = (List<Boolean>) settings.get("bindStatus");
					} else {
						bindStatus = new ArrayList<Boolean>();
						for (int i = 0; i < 6; ++i) {
							bindStatus.add(false);
						}
					}

					for (int i = 1; i <= many; ++i) {
						orbset[data.get(i)] = true;
					}
					int removeMany = data.get(many + 1);
					double factor = data.get(many + 2) / 100.0;

					for (Match m : combos) {
						if (m.type >= matchColor.length) {
							// we don't count the poison or jamar orbs
							//matchColor[m.type] = true;
							continue;
						}
						matchColor[m.type] = true;
					}
					// 6,1,4,5,3,0,2,3,30,6,4,35,5,40,6,45
					for (int i = 0; i < 6; ++i) {
						MonsterInfo mi = team.getMember(i);
						if (mi == null || bindStatus.get(i)) {
							continue;
						}
						Pair<Integer, Integer> props = mi.getProps();
						memberColor[props.first] = true;
						if (props.second != -1) {
							memberColor[props.second] = true;
						}
					}
					memberColor[2] = true;
					double[] factorArray = new double[7];
					for (int i = 0; i < removeMany; ++i) {
						factorArray[i] = 1.0;
					}

					int maxN = removeMany;
					int addition = data.get(many + 3);
					int index = 4 + many;
					if (addition > 0) {
						factorArray[removeMany] = factor;
						for (int i = 0; i < addition; i += 2) {
							int remove = data.get(index + i);
							double f = data.get(index + i + 1) / 100.0;
							factorArray[remove] = f;
							if (i == addition - 2) {
								maxN = remove;
							}
						}
					} else {
						for (int i = removeMany; i < 7; ++i) {
							factorArray[i] = factor;
						}
					}

					int conditionCount = 0;
					for (int i = 0; i < 6; ++i) {
						if (orbset[i] && matchColor[i] && memberColor[i]) {
							++conditionCount;
						}
					}
					for(int i=6; i<=8; ++i) {
						if(orbset[i] && matchColor[i]) {
							++conditionCount;
						}
					}
					if (conditionCount > maxN) {
						conditionCount = maxN;
					}

					if(factorArray[conditionCount]>1.0 && data.size()>index+addition) {
						int rcvFactor = data.get(index+addition);
						if(rcvFactor != 0) {
							total *= (rcvFactor / 100.0);
						}
					}
				}
			}
		}
		return total;
	}

	public static double getEnhancedRecovery(List<LeaderSkill> skills,
			double rcv, boolean skillFired, boolean multiMode, List<? extends Match> combos) {
		
		for(LeaderSkill skill : skills) {
			List<Integer> data = skill.getData();
			switch (skill.getType()) {
				case LST_COMBO_RCV: {
					SparseArray<Double> comboFactor = new SparseArray<Double>();
					int size = data.get(0);
					int min = 99;
					int max = 0;
					for (int i = 0; i < size; ++i) {
						int combo = data.get(i * 2 + 1);
						double factor = data.get(i * 2 + 2) / 100.0;
						comboFactor.put(combo, factor);
						if (min > combo) {
							min = combo;
						}
						if (max < combo) {
							max = combo;
						}
					}
					int combosize = combos.size();
					double hasFactor = comboFactor.get(combosize, 0.0);
					if (hasFactor != 0.0) {
						return hasFactor * rcv;
					} else if (combosize > max) {
						return comboFactor.get(max) * rcv;
					}
					return rcv;
				}
			case LST_COUNT_ORB: {
				int count = data.get(0);
				int orb = data.get(1);
				boolean fired = false;
				
				for(Match match : combos) {
					if(match.type == orb && match.count == count) {
						fired = true;
						break;
					}
				}
				
	            int powerlen = data.get(2);
				int start = 3;
				
				float factor = 1.0f;
				if(fired) {
					for (int i = 0; i < powerlen; ++i) {
						int offset = start + i * 2;
						int which = data.get(offset);
						if (which == 2) { // hp-0, atk-1, rcv-2
							factor = data.get(offset + 1) / 100.0f;
						}
					}
				}
				return Math.floor(rcv * factor);
			}
			case LST_MULTI_PLAY: {
				if (multiMode) {
			        int len = data.get(0);
	
		            int powerlen = data.get(len + 1);
					int start = len + 2;
					for (int i = 0; i < powerlen; ++i) {
						int offset = start + i * 2;
						int which = data.get(offset);
						if (which == 2) { // hp-0, atk-1, rcv-2
							float factor = data.get(offset + 1) / 100.0f;
							return Math.floor(rcv * factor);
						}
					}
				}
				break;
			}
			case LST_USED_SKILL: {
				if(skillFired) {
					int len = data.get(0);
					int powerlen = data.get(len + 1);
					int start = len + 2;
					for (int i = 0; i < powerlen; ++i) {
						int offset = start + i * 2;
						int which = data.get(offset);
						if (which == 2) { // hp-0, atk-1, rcv-2
							float factor = data.get(offset + 1) / 100.0f;
							return Math.floor(rcv * factor);
						}
					}
				}
				break;
			}
			default:
				
			}
		}
		return rcv;
	}
	
	private static int getEnhancedPower(LeaderSkill skill, MonsterInfo monster,
			int hp, int power, boolean multiMode) {
		List<Integer> data = skill.getData();
		switch (skill.getType()) {
		case LST_MULTI_PLAY: {
			if (multiMode) {
		        int len = data.get(0);
	            List<Integer> type = data.subList(1, len + 1);

	            boolean isSameType = isSameType(type, monster);
	            if (isSameType) {
					int powerlen = data.get(len + 1);
					int start = len + 2;
					for (int i = 0; i < powerlen; ++i) {
						int offset = start + i * 2;
						int which = data.get(offset);
						if (which == power) { // hp-0, atk-1, rcv-2
							float factor = data.get(offset + 1) / 100.0f;
							return (int) Math.floor(hp * factor);
						}
					}
	            }
			}
			break;
		}
		case LST_FACTOR: {
			int len = data.get(0);
			List<Integer> type = data.subList(1, len + 1);

			if (isSameType(type, monster)) {
				int powerlen = data.get(len + 1);
				int start = len + 2;
				for (int i = 0; i < powerlen; ++i) {
					int offset = start + i * 2;
					int which = data.get(offset);
					if (which == power) { // hp-0, atk-1, rcv-2
						float factor = data.get(offset + 1) / 100.0f;
						return (int) Math.floor(hp * factor);
					}
				}
			}
			break;
		}
		case LST_HP_FACTOR: {
			int len = data.get(2);
			List<Integer> type = data.subList(3, len + 3);

			if (isSameType(type, monster)) {
				int powerlen = data.get(len + 3);
				int start = len + 2 + 2;
				for (int i = 0; i < powerlen; ++i) {
					int offset = start + i * 2;
					if (data.size() <= offset) {
						break;
					}
					int which = data.get(offset);
					if (which == power) { // hp
						float factor = data.get(offset + 1) / 100.0f;
						return (int) Math.floor(hp * factor);
					}
				}
			}
			break;
		}
		default:
			break;

		}
		return hp;
	}

	public static int getEnhancedPower(MonsterInfo leader, MonsterInfo monster,
			int hp, int power, boolean multiMode) {
		if (leader != null) {
			List<LeaderSkill> list = leader.getLeaderSkill();
			for (LeaderSkill ls : list) {
				int newhp = getEnhancedPower(ls, monster, hp, power, multiMode);
				if (newhp != hp) {
					return newhp;
				}
			}
		}
		return hp;
	}

	public static double calculateRecovery(TeamInfo info, List<Match> combos,
			ActiveSkill skillFired, Map<String, Object> settings) {
		if (skillFired == null) {
			skillFired = ActiveSkill.empty();
		}
		
		boolean nullAwoken = false;
		if(settings != null && settings.containsKey("nullAwoken")) {
			nullAwoken = (Boolean)settings.get("nullAwoken");
		}
		boolean fired = false;
		if(settings != null && settings.containsKey("skillFired")) {
			fired = (Boolean)settings.get("skillFired");
		}
		boolean copMode = false;
		if(settings != null && settings.containsKey("copMode")) {
			copMode = (Boolean)settings.get("copMode");
		}
		
		double comboFactor = 1 + (0.25 * (combos.size() - 1));
		int recovery = info.getRecovery();
		if(settings != null && settings.containsKey("singleUp")) {
			 int singleUp = (Integer)settings.get("singleUp");
			 if(singleUp == SinglePowerUp.RCV.value()) {
				 recovery = (int)(recovery*1.25);
			 } else if(singleUp == SinglePowerUp.RCV_UP.value()) {
				 recovery = (int)(recovery*1.35);
			 }
		}
		double total = 0.0;
		int[] addition = {0, 0, 0, 0, 0, 0};
		boolean recoverUp = false;
		int heartPlusCnt = 0;
		if (combos != null) {
            for(int i=0; i<6; ++i) {
                MonsterInfo minfo = info.getMember(i);
                if (!nullAwoken && minfo != null) {
                	int enhanceHeart = minfo.getTargetAwokenCount(AwokenSkill.ENHANCE_HEART, copMode);
                    heartPlusCnt += enhanceHeart;
					addition[i] = enhanceHeart;
                }
            }
		    
			for (Match m : combos) {
				if (m.type != 2) {
					continue;
				}
				double c = (1.0 + (.25 * (m.count - 3)));
				if ( m.pcount > 0 ) {
					c *= (1.0 + 0.06 * m.pcount) * (1.0 + 0.05 * heartPlusCnt);
				}
				if ( m.count == 4 ) {
					recoverUp = true;
				}
				total += Math.ceil(recovery * c);
			}
		}
		double moreRecover = 0.0;
		if (recoverUp ) {
			for(int i=0; i<6; ++i) {
				if (addition[i] > 0) {
					int rcv = info.getMember(i).getRecovery(copMode);
					moreRecover += rcv * (Math.pow(1.5, addition[i]) - 1.0) * 1.25 * (1.0 + 0.05 * heartPlusCnt) * (1.0 + 0.06 * 4);
				}
			}
		}
		double calculated = total * comboFactor + moreRecover * comboFactor;
		if (calculated < 0.0) {
			calculated = 0.0;
		}

		if(skillFired.getType() == SkillType.ST_RECOVER_UP) {
			calculated *= (skillFired.getData().get(1) / 100.0);
		}
		
		MonsterInfo leader = info.getMember(0);
		MonsterInfo friend = info.getMember(5);
		if(leader != null) {
			List<LeaderSkill> list = leader.getLeaderSkill();
			calculated = getEnhancedRecovery(list, calculated, fired, copMode, combos);
			calculated = calculateRecovery2(info, list, combos, settings, calculated);
		}
		if(friend != null) {
			List<LeaderSkill> list = friend.getLeaderSkill();
			calculated = getEnhancedRecovery(list, calculated, fired, copMode, combos);
			calculated = calculateRecovery2(info, list, combos, settings, calculated);
		}
		
		return Math.floor(calculated);
	}

	// for multiple mode
    public static double calculatePoison(int totalHp,
            List<Match> mScoreBoard, ActiveSkill active) {
        int hp = totalHp;
        int totalPercent = 0;
        double totalEnhancedPercent = 0;
        for(Match m : mScoreBoard) {
            if ( m.type == 6 ) { // poison
                int percent = 20 + (m.count - 3) * 5;
                totalPercent += percent;
            } else if ( m.type == 8 ) { // enchanced poison
                double percent = 50 + (m.count - 3) * 12.5;
                totalEnhancedPercent += percent;            	
            }
        }
        return Math.ceil(hp * totalPercent / 100.0) + Math.ceil(hp * totalEnhancedPercent / 100.0);
    }
	
    public static double calculatePoison(TeamInfo mTeam,
            List<Match> mScoreBoard, ActiveSkill active) {
        int hp = mTeam.getHp();
        int totalPercent = 0;
        double totalEnhancedPercent = 0;
        for(Match m : mScoreBoard) {
            if ( m.type == 6 ) { // poison
                int percent = 20 + (m.count - 3) * 5;
                totalPercent += percent;
            } else if ( m.type == 8 ) { // enchanced poison
                double percent = 50 + (m.count - 3) * 12.5;
                totalEnhancedPercent += percent;            	
            }
        }
        return Math.ceil(hp * totalPercent / 100.0) + Math.ceil(hp * totalEnhancedPercent / 100.0);
    }

	public static double getActiveSkillMultiplier(ActiveSkill skillFired, MonsterInfo minfo) {
		SkillType stype = skillFired.getType();
		List<Integer> data = skillFired.getData();
		if (stype == SkillType.ST_POWER_UP) {
			return 2.0;
		} else if (stype == SkillType.ST_TYPE_UP) {
			Pair<MonsterType, MonsterType> monsterTypes = minfo
					.getMonsterTypes();
			MonsterType type3 = minfo.getMonsterType3();
			for (int pos = 2; pos < data.size(); ++pos) {
				int type = data.get(pos);
				if (monsterTypes.first.ordinal() == type
						|| monsterTypes.second.ordinal() == type
						|| type3.ordinal() == type) {
					return data.get(1) / 100.0;
				}
			}
		}
		return 1.0;
	}
	
	public static double[] getActiveSkillMultiplier(ActiveSkill skillFired) {
        // * prop factor
	    double[] result = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
	    if ( skillFired == null ) {
	        return result;
	    }
        SkillType stype = skillFired.getType();
        List<Integer> data = skillFired.getData();

        if (stype == SkillType.ST_POWER_UP) {
            double factor = data.get(1) / 100.0;
            int type = data.get(2);
            result[type+1] = factor;
            
            int type1 = 999;
            if (data.size() == 4) {
                type1 = data.get(3);
                result[type1+1] = factor;
            }
        } else if (stype == SkillType.ST_TYPE_UP) {
            result[0] = data.get(1) / 100.0;
        }
        return result;
	}

	public static class AttackValue {
	    public static final int ATTACK_NONE = 0;
	    public static final int ATTACK_SINGLE = 1;
	    public static final int ATTACK_MULTIPLE = 2;
	    public static final int ATTACK_TWO = 3;
	    
	    public double damage;
	    public int prop;
	    public int attackType;

		public int guardBreak = 0;
	    public int squareAttack = 0;

	    public AttackValue() {
	        damage = .0;
	        prop = -1;
	        attackType = ATTACK_NONE;
        }
	    
	    public AttackValue(double damage, int type, int attackAll) {
	        this.damage = damage;
	        this.prop = type;
	        this.attackType = attackAll;
	    }
	    
	    public static AttackValue create(double damage, int type, int attackAll) {
	        return new AttackValue(damage, type, attackAll);
	    }

		public AttackValue setGuardBreak(int gb) {
			guardBreak = gb;
			return this;
		}

		public AttackValue setSquareAttack(int sa) {
			squareAttack = sa;
			return this;
		}
	}

    public static List<Pair<AttackValue, AttackValue>> calculateDamage(TeamInfo info,
            List<Match> combos, ActiveSkill skillFired, int defence,
            Map<String, Object> settings) {
        boolean nullAwoken = false;
        int swapIndex = 0;
        List<Boolean> bindStatus = null;
        int gameMode = info.gameMode;
        boolean challengeMode = gameMode == Constants.MODE_CHALLENGE;
//        int reduceByCross = 0;
        float crossAttack = 1.0f;
        boolean isCopMode = false;
        double atkPowerUp = 1.0;
        boolean isAttackAll = false;
        float hpPercent = 100;
        if ( settings != null ) {
            if (settings.containsKey("swapIndex")) {
                swapIndex = (Integer)settings.get("swapIndex");
            }
            if (settings.containsKey("bindStatus")) {
                bindStatus = (List<Boolean>)settings.get("bindStatus");
            }
            if (settings.containsKey("nullAwoken")) {
                nullAwoken = (Boolean)settings.get("nullAwoken");
            }
//	        if (settings.containsKey("cross")) {
//	        	reduceByCross = (Integer) settings.get("cross");
//	        }
	        if (settings.containsKey("crossAtk")) {
	        	crossAttack = (Float) settings.get("crossAtk");
	        }
	        if(settings.containsKey("copMode")) {
	        	isCopMode = (Boolean) settings.get("copMode");
	        }
            if(settings.containsKey("hp")) {
                hpPercent = (Float)settings.get("hp");
            }
	        if(settings.containsKey("singleUp")) {
	        	int up = (Integer)settings.get("singleUp");
	        	//atkPowerUp = !isCopMode && up == SinglePowerUp.ATK.value();
	        	if(!isCopMode) {
	        		if(up == SinglePowerUp.ATK.value()) {
	        			atkPowerUp = 1.05;
	        		} else if(up == SinglePowerUp.ATK_UP.value()) {
	        			atkPowerUp = 1.15;
	        		}
	        	}
	        	isAttackAll = up == SinglePowerUp.ATTACK_ALL.value();
	        }
        }
        if ( bindStatus == null ) {
            bindStatus = new ArrayList<Boolean>();
            for(int i=0; i<6; ++i) {
                bindStatus.add(false);
            }
        }
        //boolean challengeMode, boolean[] bindStatus, int swapIndex
        if (skillFired == null) {
            skillFired = ActiveSkill.empty();
        }
        List<Pair<AttackValue, AttackValue>> monsterAtk = new ArrayList<Pair<AttackValue, AttackValue>>();
        if (combos != null) {
            double comboFactor = 1 + (0.25 * (combos.size() - 1));
            // one attack skill count
            int[] rowSkill = { 0, 0, 0, 0, 0, 0 };
            int[] plusOSkill = { 0, 0, 0, 0, 0, 0 };
            for (int i = 0; i < 6; ++i) {
                MonsterInfo minfo = info.getMember(i);
                if (minfo == null || bindStatus.get(i) || nullAwoken) {
                    continue;
                }
                rowSkill[0] += minfo
                        .getTargetAwokenCount(AwokenSkill.ENHANCE_DARK_ATT, isCopMode);
                rowSkill[1] += minfo
                        .getTargetAwokenCount(AwokenSkill.ENHANCE_FIRE_ATT, isCopMode);
                // rowSkill[2] +=
                // minfo.getTargetAwokenCount(AwokenSkill.ENHANCE_HEAL);
                rowSkill[3] += minfo
                        .getTargetAwokenCount(AwokenSkill.ENHANCE_LIGHT_ATT, isCopMode);
                rowSkill[4] += minfo
                        .getTargetAwokenCount(AwokenSkill.ENHANCE_WATER_ATT, isCopMode);
                rowSkill[5] += minfo
                        .getTargetAwokenCount(AwokenSkill.ENHANCE_WOOD_ATT, isCopMode);

                plusOSkill[0] += minfo
                        .getTargetAwokenCount(AwokenSkill.ENHANCE_DARK, isCopMode);
                plusOSkill[1] += minfo
                        .getTargetAwokenCount(AwokenSkill.ENHANCE_FIRE, isCopMode);
                plusOSkill[3] += minfo
                        .getTargetAwokenCount(AwokenSkill.ENHANCE_LIGHT, isCopMode);
                plusOSkill[4] += minfo
                        .getTargetAwokenCount(AwokenSkill.ENHANCE_WATER, isCopMode);
                plusOSkill[5] += minfo
                        .getTargetAwokenCount(AwokenSkill.ENHANCE_WOOD, isCopMode);
            }
            

            // calculate normal attack - atk * (rm orb factor) * (combo factor)
            List<List<Pair<Double, Double>>> normalAtk = new ArrayList<List<Pair<Double, Double>>>();
            for (int i = 0; i < 6; ++i) {
                MonsterInfo minfo = info.getMember(i);
                if (minfo == null || bindStatus.get(i)) {
                    normalAtk.add(new ArrayList<Pair<Double, Double>>());
                    continue;
                }

                int twoway = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.TWO_PRONGED_ATTACK, isCopMode);
				int combo7 = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.COMBO_7, isCopMode);
				int combo10 = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.COMBO_10, isCopMode);
				int lattack = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.L_ATTACK, isCopMode);
                int hp80 = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.ATK_UP_80, isCopMode);
                int hp50 = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.ATK_UP_50, isCopMode);
                int heartSquare = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.HEART_SQUARE, isCopMode);

                Pair<Integer, Integer> props = minfo.getProps();
                int atk = minfo.getAtk(isCopMode);
                List<Pair<Double, Double>> memberBasicAtk = new ArrayList<Pair<Double, Double>>();
				int square = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.SQUARE_ATTACK, isCopMode);
				boolean hasHeartSquare = false;

                for (Match m : combos) {
                    if (props.first != m.type && props.second != m.type) {
                        continue;
                    }

                    double damage = (1.0 + (.25 * (m.count - 3)));
                    if (m.pcount > 0) {
                        damage *= (1.0 + 0.06 * m.pcount)
                                * (1.0 + 0.05 * plusOSkill[m.type]);
                    }
//                  if (m.isOneRow() && rowSkill[m.type] > 0) {
//                      damage *= (1.0 + 0.1 * rowSkill[m.type] * rowcount[m.type]);
//                  }
                    double atk1 = 0.0;
                    double atk2 = 0.0;
                    double damageAtk = damage * atk;

                    if (props.first == m.type) {
                        atk1 = Math.ceil(damageAtk);
                    }
                    if (props.second == m.type) {
                        double calc;
                        if (props.second == props.first) {
                            calc = Math.ceil(damageAtk * 0.1);
                        } else {
                            calc = Math.ceil(damageAtk / 3.0);
                        }

                        atk2 = Math.ceil(calc);
                    }
					if (!nullAwoken && square > 0 && m.isSquare()) {
						if (props.first == m.type) {
							atk1 = Math.round(atk1 * Math.pow(2.5, square));
						}
						if (props.second == m.type) {
							atk2 = Math.round(atk2 * Math.pow(2.5, square));
						}
						if(m.type == 2 && heartSquare > 0) {
							hasHeartSquare = true;
						}
					}

					if(!nullAwoken && lattack > 0 && m.isLFormat()) {
						if (props.first == m.type) {
							atk1 = Math.round(atk1 * Math.pow(1.5, lattack));
						}
						if (props.second == m.type) {
							atk2 = Math.round(atk2 * Math.pow(1.5, lattack));
						}
					}

                    // not only major prop will be enhanced
                    if (!nullAwoken && twoway > 0 && m.isTwoWay()) {
                        if (props.first == m.type) {
                            atk1 = Math.round(atk1 * Math.pow(1.5, twoway));
                        }
                        if (props.second == m.type) {
                            atk2 = Math.round(atk2 * Math.pow(1.5, twoway));
                        }
                    }
					if(!nullAwoken && combo7 > 0 && combos.size()>=7) {
						if(props.first == m.type) {
							atk1 = Math.round(atk1 * Math.pow(2.0, combo7));
						}
						if(props.second == m.type) {
							atk2 = Math.round(atk2 * Math.pow(2.0, combo7));
						}
					}
					if(!nullAwoken && combo10 > 0 && combos.size()>=10) {
						if(props.first == m.type) {
							atk1 = Math.round(atk1 * Math.pow(5.0, combo10));
						}
						if(props.second == m.type) {
							atk2 = Math.round(atk2 * Math.pow(5.0, combo10));
						}
					}
                    if(!nullAwoken && hp80>0 && hpPercent>=80) {
                        if(props.first == m.type) {
                            atk1 = Math.round(atk1 * Math.pow(1.5, hp80));
                        }
                        if(props.second == m.type) {
                            atk2 = Math.round(atk2 * Math.pow(1.5, hp80));
                        }
                    }
                    if(!nullAwoken && hp50>0 && hpPercent<=50) {
                        if(props.first == m.type) {
                            atk1 = Math.round(atk1 * Math.pow(2.0, hp50));
                        }
                        if(props.second == m.type) {
                            atk2 = Math.round(atk2 * Math.pow(2.0, hp50));
                        }
                    }

                    memberBasicAtk.add(new Pair<Double, Double>(atk1, atk2));
                }
                if(hasHeartSquare) {
                	double atkUp = Math.pow(2.0, heartSquare);
					List<Pair<Double, Double>> powerUpAtk = new ArrayList<Pair<Double, Double>>();
					for(Pair<Double, Double> pair : powerUpAtk) {
						powerUpAtk.add(new Pair<Double, Double>(pair.first*atkUp, pair.second*atkUp));
					}
					normalAtk.add(powerUpAtk);
				} else {
					normalAtk.add(memberBasicAtk);
				}
            }
            // calculate real atk = atk * normal * combo * prop factor * leader
            // factor
            int size = normalAtk.size();
            MonsterInfo leader = info.getMember(swapIndex);
            MonsterInfo friend = info.getMember(5);

            int[] rowcount = { 0, 0, 0, 0, 0, 0, 0, 0, 0,0 };
            for (Match m : combos) {
                if (m.isOneRow()) {
                    ++rowcount[m.type];
                }
            }
            
            // new leader skill, attr. power up if %d orbs connected and include + orb
            double[] attr1Factor = null;
            double[] attr2Factor = null;
            if ( !bindStatus.get(0) ) {
                attr1Factor = getLeaderFactorAttrUp(info, leader, combos);
            }
            if (!(challengeMode || bindStatus.get(5))) {
                attr2Factor = getLeaderFactorAttrUp(info, friend, combos);
            }
            if ( attr1Factor == null ) {
                attr1Factor = new double[]{1.0,1.0,1.0,1.0,1.0,1.0};
            }
            if ( attr2Factor == null ) {
                attr2Factor = new double[]{1.0,1.0,1.0,1.0,1.0,1.0};
            }
            
			// assign leader skill factor & friend leader factor, if can't,
			// assign check later = true
			SkillType stype = skillFired.getType();
			List<Integer> data = skillFired.getData();
			float awokenUp = 1.0f;
			if(stype==SkillType.ST_POWER_UP_BY_AWOKEN) {
				int awokenSize = data.get(2);
				int total = 0;
				for (int i = 0; i < size; ++i) {
					MonsterInfo minfo = info.getMember(i);
					if (minfo == null || bindStatus.get(i)) {
						LogUtil.d("monster(", i, ") is null");
						//monsterAtk.add(new Pair<AttackValue, AttackValue>(new AttackValue(), new AttackValue()));
						continue;
					}
					if(!nullAwoken) {
						for(int j=0; j<awokenSize; ++j) {
							AwokenSkill skill = AwokenSkill.get(data.get(3+j));
							total += minfo.getTargetAwokenCount(skill, isCopMode);
							if(skill == AwokenSkill.SKILL_BOOST) {
								total += minfo.getTargetAwokenCount(AwokenSkill.SKILL_BOOST_PLUS, isCopMode) * 2;
							} else if(skill == AwokenSkill.EXTEND_TIME) {
								total += minfo.getTargetAwokenCount(AwokenSkill.EXTEND_TIME_PLUS, isCopMode) * 2;
							}
						}
					}
				}
				awokenUp = (total * data.get(1) + 100) / 100.0f; // percent
			}
            
            // assign leader skill factor & friend leader factor, if can't,
            // assign check later = true
			boolean[] firedAttr = {false, false, true, false, false, false};
			boolean[] checkAttr = {false, false, false, false, false, false, false, false, false};
			for(int i=0; i<size; ++i) {
				MonsterInfo minfo = info.getMember(i);
				if (minfo == null || bindStatus.get(i)) {
					continue;
				}
				Pair<Integer, Integer> props = minfo.getProps();
				checkAttr[props.first] = true;
				if(props.second>=0) {
					checkAttr[props.second] = true;
				}
			}
			boolean[] isSquare = {false, false, false, false, false, false, false, false, false, false};
			for (Match m : combos) {
				if(m.type < 9 && m.type >= 0 && checkAttr[m.type]) {
					firedAttr[m.type] = true;
				}
				if(m.isSquare()) {
					isSquare[m.type%10] = true;
				}
			}
			boolean fiveColors = true;
			for(int x=0; x<firedAttr.length; ++x) {
				if(!firedAttr[x]) {
					fiveColors = false;
					break;
				}
			}

            for (int i = 0; i < size; ++i) {
                MonsterInfo minfo = info.getMember(i);
                if (minfo == null || bindStatus.get(i)) {
                    LogUtil.d("monster(", i, ") is null");
                    monsterAtk.add(new Pair<AttackValue, AttackValue>(new AttackValue(), new AttackValue()));
                    continue;
                }

                List<Pair<Double, Double>> basicList = normalAtk.get(i);
                Pair<Integer, Integer> props = minfo.getProps();

                // calculate row enhanced power factor
                double rowPower1 = 1.0;
                double rowPower2 = 1.0;
                if ( rowSkill[props.first] > 0 ) {
                    rowPower1 = (1.0 + 0.1 * rowSkill[props.first] * rowcount[props.first]);
                }
                if ( props.second != -1 && rowSkill[props.second] > 0 ) {
                    rowPower2 = (1.0 + 0.1 * rowSkill[props.second] * rowcount[props.second]);
                }
                
                // * prop factor
                double typeFactor = 1.0;
                double powerFactor1 = 1.0;
                double powerFactor2 = 1.0;
                if (stype == SkillType.ST_POWER_UP) {
                    int type = data.get(2);
                    int type1 = 999;
                    if (data.size() == 4) {
                        type1 = data.get(3);
                    }
                    double factor = data.get(1) / 100.0;
                    if (props.first == type || props.first == type1) {
                        powerFactor1 = factor;
                    }
                    if (props.second == type || props.second == type1) {
                        powerFactor2 = factor;
                    }
                } else if (stype == SkillType.ST_TYPE_UP) {
                    Pair<MonsterType, MonsterType> monsterTypes = minfo
                            .getMonsterTypes();
                    MonsterType type3 = minfo.getMonsterType3();
                    for (int pos = 2; pos < data.size(); ++pos) {
                        int type = data.get(pos);
                        if (monsterTypes.first.ordinal() == type
                                || monsterTypes.second.ordinal() == type
                                || type3.ordinal() == type) {
                            typeFactor = data.get(1) / 100.0;
                            break;
                        }
                    }
                }
                
                powerFactor1 *= (attr1Factor[props.first] * attr2Factor[props.first]);
                if ( props.second != -1 ) {
                    powerFactor2 *= (attr1Factor[props.second] * attr2Factor[props.second]);
                }
                
                // * leader factor
                double f1 = 1.0;
                double f2 = 1.0;
                
                if ( !bindStatus.get(0) ) {
                    f1 = getLeaderFactor(info, leader, minfo, combos, settings);
                }
                if (!(challengeMode || bindStatus.get(5))) {
                    f2 = getLeaderFactor(info, friend, minfo, combos, settings);
                }


                //LogUtil.d(i, " => factor=", f1, ", factor=", f2);
                double totalf = f1 * f2 * crossAttack;
                double tcf1 = typeFactor * powerFactor1 * comboFactor * awokenUp;
                double tcf2 = typeFactor * powerFactor2 * comboFactor * awokenUp;
                double majoratk = 0.0;
                double minoratk = 0.0;
                for (Pair<Double, Double> basic : basicList) {

                    double atk1 = Math.floor(basic.first * tcf1);
                    double atk2 = Math.floor(basic.second * tcf2);
                    
                    atk1 = Math.ceil(Math.ceil(atk1 * rowPower1) * totalf);
                    atk2 = Math.ceil(Math.ceil(atk2 * rowPower2) * totalf);

                    majoratk += atk1;
                    minoratk += atk2;
                }
                // check again? will it cost a lot?
                int attackType1 = AttackValue.ATTACK_NONE;
                int attackType2 = AttackValue.ATTACK_NONE;
                if (majoratk > 0.0 || minoratk > 0.0) {
                    for (Match m : combos) {
                        if (props.first != m.type && props.second != m.type) {
                            continue;
                        }

                        int type = AttackValue.ATTACK_SINGLE;
                        if(isAttackAll) {
                        	type = AttackValue.ATTACK_MULTIPLE;
                        } else if (m.isTwoWay()) {
                            type = AttackValue.ATTACK_TWO;
                        } else if (m.count >= 5) {
                            type = AttackValue.ATTACK_MULTIPLE;
                        }
                        if (props.first == m.type) {
                            attackType1 = type;
                        }
                        if (props.second == m.type) {
                            attackType2 = type;
                        }
                    }
                }

				majoratk = (int)(majoratk * atkPowerUp);
				minoratk = (int)(minoratk * atkPowerUp);

				AttackValue attack1 = AttackValue.create(majoratk, props.first, attackType1);
				AttackValue attack2 = AttackValue.create(minoratk, props.second, attackType2);
				int gbPercent = 100 * ((nullAwoken)? 0:minfo.getTargetAwokenCount(AwokenSkill.GUARD_BREAK, isCopMode));
				if(gbPercent > 0 && fiveColors) {
					attack1.setGuardBreak(gbPercent);
					attack2.setGuardBreak(gbPercent);
				}

				int square = nullAwoken ? 0:minfo.getTargetAwokenCount(AwokenSkill.SQUARE_ATTACK, isCopMode);
				if(square > 0) {
					if(isSquare[props.first]) {
						attack1.setSquareAttack(1);
					}
					if(props.second != -1 && isSquare[props.second]) {
						attack2.setSquareAttack(1);
					}
				}

                monsterAtk.add(new Pair<AttackValue, AttackValue>(
                        attack1,
                        attack2));
//                monsterAtk.add(new Pair<AttackValue, AttackValue>(majoratk, minoratk));
            }

        }

        return monsterAtk;
    }
	
	public static List<Pair<Double, Double>> calculate(TeamInfo info,
			List<Match> combos, ActiveSkill skillFired, int defence, 
			Map<String, Object> settings) {
	    
	    boolean nullAwoken = false;
	    int swapIndex = 0;
	    boolean[] powerUp = null;
	    List<Boolean> bindStatus = null;
	    int gameMode = info.gameMode;
	    boolean challengeMode = (gameMode == Constants.MODE_CHALLENGE);
	    float reduceByCross = 0;
	    float crossAttack = 1f;
	    boolean copMode = false;
	    double singleAtkUp = 1.0;
        float hpPercent = 100;
	    
	    if ( settings != null ) {
	        if (settings.containsKey("swapIndex")) {
	            swapIndex = (Integer)settings.get("swapIndex");
	        }
	        if (settings.containsKey("bindStatus")) {
	            bindStatus = (List<Boolean>)settings.get("bindStatus");
	        }
	        if (settings.containsKey("nullAwoken")) {
	            nullAwoken = (Boolean)settings.get("nullAwoken");
	        }
	        if (settings.containsKey("powerUp")) {
	        	powerUp = (boolean[])settings.get("powerUp");
	        }
	        if (settings.containsKey("cross")) {
	        	reduceByCross = (Float) settings.get("cross");
	        }
	        if (settings.containsKey("crossAtk")) {
	        	crossAttack = (Float) settings.get("crossAtk");
	        }
	        if(settings.containsKey("copMode")) {
	        	copMode = ComboMasterApplication.getsInstance().isCopMode();//(Boolean) settings.get("copMode");
	        }
            if(settings.containsKey("hp")) {
                hpPercent = (Float)settings.get("hp");
            }

	        if(settings.containsKey("singleUp")) {
				int up = (Integer)settings.get("singleUp");
				if(up == SinglePowerUp.ATK.value()) {
					singleAtkUp = 1.05;
				} else if(up == SinglePowerUp.ATK_UP.value()) {
					singleAtkUp = 1.15;
				}
	        }
	    }
	    if ( bindStatus == null ) {
	        bindStatus = new ArrayList<Boolean>();
	        for(int i=0; i<6; ++i) {
	            bindStatus.add(false);
	        }
	    }
	    //boolean challengeMode, boolean[] bindStatus, int swapIndex
		if (skillFired == null) {
			skillFired = ActiveSkill.empty();
		}
		List<Pair<Double, Double>> monsterAtk = new ArrayList<Pair<Double, Double>>();
		if (combos != null) {
			double comboFactor = 1 + (0.25 * (combos.size() - 1));
			// one attack skill count
			int[] rowSkill = { 0, 0, 0, 0, 0, 0 };
			int[] plusOSkill = { 0, 0, 0, 0, 0, 0 };
			for (int i = 0; i < 6; ++i) {
				MonsterInfo minfo = info.getMember(i);
				if (minfo == null || bindStatus.get(i) || nullAwoken) {
					continue;
				}
				rowSkill[0] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_DARK_ATT, copMode);
				rowSkill[1] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_FIRE_ATT, copMode);
				//rowSkill[2] +=
				//         minfo.getTargetAwokenCount(AwokenSkill.ENHANCE_HEART);
				rowSkill[3] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_LIGHT_ATT, copMode);
				rowSkill[4] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_WATER_ATT, copMode);
				rowSkill[5] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_WOOD_ATT, copMode);

				plusOSkill[0] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_DARK, copMode);
				plusOSkill[1] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_FIRE, copMode);
//                plusOSkill[2] += minfo
//                        .getTargetAwokenCount(AwokenSkill.ENHANCE_HEART);
				plusOSkill[3] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_LIGHT, copMode);
				plusOSkill[4] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_WATER, copMode);
				plusOSkill[5] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_WOOD, copMode);
			}
			

			// calculate normal attack - atk * (rm orb factor) * (combo factor)
			List<List<Pair<Double, Double>>> normalAtk = new ArrayList<List<Pair<Double, Double>>>();
			for (int i = 0; i < 6; ++i) {
				MonsterInfo minfo = info.getMember(i);
				if (minfo == null || bindStatus.get(i)) {
					normalAtk.add(new ArrayList<Pair<Double, Double>>());
					continue;
				}
				int square = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.SQUARE_ATTACK, copMode);
				int twoway = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.TWO_PRONGED_ATTACK, copMode);
				int combo7 = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.COMBO_7, copMode);
				int combo10 = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.COMBO_10, copMode);
				int lattack = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.L_ATTACK, copMode);
                int hp80 = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.ATK_UP_80, copMode);
                int hp50 = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.ATK_UP_50, copMode);
                int heartSquare = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.HEART_SQUARE, copMode);
				boolean hasHeartSquare = false;

				//Log.e("Vincent", "twoway c=" + twoway);
				Pair<Integer, Integer> props = minfo.getProps();
				int atk = minfo.getAtk(copMode); //gameMode==Constants.MODE_MULTIPLE
				List<Pair<Double, Double>> memberBasicAtk = new ArrayList<Pair<Double, Double>>();

				for (Match m : combos) {
					if (props.first != m.type && props.second != m.type) {
						continue;
					}

					double damage = (1.0 + (.25 * (m.count - 3)));
					if (m.pcount > 0) {
						damage *= (1.0 + 0.06 * m.pcount)
								* (1.0 + 0.05 * plusOSkill[m.type]);
					}
//					if (m.isOneRow() && rowSkill[m.type] > 0) {
//						damage *= (1.0 + 0.1 * rowSkill[m.type] * rowcount[m.type]);
//					}
					double atk1 = 0.0;
					double atk2 = 0.0;
					double damageAtk = damage * atk;

					if (props.first == m.type) {
						atk1 = Math.ceil(damageAtk);
					}
					if (props.second == m.type) {
						double calc;
						if (props.second.equals(props.first)) {
							calc = Math.ceil(damageAtk * 0.1);
						} else {
							calc = Math.ceil(damageAtk / 3.0);
						}

						atk2 = Math.ceil(calc);
					}
					// not only major prop will be enhanced
					if (!nullAwoken && square > 0 && m.isSquare()) {
						if (props.first == m.type) {
							atk1 = Math.round(atk1 * Math.pow(2.5, square));
						}
						if (props.second == m.type) {
							atk2 = Math.round(atk2 * Math.pow(2.5, square));
						}
						if(m.type == 2 && heartSquare > 0) {
							hasHeartSquare = true;
						}
					}
					if (!nullAwoken && twoway > 0 && m.isTwoWay()) {
						if (props.first == m.type) {
							atk1 = Math.round(atk1 * Math.pow(1.5, twoway));
						}
						if (props.second == m.type) {
							atk2 = Math.round(atk2 * Math.pow(1.5, twoway));
						}
					}

					if(!nullAwoken && lattack > 0 && m.isLFormat()) {
						if (props.first == m.type) {
							atk1 = Math.round(atk1 * Math.pow(1.5, lattack));
						}
						if (props.second == m.type) {
							atk2 = Math.round(atk2 * Math.pow(1.5, lattack));
						}
					}

					if(!nullAwoken && combo7 > 0 && combos.size()>=7) {
						if(props.first == m.type) {
							atk1 = Math.round(atk1 * Math.pow(2.0, combo7));
						}
						if(props.second == m.type) {
							atk2 = Math.round(atk2 * Math.pow(2.0, combo7));
						}
					}
					if(!nullAwoken && combo10 > 0 && combos.size()>=10) {
						if(props.first == m.type) {
							atk1 = Math.round(atk1 * Math.pow(5.0, combo10));
						}
						if(props.second == m.type) {
							atk2 = Math.round(atk2 * Math.pow(5.0, combo10));
						}
					}
                    if(!nullAwoken && hp80>0 && hpPercent>=80) {
                        if(props.first == m.type) {
                            atk1 = Math.round(atk1 * Math.pow(1.5, hp80));
                        }
                        if(props.second == m.type) {
                            atk2 = Math.round(atk2 * Math.pow(1.5, hp80));
                        }
                    }
                    if(!nullAwoken && hp50>0 && hpPercent<=50) {
                        if(props.first == m.type) {
                            atk1 = Math.round(atk1 * Math.pow(2.0, hp50));
                        }
                        if(props.second == m.type) {
                            atk2 = Math.round(atk2 * Math.pow(2.0, hp50));
                        }
                    }

					memberBasicAtk.add(new Pair<Double, Double>(atk1, atk2));
				}
				if(hasHeartSquare) {
					double atkUp = Math.pow(2.0, heartSquare);
					List<Pair<Double, Double>> powerUpAtk = new ArrayList<Pair<Double, Double>>();
					for(Pair<Double, Double> pair : powerUpAtk) {
						powerUpAtk.add(new Pair<Double, Double>(pair.first*atkUp, pair.second*atkUp));
					}
					normalAtk.add(powerUpAtk);
				} else {
					normalAtk.add(memberBasicAtk);
				}
			}
			// calculate real atk = atk * normal * combo * prop factor * leader
			// factor
			int size = normalAtk.size();
			MonsterInfo leader = info.getMember(swapIndex);
			MonsterInfo friend = info.getMember(5);

			int[] rowcount = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			for (Match m : combos) {
				if (m.isOneRow()) {
					++rowcount[m.type];
				}
			}
			
			// new leader skill, attr. power up if %d orbs connected and include + orb
			double[] attr1Factor = null;
			double[] attr2Factor = null;
			if ( !bindStatus.get(0) ) {
			    attr1Factor = getLeaderFactorAttrUp(info, leader, combos);
			}
			if (!(challengeMode || bindStatus.get(5))) {
			    attr2Factor = getLeaderFactorAttrUp(info, friend, combos);
			}
			if ( attr1Factor == null ) {
				attr1Factor = new double[]{1.0,1.0,1.0,1.0,1.0,1.0};
			}
			if ( attr2Factor == null ) {
				attr2Factor = new double[]{1.0,1.0,1.0,1.0,1.0,1.0};
			}
			
			// assign leader skill factor & friend leader factor, if can't,
			// assign check later = true
			SkillType stype = skillFired.getType();
			List<Integer> data = skillFired.getData();
			float awokenUp = 1.0f;
			if(stype==SkillType.ST_POWER_UP_BY_AWOKEN) {
				int awokenSize = data.get(2);
				int total = 0;
				for (int i = 0; i < size; ++i) {
					MonsterInfo minfo = info.getMember(i);
					if (minfo == null || bindStatus.get(i)) {
						LogUtil.d("monster(", i, ") is null");
						monsterAtk.add(new Pair<Double, Double>(.0, .0));
						continue;
					}
					for(int j=0; j<awokenSize; ++j) {
						AwokenSkill skill = AwokenSkill.get(data.get(3+j));
						total += nullAwoken? 0: minfo.getTargetAwokenCount(skill, copMode);
						if(skill == AwokenSkill.SKILL_BOOST) {
							total += minfo.getTargetAwokenCount(AwokenSkill.SKILL_BOOST_PLUS, copMode) * 2;
						} else if(skill == AwokenSkill.EXTEND_TIME) {
							total += minfo.getTargetAwokenCount(AwokenSkill.EXTEND_TIME_PLUS, copMode) * 2;
						}

					}
				}
				awokenUp = (total * data.get(1) + 100) / 100.0f; // percent
			}
			
			for (int i = 0; i < size; ++i) {
				MonsterInfo minfo = info.getMember(i);
				if (minfo == null || bindStatus.get(i)) {
					LogUtil.d("monster(", i, ") is null");
					monsterAtk.add(new Pair<Double, Double>(.0, .0));
					continue;
				}

				List<Pair<Double, Double>> basicList = normalAtk.get(i);
				Pair<Integer, Integer> props = minfo.getProps();

				// calculate row enhanced power factor
				double rowPower1 = 1.0;
				double rowPower2 = 1.0;
				if ( rowSkill[props.first] > 0 ) {
					rowPower1 = (1.0 + 0.1 * rowSkill[props.first] * rowcount[props.first]);
				}
				if ( props.second != -1 && rowSkill[props.second] > 0 ) {
					rowPower2 = (1.0 + 0.1 * rowSkill[props.second] * rowcount[props.second]);
				}
				
				// * prop factor

				double typeFactor = 1.0;
				double powerFactor1 = 1.0;
				double powerFactor2 = 1.0;
				if (stype == SkillType.ST_POWER_UP) {
					int type = data.get(2);
					int type1 = 999;
					if (data.size() == 4) {
						type1 = data.get(3);
					}
					double factor = data.get(1) / 100.0;
					if (props.first == type || props.first == type1) {
						powerFactor1 = factor;
					}
					if (props.second == type || props.second == type1) {
						powerFactor2 = factor;
					}
				} else if (stype == SkillType.ST_TYPE_UP) {
					Pair<MonsterType, MonsterType> monsterTypes = minfo
							.getMonsterTypes();
					MonsterType type3 = minfo.getMonsterType3();
					for (int pos = 2; pos < data.size(); ++pos) {
						int type = data.get(pos);
						if (monsterTypes.first.ordinal() == type
								|| monsterTypes.second.ordinal() == type
								|| type3.ordinal() == type) {
							typeFactor = data.get(1) / 100.0;
							break;
						}
					}
				}
				
				powerFactor1 *= (attr1Factor[props.first] * attr2Factor[props.first]);
				if ( props.second != -1 ) {
					powerFactor2 *= (attr1Factor[props.second] * attr2Factor[props.second]);
				}
				
				// * leader factor
				double f1 = 1.0;
				double f2 = 1.0;
				
				if ( !bindStatus.get(0) ) {
				    f1 = getLeaderFactor(info, leader, minfo, combos, settings);
				}
				if (!(challengeMode || bindStatus.get(5))) {
				    f2 = getLeaderFactor(info, friend, minfo, combos, settings);
				}

				
				LogUtil.d(i, " => factor=", f1, ", factor=", f2);
				double totalf = f1 * f2;
				double tcf1 = typeFactor * powerFactor1 * comboFactor * awokenUp;
				double tcf2 = typeFactor * powerFactor2 * comboFactor * awokenUp;
				double majoratk = 0.0;
				double minoratk = 0.0;
				
				// stage special power up
				totalf *= DamageCalculator.getPowerUp(powerUp, minfo);
				// plus cross attack
				totalf *= crossAttack;
				
				for (Pair<Double, Double> basic : basicList) {

					double atk1 = Math.floor(basic.first * tcf1);
					double atk2 = Math.floor(basic.second * tcf2);
					
					atk1 = Math.ceil(Math.ceil(atk1 * rowPower1) * totalf);
					atk2 = Math.ceil(Math.ceil(atk2 * rowPower2) * totalf);

					majoratk += atk1;
					minoratk += atk2;
				}
				majoratk = (int)(majoratk * singleAtkUp);
				minoratk = (int)(minoratk * singleAtkUp);
				monsterAtk.add(new Pair<Double, Double>(majoratk, minoratk));
			}

		}

		return monsterAtk;
	}

	public static double[] getLeaderFactorAttrUp(TeamInfo team, MonsterInfo leader,
			List<Match> combos) {
		double[] factor = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
		if ( leader != null ) {
			List<LeaderSkill> lst = leader.getLeaderSkill();
			if ( lst != null ) {
				for(LeaderSkill l : lst) {
					if ( l.getType() == LST_FIVE_ORB ) {
						List<Integer> data = l.getData();
						int many = data.get(0);
						double f = data.get(1) / 100.0;
						for(Match m : combos) {
							if ( m.count == many && m.pcount > 0 && m.type <= 5 ) {
								factor[m.type] = f;
							}
						}
					}
				}
			}
		}
		return factor;
	}
	
	@Deprecated
	public static double getLeaderFactor(TeamInfo team, LeaderSkill lskill,
			MonsterInfo monster, Map<String, Object> settings) {
		List<Integer> data = lskill.getData();
		switch (lskill.getType()) {
		case LST_USED_SKILL: {
		    Boolean skillUsed = settings.containsKey("skillFired")? (Boolean)settings.get("skillFired"):Boolean.FALSE;
		    if ( skillUsed ) {
		        int len = data.get(0);
	            List<Integer> type = data.subList(1, len + 1);

	            boolean isSameType = isSameType(type, monster);
	            if (isSameType) {
	                int powerlen = data.get(len + 1);
	                int start = len + 2;
	                for (int i = 0; i < powerlen; ++i) {
	                    int which = data.get(start + i * 2);
	                    if (which == 1) { // attack
	                        return data.get(start + i * 2 + 1) / 100.0;
	                    }
	                }
	            }
		    }
		    return 1.0;
		}
		case LST_TARGET_ORB_COMBO:
		case LST_MULTI_ORB_COMBO:
		case LST_COLOR_FACTOR:
		case LST_COMBO_FACTOR:
			return 2.0;
		case LST_FACTOR: {
			int len = data.get(0);
			List<Integer> type = data.subList(1, len + 1);

			boolean isSameType = isSameType(type, monster);
			if (isSameType) {
				int powerlen = data.get(len + 1);
				int start = len + 2;
				for (int i = 0; i < powerlen; ++i) {
					int which = data.get(start + i * 2);
					if (which == 1) { // attack
						return data.get(start + i * 2 + 1) / 100.0;
					}
				}
			}

			return 1.0;
		}
		case LST_HP_FACTOR: {
			// int hpgtle = data.get(0);
			// int hplevel = data.get(1);
		    
		    boolean useMaxFactor = true;
		    float currentHp = 100f;
		    
		    if ( settings != null ) {
		        if (settings.containsKey("isHpChecked")) {
		            useMaxFactor = !(Boolean) settings.get("isHpChecked");
		        }
		        if (settings.containsKey("hp")) {
		            currentHp = (Float) settings.get("hp");
		        }
		    }
		    
		    if ( useMaxFactor ) {
		        return getHpFactor(data, monster);
		    } else {
                int size = data.size();
                int offset = 0;
                while(offset<size) {
                    List<Integer> partial = data.subList(offset, size);
                    
                    int hpgtle = partial.get(0);
                    int hplevel = partial.get(1);
                    
                    switch(hpgtle) {
                    case 0:
                        if ( currentHp == hplevel ) {
                            return getHpFactor(partial, monster);
                        }
                        break;
                    case 1:
                        if ( currentHp >= hplevel ) {
                            return getHpFactor(partial, monster);
                        }
                        break;
                    case 2:
                        if ( currentHp <= hplevel ) {
                            return getHpFactor(partial, monster);
                        }
                        break;
                    }
                    
                    int powerStart = offset + 2 + data.get(offset+2) + 1;
                    int nextStartPos = powerStart + data.get(powerStart) * 2 + 1;
                    offset = nextStartPos;
                }
		    }
			return 1.0;
		}
		case LST_NOT_SUPPORT:
		default:
			break;
		}
		return 1.0;
	}
//
//	private static double getLeaderFactor(TeamInfo team, LeaderSkill lskill,
//			MonsterInfo monster, List<Match> combos, Map<String, Object> settings) {
//		return getLeaderFactor(team, lskill, monster, combos, settings, false);
//	}

	public static int getTargetOrbShield(TeamInfo team, LeaderSkill lskill, List<Match> combos, Map<String, Object> settings) {
		List<Integer> data = lskill.getData();
		int setCnt = data.get(0);
		List<Pair<Integer, List<Integer>>> removeSet = new ArrayList<Pair<Integer, List<Integer>>>();
		int pos = 1;
		for (int i = 0; i < setCnt; ++i) {
			List<Integer> set = new ArrayList<Integer>();
			int orbCnt = data.get(pos);
			for (int j = 1; j <= orbCnt; ++j) {
				set.add(data.get(pos + j));
			}
			int factor = data.get(pos + orbCnt + 1);
			removeSet.add(new Pair<Integer, List<Integer>>(factor, set));
			pos += orbCnt + 2;
		}
		int maxFactor = 0;
		for (Pair<Integer, List<Integer>> pair : removeSet) {
			int[] counter = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			for (Integer orb : pair.second) {
				++counter[orb];
			}

			for (Match m : combos) {
				if (m.type >= counter.length) { // should not happen, dead
					// code..
					continue;
				}
				--counter[m.type];
			}
			boolean leaderfired = true;
			for (int i = 0; i < counter.length; ++i) {
				if (counter[i] > 0) {
					leaderfired = false;
					break;
				}
			}
			if (leaderfired) {
				int factor = pair.first;
				if (factor > maxFactor) {
					maxFactor = factor;
				}
			}
		}
		return maxFactor;
	}


	public static int getMultiShieldMulti(TeamInfo team, LeaderSkill lskill, List<Match> combos,Map<String, Object> settings) {
		List<Integer> data = lskill.getData();
		int removeN = data.get(0);
		int orbCnt = data.get(1);

		List<Integer> orbType = new ArrayList<Integer>();
		for(int i=0; i<orbCnt; ++i) {
			orbType.add(data.get(2+i));
		}
		int offset = 2 + orbCnt;

		int maxRemove = 0;
		for (Match m : combos) {
			if (orbType.contains(m.type) && m.count > maxRemove) {
				maxRemove = m.count;
			}
		}
		if (maxRemove >= removeN) {
			int factor = data.get(offset);
			int maxN = data.get(offset+1);
			if (maxN != removeN) {
				if (maxRemove >= maxN) {
					return data.get(offset+4);
				}

				int addfactor = data.get(offset+3);
				int diff = maxN - removeN;
				for (int i = 0; i <= diff; ++i) {
					if (maxRemove == removeN + i) {
						return factor + addfactor * i;
					}
				}
			} else {
				return factor;
			}
		}
		return 0;
	}


	public static int getMultiShield(TeamInfo team, LeaderSkill lskill, List<Match> combos,Map<String, Object> settings) {
		List<Integer> data = lskill.getData();
		int removeN = data.get(0);
		int orbType = data.get(1);

		int maxRemove = 0;
		for (Match m : combos) {
			if ((orbType == -1 || m.type == orbType) && m.count > maxRemove) {
				maxRemove = m.count;
			}
		}
		if (maxRemove >= removeN) {

			int factor = data.get(2);
			int maxN = data.get(3);
			if (maxN != removeN) {
				if (maxRemove >= maxN) {
					return data.get(6);
				}

				int addfactor = data.get(5);
				int diff = maxN - removeN;
				for (int i = 0; i <= diff; ++i) {
					if (maxRemove == removeN + i) {
						return factor + addfactor * i;
					}
				}
			} else {
				return factor;
			}
		}
		return 0;
	}

	public static int getComboShieldPercent(TeamInfo team, LeaderSkill lskill, List<Match> combos,Map<String, Object> settings) {
		SparseArray<Integer> comboFactor = new SparseArray<Integer>();
		List<Integer> data = lskill.getData();
		int size = data.get(0);
		int min = 99;
		int max = 0;
		for (int i = 0; i < size; ++i) {
			int combo = data.get(i * 2 + 1);
			Integer factor = data.get(i * 2 + 2);
			comboFactor.put(combo, factor);
			if (min > combo) {
				min = combo;
			}
			if (max < combo) {
				max = combo;
			}
		}
		int combosize = combos.size();
		Integer hasFactor = comboFactor.get(combosize, 0);
		if (hasFactor != 0) {
			return hasFactor;
		} else if (combosize > max) {
			return comboFactor.get(max);
		}
		return 0;
	}

	public static int getShieldPercent(TeamInfo team, LeaderSkill lskill, List<Match> combos,Map<String, Object> settings) {
		List<Integer> data = lskill.getData();
		boolean[] orbset = { false, false, false, false, false, false, false, false, false };
		boolean[] matchColor = { false, false, false, false, false, false };
		boolean[] memberColor = { false, false, false, false, false, false };
		int many = data.get(0);
		List<Boolean> bindStatus = null;
		if (settings != null && settings.containsKey("bindStatus")) {
			bindStatus = (List<Boolean>)settings.get("bindStatus");
		} else {
			bindStatus = new ArrayList<Boolean>();
			for(int i=0; i<6; ++i) {
				bindStatus.add(false);
			}
		}

		for (int i = 1; i <= many; ++i) {
			orbset[data.get(i)] = true;
		}
		int removeMany = data.get(many + 1);
		int factor = data.get(many + 2);

		for (Match m : combos) {
			if (m.type > 5) {
				// we don't count the poison or jamar orbs
				continue;
			}
			matchColor[m.type] = true;
		}
		// 6,1,4,5,3,0,2,3,30,6,4,35,5,40,6,45
		for (int i = 0; i < 6; ++i) {
			MonsterInfo mi = team.getMember(i);
			if (mi == null || bindStatus.get(i)) {
				continue;
			}
			Pair<Integer, Integer> props = mi.getProps();
			memberColor[props.first] = true;
			if (props.second != -1) {
				memberColor[props.second] = true;
			}
		}
		memberColor[2] = true;
		int[] factorArray = new int[7];
		for (int i = 0; i < removeMany; ++i) {
			factorArray[i] = 0;
		}

		int maxN = removeMany;
		int addition = data.get(many + 3);
		if (addition > 0) {
			factorArray[removeMany] = factor;
			int index = 4 + many;
			for (int i = 0; i < addition; i += 2) {
				int remove = data.get(index + i);
				int f = data.get(index + i + 1);
				factorArray[remove] = f;
				if(i==addition-2) {
					maxN = remove;
				}
			}
		} else {
			for (int i = removeMany; i < 7; ++i) {
				factorArray[i] = factor;
			}
		}

		int conditionCount = 0;
		for (int i = 0; i < 6; ++i) {
			if (orbset[i] && matchColor[i] && memberColor[i]) {
				++conditionCount;
			}
		}
		if(conditionCount>maxN) {
			conditionCount = maxN;
		}

		return factorArray[conditionCount];
	}

	public static double getCrossFactor(TeamInfo team, LeaderSkill lskill,
										 MonsterInfo monster, List<Match> combos, Map<String, Object> settings) {
		List<Integer> data = lskill.getData();
		switch (lskill.getType()) {
			case LST_CROSS: {
				boolean fired = false;
				float atk = data.size()==2 ? data.get(1)/100f:1f;
				if(settings != null && settings.containsKey("cross")) {
					if((Integer)settings.get("cross") >= 50) {
						fired = true;
					}
				}
				if(!fired) {
					for(Match match : combos) {
		    			if(match.type == 2 && match.isCross()) { // if heart and cross
		    				fired = true;
		    				break;
		    			}
		    		}
				}
				return fired? atk:1.0;
			}
		}
		return 1.0;
	}

	private static double getLeaderFactor(TeamInfo team, LeaderSkill lskill,
			MonsterInfo monster, List<Match> combos, Map<String, Object> settings,
			boolean factorOnly) {
		List<Integer> data = lskill.getData();
		switch (lskill.getType()) {
			case LST_DROP_NO_MORE:
			{
				int trigger = data.get(0);
				int removed = 0;
				for (Match m : combos) {
					if (m.type >= 9) {
						continue;
					}
					removed += m.count;
				}
				int remain = (30 - removed);
				if (remain <= trigger) {
					if (data.size() <= 2) {
						return data.get(1) / 100.0;
					} else {
						int diff = trigger - remain;
						int addition = (data.get(3) - data.get(1)) / (trigger - data.get(2));
						return (data.get(1) + addition * diff) / 100.0;
					}
				}
				return 1.0;
			}
		case LST_CROSS_P:
		case LST_CROSS: {
			if(factorOnly) {
				boolean fired = false;
				float atk = data.size() >= 2 ? data.get(1) / 100f : 1f;
				if (settings != null && settings.containsKey("cross")) {
					if ((Float) settings.get("cross") > 0) {
						fired = true;
					}
				}
				if (!fired) {
					for (Match match : combos) {
						if (match.type == 2 && match.isCross()) { // if heart and cross
							fired = true;
							break;
						}
					}
				}
				return fired ? atk : 1.0;
			}
			break;
		}
		case LST_MORE_REMOVE: {
			double factor = data.get(1) / 100.0;
            List<Integer> type = data.subList(3, data.size());

            if (isSameType(type, monster)) {
            	return factor;
            }
			return 1.0;
		}
		case LST_CROSS_ATTACK: {
			float x = data.get(0) / 100f;
			int colorCnt = data.get(1);
			boolean[] orbs = {false, false, false, false, false, false, false, false, false};
			
			for(int i=0; i<colorCnt; ++i) {
				orbs[data.get(2+i)] = true;
			}
			float total = 1.0f;
			for(Match match : combos) {
				if(match.isCross() && match.type <= 8) {
					if(orbs[match.type]) {
						total *= x;
					}
				}
			}
			
			return total;
		}
		case LST_MULTI_PLAY: {
			if (team.isCopMode()) {
		        int len = data.get(0);
	            List<Integer> type = data.subList(1, len + 1);

	            boolean isSameType = !factorOnly && isSameType(type, monster);
	            if (isSameType) {
	                int powerlen = data.get(len + 1);
	                int start = len + 2;
	                for (int i = 0; i < powerlen; ++i) {
	                    int which = data.get(start + i * 2);
	                    if (which == 1) { // attack
	                        return data.get(start + i * 2 + 1) / 100.0;
	                    }
	                }
	            }
			}
			return 1.0;
		}

		case LST_USED_SKILL: {
		    Boolean skillUsed = (settings!=null&&settings.containsKey("skillFired"))? (Boolean)settings.get("skillFired"):Boolean.FALSE;
		    if ( skillUsed ) {
		        int len = data.get(0);
	            List<Integer> type = data.subList(1, len + 1);

	            boolean isSameType = !factorOnly && isSameType(type, monster);
	            if (isSameType) {
	                int powerlen = data.get(len + 1);
	                int start = len + 2;
	                for (int i = 0; i < powerlen; ++i) {
	                    int which = data.get(start + i * 2);
	                    if (which == 1) { // attack
	                        return data.get(start + i * 2 + 1) / 100.0;
	                    }
	                }
	            }
		    }
		    return 1.0;
		}
		case LST_TARGET_ORB_COMBO: {
			int setCnt = data.get(0);
			List<Pair<Double, List<Integer>>> removeSet = new ArrayList<Pair<Double, List<Integer>>>();
			int pos = 1;
			for (int i = 0; i < setCnt; ++i) {
				List<Integer> set = new ArrayList<Integer>();
				int orbCnt = data.get(pos);
				for (int j = 1; j <= orbCnt; ++j) {
					set.add(data.get(pos + j));
				}
				double factor = data.get(pos + orbCnt + 1);
				removeSet.add(new Pair<Double, List<Integer>>(factor, set));
				pos += orbCnt + 2;
			}
			double maxFactor = 1.0;
			for (Pair<Double, List<Integer>> pair : removeSet) {
				int[] counter = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
				for (Integer orb : pair.second) {
					++counter[orb];
				}

				for (Match m : combos) {
					if (m.type >= counter.length) { // should not happen, dead
													// code..
						continue;
					}
					--counter[m.type];
				}
				boolean leaderfired = true;
				for (int i = 0; i < counter.length; ++i) {
					if (counter[i] > 0) {
						leaderfired = false;
						break;
					}
				}
				if (leaderfired) {
					double factor = pair.first / 100.0;
					if (factor > maxFactor) {
						maxFactor = factor;
					}
				}
			}

			return maxFactor;
		}
		case LST_MULTI_ORB_COMBO_MULTI: {
			int removeN = data.get(0);
			int size = data.get(1);

			Set<Integer> orbTypes = new HashSet<Integer>();
			for(int i=0; i<size; ++i) {
				orbTypes.add(data.get(2+i));
			}

			int maxRemove = 0;
			for (Match m : combos) {
				if (orbTypes.contains(m.type) && m.count > maxRemove) {
					maxRemove = m.count;
				}
			}
			if (maxRemove >= removeN) {

				double factor = data.get(2+size) / 100.0;
				int maxN = data.get(3+size);
				if (maxN != removeN) {
					if (maxRemove >= maxN) {
						return data.get(6+size) / 100.0;
					}

					double addfactor = data.get(5+size) / 100.0;
					int diff = maxN - removeN;
					for (int i = 0; i <= diff; ++i) {
						if (maxRemove == removeN + i) {
							return factor + addfactor * i;
						}
					}
				} else {
					return factor;
				}
			}

			return 1.0;
		}
		case LST_MULTI_ORB_COMBO: {
			int removeN = data.get(0);
			int orbType = data.get(1);

			int maxRemove = 0;
			for (Match m : combos) {
				if ((orbType == -1 || m.type == orbType) && m.count > maxRemove) {
					maxRemove = m.count;
				}
			}
			if (maxRemove >= removeN) {

				double factor = data.get(2) / 100.0;
				int maxN = data.get(3);
				if (maxN != removeN) {
					if (maxRemove >= maxN) {
						return data.get(6) / 100.0;
					}

					double addfactor = data.get(5) / 100.0;
					int diff = maxN - removeN;
					for (int i = 0; i <= diff; ++i) {
						if (maxRemove == removeN + i) {
							return factor + addfactor * i;
						}
					}
				} else {
					return factor;
				}
			}

			return 1.0;
		}
		// TODO: not only one factor in color factor
		case LST_COLOR_FACTOR: {
			boolean[] orbset = { false, false, false, false, false, false, false, false, false };
			boolean[] matchColor = { false, false, false, false, false, false,false, false, false };
			boolean[] memberColor = { false, false, false, false, false, false };
			int many = data.get(0);
			List<Boolean> bindStatus = null;
	        if (settings != null && settings.containsKey("bindStatus")) {
	            bindStatus = (List<Boolean>)settings.get("bindStatus");
	        } else {
	            bindStatus = new ArrayList<Boolean>();
	            for(int i=0; i<6; ++i) {
	                bindStatus.add(false);
	            }
		    }
			
			for (int i = 1; i <= many; ++i) {
				orbset[data.get(i)] = true;
			}
			int removeMany = data.get(many + 1);
			double factor = data.get(many + 2) / 100.0;

			for (Match m : combos) {
				if (m.type >= matchColor.length) {
					// we don't count the poison or jamar orbs
					continue;
				}
				matchColor[m.type] = true;
			}
			// 6,1,4,5,3,0,2,3,30,6,4,35,5,40,6,45
			for (int i = 0; i < 6; ++i) {
				MonsterInfo mi = team.getMember(i);
				if (mi == null || bindStatus.get(i)) {
					continue;
				}
				Pair<Integer, Integer> props = mi.getProps();
				memberColor[props.first] = true;
				if (props.second != -1) {
					memberColor[props.second] = true;
				}
			}
			memberColor[2] = true;
			double[] factorArray = new double[7];
			for (int i = 0; i < removeMany; ++i) {
				factorArray[i] = 1.0;
			}

			int maxN = removeMany;
			int addition = data.get(many + 3);
			if (addition > 0) {
				factorArray[removeMany] = factor;
				int index = 4 + many;
				for (int i = 0; i < addition; i += 2) {
					int remove = data.get(index + i);
					double f = data.get(index + i + 1) / 100.0;
					factorArray[remove] = f;
					if(i==addition-2) {
						maxN = remove;
					}
				}
			} else {
				for (int i = removeMany; i < 7; ++i) {
					factorArray[i] = factor;
				}
			}

			int conditionCount = 0;
			for (int i = 0; i < 6; ++i) {
				if (orbset[i] && matchColor[i] && memberColor[i]) {
					++conditionCount;
				}
			}
			for (int i = 6; i <=8;  ++i) {
				if (orbset[i] && matchColor[i]) {
					++conditionCount;
				}
			}
			if(conditionCount>maxN) {
				conditionCount = maxN;
			}
			
			return factorArray[conditionCount];

		}
		case LST_COMBO_FACTOR: {
			SparseArray<Double> comboFactor = new SparseArray<Double>();
			int size = data.get(0);
			int min = 99;
			int max = 0;
			for (int i = 0; i < size; ++i) {
				int combo = data.get(i * 2 + 1);
				double factor = data.get(i * 2 + 2) / 100.0;
				comboFactor.put(combo, factor);
				if (min > combo) {
					min = combo;
				}
				if (max < combo) {
					max = combo;
				}
			}
			int combosize = combos.size();
			double hasFactor = comboFactor.get(combosize, 0.0);
			if (hasFactor != 0.0) {
				return hasFactor;
			} else if (combosize > max) {
				return comboFactor.get(max);
			}
			return 1.0;
		}
		case LST_FACTOR: {
			int len = data.get(0);
			List<Integer> type = data.subList(1, len + 1);

			boolean isSameType = !factorOnly && isSameType(type, monster);
			if (isSameType) {
				int powerlen = data.get(len + 1);
				int start = len + 2;
				for (int i = 0; i < powerlen; ++i) {
					int which = data.get(start + i * 2);
					if (which == 1) { // attack
						return data.get(start + i * 2 + 1) / 100.0;
					}
				}
			}

			return 1.0;
		}
		case LST_HP_FACTOR: {
			// int hpgtle = data.get(0);
			// int hplevel = data.get(1);
		    
		    boolean useMaxFactor = true;
		    float currentHp = 100f;
		    
		    if ( settings != null ) {
		        if (settings.containsKey("isHpChecked")) {
		            useMaxFactor = !(Boolean) settings.get("isHpChecked");
		        }
		        if (settings.containsKey("hp")) {
		            currentHp = (Float) settings.get("hp");
		        }
		    }
		    
		    if ( useMaxFactor ) {
		        return getHpFactor(data, monster);
		    } else {
                int size = data.size();
                int offset = 0;
                while(offset<size) {
                    List<Integer> partial = data.subList(offset, size);
                    
                    int hpgtle = partial.get(0);
                    int hplevel = partial.get(1);
                    
                    switch(hpgtle) {
                    case 0:
                        if ( currentHp == hplevel ) {
                            return getHpFactor(partial, monster);
                        }
                        break;
                    case 1:
                        if ( currentHp >= hplevel ) {
                            return getHpFactor(partial, monster);
                        }
                        break;
                    case 2:
                        if ( currentHp <= hplevel ) {
                            return getHpFactor(partial, monster);
                        }
                        break;
                    }
                    
                    int powerStart = offset + 2 + data.get(offset+2) + 1;
                    int nextStartPos = powerStart + data.get(powerStart) * 2 + 1;
                    offset = nextStartPos;
                }
		    }
			return 1.0;
		}
		case LST_NOT_SUPPORT:
		default:
			break;
		}
		return 1.0;
	}

    private static double getHpFactor(List<Integer> data) {
        int len = data.get(2);
        int powerlen = data.get(2 + len + 1);
        int start = 2 + len + 2;
        for (int i = 0; i < powerlen; ++i) {
            int which = data.get(start + i * 2);
            if (which == 1) { // attack
                return data.get(start + i * 2 + 1) / 100.0;
            }
        }
        return 1.0;
    }
	
	private static double getHpFactor(List<Integer> data, MonsterInfo monster) {
        int len = data.get(2);
        
        List<Integer> type = data.subList(3, len + 3);
        boolean isSameType = isSameType(type, monster);
        if (isSameType) {
            int powerlen = data.get(2 + len + 1);
            int start = 2 + len + 2;
            for (int i = 0; i < powerlen; ++i) {
                int which = data.get(start + i * 2);
                if (which == 1) { // attack
                    return data.get(start + i * 2 + 1) / 100.0;
                }
            }
        }
        return 1.0;
	}
	
	public static double getMaxLeaderMultiplier(TeamInfo team, MonsterInfo leader, Map<String, Object> settings) {
	    double multiplier = 1.0;
	    
	    if ( leader != null ) {
	        List<LeaderSkill> list = leader.getLeaderSkill();
	        for (LeaderSkill ls : list) {
	            double multi = getMaxLeaderFactor(team, ls, settings);
	            multiplier *= multi;
	        }
	    }
	    LogUtil.d("attack multiplier=", multiplier);
	    
	    return multiplier;
	}
	
	private static double getMaxLeaderFactor(TeamInfo team, LeaderSkill lskill, Map<String, Object> settings) {
        List<Integer> data = lskill.getData();
        switch (lskill.getType()) {
        case LST_USED_SKILL: {
            Boolean skillUsed = settings.containsKey("skillFired")? (Boolean)settings.get("skillFired"):Boolean.FALSE;
            if ( skillUsed ) {
                int len = data.get(0);
                int powerlen = data.get(len + 1);
                int start = len + 2;
                for (int i = 0; i < powerlen; ++i) {
                    int which = data.get(start + i * 2);
                    if (which == 1) { // attack
                        return data.get(start + i * 2 + 1) / 100.0;
                    }
                }
            }
            return 1.0;
        }
        case LST_TARGET_ORB_COMBO: {
            int setCnt = data.get(0);
            List<Pair<Double, List<Integer>>> removeSet = new ArrayList<Pair<Double, List<Integer>>>();
            int pos = 1;
            for (int i = 0; i < setCnt; ++i) {
                List<Integer> set = new ArrayList<Integer>();
                int orbCnt = data.get(pos);
                for (int j = 1; j <= orbCnt; ++j) {
                    set.add(data.get(pos + j));
                }
                double factor = data.get(pos + orbCnt + 1);
                removeSet.add(new Pair<Double, List<Integer>>(factor, set));
                pos += orbCnt + 2;
            }
            double maxFactor = 1.0;
            for (Pair<Double, List<Integer>> pair : removeSet) {
                double factor = pair.first / 100.0;
                if (factor > maxFactor) {
                    maxFactor = factor;
                }
            }

            return maxFactor;
        }
        case LST_MULTI_ORB_COMBO: {
            int removeN = data.get(0);
            int maxN = data.get(3);
            int maxRemove = maxN;
            if (maxRemove >= removeN) {
                double factor = data.get(2) / 100.0;
                
                if (maxN != removeN) {
                    if (maxRemove >= maxN) {
                        return data.get(6) / 100.0;
                    }

                    double addfactor = data.get(5) / 100.0;
                    int diff = maxN - removeN;
                    for (int i = 0; i <= diff; ++i) {
                        if (maxRemove == removeN + i) {
                            return factor + addfactor * i;
                        }
                    }
                } else {
                    return factor;
                }
            }

            return 1.0;
        }
        // TODO: not only one factor in color factor
        case LST_COLOR_FACTOR: {
            int many = data.get(0);
            double factor = data.get(many + 2) / 100.0;

            double max = factor;

            int addition = data.get(many + 3);
            if (addition > 0) {
                int index = 4 + many;
                for (int i = 0; i < addition; i += 2) {
                    double f = data.get(index + i + 1) / 100.0;
                    if ( max < f ) {
                        max = f;
                    }
                }
            }
            return max;

        }
        case LST_COMBO_FACTOR: {
            SparseArray<Double> comboFactor = new SparseArray<Double>();
            int size = data.get(0);
            int min = 99;
            int max = 0;
            for (int i = 0; i < size; ++i) {
                int combo = data.get(i * 2 + 1);
                double factor = data.get(i * 2 + 2) / 100.0;
                comboFactor.put(combo, factor);
                if (min > combo) {
                    min = combo;
                }
                if (max < combo) {
                    max = combo;
                }
            }
            return comboFactor.get(max);
        }
        case LST_FACTOR: {
            int len = data.get(0);
            int powerlen = data.get(len + 1);
            int start = len + 2;
            for (int i = 0; i < powerlen; ++i) {
                int which = data.get(start + i * 2);
                if (which == 1) { // attack
                    return data.get(start + i * 2 + 1) / 100.0;
                }
            }

            return 1.0;
        }
        case LST_HP_FACTOR: {
            return getHpFactor(data);
        }
        case LST_NOT_SUPPORT:
        default:
            break;
        }
        return 1.0;
    }

	public static double getLeaderFactor(TeamInfo team, MonsterInfo leader,
			MonsterInfo monster, List<Match> combos, Map<String, Object> settings) {
		return getLeaderFactor(team, leader, monster, combos, settings, false);
	}
	
	public static double getLeaderFactor(TeamInfo team, MonsterInfo leader,
			MonsterInfo monster, List<Match> combos, Map<String, Object> settings, boolean factorOnly) {
		if (leader == null) {
			return 1.0;
		}
		List<LeaderSkill> list = leader.getLeaderSkill();
		double factor = 1.0;
		for (LeaderSkill ls : list) {
			double newfactor = getLeaderFactor(team, ls, monster, combos, settings, factorOnly);
			factor *= newfactor;
		}
		return factor;
	}
	
	public static float getPowerUp(boolean[] powerUpList, MonsterInfo monster) {
		if (powerUpList == null || monster == null) {
			return 1.0f;
		}
		for(int powerUp = 0; powerUp<powerUpList.length; ++powerUp) {
			if(!powerUpList[powerUp]) {
				continue;
			}
			if(powerUp < 5) {
				int[] mapping = {1,4,5,3,0};
				Pair<Integer, Integer> checkProp = monster.getProps();
				if(checkProp.first == mapping[powerUp] || checkProp.second == mapping[powerUp]) {
					return 1.5f;
				}
			} else {
				int typeUp = powerUp - 5;
				final int[] TYPES = {
						MonsterSkill.MonsterType.DRAGON.ordinal(),
						MonsterSkill.MonsterType.DEVIL.ordinal(),
						MonsterSkill.MonsterType.BALANCED.ordinal(),
						MonsterSkill.MonsterType.ATTACKER.ordinal(),
						MonsterSkill.MonsterType.HEALER.ordinal(),
						MonsterSkill.MonsterType.GOD.ordinal(),
						MonsterSkill.MonsterType.PHYSICAL.ordinal(),
						MonsterSkill.MonsterType.MACHINE.ordinal()
						};
				if(typeUp < TYPES.length && isSameType(TYPES[typeUp], monster)) {
					return 1.5f;
				} else if( typeUp == 8 && monster.getRare() <= 6 ) { // new 6 star power up
					return 3.0f;
				}
			}
		}
		return 1.0f;
	}

	private static boolean isSameType(int monsterType, MonsterInfo monster) {
		if( monsterType < 0 ) {
			return false;
		}
		Pair<MonsterType, MonsterType> pair = monster.getMonsterTypes();
		MonsterType type3 = monster.getMonsterType3();
		
		return (pair.first.ordinal() == monsterType || pair.second.ordinal() == monsterType ||
				type3.ordinal() == monsterType);
	}
	
	private static boolean isSameType(List<Integer> type, MonsterInfo monster) {
		int len = type.size();
		if (type.get(0) != -1) { // -1 means all monster prop will get the
			// enhance
			Pair<Integer, Integer> checkProp = monster.getProps();
			Pair<MonsterType, MonsterType> pair = monster.getMonsterTypes();
			MonsterType type3 = monster.getMonsterType3();
			for (int i = 0; i < len; ++i) {
				int mtype = type.get(i);
				if (mtype < MonsterSkill.COLOR.length) {

					if (mtype == checkProp.first || mtype == checkProp.second) {
						return true;
					}
				} else {
					int monsterType = mtype - MonsterSkill.COLOR.length;

					if (monsterType == pair.first.ordinal()
							|| monsterType == pair.second.ordinal() 
							|| monsterType == type3.ordinal()) {
						return true;
					}

				}
			}
		} else {
			return true;
		}
		return false;
	}


}
