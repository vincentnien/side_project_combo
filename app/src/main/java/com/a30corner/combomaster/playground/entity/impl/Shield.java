package com.a30corner.combomaster.playground.entity.impl;

import org.andengine.opengl.texture.region.ITextureRegion;

import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.entity.BuffOnEnemy;
import com.a30corner.combomaster.texturepacker.SimulateAssets;

public class Shield extends BuffOnEnemy {

    private Shield(IEnvironment pad) {
        super(pad);
    }

    
    public static Shield init(IEnvironment pad, int turn, int prop, int percent) {
        Shield shield = new Shield(pad);
        shield.setSkill(BuffOnEnemySkill.create(Type.SHIELD, turn, prop, percent));
        return shield;
    }


	@Override
	protected ITextureRegion getTexture() {
		int absorb = skill.getData().get(1);
		if(absorb == -1) {
			return ResourceManager.getInstance().getTextureRegion(SimulateAssets.class, SimulateAssets.SHIELD_ID);
		} else {
			int[] id = {
					SimulateAssets.DARK_SHIELD_ID,
					SimulateAssets.FIRE_SHIELD_ID,
					0,
					SimulateAssets.LIGHT_SHIELD_ID,
					SimulateAssets.WATER_SHIELD_ID,
					SimulateAssets.WOOD_SHIELD_ID,
			};
			return ResourceManager.getInstance().getTextureRegion(SimulateAssets.class, id[absorb]);
		}
	}
}
