package com.a30corner.combomaster.utils;

import java.io.Closeable;

import android.database.Cursor;

public class EasyUtil {

	public static void close(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (Throwable e) {
			}
		}
	}

	public static void close(Cursor c) {
		if (c != null) {
			try {
				c.close();
			} catch (Throwable e) {
			}
		}
	}
}
