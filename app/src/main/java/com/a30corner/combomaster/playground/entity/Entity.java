package com.a30corner.combomaster.playground.entity;

import java.lang.ref.WeakReference;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.util.Pair;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.scene.PlaygroundGameScene;


public abstract class Entity {

    public WeakReference<IEnvironment> envRef;
    
    public Sprite sprite;
    
    public void setVisible(final boolean visible) {
    	envRef.get().getScene().engine.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                sprite.setVisible(visible);
            }
        });
    }
    
    public void setZIndex(int index) {
    	sprite.setZIndex(index);
    }
    
    public void setColor(Color color) {
    	sprite.setColor(color);
    }
    
    public interface OnAreaTouchCallback {
        public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY);
    }
    
    public Entity(IEnvironment pad) {
        envRef = new WeakReference<IEnvironment>(pad);
    }
    
    public void create(float x, float y, ITextureRegion region, VertexBufferObjectManager vbom) {
        create(x, y, region, vbom, null);
    }
    
    public void create(float x, float y, ITextureRegion region, VertexBufferObjectManager vbom,
            final OnAreaTouchCallback callback) {
        sprite = new Sprite(0f, 0f, region, vbom) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                    float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (callback != null) {
                    callback.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                }
                return true;
            }
        };
        sprite.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        sprite.setPosition(x, y);
    }
    
    public Sprite attach(float x, float y, ITextureRegion region, VertexBufferObjectManager vbom) {
        Sprite sprite = new Sprite(0, 0, region, vbom);
        sprite.setPosition(x, y);
        this.sprite.attachChild(sprite);
        return sprite;
    }
    
    public void attachChild(IEntity s) {
        sprite.attachChild(s);
    }
    
    public void detachChild(IEntity s) {
        sprite.detachChild(s);
    }
    
    public void setPosition(float x, float y) {
        if(sprite == null) {
            return ;
        }
        sprite.setPosition(x, y);
    }
    
    public Pair<Float, Float> center() {
    	float x = sprite.getX() + sprite.getWidth() / 2 ;
    	float y = sprite.getY() + sprite.getHeight() / 2 ;
    	return new Pair<Float, Float>(x, y);
    }
    
    public void dispose() {
        sprite.dispose();
    }
    
    public void attach() {
        envRef.get().getScene().attach(sprite);
    }
    
    public void detach() {
        final PlaygroundGameScene scene = envRef.get().getScene();
        scene.engine.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                scene.unregisterTouchArea(sprite);
                scene.detachChild(sprite);
            }
        });

    }
    
    // run on call thread
    public void attachScene() {
        envRef.get().getScene().attachChild(sprite);
    }
    
    public void registerEntityModifier(final IEntityModifier modifier) {
        envRef.get().getScene().engine.runOnUpdateThread(new Runnable() {
            
            @Override
            public void run() {
                sprite.registerEntityModifier(modifier);
            }
        });
    }
}
