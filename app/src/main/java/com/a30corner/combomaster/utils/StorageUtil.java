package com.a30corner.combomaster.utils;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;

public class StorageUtil {

	public static String getCustomDirectory(Context context) {
		SharedPreferences sp = context.getSharedPreferences("comboMaster", Context.MODE_PRIVATE);
		String path = sp.getString("backup_path", "");
		if ( TextUtils.isEmpty(path) ) {
			File file = new File(getStorageDirectory(), "ComboMaster");
			if ( !file.exists() ) {
				file.mkdirs();
			}
			return file.getAbsolutePath();
		}
		return path;
	}
	
	public static void setCustomDirectory(Context context, String path) {
		SharedPreferences sp = context.getSharedPreferences("comboMaster", Context.MODE_PRIVATE);
		sp.edit().putString("backup_path", path).apply();
	}
	
	public static boolean isStorageAvailable() {
		return getStorageDirectory().exists();
	}
	
	private static boolean isExternalStorageAvailable() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
	
	public static File getStorageDirectory() {
		if ( isExternalStorageAvailable() ) {
			return Environment.getExternalStorageDirectory();
		} else {
			File file = new File("/mnt/emmc/");
			if ( file.exists() ) {
				return file;
			}
			
			return Environment.getDownloadCacheDirectory();
		}
	}
}
