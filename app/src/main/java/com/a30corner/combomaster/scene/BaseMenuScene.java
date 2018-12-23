package com.a30corner.combomaster.scene;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.activity.GameActivity;

public abstract class BaseMenuScene extends MenuScene implements IBaseScene {
	public Engine engine;
	protected GameActivity activity;
	public VertexBufferObjectManager vbom;

	public BaseMenuScene() {
	}
	
	public abstract String getSceneName();

	
	@Override
	public void onPause() {
	}
	
	@Override
	public void onResume() {
	}
	
	@Override
	public void initialize(GameActivity activity) {
		this.activity = activity;
		engine = activity.getEngine();
		vbom = activity.getVertexBufferObjectManager();

		// log each scene when entering except loading scene
		String name = getSceneName();
		if (!LoadingScene.class.getSimpleName().equals(name)) {
			ComboMasterApplication.getsInstance().onEnterScene(name);
		}
	}

	@Override
	public Scene getScene() {
		return this;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	}

	@Override
	public void onBackKeyPressed() {
		activity.finish();
	}

	@Override
	public Activity getActivity() {
		return activity;
	}
}
