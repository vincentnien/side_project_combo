package com.a30corner.combomaster.activity.fragment;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.a30corner.combomaster.activity.MonsterBoxActivity;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;

public class CMBaseFragment extends Fragment {

	private Handler mNonUiHandler;
	private WeakReference<FragmentActivity> mActivity;

	@Override
	public void onAttach(Activity activity) {
		LogUtil.d("onAttach");
		super.onAttach(activity);
	}

	protected SharedPreferenceUtil getSharedPreference() {
		return SharedPreferenceUtil.getInstance(getActivity());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		HandlerThread ht = new HandlerThread("nonUi");
		ht.start();
		mNonUiHandler = new Handler(ht.getLooper());
	}

	public void runOnNonUiThread(Runnable runnable) {
		if (mNonUiHandler == null) {
			throw new RuntimeException("should call after onCreate()");
		}
		mNonUiHandler.post(runnable);
	}

	public void pause() {
		LogUtil.d("BaseFragment.pause()");
	}

	public void resume() {
		LogUtil.d("BaseFragment.resume()");
	}

	@Override
	public void onResume() {
		LogUtil.e("onResume:", toString());
		super.onResume();
	}

	@Override
	public void onPause() {
		LogUtil.e("onPause:", toString());
		super.onPause();
	}

	@Override
	public void onDestroy() {
		LogUtil.e("onDestroy");
		mNonUiHandler.getLooper().quit();
		super.onDestroy();
	}
}
