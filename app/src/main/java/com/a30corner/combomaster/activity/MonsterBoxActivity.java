package com.a30corner.combomaster.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.fragment.CMBaseFragment;
import com.a30corner.combomaster.activity.fragment.MonsterBoxFragment;
import com.a30corner.combomaster.activity.fragment.QuickSetupFragment;
import com.a30corner.combomaster.activity.fragment.TeamFragment;
import com.a30corner.combomaster.utils.LogUtil;
import com.viewpagerindicator.TabPageIndicator;

public class MonsterBoxActivity extends FragmentActivity {

	private static final int[] FRAGMENT_TITLE = { 
			R.string.str_quick_setup,
			R.string.str_team,
			R.string.str_monster_box};
	private static final List<Class<? extends CMBaseFragment>> FRAGMENT;

	static {
		FRAGMENT = new ArrayList<Class<? extends CMBaseFragment>>();
		FRAGMENT.add(QuickSetupFragment.class);
		FRAGMENT.add(TeamFragment.class);
		FRAGMENT.add(MonsterBoxFragment.class);
		
		// FRAGMENT.add(MonsterBoxFragment.class);
	}

	private ViewPager pager;
	private TabPageIndicator indicator;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings_tabs);

		FragmentPagerAdapter adapter = new SettingsAdapter(
				MonsterBoxActivity.this, getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		
		ComboMasterApplication.getsInstance().onEnterScene("MonsterBoxFragment");
	}

	@Override
	protected void onDestroy() {
		pager.setAdapter(null);
		// SharedPreferenceUtil.getInstance(SettingsFragmentActivity.this).apply();
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
				// mFragList[position].resume();
				LogUtil.e(mFragList[position].toString());
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
