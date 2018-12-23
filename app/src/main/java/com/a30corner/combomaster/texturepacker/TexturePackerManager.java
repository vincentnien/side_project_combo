package com.a30corner.combomaster.texturepacker;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.a30corner.combomaster.utils.LogUtil;

public class TexturePackerManager {

	private Map<Class<?>, Map<Integer, Integer>> mTextureMaps = new HashMap<Class<?>, Map<Integer,Integer>>();
	
	public void init() {
		int idCounter = 0;
		Class<?>[] clzs = {GameUIAssets.class, SimulateAssets.class,
				TextureOrbs.class, TextureOrbsCw.class, TextureOrbsTos.class};
		for(Class<?> clz : clzs) {
			Field[] field = clz.getDeclaredFields();
			Map<Integer, Integer> maps = new HashMap<Integer, Integer>();
			for(Field f : field) {
				if(Modifier.isStatic(f.getModifiers())) {
					try {
						maps.put(f.getInt(null), idCounter++);
					} catch (Exception e) {
						LogUtil.e(e.toString());
					}
				}
			}
			mTextureMaps.put(clz, maps);
		}
	}
	
	public void release() {
		mTextureMaps.clear();
	}
	
	public int getID(Class<?> clz, int id) {
		if (mTextureMaps.containsKey(clz)) {
			Map<Integer, Integer> map = mTextureMaps.get(clz);
			if (map.containsKey(id)) {
				return map.get(id);
			}
		}
		return -1;
	}
}
