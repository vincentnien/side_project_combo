package com.a30corner.combomaster.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class BitmapUtil {

	public static Bitmap getBitmap(Context context, String filename) {
		File file = new File(context.getFilesDir(), filename);
		if (file.exists()) {
			return BitmapFactory.decodeFile(file.getAbsolutePath());
		}
		return null;
	}

	public static Bitmap getResizedBitmap(Bitmap bitmap, int newHeight,
			int newWidth) {

		Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight,
				Config.ARGB_8888);

		float ratioX = newWidth / (float) bitmap.getWidth();
		float ratioY = newHeight / (float) bitmap.getHeight();
		float middleX = newWidth / 2.0f;
		float middleY = newHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY
				- bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

		return scaledBitmap;

	}

//	public static void saveBitmapWithDirectory(Context context, Bitmap bitmap, String path) {
//	    File file = new File(path);
//        OutputStream os = null;
//        try {
//            os = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 80, os);
//        } catch (FileNotFoundException e) {
//            LogUtil.e(e);
//        } finally {
//            EasyUtil.close(os);
//        }
//	}
	
	public static void saveBitmap(Context context, Bitmap bitmap, String name) {
		File file = new File(context.getFilesDir(), name);
//		File file = new File("/sdcard/"+name);
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 80, os);
		} catch (FileNotFoundException e) {
			LogUtil.e(e);
		} finally {
			EasyUtil.close(os);
		}
	}
}
