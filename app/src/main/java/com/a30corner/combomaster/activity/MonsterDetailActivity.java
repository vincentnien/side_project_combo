package com.a30corner.combomaster.activity;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.ui.ArrayAdapterWithIcon;
import com.a30corner.combomaster.activity.ui.DialogUtil;
import com.a30corner.combomaster.activity.ui.PotentialPickerDialog;
import com.a30corner.combomaster.pad.monster.MonsterSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.AwokenSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MoneyAwokenSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MonsterType;
import com.a30corner.combomaster.pad.monster.MonsterVO;
import com.a30corner.combomaster.provider.LocalDBHelper;
import com.a30corner.combomaster.provider.vo.MonsterDO;
import com.a30corner.combomaster.utils.Constants;
import com.a30corner.combomaster.utils.EasyUtil;

public class MonsterDetailActivity extends Activity {

	private Intent mIntentExtra;
	private Handler mNonUiHandler;

	private ProgressDialog mDialog;

	private ImageView mImages;
	private Button mMonsterLv;
	private Button mMonsterHp;
	private Button mMonsterAtk;
	private Button mMonsterRcv;
	private Button mMonsterAwoken;
	private View mSuperAwokenLayout;
	private ImageView mSuperAwoken;
	private Button mSuperAwokenButton;
	private Button mPotentialAwokenButton;
	private List<ImageView> mAwokens = new ArrayList<ImageView>();
	private List<ImageView> mSecondAwokens = new ArrayList<ImageView>();
	private List<ImageView> mPotentialAwokens = new ArrayList<ImageView>();

	private Button mLvMax;
	private Button mHpMax;
	private Button mAtkMax;
	private Button mRcvMax;
	private Button mAwkMax;

	private Button mS1Cd;
	private Button mS2Cd;
	
	private ImageView mProp1, mProp2;
	private ImageView mType1, mType2, mType3;
	
	private TextView mLeaderSkill;
	private TextView mActiveSkill;

	private Button mButton;

	private Button mBtnNo;

	private long mLastToast = 0;

	int addHp=0;
	int addRcv=0;
	int addAtk=0;

	int index = -1;
	int no;
	int lv;
	int egg1;
	int egg2;
	int egg3;
	int awoken;
	int atk;
	int rcv;
	int hp;
	int active2 = -1;
	int cd1 = 0;
	int cd2 = 0;
	int sawoken_id = -1;
	private MonsterVO data;
	private MonsterVO data2;
	MoneyAwokenSkill[] mMAws = new MoneyAwokenSkill[PotentialPickerDialog.MAX_ARRAY_SIZE];

	private boolean initMonsterID(int value) {
		try {
			MonsterVO vo = LocalDBHelper.getMonsterData(MonsterDetailActivity.this, value);
			if ( vo != null ) {
				data = vo;
				no = value;
				initMonsterData();
				return true;
			}
		} finally { //catch (IOException e)
			// Toast.makeText(MonsterDetailActivity.this,
			// getString(R.string.no_such_id, value),
			// Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	private void setMonsterMaximum() {
		awoken = mAwokens.size();
		egg1 = 99;
		egg2 = 99;
		egg3 = 99;
		lv = data.getMaxLv();
		cd1 = data.slvmax;
		if(data2 != null) {
			cd2 = data2.slvmax;
		}
		initMonsterData();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ( resultCode == RESULT_OK ) {
			int id = data.getIntExtra("id", 1);
			if(requestCode == 0) {
				initMonsterID(id);
			} else if (requestCode == 10) {
				active2 = id;
				// TODO: lv in db
				//int lv = data.getIntExtra("lv", 99);
				reloadActive2();
				Observable.just(0).delay(200L, TimeUnit.MILLISECONDS).subscribe(new Action1<Integer>() {
					@Override
					public void call(Integer integer) {
						updateMonsterData();
					}
				});
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void reloadActive2() {
		Observable.just(active2)
			.subscribeOn(Schedulers.immediate())
			.filter(new Func1<Integer, Boolean>() {
				
				@Override
				public Boolean call(Integer id) {
					return id > 0;
				}
			})
			.map(new Func1<Integer, MonsterVO>() {
				
				@Override
				public MonsterVO call(Integer id) {
					return LocalDBHelper.getMonsterData(MonsterDetailActivity.this, id);
				}
			})
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new Action1<MonsterVO>() {
				
				@Override
				public void call(MonsterVO mvo) {
					if(mvo != null && mvo.getActiveSkill() != null) {
						data2 = mvo;
						if(data.getMonsterProps().first == data2.getMonsterProps().first) {
							addHp = (int)((data2.getHp(99)+990) *  Constants.HP_ADDITION);
							addRcv = (int)((data2.getRecovery(99)+495) *  Constants.RCV_ADDITION);
							addAtk = (int)((data2.getAtk(99)+297)* Constants.ATK_ADDITION);
						} else {
							addHp = addRcv = addAtk = 0;
						}
						updateActive2();
					}
				}
			});
	}
	
	private void initViews() {
		Button search = (Button) findViewById(R.id.btn_search);
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MonsterDetailActivity.this, SearchMonsterActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		
		mProp1 = (ImageView) findViewById(R.id.prop1);
		mProp2 = (ImageView) findViewById(R.id.prop2);
		mType1 = (ImageView) findViewById(R.id.type1);
		mType2 = (ImageView) findViewById(R.id.type2);
		mType3 = (ImageView) findViewById(R.id.type3);
		
		mImages = (ImageView) findViewById(R.id.monster_image);
		mMonsterLv = (Button) findViewById(R.id.btn_monster_lv);
		mMonsterHp = (Button) findViewById(R.id.monster_hp);
		mMonsterRcv = (Button) findViewById(R.id.monster_rec);
		mMonsterAtk = (Button) findViewById(R.id.monster_atk);
		mMonsterAwoken = (Button) findViewById(R.id.awoken_count);
		mSuperAwokenButton = (Button) findViewById(R.id.super_awoken);
		mPotentialAwokenButton = (Button) findViewById(R.id.potential_awoken);
		mBtnNo = (Button) findViewById(R.id.btn_monster_no);

		mSuperAwoken = (ImageView) findViewById(R.id.super_awoken_skill);
		mSuperAwokenLayout = findViewById(R.id.super_awoken_layout);

		mLeaderSkill = (TextView) findViewById(R.id.leader_skill);
		mActiveSkill = (TextView) findViewById(R.id.active_skill);

		Button hyper = (Button) findViewById(R.id.btn_hyper);
		hyper.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setMonsterMaximum();
			}
		});

		mBtnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtil.getNumberDialog(MonsterDetailActivity.this, no,
						new DialogUtil.ResultCallback() {

							@Override
							public void onResult(int value) {
								initMonsterID(value);
							}

							@Override
							public void onCancel() {
							}
						}).show();
			}
		});

		Button btnPrev = (Button) findViewById(R.id.pp);
		btnPrev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int counter = 1;
				do {
					if (no - counter > 0) {
						boolean success = initMonsterID(no - counter);
						if (success) {
							return;
						}
					} else {
						break;
					}
				} while (++counter < 200);
				Toast.makeText(MonsterDetailActivity.this,
						getString(R.string.no_such_id, no - counter),
						Toast.LENGTH_SHORT).show();
			}
		});
		Button btnNext = (Button) findViewById(R.id.nn);
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int counter = 1;
				do {
					boolean success = initMonsterID(no + counter);
					if (success) {
						return;
					}
				} while (++counter < 200);
				Toast.makeText(MonsterDetailActivity.this,
						getString(R.string.no_such_id, no + counter),
						Toast.LENGTH_SHORT).show();
			}
		});
		mMonsterLv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtil.getNumberPickerDialog(MonsterDetailActivity.this,
						R.string.lv, lv, 1, data.getMaxLv(),
						new DialogUtil.ResultCallback() {

							@Override
							public void onResult(int value) {
								lv = value;
								initMonsterData();
							}

							@Override
							public void onCancel() {
							}
						}).show();
			}
		});
		mMonsterHp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtil.getNumberPickerDialog(MonsterDetailActivity.this,
						R.string.hp_, egg1, 0, 99,
						new DialogUtil.ResultCallback() {

							@Override
							public void onResult(int value) {
								egg1 = value;
								initMonsterData();
							}

							@Override
							public void onCancel() {
							}
						}).show();
			}
		});
		mMonsterRcv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtil.getNumberPickerDialog(MonsterDetailActivity.this,
						R.string.rec_, egg3, 0, 99,
						new DialogUtil.ResultCallback() {

							@Override
							public void onResult(int value) {
								egg3 = value;
								initMonsterData();
							}

							@Override
							public void onCancel() {
							}
						}).show();
			}
		});
		mMonsterAtk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtil.getNumberPickerDialog(MonsterDetailActivity.this,
						R.string.atk_, egg2, 0, 99,
						new DialogUtil.ResultCallback() {

							@Override
							public void onResult(int value) {
								egg2 = value;
								initMonsterData();
							}

							@Override
							public void onCancel() {
							}
						}).show();
			}
		});
		mMonsterAwoken.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtil.getNumberPickerDialog(MonsterDetailActivity.this,
						R.string.awoken, awoken, 0,
						data.getAwokenList().size(),
						new DialogUtil.ResultCallback() {

							@Override
							public void onResult(int value) {
								awoken = value;
								initMonsterData();
							}

							@Override
							public void onCancel() {
							}
						}).show();
			}
		});
		mSuperAwokenButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String [] items;
				Integer[] icons;

				List<AwokenSkill> sawokens = data.getSuperAwokenList();
				int size = sawokens.size()+1;
				items = new String[size];
				items[0] = "CLEAR";
				for(int i=1; i<=sawokens.size(); ++i) {
					items[i] = "";
				}
				icons = new Integer[size];
				icons[0] = 0;
				for(int i=1; i<=sawokens.size(); ++i) {
					icons[i] = R.drawable.awokenskill01 + sawokens.get(i-1).ordinal() ;
				}


				ListAdapter adapter = new ArrayAdapterWithIcon(MonsterDetailActivity.this, items, icons);

				new AlertDialog.Builder(MonsterDetailActivity.this).setTitle(R.string.super_awoken_skills)
						.setAdapter(adapter, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item ) {
								if(item == 0) {
									mSuperAwoken.setImageResource(0);
									sawoken_id = -1;
								} else {
									sawoken_id = data.getSuperAwokenList().get(item-1).ordinal();
									mSuperAwoken.setImageResource(R.drawable.awokenskill01 + sawoken_id);
								}
							}
						}).show();
			}
		});
		mPotentialAwokenButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogUtil.getPotentialDialog(MonsterDetailActivity.this,
						new DialogUtil.IPotentialCallback() {

							@Override
							public void onResult(MoneyAwokenSkill[] list) {
								mMAws = list;
								initMonsterData();
							}

							@Override
							public void onCancel() {
							}
				}, mMAws).show();
			}
		});

		mLvMax = (Button) findViewById(R.id.btn_lv_max);
		mHpMax = (Button) findViewById(R.id.btn_hp_max);
		mAtkMax = (Button) findViewById(R.id.btn_atk_max);
		mRcvMax = (Button) findViewById(R.id.btn_rcv_max);
		mAwkMax = (Button) findViewById(R.id.btn_awk_max);
		mS1Cd = (Button) findViewById(R.id.skill_cd);
		mS2Cd = (Button) findViewById(R.id.skill2_cd);
		
		mS1Cd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogUtil.getNumberRangeDialog(MonsterDetailActivity.this,
						R.string.cd, cd1, data.slvmax, data.slv1,
						new DialogUtil.ResultCallback() {

							@Override
							public void onResult(int value) {
								cd1 = value;
								mS1Cd.setText(String.valueOf(cd1));
							}

							@Override
							public void onCancel() {
							}
						}).show();
			}
		});
		mS2Cd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int min = 1;
				int max = 1;
				if(data2 != null) {
					min = data2.slvmax;
					max = data2.slv1;
				}
				DialogUtil.getNumberRangeDialog(MonsterDetailActivity.this,
						R.string.cd, cd2, min, max,
						new DialogUtil.ResultCallback() {

							@Override
							public void onResult(int value) {
								cd2 = value;
								mS2Cd.setText(String.valueOf(cd2));
							}

							@Override
							public void onCancel() {
							}
						}).show();
			}
		});
		mLvMax.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lv != data.getMaxLv()) {
					lv = data.getMaxLv();
				} else {
					lv = 1;
				}
				initMonsterData();
			}
		});
		mHpMax.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (egg1 != 99) {
					egg1 = 99;
				} else {
					egg1 = 0;
				}
				initMonsterData();
			}
		});
		mAtkMax.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (egg2 != 99) {
					egg2 = 99;
				} else {
					egg2 = 0;
				}
				initMonsterData();
			}
		});
		mRcvMax.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (egg3 != 99) {
					egg3 = 99;
				} else {
					egg3 = 0;
				}
				initMonsterData();
			}
		});
		mAwkMax.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int max = data.getAwokenList().size();
				if (awoken != max) {
					awoken = max;
				} else {
					awoken = 0;
				}
				initMonsterData();
			}
		});
		/*
		 * mMonsterSkill = (TextView) findViewById(R.id.monster_skill);
		 * mMonsterSDesc = (TextView) findViewById(R.id.monster_skill_desc);
		 * mMonsterLeaderSkill = (TextView)
		 * findViewById(R.id.monster_leader_skill); mMonsterLSDesc = (TextView)
		 * findViewById(R.id.monster_leader_skill_desc);
		 */
		final int[] ids = { R.id.awoken_skill1, R.id.awoken_skill2,
				R.id.awoken_skill3, R.id.awoken_skill4, R.id.awoken_skill5,
				R.id.awoken_skill6, R.id.awoken_skill7, R.id.awoken_skill8,
				R.id.awoken_skill9 };
		for (int i = 0; i < ids.length; ++i) {
			ImageView image = (ImageView) findViewById(ids[i]);
			mAwokens.add(image);
		}

		for(int i=0; i<9; ++i) {
			ImageView image = (ImageView) findViewById(R.id.assistant_skill1+i);
			mSecondAwokens.add(image);
		}

		final int[] pids = {R.id.p_awoken_skill1, R.id.p_awoken_skill2, R.id.p_awoken_skill3,
				R.id.p_awoken_skill4, R.id.p_awoken_skill5, R.id.p_awoken_skill6,
				R.id.p_awoken_skill7, R.id.p_awoken_skill8};
		for(int i=0; i<pids.length; ++i) {
			ImageView image = (ImageView) findViewById(pids[i]);
			mPotentialAwokens.add(image);
		}

		Button setActive = (Button) findViewById(R.id.set_active2);
		setActive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MonsterDetailActivity.this, SearchMonsterActivity.class);
				intent.putExtra("rare", 5);
				intent.putExtra("needLv", true);
				startActivityForResult(intent, 10);
			}
		});
		
		Button cancel = (Button) findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Button fix = (Button) findViewById(R.id.btn_fixme);
		fix.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File f = new File(getFilesDir(), no + "i.png");
				if (f.exists()) {
					f.delete();
				}
				loadImage();
			}
		});

		mButton = (Button) findViewById(R.id.btn_confirm);
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int awsSize = data.getAwokenList().size();
				List<MoneyAwokenSkill> list = new ArrayList<MoneyAwokenSkill>();
				for(int i=0; i< PotentialPickerDialog.MAX_ARRAY_SIZE; ++i) {
					if ( mMAws[i] != null && mMAws[i] != MoneyAwokenSkill.SKILL_NONE ) {
						list.add(mMAws[i]);
					}
				}
				
				MonsterDO mdo = MonsterDO.fromData(index, no, lv, egg1, egg2,
						egg3, awoken, awsSize == awoken, list, active2, cd1, cd2, sawoken_id);
				LocalDBHelper.addMonster(MonsterDetailActivity.this, mdo);

				ComboMasterApplication instance = ComboMasterApplication
						.getsInstance();
				instance.updateTeamInfo(index);
				// instance.dataChanged();

				finish();
			}
		});
	}
	
	public void resetActive2(View view) {
		active2 = -1;
		data2 = null;
		updateActive2();
	}


	private void updateActive2() {
		if(data2 != null ) {
			((TextView)findViewById(R.id.active_skill2)).setText(MonsterSkill.getActiveSkillString(
					MonsterDetailActivity.this, data2.getActiveSkill()));
			mS2Cd.setEnabled(true);
			if(cd2 <= 0 || cd2 == 99) {
				cd2 = data2.slv1;
			}
		} else {
			((TextView)findViewById(R.id.active_skill2)).setText("");
			mS2Cd.setEnabled(false);
			cd2 = 0;
		}
		mS2Cd.setText(String.valueOf(cd2));
		updateSecondAwoken();
	}

	private void hideAssistantAwoken() {
		for(ImageView img : mSecondAwokens) {
			img.setVisibility(View.GONE);
		}
	}
	private void showAssistantAwoken(MonsterVO vo) {
		List<AwokenSkill> list = vo.getAwokenList();
		int size = list.size();
		for(int i=0; i<size; ++i) {
			ImageView img = mSecondAwokens.get(i);
			img.setImageResource(R.drawable.awokenskill01+list.get(i).ordinal());
			img.setVisibility(View.VISIBLE);
		}
		for(int i=size; i<9; ++i) {
			mSecondAwokens.get(i).setVisibility(View.GONE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mIntentExtra = getIntent();
		if (mIntentExtra == null) {
			Toast.makeText(MonsterDetailActivity.this, "no data",
					Toast.LENGTH_SHORT).show();
			return;
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.monster_confirm);
		initViews();

		HandlerThread ht = new HandlerThread("network");
		ht.start();
		mNonUiHandler = new Handler(ht.getLooper());

		mDialog = new ProgressDialog(MonsterDetailActivity.this);
		mDialog.setCancelable(true);
		mDialog.setTitle(getString(R.string.data_loading));
		mDialog.setIndeterminate(true);
		mDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				finish();
			}
		});
		mDialog.show();
		
		for(int i=0; i< PotentialPickerDialog.MAX_ARRAY_SIZE; ++i) {
			mMAws[i] = MoneyAwokenSkill.SKILL_NONE;
		}

		mNonUiHandler.post(new Runnable() {

			@Override
			public void run() {
				index = mIntentExtra.getIntExtra("index", -1);

				if (index != -1) {
					loadMonsterFromPreference();
				} else {
					no = mIntentExtra.getIntExtra(
							AddMonsterActivity.MONSTER_ID, 1);
					lv = mIntentExtra.getIntExtra(
							AddMonsterActivity.MONSTER_LV, 99);
					egg1 = mIntentExtra.getIntExtra(
							AddMonsterActivity.MONSTER_HP, 99);
					egg2 = mIntentExtra.getIntExtra(
							AddMonsterActivity.MONSTER_ATK, 99);
					egg3 = mIntentExtra.getIntExtra(
							AddMonsterActivity.MONSTER_REC, 99);
					awoken = mIntentExtra.getIntExtra(
							AddMonsterActivity.MONSTER_AWOKEN, 10);
					for(int i=0; i<PotentialPickerDialog.MAX_ARRAY_SIZE; ++i) {
						mMAws[i] = null;
					}
					active2 = -1;
					
				}

				try {
					if (no == -1) {
						no = 1;
					}
					data = LocalDBHelper.getMonsterData(MonsterDetailActivity.this, no);
					if ( data == null ) {
						Toast.makeText(MonsterDetailActivity.this,
								"no such monster id=" + no, Toast.LENGTH_SHORT)
								.show();
						finish();
					} else {
						initMonsterData();
					}
				} finally {
					
				}

			}
		});
	}

	private void loadMonsterFromPreference() {
		MonsterDO mdo = LocalDBHelper.getMonsterBox(MonsterDetailActivity.this,
				index);
		if (mdo == null) {
			no = 1;
			egg1 = egg2 = egg3 = 0;
			lv = 1;
			awoken = 0;

		} else {
			no = mdo.no;
			egg1 = mdo.egg1;
			egg2 = mdo.egg2;
			egg3 = mdo.egg3;
			lv = mdo.lv;
			awoken = mdo.awoken;
			sawoken_id = mdo.super_awoken;
			active2 = mdo.active2;
			cd1 = mdo.skill1_cd;
			cd2 = mdo.skill2_cd;
			List<MoneyAwokenSkill> list = mdo.potentialList;
			int size = list.size();
			for(int i=0; i< PotentialPickerDialog.MAX_ARRAY_SIZE; ++i) {
				if ( i < size ) {
					mMAws[i] = list.get(i);
				} else {
					mMAws[i] = null;
				}
			}
		}
	}

	private class DownloadTask extends AsyncTask<String, Void, Bitmap> {

		private ImageView mImageView;
		private Activity context;
		private int no;

		public DownloadTask(Activity context, int no, ImageView imageView) {
			this.context = context;
			this.no = no;
			mImageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String... url) {
			InputStream is = null;
			OutputStream os = null;
			boolean error = false;
			try {
				is = new URL(url[0]).openStream();
				os = context.openFileOutput(no + "i.png", Context.MODE_PRIVATE);

				int read;
				byte[] buffer = new byte[8 * 1024];
				while ((read = is.read(buffer)) >= 0) {
					os.write(buffer, 0, read);
				}

			} catch (Exception e) {
				long diff = (System.currentTimeMillis() - mLastToast) / 1000;
				if (diff > 60) {
					context.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(context,
									context.getString(R.string.need_network),
									Toast.LENGTH_LONG).show();
						}
					});
					mLastToast = System.currentTimeMillis();
				}
				error = true;
			} finally {
				EasyUtil.close(is);
				EasyUtil.close(os);
			}
			
			File f = new File(getFilesDir(), no + "i.png");
			if (f.exists()) {
				if (error) {
					f.delete();
				} else {
					return BitmapFactory.decodeFile(f.getAbsolutePath());
				}
			}
			return null;

		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result == null) {
				return;
			}
			int currentNo = Integer.parseInt(mBtnNo.getText().toString());
			if (currentNo == no) {
				mImageView.setImageBitmap(result);
			}
		}

	}

	private void loadImage() {
		final File f = new File(getFilesDir(), no + "i.png");
		if (f.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
			mImages.setImageBitmap(bitmap);
		} else {
			mImages.setImageResource(R.drawable.i000);
			new DownloadTask(MonsterDetailActivity.this, no, mImages)
					.execute(data.getImageUrl());
		}
	}

	private void initMonsterData() {
		reloadActive2();
		updateMonsterData();
	}

	private void updateSecondAwoken() {
		if(data2 != null && data2.getAwokenList().contains(AwokenSkill.ASSISTANT)) {
			showAssistantAwoken(data2);
		} else {
			hideAssistantAwoken();
		}
	}

	private void updateMonsterData() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// set up ui
				loadImage();

				List<AwokenSkill> awokens = data.getAwokenList();
				int enhanceHpCount = 0;
				int enhanceAtkCount = 0;
				int enhanceHealCount = 0;
				int hpDownCount = 0, atkDownCount = 0, healDownCount = 0;
				int awssize = awokens.size();// Math.min(awoken,
				// awokens.size());

				if (awoken > awssize) {
					awoken = awssize;
				}
				if (lv > data.getMaxLv()) {
					lv = data.getMaxLv();
				}

				Pair<Integer, Integer> props = data.getMonsterProps();
				Pair<MonsterType, MonsterType> types = data.getMonsterTypes();
				final int[] resProps = {
						R.drawable.dark, R.drawable.fire, R.drawable.heart,
						R.drawable.light, R.drawable.water, R.drawable.wood
				};
				MonsterType type3 = data.getMonsterType3();

				mProp1.setImageResource(resProps[props.first]);
				if ( props.second != -1 ) {
					mProp2.setImageResource(resProps[props.second]);
					mProp2.setVisibility(View.VISIBLE);
				} else {
					mProp2.setVisibility(View.INVISIBLE);
				}
				final int[] resTypes = {
						R.drawable.monster_type_0,R.drawable.monster_type_1,R.drawable.monster_type_2,
						R.drawable.monster_type_3,R.drawable.monster_type_4,R.drawable.monster_type_5,
						R.drawable.monster_type_6,R.drawable.monster_type_7,R.drawable.monster_type_8,
						R.drawable.monster_type_9, R.drawable.monster_type_0, R.drawable.monster_type_11,
						R.drawable.monster_type_12,R.drawable.monster_type_13
				};
				int fid = types.first.ordinal();
				if ( fid < resTypes.length ) {
					mType1.setImageResource(resTypes[fid]);
				}
				if ( types.second != MonsterType.NONE ) {
					int id = types.second.ordinal();
					if ( id < resTypes.length ) {
						mType2.setImageResource(resTypes[id]);
						mType2.setVisibility(View.VISIBLE);
					} else {
						mType2.setVisibility(View.INVISIBLE);
					}

				} else {
					mType2.setVisibility(View.INVISIBLE);
				}
				if ( type3 != null && type3 != MonsterType.NONE ) {
					int id = type3.ordinal();
					if ( id < resTypes.length ) {
						mType3.setImageResource(resTypes[id]);
						mType3.setVisibility(View.VISIBLE);
					} else {
						mType3.setVisibility(View.INVISIBLE);
					}
				} else {
					mType3.setVisibility(View.INVISIBLE);
				}

				for (int i = 0; i < 9; ++i) {
					ImageView image = mAwokens.get(i);
					if (i >= awssize) {
						image.setVisibility(View.GONE);
						continue;
					} else {
						AwokenSkill s = awokens.get(i);
						int id = s.ordinal();
						image.setVisibility(View.VISIBLE);
						image.setImageResource(R.drawable.awokenskill01 + id);
						if (i < awoken) {
							image.setAlpha(1.0f);
						} else {
							image.setAlpha(0.3f);
						}

						if (i < awoken) {
							if (s == AwokenSkill.ENHANCE_HP) {
								++enhanceHpCount;
							} else if (s == AwokenSkill.ENHANCE_ATK) {
								++enhanceAtkCount;
							} else if (s == AwokenSkill.ENHANCE_HEAL) {
								++enhanceHealCount;
							} else if (s == AwokenSkill.HP_DOWN) {
								++hpDownCount;
							} else if (s == AwokenSkill.ATK_DOWN) {
								++atkDownCount;
							} else if (s == AwokenSkill.RCV_DOWN) {
								++healDownCount;
							}
						}
					}
					// skills.add(s);
				}
				if(data2 != null) {
					List<AwokenSkill> list = data2.getAwokenList();
					for (int i = 0; i < list.size(); ++i) {
						AwokenSkill s = list.get(i);
						if(s == AwokenSkill.ENHANCE_HP) {
							++enhanceHpCount;
						} else if (s == AwokenSkill.ENHANCE_ATK) {
							++enhanceAtkCount;
						} else if (s == AwokenSkill.ENHANCE_HEAL) {
							++enhanceHealCount;
						} else if (s == AwokenSkill.HP_DOWN) {
							++hpDownCount;
						} else if (s == AwokenSkill.ATK_DOWN) {
							++atkDownCount;
						} else if (s == AwokenSkill.RCV_DOWN) {
							++healDownCount;
						}
					}
				} else {
					hideAssistantAwoken();
				}
				int enhancePotentialHpCount = 0;
				int enhancePotentialAtkCount = 0;
				int enhancePotentialRcvCount = 0;
				int enhanceAll = 0;

				int epHpCount = 0;
				int epAtkCount = 0;
				int epRcvCount = 0;

				List<MoneyAwokenSkill> pAwokens = new ArrayList<MonsterSkill.MoneyAwokenSkill>();//data.getPotentialAwokenList();

				for(int i=0; i< PotentialPickerDialog.MAX_ARRAY_SIZE; ++i) {
					if ( mMAws[i] != null && mMAws[i] != MoneyAwokenSkill.SKILL_NONE ) {
						pAwokens.add(mMAws[i]);
					}
				}
				int size = pAwokens.size();
				for(int i=0; i<PotentialPickerDialog.MAX_ARRAY_SIZE; ++i) {
					ImageView image = mPotentialAwokens.get(i);
					if ( i >= size ) {
						image.setVisibility(View.GONE);
						continue;
					}
					image.setVisibility(View.VISIBLE);
					MoneyAwokenSkill s = pAwokens.get(i);
					int id = s.ordinal() - 1;
					image.setVisibility(View.VISIBLE);
					image.setImageResource(R.drawable.mawoken01+id);

					if ( MoneyAwokenSkill.ENHANCE_HP == s ) {
						++enhancePotentialHpCount;
					} else if (MoneyAwokenSkill.ENHANCE_ATK == s) {
						++enhancePotentialAtkCount;
					} else if (MoneyAwokenSkill.ENHANCE_RCV == s ) {
						++enhancePotentialRcvCount;
					} else if (MoneyAwokenSkill.ALL_UP == s) {
						++enhanceAll;
					} else if (MoneyAwokenSkill.ENHANCE_HP_PLUS == s) {
						++epHpCount;
					} else if (MoneyAwokenSkill.ENHANCE_ATK_PLUS == s) {
						++epAtkCount;
					} else if (MoneyAwokenSkill.ENHANCE_RCV_PLUS == s ) {
						++epRcvCount;
					}
				}

				int hpp = egg1;
				int atkp = egg2;
				int rcvp = egg3;

				int tatk = data.getAtk(lv);
				int trcv = data.getRecovery(lv);
				int thp = data.getHp(lv);

				int patk = (int)(tatk * (enhanceAll * 0.02f + (enhancePotentialAtkCount) * 0.01f + epAtkCount * 0.03f));
				int prcv = (int)(trcv * (enhanceAll * 0.2f + (enhancePotentialRcvCount) * 0.1f + epRcvCount * 0.3f));
				int php = (int)(thp * (enhanceAll * 0.03f + (enhancePotentialHpCount) * 0.015f + epHpCount * 0.045f));


				atk = (tatk + (5 * atkp) + (enhanceAtkCount * 100)) + patk - atkDownCount * 1000;
				rcv = (trcv + (3 * rcvp) + (enhanceHealCount * 200)) + prcv - healDownCount * 2000;
				hp = (thp + (10 * hpp) + (enhanceHpCount * 500)) + php - hpDownCount * 5000;

				mBtnNo.setText(String.valueOf(no));
				mMonsterLv.setText("" + lv);
				String showAtk;
				String showHp;
				String showRcv;
				if(addHp>0 || addAtk>0 || addRcv>0) {
					atk += addAtk;
					hp += addHp;
					rcv += addRcv;
					if(atk <= 0) {
						atk = 1;
					}
					if(hp <= 0) {
						hp = 1;
					}

					showAtk = atk + " (+" + atkp + ") (+" +addAtk + ")";
					showHp = hp + " (+" + hpp + ") (+" +addHp + ")";
					showRcv = rcv + " (+" + rcvp + ") (+" +addRcv + ")";
				} else {
					if(atk <= 0) {
						atk = 1;
					}
					if(hp <= 0) {
						hp = 1;
					}

					showAtk = atk + " (+" + atkp + ")";
					showHp = hp + " (+" + hpp + ")";
					showRcv = rcv + " (+" + rcvp + ")";
				}

				mMonsterAtk.setText(showAtk);
				mMonsterHp.setText(showHp);
				mMonsterRcv.setText(showRcv);

				mMonsterAwoken.setText("" + awoken);

				mLeaderSkill.setText(MonsterSkill.getLeaderSkillString(
						MonsterDetailActivity.this, data.getLeaderSkill()));
				mActiveSkill.setText(MonsterSkill.getActiveSkillString(
						MonsterDetailActivity.this, data.getActiveSkill()));

				if(cd1 <= 0 || cd1>data.slv1) {
					cd1 = data.slv1;
				} else if(cd1<data.slvmax) {
					cd1 = data.slvmax;
				}
				if(cd2<=0) {
					// TODO: check active 2?
				}
				mS1Cd.setText(String.valueOf(cd1));
				mS1Cd.setEnabled(cd1>0);
				mS2Cd.setEnabled(data2 != null);
				mS2Cd.setText(String.valueOf(cd2));

				setMaxMin(lv, data.getMaxLv(), mLvMax);
				setMaxMin(egg1, 99, mHpMax);
				setMaxMin(egg2, 99, mAtkMax);
				setMaxMin(egg3, 99, mRcvMax);
				setMaxMin(awoken, awssize, mAwkMax);

				if(sawoken_id != -1) {
					mSuperAwoken.setImageResource(R.drawable.awokenskill01 + sawoken_id);
				}

				if(data.getSuperAwokenList().size()>0) {
					mSuperAwokenLayout.setVisibility(View.VISIBLE);
				} else {
					mSuperAwokenLayout.setVisibility(View.GONE);
				}

				dismissDialog();
			}
		});
	}

	private void setMaxMin(int data, int max, Button btn) {
		if (data == max) {
			btn.setText(R.string.str_min);
		} else {
			btn.setText(R.string.str_max);
		}
	}

	private void dismissDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mNonUiHandler.getLooper().quit();
		dismissDialog();
	}
}
