package com.a30corner.combomaster.pad.strategy;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.a30corner.combomaster.strategy.IAnalysisListener;
import com.a30corner.combomaster.strategy.IBoardStrategy;
import com.a30corner.combomaster.utils.BitmapUtil;
import com.a30corner.combomaster.utils.LogUtil;

public class PadBoardStrategy implements IBoardStrategy {

	private static final float[] HSV_DARK = { 300, 50, 75 };
	private static final float[] HSV_JARMA = { 200, 41, 53 };
	private static final float[] HSV_WATER = { 205, 65, 90 };
	private static final float[] HSV_POISON = { 245, 25, 50 };

	private static final float[] HSV_DARK_JPG = { 300, 45, 75 };
	private static final float[] HSV_JARMA_JPG = { 200, 43, 52 };
	private static final float[] HSV_WATER_JPG = { 205, 65, 90 };
	private static final float[] HSV_POISON_JPG = { 250, 25, 50 };

	private int row = 6;
	private int col = 5;
	
	@Override
	public void setSize(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
    @Override
    public int[][] analysis(Context context, String filename, Rect rect) throws IOException {
        if (!TextUtils.isEmpty(filename) && new File(filename).exists()) {
            Bitmap screen = BitmapFactory.decodeFile(filename);
            try {
            	float ratio = getScaledRatio(context, screen);
                startX = (int)(rect.left / ratio);
                startY = (int)(rect.top / ratio);
                endX = (int)(rect.right / ratio);
                endY = (int)(rect.bottom / ratio);
                boolean isPng = filename.toLowerCase().endsWith(".png");
                return analysis(context, screen, isPng);
            } finally {
                screen.recycle();
            }
        }
        LogUtil.e("analysis fail.");
        return null;
    }
    
    private boolean checkPlus(Bitmap bitmap) {
        int[] patternx = {0,1,1,2,1};
        int[] patterny = {1,0,1,1,2};
        
        float[] hsv = new float[3];
        int counter;
        for(int p2 = 1; p2 < 3; ++p2) {
            counter = 0;
            for(int i=0; i<5; ++i) {
                Color.colorToHSV(bitmap.getPixel(patternx[i]+p2, patterny[i]+p2), hsv);
                if (Math.abs(hsv[0]-60) <= 5) {
                    ++counter;
                }
            }
            if ( counter >= 4 ) { // max is 5, 80% is ok
                return true;
            }
        }
        
        return false;
    }
    
    private int[] getSizeByMode(int row) {
        switch(row) {
        case 5:
            return new int[]{212, 128, 50};
        case 6:
            return new int[]{88, 56, 26};
        case 7:
            return new int[]{145, 95, 49};
        }
        return new int[]{88, 56, 26};
    }
    
    private int[][] analysis(Context context, Bitmap screen, boolean isPng) {
        int width = endX - startX;
//        int height = endY - startY;
        int height = width * col / row;
        Bitmap crop = Bitmap.createBitmap(screen, startX, startY,
                width, height);
        Bitmap small = BitmapUtil.getResizedBitmap(crop, col, row);
//		BitmapUtil.saveBitmap(context, crop, "big" + (int) (Math.random()*1000) + ".jpg");
//        BitmapUtil.saveBitmap(context, small, "small" + (int) (Math.random()*1000) + ".jpg");
        int onecellw = width / row;
        int onecellh = height / col;
        
        int[] size = getSizeByMode(row);
        
        float ratioW = onecellw / (float)size[0];
        float ratioH = onecellh / (float)size[0];
        
        int pOffsetW = startX + (int)(size[1] * ratioW);
        int pOffsetH = startY + (int)(size[1] * ratioH);
        
        int sizeW = (int)(size[2] * ratioW);
        int sizeH = (int)(size[2] * ratioH);
        
        int[][] hue = new int[row][col];
        float[] hsv = new float[3];
        for (int i = 0; i < col; ++i) {
            for (int j = 0; j < row; ++j) {
                
                Color.colorToHSV(small.getPixel(j, i), hsv);
                hue[j][i] = (isPng) ? hsv2type(hsv[0])
                        : hsv2type_jpg(hsv[0]);
                if (hue[j][i] == 0) {
					float poison = (isPng) ? compare(hsv,
							HSV_POISON) : compare(hsv,
							HSV_POISON_JPG);
					float dark = (isPng) ? compare(hsv, HSV_DARK)
							: compare(hsv, HSV_DARK_JPG);
					if (poison < dark) {
						hue[j][i] = 6;
					}
				}
                if (hue[j][i] == 4) {
                    // maybe it is JAMAR orb, check it again
                    float jarma = (isPng) ? compare(hsv, HSV_JARMA)
                            : compare(hsv, HSV_JARMA_JPG);
                    float water = (isPng) ? compare(hsv, HSV_WATER)
                            : compare(hsv, HSV_WATER_JPG);
                    float min = min(jarma, water, 255f);

                    if (min == jarma) {
                        hue[j][i] = 7;
                    } else {
                        hue[j][i] = 4;
                    }
                }
                Bitmap plus = Bitmap.createBitmap(screen, pOffsetW + j*onecellw, pOffsetH + i*onecellh, sizeW, sizeH);
                Bitmap scaledPlus = BitmapUtil.getResizedBitmap(plus, 5, 5);
                if (checkPlus(scaledPlus)) {
                    hue[j][i] += 10;
                }
                plus.recycle();
                scaledPlus.recycle();
            }
        }
        return hue;
    }


	private DisplayMetrics getMetrics(WindowManager mgr) {
		Display display = mgr.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		return dm;
    }
    
	@SuppressLint("NewApi")
	private DisplayMetrics getRealMetrics(WindowManager mgr) {
		Display display = mgr.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getRealMetrics(dm);
		return dm;
    }
    
	private float getScaledRatio(Context context, Bitmap screen) {
		WindowManager mgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		int iw = screen.getWidth();
    	DisplayMetrics dm = null;
    	if ( Build.VERSION.SDK_INT < 17 ) {
    		dm = getMetrics(mgr);
    	} else {
    		dm = getRealMetrics(mgr);
    	}
		int sw = dm.widthPixels;
		
		return sw / (float)iw;
    }
	@Override
	public int[][] analysis(Context context, String filename, int vkHeight) throws IOException {
		if (!TextUtils.isEmpty(filename) && new File(filename).exists()) {
			BitmapFactory.Options options = new Options();
			options.inSampleSize = 2;
			Bitmap screen = BitmapFactory.decodeFile(filename, options);
			try {
				float ratio = getScaledRatio(context, screen);
				int realVkHeight = (int)(vkHeight / ratio);
				
    			if (findBoundary(screen, realVkHeight)) {
    			    boolean isPng = filename.toLowerCase().endsWith(".png");
    			    return analysis(context, screen, isPng);
    			}
			} finally {
			    screen.recycle();
			}
		}
		LogUtil.e("analysis fail.");
		return null;
	}

	private float min(float a, float b, float c) {
		float min = a < b ? a : b;
		return (min < c) ? min : c;
	}

	private float compare(float[] hsv, float[] compare) {
//		float total = 0f;
//		float d = Math.abs(hsv[0] - compare[0]);
//		total += Math.sqrt(d * d);
//		for (int i = 1; i < 3; i+=2) {
//			float diff = Math.abs(hsv[i] * 100f - compare[i]);
//			total += Math.sqrt(diff * diff);
//		}
//		float diff = Math.abs(hsv[2] * 100f - compare[2]);
//		total += Math.sqrt(diff * diff);
//		return total / 3f;
		return (Math.abs(hsv[2] * 100f - compare[2]));
	}

	@Override
	public void analysis(String filename, IAnalysisListener listener) {
		// TODO Auto-generated method stub

	}

	int startY = 99999;
	int endY = 0;
	int startX = 99999;
	int endX = 0;

	private boolean findBoundary(Bitmap bitmap, int vkHeight) {
		int h = bitmap.getHeight();
		int w = bitmap.getWidth();
		int sw = w / 8;

		for (int i = h - vkHeight - 1; i >= 0; --i) {
			int pixel = bitmap.getPixel(sw, i);
			int r = Color.red(pixel);
			int g = Color.green(pixel);
			int b = Color.blue(pixel);
			// if the color is not black... we regard it as start point
			if ((r > 15 || g > 15 || b > 15)) {
				endY = i;
				break;
			}
		}
		if (endY == 0) {
			return false;
		} else {
			int from = endY * 3 / 4;
			for (int i = from; i >= 0; --i) {
				float total = 0f;
				int counter = 0;
				for (int j = 20; j < sw; ++j) {
					int pixel = bitmap.getPixel(j, i);
					int r = Color.red(pixel);
					int g = Color.green(pixel);
					int b = Color.blue(pixel);
					total += (Math.sqrt(r * r) + Math.sqrt(g * g) + Math.sqrt(b
							* b)) / 3;
					++counter;
					// if (r <= 3 && g <= 3 && b <= 3) {
					// startY = i;
					// break;
					// }
				}
				if ((total / counter) < 10f) {
					startY = i;
					break;
				}
			}

		}

		if (startY != 99999) {
			int h34 = h * 3 / 4;
			for (int i = 0; i < 20; ++i) {
				int pixel = bitmap.getPixel(i, h34);
				int r = Color.red(pixel);
				int g = Color.green(pixel);
				int b = Color.blue(pixel);
				if (r > 15 || g > 15 || b > 15) {
					startX = i;
					break;
				}
			}
			if (startX == 99999) {
				return false;
			}

			for (int i = w - 1; i >= 0; --i) {
				int pixel = bitmap.getPixel(i, h34);
				int r = Color.red(pixel);
				int g = Color.green(pixel);
				int b = Color.blue(pixel);
				if (r > 15 || g > 15 || b > 15) {
					endX = i;
					break;
				}
			}
		}
		// 12-23 22:22:45.359: E/ComboMaster(6571): findBoundary=[4 , 957 , 711
		// , 1279

		LogUtil.e("findBoundary=[", startX, " , ", startY, " , ", endX, " , ",
				endY);
		return (startX >= 0 && startY > 0 && endX < w && endY < h);
	}

	// D -> 300 prop = 0
	// R -> 15 prop = 1
	// H -> 325 prop = 2
	// L -> 60 prop = 3
	// B -> 215 prop = 4
	// G -> 135 prop = 5
	// P -> 300~ s = v=55~ prop = 6
	// J -> 300~ s = <30
	private static final int[] HUE = { 300, 15, 325, 60, 215, 135 };
	private static final int[] HUE_JPG = { 300, 15, 325, 60, 200, 135 };

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

	private int hsv2type_jpg(float hsv) {
		float small = 999f;
		int index = 0;
		for (int i = 0; i < HUE_JPG.length; ++i) {
			float diff = Math.abs(hsv - HUE_JPG[i]);
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

	// private final int[] S = {33, 50, 15, 20};
	// private final int[] V = {50, 65, 55, 30};

	// there is so many issues... need to refine...
	@Override
	public int hsv2type(float h, float s, float v) {
		s = s * 100f;
		v = v * 100f;
		// check dark & heart & poison & jarmar
		if (h >= 290f) {
			if (v <= 33f && s <= 33f) {
				return 7;
			} else if (Math.abs(s - 15f) <= 8f && Math.abs(v - 50f) <= 8f) {
				return 6;
			}
		} else if (h >= 210f) {
			if (v <= 33f && s <= 33f) {
				return 7;
			}
		}

		return hsv2type(h);
	}

}
