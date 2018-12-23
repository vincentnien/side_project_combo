package com.a30corner.combomaster.engine.sprite;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;

import android.util.Pair;

import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.scene.BaseMenuScene;
import com.a30corner.combomaster.texturepacker.SimulateAssets;

public class BasicHpSprite extends SpriteGroup {

	private int mHpTotal = 0;
	private int mHpCurrent = 0;
	public int mHpWidth;
	private Pair<Integer, Integer> mProps;

	private Sprite[] mPropSprite = new Sprite[2];
	private Sprite mHpHead;
	private Sprite mHpTail;
	private Sprite mHpBackground;
	private Sprite mHp1;
	private Sprite mHp2 = null;

	private boolean mSingleMode = true;
	private int mCurrentProps;

	private ConcurrentLinkedQueue<Integer> mDamageList = new ConcurrentLinkedQueue<Integer>();
	private WeakReference<Enemy> parentRef;

	public interface AnimationCallback {
	    void animationDone();
	}
	
	@Override
	public void attachChild() {
		super.attachChild();
		if(mPropSprite[1] != null) {
			mPropSprite[1].setVisible(false);
		}
	}
	
	public BasicHpSprite(BaseMenuScene scene, int width,
			Pair<Integer, Integer> pair, Enemy parent) {
		super(scene);
		mHpWidth = width;
		mProps = pair;
		mSingleMode = (pair.second == -1);
		parentRef = new WeakReference<Enemy>(parent);
		
		mCurrentProps = pair.first;
	}

	public void changeAttribute(int attribute) {
		if(mProps.first == attribute && mProps.second == -1) {
			return ;
		}
		
		BaseMenuScene scene = getScene();
		ResourceManager manager = ResourceManager.getInstance();
		
		
		detachChild(mHp1);
		detachChild(mPropSprite[0]);
		if(!mSingleMode) {
			detachChild(mHp2);
			detachChild(mPropSprite[1]);
		}
		
		
		mProps = new Pair<Integer, Integer>(attribute, -1);
		mSingleMode = true;
		mCurrentProps = attribute;
		
        String id1 = String.format("ehp_%d.png", mProps.first);
        int[] propId = {SimulateAssets.PROP0_ID,SimulateAssets.PROP1_ID,
        		0,SimulateAssets.PROP3_ID,SimulateAssets.PROP4_ID,
        		SimulateAssets.PROP5_ID};
        
        mHp1 = new Sprite(0f, 0f, manager.getTextureRegion(id1), scene.vbom);
        mPropSprite[0] = new Sprite(0f, 0f, manager.getTextureRegion(SimulateAssets.class, propId[mProps.first]), scene.vbom);
        
        setPosition(mHpHead.getX(), mHpHead.getY());
        
        addSprite(mPropSprite[0]);
		addSprite(mHp1);
		
		mPropSprite[0].setVisible(true);
		mHp1.setVisible(true);
		
		scene.attachChild(mPropSprite[0]);
		scene.attachChild(mHp1);
        
        updateHpNew();
	}
	
	public void onCreate(float x, float y) {
	    BaseMenuScene scene = getScene();
		ResourceManager manager = ResourceManager.getInstance();
		mHpHead = new Sprite(0f, 0f, manager.getTextureRegion(SimulateAssets.class, SimulateAssets.EHP_HEAD_ID),
				scene.vbom);
		mHpTail = new Sprite(0f, 0f, manager.getTextureRegion(SimulateAssets.class, SimulateAssets.EHP_TAIL_ID),
		        scene.vbom);
		mHpBackground = new Sprite(0f, 0f, manager.getTextureRegion("ehp.png"),
		        scene.vbom);

		addSprite(mHpHead);
		addSprite(mHpTail);
		addSprite(mHpBackground);
		
        String id1 = String.format("ehp_%d.png", mProps.first);
        int[] propId = {SimulateAssets.PROP0_ID,SimulateAssets.PROP1_ID,
        		0,SimulateAssets.PROP3_ID,SimulateAssets.PROP4_ID,
        		SimulateAssets.PROP5_ID};
        if (!mSingleMode) {
            String id2 = String.format("ehp_%d.png", mProps.second);
            mHp1 = new Sprite(0f, 0f, manager.getTextureRegion(id2), scene.vbom);
            mHp2 = new Sprite(0f, 0f, manager.getTextureRegion(id1), scene.vbom);
            mPropSprite[1] = new Sprite(0f, 0f, manager.getTextureRegion(SimulateAssets.class, propId[mProps.second]), scene.vbom);
            mPropSprite[0] = new Sprite(0f, 0f, manager.getTextureRegion(SimulateAssets.class, propId[mProps.first]), scene.vbom);
            addSprite(mPropSprite[1]);
            addSprite(mHp2);
            mPropSprite[1].setVisible(false);
        } else {
            mHp1 = new Sprite(0f, 0f, manager.getTextureRegion(id1), scene.vbom);
            mPropSprite[0] = new Sprite(0f, 0f, manager.getTextureRegion(SimulateAssets.class, propId[mProps.first]), scene.vbom);
        }
        addSprite(mPropSprite[0]);
		addSprite(mHp1);

        setPosition(x, y);
	}

	public void onDispose() {
		detachChild();
	}

	public void setPosition(float x, float y) {
		mHpHead.setPosition(x, y);
		mHpHead.setZIndex(2);
		float hpBgX = x + mHpHead.getWidth();
		mHpBackground.setPosition(hpBgX, y);
		mHpBackground.setScaleCenter(0f, 0f);
		mHpBackground.setScale(mHpWidth, 1f);
		mHpBackground.setZIndex(1);

		mPropSprite[0].setPosition(x-24, y-16);
		mPropSprite[0].setZIndex(4);
		if (mSingleMode) {
			mHp1.setPosition(hpBgX, y + 4);
			mHp1.setScaleCenter(0f, 0f);
			mHp1.setScale(mHpWidth, 1f);
			mHp1.setZIndex(3);
		} else {
			float halfW = mHpWidth / 2.0f;

			mPropSprite[1].setPosition(x-24, y-16);
			mPropSprite[1].setZIndex(4);
			
			mHp1.setPosition(hpBgX, y + 4);
			mHp1.setScaleCenter(0f, 0f);
			mHp1.setScale(halfW, 1f);
			mHp1.setZIndex(3);

			mHp2.setPosition(hpBgX + halfW, y + 4);
			mHp2.setScaleCenter(0f, 0f);
			mHp2.setScale(halfW, 1f);
			mHp2.setZIndex(4);
		}
		mHpTail.setPosition(hpBgX + mHpWidth-1, y);
		mHpTail.setZIndex(2);
		
		//getScene().sortChildren();
	}

	public void setHp(int hp, int currentHp) {
		mHpTotal = hp;
		mHpCurrent = hp;
	}

	public void setCurrentHp(int hp, ICastCallback callback) {
		if (hp > mHpTotal) {
			mHpCurrent = mHpTotal;
		} else {
			mHpCurrent = hp;
		}
		if(mHpCurrent < 0) {
			mHpCurrent = 0;
		}
		// TODO: show animation...
		if (callback != null) {
		    callback.onCastFinish(true);
		}
	}

	public void addDamage(int damage) {
		mDamageList.add(damage);
	}

	public void updateHpNew() {
		if (mHpCurrent > mHpTotal) {
			mHpCurrent = mHpTotal;
		} else if (mHpCurrent < 0) {
			mHpCurrent = 0;
		}
		float scaleTo = (float) mHpCurrent / mHpTotal;
		playSinglePropAnimation(0.1f, scaleTo, scaleTo, null);
	}
	
	public void playRecoverAnimation(float previous) {
		if (mHpCurrent > mHpTotal) {
			mHpCurrent = mHpTotal;
		} else if (mHpCurrent < 0) {
			mHpCurrent = 0;
		}
		float scaleTo = (float) mHpCurrent / mHpTotal;
		if(mSingleMode || previous < 0.5f) {
			playSinglePropAnimation(0.1f, previous, scaleTo, null);
		} else {
			playDualPropsAnimation(0.1f, previous, scaleTo, null);
		}
	}
	
	public void playDamagedAnimation(float animationTime, AnimationCallback callback) {
		if (mDamageList.size() <= 0) {
		    callback.animationDone();
			return;
		}
		int damage = mDamageList.poll();

		float previous = (float) mHpCurrent / mHpTotal;
		if(mProps.second == -1 || mProps.second == currentProp()) {
		    mSingleMode = true;
		}

		float percent = (float) damage / mHpTotal;
		float scaleTo = previous - percent;
		if (scaleTo < 0f) {
			scaleTo = 0f;
		} else if (scaleTo > 1f) {
			scaleTo = 1f;
		}
		mHpCurrent -= damage;
		if (mHpCurrent > mHpTotal) {
			mHpCurrent = mHpTotal;
		} else if (mHpCurrent < 0) {
			mHpCurrent = 0;
		}

		if (mProps.second != -1 && !mSingleMode) {
			playDualPropsAnimation(animationTime, previous, scaleTo, callback);
		} else {
			playSinglePropAnimation(animationTime, previous, scaleTo, callback);
		}
	}

	private void playDualPropsAnimation(final float animationTime,
			final float previous, final float scaleTo, final AnimationCallback callback) {
		if (previous >= 0.5f) {
			final float halfHpW = mHpWidth / 2f;
			if (scaleTo < 0.5f) {
				// need to deal with hp1&hp2
				// first scale hp2 to 0, then scale hp1 to remaining

				float firstTime = animationTime * (previous - 0.5f)
						/ (previous - scaleTo);
				final float remaining = animationTime - firstTime;
				mHp2.registerEntityModifier(new ScaleModifier(firstTime,
						(previous - 0.5f) * mHpWidth, 0f, 1f, 1f) {
					@Override
					protected void onModifierFinished(IEntity pItem) {

						mHp1.registerEntityModifier(new ScaleModifier(
								remaining, halfHpW, scaleTo * mHpWidth, 1f, 1f) {
							protected void onModifierFinished(IEntity pItem) {
								if(callback!=null) {
									callback.animationDone();
								}
//								if (mDamageList.size() > 0) {
//									playDamagedAnimation(animationTime, callback);
//								}
							};
						});
					}
				});
			} else {
				mHp2.registerEntityModifier(new ScaleModifier(animationTime,
						(previous - 0.5f) * mHpWidth, (scaleTo - 0.5f)
								* mHpWidth, 1f, 1f) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						if(callback != null) {
							callback.animationDone();
						}
//						if (mDamageList.size() > 0) {
//							playDamagedAnimation(animationTime, callback);
//						}
					}
				});
			}
		} else {
			mHp1.registerEntityModifier(new ScaleModifier(animationTime,
					previous * mHpWidth, scaleTo * mHpWidth, 1f, 1f) {
				@Override
				protected void onModifierFinished(IEntity pItem) {
					if(callback != null) {
						callback.animationDone();
					}
//					if (mDamageList.size() > 0) {
//						playDamagedAnimation(animationTime, callback);
//					}
				}
			});
		}

	}

	private void playSinglePropAnimation(final float animationTime,
			float previous, float scaleTo, final AnimationCallback callback) {
		mHp1.registerEntityModifier(new ScaleModifier(animationTime, previous * mHpWidth,
				scaleTo * mHpWidth, 1f, 1f) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				if(callback != null) {
					callback.animationDone();
				}
			}
		});
	}

	public void dead() {
		for (Entity sprite : mSpriteList) {
			sprite.setVisible(false);
		}
	}
	
	public boolean isDead() {
	    return mHpCurrent <= 0;
	}

	public float getWidth() {
		return mHpBackground.getWidthScaled() + mHpHead.getWidth()
				+ mHpTail.getWidth();
	}

	public void checkProps() {
	    if (mProps.second == -1) {
	        return ;
	    }
	    
	    float current = (float) mHpCurrent / mHpTotal;
	    if (current <= 0.5f) {
	        mCurrentProps = mProps.second;
	        mPropSprite[1].setVisible(true);
	        mPropSprite[0].setVisible(false);
	    }
	}

    public int currentProp() {
        return mCurrentProps;
    }

    public int getHpFull() {
    	return mHpTotal;
    }
    
    public int getHp() {
        return mHpCurrent;
    }
}
