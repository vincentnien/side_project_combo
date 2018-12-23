package com.a30corner.combomaster.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager CM = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = CM.getActiveNetworkInfo();
		if (info != null) {
			return info.isConnected();
		}
		return false;
	}
}
