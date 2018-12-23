package com.a30corner.combomaster.playground.entity;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import com.a30corner.combomaster.activity.GameActivity;
import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.effect.Shake;
import com.a30corner.combomaster.scene.PlaygroundGameScene;
import com.a30corner.combomaster.texturepacker.GameUIAssets;
import com.a30corner.combomaster.utils.Constants;

public class HPEntity extends Entity {

    private Sprite mHp;
    private Text mHpText;
    
    private int mCurrentHp;
    private int mTotalHp;
    
    private int mTotalDamage = 0;
    private Text mDamageText;
    private Text mRecoverText;
    
    public HPEntity(IEnvironment pad) {
        super(pad);
    }

    public void init(int totalHp) {
        ResourceManager res = ResourceManager.getInstance();
        VertexBufferObjectManager vbom = envRef.get().getScene().vbom;
        
        int hpY = Constants.OFFSET_Y - 32;
        create(0f, 0f, res.getTextureRegion(GameUIAssets.class, GameUIAssets.HEART_HP_ID), vbom);
        setPosition(0f, hpY);

        mHp = new Sprite(0f, 0f, res.getTextureRegion("hp.png"), vbom);
        mHp.setPosition(40f, 12);
        mHp.setScaleCenter(0f, 0f);
        mHp.setScale(480f, 1f);
        attachChild(mHp);

        // int hpTotal = mTeam.getHp();
        mHpText = new Text(0f, 0f, res.getFontStroke(), String.format("%d/%d", totalHp, totalHp), 15, vbom);
        mHpText.setScale(1.15f);
        mHpText.setPosition(
                GameActivity.SCREEN_WIDTH - mHpText.getWidth() - 28, hpY + 12);
        mHpText.setColor(Color.GREEN);
        mHpText.setZIndex(5);
        
        mCurrentHp = mTotalHp = totalHp;
        
        mDamageText = new Text(0f, 0f, res.getFontStroke(), "-0", 12, vbom);
        mDamageText.setColor(Color.RED);
        mDamageText.setVisible(false);
        mDamageText.setScale(1.3f);
        mDamageText.setZIndex(3);
        
        mRecoverText = new Text(0f, 0f, res.getFontStroke(), "+0", 12, vbom);
        mRecoverText.setColor(Color.GREEN);
        mRecoverText.setVisible(false);
        mRecoverText.setScale(1.3f);
        mRecoverText.setZIndex(3);
        
        //attachChild(mHpText);
        
        PlaygroundGameScene scene = envRef.get().getScene();
        scene.attachChild(mHpText);
        scene.attachChild(mDamageText);
        scene.attachChild(mRecoverText);
        
    }
    
    private void adjustHp() {
    	if (mCurrentHp>mTotalHp) {
    		mCurrentHp = mTotalHp;
    	} else if(mCurrentHp<0) {
    		mCurrentHp = 0;
    	}
    	
    }
    
    public void recovery(int recovery) {
    	int cx = GameActivity.SCREEN_WIDTH / 2;
    	if(recovery>0) {
    		mRecoverText.setText("+" + recovery);
    		mRecoverText.setColor(Color.GREEN);
    	} else {
    		mRecoverText.setText("" + recovery);
    		mRecoverText.setColor(Color.RED);
    	}
    	mRecoverText.setPosition(cx-mRecoverText.getWidth()/2, Constants.OFFSET_Y - 24);
    	mRecoverText.setAlpha(1.0f);
    	mRecoverText.setVisible(true);
    	
    	IEntityModifier modifier = new AlphaModifier(1.6f, 1.0f, 0.5f){
    		@Override
    		protected void onModifierFinished(IEntity pItem) {
    			pItem.setVisible(false);
    		}
    	};
    	modifier.setAutoUnregisterWhenFinished(true);
    	mRecoverText.registerEntityModifier(modifier);
    }
    
    public void addDamage(int damage) {
    	mTotalDamage += damage;
    	int cx = GameActivity.SCREEN_WIDTH / 2;
    	
    	mDamageText.setText("-" + mTotalDamage);
    	mDamageText.setPosition(cx-mDamageText.getWidth()/2, sprite.getY());
    	mDamageText.setVisible(true);
    }
    
    public void clearDamage() {
    	mTotalDamage = 0;
    	float fromY = mDamageText.getY();
    	mDamageText.registerEntityModifier(new MoveYModifier(1.0f, fromY, fromY - 80){
    		@Override
    		protected void onModifierFinished(IEntity pItem) {
    			pItem.setVisible(false);
    		}
    	});
    }
    
    public void setHp(int currentHp, int totalHp, boolean withAnimation) {
        mHpText.setText(String.format("%d/%d", currentHp, totalHp));
        int hpY = Constants.OFFSET_Y - 32;
        mHpText.setPosition(
                GameActivity.SCREEN_WIDTH - mHpText.getWidth() - 24, hpY + 6);

        float percent = currentHp/(float)totalHp;
        float scaleX = mHp.getScaleX();
        float targetX = 480f * percent;
        //mHp.setScale(480f*percent, 1f);
        
        if (scaleX > targetX || targetX <= 0f) {
            // take damage ... ?
        	if(withAnimation) {
        		Shake.apply(sprite, 1200f, 30, 8);
        		mHp.registerEntityModifier(new ScaleModifier(0.4f, scaleX, targetX, 1f, 1f));
        	} else {
        		//mHp.registerEntityModifier(new ScaleModifier(0.05f, scaleX, targetX, 1f, 1f));
        		mHp.setScaleX(targetX);
        	}
            
        } else {
            mHp.registerEntityModifier(new ScaleModifier(0.2f, scaleX, targetX, 1f, 1f));
        }
        
        if(percent>=1.0f) {
            mHpText.setColor(0.24f, 0.96f, 0.24f);
        } else if (percent >= 0.8f) {
            mHpText.setColor(0.24f, 0.82f, 0.94f);
        } else if (percent >= 0.5f) {
            mHpText.setColor(0.93f, 0.93f, 0.356f);
        } else if (percent >= 0.2f) {
            mHpText.setColor(0.94f, 0.68f, 0.32f);
        } else {
            mHpText.setColor(0.94f, 0.24f, 0.24f);
        }
        mCurrentHp = currentHp;
    }
    
    public void setHp(int currentHp, int totalHp) {
    	setHp(currentHp, totalHp, true);
    }
}
