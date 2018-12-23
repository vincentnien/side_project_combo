package com.a30corner.combomaster.scene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.GameActivity;
import com.a30corner.combomaster.activity.ui.DialogUtil;
import com.a30corner.combomaster.activity.ui.SettingsDialog;
import com.a30corner.combomaster.activity.ui.SettingsDialog.ISettingsCallback;
import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.manager.SceneManager;
import com.a30corner.combomaster.pad.BoardStack;
import com.a30corner.combomaster.pad.GameReplay;
import com.a30corner.combomaster.pad.Match;
import com.a30corner.combomaster.pad.MatchBoard;
import com.a30corner.combomaster.pad.PadBoardAI;
import com.a30corner.combomaster.pad.PadBoardAI.IMAGE_TYPE;
import com.a30corner.combomaster.pad.RowCol;
import com.a30corner.combomaster.pad.RowCol.Direction;
import com.a30corner.combomaster.strategy.IBoardStrategy;
import com.a30corner.combomaster.strategy.StrategyUtil;
import com.a30corner.combomaster.texturepacker.GameUIAssets;
import com.a30corner.combomaster.texturepacker.TextureOrbs;
import com.a30corner.combomaster.texturepacker.TextureOrbsCw;
import com.a30corner.combomaster.texturepacker.TextureOrbsTos;
import com.a30corner.combomaster.utils.Constants;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.RealPathUtil;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;
import com.a30corner.combomaster.utils.SharedPreferenceUtil.Skill;

public class SingleDropGameScene extends BaseMenuScene implements
		IOnMenuItemClickListener {

	// board info
	private BoardStack mStack;
	private int[][] gameBoard;
	private ITextureRegion[] textureOrbs;
	private Sprite[][] sprites = new Sprite[PadBoardAI.ROWS][PadBoardAI.COLS];
	private Sprite[][] boardSprite = new Sprite[PadBoardAI.ROWS][PadBoardAI.COLS];
	// edit mode
	private static final int EDIT_SPRITE_SIZE = 11;
	private Sprite[] mSetByHandSprite = new Sprite[EDIT_SPRITE_SIZE];
	private Sprite mOpenFileSprite;
	private Sprite mEditOkSprite;
	private Sprite[] mSkill = new Sprite[6];
	// move mode
	private Sprite orbInHand = null;
	private List<Sprite> mOrbInHandList = new ArrayList<Sprite>();

	// replay mode
	private List<Shape> mReplayPathDrawing = new ArrayList<Shape>();
	private Sprite[] mCheckBox = new Sprite[2];
	private AtomicBoolean mShowPath = new AtomicBoolean(false);

	private AtomicBoolean mForceReset = new AtomicBoolean(false);

	private SettingsDialog mSettingDialog = null;
	
	// skill
	// private ActiveSkill mPreviousSkill = null;
	// private ActiveSkill mPowerUpSkill = null;

	private boolean mShowDialog = true;

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
	private static final int REQUEST_PICK_PHOTO = 999;

	private static int TIME_CHANGE_THE_WORLD = 10 * 1000;

	private enum GameState {
		GAME_RUN, GAME_MODIFY_BOARD, GAME_REPLAY, GAME_END, GAME_ANIMATION,
	}

	private GameState mCurrentState = GameState.GAME_RUN;

	private int mTimeRemain = Constants.DEFAULT_SECOND;
	private int mDropTime, mDropTimeDefault;
	private long mDropType;

	private ResourceManager mResMgr;
	private int mUserDefOffset = 0;

	// private TeamInfo mTeam;

	private int mEditSelect = 0;

	private Rectangle mProgressbar;

	private Handler handler;

	private Text mShowPathText;
	private Text mTimerText;
	private Text mComboText;
	private Text mStepCountText;
	private AtomicInteger mCombo = new AtomicInteger(99);
	private String mStepText;

//	private IMenuItem mHelpMenu;
	
	// private String mPowerUpRawText;

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

	private GameReplay mReplay;
	private ArrayList<Match> mScoreBoard = new ArrayList<Match>();

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
				MatchBoard board = (MatchBoard) msg.obj;
				doRemoval(board, msg.arg1);
				break;
			case MSG_REPLAY:
				RowCol current = (RowCol) msg.obj;
				doReplay(current);
				break;
			case MSG_REPLAY_INIT:
				if (mForceReset.get()) {
					changeState(GameState.GAME_RUN);
					return;
				}
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

	private void addToDetachList(IEntity entity) {
		if (entity != null) {
			entity.setVisible(false);
			synchronized (mRemoveList) {
				mRemoveList.add(entity);
			}

		}

	}

	private void showReplayPath() {
		List<RowCol> path = mReplay.getPath();
		boolean first = true;
		RowCol prev = null;
		float sx = 0f, sy = 0f;
		// int quarter = Constants.HALF_ORB_SIZE / 2;
		Random random = new Random();

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
				float addSize = ((float) (random.nextInt(Constants.ORB_SIZE)) - Constants.HALF_ORB_SIZE) * 0.70f;
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
				line.setLineWidth(5.0f);
				line.setZIndex(4);
				mReplayPathDrawing.add(line);

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

	private void displayReplayPath(final boolean display) {
		activity.getEngine().runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				for (Shape l : mReplayPathDrawing) {
					if (display) {
						attachChild(l);
					} else {
						detachChild(l);
					}
				}
			}
		});
	}

	private void resetDrawingPath() {
		List<IEntity> detachList = new ArrayList<IEntity>();
		for (Shape l : mReplayPathDrawing) {
			detachList.add(l);
		}
		detach(detachList);
		mReplayPathDrawing.clear();
	}

	private float getBoardX(int row) {
		return row * Constants.ORB_SIZE + Constants.OFFSET_X;
	}

	private float getBoardY(int col) {
		return col * Constants.ORB_SIZE + Constants.OFFSET_Y;
	}

	private float getBoardCenterX(int row) {
		return getBoardX(row) + Constants.HALF_ORB_SIZE;
	}

	private float getBoardCenterY(int col) {
		return getBoardY(col) + Constants.HALF_ORB_SIZE;
	}

	private void doReplay(final RowCol current) {
		if (mForceReset.get()) {
			changeState(GameState.GAME_RUN);
			return;
		}
		if (current != null && mReplay.hasNext()) {
			final RowCol next = mReplay.next();

			final float currentX = getBoardX(current.row);// current.row *
															// Constants.ORB_SIZE
															// +
															// Constants.OFFSET_X;
			final float currentY = getBoardY(current.col);// current.col *
															// Constants.ORB_SIZE+
															// Constants.OFFSET_Y;

			final float offsetX = (next.row - current.row) * Constants.ORB_SIZE
					* 0.5f;
			final float offsetY = (next.col - current.col) * Constants.ORB_SIZE
					* 0.5f;

			Sprite move = sprites[current.row][current.col];
			move.registerEntityModifier(new MoveModifier(
					Constants.SECOND_MOVE_HALF, currentX, currentX + offsetX,
					currentY, currentY + offsetY) {
				@Override
				protected void onModifierFinished(IEntity pItem) {
					float nextX = next.row * Constants.ORB_SIZE
							+ Constants.OFFSET_X;
					float nextY = next.col * Constants.ORB_SIZE
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
			s.setScale(1.0f);
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

	private void doRemoval(final MatchBoard board, final int current) {
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
			mComboText.setText(String.valueOf(comboCount) + " Combo");
			mResMgr.playComboSound(comboCount);
			// mComboText.registerEntityModifier(new ScaleModifier(
			// Constants.SECOND_COMBO, 1.0f, 1.3f) {
			// @Override
			// protected void onModifierFinished(IEntity pItem) {
			// pItem.registerEntityModifier(new ScaleModifier(
			// Constants.SECOND_COMBO, 1.3f, 1.0f));
			// super.onModifierFinished(pItem);
			// }
			// });
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
				} else if (target != null) {
					// only drop once in this mode
					// create 1~5 orbs and add to drop list...

					// int emptyLen = target.col + 1;
					// for(int k=target.col; k>=0; --k) {
					// gameBoard[target.row][k] =
					// PadBoardAI.getNewOrbRestrict(mDropType);
					// dropList.add(new Pair<RowCol,
					// RowCol>(RowCol.make(target.row, k),
					// RowCol.make(target.row, k-emptyLen)));
					// }
					// hasChanged = true;
				}
			}
		}
		if (dropList.size() > 0) {
			for (Pair<RowCol, RowCol> pair : dropList) {
				final RowCol target = pair.first;
				final RowCol orb = pair.second;
				int fromY = orb.col * Constants.ORB_SIZE + Constants.OFFSET_Y;
				int toY = target.col * Constants.ORB_SIZE + Constants.OFFSET_Y;

				if (PadBoardAI.isValid(orb.row, orb.col)) {
					Sprite temp = sprites[orb.row][orb.col];
					sprites[orb.row][orb.col] = sprites[target.row][target.col];
					sprites[target.row][target.col] = temp;
				} else { // drop from sky
//					Sprite newSprite = new Sprite(0, 0,
//							textureOrbs[gameBoard[target.row][target.col]],
//							vbom);
//					newSprite.setPosition(target.row * Constants.ORB_SIZE
//							+ Constants.OFFSET_X, fromY);
//					newSprite.setZIndex(0);
//					sprites[target.row][target.col] = newSprite;
//					attachChild(newSprite);
				}

				sprites[target.row][target.col]
						.registerEntityModifier(new MoveYModifier(
								Constants.SECOND_DROP, fromY, toY));
			}

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					findMatches();
				}
			}, (long) (1000 * (Constants.SECOND_DROP + 0.05f)));

		} else {
			int step = mReplay.size();
			if (step > 999) {
				step = 999;
			}
			mStepCountText.setText(step + mStepText);
			Collections.sort(mScoreBoard, mScoreComparator);

			// if (mPowerUpTimes != 0) {
			// mPreviousSkill = mPowerUpSkill;
			// activity.getEngine().runOnUpdateThread(new Runnable() {
			//
			// @Override
			// public void run() {
			// if (--mPowerUpTimes == 0) {
			// resetPowerUpSkill();
			// } else if (mPowerUpSkill != null) {
			// float factor = mPowerUpSkill.getData().get(1) / 10.0f;
			// setPowerUpText(factor);
			// }
			// }
			// });
			//
			// }

			showScore();
			changeState(GameState.GAME_END);
			// changeState(GameState.GAME_PLAYER_TURN);
		}
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
	private String mSecondText;

	private void showScore(boolean fromMenu) {
		if ((fromMenu || mShowDialog) && mCurrentState != GameState.GAME_REPLAY) {

			// ActiveSkill active = (mPreviousSkill == null) ?
			// ActiveSkill.empty()
			// : mPreviousSkill;
			// final List<Pair<Double, Double>> data =
			// DamageCalculator.calculate(
			// mTeam, mScoreBoard, active, 0);

			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Dialog dialog = DialogUtil.getScoreDialog(activity,
							mScoreBoard);
					dialog.show();
				}
			});
		}
	}

	private void showScore() {
		showScore(false);
	}

	private void loadSharedPreference() {
		SharedPreferenceUtil sp = SharedPreferenceUtil.getInstance(activity);
		mDropTime = (int) (1000 * sp.getTimeLimit());
		mDropType = PadBoardAI.getType(sp.getDrops());
		mShowDialog = sp.getShowResultDialog();
		mShowPath.set(sp.getShowPath());
		//Constants.OFFSET_Y = 500 - mUserDefOffset - 20;
		
		mAnalysisRect = sp.getAnalysisRect();
	}

	private void skillFired(Skill skill) {
		if (mCurrentState != GameState.GAME_END) {
			mStack.push(gameBoard);
		}
		switch (skill.getType()) {
		case SKILL_TRANSFORM:
			if (mCurrentState != GameState.GAME_END) {
				long transform = skill.getData().get(0);
				setGameBoard(PadBoardAI.generateBoard(transform, true));
			}
			break;
		case SKILL_STANCE:
			if (mCurrentState != GameState.GAME_END) {
				List<Long> data = skill.getData();
				int[][] board = new int[PadBoardAI.ROWS][PadBoardAI.COLS];
				PadBoardAI.copy_board(gameBoard, board);
				int len = data.size();
				Integer[] change = new Integer[len];
				for (int i = 0; i < len; ++i) {
					change[i] = PadBoardAI.getOrbType(data.get(i));
				}
				setChangeColorBoard(PadBoardAI.colorChangedBoard(board, change));
			}
			break;
		case SKILL_THE_WORLD:
			if (mChangeTheWorld.get()) {
				return;
			}
			if (mCurrentState == GameState.GAME_END) {
				setGameBoard(mStack.peek(), new IAnimationCallback() {

					@Override
					public void onAnimationFinished() {
						changeTheWorld();
					}
				});
			} else {
				changeTheWorld();
			}
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

		mCTWTimer.reset();
		registerUpdateHandler(mCTWTimer);
	}

	private void changeTheWorld() {
		changeTheWorld(TIME_CHANGE_THE_WORLD);
	}

	@Deprecated
	private void initMonsterData() {
		if (mDropTime != 0) {
			// for (int i = 0; i < 6; ++i) {
			// MonsterInfo info = mTeam.getMember(i);
			// if (info != null) {
			// int handCount = info
			// .getTargetAwokenCount(AwokenSkill.EXTEND_TIME);
			// mDropTime += handCount * 500;
			// }
			// }
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
	
    private Class<?> orbClz = null;
	@Override
	public void createScene() {
		init();
		loadSharedPreference();

		// check monster's skill with hand ( +0.5s )
		// initMonsterData();

		mDropTimeDefault = mDropTime;
		LogUtil.d("create scene");
		
		HandlerThread ht = new HandlerThread("ht");
		ht.start();
		handler = new NonUiHandler(ht.getLooper());

		ResourceManager res = ResourceManager.getInstance();
		res.loadGameScene();
		// res.loadPowerUpAssets(mTeam);
		res.loadMusic();
		res.playMusic();
		mResMgr = res;

		SharedPreferenceUtil utils = SharedPreferenceUtil.getInstance(activity);
		IMAGE_TYPE type = (utils.getType() == 2) ? IMAGE_TYPE.TYPE_TOS
				: IMAGE_TYPE.TYPE_PAD;
		if (utils.getBoolean(SharedPreferenceUtil.PREF_COLOR_WEAKNESS)
				&& type == IMAGE_TYPE.TYPE_PAD) {
			type = IMAGE_TYPE.TYPE_PAD_CW;
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
		int[] orbList = {TextureOrbs.DARK_ID, TextureOrbs.FIRE_ID,
				TextureOrbs.HEART_ID, TextureOrbs.LIGHT_ID,
				TextureOrbs.WATER_ID, TextureOrbs.WOOD_ID,
				TextureOrbs.POISON_ID, TextureOrbs.JAMMAR_ID,TextureOrbs.ENHANCED_POISON_ID,9999
				};
		int len = orbList.length;
		textureOrbs = new ITextureRegion[len];
		for(int i=0; i<len; ++i) {
			textureOrbs[i] = res.getTextureRegion(orbClz, orbList[i]);
		}
//		textureOrbs[9] = res.getTextureRegion("bomb.png");

        boolean index = false;
        ITextureRegion[] textureBoard = new ITextureRegion[2];
        textureBoard[0] = res.getTextureRegion(GameUIAssets.class, GameUIAssets.BOARD_A_ID);
        textureBoard[1] = res.getTextureRegion(GameUIAssets.class, GameUIAssets.BOARD_B_ID);

		for (int i = 0; i < PadBoardAI.ROWS; ++i) {
			for (int j = 0; j < PadBoardAI.COLS; ++j) {
				int x = Constants.ORB_SIZE * i;
				int y = Constants.ORB_SIZE * j;
				boardSprite[i][j] = new Sprite(0, 0,
						textureBoard[index ? 0 : 1], vbom);
				boardSprite[i][j].setPosition(x + Constants.OFFSET_X, y
						+ Constants.OFFSET_Y);
				boardSprite[i][j].setZIndex(0);
				attachChild(boardSprite[i][j]);

				sprites[i][j] = new Sprite(0, 0, textureOrbs[gameBoard[i][j]],
						vbom);
				setPosition(i, j, x, y);
				sprites[i][j].setZIndex(1);
				attachChild(sprites[i][j]);
				index = !index;
			}
		}

		for (int i = 0; i < len; ++i) {
			Sprite handOrb = new Sprite(0f, 0f, textureOrbs[i], vbom);
			handOrb.setScale(1.1f);
			handOrb.setZIndex(2);
			handOrb.setVisible(false);
			attachChild(handOrb);
			mOrbInHandList.add(handOrb);
		}

		// progress
		mProgressbar = new Rectangle(Constants.OFFSET_X,
				Constants.OFFSET_Y - 15 - 8, PROGRESS_BAR_WIDTH, 15, vbom);
		mProgressbar.setColor(0f, 1f, 0f);
		attachChild(mProgressbar);

        final int[] textureName = { GameUIAssets.S1_ID,GameUIAssets.S2_ID,
        		GameUIAssets.S3_ID,GameUIAssets.S4_ID,
        		GameUIAssets.S5_ID,GameUIAssets.S6_ID};
		SharedPreferenceUtil instance = SharedPreferenceUtil
				.getInstance(activity);
		List<Skill> skills = instance.getSkillList();
		List<Integer> skillImages = instance.getSkillImages();

		for (int i = 0; i < textureName.length; ++i) {
			final Skill defaultSkill = skills.get(i);
			// final MonsterInfo info = mTeam.getMember(i);
			ITextureRegion region = null;
			int id = skillImages.get(i);
			if (id != 0) {
				try {
					region = res.loadTextureFile(id + "i.png");
				} catch (Exception e) {
					LogUtil.e(e);
				}
			}
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
						skillFired(defaultSkill);
						// do this skill
						// if (info != null) {
						// skillFired(info.getActiveSkill());
						// } else {
						// skillFired(defaultSkill);
						// }
					}
					return true;
				}
			};
			
			if (region != null) { // orignal size is 100x100, we need to resize
									// it to 88x88
				s.setScaleCenter(0f, 0f);
				s.setScale(0.88f);
			}
			mSkill[i] = s;
			s.setPosition(Constants.OFFSET_X + i * (88 + 1),
					mProgressbar.getY() - 8 - 88);
			registerTouchArea(s);
			attachChild(s);

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

		Log.e("Vincent", "ok 1");
		mComboText = new Text(0, 0, font, String.valueOf(mCombo.get())
				+ " Combo", vbom);
		float offset = GameActivity.SCREEN_WIDTH - Constants.OFFSET_X
				- mComboText.getWidth() * 1.2f;

		float h = menuHeight + 15 + 16 + startY; // 15 -> progress height 16 ->
													// padding
		mComboText.setPosition(offset, h - mUserDefOffset);
		mComboText.setZIndex(3);
		mComboText.setColor(Color.WHITE);

		mStepText = " " + activity.getString(R.string.step);
		mStepCountText = new Text(0, 0, font, 999 + mStepText, vbom);
		mStepCountText.setPosition(offset, h + mComboText.getHeight() + 16
				- mUserDefOffset);
		mStepCountText.setZIndex(3);
		mStepCountText.setColor(Color.WHITE);
		Log.e("Vincent", "ok 2");
		mSecondText = activity.getString(R.string.second);
		String sec = "00.00 " + mSecondText;
		mTimerText = new Text(0, 0, font, sec, vbom);
		mTimerText.setPosition(offset, h + mComboText.getHeight()
				+ mStepCountText.getHeight() + 32 - mUserDefOffset);
		mTimerText.setZIndex(3);
		mTimerText.setColor(Color.WHITE);

		attachChild(mComboText);
		attachChild(mStepCountText);
		attachChild(mTimerText);

		mComboText.setText("0 Combo");
		mStepCountText.setText(0 + mStepText);
		Log.e("Vincent", "ok 3");
        // special menu ...
//        mHelpMenu = new ColorMenuItemDecorator(new SpriteMenuItem(
//                MENU_HELP, res.getTextureRegion(GameUIAssets.class, GameUIAssets.IC_HELP_ID), vbom),
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
		mShowPathText = new Text(0, 0, font, showPath, vbom);
		mShowPathText.setScale(0.5f);
		mShowPathText.setPosition(32, mCheckBox[0].getY() - 32);
		attachChild(mShowPathText);

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
		float offsetX = (GameActivity.SCREEN_WIDTH - 6 * textureOrbs[0]
				.getWidth()) / 2f;
		float offsetY = menuHeight * 1.65f;
		for (int i = 0; i < 2; ++i) {
			for (int j = 0; j < 4; ++j) {
				final int pos = i * 4 + j;
				final Sprite s = new Sprite(0, 0, textureOrbs[pos], vbom) {
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
				s.setScale(.7f);
				s.setAlpha(.5f);
				s.setPosition(offsetX + j * s.getWidth(),
						offsetY + s.getHeight() * i);
				mSetByHandSprite[pos] = s;
			}
		}
		mSetByHandSprite[0].setAlpha(1f);

		ResourceManager res = ResourceManager.getInstance();
		mOpenFileSprite = new Sprite(0f, 0f,
				res.getTextureRegion(GameUIAssets.class, GameUIAssets.CHOOSE_PHOTO_ID), vbom) {
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

		mEditOkSprite = new Sprite(0f, 0f, res.getTextureRegion(GameUIAssets.class, GameUIAssets.IC_OK_ID),
				vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					onEditModeFinish();
				}
				return true;
			}
		};
		// (GameActivity.SCREEN_WIDTH-mEditOkSprite.getWidth())/4
		// (GameActivity.SCREEN_WIDTH-mEditOkSprite.getWidth())/4
		mEditOkSprite.setScale(0.85f);
		mEditOkSprite.setPosition(mSetByHandSprite[7].getX() + 8,
				mSetByHandSprite[7].getY() + mEditOkSprite.getHeight() + 8);


		mSetByHandSprite[8] = new Sprite(0f, 0f,
				mResMgr.getTextureRegion(orbClz,TextureOrbs.ENHANCED_POISON_ID), vbom) {
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
				mSetByHandSprite[6].getY() + mSetByHandSprite[6].getHeight()
						- 20);

		mSetByHandSprite[9] = new Sprite(0f, 0f,
				mResMgr.getTextureRegion(orbClz,TextureOrbs.PLUS_ID), vbom) {
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
		mSetByHandSprite[9].setVisible(false);

		mSetByHandSprite[10] = new Sprite(0f, 0f,
				mResMgr.getTextureRegion("bomb.png"), vbom) { //mResMgr.getTextureRegion(orbClz, TextureOrbs.LOCK_ID)
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
		mSetByHandSprite[10].setScale(.7f);
		mSetByHandSprite[10].setPosition(mSetByHandSprite[5].getX(),
				mSetByHandSprite[5].getY() + mSetByHandSprite[5].getHeight()-14);
		mSetByHandSprite[10].setVisible(true);


		mSetByHandSprite[0].setAlpha(1f);
		sortChildren();
	}

	private void setPosition(int ox, int oy, float x, float y) {
		sprites[ox][oy].setPosition(x + Constants.OFFSET_X, y
				+ Constants.OFFSET_Y);
	}

	private void init() {
        int[][] cache = ComboMasterApplication.getsInstance().getBoardCache();
        if ( cache == null ) {
            gameBoard = PadBoardAI.generateBoard(0x111111, false);
        } else {
            gameBoard = PadBoardAI.copy_board(cache);
            for(int i=0; i<PadBoardAI.ROWS; ++i) {
                for(int j=0; j<PadBoardAI.COLS; ++j) {
                	int orb = gameBoard[i][j]%10;
                	if (orb < 8) {
                		gameBoard[i][j] = orb;
                	} else {
                		gameBoard[i][j] = 1;
                	}
                }
            }
        }
		mStack = new BoardStack(gameBoard);
		mReplay = new GameReplay();

		// mTeam = ComboMasterApplication.getsInstance().getTargetTeam();

		changeState(GameState.GAME_RUN);
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
		ResourceManager res = ResourceManager.getInstance();
		res.unloadGameScene();
		// res.unloadPowerUpAssets(mTeam);

		for (Sprite[] ss : sprites) {
			for (Sprite s : ss) {
				if (!s.isDisposed()) {
					s.dispose();
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

		for (Sprite s : mOrbInHandList) {
			if (!s.isDisposed()) {
				s.dispose();
			}
		}

		for (int i = 0; i < 2; ++i) {
			mSkill[i].dispose();
			unregisterTouchArea(mSkill[i]);
		}

		for (int i = 0; i < 2; ++i) {
			mCheckBox[i].dispose();
		}
		unregisterTouchArea(mCheckBox[0]);
		setOnSceneTouchListener(null);
		setOnMenuItemClickListener(null);

		res.unloadMusic();

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
			mOpenFileSprite = new Sprite(0f, 0f, ResourceManager.getInstance()
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
		List<IEntity> attachList = new ArrayList<IEntity>();
		for (int i = 0; i < 2; ++i) {
			attachList.add(mCheckBox[i]);
		}
		attachList.add(mShowPathText);
		attachList.add(mComboText);
		attachList.add(mStepCountText);
		attachList.add(mTimerText);
		attach(attachList);
		
//		mHelpMenu.setVisible(true);

		changeState(GameState.GAME_RUN);
	}

	private void onEditBoard(Scene pScene, TouchEvent pSceneTouchEvent) {
		LogUtil.d("onSceneTouched-onEditBoard");
		int event = pSceneTouchEvent.getAction();

		if (event == TouchEvent.ACTION_DOWN || event == TouchEvent.ACTION_MOVE) {
			float px = pSceneTouchEvent.getX();
			float py = pSceneTouchEvent.getY();
			float x = px - Constants.OFFSET_X;
			float y = py - Constants.OFFSET_Y;
			int indexX = (int) x / Constants.ORB_SIZE;
			int indexY = (int) y / Constants.ORB_SIZE;

			if (PadBoardAI.isValid(indexX, indexY)) {
				if (gameBoard[indexX][indexY] != mEditSelect) {
					int orb = mEditSelect;
					if(mEditSelect == 10) { // bomb, use index 9
						orb = 9;
					}
					gameBoard[indexX][indexY] = orb;
					Sprite s = new Sprite(0, 0, textureOrbs[orb], vbom);
					s.setZIndex(1);

					Sprite temp = sprites[indexX][indexY];
					addToDetachList(temp);
					attach(s);
					sprites[indexX][indexY] = s;
					setPosition(indexX, indexY, indexX * Constants.ORB_SIZE,
							indexY * Constants.ORB_SIZE);
				}
			}

		}
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
		int indexX = (int) x / Constants.ORB_SIZE;
		int indexY = (int) y / Constants.ORB_SIZE;
		boolean specialCase = false;
		if (currentX != -1) {
			if (y<0) {
				indexY = 0;
				specialCase = true;
			} else if(indexY>=PadBoardAI.COLS) {
				indexY = PadBoardAI.COLS-1;
				specialCase = true;
			} 
			if (x<0) {
				indexX = 0;
				specialCase = true;
			} else if(indexX>=PadBoardAI.ROWS) {
				indexX = PadBoardAI.ROWS-1;
				specialCase = true;
			}
		}
		
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			if (PadBoardAI.isValid(indexX, indexY)) {
				currentX = indexX;
				currentY = indexY;

				orbInHand = mOrbInHandList.get(gameBoard[indexX][indexY]);
				orbInHand.setPosition(x - 48 + Constants.OFFSET_X, y - 48
						+ Constants.OFFSET_Y);
				orbInHand.setVisible(true);
				//attach(orbInHand);

				sprites[currentX][currentY].setAlpha(0.5f);

				resetCombo();
				untouchableWhenMove();
			}
		} else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
			if (PadBoardAI.isValid(currentX, currentY)) {
				// setPosition(currentX, currentY, x-48, y-48);
				orbInHand.setPosition(x - 48 + Constants.OFFSET_X, y - 48
						+ Constants.OFFSET_Y);
			}
            // if position more than 2, skip it...
            if (Math.abs(indexX - currentX) >= 2
                    || Math.abs(indexY - currentY) >= 2) {
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
			
			// check orb is swap or not
			if (PadBoardAI.isValid(currentX, currentY)
					&& PadBoardAI.isValid(indexX, indexY)
					&& (indexX != currentX || indexY != currentY)) {
	            Sprite s = sprites[indexX][indexY];
	            if (!specialCase && !s.contains(px, py)) {
	                return true;
	            }
			    
				// if not start... start time calculating
				if (!mCountDown.get()) {
					mCountDown.set(true);
					mTimeRemain = mDropTime;
					// save the current board
					mStack.push(gameBoard);

					mReplay.start(gameBoard);
					mReplay.add(RowCol.make(currentX, currentY));

					if (mDropTime != 0) { // 0 -> inf mode
						mTimer.reset();
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

	private void onTouchUpWhenCTW() {
		if (orbInHand != null) {
			//detachChild(orbInHand);
			orbInHand.setVisible(false);
			orbInHand = null;
		}
		if (PadBoardAI.isValid(currentX, currentY)) {
			sprites[currentX][currentY].setAlpha(1.0f);
		}

		currentX = -1;
		currentY = -1;
	}

	private void onTouchUpReset() {
		LogUtil.d("onTouchUpReset()");
		if (orbInHand != null) {
			//detachChild(orbInHand);
			orbInHand.setVisible(false);
			orbInHand = null;
		}

		if (PadBoardAI.isValid(currentX, currentY)) {
			sprites[currentX][currentY].setAlpha(1.0f);
		}

		mCountDown.set(false);
		setSkillTouchable();

		currentX = -1;
		currentY = -1;
		mChangeTheWorld.set(false);
	}

	private void onTimeUp() {
		LogUtil.e("onTimeUp()");
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

		// mPreviousSkill = mPowerUpSkill;
		LogUtil.e("onTimeUp completed");
		findMatches();
	}

	static final int BOMB = 9;
	private void findMatches() {
		LogUtil.e("findMatches()");
		handler.post(new Runnable() {

			@Override
			public void run() {
//				boolean clearBombPhase = findBombs();
				final MatchBoard matchBoard = PadBoardAI.findMatches(gameBoard);
				Message.obtain(handler, NonUiHandler.MSG_REMOVAL, 0, 0,
						matchBoard).sendToTarget();
//				if(clearBombPhase) {
//					// check unlinked bomb first
//				} else {
//
//				}
			}
		});

	}

	private boolean findBombs() {
		for(int i=0; i<PadBoardAI.ROWS; ++i) {
			for(int j=0; j<PadBoardAI.COLS; ++j) {
				if(gameBoard[i][j] == BOMB) {
					return true;
				}
			}
		}
		return false;
	}

	private float HALF_ORB_SIZE_PLUS_1414 = Constants.HALF_ORB_SIZE * 1.41421356237f;
	private void swapOrb(int nx, int ny, int cx, int cy) {
		final float toX = cx * Constants.ORB_SIZE + Constants.OFFSET_X;
		final float toY = cy * Constants.ORB_SIZE + Constants.OFFSET_Y;
		float fromX = nx * Constants.ORB_SIZE + Constants.OFFSET_X;
		float fromY = ny * Constants.ORB_SIZE + Constants.OFFSET_Y;

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
//		// setPosition(nx, ny, cx*ORB_SIZE, cy*ORB_SIZE);
//		float toX = cx * Constants.ORB_SIZE + Constants.OFFSET_X;
//		float toY = cy * Constants.ORB_SIZE + Constants.OFFSET_Y;
//		float fromX = nx * Constants.ORB_SIZE + Constants.OFFSET_X;
//		float fromY = ny * Constants.ORB_SIZE + Constants.OFFSET_Y;
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
				setGameBoard(PadBoardAI.generateBoard(0x111111, false));
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
				if (!PadBoardAI.isSameBoard(gameBoard, mStack.peek())) {
					mStack.push(mReplay.getBoard());
				}
				changeState(GameState.GAME_REPLAY);
				mReplay.reset();
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
                    DialogUtil.getTinyUrlDialog(activity, gameBoard, null, null,null).show();
                }
            });
            
            break;
		}
		return false;
	}
	
	private void showSettingsDialog() {
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if ( mSettingDialog == null ) {
					mSettingDialog = new SettingsDialog(activity);
					mSettingDialog.initViews();
					mSettingDialog.setCallback(new ISettingsCallback() {
						
						@Override
                        public void onResult(Map<String, String> data) {
                            float seconds = Float.parseFloat(data.get("seconds"));
							if (seconds == 0) { // don't modify
								mDropTime = mDropTimeDefault;
							} else {
								mDropTime = (int)(seconds * 1000);
							}
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

	private void initEditMode() {
		LogUtil.d("initEditMode()");
		if (mCurrentState == GameState.GAME_END) {
			// already dropped
			setGameBoardWOAnimation(mStack.pop());
		}
		changeState(GameState.GAME_MODIFY_BOARD);
		List<IEntity> attachList = new ArrayList<IEntity>();
		for (Sprite s : mSetByHandSprite) {
			registerTouchArea(s);
			attachList.add(s);
		}
//		mHelpMenu.setVisible(false);
		
		registerTouchArea(mEditOkSprite);
		attachList.add(mEditOkSprite);

		registerTouchArea(mOpenFileSprite);
		attachList.add(mOpenFileSprite);
		
		attach(attachList);

		List<IEntity> detachList = new ArrayList<IEntity>();
		unregisterTouchArea(mCheckBox[0]);
		for (int i = 0; i < 2; ++i) {
			detachList.add(mCheckBox[i]);
		}
		detachList.add(mShowPathText);
		detachList.add(mComboText);
		detachList.add(mStepCountText);
		detachList.add(mTimerText);
		
		detach(detachList);
	}

	private synchronized void changeState(GameState state) {
		LogUtil.e("previous=", mCurrentState, " , next=", state);
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
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		switch (requestCode) {
		case REQUEST_PICK_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				// changeState(GameState.GAME_ANIMATION);
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

							try {
								if (path != null) {
									IBoardStrategy strategy = StrategyUtil
											.getStrategy(activity);
									final int[][] analysis;
									
									if ( mAnalysisRect.isEmpty() ) {
                                        analysis = strategy.analysis(
                                        		getActivity(),
                                                path,
                                                activity.getVirtualKeyHeight());
                                    } else {
                                        analysis = strategy.analysis(getActivity(),path, mAnalysisRect);
                                    }
									if ( analysis != null ) {
										for(int i=0; i<PadBoardAI.ROWS; ++i) {
											for(int j=0; j<PadBoardAI.COLS; ++j) {
												analysis[i][j] = analysis[i][j]%10; 
											}
										}
									}
									
									bigFileLoaded = true;
									activity.runOnUpdateThread(new Runnable() {

										@Override
										public void run() {
											// reloadExternalPhoto(path);
											loadSpriteFromFile(path);
											setGameBoardWOAnimation(analysis);
										}
									});
								}
								return;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (Throwable e) {
							LogUtil.e(e, e.getMessage());
							activity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(activity,
											"Error loading image",
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

	private void setGameBoardWOAnimation(int[][] board) {
		// FIXME: should not pass the null to this function
		if (board == null) {
			return;
		}
		PadBoardAI.copy_board(board, gameBoard);
		resetCombo();
		List<IEntity> attachList = new ArrayList<IEntity>();
		for (int i = 0; i < PadBoardAI.ROWS; ++i) {
			for (int j = 0; j < PadBoardAI.COLS; ++j) {
				int x = Constants.ORB_SIZE * i;
				int y = Constants.ORB_SIZE * j;

				Sprite s = sprites[i][j];
				addToDetachList(s);

				s = new Sprite(0, 0, textureOrbs[gameBoard[i][j]], vbom);
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
			mOpenFileSprite.dispose();
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
					gameBoard[i][j] = board[i][j];

					Sprite s = sprites[i][j];
					addToDetachList(s);

					s = new Sprite(0, 0, textureOrbs[gameBoard[i][j]], vbom);
					sprites[i][j] = s;
					setPosition(i, j, x, y);
					s.setZIndex(1);
					attachList.add(s);
					s.registerEntityModifier(new AlphaModifier(
							Constants.SECOND_REFRESH, .5f, 1f));
					s.registerEntityModifier(new ScaleModifier(
							Constants.SECOND_REFRESH, 1.1f, 1f));
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
		LogUtil.e("setGameBoard");
		changeState(GameState.GAME_ANIMATION);
		PadBoardAI.copy_board(board, gameBoard);

		resetCombo();
		List<IEntity> attachList = new ArrayList<IEntity>();
		for (int i = 0; i < PadBoardAI.ROWS; ++i) {
			for (int j = 0; j < PadBoardAI.COLS; ++j) {
				int x = Constants.ORB_SIZE * i;
				int y = Constants.ORB_SIZE * j;

				Sprite s = sprites[i][j];
				addToDetachList(s);

				int orb = gameBoard[i][j];
				if (orb < 0 || orb >= textureOrbs.length) {
					continue;
				}

				s = new Sprite(0, 0, textureOrbs[orb], vbom);
				sprites[i][j] = s;
				setPosition(i, j, x, y);
				s.setZIndex(1);
				attachList.add(s);
				s.registerEntityModifier(new AlphaModifier(
						Constants.SECOND_REFRESH, .5f, 1f));
				s.registerEntityModifier(new ScaleModifier(
						Constants.SECOND_REFRESH, 1.15f, 1f));

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
		return SingleDropGameScene.class.getSimpleName();
	}

    public void attach(final IEntity entity) {
        engine.runOnUpdateThread(new Runnable() {
            
            @Override
            public void run() {
            	if (!entity.hasParent()) {
            		attachChild(entity);
            		sortChildren();
            	}
                
            }
        });
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
