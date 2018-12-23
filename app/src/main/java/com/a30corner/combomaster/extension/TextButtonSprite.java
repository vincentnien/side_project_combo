package com.a30corner.combomaster.extension;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

public class TextButtonSprite extends ButtonSprite {

	private static final float HALF = 2f;
	private final Text mText;

	public TextButtonSprite(float pX, float pY,
			ITextureRegion pNormalTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, String text,
			IFont font) {
		super(pX, pY, pNormalTextureRegion, pVertexBufferObjectManager);
		mText = new Text(0f, 0f, font, text, pVertexBufferObjectManager);
		mText.setTextOptions(new TextOptions(HorizontalAlign.CENTER));

		final float hightTxt = mText.getHeight();
		final float hightBtn = pNormalTextureRegion.getHeight();

		final float widthTxt = mText.getWidth();
		final float widtBtn = pNormalTextureRegion.getWidth();

		final float positionX = widtBtn / HALF - widthTxt / HALF;
		final float positionY = hightBtn / HALF - hightTxt / HALF;

		mText.setX(positionX);
		mText.setY(positionY);
		attachChild(mText);
	}

	@Override
	public void setBlendFunction(final int pBlendFunctionSource,
			final int pBlendFunctionDestination) {
		super.setBlendFunction(pBlendFunctionSource, pBlendFunctionDestination);
		mText.setBlendFunction(pBlendFunctionSource, pBlendFunctionDestination);
	}

	@Override
	public void registerEntityModifier(IEntityModifier pEntityModifier) {
		super.registerEntityModifier(pEntityModifier);
		mText.registerEntityModifier(pEntityModifier);
	}

	@Override
	public void setAlpha(final float pAlpha) {
		super.setAlpha(pAlpha);
		mText.setAlpha(pAlpha);
	}

	public void setColor(final float pRed, final float pGreen, final float pBlue) {
		mText.setColor(pRed, pGreen, pBlue);
	}

}
