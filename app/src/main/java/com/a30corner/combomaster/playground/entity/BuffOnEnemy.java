package com.a30corner.combomaster.playground.entity;

import java.util.List;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.a30corner.combomaster.manager.ResourceManager;
import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.BuffOnEnemySkill.Type;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.texturepacker.SimulateAssets;

public abstract class BuffOnEnemy extends Entity {

    protected BuffOnEnemySkill skill;
    
    private int counter = -1; // INF
    private Text mText = null;
    
    private BuffOnEnemy(IEnvironment pad, BuffOnEnemySkill skill) {
        super(pad);
        setSkill(skill);
    }
    
    protected BuffOnEnemy(IEnvironment pad) {
        super(pad);
    }
    
    protected abstract ITextureRegion getTexture();
    protected IEntityModifier getModifier() {
        return null;
    }
    
    public boolean canOverride() {
    	return false;
    }
    
    protected void create() {
    	ITextureRegion region = getTexture();
    	VertexBufferObjectManager vbom = envRef.get().getScene().vbom;
    	create(0, 0, region, vbom);
    	
    	IEntityModifier modifier = getModifier();
    	if (modifier != null) {
    	    modifier.setAutoUnregisterWhenFinished(true);
    	    sprite.registerEntityModifier(modifier);
    	}
    	
    	int turn = skill.getData().get(0);
    	if (turn != -1 && skill.type != BuffOnEnemySkill.Type.DELAY && skill.type != BuffOnEnemySkill.Type.POISON) {
    		mText = new Text(0, 0, ResourceManager.getInstance().getFontStroke(), String.valueOf(turn), vbom);
    		attachChild(mText);
    		
    		if(skill.type == BuffOnEnemySkill.Type.COMBO_SHIELD) {
    			int id = SimulateAssets.S_NUMBER0_ID + skill.getData().get(1);
    			ITextureRegion combo = ResourceManager.getInstance().getTextureRegion(SimulateAssets.class, id);
    			float offsetx =(region.getWidth()-combo.getWidth())/2f;
    			float offsety =(region.getHeight()-combo.getHeight())/2f;
    			attach(offsetx, offsety, combo, vbom);//.setColor(Color.PINK);
    		} else if(skill.type == BuffOnEnemySkill.Type.SHIELD) {
    			float offset = 3;
    			int percent = skill.getData().get(2);
    			int[] value = {percent/10, percent%10};
    			for(int i=0; i<2; ++i) {
    				int id = SimulateAssets.S_NUMBER0_ID + value[i];
    				ITextureRegion number = ResourceManager.getInstance().getTextureRegion(SimulateAssets.class, id);
        			attach(offset, 5, number, vbom);
        			offset += number.getWidth()-2;
    			}
    		}
    	}
    	counter = turn;
    }
    
    @Override
    public void setPosition(float x, float y) {
    	super.setPosition(x, y);
    	if (mText != null) {
    		mText.setPosition(sprite.getWidth(), 0);
    	}
    }
    
    public void setSkill(BuffOnEnemySkill skill) {
        this.skill = skill;
    }
    
    public boolean isSkill(Type type) {
        return skill.type == type;
    }
    
    public List<Integer> getData() {
        return skill.getData();
    }
    
    public void countDown() {
    	if (counter>0) {
    		--counter;
    	}
    	if(counter>0 && mText != null) {
    		mText.setText(String.valueOf(counter));
    	}
    }
    
    public boolean endBuff() {
    	return counter == 0;
    }
}
