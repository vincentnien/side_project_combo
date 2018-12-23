package com.a30corner.combomaster.playground.entity.impl;

import org.andengine.opengl.texture.region.ITextureRegion;

import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.entity.BuffOnEnemy;
import com.a30corner.combomaster.texturepacker.SimulateAssets;

public class AbsorbShield extends BuffOnEnemy {

    private AbsorbShield(IEnvironment pad) {
        super(pad);
    }

    
    public static AbsorbShield init(IEnvironment pad, int turn, int prop) {
        AbsorbShield shield = new AbsorbShield(pad);
        shield.setSkill(BuffOnEnemySkill.create(Type.ABSORB_SHIELD, turn, prop));
        return shield;
    }


	@Override
	protected ITextureRegion getTexture() {
		final int[] ID = {
				SimulateAssets.DARK_ABSORB_ID,
				SimulateAssets.FIRE_ABSORB_ID,
				0,
				SimulateAssets.LIGHT_ABSORB_ID,
				SimulateAssets.WATER_ABSORB_ID,
				SimulateAssets.WOOD_ABSORB_ID
		};
		return ResourceManager.getInstance().getTextureRegion(SimulateAssets.class, ID[skill.getData().get(1)]);
	}
}
