package com.a30corner.combomaster.engine.sprite;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;

import com.a30corner.combomaster.scene.BaseMenuScene;

public class SpriteGroup {

	protected List<Entity> mSpriteList = new ArrayList<Entity>();
	private WeakReference<BaseMenuScene> mScene;

	protected SpriteGroup(BaseMenuScene scene) {
		mScene = new WeakReference<BaseMenuScene>(scene);
	}

	protected void addSprite(Sprite s) {
		mSpriteList.add(s);
	}

	public void attachChild() {
		Scene scene = mScene.get();
		for (Entity s : mSpriteList) {
			scene.attachChild(s);
			s.setVisible(true);
		}
	}

	public void detachChild() {
		Scene scene = mScene.get();
		if(mSpriteList != null) {
			for (Entity s : mSpriteList) {
				if(s.getParent() != null) {
					scene.detachChild(s);
				}
			}
		}
	}
	
	public void detachChild(Entity s) {
		Scene scene = mScene.get();
		for(Entity sprite : mSpriteList) {
			if(sprite == s) {
				mSpriteList.remove(s);
				scene.detachChild(s);
				return ;
			}
		}
	}

	protected BaseMenuScene getScene() {
		return mScene.get();
	}

	public void registerEntityModifier(IEntityModifier modifier) {
		for (Entity s : mSpriteList) {
			s.registerEntityModifier(modifier);
		}
	}
}
