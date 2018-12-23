package com.a30corner.combomaster.playground.entity.impl;

import org.andengine.opengl.texture.region.ITextureRegion;

import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.entity.BuffOnEnemy;
import com.a30corner.combomaster.texturepacker.SimulateAssets;

public class DamageVoidShield extends BuffOnEnemy {

    private DamageVoidShield(IEnvironment pad) {
        super(pad);
    }

    
    public static DamageVoidShield init(IEnvironment pad, int turn, int damage) {
        DamageVoidShield shield = new DamageVoidShield(pad);
        shield.setSkill(BuffOnEnemySkill.create(Type.DAMAGE_VOID_SHIELD, turn, damage));
        return shield;
    }


	@Override
	protected ITextureRegion getTexture() {
		return ResourceManager.getInstance().getTextureRegion(SimulateAssets.class, SimulateAssets.DAMAGE_ZERO_ID);
	}
}
