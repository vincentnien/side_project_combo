package com.a30corner.combomaster.playground.entity.impl;

import org.andengine.opengl.texture.region.ITextureRegion;

import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.entity.BuffOnEnemy;
import com.a30corner.combomaster.texturepacker.SimulateAssets;

public class ComboShield extends BuffOnEnemy {

    private ComboShield(IEnvironment pad) {
        super(pad);
    }

    
    public static ComboShield init(IEnvironment pad, int turn, int combo) {
        ComboShield shield = new ComboShield(pad);
        shield.setSkill(BuffOnEnemySkill.create(Type.COMBO_SHIELD, turn, combo));
        return shield;
    }


	@Override
	protected ITextureRegion getTexture() {
		return ResourceManager.getInstance().getTextureRegion(SimulateAssets.class, SimulateAssets.COMBO_ID);
	}
}
