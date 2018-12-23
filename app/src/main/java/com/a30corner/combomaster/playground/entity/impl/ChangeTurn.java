package com.a30corner.combomaster.playground.entity.impl;

import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.entity.BuffOnEnemy;
import com.a30corner.combomaster.texturepacker.SimulateAssets;

import org.andengine.opengl.texture.region.ITextureRegion;

public class ChangeTurn extends BuffOnEnemy {
    protected ChangeTurn(IEnvironment pad) {
        super(pad);
    }

    public static ChangeTurn init(IEnvironment pad, int percent) {
        ChangeTurn buff = new ChangeTurn(pad);
        buff.setSkill(BuffOnEnemySkill.create(BuffOnEnemySkill.Type.CHANGE_TURN, percent));
        return buff;
    }

    @Override
    protected ITextureRegion getTexture() {
        return ResourceManager.getInstance().getTextureRegion(SimulateAssets.class, SimulateAssets.S_NUMBER1_ID);
    }
}
