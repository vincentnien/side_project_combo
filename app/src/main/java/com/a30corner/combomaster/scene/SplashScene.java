package com.a30corner.combomaster.scene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.andengine.entity.scene.background.Background;
import org.andengine.util.color.Color;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.ui.DialogUtil;
import com.a30corner.combomaster.manager.SceneManager;
import com.a30corner.combomaster.pad.monster.MonsterVO;
import com.a30corner.combomaster.provider.LocalDBHelper;
import com.a30corner.combomaster.utils.EasyUtil;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.MD5Utils;
import com.a30corner.combomaster.utils.NetworkUtils;
import com.a30corner.combomaster.utils.UpdaterUtil;
import com.a30corner.combomaster.utils.ZipUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SplashScene extends BaseMenuScene {

	private AlertDialog mCheckingDialog = null;
	private ProgressDialog mDialog = null;
	private long mTotalUpdate = 0L;

	@Override
	public void createScene() {
		setBackground(new Background(Color.BLACK));
		
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				try {
					mCheckingDialog = new AlertDialog.Builder(activity)
					.setMessage("Checking update...")
					.setCancelable(false)
					.create();
					mCheckingDialog.show();

				} catch(Throwable e) {
					LogUtil.e(e);
				}
				
				new Thread(new Runnable() {

					@Override
					public void run() {
						checkUpdate();
					}    
				}).start();
			}
		});

		

	}
	
	private void writeToDB() throws Throwable {
		String path = activity.getFilesDir().getAbsolutePath();
		if (!path.endsWith(File.separator)) {
			path = path + File.separator + "temp" + File.separator + "data" + File.separator;
		} else {
			path = path + "temp" + File.separator + "data" + File.separator;
		}
		File file = new File(path);
		File[] list = file.listFiles();
		if ( list != null ) {
		    activity.runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    mDialog.setMessage(activity.getString(R.string.updating));
                }
            });
		    mDialog.setProgress(0);
			mDialog.setMax(list.length);
			LocalDBHelper.clearMonsterSkill(activity);
			
			Gson gson = new Gson();
			Type type = new TypeToken<MonsterVO>() {}.getType();
			
			List<MonsterVO> push = new ArrayList<MonsterVO>();
			for(File f : list) {
				try {
					mDialog.incrementProgressBy(1);
					String line = getJsonData(f);
					MonsterVO vo = gson.fromJson(line, type);
					//Log.e("Vincent", "no=" + vo.mNo);

					//Log.e("Vincent", "no=" + vo.getNo() + " : " + vo.overpower);
					push.add(vo);

					if ( push.size() >= 500 ) { //100
						// push data every 100s
						LocalDBHelper.addMonsterData(activity, push);
						LocalDBHelper.addMonsterSkill(activity, push);
						push.clear();
					}

				} catch (IOException e) {
					Log.e("Vincent", e.toString(), e);
				}
			}
			// last part
            if ( push.size() > 0 ) {
                LocalDBHelper.addMonsterData(activity, push);
                LocalDBHelper.addMonsterSkill(activity, push);
                push.clear();
            }
            for(File f : list) {
                f.delete();
            }
            file.delete();
		}
	}
	
	private String getJsonData(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					is, "utf8"));
			String line;
			while ((line = reader.readLine()) != null) {
				if (TextUtils.isEmpty(line.trim())) {
					continue;
				}
				return line;
			}
			return "";
		} finally{
			EasyUtil.close(reader);
			EasyUtil.close(is);
		}
	}

	private void checkUpdate() {
		LogUtil.d("check update");
		final SharedPreferences pref = activity.getSharedPreferences(
				"update", Context.MODE_PRIVATE);
		String version = activity.getString(R.string.support_version);
		String lastVersion = pref.getString("version", "");
		boolean dataInDB = pref.getBoolean("dataInDB", false);
		boolean networkAvailable = NetworkUtils.isNetworkAvailable(activity);
		if ( !dataInDB || !lastVersion.equals(version)) {
			try {
				mCheckingDialog.dismiss();
			} catch(Throwable e) {
			}
			// move data to db
			showUpdatingMessage(false);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
			}
			InputStream is = null;
			try {
				AssetManager asset = getActivity().getAssets();
				is = asset.open("data/data.zip");
				update(is, pref);
				writeToDB();
				pref.edit()
				.putString("lastUpdate",activity.getString(R.string.modified_time))
				.putString("version", version)
				.putBoolean("dataInDB", true)
				.apply();
				// reload team info from db
				ComboMasterApplication.getsInstance().init();
			} catch(Throwable e) {
				Log.e("Vincent", e.toString(), e);
			} finally {
				onCompleted();
				EasyUtil.close(is);
			}
			// Move this to menu scene
		}
		else if (networkAvailable) {
			try {
				mCheckingDialog.dismiss();
			} catch(Throwable e) {
			}
			String lastUpdate = pref.getString("lastUpdate", activity.getString(R.string.modified_time));
			mTotalUpdate = UpdaterUtil.getPetsCount(version, lastUpdate);
			showConfirmDialog(pref);

		} else {
			onCompleted();
		}
	}
	
	private void showConfirmDialog(final SharedPreferences pref) {
		activity.runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                // no idea why Xperia Z2 (D6503) meet this issue java.lang.ArrayIndexOutOfBoundsException: length=7; index=7
                // at android.content.res.Resources.toPreloadCookie(Resources.java:2447)
                try {
                    new AlertDialog.Builder(activity)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.title_confirm)
                    .setMessage(activity.getString(R.string.str_has_new, String.valueOf(mTotalUpdate)))
                    .setOnCancelListener(new OnCancelListener() {
						
						@Override
						public void onCancel(DialogInterface dialog) {
                            dialog.dismiss();
                            onCompleted();
						}
					})
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.runOnUpdateThread(new Runnable() {
                                
                                @Override
                                public void run() {
                                    update(pref);
                                }
                            });
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            onCompleted();
                        }
                    })
                    .show();
                } catch(Throwable e) {
                    LogUtil.e(e.toString());
                    activity.runOnUpdateThread(new Runnable() {
                        
                        @Override
                        public void run() {
                            update(pref);
                        }
                    });
                }
            }
        });
	}
	
	private void update(final SharedPreferences pref) {
		showUpdatingMessage(true);
		
		try {
			String version = activity.getString(R.string.support_version);
			String lastUpdate = pref.getString("lastUpdate", activity.getString(R.string.modified_time));


			long modifiedTime = UpdaterUtil.getModifiedTime();
			List<MonsterVO> list = UpdaterUtil.getPetsList(version, lastUpdate);
			if (list.size() > 0) {
				final int max = list.size();
				mDialog.setProgress(0);
				mDialog.setMax(max);

				LocalDBHelper.clearMonsterSkill(activity, list);
				LocalDBHelper.addMonsterData(activity, list);
				LocalDBHelper.addMonsterSkill(activity, list);

				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						mDialog.setProgress(max);
					}
				});
				// if failed... try again..
				if (modifiedTime == 0) {
					modifiedTime = UpdaterUtil.getModifiedTime();
				}
				if (modifiedTime > 0) { // zero is error
					pref.edit()
							.putString("lastUpdate", String.valueOf(modifiedTime))
							.putString("version", version)
							.apply();
				}
			}
		} catch(Throwable t) {
		} finally {
			onCompleted();
		}
	}
	

	private void update(InputStream is, SharedPreferences pref) throws IOException {
		try {
			File download = File.createTempFile(UUID.randomUUID().toString(), ".zip", activity.getCacheDir());
			if (mDialog != null) {
				mDialog.setMax(is.available());
			}
			OutputStream os = null;
			try {
				os = new FileOutputStream(download);
				int read;
				int total = 0;
				byte[] buffer = new byte[1024];
				while ((read = is.read(buffer)) >= 0) {
					os.write(buffer, 0, read);
					total += read;
					if (mDialog != null) {
						mDialog.setProgress(total);
					}
				}
			} finally {
				EasyUtil.close(is);
				EasyUtil.close(os);
			}

			String calculate = MD5Utils.getMD5(download);
			// it matches. unzip file
			if (ZipUtils.unpackZip(activity, download)) {
				Editor editor = pref.edit();
				editor.putString("md5", calculate);
				editor.apply();
			}

			download.delete();
		} catch(Throwable t) {
		} finally {
		}
	}
	
	private void onCompleted() {
		try {
			if(mCheckingDialog.isShowing()) {
				mCheckingDialog.dismiss();
			}
			if (mDialog != null) {

				String version = activity.getString(R.string.version);
				String currentVer = activity.getString(R.string.current_version) + version;
				final String message = "" + currentVer; //UpdaterUtil.getMessage() + "\n" +

				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							DialogUtil.getUpdateContentDialog(activity, message).show();
							mDialog.dismiss();
						} catch(Throwable t){

						}

						SceneManager.getInstance().setScene(GameMenuScene.class);
					}
				});
			} else {
				SceneManager.getInstance().setScene(GameMenuScene.class);
			}
		} catch(Throwable e) {
		}


	}

	private void showUpdatingMessage(final boolean download) {

		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					if (mDialog == null) {
						mDialog = DialogUtil.getUpdateDialog(activity, download);
					}
					if (!mDialog.isShowing()) {
						mDialog.show();
					}
				} catch (Throwable t){

				}
			}
		});
	}

	@Override
	public void disposeScene() {
	}

	@Override
	public String getSceneName() {
		return SplashScene.class.getSimpleName();
	}

}
