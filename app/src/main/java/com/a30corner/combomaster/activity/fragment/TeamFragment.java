package com.a30corner.combomaster.activity.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.R.anim;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.MonsterDetailActivity;
import com.a30corner.combomaster.activity.TeamSelectorActivity;
import com.a30corner.combomaster.activity.ui.MultiSpinner;
import com.a30corner.combomaster.activity.ui.MultiSpinner.MultiSpinnerListener;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.AwokenSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MoneyAwokenSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.SinglePowerUp;
import com.a30corner.combomaster.pad.monster.TeamInfo;
import com.a30corner.combomaster.utils.Constants;
import com.a30corner.combomaster.utils.LogUtil;

public class TeamFragment extends CMBaseFragment {

	private MultiSpinner mPowerUpSpinner;
	private Spinner mTeamSpinner;
	private Button mEdit;
	private List<ImageView> mMember = new ArrayList<ImageView>();
	private List<TextView> m297 = new ArrayList<TextView>();
	private List<TextView> mPower = new ArrayList<TextView>();
	private List<TextView> mAwokenCnt = new ArrayList<TextView>();
	private List<ImageView> mAwokenImage = new ArrayList<ImageView>();
	private List<TextView> mPotentialCnt = new ArrayList<TextView>();
	private List<ImageView> mPotentialImage = new ArrayList<ImageView>();
	private List<ImageView> mAssits = new ArrayList<ImageView>();
	private List<View> mAwokenLine = new ArrayList<View>();
	private List<View> mPotentialLine = new ArrayList<View>();
	private TextView mTvHP;
	private int mCurrentTeam = 0;

	private Spinner singleUp;
	private TextView mLeaderSkill;
	private TextView mFriendLSkill;

	private TeamInfo mTeam;

	private SparseArray<Bitmap> mCache = new SparseArray<Bitmap>();
	private ArrayAdapter<String> mAdapter;

	public static int TEAM_SIZE = 50;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final int[] IMAGE_ID = { R.id.leader1, R.id.member1, R.id.member2,
				R.id.member3, R.id.member4, R.id.leader2 };
		final int[] TEXT_ID = { R.id.leader1_plus, R.id.member1_plus, R.id.member2_plus,
				R.id.member3_plus, R.id.member4_plus, R.id.leader2_plus };
		final int[] TV_POWERID = { R.id.tv_dark, R.id.tv_fire,
				R.id.tv_recovery, R.id.tv_light, R.id.tv_water, R.id.tv_wood };
		final int[] AWOKEN_LINE = { R.id.aws_line_01, R.id.aws_line_02,
				R.id.aws_line_03, R.id.aws_line_04, R.id.aws_line_05, R.id.aws_line_06 };
		final int[] POTENTIAL_LINE = {R.id.potential_line_01, R.id.potential_line_02};

		View view = inflater.inflate(R.layout.team_setup, null);
		mTeamSpinner = view.findViewById(R.id.spinner_team);
		mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.cm_spinner);
		
		mTeamSpinner.setAdapter(mAdapter);
		mCurrentTeam = ComboMasterApplication.getsInstance().getTargetNo();
		mTeamSpinner.setSelection(mCurrentTeam);
		mTeamSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int index, long arg3) {
				if (mCurrentTeam != index) {
					mCurrentTeam = index;
					loadTeamInfo();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		mPowerUpSpinner = view.findViewById(R.id.sp_power_up);
		String[] strList = getResources().getStringArray(R.array.stage_power_up);
		mPowerUpSpinner.setItems(Arrays.asList(strList).subList(1, strList.length), strList[0], new MultiSpinnerListener() {
			
			@Override
			public void onItemsSelected(boolean[] selected) {
				ComboMasterApplication.getsInstance().setPowerUp(selected);
				loadTeamInfo();
			}
		});
		
		singleUp = view.findViewById(R.id.single_up);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.single_power_up, 
				android.R.id.text1, new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8","9","10","11","12","13"}) {
			@Override
			public View getView(int position, View convertView,
					ViewGroup parent) {
				if(convertView == null) {
					convertView = LayoutInflater.from(getActivity()).inflate(R.layout.single_power_up, null);
				}
				bindView(position, convertView);
				return convertView;
			}
			
			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				return getView(position, convertView, parent);
			}
			
			private void bindView(int position, View convertView) {
				int drawable = R.drawable.single_00 + position;
				ImageView image = convertView.findViewById(R.id.power_up);
				image.setImageResource(drawable);
			}
		};
		singleUp.setAdapter(adapter);
		singleUp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				SharedPreferences pref = getActivity().getSharedPreferences("singleUp", Context.MODE_PRIVATE);
				pref.edit().putInt("item"+mCurrentTeam, position).apply();
				
				updateTeam();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		
		mEdit = view.findViewById(R.id.btn_edit_team);
		mEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						TeamSelectorActivity.class);
				intent.putExtra("teamID", mCurrentTeam);
				startActivity(intent);
			}
		});
		for (int i = 0; i < IMAGE_ID.length; ++i) {
			ImageView imageView = view.findViewById(IMAGE_ID[i]);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
							TeamSelectorActivity.class);
					intent.putExtra("teamID", mCurrentTeam);
					startActivity(intent);
				}
			});
			mMember.add(imageView);
		}
		for(int i=0; i<TEXT_ID.length; ++i) {
			TextView tv = view.findViewById(TEXT_ID[i]);
			m297.add(tv);
		}
		for(int i=0; i<6; ++i) {
			mAssits.add((ImageView) view.findViewById(R.id.assit1+i));
		}
		
		for (int i = 0; i < TV_POWERID.length; ++i) {
			TextView tv = view.findViewById(TV_POWERID[i]);
			mPower.add(tv);
		}
		mTvHP = view.findViewById(R.id.tv_hp);

		mLeaderSkill = view.findViewById(R.id.leader_skill);
		mFriendLSkill = view.findViewById(R.id.friend_leader_skill);

		Button prev = view.findViewById(R.id.prev);
		prev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int no = mCurrentTeam - 1;
				if (no < 0) {
					no = TEAM_SIZE - 1;
				}
				mTeamSpinner.setSelection(no);
			}
		});
		Button next = view.findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int no = mCurrentTeam + 1;
				if (no >= TEAM_SIZE) {
					no = 0;
				}
				mTeamSpinner.setSelection(no);
			}
		});

		for (int i = 0; i < AWOKEN_LINE.length; ++i) {
			View line = view.findViewById(AWOKEN_LINE[i]);
			mAwokenLine.add(line);
			for (int j = 0; j < 7; ++j) {
				View awoken = line.findViewById(R.id.awoken_01 + j);
				mAwokenCnt.add((TextView) awoken
						.findViewById(R.id.awoken_count));
				mAwokenImage.add((ImageView) awoken
						.findViewById(R.id.awoken_image));
			}
		}
		
		for(int i=0; i<2; ++i) {
			View line = view.findViewById(POTENTIAL_LINE[i]);
			mPotentialLine.add(line);
			for(int j=0; j<7; ++j) {
				View awoken = line.findViewById(R.id.awoken_01 + j);
				if ( j >= 5 ) {
					awoken.setVisibility(View.GONE);
					continue;
				}
				mPotentialCnt.add((TextView) awoken
						.findViewById(R.id.awoken_count));
				mPotentialImage.add((ImageView) awoken
						.findViewById(R.id.awoken_image));
			}
		}
		

		return view;
	}

	@Override
	public void onResume() {
		updateTeamName();
		loadTeamInfo();
		super.onResume();
	}

	private void updateTeamName() {
		// do io access in ui thread is not a good behavior ...
		// but ... just do it.. :-P
		SharedPreferences sp = getActivity().getSharedPreferences("team",
				Context.MODE_PRIVATE);
		String[] teams = new String[TEAM_SIZE];
		for (int i = 0; i < TEAM_SIZE; ++i) {
			String name = sp.getString("Team" + i + "_name", "Team " + (i + 1));
			teams[i] = name;
		}
		mAdapter.clear();
		mAdapter.addAll(teams);
	}

	@Override
	public void onPause() {
		ComboMasterApplication.getsInstance().setTargetTeam(mCurrentTeam);
		super.onPause();
	}

	@Override
	public void onDestroy() {
		mCache.clear();
		super.onDestroy();
	}

	private void loadTeamInfo() {
		LogUtil.d("load Team info");
		try {
			mTeam = ComboMasterApplication.getsInstance().getTeam(mCurrentTeam, Constants.MODE_NORMAL);
			updateTeam();
		} catch (Throwable t) {
			LogUtil.e(t.toString());
		}
	}

	private void updateTeam() {
		Context ctx = getActivity();
		int[] power = { 0, 0, 0, 0, 0, 0 };
		int hp = 0;
		int hpEnhanced = mTeam.getHp();
		int rcvEnhanced = mTeam.getRecovery();
		ComboMasterApplication instance = ComboMasterApplication.getsInstance();
		Map<AwokenSkill, Integer> awokenMaps = new HashMap<MonsterSkill.AwokenSkill, Integer>();
//		Map<MoneyAwokenSkill, Integer> potentialMaps = new HashMap<MonsterSkill.MoneyAwokenSkill, Integer>();
		int[] potentialCounter = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		for (int i = 0; i < 6; ++i) {
			MonsterInfo info = mTeam.getMember(i);
			final int index = mTeam.getMemberIndex(i);
			if (info != null) {
				int no = info.getNo();
				Bitmap cache = instance.getBitmap(no);
				ImageView imageView = mMember.get(i);
				if (cache == null) {
					Bitmap bitmap = mCache.get(no, null);
					if (bitmap == null) {
						File file = new File(ctx.getFilesDir(), no + "i.png");
						if (file.exists()) {
							bitmap = BitmapFactory.decodeFile(file
									.getAbsolutePath());
							mCache.append(no, bitmap);
							imageView.setImageBitmap(bitmap);
						}
					} else {
					    imageView.setImageBitmap(bitmap);
					}
				} else {
				    imageView.setImageBitmap(cache);
				}
				imageView.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						Intent intent = new Intent(getActivity(), MonsterDetailActivity.class);
						intent.putExtra("index", index);
						startActivity(intent);
						return false;
					}
				});
				ImageView img = mAssits.get(i);
				if(info.getAssistantNo() != 0) {
					File file = new File(ctx.getFilesDir(), info.getAssistantNo() + "i.png");
					if(file.exists()) {
						img.setImageURI(Uri.fromFile(file));
						img.setVisibility(View.VISIBLE);
					}
				} else {
					img.setVisibility(View.GONE);
				}
				TextView tv297 = m297.get(i);
				int data = info.get297();
				if ( data != 0 ) {
					tv297.setText("+"+data);
				} else {
					tv297.setText("");
				}
				
				if (info.getAwoken() > 0) {
					List<AwokenSkill> list = info.getAwokenSkills(false);
					for (AwokenSkill skill : list) {
						if (awokenMaps.containsKey(skill)) {
							int value = awokenMaps.get(skill);
							awokenMaps.put(skill, value + 1);
						} else {
							awokenMaps.put(skill, 1);
						}
					}
				}
				List<MoneyAwokenSkill> list = info.getPotentialAwokenList();
				if ( list.size() > 0 ) {
					for(MoneyAwokenSkill skill : list) {
						if ( skill == MoneyAwokenSkill.SKILL_NONE ) {
							continue;
						}
						++potentialCounter[skill.ordinal()-1];
					}
				}

				hp += info.getHP();
				power[2] += info.getRecovery();

				Pair<Integer, Integer> props = info.getProps();
				if (props.first == -1) {
					LogUtil.e("error prop=-1 ==>", info.getNo());
					return;
				}
				int atk = info.getAtk();
				power[props.first] += atk;
				if (props.second == props.first) {
					power[props.second] += atk / 10;
				} else if (props.second != -1) {
					power[props.second] += atk / 3;
				}

				boolean displayLSkill = true;
				TextView displayLSkillView = null;
				if (i == 0) {
					displayLSkillView = mLeaderSkill;
				} else if (i == 5) {
					displayLSkillView = mFriendLSkill;
				} else {
					displayLSkill = false;
				}
				if (displayLSkill) { // leader
					String display = MonsterSkill.getLeaderSkillString(ctx,
							info.getLeaderSkill());
					displayLSkillView.setText(display);
				}

			} else {
				mMember.get(i).setImageResource(R.drawable.i000);
			}

		}
		SharedPreferences pref = getActivity().getSharedPreferences("singleUp", Context.MODE_PRIVATE);
		int item = pref.getInt("item"+mCurrentTeam, 0);
		singleUp.setSelection(item);
		
		SinglePowerUp powerUp = SinglePowerUp.from(item);
		
		if(powerUp == SinglePowerUp.RCV) {
			rcvEnhanced = (int)(rcvEnhanced*1.25);
		} else if(powerUp == SinglePowerUp.RCV_UP) {
			rcvEnhanced = (int)(rcvEnhanced*1.35);
		}
		for (int i = 0; i < 6; ++i) {
			TextView tv = mPower.get(i);
			if (i == 2) {
				if (power[i] != rcvEnhanced) {
					// tv.setTextColor(Color.RED);
					tv.setText(rcvEnhanced + "(" + power[i] + ")");
				} else {
					// tv.setTextColor(Color.BLACK);
					tv.setText(String.valueOf(rcvEnhanced));
				}

			} else {
				tv.setText(String.valueOf(power[i]));
			}
		}
		int count = 0;
		for (Entry<AwokenSkill, Integer> entry : awokenMaps.entrySet()) {
			if(mAwokenCnt.size() > count) {
				TextView tv = mAwokenCnt.get(count);
				ImageView iv = mAwokenImage.get(count);

				tv.setText(entry.getValue() + "x");
				tv.setVisibility(View.VISIBLE);

				iv.setImageResource(R.drawable.awokenskill01
						+ entry.getKey().ordinal());
				iv.setVisibility(View.VISIBLE);
			}
			++count;
		}
		
		int div = count / 7 + (((count % 7) == 0) ? 0 : 1);
		int next = div * 7;
		for (int i = count; i < next; ++i) {
			mAwokenCnt.get(i).setVisibility(View.INVISIBLE);
			mAwokenImage.get(i).setVisibility(View.INVISIBLE);
		}
		int max = mAwokenLine.size();
		for (int i = 0; i < max; ++i) {
			if (i < div) {
				mAwokenLine.get(i).setVisibility(View.VISIBLE);
			} else {
				mAwokenLine.get(i).setVisibility(View.GONE);
			}
		}
		boolean[] allZero = {true, true};
		int pcount = 0;
		for(int j=0; j<2; ++j) {
			for(int i=0; i<5; ++i) {
				int value = potentialCounter[pcount];
				TextView tv = mPotentialCnt.get(pcount);
				ImageView iv = mPotentialImage.get(pcount);
				tv.setText(value + "x");
				tv.setVisibility(View.VISIBLE);
	
				// MoneyAwokenSkill id starts from 1
				iv.setImageResource(R.drawable.mawoken01
						+ pcount);
				iv.setVisibility(View.VISIBLE);
				if ( value != 0 ) {
					allZero[j] = false;
				}
				++pcount;
			}
		}
		for ( int i=0; i<2; ++i) {
			if ( allZero[i] == true ) {
				mPotentialLine.get(i).setVisibility(View.GONE);
			} else {
				mPotentialLine.get(i).setVisibility(View.VISIBLE);
			}
		}
		
		if(powerUp == SinglePowerUp.HP) {
			hpEnhanced = (int)(hpEnhanced * 1.05f);
		} else if(powerUp == SinglePowerUp.HP_UP) {
			hpEnhanced = (int)(hpEnhanced * 1.15f);
		}
		
		if (hp != hpEnhanced) {
			mTvHP.setText(hpEnhanced + "(" + hp + ")");
		} else {
			mTvHP.setText(String.valueOf(hp));
		}
		

	}
}
