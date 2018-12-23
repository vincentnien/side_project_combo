package com.a30corner.combomaster.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.fragment.TeamFragment;
import com.a30corner.combomaster.pad.monster.MonsterSkill;
import com.a30corner.combomaster.pad.monster.MonsterVO;
import com.a30corner.combomaster.provider.LocalDBHelper;
import com.a30corner.combomaster.provider.vo.MonsterDO;
import com.a30corner.combomaster.utils.BitmapUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class TeamAdjustActivity extends Activity {
	
	private Spinner mTeamSpinner;
	private ArrayAdapter<String> mAdapter;
	
	private ProgressDialog mProgressDialog;
	
	private Spinner[] mLvSpinner = new Spinner[6];
	private Spinner[] mAwokenSpinner = new Spinner[6];
	
	private EditText[] mHp = new EditText[6];
	private EditText[] mAtk = new EditText[6];
	private EditText[] mRcv = new EditText[6];
	
	private int[] mSelect;
	public static int TEAM_SIZE = 20;
	
	private Handler mHandler;
	
	private int[] mTargetLv = null;
	private int[] mTargetAwoken = null;
	private int[] mTargetHp = null;
	private int[] mTargetAtk = null;
	private int[] mTargetRcv = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_team_adjust);

		HandlerThread ht = new HandlerThread("ht");
		ht.start();
		mHandler = new Handler(ht.getLooper());
		
		Intent intent = getIntent();
		//List<Integer> data = intent.g("data");
		int[] data = intent.getExtras().getIntArray("data");
		if (data != null) {
			mSelect = new int[6];
			mTargetLv = new int[6];
			mTargetAwoken = new int[6];
			mTargetHp = new int[6];
			mTargetAtk = new int[6];
			mTargetRcv = new int[6];
			for(int i=0; i<6; ++i) {
				int index = i * 6;
				if(data.length<index+6) {
					break;
				}
				mSelect[i] = data[(index)];
				mTargetLv[i] = data[(index+1)];
				mTargetHp[i] = data[(index+2)];
				mTargetAtk[i] = data[(index+3)];
				mTargetRcv[i] = data[(index+4)];
				mTargetAwoken[i] = data[(index+5)];
			}
		} else {
			mSelect = intent.getIntArrayExtra("team");
		}
		if(mSelect == null) {
			finish();
			return ;
		}
		
		
		Button saveTeam = (Button) findViewById(R.id.save_team);
		saveTeam.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mProgressDialog.setMessage(getString(R.string.str_saving));
				mProgressDialog.show();
				
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// 
						int teamId = mTeamSpinner.getSelectedItemPosition();
						SharedPreferences prefs = getSharedPreferences("team",
								Context.MODE_PRIVATE);
						List<Integer> box = null;
						int boxindex = prefs.getInt(getMemberKey(teamId, 0), 0);
						if (boxindex >= 400) {
							// replace it if data comes from quick setup
							box = new ArrayList<Integer>(6);
							for(int i=0; i<6; ++i) {
								box.add(boxindex+i);
							}
						} else {
							// find six unoccupied box index(>=400)
							box = LocalDBHelper.getSixEmptyBox(TeamAdjustActivity.this);
						}
						
						for(int i=0; i<6; ++i) {
							int no = mSelect[i];
							if (no > 0) {
								int lv = (Integer)mLvSpinner[i].getSelectedItem();
								int egg1 = parseInt(mHp[i].getText().toString());
								int egg2 = parseInt(mAtk[i].getText().toString());
								int egg3 = parseInt(mRcv[i].getText().toString());
								MonsterVO vo = LocalDBHelper.getMonsterData(TeamAdjustActivity.this, no);
								
								int awoken = (Integer)mAwokenSpinner[i].getSelectedItem();
								boolean max = awoken!=0 && mAwokenSpinner[i].getSelectedItemPosition()==0;
								MonsterDO mdo = MonsterDO.fromData(box.get(i), no, lv, egg1, egg2,
										egg3, awoken, max, new ArrayList<MonsterSkill.MoneyAwokenSkill>(), -1, vo.slvmax, -1, -1);
								
								LocalDBHelper.addMonster(TeamAdjustActivity.this, mdo);
							}
						}

						Editor editor = prefs.edit();
						for (int i = 0; i < 6; ++i) {
							if (mSelect[i] > 0) {
								editor.putInt(getMemberKey(teamId, i), box.get(i)+1);
							} else {
								editor.putInt(getMemberKey(teamId, i), -1);
							}
						}
						String teamName = "Team " + (teamId+1);
						editor.putString(getTeamNameKey(teamId), teamName);
						editor.apply();
						ComboMasterApplication instance = ComboMasterApplication
								.getsInstance();
						instance.updateTeam(teamId);
						
						runOnUiThread(new Runnable() {
							public void run() {
								mProgressDialog.dismiss();
								setResult(RESULT_OK);
								finish();
							}
						});
					}
				});
			}
		});
		
		mTeamSpinner = (Spinner) findViewById(R.id.team);
		mAdapter = new ArrayAdapter<String>(this, R.layout.cm_spinner);

		mTeamSpinner.setAdapter(mAdapter);
		
		int[] id = {
				R.id.monster01,R.id.monster02,R.id.monster03,
				R.id.monster04,R.id.monster05,R.id.monster06,
		};
		for(int i=0; i<6; ++i) {
			View view = findViewById(id[i]);
			Spinner lv = (Spinner) view.findViewById(R.id.monster_lv);
			Spinner awoken = (Spinner) view.findViewById(R.id.monster_awoken);
			mLvSpinner[i] = lv;
			mAwokenSpinner[i] = awoken;
			
			TextView name = (TextView) view.findViewById(R.id.name);
			if (i==0) {
				name.setText(R.string.str_leader);
			} else if (i==5) {
				name.setText(R.string.str_friend_leader);
			} else {
				name.setText(R.string.str_member);
			}
			ImageView image = (ImageView) view.findViewById(R.id.monster);
			if(mSelect[i]>0) {
				File file = new File(getFilesDir(), String.format("%di.png", mSelect[i]));
				if ( file.exists() ) {
					image.setImageURI(Uri.fromFile(file));
				} else {
					final MonsterVO vo = LocalDBHelper.getMonsterData(TeamAdjustActivity.this, mSelect[i]);
					if (vo != null ) {
						ImageLoader.getInstance().displayImage(vo.getImageUrl(), image, new ImageLoadingListener() {
	                        
	                        @Override
	                        public void onLoadingStarted(String arg0, View arg1) {
	                        }
	                        
	                        @Override
	                        public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
	                        }
	                        
	                        @Override
	                        public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
	                            saveImageAsync(bitmap, vo.getNo());
	                        }
	                        
	                        @Override
	                        public void onLoadingCancelled(String arg0, View arg1) {
	                        }
	                    });
					}
					//image.setImageResource(R.drawable.i000);
				}
			}
			
			final EditText hp = (EditText) view.findViewById(R.id.monster_hp);
			final EditText atk = (EditText) view.findViewById(R.id.monster_atk);
			final EditText rcv = (EditText) view.findViewById(R.id.monster_rcv);
			
			mHp[i] = hp;
			mAtk[i] = atk;
			mRcv[i] = rcv;
			
			int total = 297;
			if (mTargetHp != null) {
				hp.setText(String.valueOf(mTargetHp[i]));
				atk.setText(String.valueOf(mTargetAtk[i]));
				rcv.setText(String.valueOf(mTargetRcv[i]));
				
				total = mTargetHp[i] + mTargetAtk[i] + mTargetRcv[i];
			}
			
			Button p297 = (Button) view.findViewById(R.id.monster_297);
			if(total == 0) {
				p297.setText(R.string.str_297);
			}
			p297.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Button btn = (Button)v;
					if(btn.getText().length()>2) { // +297
						set297(true, hp, atk, rcv);
						btn.setText(R.string.str_0);
					} else { // +0
						set297(false, hp, atk, rcv);
						btn.setText(R.string.str_297);
					}
				}
			});
		}
		
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(getString(R.string.str_loading));
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		
		loadData();
	}
	
	private void saveImageAsync(final Bitmap bitmap, final int id) {
		new Thread(new Runnable() {
            
            @Override
            public void run() {
                BitmapUtil.saveBitmap(TeamAdjustActivity.this, bitmap, String.format("%di.png", id));
            }
        }).start();
	}
	
	private String getTeamNameKey(int teamId) {
		return "Team" + teamId + "_name";
	}

	private String getMemberKey(int teamId, int index) {
		return "Team" + teamId + "_" + index;
	}
	
	private int parseInt(String data) {
		try {
			int val = Integer.parseInt(data);
			if (val<0) {
				val = 0;
			} else if(val>99) {
				val = 99;
			}
			return val;
		} catch(Exception e) {
			return 0;
		}
	}
	
	private void loadData() {
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				updateTeamName();
				
				for(int i=0; i<6; ++i) {
					if (mSelect[i]>0) {
						MonsterVO vo = MonsterVO.fromDB(TeamAdjustActivity.this, mSelect[i]);
						if (vo == null ) {
							mSelect[i] = -1;
							continue;
						}
						int lv = vo.getMaxLv();
						int awokenCount = vo.getAwokenList().size();
						
						ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(TeamAdjustActivity.this,R.layout.cm_spinner);
						for(int max=lv; max>0; --max) {
							adapter.add(max);
						}
						mLvSpinner[i].setAdapter(adapter);
						ArrayAdapter<Integer> adapter1 = new ArrayAdapter<Integer>(TeamAdjustActivity.this,R.layout.cm_spinner);
						for(int max=awokenCount; max>=0; --max) {
							adapter1.add(max);
						}
						mAwokenSpinner[i].setAdapter(adapter1);
						
						if(mTargetLv!=null) {
							mLvSpinner[i].setSelection(lv-mTargetLv[i]);
							mAwokenSpinner[i].setSelection(awokenCount-mTargetAwoken[i]);
						}
					}
				}
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						mProgressDialog.dismiss();
					}
				});
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		mHandler.getLooper().quit();
		super.onDestroy();
	}
	
	private void updateTeamName() {
		// do io access in ui thread is not a good behavior ...
		// but ... just do it.. :-P
		SharedPreferences sp = getSharedPreferences("team",
				Context.MODE_PRIVATE);
		String[] teams = new String[TeamFragment.TEAM_SIZE];
		for (int i = 0; i < TEAM_SIZE; ++i) {
			String name = sp.getString("Team" + i + "_name", "Team " + (i + 1));
			teams[i] = name;
		}
		mAdapter.clear();
		mAdapter.addAll(teams);
	}
	
	private void set297(boolean is297, EditText hp, EditText atk, EditText rcv) {
		String str297 = (is297)? "99":"0";
		hp.setText(str297);
		atk.setText(str297);
		rcv.setText(str297);
	}
	
	
}
