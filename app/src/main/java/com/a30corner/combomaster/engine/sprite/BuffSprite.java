package com.a30corner.combomaster.engine.sprite;

import java.lang.ref.WeakReference;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import com.a30corner.combomaster.pad.monster.ActiveSkill;

public class BuffSprite {
	private WeakReference<Scene> mScene;
	private Sprite mPowerUpSprite;
	private Text mPowerUpText;

	private BuffSprite(Scene scene, Sprite sprite, Text text) {
		mScene = new WeakReference<Scene>(scene);
		mPowerUpSprite = sprite;
		mPowerUpText = text;
	}

	public static BuffSprite createBuff(Scene scene, ActiveSkill skill) {

		return null;
	}
}
