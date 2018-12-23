package com.a30corner.combomaster.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.a30corner.combomaster.R;

public class AddMonsterActivity extends Activity {
	protected static final String MONSTER_ID = "id";
	protected static final String MONSTER_LV = "lv";
	protected static final String MONSTER_HP = "hp";
	protected static final String MONSTER_ATK = "atk";
	protected static final String MONSTER_REC = "rcv";
	protected static final String MONSTER_AWOKEN = "awoken";
	protected static final String MONSTER_M_AWOKEN = "m_awoken";
	protected static final String MONSTER_SKIILL2 = "skill2";

	private EditText mNo;
	private NumberPicker mLvPicker;
	private NumberPicker mHpPicker;
	private NumberPicker mAtkPicker;
	private NumberPicker mRecPicker;
	private NumberPicker mAwokenPicker;

	private Button mNext;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_monster);
		mNo = (EditText) findViewById(R.id.monster_no);
		mLvPicker = setNumberPicker(R.id.lv_picker, 99, 1, 99);
		mHpPicker = setNumberPicker(R.id.hp_picker, 99, 0, 99);
		mAtkPicker = setNumberPicker(R.id.atk_picker, 99, 0, 99);
		mRecPicker = setNumberPicker(R.id.rec_picker, 99, 0, 99);
		mAwokenPicker = setNumberPicker(R.id.awoken_picker, 10, 0, 10);

		mNext = (Button) findViewById(R.id.btn_next);
		mNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mNo.getText() != null) {
					Intent intent = new Intent(AddMonsterActivity.this,
							MonsterDetailActivity.class);
					intent.putExtra(MONSTER_ID,
							Integer.parseInt(mNo.getText().toString()));
					intent.putExtra(MONSTER_LV, mLvPicker.getValue());
					intent.putExtra(MONSTER_HP, mHpPicker.getValue());
					intent.putExtra(MONSTER_ATK, mAtkPicker.getValue());
					intent.putExtra(MONSTER_REC, mRecPicker.getValue());
					intent.putExtra(MONSTER_AWOKEN, mAwokenPicker.getValue());
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(AddMonsterActivity.this, "",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private NumberPicker setNumberPicker(int id, int max, int min, int value) {
		NumberPicker picker = (NumberPicker) findViewById(id);
		picker.setMaxValue(max);
		picker.setMinValue(min);
		picker.setValue(value);
		return picker;
	}
}
