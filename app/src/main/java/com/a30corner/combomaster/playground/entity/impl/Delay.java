package com.a30corner.combomaster.playground.entity.impl;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.opengl.texture.region.ITextureRegion;

import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.entity.BuffOnEnemy;
import com.a30corner.combomaster.texturepacker.SimulateAssets;

public class Delay extends BuffOnEnemy {

    private Delay(IEnvironment pad) {
        super(pad);
    }

    public static Delay init(IEnvironment pad, int turn) {
        Delay buff = new Delay(pad);
        buff.setSkill(BuffOnEnemySkill.create(Type.DELAY, turn));
        return buff;
    }
    
    @Override
    public void countDown() {
    }
    
    @Override
    public boolean endBuff() {
    	return false;
    }

	@Override
	protected ITextureRegion getTexture() {
		return ResourceManager.getInstance().getTextureRegion(SimulateAssets.class, SimulateAssets.DELAY_ID);
	}
	
	@Override
	protected IEntityModifier getModifier() {
	    AlphaModifier forward = new AlphaModifier(0.30f, 1.0f, 0.0f);
	    AlphaModifier backward = new AlphaModifier(0.30f, 0.0f, 1.0f);
	    return new LoopEntityModifier(new SequenceEntityModifier(forward, backward));
	}
}
