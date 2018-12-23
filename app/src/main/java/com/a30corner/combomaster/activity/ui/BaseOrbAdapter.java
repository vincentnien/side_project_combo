package com.a30corner.combomaster.activity.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.a30corner.combomaster.R;

public class BaseOrbAdapter extends ArrayAdapter<Integer> {

	private int mResourceId;
	private LayoutInflater mInflater;

	private static final int[] ORB_LIST = new int[] { R.drawable.dark,
			R.drawable.fire, R.drawable.heart, R.drawable.light,
			R.drawable.water, R.drawable.wood };
	private static final Integer[] DATA = new Integer[] { 0, 1, 2, 3, 4, 5 };

	public BaseOrbAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId, DATA);
		mResourceId = textViewResourceId;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	private View getCustomView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(mResourceId, parent, false);
		}
		int orb = getItem(position);
		if (ORB_LIST.length > orb) {
			ImageView image = (ImageView) convertView
					.findViewById(android.R.id.icon);
			image.setImageResource(ORB_LIST[orb]);
		}
		return convertView;
	}
}
