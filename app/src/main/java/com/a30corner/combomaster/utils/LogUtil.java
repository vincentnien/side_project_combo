package com.a30corner.combomaster.utils;

import android.util.Log;

import com.a30corner.combomaster.BuildConfig;

public class LogUtil {
	private static final boolean RELEASE = !BuildConfig.DEBUG;

	private static final String TAG = "ComboMaster";

	public static void d(Object... text) {
		if(RELEASE) {
			return ;
		}
		StringBuilder sb = new StringBuilder();

		for (Object s : text) {
			sb.append(s.toString());
		}
		Log.d(TAG, sb.toString());
	}

	public static void d(String tag, Object... text) {
		if(RELEASE) {
			return ;
		}
		StringBuilder sb = new StringBuilder(tag);

		for (Object s : text) {
			sb.append(s.toString());
		}
		Log.d(TAG, sb.toString());
	}

	public static void e(Throwable e, Object... text) {
		StringBuilder sb = new StringBuilder();

		for (Object s : text) {
			sb.append(s.toString());
		}
		if (e != null) {
			Log.e(TAG, sb.toString(), e);
			e.printStackTrace();
		} else {
			Log.e(TAG, sb.toString());
		}

	}

	public static void e(Object... text) {
		e(null, text);

	}
}
