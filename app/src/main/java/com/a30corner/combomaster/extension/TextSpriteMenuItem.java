package com.a30corner.combomaster.extension;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

public class TextSpriteMenuItem extends SpriteMenuItem {

	private static final float HALF = 2f;
	private final Text mText;

	public TextSpriteMenuItem(int pID, ITextureRegion pTextureRegion,
			IFont font, String text,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pID, pTextureRegion, pVertexBufferObjectManager, text, font,
				new TextOptions(HorizontalAlign.CENTER));
	}

	private TextSpriteMenuItem(int pID, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, String text,
			IFont font, TextOptions options) {
		super(pID, pTextureRegion.getWidth(), pTextureRegion.getHeight(),
				pTextureRegion, pVertexBufferObjectManager);

		mText = new Text(0f, 0f, font, text, pVertexBufferObjectManager);
		mText.setTextOptions(options);

		final float hightTxt = mText.getHeight();
		final float hightBtn = pTextureRegion.getHeight();

		final float widthTxt = mText.getWidth();
		final float widtBtn = pTextureRegion.getWidth();

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
