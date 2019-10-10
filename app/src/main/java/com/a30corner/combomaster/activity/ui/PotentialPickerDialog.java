package com.a30corner.combomaster.activity.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.ui.DialogUtil.IPotentialCallback;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MoneyAwokenSkill;

public class PotentialPickerDialog extends Dialog {

	private int MAX_POTENTIAL = R.drawable.mawoken33-R.drawable.mawoken01 + 1;

	int counter = 0;
	private MoneyAwokenSkill[] mSelection = new MoneyAwokenSkill[6];
	private ImageButton[] mMAws = new ImageButton[6];
	private DialogUtil.IPotentialCallback callback = null;
	
	public PotentialPickerDialog(Context context) {
		super(context);
	}

	public void setSkills(MoneyAwokenSkill[] list) {
		for(int i=0; i<6; ++i) {
			if ( list[i] != null  && list[i] != MoneyAwokenSkill.SKILL_NONE ) {
				mSelection[i] = list[i];
			} else {
				mSelection[i] = null;
			}
		}
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		setTitle(R.string.title_potential_awoken);
		initViews();
	}
	
	private View.OnClickListener clickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int id = v.getId() - R.id.skill_01;
			if ( id >= 0 && id < MAX_POTENTIAL ) {
				for(int i=0; i<6; ++i) {
					if ( mSelection[i] == null ) {
						MoneyAwokenSkill skill = MoneyAwokenSkill.get(id+1);
						int add = (skill.ordinal()>=12)? 2:1;
						if(add+counter<=6) {
							counter += add;
							mSelection[i] = skill;
							mMAws[i].setImageResource(R.drawable.mawoken01 + id);
							break;
						}
					}
				}
			}
		}
	};
	
	private View.OnClickListener removeListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int id = v.getId() - R.id.maws_01;
			if ( id>= 0 && id < 6 ) {
				if ( mSelection[id] != null ) {
					int sub = (mSelection[id].ordinal()>=12)? 2:1;
					counter -= sub;
					mSelection[id] = null;
					mMAws[id].setImageResource(R.drawable.mawoken_bg);
				}
			}
		}
	};
	
	private void initViews() {
		counter = 0;
		for(int i=0; i<6; ++i) {
			ImageButton btn = (ImageButton) findViewById(R.id.maws_01+i);
			btn.setOnClickListener(removeListener);
			
			if ( mSelection[i] != null ) {
				int id = mSelection[i].ordinal();
				counter += (id>=12)? 2:1;
				btn.setImageResource(R.drawable.mawoken01+id-1);
			}
			
			mMAws[i] = btn;
		}
		
		for(int i=0; i<MAX_POTENTIAL; ++i) {
			ImageButton btn = (ImageButton) findViewById(R.id.skill_01+i);
			btn.setOnClickListener(clickListener);
		}
		
		Button ok = (Button) findViewById(R.id.btn_ok);
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( callback != null ) {
					callback.onResult(mSelection);
				}
				dismiss();
			}
		});
		Button cancel = (Button) findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( callback != null ) {
					callback.onCancel();
				}
				dismiss();
			}
		});
		
	}

	public void setCallback(IPotentialCallback callback2) {
		callback = callback2;
	}
}
