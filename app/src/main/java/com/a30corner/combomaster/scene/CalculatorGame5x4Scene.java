package com.a30corner.combomaster.scene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.FileBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.util.color.Color;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.functions.Action1;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;
import android.util.SparseArray;
import android.widget.Toast;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.GameActivity;
import com.a30corner.combomaster.activity.ui.DialogUtil;
import com.a30corner.combomaster.activity.ui.ScoreDialog;
import com.a30corner.combomaster.activity.ui.SetLvDialog;
import com.a30corner.combomaster.activity.ui.SettingsDialog;
import com.a30corner.combomaster.activity.ui.SettingsDialog.ISettingsCallback;
import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.manager.SceneManager;
import com.a30corner.combomaster.pad.PadBoardAI;
import com.a30corner.combomaster.pad.RowCol;
import com.a30corner.combomaster.pad.RowCol.Direction;
import com.a30corner.combomaster.pad.mode5x4.BoardStack5x4;
import com.a30corner.combomaster.pad.mode5x4.DamageCalculator5x4;
import com.a30corner.combomaster.pad.mode5x4.GameReplay5x4;
import com.a30corner.combomaster.pad.mode5x4.Match5x4;
import com.a30corner.combomaster.pad.mode5x4.MatchBoard5x4;
import com.a30corner.combomaster.pad.mode5x4.PadBoardAI5x4;
import com.a30corner.combomaster.pad.mode7x6.Match7x6;
import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.pad.monster.LeaderSkill;
import com.a30corner.combomaster.pad.monster.LeaderSkill.LeaderSkillType;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill.AwokenSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MoneyAwokenSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.SinglePowerUp;
import com.a30corner.combomaster.pad.monster.TeamInfo;
import com.a30corner.combomaster.strategy.IBoardStrategy;
import com.a30corner.combomaster.strategy.StrategyUtil;
import com.a30corner.combomaster.texturepacker.GameUIAssets;
import com.a30corner.combomaster.texturepacker.SimulateAssets;
import com.a30corner.combomaster.texturepacker.TextureOrbs;
import com.a30corner.combomaster.texturepacker.TextureOrbsCw;
import com.a30corner.combomaster.texturepacker.TextureOrbsTos;
import com.a30corner.combomaster.utils.Constants;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.RandomUtil;
import com.a30corner.combomaster.utils.RealPathUtil;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;
import com.a30corner.combomaster.utils.SharedPreferenceUtil.Skill;

import static com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType.ST_RANDOM_CHANGE_FIX;

public class CalculatorGame5x4Scene extends BaseMenuScene implements
		IOnMenuItemClickListener {

	// board info
	private BoardStack5x4 mStack;
	private int[][] gameBoard;
	private SparseArray<ITextureRegion> textureOrbs = new SparseArray<ITextureRegion>();
	private Sprite[][] sprites = new Sprite[PadBoardAI5x4.ROWS][PadBoardAI5x4.COLS];
	private Sprite[][] boardSprite = new Sprite[PadBoardAI5x4.ROWS][PadBoardAI5x4.COLS];
	// edit mode
	private static final int EDIT_SPRITE_SIZE = 11;
	private Sprite[] mSetByHandSprite = new Sprite[EDIT_SPRITE_SIZE];
	private Sprite mOpenFileSprite;
	private Sprite mEditOkSprite;
	private Sprite[] mSkill = new Sprite[6];
	
	private Sprite[] mBindSprite = new Sprite[6];
	
	private Sprite mSwapSprite;
	// move mode
	private Sprite orbInHand = null;
    private Sprite[] mCache = new Sprite[10];
	
	// replay mode
	private List<Shape> mReplayPathDrawing = new ArrayList<Shape>();
	private Sprite[] mCheckBox = new Sprite[2];
	private AtomicBoolean mShowPath = new AtomicBoolean(false);
	private Sprite[] mSkillCheckBox = new Sprite[2];
	private AtomicBoolean mSkillFired = new AtomicBoolean(false);
    private Sprite[] mDropCheckBox = new Sprite[2];
    private AtomicBoolean mContinueDrop = new AtomicBoolean(false);
    
	// calculator
	private ScoreDialog mDialog = null;
	
	private SettingsDialog mSettingDialog = null;
	private SetLvDialog mLvDialog = null;

	// skill
	private ActiveSkill mAddComboSkill = null;
	private ActiveSkill mDropRateSkill = null;
	private ActiveSkill mPreviousSkill = null;
	private ActiveSkill mPowerUpSkill = null;
	private ActiveSkill mTimeExtendSkill = null;
	private int mDropRateTimes = 0;
	private int mPowerUpTimes = 0;
	private int mTimeExtendTimes = 0;
	private List<Sprite> mPowerUpSprite = new ArrayList<Sprite>();
	private Sprite mTimeExtendSprite = null;
	private Sprite[] mDropRateSprite = null;

//	private List<Sprite> mArcList = new ArrayList<Sprite>();

	private boolean mUsedSkillPrev = false;
	private boolean mShowDialog = true;

	private AtomicBoolean mForceReset = new AtomicBoolean(false);

	private Rect mAnalysisRect;
	
	// detach list
	private List<IEntity> mRemoveList = new ArrayList<IEntity>();

	private static final long DELAY_DEFAULT = 300L;

	private static final int RELEASE_THRESHOLD = 10;

	// FIXME: use sharepreference
	private static final float PROGRESS_BAR_WIDTH = GameActivity.SCREEN_WIDTH - 10;
	private static final int MENU_RESET = 0;
	private static final int MENU_LOAD = 1;
	private static final int MENU_REPLAY = 2;
	private static final int MENU_EDIT = 3;
	private static final int MENU_SETTINGS = 4;
	private static final int MENU_PREVIOUS = 5;
	private static final int MENU_RESULT = 6;
	private static final int MENU_HELP = 7;
	private static final int MENU_SETLV = 8;
	private static final int REQUEST_PICK_PHOTO = 999;

	private static int TIME_CHANGE_THE_WORLD = 10 * 1000;

	private ResourceManager mResMgr;

	private enum GameState {
		GAME_RUN, GAME_MODIFY_BOARD, GAME_REPLAY, GAME_END, GAME_ANIMATION,
	}

	private GameState mCurrentState = GameState.GAME_RUN;

	private int mTimeFixed = 0;
	private int mTimeRemain = Constants.DEFAULT_SECOND;
	private int mDropTime;
	private int mDropTimeDefault;
	private long mDropType;
	
	private int[] mPlusOrbCount = {0,0,0,0,0,0};

	private TeamInfo mTeam;
	private int removeN = 3;
	private boolean mNullAwoken = false;
    private boolean[] mStagePowerUp = null;
	private boolean[] mBindStatus = {false, false, false, false, false, false};
	private int mSwapIndex = 0;
	Map<String, Object> mSettings = new HashMap<String, Object>();

	private int mEditSelect = 0;

	private Rectangle mProgressbar;

	private Handler handler;

	private Text mDropRateText;
	private Text mContinueDropText;
	private Text mSkillFiredText;
	private Text mShowPathText;
	private Text mTimerText;
	private Text mComboText;
	private Text mStepCountText;
	private Text mPowerUpText;
	private Text mTimeExtendText;
	private AtomicInteger mCombo = new AtomicInteger(99);
	private String mStepText;

	private Text[] mLvText; // for special mode
	
//	private IMenuItem mHelpMenu;
//	private IMenuItem mLvMenu;
	
    // hp sprite
    private Sprite mHpBackground = null;
    private Sprite mHp = null;
    private Text mHpText = null;
	
	private String mPowerUpRawText;

	private AtomicBoolean mSkillTouchable = new AtomicBoolean(true);
	private AtomicBoolean mCountDown = new AtomicBoolean(false);
	private AtomicBoolean mChangeTheWorld = new AtomicBoolean(false);
	private boolean bigFileLoaded = false;

	private TimerHandler mTimer = new TimerHandler(0.02f, true,
			new ITimerCallback() {

				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					mTimeRemain -= 20;
					float progress = mTimeRemain / (float) mDropTime;
					mProgressbar.setWidth(PROGRESS_BAR_WIDTH * progress);
					float green = 1.0f * progress;
					float red = 1.0f - green;
					mProgressbar.setColor(red, green, 0f);

					int timePass = mDropTime - mTimeRemain;
					int sec = timePass / 1000;
					if (sec > 99) {
						sec = 99;
					}
					String s = String.format("%02d.%02d ", sec,
							timePass / 10 % 100);
					mTimerText.setText(s + mSecondText);

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
					String s = String.format("%02d.%02d ", sec,
							timePass / 10 % 100);
					mTimerText.setText(s + mSecondText);

					if (mTimeRemain <= 0) {
						onTimeUp();
					}
				}
			});

	private GameReplay5x4 mReplay;
	private ArrayList<Match5x4> mScoreBoard = new ArrayList<Match5x4>();

	private interface IAnimationCallback {
		public void onAnimationFinished();
	}

	private class NonUiHandler extends Handler {

		protected static final int MSG_REMOVAL = 0;
		protected static final int MSG_REPLAY = 1;
		public static final int MSG_REPLAY_INIT = 2;

		public NonUiHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MSG_REMOVAL:
				MatchBoard5x4 board = (MatchBoard5x4) msg.obj;
				doRemoval(board, msg.arg1);
				break;
			case MSG_REPLAY:
				RowCol current = (RowCol) msg.obj;
				doReplay(current);
				break;
			case MSG_REPLAY_INIT:
				RowCol rc = mReplay.next();
				if (rc != null) {
					Sprite s = sprites[rc.row][rc.col];
					s.setScale(1.2f);
					s.setAlpha(0.5f);
					s.setZIndex(2);

					showReplayPath();

					sortChildren();
					sendMessageDelayed(Message.obtain(this, MSG_REPLAY, rc),
							DELAY_DEFAULT);
				}
				break;
			}

		}
	}

    private float getScaleRatio() {
        return Constants.ORB_SIZE_5x4 / (float)Constants.ORB_SIZE;
    }
    
    private float getPlusScale() {
        return (28f / 45f);
    }
	
	private void showReplayPath() {
		List<RowCol> path = mReplay.getPath();
		boolean first = true;
		RowCol prev = null;
		float sx = 0f, sy = 0f;
		// int quarter = Constants.HALF_ORB_SIZE_5x4 / 2;
		Random random = new Random();
//		ResourceManager res = ResourceManager.getInstance();
//		ITextureRegion arcTexture = res.getTextureRegion("arc.png");

		for (RowCol rc : path) {
			RowCol current = rc;

			if (first) {
				first = false;
				sx = getBoardCenterX(current.row);
				sy = getBoardCenterY(current.col);
			} else {
				Direction direction = RowCol.direction(prev, current);

				float nx = 0f;
				float ny = 0f;
				float addSize = ((float) (random.nextInt(Constants.ORB_SIZE_5x4)) - Constants.HALF_ORB_SIZE_5x4) * 0.70f;
				switch (direction) {
				case DIR_DOWN:
					nx = sx;
					ny = getBoardCenterY(current.col) + addSize;// sy + addSize;
					break;
				case DIR_LEFT:
					nx = getBoardCenterX(current.row) + addSize;
					ny = sy;
					break;
				case DIR_LEFT_DOWN: {
					float addx = getBoardX(prev.row) - sx;
					float addy = getBoardY(prev.col + 1) - sy;
					nx = sx + addx + addx;
					ny = sy + addy + addy;
					break;
				}
				case DIR_LEFT_UP: {
					float addx = getBoardX(prev.row) - sx;
					float addy = getBoardY(prev.col) - sy;
					nx = sx + addx + addx;
					ny = sy + addy + addy;
					break;
				}
				case DIR_NONE:
					// should never happened
					LogUtil.e("something wrong in path");
					nx = sx;
					ny = sy;
					break;
				case DIR_RIGHT:
					nx = getBoardCenterX(current.row) + addSize;
					ny = sy;
					break;
				case DIR_RIGHT_DOWN: {
					float addx = getBoardX(prev.row + 1) - sx;
					float addy = getBoardY(prev.col + 1) - sy;
					nx = sx + addx + addx;
					ny = sy + addy + addy;
					break;
				}
				case DIR_RIGHT_UP: {
					float addx = getBoardX(prev.row + 1) - sx;
					float addy = getBoardY(prev.col) - sy;
					nx = sx + addx + addx;
					ny = sy + addy + addy;
					break;
				}
				case DIR_UP:
					nx = sx;
					ny = getBoardCenterY(current.col) + addSize;
					break;
				default:
					break;
				}
				Line line = new Line(sx, sy, nx, ny, vbom);
				line.setLineWidth(3.0f);
				line.setZIndex(4);
				mReplayPathDrawing.add(line);

//				Sprite arc = new Sprite(0f, 0f, arcTexture, vbom);
//				arc.setPosition(nx, ny);
//				arc.setZIndex(5);
//				attachChild(arc);
//				mArcList.add(arc);
				
				sx = nx;
				sy = ny;
			}
			prev = current;
		}
		float addition = 360f / mReplayPathDrawing.size();
		float count = 0f;
		for (Shape line : mReplayPathDrawing) {
			float[] hsv = { count, 1f, 1f };
			int color = android.graphics.Color.HSVToColor(hsv);
			float r = android.graphics.Color.red(color) / 255f;
			float g = android.graphics.Color.green(color) / 255f;
			float b = android.graphics.Color.blue(color) / 255f;
			line.setColor(new Color(r, g, b));
			count += addition;
		}
		// attach child need run on update thread
		displayReplayPath(true);
	}

	private void addToDetachList(IEntity entity) {
		if (entity != null) {
			entity.setVisible(false);
			synchronized (mRemoveList) {
				mRemoveList.add(entity);
			}

		}

	}

	private void displayReplayPath(final boolean display) {
		activity.getEngine().runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				for (Shape l : mReplayPathDrawing) {
					if (display) {
						attachChild(l);
					} else {
						addToDetachList(l);
					}
				}
			}
		});
	}

	private void resetDrawingPath() {
		for (Shape l : mReplayPathDrawing) {
			addToDetachList(l);
		}
		mReplayPathDrawing.clear();
	}

	private float getBoardX(int row) {
		return row * Constants.ORB_SIZE_5x4 + Constants.OFFSET_X;
	}

	private float getBoardY(int col) {
		return col * Constants.ORB_SIZE_5x4 + Constants.OFFSET_Y;
	}

	private float getBoardCenterX(int row) {
		return getBoardX(row) + Constants.HALF_ORB_SIZE_5x4;
	}

	private float getBoardCenterY(int col) {
		return getBoardY(col) + Constants.HALF_ORB_SIZE_5x4;
	}

	private void doReplay(final RowCol current) {
		if (mForceReset.get()) {
			changeState(GameState.GAME_RUN);
			return;
		}
		if (current != null && mReplay.hasNext()) {
			final RowCol next = mReplay.next();

			final float currentX = getBoardX(current.row);// current.row *
															// Constants.ORB_SIZE_5x4
															// +
															// Constants.OFFSET_X;
			final float currentY = getBoardY(current.col);// current.col *
															// Constants.ORB_SIZE_5x4+
															// Constants.OFFSET_Y;

			final float offsetX = (next.row - current.row) * Constants.ORB_SIZE_5x4
					* 0.5f;
			final float offsetY = (next.col - current.col) * Constants.ORB_SIZE_5x4
					* 0.5f;

			Sprite move = sprites[current.row][current.col];
			move.registerEntityModifier(new MoveModifier(
					Constants.SECOND_MOVE_HALF, currentX, currentX + offsetX,
					currentY, currentY + offsetY) {
				@Override
				protected void onModifierFinished(IEntity pItem) {
					float nextX = next.row * Constants.ORB_SIZE_5x4
							+ Constants.OFFSET_X;
					float nextY = next.col * Constants.ORB_SIZE_5x4
							+ Constants.OFFSET_Y;

					int step = (mReplay.current() - 1);
					if (step > 999) {
						step = 999;
					}
					mStepCountText.setText(step + mStepText);

					Sprite s = sprites[next.row][next.col];
					// show move transition animation
					s.setAlpha(0.5f);
					s.registerEntityModifier(new MoveModifier(
							Constants.SECOND_MOVE_HALF, nextX, currentX, nextY,
							currentY) {
						@Override
						protected void onModifierFinished(IEntity pItem) {
							super.onModifierFinished(pItem);
							pItem.setAlpha(1.0f);
						}
					});
					pItem.registerEntityModifier(new MoveModifier(
							Constants.SECOND_MOVE_HALF, currentX + offsetX,
							nextX, currentY + offsetY, nextY) {
						@Override
						protected void onModifierFinished(IEntity pItem) {
							super.onModifierFinished(pItem);
							Message.obtain(handler, NonUiHandler.MSG_REPLAY,
									next).sendToTarget();
						}
					});
					// swap these two orbs
					swapOrb(current, next);

					super.onModifierFinished(pItem);
				}
			});
		} else {
			// done...
			Sprite s = sprites[current.row][current.col];
			s.setScale(getScaleRatio());
			s.setAlpha(1.0f);
			s.setZIndex(1);
			sortChildren();

			mReplay.reset();
			if (!mShowPath.get()) {
				displayReplayPath(false);
			}

			findMatches();
		}
	}

	private void swapOrb(RowCol c, RowCol n) {
		Sprite s = sprites[n.row][n.col];
		sprites[n.row][n.col] = sprites[c.row][c.col];
		sprites[c.row][c.col] = s;

		int orb = gameBoard[n.row][n.col];
		gameBoard[n.row][n.col] = gameBoard[c.row][c.col];
		gameBoard[c.row][c.col] = orb;
	}

	private void doRemoval(final MatchBoard5x4 board, final int current) {
		if (mForceReset.get()) {
			changeState(GameState.GAME_RUN);
			return;
		}
		if (board.matches.size() == current) {
			// end
			// calculating.set(false);
			// remove empty orbs and make new orbs
			adjustGameBoard(board);
		} else {
			Match5x4 match = board.matches.get(current);
			mScoreBoard.add(match);
			mCombo.incrementAndGet();
			final int size = match.list.size();
			final AtomicInteger counter = new AtomicInteger(0);

			// show combo text animation and remove animation
			int comboCount = mCombo.get();
			if (comboCount > 99) {
				comboCount = 99;
			}
			mComboText.setText(String.valueOf(comboCount) + " Combo");
			mResMgr.playComboSound(comboCount);

			for (RowCol rc : match.list) {
				sprites[rc.row][rc.col]
						.registerEntityModifier(new AlphaModifier(
								Constants.SECOND_REMOVE, 1.0f, 0.0f) {
							protected void onModifierFinished(
									final org.andengine.entity.IEntity pItem) {
								int total = counter.incrementAndGet();
								addToDetachList(pItem);

								if (total == size) {
									Message.obtain(handler,
											NonUiHandler.MSG_REMOVAL,
											current + 1, 0, board)
											.sendToTarget();
								}
							};
						});
			}
		}
	}

	private void adjustGameBoard(MatchBoard5x4 board) {
		gameBoard = PadBoardAI5x4.in_place_remove_matches(gameBoard, board.board);
		boolean hasChanged = true;
		ArrayList<Pair<RowCol, RowCol>> dropList = new ArrayList<Pair<RowCol, RowCol>>();
		while (hasChanged) {
			hasChanged = false;

			for (int i = 0; i < PadBoardAI5x4.ROWS; ++i) {
				RowCol target = null;
				RowCol orb = null;
				for (int j = PadBoardAI5x4.COLS - 1; j >= 0; --j) {
					if (gameBoard[i][j] == PadBoardAI5x4.X_ORBS) {
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
					gameBoard[orb.row][orb.col] = PadBoardAI5x4.X_ORBS;
					hasChanged = true;
				} else if (target != null) {
					// only drop once in this mode
					// create 1~5 orbs and add to drop list...
				    
				    // adjust drop rate by active skill
				    List<Pair<Integer, Integer>> dropData = null;
				    if ( mDropRateSkill != null ) {
				        List<Integer> data = mDropRateSkill.getData();
				        int size = data.size();
				        dropData = new ArrayList<Pair<Integer,Integer>>((size-1)/2);
				        for(int index=1; index<size; index+=2) {
				            dropData.add(new Pair<Integer, Integer>(data.get(index), data.get(index+1)));
				        }
				    }
				    
				    if ( mContinueDrop.get() ) {
                        int emptyLen = target.col + 1;
                        for (int k = target.col; k >= 0; --k) {
                            int neworb = PadBoardAI5x4.getNewOrbRestrictChances(mDropType, dropData);   
                            if (neworb <= 5 && RandomUtil.getLuck(mPlusOrbCount[neworb]*20)) {
                                neworb += 10;
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
		}
		if (dropList.size() > 0) {
			for (Pair<RowCol, RowCol> pair : dropList) {
				final RowCol target = pair.first;
				final RowCol orb = pair.second;
				int fromY = orb.col * Constants.ORB_SIZE_5x4 + OFFSET_Y_5X4;
				int toY = target.col * Constants.ORB_SIZE_5x4 + OFFSET_Y_5X4;

				if (PadBoardAI5x4.isValid(orb.row, orb.col)) {
					Sprite temp = sprites[orb.row][orb.col];
					sprites[orb.row][orb.col] = sprites[target.row][target.col];
					sprites[target.row][target.col] = temp;
				} else { // drop from sky
					Sprite newSprite = getSprite(gameBoard[target.row][target.col]);
					newSprite.setPosition(target.row * Constants.ORB_SIZE_5x4
							+ OFFSET_X_5X4, fromY);
					newSprite.setZIndex(0);
					sprites[target.row][target.col] = newSprite;
					attach(newSprite);
				}

				sprites[target.row][target.col]
						.registerEntityModifier(new MoveYModifier(
								Constants.SECOND_DROP_5x4, fromY, toY));
			}

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					findMatches();
				}
			}, (long) (1000 * (Constants.SECOND_DROP_5x4 + 0.05f)));

		} else {
			int step = mReplay.size();
			if (step > 999) {
				step = 999;
			}
			mStepCountText.setText(step + mStepText);
			Collections.sort(mScoreBoard, mScoreComparator);

            if(mAddComboSkill != null && mAddComboSkill.countDown()) {
            	mAddComboSkill = null;
            }
			if (mPowerUpTimes != 0) {
				mPreviousSkill = mPowerUpSkill;
				activity.getEngine().runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						if (--mPowerUpTimes == 0) {
							removeActiveSkill();
						} else if (mPowerUpSkill != null) {
							setPowerUpText(mPowerUpSkill.getData().get(1));
						}
					}
				});

			}
			
			if (mDropRateTimes != 0) {
                activity.getEngine().runOnUpdateThread(new Runnable() {

                    @Override
                    public void run() {
                        if (--mDropRateTimes == 0) {
                            removeDropRateSkill();
                        } else if (mDropRateSkill != null) {
                            setDropRateText();
                            //setPowerUpText(mPowerUpSkill.getData().get(1));
                        }
                    }
                });
			}
			
			if (mTimeExtendTimes != 0) {
				activity.getEngine().runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						if (--mTimeExtendTimes == 0) {
							updateDropTime();
							removeTimeExtendSkill();
						} else {
							setTimeExtendText(mTimeExtendSkill.getData().get(1));
						}
					}
				});
			}

			showScore();
			toggleSkillFired(false);
			if ( mContinueDrop.get() ) {
			    changeState(GameState.GAME_RUN);
			} else {
			    changeState(GameState.GAME_END);
			}
		}
	}
	
	private Comparator<Match5x4> mScoreComparator = new Comparator<Match5x4>() {

		@Override
		public int compare(Match5x4 lhs, Match5x4 rhs) {
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
	private String mSecondText;

	private void showScore(boolean fromMenu) {
		if ((fromMenu || mShowDialog) && mCurrentState != GameState.GAME_REPLAY) {

			ActiveSkill active = (mPreviousSkill == null) ? ActiveSkill.empty()
					: mPreviousSkill;
			if ( !fromMenu ) {
			    mUsedSkillPrev = mSkillFired.get();
			}
			mSettings.put("skillFired", mUsedSkillPrev);
			mSettings.put("singleUp", singleUp.value());
			
			final List<Pair<Double, Double>> data = DamageCalculator5x4.calculate(
					mTeam, mScoreBoard, active, 0, mSettings);
			final double recovery = DamageCalculator5x4.calculateRecovery(mTeam,
					mScoreBoard, active, mSettings);
			final double poison = DamageCalculator5x4.calculatePoison(mTeam, mScoreBoard, active);

			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (mDialog == null) {
						mDialog = new ScoreDialog(activity);
						mDialog.initViews();
					}

					// FIXME:
					mDialog.setData(mScoreBoard, data, recovery, poison);
					mDialog.show();
				}
			});
		}
	}

	private void showScore() {
		showScore(false);
	}

	private void loadSharedPreference() {
		SharedPreferenceUtil sp = SharedPreferenceUtil.getInstance(activity);
		mDropTime = (int) (1000 * (sp.getTimeLimit())); // default second + 2
		mDropType = PadBoardAI5x4.getType(sp.getDropsInCalcMode());
		mShowDialog = sp.getShowResultDialog();
		mShowPath.set(sp.getShowPath());
		mAnalysisRect = sp.getAnalysisRect();
	}
    private void loadSinglePowerUp() {
		SharedPreferences pref = getActivity().getSharedPreferences("singleUp", Context.MODE_PRIVATE);
		int item = pref.getInt("item"+ComboMasterApplication.getsInstance().getTargetNo(), 0);
		singleUp = SinglePowerUp.from(item);
    }
    
    SinglePowerUp singleUp;
    
	private void skillFired(ActiveSkill skill) {
		SkillType type = skill.getType();
		LogUtil.d("fire skill=", type);
		
		toggleSkillFired(true);
		
		switch (type) {
        case ST_TARGET_RANDOM:
            if (mCurrentState != GameState.GAME_END) {
                int[][] board = new int[PadBoardAI5x4.ROWS][PadBoardAI5x4.COLS];
                PadBoardAI5x4.copy_board(gameBoard, board);
                mStack.push(gameBoard);
            	List<Integer> sd = skill.getData();
            	int size = sd.get(0);
            	int targetSize = sd.get(size+1);
            	Set<Integer> set = new HashSet<Integer>();
            	for(int i=0; i<size; ++i) {
            		set.add(sd.get(i+1));
            	}
                setChangeColorBoard(PadBoardAI5x4
                        .randomChangedBoard(board, set, sd.subList(size+2, size+2+targetSize)));
            }
        	break;
		case ST_ADD_PLUS:
			if (mCurrentState != GameState.GAME_END) {
				List<Integer> addPlus = new ArrayList<Integer>();

				for (Integer d : skill.getData()) {
					addPlus.add(d);
					addPlus.add(d + 10);
				}

				int[][] board = new int[PadBoardAI5x4.ROWS][PadBoardAI5x4.COLS];
				PadBoardAI5x4.copy_board(gameBoard, board);
				mStack.push(gameBoard);
				

				setChangeColorBoard(PadBoardAI5x4
						.colorChangedBoard(board, addPlus));
			}
			break;
		case ST_CHANGE_THE_WORLD:
			if (mChangeTheWorld.get()) {
				return;
			}
			mStack.push(gameBoard);

			final int theWorldTime = skill.getData().get(0) * 1000;
			if (mCurrentState == GameState.GAME_END) {
				setGameBoard(PadBoardAI5x4.generateBoardWithPlus(mDropType, false, mPlusOrbCount),
						new IAnimationCallback() {

							@Override
							public void onAnimationFinished() {
								changeTheWorld(theWorldTime);
							}
						});
			} else {
				changeTheWorld(theWorldTime);
			}
			break;
		case ST_COLOR_CHANGE:
			if (mCurrentState != GameState.GAME_END) {
				List<Integer> data = new ArrayList<Integer>(skill.getData());
				int size = data.size();
				for (int i = 0; i < size; ++i) {
					data.add(data.get(i) + 10);
				}
                // poison include poison & enhanced poison
                for(int i=0; i<size; i+=2) {
                    int orb = data.get(i);
                    if(orb == 7) {
                        data.add(8);
                        data.add(data.get(i+1));
                        break;
                    }
                }
				int[][] board = new int[PadBoardAI5x4.ROWS][PadBoardAI5x4.COLS];
				PadBoardAI5x4.copy_board(gameBoard, board);
				mStack.push(gameBoard);

				setChangeColorBoard(PadBoardAI5x4.colorChangedBoard(board, data));
			}
			break;
		case ST_RANDOM_CHANGE_RESTRICT: {
            if (mCurrentState == GameState.GAME_END) {
                setGameBoard(PadBoardAI5x4.generateBoardWithPlus(mDropType, false, mPlusOrbCount),
                        new IAnimationCallback() {

                            @Override
                            public void onAnimationFinished() {
                            }
                        });
            }

            int[][] board = new int[PadBoardAI5x4.ROWS][PadBoardAI5x4.COLS];
            PadBoardAI5x4.copy_board(gameBoard, board);
            mStack.push(gameBoard);

            List<Integer> data = skill.getData();
            int size = data.size();
            
            List<Integer> colorList = new ArrayList<Integer>();
            List<Integer> countList = new ArrayList<Integer>();
            List<Integer> exceptList = new ArrayList<Integer>();
            for (int i = 0; i < size; i += 3) {
                colorList.add(data.get(i));
                countList.add(data.get(i + 1));
                exceptList.add(data.get(i+ 2));
            }

            List<RowCol> rcList = new ArrayList<RowCol>();
            for (int i = 0; i < PadBoardAI5x4.ROWS; ++i) {
                for (int j = 0; j < PadBoardAI5x4.COLS; ++j) {
                    boolean find = false;
                    for (int k = 0; k < colorList.size(); ++k) {
                        int color = colorList.get(k);
                        int except = exceptList.get(k);
                        if (board[i][j] == color || board[i][j] == (color + 10)
                                || board[i][j] == except || board[i][j] == (except+10)) {
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
                for(int i=0; i<csize; ++i) {
                	int color = colorList.get(i);
                	int ccsize = countList.get(i);
                	for(int j=0; j<ccsize; ++j) {
                		if(counter>=listSize) {
                			break;
                		}
                		RowCol rc = rcList.get(counter);
                		int addition = (gameBoard[rc.row][rc.col] >= 10 && gameBoard[rc.row][rc.col] != PadBoardAI.X_ORBS) ? 10: 0;
                		int cv = color;
                		if(cv<=5) {
                			cv += addition;
                		}
                		board[rc.row][rc.col] = cv;
                		++counter;
                	}
                }

                setChangeColorBoard(board);
            }
		    break;
		}
		case ST_RANDOM_CHANGE_FIX:
		case ST_RANDOM_CHANGE: {
			if (mCurrentState == GameState.GAME_END) {
				setGameBoard(PadBoardAI5x4.generateBoardWithPlus(mDropType, false, mPlusOrbCount),
						new IAnimationCallback() {

							@Override
							public void onAnimationFinished() {
							}
						});
			}

			int[][] board = new int[PadBoardAI5x4.ROWS][PadBoardAI5x4.COLS];
			PadBoardAI5x4.copy_board(gameBoard, board);
			mStack.push(gameBoard);

			List<Integer> data = skill.getData();
			int size = data.size();
			
			List<Integer> colorList = new ArrayList<Integer>();
			List<Integer> countList = new ArrayList<Integer>();
			for (int i = 0; i < size; i += 2) {
				colorList.add(data.get(i));
				countList.add(data.get(i + 1));
			}

			List<RowCol> rcList = new ArrayList<RowCol>();
			for (int i = 0; i < PadBoardAI5x4.ROWS; ++i) {
				for (int j = 0; j < PadBoardAI5x4.COLS; ++j) {
					boolean find = false;
					if(type != ST_RANDOM_CHANGE_FIX) {
						for (int k = 0; k < colorList.size(); ++k) {
							int color = colorList.get(k);
							if (board[i][j] == color || board[i][j] == (color + 10)) {
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
                for(int i=0; i<csize; ++i) {
                	int color = colorList.get(i);
                	int ccsize = countList.get(i);
                	for(int j=0; j<ccsize; ++j) {
                		if(counter>=listSize) {
                			break;
                		}
                		RowCol rc = rcList.get(counter);
                		int addition = (gameBoard[rc.row][rc.col] >= 10 && gameBoard[rc.row][rc.col] != PadBoardAI.X_ORBS) ? 10: 0;
                		int cv = color;
                		if(cv<=5) {
                			cv += addition;
                		}
                		board[rc.row][rc.col] = cv;
                		++counter;
                	}
                }

                setChangeColorBoard(board);
            }
			break;
		}
		case ST_POWER_UP_BY_AWOKEN:
		{           
            try {
            	createAllUpSprite(skill);
            } catch(Exception e) {
                LogUtil.e(e.toString());
            }
		}
			break;
		case ST_ADD_COMBO:
            if (mCurrentState == GameState.GAME_END) {
				setGameBoard(PadBoardAI5x4.generateBoardWithPlus(mDropType, false, mPlusOrbCount),
						new IAnimationCallback() {

							@Override
							public void onAnimationFinished() {
							}
						});
            }
            try {
            	createAddComboSkillSprite(skill);
            } catch (Exception e) {
                LogUtil.e(e.toString());
            }
            break;
		case ST_POWER_UP:
		case ST_TYPE_UP:
			if (mCurrentState == GameState.GAME_END) {
				setGameBoard(PadBoardAI5x4.generateBoardWithPlus(mDropType, false, mPlusOrbCount),
						new IAnimationCallback() {

							@Override
							public void onAnimationFinished() {
							}
						});
			}
			try {
				createSkillSprite(skill);
			} catch (Exception e) {
				LogUtil.e(e.toString());
			}
			break;
		case ST_MOVE_TIME_X:
		case ST_TIME_EXTEND:
			try {
				createTimeExtendSprite(skill);
				updateDropTime();
			} catch (Exception e) {
				LogUtil.e(e.toString());
			}
			break;
		case ST_TRANSFORM:
			// save current board
			if (mCurrentState != GameState.GAME_END) {
				mStack.push(gameBoard);
			}
			int[][] newboard = PadBoardAI5x4.generateBoardWith3Up(skill.getData());
			for (int i = 0; i < PadBoardAI5x4.ROWS; ++i) {
				for (int j = 0; j < PadBoardAI5x4.COLS; ++j) {
					if (gameBoard[i][j] >= 10
							&& gameBoard[i][j] != PadBoardAI5x4.X_ORBS &&
	                            (newboard[i][j]%10) <= 5) {
						newboard[i][j] += 10;
					}
				}
			}
			setGameBoard(newboard);
			break;
		case ST_ONE_LINE_TRANSFORM:
			if (mCurrentState == GameState.GAME_END) {
				setGameBoard(PadBoardAI5x4.generateBoardWithPlus(mDropType, false, mPlusOrbCount),
						new IAnimationCallback() {

							@Override
							public void onAnimationFinished() {
							}
						});
			}
			mStack.push(gameBoard);
			setChangeLineBoard(skill);
			break;
		case ST_DROP_RATE:
            if (mCurrentState == GameState.GAME_END) {
                setGameBoard(PadBoardAI5x4.generateBoardWithPlus(mDropType, false, mPlusOrbCount),
                        new IAnimationCallback() {

                            @Override
                            public void onAnimationFinished() {
                            }
                        });
            }
		    try {
		        createDropSkillSprite(skill);
		    } catch(Exception e) {
		        LogUtil.e(e.toString());
		    }
		    break;
		case ST_GRAVITY:
		case ST_NOT_SUPPORT:
		case ST_REDUCE_DEF:
		default:
			break;

		}
	}

	private void removeActiveSkill() {
		if (mPowerUpSprite.size() > 0) {
		    for(Sprite s : mPowerUpSprite){
		        if ( !s.isDisposed() ) {
		            s.dispose();
		        }
		        detach(s);
		    }
//			if (!mPowerUpSprite.get.isDisposed()) {
//				mPowerUpSprite[0].dispose();
//			}
//			detachChild(mPowerUpSprite);
			mPowerUpSprite.clear();

			detach(mPowerUpText);

			mPowerUpSkill = null;
		}
	}
	
	private void removeDropRateSkill() {
	    if ( mDropRateSkill != null ) {
	        for(int i=0; i<mDropRateSprite.length; ++i) {
    	        if ( !mDropRateSprite[i].isDisposed() ) {
    	            mDropRateSprite[i].dispose();
    	        }
    	        detach(mDropRateSprite[i]);
	        }
	        mDropRateSprite = null;
	        
	        detach(mDropRateText);
	        
	        mDropRateSkill = null;
	    }
	}
	
	private void removeTimeExtendSkill() {
        if (mTimeExtendSkill != null) {
            if (!mTimeExtendSprite.isDisposed()) {
                mTimeExtendSprite.dispose();
            }
            detach(mTimeExtendSprite);
            mTimeExtendSprite = null;

            detach(mTimeExtendText);

            mTimeExtendSkill = null;
        }
	}

    final int[] texture = {
            SimulateAssets.S_DARK_ID,SimulateAssets.S_FIRE_ID,0,
            SimulateAssets.S_LIGHT_ID, SimulateAssets.S_WATER_ID,SimulateAssets.S_WOOD_ID
    };

	private void createTimeExtendSprite(ActiveSkill skill) {
		removeTimeExtendSkill();
		mTimeExtendSkill = skill;
		List<Integer> data = skill.getData();

		mTimeExtendTimes = data.get(0);
		ITextureRegion pTextureRegion = mResMgr
				.getTextureRegion(SimulateAssets.class, SimulateAssets.EXTEND_TIME_ID);
		mTimeExtendSprite = new Sprite(0f, 0f, pTextureRegion, mResMgr.getVBOM());
		float x = mSkill[5].getX() + 16;
		float y = mSkill[5].getY() - mTimeExtendSprite.getHeight() - 8f;
		mTimeExtendSprite.setPosition(x, y);

		attach(mTimeExtendSprite);
	}

	private void createDropSkillSprite(ActiveSkill skill) {
        final int[] colorupTexture = {
        		SimulateAssets.S_DARK_ID,SimulateAssets.S_FIRE_ID,
        		SimulateAssets.S_HEART_ID,SimulateAssets.S_LIGHT_ID,
        		SimulateAssets.S_WATER_ID,SimulateAssets.S_WOOD_ID,
        		SimulateAssets.S_POISON_ID,SimulateAssets.S_JARMA_ID,
        		SimulateAssets.S_E_POISON_ID
        }; 
	    removeDropRateSkill();
	    List<Integer> data = skill.getData();
	    int size = data.size();
        float x = mSkill[4].getX();
        float y = mSkill[4].getY() - 8f;
        int index = 0;
        mDropRateSprite = new Sprite[(size-1)/2];
	    for(int i=1; i<size; i+=2, ++index) {
            ITextureRegion pTextureRegion;
            int type = data.get(i);
            pTextureRegion = mResMgr
                    .getTextureRegion(SimulateAssets.class,colorupTexture[type]);
    	    mDropRateSprite[index] = new Sprite(0f, 0f, pTextureRegion, mResMgr.getVBOM());
    	    mDropRateSprite[index].setPosition(x + index * pTextureRegion.getWidth() + 8, y-pTextureRegion.getHeight()+8);
    	    attach(mDropRateSprite[index]);
	    }
	    mDropRateTimes = data.get(0);
	    mDropRateText = new Text(0f, 0f, mResMgr.getFontSmall(), String.valueOf(mDropRateTimes), mResMgr.getVBOM());
	    mDropRateText.setPosition(mDropRateSprite[index-1].getX() + mDropRateSprite[index-1].getWidth() + 8, mDropRateSprite[index-1].getY());
	    attach(mDropRateText);

        mDropRateSkill = skill;
	}
	
    final int[] monsterTypeId = {
    		SimulateAssets.MONSTER_TYPE_0_ID,SimulateAssets.MONSTER_TYPE_1_ID,
    		SimulateAssets.MONSTER_TYPE_2_ID,SimulateAssets.MONSTER_TYPE_3_ID,
    		SimulateAssets.MONSTER_TYPE_4_ID,SimulateAssets.MONSTER_TYPE_5_ID,
    		SimulateAssets.MONSTER_TYPE_6_ID,SimulateAssets.MONSTER_TYPE_7_ID,
    		SimulateAssets.MONSTER_TYPE_8_ID,SimulateAssets.MONSTER_TYPE_9_ID,
    		0,SimulateAssets.MONSTER_TYPE_11_ID
    };
    

    private void createAllUpSprite(ActiveSkill skill) {
        removeActiveSkill();

        mPowerUpSkill = skill;
        
		List<Integer> data = skill.getData();
		mPowerUpTimes = data.get(0);
		int percent = data.get(1);
		int size = data.get(2);
		List<Integer> awoken = data.subList(3, 3+size);
		int total = 0;
        for (int i = 0; i < 6; ++i) {
            MonsterInfo info = mTeam.getMember(i);
            if (info != null && !mBindStatus[i]) {
    			for(Integer awk : awoken) {
                    total += info
                            .getTargetAwokenCount(AwokenSkill.get(awk), false);
    			}
            }
        }

        int factor = total * percent + 100;
        ITextureRegion pTextureRegion = mResMgr.getTextureRegion(SimulateAssets.class, SimulateAssets.ALL_UP_ID);

        Sprite s = new Sprite(0f, 0f, pTextureRegion, mResMgr.getVBOM());
        float x = mCheckBox[0].getX();
        float y = mDropCheckBox[0].getY() + mDropCheckBox[0].getHeight();
        s.setPosition(x, y + s.getHeight());
        attach(s);
        mPowerUpSprite.add(s);

        float offset = 0f;

        setPowerUpText(factor);

        float width = mPowerUpText.getWidth() / 4; // scale * 0.5, so we need to
        // sub 1/4 width
        float height = mPowerUpText.getHeight() / 4;
        mPowerUpText.setPosition(x - width + s.getWidth() + 8 + offset,
                y + s.getHeight() - height);
        attach(mPowerUpText);
    }
    
    private void createAddComboSkillSprite(ActiveSkill skill) {
        mAddComboSkill = skill;
        mAddComboSkill.setTurn(0, false);
        
        final int add = mAddComboSkill.getData().get(1);
        activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(activity, "add "+add+" combo(s)", Toast.LENGTH_SHORT).show();
			}
		});
    }
    
	private void createSkillSprite(ActiveSkill skill) {
		removeActiveSkill();

		mPowerUpSkill = skill;
		List<Integer> data = skill.getData();

		mPowerUpTimes = data.get(0);
		int type = data.get(2);
		int factor = data.get(1);

	    // maybe there is type2 or color2 enhanced
        int type2 = (data.size()>3)? data.get(3):-1;
		
		ITextureRegion pTextureRegion = null;
		ITextureRegion pTextureRegion1 = null;
		SkillType stype = skill.getType();
        if (stype == SkillType.ST_TYPE_UP) {
            pTextureRegion = mResMgr.getTextureRegion(SimulateAssets.class, monsterTypeId[type]);
            if (type2 != -1) {
                pTextureRegion1 = mResMgr.getTextureRegion(SimulateAssets.class, monsterTypeId[type]);
            }
        } else {
            pTextureRegion = mResMgr.getTextureRegion(SimulateAssets.class, texture[type]);
            if (type2 != -1) {
                pTextureRegion1 = mResMgr.getTextureRegion(SimulateAssets.class, texture[type2]);
            }
        }

		Sprite s = new Sprite(0f, 0f, pTextureRegion, mResMgr.getVBOM());
		float x = mCheckBox[0].getX();
		float y = mDropCheckBox[0].getY() + mDropCheckBox[0].getHeight();
		s.setPosition(x, y + s.getHeight());
		attach(s);		
		mPowerUpSprite.add(s);
		
		float offset = 0f;
		if ( type2 != -1 ) {
		    Sprite s2 = new Sprite(0f, 0f, pTextureRegion1, vbom);
		    s2.setPosition(x + s.getWidth() + 8, y + s.getHeight());
		    attach(s2);
		    mPowerUpSprite.add(s2);
		    offset = s2.getWidth() * 1.5f;
		}

		setPowerUpText(factor);
		
        float width = mPowerUpText.getWidth() / 4; // scale * 0.5, so we need to
        // sub 1/4 width
        float height = mPowerUpText.getHeight() / 4;
        mPowerUpText.setPosition(x - width + s.getWidth() + 8 + offset, y + s.getHeight() - height);
        attach(mPowerUpText);
	}

	private String getNoZeroFactor(int factor) {
		int count = (factor % 10) == 0 ? 1 : 2;

		return String.format("%1." + count + "f", ((float) factor / 100.0f));
	}

	private void setTimeExtendText(int time) {

	}

	private void setDropRateText() {
	    mDropRateText.setText(String.valueOf(mDropRateTimes));
	}
	
	private void setPowerUpText(int factor) {
		if (mPowerUpTimes > 1) {

			mPowerUpText.setText(String.format(mPowerUpRawText, mPowerUpTimes,
					getNoZeroFactor(factor)));
		} else {
			String nos = mPowerUpRawText;
			if (nos.contains("turns")) {
				nos = nos.replace("turns", "turn");
			}
			mPowerUpText.setText(String.format(nos, mPowerUpTimes,
					getNoZeroFactor(factor)));
		}
	}

	private void skillFired(Skill skill) {
		if (mCurrentState != GameState.GAME_END) {
			// PadBoardAI5x4.copy_board(gameBoard, previousBoard);
			mStack.push(gameBoard);
		}
		switch (skill.getType()) {
		case SKILL_TRANSFORM:
			long transform = skill.getData().get(0);
			setGameBoard(PadBoardAI5x4.generateBoard(transform, true));
			break;
		case SKILL_STANCE:
			if (mCurrentState != GameState.GAME_END) {
				List<Long> data = skill.getData();
				int[][] board = new int[PadBoardAI5x4.ROWS][PadBoardAI5x4.COLS];
				PadBoardAI5x4.copy_board(gameBoard, board);
				// PadBoardAI5x4.copy_board(board, previousBoard);
				// saveCurrentBoard();
				int len = data.size();
				Integer[] change = new Integer[len];
				for (int i = 0; i < len; ++i) {
					change[i] = PadBoardAI5x4.getOrbType(data.get(i));
				}
				setChangeColorBoard(PadBoardAI5x4.colorChangedBoard(board, change));
			}
			break;
		case SKILL_THE_WORLD:
			if (mChangeTheWorld.get()) {
				return;
			}
			if (mCurrentState == GameState.GAME_END) {
				setGameBoard(PadBoardAI5x4.generateBoard(mDropType, false),
						new IAnimationCallback() {

							@Override
							public void onAnimationFinished() {
								changeTheWorld();
							}
						});
			} else {
				changeTheWorld();
			}
			break;
		case SKILL_DROP_RATE:
		    break;
		default:
			break;
		}
	}

	private void changeTheWorld(int time) {
		TIME_CHANGE_THE_WORLD = time;
		mChangeTheWorld.set(true);
		mCountDown.set(true);
		mTimeRemain = time;

		displayProgress(true);
		
		mCTWTimer.reset();
		registerUpdateHandler(mCTWTimer);
	}

	private void changeTheWorld() {
		changeTheWorld(TIME_CHANGE_THE_WORLD);
	}

	private void initMonsterData() {
		if (!mNullAwoken) {
		    if ( mDropTime != 0 ) {
    			for (int i = 0; i < 6; ++i) {
    				MonsterInfo info = mTeam.getMember(i);
    				if (info != null) {
    					int handCount = info
    							.getTargetAwokenCount(AwokenSkill.EXTEND_TIME, false);
    					mDropTime += handCount * 500;
						mDropTime += info.getTargetAwokenCount(AwokenSkill.EXTEND_TIME_PLUS, false) * 1000;

    					int potentialCount = info.getTargetPotentialAwokenCount(MoneyAwokenSkill.EXTEND_TIME);
    					mDropTime += potentialCount * 50;
    				}
    			}
		    }
    		SharedPreferences pref = getActivity().getSharedPreferences("singleUp", Context.MODE_PRIVATE);
    		int item = pref.getInt("item"+ComboMasterApplication.getsInstance().getTargetNo(), 0);
    		SinglePowerUp singleUp = SinglePowerUp.from(item);
    		if(singleUp == SinglePowerUp.MOVE) {
    			mDropTime += 1000;
    		}
		    
		    AwokenSkill[] awokenPlus = {
		            AwokenSkill.ENHANCE_DARK, AwokenSkill.ENHANCE_FIRE, AwokenSkill.ENHANCE_HEART,
		            AwokenSkill.ENHANCE_LIGHT, AwokenSkill.ENHANCE_WATER, AwokenSkill.ENHANCE_WOOD
		    };
		    for(int i=0; i<6; ++i) {
		        mPlusOrbCount[i] = 0;
		    }
		    for(int i=0; i<6; ++i) {
		        MonsterInfo info = mTeam.getMember(i);
		        if ( info != null ) {
		            for(int j=0; j<6; ++j) {
		                mPlusOrbCount[j] += info.getTargetAwokenCount(awokenPlus[j], false);
		            }
		        }
		    }
		}
        for (int i = 0; i < 6; i+=5) {
            MonsterInfo info = mTeam.getMember(i);
            if (info != null) {
            	for(LeaderSkill skill : info.getLeaderSkill()) {
            		if(skill.getType() == LeaderSkillType.LST_EXTEND_TIME) {
            			mDropTime += skill.getData().get(0);
					} else if(skill.getType() == LeaderSkillType.LST_TIME_FIXED) {
						mTimeFixed = skill.getData().get(0);
					}
            	}
            }
        }
	}

	private void updateDropTime() {

		if (mTimeFixed == 0 && mTimeExtendTimes > 0 && mTimeExtendSkill != null) {
            int extend;
            int x = mTimeExtendSkill.getData().get(1);
            if(mTimeExtendSkill.getType() == SkillType.ST_MOVE_TIME_X) {
            	extend = (int)(mDropTimeDefault * (x-1000) / 1000.0f);
            } else {
            	extend = x;
            }
			mDropTime = mDropTimeDefault + extend;
		} else {
			mDropTime = mDropTimeDefault;
		}
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		synchronized (mRemoveList) {
			int size = mRemoveList.size();
			if (size >= RELEASE_THRESHOLD) {
				for (IEntity entity : mRemoveList) {
					if (!entity.isDisposed()) {
						entity.dispose();
					}
					detachChild(entity);
				}
				mRemoveList.clear();
			}
		}

		super.onManagedUpdate(pSecondsElapsed);
	}
	
	private void loadSpecialMode() {
	    mLvText = new Text[6];
	    IFont font = mResMgr.getFontSmall();
	    
	    for(int i=0; i<6; ++i) {
	    	MonsterInfo info = mTeam.getMember(i);
	    	if(info == null) {
	    		mLvText[i] = null;
	    		continue;
	    	}
	        Text text = new Text(0f, 0f, font, "Lv.01", vbom);
	        Sprite s = mSkill[i];
	        
	        float x = s.getX();
	        float y = s.getY();
	        float w = s.getWidthScaled();
	        float tw = text.getWidth();
	        float th = text.getHeight();
	        
	        text.setPosition(x + (w-tw)/2, y-th/2);
	        attachChild(text);
	        mLvText[i] = text;
	        info.setLv(1);
	    }
	    // calculate hp/rcv again
	    mTeam.init(Constants.MODE_SPECIAL, mStagePowerUp, (Boolean)mSettings.get("copMode"));
	    
//	    mLvMenu = new ColorMenuItemDecorator(new SpriteMenuItem(
//                MENU_SETLV, res.getTextureRegion("ic_lv.png"), vbom),
//                new Color(.5f, .5f, .5f), Color.WHITE);
//	    mLvMenu.setScale(0.8f, 0.8f);
//        addMenuItem(mLvMenu);
//        mLvMenu.setPosition(mHelpMenu.getX() + 10, mHelpMenu.getY() - mLvMenu.getHeight() - 8);
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
    
    private static final int OFFSET_X_5X4 = Constants.OFFSET_X + 5;
    private static final int OFFSET_Y_5X4 = Constants.OFFSET_Y + 15;

    Class<?> orbClz = null;
	@Override
	public void createScene() {
		loadSinglePowerUp();
	    loadSharedPreference();
		init();

		LogUtil.d("createScene");
		
		// check monster's skill with hand ( +0.5s )
		initMonsterData();
		if(mTimeFixed > 0) {
			mDropTime = mTimeFixed;
		}
		mDropTimeDefault = mDropTime;
		HandlerThread ht = new HandlerThread("ht");
		ht.start();
		handler = new NonUiHandler(ht.getLooper());

		ResourceManager res = ResourceManager.getInstance();
		res.loadGameScene();

		res.loadMusic();
		res.playMusic();
		
		mResMgr = res;
		
		SharedPreferenceUtil utils = SharedPreferenceUtil.getInstance(activity);
		
        PadBoardAI.IMAGE_TYPE type = (utils.getType() == 2) ? PadBoardAI.IMAGE_TYPE.TYPE_TOS
                : PadBoardAI.IMAGE_TYPE.TYPE_PAD;
        if (utils.getBoolean(SharedPreferenceUtil.PREF_COLOR_WEAKNESS)
                && type == PadBoardAI.IMAGE_TYPE.TYPE_PAD) {
            type = PadBoardAI.IMAGE_TYPE.TYPE_PAD_CW;
        }
		switch(type) {
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
	    float scale = getScaleRatio();
		int[] orbList = {TextureOrbs.DARK_ID, TextureOrbs.FIRE_ID,
				TextureOrbs.HEART_ID, TextureOrbs.LIGHT_ID,
				TextureOrbs.WATER_ID, TextureOrbs.WOOD_ID,
				TextureOrbs.POISON_ID, TextureOrbs.JAMMAR_ID,TextureOrbs.ENHANCED_POISON_ID,9999
				};
		for(int i=0; i<orbList.length; ++i) {
			textureOrbs.put(i, res.getTextureRegion(orbClz, orbList[i]));
			mCache[i] = new Sprite(0, 0, textureOrbs.get(i), vbom);
			mCache[i].setVisible(false);
			mCache[i].setScale(scale, scale);
			mCache[i].setZIndex(4);
			attachChild(mCache[i]);
		}

        boolean index = false;
        ITextureRegion[] textureBoard = new ITextureRegion[2];
        textureBoard[0] = res.getTextureRegion(GameUIAssets.class, GameUIAssets.BOARD_A_ID);
        textureBoard[1] = res.getTextureRegion(GameUIAssets.class, GameUIAssets.BOARD_B_ID);

		for (int j = 0; j < PadBoardAI5x4.COLS; ++j) {
			for (int i = 0; i < PadBoardAI5x4.ROWS; ++i) {
				int x = Constants.ORB_SIZE_5x4 * i;
				int y = Constants.ORB_SIZE_5x4 * j;
				
				Sprite s = new Sprite(0, 0,
						textureBoard[index ? 0 : 1], vbom);
				s.setScale(scale, scale);
				s.setPosition(x + OFFSET_X_5X4, y + OFFSET_Y_5X4);
				s.setZIndex(0);
				attachChild(s);
				boardSprite[i][j] = s;

                Sprite ns  = getSprite(gameBoard[i][j]);
				sprites[i][j] = ns;
				setPosition(i, j, x, y);
				ns.setZIndex(1);
				attachChild(ns);
				index = !index;
			}
		}


		// progress
		mProgressbar = new Rectangle(Constants.OFFSET_X,
				Constants.OFFSET_Y - 15 - 8, PROGRESS_BAR_WIDTH, 15, vbom);
		mProgressbar.setColor(0f, 1f, 0f);
		mProgressbar.setVisible(false);
		attachChild(mProgressbar);
		
        int hpY = Constants.OFFSET_Y - 32;
        mHpBackground = new Sprite(0f, 0f,
                res.getTextureRegion(GameUIAssets.class, GameUIAssets.HEART_HP_ID), vbom);
        mHpBackground.setPosition(0f, hpY);
        attachChild(mHpBackground);

        mHp = new Sprite(0f, 0f, res.getTextureRegion("hp.png"), vbom);
        mHp.setPosition(40f, hpY + 12);
        mHp.setScaleCenter(0f, 0f);
        mHp.setScale(480f, 1f);
        attachChild(mHp);

//        int hpTotal = mTeam.getHp();
        mHpText = new Text(0f, 0f, res.getFontStroke(), String.format("%.2f%%",
                100f), vbom);
        mHpText.setPosition(
                GameActivity.SCREEN_WIDTH - mHpText.getWidth() - 24, hpY + 6);
        mHpText.setColor(0.24f, 0.96f, 0.24f);
        attachChild(mHpText);

        for(int i=0; i<6; ++i) {
            Sprite s = new Sprite(0f, 0f, res.getTextureRegion(SimulateAssets.class, SimulateAssets.BIND_ID), vbom);
            s.setPosition(Constants.OFFSET_X + i * (88 + 1),
                    mProgressbar.getY() - 8 - 88);
            s.setVisible(false);
            s.setZIndex(2);
            attachChild(s);
            mBindSprite[i] = s;
        }
        
        mSwapSprite = new Sprite(0f, 0f, res.getTextureRegion(SimulateAssets.class, SimulateAssets.SWAP_ID), vbom);
        mSwapSprite.setPosition(Constants.OFFSET_X, mProgressbar.getY() - 8 - 88);
        mSwapSprite.setVisible(false);
        mSwapSprite.setAlpha(0.8f);
        mSwapSprite.setZIndex(2);
        attachChild(mSwapSprite);
        
        final int[] textureName = { GameUIAssets.S1_ID,GameUIAssets.S2_ID,
        		GameUIAssets.S3_ID,GameUIAssets.S4_ID,
        		GameUIAssets.S5_ID,GameUIAssets.S6_ID};
		List<Skill> skills = SharedPreferenceUtil.getInstance(activity)
				.getSkillList();

		for (int i = 0; i < textureName.length; ++i) {
			final Skill defaultSkill = skills.get(i);
			final MonsterInfo info = mTeam.getMember(i);
			final int teamIndex = i;
			ITextureRegion region = null;
			if (info != null) {
				LogUtil.d("load data", info.getNo());
				try {
					region = res.loadTextureFile(info.getNo() + "i.png");
				} catch (Throwable e) {
					LogUtil.e(e);
					region = null;
				}
			}
			try {
				Sprite s = new Sprite(0, 0,
						(region == null) ? res.getTextureRegion(GameUIAssets.class, textureName[i])
								: region, vbom) {
					@Override
					public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						if (isSkipState()) {
							return true;
						}
						if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
							// do this skill
							if (info != null) {
							    if ( mBindStatus[teamIndex] ) {
							        return true;
							    }
                                if(info.getActiveSkill2() == null) {
	                                List<ActiveSkill> list = info.getActiveSkill();
	                                for (ActiveSkill as : list) {
	                                    skillFired(as);
	                                }
                                } else {
                                	Observable.just(teamIndex)
                                		.observeOn(AndroidSchedulers.mainThread())
                                		.subscribe(new Action1<Integer>(){

											@Override
											public void call(Integer arg0) {
												Activity activity = getActivity();
												String[] items = activity.getResources().getStringArray(R.array.skill);
												new AlertDialog.Builder(activity)
													.setSingleChoiceItems(items, -1, new OnClickListener() {
														
														@Override
														public void onClick(DialogInterface dialog, int which) {
															dialog.dismiss();
															Observable.just(which)
																.observeOn(Schedulers.immediate())
																.subscribe(new Action1<Integer>() {
																	
																	@Override
																	public void call(Integer which) {
																		List<ActiveSkill> list;
																		if(which==0) {
											                                list = info.getActiveSkill();
																		} else {
																			list = info.getActiveSkill2();
																		}
										                                for (ActiveSkill as : list) {
										                                    skillFired(as);
										                                }
																	}
																});

														}
													})
													.create().show();
											}
                                			
                                		});
                                }
							} else {
								skillFired(defaultSkill);
							}
						}
						return true;
					}
				};
				if (region != null) {
					s.setScaleCenter(0f, 0f);
					s.setScale(0.88f);
				}
				mSkill[i] = s;
				s.setPosition(Constants.OFFSET_X + i * (88 + 1),
						mProgressbar.getY() - 8 - 88);
				registerTouchArea(s);
				attachChild(s);
			} catch (Throwable t) {
				LogUtil.e(t, t.toString());
			}

		}
		
        IMenuItem rand = new ColorMenuItemDecorator(new SpriteMenuItem(
                MENU_RESET, res.getTextureRegion(GameUIAssets.class, GameUIAssets.IC_RANDOM_ID), vbom),
                new Color(.5f, .5f, .5f), Color.WHITE);
        addMenuItem(rand);

        IMenuItem reset = new ColorMenuItemDecorator(new SpriteMenuItem(
                MENU_PREVIOUS, res.getTextureRegion(GameUIAssets.class, GameUIAssets.IC_PREVIOUS_ID), vbom),
                new Color(.5f, .5f, .5f), Color.WHITE);
        addMenuItem(reset);

        IMenuItem edit = new ColorMenuItemDecorator(new SpriteMenuItem(
                MENU_EDIT, res.getTextureRegion(GameUIAssets.class, GameUIAssets.IC_EDIT_ID), vbom),
                new Color(.5f, .5f, .5f), Color.WHITE);
        addMenuItem(edit);

		// IMenuItem load = new ColorMenuItemDecorator(new
		// SpriteMenuItem(MENU_LOAD, res.getTextureRegion("ic_image.png"),
		// vbom), new Color(.5f, .5f, .5f), Color.WHITE);
		// addMenuItem(load);
        IMenuItem replay = new ColorMenuItemDecorator(new SpriteMenuItem(
                MENU_REPLAY, res.getTextureRegion(GameUIAssets.class, GameUIAssets.IC_PLAY_ID), vbom),
                new Color(.5f, .5f, .5f), Color.WHITE);
        addMenuItem(replay);

        IMenuItem result = new ColorMenuItemDecorator(new SpriteMenuItem(
                MENU_RESULT, res.getTextureRegion(GameUIAssets.class, GameUIAssets.IC_RESULT_ID), vbom),
                new Color(.5f, .5f, .5f), Color.WHITE);
        addMenuItem(result);

        IMenuItem settings = new ColorMenuItemDecorator(new SpriteMenuItem(
                MENU_SETTINGS, res.getTextureRegion(GameUIAssets.class, GameUIAssets.IC_SETTINGS_ID), vbom),
                new Color(.5f, .5f, .5f), Color.WHITE);
        addMenuItem(settings);
		 
		// buildAnimations();

		float startY = Constants.BANNER_HEIGHT + 16;
		rand.setPosition(16, startY + 16);
		reset.setPosition(rand.getX() + rand.getWidth() + 16, startY + 16);
		edit.setPosition(reset.getX() + reset.getWidth() + 16, startY + 16);
		// load.setPosition(edit.getX() + edit.getWidth() + 16, 16);
		replay.setPosition(edit.getX() + edit.getWidth() + 16, startY + 16);
		result.setPosition(replay.getX() + replay.getWidth() + 16, startY + 16);
		settings.setPosition(result.getX() + result.getWidth() + 16, startY + 16);
		
		float menuHeight = rand.getHeight() + 8;
		initEditSprites(menuHeight);

		IFont font = res.getFont();
		IFont smallfont = res.getFontSmall();

		mComboText = new Text(0, 0, font, String.valueOf(mCombo.get())
				+ " Combo", vbom);
		float offset = GameActivity.SCREEN_WIDTH - Constants.OFFSET_X
				- mComboText.getWidth() * 1.2f;

		float h = menuHeight + 15 + 16 + startY; // 15 -> progress height 16 ->
													// padding
		mComboText.setPosition(offset, h);
		mComboText.setZIndex(3);
		mComboText.setColor(Color.WHITE);

		mPowerUpRawText = activity.getString(R.string.power_up_text);
		mPowerUpText = new Text(0, 0, res.getFont(), String.format(
				mPowerUpRawText, 0, "0.00"), res.getVBOM());
		mPowerUpText.setZIndex(3);
		mPowerUpText.setScaleCenter(0f, 0f);
		mPowerUpText.setScale(.5f, .5f);

		mStepText = " " + activity.getString(R.string.step);
		mStepCountText = new Text(0, 0, font, 999 + mStepText, vbom);
		mStepCountText.setPosition(offset, h + mComboText.getHeight() + 16);
		mStepCountText.setZIndex(3);
		mStepCountText.setColor(Color.WHITE);

		mSecondText = activity.getString(R.string.second);
		String sec = "00.00 " + mSecondText;
		mTimerText = new Text(0, 0, font, sec, vbom);
		mTimerText.setPosition(offset, h + mComboText.getHeight()
				+ mStepCountText.getHeight() + 32);
		mTimerText.setZIndex(3);
		mTimerText.setColor(Color.WHITE);

		attachChild(mComboText);
		attachChild(mStepCountText);
		attachChild(mTimerText);

		mComboText.setText("0 Combo");
		mStepCountText.setText(0 + mStepText);
		
        // special menu ...
//        mHelpMenu = new ColorMenuItemDecorator(new SpriteMenuItem(
//                MENU_HELP, res.getTextureRegion("ic_help.png"), vbom),
//                new Color(.5f, .5f, .5f), Color.WHITE);
//        addMenuItem(mHelpMenu);
//        mHelpMenu.setPosition(mStepCountText.getX() + mHelpMenu.getWidth() * 2, mStepCountText.getY() - 10);

		mCheckBox[0] = new Sprite(0, 0,
				res.getTextureRegion(GameUIAssets.class, GameUIAssets.CHECKBOX_OFF_ID), vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (isSkipState()) {
					return true;
				}
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					toggleShowPath();
				}
				return true;
			}
		};
		mCheckBox[1] = new Sprite(0, 0,
				res.getTextureRegion(GameUIAssets.class, GameUIAssets.CHECKBOX_ON_ID), vbom);
		registerTouchArea(mCheckBox[0]);
		
		mCheckBox[0].setZIndex(2);
		mCheckBox[1].setZIndex(0);
		
		for (int i = 0; i < 2; ++i) {
			mCheckBox[i].setScale(1.15f);
			mCheckBox[i].setPosition(Constants.OFFSET_X + 32, h);
			attachChild(mCheckBox[i]);
		}
		if (mShowPath.get()) {
			mCheckBox[0].setVisible(false);
		} else {
			mCheckBox[1].setVisible(false);
		}

		String showPath = activity.getString(R.string.show_path_if_available);
		mShowPathText = new Text(0, 0, smallfont, showPath, vbom) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                    float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (isSkipState()) {
                    return true;
                }
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    toggleShowPath();
                }
                return true;
            }
		};
//		mShowPathText.setScale(0.5f);
		mShowPathText.setPosition(mCheckBox[0].getX() + 32, mCheckBox[0].getY());
		registerTouchArea(mShowPathText);
		attachChild(mShowPathText);
		
		float checkBoxHeight = mCheckBox[0].getHeight() * 1.5f;
		
		mSkillCheckBox[0] = new Sprite(0f, 0f, 
				res.getTextureRegion(GameUIAssets.class, GameUIAssets.CHECKBOX_OFF_ID), vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (isSkipState()) {
					return true;
				}
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					toggleSkillFired();
				}
				return true;
			}
		};
		mSkillCheckBox[1] = new Sprite(0, 0,
				res.getTextureRegion(GameUIAssets.class, GameUIAssets.CHECKBOX_ON_ID), vbom);
		registerTouchArea(mSkillCheckBox[0]);
		
		mSkillCheckBox[0].setZIndex(2);
		mSkillCheckBox[1].setZIndex(0);
		
		for (int i = 0; i < 2; ++i) {
			mSkillCheckBox[i].setScale(1.15f);
			mSkillCheckBox[i].setPosition(Constants.OFFSET_X + 32, h + checkBoxHeight + 12);
			attachChild(mSkillCheckBox[i]);
		}
		String skillFired = activity.getString(R.string.str_skill_fired);
		mSkillFiredText = new Text(0, 0, smallfont, skillFired, vbom) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                    float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (isSkipState()) {
                    return true;
                }
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    toggleSkillFired();
                }
                return true;
            }
		};
		mSkillFiredText.setPosition(mCheckBox[0].getX() + 32, mSkillCheckBox[0].getY());
//		mSkillFiredText.setScale(0.5f);
		registerTouchArea(mSkillFiredText);
		attachChild(mSkillFiredText);
		
		if (mSkillFired.get()) {
            mSkillCheckBox[0].setVisible(false);
        } else {
            mSkillCheckBox[1].setVisible(false);
        }
		
        mDropCheckBox[0] = new Sprite(0f, 0f, 
                res.getTextureRegion(GameUIAssets.class, GameUIAssets.CHECKBOX_OFF_ID), vbom) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                    float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (isSkipState()) {
                    return true;
                }
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    toggleDroppingMode();
                }
                return true;
            }
        };
        mDropCheckBox[1] = new Sprite(0, 0,
                res.getTextureRegion(GameUIAssets.class, GameUIAssets.CHECKBOX_ON_ID), vbom);
        registerTouchArea(mDropCheckBox[0]);
        
        mDropCheckBox[0].setZIndex(2);
        mDropCheckBox[1].setZIndex(0);
        
        for (int i = 0; i < 2; ++i) {
            mDropCheckBox[i].setScale(1.15f);
            mDropCheckBox[i].setPosition(Constants.OFFSET_X + 32, h + (checkBoxHeight + 12)*2);
            attachChild(mDropCheckBox[i]);
        }
        
        String continueDrop = activity.getString(R.string.str_continue_drop);
        mContinueDropText = new Text(0, 0, smallfont, continueDrop, vbom) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                    float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (isSkipState()) {
                    return true;
                }
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    toggleDroppingMode();
                }
                return true;
            }
        };
        mContinueDropText.setPosition(mCheckBox[0].getX() + 32, mDropCheckBox[0].getY());
//        mContinueDropText.setScale(0.5f);
        registerTouchArea(mContinueDropText);
        attachChild(mContinueDropText);
        
        if (mContinueDrop.get()) {
        	mDropCheckBox[0].setVisible(false);
        } else {
        	mDropCheckBox[1].setVisible(false);
        }
		
        // load special mode ( Lv ) , need run after character sprite and ic_help ready
        if ( ComboMasterApplication.getsInstance().getGameMode() == Constants.MODE_SPECIAL ) {
            loadSpecialMode();
        } else {
            // reset level
            mTeam.load();
        }
        
		sortChildren();

		setOnSceneTouchListener(this);
		setOnMenuItemClickListener(this);

	}

	private boolean isSkipState() {
		return mCurrentState == GameState.GAME_REPLAY
				|| mCurrentState == GameState.GAME_ANIMATION
				|| mCurrentState == GameState.GAME_MODIFY_BOARD
				|| mChangeTheWorld.get();
	}

	private void initEditSprites(float menuHeight) {
        float offsetX = (GameActivity.SCREEN_WIDTH - 6 * textureOrbs.get(0)
                .getWidth()) / 2f;
		float offsetY = menuHeight * 1.65f + 20;
		float scale = .7f;
		for (int i = 0; i < 2; ++i) {
			for (int j = 0; j < 4; ++j) {
				final int pos = i * 4 + j;
				final Sprite s = new Sprite(0, 0, textureOrbs.get(pos), vbom) {
					@Override
					public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
							mEditSelect = pos;
							for (int i = 0; i < EDIT_SPRITE_SIZE; ++i) {
								if (i != pos) {
									mSetByHandSprite[i].setAlpha(.5f);
								} else {
									mSetByHandSprite[i].setAlpha(1f);
								}
							}
						}
						return true;
					}
				};
				s.setScale(scale);
				s.setAlpha(.5f);
				s.setPosition(offsetX + j * s.getWidth(),
						offsetY + s.getHeight() * i);
				mSetByHandSprite[pos] = s;
			}
		}
		
        mEditOkSprite = new Sprite(0f, 0f,
                mResMgr.getTextureRegion(GameUIAssets.class, GameUIAssets.IC_OK_ID), vbom) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                    float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    onEditModeFinish();
                }
                return true;
            }
        };
        mEditOkSprite.setScale(0.85f);
        mEditOkSprite.setPosition(mSetByHandSprite[7].getX() + 8, mSetByHandSprite[7].getY() + mEditOkSprite.getHeight() + 8);

        mSetByHandSprite[8] = new Sprite(0f, 0f,
                mResMgr.getTextureRegion(orbClz, TextureOrbs.ENHANCED_POISON_ID), vbom) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                    float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    mEditSelect = 8;
                    for (int i = 0; i < EDIT_SPRITE_SIZE; ++i) {
                        if (i != 8) {
                            mSetByHandSprite[i].setAlpha(.5f);
                        } else {
                            mSetByHandSprite[i].setAlpha(1f);
                        }
                    }
                }
                return true;
            }
        };
        mSetByHandSprite[8].setAlpha(.5f);
        mSetByHandSprite[8].setScale(.7f);
        mSetByHandSprite[8].setPosition(mSetByHandSprite[6].getX(),
                mSetByHandSprite[6].getY() + mSetByHandSprite[6].getHeight() - 20);
        
        mSetByHandSprite[9] = new Sprite(0f, 0f,
                mResMgr.getTextureRegion(orbClz, TextureOrbs.PLUS_ID), vbom) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                    float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    mEditSelect = 9;
                    for (int i = 0; i < EDIT_SPRITE_SIZE; ++i) {
                        if (i != 9) {
                            mSetByHandSprite[i].setAlpha(.5f);
                        } else {
                            mSetByHandSprite[i].setAlpha(1f);
                        }
                    }
                }
                return true;
            }
        };
        mSetByHandSprite[9].setAlpha(.5f);
        mSetByHandSprite[9].setPosition(mSetByHandSprite[4].getX() + 22,
                mSetByHandSprite[4].getY() + mSetByHandSprite[4].getHeight());

        mSetByHandSprite[10] = new Sprite(0f, 0f,
                mResMgr.getTextureRegion(orbClz, TextureOrbs.LOCK_ID), vbom) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                    float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    mEditSelect = 10;
                    for (int i = 0; i < EDIT_SPRITE_SIZE; ++i) {
                        if (i != 10) {
                            mSetByHandSprite[i].setAlpha(.5f);
                        } else {
                            mSetByHandSprite[i].setAlpha(1f);
                        }
                    }
                }
                return true;
            }
        };
        mSetByHandSprite[10].setAlpha(.5f);
        mSetByHandSprite[10].setPosition(mSetByHandSprite[5].getX() + 16,
                mSetByHandSprite[5].getY() + mSetByHandSprite[5].getHeight() + 8);
        mSetByHandSprite[10].setVisible(false);
		mSetByHandSprite[0].setAlpha(1f);

		mOpenFileSprite = new Sprite(0f, 0f,
				mResMgr.getTextureRegion(GameUIAssets.class, GameUIAssets.CHOOSE_PHOTO_ID), vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					try {
		                Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
		                pickIntent.setType("image/*");
		                pickIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
		                activity.startActivityForResult(pickIntent, REQUEST_PICK_PHOTO);
					} catch(Throwable e) {
	                       activity.runOnUiThread(new Runnable() {

	                            @Override
	                            public void run() {
	                                Toast.makeText(activity, activity.getString(R.string.str_need_album_app), Toast.LENGTH_LONG).show();
	                            }
	                       });
					}
				}
				return true;
			}
		};

		float x = mSetByHandSprite[3].getX() + mSetByHandSprite[3].getWidth()
				+ 10;
		mOpenFileSprite.setPosition(x, menuHeight + 24);

	}
	
	private void setPosition(int ox, int oy, float x, float y) {
		sprites[ox][oy].setPosition(x + OFFSET_X_5X4, y
				+ OFFSET_Y_5X4);
	}

	private void init() {
	    ComboMasterApplication instance = ComboMasterApplication.getsInstance();
	    gameBoard = PadBoardAI5x4.generateBoardWithPlus(mDropType, false, mPlusOrbCount);
	    
		mStack = new BoardStack5x4(gameBoard);
		mReplay = new GameReplay5x4();

		mTeam = instance.getTargetTeam();
		removeN = getMoreRemove(mTeam);
		mNullAwoken = instance.isAwokenNulled();
        mStagePowerUp = instance.getPowerUp();

        mSettings.put("copMode", instance.isCopMode());
        mSettings.put("nullAwoken", mNullAwoken);
        mSettings.put("powerUp", mStagePowerUp);

		
		changeState(GameState.GAME_RUN);
	}

    private int getMoreRemove(TeamInfo team) {
        MonsterInfo leader = team.getMember(0);
        MonsterInfo friend = team.getMember(5);
        
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
		if (mCurrentState == GameState.GAME_MODIFY_BOARD) {
			activity.getEngine().runOnUpdateThread(new Runnable() {

				@Override
				public void run() {
					onEditModeFinish();
				}
			});

		} else {
			SceneManager.getInstance().setScene(GameMenuScene.class);
		}
	}

	@Override
	public void disposeScene() {
		mResMgr.unloadGameScene();
		
		for (Sprite[] ss : sprites) {
			for (Sprite s : ss) {
				if (!s.isDisposed()) {
					s.dispose();
				}
			}
		}
		mSwapSprite.dispose();
		
		if ( mLvText != null ) {
		    for(Text text : mLvText) {
		        if (text != null && !text.isDisposed()) {
		            text.dispose();
		        }
		    }
		}
		
		for (Sprite[] ss : boardSprite) {
			for (Sprite s : ss) {
				if (!s.isDisposed()) {
					s.dispose();
				}
			}
		}
		for (Sprite s : mSetByHandSprite) {
			if (!s.isDisposed()) {
				s.dispose();
			}
		}

		for(int i=0; i<6; ++i) {
		    mBindSprite[i].dispose();
		}
		
		for (int i = 0; i < 6; ++i) {
			mSkill[i].dispose();
			unregisterTouchArea(mSkill[i]);
		}

		for (int i = 0; i < 2; ++i) {
			mCheckBox[i].dispose();
			mSkillCheckBox[i].dispose();
		}
		unregisterTouchArea(mSkillFiredText);
		unregisterTouchArea(mSkillCheckBox[0]);
		unregisterTouchArea(mShowPathText);
		unregisterTouchArea(mCheckBox[0]);
		setOnSceneTouchListener(null);
		setOnMenuItemClickListener(null);

		mResMgr.unloadMusic();
		handler.getLooper().quit();
	}

	int currentX = -1;
	int currentY = -1;

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		switch (mCurrentState) {
		case GAME_RUN:
			if (onGameRun(pScene, pSceneTouchEvent) == false) {
				return super.onSceneTouchEvent(pScene, pSceneTouchEvent);
			}
			break;
		case GAME_MODIFY_BOARD:
			onEditBoard(pScene, pSceneTouchEvent);
			break;
		case GAME_REPLAY:
			// do nothing
			break;
		default:
			break;
		}

		return super.onSceneTouchEvent(pScene, pSceneTouchEvent);
	}

	// TODO: check thread
	private void onEditModeFinish() {
		LogUtil.d("onEditModeFinish");
		List<IEntity> detachList = new ArrayList<IEntity>();
		for (Sprite s : mSetByHandSprite) {
			unregisterTouchArea(s);
			detachList.add(s);
		}
		unregisterTouchArea(mEditOkSprite);
		detachList.add(mEditOkSprite);
		unregisterTouchArea(mOpenFileSprite);
		detachList.add(mOpenFileSprite);
		detach(detachList);

		LogUtil.d("if file loaded, release it");
		// if load big file... release it for saving memory
		if (bigFileLoaded) {
			bigFileLoaded = false;
			if (!mOpenFileSprite.isDisposed()) {
				mOpenFileSprite.dispose();
			}
			float x = mOpenFileSprite.getX();
			float y = mOpenFileSprite.getY();
			mOpenFileSprite = new Sprite(0f, 0f, mResMgr
					.getTextureRegion(GameUIAssets.class, GameUIAssets.CHOOSE_PHOTO_ID), vbom) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
						try {
			                Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
			                pickIntent.setType("image/*");
			                pickIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
			                activity.startActivityForResult(pickIntent, REQUEST_PICK_PHOTO);
						} catch(Throwable e) {
		                       activity.runOnUiThread(new Runnable() {

		                            @Override
		                            public void run() {
		                                Toast.makeText(activity, activity.getString(R.string.str_need_album_app), Toast.LENGTH_LONG).show();
		                            }
		                       });
						}
					}
					return true;
				}
			};
			mOpenFileSprite.setPosition(x, y);
		}

		registerTouchArea(mCheckBox[0]);
		registerTouchArea(mSkillCheckBox[0]);
		registerTouchArea(mDropCheckBox[0]);
		registerTouchArea(mShowPathText);
		registerTouchArea(mSkillFiredText);
		registerTouchArea(mContinueDropText);
        
		List<IEntity> attachList = new ArrayList<IEntity>();
		for (int i = 0; i < 2; ++i) {
			attachList.add(mCheckBox[i]);
			attachList.add(mSkillCheckBox[i]);
			attachList.add(mDropCheckBox[i]);
		}
		attachList.add(mContinueDropText);
		attachList.add(mShowPathText);
		attachList.add(mSkillFiredText);
		attachList.add(mComboText);
		attachList.add(mStepCountText);
		attachList.add(mTimerText);
		
		attach(attachList);
		
//		mHelpMenu.setVisible(true);
		// add to stack if modified
		mStack.push(gameBoard);

		changeState(GameState.GAME_RUN);
	}

	// TODO: check thread
	private void onEditBoard(Scene pScene, TouchEvent pSceneTouchEvent) {
		LogUtil.d("onSceneTouched-onEditBoard");
		int event = pSceneTouchEvent.getAction();

		if (event == TouchEvent.ACTION_DOWN || event == TouchEvent.ACTION_MOVE) {
			float px = pSceneTouchEvent.getX();
			float py = pSceneTouchEvent.getY();
			float x = px - Constants.OFFSET_X;
			float y = py - Constants.OFFSET_Y;
			int indexX = (int) x / Constants.ORB_SIZE_5x4;
			int indexY = (int) y / Constants.ORB_SIZE_5x4;

			if (PadBoardAI5x4.isValid(indexX, indexY)) {
				if (mEditSelect == 9) {
					int orb = gameBoard[indexX][indexY];
					if (orb < 10 && orb != 6 && orb != 7 && orb != 8) {
						gameBoard[indexX][indexY] += 10;
						Sprite s = getSprite(orb + 10);
						s.setZIndex(1);

						Sprite temp = sprites[indexX][indexY];
						addToDetachList(temp);
						attach(s);
						sprites[indexX][indexY] = s;
						setPosition(indexX, indexY,
								indexX * Constants.ORB_SIZE_5x4, indexY
										* Constants.ORB_SIZE_5x4);
					}
				} else if (mEditSelect == 10) {
				} else if (gameBoard[indexX][indexY] != mEditSelect) {
					gameBoard[indexX][indexY] = mEditSelect;
					Sprite s = getSprite(mEditSelect);
					s.setZIndex(1);

					Sprite temp = sprites[indexX][indexY];
					addToDetachList(temp);
					attach(s);
					sprites[indexX][indexY] = s;
					setPosition(indexX, indexY, indexX * Constants.ORB_SIZE_5x4,
							indexY * Constants.ORB_SIZE_5x4);
				}
			}

		}
	}

	private void displayProgress(boolean display) {
	    mProgressbar.setVisible(display);
	    
	    mHp.setVisible(!display);
	    mHpBackground.setVisible(!display);
	    mHpText.setVisible(!display);
	}
	
	private boolean onGameRun(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (mCurrentState == GameState.GAME_ANIMATION) {
			LogUtil.d("skip scene touch due to state=GAME_ANIMATION");
			return false;
		}
		LogUtil.d("onSceneTouched");
		float px = pSceneTouchEvent.getX();
		float py = pSceneTouchEvent.getY();
		float x = px - Constants.OFFSET_X;
		float y = py - Constants.OFFSET_Y;
		int indexX = (int) x / Constants.ORB_SIZE_5x4;
		int indexY = (int) y / Constants.ORB_SIZE_5x4;
		boolean specialCase = false;
		if (currentX != -1) {
			if (y<0) {
				indexY = 0;
				specialCase = true;
			} else if(indexY>=PadBoardAI5x4.COLS) {
				indexY = PadBoardAI5x4.COLS-1;
				specialCase = true;
			} 
			if (x<0) {
				indexX = 0;
				specialCase = true;
			} else if(indexX>=PadBoardAI5x4.ROWS) {
				indexX = PadBoardAI5x4.ROWS-1;
				specialCase = true;
			}
		}
		
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			if (PadBoardAI5x4.isValid(indexX, indexY)) {
				currentX = indexX;
				currentY = indexY;

				orbInHand = getCacheSprite(gameBoard[indexX][indexY]%10);
				orbInHand.setZIndex(3);
				orbInHand.setPosition(x - 48 + Constants.OFFSET_X, y - 48
						+ Constants.OFFSET_Y);
				orbInHand.setVisible(true);

				sprites[currentX][currentY].setAlpha(0.5f);

				resetCombo();
				untouchableWhenMove();
			}
		} else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
			if (PadBoardAI5x4.isValid(currentX, currentY) && orbInHand != null) {
				// setPosition(currentX, currentY, x-48, y-48);
				orbInHand.setPosition(x - 48 + Constants.OFFSET_X, y - 48
						+ Constants.OFFSET_Y);
			}
			
			// check orb is swap or not
			if (PadBoardAI5x4.isValid(currentX, currentY)
					&& PadBoardAI5x4.isValid(indexX, indexY)
					&& (indexX != currentX || indexY != currentY)) {
            	// if position more than 2, skip it...
//            	if ( Math.abs(indexX - currentX) >= 2 || Math.abs(indexY - currentY) >= 2 ) {
//            		return true;
//            	}
                Sprite s = sprites[indexX][indexY];
                if (!specialCase && !s.contains(px, py)) {
                    return true;
                }
            	
				// if not start... start time calculating
				if (!mCountDown.get()) {
					mCountDown.set(true);
					mTimeRemain = mDropTime;
					// PadBoardAI5x4.copy_board(gameBoard, previousBoard);
					mStack.push(gameBoard);

					mReplay.start(gameBoard);
					mReplay.add(RowCol.make(currentX, currentY));

					if (mDropTime != 0) { // 0 -> inf mode
						mTimer.reset();
						displayProgress(true);
						registerUpdateHandler(mTimer);
					}
				}
				mReplay.add(RowCol.make(indexX, indexY));
				swapOrb(indexX, indexY, currentX, currentY);
				currentX = indexX;
				currentY = indexY;
				mStepCountText.setText((mReplay.size()) + mStepText);
			}
		} else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			if (mChangeTheWorld.get() == false) {
				if (mCountDown.get() && PadBoardAI5x4.isValid(currentX, currentY)) {
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
			for (int i = 0; i < mSkill.length; ++i) {
				unregisterTouchArea(mSkill[i]);
			}
			setOnMenuItemClickListener(null);
		}
	}

	private void setSkillTouchable() {
		if (!mSkillTouchable.get()) {
			mSkillTouchable.set(true);
			for (int i = 0; i < mSkill.length; ++i) {
				registerTouchArea(mSkill[i]);
			}
			setOnMenuItemClickListener(this);
		}
	}

	private void resetCombo() {
		// start calculating from zero
		mCombo.set(0);
		mComboText.setText("0 Combo");

		mStepCountText.setText(0 + mStepText);

		mTimerText.setText("00.00 " + mSecondText);

		mScoreBoard.clear();
	}

	// TODO: check thread
	private void onTouchUpWhenCTW() {
		if (orbInHand != null) {
			//detachChild(orbInHand);
			orbInHand.setVisible(false);
			orbInHand = null;
		}
		if (PadBoardAI5x4.isValid(currentX, currentY)) {
			sprites[currentX][currentY].setAlpha(1.0f);
		}

		currentX = -1;
		currentY = -1;
	}

	// TODO: check thread
	private void onTouchUpReset() {
		LogUtil.d("onTouchUpReset()");
		if (orbInHand != null) {
			orbInHand.setVisible(false);
			orbInHand = null;
		}

		if (PadBoardAI5x4.isValid(currentX, currentY)) {
			sprites[currentX][currentY].setAlpha(1.0f);
		}
		currentX = -1;
		currentY = -1;
		
		displayProgress(false);
		mChangeTheWorld.set(false);
		setSkillTouchable();
		
		if ( mCountDown.get() == false ) {
			LogUtil.d("run twice @ reset?");
			return ;
		}
		mCountDown.set(false);



	}

	private void onTimeUp() {
		LogUtil.d("onTimeUp()");
		if (!mCountDown.get()) {
			// already reset
			LogUtil.e("cannot run twice");
			return ;
		}
		
		changeState(GameState.GAME_ANIMATION);

		if (mChangeTheWorld.get()) {
			unregisterUpdateHandler(mCTWTimer);
		} else {
			unregisterUpdateHandler(mTimer);
		}
		onTouchUpReset();

		mProgressbar.setWidth(PROGRESS_BAR_WIDTH);
		mProgressbar.setColor(0f, 1f, 0f);

		mStepCountText.setText((mReplay.size()) + mStepText);

		mPreviousSkill = mPowerUpSkill;

		findMatches();
	}

	private void findMatches() {
		LogUtil.d("findMatches()");
		handler.post(new Runnable() {

			@Override
			public void run() {

				final MatchBoard5x4 matchBoard = PadBoardAI5x4.findMatches(gameBoard, removeN);
                if(mScoreBoard.size() == 0 && mAddComboSkill != null) {
                	ArrayList<RowCol> list = new ArrayList<RowCol>();
                	list.add(RowCol.make(0, 0));
                	list.add(RowCol.make(0, 1));
                	list.add(RowCol.make(0, 2));
                	
                	int addition = mAddComboSkill.getData().get(1);
                	List<Match5x4> match54 = new ArrayList<Match5x4>();
                	for(int i=0; i<addition; ++i) {
                		match54.add(Match5x4.make(9, 3, 0, list));
                	}
                	mCombo.set(addition);
                	mScoreBoard.addAll(match54);
                }
				Message.obtain(handler, NonUiHandler.MSG_REMOVAL, 0, 0,
						matchBoard).sendToTarget();
			}
		});

	}


	private float HALF_ORB_SIZE_PLUS_1414 = Constants.HALF_ORB_SIZE_5x4 * 1.41421356237f;
	private void swapOrb(int nx, int ny, int cx, int cy) {
		// setPosition(nx, ny, cx*ORB_SIZE, cy*ORB_SIZE);
		final float toX = cx * Constants.ORB_SIZE_5x4 + OFFSET_X_5X4;
		final float toY = cy * Constants.ORB_SIZE_5x4 + OFFSET_Y_5X4;
		float fromX = nx * Constants.ORB_SIZE_5x4 + OFFSET_X_5X4;
		float fromY = ny * Constants.ORB_SIZE_5x4 + OFFSET_Y_5X4;

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
        sprites[cx][cy] = sprites[nx][ny];
        sprites[nx][ny] = s;

        int current = gameBoard[cx][cy];
        gameBoard[cx][cy] = gameBoard[nx][ny];
        gameBoard[nx][ny] = current;

        mResMgr.playSwapSound();
	}
//	private void swapOrb(int nx, int ny, int cx, int cy) {
//		// setPosition(nx, ny, cx*ORB_SIZE_5x4, cy*ORB_SIZE_5x4);
//		float toX = cx * Constants.ORB_SIZE_5x4 + Constants.OFFSET_X;
//		float toY = cy * Constants.ORB_SIZE_5x4 + Constants.OFFSET_Y;
//		float fromX = nx * Constants.ORB_SIZE_5x4 + Constants.OFFSET_X;
//		float fromY = ny * Constants.ORB_SIZE_5x4 + Constants.OFFSET_Y;
//		sprites[nx][ny].registerEntityModifier(new MoveModifier(
//				Constants.SECOND_SWAP, fromX, toX, fromY, toY));
//		sprites[cx][cy].setPosition(fromX, fromY);
//
//		Sprite s = sprites[cx][cy];
//		sprites[cx][cy] = sprites[nx][ny];
//		sprites[nx][ny] = s;
//
//		int current = gameBoard[cx][cy];
//		gameBoard[cx][cy] = gameBoard[nx][ny];
//		gameBoard[nx][ny] = current;
//	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		int id = pMenuItem.getID();
		boolean isSkipState = isSkipState();
		if (isSkipState && id != MENU_RESET && id != MENU_PREVIOUS) {
			LogUtil.d("skip menu item clicked");
			return false;
		}
		LogUtil.d("onMenuItemClicked()");
		switch (id) {
		case MENU_RESET:
			if (!isSkipState) {
				if (GameState.GAME_END != mCurrentState
						&& GameState.GAME_ANIMATION != mCurrentState) {
					mStack.push(gameBoard);
				}
				setGameBoard(PadBoardAI5x4.generateBoardWithPlus(mDropType, false, mPlusOrbCount));
			} else {
				mForceReset.set(true);
			}
			break;
		case MENU_PREVIOUS:
			if (!isSkipState) {
				setGameBoard(mStack.pop());
			} else {
				mForceReset.set(true);
			}
			break;
		case MENU_EDIT:
			if (GameState.GAME_END != mCurrentState) {
				mStack.push(gameBoard);
			}
			initEditMode();

			break;
		case MENU_LOAD:
			try {
                Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pickIntent.setType("image/*");
                pickIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                activity.startActivityForResult(pickIntent, REQUEST_PICK_PHOTO);
			} catch(Throwable e) {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(activity, activity.getString(R.string.str_need_album_app), Toast.LENGTH_LONG).show();
                    }
               });
			}
			break;
		case MENU_REPLAY:
			if (mReplay.hasReplay()) {
				if (!PadBoardAI5x4.isSameBoard(gameBoard, mStack.peek())) {
					mStack.push(mReplay.getBoard());
				}
				changeState(GameState.GAME_REPLAY);
				resetCombo();
				resetDrawingPath();
				setGameBoardWOAnimation(mReplay.getBoard());
				handler.sendEmptyMessageDelayed(NonUiHandler.MSG_REPLAY_INIT,
						(long) (1000 * (Constants.SECOND_REFRESH + 1f)));
			}
			break;
		case MENU_SETTINGS:
//			activity.startActivity(new Intent(activity,
//					SettingsFragmentActivity.class));
			showSettingsDialog();
			
			break;
		case MENU_RESULT:
			showScore(true);
			break;
		case MENU_HELP:
		    activity.runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    DialogUtil.getTinyUrlDialog(activity, gameBoard, mTeam, mPowerUpSkill, mSettings).show();
                }
            });
		    
			break;
		case MENU_SETLV:
		    activity.runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    mLvDialog = DialogUtil.getSetLvDialog(activity);
                    mLvDialog.setTeamInfo(mTeam);
                    mLvDialog.setCallback(new SetLvDialog.IResultCallback() {
                        
                        @Override
                        public void onResult() {
                            updateLevelText();
                        }
                    });
                    mLvDialog.show();
                }
            });
		    break;
		}
		return false;
	}
	
	private void updateLevelText() {
        engine.runOnUpdateThread(new Runnable() {
            
            @Override
            public void run() {
                for(int i=0; i<6; ++i) {
                   MonsterInfo info = mTeam.getMember(i);
                   if ( info == null ) {
                       continue;
                   }
                   mLvText[i].setText(String.format("Lv.%02d", info.getLv()));
                }
                mTeam.init(Constants.MODE_SPECIAL, mStagePowerUp, (Boolean)mSettings.get("copMode"));
            }
        });
	}
	
	private void showSettingsDialog() {
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if ( mSettingDialog == null ) {
				    Map<String, Object> data = new HashMap<String, Object>();
				    data.put("hp", mTeam.getHp());
				    
					mSettingDialog = new SettingsDialog(activity);
					mSettingDialog.setData(data);
					mSettingDialog.initViews(true);
					mSettingDialog.setCallback(new ISettingsCallback() {
						
						@Override
						public void onResult(Map<String, String> data) {
						    float seconds = Float.parseFloat(data.get("seconds"));
						    String bind = data.get("bind");
						    int old = mSwapIndex;
						    mSwapIndex = Integer.parseInt(data.get("swap"));
						    
						    if ( old != mSwapIndex ) {
					            mSwapSprite.setPosition(Constants.OFFSET_X + mSwapIndex * (88 + 1),
					                    mProgressbar.getY() - 8 - 88);
					            if ( mSwapIndex != 0 ) {
					            	mSwapSprite.setVisible(true);
					            	
					            	mSkill[0].setPosition(Constants.OFFSET_X + mSwapIndex * (88 + 1),
						                    mProgressbar.getY() - 8 - 88);
					            	mSkill[mSwapIndex].setPosition(Constants.OFFSET_X,
						                    mProgressbar.getY() - 8 - 88);
					            	if ( old != 0 ) {
						            	mSkill[old].setPosition(Constants.OFFSET_X + old * (88 + 1),
							                    mProgressbar.getY() - 8 - 88);
					            	}
					            } else {
					            	mSwapSprite.setVisible(false);
					            	
					            	mSkill[old].setPosition(Constants.OFFSET_X + old * (88 + 1),
						                    mProgressbar.getY() - 8 - 88);
					            	mSkill[0].setPosition(Constants.OFFSET_X,
						                    mProgressbar.getY() - 8 - 88);
					            }
						    }

						    if (data.containsKey("drop0")) {
						        final ActiveSkill skill = new ActiveSkill(SkillType.ST_DROP_RATE);
						        int droprate = Integer.parseInt(data.get("droprate"));
						        int type0 = Integer.parseInt(data.get("drop0"));
						        
						        skill.addData(99, type0, droprate);
						        
						        if (data.containsKey("drop1")) {
                                    int type1 = Integer.parseInt(data.get("drop1"));
                                    skill.addData(type1, droprate);
                                }
						        
						        engine.runOnUpdateThread(new Runnable() {
                                    
                                    @Override
                                    public void run() {
                                        skillFired(skill);
                                    }
                                });
						        
						    } else {
						        engine.runOnUpdateThread(new Runnable() {
                                    
                                    @Override
                                    public void run() {
                                        removeDropRateSkill();
                                    }
                                });
						        
						    }

						    
							if (seconds == 0f) { // don't modify
								mDropTime = mDropTimeDefault;
							} else {
								mDropTime = (int)(seconds * 1000);
							}
							for(int i=0; i<6; ++i) {
							    mBindStatus[i] = bind.charAt(i) == '1';
							    if ( mBindStatus[i] ) {
							        mBindSprite[i].setVisible(true);
							    } else {
							        mBindSprite[i].setVisible(false);
							    }
							}
							
                            final Boolean isHpChecked = Boolean.parseBoolean(data.get("isHpChecked"));
                            final Float hpPercent = Float.parseFloat(data.get("hp"));
                            engine.runOnUpdateThread(new Runnable() {
                                
                                @Override
                                public void run() {
                                    float percent = hpPercent;
                                    if ( !isHpChecked ) {
                                        percent = 100f;
                                    }
                                    float scaleWidth = percent * 480f / 100f;
                                    if ( scaleWidth > 480f ) {
                                        scaleWidth = 480f;
                                    } else if ( scaleWidth < 0f ) {
                                        scaleWidth = 0f;
                                    }
                                    mHp.setScale(scaleWidth, 1.0f);
                                    mHpText.setText(String.format("%.2f%%", percent));
                                }
                            });
							
							
                            mSettings.put("nullAwoken", mNullAwoken);
				            mSettings.put("powerUp", mStagePowerUp);
				            List<Boolean> bindStatus = new ArrayList<Boolean>();
				            for(boolean value: mBindStatus) {
				                bindStatus.add(value);
				            }
				            mSettings.put("bindStatus", bindStatus);
				            mSettings.put("swapIndex", mSwapIndex);
				            mSettings.put("isHpChecked", isHpChecked);
				            mSettings.put("hp", hpPercent);
						}
						
						@Override
						public void onCancel() {
						}
					});
				}
				mSettingDialog.show();
			}
		});
	}

	private void toggleShowPath() {
		boolean newValue = !mShowPath.get();
		mShowPath.set(newValue);
		displayReplayPath(newValue);
		mCheckBox[0].setVisible(!newValue);
		mCheckBox[1].setVisible(newValue);
	}

	private void toggleDroppingMode() {
	    boolean toggle = !mContinueDrop.get();
	    mContinueDrop.set(toggle);
        mDropCheckBox[0].setVisible(!toggle);
        mDropCheckBox[1].setVisible(toggle);
	}
	
	private void toggleSkillFired() {
	    boolean toggle = !mSkillFired.get();
        mSkillFired.set(toggle);
        mSkillCheckBox[0].setVisible(!toggle);
        mSkillCheckBox[1].setVisible(toggle);
	}
	
	private void toggleSkillFired(boolean toggle) {
		if ( mSkillFired.get() != toggle ) {
    		mSkillFired.set(toggle);
    		mSkillCheckBox[0].setVisible(!toggle);
    		mSkillCheckBox[1].setVisible(toggle);
		}
	}
	
	// TODO: check thread
	private void initEditMode() {
		LogUtil.d("initEditMode()");
		if (mCurrentState == GameState.GAME_END) {
			// already dropped
			setGameBoardWOAnimation(mStack.peek());
		}
		changeState(GameState.GAME_MODIFY_BOARD);
		List<IEntity> attachList = new ArrayList<IEntity>();
		for (Sprite s : mSetByHandSprite) {
			registerTouchArea(s);
			attachList.add(s);
		}
		//mHelpMenu.setVisible(false);
		
		registerTouchArea(mEditOkSprite);
		attachList.add(mEditOkSprite);

		registerTouchArea(mOpenFileSprite);
		attachList.add(mOpenFileSprite);
		
		attach(attachList);

		unregisterTouchArea(mCheckBox[0]);
		unregisterTouchArea(mSkillCheckBox[0]);
		unregisterTouchArea(mDropCheckBox[0]);
		unregisterTouchArea(mShowPathText);
		unregisterTouchArea(mContinueDropText);
		unregisterTouchArea(mSkillFiredText);
		
		List<IEntity> detachList = new ArrayList<IEntity>();
		for (int i = 0; i < 2; ++i) {
			detachList.add(mCheckBox[i]);
			detachList.add(mSkillCheckBox[i]);
			detachList.add(mDropCheckBox[i]);
		}
		detachList.add(mShowPathText);
		detachList.add(mSkillFiredText);
		detachList.add(mComboText);
		detachList.add(mStepCountText);
		detachList.add(mContinueDropText);
		detachList.add(mTimerText);
		
		detach(detachList);
	}

	private synchronized void changeState(GameState state) {
		// LogUtil.d("previous=", mCurrentState, " , next=", state);
		if (state == GameState.GAME_RUN && mForceReset.get()) {
			mForceReset.set(false);
			if (mCurrentState == GameState.GAME_REPLAY) {
				resetDrawingPath();
			}
			// reset state
			setGameBoard(mStack.pop());
		}
		if (mCurrentState != state) {
			mCurrentState = state;
			
			if ( mCurrentState == GameState.GAME_END ) {
			    
			}
		}
	}

	// private String getScaledImage(String path) {
	// if (!TextUtils.isEmpty(path) && new File(path).exists()) {
	// Bitmap screen = BitmapFactory.decodeFile(path);
	// int newWidth = screen.getWidth() / 2;
	// int newHeight = screen.getHeight() / 2;
	// Bitmap small = BitmapUtil.getResizedBitmap(screen, newHeight, newWidth);
	//
	// }
	// return path;
	// }

	@Override
	public void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		switch (requestCode) {
		case REQUEST_PICK_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						try {
							Uri uri = data.getData();
                            final String path;
                            if (Build.VERSION.SDK_INT < 19) {
                                path = RealPathUtil
                                        .getRealPathFromURI_API11to18(activity,
                                                uri);
                            } else {
                                path = RealPathUtil.getRealPathFromURI_API19(
                                        activity, uri);
                            }

							if (path != null) {
								IBoardStrategy strategy = StrategyUtil
										.getStrategy(activity);
								try {
									// TODO: scale to half size image for memory
									// usage
								    final int[][] analysis;

								    boolean support = true;
								    if ( support ) {
								    	strategy.setSize(PadBoardAI5x4.ROWS, PadBoardAI5x4.COLS);
									    if ( mAnalysisRect.isEmpty() ) {
	    									analysis = strategy.analysis(
	    											getActivity(),
	    											path,
	    											activity.getVirtualKeyHeight());
									    } else {
									        analysis = strategy.analysis(getActivity(),path, mAnalysisRect);
									    }
										bigFileLoaded = true;
								    } else {
								    	analysis = null;
								    }

									activity.runOnUpdateThread(new Runnable() {

										@Override
										public void run() {
											// reloadExternalPhoto(path);
											loadSpriteFromFile(path);

											// FIXME:
											boolean support = true;
											if ( support ) {
												setGameBoardWOAnimation(analysis);
											} else {
												activity.runOnUiThread(new Runnable() {
													
													@Override
													public void run() {
														Toast.makeText(activity, activity.getString(R.string.str_still_working), Toast.LENGTH_SHORT).show();
													}
												});
											}
										}
									});
								} catch (IOException e) {
									LogUtil.e(e, e.getMessage());
								}
							}
							return;
						} catch (final Throwable e) {
							LogUtil.e(e, e.getMessage());
							activity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(activity,
											"load image failed" + e.getMessage(),
											Toast.LENGTH_SHORT).show();
								}
							});
						}
						// if failed, restore state...
						// changeState(GameState.GAME_MODIFY_BOARD);
					}
				});
			} else {
				// Toast.makeText(activity, "D", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	// TODO: check thread
	private void setGameBoardWOAnimation(int[][] board) {
		if (board == null) {
			return;
		}
		PadBoardAI5x4.copy_board(board, gameBoard);
		resetCombo();
		List<IEntity> attachList = new ArrayList<IEntity>();
		for (int i = 0; i < PadBoardAI5x4.ROWS; ++i) {
			for (int j = 0; j < PadBoardAI5x4.COLS; ++j) {
				int x = Constants.ORB_SIZE_5x4 * i;
				int y = Constants.ORB_SIZE_5x4 * j;

				Sprite s = sprites[i][j];
				addToDetachList(s);

				s = getSprite(gameBoard[i][j]);
				sprites[i][j] = s;
				setPosition(i, j, x, y);
				s.setZIndex(1);
				attachList.add(s);
			}
		}
		attach(attachList);
	}

	private Pair<Integer, Integer> getImageSize(String file) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file, options);
		int imageHeight = options.outHeight;
		int imageWidth = options.outWidth;

		return new Pair<Integer, Integer>(imageWidth, imageHeight);
	}
	
    private Sprite getCacheSprite(int orb) {
    	return mCache[orb];
    }
	
    private Sprite getSprite(int orb) {
        float scaleByBoard = getScaleRatio();
        Sprite s = new Sprite(0, 0, textureOrbs.get(orb%10), vbom) {
            @Override
            public void setAlpha(float pAlpha) {
                int count = getChildCount();
                for(int i=0; i<count; ++i) {
                    getChildByIndex(i).setAlpha(pAlpha);
                }
                
                super.setAlpha(pAlpha);
            }
        };
        
        if ( orb >= 10 ) {
            int addition = (int)(orb/10);
            if (addition == 1 || addition == 3) {
                Sprite plus = new Sprite(0, 0, mResMgr.getTextureRegion(orbClz, TextureOrbs.PLUS_ID), vbom);
                float scale = getPlusScale();
                plus.setScale(scale);
                plus.setPosition(45f, 45f);
                s.attachChild(plus);
            }
//            if (addition > 1) {
//                Sprite lock = new Sprite(0, 0, mResMgr.getTextureRegion("lock.png"), vbom);
//                lock.setPosition(10f, 10f);
//                s.attachChild(lock);                
//            }
        }
        s.setScale(scaleByBoard);
        return s;
    }

	// TODO: check thread
	private void loadSpriteFromFile(String file) {
		TextureManager tm = activity.getTextureManager();

		Pair<Integer, Integer> size = getImageSize(file);
		BitmapTextureAtlas bitmap = new BitmapTextureAtlas(tm, size.first,
				size.second, TextureOptions.DEFAULT);
		FileBitmapTextureAtlasSource texture = FileBitmapTextureAtlasSource
				.create(new File(file));
		tm.loadTexture(bitmap);
		TextureRegion region = TextureRegionFactory.createFromSource(bitmap,
				texture, 0, 0);
		float x = mOpenFileSprite.getX();
		float y = mOpenFileSprite.getY();
		if (!mOpenFileSprite.isDisposed()) {
			unregisterTouchArea(mOpenFileSprite);
			detach(mOpenFileSprite);
		}
		mOpenFileSprite = new Sprite(0f, 0f, 152f, 270f, region, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					try {
		                Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
		                pickIntent.setType("image/*");
		                pickIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
		                activity.startActivityForResult(pickIntent, REQUEST_PICK_PHOTO);
					} catch(Throwable e) {
	                       activity.runOnUiThread(new Runnable() {

	                            @Override
	                            public void run() {
	                                Toast.makeText(activity, activity.getString(R.string.str_need_album_app), Toast.LENGTH_LONG).show();
	                            }
	                       });
					}
				}
				return true;
			}
		};
		mOpenFileSprite.setPosition(x, y);

		attach(mOpenFileSprite);
		registerTouchArea(mOpenFileSprite);
	}

	private void setChangeLineBoard(ActiveSkill skill) {
		changeState(GameState.GAME_ANIMATION);
		resetCombo();

		List<Integer> data = skill.getData();
		int pos = data.get(0);
		int orb = data.get(1);

		List<IEntity> attachList = new ArrayList<IEntity>();
		if (pos < 3 || pos == 5 || pos == 6) {
			int line;
			if (pos < 2) {
				line = pos * 2;
			} else if (pos == 2) {
				line = 3;
			} else if (pos == 5) {
				line = 1;
			} else {
				line = 2;
			}
			int y = Constants.ORB_SIZE_5x4 * line;
			for (int i = 0; i < PadBoardAI5x4.ROWS; ++i) {
				int x = Constants.ORB_SIZE_5x4 * i;
				int addition = (gameBoard[i][line] >= 10 && gameBoard[i][line] != PadBoardAI5x4.X_ORBS) ? 10
						: 0;
				gameBoard[i][line] = orb + addition;

                Sprite s = sprites[i][line];
                if (!s.isDisposed()) {
                    s.setZIndex(2);
                    s.registerEntityModifier(new ScaleModifier(
                            Constants.SECOND_REFRESH, 0.4f, 1.6f));
                    s.registerEntityModifier(new AlphaModifier(
                            Constants.SECOND_REFRESH, 1.0f, 0.0f) {
                        protected void onModifierFinished(IEntity pItem) {
                            addToDetachList(pItem);
                        };
                    });
                }
                s = getSprite(gameBoard[i][line]);
                sprites[i][line] = s;
                setPosition(i, line, x, y);
                s.setZIndex(1);
                attachList.add(s);
			}
		} else {
			int line = (pos == 3) ? 0 : 4;
			switch (pos) {
			case 3:
				line = 0;
				break;
			case 4:
				line = 4;
				break;
			case 7:
			case 8:
			case 9:
				line = pos - 7 + 1;
				break;
			}
			int x = Constants.ORB_SIZE_5x4 * line;
			for (int j = 0; j < PadBoardAI5x4.COLS; ++j) {
				int y = Constants.ORB_SIZE_5x4 * j;
				int addition = (gameBoard[line][j] >= 10 && gameBoard[line][j] != PadBoardAI5x4.X_ORBS) ? 10
						: 0;
				gameBoard[line][j] = orb + addition;

                Sprite s = sprites[line][j];
                if (!s.isDisposed()) {
                    s.setZIndex(2);
                    s.registerEntityModifier(new ScaleModifier(
                            Constants.SECOND_REFRESH, 0.4f, 1.6f));
                    s.registerEntityModifier(new AlphaModifier(
                            Constants.SECOND_REFRESH, 1.0f, 0.0f) {
                        protected void onModifierFinished(IEntity pItem) {
                            addToDetachList(pItem);
                        };
                    });
                }
                s = getSprite(gameBoard[line][j]);
                sprites[line][j] = s;
                setPosition(line, j, x, y);
                s.setZIndex(1);
                attachList.add(s);
			}
		}
		attach(attachList);
		registerUpdateHandler(new TimerHandler(Constants.SECOND_REFRESH + 0.1f,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						unregisterUpdateHandler(pTimerHandler);
						changeState(GameState.GAME_RUN);
					}
				}));
	}

	private void setChangeColorBoard(int[][] board) {
		changeState(GameState.GAME_ANIMATION);

		// PadBoardAI5x4.copy_board(board, gameBoard);

		resetCombo();
		List<IEntity> attachList = new ArrayList<IEntity>();
		for (int i = 0; i < PadBoardAI5x4.ROWS; ++i) {
			for (int j = 0; j < PadBoardAI5x4.COLS; ++j) {
				int x = Constants.ORB_SIZE_5x4 * i;
				int y = Constants.ORB_SIZE_5x4 * j;

				if (gameBoard[i][j] != board[i][j]) {
					gameBoard[i][j] = board[i][j];

					Sprite s = sprites[i][j];
					if (!s.isDisposed()) {
						s.setZIndex(2);
	                    s.registerEntityModifier(new ScaleModifier(
	                            Constants.SECOND_REFRESH, 0.4f, 1.6f));
	                    s.registerEntityModifier(new AlphaModifier(
	                            Constants.SECOND_REFRESH, 1.0f, 0.0f) {
	                        protected void onModifierFinished(IEntity pItem) {
	                            addToDetachList(pItem);
	                        };
	                    });
					}
					s = getSprite(gameBoard[i][j]);
					sprites[i][j] = s;
					setPosition(i, j, x, y);
					s.setZIndex(1);
					attachList.add(s);
				}

			}
		}
		attach(attachList);
		registerUpdateHandler(new TimerHandler(Constants.SECOND_REFRESH + 0.1f,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						unregisterUpdateHandler(pTimerHandler);
						changeState(GameState.GAME_RUN);
					}
				}));
	}

	private void setGameBoard(int[][] board, final IAnimationCallback callback) {
		changeState(GameState.GAME_ANIMATION);
		PadBoardAI5x4.copy_board(board, gameBoard);

		resetCombo();
		List<IEntity> attachList = new ArrayList<IEntity>();
		for (int i = 0; i < PadBoardAI5x4.ROWS; ++i) {
			for (int j = 0; j < PadBoardAI5x4.COLS; ++j) {
				int x = Constants.ORB_SIZE_5x4 * i;
				int y = Constants.ORB_SIZE_5x4 * j;

                Sprite s = sprites[i][j];
                if (!s.isDisposed()) {
                    s.setZIndex(2);
                    s.registerEntityModifier(new ScaleModifier(
                            Constants.SECOND_REFRESH, 0.4f, 1.6f));
                    s.registerEntityModifier(new AlphaModifier(
                            Constants.SECOND_REFRESH, 1.0f, 0.0f) {
                        protected void onModifierFinished(IEntity pItem) {
                            addToDetachList(pItem);
                        };
                    });
                }
                int orb = gameBoard[i][j];
                if (orb < 0) {
                    continue;
                }

                s = getSprite(orb);
                sprites[i][j] = s;
                setPosition(i, j, x, y);
                s.setZIndex(1);
                attachList.add(s);

			}
		}
		attach(attachList);
		registerUpdateHandler(new TimerHandler(Constants.SECOND_REFRESH + 0.1f,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						unregisterUpdateHandler(pTimerHandler);
						changeState(GameState.GAME_RUN);
						if (callback != null) {
							callback.onAnimationFinished();
						}
					}
				}));
	}

	private void setGameBoard(int[][] board) {
		setGameBoard(board, null);
	}

	@Override
	public String getSceneName() {
		return CalculatorGame5x4Scene.class.getSimpleName();
	}

	public void attach(final List<IEntity> entities) {
        engine.runOnUpdateThread(new Runnable() {
            
            @Override
            public void run() {
            	try {
            		for(IEntity entity : entities) {
		            	if (!entity.hasParent()) {
			                attachChild(entity);
		            	}
            		}
            		sortChildren();
            	} catch(Exception e) {
            	}
            }
        });
	}

    public void attach(final IEntity entity) {
        engine.runOnUpdateThread(new Runnable() {
            
            @Override
            public void run() {
            	try {
	            	if (!entity.hasParent()) {
		                attachChild(entity);
		                sortChildren();
	            	}
            	} catch(Exception e) {
            	}
            }
        });
    }

    public void detach(final IEntity[] entityList) {
        engine.runOnUpdateThread(new Runnable() {
            
            @Override
            public void run() {
            	try {
	                for(IEntity entity : entityList) {
	                    if (!entity.isDisposed()) {
	                        entity.dispose();
	                    }
	                    detachChild(entity);
	                }
            	} catch(Exception e) {
            	}
            }
        });
    }
    
    public void detach(final List<IEntity> entityList) {
        engine.runOnUpdateThread(new Runnable() {
            
            @Override
            public void run() {
                for(IEntity entity : entityList) {
                    if (!entity.isDisposed()) {
                        entity.dispose();
                    }
                    detachChild(entity);
                }
            }
        });
    }
    
    public void detach(final IEntity entity) {
        engine.runOnUpdateThread(new Runnable() {
            
            @Override
            public void run() {
            	if (entity != null) {
	                if (!entity.isDisposed()) {
	                    entity.dispose();
	                }
	                detachChild(entity);
            	}
            }
        });
    }
}
