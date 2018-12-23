package com.a30corner.combomaster.utils;

import java.io.FileNotFoundException;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;

public class FileUtil {

	public static void saveImage(Context context, Bitmap bitmap, String fileName) {
		OutputStream os = null;
		try {
			os = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		} catch (FileNotFoundException e) {
		} finally {
			EasyUtil.close(os);
		}

	}
}
