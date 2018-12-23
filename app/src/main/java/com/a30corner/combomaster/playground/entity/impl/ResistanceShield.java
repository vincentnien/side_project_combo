package com.a30corner.combomaster.playground.entity.impl;

import org.andengine.opengl.texture.region.ITextureRegion;

import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.entity.BuffOnEnemy;
import com.a30corner.combomaster.texturepacker.SimulateAssets;

public class ResistanceShield extends BuffOnEnemy {

    private ResistanceShield(IEnvironment pad) {
        super(pad);
    }

    public static ResistanceShield init(IEnvironment pad, int turn) {
        ResistanceShield shield = new ResistanceShield(pad);
        shield.setSkill(BuffOnEnemySkill.create(Type.RESISTANCE_SHIELD, turn));
        
        return shield;
    }
    
    @Override
    protected ITextureRegion getTexture() {
    	return ResourceManager.getInstance().getTextureRegion(SimulateAssets.class, SimulateAssets.RESISTANCE_ID);
    }
}
