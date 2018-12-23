package com.a30corner.combomaster.activity.ui;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.pad.Match;
import com.a30corner.combomaster.pad.PadBoardAI.IMAGE_TYPE;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill;
import com.a30corner.combomaster.pad.monster.TeamInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill.AwokenSkill;
import com.a30corner.combomaster.utils.BitmapUtil;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;

public class ScoreDialog {

	private List<Match> mMatches;
	private List<Pair<Double, Double>> mDamageData;
	private double mRecovery;
	private double mPoison;

	// layout
	private List<TextView> mComboText = new ArrayList<TextView>();
	private TextView mTotalCombo;
	private TextView mTotalDamage;
	private List<Pair<TextView, TextView>> mMemberDamage = new ArrayList<Pair<TextView, TextView>>();
	private List<ImageView> mMemberImage = new ArrayList<ImageView>();

	private AlertDialog mDialog;
	private Context mContext;

	private View mComboLayout;
	private View mDamageLayout;
	private View mAdvancedLayout;

	private Button mComboButton;
	private Button mDamageButton;
	private Button mAdvancedButton;

	private Button mUpdateButton;
	private EditText mDefense;

	private List<ImageButton> mColorButton = new ArrayList<ImageButton>();
	private List<ImageButton> mShieldButton = new ArrayList<ImageButton>();
	private List<ImageButton> mAbsorbButton = new ArrayList<ImageButton>();
	private List<ImageButton> mTypeButton = new ArrayList<ImageButton>();

	private int mShieldPercent = 50;
	private boolean mShieldEnabled = false;
	private List<Boolean> mShieldSelection = new ArrayList<Boolean>(5);
	private List<Boolean> mAbsorbSelection = new ArrayList<Boolean>(5);
	private List<Boolean> mTypeSelection = new ArrayList<Boolean>(10);
	
	public ScoreDialog(Context context) {
		mContext = context;
	}

	public void show() {
		mDialog.show();
	}

	private enum TAB {
	    DAMAGE,
	    COMBO,
	    ADVANCED,
	}
	
	private void setCurrentTab(TAB tab) {
		if (tab == TAB.DAMAGE) {
			mComboLayout.setVisibility(View.GONE);
			mDamageLayout.setVisibility(View.VISIBLE);
			mAdvancedLayout.setVisibility(View.GONE);
			mDamageButton.setTextColor(android.graphics.Color.WHITE);
			mComboButton.setTextColor(android.graphics.Color.GRAY);
			mAdvancedButton.setTextColor(android.graphics.Color.GRAY);
		} else if ( tab == TAB.COMBO ){
			mDamageLayout.setVisibility(View.GONE);
			mComboLayout.setVisibility(View.VISIBLE);
			mAdvancedLayout.setVisibility(View.GONE);
			mComboButton.setTextColor(android.graphics.Color.WHITE);
			mDamageButton.setTextColor(android.graphics.Color.GRAY);
			mAdvancedButton.setTextColor(android.graphics.Color.GRAY);
		} else {
            mDamageLayout.setVisibility(View.GONE);
            mComboLayout.setVisibility(View.GONE);
            mAdvancedLayout.setVisibility(View.VISIBLE);
            mComboButton.setTextColor(android.graphics.Color.GRAY);
            mDamageButton.setTextColor(android.graphics.Color.GRAY);
            mAdvancedButton.setTextColor(android.graphics.Color.WHITE);		    
		}
	}

	public void initViews() {
		mDialog = new AlertDialog.Builder(mContext).create();
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.score_dialog, null);
		mDialog.setView(view);

		mAdvancedButton = (Button) view.findViewById(R.id.btnAdvanced);
		mDamageButton = (Button) view.findViewById(R.id.btnDamage);
		mComboButton = (Button) view.findViewById(R.id.btnCombo);
		mDamageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setCurrentTab(TAB.DAMAGE);
			}
		});
		mComboButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setCurrentTab(TAB.COMBO);
			}
		});
		mAdvancedButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setCurrentTab(TAB.ADVANCED);
            }
        });

		// init
		mDamageLayout = view.findViewById(R.id.damage_layout);
		mComboLayout = view.findViewById(R.id.combo_layout);
		mAdvancedLayout = view.findViewById(R.id.advanced_layout);

		final SharedPreferences prefs = mContext.getSharedPreferences(
				"comboMaster", Context.MODE_PRIVATE);
		String def = prefs.getString("defense", "0");
		mDefense = (EditText) mAdvancedLayout.findViewById(R.id.et_defence);
		mDefense.setText(def);

		mUpdateButton = (Button) mAdvancedLayout.findViewById(R.id.btn_update);
		mUpdateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor editor = prefs.edit();
				editor.putString("defense", mDefense.getText().toString());
				editor.apply();

				// do update
				setDamageData();
			}
		});

		// TODO: use sharedpref ?
		setCurrentTab(TAB.DAMAGE);

		initComboLayout(view);
		initDamageLayout(view);
		initAdvancedLayout(view);
		Button btn = (Button) view.findViewById(R.id.btn_ok);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
	}

	private void initComboLayout(View view) {
		final int[] id = { R.id.orb1_text, R.id.orb2_text, R.id.orb3_text,
				R.id.orb4_text, R.id.orb5_text, R.id.orb6_text, R.id.orb7_text,
				R.id.orb8_text };

		for (int i = 0; i < id.length; ++i) {
			TextView tv = (TextView) view.findViewById(id[i]);
			mComboText.add(tv);
		}

		mTotalCombo = (TextView) view.findViewById(R.id.total_combo);
	}
	
	private void initAdvancedLayout(View view) {
	    for(int i=0; i<6; ++i) {
	        mShieldSelection.add(false);
	        mAbsorbSelection.add(false);
	    }
	    for(int i=0; i<10; ++i) {
	    	mTypeSelection.add(false);
	    }
	    
	    Spinner spinner = (Spinner) view.findViewById(R.id.prop_shield);
	    spinner.setSelection(4);
	    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                mShieldPercent = Integer.parseInt(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
	    
	    ImageButton iBtn = (ImageButton) view.findViewById(R.id.btn_shield);
	    iBtn.setAlpha(0.3f);
	    iBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if(v.getAlpha()==1.0f) {
                    v.setAlpha(0.3f);
                    mShieldEnabled = false;
                } else {
                    v.setAlpha(1.0f);
                    mShieldEnabled = true;
                }
                setDamageData();
            }
        });
	    
	    final int[] shieldID = {R.id.btn_fire_shield,R.id.btn_water_shield,R.id.btn_wood_shield,
                R.id.btn_light_shield,R.id.btn_dark_shield};
        for (int i=0; i<5; ++i) {
            ImageButton btn = (ImageButton) view.findViewById(shieldID[i]);
            btn.setAlpha(0.3f);
            final int index = i;
            btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getAlpha() == 1.0f) {
                        v.setAlpha(0.3f);
                        mShieldSelection.set(index, false);
                    } else {
                        v.setAlpha(1.0f);
                        mShieldSelection.set(index, true);
                    }
                    setDamageData();
                }
            });

            mShieldButton.add(btn);
        }
        final int[] absorbID = {R.id.btn_fire_absorb,R.id.btn_water_absorb,R.id.btn_wood_absorb,
                R.id.btn_light_absorb,R.id.btn_dark_absorb};
        for (int i=0; i<5; ++i) {
            ImageButton btn = (ImageButton) view.findViewById(absorbID[i]);
            btn.setAlpha(0.3f);
            final int index = i;
            btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getAlpha() == 1.0f) {
                        v.setAlpha(0.3f);
                        mAbsorbSelection.set(index, false);
                    } else {
                        v.setAlpha(1.0f);
                        mAbsorbSelection.set(index, true);
                    }
                    setDamageData();
                }
            });

            mAbsorbButton.add(btn);
        }
        final int[] typeID = {R.id.btn_type_dragon, R.id.btn_type_devil,R.id.btn_type_god,R.id.btn_type_machine,
        		R.id.btn_type_balanced,R.id.btn_type_attacker,R.id.btn_type_healer,R.id.btn_type_physical,
        		R.id.btn_type_evo_matrial,R.id.btn_type_enhance_matrial};
        for(int i=0; i<typeID.length; ++i) {
        	ImageButton btn = (ImageButton) view.findViewById(typeID[i]);
            btn.setAlpha(0.3f);
            final int index = i;
            btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getAlpha() == 1.0f) {
                        v.setAlpha(0.3f);
                        mTypeSelection.set(index, false);
                    } else {
                        v.setAlpha(1.0f);
                        mTypeSelection.set(index, true);
                    }
                    setDamageData();
                }
            });

            mTypeButton.add(btn);
        }
	}

	private void initDamageLayout(View view) {
		final int[] layoutID = { R.id.leader1, R.id.member1, R.id.member2,
				R.id.member3, R.id.member4, R.id.leader2 };
		mTotalDamage = (TextView) view.findViewById(R.id.total_damage);
		for (int i = 0; i < 6; ++i) {
			View layout = view.findViewById(layoutID[i]);
			ImageView image = (ImageView) layout.findViewById(R.id.monster);
			TextView primary = (TextView) layout
					.findViewById(R.id.primary_prop);
			TextView secondary = (TextView) layout
					.findViewById(R.id.secondary_prop);

			mMemberImage.add(image);
			mMemberDamage.add(new Pair<TextView, TextView>(primary, secondary));
		}
		final int[] btnID = { R.id.btn_fire, R.id.btn_water, R.id.btn_wood,
				R.id.btn_light, R.id.btn_dark };
		for (int i = 0; i < 5; ++i) {
			ImageButton btn = (ImageButton) view.findViewById(btnID[i]);
			btn.setAlpha(0.3f);
			final int index = i;
			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onColorButtonClick(index);
					setDamageData();
				}
			});

			mColorButton.add(btn);
		}
		
	}
	
	private void onColorButtonClick(int index) {
		for (int i = 0; i < 5; ++i) {
			if (index != i) {
				mColorButton.get(i).setAlpha(0.3f);
			} else {
				ImageButton btn = mColorButton.get(i);
				if (btn.getAlpha() == 1.0f) {
					// set to no enemy color
					btn.setAlpha(0.3f);
				} else {
					btn.setAlpha(1f);
				}
			}
		}
	}
	
	private int getColorButtonSelection() {
	    for (int i = 0; i < 5; ++i) {
            if (mColorButton.get(i).getAlpha() == 1.0f) {
                return i;
            }
        }
	    return -1;
	}

	public void setData(List<? extends Match> matches, List<Pair<Double, Double>> damage,
			double recovery, double poison) {
		mMatches = Collections.unmodifiableList(matches);
		mDamageData = Collections.unmodifiableList(damage);
		mRecovery = recovery;
		mPoison = poison;
		setComboData();
		setDamageData();
	}

	private void setComboData() {
		final int[] id = { R.id.orb1_text, R.id.orb2_text, R.id.orb3_text,
				R.id.orb4_text, R.id.orb5_text, R.id.orb6_text, R.id.orb7_text,
				R.id.orb8_text };

		int[] imageId;
		SharedPreferenceUtil utils = SharedPreferenceUtil.getInstance(mContext);
		IMAGE_TYPE imagetype = (utils.getType() == 2) ? IMAGE_TYPE.TYPE_TOS
				: IMAGE_TYPE.TYPE_PAD;
		if (imagetype == IMAGE_TYPE.TYPE_PAD) {
			imageId = new int[] { R.drawable.dark, R.drawable.fire,
					R.drawable.heart, R.drawable.light, R.drawable.water,
					R.drawable.wood, R.drawable.poison, R.drawable.jammar };
		} else {
			imageId = new int[] { R.drawable.gem_dark, R.drawable.gem_fire,
					R.drawable.gem_heart, R.drawable.gem_light,
					R.drawable.gem_water, R.drawable.gem_wood,
					R.drawable.poison, R.drawable.jammar };
		}

		Resources res = mContext.getResources();
		StringBuilder[] sb = new StringBuilder[9];
		for (int i = 0; i < 9; ++i) {
			sb[i] = new StringBuilder();
		}
		int[] count = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		int total = 0;
		for (Match m : mMatches) {
			int type = m.type;
			if(type > 8) {
				++total;
				continue;
			}
			++count[type];
			sb[type].append(m.count);
			sb[type].append("x ");
		}

		for (int i = 0; i < id.length; ++i) {
			TextView tv = mComboText.get(i);
			mComboText.add(tv);
			tv.setCompoundDrawablesWithIntrinsicBounds(
					res.getDrawable(imageId[i]), null, null, null);
			if (count[i] == 0) {
				tv.setText(" 0c");
			} else {
				tv.setText(" " + count[i] + "c=" + sb[i].toString());
			}
			total += count[i];
		}

		mTotalCombo.setText(mContext.getString(R.string.total_combo, total));
	}

	private void setDamageData() {
		int toggle = getColorButtonSelection();
		
		int defense = 0;
		try {
			defense = Integer.parseInt(mDefense.getText().toString());
		} catch (Exception e) {
		}
		calculateDamage(toggle, defense);
	}
	
	private void calculateDamage(int enemyColorIndex, int defense) {
		// index => 0 fire, 1 water, 2 wood, 3 light 4 dark
		// translate to color prop id
		// color prop => 0 dark, 1 fire, 2 recovery, 3 light, 4 water, 5 wood
		int[] enemyType = { 1, 4, 5, 3, 0 };
		int enemy = 2;
		if (enemyColorIndex != -1) {
			enemy = enemyType[enemyColorIndex];
		}
		int[] atkUp = { 3, 5, -1, 0, 1, 4 };
		int[] atkDown = { -1, 4, -1, -1, 5, 1 };
		
		int[] colorIndex = {4,0,0,3,1,2};

		// final String[] orbType =
		// mContext.getResources().getStringArray(R.array.orb_type);

		ComboMasterApplication instance = ComboMasterApplication.getsInstance();
		TeamInfo team = instance.getTargetTeam();
		Resources res = mContext.getResources();

		double total = 0.0;
		double absorb = 0.0;
		for (int i = 0; i < 6; ++i) {
			ImageView image = mMemberImage.get(i);
			Pair<TextView, TextView> pair = mMemberDamage.get(i);
			TextView primary = pair.first;
			TextView secondary = pair.second;

			MonsterInfo info = team.getMember(i);
			if (info != null) {
				Pair<Double, Double> d = mDamageData.get(i);

				Pair<Integer, Integer> props = info.getProps();
				double primaryAtk = d.first;
				double secondaryAtk = d.second;
				int colorId = android.R.color.black;
				if (atkUp[props.first] == enemy) {
					primaryAtk = d.first * 2;
					colorId = R.color.value_up;
				} else if (atkDown[props.first] == enemy) {
					primaryAtk = d.first / 2;
					colorId = R.color.value_down;
				}
				
				// check enemy type
				AwokenSkill[] killer = {AwokenSkill.DRAGON_KILLER,
						AwokenSkill.DEVIL_KILLER, AwokenSkill.GOD_KILLER,
						AwokenSkill.MACHINE_KILLER, AwokenSkill.BALANCED_KILLER,
						AwokenSkill.ATTACKER_KILLER,AwokenSkill.HEALER_KILLER,
						AwokenSkill.PHYSICAL_KILLER,AwokenSkill.EVO_MATRIAL_KILLER,
						AwokenSkill.ENHANCE_MATRIAL_KILLER
				};
				MonsterSkill.MoneyAwokenSkill[] subkiller = {
						MonsterSkill.MoneyAwokenSkill.DRAGON_KILLER,
						MonsterSkill.MoneyAwokenSkill.DEVIL_KILLER,
						MonsterSkill.MoneyAwokenSkill.GOD_KILLER,
						MonsterSkill.MoneyAwokenSkill.MACHINE_KILLER,
						MonsterSkill.MoneyAwokenSkill.BALANCED_KILLER,
						MonsterSkill.MoneyAwokenSkill.ATTACKER_KILLER,
						MonsterSkill.MoneyAwokenSkill.HEALER_KILLER,
						MonsterSkill.MoneyAwokenSkill.PHYSICAL_KILLER,
						MonsterSkill.MoneyAwokenSkill.EVO_MATRIAL_KILLER,
						MonsterSkill.MoneyAwokenSkill.ENHANCE_MATRIAL_KILLER,
						MonsterSkill.MoneyAwokenSkill.KILLER_3,
				};

//		        final int[] typeID = {R.id.btn_type_dragon, R.id.btn_type_devil,R.id.btn_type_god,R.id.btn_type_machine,
//		        		R.id.btn_type_balanced,R.id.btn_type_attacker,R.id.btn_type_healer,R.id.btn_type_physical,
//		        		R.id.btn_type_evo_matrial,R.id.btn_type_enhance_matrial};
				
				for(int index=0; index<mTypeSelection.size(); ++index) {
					if(mTypeSelection.get(index)) {
						int cnt = info.getTargetAwokenCount(killer[index], instance.isCopMode());
						if(cnt > 0) {
							primaryAtk *= Math.pow(3.0, cnt);
							colorId = R.color.value_up;
						}
					}
				}
				for(int index=0; index<mTypeSelection.size(); ++index) {
					if(mTypeSelection.get(index)) {
						int cnt = info.getTargetMoneyAwokenCount(subkiller[index]);
						if(cnt>0) {
							primaryAtk *= Math.pow(1.5, cnt);
							colorId = R.color.value_up;
						}
					}
				}
				
				if (primaryAtk == 0) {
					// do nothing
				} else if (primaryAtk <= defense) {
					primaryAtk = 1.0;
				} else {
					primaryAtk -= defense;
				}
				
				// shield
				if(mShieldEnabled) {
				    primaryAtk = primaryAtk * (100-mShieldPercent) / 100.0;
				    colorId = R.color.value_down;
				}
                
                // set damage by half shield
                if ( mShieldSelection.get(colorIndex[props.first]) ) {
                    primaryAtk = primaryAtk / 2.0;

                    colorId = R.color.value_down;
                }

                
                if(primaryAtk != 0.0 && primaryAtk < 1.0) {
                    primaryAtk = 1.0;
                }

				if ( mAbsorbSelection.get(colorIndex[props.first]) ) {
				    primaryAtk = -primaryAtk;
				}

				
				primary.setTextColor(res.getColor(colorId));
				primary.setText(String.format("%.0f", primaryAtk));
				if (props.second != -1) {
					int scolorId = android.R.color.black;
					if (atkUp[props.second] == enemy) {
						secondaryAtk = d.second * 2;
						scolorId = R.color.value_up;
					} else if (atkDown[props.second] == enemy) {
						secondaryAtk = d.second / 2;
						scolorId = R.color.value_down;
					}
					
					for(int index=0; index<mTypeSelection.size(); ++index) {
						if(mTypeSelection.get(index)) {
							int cnt = info.getTargetAwokenCount(killer[index], instance.isCopMode());
							if(cnt > 0) {
								secondaryAtk *= Math.pow(3.0, cnt);
								scolorId = R.color.value_up;
							}
						}
					}
					for(int index=0; index<mTypeSelection.size(); ++index) {
						if(mTypeSelection.get(index)) {
							int cnt = info.getTargetMoneyAwokenCount(subkiller[index]);
							if(cnt>0) {
								secondaryAtk *= Math.pow(1.5, cnt);
								scolorId = R.color.value_up;
							}
						}
					}

					if (secondaryAtk == 0) {
						// do nothing
					} else if (secondaryAtk <= defense) {
						secondaryAtk = 1.0;
					} else {
						secondaryAtk -= defense;
					}
					
					if(mShieldEnabled) {
					    secondaryAtk = secondaryAtk * (100-mShieldPercent) / 100.0;
					    scolorId = R.color.value_down;
                    }
                    
                    // set damage by half shield
                    if (mShieldSelection.get(colorIndex[props.second])) {
                        secondaryAtk = secondaryAtk / 2.0;
                        scolorId = R.color.value_down;
                    }
                    if(secondaryAtk != 0.0 && secondaryAtk < 1.0) {
                        secondaryAtk = 1.0;
                    }
					
	                if ( mAbsorbSelection.get(colorIndex[props.second]) ) {
	                    secondaryAtk = -secondaryAtk;
	                }

	                
					secondary.setTextColor(res.getColor(scolorId));
					secondary.setText(String.format("%.0f", secondaryAtk));
				}
				

				// damage absorb
				if ( primaryAtk >= 0.0 ) {
				    total += Math.ceil(primaryAtk);
				} else {
				    absorb += Math.ceil(primaryAtk);
				}
				if ( secondaryAtk >= 0.0 ) {
				    total += Math.ceil(secondaryAtk);
				} else {
				    absorb += Math.ceil(secondaryAtk);
				}

				int no = info.getNo();
				Bitmap bitmap = instance.getBitmap(no);
				if (bitmap == null) {
					bitmap = BitmapUtil.getBitmap(mContext, no + "i.png");
				}
				if (bitmap != null) {
					image.setImageBitmap(bitmap);
				}
			} else {

			}
		}
		NumberFormat format = NumberFormat.getNumberInstance();
		String displayRecovery = null;
		if ( mPoison != 0 ) {
		    if ( mRecovery != 0 ) {
    		    displayRecovery = String.format("%s - %s = %s", 
    		            format.format(mRecovery),
    		            format.format(mPoison),
    		            format.format(mRecovery-mPoison));
		    } else {
		        displayRecovery = format.format(-mPoison);
		    }
		} else {
		    displayRecovery = format.format(mRecovery);
		}
		String displayDamage = null;
		if ( absorb != 0.0 ) {
		    displayDamage = String.format("%s - %s = %s", 
                    format.format(total),
                    format.format(-absorb),
                    format.format(total+absorb));
		} else {
		    displayDamage = format.format(total);
		}
		
		String strDamage = mContext.getString(R.string.str_damage,
		        displayDamage, displayRecovery);
		mTotalDamage.setText(strDamage);
	}

}
