package com.a30corner.combomaster.scene;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;

import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.manager.SceneManager;

public class LoadingScene extends BaseMenuScene {

	private Class<? extends IBaseScene> mScene;

	public void setNextScene(Class<? extends IBaseScene> scene) {
		mScene = scene;
	}

	@Override
	public void createScene() {
		ResourceManager rm = ResourceManager.getInstance();
		rm.loadLoadingScene();

		IMenuItem loading = new SpriteMenuItem(0,
				rm.getTextureRegion("loading.png"), vbom);
		addMenuItem(loading);
		buildAnimations();
		engine.registerUpdateHandler(new TimerHandler(.7f,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						engine.unregisterUpdateHandler(pTimerHandler);
						SceneManager.getInstance().setScene(mScene);
					}
				}));
	}

	@Override
	public void onBackKeyPressed() {
	}

	@Override
	public void disposeScene() {
		ResourceManager.getInstance().unloadLoadingScene();
	}

	@Override
	public String getSceneName() {
		return LoadingScene.class.getSimpleName();
	}
}
