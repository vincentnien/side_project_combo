package com.a30corner.combomaster.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.fragment.BasicSettingsFragment;
import com.a30corner.combomaster.activity.fragment.CMBaseFragment;
import com.a30corner.combomaster.activity.fragment.SkillListFragment;
import com.a30corner.combomaster.activity.fragment.TrainingSettingsFragment;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;
import com.viewpagerindicator.TabPageIndicator;

public class SettingsFragmentActivity extends FragmentActivity {

	// , R.string.str_monster
	private static final int[] FRAGMENT_TITLE = { R.string.str_basic,
			R.string.str_training, R.string.str_skills };
	private static final List<Class<? extends CMBaseFragment>> FRAGMENT;

	static {
		FRAGMENT = new ArrayList<Class<? extends CMBaseFragment>>();
		FRAGMENT.add(BasicSettingsFragment.class);
		FRAGMENT.add(TrainingSettingsFragment.class);
		FRAGMENT.add(SkillListFragment.class);
		// FRAGMENT.add(MonsterBoxFragment.class);
	}

	private ViewPager pager;
	private TabPageIndicator indicator;

	private int mCurrentIndex = -1;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings_tabs);

		final FragmentPagerAdapter adapter = new SettingsAdapter(
				SettingsFragmentActivity.this, getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				CMBaseFragment f = (CMBaseFragment) adapter.getItem(index);
				if (f != null) {
					f.resume();
				}
				if (mCurrentIndex != -1 && mCurrentIndex != index) {
					((CMBaseFragment) adapter.getItem(mCurrentIndex)).pause();
				}
				mCurrentIndex = index;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		ComboMasterApplication.getsInstance().onEnterScene("SettingsFragment");
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
	    super.onActivityResult(arg0, arg1, arg2);
	}
	
	@Override
	protected void onDestroy() {
		pager.setAdapter(null);
		SharedPreferenceUtil.getInstance(SettingsFragmentActivity.this).apply();
		super.onDestroy();
	}

	private static class SettingsAdapter extends FragmentPagerAdapter {

		private CMBaseFragment[] mFragList = new CMBaseFragment[FRAGMENT.size()];
		private FragmentActivity mContext;

		public SettingsAdapter(FragmentActivity context, FragmentManager manager) {
			super(manager);
			mContext = context;
		}

		@Override
		public Fragment getItem(int position) {

			try {
				// mFragList.set(position, );
				mFragList[position] = FRAGMENT.get(position % FRAGMENT.size())
						.newInstance();
				return mFragList[position];
			} catch (Exception e) {
			}
			return null;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mContext.getString(FRAGMENT_TITLE[position
					% FRAGMENT_TITLE.length]);
		}

		@Override
		public int getCount() {
			return FRAGMENT_TITLE.length;
		}

	}
}
