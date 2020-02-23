package com.a30corner.combomaster.playground;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.GameActivity;
import com.a30corner.combomaster.activity.ui.DialogUtil;
import com.a30corner.combomaster.engine.sprite.BasicHpSprite.AnimationCallback;
import com.a30corner.combomaster.engine.sprite.SpritePool;
import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.pad.DamageCalculator;
import com.a30corner.combomaster.pad.DamageCalculator.AttackValue;
import com.a30corner.combomaster.pad.Match;
import com.a30corner.combomaster.pad.enemy.EnemySkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.LeaderSkill;
import com.a30corner.combomaster.pad.monster.LeaderSkill.LeaderSkillType;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill.AwokenSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MoneyAwokenSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.SinglePowerUp;
import com.a30corner.combomaster.pad.monster.MonsterVO;
import com.a30corner.combomaster.pad.monster.Skill;
import com.a30corner.combomaster.pad.monster.TeamInfo;
import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.Team.IDamageCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.effect.Jump;
import com.a30corner.combomaster.playground.effect.Shake;
import com.a30corner.combomaster.playground.enemy.EnemyAttackMode;
import com.a30corner.combomaster.playground.entity.BuffOnEnemy;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.playground.entity.HPEntity;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.playground.entity.SkillEntity;
import com.a30corner.combomaster.playground.module.AttackModule;
import com.a30corner.combomaster.playground.skill.SkillDispatcher;
import com.a30corner.combomaster.playground.stage.EnemyData;
import com.a30corner.combomaster.playground.stage.Floor;
import com.a30corner.combomaster.playground.stage.StageData;
import com.a30corner.combomaster.provider.LocalDBHelper;
import com.a30corner.combomaster.scene.PadGameScene;
import com.a30corner.combomaster.scene.PadGameScene.GameState;
import com.a30corner.combomaster.scene.PadGameScene7x6;
import com.a30corner.combomaster.scene.PlaygroundGameScene;
import com.a30corner.combomaster.utils.Constants;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.RandomUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import javax.microedition.khronos.opengles.GL10;

import au.com.ds.ef.EasyFlow;
import au.com.ds.ef.EventEnum;
import au.com.ds.ef.FlowBuilder;
import au.com.ds.ef.StateEnum;
import au.com.ds.ef.StatefulContext;
import au.com.ds.ef.call.ContextHandler;
import au.com.ds.ef.err.LogicViolationError;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PadEnvironment implements IEnvironment {
    protected WeakReference<PlaygroundGameScene> mSceneRef;
    private ExecutorHandler handler;
    protected PadContext padContext = new PadContext();
    
    // stage info
    protected boolean nullAwoken = false;
    
    protected String mStage;
    
    protected int currentStage = 1;
    protected SparseArray<ArrayList<Enemy>> mStageEnemies = new SparseArray<ArrayList<Enemy>>(); 
    
    public Team mTeamData;
    public Member[] mMemberList = new Member[6];
    
    protected Color[] ATTACK_COLOR = new Color[6];
    
    protected Text mStageTop;
    protected Text mStageText;
    protected Text mJumpTo;
    protected HPEntity mHp;
    
    protected boolean jumpUsed = false;
    protected SinglePowerUp singleUp;

    protected SkillEntity mLeaderSwitch = null;
    protected Map<String, SkillEntity> mSkillList = new HashMap<String, SkillEntity>();
    private ActiveSkill mDarkSkill = null;
    
    protected SpritePool mBulletPool;
    
//    protected Toast customToast;
    protected int mStoneUsed;
    
    protected List<Pair<Text, Text>> mDamageList = new ArrayList<Pair<Text, Text>>(6);
    //private List<Pair<NumberText, NumberText>> mDamageList = new ArrayList<Pair<NumberText, NumberText>>(6);
    
    protected Pair<Float, Float> mCrossShield = null;
    private boolean isCopMode = false;
    
    public float getCrossReduceShield() {
    	if(mCrossShield != null) {
    		return mCrossShield.first;
    	}
    	return 0;
    }
    boolean hasHeartSquare(List<Match> matches) {
        boolean hasHeartSquare = false;
        for(Match match : matches) {
            if (match.isSquare() && match.type == 2) {
                hasHeartSquare = true;
                break;
            }
        }
        if(!hasHeartSquare) {
            return false;
        }
        for(Member m : mMemberList) {
            int c = getTargetAwokenCount(m.info,AwokenSkill.HEART_SQUARE);
            if(c>0) {
                return true;
            }
        }
        return false;
    }

    boolean hasHeartArrow(List<Match> matches) {
        boolean hasHeartColumn = false;
        for(Match match : matches) {
            if (match.isHeartColumn()) {
                hasHeartColumn = true;
                break;
            }
        }
        if(!hasHeartColumn) {
            return false;
        }
        for(Member m : mMemberList) {
            int c = getTargetAwokenCount(m.info,AwokenSkill.HEART_ARROW);
            if(c>0) {
                return true;
            }
        }
        return false;
    }

    static boolean isAdditionAttack(MonsterInfo info) {
    	if(info != null) {
    		for(LeaderSkill s :info.getLeaderSkill()) {
    		    LeaderSkillType type = s.getType();
    			if(type == LeaderSkillType.LST_ATTACK ||
                    type == LeaderSkillType.LST_MULTI_ORB_DIRECT_ATTACK ||
                        type == LeaderSkillType.LST_COLOR_DIRECT_ATTACK  ||
                        type == LeaderSkillType.LST_TARGET_ORB_DIRECT_ATTACK) {
    				return true;
    			}
    		}
    	}
    	return false;
    }

    @Override
    public int getTargetAwokenCount(MonsterInfo info,AwokenSkill skill) {
        if (info == null || nullAwoken || isAwokenLocked()) {
            return 0;
        }
        return info.getTargetAwokenCount(skill, isCopMode);
    }

    class ExecutorHandler extends Handler {
        public static final int MSG_START_ATTACK = 0;
        public static final int MSG_CHECK_HP = 12;
        public static final int MSG_PLAYER_ATTACK = 1;
        public static final int MSG_END_OF_ATTACK = 2;

        public static final int MSG_FIRE_SKILL_INIT = 3;
        public static final int MSG_FIRE_SKILL = 4;

        public static final int MSG_POISON_DAMAGE = 5;
        public static final int MSG_ADDITION_ATTACK = 13;

        public static final int MSG_BEGIN_ENEMY_ATTACK = 6;
        public static final int MSG_ENEMY_ATTACK_LIST = 7;

        public static final int MSG_BEGIN_ENEMY_FIRST_STRIKE = 8;
        public static final int MSG_ENEMY_FIRST_STRIKE = 9;

        public static final int MSG_END_ENEMY_ATTACK = 10;
        public static final int MSG_END_FIRST_STRIKE = 11;

        public ExecutorHandler(Looper looper) {
            super(looper);
        }

        private void attackNext(List<Pair<AttackValue, AttackValue>> data, int index, int major) {
            if (index==5) {
                // attack done...
                if (major == 1) {
                    //sendEmptyMessage(MSG_END_OF_ATTACK);
                	sendEmptyMessage(MSG_POISON_DAMAGE);
                } else {
                    Message.obtain(handler, MSG_PLAYER_ATTACK, 0, 1, data).sendToTarget();
                }
            } else {
                Message.obtain(handler, MSG_PLAYER_ATTACK, index+1, major, data).sendToTarget();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            LogUtil.d("msg=", msg.what);
            switch(msg.what) {
            case MSG_END_FIRST_STRIKE: {
            	try {
            		if(hasFirstStrike) {
            			clearPhase();
            		}

                    if(mTeamData.getCurrentHp()>0) {
                    	flow.trigger(Events.afterFirstStrike, padContext);
                    } else {
                    	flow.trigger(Events.hpZero, padContext);
                    }
            	} catch (LogicViolationError e1) {
					e1.printStackTrace();
				}
            	return ;
            }
            case MSG_BEGIN_ENEMY_FIRST_STRIKE: {
            	final int index = msg.arg1;
            	List<Enemy> list = getEnemies();
            	if (index >= list.size()) {
            		sendEmptyMessage(MSG_END_FIRST_STRIKE);
            		return ;
            	}
            	Enemy enemy = list.get(index);
            	List<EnemyAction> firstStrikes = enemy.firstStrike();
            	if (firstStrikes.size()>0) {
            		hasFirstStrike = true;
            		handler.sendMessageDelayed(Message.obtain(handler, MSG_ENEMY_FIRST_STRIKE, index, 0, firstStrikes), 500);
            	} else {
            		Message.obtain(handler, MSG_BEGIN_ENEMY_FIRST_STRIKE, index+1, 0).sendToTarget();
            	}
            	break;
            }
            case MSG_ENEMY_FIRST_STRIKE: {
                @SuppressWarnings("unchecked")
                final List<EnemyAction> actionList = (List<EnemyAction>) msg.obj;
                final int index = msg.arg1;
                final int subindex = msg.arg2;
                actionList.get(subindex).doAction(PadEnvironment.this, getEnemies().get(index), new ICastCallback() {

                    @Override
                    public void onCastFinish(boolean casted) {
                        if (actionList.size() == subindex+1) {
                        	getEnemies().get(index).afterAttack();
                            handler.sendMessageDelayed(Message.obtain(handler, MSG_BEGIN_ENEMY_FIRST_STRIKE, index+1, 0), 1000);
                        } else {
                            Message msg = Message.obtain(handler, MSG_ENEMY_FIRST_STRIKE, index, subindex+1, actionList);
                            handler.sendMessageDelayed(msg, (int)((Shake.DEFAULT_DURATION+0.05f) * 1000));
                        }

                    }
                });
            	break;
            }
            case MSG_END_ENEMY_ATTACK: {
            	try {
            		flow.trigger(Events.enemyAttack, padContext);
            	} catch (LogicViolationError e1) {
					e1.printStackTrace();
				}
            	break;
            }
            case MSG_BEGIN_ENEMY_ATTACK: {
            	final int index = msg.arg1;
            	List<Enemy> list = getEnemies();
            	if (index >= list.size()) {
            		sendMessageDelayed(Message.obtain(this, MSG_END_ENEMY_ATTACK), 1000);
            		return ;
            	}

            	boolean execute = false;
            	Enemy enemy = list.get(index);
            	if (!enemy.dead() && enemy.attackCountDown()) {
            	    getScene().setBoardAlpha(1.0f, 0.6f);

            		List<EnemyAction> actionList = enemy.attack();
            		int size = actionList.size();
            		if (size > 0) {
            		    handler.sendMessageDelayed(Message.obtain(handler, MSG_ENEMY_ATTACK_LIST, index, 0, actionList), 500);
            		    execute = true;
            		}
            	}
            	if(!execute) {
            		Message.obtain(handler, MSG_BEGIN_ENEMY_ATTACK, index+1, 0).sendToTarget();
            	}
            	break;
            }
            case MSG_ENEMY_ATTACK_LIST: {
                @SuppressWarnings("unchecked")
                final List<EnemyAction> actionList = (List<EnemyAction>) msg.obj;
                final int index = msg.arg1;
                final int subindex = msg.arg2;
                actionList.get(subindex).doAction(PadEnvironment.this, getEnemies().get(index), new ICastCallback() {

                    @Override
                    public void onCastFinish(boolean casted) {
                    	Enemy enemy = getEnemies().get(index);

                        if (actionList.size() == subindex+1 || enemy.diedThisTurn()) {
                        	getEnemies().get(index).afterAttack();
                            Message msg = Message.obtain(handler, MSG_BEGIN_ENEMY_ATTACK, index+1, 0);
                            handler.sendMessageDelayed(msg, (index+1<getEnemies().size())? 700L:0L);
                        } else {
                            Message msg = Message.obtain(handler, MSG_ENEMY_ATTACK_LIST, index, subindex+1, actionList);
                            handler.sendMessageDelayed(msg, (int)((Shake.DEFAULT_DURATION+0.05f) * 1000));
                        }

                    }
                });
                break;
            }
            case MSG_FIRE_SKILL_INIT:
                getScene().onFireSkills(true);
                // TODO: play fired animation...

                obtainMessage(MSG_FIRE_SKILL, 0, msg.arg2, msg.obj).sendToTarget();
                break;

            case MSG_FIRE_SKILL:
            {
                @SuppressWarnings("unchecked")
                final Member caster = (Member) msg.obj;
                final int skill = msg.arg2;
                List<ActiveSkill> data = (skill==1)? caster.getActiveSkill():caster.getActiveSkill2();
                final int index = msg.arg1;
                LogUtil.e(data.get(0).getType());
                if(data != null && index < data.size()) {
                    fireSkill(caster, data.get(index), new ICastCallback() {

                        @Override
                        public void onCastFinish(boolean casted) {
                            Message msg = Message.obtain(handler, MSG_FIRE_SKILL, index+1, skill, caster);
                            sendMessageDelayed(msg, 300);
                        }
                    });
                } else {
                    getScene().onFireSkills(false);
                }

                break;
            }
            case MSG_POISON_DAMAGE: {
            	List<Enemy> enemies = getEnemies();
                // change prop change first... and check all enemy destroyed or not
                for(Enemy e : enemies) {
                    e.afterPlayerAttack();
                }

            	List<Pair<Enemy, BuffOnEnemy>> target = new ArrayList<Pair<Enemy, BuffOnEnemy>>();
            	for(Enemy enemy : enemies) {
            		if (enemy.dead()) {
            			continue;
            		}
            		List<BuffOnEnemy> debuff = enemy.getDebuffList();
            		for(BuffOnEnemy buff : debuff) {
            			if(buff.isSkill(Type.POISON)) {
            				target.add(new Pair<Enemy, BuffOnEnemy>(enemy, buff));
            				break;
            			}
            		}
            	}
                List<Match> matches = getScene().getMatches();
            	boolean hasLstAttack = matches.size()>0 &&
            			(isAdditionAttack(mMemberList[0].info) || isAdditionAttack(mMemberList[5].info)
                        || hasHeartArrow(matches) || hasHeartSquare(matches));
            	final int nextMsg = (hasLstAttack)? MSG_ADDITION_ATTACK:MSG_END_OF_ATTACK;

                if(matches.size() == 0) { // special case, no removal cause no hp recover
                    double addition = 0.0;
                    if (mSkillList.containsKey(Constants.SK_TURN_RECOVER)) {
                        ActiveSkill tr = (ActiveSkill) mSkillList.get(Constants.SK_TURN_RECOVER).skill;
                        addition = tr.getData().get(2) * mTeamData.getHp() / 100.0;
                    }
                    if(addition > 0) {
                        recoverPlayerHp((int)addition);
                    }
                }

            	int size = target.size();
            	if(size>0) {
            		final AtomicInteger counter = new AtomicInteger(size);
            		for(Pair<Enemy, BuffOnEnemy> pair : target) {
            			Enemy enemy = pair.first;
            			enemy.dealtDamageDirect(AttackValue.create(pair.second.getData().get(0), -1, AttackValue.ATTACK_SINGLE), 0.6f,
                                new AnimationCallback() {

                                    @Override
                                    public void animationDone() {
                                    	if(counter.decrementAndGet() == 0) {
                                    		sendEmptyMessage(nextMsg);
                                    	}
                                    }
                                });
            		}
            	} else {
            		sendEmptyMessage(nextMsg);
            	}

            	break;
            }
            case MSG_ADDITION_ATTACK: {
                int arg = msg.arg1;
                if(arg == 0) {
                    int total = 0;
                    for (int i = 0; i < 6; i += 5) {
                        for (LeaderSkill s : mMemberList[i].info.getLeaderSkill()) {
                            if (s.getType() == LeaderSkillType.LST_ATTACK) {
                                total += s.getData().get(0) * mMemberList[i].info.getAtk(false);
                            }
                        }
                    }
                    if (total > 0) {
                        final int damage = total;
                        postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                fireAdditionAttack(AttackValue.create(damage, 2, AttackValue.ATTACK_MULTIPLE), mStageEnemies.get(currentStage));
                                sendMessageDelayed(Message.obtain(ExecutorHandler.this, MSG_ADDITION_ATTACK, 1, 0), 300);
                            }
                        }, 500L);

                    } else {
                        sendMessage(Message.obtain(ExecutorHandler.this, MSG_ADDITION_ATTACK, 1, 0));
                    }
                } else {
                    int heartArrow = 0;
                    List<Match> matches = getScene().getMatches();
                    boolean hasArrow = hasHeartArrow(matches), hasSquare = hasHeartSquare(matches);

                    for (Member m : mMemberList) {
                        int c = getTargetAwokenCount(m.info,AwokenSkill.HEART_ARROW);
                        if (hasArrow && c > 0) {
                            heartArrow += c;
                        }
                        int s = getTargetAwokenCount(m.info,AwokenSkill.HEART_SQUARE);
                        if (hasSquare && s > 0) {
                            heartArrow += 99;
                        }
                    }
                    // check direct attack or not
                    Team team = getTeam();
                    MonsterInfo leader = team.getMember(0);
                    MonsterInfo friend = team.getMember(5);
                    if(leader != null && !getMember(0).isBinded()) {
                        for(LeaderSkill s : leader.getLeaderSkill()) {
                            if (s.getType() == LeaderSkillType.LST_COLOR_DIRECT_ATTACK ||
                                    s.getType() == LeaderSkillType.LST_MULTI_ORB_DIRECT_ATTACK ||
                            s.getType() == LeaderSkillType.LST_TARGET_ORB_DIRECT_ATTACK) {
                                double factor = DamageCalculator.getLeaderFactor(mTeamData.team(), s, leader, getScene().getMatches(), null, false, true);
                                if(factor > 1.0) {
                                    heartArrow += factor;
                                }
                            }
                        }
                    }
                    if(friend != null && !getMember(5).isBinded()) {
                        for(LeaderSkill s : friend.getLeaderSkill()) {
                            if (s.getType() == LeaderSkillType.LST_COLOR_DIRECT_ATTACK ||
                                    s.getType() == LeaderSkillType.LST_MULTI_ORB_DIRECT_ATTACK ||
                                    s.getType() == LeaderSkillType.LST_TARGET_ORB_DIRECT_ATTACK) {
                                double factor = DamageCalculator.getLeaderFactor(mTeamData.team(), s, friend, getScene().getMatches(), null, false, true);
                                if(factor > 1.0) {
                                    heartArrow += factor;
                                }
                            }
                        }
                    }
                    if (heartArrow > 0) {
                        List<Enemy> enemies = getEnemies();
                        final AtomicInteger counter = new AtomicInteger(enemies.size());
                        for (Enemy enemy : enemies) {
                            if (enemy.dead()) {
                                if (counter.decrementAndGet() == 0) {
                                    sendEmptyMessage(MSG_END_OF_ATTACK);
                                }
                            }
                            enemy.dealtDamageDirect(AttackValue.create(heartArrow, -1, AttackValue.ATTACK_SINGLE), 0.6f,
                                    new AnimationCallback() {

                                        @Override
                                        public void animationDone() {
                                            if (counter.decrementAndGet() == 0) {
                                                sendEmptyMessage(MSG_END_OF_ATTACK);
                                            }
                                        }
                                    });
                        }
                    } else {
                        sendEmptyMessage(MSG_END_OF_ATTACK);
                    }
                }

                break;
            }
            case MSG_END_OF_ATTACK: {
                // hide damage
                getScene().engine.runOnUpdateThread(new Runnable() {

                    @Override
                    public void run() {
                        PlaygroundGameScene scene = getScene();
                        for (int i = 0; i < 6; ++i) {
                            if (mMemberList[i] != null) {
                                mMemberList[i].sprite.setAlpha(1f);
                            }
                        }

                        for (Pair<Text, Text> pair : mDamageList) {
                            pair.first.setVisible(false);
                            pair.second.setVisible(false);

                            scene.detachChild(pair.first);
                            scene.detachChild(pair.second);
                        }

                    }
                });

                List<Enemy> list = mStageEnemies.get(currentStage);
                boolean destroyed = true;
                // change prop change first... and check all enemy destroyed or not
                for (Enemy e : list) {
                    e.afterPoisonAttack();
                    if (!e.dead()) {
                        destroyed = false;
                    }
                }
                try {
                    List<String> removeList = new ArrayList<String>();
                    for (String key : mSkillList.keySet()) {
                        if (Constants.SK_TIME_CHANGE.equals(key) ||
                                Constants.SK_TIME_CHANGE_X.equals(key) ||
                                Constants.SK_DROP_ONLY.equals(key) ||
                                Constants.SK_DROP_RATE.equals(key) ||
                                Constants.SK_DROP_RATE_NEG.equals(key) ||
                                Constants.SK_DROP_LOCK.equals(key) ||
                                Constants.SK_SKILL_LOCK.equals(key) ||
                                Constants.SK_POWER_UP.equals(key) ||
                                Constants.SK_AWOKEN_LOCK.equals(key) ||
                                Constants.SK_ADD_COMBO.equals(key) ||
                                Constants.SK_TURN_RECOVER.equals(key) ||
                                Constants.SK_ENHANCE_ORB.equals(key) ||
                                Constants.SK_RCV_UP.equals(key) ||
                                Constants.SK_NO_DROP.equals(key) ||
                                Constants.SK_CLOUD.equals(key) ||
                                Constants.SK_HP_CHANGE.equals(key) ||
                                Constants.SK_LOCK_REMOVE.equals(key)) {
                            if (mSkillList.get(key).countDown()) {
                                removeList.add(key);
                            }
                        }
                    }
                    if (mLeaderSwitch != null && mLeaderSwitch.countDown()) {
                        int index = mLeaderSwitch.skill.getData().get(1);
                        switchMember(index);

                        mLeaderSwitch.detach();
                        mLeaderSwitch = null;
                    }

                    for (String key : removeList) {
                        SkillEntity entity = mSkillList.remove(key);
                        if (Constants.SK_TIME_CHANGE.equals(key)) {
                            getScene().setAdditionMoveTime(0);
                        } else if (Constants.SK_TIME_CHANGE_X.equals(key)) {
                            getScene().setAdditionMoveTimeX(0);
                        } else if (Constants.SK_DROP_RATE.equals(key) ||
                                Constants.SK_DROP_RATE_NEG.equals(key)) {
                            boolean isNegative = entity.skill.getData().get(1) >= 6;
                            getScene().removeDropRateSkill(isNegative);
                        } else if (Constants.SK_DROP_LOCK.equals(key)) {
                            getScene().removeDropLock();
                        } else if (Constants.SK_AWOKEN_LOCK.equals(key)) {
                            calcNullAwoken();
                        } else if (Constants.SK_ADD_COMBO.equals(key)) {
                            getScene().removeAddComboSkill();
                        } else if (Constants.SK_ENHANCE_ORB.equals(key)) {
                            getScene().removeSkill(key);
                        } else if (Constants.SK_NO_DROP.equals(key)) {
                            getScene().removeSkill(key);
                        } else if (Constants.SK_CLOUD.equals(key)) {
                            getScene().removeSkill(key);
                        } else if (Constants.SK_LOCK_REMOVE.equals(key)) {
                            getScene().removeSkill(key);
                        } else if (Constants.SK_DROP_ONLY.equals(key)) {
                            getScene().removeSkill(key);
                        } else if (Constants.SK_HP_CHANGE.equals(key)) {
                            mTeamData.setMaxHp(-1);
                        }
                    }

                    if (getScene().getMatches().size() > 0) {
                        for (int i = 0; i < 6; ++i) {
                            if (mMemberList[i] != null) {
                                mMemberList[i].countDownSkill();
                            }
                        }
                    }

                    if (destroyed) {
                        flow.trigger(Events.enemyDestroyed, padContext);
                    } else if (getScene().isClearBoard()) {
                        // still player's turn after clearing board
                        flow.trigger(Events.stillMyTurn, padContext);
                    } else {
                        flow.trigger(Events.attackDone, padContext);
                    }
                } catch (LogicViolationError e) {
                }

                break;
            }
            case MSG_START_ATTACK: 
            {
                PlaygroundGameScene scene = getScene();
                List<Match> matches = scene.getMatches();
                int bombCount = scene.popBombCount();
                boolean hasMatches = matches.size()>0;
                if(hasMatches) {
                    scene.setBoardAlpha(1.0f, 0.6f);
                }
                
                boolean awokenLocked = mSkillList.containsKey(Constants.SK_AWOKEN_LOCK);
                Map<String, Object> settings = new HashMap<String, Object>();
                settings.put("nullAwoken", awokenLocked || nullAwoken);
                settings.put("skillFired", firedThisTurn);
                settings.put("singleUp", singleUp.value());
                
                ActiveSkill sk = null;
                if(mSkillList.containsKey(Constants.SK_RCV_UP)) {
                	sk = (ActiveSkill)mSkillList.get(Constants.SK_RCV_UP).skill;
                }
                double addition = 0.0;
                if(mSkillList.containsKey(Constants.SK_TURN_RECOVER)) {
                    ActiveSkill tr = (ActiveSkill) mSkillList.get(Constants.SK_TURN_RECOVER).skill;
                    addition = tr.getData().get(2) * mTeamData.getHp() / 100.0;
                }
                
                double recovery = DamageCalculator.calculateRecovery(mTeamData.team(), matches, sk, settings);
                double poison = DamageCalculator.calculatePoison(mTeamData.team(), matches, null) +
                        DamageCalculator.calculateBomb(mTeamData.getHp(), bombCount);

                // bind recover
                int bindRecoverCount = 0;
                boolean hasBinded = false;
                
                for(int i=0; i<6; ++i) {
                	if(mMemberList[i] == null) {
                		continue;
                	}
                	MonsterInfo info = mMemberList[i].info;
                	if (info == null) {
                		continue;
                	}
                	if(!awokenLocked && !mMemberList[i].isBinded()) {
                		bindRecoverCount += getTargetAwokenCount(info, AwokenSkill.RECOVER_BIND);
                	} else {
                		hasBinded = true;
                	}
                }
                if(hasBinded && bindRecoverCount>0) {
                	int recover = 0;
                	for(Match match : matches) {
                		if(match.type == 2 && match.isOneRow()) {
                			recover += bindRecoverCount * 3;
                		}
                	}
                	final int bindRecover = recover;
                	bindRecover(bindRecover);
                }
                
                int diff = (int)(recovery + addition - poison);
                if (diff > 0 ) {
                    recoverPlayerHp(diff);
                } else if (diff < 0){
                	attackByOthers(diff);
                }
                // reset skill flag
                firedThisTurn = false;
                
                long delay = 0L;
                if(hasMatches && mTeamData.getCurrentHp()<getTeamHpTotal()) {
                	delay = 500L;
                }
                sendMessageDelayed(Message.obtain(handler, MSG_CHECK_HP, hasMatches?1:0, 0, msg.obj), delay);
                break;
            }
            case MSG_CHECK_HP: {
            	boolean hasMatches = msg.arg1 == 1;
                // auto recovery
                int recover = 0;
            	boolean awokenLocked = mSkillList.containsKey(Constants.SK_AWOKEN_LOCK);
                if (hasMatches && mTeamData.getCurrentHp()<getTeamHpTotal()) {
	                int count = 0;
	                if(!awokenLocked) {
		                for(int i=0; i<6; ++i) {
		                	if(mMemberList[i] == null || 
		                			mMemberList[i].isBinded()) {
		                		continue;
		                	}
		                	count += getTargetAwokenCount(mMemberList[i].info, AwokenSkill.AUTO_RECOVER);
		                }
	                }
	                if(count>0) {
	                	recoverPlayerHp(count*1000);
	                }
	                

	                Member leader = mMemberList[0];
	                Member friend = mMemberList[5];
	                if(leader != null && !leader.isBinded() && leader.info != null) {
	                	List<LeaderSkill> ls = leader.info.getLeaderSkill();
	                	for(LeaderSkill s : ls) {
	                		if(LeaderSkill.LeaderSkillType.LST_HP_RECOVER.equals(s.getType())) {
	                			int type = s.getData().get(0);
	                			int x = s.getData().get(1);
	                			if(type == 0) {
	                				recover = x * leader.info.getRecovery(false);
	                			} else {
	                				recover = x;
	                			}
	                		}
	                	}
	                }
	                if(friend != null && !friend.isBinded() && friend.info != null) {
	                	int addition = 0;
	                	List<LeaderSkill> ls = friend.info.getLeaderSkill();
	                	for(LeaderSkill s : ls) {
	                		if(LeaderSkill.LeaderSkillType.LST_HP_RECOVER.equals(s.getType())) {
	                			int type = s.getData().get(0);
	                			int x = s.getData().get(1);
	                			if(type == 0) {
	                				addition = x * friend.info.getRecovery(false);
	                			} else {
	                				addition = x;
	                			}
	                		}
	                	}
	                	recover += addition;
	                }
                }
                if(recover > 0) {
                    final int r = recover;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recoverPlayerHp(r);
                            if (mTeamData.getCurrentHp() <= 0) {
                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        try {
                                            clearPhase();
                                            flow.trigger(Events.hpZero, padContext);
                                        } catch (LogicViolationError e1) {
                                        }
                                    }
                                }, 1500);
                            }
                        }
                    }, 200);
                } else {
                    if (mTeamData.getCurrentHp() <= 0) {
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    clearPhase();
                                    flow.trigger(Events.hpZero, padContext);
                                } catch (LogicViolationError e1) {
                                }
                            }
                        }, 1500);
                        return;
                    }
                }
                List<Enemy> enemies = mStageEnemies.get(currentStage);
                // change prop change first... and check all enemy destroyed or not
                for(Enemy enemy : enemies) {
                    enemy.beforePlayerAttack();
                }
                
                @SuppressWarnings("unchecked")
                final List<Pair<AttackValue, AttackValue>> data = (List<Pair<AttackValue, AttackValue>>) msg.obj;
                for(int i=0; i<6; ++i) {
                	if(mMemberList[i] == null) {
                		continue;
                	}
                	mMemberList[i].sprite.setAlpha(.5f);
                    Pair<AttackValue, AttackValue> attacks = data.get(i);
                    Pair<Text, Text> damageText = mDamageList.get(i);

                    if(attacks.first.damage > 0.0) {
                    	
                        damageText.first.setText(getShortDamage(attacks.first.damage));
                        damageText.first.setVisible(true);
                    }
                    if(attacks.second.damage > 0.0) {
                        damageText.second.setText(getShortDamage(attacks.second.damage));
                        damageText.second.setVisible(true);
                    }
                }

                getScene().engine.runOnUpdateThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        Scene scene = getScene();
                        for(Pair<Text, Text> pair : mDamageList) {
                            scene.attachChild(pair.first);
                            scene.attachChild(pair.second);
                        }
                        Message.obtain(handler, MSG_PLAYER_ATTACK, 0, 0, data).sendToTarget();
                    }
                });
            	break;
            }
            case MSG_PLAYER_ATTACK:
                int index = msg.arg1; // current index
                int major = msg.arg2;
                
                List<Pair<AttackValue, AttackValue>> data = (List<Pair<AttackValue, AttackValue>>) msg.obj;
                Pair<AttackValue, AttackValue> pair = data.get(index);
                
                // if there's no attack... skip it
                if ((major == 0 && pair.first.damage <= 0.0) ||
                        (major == 1 && pair.second.damage <= 0.0)) {
                    attackNext(data, index, major);
                    return ;
                }
                
                final AttackValue attack = (major==0)? pair.first:pair.second;
                List<Enemy> target = AttackModule.findSuitableEnemies(mTeamData.getMember(index),mStageEnemies.get(currentStage), attack, false);
                if (target.size() == 0) {
                    sendEmptyMessage(MSG_END_OF_ATTACK);
                    return ;
                }
                
                float space = GameActivity.SCREEN_WIDTH / 6.0f * index;
                float fromY = GameActivity.SCREEN_HEIGHT / 2.0f;
                AtomicInteger counter = new AtomicInteger(target.size());
                if (attack.attackType == AttackValue.ATTACK_MULTIPLE) {
                    attackAll(attack, target, data, index, major, counter);
                } else {
                    for(Enemy to : target) {
                        attackTo(attack, to, space+44f, fromY, data, index, major, counter);
                    }
                }
                break;

            }
        }
        
        private String getShortDamage(double damage) {
        	String str = null;
        	if(damage < 1000) {
        		str = String.format("%.2f", damage);
        	} else if(damage < 1000000) {
        		str = String.format("%.2fK", (damage/1000));
        	} else {
        		str = String.format("%.2fM", (damage/1000000));
        	}
        	return str;
        }
        
        private void fireAdditionAttack(final AttackValue attack, final List<Enemy> target) {
            Sprite s = target.get(0).sprite;
            final IEntity bullet = mBulletPool.obtainPoolItem();
            float fromY = s.getY() + s.getHeight()/2f;
            bullet.setPosition(0, fromY);
            bullet.setColor(ATTACK_COLOR[attack.prop]);
            bullet.setScale(1.5f);
            MoveModifier modifier = new MoveModifier(0.20f, 0, GameActivity.SCREEN_WIDTH, fromY, fromY){
                @Override
                protected void onModifierFinished(final IEntity pItem) {
                	//MonsterInfo info = mTeamData.getMember(index);
                    for(Enemy enemy : target) {
                    	if(!enemy.dead()) { 
	                    	enemy.dealtDamageByLST(attack, 0.2f, new AnimationCallback() {
								
								@Override
								public void animationDone() {
								}
							});
                    	}
                    }
                    pItem.setScale(1.0f);
                    mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
                        
                        @Override
                        public void run() {
                            mSceneRef.get().detachChild(pItem);
                            mBulletPool.recyclePoolItem(pItem);
                        }
                    });
                    
                }
            };
            modifier.setAutoUnregisterWhenFinished(true);
            bullet.registerEntityModifier(modifier);
            
            mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
                
                @Override
                public void run() {
                    mSceneRef.get().attachChild(bullet);
                }
            });
        }
        
        private void attackAll(final AttackValue attack, final List<Enemy> target,
                final List<Pair<AttackValue, AttackValue>> data, 
                final int index, final int major,
                final AtomicInteger counter) {
            Sprite s = target.get(0).sprite;
            final IEntity bullet = mBulletPool.obtainPoolItem();
            float fromY = s.getY() + s.getHeight()/2f;
            bullet.setPosition(0, fromY);
            bullet.setColor(ATTACK_COLOR[attack.prop]);
            bullet.setScale(1.5f);
            MoveModifier modifier = new MoveModifier(0.20f, 0, GameActivity.SCREEN_WIDTH, fromY, fromY){
                @Override
                protected void onModifierFinished(final IEntity pItem) {
                	MonsterInfo info = mTeamData.getMember(index);
                    for(Enemy enemy : target) {
                        enemy.dealtDamageBySkill(info, attack,0.1f, new AnimationCallback() {
    
                            @Override
                            public void animationDone() {
                                if(counter.decrementAndGet()==0) {
                                    attackNext(data, index, major);
                                }
                            }
                        });
                    }
                    pItem.setScale(1.0f);
                    mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
                        
                        @Override
                        public void run() {
                            try {
                                mSceneRef.get().detachChild(pItem);
                                mBulletPool.recyclePoolItem(pItem);
                            } catch(Exception e) {

                            }
                        }
                    });
                    
                }
            };
            modifier.setAutoUnregisterWhenFinished(true);
            bullet.registerEntityModifier(modifier);
            
            mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
                
                @Override
                public void run() {
                    try {
                        mSceneRef.get().attachChild(bullet);
                    } catch(Exception e) {
                    }
                }
            });
        }
        
        private void attackTo(final AttackValue attack, final Enemy enemy, float fromX, float fromY,
                final List<Pair<AttackValue, AttackValue>> data, 
                final int index, final int major, final AtomicInteger counter) {
            Sprite s = enemy.sprite;
            float toX = s.getX() + s.getWidth() / 2;
            float toY = s.getY() + s.getHeight() / 2;
            final IEntity bullet = mBulletPool.obtainPoolItem();
            bullet.setPosition(fromX, fromY);
            bullet.setColor(ATTACK_COLOR[attack.prop]);
            
            MoveModifier modifier = new MoveModifier(0.20f, fromX, toX, fromY, toY){
                @Override
                protected void onModifierFinished(final IEntity pItem) {
                	MonsterInfo info = mTeamData.getMember(index);
                    enemy.dealtDamageBySkill(info, attack,0.1f, new AnimationCallback() {

                        @Override
                        public void animationDone() {
                            if(counter.decrementAndGet()==0) {
                                attackNext(data, index, major);
                            }
                        }
                    });
                    mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
                        
                        @Override
                        public void run() {
                        	try {
	                            mSceneRef.get().detachChild(pItem);
	                            mBulletPool.recyclePoolItem(pItem);
                        	} catch(Exception e) {
                        	}
                        }
                    });
                    
                }
            };
            modifier.setAutoUnregisterWhenFinished(true);
            bullet.registerEntityModifier(modifier);
            
            mSceneRef.get().attach(bullet);
        }
    }
    
    private static class PadContext extends StatefulContext {
    }
    
    public boolean hasDebuff(String skill, int val) {
    	if(mSkillList.containsKey(skill)) {
    		SkillEntity entity = mSkillList.get(skill);
    		List<Integer> data = entity.skill.getData();
    		if(Constants.SK_TIME_CHANGE.equals(skill)) {
                return entity.skill.getData().get(1) < 0;
    		} else if(Constants.SK_DROP_RATE_NEG.equals(skill) ||
    				Constants.SK_DROP_RATE.equals(skill)	) {
    			boolean hasDebuf = data.get(1) == val;
    			if(data.size()>3) {
    				return hasDebuf || data.get(3) == val;
    			}
    			return hasDebuf;
    		} else if (Constants.SK_RCV_UP.equals(skill)) {
    		    return data.get(1) < 100;
    		} else if (Constants.SK_SKILL_LOCK.equals(skill)) {
    		    return true;
            } else if (Constants.SK_AWOKEN_LOCK.equals(skill)) {
    		    return true;
            }
    	} else if (mSkillList.containsKey(Constants.SK_TIME_CHANGE_X) &&
                Constants.SK_TIME_CHANGE_X.startsWith(skill)) {
            SkillEntity entity = mSkillList.get(skill+"X");
            return entity.skill.getData().get(1) < 1000;
        }
    	return false;
    }
    
    
    private boolean isEnemiesDestroy() {
        List<Enemy> list = mStageEnemies.get(currentStage);
        // change prop change first... and check all enemy destroyed or not
        for(Enemy e : list) {
            if (!e.dead()) {
                return false;
            }
        }
        return true;
    }
    
    private void checkEnemyState() {
        List<Enemy> list = mStageEnemies.get(currentStage);
        // change prop change first...

        for(Enemy e : list) {
            e.updateEnemyState();
        }
        // check all enemy destroyed or not
        boolean destroyed = true;
        for(Enemy enemy : list) {
            if(enemy.getHp()>0) {
                destroyed = false;
                break;
            }
        }
        try {
            if(destroyed) {
                flow.trigger(Events.enemyDestroyed, padContext);
            }
        } catch (LogicViolationError e) {
        }
    }
    
    public void attackToDirect(final AttackValue attack, final Enemy enemy, Member from,
            final AtomicInteger counter, final ICastCallback callback) {
        Sprite s = enemy.sprite;
        Sprite fs = from.sprite;
        float toX = s.getX() + s.getWidth() / 2;
        float toY = s.getY() + s.getHeight() / 2;
        final IEntity bullet = mBulletPool.obtainPoolItem();
        float x = fs.getX() + fs.getWidth() / 2;
        float y = fs.getY() + fs.getHeight() / 2;
        bullet.setPosition(x, y);
        //bullet.setColor(ATTACK_COLOR[attack.prop]);
        
        MoveModifier modifier = new MoveModifier(0.25f, x, toX, y, toY){
            @Override
            protected void onModifierFinished(final IEntity pItem) {
                enemy.dealtDamageDirect(attack,0.1f, new AnimationCallback() {

                    @Override
                    public void animationDone() {
                        if(counter.decrementAndGet()==0) {
                            callback.onCastFinish(true);
                        }
                    }
                });
                mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        mSceneRef.get().detachChild(pItem);
                        mBulletPool.recyclePoolItem(pItem);
                    }
                });
            }
        };
        modifier.setAutoUnregisterWhenFinished(true);
        bullet.registerEntityModifier(modifier);
        
        mSceneRef.get().attach(bullet);
    }
    
    @Override
    public int attackVampire(final MonsterInfo info, final AttackValue attack, final Enemy enemy, Member from,
            final AtomicInteger counter, final ICastCallback callback, final int percent) {
    	Sprite s = enemy.sprite;
        Sprite fs = from.sprite;
        float toX = s.getX() + s.getWidth() / 2;
        float toY = s.getY() + s.getHeight() / 2;
        final IEntity bullet = mBulletPool.obtainPoolItem();
        float x = fs.getX() + fs.getWidth() / 2;
        float y = fs.getY() + fs.getHeight() / 2;
        bullet.setPosition(x, y);
        bullet.setColor(ATTACK_COLOR[attack.prop]);
        
        MoveModifier modifier = new MoveModifier(0.25f, x, toX, y, toY){
            @Override
            protected void onModifierFinished(final IEntity pItem) {
            	
                double damage = enemy.dealtDamageBySkill(info, attack,0.1f, new AnimationCallback() {

                    @Override
                    public void animationDone() {
                        if(counter.decrementAndGet()==0) {
                            callback.onCastFinish(true);
                        }
                    }
                });
                int added = (int)(damage * percent / 100.0);
                recoverPlayerHp(added);
                
                mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        mSceneRef.get().detachChild(pItem);
                        mBulletPool.recyclePoolItem(pItem);
                    }
                });
            }
        };
        modifier.setAutoUnregisterWhenFinished(true);
        bullet.registerEntityModifier(modifier);
        
        mSceneRef.get().attach(bullet);
        
        return 0;
    }
    
    @Override
    public int attackTo(final MonsterInfo info, final AttackValue attack, final Enemy enemy, Member from,
            final AtomicInteger counter, final ICastCallback callback) {
        Sprite s = enemy.sprite;
        Sprite fs = from.sprite;
        float toX = s.getX() + s.getWidth() / 2;
        float toY = s.getY() + s.getHeight() / 2;
        final IEntity bullet = mBulletPool.obtainPoolItem();
        float x = fs.getX() + fs.getWidth() / 2;
        float y = fs.getY() + fs.getHeight() / 2;
        bullet.setPosition(x, y);
        bullet.setColor(ATTACK_COLOR[attack.prop]);
        
        MoveModifier modifier = new MoveModifier(0.25f, x, toX, y, toY){
            @Override
            protected void onModifierFinished(final IEntity pItem) {
            	
                enemy.dealtDamageBySkill(info, attack,0.1f, new AnimationCallback() {

                    @Override
                    public void animationDone() {
                        if(counter.decrementAndGet()==0) {
                            callback.onCastFinish(true);
                        }
                    }
                });
                mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        mSceneRef.get().detachChild(pItem);
                        mBulletPool.recyclePoolItem(pItem);
                    }
                });
            }
        };
        modifier.setAutoUnregisterWhenFinished(true);
        bullet.registerEntityModifier(modifier);
        
        mSceneRef.get().attach(bullet);
        
        return 0;
    }
    
    private static class StateMachineExecutor implements Executor {
        private Handler handler;
        
        public StateMachineExecutor(Handler h) {
            handler = h;
        }
        
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
        
    }
    
    public PadEnvironment(PlaygroundGameScene scene) {
        mSceneRef = new WeakReference<PlaygroundGameScene>(scene);
//        final Activity activity = scene.getActivity();
//        activity.runOnUiThread(new Runnable() {
//
//			@Override
//			public void run() {
//
//			}
//		});

    }
    
    public PlaygroundGameScene getScene() {
        return mSceneRef.get();
    }

    
    private void loadSinglePowerUp() {
		SharedPreferences pref = getScene().getActivity().getSharedPreferences("singleUp", Context.MODE_PRIVATE);
		int item = pref.getInt("item"+ComboMasterApplication.getsInstance().getTargetNo(), 0);
		singleUp = SinglePowerUp.from(item);
    }
    
    public void create(String stage) {
        mTeamData = new Team();
        
        mStoneUsed = 0;
        if (stage.equals("201806_10") || stage.equals("quest_201901_lv10")) {
            nullAwoken = true;
            ComboMasterApplication.getsInstance().setNullAwokenSkill(true);
        }

        if (stage.equals("quest_201901_lv9")) {
            getScene().setDefaultTime(4000);
        }

        if (stage.equals("quest_201903_lv10")) {
            getScene().setNormalDrop(false);
        }
        
        int visible = View.VISIBLE;
//        int visible = View.INVISIBLE;
//        if(ComboMasterApplication.getsInstance().getAdSceneType()==1) {
//        	visible = View.VISIBLE;
//        }
        ((GameActivity)getScene().getActivity()).setAdViewVisible(visible);
        mStage = stage;

        ATTACK_COLOR[0] = new Color(0.8f, 0.0f, 0.8f);
        ATTACK_COLOR[1] = Color.RED;
        ATTACK_COLOR[2] = Color.WHITE;
        ATTACK_COLOR[3] = Color.YELLOW;
        ATTACK_COLOR[4] = new Color(.5f, .5f, 1.0f);
        ATTACK_COLOR[5] = Color.GREEN;
        
        loadSinglePowerUp();
        
        bindFlow();
        initMemberSprite();
        initStageSprite();
        
        HandlerThread ht = new HandlerThread("pad_executor");
        ht.start();
        Looper looper = ht.getLooper();
        handler = new ExecutorHandler(looper);
        flow.executor(new StateMachineExecutor(handler));

        mBulletPool = new SpritePool(ResourceManager.getInstance().getTextureRegion("texture.png"));
        
        // game start
        flow.start(padContext);
    }
    
    // method is run on update thread
    private void initMemberSprite() {
        ResourceManager res = ResourceManager.getInstance();
        VertexBufferObjectManager vbom = getScene().vbom;
        for(int i=0; i<6; ++i) {
            MonsterInfo info = mTeamData.getMember(i);
            if (info != null) {
                Member member = null;
                
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
                if (region != null) {
                    member = new Member(this, info);
                    member.init(Constants.OFFSET_X + i * (88 + 1),
                            Constants.OFFSET_Y - 15 - 8 - 8 - 88, 
                            region, vbom);
                }
                mMemberList[i] = member;
            }
        }
        PlaygroundGameScene scene = getScene();
        for(int i=0; i<6; ++i) {
            Member member = mMemberList[i];
            if (member != null) {
                scene.attachChild(member.sprite);
            }
        }
    }
    
    AlertDialog inputDialog = null;
    EditText edit = null;
    
    private void showJumpDialog() {
    	final Activity activity = getScene().getActivity();

        if(inputDialog == null) {
            final EditText input = new EditText(activity);
            edit = input;
            input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        	inputDialog = new AlertDialog.Builder(activity)
	                .setMessage(R.string.move_to_stage)
	                .setView(input)
	                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							inputDialog.dismiss();
						}
					})
	                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                        try {
	                        	int jumped = Integer.parseInt(input.getText().toString());
	                        	if(jumped > currentStage && mStageEnemies.get(jumped, null) != null) {
	                        		final int previous = currentStage;
	                        		currentStage = jumped-1;
	                        		((GameActivity)getScene().getActivity()).setAdViewVisible(View.VISIBLE);
	                        		getScene().engine.runOnUpdateThread(new Runnable() {
										
										@Override
										public void run() {
											jumping = true;
						        			jumpUsed = true;
						        			
						        			List<Enemy> list = mStageEnemies.get(previous);
						        			for(Enemy enemy : list) {
						        				enemy.setDead();
						        			}
						        			
						                    try {
						                        flow.trigger(Events.enemyDestroyed, padContext);
						                    } catch (LogicViolationError e) {
						                    }
										}
									});
	                        	} else {
	                        		Toast.makeText(activity, R.string.move_error, Toast.LENGTH_SHORT).show();
	                        	}
	                        } catch(Exception e) {
	                        	Toast.makeText(activity, "input error", Toast.LENGTH_SHORT).show();
	                        }
	                        inputDialog.dismiss();
	                    }
	                }).create();
        }
        edit.setText(""+(currentStage+1));
        if(!inputDialog.isShowing()) {
        	inputDialog.show();
        }
    }
    
    boolean jumping = false;
    private void initStageSprite() {
        ResourceManager res = ResourceManager.getInstance();
        VertexBufferObjectManager vbom = getScene().vbom;

        mJumpTo = new Text(0, 0, res.getFont(), "JUMP", vbom) {
        	@Override
        	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
        			float pTouchAreaLocalX, float pTouchAreaLocalY) {
        		if(jumping == false && getScene().getState() == GameState.GAME_PLAYER_TURN) {
        			final GameActivity activity = (GameActivity) getScene().getActivity();
        			activity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							showJumpDialog();
						}
					});


        			
        		}
        		
        		return true;
        	}
        };
        getScene().registerTouchArea(mJumpTo);
        
        mStageTop = new Text(0f, 0f, res.getFontStroke(), "Stage 01/01", vbom);
        mStageTop.setPosition(GameActivity.SCREEN_WIDTH - 8 - mStageTop.getWidth(), 84f);
        mJumpTo.setPosition(GameActivity.SCREEN_WIDTH - 8 - mJumpTo.getWidth(), 84f + mStageTop.getHeight());
        getScene().attach(mJumpTo);
        getScene().attach(mStageTop);
        mStageText = new Text(0f, 0f, res.getFont(),
                "Stage 01/01", vbom);
        mStageText.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        float y = mMemberList[0].sprite.getY();
        // prepare damage text list
        for(int i=0; i<6; ++i) {
            Text text1 = new Text(0, 0, res.getFontStroke(), "0000000000", 10, vbom);
            Text text2 = new Text(0, 0, res.getFontStroke(), "0000000000", 10, vbom);
        	
            text1.setVisible(false);
            text2.setVisible(false);
            
            text1.setZIndex(5);
            text2.setZIndex(5);
            
            text1.setPosition(88*i+5, y);
            text2.setPosition(88*i+5, y+20);
            
            MonsterInfo info = mTeamData.getMember(i);
            if (info!=null) {
                Pair<Integer, Integer> props = info.getProps();
                text1.setColor(ATTACK_COLOR[props.first]);
                if(props.second!=-1) {
                    text2.setColor(ATTACK_COLOR[props.second]);
                }
            }

            mDamageList.add(new Pair<Text, Text>(text1, text2));
        }
        
        mHp = new HPEntity(this);
        

        mHp.init(getTeamHpTotal());
        mHp.attachScene();
        //mHp.attach();
        
    }
    
    private int getTeamHpTotal() {
        int hpTotal = mTeamData.getHp();
        if(singleUp.value() == SinglePowerUp.HP.value()) {
        	hpTotal = (int)(hpTotal * 1.05);
        } else if (singleUp.value() == SinglePowerUp.HP_UP.value()) {
        	hpTotal = (int)(hpTotal * 1.15);
        }
        return hpTotal;
    }
    
    public void finish() {
    	if(mLeaderSwitch != null) {
    		// change back to normal
    		int index = mLeaderSwitch.skill.getData().get(1);
    		switchMember(index);
    		mLeaderSwitch.detach();
    		mLeaderSwitch = null;
    	}

        ComboMasterApplication.getsInstance().setNullAwokenSkill(false);
    	((GameActivity)getScene().getActivity()).setAdViewVisible(View.VISIBLE);
        mSceneRef.clear();
        handler.getLooper().quit();
    }
    
    public boolean isNullAwokenStage() {
        return nullAwoken;
    }
    
    public void triggerAttackPhase() {
        try {
            flow.trigger(Events.playerAttack, padContext);
        } catch (LogicViolationError e) {
            LogUtil.e(e, e.toString());
        }
    }
    
    enum States implements StateEnum {
        ON_FIRST_ENTER,
        ENTER_STAGE,
        ENEMY_FIRST_STRIKE,
        PLAYER_TURN,
        ATTACK_PHASE,
        ENEMY_TURN,
        DAMAGE_PHASE,
        NEXT_STAGE,
        GAME_OVER,
        RESURRECTION,
        GAME_END,
        QUIT
    }
    
    enum Events implements EventEnum {
        enterStage,
        firstStrike,
        afterFirstStrike,
        playerAttack,
        attackDone,
        stillMyTurn,
        enemyDestroyed,
        enemyAttack,
        hpZero,
        quitGame,
        useMagicStone,
        afterResurrection,
        gameEnd,
    }

    @Override
    public void awokenRecover(final int recover) {
        if(mSkillList.containsKey(Constants.SK_AWOKEN_LOCK)) {
            SkillEntity entity = mSkillList.get(Constants.SK_AWOKEN_LOCK);
            int remainTurns = entity.decreaseAndGet(recover);
            if(remainTurns <= 0) {
                mSkillList.remove(Constants.SK_AWOKEN_LOCK);
                entity.detach();
                calcNullAwoken();
            }
        }
    }

    public void bindRecover(final int recover) {
    	Observable.from(mMemberList)
		.filter(new Func1<Member, Boolean>() {
			
			@Override
			public Boolean call(Member member) {
				return member != null && member.info != null && member.isBinded();
			}
		})
		.toList()
		.subscribe(new Action1<List<Member>>() {
			
			@Override
			public void call(final List<Member> list) {
				boolean changed = false;
				for(Member member : list) {
					member.bindRecover(recover);
					if(!member.isBinded()) {
						for(int index=0; index<6; ++index) {
							if(mMemberList[index] == member) {
								mTeamData.bindPet(index, false);
								changed = true;
								break;
							}
						}
					}
				}
                if(changed) {
                	getScene().updateSpecialLST();
                	setCurrentHp(mTeamData.getCurrentHp(), false);
                }
			}
		});
    }
    
    
    
    private void onFirstEnter(final PadContext context) {
        // load stage data
        currentStage = 1;

        LogUtil.e("before init stage data");
        if(mStage.length()<=1) {
        	initStage(mStage);
        } else {
        	StageGenerator.initWith(this, mStageEnemies, mStage);
        }
        LogUtil.e("init stage data");

        
        getScene().clearBoard(new ICastCallback() {
			
			@Override
			public void onCastFinish(boolean casted) {
		        try {
		            int skillBoost = 0;
		            for(int i=0; i<6; ++i) {
		            	if(mMemberList[i] != null) {
		            		skillBoost += getTargetAwokenCount(mMemberList[i].info, AwokenSkill.SKILL_BOOST);
                            skillBoost += getTargetAwokenCount(mMemberList[i].info, AwokenSkill.SKILL_BOOST_PLUS)*2;
		            	}
		            }
		            if(!mTeamData.team().isCopMode() && singleUp == SinglePowerUp.SB) {
		            	++skillBoost;
		            }
		            
		            if(mStage.equals("superfight") || mStage.equals("sopdet")) { // FIXME:
		            	skillBoost = 99;
		            }
		            
		            for(int i=0; i<6; ++i) {
		            	if(mMemberList[i] != null) {
		            		mMemberList[i].setCd(skillBoost);
		            	}
		            }
		        	
		            flow.trigger(Events.enterStage, context);
		        } catch (LogicViolationError e) {
		        }
			}
		});

    }

    private void initStage(String stage) {
    	mStageEnemies.clear();
    	
    	Activity activity = getScene().getActivity();
    	Gson gson = new Gson();
    	try {
    		final int[] stageName = {R.string.stage, R.string.stage1};
    		String json = activity.getResources().getString(stageName[Integer.parseInt(stage)]);
			java.lang.reflect.Type type = new TypeToken<StageData>(){}.getType();
			
			StageData data = gson.fromJson(json, type);
			ArrayList<Enemy> enemies;
			
			initEnemies(data.enemies);
			for(int floor=0; floor<data.floor; ++floor) {
				Floor detail = data.detail.get(floor);
				int total = RandomUtil.range(detail.size.get(0), detail.size.get(1));
				int indexSize = detail.index.size();
				
				enemies = new ArrayList<Enemy>();
				while(total>0) {
					int choice = RandomUtil.getInt(indexSize);
					// FIXME: find a good way to get enemy
					EnemyData enemy = findEnemy(data, detail, total, detail.index.get(choice));
					if(enemy != null) {
						enemies.add(createEnemy(enemy));
						total -= enemy.size;
					}
				}
				for(Integer must : detail.must) {
					if(must == 0) {
						continue;
					}
					EnemyData enemy = findEnemy(data, detail, 10, must);
					if(enemy != null) {
						enemies.add(createEnemy(enemy));
					}
				}
				mStageEnemies.put((floor+1), enemies);
			}
		} catch (Exception e) {
			LogUtil.e("ComboMaster", e.toString());
			e.printStackTrace();
		}
    }
    
    private void initEnemies(List<EnemyData> enemies) {
    	Activity activity = mSceneRef.get().getActivity();
		for(EnemyData enemy : enemies) {
			MonsterVO mvo = LocalDBHelper.getMonsterData(activity, enemy.id);
			enemy.prop1 = mvo.getMonsterProps().first;
			enemy.prop2 = mvo.getMonsterProps().second;
		}
	}

	private Enemy createEnemy(EnemyData enemy) {		
        Enemy newEnemy = Enemy.create(this, enemy.id, enemy.hp, enemy.atk, enemy.def, enemy.cd, new Pair<Integer, Integer>(enemy.prop1,enemy.prop2), StageGenerator.getMonsterTypes(this, enemy.id)); //
        newEnemy.attackMode = new EnemyAttackMode();
        newEnemy.attackMode.createSimpleAttackAction("", "", 100);
        return newEnemy;
    }
    
    private EnemyData findEnemy(StageData data, Floor detail, int total, int id) {
    	int enemySize = data.enemies.size();
    	for(int i=0; i<enemySize; ++i) {
    		EnemyData enemy = data.enemies.get(i);
    		if(enemy.id == id &&
    				total >= enemy.size) {
    			if(enemy.rare != 1) {
    				return enemy;
    			} else if(enemy.rare == 1 && RandomUtil.getLuck(15)) {
    				return enemy;
    			}
    		}
		}
    	
		return null;
	}

    private void gameEnd(final PadContext context) {
    	String used = (jumpUsed)? "-1:"+mStoneUsed:""+mStoneUsed;
    	ComboMasterApplication.getsInstance().putGaAction(mStage, "pass", used);
    	
    	getScene().engine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {				
				PlaygroundGameScene scene = getScene();
				Text text = new Text(0, 0, ResourceManager.getInstance().getFont(),
						"C L E A R !", scene.vbom);
				text.setScale(2f);
				float x = (GameActivity.SCREEN_WIDTH-text.getWidth()) / 2f;
				float y = GameActivity.SCREEN_HEIGHT / 5f;
				text.setPosition(x, y);
				scene.attach(text);
			}
		});
    	final Activity activity = getScene().getActivity();
    	handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
		    	activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
				    	new AlertDialog.Builder(activity)
						.setMessage(activity.getString(R.string.congraduation, mStoneUsed))
						.setPositiveButton(android.R.string.ok, new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
						        activity.onBackPressed();
							}
						}).show();
					}
				});
			}
		}, 500);


    }
    
	private void enterStage(final PadContext context) {
        final List<Enemy> enemies = mStageEnemies.get(currentStage, null);
        if (enemies == null) {
            // error, do something?
            return ;
        }

        int size = enemies.size();
        
        int space = GameActivity.SCREEN_WIDTH / size;
        int height = GameActivity.SCREEN_HEIGHT / 6;
        
        float hpWidth = space * 0.7f; 
        
        ActiveSkill skill = null;
        if(mSkillList.containsKey(Constants.SK_REDUCE_DEF)) {
        	skill = (ActiveSkill) mSkillList.get(Constants.SK_REDUCE_DEF).skill;
        }
        
        final PlaygroundGameScene scene = mSceneRef.get();
        for(int index=0; index<size; ++index) {
            Enemy e = enemies.get(index);
            e.loadData(scene, hpWidth);
            
            float offsetx = (space - e.sprite.getWidth()) / 2f + index * space;
            e.setPosition(offsetx, height);
            if(skill != null) {
            	e.addDebuff(null, Type.REDUCE_DEF, skill.getData());
            }
            e.initOwnAbility();
            //e.addDebuff(owner, Type.REDUCE_DEF, skill.getData());
            
        }
        
        // play stage animation
        int stageSize = mStageEnemies.size();
        final float centerX = (GameActivity.SCREEN_WIDTH - mStageText.getWidth())/2.0f;
        String stage = String.format("Battle %02d/%02d", currentStage, stageSize);
        mStageText.setText(stage);
        float fromX = centerX + mStageText.getWidth();
        mStageText.setPosition(fromX, height);
        mStageText.registerEntityModifier(new MoveXModifier(1.0f, fromX, centerX));
        mStageText.registerEntityModifier(new AlphaModifier(1.0f, 0.0f, 1.0f){
            @Override
            protected void onModifierFinished(IEntity pItem) {
                handler.postDelayed(new Runnable() {
                    
                    @Override
                    public void run() {
                        mStageText.registerEntityModifier(new MoveXModifier(0.8f, centerX, centerX - mStageText.getWidth()));
                        mStageText.registerEntityModifier(new AlphaModifier(1.0f, 1.0f, .0f) {
                            @Override
                            protected void onModifierFinished(final IEntity pItem) {
                                scene.detach(pItem);
                                
                                for(Enemy enemy : enemies) {
                                    enemy.attach();
                                }

                                try {
                                    flow.trigger(Events.firstStrike, context);
                                } catch (LogicViolationError e) {
                                }
                            }
                        });
                    }
                }, 250);
            }
        });
        mStageTop.setText(stage);
        mStageTop.setPosition(GameActivity.SCREEN_WIDTH - 8 - mStageTop.getWidth(), 84f);
        mJumpTo.setPosition(GameActivity.SCREEN_WIDTH - 8 - mJumpTo.getWidth(), 84f + mStageTop.getHeight());
        scene.attach(mStageText);

    }

    @Override
    public boolean is7x6() {
        return mSceneRef.get().is7x6();
    }

    boolean hasFirstStrike;
    private void enemyFirstStrike(PadContext context) {
        List<Enemy> enemies = mStageEnemies.get(currentStage, null);
        if (enemies == null) {
            // error, do something?
            return ;
        }
        hasFirstStrike = false;
        Message.obtain(handler, ExecutorHandler.MSG_BEGIN_ENEMY_FIRST_STRIKE, 0, 0).sendToTarget();
    }
    
    private void playerTurn(PadContext context) {
        PlaygroundGameScene scene = mSceneRef.get();
        jumping = false;
//        if(isEnemiesDestroy()) {
//        	try {
//				flow.trigger(Events.enemyDestroyed, padContext);
//			} catch (LogicViolationError e) {
//				e.printStackTrace();
//			}
//        } else {
//
//        }
    	scene.onPlayerTurn();
    	registerPlayerTouchEvent();
    }
    
    private void attackPhase(PadContext context) {
        unregisterPlayerTouchEvent();
        
        // FIXME: continue cross shield
        //calculateCrossShield();
        
        PlaygroundGameScene scene = mSceneRef.get();
        Map<String, Object> settings = new HashMap<String, Object>();
        List<Boolean> bindStatus = new ArrayList<Boolean>();
        for(int i=0; i<6; ++i) {
        	if (mMemberList[i] != null) {
        		bindStatus.add(mMemberList[i].isBinded());
        	} else {
        		bindStatus.add(false);
        	}
        }
        boolean awokenLocked = isAwokenLocked();

        settings.put("bindStatus", bindStatus);
        settings.put("isHpChecked", true);
        settings.put("hp", 100 * mTeamData.getCurrentHp() / (float)getTeamHpTotal());
        settings.put("skillFired", firedThisTurn);
        settings.put("nullAwoken", awokenLocked || nullAwoken);
        settings.put("copMode", mTeamData.team().isCopMode());
        settings.put("singleUp", singleUp.value());
        if (mSkillList.containsKey(Constants.SK_RCV_UP)) {
            settings.put("rcvUp", mSkillList.get(Constants.SK_RCV_UP));
        }

        List<Match> matches = scene.getMatches();
        // check recover awoken first
        if (awokenLocked && executeRecoverAwokenByLeaderSkill(getTeam(), matches, settings)) {
            settings.put("bindStatus", bindStatus);
            settings.put("hp", 100 * mTeamData.getCurrentHp() / (float)mTeamData.getHp());
            settings.put("nullAwoken", isAwokenLocked() || nullAwoken);
        }

        if(mCrossShield != null) {
			settings.put("cross", mCrossShield.first);
			settings.put("crossAtk", mCrossShield.second);
        } else {
            // check cross heart
            {
                float reduced_total = 1f;
                boolean[] fired = {false, false};
            	int[] reduced = {0, 0};
            	float[] crossSkill = {0, 0};
                for(Match match : matches) {
                    if(match.type == 2 && match.isCross()) { // if heart and cross
                        fired[0] = fired[1] = true;
                        break;
                    }
                }

            	MonsterInfo leader = getTeam().getMember(0);
            	MonsterInfo friend = getTeam().getMember(5);
            	if(leader != null && !mMemberList[0].isBinded()) {
            		for(LeaderSkill skill : leader.getLeaderSkill()) {
            			LeaderSkillType type = skill.getType();
            			if(fired[0] && (type == LeaderSkillType.LST_CROSS||
            					type == LeaderSkillType.LST_CROSS_P)) {
            				float atk = skill.getData().size()>=2 ? skill.getData().get(1)/100f:1;
            				crossSkill[0] = atk;
            				if(type == LeaderSkillType.LST_CROSS) {
            					reduced[0] = 50;
            				} else {
            					reduced[0] = skill.getData().get(2);
            				}
            			} else if(type == LeaderSkillType.LST_COLOR_SHIELD) {
                            crossSkill[0] = 1;
                            reduced[0] = DamageCalculator.getShieldPercent(mTeamData.team(), skill, matches, settings);
                        } else if(type == LeaderSkillType.LST_COMBO_SHIELD) {
                            crossSkill[0] = 1;
                            reduced[0] = DamageCalculator.getComboShieldPercent(mTeamData.team(), skill, matches, settings);
                        } else if (type == LeaderSkillType.LST_TARGET_ORB_SHIELD) {
                            crossSkill[0] = 1;
                            reduced[0] = DamageCalculator.getTargetOrbShield(mTeamData.team(), skill, matches, settings);
                        } else if(type == LeaderSkillType.LST_MULTI_ORB_SHIELD) {
                            crossSkill[0] = 1;
                            reduced[0] = DamageCalculator.getMultiShield(mTeamData.team(), skill, matches, settings);
                        } else if(type == LeaderSkillType.LST_MULTI_ORB_SHIELD_MULTI) {
                            crossSkill[0] = 1;
                            reduced[0] = DamageCalculator.getMultiShieldMulti(mTeamData.team(), skill, matches, settings);
                        } else if(type == LeaderSkillType.LST_L_ATTACK) {
                            crossSkill[0] = 1;
                            reduced[0] = DamageCalculator.getLFormatShield(mTeamData.team(), skill, matches, settings);
                        } else if(type == LeaderSkillType.LST_HEART_FACTOR) {
                            ActiveSkill sk = null;
                            if(mSkillList.containsKey(Constants.SK_RCV_UP)) {
                                sk = (ActiveSkill)mSkillList.get(Constants.SK_RCV_UP).skill;
                            }
                            int heartReduced = DamageCalculator.getHeartFactor(mTeamData.team(), skill, matches, settings, sk,3 );
                            if(heartReduced > 0) {
                                crossSkill[0] = 1;
                                reduced[0] = heartReduced;
                            }
                        }
                        if(reduced[0] > 0) {
                            reduced_total = reduced_total * (100-reduced[0])/100;
                        }
            		}
            	}
            	if(friend != null && !mMemberList[5].isBinded()) {
            		for(LeaderSkill skill : friend.getLeaderSkill()) {
            			LeaderSkillType type = skill.getType();
            			if(fired[1] && (type == LeaderSkillType.LST_CROSS||
            					type == LeaderSkillType.LST_CROSS_P)) {
            				float atk = skill.getData().size()>=2 ? skill.getData().get(1)/100f:1;
            				crossSkill[1] = atk;
            				if(type == LeaderSkillType.LST_CROSS) {
            					reduced[1] = 50;
            				} else {
            					reduced[1] = skill.getData().get(2);
            				}
            			} else if(type == LeaderSkillType.LST_COLOR_SHIELD) {
                            crossSkill[1] = 1;
                            reduced[1] = DamageCalculator.getShieldPercent(mTeamData.team(), skill, matches, settings);
                        } else if(type == LeaderSkillType.LST_COMBO_SHIELD) {
                            crossSkill[1] = 1;
                            reduced[1] = DamageCalculator.getComboShieldPercent(mTeamData.team(), skill, matches, settings);
                        } else if(type == LeaderSkillType.LST_MULTI_ORB_SHIELD) {
                            crossSkill[1] = 1;
                            reduced[1] = DamageCalculator.getMultiShield(mTeamData.team(), skill, matches, settings);
                        } else if(type == LeaderSkillType.LST_MULTI_ORB_SHIELD_MULTI) {
                            crossSkill[1] = 1;
                            reduced[1] = DamageCalculator.getMultiShieldMulti(mTeamData.team(), skill, matches, settings);
                        } else if(type == LeaderSkillType.LST_L_ATTACK) {
                            crossSkill[1] = 1;
                            reduced[1] = DamageCalculator.getLFormatShield(mTeamData.team(), skill, matches, settings);
                        } else if(type == LeaderSkillType.LST_HEART_FACTOR) {
                            ActiveSkill sk = null;
                            if(mSkillList.containsKey(Constants.SK_RCV_UP)) {
                                sk = (ActiveSkill)mSkillList.get(Constants.SK_RCV_UP).skill;
                            }
                            int heartReduced = DamageCalculator.getHeartFactor(mTeamData.team(), skill, matches, settings, sk, 3);
                            if(heartReduced > 0) {
                                crossSkill[1] = 1;
                                reduced[1] = heartReduced;
                            }
                        }
                        if(reduced[1] > 0) {
                            reduced_total = reduced_total * (100-reduced[1])/100;
                        }
            		}
            	}
            
	            float percent = 0;
	    		float atk = 1f;
	        	if(crossSkill[0]>0 || crossSkill[1]>0) {
	        	    percent = reduced_total;

                    if(crossSkill[0]>0) {
                        atk *= crossSkill[0];
                    }
                    if(crossSkill[1]>0) {
                        atk *= crossSkill[1];
                    }
	        	}
				settings.put("cross", percent);
				settings.put("crossAtk", atk);
				if(percent>0) {
					mCrossShield = new Pair<Float, Float>(percent, atk);
					getScene().enableCrossShield(true);
				}
            }
        }
        
        ActiveSkill fired = null;
        if (mSkillList.containsKey(Constants.SK_POWER_UP)) {
        	fired = (ActiveSkill) mSkillList.get(Constants.SK_POWER_UP).skill;
        }
        if(matches.size() == 0) {
        	handler.sendEmptyMessage(ExecutorHandler.MSG_POISON_DAMAGE);
        } else {
	        List<Pair<AttackValue, AttackValue>> damage = DamageCalculator.calculateDamage(mTeamData.team(), matches, fired, 0, settings);
            if(!skillCharger(mTeamData.team(), matches, settings, damage)) {
                Message.obtain(handler, ExecutorHandler.MSG_START_ATTACK, damage).sendToTarget();
            }
            //Message.obtain(handler, ExecutorHandler.MSG_START_ATTACK, damage).sendToTarget();
        }
    }


    private boolean executeRecoverAwokenByLeaderSkill(Team team, List<Match> matches, Map<String, Object> settings) {
        MonsterInfo leader = team.getMember(0);
        MonsterInfo friend = team.getMember(5);
        int totalReduced = 0;
        if(leader != null && !mMemberList[0].isBinded()) {
            for (LeaderSkill skill : leader.getLeaderSkill()) {
                if(skill.getType() == LeaderSkillType.LST_HEART_FACTOR) {
                    ActiveSkill sk = null;
                    if(mSkillList.containsKey(Constants.SK_RCV_UP)) {
                        sk = (ActiveSkill)mSkillList.get(Constants.SK_RCV_UP).skill;
                    }
                    totalReduced += DamageCalculator.getHeartFactor(mTeamData.team(), skill, matches, settings, sk, 4);
                }
            }
        }
        if(friend != null && !mMemberList[5].isBinded()) {
            for (LeaderSkill skill : friend.getLeaderSkill()) {
                if(skill.getType() == LeaderSkillType.LST_HEART_FACTOR) {
                    ActiveSkill sk = null;
                    if(mSkillList.containsKey(Constants.SK_RCV_UP)) {
                        sk = (ActiveSkill)mSkillList.get(Constants.SK_RCV_UP).skill;
                    }
                    totalReduced += DamageCalculator.getHeartFactor(mTeamData.team(), skill, matches, settings, sk, 4);
                }
            }
        }
        if(totalReduced>0) {
            awokenRecover(totalReduced);
            return true;
        }
        return false;
    }

    private boolean skillCharger(TeamInfo info, List<Match> combos, Map<String, Object> settings, final List<Pair<AttackValue, AttackValue>> damage) {
        List<Boolean> bindStatus = null;
        if (settings.containsKey("bindStatus")) {
            bindStatus = (List<Boolean>)settings.get("bindStatus");
        }

        List<Integer> skillCharger = new ArrayList<Integer>();
        for(int i=0; i<6; ++i) {
            skillCharger.add(i, 0);
        }
        boolean[] firedAttr = {false, false, true, false, false, false};
        boolean[] checkAttr = {false, false, false, false, false, false, false, false, false};
        for(int i=0; i<6; ++i) {
            MonsterInfo minfo = info.getMember(i);
            if (minfo == null || (bindStatus!=null && bindStatus.get(i))) {
                continue;
            }
            Pair<Integer, Integer> props = minfo.getProps();
            checkAttr[props.first] = true;
            if(props.second>=0) {
                checkAttr[props.second] = true;
            }
            int count = getTargetAwokenCount(minfo, AwokenSkill.SKILL_CHARGE);
            skillCharger.set(i, count);
        }
        if(skillCharger.size()>0) {
            for (Match m : combos) {
                if (m.type < 9 && m.type >= 0 && checkAttr[m.type]) {
                    firedAttr[m.type] = true;
                }
            }
            boolean fiveColors = true;
            for (int x = 0; x < firedAttr.length; ++x) {
                if (!firedAttr[x]) {
                    fiveColors = false;
                    break;
                }
            }

            if (fiveColors) {
                skillBoost(skillCharger, null, new ICastCallback() {
                    @Override
                    public void onCastFinish(boolean casted) {
                        Message.obtain(handler, ExecutorHandler.MSG_START_ATTACK, damage).sendToTarget();
                    }
                });
            }
            return fiveColors;
        }
        return false;
    }
    private void enemyTurn(PadContext context) {
    	Message.obtain(handler, ExecutorHandler.MSG_BEGIN_ENEMY_ATTACK, 0, 0).sendToTarget();
    }
    
    private void clearPhase() {
    	getScene().engine.runOnUpdateThread(new Runnable() {
            
            @Override
            public void run() {
            	boolean changed = false;
                for(int i=0; i<6; ++i) {
                	Member member = mMemberList[i];
                	if (member != null) {
                		boolean bind = member.isBinded();
                		member.countDown();
                		mTeamData.bindPet(i, member.isBinded());
                		changed |= (bind != member.isBinded());
                	}
                }
                if(changed) {
                	getScene().updateSpecialLST();
                	setCurrentHp(mTeamData.getCurrentHp(), false);
                }
                
                // reset cross shield
                mCrossShield = null;
                getScene().enableCrossShield(false);
                // FIXME: move time or something should decrease in other place                
                List<String> removeList = new ArrayList<String>();
                for(String key : mSkillList.keySet()) {
                	if(Constants.SK_TIME_CHANGE.equals(key) ||
                			Constants.SK_TIME_CHANGE_X.equals(key) ||
                            Constants.SK_DROP_ONLY.equals(key) ||
                			Constants.SK_DROP_RATE.equals(key) ||
                			Constants.SK_DROP_RATE_NEG.equals(key) ||
                			Constants.SK_DROP_LOCK.equals(key) ||
                			Constants.SK_SKILL_LOCK.equals(key) ||
                			Constants.SK_POWER_UP.equals(key) ||
                			Constants.SK_AWOKEN_LOCK.equals(key) ||
                			Constants.SK_ADD_COMBO.equals(key) ||
                            Constants.SK_TURN_RECOVER.equals(key) ||
                            Constants.SK_ENHANCE_ORB.equals(key) ||
                            Constants.SK_RCV_UP.equals(key) ||
                            Constants.SK_CLOUD.equals(key) ||
                            Constants.SK_HP_CHANGE.equals(key) ||
                            Constants.SK_LOCK_REMOVE.equals(key)) {
                		continue;
                	}
                	SkillEntity entity = mSkillList.get(key);
                	if (entity.countDown()) {
                		removeList.add(key);
                	}
                }
                for(String key : removeList) {
                	mSkillList.remove(key);
                }
                mHp.clearDamage();
            }
        });

    }
    
    private void damagePhase(PadContext context) {
    	clearPhase();
        
        try {
	    	int hp = mTeamData.getCurrentHp();
	    	if (hp <= 0) {
	    		flow.trigger(Events.hpZero, context);
	    	} else {
                if(isEnemiesDestroy()) {
                	flow.trigger(Events.enemyDestroyed, context);
                } else {
                	flow.trigger(Events.attackDone, context);
                }
	    	}
            
        } catch (LogicViolationError e) {
        }
    }
    
    private void nextStage(PadContext context) {
    	// remove if has hasResurrection
    	List<Enemy> previous = mStageEnemies.get(currentStage, null);
    	if(previous != null) {
	    	for(Enemy enemy : previous) {
	    		if(enemy.hasResurrection()) {
	    			enemy.detach();
	    		}
	    	}
    	}
    	
        ++currentStage;
        try {
            List<Enemy> data = mStageEnemies.get(currentStage, null);
            if (data == null) {
                //stage completed, congratulation
            	flow.trigger(Events.gameEnd, context);
            } else {
            	flow.trigger(Events.enterStage, context);
            }
            
        } catch (LogicViolationError e) {
        }
    }
    
    
    private void gameOver(final PadContext context) {
    	new AlertDialog.Builder(getScene().getActivity())
    		.setTitle(R.string.game_over)
    		.setCancelable(false)
    		.setMessage(R.string.use_magic_stone)
    		.setPositiveButton(android.R.string.yes, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
			        try {
			        	flow.trigger(Events.useMagicStone, context);
			        } catch (LogicViolationError e) {
			        }
				}
			})
			.setNegativeButton(android.R.string.no, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						ComboMasterApplication.getsInstance().putGaAction(mStage, "giveup", ""+mStoneUsed);
						flow.trigger(Events.quitGame, context);
					} catch(LogicViolationError e) {
					}
				}
			}).show();

    }
    
    private void resurrection(PadContext context) {
    	useMagicStone(context);
    }
    
    private void quit(PadContext context) {
    	ComboMasterApplication.getsInstance().putGaAction(mStage, "leave", ""+currentStage);
    	getScene().getActivity().onBackPressed();
    }
    
    private void removeNegative() {
    	List<String> removeList = new ArrayList<String>();
    	for(String key: mSkillList.keySet()) {
    		if(key.equals(Constants.SK_DROP_RATE_NEG)) {
    			mSkillList.get(key).detach();
    			removeList.add(key);
    			getScene().removeDropRateSkill(true);
    		} else if(key.equals(Constants.SK_DROP_LOCK)) {
    			mSkillList.get(key).detach();
    			removeList.add(key);
    			getScene().removeDropLock();
    		} else if(key.equals(Constants.SK_TIME_CHANGE)) {
    			SkillEntity skill = mSkillList.get(key);
    			if(skill.skill.getData().get(1) < 0) {
    				getScene().setAdditionMoveTime(0);
    				skill.detach();
    				removeList.add(key);
    			}
    		} else if(key.equals(Constants.SK_SKILL_LOCK)) {
    			SkillEntity skill = mSkillList.get(key);
    			skill.detach();
    			removeList.add(key);
    		} else if (key.equals(Constants.SK_HP_CHANGE)) {
                SkillEntity skill = mSkillList.get(key);
                skill.detach();
                removeList.add(key);
                mTeamData.setMaxHp(-1);
            }
    	}
    	for(String key : removeList) {
    		mSkillList.remove(key);
    	}
    	
    	if(mLeaderSwitch != null) {
    		int index = mLeaderSwitch.skill.getData().get(1);
    		switchMember(index);
    		mLeaderSwitch.detach();
    		mLeaderSwitch = null;
    	}
    }
    
    private void useMagicStone(final PadContext context) {
    	++mStoneUsed;

        bindRecover(999);
        awokenRecover(99);
    	removeNegative();
    	recoverPlayerHp(getTeamHpTotal());
    	getScene().clearBoard(new ICastCallback() {
			
			@Override
			public void onCastFinish(boolean casted) {
		        try {
		        	flow.trigger(Events.afterResurrection, context);
		        } catch (LogicViolationError e) {
		        }
				
			}
		});
    }

    EasyFlow<PadContext> flow = FlowBuilder.from(States.ON_FIRST_ENTER)
            .transit(
                    FlowBuilder.on(Events.enterStage).to(States.ENTER_STAGE).transit(
                            FlowBuilder.on(Events.firstStrike).to(States.ENEMY_FIRST_STRIKE).transit(
                                FlowBuilder.on(Events.afterFirstStrike).to(States.PLAYER_TURN).transit(
                                        FlowBuilder.on(Events.playerAttack).to(States.ATTACK_PHASE).transit(
                                                FlowBuilder.on(Events.enemyDestroyed).to(States.NEXT_STAGE).transit(
                                                        FlowBuilder.on(Events.enterStage).to(States.ENTER_STAGE),
                                                        FlowBuilder.on(Events.gameEnd).to(States.GAME_END).transit(
                                                        		FlowBuilder.on(Events.enterStage).to(States.ON_FIRST_ENTER)
                                                        		)
                                                        ),
                                                FlowBuilder.on(Events.attackDone).to(States.ENEMY_TURN).transit(
                                                        FlowBuilder.on(Events.enemyAttack).to(States.DAMAGE_PHASE).transit(
                                                                FlowBuilder.on(Events.hpZero).to(States.GAME_OVER),
                                                                FlowBuilder.on(Events.attackDone).to(States.PLAYER_TURN),
                                                                FlowBuilder.on(Events.enemyDestroyed).to(States.NEXT_STAGE)
                                                                )
                                                        ),
                                                FlowBuilder.on(Events.hpZero).to(States.GAME_OVER),
                                                FlowBuilder.on(Events.stillMyTurn).to(States.PLAYER_TURN)
                                                ),
                                        FlowBuilder.on(Events.enemyDestroyed).to(States.NEXT_STAGE)
                                        ),
                                FlowBuilder.on(Events.hpZero).to(States.GAME_OVER).transit(
                                        FlowBuilder.on(Events.useMagicStone).to(States.RESURRECTION).transit(
                                                FlowBuilder.on(Events.afterResurrection).to(States.PLAYER_TURN)
                                                ),
                                        FlowBuilder.on(Events.quitGame).to(States.QUIT).transit(
                                                // library ask a state to transit ...
                                                FlowBuilder.on(Events.enterStage).to(States.ON_FIRST_ENTER)
                                                )
                                        )
                                
                           )
                    )
            )
            ;
    
    public void bindFlow() {

        flow.whenEnter(States.ON_FIRST_ENTER, new ContextHandler<PadContext>() {
            @Override
            public void call(PadContext context) throws Exception {
                onFirstEnter(context);
            }
        });
        
        flow.whenEnter(States.ENTER_STAGE, new ContextHandler<PadContext>() {
            @Override
            public void call(PadContext context) throws Exception {
                enterStage(context);
            }
        });
        
        flow.whenEnter(States.GAME_END, new ContextHandler<PadContext>() {

			@Override
			public void call(PadContext context) throws Exception {
				gameEnd(context);
			}
		});
        
        flow.whenEnter(States.ENEMY_FIRST_STRIKE, new ContextHandler<PadContext>() {
            @Override
            public void call(PadContext context) throws Exception {
                enemyFirstStrike(context);
            }
        });
        
        flow.whenEnter(States.PLAYER_TURN, new ContextHandler<PadContext>() {
            @Override
            public void call(PadContext context) throws Exception {
                playerTurn(context);
            }
        });
        
        flow.whenEnter(States.ATTACK_PHASE, new ContextHandler<PadContext>() {
            @Override
            public void call(PadContext context) throws Exception {
                attackPhase(context);
                if(mDarkSkill != null) {
                    mDarkSkill.countDown();
                    int turns = mDarkSkill.turns();
                    getScene().updateSuperDark(turns);
                    if(turns == 0) {
                        mDarkSkill = null;
                    }
                }
            }
        });
        
        flow.whenEnter(States.ENEMY_TURN, new ContextHandler<PadContext>() {
            @Override
            public void call(PadContext context) throws Exception {
                enemyTurn(context);
            }
        });
        
        flow.whenEnter(States.DAMAGE_PHASE, new ContextHandler<PadContext>() {
            @Override
            public void call(PadContext context) throws Exception {
                damagePhase(context);
            }
        });
        
        flow.whenEnter(States.NEXT_STAGE, new ContextHandler<PadContext>() {
            @Override
            public void call(PadContext context) throws Exception {
                nextStage(context);
            }
        });
        
        flow.whenEnter(States.GAME_OVER, new ContextHandler<PadContext>() {
            @Override
            public void call(PadContext context) throws Exception {
                gameOver(context);
            }
        });

        flow.whenEnter(States.RESURRECTION, new ContextHandler<PadContext>() {
            @Override
            public void call(PadContext context) throws Exception {
                resurrection(context);
            }
        });
        
        flow.whenEnter(States.QUIT, new ContextHandler<PadContext>() {
            @Override
            public void call(PadContext context) throws Exception {
                quit(context);
            }
        });
    }

    public boolean needConfirmSkill() {
        return true;
    }

    public interface ICastCallback {
        public void onCastFinish(boolean casted);
    }
    
    /**
     *  Execute global effect skill, 
     *  ex: reduce move time, sky drop rate ... etc
     * @param key
     * @param skill
     * @param callback
     */
    @Override
    public <T extends Skill> void fireGlobalSkill(String key, T skill, final ICastCallback callback) {//String key,
    	if(mSkillList.containsKey(key)) {
    		mSkillList.get(key).detach();
    	}
    	if("leader_switch".equals(key)) {
    		switchMemberSkill(skill);
        	if (callback != null) {
        		callback.onCastFinish(true);
        	}
    		return ;
    	}
    	
    	if (Constants.SK_TIME_CHANGE.equals(key)) {
    		if(mSkillList.containsKey(Constants.SK_TIME_CHANGE_X)) {
    			mSkillList.get(Constants.SK_TIME_CHANGE_X).detach();
    		}
    	} else if(Constants.SK_TIME_CHANGE_X.equals(key)) {
    		if(mSkillList.containsKey(Constants.SK_TIME_CHANGE)) {
    			mSkillList.get(Constants.SK_TIME_CHANGE).detach();
    		}
    	}
    	
    	SkillEntity skillEntity = new SkillEntity(this);
    	int y = (int)(mMemberList[0].sprite.getY()-50);
    	skillEntity.initSkill(0, y, skill);

    	mSkillList.put(key, skillEntity);
    	if (Constants.SK_TIME_CHANGE.equals(key)) {
    		getScene().setAdditionMoveTime(skill.getData().get(1));
    		getScene().setAdditionMoveTimeX(0);
    	} else if (Constants.SK_TIME_CHANGE_X.equals(key)) {
    		getScene().setAdditionMoveTime(0);
    		getScene().setAdditionMoveTimeX(skill.getData().get(1));
    	} else if (Constants.SK_REDUCE_DEF.equals(key)) {
    		skillEntity.setVisible(false);
    	} else if(Constants.SK_AWOKEN_LOCK.equals(key)) {
    		calcNullAwoken();
    	} else if(Constants.SK_CLOUD.equals(key)) {
    	    getScene().skillFired((ActiveSkill)skill, null, true);
        } else if(Constants.SK_LOCK_REMOVE.equals(key)) {
            getScene().skillFired((ActiveSkill)skill, null, true);
        } else if(Constants.SK_HP_CHANGE.equals(key)) {
            fireSkill(null, (ActiveSkill)skill, null);
        }
    	
    	int offsetX = 0;
    	for(SkillEntity entity : mSkillList.values()) {
    		entity.setPosition(offsetX, y);
    		offsetX += (int)entity.getWidth() + 10;
    	}
    	
    	if (callback != null) {
    		callback.onCastFinish(true);
    	}
    }
    
    @Override
    public void removePositiveSkills() {
    	List<String> removeList = new ArrayList<String>();
    	for(String key : mSkillList.keySet()) {
    		if(Constants.SK_COUNTER_ATTACK.equals(key)) {
    			removeList.add(key);
    		} else if (Constants.SK_DROP_RATE.equals(key)) {
    			removeList.add(key);
    		} else if (Constants.SK_POWER_UP.equals(key)) {
    			removeList.add(key);
    		} else if (Constants.SK_SHIELD.equals(key)) {
    			removeList.add(key);
    		} else if (Constants.SK_TIME_CHANGE.equals(key)) {
    			if(mSkillList.get(key).skill.getData().get(1)>0) {
    				removeList.add(key);
    			}
    		} else if (Constants.SK_TIME_CHANGE_X.equals(key)) {
    			if(mSkillList.get(key).skill.getData().get(1)>0) {
    				removeList.add(key);
    			}
    		}
    	}
    	
        for(String key : removeList) {
        	SkillEntity entity = mSkillList.remove(key);
        	entity.detach();
        	if(Constants.SK_TIME_CHANGE.equals(key)) {
        		getScene().setAdditionMoveTime(0);
        	} else if(Constants.SK_TIME_CHANGE_X.equals(key)) {
        		getScene().setAdditionMoveTimeX(0);
        	} else if (Constants.SK_DROP_RATE.equals(key)) {
        		getScene().removeDropRateSkill(false);
        	}
        }
    	
    }

    private void calcNullAwoken() {
    	boolean nilAwoken = mSkillList.containsKey(Constants.SK_AWOKEN_LOCK) || nullAwoken;

        PlaygroundGameScene scene = mSceneRef.get();

        if(scene instanceof PadGameScene) {
            ((PadGameScene)scene).calcNullAwoken(nilAwoken, mTeamData.team());
        } else {
            ((PadGameScene7x6)scene).calcNullAwoken(nilAwoken, mTeamData.team());
        }
    }
    
    int prevIndex = -1;
    private <T extends Skill> void switchMemberSkill(T skill) {
    	int index = skill.getData().get(0);
    	if(index == 0) {
    		if(prevIndex != -1) {
    			switchMember(prevIndex, true);
    			prevIndex = -1;
    		}
    	} else {
    		switchMember(index, true);
    		prevIndex = index;
    	}
    }
    
    private void switchMember(int index) {
    	switchMember(index, false);
    }
    
    private void switchMember(int index, boolean isSkill) {
		Member member = mMemberList[index];
		mMemberList[index] = mMemberList[0];
		mMemberList[0] = member;
		int normalIndex = 1;
		for(; normalIndex<6; ++normalIndex) {
			if(normalIndex != index) {
				break;
			}
		}


		float x = member.sprite.getX();
		float normalY = mMemberList[normalIndex].sprite.getY();
		if(mMemberList[normalIndex].isSkillUsable()) {
			normalY = mMemberList[normalIndex].sprite.getY() + 10;
		}
		
		float n0y, n1y;
		if(!isSkill) {
			n0y = normalY;
			if(mMemberList[0].isSkillUsable()) {
				n0y -= 10;
			}
			n1y = normalY;
			if(mMemberList[index].isSkillUsable()) {
				n1y -= 10;
			}
		} else {
			n0y = mMemberList[0].sprite.getY();
			n1y = mMemberList[index].sprite.getY();
		}

		mMemberList[0].setPosition(mMemberList[index].sprite.getX(), n0y);
		mMemberList[index].setPosition(x, n1y);
		
		mTeamData.switchMember(index);
		setCurrentHp(mTeamData.getCurrentHp(), false);
    }
    
    public void executeLeaderSwitch(ActiveSkill skill, final ICastCallback callback) {
    	if(!hasAlreadySwitched()) {
    		mLeaderSwitch = new SkillEntity(this);
    		int index = skill.getData().get(1);
    		Sprite sprite = mMemberList[index].sprite;
    		int x = (int)(sprite.getX() + sprite.getWidth()/2 - 5);
    		int y = (int)(sprite.getY() + sprite.getHeight()/2 - 10);
    		mLeaderSwitch.initSkill(x, y, skill);
    		
    		switchMember(index);
    	}
    	if(callback != null) {
    		callback.onCastFinish(true);
    	}
    }
    
    public boolean hasAlreadySwitched() {
    	return mLeaderSwitch != null || prevIndex != -1;
    }
    
    @Override
    public void fireSkill(final Member caster, final ActiveSkill skill, final ICastCallback callback) {
        boolean casted = getScene().skillFired(skill, callback, caster == null);
        if (!casted) { // damage skill will not cast, handle here
            if(caster == null && skill.getType() == ActiveSkill.SkillType.ST_SUPER_DARK) {
                // special case
                mDarkSkill = skill;
                mDarkSkill.setTurn(0, true);
                return ;
            } else if (skill.getType() == ActiveSkill.SkillType.ST_RECOVER_LOCK_REMOVE) {
                if (mSkillList.containsKey(Constants.SK_LOCK_REMOVE)) {
                    SkillEntity entity = mSkillList.get(Constants.SK_LOCK_REMOVE);
                    int turns = entity.decreaseAndGet(skill.getData().get(0));
                    if(turns <= 0) {
                        removeSkill(Constants.SK_LOCK_REMOVE);
                    }
                }
            } else if (skill.getType() == ActiveSkill.SkillType.ST_HP_CHANGE) {
                List<Integer> data = skill.getData();
                int type = data.get(1);
                if (type == 1) { // FIX HP
                    mTeamData.setMaxHp(data.get(2));
                } else if (type == 3) { // % HP
                    int hp = mTeamData.getRealHp() * data.get(2) / 100;
                    mTeamData.setMaxHp(hp);
                }
            } else if (skill.getType() == ActiveSkill.SkillType.ST_HEN_SHIN) {

                SkillDispatcher.fire(this, caster, skill, new ICastCallback() {

                    @Override
                    public void onCastFinish(boolean casted) {
                        callback.onCastFinish(true);
                        // check all enemies dead or not

                        mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
                            @Override
                            public void run() {
                                checkEnemyState();
                            }
                        });

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        for(int i=0; i<6; ++i) {
                            if (caster != null && mMemberList[i] == caster) {
                                mTeamData.henshin(i, skill.getData().get(0));

                                ResourceManager res = ResourceManager.getInstance();
                                VertexBufferObjectManager vbom = getScene().vbom;
                                ITextureRegion region = null;
                                MonsterInfo info = mTeamData.getMember(i);
                                LogUtil.d("load data", info.getNo());
                                try {
                                    region = res.loadTextureFile(info.getNo() + "i.png");
                                } catch (Throwable e) {
                                    LogUtil.e(e);
                                }
                                if (region != null) {
                                    float mx = mMemberList[i].sprite.getX();
                                    float my = mMemberList[i].sprite.getY();
                                    Member member = new Member(PadEnvironment.this, info);
                                    member.init(mx, my, region, vbom);
                                    PlaygroundGameScene scene = getScene();
                                    scene.unregisterTouchArea(mMemberList[i].sprite);
                                    scene.detachChild(mMemberList[i].sprite);
                                    scene.attachChild(member.sprite);
                                    scene.registerTouchArea(member.sprite);

                                    mMemberList[i] = member;
                                }
                                break;
                            }
                        }
                        calcNullAwoken();
                        setCurrentHp(mTeamData.getCurrentHp(), false);
                    }
                });

                return ;
            }
            SkillDispatcher.fire(this, caster, skill, new ICastCallback() {

                @Override
                public void onCastFinish(boolean casted) {
                    callback.onCastFinish(true);
                    // check all enemies dead or not

                    mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
                        @Override
                        public void run() {
                            checkEnemyState();
                        }
                    });

                }
            });
        }
    }
    
    boolean firedThisTurn = false;
    @Override
    public void fireSkill(Member owner, int no) {
    	firedThisTurn = true;
    	Message.obtain(handler, ExecutorHandler.MSG_FIRE_SKILL_INIT, 0, no, owner).sendToTarget();
    }
    
    @Override
    public void fireSkill(Member owner) {
    	fireSkill(owner, 1);
    }
    
    public void registerPlayerTouchEvent() {
        Scene scene = getScene();
        for(int i=0; i<6; ++i) {
            Member member = mMemberList[i];
            if (member != null) {
                scene.registerTouchArea(member.sprite);
            }
        }
    }
    
    public void unregisterPlayerTouchEvent() {
        Scene scene = getScene();
        for(int i=0; i<6; ++i) {
            Member member = mMemberList[i];
            if (member != null) {
                scene.unregisterTouchArea(member.sprite);
            }
        }
    }
    
    // get current enemies
    public List<Enemy> getEnemies() {
        return mStageEnemies.get(currentStage);
    }
    
    public Team getTeam() {
    	return mTeamData;
    }
    
    public boolean resistBadEffect(AwokenSkill awoken) {
		int resistant = 0;

		if(!isAwokenLocked()) {
			for(int i=0; i<6; ++i) {
				if(mMemberList[i] != null && !mMemberList[i].isBinded()) {
					resistant += getTargetAwokenCount(mMemberList[i].info, awoken);
				}
			}
		}
		int plus = 0;
		if(singleUp == SinglePowerUp.RESIST_SKILL_BIND && awoken == AwokenSkill.RESISTANCE_SKILL_LOCK) {
			plus = 50;
		}

		int x = 20;
        if (awoken == AwokenSkill.RESISTANCE_KUMO ||
                awoken == AwokenSkill.RESISTANCE_DARK_PLUS ||
                awoken == AwokenSkill.RESISTANCE_JAMMER_PLUS ||
                awoken == AwokenSkill.RESISTANCE_POISON_PLUS) {
		    x = 100;
        }
        boolean resist = RandomUtil.getLuck(resistant*x + plus);
        if (!resist) {
            if (awoken == AwokenSkill.RESISTANCE_DARK) {
                return resistBadEffect(AwokenSkill.RESISTANCE_DARK_PLUS);
            } else if (awoken == AwokenSkill.RESISTANCE_JAMMER) {
                return resistBadEffect(AwokenSkill.RESISTANCE_JAMMER_PLUS);
            } else if (awoken == AwokenSkill.RESISTANCE_POISON) {
                return resistBadEffect(AwokenSkill.RESISTANCE_POISON_PLUS);
            }
        }
        return resist;
    }
    @Override
    public boolean isMultiMode() {
        return false;
    }
    public void lockSkill(Enemy enemy, final int turns, final ICastCallback callback) {
    	Pair<Float, Float> center = enemy.center();
        final IEntity bullet = mBulletPool.obtainPoolItem();
        
        bullet.setPosition(center.first, center.second);
        bullet.setColor(Color.WHITE);
        bullet.setScale(1.5f);
        
        float toX = GameActivity.SCREEN_WIDTH / 2f;
        float toY = mMemberList[0].center().second;
        
        final MoveModifier modifier = new MoveModifier(0.25f, center.first, toX, center.second, toY){
        	@Override
        	protected void onModifierFinished(IEntity pItem) {
        		boolean resisted = resistBadEffect(AwokenSkill.RESISTANCE_SKILL_LOCK);
        		if(!resisted) { // mTeamData.isLockedSkill()
        			EnemySkill skill = EnemySkill.newSkill(EnemySkill.TYPE.BIND_SKILL, turns);
        			fireGlobalSkill(Constants.SK_SKILL_LOCK, skill, callback);
        			// TODO: show lock animation
        		} else {
        			// TODO: play resistance animation
        			callback.onCastFinish(true);
        		}
        		
                mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        mSceneRef.get().detachChild(bullet);
                        mBulletPool.recyclePoolItem(bullet);
                    }
                });
        	}
        };
        mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
            
            @Override
            public void run() {
            	modifier.setAutoUnregisterWhenFinished(true);
            	bullet.registerEntityModifier(modifier);
                mSceneRef.get().attachChild(bullet);
            }
        });
    }
    
    public void bindPets(Enemy enemy, List<Pair<Integer, Integer>> bindList, final ICastCallback callback) {
    	int size = bindList.size();
    	if(size == 0) {
    		callback.onCastFinish(true);
    		return ;
    	}
    	Pair<Float, Float> center = enemy.center();
    	final AtomicInteger callbackCounter = new AtomicInteger(size);
    	for(Pair<Integer, Integer> pair : bindList) {
            final IEntity bullet = mBulletPool.obtainPoolItem();
            final Pair<Integer, Integer> bindTo = pair;
            bullet.setPosition(center.first, center.second);
            bullet.setColor(Color.WHITE);
            bullet.setScale(1.5f);
            
            Pair<Float, Float> memberPos = mMemberList[pair.first].center();
            final MoveModifier modifier = new MoveModifier(0.25f, center.first, memberPos.first, center.second, memberPos.second){
            	@Override
            	protected void onModifierFinished(IEntity pItem) {
                    mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
                        
                        @Override
                        public void run() {
                        	boolean resistBind = false;
                        	if(!isAwokenLocked() && bindTo.first == 0 && singleUp == SinglePowerUp.RESIST_BIND) {
                        		resistBind = true;
                        	}
                        	if(!resistBind) {
	                    		boolean binded = mMemberList[bindTo.first].bind(bindTo.second);
	                    		if (binded) {
	                    			//FIXME: need to maintain a bind status in team data?
	                    			//mTeamData.bindPet(bindTo.first, bindTo.second);
	                    			mTeamData.bindPet(bindTo.first, true);
	                    			if(bindTo.first == 0 || bindTo.first == 5) {
	                    				getScene().updateSpecialLST();
	                    				setCurrentHp(mTeamData.getCurrentHp(), false);
	                    			}
	                    		}
                        	}

                            mSceneRef.get().detachChild(bullet);
                            mBulletPool.recyclePoolItem(bullet);
                        }
                    });
                    if(callbackCounter.decrementAndGet()==0) {
                    	callback.onCastFinish(true);
                    }
            	}
            };
            
            mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
                
                @Override
                public void run() {
                	modifier.setAutoUnregisterWhenFinished(true);
                	bullet.registerEntityModifier(modifier);
                    mSceneRef.get().attachChild(bullet);
                }
            });
    	}

    }

    public void attackByOthers(int damage) {
    	mHp.addDamage(-damage);
    	int currentHp = mTeamData.getCurrentHp() + damage;
    	setCurrentHp(currentHp);
    }
    
    private void showToast(String title, String description, float offsetY) {
    	showToast(title + "\n" + description, offsetY);
    }
    
    private void showToast(final String text, final float offsetY) {
		final Activity activity = getScene().getActivity();
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
                View toastView = activity.getLayoutInflater().inflate(R.layout.layout_toast, null);
                Toast customToast = new Toast(activity);
                customToast.setDuration(Toast.LENGTH_SHORT);
                customToast.setView(toastView);
				TextView view = toastView.findViewById(android.R.id.text1);
				view.setText(text);
				customToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 
						0, (int)offsetY);
				customToast.show();
			}
		});
    }
    
    public void toastEnemyAction(final EnemyAction action, Enemy enemy) {
    	if(!TextUtils.isEmpty(action.title)) {
    		showToast(action.title, action.description, enemy.sprite.getY() + enemy.sprite.getHeight());
    	}
    }
    
    public void toastEnemyAction(final EnemyAction action, Enemy enemy, final Action0 todo) {
    	if(!TextUtils.isEmpty(action.title)) {
    		showToast(action.title, action.description, enemy.sprite.getY() + enemy.sprite.getHeight());
    		handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					todo.call();
				}
			}, 1000L); // toast short -> 2000L
    		
    	} else {
    		todo.call();
    	}
    }
    

    @Override
    public void attackByEnemyMultiple(final Enemy enemy, final int attackTimes, final int damage, final ICastCallback callback) {
    	// TODO: check buff, reduce damage... etc
    	
    	// play attack animation / show dialog
    	Pair<Float, Float> center = enemy.center();
        final IEntity bullet = mBulletPool.obtainPoolItem();
        
        bullet.setPosition(center.first, center.second);
        bullet.setColor(Color.RED);
        bullet.setScale(1.5f);
        
        float toX = GameActivity.SCREEN_WIDTH / 2f;
        float toY = mMemberList[0].center().second;
        
        final MoveModifier modifier = new MoveModifier(0.25f, center.first, toX, center.second, toY){
        	@Override
        	protected void onModifierFinished(IEntity pItem) {
        		int recalcDamage = reducedByShield(enemy, damage);

        		final AtomicInteger countDown = new AtomicInteger(attackTimes);
        		for(int i=0; i<attackTimes; ++i) {
	        		mTeamData.reduceDamage(enemy, recalcDamage, new IDamageCallback() {
						
						@Override
						public void onResult(double damage, List<Boolean> reduced) {
							int damageReduced = (int)damage;
							boolean last = countDown.decrementAndGet() == 0;
					    	mHp.addDamage(damageReduced);
					    	
					    	int currentHp = mTeamData.getCurrentHp() - damageReduced;
					    	setCurrentHp(currentHp, last);
	
					    	for(int i=0; i<6; ++i) {
					    		Boolean fired = reduced.get(i);
					    		if(fired) {
					    			Jump.apply(mMemberList[i].sprite, 0.3f);
					    		}
					    	}
					    	
					    	if(mSkillList.containsKey(Constants.SK_COUNTER_ATTACK)) {
					    		SkillEntity entity = mSkillList.get(Constants.SK_COUNTER_ATTACK);
					    		List<Integer> data = entity.skill.getData();
					    		int x = data.get(1);
					    		int prop = data.get(2);
					    		
					    		enemy.dealtDamage(AttackValue.create(damageReduced*x, prop, 0), 0.1f, new AnimationCallback() {

									@Override
									public void animationDone() {
									}
					    			
					    		});
					    	}
                            counterStrike(mMemberList[0], enemy, damageReduced);
                            counterStrike(mMemberList[5], enemy, damageReduced);
					    	
					    	//mHp.getDamage(damage, mTeamData.getCurrentHp(), mTeamData.getHp());
					    	if(callback != null && last) {
					    		callback.onCastFinish(true);
					    	}
					    	
						}
					});
        		}
            	
                mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        mSceneRef.get().detachChild(bullet);
                        mBulletPool.recyclePoolItem(bullet);
                    }
                });
        	}
        };
        mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
            
            @Override
            public void run() {
            	modifier.setAutoUnregisterWhenFinished(true);
            	bullet.registerEntityModifier(modifier);
                mSceneRef.get().attachChild(bullet);
            }
        });

    }

    private void counterStrike(Member member, Enemy enemy, int damageReduced) {
        if(member != null) {
            for(LeaderSkill ls : member.info.getLeaderSkill()) {
                if(ls.getType() == LeaderSkillType.LST_COUNTER_STRIKE) {
                    List<Integer> data = ls.getData();
                    if(RandomUtil.getLuck(data.get(0))) {
                        int x = data.get(1);
                        int prop = data.get(2);

                        enemy.dealtDamage(AttackValue.create(damageReduced*x, prop, 0), 0.1f, new AnimationCallback() {

                            @Override
                            public void animationDone() {
                            }

                        });
                    }
                    break;
                }
            }
        }
    }

    private int reducedByShield(Enemy enemy, int damage) {
    	int recalcDamage = damage;
		if(mSkillList.containsKey(Constants.SK_SHIELD)) {
			SkillEntity se = mSkillList.get(Constants.SK_SHIELD);
			ActiveSkill as = (ActiveSkill) se.skill;
			List<Integer> data = as.getData();
			
			int percent = data.get(1);
			if(data.size()<3) {
    			recalcDamage = (int)(damage * (100-percent) / 100.0f);
			} else {
				int prop = data.get(2);
				if(prop==enemy.currentProp()) {
					recalcDamage = (int)(damage * (100-percent) / 100.0f);
				}
			}
		}
		if(mCrossShield != null) {
			recalcDamage = (int)(recalcDamage * getCrossReduceShield());
		}
		return recalcDamage;
    }
    
    @Override
    public void attackByEnemy(final Enemy enemy, final int damage, final ICastCallback callback) {
    	// TODO: check buff, reduce damage... etc
    	
    	// play attack animation / show dialog
    	Pair<Float, Float> center = enemy.center();
        final IEntity bullet = mBulletPool.obtainPoolItem();
        
        bullet.setPosition(center.first, center.second);
        bullet.setColor(Color.RED);
        bullet.setScale(1.5f);
        
        float toX = GameActivity.SCREEN_WIDTH / 2f;
        float toY = mMemberList[0].center().second;
        
        final MoveModifier modifier = new MoveModifier(0.25f, center.first, toX, center.second, toY){
        	@Override
        	protected void onModifierFinished(IEntity pItem) {
        		int recalcDamage = reducedByShield(enemy, damage);

        		mTeamData.reduceDamage(enemy, recalcDamage, new IDamageCallback() {
					
					@Override
					public void onResult(double damage, List<Boolean> reduced) {
						int damageReduced = (int)damage;
				    	mHp.addDamage(damageReduced);
				    	
				    	int currentHp = mTeamData.getCurrentHp() - damageReduced;
				    	setCurrentHp(currentHp);

				    	for(int i=0; i<6; ++i) {
				    		Boolean fired = reduced.get(i);
				    		if(fired) {
				    			Jump.apply(mMemberList[i].sprite, 0.3f);
				    		}
				    	}
				    	
				    	if(mSkillList.containsKey(Constants.SK_COUNTER_ATTACK)) {
				    		SkillEntity entity = mSkillList.get(Constants.SK_COUNTER_ATTACK);
				    		List<Integer> data = entity.skill.getData();
				    		int x = data.get(1);
				    		int prop = data.get(2);
				    		
				    		enemy.dealtDamage(AttackValue.create(damageReduced*x, prop, 0), 0.1f, new AnimationCallback() {

								@Override
								public void animationDone() {
								}
				    			
				    		});
				    	}
                        counterStrike(mMemberList[0], enemy, damageReduced);
                        counterStrike(mMemberList[5], enemy, damageReduced);

				    	//mHp.getDamage(damage, mTeamData.getCurrentHp(), mTeamData.getHp());
				    	if(callback != null) {
				    		callback.onCastFinish(true);
				    	}
					}
				});
            	
                mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
                    
                    @Override
                    public void run() {
                    	try {
	                        mSceneRef.get().detachChild(bullet);
	                        mBulletPool.recyclePoolItem(bullet);
                    	} catch(Exception e) {
                    	}
                    }
                });
        	}
        };
        mSceneRef.get().engine.runOnUpdateThread(new Runnable() {
            
            @Override
            public void run() {
            	modifier.setAutoUnregisterWhenFinished(true);
            	bullet.registerEntityModifier(modifier);
                mSceneRef.get().attachChild(bullet);
            }
        });

    }
    
//    public void gravityByEnemy(final Enemy enemy, int percent, final ICastCallback callback) {
//    	double damage = mTeamData.getCurrentHp() * percent / 100.0;
//    	mTeamData.reduceDamage(enemy, (int)damage, new IDamageCallback() {
//			
//			@Override
//			public void onResult(double damage, List<Boolean> reduced) {
//				int damageReduced = (int)damage;
//		    	mHp.addDamage(damageReduced);
//		    	
//		    	int currentHp = mTeamData.getCurrentHp() - damageReduced;
//		    	setCurrentHp(currentHp);
//		    	
//		    	if(mSkillList.containsKey(Constants.SK_COUNTER_ATTACK)) {
//		    		SkillEntity entity = mSkillList.get(Constants.SK_COUNTER_ATTACK);
//		    		List<Integer> data = entity.skill.getData();
//		    		int x = data.get(1);
//		    		int prop = data.get(2);
//		    		
//		    		enemy.dealtDamage(AttackValue.create(damageReduced*x, prop, 0), 0.1f, new AnimationCallback() {
//
//						@Override
//						public void animationDone() {
//						}
//		    			
//		    		});
//		    	}
//		    	
//		    	if(callback != null) {
//		    		callback.onCastFinish(true);
//		    	}
//			}
//		});
//
//    }
    
    private void setCurrentHp(int currentHp) {
    	setCurrentHp(currentHp, true);
    }
    
    private void setCurrentHp(int currentHp, boolean withAnimation) {
        int hp = getTeamHpTotal();
        if(currentHp < 0) {
            currentHp = 0;
        } else if (currentHp > hp) {
            currentHp = hp;
        }
        mHp.setHp(currentHp, getTeamHpTotal(), withAnimation);
        mTeamData.setCurrentHp(currentHp);
    }

    public void recoverPlayerHp(int recovery) {
        mHp.recovery(recovery);

        int currentHp = mTeamData.getCurrentHp() + recovery;
        setCurrentHp(currentHp);
    }


    @Override
    public void showSkillDialog(final int counter, final MonsterInfo info, final int cd1,final int cd2, final ISkillCallback callback) {
        if(getScene().getState() != GameState.GAME_PLAYER_TURN) {
            return ;
        }
        if(needConfirmSkill()) {
        	Observable.just(0)
    		.observeOn(AndroidSchedulers.mainThread())
    		.subscribe(new Action1<Integer>() {
				
				@Override
				public void call(Integer arg0) {
					DialogUtil.getSkillDialog(getScene().getActivity(), info, counter, cd1, cd2,
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									callback.onFire(PadEnvironment.this, (cd2>0 && counter>=(cd1+cd2))? 2:1);
								}
							},
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).show();
					}
						
			});

        } else {
            callback.onFire(this);
        }
    }

	public boolean isSkillLocked() {
		return mSkillList.containsKey(Constants.SK_SKILL_LOCK);
	}

    public boolean hasSkill(String skill) {
        return mSkillList.containsKey(skill);
    }

    @Override
    public void removeSkill(String key) {
        if (mSkillList.containsKey(key)) {
            mSkillList.get(key).detach();
            mSkillList.remove(key);
            if(key.equals(Constants.SK_DROP_LOCK)) {
                getScene().removeDropLock();
            } else if (key.equals(Constants.SK_LOCK_REMOVE)) {
                getScene().removeSkill(Constants.SK_LOCK_REMOVE);
            }
        }
    }

    public Member getMember(int index) {
		return mMemberList[index];
	}

	public void skillBoost(final List<Integer> adjust, final Member owner, final ICastCallback callback) {

		Observable.just(adjust)
			.observeOn(Schedulers.immediate())
			.map(new Func1<List<Integer>, List<Pair<Boolean, Integer>>>() {
				
				@Override
				public List<Pair<Boolean, Integer>> call(List<Integer> data) {
					List<Pair<Boolean, Integer>> boosted = new ArrayList<Pair<Boolean, Integer>>(6);
					for(int index=0; index<6; ++index) {
						Member member = mMemberList[index];
                        int adjust = data.get(index);
                        int newAdjust = adjust;
						if(member == null || member == owner) {
						    newAdjust = 0;
                        } else {
                            if (adjust < 0) {
                                // check money awoke skill
                                int count = member.info.getTargetPotentialAwokenCount(MoneyAwokenSkill.SKILL_HINDER);
                                newAdjust += count;
                                if (newAdjust > 0) {
                                    newAdjust = 0;
                                }
                            }
                        }
						
						boolean boost = member.adjustCd(newAdjust);
						boosted.add(new Pair<Boolean, Integer>(boost, newAdjust));
						
					}
					return boosted;
				}
			})
			.subscribe(new Action1<List<Pair<Boolean, Integer>>>() {
				
				@Override
				public void call(List<Pair<Boolean, Integer>> boosted) {
					ResourceManager res = ResourceManager.getInstance();
					final List<Text> skillText = new ArrayList<Text>();
					final AtomicInteger countDown = new AtomicInteger(0);
					
					for(int index=0; index<6; ++index) {
						if(boosted.get(index).first) {
							countDown.incrementAndGet();
						}
					}
					
					if(countDown.get() == 0) {
						callback.onCastFinish(true);
					} else {
						final PlaygroundGameScene scene = getScene();
						for(int index=0; index<6; ++index) {
							if(boosted.get(index).first && mMemberList[index] != null) {
								float x = mMemberList[index].sprite.getX();
								float y = mMemberList[index].sprite.getY();
								float w = mMemberList[index].sprite.getWidth() / 2f;
								
								String adjustText;
								int cd = boosted.get(index).second;
                                float sy, ey;
                                if(cd>=0) {
                                    adjustText = "S+" + cd;
                                    sy = y;
                                    ey = y-16;
                                } else {
                                    adjustText = "S" + cd;
                                    sy = y-16;
                                    ey = y;
                                }
								Text text = new Text(0, 0, res.getFontStroke(), adjustText, res.getVBOM());
								text.setZIndex(6);
								text.setScale(1.8f);
								text.setPosition(x + w - text.getWidth()/2, y);
								text.setColor(Color.WHITE);
								text.registerEntityModifier(new MoveYModifier(0.5f, sy, ey){
									@Override
									protected void onModifierFinished(IEntity pItem) {
										scene.detach(pItem);
										if(countDown.decrementAndGet() == 0) {
											callback.onCastFinish(true);
										}
									}
								});
								skillText.add(text);
							}
						}
						scene.engine.runOnUpdateThread(new Runnable() {

							@Override
							public void run() {
								getScene().attach(skillText);
							}
							
						});
					}
					

				}
			});
	}

	public void skillBoost(final int adjust, Member owner, final ICastCallback callback) {

		Observable.just(owner)
			.observeOn(Schedulers.immediate())
			.map(new Func1<Member, List<Pair<Boolean, Integer>>>() {
				
				@Override
				public List<Pair<Boolean, Integer>> call(Member owner) {
					List<Pair<Boolean, Integer>> boosted = new ArrayList<Pair<Boolean, Integer>>(6);
					for(int index=0; index<6; ++index) {
						Member member = mMemberList[index];
						if(member != null && member != owner) {
							int newAdjust = adjust;
					        if(adjust<0) {
					            // check money awoke skill
					        	int count = member.info.getTargetPotentialAwokenCount(MoneyAwokenSkill.SKILL_HINDER);
					        	newAdjust += count;
					        	if(newAdjust > 0) {
					        		newAdjust = 0;
					        	}
					        }
							
							boolean boost = member.adjustCd(newAdjust);
							boosted.add(new Pair<Boolean, Integer>(boost, newAdjust));
						} else {
							boosted.add(new Pair<Boolean, Integer>(false, 0));
						}
					}
					return boosted;
				}
			})
			.subscribe(new Action1<List<Pair<Boolean, Integer>>>() {
				
				@Override
				public void call(List<Pair<Boolean, Integer>> boosted) {
					ResourceManager res = ResourceManager.getInstance();
					final List<Text> skillText = new ArrayList<Text>();
					final AtomicInteger countDown = new AtomicInteger(0);
					
					for(int index=0; index<6; ++index) {
						if(boosted.get(index).first) {
							countDown.incrementAndGet();
						}
					}
					
					final PlaygroundGameScene scene = getScene();
					for(int index=0; index<6; ++index) {
						Pair<Boolean, Integer> pair = boosted.get(index);
						if(pair.first && mMemberList[index] != null) {
							float x = mMemberList[index].sprite.getX();
							float y = mMemberList[index].sprite.getY();
							float w = mMemberList[index].sprite.getWidth() / 2f;
							
							String adjustText;
							if(pair.second>=0) {
								adjustText = "S+" + pair.second;
							} else {
								adjustText = "S" + pair.second;
							}
							Text text = new Text(0, 0, res.getFontStroke(), adjustText, res.getVBOM());
							text.setZIndex(6);
							text.setScale(1.8f);
							text.setColor(Color.WHITE);
							text.setPosition(x + w - text.getWidth()/2, y);
							text.registerEntityModifier(new MoveYModifier(0.5f, y, y-16){
								@Override
								protected void onModifierFinished(IEntity pItem) {
									scene.detach(pItem);
									if(countDown.decrementAndGet() == 0) {
										callback.onCastFinish(true);
									}
								}
							});
							skillText.add(text);
						}
					}
					if(countDown.get() == 0) {
						callback.onCastFinish(true);
					}
					scene.engine.runOnUpdateThread(new Runnable() {

						@Override
						public void run() {
							getScene().attach(skillText);
						}
						
					});
				}
			});
		
	}
    
	@Override
	public boolean hasBuff() {
		if(mSkillList.containsKey(Constants.SK_TIME_CHANGE)) {
			SkillEntity entity = mSkillList.get(Constants.SK_TIME_CHANGE);
			if(entity.skill.getData().get(1)>0) {
				return true;
			}
		}
		if(mSkillList.containsKey(Constants.SK_TIME_CHANGE_X)) {
			SkillEntity entity = mSkillList.get(Constants.SK_TIME_CHANGE_X);
			if(entity.skill.getData().get(1)>1000) {
				return true;
			}
		}
        boolean has_positive = false;
        if(mSkillList.containsKey(Constants.SK_DROP_RATE)) {
		    SkillEntity entity = mSkillList.get(Constants.SK_DROP_RATE);
		    List<Integer> data = entity.skill.getData();
            has_positive = true;
		    for(int i=1; i<data.size(); i += 2) {
		        if(data.get(i) >= 6) {
                    has_positive = false;
		            break;
                }
            }
        }
		
		return mSkillList.containsKey(Constants.SK_COUNTER_ATTACK) ||
				mSkillList.containsKey(Constants.SK_POWER_UP) ||
				mSkillList.containsKey(Constants.SK_SHIELD) || has_positive;
	}

    @Override
    public boolean isAwokenLocked() {
        return mSkillList.containsKey(Constants.SK_AWOKEN_LOCK);
    }
}
