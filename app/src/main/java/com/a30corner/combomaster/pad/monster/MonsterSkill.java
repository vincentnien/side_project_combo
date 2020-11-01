package com.a30corner.combomaster.pad.monster;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.a30corner.combomaster.R;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.utils.LogUtil;

public class MonsterSkill {
	// = { "龍", "惡魔", "平衡", "攻擊", "體力", "回復",
	// "神", "進化用", "强化合成用", "特別保護", "無" };
	// <!-- ls_become_x, power_up_text as_power_up, as_type_up -->
	public static enum MonsterType {
		DRAGON(0), DEVIL(1), BALANCED(2), ATTACKER(3), PHYSICAL(4), HEALER(5), GOD(
				6), EVO_MATRIAL(7), ENHANCE_MATRIAL(8), SPECIAL_PROTECTED(9), NONE(
				10), MACHINE(11), AWOKEN(12), SELL(13);

		private final int value;

		private MonsterType(int v) {
			value = v;
		}
		
		public int value() {
			return value;
		}

		public static MonsterType get(int v) {
			MonsterType[] values = MonsterType.values();
			if (v == -1 || v >= values.length) {
				return MonsterType.NONE;
			}
			
			return values[v];
		}
	}
	
	public static enum MoneyAwokenSkill {
		SKILL_NONE(0),
		ENHANCE_HP(1), ENHANCE_ATK(2), ENHANCE_RCV(3),
		AUTO_RECOVERY(4),EXTEND_TIME(5),REDUCE_FIRE(6),
		REDUCE_WATER(7),REDUCE_WOOD(8),REDUCE_LIGHT(9),
		REDUCE_DARK(10),
		SKILL_HINDER(11),ALL_UP(12),GOD_KILLER(13),DRAGON_KILLER(14),
		DEVIL_KILLER(15),MACHINE_KILLER(16),BALANCED_KILLER(17),
		ATTACKER_KILLER(18),PHYSICAL_KILLER(19),HEALER_KILLER(20),
		EVO_MATRIAL_KILLER(21), ENHANCE_MATRIAL_KILLER(22), KILLER_3(23),
		ENHANCE_HP_PLUS(24), ENHANCE_ATK_PLUS(25), ENHANCE_RCV_PLUS(26),
		ENHANCE_EXTEND_TIME_PLUS(27), REDUCE_FIRE_PLUS(28), REDUCE_WATER_PLUS(29),
		REDUCE_WOOD_PLUS(30), REDUCE_LIGHT_PLUS(31), REDUCE_DARK_PLUS(32),
		SELL_KILLER(33);
		
		private final int value;
		
		private MoneyAwokenSkill(int value) {
			this.value = value;
		}
		
		public static MoneyAwokenSkill get(int value) {
			MoneyAwokenSkill[] values = MoneyAwokenSkill.values();
			if ( value >= values.length || value < 0 ) {
				return SKILL_NONE;
			}
			return values[value];
		}
	}

	public static enum AwokenSkill {
		ENHANCE_HP(0), ENHANCE_ATK(1), ENHANCE_HEAL(2), REDUCE_FIRE(3), REDUCE_WATER(
				4), REDUCE_WOOD(5), REDUCE_LIGHT(6), REDUCE_DARK(7), AUTO_RECOVER(
				8), RESISTANCE_BIND(9), RESISTANCE_DARK(10), RESISTANCE_JAMMER(
				11), RESISTANCE_POISON(12), ENHANCE_FIRE(13), ENHANCE_WATER(14), ENHANCE_WOOD(
				15), ENHANCE_LIGHT(16), ENHANCE_DARK(17), EXTEND_TIME(18), RECOVER_BIND(
				19), SKILL_BOOST(20), ENHANCE_FIRE_ATT(21), ENHANCE_WATER_ATT(
				22), ENHANCE_WOOD_ATT(23), ENHANCE_LIGHT_ATT(24), ENHANCE_DARK_ATT(
				25), TWO_PRONGED_ATTACK(26), RESISTANCE_SKILL_LOCK(27), SKILL_NONE(28),
		ENHANCE_HEART(29),
		GOD_KILLER(30),MACHINE_KILLER(31),MATCH_BOOST(32),DEVIL_KILLER(33),DRAGON_KILLER(34),
		BALANCED_KILLER(35), ATTACKER_KILLER(36), PHYSICAL_KILLER(37), HEALER_KILLER(38),
		EVO_MATRIAL_KILLER(39), AWOKEN_KILLER(40), ENHANCE_MATRIAL_KILLER(41),
		COMBO_7(42), GUARD_BREAK(43),HEART_ARROW(44),TEAM_HP_UP(45),TEAM_RCV_UP(46),
		SQUARE_ATTACK(47), ASSISTANT(48),HEART_SQUARE(49),SKILL_CHARGE(50),
		RESISTANCE_BIND_PLUS(51), EXTEND_TIME_PLUS(52), RESISTANCE_KUMO(53), RESISTANCE_BIND_MOVE(54), SKILL_BOOST_PLUS(55), ATK_UP_80(56),
		ATK_UP_50(57),L_SHIELD(58),L_ATTACK(59), SELL_KILLER(60), COMBO_10(61), SKILL_VOICE(62), COMBO_DROP(63),
		DUNGEON_BONUS(64),HP_DOWN(65),ATK_DOWN(66),RCV_DOWN(67),
		RESISTANCE_DARK_PLUS(68), RESISTANCE_JAMMER_PLUS(69), RESISTANCE_POISON_PLUS(69),
		BLESSING_OF_JAMMER_DROP(70), BLESSING_OF_POISON_DROP(71);

		private final int value;

		private AwokenSkill(int value) {
			this.value = value;
		}

		public static AwokenSkill get(int value) {
			AwokenSkill[] values = AwokenSkill.values();
			if ( value >= values.length || value < 0 ) {
				return AwokenSkill.SKILL_NONE;
			}
			return values[value];
		}
	}

	public static enum SinglePowerUp {
		COST(0), MOVE(1), ATTACK_ALL(2),
		RCV(3), HP(4), ATK(5), SB(6),
		RESIST_BIND(7), RESIST_SKILL_BIND(8),
		RCV_UP(9),HP_UP(10),ATK_UP(11),MOVE_UP(12),
		NO_DROP(13);
		private final int value;

		private SinglePowerUp(int v) {
			value = v;
		}

		public int value() {
			return value;
		}

		public static SinglePowerUp from(int value) {
			SinglePowerUp[] values = SinglePowerUp.values();
			if(values.length<=value) {
				return SinglePowerUp.COST;
			}
			return values[value];
		}
	}
	
	public static enum HpCondition {
		HP_FULL(0), HP_LARGER(1), HP_BELOW(2);
		private final int value;

		private HpCondition(int v) {
			value = v;
		}

		public int value() {
			return value;
		}

		public static HpCondition parse(int value) {
			return HpCondition.values()[value];
		}
	}

	public static String getActiveSkillString(Context context,
			List<ActiveSkill> list) {
		String display = "";
		int size = list.size();
		for (int i = 0; i < size; ++i) {
			ActiveSkill t = list.get(i);
			display += getViewableString(context, t);
			if (i < size - 1) {
				display += "\n";
			}
		}
		return display;
	}

	public static String getLeaderSkillString(Context context,
			List<LeaderSkill> list) {
		String display = "";
		int size = list.size();
		for (int i = 0; i < size; ++i) {
			LeaderSkill t = list.get(i);
			display += getViewableString(context, t);
			if (i < size - 1) {
				display += "\n";
			}
		}
		return display;
	}

	public static String getViewableString(Context context, ActiveSkill skill) {
		if (skill != null && skill.getType() != null) {
			List<Integer> data = skill.getData();
			Resources res = context.getResources();
			final String[] orbType = res.getStringArray(R.array.orb_type);
			final String[] monsters = res.getStringArray(R.array.monster_type);
			switch (skill.getType()) {
				case ST_NO_DROP: {
					int turn = data.get(0);
					return context.getString(R.string.as_no_drop, turn);
				}
				case ST_ENHANCE_ORB: {
					int turn = data.get(0);
					int percent = data.get(1);
					return context.getString(R.string.as_enhance_orb, turn, percent);
				}
				case ST_TURN_RECOVER: {
					int turn = data.get(0);
					int percent = data.get(2);
					return res.getString(R.string.as_turn_recover, turn, percent);
				}
				case ST_L_FORMAT: {
					return context.getString(R.string.as_format_l, orbType[data.get(0)]);
				}
				case ST_CROSS_FORMAT: {
					String second = "";
					if (data.size() > 2) {
						second = "\n" + context.getString(R.string.as_format_cross, orbType[data.get(2)]);
					}
					return context.getString(R.string.as_format_cross, orbType[data.get(0)]) + second;
				}
				case ST_SQUARE_FORMAT: {
					return context.getString(R.string.as_format_square, orbType[data.get(0)]);
				}
				case ST_DROP_ONLY:{
					int turn = data.get(0);
					int size = data.size();
					String display = orbType[data.get(1)];
					for (int i = 2; i < size; ++i) {
						display += "," + orbType[data.get(i)];
					}
					return context.getString(R.string.as_drop_only, turn, display);
				}
			case ST_VOID_ATTR: {
				int turn = data.get(0);
				return context.getString(R.string.as_void_attr_absorb, turn);
			}
			case ST_VOID: {
				int turn = data.get(0);
				return context.getString(R.string.as_void_absorb, turn);
			}
			case ST_VOID_0: {
				int turn = data.get(0);
				return context.getString(R.string.as_void_void_shield, turn);
			}
			case ST_HEN_SHIN: {
				int id = data.get(0);
				return context.getString(R.string.as_hen_shin, id);
			}
			case ST_BIND_SKILL: {
				int turn = data.get(0);
				return context.getString(R.string.as_bind_skill, turn);
			}
			case ST_RECOVER_LOCK_REMOVE: {
				int turn = data.get(0);
				return context.getString(R.string.as_reduce_eliminate_status, turn);
			}
			case ST_UNLOCK: {
				return context.getString(R.string.as_unlock);
			}
			case ST_RECOVER_UP: {
				return context.getString(R.string.as_recover_up, data.get(0), data.get(1)/100f);
			}
			case ST_LEADER_SWITCH: {
				return context.getString(R.string.as_switch_leader);
			}
			case ST_POISON: {
				return context.getString(R.string.as_poison, data.get(0));
			}
			case ST_HP_1: {
				int x = data.get(0);
				if(x == 1) {
					return context.getString(R.string.as_hp_1);
				} else {
					return context.getString(R.string.as_hp_x, x);
				}
			}
			case ST_CHANGE_ENEMY_ATTR: {
				return context.getString(R.string.as_change_attribute, orbType[data.get(0)]);
			}
			case ST_COUNTER_ATTACK: {
				int turn = data.get(0);
				int x = data.get(1);
				int prop = data.get(2);
				return context.getString(R.string.as_counter_attack, turn, x, orbType[prop]);
			}
			case ST_RECOVER_HP: {
				int type = data.get(0);
				if(type == 0) {
					return context.getString(R.string.as_bind_recover_type2, data.get(1));
				} else if(type == 1) {
					return context.getString(R.string.as_bind_recover_type3, data.get(1));
				} else {
					return context.getString(R.string.as_bind_recover_type1, data.get(1));
				}
			}
			case ST_CLEAR_BOARD: {
				return context.getString(R.string.as_clear_board);
			}

			case ST_DIRECT_ATTACK: {
				int damage = data.get(1);
				boolean all = data.get(0) == 1;
				if(all) {
					return context.getString(R.string.as_direct_damage_all, damage);
				} else {
					return context.getString(R.string.as_direct_damage_single, damage);
				}
			}
			case ST_MOVE_TIME_X: {
				float factor = data.get(1) / 1000f;
				return context.getString(R.string.as_move_time_x, data.get(0), factor);
			}
			case ST_ADD_COMBO: {
				int turn = data.get(0);
				int add = data.get(1);
				return context.getString(R.string.as_add_combo, turn, add);
			}
			case ST_DROP_LOCK: {
				int size = data.size();
				String orbs = "";
				for(int i=1; i<size; i+=2) {
					int up = data.get(i);
					if(up == -1) {
						orbs = context.getString(R.string.orb_any);
					} else {
						orbs += orbType[up];
						if (i < size - 2) {
							orbs += ",";
						}
					}
				}
				int turn = data.get(0);
				return context.getString(R.string.as_drop_lock, turn, orbs);
			}
			case ST_GRAVITY_100: {
				return context.getString(R.string.as_gravity_100, data.get(0));
			}
			case ST_SKILL_BOOST: {
				if (data.size() == 1) {
					return context.getString(R.string.as_skill_boost, data.get(0));
				} else {
					return context.getString(R.string.as_skill_boost_random, data.get(0), data.get(1));
				}
			}
			case ST_REDUCE_SHIELD: {
				if(data.size()<3) {
					return context.getString(R.string.as_reduce_shield, data.get(0), data.get(1));
				} else {
					return context.getString(R.string.as_reduce_shield_color, data.get(0), orbType[data.get(2)]);
				}
			}
			case ST_AWOKEN_RECOVER: {
				return context.getString(R.string.as_awoken_recover, data.get(0));
			}
			case ST_BIND_RECOVER: {
				final int[] type = {
						0,
						R.string.as_bind_recover_type1,
						R.string.as_bind_recover_type2,
						R.string.as_bind_recover_type3,
				};
				int typeId = data.get(1);
				int many = data.get(2);
				int turn = data.get(0);
				if(typeId>0) {
					return context.getString(type[typeId], many)
							+context.getString(R.string.as_bind_recover, turn);
				} else {
					return context.getString(R.string.as_bind_recover, turn);
				}
			}
			case ST_HP_ATTACK: {
				final int[] multiple = { R.string.single_target,
						R.string.multiple_targets };
				String display = orbType[data.get(1)];
				String factor = data.get(2) + "x~" +data.get(3) + "x";

				String ret = context.getString(R.string.as_attack_skill_hp, display,
						factor, context.getString(multiple[data.get(0)]));
				return ret;
			}
				case ST_TEAM_HP_ATTACK: {
					final int[] multiple = { R.string.single_target,
							R.string.multiple_targets };
					String display = orbType[data.get(2)];
					String factor = data.get(1) + "x ";

					String ret = context.getString(R.string.as_attack_skill_team_hp, display,
							factor, context.getString(multiple[data.get(0)]));
					return ret;
				}
			case ST_ATTACK_FIXED:
			case ST_ATTACK: {
				final int[] multiple = { R.string.single_target,
						R.string.multiple_targets };
				String display = orbType[data.get(2)];
				int factor = data.get(1);

				if(skill.getType() == SkillType.ST_ATTACK) {
					String ret = context.getString(R.string.as_attack_skill, display,
							factor, context.getString(multiple[data.get(0)]));
					if(data.size()>=4) {
						ret += context.getString(R.string.as_drain, data.get(3));
					}
					return ret;
				} else {
					return context.getString(R.string.as_attack_skill_fix, display,
							factor, context.getString(multiple[data.get(0)]));
				}
			}
			case ST_ADD_LOCK: {
				String display = orbType[data.get(0)];
				int size = data.size();
				if (size > 1) {
					for (int i = 1; i < size; ++i) {
						display += "," + orbType[data.get(i)];
					}
				}
				return context.getString(R.string.as_add_lock, display);
			}
			case ST_ADD_PLUS: {
				String display = orbType[data.get(0)];
				int size = data.size();
				if (size > 1) {
					for (int i = 1; i < size; ++i) {
						display += "," + orbType[data.get(i)];
					}
				}
				return context.getString(R.string.as_add_plus, display);
			}
			case ST_TARGET_RANDOM: {
				List<String> orbs = new ArrayList<String>();
				List<String> toOrbs = new ArrayList<String>();
				int size = data.get(0);
				for(int i=0; i<size; ++i) {
					orbs.add(orbType[data.get(i+1)]);
				}
				int targetSize = data.get(size+1);
				for(int i=0; i<targetSize; ++i) {
					toOrbs.add(orbType[data.get(size+2+i)]);
				}
				return context.getString(R.string.as_target_random, 
						TextUtils.join(",", orbs), 
						TextUtils.join(",", toOrbs));
			}
			case ST_CHANGE_THE_WORLD: {
				return context.getString(R.string.as_change_the_world,
						data.get(0));
			}
			case ST_COLOR_CHANGE: {
				int size = data.size();
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < size; i += 2) {
					sb.append(context.getString(R.string.as_color_change,
							orbType[data.get(i)], orbType[data.get(i + 1)]));
				}
				return sb.toString();
			}
			case ST_TIME_EXTEND: {
				int turn = data.get(0);
				float sec = data.get(1) / 1000;
				return context.getString(R.string.as_time_extend, turn, sec);
			}
			case ST_POWER_UP: {
				int turn = data.get(0);
				int size = data.size();
				String monsterType = "";
				for (int i = 2; i < size; ++i) {
					int up = data.get(i);
					monsterType += orbType[up];
					if (i < size - 1) {
						monsterType += ",";
					}
				}

				String factor = getNoZeroFactor(data.get(1));
				return context.getString(R.string.as_power_up, turn,
						monsterType, factor);

			}
			case ST_TYPE_UP:
			// 3, 11, 5
			{
				int turn = data.get(0);
				int size = data.size();
				String monsterType = "";
				for (int i = 2; i < size; ++i) {
					int up = data.get(i);
					monsterType += monsters[up];
					if (i < size - 1) {
						monsterType += ",";
					}
				}

				String factor = getNoZeroFactor(data.get(1));
				return context.getString(R.string.as_type_up, turn,
						monsterType, factor);

			}
			case ST_TRANSFORM: {
				int size = data.size() - 1;
				StringBuilder orbList = new StringBuilder();
				for (int i = 0; i < size; ++i) {
					orbList.append(orbType[data.get(i)]).append(",");
				}
				orbList.append(orbType[data.get(size)]);
				return context.getString(R.string.as_transform,
						orbList.toString());
			}
			case ST_GRAVITY: {
				return context.getString(R.string.as_gravity, data.get(0));
			}
			case ST_ONE_LINE_TRANSFORM: {
				final String[] position = res
						.getStringArray(R.array.line_position);
				int pos = data.get(0);
				int orb = data.get(1);

				return context.getString(R.string.as_one_line_transform,
						position[pos], orbType[orb]);
			}
			case ST_REDUCE_DEF: {
				int turn = data.get(0);
				int percent = data.get(1);
				return context.getString(R.string.as_reduce_def, turn, percent);
			}
			case ST_DELAY: {
				int turn = data.get(0);
				return context.getString(R.string.as_delay, turn);
			}
			case ST_RANDOM_CHANGE_RESTRICT_MORE: {
			    int size = data.size();

			    StringBuilder sb = new StringBuilder();
                int a;
                for (a = 0; a < size; a += 4) {
                    int color = data.get(a);
                    int count = data.get(a+1);
                    String except = orbType[data.get(a+2)];
                    if(data.get(a+2) != data.get(a+3)) {
                        except += "," + orbType[data.get(a+3)];
                    }

                    sb.append(context.getString(R.string.as_random_change_restrict, count,
                            except,
                            orbType[color]));
                    sb.append("\n");
                }

				return sb.toString();
			}
			case ST_RANDOM_CHANGE_RESTRICT: {
                int color = data.get(0);
                int count = data.get(1);
                int except = data.get(2);
                int size = data.size();
                if (size <= 3) {
                    return context.getString(R.string.as_random_change_restrict, count,
                            orbType[except],
                            orbType[color]);
                } else {
                	String exceptStr = orbType[except];
                	if ((data.size()%3) > 0) {
                		exceptStr += "," + orbType[data.get(data.size()-1)];
					}
                    String combind = context.getString(
                            R.string.as_random_change_restrict, count, exceptStr, orbType[color]);
                    for (int i = 3; i < size; i += 3) {
                        int c = data.get(i);
                        int cnt = data.get(i + 1);
                        int ex = data.get(i+2);
                        combind += "\n"
                                + context.getString(R.string.as_random_change_restrict,
                                        cnt, orbType[ex], orbType[c]);
                    }
                    return combind;
                }
			}
			case ST_RANDOM_CHANGE_FIX:
			case ST_RANDOM_CHANGE: {
				int color = data.get(0);
				int count = data.get(1);
				int size = data.size();
				if (size <= 2) {
					return context.getString(R.string.as_random_change, count,
							orbType[color]);
				} else {
					String combind = context.getString(
							R.string.as_random_change, count, orbType[color]);
					for (int i = 2; i < size; i += 2) {
						int c = data.get(i);
						int cnt = data.get(i + 1);
						combind += "\n"
								+ context.getString(R.string.as_random_change,
										cnt, orbType[c]);
					}
					return combind;
				}
			}
			case ST_DROP_RATE:
			    int size = data.size();
			    String orbs = "";
			    for(int i=1; i<size; i+=2) {
                    int up = data.get(i);
                    orbs += orbType[up];
                    if (i < size - 2) {
                        orbs += ",";
                    }
			    }
			    int turn = data.get(0);
                int percent = data.get(2);
			    return context.getString(R.string.as_drop_rate, turn, orbs, percent);
			case ST_DEF_UP_BY_AWOKEN:
			{
				turn = data.get(0);
				percent = data.get(1);
				size = data.get(2);
				List<Integer> awoken = data.subList(3, 3+size);
				List<String> awokenString = new ArrayList<String>();

				for(Integer awk : awoken) {
					awokenString.add(getAwokenSkill(context, awk));
				}

				String skills = TextUtils.join(",", awokenString);
				return context.getString(R.string.as_def_up_by_awoken, turn, skills, percent);
			}
			case ST_POWER_UP_BY_AWOKEN:
			{
				turn = data.get(0);
				percent = data.get(1);
				size = data.get(2);
				List<Integer> awoken = data.subList(3, 3+size);
				List<String> awokenString = new ArrayList<String>();
				
				for(Integer awk : awoken) {
					awokenString.add(getAwokenSkill(context, awk));
				}
				
				String skills = TextUtils.join(",", awokenString);
				return context.getString(R.string.as_power_up_by_awoken, turn, skills, percent);
			}
			case ST_NOT_SUPPORT:
				break;
			default:
				break;

			}
		}
		return context.getString(R.string.ls_not_support);
	}

	private static String getNoZeroFactor(int factor) {
		int count = (factor % 10) == 0 ? 1 : 2;

		return String.format("%1." + count + "f", (factor / 100.0f));
	}

	public static String getViewableString(Context context, LeaderSkill skill) {
		if (skill != null && skill.getType() != null) {
			String[] arrProps = context.getResources().getStringArray(
					R.array.orb_type);
			List<Integer> data = skill.getData();
			switch (skill.getType()) {
			case LST_MORE_REMOVE: {
				int n = data.get(0);
				float factor = data.get(1) / 100f;
				int count = data.get(2);
				List<String> up = new ArrayList<String>(count);
				for(int i=0; i<count; ++i) {
					int prop = data.get(3+i);
					if(prop != -1) {
						up.add(arrProps[prop]);
					} else {
						up.add(context.getString(R.string.ls_all_monster));
					}
				}
				return context.getString(R.string.lst_more_remove, n, TextUtils.join(",", up), factor);
			}
			case LST_L_ATTACK: {
				int[] typeString = {R.string.lst_attr_0, R.string.lst_attr_1, R.string.lst_attr_2, R.string.lst_attr_3};
				int colorcnt = data.get(0);
				int orb = data.get(1);
				StringBuilder color;
				if(orb == -1) {
					color = new StringBuilder(context.getString(R.string.orb_any));
				} else {
					color = new StringBuilder(arrProps[data.get(1)]);
				}
				for(int i=1; i<colorcnt; ++i) {
					color.append(",").append(arrProps[data.get(1+i)]);
				}
				int offset = colorcnt + 1;
				int count = data.get(offset);
				StringBuilder sb = new StringBuilder(context.getString(R.string.lst_l_format, color.toString()));
				for(int i=0; i<count; ++i) {
					int type = data.get(offset+1+i*2);
					int factor = data.get(offset+1+i*2+1);
					if(type == 1 || type == 2) {
						sb.append(context.getString(typeString[type], factor/100f));
					} else {
						sb.append(context.getString(typeString[type], factor));
					}
				}
				return sb.toString();
			}
			case LST_COMBO_ATTACK: {
				int[] typeString = {R.string.lst_attr_0, R.string.lst_attr_1, R.string.lst_attr_2, R.string.lst_attr_3};
				int colorcnt = data.get(0);
				String colors = arrProps[data.get(1)];
				for(int i=1; i<colorcnt; ++i) {
					colors += "," + arrProps[data.get(1+i*2)];
				}
				int needcount = data.get(2);
				int factorcount = data.get(1+colorcnt*2);
				StringBuilder factor = new StringBuilder();
				for(int i=0; i<factorcount; ++i) {
					int type = data.get(2+colorcnt*2+i*2);
					int f = data.get(2+colorcnt*2+i*2+1);
					if(type == 1 || type == 2) {
						factor.append(context.getString(typeString[type], f/100f));
					} else {
						factor.append(context.getString(typeString[type], f));
					}
				}
				return context.getString(R.string.lst_orb_combo_attack, needcount, colors, factor.toString());
			}
			case LST_COLOR_COMBO: {
				int colorcnt = data.get(0);
				int factor = data.get(2 + colorcnt);

				return context.getString(R.string.lst_attr_3, factor);
			}
				case LST_TARGET_ORB_ADD_COMBO: {
					int factor = data.get(data.size()-1);

					return context.getString(R.string.lst_attr_3, factor);
				}
			case LST_COUNTER_STRIKE: {
				int percent = data.get(0);
				int x = data.get(1);
				int orb = data.get(2);
				return context.getString(R.string.lst_counter_strike, percent, arrProps[orb], x);
			}
			case LST_NO_POISON: {
				return context.getString(R.string.lst_no_poison);
			}

			case LST_CROSS_ATTACK: {
				float x = data.get(0) / 100f;
				int colorcnt = data.get(1);
				String[] orbs = new String[colorcnt];
				for (int j = 0; j < colorcnt; ++j) {
					int color = data.get(2+j);
					orbs[j] = arrProps[color];
				}
				return context.getString(R.string.lst_cross_attack, TextUtils.join(",", orbs), x);
			}
			case LST_HP_RECOVER: {
				int many = data.get(1);
				return context.getString(R.string.lst_hp_recover, many);
			}
			case LST_COUNT_ORB: {
                int powerupcnt = data.get(2);
                int[] powerup = new int[powerupcnt * 2];
                for (int i = 0; i < powerupcnt; ++i) {
                    int index = i * 2;
                    powerup[index] = data.get(3 + index);
                    powerup[index + 1] = data.get(3 + index + 1);
                }
                Resources res = context.getResources();
                String monsterType = context.getString(R.string.ls_all_monster);

                String[] arrUp = res.getStringArray(R.array.power_up);
                StringBuilder sb1 = new StringBuilder();
                for (int i = 0; i < powerupcnt; ++i) {
                    int index = i * 2;

                    String factor = getNoZeroFactor(powerup[index + 1]);
                    sb1.append(context.getString(R.string.ls_become_x,
                            arrUp[powerup[index]], factor));
                    if (i != powerupcnt - 1) {
                        sb1.append(",");
                    }
                }
                int orb = data.get(1);
                return context.getString(R.string.lst_remove_n, arrProps[orb], data.get(0)) + monsterType + sb1.toString();
			}
			case LST_EXTEND_TIME: {
				float time = data.get(0) / 1000f;
				return context.getString(R.string.lst_time_extend, time);
			}
			case LST_CROSS_P:
			{
				int percent = data.get(2);
				float atk = (data.get(0)==1)? data.get(1)/100f:0;
				return context.getString(R.string.lst_cross_atk_def, percent, atk);
			}
			case LST_CROSS: {
				float atk = (data.get(0)==1)? data.get(1)/100f:0;
				if(atk>0) {
					return context.getString(R.string.as_cross_atk, atk);
				} else {
					return context.getString(R.string.as_cross); //atk
				}
			}
			case LST_HEAL: {
				int percent = data.get(0);
				return context.getString(R.string.lst_heal, percent);
			}
			case LST_DO_KONJO: {
				int percent = data.get(0);
				return context.getString(R.string.lst_do_konjo, percent,
						percent);
			}
			case LST_TIME_FIXED:{
				float time = data.get(0)/1000f;
				return context.getString(R.string.lst_time_fixed, time);
			}
			case LST_REDUCE_DAMAGE: {
				int percent = data.get(0);
				int cnt = data.get(1);
				String types = "";
				for (int i = 0; i < cnt; ++i) {
					int type = data.get(2 + i);
					if (type != -1) {
						types += arrProps[data.get(2 + i)];
						if (i < cnt - 1) {
							types += ",";
						}
					} else {
						types += context.getString(R.string.ls_all_monster);
					}
				}
				return context.getString(R.string.lst_reduce_damage, percent,
						types);
			}
			case LST_REDUCE_BY_HP_CONDITION: {
				int hpCondition = data.get(0);
				int percent = data.get(1);
				boolean bigger = data.size()>2;
				if (hpCondition == 100) {
					return context.getString(R.string.lst_reduce_by_hp_full,
							percent);
				} else if(!bigger){
					return context.getString(R.string.lst_reduce_by_hp_50,
							hpCondition, percent);
				} else {
					return context.getString(R.string.lst_reduce_by_hp_50_up,
							hpCondition, percent);
				}

			}
				case LST_MULTI_ORB_RCV: {
					float x = data.get(2) / 100.0f;
					return context.getString(R.string.lst_rcv, x);
				}
				case LST_MULTI_ORB_SHIELD_MULTI: {
					int removeN = data.get(0);
					//String orb = arrProps[data.get(1)];
					int offset = data.get(1);
					int factor = data.get(2+offset);
					int maxN = data.get(3+offset);
					if (maxN > removeN) {
//						float addFactor = data.get(5);
//						float maxFactor = data.get(6);
//
//						String ret = context.getString(
//								R.string.ls_multi_orb_combo_addition, removeN, orb,
//								factor, addFactor, maxFactor, maxN);
//						return ret;
					} else {
						return context.getString(R.string.ls_multi_orb_shield, factor);
					}
				}
				case LST_MULTI_ORB_SHIELD: {
					int removeN = data.get(0);
					//String orb = arrProps[data.get(1)];
					int factor = data.get(2);
					int maxN = data.get(3);
					if (maxN > removeN) {
//						float addFactor = data.get(5);
//						float maxFactor = data.get(6);
//
//						String ret = context.getString(
//								R.string.ls_multi_orb_combo_addition, removeN, orb,
//								factor, addFactor, maxFactor, maxN);
//						return ret;
					} else {
						return context.getString(R.string.ls_multi_orb_shield, factor);
					}
				}
				case LST_TARGET_ORB_SHIELD: {
					StringBuilder sb = new StringBuilder();
					int count = data.get(0);
					int pos = 1;
					for (int i = 0; i < count; ++i) {
						int colorcnt = data.get(pos);
						String[] orbs = new String[colorcnt];
						for (int j = 0; j < colorcnt; ++j) {
							int color = data.get(pos + j + 1);
							orbs[j] = arrProps[color];
						}
						int factor = data.get(pos + colorcnt + 1);

						sb.append(context.getString(R.string.ls_target_orb_shield,
								colorcnt, TextUtils.join(",", orbs), factor));
						pos += colorcnt + 2;
					}
					return sb.toString();
				}
				case LST_TARGET_ORB_DIRECT_ATTACK:{
					StringBuilder sb = new StringBuilder();
					int count = data.get(0);
					int pos = 1;
					for (int i = 0; i < count; ++i) {
						int colorcnt = data.get(pos);
						String[] orbs = new String[colorcnt];
						for (int j = 0; j < colorcnt; ++j) {
							int color = data.get(pos + j + 1);
							orbs[j] = arrProps[color];
						}
						int factor = data.get(pos + colorcnt + 1);

						sb.append(context.getString(R.string.ls_target_orb_combo_direct_damage,
								colorcnt, TextUtils.join(",", orbs), factor));
						pos += colorcnt + 2;
					}
					return sb.toString();
				}
			case LST_TARGET_ORB_COMBO: {
				StringBuilder sb = new StringBuilder();
				int count = data.get(0);
				int pos = 1;
				for (int i = 0; i < count; ++i) {
					int colorcnt = data.get(pos);
					String[] orbs = new String[colorcnt];
					for (int j = 0; j < colorcnt; ++j) {
						int color = data.get(pos + j + 1);
						orbs[j] = arrProps[color];
					}
					float factor = data.get(pos + colorcnt + 1) / 100f;

					sb.append(context.getString(R.string.ls_target_orb_combo,
							colorcnt, TextUtils.join(",", orbs), factor));
					pos += colorcnt + 2;
				}
				return sb.toString();
			}
			case LST_DROP_NO_MORE: {
				double factor = data.get(1) / 100.0;
				int trigger = data.get(0);
				String addition = "";
				if (data.size()>2) {
					addition = String.format(" (Max: %.2fx)", data.get(3)/100.0);
				}
				return context.getString(R.string.lst_drop_no_more, trigger, factor) + addition;
			}
			case LST_COMBO_SHIELD: {
				int combocnt = data.get(0);
				int start = data.get(1);
				int startFactor = data.get(2);

				String display = context.getString(R.string.ls_combo_shield, start, (startFactor));
				if (combocnt > 1) {
					int index = combocnt * 2;
					int end = data.get(index - 1);
					int endFactor = data.get(index);

					String addition = context.getString(
							R.string.ls_combo_shield_addition, end,
							(endFactor - startFactor) / (end - start),
							endFactor);

					display += addition;
				}
				return display;
			}
			case LST_COLOR_SHIELD: {
				int colorcnt = data.get(0);
				List<Integer> colors = data.subList(1, 1 + colorcnt);
				StringBuilder sb = new StringBuilder();
				int sizem1 = colors.size() - 1;
				for (int i = 0; i < sizem1; ++i) {
					sb.append(arrProps[colors.get(i)]).append(",");
				}
				sb.append(arrProps[colors.get(sizem1)]);

				int factor = data.get(2 + colorcnt);

				String display = context.getString(R.string.lst_color_reduce_shield, factor);

				return display;
			}
			case LST_COLOR_FACTOR: {
				// 6,1,4,5,3,0,2,3,30,6,4,35,5,40,6,45
				// 3,4,3,0,2,30,2,3,35
				int colorcnt = data.get(0);
				List<Integer> colors = data.subList(1, 1 + colorcnt);
				StringBuilder sb = new StringBuilder();
				int sizem1 = colors.size() - 1;
				for (int i = 0; i < sizem1; ++i) {
					sb.append(arrProps[colors.get(i)]).append(",");
				}
				sb.append(arrProps[colors.get(sizem1)]);

				int removeN = data.get(1 + colorcnt);
				float factor = data.get(2 + colorcnt) / 100f;

				int additionCnt = data.get(3 + colorcnt);
				String display = context.getString(R.string.ls_color_factor,
						sb.toString(), removeN, factor);
				int index = 3 + colorcnt;
				if (additionCnt > 0) {
					float factorMax = data.get(index + additionCnt) / 100f;
					int removeMax = data.get(index + additionCnt - 1);
					float addFactor = (factorMax - factor)
							/ (removeMax - removeN);
					display += context.getString(
							R.string.ls_color_factor_addition, addFactor,
							factorMax);
				}
				if(data.size()>index+additionCnt+1) {
					int rcv = data.get(index+additionCnt+1);
					if(rcv != 0) {
						return display + context.getString(R.string.lst_rcv, rcv / 100.0f);
					}
				}

				return display;
			}
				case LST_COLOR_DIRECT_ATTACK: {
					// 6,1,4,5,3,0,2,3,30,6,4,35,5,40,6,45
					// 3,4,3,0,2,30,2,3,35
					int colorcnt = data.get(0);
					List<Integer> colors = data.subList(1, 1 + colorcnt);
					StringBuilder sb = new StringBuilder();
					int sizem1 = colors.size() - 1;
					for (int i = 0; i < sizem1; ++i) {
						sb.append(arrProps[colors.get(i)]).append(",");
					}
					sb.append(arrProps[colors.get(sizem1)]);

					int removeN = data.get(1 + colorcnt);
					int factor = data.get(2 + colorcnt);

					return context.getString(R.string.ls_color_direct_attack,
							sb.toString(), removeN, factor);

				}
			case LST_MULTI_ORB_COMBO_MULTI: {
				// [4,3,25,4]
				int removeN = data.get(0);
				int size = data.get(1);
				List<String> types = new ArrayList<String>(size);
				for(int i=0; i<size; ++i) {
					types.add(arrProps[data.get(2+i)]);
				}
				String orb = TextUtils.join(",", types);
				float factor = data.get(2+size) / 100.0f;
				int maxN = data.get(3+size);
				if (maxN > removeN) {
					float addFactor = data.get(5+size) / 100f;
					float maxFactor = data.get(6+size) / 100f;
					float rcv = 1.0f;
					int xsize = data.size();
					for(int x=7+size; x<xsize; ++x) {
						rcv = data.get(x) / 100f;
					}

					String ret = context.getString(
							R.string.ls_multi_orb_combo_addition, removeN, orb,
							factor, addFactor, maxFactor, maxN);
					if(rcv > 1.0f) {
						return ret + context.getString(R.string.lst_rcv_max, rcv);
					} else {
						return ret;
					}
				} else {
					float rcv = 1.0f;
					if(data.size()>4+size) {
						rcv = data.get(4+size) / 100f;
					}
					String ret = context.getString(R.string.ls_multi_orb_combo,
							removeN, orb, factor);
					if(rcv > 1.0f) {
						return ret + context.getString(R.string.lst_rcv, rcv);
					} else {
						return ret;
					}
				}

			}
			case LST_MULTI_ORB_COMBO: {
				// [4,3,25,4]
				int removeN = data.get(0);
				String orb;
				if (data.get(1) != -1) {
					orb = arrProps[data.get(1)];
				} else {
					orb = context.getString(R.string.orb_any);
				}
				float factor = data.get(2) / 100.0f;
				int maxN = data.get(3);
				if (maxN > removeN) {
					float addFactor = data.get(5) / 100f;
					float maxFactor = data.get(6) / 100f;
					float rcv = 1.0f;
					if(data.size()>7) {
						rcv = data.get(7) / 100f;
					}

					String ret = context.getString(
							R.string.ls_multi_orb_combo_addition, removeN, orb,
							factor, addFactor, maxFactor, maxN);
					if(rcv > 1.0f) {
						return ret + context.getString(R.string.lst_rcv, rcv);
					} else {
						return ret;
					}
				} else {
					float rcv = 1.0f;
					if(data.size()>4) {
						rcv = data.get(4) / 100f;
					}
					String ret = context.getString(R.string.ls_multi_orb_combo,
							removeN, orb, factor);
					if(rcv > 1.0f) {
						return ret + context.getString(R.string.lst_rcv, rcv);
					} else {
						return ret;
					}
				}

			}
				case LST_MULTI_ORB_DIRECT_ATTACK: {
					// [4,3,25,4]
					int removeN = data.get(0);
					String orb;
					if (data.get(1) != -1) {
						orb = arrProps[data.get(1)];
					} else {
						orb = context.getString(R.string.orb_any);
					}
					return context.getString(R.string.ls_multi_direct_attack,
							removeN, orb, data.get(2));

				}
			case LST_USED_SKILL: {
			    int typecnt = data.get(0);
                Integer[] types = new Integer[typecnt];
                for (int i = 0; i < typecnt; ++i) {
                    types[i] = data.get(i + 1);
                }
                int powerupcnt = data.get(1 + typecnt);
                int[] powerup = new int[powerupcnt * 2];
                for (int i = 0; i < powerupcnt; ++i) {
                    int index = i * 2;
                    powerup[index] = data.get(2 + typecnt + index);
                    powerup[index + 1] = data.get(2 + typecnt + index + 1);
                }
                Resources res = context.getResources();
                String monsterType;
                if (typecnt == 1 && types[0] == -1) {
                    monsterType = context.getString(R.string.ls_all_monster);
                } else {
                    monsterType = getMonsterType(context, types, typecnt);
                }
                String[] arrUp = res.getStringArray(R.array.power_up);
                StringBuilder sb1 = new StringBuilder();
                for (int i = 0; i < powerupcnt; ++i) {
                    int index = i * 2;
                    // sb1.append(String.format("%1.1fx ",
                    // (float)(powerup[index+1])/10f));
                    // sb1.append(arrUp[powerup[index]]);

                    String factor = getNoZeroFactor(powerup[index + 1]);
                    sb1.append(context.getString(R.string.ls_become_x,
                            arrUp[powerup[index]], factor));
                    if (i != powerupcnt - 1) {
                        sb1.append(",");
                    }
                }
                return context.getString(R.string.lst_used_skill) + monsterType + sb1.toString();
			}
			case LST_BOARD: {
				String[] board = {"6x5","7x6", "5x4"};
				return context.getString(R.string.lst_change_board, board[data.get(0)]);
			}
			case LST_COMBO_RCV: {
				int combocnt = data.get(0);
				int start = data.get(1);
				int startFactor = data.get(2);

				String display = context.getString(R.string.ls_combo_rcv,
						start, startFactor / 100f);
				if (combocnt > 1) {
					int index = combocnt * 2;
					int end = data.get(index - 1);
					int endFactor = data.get(index);

					String addition = context.getString(
							R.string.ls_combo_rcv_addition, end,
							(endFactor - startFactor) / (end - start) / 100f,
							endFactor / 100f);

					display += addition;
				}
				return display;
			} 
			case LST_COMBO_FACTOR: {
				// [4,4,25,5,30,6,35,7,40]
				int combocnt = data.get(0);
				int start = data.get(1);
				int startFactor = data.get(2);

				String display = context.getString(R.string.ls_combo_factor,
						start, startFactor / 100f);
				if (combocnt > 1) {
					int index = combocnt * 2;
					int end = data.get(index - 1);
					int endFactor = data.get(index);

					String addition = context.getString(
							R.string.ls_combo_factor_addition, end,
							(endFactor - startFactor) / (end - start) / 100f,
							endFactor / 100f);

					display += addition;
				}
				return display;
			}

			case LST_ATTACK: {
				return context.getString(R.string.lst_attack, data.get(0));
			}
			case LST_NO_DROP: {
				return context.getString(R.string.lst_no_drop);
			}
			case LST_HEART_FACTOR: {
				int hp = data.get(1);
				String display = context.getString(R.string.lst_heart, hp);
				int powerupcnt = data.get(2);
				int[] powerup = new int[powerupcnt * 2];
				for (int i = 0; i < powerupcnt; ++i) {
					int index = i * 2;
					powerup[index] = data.get(3 + index);
					powerup[index + 1] = data.get(3 + index + 1);
				}
				Resources res = context.getResources();
				String[] arrUp = res.getStringArray(R.array.power_up);
				String[] leaderPower = res.getStringArray(R.array.leader_power);
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < powerupcnt; ++i) {
					int index = i * 2;

					String factor;
					if (powerup[index] <= 2) {
						factor = getNoZeroFactor(powerup[index + 1]);
						sb.append(context.getString(R.string.ls_become_x,
								arrUp[powerup[index]], factor));
					} else {
						String str = String.format(leaderPower[powerup[index]-3], powerup[index + 1]);
						sb.append(context.getString(R.string.ls_reduce_x, str));
					}

					if (i != powerupcnt - 1) {
						sb.append(",");
					}
				}
				return display + sb.toString();
			}
			case LST_FACTOR: {
				// [1,1,2,0,20,2,20]
				int typecnt = data.get(0);
				Integer[] types = new Integer[typecnt];
				for (int i = 0; i < typecnt; ++i) {
					types[i] = data.get(i + 1);
				}
				int powerupcnt = data.get(1 + typecnt);
				int[] powerup = new int[powerupcnt * 2];
				for (int i = 0; i < powerupcnt; ++i) {
					int index = i * 2;
					powerup[index] = data.get(2 + typecnt + index);
					powerup[index + 1] = data.get(2 + typecnt + index + 1);
				}
				Resources res = context.getResources();
				String monsterType;
				if (typecnt == 1 && types[0] == -1) {
					monsterType = context.getString(R.string.ls_all_monster);
				} else {
					monsterType = getMonsterType(context, types, typecnt);
				}
				String[] arrUp = res.getStringArray(R.array.power_up);
				StringBuilder sb1 = new StringBuilder();
				for (int i = 0; i < powerupcnt; ++i) {
					int index = i * 2;
					// sb1.append(String.format("%1.1fx ",
					// (float)(powerup[index+1])/10f));
					// sb1.append(arrUp[powerup[index]]);

					String factor = getNoZeroFactor(powerup[index + 1]);
					sb1.append(context.getString(R.string.ls_become_x,
							arrUp[powerup[index]], factor));
					if (i != powerupcnt - 1) {
						sb1.append(",");
					}
				}
				return monsterType + sb1.toString();
			}
			case LST_HP_FACTOR: {
				// [1,80,1,14,1,1,35]
			    int size = data.size();
			    int offset = 0;
			    StringBuilder sb = new StringBuilder();
			    while(offset<size) {
			        List<Integer> partial = data.subList(offset, size);
    				String hp = getHpFactorString(context, partial);
    				sb.append(hp);
    				
                    int powerStart = offset + 2 + data.get(offset+2) + 1;
                    int nextStartPos = powerStart + data.get(powerStart) * 2 + 1;
    				offset = nextStartPos;
    				if ( offset < size ) {
    				    sb.append("\n");
    				} else {
    				    break;
    				}
			    }
				return sb.toString();
			}
			case LST_FIVE_ORB: {
				// [5,150]
				int many = data.get(0);
				float factor = data.get(1) / 100f;
				return context.getString(R.string.lst_five_orbs, many, factor);
			}
			case LST_MULTI_PLAY: {
				int typecnt = data.get(0);
				Integer[] types = new Integer[typecnt];
				for (int i = 0; i < typecnt; ++i) {
					types[i] = data.get(i + 1);
				}
				int powerupcnt = data.get(1 + typecnt);
				int[] powerup = new int[powerupcnt * 2];
				for (int i = 0; i < powerupcnt; ++i) {
					int index = i * 2;
					powerup[index] = data.get(2 + typecnt + index);
					powerup[index + 1] = data.get(2 + typecnt + index + 1);
				}
				Resources res = context.getResources();
				String monsterType;
				if (typecnt == 1 && types[0] == -1) {
					monsterType = context.getString(R.string.ls_all_monster);
				} else {
					monsterType = getMonsterType(context, types, typecnt);
				}
				String[] arrUp = res.getStringArray(R.array.power_up);
				StringBuilder sb1 = new StringBuilder();
				for (int i = 0; i < powerupcnt; ++i) {
					int index = i * 2;
					// sb1.append(String.format("%1.1fx ",
					// (float)(powerup[index+1])/10f));
					// sb1.append(arrUp[powerup[index]]);

					String factor = getNoZeroFactor(powerup[index + 1]);
					sb1.append(context.getString(R.string.ls_become_x,
							arrUp[powerup[index]], factor));
					if (i != powerupcnt - 1) {
						sb1.append(",");
					}
				}
				return context.getString(R.string.ls_multi_mode) + monsterType + sb1.toString();
			}
			case LST_NOT_SUPPORT:
			default:
				break;
			}
		}
		return context.getString(R.string.ls_not_support);
	}
	
	private static String getHpFactorString(Context context, List<Integer> data) {
	    int gtlt = data.get(0);
        int hpLevel = data.get(1);
        int typecnt = data.get(2);
        Integer[] types = new Integer[typecnt];
        for (int i = 0; i < typecnt; ++i) {
            types[i] = data.get(3 + i);
        }
        int powerupcnt = data.get(3 + typecnt);
        int[] powerup = new int[powerupcnt * 2];
        for (int i = 0; i < powerupcnt; ++i) {
            int index = i * 2;
            if ((4 + typecnt + index) < data.size() - 1) {
                powerup[index] = data.get(4 + typecnt + index);
                powerup[index + 1] = data.get(4 + typecnt + index + 1);
            } else {
                powerup[index] = 0;
                powerup[index + 1] = 100;
            }
        }
        String hpCondition;
        if (gtlt == 0) {
            hpCondition = context
                    .getString(R.string.ls_hp_condition_full);
        } else if (gtlt == 1) {
            hpCondition = context.getString(R.string.ls_hp_condition,
                    ">=", hpLevel);
        } else {
            hpCondition = context.getString(R.string.ls_hp_condition,
                    "<", hpLevel);
        }
        Resources res = context.getResources();
        String monsterType;
        if (typecnt == 1 && types[0] == -1) {
            monsterType = context.getString(R.string.ls_all_monster);
        } else {
            monsterType = getMonsterType(context, types, typecnt);
        }
        String[] arrUp = res.getStringArray(R.array.power_up);
        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < powerupcnt; ++i) {
            int index = i * 2;
            // sb1.append(String.format("%1.1fx ",
            // (float)(powerup[index+1])/10f));
            // sb1.append(arrUp[powerup[index]]);
            String factor = getNoZeroFactor(powerup[index + 1]);
            sb1.append(context.getString(R.string.ls_become_x,
                    arrUp[powerup[index]], factor));
            if (i != powerupcnt - 1) {
                sb1.append(",");
            }
        }
        return hpCondition + monsterType + sb1.toString();
	}

	private static String getMonsterType(Context context, Integer[] types,
			int typecnt) {
		Resources res = context.getResources();
		String[] arrMonsterType = res.getStringArray(R.array.monster_type);
		String[] arrProps = res.getStringArray(R.array.orb_type);
		String rcv = context.getString(R.string.rcv);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < typecnt; ++i) {
			int type = types[i];
			if (type < MonsterSkill.COLOR.length) {
				if (type == 2) {
					sb.append(rcv);
				} else {
					sb.append(arrProps[type]);
				}
			} else {
				sb.append(arrMonsterType[type - MonsterSkill.COLOR.length]);
			}
			if (i != typecnt - 1) {
				sb.append(",");
			}
		}
		return context.getString(R.string.ls_monster_type, sb.toString());
	}


	public static final String[] COLOR = { "暗", "火", "心", "光", "水", "木", "毒",
			"妨礙" };

	public static int getColorType(String type) {
		if (TextUtils.isEmpty(type)) {
			return -1;
		}
		type = type.trim();
		for (int i = 0; i < COLOR.length; ++i) {
			if (COLOR[i].equals(type)) {
				return i;
			}
		}
		// if ( "回復".equals(prop) ) {
		// return 2;
		// }
		return -1;
	}

	public static String getAwokenSkill(Context context, int id) {
		String[] data = context.getResources().getStringArray(
				R.array.awoken_skills);
		if (id < data.length) {
			return data[id];
		}
		return "";
	}

	public static int getAwokenSkillId(Context context, String skill) {
		String[] data = context.getResources().getStringArray(
				R.array.awoken_skills);
		for (int i = 0; i < data.length; ++i) {
			String d = data[i];
			LogUtil.e(d, " : ", skill);
			if (d.equals(skill)) {
				return i;
			}
		}
		return 0;
	}
}
