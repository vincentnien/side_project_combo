package com.a30corner.combomaster.engine.sprite;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.pool.GenericPool;

import com.a30corner.combomaster.manager.ResourceManager;

public class SpritePool extends GenericPool<IEntity> {

    private ITextureRegion mTextureRegion;
    
    @Override
    protected IEntity onAllocatePoolItem() {
        Sprite s = new Sprite(0, 0, mTextureRegion, ResourceManager.getInstance().getVBOM());
        s.setBlendingEnabled(true);
        s.setZIndex(1);
        s.setVisible(true);
        return s;
    }

    public SpritePool(ITextureRegion texture) {
        mTextureRegion = texture;
    }
    
    @Override
    protected void onHandleRecycleItem(IEntity pItem) {
        pItem.setIgnoreUpdate(true);
        pItem.setVisible(false);
    }
    
    @Override
    protected void onHandleObtainItem(IEntity pItem) {
        pItem.reset();
    }
}
