package com.a30corner.combomaster.scene;

import org.andengine.entity.scene.Scene;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;

import com.a30corner.combomaster.activity.GameActivity;

public interface IBaseScene {

	public void initialize(GameActivity activity);

	public void createScene();

	public void onBackKeyPressed();

	public void onPause();
	public void onResume();
	
	public void disposeScene();

	public Scene getScene();

	public Activity getActivity();

	public void onActivityResult(int requestCode, int resultCode, Intent data);
	
	public boolean onKeyDown(int keyCode, KeyEvent event);
}
