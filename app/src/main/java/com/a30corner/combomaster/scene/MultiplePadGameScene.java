package com.a30corner.combomaster.scene;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;
import android.util.SparseArray;
import android.view.KeyEvent;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.activity.GameActivity;
import com.a30corner.combomaster.engine.sprite.BasicHpSprite.AnimationCallback;
import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.manager.SceneManager;
import com.a30corner.combomaster.pad.DamageCalculator;
import com.a30corner.combomaster.pad.DamageCalculator.AttackValue;
import com.a30corner.combomaster.pad.GameReplay;
import com.a30corner.combomaster.pad.Match;
import com.a30corner.combomaster.pad.MatchBoard;
import com.a30corner.combomaster.pad.PadBoardAI;
import com.a30corner.combomaster.pad.PadBoardAI.IMAGE_TYPE;
import com.a30corner.combomaster.pad.RowCol;
import com.a30corner.combomaster.pad.RowCol.Direction;
import com.a30corner.combomaster.pad.mode7x6.PadBoardAI7x6;
import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.pad.monster.LeaderSkill;
import com.a30corner.combomaster.pad.monster.LeaderSkill.LeaderSkillType;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill.AwokenSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MoneyAwokenSkill;
import com.a30corner.combomaster.pad.monster.TeamInfo;
import com.a30corner.combomaster.playground.MultiplePadEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.Team;
import com.a30corner.combomaster.playground.effect.Shake;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.playground.entity.FlashSprite;
import com.a30corner.combomaster.scene.PadGameScene.GameState;
import com.a30corner.combomaster.texturepacker.GameUIAssets;
import com.a30corner.combomaster.texturepacker.SimulateAssets;
import com.a30corner.combomaster.texturepacker.TextureOrbs;
import com.a30corner.combomaster.texturepacker.TextureOrbsCw;
import com.a30corner.combomaster.texturepacker.TextureOrbsTos;
import com.a30corner.combomaster.utils.Constants;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.RandomUtil;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;
import com.a30corner.combomaster.utils.SimulatorConstants;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.color.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType.ST_RANDOM_CHANGE_FIX;

public class MultiplePadGameScene extends PlaygroundGameScene implements
		IOnMenuItemClickListener {

	private int currentTeam = 0;

	// board info
	// private BoardStack mStack;
	private int[][] gameBoard;
	private SparseArray<ITextureRegion> textureOrbs = new SparseArray<ITextureRegion>();
	private Sprite[][] sprites = new Sprite[PadBoardAI.ROWS][PadBoardAI.COLS];
	private Sprite[][] boardSprite = new Sprite[PadBoardAI.ROWS][PadBoardAI.COLS];
	private List<Sprite> cloudSprite = new ArrayList<Sprite>();
	// edit mode

	private Sprite mBackground;

	private Sprite[] mCache = new Sprite[10];
	private Sprite[][] mBindSprite = new Sprite[2][6];

	private Sprite mCrossShield = null;

	// move mode
	private Sprite orbInHand = null;

	private Text mLeaderSkillCombo = null;
	private List<Text> mComboText = new ArrayList<Text>();
	private List<Text> mFactorText = new ArrayList<Text>();
	private Text mHeals;
	private Text mPoisonText;

	private ResourceManager mResMgr;

	private MultiplePadEnvironment mEnvironment;

	private ActiveSkill mFiredSkill = null;
	private int poisonDropCount = 0, jammerDropCount = 0;

	// detach list
	private Set<Integer> mShineSet = new HashSet<Integer>();
	private List<IEntity> mRemoveList = new ArrayList<IEntity>();

	private static final int RELEASE_THRESHOLD = 10;

	// FIXME: use sharepreference
	private static final float PROGRESS_BAR_WIDTH = GameActivity.SCREEN_WIDTH - 10;

	private static int TIME_CHANGE_THE_WORLD = 10 * 1000;
	private boolean stillMyTurn = false;
	private boolean alreadySwap = false;
	private List<Integer> cantRemove = null;
	private int mAdjustTime, mAdjustTimeX;
	private GameState mCurrentState = GameState.GAME_INIT;

	private int mTimeFixed = 0;
	private int mTimeRemain = Constants.DEFAULT_SECOND;
	private int[] mDropTime = { Constants.DEFAULT_SECOND, Constants.DEFAULT_SECOND };
	private long mDropType;

	private int[][] mPlusOrbCount = { { 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0 } };

	private TeamInfo[] mTeam;
	private int removeN = 3;
	private boolean forceNoDrop = false, normalDrop = true;
	private boolean mNullAwoken = false;
	public boolean playerAttacked = false;
	
	private int gameMode = 0;
	Map<String, Object> mSettings = new HashMap<String, Object>();

	private ActiveSkill mAddComboSkill = null;
	private ActiveSkill mDropLock = null;
	private ActiveSkill mDropOnlySkill = null;
	private ActiveSkill mDropRateSkill = null;
	private ActiveSkill mDropEnhanceSkill = null;
	private ActiveSkill mNoDropSkill = null;
	private ActiveSkill mNegativeDrop = null;

	private Rectangle mProgressbar;

	private Handler handler;

	private AtomicInteger mCombo = new AtomicInteger(99);

	private AtomicBoolean mSkillTouchable = new AtomicBoolean(true);
	private AtomicBoolean mCountDown = new AtomicBoolean(false);
	private AtomicBoolean mChangeTheWorld = new AtomicBoolean(false);

	TimerHandler thandler;

	private class ShineCallback implements ITimerCallback {

		private AtomicInteger index = new AtomicInteger(0);

		@Override
		public void onTimePassed(final TimerHandler pTimerHandler) {
			int current = index.getAndIncrement();
			if (current <= 5) {
				addShineEffect(current);
			} else if (current == 9) {
				index.set(0);
			}
		}

	}

	//
	// private List<TimerHandler> mShineTimers;

	private TimerHandler mTimer = new TimerHandler(0.02f, true,
			new ITimerCallback() {

				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					mTimeRemain -= 20;
					float progress = mTimeRemain
							/ (float) mDropTime[currentTeam];
					mProgressbar.setWidth(PROGRESS_BAR_WIDTH * progress);
					float green = 1.0f * progress;
					float red = 1.0f - green;
					mProgressbar.setColor(red, green, 0f);

					int timePass = mDropTime[currentTeam] - mTimeRemain;
					int sec = timePass / 1000;
					if (sec > 99) {
						sec = 99;
					}

					if (mTimeRemain <= 0) {
						onTimeUp();
					}
				}
			});
	private TimerHandler mCTWTimer = new TimerHandler(0.02f, true,
			new ITimerCallback() {

				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					mTimeRemain -= 20;
					float progress = mTimeRemain
							/ (float) TIME_CHANGE_THE_WORLD;
					mProgressbar.setWidth(PROGRESS_BAR_WIDTH * progress);
					float green = 1.0f * progress;
					float red = 1.0f - green;
					mProgressbar.setColor(red, green, 0f);

					int timePass = TIME_CHANGE_THE_WORLD - mTimeRemain;
					int sec = timePass / 1000;
					if (sec > 99) {
						sec = 99;
					}

					if (mTimeRemain <= 0) {
						onTimeUp();
					}
				}
			});

	private GameReplay mReplay;
	private List<Match> mScoreBoard = new ArrayList<Match>();

	private interface IAnimationCallback {
		public void onAnimationFinished();
	}

	private class NonUiHandler extends Handler {

		protected static final int MSG_REMOVAL = 0;

		public NonUiHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MSG_REMOVAL:
				MatchBoard board = (MatchBoard) msg.obj;

				doRemoval(board, msg.arg1);
				break;
			}

		}
	}

	@Override
	public void setDefaultTime(int defaultTime) {
		mTimeFixed = defaultTime;
		if(mTimeFixed > 0) {
			mDropTime[0] = mTimeFixed;
			mDropTime[1] = mTimeFixed;
		}
	}

	@Override
	public void setNormalDrop(boolean normal) {
		forceNoDrop = !normal;
		normalDrop = !forceNoDrop;
	}

	private List<Double> getPoisonDamage(List<Match> matches) {
		int size = matches.size();
		List<Double> list = new ArrayList<Double>();
		for (int i = 0; i < size; ++i) {
			list.add(DamageCalculator.calculatePoison(
					mEnvironment.mTeamData.team(), mEnvironment.mTeamData.getHp(),
					matches.subList(0, i + 1),
					null));
		}
		return list;
	}

	private List<Double> getRecovery(List<Match> matches) {
		Map<String, Object> settings = new HashMap<String, Object>();
		settings.put("nullAwoken", isNullAwoken());
		int size = matches.size();
		List<Double> list = new ArrayList<Double>();
		for (int i = 0; i < size; ++i) {
			list.add(DamageCalculator.calculateRecovery(
					mEnvironment.mTeamData.team(), matches.subList(0, i + 1),
					null, settings));
		}
		return list;
	}

	private List<Double> getFactor(List<Match> matches) {
		int size = matches.size();
		List<Double> list = new ArrayList<Double>();
		TeamInfo team = mEnvironment.mTeamData.team();
		Map<String, Object> settings = new HashMap<String, Object>();
		List<Boolean> bindStatus = new ArrayList<Boolean>();
		for (int i = 0; i < 6; ++i) {
			if (mEnvironment.mMemberList[currentTeam][i] != null) {
				bindStatus.add(mEnvironment.mMemberList[currentTeam][i]
						.isBinded());
			} else {
				bindStatus.add(false);
			}
		}
		settings.put("bindStatus", bindStatus);
		settings.put("isHpChecked", true);
		settings.put("hp", 100 * mEnvironment.mTeamData.getCurrentHp()
				/ (float) mEnvironment.mTeamData.getHp());
		settings.put("cross", mEnvironment.getCrossReduceShield());


		for (int i = 0; i < size; ++i) {
			double f1 = 1.0;
			if (!mEnvironment.mMemberList[currentTeam][0].isBinded()) {
				f1 = DamageCalculator.getLeaderFactor(team,
						mEnvironment.mMemberList[currentTeam][0].info,
						MonsterInfo.empty(0), matches.subList(0, i + 1),
						settings, true);
			}
			double f2 = 1.0;
			if (!mEnvironment.mMemberList[currentTeam][5].isBinded()) {
				f2 = DamageCalculator.getLeaderFactor(team,
						mEnvironment.mMemberList[currentTeam][5].info,
						MonsterInfo.empty(0), matches.subList(0, i + 1),
						settings, true);
			}
			list.add(f1 * f2);
		}
		return list;
	}

	private float getScaleRatio() {
		return 1f;
	}

	private float getPlusScale() {
		return 28f / 45f;
	}

	private void addToDetachList(IEntity entity) {
		if (entity != null) {
			entity.setVisible(false);
			synchronized (mRemoveList) {
				mRemoveList.add(entity);
			}

		}

	}

	private void doRemoval(final MatchBoard board, final int current) {
		if (board.matches.size() == current) {
			// end
			// calculating.set(false);
			// remove empty orbs and make new orbs
			adjustGameBoard(board);
		} else {
			Match match = board.matches.get(current);
			mScoreBoard.add(match);
			mCombo.incrementAndGet();
			final int size = match.list.size();
			final AtomicInteger counter = new AtomicInteger(0);

			// show combo text animation and remove animation
			int comboCount = mCombo.get();
			if (comboCount > 99) {
				comboCount = 99;
			}
			RowCol pos = match.list.get(match.count / 2);
			Sprite sprite = sprites[pos.row][pos.col];
			Text text = null;
			Text factor = null;
			if (comboCount <= mComboText.size()) {
				text = mComboText.get(comboCount - 1);
				factor = mFactorText.get(comboCount - 1);
			} else {
				text = new Text(0f, 0f, ResourceManager.getInstance()
						.getFontStroke(),
						String.format("Combo %d", comboCount), vbom);
				text.setColor(Color.YELLOW);
				text.setZIndex(6);
				mComboText.add(text);
				factor = new Text(0f, 0f, ResourceManager.getInstance()
						.getFontStroke(), "x10000.00 ", vbom);
				factor.setAlpha(1.0f);
				factor.setColor(Color.YELLOW);
				factor.setZIndex(7);
				factor.setVisible(true);
				mFactorText.add(factor);
				attach(factor);
			}

			text.setAlpha(1.0f);
			text.setPosition(sprite.getX(), sprite.getY());
			attach(text);
			double f = factors.get(comboCount - 1);
			if (f > 1.0) {
				factor.setAlpha(1.0f);
				factor.setPosition(sprite.getX(),
						sprite.getY() + factor.getHeight());
				factor.setText(String.format("x%.2f", f));
				factor.setVisible(true);
			}

			double poison = poisoned.get(comboCount - 1)+bombed*0.2*mEnvironment.mTeamData.getHp();
			if (poison > 0.0) {
				mPoisonText.setText(String.format("-%.0f", poison));
				float y = Constants.OFFSET_Y - 30;
				mPoisonText.setPosition(48, y);
				mPoisonText.setVisible(true);
			}

			double r = heals.get(comboCount - 1);
			if (r > 0.0) {
				mHeals.setText(String.format("+%.0f", r));
				float y = Constants.OFFSET_Y - 24;
				mHeals.setPosition(
						(GameActivity.SCREEN_WIDTH - mHeals.getWidth()) / 2, y);
				mHeals.setVisible(true);
			}
			mResMgr.playComboSound(comboCount);

			for (RowCol rc : match.list) {
				final Sprite s = sprites[rc.row][rc.col];
				final IEntity entity = s.getChildByTag(1);
				if (entity != null) {
					engine.runOnUpdateThread(new Runnable() {

						@Override
						public void run() {
							if (!entity.isDisposed()) {
								entity.dispose();
								s.detachChild(entity);
							}
						}
					});
				}
				s.registerEntityModifier(new AlphaModifier(
						SimulatorConstants.SECOND_REMOVE, 1.0f, 0.0f) {
					protected void onModifierFinished(
							final org.andengine.entity.IEntity pItem) {
						int total = counter.incrementAndGet();
						addToDetachList(pItem);

						if (total == size) {
							Message.obtain(handler, NonUiHandler.MSG_REMOVAL,
									current + 1, 0, board).sendToTarget();
						}
					}
				});
			}
		}
	}

    private void clearCombo() {
        int cnt = mCombo.get();
        int addition = 0;
        if(mAddComboSkill != null) {
        	addition = mAddComboSkill.getData().get(1);
        }
        for(int c=addition; c<cnt; ++c) {
            mComboText.get(c).registerEntityModifier(new AlphaModifier(0.3f, 1.0f, 0.0f){
                @Override
                protected void onModifierFinished(IEntity pItem) {
                    detach(pItem);
                }
            });
            mFactorText.get(c).registerEntityModifier(new AlphaModifier(0.3f, 1.0f, 0.0f){
                @Override
                protected void onModifierFinished(IEntity pItem) {
                	pItem.setVisible(false);
                }
            });
        }
		if(mLeaderSkillCombo != null) {
			mLeaderSkillCombo.registerEntityModifier(new AlphaModifier(0.3f, 1.0f, 0.0f) {
				@Override
				protected void onModifierFinished(IEntity pItem) {
					detach(pItem);
				}
			});
			mLeaderSkillCombo = null;
		}
    }

	
	private void adjustGameBoard(MatchBoard board) {
		gameBoard = PadBoardAI.in_place_remove_matches(gameBoard, board.board);
		boolean hasChanged = true;
		ArrayList<Pair<RowCol, RowCol>> dropList = new ArrayList<Pair<RowCol, RowCol>>();
		while (hasChanged) {
			hasChanged = false;

			for (int i = 0; i < PadBoardAI.ROWS; ++i) {
				RowCol target = null;
				RowCol orb = null;
				for (int j = PadBoardAI.COLS - 1; j >= 0; --j) {
					if (gameBoard[i][j] == PadBoardAI.X_ORBS) {
						if (target == null) {
							target = RowCol.make(i, j);
						}
					} else {
						if (target != null) {
							orb = RowCol.make(i, j);
							break;
						}
					}
				}
				if (target != null && orb != null) {
					dropList.add(new Pair<RowCol, RowCol>(target, orb));
					gameBoard[target.row][target.col] = gameBoard[orb.row][orb.col];
					gameBoard[orb.row][orb.col] = PadBoardAI.X_ORBS;
					hasChanged = true;
				} else if (mNoDropSkill == null && normalDrop && target != null) {
					// only drop once in this mode
					// create 1~5 orbs and add to drop list...

					// adjust drop rate by active skill
					List<Pair<Integer, Integer>> dropData = new ArrayList<Pair<Integer, Integer>>();
					if (mDropOnlySkill == null) {
						if (mDropRateSkill != null) {
							List<Integer> data = mDropRateSkill.getData();
							int size = data.size();
							for (int index = 1; index < size; index += 2) {
								dropData.add(new Pair<Integer, Integer>(data
										.get(index), data.get(index + 1)));
							}
						}
						int poisonRate = 0, jammerRate = 0;
						if (mNegativeDrop != null) {
							List<Integer> data = mNegativeDrop.getData();
							int size = data.size();
							for (int index = 1; index < size; index += 2) {
								int val = data.get(index);
								if(val != 6 && val != 7) {
									dropData.add(new Pair<Integer, Integer>(data
											.get(index), data.get(index + 1)));
								} else if (val == 6){
									poisonRate = data.get(index + 1);
								} else {
									jammerRate = data.get(index + 1);
								}
							}
						}
						if(poisonDropCount>0) {
							poisonRate += Constants.POISON_DROP_RATE;
						}
						if (poisonRate > 0) {
							dropData.add(new Pair<Integer, Integer>(6, poisonRate));
						}
						if(jammerDropCount>0) {
							jammerRate += Constants.POISON_DROP_RATE;
						}
						if (jammerRate > 0) {
							dropData.add(new Pair<Integer, Integer>(7, jammerRate));
						}
					}

					int lockPercent = 0;
					int targetOrb = -1;
					if (mDropLock != null) {
						targetOrb = mDropLock.getData().get(1);
						lockPercent = mDropLock.getData().get(2);
					}

					int addition = 0;
					if (mDropEnhanceSkill != null) {
						addition = mDropEnhanceSkill.getData().get(1);
					}

					int emptyLen = target.col + 1;
					for (int k = target.col; k >= 0; --k) {
						int neworb;
						if (mDropOnlySkill == null) {
							neworb = PadBoardAI7x6.getNewOrbRestrictChances(
									mDropType, dropData);
						} else {
							neworb = PadBoardAI.getNewOrb(mDropOnlySkill.getData());
						}
						if (neworb <= 5
								&& RandomUtil
								.getLuck(mPlusOrbCount[currentTeam][neworb] * 20 + addition)) {
							neworb += 10;
						}
						if (lockPercent > 0) {
							if (targetOrb == -1
									&& RandomUtil.getLuck(lockPercent)) {
								neworb += 20;
							} else if ((neworb % 10) == targetOrb
									&& RandomUtil.getLuck(lockPercent)) {
								neworb += 20;
							}

						}

						gameBoard[target.row][k] = neworb;
						dropList.add(new Pair<RowCol, RowCol>(RowCol.make(
								target.row, k), RowCol.make(target.row, k
								- emptyLen)));
					}
					hasChanged = true;
				}
			}
		}
		if (dropList.size() > 0) {
			for (Pair<RowCol, RowCol> pair : dropList) {
				final RowCol target = pair.first;
				final RowCol orb = pair.second;
				int fromY = orb.col * SimulatorConstants.ORB_SIZE
						+ SimulatorConstants.OFFSET_Y;
				int toY = target.col * SimulatorConstants.ORB_SIZE
						+ SimulatorConstants.OFFSET_Y;

				if (PadBoardAI.isValid(orb.row, orb.col)) {
					Sprite temp = sprites[orb.row][orb.col];
					sprites[orb.row][orb.col] = sprites[target.row][target.col];
					sprites[target.row][target.col] = temp;
				} else { // drop from sky
					Sprite newSprite = getSprite(gameBoard[target.row][target.col]);
					newSprite.setPosition(target.row
							* SimulatorConstants.ORB_SIZE
							+ SimulatorConstants.OFFSET_X, fromY);
					newSprite.setZIndex(3);
					sprites[target.row][target.col] = newSprite;
					attach(newSprite);
				}

				sprites[target.row][target.col]
						.registerEntityModifier(new MoveYModifier(
								SimulatorConstants.SECOND_DROP, fromY, toY) {
							protected void onModifierFinished(IEntity pItem) {
								pItem.setZIndex(0);
							}

							;
						});
			}

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					findMatches();
				}
			}, (long) (1000 * (SimulatorConstants.SECOND_DROP + 0.05f)));

		} else {

			addAdditionCombos(new Runnable(){

				@Override
				public void run() {
					finishCombo();
				}
			});

		}
	}

	private void addAdditionCombos(final Runnable runnable) {
		int additionCombos = 0;

		MonsterInfo m0 = mTeam[currentTeam].getMember(0);
		MonsterInfo f0 = mTeam[currentTeam].getMember(5);
		additionCombos += getAdditionCombos(m0) + getAdditionCombos(f0);

		if(additionCombos > 0) {
			Text text = mComboText.get(mCombo.get()-1);
			mLeaderSkillCombo = new Text(text.getX(), text.getY() - 20, ResourceManager.getInstance().getFontStroke(), String.format("Combo %d", mCombo.get() + additionCombos), vbom);
			mLeaderSkillCombo.setColor(Color.YELLOW);
			mLeaderSkillCombo.setZIndex(6);
			mLeaderSkillCombo.setScale(1.3f);

			List<Match> match = addCombos(additionCombos);
			mScoreBoard.addAll(match);

			mLeaderSkillCombo.registerEntityModifier(new ScaleModifier(0.4f, 2f, 1.0f){
				@Override
				protected void onModifierFinished(IEntity pItem) {
					runnable.run();
				}
			});
			attachChild(mLeaderSkillCombo);
		} else {
			runnable.run();
		}
	}

	private int getAdditionCombos(MonsterInfo m) {
		if(m == null) {
			return 0;
		}
		int combos = 0;
		List<LeaderSkill> ls = m.getLeaderSkill();
		LeaderSkill colorSkill = null;
		for(LeaderSkill s : ls) {
			if(s.getType() == LeaderSkillType.LST_COLOR_FACTOR) {
				colorSkill = s;
			}
			if(s.getType() == LeaderSkillType.LST_COMBO_ATTACK) {
				//double f = DamageCalculator.getLeaderFactor(mTeam[currentTeam],s,m,mScoreBoard,null,true);
				if(DamageCalculator.isLSMatched(mTeam[currentTeam],s,m,mScoreBoard,null,true)) {
					return DamageCalculator.getLSCombo(s);
				}
			} else if (s.getType() == LeaderSkillType.LST_COLOR_COMBO) {
				if(colorSkill != null) {
					double f = DamageCalculator.getLeaderFactor(mTeam[currentTeam],colorSkill,m,mScoreBoard,null,true, false);
					if(f>1.0) {
						return DamageCalculator.getLSCombo(s);
					}
				}
			} else if (s.getType() == LeaderSkillType.LST_TARGET_ORB_ADD_COMBO) {
				if(DamageCalculator.isLSMatched(mTeam[currentTeam],s,m,mScoreBoard,null,true)) {
					return DamageCalculator.getLSCombo(s);
				}
			}
		}
		return combos;
	}
	private List<Match> addCombos(int addition) {
		ArrayList<RowCol> list = new ArrayList<RowCol>();
		list.add(RowCol.make(0, 0));
		list.add(RowCol.make(0, 1));
		list.add(RowCol.make(0, 2));

		List<Match> match = new ArrayList<Match>();
		for(int i=0; i<addition; ++i) {
			match.add(Match.make(9, 3, 0, list));
		}
		return match;
	}

	private void finishCombo() {

		if(normalDrop && mNoDropSkill == null) {
			triggerAttack();
		} else {
			startDropping();
		}
	}


    private void startDropping() {
        //gameBoard = PadBoardAI.in_place_remove_matches(gameBoard, board.board);
        boolean hasChanged = true;
        ArrayList<Pair<RowCol, RowCol>> dropList = new ArrayList<Pair<RowCol, RowCol>>();
        while (hasChanged) {
            hasChanged = false;

            for (int i = 0; i < PadBoardAI.ROWS; ++i) {
                RowCol target = null;
                RowCol orb = null;
                for (int j = PadBoardAI.COLS - 1; j >= 0; --j) {
                    if (gameBoard[i][j] == PadBoardAI.X_ORBS) {
                        if (target == null) {
                            target = RowCol.make(i, j);
                        }
                    } else if (target != null) {
                        orb = RowCol.make(i, j);
                        break;
                    }
                }
                if (target != null && orb != null) {
                    dropList.add(new Pair<RowCol, RowCol>(target, orb));
                    gameBoard[target.row][target.col] = gameBoard[orb.row][orb.col];
                    gameBoard[orb.row][orb.col] = PadBoardAI.X_ORBS;
                    hasChanged = true;
                } else if (target != null) {
                    // only drop once in this mode
                    // create 1~5 orbs and add to drop list...

                    // adjust drop rate by active skill
                    List<Pair<Integer, Integer>> dropData = new ArrayList<Pair<Integer, Integer>>();
                    if (mDropRateSkill != null) {
                        List<Integer> data = mDropRateSkill.getData();
                        int size = data.size();
                        for (int index = 1; index < size; index += 2) {
                            dropData.add(new Pair<Integer, Integer>(data
                                    .get(index), data.get(index + 1)));
                        }
                    }
					int poisonRate = 0, jammerRate = 0;
					if (mNegativeDrop != null) {
						List<Integer> data = mNegativeDrop.getData();
						int size = data.size();
						for (int index = 1; index < size; index += 2) {
							int val = data.get(index);
							if(val != 6 && val != 7) {
								dropData.add(new Pair<Integer, Integer>(val, data.get(index + 1)));
							} else if (val == 6){
								poisonRate = data.get(index + 1);
							} else {
								jammerRate = data.get(index + 1);
							}
						}
					}
					if(poisonDropCount>0) {
						poisonRate += Constants.POISON_DROP_RATE;
					}
					if (poisonRate > 0) {
						dropData.add(new Pair<Integer, Integer>(6, poisonRate));
					}
					if(jammerDropCount>0) {
						jammerRate += Constants.POISON_DROP_RATE;
					}
					if (jammerRate > 0) {
						dropData.add(new Pair<Integer, Integer>(7, jammerRate));
					}
                    int lockPercent = 0;
                    int targetOrb = -1;
                    if(mDropLock != null) {
                    	targetOrb = mDropLock.getData().get(1);
                    	lockPercent = mDropLock.getData().get(2);
                    }
                	
                    int emptyLen = target.col + 1;
                    for (int k = target.col; k >= 0; --k) {
                        int neworb = PadBoardAI.getNewOrbRestrictChances(
                                mDropType, dropData);
                        if (neworb <= 5
                                && RandomUtil
                                        .getLuck(mPlusOrbCount[currentTeam][neworb] * 20)) {
                            neworb += 10;
                        }
                        if(lockPercent>0) {
                        	if(targetOrb == -1 && RandomUtil.getLuck(lockPercent)) {
                        		neworb += 20;
                        	} else if((neworb%10) == targetOrb && RandomUtil.getLuck(lockPercent)) {
                        		neworb += 20;
                        	}
                        	
                        }

                        gameBoard[target.row][k] = neworb;
                        dropList.add(new Pair<RowCol, RowCol>(RowCol.make(
                                target.row, k), RowCol.make(target.row, k
                                - emptyLen)));
                    }

                    hasChanged = true;
                }
            }
        }
        if (dropList.size() > 0) {
            for (Pair<RowCol, RowCol> pair : dropList) {
                final RowCol target = pair.first;
                final RowCol orb = pair.second;
                int fromY = orb.col * SimulatorConstants.ORB_SIZE + SimulatorConstants.OFFSET_Y;
                int toY = target.col * SimulatorConstants.ORB_SIZE + SimulatorConstants.OFFSET_Y;

                if (PadBoardAI.isValid(orb.row, orb.col)) {
                    Sprite temp = sprites[orb.row][orb.col];
                    sprites[orb.row][orb.col] = sprites[target.row][target.col];
                    sprites[target.row][target.col] = temp;
                } else { // drop from sky
                    Sprite newSprite = getSprite(gameBoard[target.row][target.col]);
                    newSprite.setPosition(target.row * SimulatorConstants.ORB_SIZE
                            + SimulatorConstants.OFFSET_X, fromY);
                    newSprite.setZIndex(3);
                    sprites[target.row][target.col] = newSprite;
                    attach(newSprite);
                }

                sprites[target.row][target.col]
                        .registerEntityModifier(new MoveYModifier(
                                SimulatorConstants.SECOND_DROP, fromY, toY) {
                            protected void onModifierFinished(IEntity pItem) {
                                pItem.setZIndex(0);
                            };
                        });
            }
        }
        triggerAttack();
    }

	@Override
	public int popBombCount() {
		int bombCount = bombed;
		bombed = 0;
		return bombCount;
	}

	private void triggerAttack() {
		Collections.sort(mScoreBoard, mScoreComparator);

		clearCombo();
		mHeals.setVisible(false);
		mPoisonText.setVisible(false);
		matches.clear();
		factors.clear();
		// TODO: it could be skill clear board
		if (mFiredSkill == null
				|| mFiredSkill.getType() != SkillType.ST_CLEAR_BOARD) {
			changeState(GameState.GAME_ENEMY_TURN);
		}
		mEnvironment.triggerAttackPhase();
	}
	
	public boolean isClearBoard() {
		return mFiredSkill != null
				&& mFiredSkill.getType() == SkillType.ST_CLEAR_BOARD;
	}

	private Comparator<Match> mScoreComparator = new Comparator<Match>() {

		@Override
		public int compare(Match lhs, Match rhs) {
			if (lhs.type == rhs.type) {
				if (lhs.count < rhs.count) {
					return -1;
				} else if (lhs.count == rhs.count) {
					return 0;
				}
				return 1;
			} else {
				return lhs.type < rhs.type ? -1 : 1;
			}
		}
	};

	private void loadSharedPreference() {
		// SharedPreferenceUtil sp = SharedPreferenceUtil.getInstance(activity);
		mDropTime[0] = mDropTime[1] = Constants.DEFAULT_SECOND;// (int) (1000 * sp.getTimeLimit());
		mDropType = PadBoardAI.getType(ComboMasterApplication.getsInstance()
				.getDropOrbs());
	}

	@Override
	public void enableCrossShield(boolean enable) {
		mCrossShield.setVisible(enable);
	}

	public void removeDropRateSkill(boolean negative) {
		if (negative) {
			mNegativeDrop = null;
		} else {
			mDropRateSkill = null;
		}
	}

	public void removeDropLock() {
		mDropLock = null;
	}

	public boolean skillFired(ActiveSkill skill, ICastCallback callback,
			boolean byEnemy) {
		if(!byEnemy) {
			mFiredSkill = skill;
		}
		SkillType type = skill.getType();
		LogUtil.d("fire skill=", type);

		switch (type) {
			case ST_RECOVER_LOCK_REMOVE: {
				return false;
			}
			case ST_LOCK_REMOVE: {
				List<Integer> data = skill.getData();
				cantRemove = data.subList(1, data.size());
				return true;
			}
			case ST_CLOUD:
				final ActiveSkill cskill = skill;
				engine.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						setCloud(cskill.getData());
					}
				});

				return false;
		case ST_SUPER_DARK:
			setSuperDark(skill.getData(), callback);
			return false;
		case ST_TARGET_RANDOM:
			if (mCurrentState != GameState.GAME_END) {
				int[][] board = new int[PadBoardAI.ROWS][PadBoardAI.COLS];
				PadBoardAI.copy_board(gameBoard, board);
				// mStack.push(gameBoard);
				List<Integer> sd = skill.getData();
				int size = sd.get(0);
				int targetSize = sd.get(size + 1);
				Set<Integer> set = new HashSet<Integer>();
				for (int i = 0; i < size; ++i) {
					set.add(sd.get(i + 1));
				}
				setChangeColorBoard(PadBoardAI.randomChangedBoard(board, set,
						sd.subList(size + 2, size + 2 + targetSize)));
			}
			break;
		case ST_ADD_PLUS: {
			List<Integer> addPlus = new ArrayList<Integer>();

			for (Integer d : skill.getData()) {
				addPlus.add(d);
				addPlus.add(d + 10);
			}
			setChangeColorBoard(
					PadBoardAI.colorChangedBoard(gameBoard, addPlus), callback,
					false);
			return true;
		}

		case ST_CLEAR_BOARD: {
			resetCombo();
			setBoardAlpha(1.0f, 0.0f, new ICastCallback() {

				@Override
				public void onCastFinish(boolean casted) {
					generateAllBoard();
				}
			});
			return true;
		}
			case ST_UNLOCK:
			{
				if (mCurrentState != GameState.GAME_END) {
					List<Integer> rmLock = new ArrayList<Integer>();

					for(int i=20; i<30; ++i) {
						rmLock.add(i);
						rmLock.add(i-20);
						rmLock.add(i+10);
						rmLock.add(i-10);
					}

					int[][] board = new int[PadBoardAI.ROWS][PadBoardAI.COLS];
					PadBoardAI.copy_board(gameBoard, board);

					setChangeColorBoard(PadBoardAI
							.colorChangedBoard(board, rmLock), callback);
				}
				return true;
			}
		case ST_ADD_LOCK:
			if (mCurrentState != GameState.GAME_END) {
				List<Integer> addPlus = new ArrayList<Integer>();

				for (Integer d : skill.getData()) {
					addPlus.add(d);
					addPlus.add(d + 20);
					addPlus.add(d + 10);
					addPlus.add(d + 10 + 20);
				}

				int[][] board = new int[PadBoardAI.ROWS][PadBoardAI.COLS];
				PadBoardAI.copy_board(gameBoard, board);
				// mStack.push(gameBoard);

				setChangeColorBoard(
						PadBoardAI.colorChangedBoard(board, addPlus), callback,
						byEnemy);
			}
			return true;
		case ST_CHANGE_THE_WORLD:
			if (mChangeTheWorld.get()) {
				return true;
			}
			int theWorldTime = skill.getData().get(0) * 1000;
			changeTheWorld(theWorldTime);
			return true;
		case ST_LINE_RANDOM_CHANGE: {
			List<Integer> data = skill.getData();
			int colorCount = data.get(0);
			int lineCount = data.get(colorCount + 1);
			List<Integer> colors = new ArrayList<Integer>(colorCount);
			for (int i = 1; i <= colorCount; ++i) {
				colors.add(data.get(i));
			}

			int[][] board = PadBoardAI.copy_board(gameBoard);
			for (int i = 1; i <= lineCount; ++i) {
				int line = data.get(colorCount + 1 + i);
				if (line < 10) {
					for (int x = 0; x < PadBoardAI.ROWS; ++x) {
						int rand = RandomUtil.getInt(colorCount);
						board[x][line] = colors.get(rand);
					}
				} else {
					int pos = line % 10;
					for (int y = 0; y < PadBoardAI.COLS; ++y) {
						int rand = RandomUtil.getInt(colorCount);
						board[pos][y] = colors.get(rand);
					}
				}
			}
			setChangeColorBoard(board, callback, byEnemy);
			return true;
		}
		case ST_COLOR_CHANGE: {
			List<Integer> data = new ArrayList<Integer>(skill.getData());
			int size = data.size();
			for (int i = 0; i < size; i += 2) {
				int orb = data.get(i);
				if (orb == -1) {
					orb = RandomUtil.getColor(6);
				}

				int toOrb = data.get(i + 1);
				if (orb <= 5) {
					data.add(orb + 10);
					if (toOrb <= 5) {
						data.add(toOrb + 10);
					} else {
						data.add(toOrb);
					}
				}
			}
			// setChangeColorBoard(PadBoardAI.colorChangedBoard(gameBoard,
			// data), callback);
			setChangeColorBoard(PadBoardAI.colorChangedBoard(gameBoard, data),
					data, callback, byEnemy);
			return true;
		}
		case ST_RANDOM_CHANGE_RESTRICT_MORE: {
			List<Integer> data = skill.getData();
			int size = data.size();

			int[][] board = new int[PadBoardAI.ROWS][PadBoardAI.COLS];
			PadBoardAI.copy_board(gameBoard, board);
			// mStack.push(gameBoard);

			List<Integer> colorList = new ArrayList<Integer>();
			List<Integer> countList = new ArrayList<Integer>();
			List<Integer> exceptList = new ArrayList<Integer>();
			int a;
			for (a = 0; a < size; a += 4) {
				colorList.add(data.get(a));
				countList.add(data.get(a + 1));
				exceptList.add(data.get(a + 2));
				if(!exceptList.contains(data.get(a+3))) {
					exceptList.add(data.get(a + 3));
				}

			}


			HashSet<RowCol> rcList = new HashSet<RowCol>();
			for (int i = 0; i < PadBoardAI.ROWS; ++i) {
				for (int j = 0; j < PadBoardAI.COLS; ++j) {
					boolean find = false;
					for (int k = 0; k < colorList.size(); ++k) {
						int color = colorList.get(k);
						for(int h=0; h<exceptList.size(); ++h) {
							int except = exceptList.get(h);
							if (((board[i][j] % 10) == color
									|| (board[i][j] % 10) == except)) {
								find = true;
								break;
							}
						}
					}
					if (!find) {
						rcList.add(RowCol.make(i, j));
					}
				}
			}

			int listSize = rcList.size();
			if (listSize > 0) {
				List<RowCol> list = new ArrayList<RowCol>(rcList);
				Collections.shuffle(list);

				int csize = colorList.size();
				int counter = 0;
				for (int i = 0; i < csize; ++i) {
					int color = colorList.get(i);
					int ccsize = countList.get(i);
					for (int j = 0; j < ccsize; ++j) {
						if (counter >= listSize) {
							break;
						}
						RowCol rc = list.get(counter);
						int addition = (gameBoard[rc.row][rc.col] >= 10 && gameBoard[rc.row][rc.col] != PadBoardAI.X_ORBS) ? 10
								: 0;
						int cv = color;
						if (cv <= 5) {
							cv += addition;
						}
						board[rc.row][rc.col] = cv;
						++counter;
					}
				}

				setChangeColorBoard(board, callback, byEnemy);
			} else {
				callback.onCastFinish(false);
			}
			return true;
		}
		case ST_RANDOM_CHANGE_RESTRICT: {
			List<Integer> data = skill.getData();
			int size = data.size();

			int[][] board = new int[PadBoardAI.ROWS][PadBoardAI.COLS];
			PadBoardAI.copy_board(gameBoard, board);
			// mStack.push(gameBoard);

			List<Integer> colorList = new ArrayList<Integer>();
			List<Integer> countList = new ArrayList<Integer>();
			List<Integer> exceptList = new ArrayList<Integer>();
			int a = 0;
			for (a = 0; a < size; a += 3) {
				colorList.add(data.get(a));
				countList.add(data.get(a + 1));
				exceptList.add(data.get(a + 2));
			}
			if ((data.size()%3) > 0) {
				exceptList.add(data.get(data.size()-1));
			}

			List<RowCol> rcList = new ArrayList<RowCol>();
			for (int i = 0; i < PadBoardAI.ROWS; ++i) {
				for (int j = 0; j < PadBoardAI.COLS; ++j) {
					boolean find = false;
					for (int k = 0; k < colorList.size(); ++k) {
						int color = colorList.get(k);
						int except = exceptList.get(k);
						if ((board[i][j] % 10) == color
								|| (board[i][j] % 10) == except) {
							find = true;
							break;
						}
					}
					if (!find) {
						rcList.add(RowCol.make(i, j));
					}
				}
			}

			int listSize = rcList.size();
			if (listSize >= 0) {
				Collections.shuffle(rcList);

				int csize = colorList.size();
				int counter = 0;
				for (int i = 0; i < csize; ++i) {
					int color = colorList.get(i);
					int ccsize = countList.get(i);
					for (int j = 0; j < ccsize; ++j) {
						if (counter >= listSize) {
							break;
						}
						RowCol rc = rcList.get(counter);
						int addition = (gameBoard[rc.row][rc.col] >= 10 && gameBoard[rc.row][rc.col] != PadBoardAI.X_ORBS) ? 10
								: 0;
						int cv = color;
						if (cv <= 5) {
							cv += addition;
						}
						board[rc.row][rc.col] = cv;
						++counter;
					}
				}

				setChangeColorBoard(board, callback, byEnemy);
			} else {
				callback.onCastFinish(false);
			}
			return true;
		}
		case ST_RANDOM_CHANGE_FIX:
		case ST_RANDOM_CHANGE: {
			int[][] board = new int[PadBoardAI.ROWS][PadBoardAI.COLS];
			PadBoardAI.copy_board(gameBoard, board);
			// mStack.push(gameBoard);

			List<Integer> data = skill.getData();
			int size = data.size();

			List<Integer> colorList = new ArrayList<Integer>();
			List<Integer> countList = new ArrayList<Integer>();
			for (int i = 0; i < size; i += 2) {
				colorList.add(data.get(i));
				countList.add(data.get(i + 1));
			}

			List<RowCol> rcList = new ArrayList<RowCol>();
			for (int i = 0; i < PadBoardAI.ROWS; ++i) {
				for (int j = 0; j < PadBoardAI.COLS; ++j) {
					boolean find = false;
					if(type != ST_RANDOM_CHANGE_FIX) {
						for (int k = 0; k < colorList.size(); ++k) {
							int color = colorList.get(k);
							if ((board[i][j] % 10) == color) {
								find = true;
								break;
							}
						}
					}
					if (!find) {
						rcList.add(RowCol.make(i, j));
					}
				}
			}

			int listSize = rcList.size();
			if (listSize >= 0) {
				Collections.shuffle(rcList);

				int csize = colorList.size();
				int counter = 0;
				for (int i = 0; i < csize; ++i) {
					int color = colorList.get(i);
					int ccsize = countList.get(i);
					for (int j = 0; j < ccsize; ++j) {
						if (counter >= listSize) {
							break;
						}
						RowCol rc = rcList.get(counter);
						int addition = (gameBoard[rc.row][rc.col] >= 10 && gameBoard[rc.row][rc.col] != PadBoardAI.X_ORBS) ? 10
								: 0;
						int cv = color;
						if (cv <= 5) {
							cv += addition;
						}
						board[rc.row][rc.col] = cv;
						++counter;
					}
				}

				setChangeColorBoard(board, callback, byEnemy);
			} else {
				callback.onCastFinish(false);
			}
			return true;
		}
			case ST_POSITION_CHANGE: {
				int[][] board = new int[PadBoardAI.ROWS][PadBoardAI.COLS];
				PadBoardAI.copy_board(gameBoard, board);
				// mStack.push(gameBoard);

				List<Integer> data = skill.getData();
				int size = data.size();
				int orb = data.get(0);
				int pos_type = data.get(1);
				if (pos_type == 0) {
					for(int i=2; i<size; i+=2) {
						int position = data.get(i);
						int offset = data.get(i+1);
						int x = 0, y = 0;

						if(position == 0) { // left top
							y = 0;
							x = offset;
						} else if (position == 1) { // right top
							y = 0;
							x = PadBoardAI.ROWS - 1 - offset;
						} else if (position == 2) { // left bottom
							y = PadBoardAI.COLS - 1;
							x = offset;
						} else if (position == 3) { // right bottom
							y = PadBoardAI.COLS - 1;
							x = PadBoardAI.ROWS - 1 - offset;
						} else if (position == 5) { // right center
							y = (PadBoardAI.COLS-1) / 2;
							x = PadBoardAI.ROWS - 1 - offset;
						} else if (position == 4) { // left center
							y = (PadBoardAI.COLS-1) / 2;
							x = offset;
						} else if (position == 6) { // left top + 1
							y = 1;
							x = offset;
						} else if (position == 8) { // left bottom - 1
							y = PadBoardAI.COLS - 2;
							x = offset;
						}
						board[x][y] = orb;
					}

				} else {
					for(int i=2; i<size; i+=2) {
						int x = data.get(i);
						int y = data.get(i+1);
						board[x][y] = orb;
					}
				}
				setChangeColorBoard(board, callback, byEnemy);
				return false;
			}
		case ST_TRANSFORM:
			// save current board
			int[][] newboard = PadBoardAI.generateBoardWith3Up(skill.getData());
			for (int i = 0; i < PadBoardAI.ROWS; ++i) {
				for (int j = 0; j < PadBoardAI.COLS; ++j) {
					int orb = gameBoard[i][j];
					// if(orb>=20) {
					// newboard[i][j] = gameBoard[i][j];
					// } else
					if (orb >= 10 && orb < 20 && orb != PadBoardAI.X_ORBS
							&& newboard[i][j] <= 5) {
						newboard[i][j] += 10;
					}
				}
			}
			setGameBoard(newboard, callback, byEnemy);
			return true;
		case ST_ONE_LINE_TRANSFORM:
			// mStack.push(gameBoard);
			setChangeLineBoard(skill, callback, byEnemy);
			return true;
		case ST_DARK_SCREEN:
			setDarkScreen(callback);
			return true;
		case ST_L_FORMAT: {
			int[][] nb = PadBoardAI.copy_board(gameBoard);
			int size = skill.getData().size();
			List<Integer> data = skill.getData();
			for(int i=0; i<size; i+=2) {
				int color = data.get(i);
				int pos = data.get(i + 1);

				int r, c;
				if(pos == 0) {
					r = 0;
					c = 0;
				} else if (pos == 1) {
					r = PadBoardAI.ROWS-1;
					c = 0;
				} else if (pos == 2) {
					r = 0;
					c = PadBoardAI.COLS-1;
				} else {
					r = PadBoardAI.ROWS-1;
					c = PadBoardAI.COLS-1;
				}
				nb[r][c] = (nb[r][c] - (nb[r][c] % 10)) + color;
				if (r + 2 < PadBoardAI.ROWS && c - 2 >= 0) {
					// |
					// |____
					nb[r + 1][c] = (nb[r + 1][c] - (nb[r + 1][c] % 10)) + color;
					nb[r + 2][c] = (nb[r + 2][c] - (nb[r + 2][c] % 10)) + color;
					nb[r][c - 1] = (nb[r][c - 1] - (nb[r][c - 1] % 10)) + color;
					nb[r][c - 2] = (nb[r][c - 2] - (nb[r][c - 2] % 10)) + color;
				} else if (r + 2 < PadBoardAI.ROWS && c + 2 < PadBoardAI.COLS) {
					// ____
					// |
					// |
					nb[r + 1][c] = (nb[r + 1][c] - (nb[r + 1][c] % 10)) + color;
					nb[r + 2][c] = (nb[r + 2][c] - (nb[r + 2][c] % 10)) + color;
					nb[r][c + 1] = (nb[r][c + 1] - (nb[r][c + 1] % 10)) + color;
					nb[r][c + 2] = (nb[r][c + 2] - (nb[r][c + 2] % 10)) + color;
				} else if (r - 2 >= 0 && c - 2 >= 0) {
					//     |
					// ____|

					nb[r - 1][c] = (nb[r - 1][c] - (nb[r - 1][c] % 10)) + color;
					nb[r - 2][c] = (nb[r - 2][c] - (nb[r - 2][c] % 10)) + color;
					nb[r][c - 1] = (nb[r][c - 1] - (nb[r][c - 1] % 10)) + color;
					nb[r][c - 2] = (nb[r][c - 2] - (nb[r][c - 2] % 10)) + color;
				} else {
					// ____
					//     |
					//     |

					nb[r - 1][c] = (nb[r - 1][c] - (nb[r - 1][c] % 10)) + color;
					nb[r - 2][c] = (nb[r - 2][c] - (nb[r - 2][c] % 10)) + color;
					nb[r][c + 1] = (nb[r][c + 1] - (nb[r][c + 1] % 10)) + color;
					nb[r][c + 2] = (nb[r][c + 2] - (nb[r][c + 2] % 10)) + color;
				}
			}
			setGameBoard(nb, callback, byEnemy);
			return true;
		}
			case ST_SQUARE_FORMAT: {
				int[][] nb = PadBoardAI.copy_board(gameBoard);

				List<Integer> data = skill.getData();
				int size = data.size();

				for(int i=0; i<size; i+=2) {
					int color = data.get(i);
					int pos = data.get(i + 1);

					int r, c;
					if (pos == 0) {
						r = 1;
						c = 1;
					} else if (pos == 1) {
						r = PadBoardAI.ROWS - 2;
						c = 1;
					} else if (pos == 2) {
						r = 1;
						c = PadBoardAI.COLS - 2;
					} else if (pos == 3) {
						r = PadBoardAI.ROWS - 2;
						c = PadBoardAI.COLS - 2;
					} else {
						r = 1;
						c = 2;
					}

					nb[r][c] = nb[r][c] - (nb[r][c] % 10) + color;
					nb[r + 1][c] = nb[r + 1][c] - (nb[r + 1][c] % 10) + color;
					nb[r + 1][c - 1] = nb[r + 1][c - 1] - (nb[r + 1][c - 1] % 10) + color;
					nb[r - 1][c] = nb[r - 1][c] - (nb[r - 1][c] % 10) + color;
					nb[r - 1][c + 1] = nb[r - 1][c + 1] - (nb[r - 1][c + 1] % 10) + color;
					nb[r][c + 1] = nb[r][c + 1] - (nb[r][c + 1] % 10) + color;
					nb[r + 1][c + 1] = nb[r + 1][c + 1] - (nb[r + 1][c + 1] % 10) + color;
					nb[r][c - 1] = nb[r][c - 1] - (nb[r][c - 1] % 10) + color;
					nb[r - 1][c - 1] = nb[r - 1][c - 1] - (nb[r - 1][c - 1] % 10) + color;
				}

            setGameBoard(nb, callback, byEnemy);
            return true;
        }
			case ST_CROSS_FORMAT: {
				int[][] nb = PadBoardAI.copy_board(gameBoard);

				List<Integer> data = skill.getData();
				int size = data.size();

				for(int i=0; i<size; i+=2) {
					int color = data.get(i);
					int pos = data.get(i + 1);

					int r, c;
					if (pos == 0) {
						r = 1;
						c = 1;
					} else if (pos == 1) {
						r = PadBoardAI.ROWS - 2;
						c = 1;
					} else if (pos == 2) {
						r = 1;
						c = PadBoardAI.COLS - 2;
					} else {
						r = PadBoardAI.ROWS - 2;
						c = PadBoardAI.COLS - 2;
					}

					nb[r][c] = nb[r][c] - (nb[r][c] % 10) + color;
					nb[r + 1][c] = nb[r + 1][c] - (nb[r + 1][c] % 10) + color;
					nb[r - 1][c] = nb[r - 1][c] - (nb[r - 1][c] % 10) + color;
					nb[r][c + 1] = nb[r][c + 1] - (nb[r][c + 1] % 10) + color;
					nb[r][c - 1] = nb[r][c - 1] - (nb[r][c - 1] % 10) + color;
				}

            setGameBoard(nb, callback, byEnemy);
            return true;
        }
		case ST_NO_DROP: {
			mNoDropSkill = skill;
			return false;
		}
		case ST_ENHANCE_ORB: {
			mDropEnhanceSkill = skill;
			return false;
		}
		case ST_DROP_RATE: {
			boolean isNegative = skill.getData().get(1) >= 6;
			if (isNegative) {
				mNegativeDrop = skill;
			} else {
				mDropRateSkill = skill;
			}
			return false;
		}
			case ST_DROP_ONLY: {
				mDropOnlySkill = skill;
				return false;
			}
        case ST_ADD_COMBO: {
        	mAddComboSkill = skill;
        	return false;
        }
		case ST_DROP_LOCK:
			mDropLock = skill;
			return false;
		case ST_CHANGE_SELF_ATTR:
		case ST_TIME_EXTEND:
		case ST_GRAVITY:
		case ST_NOT_SUPPORT:
		case ST_REDUCE_DEF:
		case ST_POWER_UP:
		case ST_TYPE_UP:
		default:
			break;

		}
		return false;
	}

	final int[] texture = { SimulateAssets.S_DARK_ID, SimulateAssets.S_FIRE_ID,
			SimulateAssets.S_LIGHT_ID, SimulateAssets.S_HEART_ID,
			SimulateAssets.S_WATER_ID, SimulateAssets.S_WOOD_ID };

	public void clearBoard(final ICastCallback callback) {
		changeState(GameState.GAME_ANIMATION);

		int[][] newBoard = PadBoardAI.generateBoard(mDropType, false);// generateBoard(mDropType,
																		// false);

		ArrayList<Pair<RowCol, RowCol>> dropList = new ArrayList<Pair<RowCol, RowCol>>();
		for (int row = 0; row < PadBoardAI.ROWS; ++row) {
			for (int k = 4; k >= 0; --k) {
				Sprite s = sprites[row][k];
				if (!s.isDisposed()) {
					addToDetachList(s);
				}

				int neworb = newBoard[row][k];
				if (neworb <= 5
						&& RandomUtil
								.getLuck(mPlusOrbCount[currentTeam][neworb] * 20)) {
					neworb += 10;
				}

				gameBoard[row][k] = neworb;
				dropList.add(new Pair<RowCol, RowCol>(RowCol.make(row, k),
						RowCol.make(row, k - 5)));
			}
		}
		for (Pair<RowCol, RowCol> pair : dropList) {
			final RowCol target = pair.first;
			final RowCol orb = pair.second;
			int fromY = orb.col * SimulatorConstants.ORB_SIZE
					+ SimulatorConstants.OFFSET_Y;
			int toY = target.col * SimulatorConstants.ORB_SIZE
					+ SimulatorConstants.OFFSET_Y;

			if (PadBoardAI.isValid(orb.row, orb.col)) {
				Sprite temp = sprites[orb.row][orb.col];
				sprites[orb.row][orb.col] = sprites[target.row][target.col];
				sprites[target.row][target.col] = temp;
			} else { // drop from sky
				Sprite newSprite = getSprite(gameBoard[target.row][target.col]);
				newSprite.setPosition(target.row * SimulatorConstants.ORB_SIZE
						+ SimulatorConstants.OFFSET_X, fromY);
				newSprite.setZIndex(3);
				sprites[target.row][target.col] = newSprite;
				attach(newSprite);
			}

			sprites[target.row][target.col]
					.registerEntityModifier(new MoveYModifier(
							SimulatorConstants.SECOND_DROP, fromY, toY) {
						protected void onModifierFinished(IEntity pItem) {
							pItem.setZIndex(0);
						};
					});
		}
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				callback.onCastFinish(true);
			}
		}, (long) (1000 * (SimulatorConstants.SECOND_DROP + 0.05f)));
	}

	private void generateAllBoard() {
		changeState(GameState.GAME_ANIMATION);

		ArrayList<Pair<RowCol, RowCol>> dropList = new ArrayList<Pair<RowCol, RowCol>>();
		for (int row = 0; row < PadBoardAI.ROWS; ++row) {
			for (int k = 4; k >= 0; --k) {
				Sprite s = sprites[row][k];
				if (!s.isDisposed()) {
					addToDetachList(s);
				}

				int neworb = PadBoardAI.getNewOrbRestrictChances(mDropType,
						null);
				if (neworb <= 5
						&& RandomUtil
								.getLuck(mPlusOrbCount[currentTeam][neworb] * 20)) {
					neworb += 10;
				}

				gameBoard[row][k] = neworb;
				dropList.add(new Pair<RowCol, RowCol>(RowCol.make(row, k),
						RowCol.make(row, k - 5)));
			}
		}
		for (Pair<RowCol, RowCol> pair : dropList) {
			final RowCol target = pair.first;
			final RowCol orb = pair.second;
			int fromY = orb.col * SimulatorConstants.ORB_SIZE
					+ SimulatorConstants.OFFSET_Y;
			int toY = target.col * SimulatorConstants.ORB_SIZE
					+ SimulatorConstants.OFFSET_Y;

			if (PadBoardAI.isValid(orb.row, orb.col)) {
				Sprite temp = sprites[orb.row][orb.col];
				sprites[orb.row][orb.col] = sprites[target.row][target.col];
				sprites[target.row][target.col] = temp;
			} else { // drop from sky
				Sprite newSprite = getSprite(gameBoard[target.row][target.col]);
				newSprite.setPosition(target.row * SimulatorConstants.ORB_SIZE
						+ SimulatorConstants.OFFSET_X, fromY);
				newSprite.setZIndex(3);
				sprites[target.row][target.col] = newSprite;
				attach(newSprite);
			}

			sprites[target.row][target.col]
					.registerEntityModifier(new MoveYModifier(
							SimulatorConstants.SECOND_DROP, fromY, toY) {
						protected void onModifierFinished(IEntity pItem) {
							pItem.setZIndex(0);
						};
					});
		}

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				findMatches();
			}
		}, (long) (1000 * (SimulatorConstants.SECOND_DROP + 0.05f)));
	}

	private void createChangeSelfAttrSprite(int index, ActiveSkill skill) {
		// final int[] ATTR = {
		// SimulateAssets.S_DARK_ID,SimulateAssets.S_FIRE_ID,
		// 0,SimulateAssets.S_LIGHT_ID,
		// SimulateAssets.S_WATER_ID,SimulateAssets.S_WOOD_ID
		// };
		// if (mAttrSprite[index] != null) {
		// mAttrSprite[index].detach(this);
		// }
		// // List<Integer> data = skill.getData();
		// // int attr = data.get(1);
		// int turn = 2;
		// int attr = 1;
		//
		// ResourceManager mgr = ResourceManager.getInstance();
		// TextSprite ts = new TextSprite(String.valueOf(turn),
		// mgr.getFontSmall(),
		// mgr.getTextureRegion(SimulateAssets.class,ATTR[attr]), vbom);
		// float x = mSkill[index].getX() + mSkill[index].getWidthScaled() / 2;
		// float y = mSkill[index].getY() + mSkill[index].getHeightScaled() / 2;
		// ts.setPosition(x - ts.getWidth() / 2, y);
		// ts.setZIndex(5);
		// ts.attach(this);
		// mAttrSprite[index] = ts;
	}

	final int[] monsterTypeId = { SimulateAssets.MONSTER_TYPE_0_ID,
			SimulateAssets.MONSTER_TYPE_1_ID, SimulateAssets.MONSTER_TYPE_2_ID,
			SimulateAssets.MONSTER_TYPE_3_ID, SimulateAssets.MONSTER_TYPE_4_ID,
			SimulateAssets.MONSTER_TYPE_5_ID, SimulateAssets.MONSTER_TYPE_6_ID,
			SimulateAssets.MONSTER_TYPE_7_ID, SimulateAssets.MONSTER_TYPE_8_ID,
			SimulateAssets.MONSTER_TYPE_9_ID, 0,
			SimulateAssets.MONSTER_TYPE_11_ID };

	private void changeTheWorld(int time) {
		TIME_CHANGE_THE_WORLD = time;
		changeState(GameState.GAME_PLAYER_TURN);
		mChangeTheWorld.set(true);
		mCountDown.set(true);
		stillMyTurn = false;
		mTimeRemain = time;

		displayProgress(true);

		mCTWTimer.reset();
		registerUpdateHandler(mCTWTimer);
	}

	public void calcNullAwoken(boolean nullAwoken, TeamInfo[] team) {
		initMonsterData(nullAwoken, team);
	}

	private void initMonsterData(boolean nullAwoken, TeamInfo[] team) {
		if(team != null) {
			mTeam = team;
		}
		poisonDropCount = jammerDropCount = 0;
		for (int j = 0; j < 2; ++j) {
			if (mDropTime[j] != 0) {
				mDropTime[j] = Constants.DEFAULT_SECOND;
				if (!nullAwoken) {
					for (int i = 0; i < 6; ++i) {
						MonsterInfo info = mTeam[j].getMember(i);
						if (info != null) {
							int handCount = info
									.getTargetAwokenCount(AwokenSkill.EXTEND_TIME, true);
							mDropTime[j] += handCount * 500;
							mDropTime[j] += info.getTargetAwokenCount(AwokenSkill.EXTEND_TIME_PLUS, true) * 1000;

							int potentialCount = info
									.getTargetPotentialAwokenCount(MoneyAwokenSkill.EXTEND_TIME);
							mDropTime[j] += potentialCount * 50;
							poisonDropCount += info.getTargetAwokenCount(AwokenSkill.BLESSING_OF_POISON_DROP, true);
							jammerDropCount += info.getTargetAwokenCount(AwokenSkill.BLESSING_OF_JAMMER_DROP, true);
						}
					}
				}
			}
			AwokenSkill[] awokenPlus = { AwokenSkill.ENHANCE_DARK,
					AwokenSkill.ENHANCE_FIRE, AwokenSkill.ENHANCE_HEART,
					AwokenSkill.ENHANCE_LIGHT, AwokenSkill.ENHANCE_WATER,
					AwokenSkill.ENHANCE_WOOD };
			for (int i = 0; i < 6; ++i) {
				mPlusOrbCount[j][i] = 0;
			}
			if (!nullAwoken) {
				for (int i = 0; i < 6; ++i) {
					MonsterInfo info = mTeam[j].getMember(i);
					if (info != null) {
						for (int k = 0; k < 6; ++k) {
							mPlusOrbCount[j][k] += info
									.getTargetAwokenCount(awokenPlus[k], true);
						}
					}
				}
			}
			for (int i = 0; i < 6; i += 5) {
				MonsterInfo info = mTeam[j].getMember(i);
				if (info != null) {
					for (LeaderSkill skill : info.getLeaderSkill()) {
						if (skill.getType() == LeaderSkillType.LST_EXTEND_TIME) {
							mDropTime[j] += skill.getData().get(0);
						} else if(skill.getType() == LeaderSkillType.LST_TIME_FIXED) {
							mTimeFixed = skill.getData().get(0);
						}
					}
				}
			}
		}
		if(mTimeFixed > 0) {
			mDropTime[0] = mTimeFixed;
			mDropTime[1] = mTimeFixed;
		}

		LogUtil.e("Recalc drop time = ", mDropTime[0]);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		synchronized (mRemoveList) {
			int size = mRemoveList.size();
			if (size >= RELEASE_THRESHOLD) {
				detach(Collections.unmodifiableList(mRemoveList));
				mRemoveList.clear();
			}
		}

		super.onManagedUpdate(pSecondsElapsed);
	}

	@Override
	public void onResume() {
		super.onResume();
		ResourceManager.getInstance().playMusic();
	}

	@Override
	public void onPause() {
		super.onPause();
		ResourceManager.getInstance().pauseMusic();
	}

	Class<?> orbClz = null;

	@Override
	public void createScene() {
		loadSharedPreference();
		init();

		// check monster's skill with hand ( +0.5s )
		initMonsterData(mNullAwoken, null);

		HandlerThread ht = new HandlerThread("ht");
		ht.start();
		handler = new NonUiHandler(ht.getLooper());

		ResourceManager res = ResourceManager.getInstance();
		res.loadGameScene(true);

		res.loadMusic();
		res.playMusic();

		mBackground = new Sprite(0, 0, res.getTextureRegion("background.png"),
				vbom);
		mBackground.setScaleCenter(0f, 0f);
		mBackground
				.setScale(GameActivity.SCREEN_WIDTH / mBackground.getWidth());
		attachChild(mBackground);

		mResMgr = res;

		mFactorText.clear();
		mComboText.clear();
		for (int i = 1; i <= 10; ++i) {
			Text text = new Text(0, 0, res.getFontStroke(), String.format(
					"Combo %d", i), vbom);
			text.setColor(Color.YELLOW);
			text.setZIndex(6);
			mComboText.add(text);
			Text factor = new Text(0, 0, res.getFontStroke(),
					String.format("x10000.00 "), vbom);
			factor.setColor(Color.YELLOW);
			factor.setZIndex(7);
			factor.setVisible(false);
			mFactorText.add(factor);
			attachChild(factor);
		}
		mHeals = new Text(0, 0, res.getFontStroke(), "+0", 12, vbom);
		mHeals.setColor(Color.GREEN);
		mHeals.setZIndex(5);
		mHeals.setScale(1.3f);
		mHeals.setVisible(false);
		attachChild(mHeals);

		mCrossShield = new Sprite(0, 0,
				res.getTextureRegion("cross_shield.png"), vbom);
		mCrossShield.setPosition(3, Constants.OFFSET_Y - 34);
		mCrossShield.setVisible(false);
		mCrossShield.setZIndex(3);
		mCrossShield
				.registerEntityModifier(new LoopEntityModifier(
						new SequenceEntityModifier(new AlphaModifier(
								Constants.SECOND_REFRESH, 1.0f, 0.0f),
								new AlphaModifier(Constants.SECOND_REFRESH,
										0.0f, 1.0f))));
		attachChild(mCrossShield);

		mPoisonText = new Text(0f, 0f, res.getFontStroke(), "-0", 12, vbom);
		mPoisonText.setColor(Color.RED);
		mPoisonText.setVisible(false);
		mPoisonText.setScale(1.3f);
		mPoisonText.setZIndex(3);
		attachChild(mPoisonText);

		SharedPreferenceUtil utils = SharedPreferenceUtil.getInstance(activity);
		IMAGE_TYPE type = (utils.getType() == 2) ? IMAGE_TYPE.TYPE_TOS
				: IMAGE_TYPE.TYPE_PAD;
		if (utils.getBoolean(SharedPreferenceUtil.PREF_COLOR_WEAKNESS)
				&& type == IMAGE_TYPE.TYPE_PAD) {
			type = IMAGE_TYPE.TYPE_PAD_CW;
		}
		switch (type) {
		case TYPE_PAD_CW:
			orbClz = TextureOrbsCw.class;
			break;
		case TYPE_TOS:
			orbClz = TextureOrbsTos.class;
			break;
		case TYPE_PAD:
		default:
			orbClz = TextureOrbs.class;
			break;
		}
		int[] orbList = { TextureOrbs.DARK_ID, TextureOrbs.FIRE_ID,
				TextureOrbs.HEART_ID, TextureOrbs.LIGHT_ID,
				TextureOrbs.WATER_ID, TextureOrbs.WOOD_ID,
				TextureOrbs.POISON_ID, TextureOrbs.JAMMAR_ID,
				TextureOrbs.ENHANCED_POISON_ID,9999 };
		for (int i = 0; i < orbList.length; ++i) {
			textureOrbs.put(i, res.getTextureRegion(orbClz, orbList[i]));
			mCache[i] = new Sprite(0, 0, textureOrbs.get(i), vbom);
			mCache[i].setVisible(false);
			mCache[i].setZIndex(4);
			mCache[i].setScale(1.2f);
			attachChild(mCache[i]);
		}

		boolean index = false;
		ITextureRegion[] textureBoard = new ITextureRegion[2];
		textureBoard[0] = res.getTextureRegion(GameUIAssets.class,
				GameUIAssets.BOARD_A_ID);
		textureBoard[1] = res.getTextureRegion(GameUIAssets.class,
				GameUIAssets.BOARD_B_ID);

		for (int i = 0; i < PadBoardAI.ROWS; ++i) {
			for (int j = 0; j < PadBoardAI.COLS; ++j) {
				int x = SimulatorConstants.ORB_SIZE * i;
				int y = SimulatorConstants.ORB_SIZE * j;
				boardSprite[i][j] = new Sprite(0, 0,
						textureBoard[index ? 0 : 1], vbom);
				boardSprite[i][j].setPosition(x + SimulatorConstants.OFFSET_X,
						y + SimulatorConstants.OFFSET_Y);
				boardSprite[i][j].setZIndex(0);
				attachChild(boardSprite[i][j]);

				sprites[i][j] = getSprite(gameBoard[i][j]);
				setPosition(i, j, x, y);
				sprites[i][j].setZIndex(1);
				attachChild(sprites[i][j]);
				index = !index;
			}
		}

		// progress
		mProgressbar = new Rectangle(SimulatorConstants.OFFSET_X,
				SimulatorConstants.OFFSET_Y - 15 - 8, PROGRESS_BAR_WIDTH, 15,
				vbom);
		mProgressbar.setZIndex(3);
		mProgressbar.setColor(0f, 1f, 0f);
		mProgressbar.setVisible(false);
		attachChild(mProgressbar);

		for (int j = 0; j < 2; ++j) {
			for (int i = 0; i < 6; ++i) {
				Sprite s = new Sprite(0f, 0f, res.getTextureRegion(
						SimulateAssets.class, SimulateAssets.BIND_ID), vbom);
				s.setPosition(SimulatorConstants.OFFSET_X + i * (88 + 1),
						mProgressbar.getY() - 8 - 88);
				s.setVisible(false);
				s.setZIndex(2);
				attachChild(s);
				mBindSprite[j][i] = s;
			}
		}

		// put it last... because we need assets be ready
		mEnvironment = new MultiplePadEnvironment(this);
		mEnvironment.create(ComboMasterApplication.getsInstance().getStage());
		resetCombo();
		sortChildren();

		setOnSceneTouchListener(this);
		setOnMenuItemClickListener(this);

		thandler = new TimerHandler(0.3f, true, new ShineCallback());
		registerUpdateHandler(thandler);

	}

	private boolean isSkipState() {
		return mCurrentState != GameState.GAME_PLAYER_TURN
				|| mChangeTheWorld.get();
	}

	private void setPosition(int ox, int oy, float x, float y) {
		sprites[ox][oy].setPosition(x + SimulatorConstants.OFFSET_X, y
				+ SimulatorConstants.OFFSET_Y);
	}

	private void init() {
		ComboMasterApplication instance = ComboMasterApplication.getsInstance();
		int[][] cache = instance.getBoardCache();
		if (cache == null) {
			gameBoard = PadBoardAI.generateBoardWithPlus(mDropType, false,
					mPlusOrbCount[currentTeam]);
		} else {
			gameBoard = PadBoardAI.copy_board(cache);
		}
		// mStack = new BoardStack(gameBoard);
		mReplay = new GameReplay();

		mTeam = instance.getTarget2Team();
		updateSpecialLST();
		gameMode = instance.getGameMode();
		mNullAwoken = instance.isAwokenNulled();

		mSettings.put("copMode", true);
		mSettings.put("gameMode", gameMode);
		mSettings.put("nullAwoken", mNullAwoken);

		changeState(GameState.GAME_INIT);
	}
	
    @Override
    public void updateSpecialLST() {
    	removeN = getMoreRemove(mTeam);
    	normalDrop = isNormalDrop(mTeam);
    }
    
    private boolean isNormalDrop(TeamInfo[] team) {
        MonsterInfo leader = team[0].getMember(0);
        MonsterInfo friend = team[1].getMember(0);
        
        if(leader != null) {
        	for(LeaderSkill ls : leader.getLeaderSkill()) {
        		if(ls.getType() == LeaderSkillType.LST_NO_DROP) {
        			return false;
        		}
        	}
        }
        if(friend != null) {
        	for(LeaderSkill ls : friend.getLeaderSkill()) {
        		if(ls.getType() == LeaderSkillType.LST_NO_DROP) {
        			return false;
        		}
        	}
        }
        
        return !forceNoDrop;
    }

    private int getMoreRemove(TeamInfo[] team) {
        MonsterInfo leader = team[0].getMember(0);
        MonsterInfo friend = team[1].getMember(0);
        
        if(leader != null) {
        	for(LeaderSkill ls : leader.getLeaderSkill()) {
        		if(ls.getType() == LeaderSkillType.LST_MORE_REMOVE) {
        			return ls.getData().get(0) + 1;
        		}
        	}
        }
        if(friend != null) {
        	for(LeaderSkill ls : friend.getLeaderSkill()) {
        		if(ls.getType() == LeaderSkillType.LST_MORE_REMOVE) {
        			return ls.getData().get(0) + 1;
        		}
        	}
        }
        
        return 3;
    }
	
	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().setScene(GameMenuScene.class);
		// TODO: show dialog to ask
		// super.onBackKeyPressed();
	}

	@Override
	public void disposeScene() {
		mEnvironment.finish();
		ComboMasterApplication.getsInstance().init();
		ComboMasterApplication.getsInstance().init2();
		try {
			mResMgr.unloadGameScene();

			for (Sprite[] ss : sprites) {
				for (Sprite s : ss) {
					if (!s.isDisposed()) {
						s.dispose();
					}
				}
			}

			for (Text text : mComboText) {
				if (!text.isDisposed()) {
					text.dispose();
				}
			}
			for (Text text : mFactorText) {
				if (!text.isDisposed()) {
					text.dispose();
				}
			}
			if (!mHeals.isDisposed()) {
				mHeals.dispose();
			}
			if (!mPoisonText.isDisposed()) {
				mPoisonText.dispose();
			}

			for (Sprite[] ss : boardSprite) {
				for (Sprite s : ss) {
					if (!s.isDisposed()) {
						s.dispose();
					}
				}
			}

			for (int j = 0; j < 2; ++j) {
				for (int i = 0; i < 6; ++i) {
					if (!mBindSprite[j][i].isDisposed()) {
						mBindSprite[j][i].dispose();
					}
				}
			}
		} catch (Exception e) {

		}
		setOnSceneTouchListener(null);
		setOnMenuItemClickListener(null);

		mResMgr.unloadMusic();
		handler.getLooper().quit();

		clearUpdateHandlers();

	}

	int currentX = -1;
	int currentY = -1;

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		switch (mCurrentState) {
		case GAME_PLAYER_TURN:
			if (onGameRun(pScene, pSceneTouchEvent) == false) {
				return super.onSceneTouchEvent(pScene, pSceneTouchEvent);
			}
			break;
		default:
			break;
		}

		return super.onSceneTouchEvent(pScene, pSceneTouchEvent);
	}

	private void displayProgress(boolean display) {
		mProgressbar.setVisible(display);

		// mHp.setVisible(!display);
		// mHpBackground.setVisible(!display);
		// mHpText.setVisible(!display);
	}

	private boolean onGameRun(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (mCurrentState == GameState.GAME_ANIMATION) {
			LogUtil.d("skip scene touch due to state=GAME_ANIMATION");
			return false;
		}
		LogUtil.d("onSceneTouched");
		float px = pSceneTouchEvent.getX();
		float py = pSceneTouchEvent.getY();
		float x = px - SimulatorConstants.OFFSET_X;
		float y = py - SimulatorConstants.OFFSET_Y;
		int indexX = (int) x / SimulatorConstants.ORB_SIZE;
		int indexY = (int) y / SimulatorConstants.ORB_SIZE;
		boolean specialCase = false;
		if (currentX != -1) {
			if (y < 0) {
				indexY = 0;
				specialCase = true;
			} else if (indexY >= PadBoardAI.COLS) {
				indexY = PadBoardAI.COLS - 1;
				specialCase = true;
			}
			if (x < 0) {
				indexX = 0;
				specialCase = true;
			} else if (indexX >= PadBoardAI.ROWS) {
				indexX = PadBoardAI.ROWS - 1;
				specialCase = true;
			}
		}

		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			if (PadBoardAI.isValid(indexX, indexY)) {
				currentX = indexX;
				currentY = indexY;

				orbInHand = getCacheSprite(gameBoard[indexX][indexY] % 10);
				orbInHand.setZIndex(15);
				orbInHand.setPosition(x - 48 + SimulatorConstants.OFFSET_X, y
						- 48 + SimulatorConstants.OFFSET_Y);
				orbInHand.setVisible(true);
				// attachChild(orbInHand);
				sprites[currentX][currentY].setAlpha(0.5f);

				resetCombo();
				untouchableWhenMove();

				if(mChangeTheWorld.get() && !alreadySwap) {
					stillMyTurn = true;
				}

				sortChildren();
			}
		} else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
			if (PadBoardAI.isValid(currentX, currentY) && orbInHand != null) {
				// setPosition(currentX, currentY, x-48, y-48);
				orbInHand.setPosition(x - 48 + SimulatorConstants.OFFSET_X, y
						- 48 + SimulatorConstants.OFFSET_Y);
			}
			// check orb is swap or not
			if (PadBoardAI.isValid(currentX, currentY)
					&& PadBoardAI.isValid(indexX, indexY)
					&& (indexX != currentX || indexY != currentY)) {
				// if position more than 2, skip it...
				 if (Math.abs(indexX - currentX) >= 2 || Math.abs(indexY - currentY) >= 2) {
	                	// find closest and swap it
	                	int oriX = indexX, oriY = indexY;
	                	
	                	int tempX = indexX - currentX;
	                	int tempY = indexY - currentY;
	                	if(tempX != 0) {
	                		oriX = currentX + (tempX / Math.abs(tempX));
	                	}
	                	if(tempY != 0) {
	                		oriY = currentY + (tempY / Math.abs(tempY));
	                	}
	                	
	                	if(PadBoardAI.isValid(oriX, oriY)) {
	                		indexX = oriX;
	                		indexY = oriY;
	                	} else if(PadBoardAI.isValid(oriX, currentY)) {
	                		indexX = oriX;
	                	} else if(PadBoardAI.isValid(currentX, oriY)) {
	                		indexY = oriY;
	                	} else {
	                		LogUtil.e("Error swap, but still process..");
	                		//return true;
	                	}
				 }
				Sprite s = sprites[indexX][indexY];
				if (!specialCase && !s.contains(px, py)) {
					return true;
				}
				if(mChangeTheWorld.get()) {
					alreadySwap = true;
					stillMyTurn = false;
				}

				// if not start... start time calculating
				if (!mCountDown.get()) {
					playerAttacked = true;
					
					mCountDown.set(true);
					if(mTimeFixed == 0) {
						mTimeRemain = mDropTime[currentTeam] + mAdjustTime;
						if (mAdjustTimeX != 0) {
							mTimeRemain = (int) (mTimeRemain * mAdjustTimeX / 1000f);
						}
					} else {
						mTimeRemain = mDropTime[currentTeam];
					}
					LogUtil.e("Move time = ", mTimeRemain);
					// PadBoardAI.copy_board(gameBoard, previousBoard);
					// mStack.push(gameBoard);

					mReplay.start(gameBoard);
					mReplay.add(RowCol.make(currentX, currentY));

					if (mDropTime[currentTeam] != 0) { // 0 -> inf mode
						mTimer.reset();
						displayProgress(true);
						registerUpdateHandler(mTimer);
					}
				}
				mReplay.add(RowCol.make(indexX, indexY));
				swapOrb(indexX, indexY, currentX, currentY);
				currentX = indexX;
				currentY = indexY;
			}
		} else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			if (mChangeTheWorld.get() == false) {
				if (mCountDown.get() && PadBoardAI.isValid(currentX, currentY)) {
					onTimeUp();
				} else {
					onTouchUpReset();
				}
			} else {
				onTouchUpWhenCTW();
			}
		}
		return true;
	}

	private void untouchableWhenMove() {
		if (mSkillTouchable.get()) {
			mSkillTouchable.set(false);

			mEnvironment.unregisterPlayerTouchEvent();
			setOnMenuItemClickListener(null);
		}
	}

	private void setSkillTouchable() {
		if (!mSkillTouchable.get()) {
			mSkillTouchable.set(true);

			mEnvironment.registerPlayerTouchEvent();
			setOnMenuItemClickListener(this);
		}
	}

	private void resetCombo() {
		// start calculating from zero
		mCombo.set(0);
		mScoreBoard.clear();
	}

	// TODO: check thread
	private void onTouchUpWhenCTW() {
		if (orbInHand != null) {
			// detach(orbInHand);
			orbInHand.setVisible(false);
			orbInHand = null;
		}
		if (PadBoardAI.isValid(currentX, currentY)) {
			sprites[currentX][currentY].setAlpha(1.0f);
		}
		stillMyTurn = false;
		currentX = -1;
		currentY = -1;
	}

	// TODO: check thread
	private void onTouchUpReset() {
		LogUtil.d("onTouchUpReset()");
		if (orbInHand != null) {
			// detach(orbInHand);
			orbInHand.setVisible(false);
			orbInHand = null;
		}

		if (PadBoardAI.isValid(currentX, currentY)) {
			sprites[currentX][currentY].setAlpha(1.0f);
		}
		currentX = -1;
		currentY = -1;

		displayProgress(false);
		mChangeTheWorld.set(false);
		stillMyTurn = false;
		alreadySwap = false;
		setSkillTouchable();

		if (mCountDown.get() == false) {
			LogUtil.d("run twice @ reset?");
			return;
		}
		mCountDown.set(false);

	}

	private void onTimeUp() {
		LogUtil.d("onTimeUp()");
		if (!mCountDown.get()) {
			// already reset
			LogUtil.e("cannot run twice");
			return;
		}
		boolean myTurn = stillMyTurn;
		if(!(mChangeTheWorld.get() && myTurn)) {
			changeState(GameState.GAME_ANIMATION);
		}


		if (mChangeTheWorld.get()) {
			unregisterUpdateHandler(mCTWTimer);
		} else {
			unregisterUpdateHandler(mTimer);
		}
		onTouchUpReset();

		mProgressbar.setWidth(PROGRESS_BAR_WIDTH);
		mProgressbar.setColor(0f, 1f, 0f);

		if(!myTurn) {
			findMatches();
		}
	}



	private MatchBoard hasBomb() {
		MatchBoard matchBoard = PadBoardAI.findMatches(gameBoard, removeN, cantRemove);
		for(int i=0; i<PadBoardAI.ROWS; ++i) {
			for(int j=0; j<PadBoardAI.COLS; ++j) {
				if (gameBoard[i][j] != PadBoardAI.X_ORBS && (gameBoard[i][j]%10) == 9 &&
						matchBoard.board[i][j] == PadBoardAI.UNDEFINED ) {
					return matchBoard;
				}
			}
		}
		return null;
	}

	private void bombExplode(MatchBoard mb) {
		int[][] board = PadBoardAI.create_empty_board();
		int bombCount = 0;
		for(int i=0; i<PadBoardAI.ROWS; ++i) {
			for(int j=0; j<PadBoardAI.COLS; ++j) {
				if (gameBoard[i][j] != PadBoardAI.X_ORBS && (gameBoard[i][j]%10) == 9 &&
						mb.board[i][j] == PadBoardAI.UNDEFINED) {
					for(int x=0; x<PadBoardAI.ROWS; ++x) {
						board[x][j] = 9;
					}
					for(int y=0; y<PadBoardAI.COLS; ++y) {
						board[i][y] = 9;
					}
					++bombCount;
				}
			}
		}
		ArrayList<RowCol> matchList = new ArrayList<RowCol>();
		for(int i=0; i<PadBoardAI.ROWS; ++i) {
			for (int j = 0; j < PadBoardAI.COLS; ++j) {
				if (board[i][j] == 9) {
					matchList.add(RowCol.make(i, j));
					gameBoard[i][j] = PadBoardAI.X_ORBS;
				}
			}
		}

		bombed = bombCount;
		final int size = matchList.size();
		final AtomicInteger cc = new AtomicInteger(0);

		for (RowCol rc : matchList) {
			final Sprite s = sprites[rc.row][rc.col];
			final IEntity entity = s.getChildByTag(1);
			if (entity != null) {
				engine.runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						entity.dispose();
						s.detachChild(entity);
					}
				});
			}
			s.registerEntityModifier(new AlphaModifier(
					SimulatorConstants.SECOND_REMOVE, 1.0f, 0.0f) {
				protected void onModifierFinished(
						final org.andengine.entity.IEntity pItem) {
					int total = cc.incrementAndGet();
					addToDetachList(pItem);

					if (total == size) {
						findMatches();
					}
				};
			});
		}
	}


	List<Match> matches = new ArrayList<Match>();
	List<Double> factors;
	List<Double> heals;
	List<Double> poisoned;
	int bombed = 0;

	private void findMatches() {
		LogUtil.d("findMatches()");
		MatchBoard mb = hasBomb();
		if (mb != null) {
			bombExplode(mb);
		} else {
			handler.post(new Runnable() {

				@Override
				public void run() {

					final MatchBoard matchBoard = PadBoardAI.findMatches(gameBoard, removeN, cantRemove);
					if (matches.size() == 0 && mAddComboSkill != null) {
						ArrayList<RowCol> list = new ArrayList<RowCol>();
						list.add(RowCol.make(0, 0));
						list.add(RowCol.make(0, 1));
						list.add(RowCol.make(0, 2));

						int addition = mAddComboSkill.getData().get(1);
						for (int i = 0; i < addition; ++i) {
							matches.add(Match.make(9, 3, 0, list));
						}
						mCombo.set(addition);
						mScoreBoard.addAll(matches);
					}
					matches.addAll(matchBoard.matches);
					factors = getFactor(matches);
					heals = getRecovery(matches);
					poisoned = getPoisonDamage(matches);

					if (unlockAction(matchBoard.matches)) {
                        mEnvironment.removeSkill(Constants.SK_DROP_LOCK);
                        if(hasLockOrb()) {
                            ActiveSkill skill = new ActiveSkill(SkillType.ST_UNLOCK);
                            skillFired(skill, new ICastCallback() {
                                @Override
                                public void onCastFinish(boolean casted) {
                                    Message.obtain(handler, NonUiHandler.MSG_REMOVAL, 0, 0,
                                            matchBoard).sendToTarget();
                                }
                            }, false);
                        } else {
							Message.obtain(handler, NonUiHandler.MSG_REMOVAL, 0, 0,
									matchBoard).sendToTarget();
						}
					} else {
						Message.obtain(handler, NonUiHandler.MSG_REMOVAL, 0, 0,
								matchBoard).sendToTarget();
					}
				}
			});
		}

	}

    private boolean hasLockOrb() {
        for (int i = 0; i < PadBoardAI.ROWS; ++i) {
            for (int j = 0; j < PadBoardAI.COLS; ++j) {
                if(gameBoard[i][j]>=20) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean unlockAction(List<Match> matches) {
        Team team = mEnvironment.getTeam();
        boolean unlockColor[] = {false, false, false, false, false, false};
        boolean unlockOrbs = false;
        for(int i=0; i<6; ++i) {
            MonsterInfo info = team.getMember(i);
            if(!isNullAwoken() && info != null &&
                    info.getTargetAwokenCount(AwokenSkill.L_ATTACK, true)>0) {
                Pair<Integer, Integer> props = info.getProps();
                unlockColor[props.first] = true;
                if(props.second != -1) {
                    unlockColor[props.second] = true;
                }
                unlockOrbs = true;
            }
        }
        boolean doUnlock = false;
        if(unlockOrbs) {
            for (Match m : matches) {
                if(m.type <= 5 && unlockColor[m.type] && m.isLFormat()) {
                    doUnlock = true;
                    break;
                }
            }
        }
        return doUnlock;
    }

	private float HALF_ORB_SIZE_PLUS_1414 = SimulatorConstants.HALF_ORB_SIZE * 1.41421356237f;

	private void swapOrb(int nx, int ny, int cx, int cy) {
		final float toX = cx * SimulatorConstants.ORB_SIZE
				+ SimulatorConstants.OFFSET_X;
		final float toY = cy * SimulatorConstants.ORB_SIZE
				+ SimulatorConstants.OFFSET_Y;
		float fromX = nx * SimulatorConstants.ORB_SIZE
				+ SimulatorConstants.OFFSET_X;
		float fromY = ny * SimulatorConstants.ORB_SIZE
				+ SimulatorConstants.OFFSET_Y;

        float centerX = (toX + fromX) / 2.0f;
        float centerY = (toY + fromY) / 2.0f;

        int angle = 0;
        boolean isAdd = true;
        float radius = Constants.HALF_ORB_SIZE;
        Direction direction = RowCol.direction(RowCol.make(cx, cy),
                RowCol.make(nx, ny));
        switch (direction) {
        case DIR_DOWN:
            angle = 90;
            isAdd = false;
            break;
        case DIR_UP:
            angle = 270;
            isAdd = true;
            break;
        case DIR_LEFT:
            angle = 180;
            isAdd = false;
            break;
        case DIR_RIGHT:
            angle = 0;
            isAdd = true;
            break;
        case DIR_LEFT_DOWN:
            radius = HALF_ORB_SIZE_PLUS_1414;
            angle = 135;
            isAdd = true;
            break;
        case DIR_LEFT_UP:
            radius = HALF_ORB_SIZE_PLUS_1414;
            angle = 225;
            isAdd = false;
            break;
        case DIR_RIGHT_DOWN:
            radius = HALF_ORB_SIZE_PLUS_1414;
            angle = 45;
            isAdd = true;
            break;
        case DIR_RIGHT_UP:
            radius = HALF_ORB_SIZE_PLUS_1414;
            angle = 315;
            isAdd = false;
            break;
        default:
            break;
        }

        int step = 36;
        if (isAdd) {
            angle += step;
        } else {
            angle -= step;
        }
        float[] xcoord = new float[5];
        float[] ycoord = new float[5];
        for (int i = 0; i < 5; ++i) {
            double radians = Math.toRadians(angle);
            xcoord[i] = centerX + radius * (float) Math.cos(radians);
            ycoord[i] = centerY + radius * (float) Math.sin(radians);
            if (isAdd) {
                angle += step;
            } else {
                angle -= step;
            }
        }

        PathModifier modifier = new PathModifier(Constants.SECOND_SWAP,
                new Path(xcoord, ycoord)) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                super.onModifierFinished(pItem);
                pItem.setPosition(toX, toY);
            }
        };
        modifier.setAutoUnregisterWhenFinished(true);
        AlphaModifier amodifier = new AlphaModifier(Constants.SECOND_SWAP, 0.5f, 1.0f);
        amodifier.setAutoUnregisterWhenFinished(true);
        sprites[nx][ny].registerEntityModifier(modifier);
        sprites[nx][ny].registerEntityModifier(amodifier);
        sprites[cx][cy].setPosition(fromX, fromY);

        Sprite s = sprites[cx][cy];
        Sprite ns = sprites[nx][ny];
        sprites[cx][cy] = sprites[nx][ny];
        sprites[nx][ny] = s;
        
        if (isBlack(ns.getColor()) && ns.getChildByTag(PadGameScene.TAG_SUPER_DARK) == null) {
            ns.setColor(Color.WHITE);
        }
        if (isBlack(s.getColor()) && s.getChildByTag(PadGameScene.TAG_SUPER_DARK) == null) {
            s.setColor(Color.WHITE);
        }

        int current = gameBoard[cx][cy];
        gameBoard[cx][cy] = gameBoard[nx][ny];
        gameBoard[nx][ny] = current;

        mResMgr.playSwapSound();
	}

	private boolean isBlack(Color color) {
		return (color.getRed() == 0f && color.getGreen() == 0f && color
				.getBlue() == 0f);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		return false;
	}

	private synchronized void changeState(GameState state) {
		LogUtil.d("previous=", mCurrentState, " , next=", state);
		if (mCurrentState != state) {
			mCurrentState = state;

			if (mCurrentState == GameState.GAME_PLAYER_TURN) {
				mFiredSkill = null;
				setBoardAlpha(0.5f, 1.0f);
			}
			if (mCurrentState == GameState.GAME_END) {

			}
		}
	}

	private void setBoardAlpha(float alpha, float to,
			final ICastCallback callback) {
		setBoardAlpha(alpha, to);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				callback.onCastFinish(true);
			}
		}, 350);
	}

	public void setBoardAlpha(float alpha, float to) {
		// no need to
		if (sprites[0][0].getAlpha() == to) {
			return;
		}
		for (int i = 0; i < PadBoardAI.ROWS; ++i) {
			for (int j = 0; j < PadBoardAI.COLS; ++j) {
				IEntityModifier modifier = new AlphaModifier(0.3f, alpha, to);
				modifier.setAutoUnregisterWhenFinished(true);
				sprites[i][j].registerEntityModifier(modifier);
			}
		}
	}

	private Sprite getCacheSprite(int orb) {
		return mCache[orb];
	}

	private Sprite getSprite(int orb) {
		return getSprite(orb, null);
	}

	final String[][] FLASH = { { "flash01.png", "flash02.png", "flash03.png" },
			{ "rflash01.png", "rflash02.png", "rflash03.png" } };

	private void addShineEffect(int index) {
		if (index <= 5) {
			boolean needUpdate = false;
			synchronized (mShineSet) {
				if (mShineSet.contains(index)) {
					needUpdate = true;
					mShineSet.remove(index);
				}
			}
			if (needUpdate) {
				int heart = 0;
				if (index == 2) {
					heart = 1;
				}

				for (int i = 0; i < PadBoardAI.ROWS; ++i) {
					for (int j = 0; j < PadBoardAI.COLS; ++j) {
						if (gameBoard[i][j] >= 10
								&& (gameBoard[i][j] % 10) == index) {
							final Sprite s = sprites[i][j];
							final IEntity entity = s.getChildByTag(1);
							if (entity != null) {
								engine.runOnUpdateThread(new Runnable() {

									@Override
									public void run() {
										if (!entity.isDisposed()) {
											entity.dispose();
										}
										s.detachChild(entity);
									}
								});
							}
							FlashSprite flash = new FlashSprite(0f, 0f,
									mResMgr.getTextureRegion(FLASH[heart][0]),
									mResMgr.getTextureRegion(FLASH[heart][1]),
									mResMgr.getTextureRegion(FLASH[heart][2]),
									vbom);
							flash.setTag(1);

							s.attachChild(flash);
						}

					}
				}

			}
		}
	}
	private Sprite getSprite(int orb, Sprite olds) {
		Sprite s = new Sprite(0, 0, textureOrbs.get(orb % 10), vbom) {
			@Override
			public void setAlpha(float pAlpha) {
				int count = getChildCount();
				for (int i = 0; i < count; ++i) {
					getChildByIndex(i).setAlpha(pAlpha);
				}

				super.setAlpha(pAlpha);
			}

			@Override
			public void setColor(Color pColor) {
				int count = getChildCount();
				for (int i = 1; i < count; ++i) {
					getChildByIndex(i).setColor(pColor);
				}
				super.setColor(pColor);
			}

		};

		if ((orb >= 10 && orb <= 19) ||
				(orb >= 30 && orb <= 39)) {
			if ((orb % 10) <= 5) {
				synchronized (mShineSet) {
					mShineSet.add(orb % 10);
				}
			}

			float scale = getPlusScale();
			Sprite plus = new Sprite(0, 0,
					mResMgr.getTextureRegion(orbClz, TextureOrbs.PLUS_ID), vbom);
			plus.setScale(scale, scale);
			plus.setPosition(45f, 45f);
			s.attachChild(plus);
		}
		if (orb >= 20) {
			Sprite lock = new Sprite(0, 0, mResMgr.getTextureRegion(orbClz, TextureOrbs.LOCK_ID), vbom);
			lock.setScale(0.75f);
			lock.setPosition(0f, 0f);
			lock.setZIndex(5);
			s.attachChild(lock);
		}
		if (olds != null) {
			if (isBlack(olds.getColor())) {
				s.setColor(Color.BLACK);
				IEntity entity = olds.getChildByTag(PadGameScene.TAG_SUPER_DARK);
				if (entity != null) {
					olds.detachChild(PadGameScene.TAG_SUPER_DARK);
					s.attachChild(entity);
				}
			}
			float alpha = olds.getAlpha();
			if (alpha < 1.0f) {
				s.setAlpha(alpha);
			}
		}
		return s;
	}

	private void setSuperDarkOrb(ResourceManager res, Sprite s, int turns) {
		if ( s == null ) {
			return ;
		}
		IEntity child = s.getChildByTag(PadGameScene.TAG_SUPER_DARK);
		if (isBlack(s.getColor()) && child != null) {
			Text text = (Text) child.getChildByTag(PadGameScene.TAG_SUPER_DARK_TEXT);
			//FIXME: need to calculate for each independence orbs
			//int addition = Integer.parseInt(text.getText().toString());
			text.setText(String.valueOf(turns)); //addition
		} else {
			s.setColor(Color.BLACK);
			Sprite sdark = new Sprite(0, 0, res.getTextureRegion(SimulateAssets.class, SimulateAssets.SUPERDARK_ID), vbom);
			Text text = new Text(0, 0, res.getFontSmall(), String.valueOf(turns), vbom);
			text.setTag(PadGameScene.TAG_SUPER_DARK_TEXT);
			float px = (sdark.getWidth() - text.getWidth()) / 2f;
			text.setPosition(px, 4f);
			text.setColor(Color.RED);
			sdark.attachChild(text);
			sdark.setPosition(12, 8);
			sdark.setTag(PadGameScene.TAG_SUPER_DARK);
			s.attachChild(sdark);
		}
	}


	private void setCloud(final List<Integer> data) {
		final ResourceManager res = ResourceManager.getInstance();

		if(data.get(1) == -1) {
			int line = data.get(2);
			int offset = data.get(3);

			float scale = getScaleRatio();
			if (line >= 10) {
//				for (int i = 0; i < x; ++i) {
				int x = (line == 10)? 0: PadBoardAI.ROWS-1;
				for (int j = 0; j < PadBoardAI.COLS; ++j) {
					int posx = SimulatorConstants.ORB_SIZE * (x + offset) + SimulatorConstants.OFFSET_X - 6;
					int posy = SimulatorConstants.ORB_SIZE * (j) + SimulatorConstants.OFFSET_Y - 6;
					Sprite s = new Sprite(0, 0, res.getTextureRegion("cloud.png"), vbom);
					s.setScale(scale, scale);
					s.setPosition(posx, posy);
					s.setZIndex(4);
					attachChild(s);
					s.registerEntityModifier(new AlphaModifier(SimulatorConstants.SECOND_REFRESH, 0.3f, 1.0f));
					s.registerEntityModifier(new ScaleModifier(SimulatorConstants.SECOND_REFRESH, 0.8f, 1.1f) {
						@Override
						protected void onModifierFinished(IEntity pItem) {
							super.onModifierFinished(pItem);
							if (mEnvironment.resistBadEffect(AwokenSkill.RESISTANCE_KUMO)) {
								removeCloud();
							}
						}
					});

					cloudSprite.add(s);
				}
//				}
			} else {
				int y = (line == 0)? 0:PadBoardAI.COLS-1;
				for (int i = 0; i < PadBoardAI.ROWS; ++i) {
//					for (int j = 0; j < y; ++j) {
					int posx = SimulatorConstants.ORB_SIZE * (i) + SimulatorConstants.OFFSET_X - 6;
					int posy = SimulatorConstants.ORB_SIZE * (y+offset) + SimulatorConstants.OFFSET_Y - 6;
					Sprite s = new Sprite(0, 0, res.getTextureRegion("cloud.png"), vbom);
					s.setScale(scale, scale);
					s.setPosition(posx, posy);
					s.setZIndex(4);
					attachChild(s);
					s.registerEntityModifier(new AlphaModifier(SimulatorConstants.SECOND_REFRESH, 0.3f, 1.0f));
					s.registerEntityModifier(new ScaleModifier(SimulatorConstants.SECOND_REFRESH, 0.8f, 1.1f) {
						@Override
						protected void onModifierFinished(IEntity pItem) {
							super.onModifierFinished(pItem);
							if (mEnvironment.resistBadEffect(AwokenSkill.RESISTANCE_KUMO)) {
								removeCloud();
							}
						}
					});

					cloudSprite.add(s);
//					}
				}
			}
		} else {
			int x = data.get(1);
			int y = data.get(2);

			int offset_x = RandomUtil.getInt(PadBoardAI.ROWS - x + 1);
			int offset_y = RandomUtil.getInt(PadBoardAI.COLS - y + 1);


			for (int i = 0; i < x; ++i) {
				for (int j = 0; j < y; ++j) {
					int posx = SimulatorConstants.ORB_SIZE * (i + offset_x) + SimulatorConstants.OFFSET_X - 6;
					int posy = SimulatorConstants.ORB_SIZE * (j + offset_y) + SimulatorConstants.OFFSET_Y - 6;
					Sprite s = new Sprite(0, 0, res.getTextureRegion("cloud.png"), vbom);
					s.setPosition(posx, posy);
					s.setZIndex(4);
					attachChild(s);
					s.registerEntityModifier(new AlphaModifier(SimulatorConstants.SECOND_REFRESH, 0.3f, 1.0f));
					s.registerEntityModifier(new ScaleModifier(SimulatorConstants.SECOND_REFRESH, 0.8f, 1.1f) {
						@Override
						protected void onModifierFinished(IEntity pItem) {
							super.onModifierFinished(pItem);
							if (mEnvironment.resistBadEffect(AwokenSkill.RESISTANCE_KUMO)) {
								removeCloud();
							}
						}
					});

					cloudSprite.add(s);
				}
			}
		}
	}

	private void clearCantRemove() {
		cantRemove = null;
	}

	private void removeCloud() {
		for(Sprite s : cloudSprite) {
			//s.registerEntityModifier();
			s.registerEntityModifier(new AlphaModifier(SimulatorConstants.SECOND_REFRESH, 1.0f, 0.2f));
			s.registerEntityModifier(new ScaleModifier(SimulatorConstants.SECOND_REFRESH, 1.0f, 1.3f){
				@Override
				protected void onModifierFinished(IEntity pItem) {
					detach(pItem);
					super.onModifierFinished(pItem);
				}
			});
		}
		cloudSprite.clear();
	}

	private void setSuperDark(final List<Integer> data, final ICastCallback callback) {
		final ResourceManager res = ResourceManager.getInstance();
		Sprite circle1 = new Sprite(0, 0, res.getTextureRegion("kage.png"), vbom);
		float cx = circle1.getWidth()/2f;
		float cy = circle1.getHeight()/2f;

		circle1.setPosition(GameActivity.SCREEN_WIDTH/2.0f-cx,
				GameActivity.SCREEN_HEIGHT * 0.3f -cy);
		circle1.setColor(Color.BLACK);
		circle1.setScaleCenter(cx, cy);
		circle1.setZIndex(10);

		IEntityModifier modifier = new ScaleModifier(0.6f, 1f, 28f) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				final boolean isTarget = (data.size()%2) == 1;
				int turns = data.get(0);
				int size = data.size() - 1;
				if (data.get(1) == -1 && data.size() > 2) {
					int random = RandomUtil.range(data.get(2), data.get(3));
					boolean is7x6 = mEnvironment.is7x6();
					for (int i = 0; i < random; ++i) {
						int y = RandomUtil.getInt((is7x6)? 6:5);
						int x = RandomUtil.getInt((is7x6)? 7:6);
						Sprite s = sprites[x][y];
						setSuperDarkOrb(res, s, turns);
					}
				}
				else if (isTarget) {
					for (int i = 1; i < size; i += 2) {
						int y = data.get(i) - 1, x = data.get(i + 1) - 1;
						Sprite s = sprites[x][y];
						setSuperDarkOrb(res, s, turns);
					}
				} else {
					if (data.get(size) == -1) { // all
						for(int i=0; i<PadBoardAI.ROWS; ++i) {
							for(int j=0; j<PadBoardAI.COLS; ++j) {
								Sprite s = sprites[i][j];
								setSuperDarkOrb(res, s, turns);
							}
						}
					} else {
						for (int i = 1; i < size; i += 2) {
							int line = data.get(i);
							int offset = data.get(i+1);
							if (line == 0) { // top
								for(int j=0; j<PadBoardAI.ROWS; ++j) {
									Sprite s = sprites[j][offset];
									setSuperDarkOrb(res, s, turns);
								}
							} else if (line == 1) { // bottom
								for(int j=0; j<PadBoardAI.ROWS; ++j) {
									Sprite s = sprites[j][PadBoardAI.COLS-offset-1];
									setSuperDarkOrb(res, s, turns);
								}
							} else if (line == 2) { // left
								for(int j=0; j<PadBoardAI.COLS; ++j) {
									Sprite s = sprites[offset][j];
									setSuperDarkOrb(res, s, turns);
								}
							} else if (line == 3) { // right
								for(int j=0; j<PadBoardAI.COLS; ++j) {
									Sprite s = sprites[PadBoardAI.ROWS-offset-1][j];
									setSuperDarkOrb(res, s, turns);
								}
							}
						}
					}
				}

				IEntityModifier modifier = new ScaleModifier(0.4f, 28f, 1f) {
					protected void onModifierFinished(IEntity pItem) {
						super.onModifierFinished(pItem);
						detach(pItem);

						if(mEnvironment.resistBadEffect(AwokenSkill.RESISTANCE_DARK)) {
							int size = data.size();
							if ( isTarget ) {
								for (int i = 1; i < size; i += 2) {
									int y = data.get(i) - 1, x = data.get(i + 1) - 1;
									sprites[x][y].registerEntityModifier(new ColorModifier(0.4f, Color.BLACK, Color.WHITE));
									sprites[x][y].detachChild(PadGameScene.TAG_SUPER_DARK);
								}
							} else {
								for(int i=0; i<PadBoardAI.ROWS; ++i) {
									for (int j=0; j<PadBoardAI.COLS; ++j) {
										IEntity entity = sprites[i][j].getChildByTag(PadGameScene.TAG_SUPER_DARK);
										if (entity != null) {
											sprites[i][j].registerEntityModifier(new ColorModifier(0.4f, Color.BLACK, Color.WHITE));
											sprites[i][j].detachChild(entity);
										}
									}
								}
							}
						}
					};
				};
				modifier.setAutoUnregisterWhenFinished(true);
				pItem.registerEntityModifier(modifier);
				callback.onCastFinish(true);
				super.onModifierFinished(pItem);
			}
		};
		modifier.setAutoUnregisterWhenFinished(true);
		circle1.registerEntityModifier(modifier);
		attach(circle1);
	}


	private void setDarkScreen(final ICastCallback callback) {
		ResourceManager res = ResourceManager.getInstance();
		Sprite circle1 = new Sprite(0, 0, res.getTextureRegion("kage.png"),
				vbom);
		float cx = circle1.getWidth() / 2f;
		float cy = circle1.getHeight() / 2f;

		circle1.setPosition(GameActivity.SCREEN_WIDTH / 2.0f - cx,
				GameActivity.SCREEN_HEIGHT * 0.3f - cy);
		circle1.setColor(Color.BLACK);
		circle1.setScaleCenter(cx, cy);
		circle1.setZIndex(10);

		IEntityModifier modifier = new ScaleModifier(0.6f, 1f, 28f) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				for (int i = 0; i < PadBoardAI.ROWS; ++i) {
					for (int j = 0; j < PadBoardAI.COLS; ++j) {
						sprites[i][j].setColor(Color.BLACK);
					}
				}
				IEntityModifier modifier = new ScaleModifier(0.4f, 28f, 1f) {
					protected void onModifierFinished(IEntity pItem) {
						super.onModifierFinished(pItem);
						detach(pItem);

						if (mEnvironment
								.resistBadEffect(AwokenSkill.RESISTANCE_DARK)) {
							for (int i = 0; i < PadBoardAI.ROWS; ++i) {
								for (int j = 0; j < PadBoardAI.COLS; ++j) {
									sprites[i][j]
											.registerEntityModifier(new ColorModifier(
													0.4f, Color.BLACK,
													Color.WHITE));
								}
							}
						}
					};
				};
				modifier.setAutoUnregisterWhenFinished(true);
				pItem.registerEntityModifier(modifier);
				callback.onCastFinish(true);
				super.onModifierFinished(pItem);
			}
		};
		modifier.setAutoUnregisterWhenFinished(true);
		circle1.registerEntityModifier(modifier);
		attach(circle1);
	}

	private boolean isPoison(int orb) {
		int x10 = orb%10;
		return x10 == 6 || x10 == 8;
	}

	private boolean isJarma(int orb) {
		int x10 = orb%10;
		return x10 == 7 || x10 == 9;
	}

	private void setChangeLineBoard(ActiveSkill skill,
			final ICastCallback callback, final boolean byEnemy) {
		changeState(GameState.GAME_ANIMATION);
		resetCombo();

		final List<Integer> data = skill.getData();

		engine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				boolean[] counterSpell = { false, false };
				if (byEnemy) {
					counterSpell[0] = mEnvironment
							.resistBadEffect(AwokenSkill.RESISTANCE_POISON);
					counterSpell[1] = mEnvironment
							.resistBadEffect(AwokenSkill.RESISTANCE_JAMMER);
				}

				int loop = data.size();
				for (int index = 0; index < loop; index += 2) {
					int pos = data.get(index);
					int toOrb = data.get(index + 1);

					if (pos < 3 || pos == 5 || pos == 6) {
						int line;
						if (pos < 3) {
							line = pos * 2;
						} else if (pos == 5) {
							line = 1;
						} else {
							line = 3;
						}
						int y = SimulatorConstants.ORB_SIZE * line;
						for (int i = 0; i < PadBoardAI.ROWS; ++i) {
							if (gameBoard[i][line] >= 20) {
								Shake.apply(sprites[i][line], 250f, 25, 5);
								continue;
							}

							int x = SimulatorConstants.ORB_SIZE * i;
							int addition = 0;
							if (gameBoard[i][line] >= 10
									&& gameBoard[i][line] != PadBoardAI.X_ORBS
									&& toOrb <= 5) {
								addition = 10;
							}

							final Sprite s = sprites[i][line];

							Sprite ns = getSprite(toOrb + addition, s);
							ns.setPosition(x + SimulatorConstants.OFFSET_X, y
									+ SimulatorConstants.OFFSET_Y);
							ns.setZIndex(1);
							attachChild(ns);

							if ((counterSpell[0] && isPoison(toOrb))
									|| (counterSpell[1] && isJarma(toOrb))) {
								ns.registerEntityModifier(new ScaleModifier(
										SimulatorConstants.SECOND_REFRESH,
										0.4f, 2.0f) {
									protected void onModifierFinished(
											IEntity pItem) {
										pItem.registerEntityModifier(new ScaleModifier(
												SimulatorConstants.SECOND_REFRESH,
												2.0f, 0.1f) {
											protected void onModifierFinished(
													IEntity pItem) {
												pItem.setVisible(false);
												addToDetachList(pItem);
											};
										});

										s.registerEntityModifier(new ScaleModifier(
												SimulatorConstants.SECOND_REFRESH,
												2.4f, 1.0f));
									};
								});
							} else {
								gameBoard[i][line] = toOrb + addition;
								sprites[i][line] = ns;

								if (!s.isDisposed()) {
									s.setZIndex(2);
									s.setColor(Color.WHITE);

									s.registerEntityModifier(new ScaleModifier(
											SimulatorConstants.SECOND_REFRESH,
											0.4f, 1.6f));
									s.registerEntityModifier(new AlphaModifier(
											SimulatorConstants.SECOND_REFRESH,
											1.0f, 0.3f) {
										protected void onModifierFinished(
												IEntity pItem) {
											addToDetachList(pItem);
										};
									});
								}
							}
						}
					} else {
						int line = (pos == 3) ? 0 : 5;
						switch (pos) {
						case 3:
							line = 0;
							break;
						case 4:
							line = 5;
							break;
						case 7:
						case 8:
						case 9:
						case 10:
							line = pos - 7 + 1;
							break;
						}
						int x = SimulatorConstants.ORB_SIZE * line;
						for (int j = 0; j < PadBoardAI.COLS; ++j) {

							if (gameBoard[line][j] >= 20) {
								Shake.apply(sprites[line][j], 250f, 25, 5);
								continue;
							}
							int y = SimulatorConstants.ORB_SIZE * j;
							int addition = (toOrb <= 5
									&& gameBoard[line][j] >= 10 && gameBoard[line][j] != PadBoardAI.X_ORBS) ? 10
									: 0;

							final Sprite s = sprites[line][j];

							Sprite ns = getSprite(toOrb + addition, s);
							ns.setPosition(x + SimulatorConstants.OFFSET_X, y
									+ SimulatorConstants.OFFSET_Y);
							ns.setZIndex(1);
							attachChild(ns);

							if ((counterSpell[0] && isPoison(toOrb))
									|| (counterSpell[1] && isJarma(toOrb))) {
								ns.registerEntityModifier(new ScaleModifier(
										SimulatorConstants.SECOND_REFRESH,
										0.4f, 2.0f) {
									protected void onModifierFinished(
											IEntity pItem) {
										pItem.registerEntityModifier(new ScaleModifier(
												SimulatorConstants.SECOND_REFRESH,
												2.0f, 0.1f) {
											protected void onModifierFinished(
													IEntity pItem) {
												pItem.setVisible(false);
												addToDetachList(pItem);
											};
										});

										s.registerEntityModifier(new ScaleModifier(
												SimulatorConstants.SECOND_REFRESH,
												2.4f, 1.0f));
									};
								});
							} else {
								gameBoard[line][j] = toOrb + addition;
								sprites[line][j] = ns;

								if (!s.isDisposed()) {
									s.setZIndex(2);
									s.setColor(Color.WHITE);

									s.registerEntityModifier(new ScaleModifier(
											SimulatorConstants.SECOND_REFRESH,
											0.4f, 1.6f));
									s.registerEntityModifier(new AlphaModifier(
											SimulatorConstants.SECOND_REFRESH,
											1.0f, 0.3f) {
										protected void onModifierFinished(
												IEntity pItem) {
											addToDetachList(pItem);
										};
									});
								}
							}
						}
					}
				}
				sortChildren();
			}
		});

		registerUpdateHandler(new TimerHandler(
				SimulatorConstants.SECOND_REFRESH + 0.1f, new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						unregisterUpdateHandler(pTimerHandler);
						if (callback != null) {
							callback.onCastFinish(true);
						} else {
							changeState(GameState.GAME_PLAYER_TURN);
						}
					}
				}));
	}

	public void setChangeColorBoard(final int[][] board,
			final List<Integer> changeList, final ICastCallback callback,
			final boolean byEnemy) {
		changeState(GameState.GAME_ANIMATION);

		// PadBoardAI.copy_board(board, gameBoard);

		resetCombo();

		engine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				boolean[] badOrb = { false, false, false, false };
				Set<Integer> changeSet = new HashSet<Integer>();
				int size = changeList.size();
				for (int i = 0; i < size; i += 2) {
					changeSet.add(changeList.get(i) % 10);
					int toOrb = changeList.get(i + 1) % 10;
					if (toOrb > 5) {
						badOrb[toOrb - 6] = true;
					}
				}

				boolean[] counterSpell = { false, false };
				if (byEnemy) {
					if (badOrb[0] || badOrb[2]) {
						counterSpell[0] = mEnvironment
								.resistBadEffect(AwokenSkill.RESISTANCE_POISON);
					} else if (badOrb[1]) {
						counterSpell[1] = mEnvironment
								.resistBadEffect(AwokenSkill.RESISTANCE_JAMMER);
					}
				}

				for (int i = 0; i < PadBoardAI.ROWS; ++i) {
					for (int j = 0; j < PadBoardAI.COLS; ++j) {
						int x = SimulatorConstants.ORB_SIZE * i;
						int y = SimulatorConstants.ORB_SIZE * j;

						int toOrb = board[i][j] % 10;
						if (gameBoard[i][j] != board[i][j]
								|| changeSet.contains(gameBoard[i][j] % 10)) {
							if (gameBoard[i][j] >= 20
									&& (gameBoard[i][j] + 10) != toOrb) {
								Shake.apply(sprites[i][j], 250f, 25, 5);
								continue;
							}

							final Sprite s = sprites[i][j];
							Sprite ns = getSprite(board[i][j], s);
							ns.setPosition(x + SimulatorConstants.OFFSET_X, y
									+ SimulatorConstants.OFFSET_Y);

							ns.setZIndex(1);
							attachChild(ns);

							if ((counterSpell[0] && isPoison(toOrb))
									|| (counterSpell[1] && isJarma(toOrb))) {
								ns.registerEntityModifier(new ScaleModifier(
										SimulatorConstants.SECOND_REFRESH,
										0.4f, 2.0f) {
									protected void onModifierFinished(
											IEntity pItem) {
										pItem.registerEntityModifier(new ScaleModifier(
												SimulatorConstants.SECOND_REFRESH,
												2.0f, 0.1f) {
											protected void onModifierFinished(
													IEntity pItem) {
												pItem.setVisible(false);
												addToDetachList(pItem);
											};
										});

										s.registerEntityModifier(new ScaleModifier(
												SimulatorConstants.SECOND_REFRESH,
												2.4f, 1.0f));
									};
								});
							} else {
								gameBoard[i][j] = board[i][j];
								sprites[i][j] = ns;

								if (!s.isDisposed()) {
									s.setZIndex(2);
									s.setColor(Color.WHITE);

									s.registerEntityModifier(new ScaleModifier(
											SimulatorConstants.SECOND_REFRESH,
											0.4f, 1.6f));
									s.registerEntityModifier(new AlphaModifier(
											SimulatorConstants.SECOND_REFRESH,
											1.0f, 0.3f) {
										protected void onModifierFinished(
												IEntity pItem) {
											addToDetachList(pItem);
										};
									});
								}
							}
						}

					}
				}

				sortChildren();
			}
		});

		registerUpdateHandler(new TimerHandler(
				SimulatorConstants.SECOND_REFRESH + 0.1f, new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						unregisterUpdateHandler(pTimerHandler);
						if (callback != null) {
							callback.onCastFinish(true);
						} else {
							changeState(GameState.GAME_PLAYER_TURN);
						}
					}
				}));
	}

	public void setChangeColorBoard(final int[][] board,
			final ICastCallback callback, final boolean byEnemy) {
		changeState(GameState.GAME_ANIMATION);

		// PadBoardAI.copy_board(board, gameBoard);

		resetCombo();

		engine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				boolean[] counterSpell = { false, false };
				if (byEnemy) {
					counterSpell[0] = mEnvironment
							.resistBadEffect(AwokenSkill.RESISTANCE_POISON);
					counterSpell[1] = mEnvironment
							.resistBadEffect(AwokenSkill.RESISTANCE_JAMMER);
				}

				for (int i = 0; i < PadBoardAI.ROWS; ++i) {
					for (int j = 0; j < PadBoardAI.COLS; ++j) {
						int x = SimulatorConstants.ORB_SIZE * i;
						int y = SimulatorConstants.ORB_SIZE * j;

						if (gameBoard[i][j] != board[i][j]) {
							// if lock orb and not add plus
							if (gameBoard[i][j] >= 20
									&& (gameBoard[i][j] + 10) != board[i][j]) {
								Shake.apply(sprites[i][j], 250f, 25, 5);
								continue;
							}
							int toOrb = board[i][j] % 10;

							final Sprite s = sprites[i][j];
							Sprite ns = getSprite(board[i][j], s);
							ns.setPosition(x + SimulatorConstants.OFFSET_X, y
									+ SimulatorConstants.OFFSET_Y);
							ns.setZIndex(1);
							attachChild(ns);

							if ((counterSpell[0] && isPoison(toOrb))
									|| (counterSpell[1] && isJarma(toOrb))) {
								ns.registerEntityModifier(new ScaleModifier(
										SimulatorConstants.SECOND_REFRESH,
										0.4f, 2.0f) {
									protected void onModifierFinished(
											IEntity pItem) {
										pItem.registerEntityModifier(new ScaleModifier(
												SimulatorConstants.SECOND_REFRESH,
												2.0f, 0.1f) {
											protected void onModifierFinished(
													IEntity pItem) {
												pItem.setVisible(false);
												addToDetachList(pItem);
											};
										});

										s.registerEntityModifier(new ScaleModifier(
												SimulatorConstants.SECOND_REFRESH,
												2.4f, 1.0f));
									};
								});
							} else {
								gameBoard[i][j] = board[i][j];
								sprites[i][j] = ns;

								if (!s.isDisposed()) {
									s.setZIndex(2);
									s.setColor(Color.WHITE);

									s.registerEntityModifier(new ScaleModifier(
											SimulatorConstants.SECOND_REFRESH,
											0.4f, 1.6f));
									s.registerEntityModifier(new AlphaModifier(
											SimulatorConstants.SECOND_REFRESH,
											1.0f, 0.3f) {
										protected void onModifierFinished(
												IEntity pItem) {
											addToDetachList(pItem);
										};
									});
								}
							}

							// gameBoard[i][j] = board[i][j];
							//
							// Sprite s = sprites[i][j];
							// boolean isBlack = isBlack(s.getColor());
							// float alpha = s.getAlpha();
							// if (!s.isDisposed()) {
							// s.setZIndex(2);
							// s.setColor(Color.WHITE);
							// s.registerEntityModifier(new ScaleModifier(
							// SimulatorConstants.SECOND_REFRESH, 0.4f, 1.6f));
							// s.registerEntityModifier(new AlphaModifier(
							// SimulatorConstants.SECOND_REFRESH, 1.0f, 0.3f) {
							// protected void onModifierFinished(IEntity pItem)
							// {
							// addToDetachList(pItem);
							// };
							// });
							// }
							// s = getSprite(gameBoard[i][j], isBlack, alpha);
							// sprites[i][j] = s;
							// setPosition(i, j, x, y);
							// s.setZIndex(1);
							// attachChild(s);
						}

					}
				}

				sortChildren();
			}
		});

		registerUpdateHandler(new TimerHandler(
				SimulatorConstants.SECOND_REFRESH + 0.1f, new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						unregisterUpdateHandler(pTimerHandler);
						if (callback != null) {
							callback.onCastFinish(true);
						} else {
							changeState(GameState.GAME_PLAYER_TURN);
						}
					}
				}));
	}


    private void setChangeColorBoard(int[][] board,final ICastCallback callback) {
        changeState(GameState.GAME_ANIMATION);

        // PadBoardAI.copy_board(board, gameBoard);

        resetCombo();
        List<IEntity> attachList = new ArrayList<IEntity>();
        for (int i = 0; i < PadBoardAI.ROWS; ++i) {
            for (int j = 0; j < PadBoardAI.COLS; ++j) {
                int x = Constants.ORB_SIZE * i;
                int y = Constants.ORB_SIZE * j;

                if (gameBoard[i][j] != board[i][j]) {
                    if(gameBoard[i][j]>=20 && board[i][j]==gameBoard[i][j]-20) {
                        //LogUtil.d("do unlock orb.");
                    } else if(gameBoard[i][j]>=20 && (gameBoard[i][j]+10) != board[i][j]) {
                        Shake.apply(sprites[i][j], 250f, 25, 5);
                        continue;
                    }

                    gameBoard[i][j] = board[i][j];

                    Sprite s = sprites[i][j];
                    Sprite ns = getSprite(gameBoard[i][j], s);
                    sprites[i][j] = ns;
                    setPosition(i, j, x, y);
                    ns.setZIndex(1);
                    attachList.add(ns);

					if (!s.isDisposed()) {
						s.setZIndex(2);
						s.setColor(Color.WHITE);
						s.registerEntityModifier(new ScaleModifier(
								Constants.SECOND_REFRESH, 0.4f, 1.6f));
						s.registerEntityModifier(new AlphaModifier(
								Constants.SECOND_REFRESH, 1.0f, 0.0f) {
							protected void onModifierFinished(IEntity pItem) {
								addToDetachList(pItem);
							};
						});
					}
                }

            }
        }
        attach(attachList);
        registerUpdateHandler(new TimerHandler(Constants.SECOND_REFRESH + 0.3f,
                new ITimerCallback() {

                    @Override
                    public void onTimePassed(TimerHandler pTimerHandler) {
                        unregisterUpdateHandler(pTimerHandler);
                        if(callback != null) {
                            callback.onCastFinish(true);
                        } else {
                            changeState(GameState.GAME_PLAYER_TURN);
                        }
                    }
                }));
    }

	private void setChangeColorBoard(int[][] board) {
		changeState(GameState.GAME_ANIMATION);

		// PadBoardAI.copy_board(board, gameBoard);

		resetCombo();
		List<IEntity> attachList = new ArrayList<IEntity>();
		for (int i = 0; i < PadBoardAI.ROWS; ++i) {
			for (int j = 0; j < PadBoardAI.COLS; ++j) {
				int x = Constants.ORB_SIZE * i;
				int y = Constants.ORB_SIZE * j;

				if (gameBoard[i][j] != board[i][j]) {
					if(gameBoard[i][j]>=20 && board[i][j]==gameBoard[i][j]-20) {
						//LogUtil.d("do unlock orb.");
					} else if (gameBoard[i][j] >= 20
							&& (gameBoard[i][j] + 10) != board[i][j]) {
						Shake.apply(sprites[i][j], 250f, 25, 5);
						continue;
					}

					gameBoard[i][j] = board[i][j];

					Sprite s = sprites[i][j];
					Sprite ns = getSprite(gameBoard[i][j], s);
					sprites[i][j] = ns;
					setPosition(i, j, x, y);
					ns.setZIndex(1);
					attachList.add(ns);

					if (!s.isDisposed()) {
						s.setZIndex(2);
						s.setColor(Color.WHITE);
						s.registerEntityModifier(new ScaleModifier(
								Constants.SECOND_REFRESH, 0.4f, 1.6f));
						s.registerEntityModifier(new AlphaModifier(
								Constants.SECOND_REFRESH, 1.0f, 0.0f) {
							protected void onModifierFinished(IEntity pItem) {
								addToDetachList(pItem);
							};
						});
					}
				}

			}
		}
		attach(attachList);
		registerUpdateHandler(new TimerHandler(Constants.SECOND_REFRESH + 0.1f,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						unregisterUpdateHandler(pTimerHandler);
						changeState(GameState.GAME_PLAYER_TURN);
					}
				}));
	}

	private void setGameBoard(final int[][] board,
			final IAnimationCallback callback,
			final ICastCallback castCallback, final boolean byEnemy) {
		changeState(GameState.GAME_ANIMATION);
		// PadBoardAI.copy_board(board, gameBoard);

		resetCombo();
		engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				boolean[] counterSpell = { false, false };
				if (byEnemy) {
					counterSpell[0] = mEnvironment
							.resistBadEffect(AwokenSkill.RESISTANCE_POISON);
					counterSpell[1] = mEnvironment
							.resistBadEffect(AwokenSkill.RESISTANCE_JAMMER);
				}

				for (int i = 0; i < PadBoardAI.ROWS; ++i) {
					for (int j = 0; j < PadBoardAI.COLS; ++j) {
						int x = SimulatorConstants.ORB_SIZE * i;
						int y = SimulatorConstants.ORB_SIZE * j;

						final Sprite s = sprites[i][j];
						if(gameBoard[i][j] == board[i][j]) {
							continue;
						}

						if (gameBoard[i][j] >= 20
								&& (gameBoard[i][j] + 10) != board[i][j]) {
							Shake.apply(s, 250f, 25, 5);
							continue;
						}
						int toOrb = board[i][j] % 10;

						Sprite ns = getSprite(board[i][j], s);
						ns.setPosition(x + SimulatorConstants.OFFSET_X, y
								+ SimulatorConstants.OFFSET_Y);
						ns.setZIndex(1);
						attachChild(ns);

						if ((counterSpell[0] && isPoison(toOrb))
								|| (counterSpell[1] && isJarma(toOrb))) {
							ns.registerEntityModifier(new ScaleModifier(
									SimulatorConstants.SECOND_REFRESH, 0.4f,
									2.0f) {
								protected void onModifierFinished(IEntity pItem) {
									pItem.registerEntityModifier(new ScaleModifier(
											SimulatorConstants.SECOND_REFRESH,
											2.0f, 0.1f) {
										protected void onModifierFinished(
												IEntity pItem) {
											pItem.setVisible(false);
											addToDetachList(pItem);
										};
									});

									s.registerEntityModifier(new ScaleModifier(
											SimulatorConstants.SECOND_REFRESH,
											2.4f, 1.0f));
								};
							});
							continue;
						} else {
							gameBoard[i][j] = board[i][j];
							sprites[i][j] = ns;

							if (!s.isDisposed()) {
								s.setZIndex(2);
								s.setColor(Color.WHITE);

								s.registerEntityModifier(new ScaleModifier(
										SimulatorConstants.SECOND_REFRESH,
										0.4f, 1.6f));
								s.registerEntityModifier(new AlphaModifier(
										SimulatorConstants.SECOND_REFRESH,
										1.0f, 0.3f) {
									protected void onModifierFinished(
											IEntity pItem) {
										addToDetachList(pItem);
									};
								});
							}
						}

					}
				}
				sortChildren();
			}
		});

		registerUpdateHandler(new TimerHandler(
				SimulatorConstants.SECOND_REFRESH + 0.1f, new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						unregisterUpdateHandler(pTimerHandler);
						if (callback != null) {
							callback.onAnimationFinished();
						}
						if (castCallback != null) {
							castCallback.onCastFinish(true);
						} else {
							changeState(GameState.GAME_PLAYER_TURN);
						}
					}
				}));
	}

	public void setGameBoard(int[][] board, ICastCallback callback,
			boolean byEnemy) {
		setGameBoard(board, null, callback, byEnemy);
	}

	@Override
	public String getSceneName() {
		return MultiplePadGameScene.class.getSimpleName();
	}

	@Override
	public void onPlayerTurn() {
		playerAttacked = false;
		currentTeam = (currentTeam == 0) ? 1 : 0;
		changeState(GameState.GAME_PLAYER_TURN);
	}

	@Override
	public void onFireSkills(boolean start) {
		if (start) {
			changeState(GameState.GAME_FIRE_SKILL);
		} else {
			changeState(GameState.GAME_PLAYER_TURN);
		}
	}

	private int counter = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			if (++counter >= 6) {
				counter = 0;
				Observable.from(mEnvironment.getEnemies())
						.observeOn(Schedulers.immediate())
						.subscribe(new Action1<Enemy>() {

							@Override
							public void call(Enemy enemy) {
								if (!enemy.dead()) {
									AttackValue attack = new AttackValue(
											5000000, 0, 0);
									enemy.dealtDamageDirect(attack, 1.0f,
											new AnimationCallback() {

												@Override
												public void animationDone() {
												}
											});
								}
							}
						});
			}
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			counter = 0;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public GameState getState() {
		return mCurrentState;
	}

	@Override
	public List<Match> getMatches() {
		return mScoreBoard;
	}

	@Override
	public TeamInfo getTeam() {
		return mTeam[currentTeam];
	}

	@Override
	public void attach(final List<? extends IEntity> entity) {
		engine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				for (IEntity e : entity) {
					attachChild(e);
				}
				sortChildren();
			}
		});
	}

	@Override
	public void attach(final IEntity entity) {
		engine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
                if(entity.hasParent()) {
                    entity.detachSelf();
                }
				attachChild(entity);
				sortChildren();
			}
		});
	}

	@Override
	public void detach(final List<IEntity> entityList) {
		engine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				for (IEntity entity : entityList) {
					if (!entity.isDisposed()) {
						entity.dispose();
					}
					detachChild(entity);
				}
			}
		});
	}

	@Override
	public void detach(final IEntity entity) {
		engine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				if (!entity.isDisposed()) {
					entity.dispose();
				}
				detachChild(entity);
			}
		});
	}

	@Override
	public int[][] getBoard() {
		return gameBoard;
	}

	@Override
	public Pair<Integer, Integer> getSize() {
		return new Pair<Integer, Integer>(PadBoardAI.ROWS, PadBoardAI.COLS);
	}

	@Override
	public void setAdditionMoveTime(int msec) {
		mAdjustTime = msec;
	}
	
	@Override
	public void setAdditionMoveTimeX(int x) {
		mAdjustTimeX = x;
	}
	
	@Override
	public void removeAddComboSkill() {
		mAddComboSkill = null;
	}

	@Override
	public void updateSuperDark(final int turns) {
		engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				boolean end = turns == 0;
				if(end) {
					for(int i=0; i<PadBoardAI.ROWS; ++i) {
						for (int j = 0; j < PadBoardAI.COLS; ++j) {
							Sprite s = sprites[i][j];
							if(isBlack(s.getColor()) && s.getChildByTag(PadGameScene.TAG_SUPER_DARK) != null) {
								s.detachChild(PadGameScene.TAG_SUPER_DARK);
								s.setColor(Color.WHITE);
							}
						}
					}
				} else {
					for(int i=0; i<PadBoardAI.ROWS; ++i) {
						for (int j = 0; j < PadBoardAI.COLS; ++j) {
							Sprite s = sprites[i][j];
							if(isBlack(s.getColor())) {
								IEntity dark = s.getChildByTag(PadGameScene.TAG_SUPER_DARK);
								if(dark != null) {
									Text text = (Text) dark.getChildByTag(PadGameScene.TAG_SUPER_DARK_TEXT);
									text.setText(String.valueOf(turns));
								}
							}
						}
					}
				}
			}
		});

	}


	@Override
	public void removeSkill(String key) {
		if(Constants.SK_DROP_ONLY.equals(key)) {
			mDropOnlySkill = null;
		} else if(Constants.SK_ENHANCE_ORB.equals(key)) {
			mDropEnhanceSkill = null;
		} else if (Constants.SK_NO_DROP.equals(key)) {
			mNoDropSkill = null;
		} else if (Constants.SK_CLOUD.equals(key)) {
			engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					removeCloud();
				}
			});
		} else if (Constants.SK_LOCK_REMOVE.equals(key)) {
			engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					clearCantRemove();
				}
			});
		}
	}

	@Override
	public boolean isNullAwoken() {
		return mNullAwoken || mEnvironment.isAwokenLocked();
	}
}
