package com.a30corner.combomaster.engine.sprite;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

public class TextSprite {

	private Sprite mSprite;
	private Text mText;
	
	public enum TextAlign {
	    RIGHT, LEFT
	}
	
	private TextAlign mAlign = TextAlign.RIGHT;

	public TextSprite(String text, IFont font, ITextureRegion texture,
			VertexBufferObjectManager vbom) {
		mSprite = new Sprite(0f, 0f, texture, vbom);
		mText = new Text(0f, 0f, font, text, vbom);
	}

	public void attach(Scene scene) {
		scene.attachChild(mSprite);
		scene.attachChild(mText);
	}

	public void detach(Scene scene) {
		scene.detachChild(mSprite);
		scene.detachChild(mText);
	}

	public void setAlignment(TextAlign align) {
	    mAlign = align;
	}
	
	public void setPosition(float x, float y) {
		mSprite.setPosition(x, y);
		
		float ox = x;
		float oy = y;
		switch(mAlign) {
		case RIGHT:
		    ox += mSprite.getWidthScaled() + 8;
		    oy -= mText.getHeightScaled() + mSprite.getHeightScaled() / 2;
		    break;
		case LEFT:
		    ox -= mSprite.getWidthScaled() - mText.getWidthScaled();
		    oy -= mText.getHeightScaled() + mSprite.getHeightScaled() / 2;
		    break;
		}
		mText.setPosition(ox, oy);
	}
	
	public void setZIndex(int zindex) {
	    mText.setZIndex(zindex);
	    mSprite.setZIndex(zindex);
	}
	
	public void setTextColor(Color color) {
	    mText.setColor(color);
	}

	public void setText(String text) {
		mText.setText(text);
	}
	
	public float getWidth() {
	    return mSprite.getWidthScaled() + mText.getWidthScaled();
	}
	
	public float getHeight() {
	    return mSprite.getHeightScaled() + mText.getHeightScaled();
	}
}
