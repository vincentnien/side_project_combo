package com.a30corner.combomaster.utils;

import java.util.Random;

public class RandomUtil {

    private static Random random = new Random();
    
	// int -> 0 ~ 100
	public static boolean getLuck(int percent) {
		if (percent >= 100) {
			return true;
		} else if ( percent == 0 ) {
		    return false;
		} else {
			return random.nextInt(101) <= percent;
		}
	}

	public static int getColor(int i) {
		return random.nextInt(i);
	}

	public static int getInt(int size) {
		return random.nextInt(size);
	}
	
	public static int chooseOne(int[] data) {
		return data[getInt(data.length)];
	}

	public static int range(Integer from, Integer to) {
		int size = Math.abs(to - from);
		if (size == 0) {
			return from;
		}
		return getInt(size + 1) + from;
	}

}
