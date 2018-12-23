package com.a30corner.combomaster.manager;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.a30corner.combomaster.activity.GameActivity;
import com.a30corner.combomaster.scene.IBaseScene;
import com.a30corner.combomaster.scene.LoadingScene;
import com.a30corner.combomaster.utils.LogUtil;

public class SceneManager {
	private GameActivity activity;

	private static SceneManager sInstance = new SceneManager();

	public static SceneManager getInstance() {
		return sInstance;
	}

	private IBaseScene currentScene = null;

	public Scene getScene() {
	    if ( currentScene != null ) {
	        return currentScene.getScene();
	    }
	    return null;
	}
	
	public void onPause() {
	    if ( currentScene != null ) {
	        currentScene.onPause();
	    }
	}
	
	public void onResume() {
	    if ( currentScene != null ) {
	        currentScene.onResume();
	    }
	}

	public void create(GameActivity activity) {
		this.activity = activity;
	}

	public Scene setScene(Class<LoadingScene> loadingScene,
			Class<? extends IBaseScene> scene) {
		IBaseScene previous = currentScene;
		try {
			LogUtil.d("setScene:", loadingScene.getName());
			IBaseScene instance = (IBaseScene) loadingScene.getConstructor()
					.newInstance();
			currentScene = instance;

			if (instance instanceof MenuScene) {
				((MenuScene) instance).setCamera(activity.getEngine()
						.getCamera());
			}
			((LoadingScene) instance).setNextScene(scene);

			currentScene.initialize(activity);
			currentScene.createScene();
			activity.getEngine().setScene(currentScene.getScene());

			if (previous != null) {
				previous.disposeScene();
			}
			return currentScene.getScene();
		} catch (Throwable e) {
			LogUtil.e(e.getMessage(), e);
		}
		return null;
	}

	public Scene setScene(Class<? extends IBaseScene> scene) {
		IBaseScene previous = currentScene;
		try {
			LogUtil.d("setScene:", scene.getName());
			IBaseScene instance = (IBaseScene) scene.getConstructor()
					.newInstance();
			currentScene = instance;

			if (instance instanceof MenuScene) {
				((MenuScene) instance).setCamera(activity.getEngine()
						.getCamera());
			}

			currentScene.initialize(activity);
			currentScene.createScene();
			activity.getEngine().setScene(currentScene.getScene());

			if (previous != null) {
				previous.disposeScene();
			}
			return currentScene.getScene();
		} catch (Throwable e) {
			Log.e("ComboMaster", e.toString(), e);
			LogUtil.e(e.toString(), e);
		}
		return null;
	}

	public void destroy() {
		if (currentScene != null) {
			currentScene.disposeScene();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (currentScene != null) {
			currentScene.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (currentScene != null) {
			return currentScene.onKeyDown(keyCode, event);
		}
		return false;
	}

	public void onBackPressed() {
		if (currentScene != null) {
			currentScene.onBackKeyPressed();
		} else {
			activity.finish();
		}
	}
}
