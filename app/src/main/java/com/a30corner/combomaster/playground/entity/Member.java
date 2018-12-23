package com.a30corner.combomaster.playground.entity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import android.widget.Toast;

import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill.AwokenSkill;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.ISkillCallback;
import com.a30corner.combomaster.playground.effect.Jump;
import com.a30corner.combomaster.texturepacker.SimulateAssets;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.RandomUtil;

public class Member extends Entity {

    public MonsterInfo info;
    private AtomicInteger cdCounter = new AtomicInteger(0);
    
    private int bindCounter = 0;
    private boolean bindThisTurn = false;
    private Sprite bindSprite = null;
    private Text bindText = null;

    public Member(IEnvironment pad, MonsterInfo info) {
        super(pad);
        setMonsterInfo(info);
    }

    public void setMonsterInfo(MonsterInfo info) {
        this.info = info;
        cdCounter.set(0);
    }
    
    public void init(float x, float y, ITextureRegion region, VertexBufferObjectManager vbom) {
        create(x, y, region, vbom, new OnAreaTouchCallback() {
            
            @Override
            public boolean onAreaTouched(TouchEvent touchEvent,
                    float pTouchAreaLocalX, float pTouchAreaLocalY) {
            	boolean locked = envRef.get().isSkillLocked();
                if (!locked && touchEvent.isActionUp()) {
                	if(info.getActiveSkill() == null ||
                			 info.getActiveSkill().get(0).getType() == SkillType.ST_NOT_SUPPORT) {
                		return true;
                	}
                    if (bindCounter == 0) {
                    	//cdCounter.get() >= info.getCd() &&
                    	int cd2;
                    	if(info.getActiveSkill2() == null ||
                    			info.getActiveSkill2().size() == 0) {
                    		cd2 = -1;
                    	} else {
                    		cd2 = info.getCd2();
                    	}
                    	//MonsterSkill.getViewableString(envRef.get().getScene().getActivity(), )
                        envRef.get().showSkillDialog(
                        		cdCounter.get(),
                        		info,
                        		info.getCd(),
                        		cd2,
                        
                        	new ISkillCallback() {

                            @Override
                            public void onFire(IEnvironment env) {
                                fireSkill(env);
                            }

                            @Override
                            public void onCancel(IEnvironment env) {
                            }

							@Override
							public void onFire(IEnvironment env, int which) {
								fireSkill(env, which);
							}
                            
                        });
                    }
                } else if(touchEvent.isActionDown()){
                	int cd = (info.getCd() - cdCounter.get());
                	int realcd = cd;
                	if ( cd < 0 ) {
                    	int cd2;
                    	if(info.getActiveSkill2() == null ||
                    			info.getActiveSkill2().size() == 0) {
                    		cd2 = 0;
                    	} else {
                    		cd2 = info.getCd2();
                    	}
                    	if( cd2 > 0 ) {
                    		realcd = cd2 + cd;
                    	}
                	}
                	Observable.just(realcd)
                	.observeOn(AndroidSchedulers.mainThread())
                	.subscribe(new Action1<Integer>() {
						
						@Override
						public void call(Integer cd) {							
							Toast.makeText(envRef.get().getScene().getActivity(), cd + " turn(s)", Toast.LENGTH_SHORT).show();
						}
					});
                }
                return true;
            }
        });
        sprite.setZIndex(2);
        sprite.setScaleCenter(0f, 0f);
        sprite.setScale(0.88f);
    }
    
    private void fireSkill(IEnvironment env, int skillNo) {
    	int totalCd = info.getCd();
    	int cd2 = info.getCd2();
    	if(cd2>0 && cd2 != 99) {
    		totalCd += cd2;
    	}
    	if(skillNo>=2 && cdCounter.get() >= totalCd) {
    		setCd(0);
            env.fireSkill(Member.this, skillNo);
    	} else {
    		fireSkill(env);
    	}
    }
    
    private void fireSkill(IEnvironment env) {
        if(cdCounter.get() >= info.getCd()) {
            setCd(0);
            env.fireSkill(Member.this);
        }
    }
    
    public void setCd(int cd) {
    	int prev = cdCounter.get();
    	boolean upPrev = prev>=info.getCd();
    	boolean doJump = false;
    	
    	if(cd <= 0) {
    		cdCounter.set(0);
    		if(upPrev) {
    			doJump = true;
    		}
    	} else {
	    	int skillMax = info.getCd();
	    	int cd2 = info.getCd2();
	    	if(cd2>0 && cd2 != 99) {
	    		skillMax += cd2;
	    	}
	    	if(cd>=skillMax) {
	    		cd = skillMax;
	    	}
	        cdCounter.set(cd);
	        
	        if(cd>=info.getCd() && !upPrev) {
	        	doJump = true;
	        } else if(upPrev && cd < info.getCd()) {
	        	doJump = true;
	        }
    	}

    	if(doJump) {
    		updatePosition();
    	}
    }
    
    private void updatePosition() {
    	envRef.get().getScene().engine.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
		    	int cd = cdCounter.get();
		    	float y = sprite.getY();
		    	if(cd < info.getCd()) {
		    		sprite.registerEntityModifier(new MoveYModifier(.3f, y, y+10));
		    	} else {
		    		sprite.registerEntityModifier(new MoveYModifier(.3f, y, y-10));
		    	}
			}
		});
    }
    
    public boolean isSkillUsable() {
    	return info.getCd() != -1 && (info.getCd() - cdCounter.get()) <= 0;
    }
    
    public boolean adjustCd(int value) {
    	if(info.getCd() == -1) {
    		return false;
    	}
        int prev = cdCounter.get();
        setCd(prev+value);
        
        return prev != cdCounter.get();
    }
    
    public List<ActiveSkill> getActiveSkill() {
        return info.getActiveSkill();
    }
    
    public List<ActiveSkill> getActiveSkill2() {
    	return info.getActiveSkill2();
    }
    
    public boolean isBinded() {
    	return bindCounter > 0;
    }
    
    public void bindRecover(int cd) {
    	if (bindCounter > 0) {
    		bindCounter = (bindCounter-cd>0)? bindCounter-cd:0;
    		if(bindCounter == 0) {
	    		detachChild(bindSprite);
	    		detachChild(bindText);
    		} else {
    			bindText.setText(String.valueOf(bindCounter));
    		}
    	}
    }
    
    public void countDownSkill() {
    	int petCd = info.getCd();
    	List<ActiveSkill> skill2 = info.getActiveSkill2();
    	if(skill2 != null && skill2.size()>0) {
    		int cd2 = info.getCd2();
    		if(cd2 > 0 && cd2 != 99) {
    			petCd += cd2;
    		}
    	}
    	if(cdCounter.get()<petCd) {
    		int cd = cdCounter.incrementAndGet();
    		if(cd == info.getCd()) { //cd == petCd || 
    			// TODO: move up
    			updatePosition();
    		}
    	}
    }
    
    public void countDown() {
    	if (bindCounter > 0 && !bindThisTurn) {
    		if (--bindCounter == 0) {
	    		detachChild(bindSprite);
	    		detachChild(bindText);
    		} else {
    			bindText.setText(String.valueOf(bindCounter));
    		}
    	}
    	bindThisTurn = false;
    }
    
    public boolean bind(int turn) {
    	IEnvironment env = envRef.get();
        if(!env.isNullAwokenStage()) {
            int count = info.getTargetAwokenCount(AwokenSkill.RESISTANCE_BIND, env.isMultiMode());
            if(RandomUtil.getLuck(50*count)) {
            	Jump.apply(sprite, 0.3f);
                return false;
            }
			count = info.getTargetAwokenCount(AwokenSkill.RESISTANCE_BIND_PLUS, env.isMultiMode());
			if(count>0) {
				Jump.apply(sprite, 0.3f);
				return false;
			}
        }
        
        if (bindSprite == null) {
        	bindSprite = new Sprite(0, 0, ResourceManager.getInstance().getTextureRegion(SimulateAssets.class, SimulateAssets.BIND_ID), env.getScene().vbom);
        	bindSprite.setPosition(
            		(sprite.getWidth()-bindSprite.getWidth())/2f,
            		(sprite.getHeight()-bindSprite.getHeight())/2f
        			);
        	bindText = new Text(0, 0, ResourceManager.getInstance().getFont(), "99", env.getScene().vbom);
        }
        if(bindCounter == 0) {
            attachChild(bindSprite);
            attachChild(bindText);
        }
        bindCounter += turn;
        bindText.setText(String.valueOf(bindCounter));
        bindText.setPosition(
        		sprite.getWidth()/2f - bindText.getWidth()/2f, 
        		sprite.getHeight()/2f - bindText.getHeight()/2f);
        bindThisTurn = true;

        return true;
    }
}
