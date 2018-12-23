package com.a30corner.combomaster.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.ui.DialogUtil;
import com.a30corner.combomaster.activity.ui.FilterDialog;
import com.a30corner.combomaster.activity.ui.FilterDialog.IFilterCallback;
import com.a30corner.combomaster.activity.ui.SortDialog;
import com.a30corner.combomaster.provider.LocalDBHelper;
import com.a30corner.combomaster.provider.vo.MonsterData;
import com.a30corner.combomaster.utils.BitmapUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class QuickTeamSetupActivity extends Activity {
	private List<ImageView> mImage = new ArrayList<ImageView>();

	private int[] mSelector = { -1, -1, -1, -1, -1, -1 };

	private ListView mListView;

	private FilterDialog mFilterDialog = null;
	private Handler mNonUi;
	
	private MonsterAdapter mAdapter;
	
    private List<MonsterData> mData;
    private Map<String, String> mSortData = null;
	
	private class MonsterAdapter extends BaseAdapter {

		private List<MonsterData> mData = new ArrayList<MonsterData>();
		private Context mCtx;
		private LayoutInflater inflater;
		
		private class ViewHolder {
			ImageView[] images = new ImageView[5];
		}
		
		public MonsterAdapter(Context context) {
			mCtx = context;
			inflater = LayoutInflater.from(context);
		}
		
		public void setData(List<MonsterData> data) {
			mData.clear();
			mData.addAll(data);
		}
		
		private MonsterData getData(int position, int offset) {
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
            ViewHolder holder = new ViewHolder();
            for(int i=0; i<5; ++i) {
            	holder.images[i] = (ImageView) view.findViewById(images[i]);
            }
            view.setTag(holder);
			return view;
		}
		
		private void bindView(int position, View view) {
			ViewHolder holder = (ViewHolder)view.getTag();
			for(int i=0; i<5; ++i) {
				final MonsterData data = getData(position, i);
				ImageView image = holder.images[i];
				
				if ( data == null ) {
					image.setVisibility(View.INVISIBLE);
					image.setImageResource(R.drawable.i000);
				} else {
					if ( data.id > 0 ) {
						File file = new File(mCtx.getFilesDir(), String.format("%di.png", data.id));
						if ( file.exists() ) {
							image.setImageURI(Uri.fromFile(file));
						} else {
							image.setImageResource(R.drawable.i000);
							ImageLoader.getInstance().displayImage(data.url, holder.images[i], new ImageLoadingListener() {
	                            
	                            @Override
	                            public void onLoadingStarted(String arg0, View arg1) {
	                            }
	                            
	                            @Override
	                            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
	                            }
	                            
	                            @Override
	                            public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
	                                saveImageAsync(bitmap, data.id);
	                            }
	                            
	                            @Override
	                            public void onLoadingCancelled(String arg0, View arg1) {
	                            }
	                        });
						}
						image.setVisibility(View.VISIBLE);
						
					} else {
						image.setVisibility(View.VISIBLE);
						image.setImageResource(R.drawable.i000);
					}
					image.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                        	if(canSelect()) {
                        		addSelection(data.id);
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
		
		setContentView(R.layout.quick_team_selector);
		
		HandlerThread ht = new HandlerThread("ht");
		ht.start();
		mNonUi = new Handler(ht.getLooper());

		mListView = (ListView) findViewById(android.R.id.list);
		mAdapter = new MonsterAdapter(QuickTeamSetupActivity.this);
		mListView.setAdapter(mAdapter);

		final int[] IMAGE_ID = { R.id.leader1, R.id.member1, R.id.member2,
				R.id.member3, R.id.member4, R.id.leader2 };
		for (int i = 0; i < IMAGE_ID.length; ++i) {
			ImageView imageView = (ImageView) findViewById(IMAGE_ID[i]);
			mImage.add(imageView);
			final int index = i;
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mSelector[index] = -1;
					updateTeam();
				}
			});
		}

		Button filter = (Button) findViewById(R.id.filter);
		filter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( mFilterDialog == null ) {
					mFilterDialog = DialogUtil.getFilterDialog(QuickTeamSetupActivity.this,
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

		Button done = (Button) findViewById(R.id.btn_team_edit);
		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(QuickTeamSetupActivity.this, TeamAdjustActivity.class);
				intent.putExtra("team", mSelector);
				startActivityForResult(intent, 0);
			}
		});
		reloadMonsterBox();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		mNonUi.getLooper().quit();
		super.onDestroy();
	}
	
	private boolean canSelect() {
		for (int i = 0; i < 6; ++i) {
			if (mSelector[i] == -1) {
				return true;
			}
		}
		return false;
	}

	private int addSelection(int no) {
		for (int i = 0; i < 6; ++i) {
			if (mSelector[i] == -1) {
				mSelector[i] = no;
				return i;
			}
		}
		return -1;
	}

	private void updateTeam() {
		for (int i = 0; i < 6; ++i) {
			int no = mSelector[i];
			if (no != -1) {
				mImage.get(i).setImageURI(
						Uri.fromFile(new File(getFilesDir(), no + "i.png")));
			} else {
				mImage.get(i).setImageResource(R.drawable.i000);
			}
		}
	}
	
	private void saveImageAsync(final Bitmap bitmap, final int id) {
	    mNonUi.post(new Runnable() {
            
            @Override
            public void run() {
                BitmapUtil.saveBitmap(QuickTeamSetupActivity.this, bitmap, String.format("%di.png", id));
            }
        });
	}
	
	private void reloadMonsterBox() {
		mNonUi.post(new Runnable() {
			
			@Override
			public void run() {
			    
				queryBox(loadFilter());
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			finish();
		}
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
    
    private void sort(List<MonsterData> list) {
        sort(list, mSortData);
    }
    
    private void sort(List<MonsterData> list, Map<String, String> data) {
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
        Collections.sort(list, new Comparator<MonsterData>() {

            @Override
            public int compare(MonsterData lhs, MonsterData rhs) {
                int lno = lhs.id;
                int rno = rhs.id;
                
                if ( lno == -1 || rno == -1 ) {
                    if ( lno != -1 ) {
                        return -1;
                    } else if ( rno != -1 ) {
                        return 1;
                    }
                    return 0;
                }
                
                return 0;
            }
            
        });
    }

	
	private void queryBox(Map<String, Object> filter) {
		if ( filter != null ) {			
			mData = LocalDBHelper.getMonsterList(QuickTeamSetupActivity.this, filter);
            List<MonsterData> data = new ArrayList<MonsterData>(mData);
            sort(data);
			mAdapter.setData(data);
		} else {
			mData = LocalDBHelper.getMonsterList(QuickTeamSetupActivity.this, new HashMap<String, Object>());
	        List<MonsterData> data = new ArrayList<MonsterData>(mData);
	        sort(data);
			mAdapter.setData(data);
		}
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if ( mData.size() == 0 ) {
					Toast.makeText(QuickTeamSetupActivity.this, getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
				}
				mAdapter.notifyDataSetInvalidated();
			}
		});
	}


}
