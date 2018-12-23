package com.a30corner.combomaster.activity.ui;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.a30corner.combomaster.R;

public class SettingsDialog {
	
	private AlertDialog mDialog;
	private Context mContext;
	private ISettingsCallback mCallback = null;
	private Spinner mSpinner;
	private Spinner mSwap;
	private CheckBox[] mBindBox = new CheckBox[6];
	private String[] mData;
	private ImageButton[] mColorButton = new ImageButton[6];
	
	private CheckBox mHpCondition;
	private EditText mCurrentHp, mTotalHp;
	private SeekBar mHpSeekBar;
	private TextView mHpPercentage;
	private TextView mDropRateText;
	
	
	
	private View mHpLayout;
	private int mDropRate = 15;
	
	private static final int[] BTN_IDS = {R.id.btn_dark, R.id.btn_fire, 0, R.id.btn_light, R.id.btn_water, R.id.btn_wood};
	
	private Map<String, Object> mSettingsData = new HashMap<String, Object>();
	
	private boolean mIsCalculatorMode = false;
	
	private float mHpValue = 100f;
	
	public interface ISettingsCallback {
		public void onResult(Map<String, String> data);
		public void onCancel();
	}
	
	public void setCallback(ISettingsCallback callback) {
		mCallback = callback;
	}
	
	public SettingsDialog(Context context) {
		mContext = context;
	}
	
	public void show() {
		mDialog.show();
	}
	
	private void setHpConditionEnable(boolean enabled) {
		mHpSeekBar.setEnabled(enabled);
		mTotalHp.setEnabled(enabled);
		mCurrentHp.setEnabled(enabled);
		mHpLayout.setVisibility((enabled)? View.VISIBLE:View.GONE);
	}
	
	private void updateHpPercent(float percent) {
		mHpPercentage.setText(String.format("%.2f %%", percent));
        mHpValue = percent;
	}
	
	private void loadCalculatorMode(View view) {
		view.findViewById(R.id.ll_calculator_mode).setVisibility(View.VISIBLE);
		
		for(int i=0; i<6; ++i) {
		    mBindBox[i] = (CheckBox) view.findViewById(R.id.cb_m01+i);
		}
		
		mSwap = (Spinner) view.findViewById(R.id.swap_member);
		String member = mContext.getString(R.string.str_member);
		String[] memberArray = new String[5];
		memberArray[0] = mContext.getString(R.string.str_no_change);
		for(int i=1; i<5; ++i) {
			memberArray[i] = member + i;
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, 
				memberArray
		);
		mSwap.setAdapter(adapter);

		mHpCondition = (CheckBox) view.findViewById(R.id.cb_hp_condition);
		mHpCondition.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				setHpConditionEnable(isChecked);
			}
		});
		mHpLayout = view.findViewById(R.id.layout_hp_condition);
		
		mHpPercentage = (TextView) view.findViewById(R.id.tv_hp_percentage);
		mCurrentHp = (EditText) view.findViewById(R.id.et_current_hp);
		mTotalHp = (EditText) view.findViewById(R.id.et_total_hp);
		
		if ( mSettingsData.containsKey("hp") ) {
    		int hp = (Integer) mSettingsData.get("hp");
    		if ( hp != 0 ) {
    		    String textHp = String.valueOf(hp);
    		    mCurrentHp.setText(textHp);
    		    mTotalHp.setText(textHp);
    		}
		}
		
		mCurrentHp.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int current = Integer.parseInt(String.valueOf(s));
                    int total = Integer.parseInt(mTotalHp.getText().toString());
                    if ( total != 0 ) {
                        float percent = (float)current * 100f / total;
                        updateHpPercent(percent);
                    }
                } catch(Throwable t) {
                }
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
		mTotalHp.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int total = Integer.parseInt(String.valueOf(s));
                    int current = Integer.parseInt(mCurrentHp.getText().toString());
                    if ( total != 0 ) {
                        float percent = (float)current * 100f / total;
                        updateHpPercent(percent);
                    }
                } catch(Throwable t) {
                }
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
		
		mHpSeekBar = (SeekBar) view.findViewById(R.id.sb_hp_condition);
		mHpSeekBar.setProgress(100);
		mHpSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHpValue = seekBar.getProgress();
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                mHpPercentage.setText(progress + " %");
            }
        });
		setHpConditionEnable(false);
	}
	
	public void initViews() {
		initViews(false);
	}

	public void setData(Map<String, Object> data) {
	    mSettingsData.putAll(data);
	}
	
	private void loadDropRateLayout(View view) {
	    Button btnMinus = (Button)view.findViewById(R.id.btn_minus);
	    btnMinus.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if ( mDropRate > 5 ) {
                    mDropRate -= 5;
                }
                updateDropRateText();
            }
        });
        Button btnPlus = (Button)view.findViewById(R.id.btn_plus);
        btnPlus.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if ( mDropRate < 30 ) {
                    mDropRate += 5;
                }
                updateDropRateText();
            }
        });
        mDropRateText = (TextView) view.findViewById(R.id.drop_rate);

        for(int i=0; i<BTN_IDS.length; ++i) {
            if ( i == 2 ) {
                // no heart button
                mColorButton[2] = null;
                continue;
            }
            ImageButton imgBtn = (ImageButton) view.findViewById(BTN_IDS[i]);
            imgBtn.setAlpha(0.3f);
            imgBtn.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    toggleImageButton(v);
                }
            });
            mColorButton[i] = imgBtn;
        }
	}
	
	private void toggleImageButton(View view) {
	    int toggled = 0;
	    for(int i=0; i<BTN_IDS.length; ++i) {
	        if ( i!=2 && mColorButton[i].getAlpha() == 1.0f ) {
	            ++toggled;
	        }
	    }
	    for(int i=0; i<BTN_IDS.length; ++i) {
	        if ( view == mColorButton[i] ) {
	            if ( view.getAlpha() == 1.0f ) {
	                view.setAlpha(0.3f);
	            } else if ( toggled <= 1 ) {
	                view.setAlpha(1.0f);
	            }
	            break;
	        }
	    }
	}
	
	private void updateDropRateText() {
	    mDropRateText.setText(String.format("+%d%%", mDropRate));
	}

	public void initViews(boolean calculatorMode) {
		mIsCalculatorMode = calculatorMode;
		
		mData = mContext.getResources().getStringArray(R.array.tmp_time_list);
		
		mDialog = new AlertDialog.Builder(mContext).create();
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_temp_settings, null);
		mDialog.setTitle(R.string.app_name);
		mDialog.setView(view);
		
		mSpinner = (Spinner) view.findViewById(R.id.drop_time);
		//mSpinner.setMinimumWidth(180);
		loadDropRateLayout(view);
		
		if ( calculatorMode ) {
			loadCalculatorMode(view);
		}
		
		Button button = (Button) view.findViewById(R.id.btn_ok);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( mCallback != null ) {
				    Map<String, String> data = new HashMap<String, String>();
					float seconds = 0f;
					int index = mSpinner.getSelectedItemPosition();
					if ( index == 0 ) { // don't modify 
					} else if ( index == 1 ) { // no limits
						seconds = 600f;
					} else {
						try {
							seconds = Float.parseFloat(mData[index]);
						} catch(Throwable t) {
						}
					}
					data.put("seconds", String.valueOf(seconds));

					int dropCnt = 0;
					for(int i=0; i<BTN_IDS.length; ++i) {
					    if ( i != 2 && mColorButton[i].getAlpha() == 1.0f ) {
					        data.put("drop"+dropCnt, String.valueOf(i));
					        ++dropCnt;
					    }
					}
					if ( dropCnt > 0 ) {
					    data.put("droprate", String.valueOf(mDropRate));
					}
					
					if ( mIsCalculatorMode ) {
						StringBuilder sb = new StringBuilder();
						for(int i=0;i<6; ++i) {
						    String append = mBindBox[i].isChecked()? "1":"0";
						    sb.append(append);
						}
						data.put("bind", sb.toString());
						data.put("swap", String.valueOf(mSwap.getSelectedItemPosition()));
						data.put("hp", String.valueOf(mHpValue));
						data.put("isHpChecked", String.valueOf(mHpCondition.isChecked()));
					}
					mCallback.onResult(data);
				}
				mDialog.dismiss();
			}
		});
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( mCallback != null ) {
					mCallback.onCancel();
				}
				mDialog.dismiss();
			}
		});
	}
}
