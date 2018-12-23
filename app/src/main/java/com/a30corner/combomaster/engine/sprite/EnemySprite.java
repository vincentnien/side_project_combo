package com.a30corner.combomaster.engine.sprite;

import java.lang.ref.WeakReference;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import android.util.Pair;
import android.util.SparseArray;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.scene.BaseMenuScene;

public class EnemySprite {

	private Sprite mSprite;
	private SparseArray<Pair<Entity, Entity>> mBuffSprite;
	private WeakReference<BaseMenuScene> mScene;

	private Text mAttackTurns;
	private BasicHpSprite mHpSprite;

	public EnemySprite(BaseMenuScene scene) {
		mScene = new WeakReference<BaseMenuScene>(scene);
		mBuffSprite = new SparseArray<Pair<Entity, Entity>>(4);
	}

	public void addBuff(ActiveSkill skill, Text text, Sprite buff) {
		BaseMenuScene scene = mScene.get();

		removeBuff(skill);

		int size = mBuffSprite.size() + 1;
		float x = mSprite.getX() - 10f;
		float y = mSprite.getY() - (buff.getHeight() + 10f) * size;

		buff.setPosition(x, y);

		if (text != null) {
			text.setPosition(x + buff.getWidth() + 10f, y);
			scene.attachChild(text);
		}
		scene.attachChild(buff);

		int id = skill.getType().ordinal();
		mBuffSprite.put(id, new Pair<Entity, Entity>(text, buff));
	}

	public void updateBuffText(ActiveSkill skill, String updated) {
		int id = skill.getType().ordinal();
		Pair<Entity, Entity> pair = mBuffSprite.get(id, null);
		if (pair != null) {
			((Text) pair.first).setText(updated);
		}
	}

	public void removeBuff(ActiveSkill skill) {
		int key = skill.getType().ordinal();
		Pair<Entity, Entity> pair = mBuffSprite.get(key, null);
		remove(pair);
		mBuffSprite.remove(key);

		int size = mBuffSprite.size();
		for (int i = 1; i <= size; ++i) {
			Pair<Entity, Entity> update = mBuffSprite.valueAt(i - 1);
			Sprite s = (Sprite) update.second;
			float x = mSprite.getX() - 10f;
			float y = mSprite.getY() - (s.getHeight() + 10f) * i;
			update.second.setPosition(x, y);
			if (update.first != null) {
				update.first.setPosition(x + s.getWidth() + 10f, y);
			}
		}
	}

	public void dead() {
		mHpSprite.dead();
		mAttackTurns.setVisible(false);
		int size = mBuffSprite.size();
		for (int i = 0; i < size; ++i) {
			Pair<Entity, Entity> pair = mBuffSprite.valueAt(i);
			invisible(pair.first);
			invisible(pair.second);
		}
	}

	private void invisible(Entity entity) {
		if (entity != null) {
			entity.setVisible(false);
		}
	}

	public void dispose() {
		BaseMenuScene scene = mScene.get();
		scene.detachChild(mSprite);
		mSprite.dispose();

		int size = mBuffSprite.size();
		for (int i = 0; i < size; ++i) {
			Pair<Entity, Entity> pair = mBuffSprite.valueAt(i);
			remove(pair);
		}
		scene.detachChild(mAttackTurns);
	}

	private void remove(Pair<Entity, Entity> pair) {
		if (pair != null) {
			BaseMenuScene scene = mScene.get();
			Entity oldtext = pair.first;
			if (oldtext != null) {
				scene.detachChild(oldtext);
				oldtext.dispose();
			}

			Entity sprite = pair.second;
			if (sprite != null) {
				scene.detachChild(sprite);
				sprite.dispose();
			}
		}
	}

	public void registerEntityModifier(IEntityModifier modifier) {
		mSprite.registerEntityModifier(modifier);
		// mAttackTurns.registerEntityModifier(modifier);

	}

	public float getX() {
		return mSprite.getX();
	}

	public float getY() {
		return mSprite.getY();
	}

	public Sprite getSprite() {
		return mSprite;
	}

	public void init(Sprite sprite, Text attackText, BasicHpSprite hpSprite) {
		BaseMenuScene scene = mScene.get();
		mSprite = sprite;
		mAttackTurns = attackText;
		mHpSprite = hpSprite;

		scene.attachChild(mSprite);
		scene.attachChild(mAttackTurns);
	}

	public void setAttackTurns(int turns) {
		mAttackTurns.setText(String.valueOf(turns));
	}

	public void addDamage(int damage) {
		mHpSprite.addDamage(damage);
	}

	public void playDamagedAnimation(float animHpDec) {
		mHpSprite.playDamagedAnimation(animHpDec, null);
	}

	public void setPosition(float currentX, float currentY) {
		mSprite.setPosition(currentX, currentY);
	}

	public void attacked() {
		// if we got delay debuf, clear it
		int id = SkillType.ST_DELAY.ordinal();
		Pair<Entity, Entity> pair = mBuffSprite.get(id, null);
		if (pair != null) {
			mScene.get().detachChild(pair.second);
			pair.second.dispose();
			mBuffSprite.remove(id);
		}
	}

	public void registerTouchArea(BaseMenuScene scene) {
		scene.registerTouchArea(mSprite);
	}

}
