package com.a30corner.combomaster.playground.entity.impl;

import org.andengine.opengl.texture.region.ITextureRegion;

import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.entity.BuffOnEnemy;
import com.a30corner.combomaster.texturepacker.SimulateAssets;

public class Konjo extends BuffOnEnemy {

    private Konjo(IEnvironment pad) {
        super(pad);
    }

    public static Konjo init(IEnvironment pad, int percent) {
        Konjo buff = new Konjo(pad);
        buff.setSkill(BuffOnEnemySkill.create(Type.KONJO, percent));
        return buff;
    }

	@Override
	protected ITextureRegion getTexture() {
		return ResourceManager.getInstance().getTextureRegion(SimulateAssets.class, SimulateAssets.KONJO_ID);
	}
}
