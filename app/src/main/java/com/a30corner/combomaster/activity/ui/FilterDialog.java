package com.a30corner.combomaster.activity.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.a30corner.combomaster.R;

public class FilterDialog extends Dialog {

    public static final String PREF_FILTER = "pref_filter";
    
	private List<ImageButton> mAttrButton1 = new ArrayList<ImageButton>();

	private List<ImageButton> mTypeButton1 = new ArrayList<ImageButton>();

	private Spinner mRareFrom, mRareEnd;
	private ToggleButton mIncludeAttr2;

	private List<Boolean> mSelectedAttr1;// = {false, false, false, false, false};

	private List<Boolean> mSelectedType1;// = {false, false, false, false, false, false, false, false, false, false, false};
	
	public static final String FILTER_RARE_FROM = "rare_from";
	public static final String FILTER_RARE_END = "rare_end";
	public static final String FILTER_INCLUDE_ATTR2 = "include_attr2";
	public static final String FILTER_ATTR = "attr";
	public static final String FILTER_TYPE = "prop";
	
	private String mPref = null;
	
	public interface IFilterCallback {
		public void onResult(Map<String, Object> filter);
	}
	
	private IFilterCallback mCallback;
	
	public FilterDialog(Context context) {
		super(context);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		
		initViews();
	}
	
	public void setPreferenceFileName(String name) {
		mPref = name;
	}
	
	public void setCallback(IFilterCallback callback) {
		mCallback = callback;
	}
	
	final int[] btnID1 = { R.id.btn_dark, R.id.btn_fire,
			R.id.btn_light, R.id.btn_water, R.id.btn_wood};

	final int[] btnType1 = {
			R.id.btn_m0, R.id.btn_m1, R.id.btn_m2, R.id.btn_m3, R.id.btn_m4,
			R.id.btn_m5, R.id.btn_m6, R.id.btn_m7, R.id.btn_m8, R.id.btn_m9, 
			0,
			R.id.btn_m11,
	};
	private void initViews() {

		
		mRareFrom = (Spinner)findViewById(R.id.rare_from);
		mRareEnd  = (Spinner)findViewById(R.id.rare_end);
		mRareFrom.setSelection(0);
		mRareEnd.setSelection(mRareEnd.getCount()-1);
		
		
		mIncludeAttr2 = (ToggleButton) findViewById(R.id.include_attr2);
		

		
		mSelectedAttr1 = new ArrayList<Boolean>();
		for(int i=0; i<btnID1.length; ++i) {
			mSelectedAttr1.add(false);
		}
		
		
		for(int i=0; i<btnID1.length; ++i) {
			View v = findViewById(btnID1[i]);
			mAttrButton1.add((ImageButton)v);
			final int index = i;
			v.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					boolean selected = !mSelectedAttr1.get(index);
					mSelectedAttr1.set(index, selected);
					updateColorButton(mAttrButton1);
				}
			});
		}

		mSelectedType1 = new ArrayList<Boolean>();
		for(int i=0; i<btnType1.length; ++i) {
			mSelectedType1.add(false);
		}
		for(int i=0; i<btnType1.length; ++i) {
			if ( btnType1[i] == 0 ) {
				mTypeButton1.add(new ImageButton(getContext()));
				continue;
			}
			View v = findViewById(btnType1[i]);
			mTypeButton1.add((ImageButton)v);
			final int index = i;
			v.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					boolean selected = !mSelectedType1.get(index);
					mSelectedType1.set(index, selected);
					updateTypeButton(mTypeButton1);
				}
			});
		}

		
		Button reset = (Button) findViewById(R.id.btn_reset);
		reset.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for(int i=0; i<mSelectedAttr1.size(); ++i) {
					mSelectedAttr1.set(i, false);
				}
				updateColorButton(mAttrButton1);
				for(int i=0; i<mSelectedType1.size(); ++i) {
					mSelectedType1.set(i, false);
				}
				updateTypeButton(mTypeButton1);

				mIncludeAttr2.setChecked(false);
				
				mRareFrom.setSelection(0);
				mRareEnd.setSelection(mRareEnd.getCount()-1);
			}
		});
		
		Button ok = (Button) findViewById(R.id.btn_ok);
		Button cancel = (Button) findViewById(R.id.btn_cancel);
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Map<String, Object> data = new HashMap<String, Object>();
				int from = mRareFrom.getSelectedItemPosition()+1;
				int end = mRareEnd.getSelectedItemPosition()+1;
				data.put(FILTER_RARE_FROM, from);
				data.put(FILTER_RARE_END, end);
				
				data.put(FILTER_INCLUDE_ATTR2, mIncludeAttr2.isChecked());
				
				data.put(FILTER_ATTR, mSelectedAttr1);
				data.put(FILTER_TYPE, mSelectedType1);
				
				String filename = mPref;
				if ( TextUtils.isEmpty(filename) ) {
					filename = PREF_FILTER;
				}
				
				SharedPreferences sp = getContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.putInt(FILTER_RARE_FROM, from);
				editor.putInt(FILTER_RARE_END, end);
				editor.putBoolean(FILTER_INCLUDE_ATTR2, mIncludeAttr2.isChecked());
				for(int i=0; i<mSelectedAttr1.size(); ++i) {
				    editor.putBoolean(FILTER_ATTR+i, mSelectedAttr1.get(i));
				}
				for(int i=0; i<mSelectedType1.size(); ++i) {
				    editor.putBoolean(FILTER_TYPE+i, mSelectedType1.get(i));
				}
				editor.apply();
				
				if ( mCallback != null ) {
					mCallback.onResult(data);
				}
				dismiss();
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
	
	@Override
	public void show() {
		String filename = mPref;
		if ( TextUtils.isEmpty(filename) ) {
			filename = PREF_FILTER;
		}
		
		SharedPreferences sp = getContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
		// has data in shared preferences
		if (sp.contains(FILTER_RARE_FROM)) {
	        int from = sp.getInt(FILTER_RARE_FROM, 1);
	        int end = sp.getInt(FILTER_RARE_END, 8);
	        mRareFrom.setSelection(from-1);
	        mRareEnd.setSelection(end-1);
	        
	        mIncludeAttr2.setChecked(sp.getBoolean(FILTER_INCLUDE_ATTR2, false));
	        
	        for(int i=0; i<btnID1.length; ++i) {
	            boolean checked = sp.getBoolean(FILTER_ATTR+i, false);
	            mSelectedAttr1.set(i, checked);
	        }
	        for(int i=0; i<btnType1.length; ++i) {
	            boolean checked = sp.getBoolean(FILTER_TYPE+i, false);
	            mSelectedType1.set(i, checked);
	        }
	        updateColorButton(mAttrButton1);
	        updateTypeButton(mTypeButton1);
		}
		
		super.show();
	}
	
	private void updateTypeButton(List<ImageButton> btnList) {
		int size = btnList.size();
		for(int i=0; i<size; ++i) {
			if ( mSelectedType1.get(i) ) {
				btnList.get(i).setAlpha(1.0f);
			} else {
				btnList.get(i).setAlpha(0.3f);
			}
		}
	}

	private void updateColorButton(List<ImageButton> btnList) {
		int size = btnList.size();
		for(int i=0; i<size; ++i) {
			if ( mSelectedAttr1.get(i)) {
				btnList.get(i).setAlpha(1.0f);
			} else {
				btnList.get(i).setAlpha(0.3f);
			}
		}
	}
}
