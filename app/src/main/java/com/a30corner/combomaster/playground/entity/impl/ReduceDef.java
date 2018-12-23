package com.a30corner.combomaster.playground.entity.impl;

import org.andengine.opengl.texture.region.ITextureRegion;

import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.entity.BuffOnEnemy;
import com.a30corner.combomaster.texturepacker.SimulateAssets;

public class ReduceDef extends BuffOnEnemy {

    private ReduceDef(IEnvironment pad) {
        super(pad);
    }

    public static ReduceDef init(IEnvironment pad, int turn, int percent) {
        ReduceDef reduce = new ReduceDef(pad);
        reduce.setSkill(BuffOnEnemySkill.create(Type.REDUCE_DEF, turn, percent));
        return reduce;
    }

	@Override
	protected ITextureRegion getTexture() {
		return ResourceManager.getInstance().getTextureRegion(SimulateAssets.class, SimulateAssets.REDUCE_DEF_ID);
	}
	
	@Override
	public boolean canOverride() {
		return true;
	}
}
