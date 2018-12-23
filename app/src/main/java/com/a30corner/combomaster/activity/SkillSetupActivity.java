package com.a30corner.combomaster.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.ui.BaseOrbAdapter;
import com.a30corner.combomaster.pad.PadBoardAI;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;
import com.a30corner.combomaster.utils.SharedPreferenceUtil.Skill;
import com.a30corner.combomaster.utils.SharedPreferenceUtil.SkillType;

public class SkillSetupActivity extends Activity {

	private Spinner mSkillSpinner;
	private View mSkillEditor;
	private View mTransformLayout;
	private View mStanceLayout;

	private Spinner[] mStanceSpinner;

	private int mSkillIndex;

	private ImageView[] mOrbImage;
	private boolean[] mSelectedOrb = new boolean[6];

	private SharedPreferenceUtil mSp;
	private Skill mSkill;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent == null) {
			// error... finish now
			finish();
			return;
		}
		mSp = SharedPreferenceUtil.getInstance(SkillSetupActivity.this);
		mSkillIndex = getIntent().getIntExtra("index", 0);
		mSkill = mSp.getSkillList().get(mSkillIndex);

		setContentView(R.layout.skill_setup);

		mSkillEditor = findViewById(R.id.layout_skill_1);
		mTransformLayout = mSkillEditor.findViewById(R.id.skill_transform);
		mStanceLayout = mSkillEditor.findViewById(R.id.skill_stance);

		initSpinner();

		initOrbsTransformLayout();

		initStanceLayout();

		initFooter();
	}

	private boolean saveTransform() {
		long data = 0L;
		final long[] type = { PadBoardAI.TYPE_DARK, PadBoardAI.TYPE_FIRE,
				PadBoardAI.TYPE_HEART, PadBoardAI.TYPE_LIGHT,
				PadBoardAI.TYPE_WATER, PadBoardAI.TYPE_WOOD };
		for (int i = 0; i < 6; ++i) {
			if (mSelectedOrb[i]) {
				data |= type[i];
			}
		}
		if (data != 0L) {
			mSp.setSkill(mSkillIndex, SkillType.SKILL_TRANSFORM, data);
			return true;
		}
		return false;
	}

	private boolean saveStance() {
		final long[] type = { PadBoardAI.TYPE_DARK, PadBoardAI.TYPE_FIRE,
				PadBoardAI.TYPE_HEART, PadBoardAI.TYPE_LIGHT,
				PadBoardAI.TYPE_WATER, PadBoardAI.TYPE_WOOD };
		long[] data = new long[4];
		for (int i = 0; i < 4; ++i) {
			int pos = mStanceSpinner[i].getSelectedItemPosition();
			data[i] = type[pos];
		}
		if (data[0] == data[1]) {
			Toast.makeText(SkillSetupActivity.this, R.string.error_same_type,
					Toast.LENGTH_SHORT).show();
			return false;
		}
		mSp.setSkill(mSkillIndex, SkillType.SKILL_STANCE, data[0], data[1],
				data[2], data[3]);
		return true;
	}

	private boolean saveChangeTheWorld() {
		mSp.setSkill(mSkillIndex, SkillType.SKILL_THE_WORLD);
		return true;
	}

	private void initFooter() {
		Button ok = (Button) findViewById(R.id.btn_save);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO: save
				boolean saveOk = true;
				int skill = mSkillSpinner.getSelectedItemPosition();
				switch (skill) {
				case 0:
					saveOk = saveTransform();
					break;
				case 1:
					saveOk = saveStance();
					break;
				case 2:
					saveOk = saveChangeTheWorld();
					break;
				}
				if (saveOk) {
					mSp.apply();
					finish();
				}
			}
		});

		Button cancel = (Button) findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initStanceLayout() {
		mStanceSpinner = new Spinner[4];
		mStanceSpinner[0] = (Spinner) mStanceLayout.findViewById(R.id.from1);
		mStanceSpinner[1] = (Spinner) mStanceLayout.findViewById(R.id.to1);
		mStanceSpinner[2] = (Spinner) mStanceLayout.findViewById(R.id.from2);
		mStanceSpinner[3] = (Spinner) mStanceLayout.findViewById(R.id.to2);

		for (int i = 0; i < 4; ++i) {
			mStanceSpinner[i].setAdapter(new BaseOrbAdapter(
					SkillSetupActivity.this, R.layout.spinner_item));
		}

		List<Long> data = mSkill.getData();
		int size = data.size();
		for (int i = 0; i < size; ++i) {
			int type = PadBoardAI.getOrbType(data.get(i));
			mStanceSpinner[i].setSelection(type);
		}
	}

	private void initOrbsTransformLayout() {
		List<Long> skillData = mSkill.getData();
		if (skillData.size() > 0) {
			Long data = skillData.get(0);
			boolean[] selected = PadBoardAI.getSelectedOrbs(data);
			for (int i = 0; i < 6; ++i) {
				mSelectedOrb[i] = selected[i];
			}

			mOrbImage = new ImageView[6];
			int[] resDrop = { R.id.drop_type_1, R.id.drop_type_2,
					R.id.drop_type_3, R.id.drop_type_4, R.id.drop_type_5,
					R.id.drop_type_6 };
			for (int i = 0; i < 6; ++i) {
				mOrbImage[i] = (ImageView) mTransformLayout
						.findViewById(resDrop[i]);
				mOrbImage[i].setOnClickListener(orbClickListener);
				if (!mSelectedOrb[i]) {
					mOrbImage[i].setAlpha(.5f);
				} else {
					mOrbImage[i].setAlpha(1f);
				}
			}
		}
	}

	private OnClickListener orbClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = 0;
			switch (v.getId()) {
			case R.id.drop_type_1:
				id = 0;
				break;
			case R.id.drop_type_2:
				id = 1;
				break;
			case R.id.drop_type_3:
				id = 2;
				break;
			case R.id.drop_type_4:
				id = 3;
				break;
			case R.id.drop_type_5:
				id = 4;
				break;
			case R.id.drop_type_6:
				id = 5;
				break;
			}

			mSelectedOrb[id] = !mSelectedOrb[id];
			int count = 0;
			for (int i = 0; i < 6; ++i) {
				if (mSelectedOrb[i]) {
					++count;
				}
			}
			if (count < 1) {
				mSelectedOrb[id] = !mSelectedOrb[id];
				Toast.makeText(SkillSetupActivity.this, R.string.at_least_one,
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (!mSelectedOrb[id]) {
				mOrbImage[id].setAlpha(.5f);
			} else {
				mOrbImage[id].setAlpha(1f);
			}
		}
	};

	private void initSpinner() {
		mSkillSpinner = (Spinner) findViewById(R.id.spinner_skill);
		mSkillSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					mSkillEditor.setVisibility(View.VISIBLE);
					mTransformLayout.setVisibility(View.VISIBLE);
					mStanceLayout.setVisibility(View.GONE);
				} else if (position == 1) {
					mSkillEditor.setVisibility(View.VISIBLE);
					mTransformLayout.setVisibility(View.GONE);
					mStanceLayout.setVisibility(View.VISIBLE);
				} else {
					// change the world, do nothing
					mSkillEditor.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		mSkillSpinner.setSelection(mSkill.getType().getValue());
	}
}
