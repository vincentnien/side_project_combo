package com.a30corner.combomaster.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.ui.DialogUtil;
import com.a30corner.combomaster.activity.ui.FilterDialog;
import com.a30corner.combomaster.activity.ui.FilterDialog.IFilterCallback;
import com.a30corner.combomaster.activity.ui.SetLvDialog;
import com.a30corner.combomaster.provider.LocalDBHelper;
import com.a30corner.combomaster.provider.table.TableMonsterData;
import com.a30corner.combomaster.provider.vo.MonsterData;
import com.a30corner.combomaster.utils.BitmapUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class SearchMonsterActivity extends Activity {

    private ImageView mSelectedImage = null;
	private int mSelectedID = -1;
	private ListView mListView;
	private Dialog mFilterDialog = null;
	
	private MonsterAdapter mAdapter;
	
	private Handler mNonUi;
	private boolean needLv = false;
	private void queryMonster(Map<String, Object> filter) {
		List<Boolean> attr;// = (ArrayList<Boolean>) filter.get(FilterDialog.FILTER_ATTR);
		List<Boolean> type;// = (ArrayList<Boolean>) filter.get(FilterDialog.FILTER_TYPE);
		Integer rareFrom = 1;// = (Integer)filter.get(FilterDialog.FILTER_RARE_FROM);
		Integer rareEnd = 10;//(Integer)filter.get(FilterDialog.FILTER_RARE_END);
		boolean includeAttr = false;//(Boolean)filter.get(FilterDialog.FILTER_INCLUDE_ATTR2);
		
		// init
		if ( filter.containsKey(FilterDialog.FILTER_ATTR) ) {
			attr = (ArrayList<Boolean>) filter.get(FilterDialog.FILTER_ATTR);
		} else {
			attr = new ArrayList<Boolean>(0);
		}
		if ( filter.containsKey(FilterDialog.FILTER_TYPE) ) {
			type = (ArrayList<Boolean>) filter.get(FilterDialog.FILTER_TYPE);
		} else {
			type = new ArrayList<Boolean>(0);
		}
		if ( filter.containsKey(FilterDialog.FILTER_RARE_FROM) ) {
			rareFrom = (Integer)filter.get(FilterDialog.FILTER_RARE_FROM);
		}
		if ( filter.containsKey(FilterDialog.FILTER_RARE_END) ) {
			rareEnd = (Integer)filter.get(FilterDialog.FILTER_RARE_END);
		}
		if ( filter.containsKey(FilterDialog.FILTER_INCLUDE_ATTR2) ) {
			includeAttr = (Boolean)filter.get(FilterDialog.FILTER_INCLUDE_ATTR2);
		}
		
		// generate sql query string
		StringBuilder sb = new StringBuilder();
		int max = Math.max(rareFrom, rareEnd);
		int min = Math.min(rareFrom, rareEnd);
		List<String> args = new ArrayList<String>();
		int len;
		
		args.add(String.valueOf(min));
		args.add(String.valueOf(max));
		sb.append(TableMonsterData.Columns.RARE).append(">=? AND ").append(TableMonsterData.Columns.RARE).append("<=? ");
		
		List<Integer> attrFilter = new ArrayList<Integer>(5);
		len = attr.size();
		
		for(int i=0; i<len; ++i) {
			if ( attr.get(i) ) {
				if (i<2) {
					attrFilter.add(i);
				} else {
					attrFilter.add(i+1);
				}
			}
		}
		if ( attrFilter.size() > 0 ) {
			StringBuilder attrSb = new StringBuilder();
			attrSb.append(" IN ('").append(TextUtils.join("','", attrFilter)).append("')");
			
			String query = attrSb.toString();
			sb.append(" AND (").append(TableMonsterData.Columns.PROP1).append(query);
			if ( includeAttr ) {
				sb.append(" OR ").append(TableMonsterData.Columns.PROP2).append(query);
			}
			sb.append(")");
		}
		List<Integer> typeFilter = new ArrayList<Integer>();
		len = type.size();
		for(int i=0; i<len; ++i) {
			if ( type.get(i) ) {
				typeFilter.add(i);
			}
		}
		if ( typeFilter.size() > 0 ) {
			StringBuilder typeSb = new StringBuilder();
			typeSb.append(" IN ('").append(TextUtils.join("','", typeFilter)).append("')");
			
			String query = typeSb.toString();
			sb.append(" AND (").append(TableMonsterData.Columns.TYPE1).append(query)
				.append(" OR ").append(TableMonsterData.Columns.TYPE2).append(query)
				.append(" OR ").append(TableMonsterData.Columns.TYPE3).append(query)
				.append(")");
		}
		
		List<MonsterData> data = LocalDBHelper.queryMonsterData(SearchMonsterActivity.this,
				sb.toString(), args.toArray(new String[args.size()]), TableMonsterData.Columns.ID + " DESC");
		mAdapter.setData(data);
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				mAdapter.notifyDataSetInvalidated();
			}
		});
	}
	
	private int rare = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_search_monster);
		mListView = (ListView) findViewById(android.R.id.list);
		mAdapter = new MonsterAdapter(SearchMonsterActivity.this);
		mListView.setAdapter(mAdapter);
		
		rare = getIntent().getIntExtra("rare", 1);
		needLv = getIntent().getBooleanExtra("needLv", false);
		
		Button filter = (Button) findViewById(R.id.filter);
		filter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( mFilterDialog == null ) {
					mFilterDialog = DialogUtil.getFilterDialog(SearchMonsterActivity.this,
							new IFilterCallback() {
								
								@Override
								public void onResult(final Map<String, Object> filter) {
									mNonUi.post(new Runnable() {
										
										@Override
										public void run() {
											queryMonster(filter);
										}
									});
									
								}
							});
				}
				mFilterDialog.show();
			}
		});
		
		HandlerThread ht = new HandlerThread("nonUi");
		ht.start();
		mNonUi = new Handler(ht.getLooper());
		
		mNonUi.post(new Runnable() {
			
			@Override
			public void run() {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(FilterDialog.FILTER_RARE_FROM, rare);
				queryMonster(map);
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		mNonUi.getLooper().quit();
		super.onDestroy();
	}
	
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
                    R.layout.list_item_monster_list, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.images[0] = (ImageView) view.findViewById(R.id.monster01);
            holder.images[1] = (ImageView) view.findViewById(R.id.monster02);
            holder.images[2] = (ImageView) view.findViewById(R.id.monster03);
            holder.images[3] = (ImageView) view.findViewById(R.id.monster04);
            holder.images[4] = (ImageView) view.findViewById(R.id.monster05);
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
				} else {
					if ( data.id == mSelectedID ) {
						image.setColorFilter(0x55ffffff, PorterDuff.Mode.LIGHTEN );
					} else {
						image.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
					}
					
					File file = new File(mCtx.getFilesDir(), String.format("%di.png", data.id));
					if ( file.exists() ) {
					    ImageLoader.getInstance().displayImage(Scheme.FILE.wrap(file.getAbsolutePath()), holder.images[i]);
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
					image.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                            mSelectedID = data.id;
                            if ( mSelectedImage != null ) {
                                mSelectedImage.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                            }
                            mSelectedImage = (ImageView)v;
                            mSelectedImage.setColorFilter(0x55ffffff, PorterDuff.Mode.LIGHTEN );
                        }
                    });
					image.setVisibility(View.VISIBLE);
				}
			}
		}
		
	}
	
	private void saveImageAsync(final Bitmap bitmap, final int id) {
	    mNonUi.post(new Runnable() {
            
            @Override
            public void run() {
                BitmapUtil.saveBitmap(SearchMonsterActivity.this, bitmap, String.format("%di.png", id));
            }
        });
	}
	
	public void pressOK(View view) {
		Intent data = new Intent();
		if ( mSelectedID > 0 ) {
			boolean supportLv = false;
			if(supportLv && needLv) {
				// finish by myself
				DialogUtil.getNumberPickerDialog(this,
						R.string.lv, 1, 1, 99,
						new DialogUtil.ResultCallback() {

							@Override
							public void onResult(int value) {
								Intent data = new Intent();
								data.putExtra("id", mSelectedID);
								data.putExtra("lv", value);
								setResult(RESULT_OK, data);
								finish();
							}

							@Override
							public void onCancel() {
							}
						}).show();

				return ;
			} else {
				data.putExtra("id", mSelectedID);
				setResult(RESULT_OK, data);
			}
		} else {
			setResult(RESULT_CANCELED);
		}
		finish();		
	}
	
	public void pressCancel(View view) {
		setResult(RESULT_CANCELED);
		finish();
	}
	
}
