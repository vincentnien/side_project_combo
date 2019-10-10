package com.a30corner.combomaster.scene;

import java.util.Map;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.GameActivity;
import com.a30corner.combomaster.activity.MachineListActivity;
import com.a30corner.combomaster.activity.MonsterBoxActivity;
import com.a30corner.combomaster.activity.SettingsFragmentActivity;
import com.a30corner.combomaster.activity.StageSelectActivity;
import com.a30corner.combomaster.activity.ui.DialogUtil;
import com.a30corner.combomaster.activity.ui.DialogUtil.ITeamSelectCallback;
import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.manager.SceneManager;
import com.a30corner.combomaster.pad.monster.TeamInfo;
import com.a30corner.combomaster.utils.Constants;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class GameMenuScene extends BaseMenuScene 
    implements IOnMenuItemClickListener {

	private static final int MENU_DROP_MODE = 1;
	private static final int MENU_SINGLE_MODE = 2;
	private static final int MENU_SETTINGS = 3;
	private static final int MENU_QUIT = 4;
	private static final int MENU_CALCULATOR_MODE = 5;
	private static final int MENU_TEAM = 6;
	private static final int MENU_SIMULATOR = 7;
	private static final int MENU_REPORT = 8;
	private static final int MENU_CALCULATOR_7x6_MODE = 9;
	private static final int MENU_EGG_MACHINE = 10;
	private static final int MENU_STAGE = 11;
	private static final int MENU_COP = 12;


	public GameMenuScene() {
	}

	@Override
	public void createScene() {

		ResourceManager res = ResourceManager.getInstance();
		res.loadMenuScene();

		Sprite title = new Sprite(0, 0, res.getTextureRegion("title.png"), vbom);
		title.setPosition((GameActivity.SCREEN_WIDTH - title.getWidth()) / 2,
				50);
		attachChild(title);

		IFont font = ResourceManager.getInstance().getFont();
		IMenuItem train = new ScaleMenuItemDecorator(new TextMenuItem(
				MENU_DROP_MODE, font,
				activity.getString(R.string.menu_training_mode), vbom), 1.2f,
				1.0f);
		IMenuItem test = new ScaleMenuItemDecorator(new TextMenuItem(
				MENU_SINGLE_MODE, font,
				activity.getString(R.string.menu_single_mode), vbom), 1.2f,
				1.0f);
		IMenuItem calc = new ScaleMenuItemDecorator(new TextMenuItem(
				MENU_CALCULATOR_MODE, font,
				activity.getString(R.string.menu_calculate_mode), vbom), 1.2f,
				1.0f);
		IMenuItem calc7x6 = new ScaleMenuItemDecorator(new TextMenuItem(
				MENU_CALCULATOR_7x6_MODE, font,
				activity.getString(R.string.menu_calculate_7x6_mode), vbom), 1.2f,
				1.0f);
		IMenuItem calc5x4 = new ScaleMenuItemDecorator(new TextMenuItem(
				MENU_EGG_MACHINE, font,
				activity.getString(R.string.menu_egg_machine), vbom), 1.2f,
				1.0f);
		IMenuItem simulator = new ScaleMenuItemDecorator(new
		TextMenuItem(MENU_SIMULATOR, font,
		activity.getString(R.string.menu_simulator_mode), vbom), 1.2f, 1.0f);
		
		IMenuItem cop = new ScaleMenuItemDecorator(new
				TextMenuItem(MENU_COP, font,
				activity.getString(R.string.menu_cop_mode), vbom), 1.2f, 1.0f);
		
		IMenuItem team = new ScaleMenuItemDecorator(new TextMenuItem(MENU_TEAM,
				font, activity.getString(R.string.menu_monster_box), vbom),
				1.2f, 1.0f);
		IMenuItem settings = new ScaleMenuItemDecorator(new TextMenuItem(
				MENU_SETTINGS, font,
				activity.getString(R.string.menu_settings_mode), vbom), 1.2f,
				1.0f);
		IMenuItem report = new ScaleMenuItemDecorator(new TextMenuItem(
				MENU_REPORT, font, activity.getString(R.string.menu_report),
				vbom), 1.2f, 1.0f);
//		IMenuItem quit = new ScaleMenuItemDecorator(new TextMenuItem(MENU_QUIT,
//				font, activity.getString(R.string.menu_quit_mode), vbom), 1.2f,
//				1.0f);
//		IMenuItem stage = new ScaleMenuItemDecorator(new TextMenuItem(MENU_STAGE,
//				font, activity.getString(R.string.menu_simulator), vbom), 1.2f,
//				1.0f); 

		// buildAnimations();

		float h = GameActivity.SCREEN_HEIGHT * 0.3f;
		float menuHeight = train.getHeight() * 1.5f;

		addMenuItemOrdered(train, h, menuHeight);
		addMenuItemOrdered(test, h, menuHeight);
		addMenuItemOrdered(calc, h, menuHeight);
		addMenuItemOrdered(calc7x6, h, menuHeight);
		addMenuItemOrdered(calc5x4, h, menuHeight);
		addMenuItemOrdered(simulator, h, menuHeight);
		addMenuItemOrdered(cop, h, menuHeight);
		addMenuItemOrdered(team, h, menuHeight);
		addMenuItemOrdered(settings, h, menuHeight);
		addMenuItemOrdered(report, h, menuHeight);
//		addMenuItemOrdered(stage, h, menuHeight);
//		addMenuItemOrdered(quit, h, menuHeight);

		Text version = new Text(0, 0, font, "ver. "
				+ activity.getString(R.string.version), vbom);
		version.setScale(0.8f);
		version.setPosition(GameActivity.SCREEN_WIDTH - version.getWidth() - 8,
				GameActivity.SCREEN_HEIGHT - version.getHeight() - 8);
		attachChild(version);

		setOnMenuItemClickListener(this);

//		checkUpdate();
		// moveDataToDB();

		Observable.just(0)
				.subscribeOn(AndroidSchedulers.mainThread())
				.subscribe(new Action1<Integer>() {
					@Override
					public void call(Integer integer) {

						// Here, thisActivity is the current activity
						if (ContextCompat.checkSelfPermission(activity,
								Manifest.permission.WRITE_EXTERNAL_STORAGE)
								!= PackageManager.PERMISSION_GRANTED) {

							// Should we show an explanation?
							if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
									Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

								// Show an explanation to the user *asynchronously* -- don't block
								// this thread waiting for the user's response! After the user
								// sees the explanation, try again to request the permission.
								Toast.makeText(activity, activity.getResources().getString(R.string.need_read_storage), Toast.LENGTH_LONG).show();
							}
							// No explanation needed, we can request the permission.

							ActivityCompat.requestPermissions(activity,
									new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
									1005);

							// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
							// app-defined int constant. The callback method gets the
							// result of the request.
						}
					}
				});
	}


	private void addMenuItemOrdered(IMenuItem item, float h, float menuHeight) {
		int count = getMenuItemCount();
		addMenuItem(item);
		item.setPosition((GameActivity.SCREEN_WIDTH - item.getWidth()) / 2f, h
				+ menuHeight * count);
	}

	@Override
	public void onBackKeyPressed() {
		activity.finish();
	}

	@Override
	public void disposeScene() {
		ResourceManager.getInstance().unloadMenuScene();
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case MENU_STAGE:
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					DialogUtil.getTeamSelectDialog(activity,
							new ITeamSelectCallback() {

								@Override
								public void onSelect(int index, Map<String, Object> data) {
									ComboMasterApplication instance = ComboMasterApplication.getsInstance();
									int mode = (Integer)data.get("gameMode");
									boolean nullAwoken = (Boolean) data.get("nullAwoken");
									boolean[] powerUp = (boolean[]) data.get("monsterUp");
									// FIXME: this is not good design... 
									// getTargetTeam will check game mode... 
									// so the order cannot be changed, need to refactor...
									boolean cop = (Boolean) data.get("copMode");
									instance.setCopMode(cop);
									instance.setGameMode(mode);
									instance.setTargetTeam(index);
									instance.setNullAwokenSkill(nullAwoken);
									instance.setPowerUp(powerUp);
									
									SceneManager.getInstance().setScene(
											LoadingScene.class,
											PadGameScene.class);
								}

								@Override
								public void onCancel() {
								}
							}).show();
				}
			});
			break;
		case MENU_SINGLE_MODE:
			SceneManager.getInstance().setScene(LoadingScene.class,
					SingleDropGameScene.class);

			break;
		case MENU_DROP_MODE:
			SceneManager.getInstance().setScene(LoadingScene.class,
					ContinueDropGameScene.class);
			break;
		case MENU_CALCULATOR_MODE:
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					DialogUtil.getTeamSelectDialog(activity,
							new ITeamSelectCallback() {

								@Override
								public void onSelect(int index, Map<String, Object> data) {
									ComboMasterApplication instance = ComboMasterApplication.getsInstance();
									int mode = (Integer)data.get("gameMode");
									boolean nullAwoken = (Boolean) data.get("nullAwoken");
									boolean[] powerUp = (boolean[]) data.get("monsterUp");
									
									// FIXME: this is not good design... 
									// getTargetTeam will check game mode... 
									// so the order cannot be changed, need to refactor...
									boolean cop = (Boolean) data.get("copMode");
									instance.setCopMode(cop);
									instance.setGameMode(mode);
									instance.setTargetTeam(index);
									instance.setNullAwokenSkill(nullAwoken);
									instance.setPowerUp(powerUp);
									
									SceneManager.getInstance().setScene(
											LoadingScene.class,
											CalculatorGameScene.class);
								}

								@Override
								public void onCancel() {
								}
							}).show();
				}
			});
			break;
		case MENU_EGG_MACHINE:
			activity.startActivity(new Intent(activity,
					MachineListActivity.class));
			break;
		case MENU_CALCULATOR_7x6_MODE:
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					DialogUtil.getTeamSelectDialog(activity,
							new ITeamSelectCallback() {

								@Override
								public void onSelect(int index, Map<String, Object> data) {
									ComboMasterApplication instance = ComboMasterApplication.getsInstance();
									int mode = (Integer)data.get("gameMode");
									boolean nullAwoken = (Boolean) data.get("nullAwoken");
									boolean[] powerUp = (boolean[]) data.get("monsterUp");
									// FIXME: this is not good design... 
									// getTargetTeam will check game mode... 
									// so the order cannot be changed, need to refactor...
									boolean cop = (Boolean) data.get("copMode");
									instance.setCopMode(cop);
									instance.setGameMode(mode);
									instance.setTargetTeam(index);
									instance.setNullAwokenSkill(nullAwoken);
									instance.setPowerUp(powerUp);
									
									SceneManager.getInstance().setScene(
											LoadingScene.class,
											CalculatorGame7x6Scene.class);
								}

								@Override
								public void onCancel() {
								}
							}).show();
				}
			});
			break;
		case MENU_COP: {
			Intent intent = new Intent(activity, StageSelectActivity.class);
			intent.putExtra("simulator", 2);
			activity.startActivityForResult(intent, 1001);
			break;
		}
			
		case MENU_SIMULATOR: {
			Intent intent = new Intent(activity, StageSelectActivity.class);
			intent.putExtra("simulator", 1);
			activity.startActivityForResult(intent, 1000);

			break;
		}
		case MENU_TEAM:
			activity.startActivity(new Intent(activity,
					MonsterBoxActivity.class));
			break;
		case MENU_SETTINGS:
			activity.startActivity(new Intent(activity,
					SettingsFragmentActivity.class));
			
			break;
		case MENU_REPORT:
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(Constants.TUTORIAL_URL));
			activity.startActivity(intent);
			break;
		case MENU_QUIT:
			activity.finish();
			break;
		}
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK) {
			ComboMasterApplication app = ComboMasterApplication.getsInstance();
			if(requestCode == 1000) {
				TeamInfo info = app.getTargetTeam();
				if(info.is7x6()) {
					SceneManager.getInstance().setScene(PadGameScene7x6.class);
				} else {
				SceneManager.getInstance().setScene(PadGameScene.class);
			}
		} else if (requestCode == 1001) {
			TeamInfo[] info = app.getTarget2Team();
			if(info[0].is7x6LeaderOnly() || info[1].is7x6LeaderOnly()) {
				SceneManager.getInstance().setScene(MultiplePadGameScene7x6.class);
			} else {
				SceneManager.getInstance().setScene(MultiplePadGameScene.class);
			}
			}
		}
	}
	
	@Override
	public String getSceneName() {
		return GameMenuScene.class.getSimpleName();
	}
}
