package com.a30corner.combomaster.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.Toast;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.manager.SceneManager;
import com.a30corner.combomaster.scene.GameMenuScene;
import com.a30corner.combomaster.scene.SplashScene;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.NetworkUtils;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;
import com.a30corner.combomaster.utils.UpdaterUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.LayoutGameActivity;

public class GameActivity extends LayoutGameActivity {
	public static final int SCREEN_WIDTH = 540;
	public static final int SCREEN_HEIGHT = 960;

	private Handler handler;
	private Camera camera;
	private boolean fullscreen = true;
	
	private int vkHeight = 0;
	AdView adView;
	
	public void setAdViewVisible(int visible) {
		adView.setVisibility(visible);
	}
	
	@Override
	protected void onSetContentView() {
		super.onSetContentView();
		LogUtil.d("onSetContentView");

		adView = findViewById(R.id.adView);
		adView.refreshDrawableState();
		adView.setVisibility(View.VISIBLE);

		AdRequest request = new AdRequest.Builder()
			.build();
		adView.loadAd(request);

		final View view = findViewById(R.id.main_layout);
		view.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						vkHeight = view.getRootView().getHeight()
								- view.getHeight();

						if (!fullscreen) {
						    int statusBar = getStatusBarHeight();
						    vkHeight -= statusBar;
						}
					}
				});
	}

	public int getStatusBarHeight() { 
	      int result = 0;
	      int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
	          result = getResources().getDimensionPixelSize(resourceId);
	      } 
	      return result;
	} 
	
	public int getVirtualKeyHeight() {
		return vkHeight;
	}

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		fullscreen = SharedPreferenceUtil.getInstance(this).getBoolean(SharedPreferenceUtil.PREF_FULLSCREEN, true);
		if ( !fullscreen ) {
		    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		
		handler = new Handler();
		ComboMasterApplication.getsInstance().initBoardCache();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch(requestCode) {
			case 1005:
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted, yay! Do the
					// contacts-related task you need to do.
					Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
		}
	}

	public Handler getUIHandler() {
		return handler;
	}

	@Override
	protected void onDestroy() {
		ComboMasterApplication.getsInstance().saveBoardCache();
		super.onDestroy();
		System.exit(0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		SceneManager.getInstance().onActivityResult(requestCode, resultCode,
				data);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		SceneManager.getInstance().onKeyDown(keyCode, event);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		SceneManager.getInstance().create(this);
		pEngineOptions.getAudioOptions().setNeedsMusic(true);
		pEngineOptions.getAudioOptions().setNeedsSound(true);
		return new LimitedFPSEngine(pEngineOptions, 50);
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		boolean keepRatio = SharedPreferenceUtil.getInstance(this).getBoolean(SharedPreferenceUtil.PREF_KEEP_RATIO, true);
		IResolutionPolicy policy = null;
		if ( !keepRatio ) {
			policy = new FillResolutionPolicy();
		} else {
			policy = new RatioResolutionPolicy(SCREEN_WIDTH, SCREEN_HEIGHT);
		}
		EngineOptions options = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
				policy, camera);
		options.getTouchOptions().setTouchEventIntervalMilliseconds(1);
		options.setWakeLockOptions(WakeLockOptions.SCREEN_DIM);
		
		return options;
	}

	@Override
	public void onBackPressed() {
		SceneManager.getInstance().onBackPressed();
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		ResourceManager instance = ResourceManager.getInstance();
		instance.initialize(this);
		instance.loadFont();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onDestroyResources() throws Exception {
		ResourceManager.getInstance().unloadFont();
		super.onDestroyResources();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		Scene scene = checkUpdate() ? SceneManager.getInstance().setScene(
				SplashScene.class) : SceneManager.getInstance().setScene(
				GameMenuScene.class);
		pOnCreateSceneCallback.onCreateSceneFinished(scene);

	}

	private boolean checkUpdate() {
		SharedPreferences pref = getSharedPreferences("update",
				Context.MODE_PRIVATE);

		boolean dataInDB = pref.getBoolean("dataInDB", false);
		if ( !dataInDB ) {
			return true;
		}

		if (NetworkUtils.isNetworkAvailable(this)) {
			String lastVersion = pref.getString("version", "");
			String version = getString(R.string.support_version);
			if(!lastVersion.equals(version)) {
				return true;
			}

			String lastUpdate = pref.getString("lastUpdate", getString(R.string.modified_time));
			long count = UpdaterUtil.getPetsCount(version, lastUpdate);
			if (count>0L) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    
	    if ( mEngine != null && mEngine.isRunning() ) {
	    	mEngine.stop();
	    }
	    SceneManager.getInstance().onPause();
	}
	
	@Override
	protected synchronized void onResume() {
	    super.onResume();

	    if ( mEngine != null && !mEngine.isRunning() ) {
	    	mEngine.start();
	    }
	    SceneManager.getInstance().onResume();
	}
	
	@Override
	public synchronized void onGameDestroyed() {
		try {
			SceneManager.getInstance().destroy();
			super.onGameDestroyed();
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	protected int getLayoutID() {
		return R.layout.game_layout;
	}

	@Override
	protected int getRenderSurfaceViewID() {
		return R.id.surface_view;
	}

}
