package com.a30corner.combomaster.pad.mode5x4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.Pair;
import android.util.SparseArray;

import com.a30corner.combomaster.pad.DamageCalculator;
import com.a30corner.combomaster.pad.Match;
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
import com.a30corner.combomaster.utils.Constants;
import com.a30corner.combomaster.utils.LogUtil;

public class DamageCalculator5x4 {

	public static class Damage {
		public double value;
		public int prop;
		public boolean isAttackAll;
		public boolean isTwoWay;

		public Damage(double v, int p, boolean atkAll, boolean twoWay) {
			value = v;
			prop = p;
			isAttackAll = atkAll;
			isTwoWay = twoWay;
		}

		public static Damage zero(int prop) {
			return new Damage(0.0, prop, false, false);
		}

		public static Damage zero() {
			return new Damage(0.0, -1, false, false);
		}
	}

	// for simulator, we need to know the attack is attack all or two way or
	// single target
	public static List<Pair<Damage, Damage>> calculate(TeamInfo info,
			List<Match5x4> combos, ActiveSkill skillFired) {
		if (skillFired == null) {
			skillFired = ActiveSkill.empty();
		}
		List<Pair<Damage, Damage>> monsterAtk = new ArrayList<Pair<Damage, Damage>>();
		if (combos != null) {
			double comboFactor = 1 + (0.25 * (combos.size() - 1));
			// one attack skill count
			int[] rowSkill = { 0, 0, 0, 0, 0, 0 };
			int[] plusOSkill = { 0, 0, 0, 0, 0, 0 };
			for (int i = 0; i < 6; ++i) {
				MonsterInfo minfo = info.getMember(i);
				if (minfo == null) {
					continue;
				}
				rowSkill[0] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_DARK_ATT, false);
				rowSkill[1] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_FIRE_ATT, false);
				// rowSkill[2] +=
				// minfo.getTargetAwokenCount(AwokenSkill.ENHANCE_HEAL);
				rowSkill[3] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_LIGHT_ATT, false);
				rowSkill[4] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_WATER_ATT, false);
				rowSkill[5] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_WOOD_ATT, false);

				plusOSkill[0] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_DARK, false);
				plusOSkill[1] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_FIRE, false);
				plusOSkill[3] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_LIGHT, false);
				plusOSkill[4] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_WATER, false);
				plusOSkill[5] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_WOOD, false);
			}

			// calculate normal attack - atk * (rm orb factor) * (combo factor)
			boolean[] pattackAll = { false, false, false, false, false, false };
			boolean[] ptwoWay = { false, false, false, false, false, false };
			boolean[] sattackAll = { false, false, false, false, false, false };
			boolean[] stwoWay = { false, false, false, false, false, false };
			List<List<Pair<Double, Double>>> normalAtk = new ArrayList<List<Pair<Double, Double>>>();
			for (int i = 0; i < 6; ++i) {
				MonsterInfo minfo = info.getMember(i);
				if (minfo == null) {
					continue;
				}
				int twoway = minfo
						.getTargetAwokenCount(AwokenSkill.TWO_PRONGED_ATTACK, false);
				int combo7 = minfo.getTargetAwokenCount(AwokenSkill.COMBO_7, false);
				int combo10 = minfo.getTargetAwokenCount(AwokenSkill.COMBO_10, false);
				Pair<Integer, Integer> props = minfo.getProps();
				int atk = minfo.getAtk();
				List<Pair<Double, Double>> memberBasicAtk = new ArrayList<Pair<Double, Double>>();

				int[] rowcount = { 0, 0, 0, 0, 0, 0, 0, 0,0 };
				for (Match5x4 m : combos) {
					if (m.isOneRow()) {
						++rowcount[m.type];
					}
				}
				for (Match5x4 m : combos) {
					if (props.first != m.type && props.second != m.type) {
						continue;
					}

					double damage = (1.0 + (.25 * (m.count - 3)));
					if (m.pcount > 0) {
						damage *= (1.0 + 0.06 * m.pcount)
								* (1.0 + 0.05 * plusOSkill[m.type]);
					}
					if (m.isOneRow() && rowSkill[m.type] > 0) {
						damage *= (1.0 + 0.1 * rowSkill[m.type]
								* rowcount[m.type]);
					}
					double atk1 = 0.0;
					double atk2 = 0.0;

					if (props.first == m.type) {
						atk1 = Math.ceil(damage * atk);
						if (m.count >= 5) {
							pattackAll[i] = true;
						}
					}
					if (props.second == m.type) {
						double calc = damage;
						if (props.second == props.first) {
							calc = Math.ceil(calc * atk * 0.1);
						} else {
							calc = Math.ceil(calc * atk / 3.0);
						}

						atk2 = Math.ceil(calc);
						if (m.count >= 5) {
							sattackAll[i] = true;
						}
					}
					// not only major prop will be enhanced
					if (twoway > 0 && m.isTwoWay()) {
						if (props.first == m.type) {
							atk1 = Math.round(atk1 * Math.pow(1.5, twoway));
							ptwoWay[i] = true;
						}
						if (props.second == m.type) {
							atk2 = Math.round(atk2 * Math.pow(1.5, twoway));
							stwoWay[i] = true;
						}
					}
					if(combo7 > 0 && combos.size()>=7) {
						if(props.first == m.type) {
							atk1 = Math.round(atk1 * Math.pow(2.0, combo7));
						}
						if(props.second == m.type) {
							atk2 = Math.round(atk2 * Math.pow(2.0, combo7));
						}
					}
					if(combo10 > 0 && combos.size()>=10) {
						if(props.first == m.type) {
							atk1 = Math.round(atk1 * Math.pow(5.0, combo10));
						}
						if(props.second == m.type) {
							atk2 = Math.round(atk2 * Math.pow(5.0, combo10));
						}
					}

					memberBasicAtk.add(new Pair<Double, Double>(atk1, atk2));
				}
				normalAtk.add(memberBasicAtk);
			}
			// calculate real atk = atk * normal * combo * prop factor * leader
			// factor
			int size = normalAtk.size();
			MonsterInfo leader = info.getMember(0);
			MonsterInfo friend = info.getMember(5);

			// assign leader skill factor & friend leader factor, if can't,
			// assign check later = true
			for (int i = 0; i < size; ++i) {
				MonsterInfo minfo = info.getMember(i);
				if (minfo == null) {
					LogUtil.d("monster(", i, ") is null");
					monsterAtk.add(new Pair<Damage, Damage>(Damage.zero(),
							Damage.zero()));
					continue;
				}
				List<Pair<Double, Double>> basicList = normalAtk.get(i);
				Pair<Integer, Integer> props = minfo.getProps();

				// * prop factor
				SkillType stype = skillFired.getType();
				List<Integer> data = skillFired.getData();
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
				// * leader factor
				double f1 = getLeaderFactor(info, leader, minfo, combos, null);
				double f2 = getLeaderFactor(info, friend, minfo, combos, null);
				LogUtil.d(i, " => factor=", f1, ", factor=", f2);
				double totalf = f1 * f2;
				double tcf1 = typeFactor * powerFactor1 * comboFactor;
				double tcf2 = typeFactor * powerFactor2 * comboFactor;
				double majoratk = 0.0;
				double minoratk = 0.0;
				for (Pair<Double, Double> basic : basicList) {

					double atk1 = Math.ceil(basic.first * tcf1);
					double atk2 = Math.ceil(basic.second * tcf2);

					atk1 = Math.ceil(atk1 * totalf);
					atk2 = Math.ceil(atk2 * totalf);

					majoratk += atk1;
					minoratk += atk2;
				}
				monsterAtk.add(new Pair<Damage, Damage>(new Damage(majoratk,
						props.first, pattackAll[i], ptwoWay[i]), new Damage(
						minoratk, props.second, sattackAll[i], stwoWay[i])));
			}

		}

		return monsterAtk;
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

	public static double calculateRecovery(TeamInfo info, List<Match5x4> combos,
			ActiveSkill skillFired, Map<String, Object> settings) {
		if (skillFired == null) {
			skillFired = ActiveSkill.empty();
		}
		
		double comboFactor = 1 + (0.25 * (combos.size() - 1));
		int recovery = info.getRecovery();
		if(settings != null && settings.containsKey("singleUp")) {
			 int singleUp = (Integer)settings.get("singleUp");
			 if(singleUp == SinglePowerUp.RCV.value()) {
				 recovery = (int)(recovery*1.25);
			 }
		}
		
		double total = 0.0;
		if (combos != null) {
            int heartPlusCnt = 0;
            for(int i=0; i<6; ++i) {
                MonsterInfo minfo = info.getMember(i);
                if (minfo != null ) {
                    heartPlusCnt += minfo.getTargetAwokenCount(AwokenSkill.ENHANCE_HEART, false);
                }
            }
            
            for (Match5x4 m : combos) {
                if (m.type != 2) {
                    continue;
                }
                double c = (1.0 + (.25 * (m.count - 3)));
                if ( m.pcount > 0 ) {
                    c *= (1.0 + 0.06 * m.pcount) * (1.0 + 0.05 * heartPlusCnt);
                }
                total += Math.ceil(recovery * c);
            }
		}
		double calculated = total * comboFactor;
		if (calculated < 0.0) {
			calculated = 0.0;
		}
		return Math.floor(calculated);
	}
	
    public static double calculatePoison(TeamInfo mTeam,
            List<Match5x4> mScoreBoard, ActiveSkill active) {
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
	
	public static List<Pair<Double, Double>> calculate(TeamInfo info,
			List<Match5x4> combos, ActiveSkill skillFired, int defence, 
			Map<String, Object> settings) {
	    boolean nullAwoken = false;
	    int swapIndex = 0;
	    List<Boolean> bindStatus = null;
	    boolean[] powerUp = null;
	    int gameMode = info.gameMode;
	    boolean challengeMode = gameMode == Constants.MODE_CHALLENGE;
	    float reduceByCross = 0;
	    float crossAttack = 1f;
	    boolean copMode = false;
	    boolean singleAtkUp = false;
	    
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
	        if (settings.containsKey("copMode")) {
	        	copMode = (Boolean) settings.get("copMode");
	        }
	        if(settings.containsKey("singleUp")) {
	        	singleAtkUp = ((Integer)settings.get("singleUp")) == SinglePowerUp.ATK.value();
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
						.getTargetAwokenCount(AwokenSkill.ENHANCE_DARK_ATT, false);
				rowSkill[1] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_FIRE_ATT, false);
				// rowSkill[2] +=
				// minfo.getTargetAwokenCount(AwokenSkill.ENHANCE_HEAL);
				rowSkill[3] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_LIGHT_ATT, false);
				rowSkill[4] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_WATER_ATT, false);
				rowSkill[5] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_WOOD_ATT, false);

				plusOSkill[0] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_DARK, false);
				plusOSkill[1] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_FIRE, false);
				plusOSkill[3] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_LIGHT, false);
				plusOSkill[4] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_WATER, false);
				plusOSkill[5] += minfo
						.getTargetAwokenCount(AwokenSkill.ENHANCE_WOOD, false);
			}
			

			// calculate normal attack - atk * (rm orb factor) * (combo factor)
			List<List<Pair<Double, Double>>> normalAtk = new ArrayList<List<Pair<Double, Double>>>();
			for (int i = 0; i < 6; ++i) {
				MonsterInfo minfo = info.getMember(i);
				if (minfo == null || bindStatus.get(i)) {
					normalAtk.add(new ArrayList<Pair<Double, Double>>());
					continue;
				}
				int twoway = minfo
						.getTargetAwokenCount(AwokenSkill.TWO_PRONGED_ATTACK, false);
				int combo7 = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.COMBO_7, false);
				int combo10 = nullAwoken? 0:minfo.getTargetAwokenCount(AwokenSkill.COMBO_10, false);
				Pair<Integer, Integer> props = minfo.getProps();
				int atk = minfo.getAtk(copMode);
				List<Pair<Double, Double>> memberBasicAtk = new ArrayList<Pair<Double, Double>>();

				for (Match5x4 m : combos) {
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
						if (props.second == props.first) {
							calc = Math.ceil(damageAtk * 0.1);
						} else {
							calc = Math.ceil(damageAtk / 3.0);
						}

						atk2 = Math.ceil(calc);
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
					memberBasicAtk.add(new Pair<Double, Double>(atk1, atk2));
				}
				normalAtk.add(memberBasicAtk);
			}
			// calculate real atk = atk * normal * combo * prop factor * leader
			// factor
			int size = normalAtk.size();
			MonsterInfo leader = info.getMember(swapIndex);
			MonsterInfo friend = info.getMember(5);

			int[] rowcount = { 0, 0, 0, 0, 0, 0, 0, 0,0 };
			for (Match5x4 m : combos) {
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
						total += minfo.getTargetAwokenCount(AwokenSkill.get(data.get(3+j)), false);
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

				//LogUtil.d(i, " => factor=", f1, ", factor=", f2);
				double totalf = f1 * f2;
				double tcf1 = typeFactor * powerFactor1 * comboFactor * awokenUp;
				double tcf2 = typeFactor * powerFactor2 * comboFactor * awokenUp;
				double majoratk = 0.0;
				double minoratk = 0.0;
				

				// stage special power up
				totalf *= DamageCalculator.getPowerUp(powerUp, minfo);
				totalf *= crossAttack;
				
				
				for (Pair<Double, Double> basic : basicList) {

					double atk1 = Math.floor(basic.first * tcf1);
					double atk2 = Math.floor(basic.second * tcf2);
					
					atk1 = Math.ceil(Math.ceil(atk1 * rowPower1) * totalf);
					atk2 = Math.ceil(Math.ceil(atk2 * rowPower2) * totalf);

					majoratk += atk1;
					minoratk += atk2;
				}
				if(singleAtkUp) {
					majoratk = (int)(majoratk * 1.05);
					minoratk = (int)(minoratk * 1.05);
				}
				monsterAtk.add(new Pair<Double, Double>(majoratk, minoratk));
			}

		}

		return monsterAtk;
	}

	public static double[] getLeaderFactorAttrUp(TeamInfo team, MonsterInfo leader,
			List<Match5x4> combos) {
		double[] factor = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
		if ( leader != null ) {
			List<LeaderSkill> lst = leader.getLeaderSkill();
			if ( lst != null ) {
				for(LeaderSkill l : lst) {
					if ( l.getType() == LeaderSkillType.LST_FIVE_ORB ) {
						List<Integer> data = l.getData();
						int many = data.get(0);
						double f = data.get(1) / 100.0;
						for(Match5x4 m : combos) {
							if ( m.count == many && m.pcount > 0 ) {
								factor[m.type] = f;
							}
						}
					}
				}
			}
		}
		return factor;
	}
	
	
	private static double getLeaderFactor(TeamInfo team, LeaderSkill lskill,
			MonsterInfo monster, List<Match5x4> combos, Map<String, Object> settings) {
		List<Integer> data = lskill.getData();
		switch (lskill.getType()) {
		case LST_MORE_REMOVE: {
			double factor = data.get(1) / 100.0;
            List<Integer> type = data.subList(3, data.size());

            if (isSameType(type, monster)) {
            	return factor;
            }
			return 1.0;
		}
		case LST_MULTI_PLAY: {
			if (team.isCopMode()) {
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
			return 1.0f;
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

				for (Match5x4 m : combos) {
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
			for (Match5x4 m : combos) {
				if (orbTypes.contains(m.type) && m.count > maxRemove) {
					maxRemove = m.count;
				}
			}
			if (maxRemove >= removeN) {

				double factor = data.get(2+size+1) / 100.0;
				int maxN = data.get(3+size+1);
				if (maxN != removeN) {
					if (maxRemove >= maxN) {
						return data.get(6+size+1) / 100.0;
					}

					double addfactor = data.get(5+size+1) / 100.0;
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
			for (Match5x4 m : combos) {
				if (m.type == orbType && m.count > maxRemove) {
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
			boolean[] matchColor = { false, false, false, false, false, false };
			boolean[] memberColor = { false, false, false, false, false, false };
			int many = data.get(0);
			for (int i = 1; i <= many; ++i) {
				orbset[data.get(i)] = true;
			}
			int removeMany = data.get(many + 1);
			double factor = data.get(many + 2) / 100.0;
            List<Boolean> bindStatus = null;
            if (settings != null && settings.containsKey("bindStatus")) {
                bindStatus = (List<Boolean>)settings.get("bindStatus");
            } else {
                bindStatus = new ArrayList<Boolean>();
                for(int i=0; i<6; ++i) {
                    bindStatus.add(false);
                }
            }
            
			for (Match5x4 m : combos) {
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
	
	private static double getLeaderFactor(TeamInfo team, MonsterInfo leader,
			MonsterInfo monster, List<Match5x4> combos, Map<String, Object> settings) {
		if (leader == null) {
			return 1.0;
		}
		List<LeaderSkill> list = leader.getLeaderSkill();
		double factor = 1.0;
		for (LeaderSkill ls : list) {
			double newfactor = getLeaderFactor(team, ls, monster, combos, settings);
			factor *= newfactor;
		}
		return factor;
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
