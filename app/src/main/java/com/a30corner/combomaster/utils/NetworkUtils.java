package com.a30corner.combomaster.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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


	public static String httpRequest(String http) {

		HttpURLConnection connection = null;
		BufferedReader reader = null;

		try {
			URL url = new URL(http);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();


			InputStream stream = connection.getInputStream();

			reader = new BufferedReader(new InputStreamReader(stream));

			StringBuffer buffer = new StringBuffer();
			String line;

			while ((line = reader.readLine()) != null) {
				buffer.append(line).append("\n");
			}

			return buffer.toString();


		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

}
