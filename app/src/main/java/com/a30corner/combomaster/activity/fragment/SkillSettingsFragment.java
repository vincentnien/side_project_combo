package com.a30corner.combomaster.activity.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.a30corner.combomaster.R;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;
import com.a30corner.combomaster.utils.SharedPreferenceUtil.Skill;

public class SkillSettingsFragment extends CMBaseFragment {

	private Spinner[] mSkills;
	private List<SharedPreferenceUtil.Skill> mSkillList;
	private View[] mSubSkillView;

	private static class ViewTag {
		private View[] subview = new View[2];

		public ViewTag(View v1, View v2) {
			subview[0] = v1;
			subview[1] = v2;
		}

		public View getView1() {
			return subview[0];
		}

		public View getView2() {
			return subview[1];
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.skill_fragment, null);
		mSkills = new Spinner[6];
		mSubSkillView = new View[6];

		mSkillList = getSharedPreference().getSkillList();
		int[] resSkill = { R.id.skill1, R.id.skill2, R.id.skill3, R.id.skill4,
				R.id.skill5, R.id.skill6 };
		int[] resLayoutSkill = { R.id.layout_skill_1, R.id.layout_skill_2,
				R.id.layout_skill_3, R.id.layout_skill_4, R.id.layout_skill_5,
				R.id.layout_skill_6 };
		for (int i = 0; i < 6; ++i) {
			Spinner s = (Spinner) view.findViewById(resSkill[i]);
			s.setOnItemSelectedListener(mOnItemSelectedListener);

			mSubSkillView[i] = view.findViewById(resLayoutSkill[i]);
			View st = mSubSkillView[i].findViewById(R.id.skill_transform);
			View ss = mSubSkillView[i].findViewById(R.id.skill_stance);
			mSubSkillView[i].setTag(new ViewTag(st, ss));
			showSubView(i);

			mSkills[i] = s;
		}
		return view;
	}

	private void showSubView(int index) {
		Skill skill = mSkillList.get(index);
		ViewTag tag = (ViewTag) mSubSkillView[index].getTag();
		switch (skill.getType()) {
		case SKILL_TRANSFORM:
			tag.getView1().setVisibility(View.VISIBLE);
			tag.getView2().setVisibility(View.GONE);
			break;
		case SKILL_STANCE:
			tag.getView2().setVisibility(View.VISIBLE);
			tag.getView1().setVisibility(View.GONE);
			break;
		default:
			tag.getView1().setVisibility(View.GONE);
			tag.getView2().setVisibility(View.GONE);
		}
	}

	private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View view,
				int position, long arg3) {
			int index = 0;
			switch (view.getId()) {
			case R.id.skill1:
				break;
			case R.id.skill2:
				index = 1;
				break;
			case R.id.skill3:
				index = 2;
				break;
			case R.id.skill4:
				index = 3;
				break;
			case R.id.skill5:
				index = 4;
				break;
			case R.id.skill6:
				index = 5;
				break;
			}
			ViewTag tag = (ViewTag) mSubSkillView[index].getTag();
			switch (position) {
			case 0:
				tag.getView1().setVisibility(View.VISIBLE);
				tag.getView2().setVisibility(View.GONE);
				break;
			case 1:
				tag.getView2().setVisibility(View.VISIBLE);
				tag.getView1().setVisibility(View.GONE);
				break;
			default:
				tag.getView1().setVisibility(View.GONE);
				tag.getView2().setVisibility(View.GONE);
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
}
