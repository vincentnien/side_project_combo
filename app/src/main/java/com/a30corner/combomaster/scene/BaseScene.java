package com.a30corner.combomaster.scene;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Intent;

import com.a30corner.combomaster.activity.GameActivity;

/**
 * @author Mateusz Mysliwiec
 * @author www.matim-dev.com
 * @version 1.0
 */
public abstract class BaseScene extends Scene implements IBaseScene {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	protected Engine engine;
	protected GameActivity activity;
	protected VertexBufferObjectManager vbom;

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public BaseScene() {
	}

	@Override
	public void initialize(GameActivity activity) {
		this.activity = activity;
		engine = activity.getEngine();
		vbom = activity.getVertexBufferObjectManager();
	}

	@Override
	public Scene getScene() {
		return this;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	}

}