package com.a30corner.combomaster.tos;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextUtils;

import com.a30corner.combomaster.strategy.IAnalysisListener;
import com.a30corner.combomaster.strategy.IBoardStrategy;
import com.a30corner.combomaster.utils.BitmapUtil;

public class TosBoardStrategy implements IBoardStrategy {

	private int row = 5;
	private int col = 6;
	
	@Override
	public void setSize(int row, int col) {

	}
	
	@Override
	public int[][] analysis(Context context, String filename, int vkHeight) throws IOException {
		if (!TextUtils.isEmpty(filename) && new File(filename).exists()) {
			Bitmap screen = BitmapFactory.decodeFile(filename);
			if (findBoundary(screen, vkHeight)) {
				return analysis(screen);
			}
		}
		throw new IOException("can't analysis.");
	}

	int startY = 99999;
	int endY = 0;
	int startX = 99999;
	int endX = 0;

	private boolean findBoundary(Bitmap bitmap, int vkHeight) {
		int h = bitmap.getHeight();
		int w = bitmap.getWidth();
		int sw = w / 2;
		int hdiv2 = h / 2;

		for (int i = hdiv2; i < h; ++i) {
			int pixel = bitmap.getPixel(sw, i);
			int r = Color.red(pixel);
			int g = Color.green(pixel);
			int b = Color.blue(pixel);
			if (r <= 5 && g <= 5 && b <= 5) {
				endY = i;
				break;
			}
		}
		if (endY == 0) {
			return false;
		} else {
			startY = endY - (w * 5 / 6);
		}

		if (endY != 0) {
			startX = 0;
			endX = w;
		}

		return (startX >= 0 && startY > 0 && endX <= w && endY < h);
	}

	@Override
	public void analysis(String filename, IAnalysisListener listener) {
		// TODO Auto-generated method stub

	}

	private static final int[] HUE = { 292, 10, 325, 40, 205, 120 };

	@Override
	public int hsv2type(float hsv) {
		float small = 999f;
		int index = 0;
		for (int i = 0; i < HUE.length; ++i) {
			float diff = Math.abs(hsv - HUE[i]);
			if (diff <= 10f) {
				return i;
			} else {
				if (small > diff) {
					small = diff;
					index = i;
				}
			}

		}
		return index;
	}

	@Override
	public int hsv2type(float h, float s, float v) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int[][] analysis(Bitmap screen) {
        Bitmap crop = Bitmap.createBitmap(screen, startX, startY, endX
                - startX, endY - startY);
        Bitmap small = BitmapUtil.getResizedBitmap(crop, 5, 6);

        int[][] hue = new int[6][5];
        float[] hsv = new float[3];
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 6; ++j) {
                Color.colorToHSV(small.getPixel(j, i), hsv);
                hue[j][i] = hsv2type(hsv[0]);
            }
        }
        return hue;
	}
	
    @Override
    public int[][] analysis(Context context, String filename, Rect rect) throws IOException {
        if (!TextUtils.isEmpty(filename) && new File(filename).exists()) {
            Bitmap screen = BitmapFactory.decodeFile(filename);
            startX = rect.left;
            startY = rect.top;
            endX = rect.right;
            endY = rect.bottom;
            return analysis(screen);
        }
        throw new IOException("can't analysis.");
    }

}
