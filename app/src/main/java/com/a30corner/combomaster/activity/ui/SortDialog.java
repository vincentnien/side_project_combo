package com.a30corner.combomaster.activity.ui;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.a30corner.combomaster.R;

public class SortDialog extends Dialog {

    public static final String PREF_SORT = "pref_sort";
    
    public interface ISortCallback {
        public void onResult(Map<String, String> data);
    }
    
    private int[] condition = {0,0,0,0,0};
    private int[] order = {1,1,1,1,1};
    private ISortCallback callback = null;
    
    private Spinner[] spinners = new Spinner[5];
    private Button[] switches = new Button[5];
    
    private String mSortPref = null;
    
    public SortDialog(Context context) {
        super(context);

    }

    public void setCallback(ISortCallback callback) {
        this.callback = callback;
    }
    
    public void setPreferenceFileName(String name) {
    	mSortPref = name;
    }
    
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        
        initViews();
    }
    
    private void initViews() {
        Context context = getContext();
        final int[] btnID = {R.id.btn_01,R.id.btn_02,R.id.btn_03,R.id.btn_04,R.id.btn_05};
        final int[] switchID = {R.id.switch_01,R.id.switch_02,R.id.switch_03,R.id.switch_04,R.id.switch_05};
        
        loadPreferences();
        
        for(int i=0; i<btnID.length; ++i) {
            Spinner spinner = (Spinner) findViewById(btnID[i]);
            final Button swit = (Button) findViewById(switchID[i]);
            
            spinners[i] = spinner;
            switches[i] = swit;
            
            final int index = i;
            
            spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                        int position, long id) {
                    condition[index] = position;
                    if (position==0) { // na selected
                        swit.setEnabled(false);
                    } else {
                        swit.setEnabled(true);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            
            swit.setOnClickListener(new View.OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    int val = (order[index]==0)? 1:0;
                    order[index] = val;
                    int ascId = (order[index]==1)? R.string.asc:R.string.desc;
                    swit.setText(getContext().getString(ascId));
                }
            });
            if ( condition[i] == 0 ) {
                swit.setEnabled(false);
            } else {
                spinner.setSelection(condition[i]);
                swit.setEnabled(true);
                int ascId = (order[i]==1)? R.string.asc:R.string.desc;
                swit.setText(context.getString(ascId));
            }
        }
        
        Button btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();

                savePreferences();
                
                if ( callback != null ) {
                    Map<String, String> data = new HashMap<String, String>();
                    
                    for(int i=0; i<5; ++i) {
                        data.put("priority"+i, String.valueOf(condition[i]));
                        data.put("order"+i, String.valueOf(order[i]));
                    }
                    
                    callback.onResult(data);
                }
            }
        });
        Button reset = (Button) findViewById(R.id.btn_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                for(int i=0; i<btnID.length; ++i) {
                    spinners[i].setSelection(0);
                }
            }
        });
    }
    
    private void savePreferences() {
    	String filename = mSortPref;
    	if ( TextUtils.isEmpty(filename) ) {
    		filename = PREF_SORT;
    	}
    	
        Context context = getContext();
        SharedPreferences pref = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        for(int i=0; i<5; ++i) {
            editor.putInt("priority"+i, condition[i]);
            editor.putInt("order"+i, order[i]);
        }
        editor.commit();
    }
    
    private void loadPreferences() {
    	String filename = mSortPref;
    	if ( TextUtils.isEmpty(filename) ) {
    		filename = PREF_SORT;
    	}
        Context context = getContext();
        SharedPreferences pref = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        for(int i=0; i<5; ++i) {
            int priority = pref.getInt("priority"+i, 0);
            int asc = pref.getInt("order"+i, 1);
            
            condition[i] = priority;
            order[i] = asc;
        }
    }
}
