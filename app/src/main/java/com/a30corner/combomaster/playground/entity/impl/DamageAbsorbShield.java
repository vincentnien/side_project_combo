package com.a30corner.combomaster.playground.entity.impl;

import org.andengine.opengl.texture.region.ITextureRegion;

import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.entity.BuffOnEnemy;
import com.a30corner.combomaster.texturepacker.SimulateAssets;

public class DamageAbsorbShield extends BuffOnEnemy {

    private DamageAbsorbShield(IEnvironment pad) {
        super(pad);
    }

    
    public static DamageAbsorbShield init(IEnvironment pad, int turn, int damage) {
        DamageAbsorbShield shield = new DamageAbsorbShield(pad);
        shield.setSkill(BuffOnEnemySkill.create(Type.DAMAGE_ABSORB_SHIELD, turn, damage));
        return shield;
    }


	@Override
	protected ITextureRegion getTexture() {
		return ResourceManager.getInstance().getTextureRegion(SimulateAssets.class, SimulateAssets.DAMAGE_ABSORB_ID);
	}
}
