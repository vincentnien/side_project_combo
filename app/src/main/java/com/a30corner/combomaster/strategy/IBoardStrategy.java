package com.a30corner.combomaster.strategy;

import java.io.IOException;

import android.content.Context;
import android.graphics.Rect;

public interface IBoardStrategy {

	public void setSize(int row, int col);
	
    public int[][] analysis(Context context, String filename, Rect rect) throws IOException;
    
    //@Deprecated
	//public int[][] analysis(String filename, int vkHeight) throws IOException;
    
    public int[][] analysis(Context context, String filename, int vkHeight) throws IOException;

	public void analysis(String filename, IAnalysisListener listener);

	public int hsv2type(float hsv);

	public int hsv2type(float h, float s, float v);
}
