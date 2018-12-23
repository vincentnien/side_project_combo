package com.a30corner.combomaster.playground.effect;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;

public class Jump {

    public static final float DEFAULT_DURATION = 0.6f;
    
    float duration;
    
    private Jump(float duration) {
        this.duration = duration;
    }

    
    // I think this is not a good function
    public static void apply(Sprite s, float duration) {
    	float y = s.getY();
    	IEntityModifier modifier = new SequenceEntityModifier(
    			new MoveYModifier(duration/2, y, y-15),
    			new MoveYModifier(duration, y-15, y)
    			);
    	modifier.setAutoUnregisterWhenFinished(true);
    	s.registerEntityModifier(modifier);
    }
    
    public static Jump create(float duration) {
        return new Jump(duration);
    }
}
