package com.a30corner.combomaster;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.SparseArray;

import com.a30corner.combomaster.activity.fragment.TeamFragment;
import com.a30corner.combomaster.pad.PadBoardAI;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.TeamInfo;
import com.a30corner.combomaster.utils.BitmapUtil;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComboMasterApplication extends Application {

	private static ComboMasterApplication sInstance = null;

	private SparseArray<TeamInfo> mTeamList;
	private int mTarget = 0;
	private int mTarget2 = 1;
	private boolean mIsChallengeMode = false;
	private boolean mNullAwoken = false;
	private boolean mIsSpecialMode = false;
	private boolean[] mPetsUp = null;
	private int gameMode; // don't use m anymore...

	private SparseArray<Bitmap> mImageCache = new SparseArray<Bitmap>();
	private int[][] gameBoardCache = null;

	private boolean dataChanged = false;
	private String stage;
	private boolean rewarded = false;
	private boolean copMode = false;
	
	private int showAdScene = 0;

	// The following line should be changed to include the correct property id.
	private static final String PROPERTY_ID = "UA-56776622-1";

	private SharedPreferenceUtil mPref;
	
	private boolean[] dropOrbs = {true,true,true,true,true,true};
	public void setDropOrbs() {
		dropOrbs = new boolean[]{true, true, true, true, true, true};
	}
	
	public void setDropOrbs(int... exclude) {
		for(int exc : exclude) {
			dropOrbs[exc] = false;
		}
	}
	
	public boolean[] getDropOrbs() {
		return dropOrbs;
	}
	
	/**
	 * Enum used to identify the tracker that needs to be used for tracking.
	 *
	 * A single tracker is usually enough for most purposes. In case you do need
	 * multiple trackers, storing them all in Application object helps ensure
	 * that they are created only once per application instance.
	 */
	public enum TrackerName {
		APP_TRACKER, // Tracker used only in this app.
		GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg:
						// roll-up tracking.
		ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a
							// company.
	}

//	HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

//	synchronized Tracker getTracker(TrackerName trackerId) {
//		try {
//			if (!mTrackers.containsKey(trackerId)) {
//
//				GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//				Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics
//						.newTracker(PROPERTY_ID) : analytics
//						.newTracker(R.xml.global_tracker);
//				// (trackerId == TrackerName.GLOBAL_TRACKER) ?
//				// : analytics.newTracker(R.xml.ecommerce_tracker);
//				mTrackers.put(trackerId, t);
//
//			}
//			return mTrackers.get(trackerId);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	public static ComboMasterApplication getsInstance() {
		if (sInstance == null) {
			throw new IllegalStateException("Application not created yet!");
		}
		return sInstance;
	}

	private void initImageLoader() {
	    ImageLoader imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .tasksProcessingOrder(QueueProcessingType.LIFO)
            .build();
        imageLoader.init(config);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();

		Fresco.initialize(this);

		LogUtil.d("Application.onCreate()");
		sInstance = this;
		mPref = new SharedPreferenceUtil(this);
		LogUtil.d("Application.loadTeamDone()");

		initImageLoader();
		
		// cache the first team
		SharedPreferences pref = getSharedPreferences("update", Context.MODE_PRIVATE);
		if ( pref.getBoolean("dataInDB", false) ) {
    		init();
		}
	}

	public SharedPreferenceUtil getSharedPref() {
		return mPref;
	}
	
	public void init() {
	    loadTeamInfo();
	    initBitmapCache();
	}

	public void init2() {
		loadTeamInfo2();
		initBitmapCache2();
	}

	public void onEnterScene(String screen) {
//		Tracker t = getTracker(TrackerName.APP_TRACKER);
//		if ( t != null ) {
//			t.setScreenName(screen);
//			t.send(new HitBuilders.AppViewBuilder().build());
//
//			t.setScreenName(null);
//		}
	}
	
	public void putGaAction(String category, String action) {
		putGaAction(category, action, "");
	}
	
	public void putGaAction(String category, String action, String label) {
//		Tracker t = getTracker(TrackerName.APP_TRACKER);
//		if (t != null) {
//			t.setScreenName(ComboMasterApplication.class.getSimpleName());
//			t.send(new HitBuilders.EventBuilder()
//			.setCategory(category)
//			.setAction(action)
//			.setLabel(label)
//			.build()
//			);
//		}
	}
	
	public void setHasMusic(boolean music, boolean wav) {
//		Tracker t = getTracker(TrackerName.APP_TRACKER);
//		if (t != null) {
//			t.setScreenName(SettingsFragmentActivity.class.getSimpleName());
//			t.send(new HitBuilders.EventBuilder()
//			.setCategory("UX")
//			.setAction("Music:"+(music? 1:0))
//			.build()
//			);
//
//			t.send(new HitBuilders.EventBuilder()
//			.setCategory("UX")
//			.setAction("Sound:"+(wav? 1:0))
//			.build()
//			);
//		}
	}

	private void initBitmapCache() {
		TeamInfo team = mTeamList.get(mTarget, null);
		if (team == null) {
			loadTeamInfo();
			team = mTeamList.get(mTarget);
		}
		Set<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < 6; ++i) {
			MonsterInfo monster = team.getMember(i);
			if (monster != null) {
				set.add(monster.getNo());
			}
		}
		int size = mImageCache.size();
		List<Integer> removeList = new ArrayList<Integer>(size);
		for (int i = 0; i < size; ++i) {
			int key = mImageCache.keyAt(i);
			if (!set.contains(key)) {
				removeList.add(key);
			}
		}
		for (Integer remove : removeList) {
			mImageCache.remove(remove);
		}
		for (int i = 0; i < 6; ++i) {
			MonsterInfo monster = team.getMember(i);
			if (monster != null) {
				int no = monster.getNo();
				Bitmap b = mImageCache.get(no, null);
				if (b == null) {
					Bitmap bitmap = BitmapUtil.getBitmap(this, no + "i.png");
					if (bitmap != null) {
						mImageCache.put(no, bitmap);
					}
				}
			}
		}
	}
	
	private void initBitmapCache2() {
		if(mTarget2 == -1) {
			return ;
		}
		
		TeamInfo team = mTeamList.get(mTarget2, null);
		if (team == null) {
			loadTeamInfo2();
			team = mTeamList.get(mTarget2);
		}
		Set<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < 6; ++i) {
			MonsterInfo monster = team.getMember(i);
			if (monster != null) {
				set.add(monster.getNo());
			}
		}
		int size = mImageCache.size();
		List<Integer> removeList = new ArrayList<Integer>(size);
		for (int i = 0; i < size; ++i) {
			int key = mImageCache.keyAt(i);
			if (!set.contains(key)) {
				removeList.add(key);
			}
		}
		for (Integer remove : removeList) {
			mImageCache.remove(remove);
		}
		for (int i = 0; i < 6; ++i) {
			MonsterInfo monster = team.getMember(i);
			if (monster != null) {
				int no = monster.getNo();
				Bitmap b = mImageCache.get(no, null);
				if (b == null) {
					Bitmap bitmap = BitmapUtil.getBitmap(this, no + "i.png");
					if (bitmap != null) {
						mImageCache.put(no, bitmap);
					}
				}
			}
		}
	}

	private void loadTeamInfo2() {
		if(mTarget2 == -1) {
			return ;
		}
		
		if(mTeamList == null) {
			mTeamList = new SparseArray<TeamInfo>();
		}

		TeamInfo team = new TeamInfo(this, mTarget2);
		team.load();
		mTeamList.put(mTarget2, team);
	}
	
	private void loadTeamInfo() {
		mTeamList = new SparseArray<TeamInfo>();

		TeamInfo team = new TeamInfo(this, mTarget);
		team.load();
		mTeamList.put(mTarget, team);
	}
	
	public void setAdSceneType(int type) {
		showAdScene = type;
	}
	
	public int getAdSceneType() {
		return showAdScene;
	}
	
	public void initBoardCache() {
	    SharedPreferences sp = getSharedPreferences("boardCache", Context.MODE_PRIVATE);
	    if (sp.contains("cache")) {
	        gameBoardCache = new int[PadBoardAI.ROWS][PadBoardAI.COLS];
	        String board = sp.getString("cache", "");
	        int len = board.length();
	        for(int i=0; i<PadBoardAI.ROWS; ++i) {
	            for(int j=0; j<PadBoardAI.COLS; ++j) {
	                int index = i * PadBoardAI.COLS + j;
	                if ( index >= len ) { // prevent out of bound exception
	                    return;
	                }
    	            char ch = board.charAt(index);
    	            int orb = 0;
    	            if ( ch >= '0' && ch <='9' ) {
    	                orb = ch-'0';
    	            } else if ( ch >='A' && ch <= 'Z' ) {
    	                orb = 10+ch-'A';
    	            } else if ( ch >= 'a' && ch <= 'z') {
    	            	orb = 10+ch-'a';
    	            }
    	            if ( (orb >= 0 && orb < 30)) {
    	                gameBoardCache[i][j] = orb;
    	            } else {
    	                gameBoardCache[i][j] = 1;
    	            }
	            }
	        }
	    }
	}
	
	public void setBoardCache(int[][] cache) {
	    if ( gameBoardCache == null ) {
	        gameBoardCache = new int[PadBoardAI.ROWS][PadBoardAI.COLS];
	    }
	    for(int i=0; i<PadBoardAI.ROWS; ++i) {
	        for(int j=0; j<PadBoardAI.COLS; ++j) {
	            gameBoardCache[i][j] = cache[i][j];
	        }
	    }
	}
	
	public int[][] getBoardCache() {
	    return gameBoardCache;
	}
	
	public void saveBoardCache() {
	    if ( gameBoardCache != null ) {
	        StringBuilder sb = new StringBuilder();
	        for(int i=0; i<PadBoardAI.ROWS; ++i) {
	            for(int j=0; j<PadBoardAI.COLS; ++j) {
	                int orb = gameBoardCache[i][j];
	                if ( orb >= 0 && orb < 10 ) {
	                    sb.append(Character.toString((char)('0'+orb)));
	                } else if ( orb >= 10 && orb < 20 ) {
	                    sb.append(Character.toString((char)('A'+orb-10)));
	                } else if ( orb >= 20 && orb < 30 ) {
	                	sb.append(Character.toString((char)('a'+orb-10)));
	                }
	            }
	        }
	        SharedPreferences sp = getSharedPreferences("boardCache", Context.MODE_PRIVATE);
	        sp.edit().putString("cache", sb.toString()).apply();
	    }
	}

	public void updateTeamInfo(int boxIndex) {
		LogUtil.d("update ", boxIndex, " monster");
		for (int i = 0; i < TeamFragment.TEAM_SIZE; ++i) {
			updateTeamIfAny(i, boxIndex);
		}
		initBitmapCache();
		initBitmapCache2();
	}

	public void setTargetTeam(int no) {
		if (mTarget != no) {
			mTarget = no;
			initBitmapCache();
		}
	}
	
	public void setTargetTeam2(int no) {
		if(mTarget2 != no) {
			mTarget2 = no;
			initBitmapCache2();
		}
	}
	
	public void setNullAwokenSkill(boolean isNull) {
	    mNullAwoken = isNull;
	}
	
	public void setCopMode(boolean cop) {
		copMode = cop;
	}
	
	public boolean isCopMode() {
		return copMode;
	}
	
	public void setGameMode(int mode) {
		gameMode = mode;
	}
	
	public int getGameMode() {
		return gameMode;
	}
	
	@Deprecated
	public void setChallengeMode(boolean isChallengeMode) {
	    mIsChallengeMode = isChallengeMode;
	}
	
	public boolean isAwokenNulled() {
	    return mNullAwoken;
	}
	
	@Deprecated
	public boolean isChallengeMode() {
	    return mIsChallengeMode;
	}

	public Bitmap getBitmap(int no) {
		return mImageCache.get(no, null);
	}

	public int getTargetNo() {
		return mTarget;
	}

	public TeamInfo getTargetTeam() {
		return getTeam(mTarget, gameMode);
	}
	
	public TeamInfo[] getTarget2Team() {
		return new TeamInfo[]{getTeam(mTarget, gameMode), getTeam(mTarget2, gameMode)};
	}	

	public void updateTeam(int index) {
		TeamInfo team = new TeamInfo(this, index);
		team.load();
		mTeamList.put(index, team);
	}

	public void updateTeamIfAny(int index, int boxIndex) {
		TeamInfo info = mTeamList.get(index, null);
		if (info != null) {
			TeamInfo team = new TeamInfo(this, index);

			if (team.containAny(boxIndex)) {
				LogUtil.d("team ", index, " contains no=xxx, update it");
				team.load();
				mTeamList.put(index, team);
			}
		}
	}
	
	public TeamInfo getTeam(int no, int mode) {
		if(no == -1) {
			return null;
		}
		
	    TeamInfo info = new TeamInfo(this, no);
		info.load();
		// we need every object is isolated, otherwise hen-shin will also change other instance
//        if (mTeamList.get(no, null) == null) {
//            info = new TeamInfo(this, no);
//            info.load();
//        } else {
//            info = mTeamList.get(no);
//        }
        info.init(mode, mPetsUp, copMode);
        return info;
	}

	public TeamInfo getTeam(int no) {
		return getTeam(no, gameMode);
	}

	public void dataChanged() {
		// FIXME: didn't support backup currently
		// dataChanged = true;
		dataChanged = false;
	}

	@Deprecated
    public void setSpecialMode(boolean special) {
        mIsSpecialMode = special;
    }
    
	@Deprecated
    public boolean isSpecialMode() {
        return mIsSpecialMode;
    }
    
    public void setPowerUp(boolean[] index) {
    	mPetsUp = index;
    }
    
    public boolean[] getPowerUp() {
    	return mPetsUp;
    }

	public void setStage(String string) {
		stage = string;
	}
	
	public String getStage() {
		return stage;
	}

	public void setRewarded(boolean b) {
		rewarded = b;
	}
	
	public boolean rewarded() {
		return rewarded;
	}
}
