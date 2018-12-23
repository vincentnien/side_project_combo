package com.a30corner.combomaster.playground.entity.impl;

import org.andengine.opengl.texture.region.ITextureRegion;

import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.entity.BuffOnEnemy;
import com.a30corner.combomaster.texturepacker.SimulateAssets;

public class Poison extends BuffOnEnemy {

    private Poison(IEnvironment pad) {
        super(pad);
    }

    public static Poison init(IEnvironment pad, int damage) {
        Poison buff = new Poison(pad);
        buff.setSkill(BuffOnEnemySkill.create(Type.POISON, damage));
        return buff;
    }

	@Override
	protected ITextureRegion getTexture() {
		return ResourceManager.getInstance().getTextureRegion(SimulateAssets.class, SimulateAssets.POISON_ID);
	}
	
	@Override
	public boolean canOverride() {
		return true;
	}
}
