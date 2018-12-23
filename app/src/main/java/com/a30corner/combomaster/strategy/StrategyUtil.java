package com.a30corner.combomaster.strategy;

import android.content.Context;

import com.a30corner.combomaster.pad.strategy.PadBoardStrategy;
import com.a30corner.combomaster.pad.strategy.PadBoardStrategyColorWeakness;
import com.a30corner.combomaster.tos.TosBoardStrategy;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;

public class StrategyUtil {

	public static IBoardStrategy getStrategy(Context context) {
		SharedPreferenceUtil utils = SharedPreferenceUtil.getInstance(context);
		if (utils.getType() == 2) {
			return new TosBoardStrategy();
		} else {
			if (utils.getBoolean(SharedPreferenceUtil.PREF_COLOR_WEAKNESS)) {
				return new PadBoardStrategyColorWeakness();
			} else {
				return new PadBoardStrategy();
			}
		}
	}
}