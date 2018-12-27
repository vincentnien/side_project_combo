package com.a30corner.combomaster.playground.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.graphics.PointF;
import android.util.Log;
import android.util.Pair;

import com.a30corner.combomaster.engine.sprite.BasicHpSprite;
import com.a30corner.combomaster.engine.sprite.BasicHpSprite.AnimationCallback;
import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.pad.DamageCalculator.AttackValue;
import com.a30corner.combomaster.pad.PadBoardAI;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill;
import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.action.impl.Attack;
import com.a30corner.combomaster.playground.enemy.EnemyAttack;
import com.a30corner.combomaster.playground.enemy.EnemyAttackMode;
import com.a30corner.combomaster.playground.entity.impl.AbsorbShield;
import com.a30corner.combomaster.playground.entity.impl.Angry;
import com.a30corner.combomaster.playground.entity.impl.ChangeTurn;
import com.a30corner.combomaster.playground.entity.impl.ComboShield;
import com.a30corner.combomaster.playground.entity.impl.DamageAbsorbShield;
import com.a30corner.combomaster.playground.entity.impl.DamageVoidShield;
import com.a30corner.combomaster.playground.entity.impl.Delay;
import com.a30corner.combomaster.playground.entity.impl.Konjo;
import com.a30corner.combomaster.playground.entity.impl.Poison;
import com.a30corner.combomaster.playground.entity.impl.ReduceDef;
import com.a30corner.combomaster.playground.entity.impl.ResistanceShield;
import com.a30corner.combomaster.playground.entity.impl.Shield;
import com.a30corner.combomaster.scene.PlaygroundGameScene;
import com.a30corner.combomaster.texturepacker.SimulateAssets;
import com.a30corner.combomaster.utils.Constants;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.RandomUtil;
import com.a30corner.combomaster.utils.SimulatorConstants;



public class Enemy extends Entity {

    // monster data - attack turns
    private int mAttackTurnsSaved;
    // hp
    public int hp;
    // def
    public int def;
    // atk
    public int atk;
    // no
    public int no;
    
    public Pair<Integer, Integer> props;
    
    // current attack turns
    public int attackTurns = 1;
    
    public Text mTurnText;
    public BasicHpSprite mhp;
    private Sprite mTarget;
    
    private int mAttackCount = 1;
    private int currentMode = 0;
    
    private boolean isDead = false;
    
    private boolean attackTarget = false;
    private LinkedList<BuffOnEnemy> mFixedList = new LinkedList<BuffOnEnemy>();
    private LinkedList<BuffOnEnemy> mBuffList = new LinkedList<BuffOnEnemy>();
    private LinkedList<BuffOnEnemy> mDebuffList = new LinkedList<BuffOnEnemy>();

    // for konjo... is it better to put to another place?
    private int konjoHp = -1;

    private IEntityModifier turnModifier = null;
    private IEntityModifier[] modifierArray = new IEntityModifier[4];
    
    public EnemyAttackMode attackMode;
    public List<MonsterSkill.MonsterType> types = new ArrayList<MonsterSkill.MonsterType>();
    
    private boolean backThisTurn = false;
    private boolean hasResurrection = false;
    
    private Enemy(IEnvironment env, int no, int hp, int atk, int def, int turns, Pair<Integer, Integer> props, List<MonsterSkill.MonsterType> types) {
        super(env);
        this.no = no;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        mAttackTurnsSaved = turns;
        this.props = props;
        this.types.addAll(types);
        
        for(int i=0; i<4; ++i) {
            float duration = 0.1f * i + 0.05f;
            LoopEntityModifier modifier = new LoopEntityModifier(new SequenceEntityModifier(
                    new ColorModifier(duration, Color.WHITE, Color.RED), new ColorModifier(duration, Color.RED, Color.WHITE)));
            modifier.setAutoUnregisterWhenFinished(true);
            modifierArray[i] = modifier;
        }
    }
    
    public static Enemy create(IEnvironment env, int no, int hp, int atk, int def, int turns, Pair<Integer, Integer> props, List<MonsterSkill.MonsterType> types) {
        return new Enemy(env, no, hp, atk, def, turns, props, types);
    }
    
    float hpLength;
    // FIXME:  remove hp width, use create to set the width
    public void loadData(PlaygroundGameScene scene, float hpWidth) {
    	hpLength = hpWidth;
        int random = new Random().nextInt(3)-1;
        attackTurns = mAttackTurnsSaved + random;
        if (attackTurns <= 0) {
            attackTurns = 1;
        }
        isDead = false;
        
        ResourceManager res = ResourceManager.getInstance();
        VertexBufferObjectManager vbom = envRef.get().getScene().vbom;
        ITextureRegion region = res.loadTextureFile(no + "i.png");
        create(0, 0, region, vbom, new OnAreaTouchCallback() {
            
            @Override
            public boolean onAreaTouched(TouchEvent touchEvent,
                    float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(touchEvent.isActionDown()){
                    List<Enemy> enemies = envRef.get().getEnemies();
                    for(Enemy e : enemies) {
                        if(!e.dead() && e.equals(Enemy.this)) {
                            setTarget(!isTarget());
                        } else {
                            e.setTarget(false);
                        }
                    }
                }
                return true;
            }
        });
        
        mTarget = new Sprite(0, 0, res.getTextureRegion(SimulateAssets.class, SimulateAssets.TARGET_ID), vbom);
        mTarget.registerEntityModifier(new LoopEntityModifier(new RotationModifier(4.0f, 0, 360)));
        
        mhp = new BasicHpSprite(scene, (int)hpWidth, props, this);
        mhp.setHp(hp, hp);
        mhp.onCreate(0, 0);
        
        mTurnText = new Text(0f, 0f, res.getFontStroke(),"10", 4, envRef.get().getScene().vbom);
        mTurnText.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        mTurnText.setColor(Color.WHITE);
        setTurnText(attackTurns);
        attachChild(mTurnText);
        
        scene.registerTouchArea(sprite);
    }
    
    public void changeAttribute(int attribute) {
    	props = new Pair<Integer, Integer>(attribute, -1);
    	envRef.get().getScene().engine.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				mhp.changeAttribute(props.first);
			}
		});

    }
    
    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        
        float halfw = sprite.getWidth()/2;
        float cx = x + halfw;
        mhp.setPosition(cx - mhp.getWidth()/2, y + sprite.getHeight() + 12);

        mTurnText.setPosition(halfw, -mTurnText.getHeight()-8);
    }
    
    public boolean attackCountDown() {
    	if(diedThisTurn() || backThisTurn) {
    		backThisTurn = false;
    		return false;
    	}
    	
        setTurnText(attackTurns-1);
        countDownReduceDef();
        
        return attackTurns == 0;
    }
    
    private void countDownReduceDef() {
        for(BuffOnEnemy debuf : mDebuffList) {
        	if(debuf.isSkill(Type.REDUCE_DEF)) {
        		debuf.countDown();
        		if (debuf.endBuff()) {
        			sprite.detachChild(debuf.sprite);
        			mDebuffList.remove(debuf);
        			break;
        		}
        	}
        }
    }
    
    public void setTurnText(int turn) {
    	attackTurns = turn;
    	
        mTurnText.setText(String.valueOf(turn));
        if(turnModifier != null) {
            mTurnText.unregisterEntityModifier(turnModifier);
            turnModifier = null;
        }
        
        if(turn<4) {
            turnModifier = modifierArray[turn];
            mTurnText.registerEntityModifier(turnModifier);
        } else {
            mTurnText.setColor(Color.WHITE);
        }
    }
    
    public void initOwnAbility() {
    	for(BuffOnEnemy buff : mFixedList) {
    		buff.create();
    		attachChild(buff.sprite);
    	}
    	displayBuff();
    }
    
    public List<EnemyAction> firstStrike() {
    	EnemyAttack attack = attackMode.firstStrike(envRef.get(), this);
    	if ( attack != null ) {
    		if (attack.actionList.size()>0) {
    			mAttackCount = 0;
    			setTurnText(0);
    			countDownReduceDef();
    		}
    		return attack.actionList;
    	}
    	
    	return new ArrayList<EnemyAction>();
    }
    
    public List<EnemyAction> attack() {
        EnemyAttack attack = attackMode.nextMove(envRef.get(), this);
        if ( attack != null ) {
        	return attack.actionList;
        }
        
        List<EnemyAction> list = new ArrayList<EnemyAction>();
        list.add(new Attack(100));
        return list;
    }

    public int attackCount() {
    	return mAttackCount;
    }

    public boolean isMode(int max, int mode) {
        return (currentMode%max) == mode;
    }

    public void gotoMode() {
        gotoMode(-1);
    }

    public void gotoMode(int mode) {
        //Log.e("Vincent", "mode from " + currentMode + " to " + mode);
        if (mode == -1) {
            ++currentMode;
        } else {
            currentMode = mode;
        }
    }
    
    public void afterAttack() {
        if (attackTurns<=0) {
            setTurnText(mAttackTurnsSaved);
            ++mAttackCount;

            // remove if there is a delay 
            int size = mDebuffList.size();
            for(int i=0; i<size; ++i) {
            	BuffOnEnemy buff = mDebuffList.get(i);
            	if(buff.isSkill(Type.DELAY)) {
            		detachChild(buff.sprite);
            		mDebuffList.remove(buff);
            		break;
            	}
            }
        }
        countDownDebuff();
        updateEnemyState();
    }
    
    public void addOwnAbility(Type buff, int... data) {
    	IEnvironment env = envRef.get();
    	BuffOnEnemy buffOnEnemy = null;
    	switch(buff) {
    	case KONJO:
    		buffOnEnemy = Konjo.init(env, data[0]);
    		break;
    	case SHIELD:
    		buffOnEnemy = Shield.init(env, data[0], data[1], data[2]);
    		break;
    	case CHANGE_TURN:
    	    buffOnEnemy = ChangeTurn.init(env, data[0]);
    	default:
    	}
    	if(buffOnEnemy != null) {
    		mFixedList.add(buffOnEnemy);
    	}
    }
    
    public void addBuff(Type buff, List<Integer> data) {
    	IEnvironment env = envRef.get();
        BuffOnEnemy buffOnEnemy = null;
        
        for(BuffOnEnemy exist : mBuffList) {
            if (!exist.isSkill(Type.ABSORB_SHIELD) && exist.isSkill(buff)) {
                return ;
            }
        }
        
        switch(buff) {
        case RESISTANCE_SHIELD:
            buffOnEnemy = ResistanceShield.init(env, data.get(0));
            break;
        case ABSORB_SHIELD:
            buffOnEnemy = AbsorbShield.init(env, data.get(0), data.get(1));
            break;
        case ANGRY:
            buffOnEnemy = Angry.init(env, data.get(0), data.get(1));
            break;
        case DAMAGE_ABSORB_SHIELD:
            buffOnEnemy = DamageAbsorbShield.init(env, data.get(0), data.get(1));
            break;
        case DAMAGE_VOID_SHIELD:
            buffOnEnemy = DamageVoidShield.init(env, data.get(0), data.get(1));
            break;
        case KONJO:
            buffOnEnemy = Konjo.init(env, data.get(0));
            break;
        case SHIELD:
            buffOnEnemy = Shield.init(env, data.get(0), data.get(1), data.get(2));
            break;
        case COMBO_SHIELD:
        	buffOnEnemy = ComboShield.init(env, data.get(0), data.get(1));
            break;
        default:
            return ;
        }
        buffOnEnemy.create();
        attachChild(buffOnEnemy.sprite);
        mBuffList.add(buffOnEnemy);
        displayBuff();
    }
    
    public boolean addDebuff(Member owner, Type debuff, List<Integer> data) {
        // if resistance shield, defuff is void
        for(BuffOnEnemy buff : mBuffList) {
            if (buff != null && buff.isSkill(Type.RESISTANCE_SHIELD)) {
                return false;
            }
        }
        boolean hasDelay = false;
        int index = -1;
        int size = mDebuffList.size();
        for(int i=0; i<size; ++i) {
            BuffOnEnemy b = mDebuffList.get(i);
            if(b != null) {
                if (b.isSkill(Type.DELAY)) {
                    hasDelay = true;
                } else if (b.isSkill(debuff)) {
                    if (!b.canOverride()) {
                        return false;
                    }
                    index = i;
                }
            }
        }
        
        // if already be delayed, cannot delay again
        if (debuff == Type.DELAY) {
        	if (hasDelay) {
        		return false;
        	} else {
        		int turns = attackTurns + data.get(0);
        		setTurnText(turns);
        	}
        }
        
        IEnvironment env = envRef.get();
        BuffOnEnemy buff = null;
        switch(debuff) {
        case CHANGE_ATTR:
        	break;
        case DELAY:
        	buff = Delay.init(env, data.get(0));
        	break;
        case POISON:
        	buff = Poison.init(env, data.get(0)*owner.info.getAtk());
        	break;
        case REDUCE_DEF:
        	buff = ReduceDef.init(env, data.get(0), data.get(1));
        	break;
    	default:
    	    break;
        }
        if ( buff != null ) {
        	// replace old if the same prop
        	buff.create();
        	if ( index != -1 ) {
        		BuffOnEnemy old = mDebuffList.get(index);
        		detachChild(old.sprite);

        		mDebuffList.set(index, buff);
        	} else {
        		mDebuffList.add(buff);
        	}
//	        int pos = mDebuffList.size()-1;
//	        buff.setPosition(-buff.sprite.getWidth()-30, 30*pos);
	        attachChild(buff.sprite);
        }
        displayBuff();
        return true;
    }

    @Override
    public void attach() {
        super.attach();
        final PlaygroundGameScene scene = envRef.get().getScene();
        scene.engine.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                mhp.attachChild();
            }
        });
    }
    
    @Override
    public void detach() {
        super.detach();
        final PlaygroundGameScene scene = envRef.get().getScene();
        if(mhp != null) {
	        scene.engine.runOnUpdateThread(new Runnable() {
	            @Override
	            public void run() {
	                mhp.detachChild();
	            }
	        });
        }
        
    }
    
    private void countDownBuff() {
        List<BuffOnEnemy> removeList = new ArrayList<BuffOnEnemy>();
        int size = mBuffList.size();
        for(int i=0; i<size; ++i) {
            BuffOnEnemy buff = mBuffList.get(i);
            buff.countDown();
            if (buff.endBuff()) {
                sprite.detachChild(buff.sprite);
                removeList.add(buff);
            }
        }
        if (removeList.size()>0) {
            mBuffList.removeAll(removeList);
        }
        displayBuff();
    }
    
    private void countDownDebuff() {
    	List<BuffOnEnemy> removeList = new ArrayList<BuffOnEnemy>();
    	int size = mDebuffList.size();
    	for(int i=0; i<size; ++i) {
    		BuffOnEnemy buff = mDebuffList.get(i);
    		if(buff.isSkill(Type.REDUCE_DEF)) {
    			continue;
    		}
    		buff.countDown();
    		if (buff.endBuff()) {
    			sprite.detachChild(buff.sprite);
    			removeList.add(buff);
    		}
    	}
    	
    	mDebuffList.removeAll(removeList);
    	displayBuff();
    }
    
    private float getAdjustX(float x) {
    	float screenX = sprite.getX() + x;
    	return (screenX<0)? x-screenX:x;
    }
    
    private void displayBuff() {
        int index = 0;
        
        int fixedsize = mFixedList.size();
        for(int i=0; i<fixedsize; ++i, ++index) {
            BuffOnEnemy buff = mFixedList.get(i);
            if(buff != null) {
                buff.setPosition(-20, -60 + 30 * index);
            }
        }
        
        int buffsize = mBuffList.size();
        for(int i=0; i<buffsize; ++i, ++index) {
            BuffOnEnemy buff = mBuffList.get(i);
            //-buff.sprite.getWidth()-20 -buff.sprite.getWidth()-20
            if(buff != null) {
                buff.setPosition(-20, -60 + 30 * index);
            }
        }
        
        int debuffsize = mDebuffList.size();
        for(int i=0; i<debuffsize; ++i, ++index) {
            BuffOnEnemy buff = mDebuffList.get(i);
          //-buff.sprite.getWidth()-20  getAdjustX(sprite.getX())
            if(buff != null) {
                buff.setPosition(-20, -60 + 30 * index);
            }
        }
    }
    
    public void setHasResurrection() {
    	hasResurrection = true;
    }
    
    public boolean hasResurrection() {
    	return hasResurrection;
    }
    
    public void setDead() {
        isDead = true;

        registerEntityModifier(new AlphaModifier(1.0f, 1.0f, 0.0f){
            @Override
            protected void onModifierFinished(IEntity pItem) {
            	if(!hasResurrection) {
            		detach();
            	} else {
            		for(BuffOnEnemy buff : mFixedList) {
            			buff.setVisible(false);
            			buff.detach();
            		}
            		for(BuffOnEnemy buff : mBuffList) {
            			buff.setVisible(false);
            			buff.detach();
            		}
            		for(BuffOnEnemy buff : mDebuffList) {
            			buff.setVisible(false);
            			buff.detach();
            		}
            		mTurnText.setVisible(false);
            	}
            	mBuffList.clear();
            	mDebuffList.clear();
            }
        });
    }
    
    @Override
    public void registerEntityModifier(final IEntityModifier modifier) {
        envRef.get().getScene().engine.runOnUpdateThread(new Runnable() {
            
            @Override
            public void run() {
                sprite.registerEntityModifier(modifier);
                mhp.registerEntityModifier(modifier);
            }
        });
    }
    
    public boolean dead() {
        return isDead;
    }
    
    public boolean diedThisTurn() {
    	return mhp.isDead();
    }

    public void recoverHp(int percent, ICastCallback callback) {
    	if(diedThisTurn()) {
    		isDead = true;
    	}
    	
    	int recover = (int)(mhp.getHpFull() / 100.0 * percent);
    	int hp = mhp.getHp() + recover;
    	float previous = mhp.getHp()/(float)mhp.getHpFull();
    	mhp.setCurrentHp(hp, callback);
    	mhp.playRecoverAnimation(previous);
    	
    	updateEnemyState();
    }
    
    public void dealtDamageDirect(AttackValue attack, float animationTime, AnimationCallback callback) {
        if(dead()) {
            return ;
        }
        int damage = calculateBuffWOComboDirect(attack.prop, attack.damage);
        mhp.addDamage(damage);
        mhp.playDamagedAnimation(animationTime, callback);
        
        displayDamage(attack, 1);
    }
    
    private void displayDamage(AttackValue attack, int restraint) {
    	// FIXME:
    	Color[] ATTACK_COLOR = new Color[6];
        ATTACK_COLOR[0] = new Color(0.8f, 0.0f, 0.8f);
        ATTACK_COLOR[1] = Color.RED;
        ATTACK_COLOR[2] = Color.WHITE;
        ATTACK_COLOR[3] = Color.YELLOW;
        ATTACK_COLOR[4] = Color.BLUE;
        ATTACK_COLOR[5] = Color.GREEN;
    	
        ResourceManager res = ResourceManager.getInstance();
        String display = (attack.damage>=0.0)? String.valueOf((int)attack.damage):String.format("+%.0f", -attack.damage);
        Text text = new Text(0, 0, res.getFontStroke(), display, res.getVBOM());
        float x = sprite.getX()-text.getWidth()/2+sprite.getWidth()/2;
        float y = sprite.getY();
        if(attack.prop >= 0 && attack.prop <= 5) {
        	text.setColor(ATTACK_COLOR[attack.prop]);
        }
        int upDown = restraint * RandomUtil.range((int)(text.getHeight()*1.5), (int)text.getHeight()*3);
        text.registerEntityModifier(new MoveModifier(1.2f, x, x+RandomUtil.range(-40, 40), y, y-upDown){
            @Override
            protected void onModifierFinished(IEntity pItem) {
                envRef.get().getScene().detach(pItem);
            }
        });
        envRef.get().getScene().attach(text);
    }
    
    public double dealtDamageByLST(AttackValue attack, float animationTime, AnimationCallback callback) {
        double damage = attack.damage;
        if (damage <= 0.0) {
            return 0.0;
        }
        
        int restraint = 1; // up or down
        if(PadBoardAI.isRestraint(attack.prop, mhp.currentProp())){
            damage *= 2.0;
        } else if (PadBoardAI.beRestraint(attack.prop, mhp.currentProp())) {
            damage /= 2.0;
            restraint = -1;
        }

        // check there's a buff or something...
        for(BuffOnEnemy buff : mFixedList) {
       	 if(buff.isSkill(Type.SHIELD)) {
            	int prop = buff.getData().get(1);
                int percent = buff.getData().get(2);
                
                if(prop == -1 || attack.prop == prop) {
                	damage = damage * (100-percent) / 100.0;
                }
            }
       }
        for(BuffOnEnemy buff : mBuffList) {
        	 if(buff.isSkill(Type.SHIELD)) {
             	int prop = buff.getData().get(1);
                 int percent = buff.getData().get(2);
                 if(prop == -1 ||attack.prop == prop) {
                 	damage = damage * (100-percent) / 100.0;
                 }
             }
        }
        
        double defBuffed = def;
        for(BuffOnEnemy buff : mDebuffList) {
            if(buff.isSkill(Type.REDUCE_DEF)) {
                int percent = buff.getData().get(1);
                defBuffed = defBuffed * (1.0 - percent / 100.0);
            }
        }

        double diff = damage - (int)defBuffed;
        if(damage>0.0 && diff<0.0) {
            damage = 0.0;
        } else {
            damage = diff;
        }
        
        int buffedDamage = calculateBuffWOCombo(attack.prop, damage);
        mhp.addDamage(buffedDamage);
        mhp.playDamagedAnimation(animationTime, callback);
        displayDamage(AttackValue.create((int)damage, attack.prop, attack.attackType), restraint);
        return damage;
    }
    
    public double dealtDamage(AttackValue attack, float animationTime, AnimationCallback callback) {
        double damage = attack.damage;
        if (damage <= 0.0) {
            return 0.0;
        }
        
        int restraint = 1; // up or down
        if(PadBoardAI.isRestraint(attack.prop, mhp.currentProp())){
            damage *= 2.0;
        } else if (PadBoardAI.beRestraint(attack.prop, mhp.currentProp())) {
            damage /= 2.0;
            restraint = -1;
        }

        // check there's a buff or something...
        for(BuffOnEnemy buff : mFixedList) {
       	 if(buff.isSkill(Type.SHIELD)) {
            	int prop = buff.getData().get(1);
                int percent = buff.getData().get(2);
                
                if(prop == -1 || attack.prop == prop) {
                	damage = damage * (100-percent) / 100.0;
                }
            }
       }
        for(BuffOnEnemy buff : mBuffList) {
        	 if(buff.isSkill(Type.SHIELD)) {
             	int prop = buff.getData().get(1);
                 int percent = buff.getData().get(2);
                 if(prop == -1 ||attack.prop == prop) {
                 	damage = damage * (100-percent) / 100.0;
                 }
             }
        }
        
        double defBuffed = def;
        for(BuffOnEnemy buff : mDebuffList) {
            if(buff.isSkill(Type.REDUCE_DEF)) {
                int percent = buff.getData().get(1);
                defBuffed = defBuffed * (1.0 - percent / 100.0);
            }
        }

        double diff = damage - (int)defBuffed;
        if(damage>0.0 && diff<0.0) {
            damage = 1.0;
        } else {
            damage = diff;
        }
        
        int buffedDamage = calculateBuffWOCombo(attack.prop, damage);
        mhp.addDamage(buffedDamage);
        mhp.playDamagedAnimation(animationTime, callback);
        displayDamage(AttackValue.create((int)damage, attack.prop, attack.attackType), restraint);
        return damage;
    }
    
    public double dealtDamageBySkill(MonsterInfo info, AttackValue attack, float animationTime, AnimationCallback callback) {
        if(dead()) {
            return 0.0;
        }
        
        double damage = attack.damage;
        if (damage <= 0.0) {
            return 0.0;
        }
        
        int killerCount = SimulatorConstants.getKillerCount(info, this);
        if(killerCount > 0) {
        	damage *= Math.pow(3.0, killerCount);
        }
        int subkillerCount = SimulatorConstants.getSubKillerCount(info, this);
        if(subkillerCount>0) {
            damage *= Math.pow(1.5, subkillerCount);
        }
        
        int restrint = 1;
        if(PadBoardAI.isRestraint(attack.prop, mhp.currentProp())){
            damage *= 2.0;
        } else if (PadBoardAI.beRestraint(attack.prop, mhp.currentProp())) {
            damage /= 2.0;
            restrint = -1;
        }
        
        // check there's a buff or something...
        for(BuffOnEnemy buff : mFixedList) {
       	 if(buff.isSkill(Type.SHIELD)) {
            	int prop = buff.getData().get(1);
                int percent = buff.getData().get(2);
                //Pair<Integer, Integer> props = info.getProps();
                if(prop == -1) { // ||props.first == prop
                	damage = damage * (100-percent) / 100.0;
                } else if(prop == attack.prop) {
                	damage = damage * (100-percent) / 100.0;
                }
            }
       }
        for(BuffOnEnemy buff : mBuffList) {
        	 if(buff.isSkill(Type.SHIELD)) {
             	int prop = buff.getData().get(1);
                 int percent = buff.getData().get(2);
                 //Pair<Integer, Integer> props = info.getProps();
                 if(prop == -1) { // ||(props.first == prop || props.second == prop)
                 	damage = damage * (100-percent) / 100.0;
                 } else if(prop == attack.prop) {
                 	damage = damage * (100-percent) / 100.0;
                 }
             }
        }
        float percent = 1f;
        for(BuffOnEnemy buff : mDebuffList) {
            if(buff.isSkill(Type.REDUCE_DEF)) {
                percent = (1f - buff.getData().get(1) / 100.0f);
            }
        }
        double defBuffed = def * percent * (1f - attack.guardBreak / 100.0f);

        double diff = damage - (int)defBuffed;
        if(damage>0.0 && diff<0.0) {
            damage = 1.0;
        } else {
            damage = diff;
        }

        Map<String, Boolean> voidSetting = new HashMap<String, Boolean>();
        voidSetting.put(Constants.SK_VOID, envRef.get().hasSkill(Constants.SK_VOID));
        voidSetting.put(Constants.SK_VOID_ATTR, envRef.get().hasSkill(Constants.SK_VOID_ATTR));

        int buffedDamage = calculateBuff(attack, damage, voidSetting);
        mhp.addDamage(buffedDamage);
        mhp.playDamagedAnimation(animationTime, callback);
        displayDamage(AttackValue.create((int)damage, attack.prop, attack.attackType), restrint);
        return damage;
    }

    private int calculateBuffWOComboDirect(int prop, double damage) {
        for(BuffOnEnemy buff : mBuffList) {
            List<Integer> data = buff.getData();
            if (buff.isSkill(Type.ABSORB_SHIELD)) {
                if(data.get(1) == prop) {
                    return -(int)damage;
                }
            }
            if (buff.isSkill(Type.DAMAGE_ABSORB_SHIELD)) {
                if (damage >= data.get(1)) {
                    return -(int)(damage);
                }
            }
            if (buff.isSkill(Type.DAMAGE_VOID_SHIELD)) {
                if (damage >= data.get(1)) {
                    return 0;
                }
            }
        }
        return (int)damage;
    }

    private int calculateBuffWOCombo(int prop, double damage) {
        for(BuffOnEnemy buff : mBuffList) {
            List<Integer> data = buff.getData();
            if (buff.isSkill(Type.ABSORB_SHIELD)) {
                if(data.get(1) == prop) {
                    return -(int)damage;
                }
            } 
            if (buff.isSkill(Type.SHIELD)) {
                if ( data.get(1) == prop ) {
                    int percent = data.get(2);
                    return (int)(damage * (100-percent) / 100.0);
                }
            }
            if (buff.isSkill(Type.DAMAGE_ABSORB_SHIELD)) {
                if (damage >= data.get(1)) {
                    return -(int)(damage);
                }
            }
            if (buff.isSkill(Type.DAMAGE_VOID_SHIELD)) {
                if (damage >= data.get(1)) {
                    return 0;
                }
            }
        }
        return (int)damage;
    }
    
    private int calculateBuff(AttackValue attack, double damage, Map<String, Boolean> settings) {
        int prop = attack.prop;
        for(BuffOnEnemy buff : mBuffList) {
            List<Integer> data = buff.getData();
            boolean skipAttrAbsorb = false;
            if(settings.containsKey(Constants.SK_VOID_ATTR)) {
                skipAttrAbsorb = settings.get(Constants.SK_VOID_ATTR);
            }
            if (!skipAttrAbsorb && buff.isSkill(Type.ABSORB_SHIELD)) {
                if(data.get(1) == prop) {
                    return -(int)damage;
                }
            } 
            if (buff.isSkill(Type.COMBO_SHIELD)) {
                int combos = envRef.get().getScene().getMatches().size();
                if(combos <= data.get(1)) {
                    return -(int)damage;
                }
            }
            if (buff.isSkill(Type.SHIELD)) {
                if ( data.get(1) == prop ) {
                    int percent = data.get(2);
                    return (int)(damage * (100-percent) / 100.0);
                }
            }
            boolean skipAbsorb = false;
            if(settings.containsKey(Constants.SK_VOID)) {
                skipAbsorb = settings.get(Constants.SK_VOID);
            }
            if (!skipAbsorb && buff.isSkill(Type.DAMAGE_ABSORB_SHIELD)) {
                if (damage >= data.get(1)) {
                    return -(int)(damage);
                }
            }

            if (buff.isSkill(Type.DAMAGE_VOID_SHIELD)) {
                if (attack.squareAttack == 0 && damage >= data.get(1)) {
                    return 0;
                }
            }
        }
        return (int)damage;
    }
    
    public void beforePlayerAttack() {
        for(BuffOnEnemy buff : mFixedList) {
            if(buff.isSkill(Type.KONJO)) {
                konjoHp = mhp.getHp();
                break;
            }
        }
    }
    
    public void afterPlayerAttack() {
        if (dead()) {
            return ;
        }
        countDownBuff();
        updateEnemyState();
    }
    
    public void afterPoisonAttack() {
        if (dead()) {
            return ;
        }

        updateEnemyState();
    }
    
    private void imback() {
    	isDead = false;
    	backThisTurn = true;
    	setTurnText(mAttackTurnsSaved);
    	mTurnText.setVisible(true);
        registerEntityModifier(new AlphaModifier(1.0f, 0.0f, 1.0f){
            @Override
            protected void onModifierFinished(IEntity pItem) {
            }
        });
    }
    
    public void updateEnemyState() {
        if(dead()) {
        	if(mhp.getHp()>0) {
        		imback();
        	}
            return ;
        }
    	if(backThisTurn) {
    		backThisTurn = false;
    	}
        //countDownBuff();
        mhp.checkProps();
        if(mhp.isDead()) { // isDead means die this turn... hp = 0
            boolean realDead = true;
            for(BuffOnEnemy buff : mFixedList) {
                if(buff.isSkill(Type.KONJO)) {
                    int percent = buff.getData().get(0);
                    double targetHp = mhp.getHpFull() / 100.0 * percent;
                    if (konjoHp >= targetHp) {
                        realDead = false;
                    }
                    break;
                }
            }
            if ( realDead ) {
                setDead();
            } else {
                mhp.setCurrentHp(1, null);
            }
        }
        if(!dead()) {
            BuffOnEnemy removeIt = null;
            for(BuffOnEnemy buff : mFixedList) {
                if(buff.isSkill(Type.KONJO)) {
                    int percent = buff.getData().get(0);
                    double targetHp = mhp.getHpFull() / 100.0 * percent;
                    double current = mhp.getHp();
                    if (current < targetHp) {
                    	buff.setVisible(false);
                    } else if ( current >= targetHp ) {
                    	buff.setVisible(true);
                    }
                    konjoHp = mhp.getHp();
                }

                if(buff.isSkill(Type.CHANGE_TURN)) {
                    int percent = buff.getData().get(0);
                    double targetHp = mhp.getHpFull() / 100.0 * percent;
                    double current = mhp.getHp();
                    if (current < targetHp) {
                        mAttackTurnsSaved = 1;
                        setTurnText(mAttackTurnsSaved);

                        buff.setVisible(false);
                        removeIt = buff;
                    }
                }
            }
            if (removeIt != null) {
                mFixedList.remove(removeIt);
            }
        }
    }
    
    public boolean isTarget() {
        if (dead() || diedThisTurn()) {
            return false;
        }
        return attackTarget;
    }
    
    public void setTarget(boolean target) {
        attackTarget = target;
        if(target) {
        	if(mTarget.hasParent()) {
        		detachChild(mTarget);
        	}
        	attachChild(mTarget);
        } else {
            detachChild(mTarget);
        }
    }

    public int currentProp() {
        return mhp.currentProp();
    }
    
    public int getHp() {
        return mhp.getHp();
    }
    
    public int getHpTotal() {
    	return mhp.getHpFull();
    }
    
    public float getHpPercent() {
    	return getHp() * 100.0f / getHpTotal();
    }
    
    public List<BuffOnEnemy> getDebuffList() {
    	return mDebuffList;
    }
    
    public List<BuffOnEnemy> getBuffList() {
    	return mBuffList;
    }
}
