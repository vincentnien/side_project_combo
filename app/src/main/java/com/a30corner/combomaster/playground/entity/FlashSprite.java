package com.a30corner.combomaster.playground.entity;

import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.opengl.GLES10;

public class FlashSprite extends Sprite {

    private Sprite[] sprite = new Sprite[2];
    
    public FlashSprite(float pX, float pY, ITextureRegion pTextureRegion, ITextureRegion pTextureRegion1,ITextureRegion pTextureRegion2,
            VertexBufferObjectManager pSpriteVertexBufferObject) {
        super(pX, pY, pTextureRegion, pSpriteVertexBufferObject);
        
        create(pTextureRegion1, pTextureRegion2);
        
        setColor(Color.BLACK);
        setBlendingEnabled(true);
        setBlendFunction(GLES10.GL_ONE, GLES10.GL_ONE);
        for(int i=0; i<2; ++i) {
            sprite[i].setColor(Color.BLACK);
            sprite[i].setBlendingEnabled(true);
            sprite[i].setBlendFunction(GLES10.GL_ONE, GLES10.GL_ONE);
        }
        
        registerModifier();
    }
    
    private void registerModifier() {
        Color gray = new Color(0.4f, 0.4f, 0.4f);
        IEntityModifier loopModifier = new LoopEntityModifier(
                new SequenceEntityModifier(
                        new ColorModifier(.2f, Color.BLACK, gray),
                        new ColorModifier(.2f, gray, Color.BLACK),
                        new ColorModifier(0.6f, Color.BLACK, Color.BLACK)));
        registerEntityModifier(loopModifier);

        IEntityModifier loopModifier1 = new LoopEntityModifier(
                new SequenceEntityModifier(
                        new ColorModifier(0.2f, Color.BLACK, Color.BLACK),
                        new ColorModifier(0.2f, Color.BLACK, gray),
                        new ColorModifier(0.2f, gray, Color.BLACK),
                        new ColorModifier(0.4f, Color.BLACK, Color.BLACK)));
        sprite[0].registerEntityModifier(loopModifier1);

        IEntityModifier loopModifier2 = new LoopEntityModifier(
                new SequenceEntityModifier(
                        new ColorModifier(0.4f, Color.BLACK, Color.BLACK),
                        new ColorModifier(0.2f, Color.BLACK, gray),
                        new ColorModifier(0.2f, gray, Color.BLACK),
                        new ColorModifier(0.2f, Color.BLACK, Color.BLACK)));
        sprite[1].registerEntityModifier(loopModifier2);
    }

    private void create(ITextureRegion pTextureRegion1,ITextureRegion pTextureRegion2) {
        VertexBufferObjectManager vbom = getVertexBufferObjectManager();
        sprite[0] = new Sprite(0f, 0f, pTextureRegion1, vbom);
        sprite[1] = new Sprite(0f, 0f, pTextureRegion2, vbom);
        
        attachChild(sprite[0]);
        attachChild(sprite[1]);
    }
    
}
