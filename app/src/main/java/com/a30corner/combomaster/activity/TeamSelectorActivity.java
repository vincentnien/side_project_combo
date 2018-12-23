package com.a30corner.combomaster.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.ui.DialogUtil;
import com.a30corner.combomaster.activity.ui.FilterDialog;
import com.a30corner.combomaster.activity.ui.FilterDialog.IFilterCallback;
import com.a30corner.combomaster.activity.ui.SortDialog;
import com.a30corner.combomaster.activity.ui.SortDialog.ISortCallback;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MoneyAwokenSkill;
import com.a30corner.combomaster.provider.LocalDBHelper;

public class TeamSelectorActivity extends Activity {
	private List<ImageView> mImage = new ArrayList<ImageView>();
	private int mTeamID = -1;

	private int[] mSelector = { -1, -1, -1, -1, -1, -1 };

	private EditText mTeamName;
	private ListView mListView;

	private FilterDialog mFilterDialog = null;
	private Handler mNonUi;
	
	private MonsterAdapter mAdapter;
	final int[] awsRes = { R.drawable.aws_01,
			R.drawable.aws_02, R.drawable.aws_03,
			R.drawable.aws_04, R.drawable.aws_05,
			R.drawable.aws_06, R.drawable.aws_07,
			R.drawable.aws_08, R.drawable.aws_max };
	
    private List<MonsterInfo> mData;
    private Map<String, String> mSortData = null;
    
    private int mPrimaryOrder = 4;
	
	private class MonsterAdapter extends BaseAdapter {

		private List<MonsterInfo> mData = new ArrayList<MonsterInfo>();
		private Context mCtx;
		private LayoutInflater inflater;
		
		private class ViewHolder {
			ImageView[] images = new ImageView[5];
			TextView[] lvs = new TextView[5];
			TextView[] p297s = new TextView[5];
			ImageView[] awokens = new ImageView[5];
			ImageView[] mawokens = new ImageView[5];
		}
		
		public MonsterAdapter(Context context) {
			mCtx = context;
			inflater = LayoutInflater.from(context);
		}
		
		public void setData(List<MonsterInfo> data) {
			mData.clear();
			mData.addAll(data);
		}
		
		private MonsterInfo getData(int position, int offset) {
			int index = position * 5 + offset;
			if ( index < mData.size() ) {
				return mData.get(index);
			} else {
				return null;
			}
		}
		
		@Override
		public int getCount() {
			int size = mData.size();
			int count = size/5;
			return ((size%5)!=0) ? count+1:count;
		}

		@Override
		public Integer getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
            	convertView = newView(parent);
            }
            bindView(position, convertView);
			return convertView;
		}
		
		private View newView(ViewGroup parent) {
            View view = inflater.inflate(
			R.layout.box_line, parent, false);
			final int[] images = { R.id.monster01, R.id.monster02, R.id.monster03,
					R.id.monster04, R.id.monster05 };
			final int[] texts = { R.id.monsterlv01, R.id.monsterlv02,
					R.id.monsterlv03, R.id.monsterlv04, R.id.monsterlv05 };
			final int[] p297 = { R.id.plus297_01, R.id.plus297_02, R.id.plus297_03,
					R.id.plus297_04, R.id.plus297_05 };
			final int[] aws = { R.id.aws_count_01, R.id.aws_count_02,
					R.id.aws_count_03, R.id.aws_count_04, R.id.aws_count_05 };
			final int[] maws = {R.id.maws_count_01, R.id.maws_count_02,
					R.id.maws_count_03, R.id.maws_count_04, R.id.maws_count_05};
			ViewHolder holder = new ViewHolder();
            for(int i=0; i<5; ++i) {
            	holder.images[i] = (ImageView) view.findViewById(images[i]);
            	holder.lvs[i] = (TextView) view.findViewById(texts[i]);
            	holder.p297s[i] = (TextView) view.findViewById(p297[i]);
            	holder.awokens[i] = (ImageView) view.findViewById(aws[i]);
            	holder.mawokens[i] = (ImageView) view.findViewById(maws[i]);
            }
            view.setTag(holder);
			return view;
		}
		
		private void bindView(int position, View view) {
			ViewHolder holder = (ViewHolder)view.getTag();
			for(int i=0; i<5; ++i) {
				final MonsterInfo data = getData(position, i);
				ImageView image = holder.images[i];
				TextView lv = holder.lvs[i];
				TextView p297 = holder.p297s[i];
				ImageView awk = holder.awokens[i];
				ImageView mawk = holder.mawokens[i];
				
				if ( data == null ) {
					image.setVisibility(View.INVISIBLE);
					image.setImageResource(R.drawable.i000);
					lv.setText("");
					p297.setText("");
					awk.setVisibility(View.INVISIBLE);
					mawk.setVisibility(View.INVISIBLE);
				} else {
					if ( data.getNo() > 0 ) {
						File file = new File(mCtx.getFilesDir(), String.format("%di.png", data.getNo()));
						if ( file.exists() ) {
							image.setImageURI(Uri.fromFile(file));
						} else {
							image.setImageResource(R.drawable.i000);
						}
						image.setVisibility(View.VISIBLE);
						
						lv.setText("Lv." + data.getLv());
						String plus = "+" + (data.getEgg1()+data.getEgg2()+data.getEgg3());
						p297.setText(plus);
						if (data.isAwokenMax()) {
							if (data.getAwoken() > 0) {
								awk.setImageResource(R.drawable.aws_max);
								awk.setVisibility(View.VISIBLE);
							}
						} else {
							int aws_count = data.getAwoken();
							if (aws_count > awsRes.length) {
								aws_count = awsRes.length;
							}
							if (aws_count > 0) {
								awk.setImageResource(awsRes[aws_count - 1]);
								awk.setVisibility(View.VISIBLE);
							}
						}
						List<MoneyAwokenSkill> list = data.getPotentialAwokenList();
						int size = list.size();
						if ( size > 0 ) {
							if(size == 6) {
								mawk.setImageResource(R.drawable.maws_05);
							} else {
								mawk.setImageResource(R.drawable.maws_01 + size - 1);
							}
							mawk.setVisibility(View.VISIBLE);
						} else {
							mawk.setVisibility(View.INVISIBLE);
						}
						
					} else {
						image.setVisibility(View.VISIBLE);
						image.setImageResource(R.drawable.i000);
						lv.setText("");
						p297.setText("");
						awk.setVisibility(View.INVISIBLE);
						mawk.setVisibility(View.INVISIBLE);
					}
					image.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
    						if (canSelect() && data.getNo() > 0) {
    							addSelection(data.getIndex()+1);
    							updateTeam();
    						}
                        }
                    });
					
				}
			}
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.team_selector);

		mTeamID = getIntent().getIntExtra("teamID", -1);
		if (mTeamID < 0) {
			Toast.makeText(TeamSelectorActivity.this, "invalid id",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		HandlerThread ht = new HandlerThread("ht");
		ht.start();
		mNonUi = new Handler(ht.getLooper());
		
		mTeamName = (EditText) findViewById(R.id.et_team_name);

		mListView = (ListView) findViewById(android.R.id.list);
		mAdapter = new MonsterAdapter(TeamSelectorActivity.this);
		mListView.setAdapter(mAdapter);

		final int[] IMAGE_ID = { R.id.leader1, R.id.member1, R.id.member2,
				R.id.member3, R.id.member4, R.id.leader2 };
		for (int i = 0; i < IMAGE_ID.length; ++i) {
			ImageView imageView = (ImageView) findViewById(IMAGE_ID[i]);
			mImage.add(imageView);
			final int index = i;
			imageView.setOnLongClickListener(new OnLongClickListener() {
                
                @Override
                public boolean onLongClick(View v) {
                    if ( mSelector[index] != -1 ) {
                        Intent intent = new Intent(TeamSelectorActivity.this,
                                MonsterDetailActivity.class);
                        intent.putExtra("index", mSelector[index]-1);
                        startActivity(intent);
                    }
                    return true;
                }
            });
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mSelector[index] = -1;
					updateTeam();
				}
			});
		}
		
		Spinner spinner = (Spinner) findViewById(R.id.page);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeamSelectorActivity.this,R.layout.cm_spinner);
		final String[] teams = { "Page 1", "Page 2", "Page 3", "Page 4",
				"Page 5", "Page 6", "Page 7", "Page 8", "Page 9", "Page 10" };
		adapter.addAll(teams);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int index, long arg3) {
				mListView.setSelection(index * 6);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		Button filter = (Button) findViewById(R.id.filter);
		filter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( mFilterDialog == null ) {
					mFilterDialog = DialogUtil.getFilterDialog(TeamSelectorActivity.this,
							new IFilterCallback() {
								
								@Override
								public void onResult(final Map<String, Object> filter) {
									mNonUi.post(new Runnable() {
										public void run() {
											queryBox(filter);
										}
									});
								}
							});
				}
				mFilterDialog.show();
			}
		});
		
        Button btnSort = (Button) findViewById(R.id.btn_sort);
        btnSort.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                DialogUtil.getSortDialog(TeamSelectorActivity.this, new ISortCallback() {
                    
                    @Override
                    public void onResult(final Map<String, String> data) {
                        mNonUi.post(new Runnable() {
                            public void run() {
                                List<MonsterInfo> monsterData = new ArrayList<MonsterInfo>(mData);
                                mSortData = data;
                                sort(monsterData);
                                mAdapter.setData(monsterData);
                                
                                runOnUiThread(new Runnable() {
                                    
                                    @Override
                                    public void run() {
                                        mAdapter.notifyDataSetInvalidated();
                                    }
                                });
                            }
                        });

                    }
                }).show();
            }
        });

		Button done = (Button) findViewById(R.id.btn_team_edit);
		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences prefs = getSharedPreferences("team",
						Context.MODE_PRIVATE);
				Editor editor = prefs.edit();
				for (int i = 0; i < 6; ++i) {
					editor.putInt(getMemberKey(mTeamID, i), mSelector[i]);
				}
				String teamName = mTeamName.getText().toString();
				if (TextUtils.isEmpty(teamName)) {
					teamName = "Team " + (mTeamID + 1);
				}
				editor.putString(getTeamNameKey(mTeamID), teamName);
				editor.apply();
				ComboMasterApplication instance = ComboMasterApplication
						.getsInstance();
				instance.updateTeam(mTeamID);

				finish();
			}
		});
		reloadMonsterBox();
	}

	@Override
	protected void onResume() {
		loadTeamInfo();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		mNonUi.getLooper().quit();
		super.onDestroy();
	}

	private void loadTeamInfo() {
		SharedPreferences prefs = getSharedPreferences("team",
				Context.MODE_PRIVATE);
		String teamName = prefs.getString(getTeamNameKey(mTeamID), "Team "
				+ (mTeamID + 1));
		mTeamName.setText(teamName);
		for (int i = 0; i < 6; ++i) {
			mSelector[i] = prefs.getInt(getMemberKey(mTeamID, i), -1);
		}
		updateTeam();
	}

	private String getTeamNameKey(int teamId) {
		return "Team" + teamId + "_name";
	}

	private String getMemberKey(int teamId, int index) {
		return "Team" + teamId + "_" + index;
	}

	private void reloadMonsterBox() {
		mNonUi.post(new Runnable() {
			
			@Override
			public void run() {
			    
				queryBox(loadFilter());
			}
		});
	}
	
	private Map<String, Object> loadFilter() {
//        SharedPreferences sp = getSharedPreferences(FilterDialog.PREF_FILTER, Context.MODE_PRIVATE);
        Map<String, Object> filter = null;
//        if (sp.contains(FilterDialog.FILTER_RARE_FROM)) {
//            filter = new HashMap<String, Object>();
//            int from = sp.getInt(FilterDialog.FILTER_RARE_FROM, 1);
//            int end = sp.getInt(FilterDialog.FILTER_RARE_END, 1);
//            filter.put(FilterDialog.FILTER_RARE_FROM, from);
//            filter.put(FilterDialog.FILTER_RARE_END, end);
//            
//            List<Boolean> attrs = new ArrayList<Boolean>();
//            List<Boolean> types = new ArrayList<Boolean>();
//            for(int i=0; i<5; ++i) {
//                boolean checked = sp.getBoolean(FilterDialog.FILTER_ATTR+i, false);
//                attrs.add(checked);
//            }
//            for(int i=0; i<12; ++i) {
//                boolean checked = sp.getBoolean(FilterDialog.FILTER_TYPE+i, false);
//                types.add(checked);
//            }
//            filter.put(FilterDialog.FILTER_INCLUDE_ATTR2, sp.getBoolean(FilterDialog.FILTER_INCLUDE_ATTR2, false));
//            filter.put(FilterDialog.FILTER_ATTR, attrs);
//            filter.put(FilterDialog.FILTER_TYPE, types);
//        }
        return filter;
	}

    static final SparseArray<Integer> WEIGHT_PROP = new SparseArray<Integer>();
    static {
        WEIGHT_PROP.put(-1, 0);
        WEIGHT_PROP.put(0, 1);
        WEIGHT_PROP.put(1, 5);
        WEIGHT_PROP.put(3, 2);
        WEIGHT_PROP.put(4, 4);
        WEIGHT_PROP.put(5, 3);
    }
    
    private void sort(List<MonsterInfo> list) {
        sort(list, mSortData);
    }
    
    private void sort(List<MonsterInfo> list, Map<String, String> data) {
        final List<Integer> priority = new ArrayList<Integer>(5);
        final List<Integer> order = new ArrayList<Integer>(5);
        boolean skipSorting = true;
        if ( data == null ) {
            // load from shared pref
            SharedPreferences pref = getSharedPreferences(SortDialog.PREF_SORT, Context.MODE_PRIVATE);
            for(int i=0; i<5; ++i) {
                int prior = pref.getInt("priority"+i, 0);
                int asc = pref.getInt("order"+i, 1);
                priority.add(prior);
                order.add(asc);
                
                if ( i == 0 ) {
                    mPrimaryOrder = prior;
                }
                
                if (prior != 0) {
                    skipSorting = false;
                }
            }
        } else {
            for(int i=0; i<5; ++i) {
                String key = "priority"+i;
                int val = 0;
                if (data.containsKey(key)) {
                    val = Integer.parseInt(data.get(key));
                    priority.add(val);
                    if (val != 0) {
                        skipSorting = false;
                    }
                } else {
                    priority.add(0);
                }
                if ( i == 0 ) {
                    mPrimaryOrder = val;
                }
                
                String orderKey = "order"+i;
                if (data.containsKey(orderKey)) {
                    order.add(Integer.parseInt(data.get(orderKey)));
                } else {
                    order.add(0);
                }
            }
        }
        if (skipSorting) {
            return ;
        }
        Collections.sort(list, new Comparator<MonsterInfo>() {

            @Override
            public int compare(MonsterInfo lhs, MonsterInfo rhs) {
                int lno = lhs.getNo();
                int rno = rhs.getNo();
                
                if ( lno == -1 || rno == -1 ) {
                    if ( lno != -1 ) {
                        return -1;
                    } else if ( rno != -1 ) {
                        return 1;
                    }
                    return 0;
                }
                
                return sortByCondition(lhs, rhs, priority, order);
            }
            
        });
    }

    private int sortByCondition(MonsterInfo lhs, MonsterInfo rhs, List<Integer> priority, List<Integer> order) {
        int[][] asc = {{-1, 1},{1,-1}};
        int size = priority.size();
        for(int i=0; i<size; ++i) {
            int prior = priority.get(i);
            if ( prior == 0 ) {
                continue;
            }
            int isAsc = order.get(i);
            
            int lhsVal=0, rhsVal=0;
            switch(prior) {
            case 1:
                lhsVal = lhs.getAtk();
                rhsVal = rhs.getAtk();
                break;
            case 2:
                lhsVal = lhs.getHP();
                rhsVal = rhs.getHP();
                break;
            case 3:
                lhsVal = lhs.getRecovery();
                rhsVal = rhs.getRecovery();
                break;
            case 4:
                lhsVal = lhs.getLv();
                rhsVal = rhs.getLv();
                break;
            case 5:
                lhsVal = lhs.getCost();
                rhsVal = rhs.getCost();
                break;
            case 6:
                lhsVal = lhs.getRare();
                rhsVal = rhs.getRare();
                break;
            case 7:
                lhsVal = WEIGHT_PROP.get(lhs.getProps().first);
                rhsVal = WEIGHT_PROP.get(rhs.getProps().first);
                break;
            case 8:
                lhsVal = WEIGHT_PROP.get(lhs.getProps().second);
                rhsVal = WEIGHT_PROP.get(rhs.getProps().second);
                break;
            case 9:
                lhsVal = lhs.getMonsterTypes().first.ordinal();
                rhsVal = rhs.getMonsterTypes().first.ordinal();
                break;
            case 10:
                lhsVal = lhs.getMonsterTypes().second.ordinal();
                rhsVal = rhs.getMonsterTypes().second.ordinal();
                break;
            case 11:
                lhsVal = lhs.getEgg1()+lhs.getEgg2()+lhs.getEgg3();
                rhsVal = rhs.getEgg1()+rhs.getEgg2()+rhs.getEgg3();
                break;
            }
            
            if ( lhsVal == rhsVal ) {
                // continue to next one
                continue;
            } else if ( lhsVal < rhsVal ) {
                return asc[isAsc][0];
            } else {
                return asc[isAsc][1];
            }
        }
        // default sort by box index
        int lhsVal = lhs.getIndex();
        int rhsVal = rhs.getIndex();
        
        if ( lhsVal < rhsVal ) {
            return asc[1][0];
        } else if ( lhsVal > rhsVal ) {
            return asc[1][1];
        }
        return 0;
    }
	
	private void queryBox(Map<String, Object> filter) {
		if ( filter != null ) {			
			mData = LocalDBHelper.getMonsterBoxFilter(TeamSelectorActivity.this, filter);
            List<MonsterInfo> data = new ArrayList<MonsterInfo>(mData);
            sort(data);
			mAdapter.setData(data);
		} else {
			mData = LocalDBHelper.getMonsterBox(TeamSelectorActivity.this);
	        List<MonsterInfo> data = new ArrayList<MonsterInfo>(mData);
	        sort(data);
			mAdapter.setData(data);
		}
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if ( mData.size() == 0 ) {
					Toast.makeText(TeamSelectorActivity.this, getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
				}
				mAdapter.notifyDataSetInvalidated();
			}
		});
	}
	
	private void updateTeam() {
		for (int i = 0; i < 6; ++i) {
			int index = mSelector[i];
			if (index != -1) {
				int no = LocalDBHelper.getMonsterBoxNo(this, index - 1);
				mImage.get(i).setImageURI(
						Uri.fromFile(new File(getFilesDir(), no + "i.png")));
			} else {
				mImage.get(i).setImageResource(R.drawable.i000);
			}
		}
	}

	private int inSelection(int index) {
		for (int i = 0; i < 6; ++i) {
			if (mSelector[i] == index) {
				return i;
			}
		}
		return -1;
	}

	private boolean canSelect() {
		for (int i = 0; i < 6; ++i) {
			if (mSelector[i] == -1) {
				return true;
			}
		}
		return false;
	}

	private int addSelection(int index) {
		for (int i = 0; i < 6; ++i) {
			if (mSelector[i] == -1) {
				mSelector[i] = index;
				return i;
			}
		}
		return -1;
	}
}
