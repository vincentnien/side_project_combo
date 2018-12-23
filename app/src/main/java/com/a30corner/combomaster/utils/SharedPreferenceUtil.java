package com.a30corner.combomaster.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Rect;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.pad.PadBoardAI;

// for combo master only...
public class SharedPreferenceUtil {

	private static final String PREF_TIME_LIMIT = "TimeLimit";
	private static final String PREF_ORB_TYPE = "OrbType";
	private static final String PREF_SOUND_ENABLE = "SoundEnable";
	private static final String[] PREF_DROPS = { "d1", "d2", "d3", "d4", "d5",
			"d6" };
	
    private static final String[] PREF_DROPS_CALC_MODE = { "cd1", "cd2", "cd3", "cd4", "cd5",
    "cd6" };

	private static final String[] PREF_SKILLS = { "sk1", "sk2", "sk3", "sk4",
			"sk5", "sk6" };

	private static final String PREF_SHOW_RESULT = "show_dialog";

	private static final String PREF_COMBO = "Combos";
	private static final String PREF_COUNT = "Count";
	private static final String PREF_SHOW_PATH = "show_path";

	private static final String PREF_BGM = "Bgm";
	private static final String PREF_SOUND = "Sound";

	private static final String PREF_DEFINE_OFFSET = "offset";
	public static final String PREF_COLOR_WEAKNESS = "color_weakness";
	
	private static final String PREF_ANALYSIS_LEFT = "left";
	private static final String PREF_ANALYSIS_TOP = "top";
	private static final String PREF_ANALYSIS_RIGHT = "right";
	private static final String PREF_ANALYSIS_BOTTOM = "bottom";

	public static final String PREF_FULLSCREEN = "fullscreen";
	public static final String PREF_KEEP_RATIO = "keep_ratio";
	
	public static final String PREF_SHINE_EFFECT = "shine_effect";
	
	private static final String[] PREF_SKILL_IMAGES = { "sk_image1",
			"sk_image2", "sk_image3", "sk_image4", "sk_image5", "sk_image6" };

	public enum SkillType {
		SKILL_TRANSFORM(0), SKILL_STANCE(1), SKILL_THE_WORLD(2), SKILL_DROP_RATE(3), 
		SKILL_NONE(99);

		private final int value;

		private SkillType(final int v) {
			value = v;
		}

		public int getValue() {
			return value;
		}

		public static SkillType fromValue(int v) {
			switch (v) {
			case 0:
				return SKILL_TRANSFORM;
			case 1:
				return SKILL_STANCE;
			case 2:
				return SKILL_THE_WORLD;
			}
			return SKILL_NONE;
		}
	}

	private Context mContext;
	private SharedPreferences mPref;
	private float mTimeLimit;
	private int mOrbType;
	private boolean mSoundEnable;
	private boolean[] mDrops;
	private boolean[] mDropsCMode;
	private long mCombos;
	private long mCount;
	private boolean mShowDialog;
	private boolean mShowPath;
	private int mOffset;
	private boolean mColorWeakness;
	private boolean mFullscreen;
	private boolean mKeepRatio;
	private boolean mShineEffect;
	private String mBgm;
	private String mSound;

	private List<Skill> mSkill;
	private List<Integer> mSkillImageList;

	private Map<String, Boolean> mChangedMap;
	
	private Rect mAnalysisRect;

	private static SharedPreferenceUtil sInstance = null;

	public static class SkillData {
		private final List<Long> data;

		private SkillData(List<Long> data1) {
			data = new ArrayList<Long>(data1);
		}

		public static SkillData create(List<Long> data) {
			return new SkillData(data);
		}

		public List<Long> getData() {
			return data;
		}

	}

	public static class Skill {
		private SkillType type;
		private SkillData data;

		private Skill(SkillType t, List<Long> d) {
			type = t;
			data = SkillData.create(d);
		}

		public static Skill create(SkillType t, List<Long> data) {
			return new Skill(t, data);
		}

		public SkillType getType() {
			return type;
		}

		public List<Long> getData() {
			return data.getData();
		}
	}

	public SharedPreferenceUtil(Context context, String name) {
		mContext = context;
		mPref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		mSoundEnable = isSoundEnabled();
		mTimeLimit = getTimeLimit();
		mOrbType = getType();
		mDrops = getDrops();
		mDropsCMode = getDropsInCalcMode();
		mChangedMap = new HashMap<String, Boolean>();
		mSkill = getSkillType();
		mCombos = getCombos();
		mCount = getPlayCount();
		mShowDialog = getShowResultDialog();
		mShowPath = getShowPath();
		mSkillImageList = getSkillImageList();
		mBgm = getBgm();
		mSound = getSound();
		mColorWeakness = getBoolean(PREF_COLOR_WEAKNESS);
		mAnalysisRect = getAnalysisRect();
	}
	
	public Rect getAnalysisRect() {
	    int left = mPref.getInt(PREF_ANALYSIS_LEFT, 0);
	    int top = mPref.getInt(PREF_ANALYSIS_TOP, 0);
	    int right = mPref.getInt(PREF_ANALYSIS_RIGHT, 0);
	    int bottom = mPref.getInt(PREF_ANALYSIS_BOTTOM, 0);
	    return new Rect(left, top, right, bottom);
	}
	
	public void setAnalysisRect(int left, int top, int width, int height) {
	    mAnalysisRect.left = left;
	    mAnalysisRect.top = top;
	    mAnalysisRect.right = left + width;
	    mAnalysisRect.bottom = top + height;
	    mChangedMap.put(PREF_ANALYSIS_LEFT, true);
	    
	}

	public synchronized static SharedPreferenceUtil getInstance(Context context) {
		// if (sInstance == null) {
		// sInstance = new SharedPreferenceUtil(context);
		// }
		// return sInstance;
		return ComboMasterApplication.getsInstance().getSharedPref();
	}

	public boolean getShowPath() {
		return mPref.getBoolean(PREF_SHOW_PATH, false);
	}

	public void setShowPath(boolean show) {
		if (show != mShowPath) {
			mShowPath = show;
			mChangedMap.put(PREF_SHOW_PATH, true);
		}
	}

	public List<Integer> getSkillImages() {
		return Collections.unmodifiableList(mSkillImageList);
	}

	public List<Skill> getSkillList() {
		return Collections.unmodifiableList(mSkill);
	}

	private String getPrefSkillDetailKey(int n, int index) {
		return String.format("skd_%d_%d", n, index);
	}

	private List<Integer> getSkillImageList() {
		List<Integer> imageList = new ArrayList<Integer>();
		for (int i = 0; i < 6; ++i) {
			int id = mPref.getInt(PREF_SKILL_IMAGES[i], 0);
			imageList.add(id);
		}
		return imageList;
	}

	private List<Skill> getSkillType() {
		List<Skill> skill = new ArrayList<SharedPreferenceUtil.Skill>();
		for (int i = 0; i < 6; ++i) {
			int v = mPref.getInt(PREF_SKILLS[i], 0);
			SkillType type = SkillType.fromValue(v);
			List<Long> data = null;
			switch (type) {
			case SKILL_TRANSFORM:
				long transform = mPref.getLong(getPrefSkillDetailKey(i, 0),
						PadBoardAI.TYPE_FIRE | PadBoardAI.TYPE_DARK);
				data = new ArrayList<Long>();
				data.add(transform);
				break;
			case SKILL_STANCE:
				long from1 = mPref.getLong(getPrefSkillDetailKey(i, 0), 0);
				long to1 = mPref.getLong(getPrefSkillDetailKey(i, 1), 0);
				long from2 = mPref.getLong(getPrefSkillDetailKey(i, 2), 0);
				long to2 = mPref.getLong(getPrefSkillDetailKey(i, 3), 0);
				data = new ArrayList<Long>();
				data.add(from1);
				data.add(to1);
				if (from2 != to2) {
					data.add(from2);
					data.add(to2);
				}
				break;
			default:
				break;
			}
			if (data == null) {
				data = new ArrayList<Long>();
			}
			skill.add(Skill.create(type, data));
		}
		return skill;
	}

	public void setSkill(int index, SkillType type) {
		setSkill(index, type, -1L, -1L);
	}

	public void setSkill(int index, SkillType type, long... data) {
		List<Long> list = new ArrayList<Long>();
		for (long d : data) {
			list.add(d);
		}
		mSkill.set(index, Skill.create(type, list));
		mChangedMap.put(PREF_SKILLS[index], true);
	}

	public void setImageId(int index, int id) {
		mSkillImageList.set(index, id);
		mChangedMap.put(PREF_SKILL_IMAGES[index], true);
	}

	public boolean[] getDrops() {
		boolean[] drops = new boolean[6];
		for (int i = 0; i < 6; ++i) {
			drops[i] = mPref.getBoolean(PREF_DROPS[i], true);
		}
		return drops;
	}
	
	public boolean[] getDropsInCalcMode() {
        boolean[] drops = new boolean[6];
        for (int i = 0; i < 6; ++i) {
            drops[i] = mPref.getBoolean(PREF_DROPS_CALC_MODE[i], true);
        }
        return drops;
	}

	public void setBgm(String bgm) {
		mChangedMap.put(PREF_BGM, true);
		mBgm = bgm;
	}
	
	public void setSound(String sound) {
		mChangedMap.put(PREF_SOUND, true);
		mSound = sound;
	}

	public SharedPreferenceUtil(Context context) {
		this(context, Constants.PREF_NAME);

	}

	private static final float[] LIMIT = { 2f, 2.5f, 3, 3.5f, 4f, 4.5f, 5f,
			5.5f, 6f, 6.5f, 7.0f, 7.5f, 8.0f, 8.5f, 9.0f, 9.5f, 10.0f, 600f };

	public void setDrops(boolean[] drops) {
		if (drops != null && drops.length == 6) {
			System.arraycopy(drops, 0, mDrops, 0, drops.length);
			mChangedMap.put(PREF_DROPS[0], true);
		}
	}
	
    public void setDropsInCalcMode(boolean[] drops) {
        if (drops != null && drops.length == 6) {
            System.arraycopy(drops, 0, mDropsCMode, 0, drops.length);
            mChangedMap.put(PREF_DROPS_CALC_MODE[0], true);
        }
    }

	public void setTimeLimit(int position) {
		if (LIMIT.length > position && LIMIT[position] != mTimeLimit) {
			mTimeLimit = LIMIT[position];
			mChangedMap.put(PREF_TIME_LIMIT, true);
		}

	}

	public int getTimeLimitPos() {
		float limit = getTimeLimit();
		for (int i = 0; i < LIMIT.length; ++i) {
			if (LIMIT[i] == limit) {
				return i;
			}
		}
		return 4;
	}

	public float getTimeLimit() {
		return mPref.getFloat(PREF_TIME_LIMIT, 5f);
	}

	public void setType(int type) {
		if (mOrbType != type) {
			mOrbType = type;
			mChangedMap.put(PREF_ORB_TYPE, true);
		}

	}

	public synchronized void addCombo(long combo) {
		mChangedMap.put(PREF_COMBO, true);
		mCombos += combo;
		++mCount;
	}

	public void resetCombo() {
		mChangedMap.put(PREF_COMBO, true);
		mCombos = 0;
		mCount = 0;
		apply();
	}

	public long getCombos() {
		return mPref.getLong(PREF_COMBO, 0L);
	}

	public long getPlayCount() {
		return mPref.getLong(PREF_COUNT, 0L);
	}

	public int getType() {
		return mPref.getInt(PREF_ORB_TYPE, 1);
	}

	public boolean getSoundEnable() {
		return mSoundEnable;
	}

	public void setSoundEnable(boolean enable) {
		if (mSoundEnable != enable) {
			mSoundEnable = enable;
			mChangedMap.put(PREF_SOUND_ENABLE, true);
		}
	}

	public boolean isSoundEnabled() {
		return mPref.getBoolean(PREF_SOUND_ENABLE, true);
	}

	public synchronized boolean apply() {
		boolean changed = mChangedMap.size() > 0;

		if (changed) {
			Editor editor = mPref.edit();
			if (isChanged(PREF_TIME_LIMIT)) {
				editor.putFloat(PREF_TIME_LIMIT, mTimeLimit);
			}
			if (isChanged(PREF_ORB_TYPE)) {
				editor.putInt(PREF_ORB_TYPE, mOrbType);
			}
			if (isChanged(PREF_SOUND_ENABLE)) {
				editor.putBoolean(PREF_SOUND_ENABLE, mSoundEnable);
			}
			if (isChanged(PREF_DROPS[0])) {
				for (int i = 0; i < 6; ++i) {
					editor.putBoolean(PREF_DROPS[i], mDrops[i]);
				}
			}
			if (isChanged(PREF_DROPS_CALC_MODE[0])) {
                for (int i = 0; i < 6; ++i) {
                    editor.putBoolean(PREF_DROPS_CALC_MODE[i], mDropsCMode[i]);
                }
			}
			if (isChanged(PREF_ANALYSIS_LEFT)) {
			    editor.putInt(PREF_ANALYSIS_LEFT, mAnalysisRect.left);
			    editor.putInt(PREF_ANALYSIS_TOP, mAnalysisRect.top);
			    editor.putInt(PREF_ANALYSIS_RIGHT, mAnalysisRect.right);
			    editor.putInt(PREF_ANALYSIS_BOTTOM, mAnalysisRect.bottom);
			}
			for (int i = 0; i < 6; ++i) {
				if (isChanged(PREF_SKILLS[i])) {
					Skill s = mSkill.get(i);
					List<Long> data = s.getData();
					editor.putInt(PREF_SKILLS[i], s.getType().getValue());
					for (int j = 0; j < data.size(); ++j) {
						editor.putLong(getPrefSkillDetailKey(i, j), data.get(j));
					}
				}
			}
			for (int i = 0; i < 6; ++i) {
				if (isChanged(PREF_SKILL_IMAGES[i])) {
					editor.putInt(PREF_SKILL_IMAGES[i], mSkillImageList.get(i));
				}
			}
			if (isChanged(PREF_COMBO)) {
				editor.putLong(PREF_COMBO, mCombos);
				editor.putLong(PREF_COUNT, mCount);
			}

			if (isChanged(PREF_SHOW_RESULT)) {
				editor.putBoolean(PREF_SHOW_RESULT, mShowDialog);
			}

			if (isChanged(PREF_BGM)) {
				editor.putString(PREF_BGM, mBgm);
			}
			if (isChanged(PREF_SOUND)) {
				editor.putString(PREF_SOUND, mSound);
			}
			if (isChanged(PREF_DEFINE_OFFSET)) {
				editor.putInt(PREF_DEFINE_OFFSET, mOffset);
			}
			if (isChanged(PREF_COLOR_WEAKNESS)) {
				editor.putBoolean(PREF_COLOR_WEAKNESS, mColorWeakness);
			}
			if (isChanged(PREF_FULLSCREEN)) {
                editor.putBoolean(PREF_FULLSCREEN, mFullscreen);
            }
			if (isChanged(PREF_KEEP_RATIO)) {
				editor.putBoolean(PREF_KEEP_RATIO, mKeepRatio);
			}
			if (isChanged(PREF_SHINE_EFFECT)) {
			    editor.putBoolean(PREF_SHINE_EFFECT, mShineEffect);
			}
			mChangedMap.clear();

			// BackupManager bm = new BackupManager(mContext);
			// bm.dataChanged();

			return editor.commit();
		}
		return false;
	}

	private boolean isChanged(String tag) {
		return mChangedMap.containsKey(tag) && mChangedMap.get(tag);
	}

	public void setShowResultDialog(boolean isChecked) {
		mChangedMap.put(PREF_SHOW_RESULT, true);
		mShowDialog = isChecked;
	}

	public void setUserDefineOffset(int offset) {
		mChangedMap.put(PREF_DEFINE_OFFSET, true);
		mOffset = offset;
	}

	public boolean getShowResultDialog() {
		return mPref.getBoolean(PREF_SHOW_RESULT, true);
	}

	public String getBgm() {
		return mPref.getString(PREF_BGM, "");
	}
	
	public String getSound() {
		return mPref.getString(PREF_SOUND, "");
	}

	public int getUserDefineOffset() {
		return mPref.getInt(PREF_DEFINE_OFFSET, 0);
	}

	public boolean getBoolean(String key) {
		return mPref.getBoolean(key, false);
	}
	
	public boolean getBoolean(String key, boolean def) {
	    return mPref.getBoolean(key, def);
	}

	public void setBoolean(String key, boolean value) {
		mChangedMap.put(key, true);
		if (PREF_COLOR_WEAKNESS.equals(key)) {
			mColorWeakness = value;
		} else if ( PREF_FULLSCREEN.equals(key) ) {
		    mFullscreen = value;
		} else if (PREF_KEEP_RATIO.equals(key)) {
			mKeepRatio = value;
		} else if (PREF_SHINE_EFFECT.equals(key)) {
		    mShineEffect = value;
		}
	}
}
