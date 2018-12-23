package com.a30corner.combomaster.activity.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;

public class TrainingSettingsFragment extends CMBaseFragment {

	private ImageView[] mDropImage;
	private ImageView[] mDropImageCMode;
	private boolean[] mDrops;
	private boolean[] mDropsCMode;
	private TextView mTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SharedPreferenceUtil spUtil = getSharedPreference();
		View view = inflater.inflate(R.layout.training_fragment, null);
		mDrops = spUtil.getDrops();
		mDropsCMode = spUtil.getDropsInCalcMode();
		mDropImage = new ImageView[6];
		mDropImageCMode = new ImageView[6];
		int[] resDrop = { R.id.drop_type_1, R.id.drop_type_2, R.id.drop_type_3,
				R.id.drop_type_4, R.id.drop_type_5, R.id.drop_type_6 };
		View dropList = view.findViewById(R.id.drop_orbs);
		for (int i = 0; i < 6; ++i) {
			mDropImage[i] = (ImageView) dropList.findViewById(resDrop[i]);
			mDropImage[i].setOnClickListener(orbClickListener);
			if (!mDrops[i]) {
				mDropImage[i].setAlpha(.5f);
			}
		}
		View cdropList = view.findViewById(R.id.drop_orbs_in_calc_mode);
        for (int i = 0; i < 6; ++i) {
            mDropImageCMode[i] = (ImageView) cdropList.findViewById(resDrop[i]);
            mDropImageCMode[i].setOnClickListener(orbCModeClickListener);
            if (!mDropsCMode[i]) {
                mDropImageCMode[i].setAlpha(.5f);
            }
        }

        CheckBox shine = (CheckBox) view.findViewById(R.id.shine_effect);
        shine.setChecked(getSharedPreference().getBoolean(SharedPreferenceUtil.PREF_SHINE_EFFECT, false));
        shine.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getSharedPreference().setBoolean(SharedPreferenceUtil.PREF_SHINE_EFFECT, isChecked);
                ComboMasterApplication.getsInstance().putGaAction("Settings", "Shine:"+isChecked);
            }
        });
        
		mTextView = (TextView) view.findViewById(R.id.tv_avg_combo);
		updateComboText();

		Button reset = (Button) view.findViewById(R.id.btn_reset);
		reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(getActivity())
						.setTitle(R.string.title_confirm)
						.setMessage(R.string.question_reset_combo)
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										SharedPreferenceUtil sp = getSharedPreference();
										sp.resetCombo();
										updateComboText();
									}
								})
						.setNegativeButton(android.R.string.no,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();

			}
		});

		return view;
	}

	@Override
	public void onResume() {
		updateComboText();
		super.onResume();
	}

	private void updateComboText() {
		SharedPreferenceUtil spUtil = getSharedPreference();
		long combo = spUtil.getCombos();
		long count = spUtil.getPlayCount();
		double avg = .0;
		if (count > 0) {
			avg = combo / (double) count;
		}
		mTextView.setText(getString(R.string.avg_combo)
				+ String.format("%2.2f", avg));
	}

    private OnClickListener orbCModeClickListener = new OnClickListener() {

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

            mDropsCMode[id] = !mDropsCMode[id];
            int count = 0;
            for (int i = 0; i < 6; ++i) {
                if (mDropsCMode[i]) {
                    ++count;
                }
            }
            if (count < 2) {
                mDropsCMode[id] = !mDropsCMode[id];
                Toast.makeText(getActivity(), "at lease 2 prop of drops",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (!mDropsCMode[id]) {
                mDropImageCMode[id].setAlpha(.5f);
            } else {
                mDropImageCMode[id].setAlpha(1f);
            }

            getSharedPreference().setDropsInCalcMode(mDropsCMode);
        }

    };
	
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

			mDrops[id] = !mDrops[id];
			int count = 0;
			for (int i = 0; i < 6; ++i) {
				if (mDrops[i]) {
					++count;
				}
			}
			if (count < 2) {
				mDrops[id] = !mDrops[id];
				Toast.makeText(getActivity(), "at lease 2 prop of drops",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (!mDrops[id]) {
				mDropImage[id].setAlpha(.5f);
			} else {
				mDropImage[id].setAlpha(1f);
			}

			getSharedPreference().setDrops(mDrops);
		}

	};
}
