package com.a30corner.combomaster.playground.entity;

import java.util.List;

import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.pad.enemy.EnemySkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.pad.monster.Skill;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.texturepacker.SimulateAssets;

public class SkillEntity extends Entity {

	public Skill skill;
	private Text turnText;
	private int turns = 0;
	public int which = 0;
	
	public SkillEntity(IEnvironment pad) {
		super(pad);
	}

	public int decreaseAndGet(int t) {
		turns -= t;
		if(turns < 0) {
			turns = 0;
		}
		return turns;
	}

	public boolean countDown() {
		if (--turns <= 0) {
			detach();
			return true;
		}
		turnText.setText(String.valueOf(turns));
		return false;
	}
	
	public float getWidth() {
		return turnText.getX() + turnText.getWidth();
	}
	
	public void initSkill(int x, int y, Skill skill) {
		this.skill = skill;
		
		VertexBufferObjectManager vbom = envRef.get().getScene().vbom;
		int id = getAssetId(skill);		
		ResourceManager res = ResourceManager.getInstance();
		ITextureRegion region = res.getTextureRegion(SimulateAssets.class, id);
		create(x, y, region, vbom);

		int offset = 0;
		if(skill instanceof ActiveSkill) {
			List<Integer> data = skill.getData();
			ActiveSkill as = (ActiveSkill)skill;
			SkillType type = as.getType();
			if (type.equals(SkillType.ST_POWER_UP)) {
				if(data.size()>3) {
					final int[] mapping = {SimulateAssets.S_DARK_ID, SimulateAssets.S_FIRE_ID,
							SimulateAssets.S_HEART_ID, SimulateAssets.S_LIGHT_ID, SimulateAssets.S_WATER_ID,
							SimulateAssets.S_WOOD_ID, SimulateAssets.S_POISON_ID, SimulateAssets.S_JARMA_ID,
							SimulateAssets.S_E_POISON_ID};
					int subid = skill.getData().get(3);
					ITextureRegion subregion = res.getTextureRegion(SimulateAssets.class, mapping[subid]);
					attach(region.getWidth(), 0, subregion, vbom);
					offset = (int) subregion.getWidth();
				}
			} else if (type.equals(SkillType.ST_TYPE_UP)) {
				if(data.size()>3) {
					final int[] mapping = {SimulateAssets.MONSTER_TYPE_0_ID, SimulateAssets.MONSTER_TYPE_1_ID,
							SimulateAssets.MONSTER_TYPE_2_ID, SimulateAssets.MONSTER_TYPE_3_ID, SimulateAssets.MONSTER_TYPE_4_ID,
							SimulateAssets.MONSTER_TYPE_5_ID, SimulateAssets.MONSTER_TYPE_6_ID, SimulateAssets.MONSTER_TYPE_7_ID,
							SimulateAssets.MONSTER_TYPE_8_ID, SimulateAssets.MONSTER_TYPE_9_ID, 0,
							SimulateAssets.MONSTER_TYPE_11_ID,};
					int subid = skill.getData().get(3);
					ITextureRegion subregion = res.getTextureRegion(SimulateAssets.class, mapping[subid]);
					attach(region.getWidth(), 0, subregion, vbom);
					offset = (int) subregion.getWidth();
				}
			} else if (type.equals(SkillType.ST_DROP_RATE)) {
				if(data.size()>3) {
					final int[] mapping = {SimulateAssets.DARK_DROP_ID, SimulateAssets.FIRE_DROP_ID,
							SimulateAssets.HEART_DROP_ID, SimulateAssets.LIGHT_DROP_ID, SimulateAssets.WATER_DROP_ID,
							SimulateAssets.WOOD_DROP_ID, SimulateAssets.S_POISON_ID, SimulateAssets.S_JARMA_ID,
							SimulateAssets.S_E_POISON_ID};
					int subid = skill.getData().get(3);
					ITextureRegion subregion = res.getTextureRegion(SimulateAssets.class, mapping[subid]);
					attach(region.getWidth(), 0, subregion, vbom);
					offset = (int) subregion.getWidth();
				}
//			} else if(type.equals(SkillType.ST_DROP_LOCK)) {
//				ITextureRegion subregion = res.getTextureRegion(TextureOrbs.class, TextureOrbs.LOCK_ID);
//				attach(region.getWidth(), 0, subregion, vbom);
//				offset = (int) subregion.getWidth();
			} else if(type.equals(SkillType.ST_LEADER_SWITCH)) {
				// handle by itself
				turnText = new Text(0, 3, res.getFontStroke(), "99", vbom);
				turnText.setText(String.valueOf(turns));
				turnText.setPosition(-turnText.getWidth()-2, 3);
				
				sprite.setZIndex(6);
				turnText.setZIndex(6);
				
				attachChild(turnText);
				
				attach();
				return ;
			}
		}
		
		turnText = new Text(sprite.getWidth()+offset+8, 0, res.getFontStroke(), "99", vbom);
		turnText.setText(String.valueOf(turns));
		attachChild(turnText);
		
		attach();
	}
	
	private int getAssetId(Skill skill) {
		int id = SimulateAssets.REDUCE_DEF_ID;
		if (skill instanceof EnemySkill) {
			EnemySkill s = (EnemySkill)skill;
			switch(s.getSkillType()) {
			case BIND_SKILL:
				id = SimulateAssets.SKILL_LOCK_ID;
				turns = s.getData().get(0);
				break;		
			default:
				break;
			}
		} else {
			ActiveSkill s = (ActiveSkill)skill;
			List<Integer> data = s.getData();
			switch(s.getType()) {
				case ST_HP_CHANGE:
					turns = data.get(0);
					id = SimulateAssets.S_HEART_ID;
					break;
				case ST_NO_DROP:
					turns = data.get(0);
					id = SimulateAssets.NO_DROP_ID;
					break;
				case ST_ENHANCE_ORB:
					turns = data.get(0);
					id = SimulateAssets.NO_DROP_ID;
					break;
			case ST_TURN_RECOVER:
				turns = data.get(0);
				id = SimulateAssets.S_HEART_ID;
				break;
			case ST_RECOVER_UP:
				turns = data.get(0);
				id = SimulateAssets.S_HEART_ID;
				break;
			case ST_VOID_ATTR:
				turns = data.get(0);
				id = SimulateAssets.FIRE_ABSORB_ID;
				break;
			case ST_VOID:{
				turns = data.get(0);
				id = SimulateAssets.DAMAGE_ABSORB_ID;
				break;
			}
			case ST_VOID_0:{
				turns = data.get(0);
				id = SimulateAssets.DAMAGE_ZERO_ID;
				break;
			}
			case ST_ADD_COMBO:
				turns = data.get(0);
				int added = data.get(1);
				if(added <= 3) {
					id = SimulateAssets.COMBO1_ID + added - 1;
				} else {
					id = SimulateAssets.COMBO_ID;
				}
				break;
			case ST_DROP_LOCK:
				id = SimulateAssets.LOCK_ID;
				turns = data.get(0);
				break;
			case ST_DISABLE_AWOKEN:
				id = SimulateAssets.BIND_AWOKEN_ID;
				turns = data.get(0);
				break;
			case ST_TIME_EXTEND:
			case ST_MOVE_TIME_X:
				int addition = data.get(1);
				id = (addition > 0)?
						SimulateAssets.EXTEND_TIME_ID:SimulateAssets.REDUCE_TIME_ID;
				turns = data.get(0);
				break;
			case ST_REDUCE_SHIELD:
				id = SimulateAssets.SHIELD_ID;
				turns = data.get(0);
				break;
			case ST_LEADER_SWITCH:
				id = SimulateAssets.SWAP_ID;
				turns = data.get(0);
				break;
			case ST_COUNTER_ATTACK:
				id = SimulateAssets.COUNTER_STRIKE_ID;
				turns = data.get(0);
				break;
			case ST_ADD_LOCK:
				break;
			case ST_ADD_PLUS:
				break;
			case ST_TEAM_HP_ATTACK:
			case ST_HP_ATTACK:
			case ST_ATTACK:
				break;
			case ST_CHANGE_ENEMY_ATTR:
				break;
			case ST_CHANGE_SELF_ATTR:
				break;
			case ST_CHANGE_THE_WORLD:
				break;
			case ST_CLEAR_BOARD:
				break;
			case ST_COLOR_CHANGE:
				break;
			case ST_DARK_SCREEN:
				break;
			case ST_DELAY:
				break;
			case ST_DIRECT_ATTACK:
				break;
			case ST_DROP_RATE: {
				final int[] mapping = {SimulateAssets.DARK_DROP_ID, SimulateAssets.FIRE_DROP_ID,
						SimulateAssets.HEART_DROP_ID, SimulateAssets.LIGHT_DROP_ID, SimulateAssets.WATER_DROP_ID,
						SimulateAssets.WOOD_DROP_ID, SimulateAssets.S_POISON_ID, SimulateAssets.S_JARMA_ID,
						SimulateAssets.S_E_POISON_ID};
				turns = data.get(0);
				int color = data.get(1);
				id = mapping[color];
				break;
			}
			case ST_DROP_ONLY: {
				final int[] mapping = {SimulateAssets.DARK_DROP_ID, SimulateAssets.FIRE_DROP_ID,
						SimulateAssets.HEART_DROP_ID, SimulateAssets.LIGHT_DROP_ID, SimulateAssets.WATER_DROP_ID,
						SimulateAssets.WOOD_DROP_ID, SimulateAssets.S_POISON_ID, SimulateAssets.S_JARMA_ID,
						SimulateAssets.S_E_POISON_ID};
				turns = data.get(0);
				int color = data.get(1);
				id = mapping[color];
				break;
			}
//			case ST_PERIOD_CHANGE:
//				return 9990;
			case ST_GRAVITY:
				break;
			case ST_NOT_SUPPORT:
				break;
			case ST_ONE_LINE_TRANSFORM:
				break;
			case ST_POISON:
				id = SimulateAssets.POISON_ID;
				break;
			case ST_POWER_UP: {
				final int[] mapping = {SimulateAssets.S_DARK_ID, SimulateAssets.S_FIRE_ID,
						SimulateAssets.S_HEART_ID, SimulateAssets.S_LIGHT_ID, SimulateAssets.S_WATER_ID,
						SimulateAssets.S_WOOD_ID, SimulateAssets.S_POISON_ID, SimulateAssets.S_JARMA_ID,
						SimulateAssets.S_E_POISON_ID};
				turns = data.get(0);
				int color = data.get(2);
				id = mapping[color];
				break;
			}
			case ST_DEF_UP_BY_AWOKEN: {
				turns = data.get(0);
				id = SimulateAssets.SHIELD_ID;
				break;
			}
			case ST_POWER_UP_BY_AWOKEN: {
				turns = data.get(0);
				id = SimulateAssets.ALL_UP_ID;
				break;
			}
			case ST_RANDOM_CHANGE:
				break;
			case ST_RANDOM_CHANGE_RESTRICT:
				break;
			case ST_REDUCE_DEF:
				id = SimulateAssets.REDUCE_DEF_ID;
				turns = data.get(0);
				break;
			case ST_TARGET_RANDOM:
				break;
			case ST_TRANSFORM:
				break;
			case ST_TYPE_UP: {
				final int[] mapping = {SimulateAssets.MONSTER_TYPE_0_ID, SimulateAssets.MONSTER_TYPE_1_ID,
						SimulateAssets.MONSTER_TYPE_2_ID, SimulateAssets.MONSTER_TYPE_3_ID, SimulateAssets.MONSTER_TYPE_4_ID,
						SimulateAssets.MONSTER_TYPE_5_ID, SimulateAssets.MONSTER_TYPE_6_ID, SimulateAssets.MONSTER_TYPE_7_ID,
						SimulateAssets.MONSTER_TYPE_8_ID, SimulateAssets.MONSTER_TYPE_9_ID, 0,
						SimulateAssets.MONSTER_TYPE_11_ID,};
				turns = data.get(0);
				int type = data.get(2);
				id = mapping[type];
				break;
			}
			case ST_CLOUD: {
				turns = data.get(0);
				id = 1001;
				break;
			}
			case ST_LOCK_REMOVE: {
				turns = data.get(0);
				id = 10001 + data.get(1);
			}
			default:
				break;
			}
		}
		return id;
	}
}
