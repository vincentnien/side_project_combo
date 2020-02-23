package com.a30corner.combomaster.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.ui.DialogUtil;
import com.a30corner.combomaster.activity.ui.SortDialog;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill;
import com.a30corner.combomaster.provider.LocalDBHelper;
import com.a30corner.combomaster.utils.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class NewMonsterBoxActivity extends Activity {

    private static final String BOX_SORT_PREF = "box_sort_pref";
    private static final String BOX_FILTER_PREF = "box_filter_pref";

    private ListView mListView;

    private int index = -1;
    private int top;

    private List<MonsterInfo> mData;
    private Map<String, String> mSortData = null;

    private int mPrimaryOrder = 4;
    //private FilterDialog mFilterDialog = null;

    private MonsterAdapter mAdapter;
    final int[] awsRes = { R.drawable.aws_01,
            R.drawable.aws_02, R.drawable.aws_03,
            R.drawable.aws_04, R.drawable.aws_05,
            R.drawable.aws_06, R.drawable.aws_07,
            R.drawable.aws_08, R.drawable.aws_max };

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
            ImageView[] assits = new ImageView[5];
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
            final int[] assit = {R.id.assit_01, R.id.assit_02, R.id.assit_03,
                    R.id.assit_04, R.id.assit_05};
            MonsterAdapter.ViewHolder holder = new MonsterAdapter.ViewHolder();
            for(int i=0; i<5; ++i) {
                holder.images[i] = view.findViewById(images[i]);
                holder.lvs[i] = view.findViewById(texts[i]);
                holder.p297s[i] = view.findViewById(p297[i]);
                holder.awokens[i] = view.findViewById(aws[i]);
                holder.mawokens[i] = view.findViewById(maws[i]);
                holder.assits[i] = view.findViewById(assit[i]);
            }
            view.setTag(holder);
            return view;
        }

        private void bindView(int position, View view) {
            MonsterAdapter.ViewHolder holder = (MonsterAdapter.ViewHolder)view.getTag();
            for(int i=0; i<5; ++i) {
                final MonsterInfo data = getData(position, i);
                ImageView image = holder.images[i];
                TextView lv = holder.lvs[i];
                TextView p297 = holder.p297s[i];
                ImageView awk = holder.awokens[i];
                ImageView mawk = holder.mawokens[i];
                ImageView assit = holder.assits[i];

                if ( data == null ) {
                    image.setVisibility(View.INVISIBLE);
                    image.setImageResource(R.drawable.i000);
                    lv.setText("");
                    p297.setText("");
                    awk.setVisibility(View.INVISIBLE);
                    mawk.setVisibility(View.INVISIBLE);
                    assit.setVisibility(View.INVISIBLE);
                } else {
                    if ( data.getNo() > 0 ) {
                        File file = new File(mCtx.getFilesDir(), String.format("%di.png", data.getNo()));
                        if ( file.exists() ) {
                            image.setImageURI(Uri.fromFile(file));
                        } else {
                            image.setImageResource(R.drawable.i000);
                        }
                        image.setVisibility(View.VISIBLE);

                        switch(mPrimaryOrder) {
                            case 1:
                                lv.setText(""+data.getAtk());
                                break;
                            case 2:
                                lv.setText(""+data.getHP());
                                break;
                            case 3:
                                lv.setText(""+data.getRecovery());
                                break;
                            case 5:
                                lv.setText(""+data.getCost());
                                break;
                            case 6:
                                lv.setText(""+data.getRare());
                                break;
                            default:
                                lv.setText("Lv." + data.getLv());
                                break;

                        }

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
                        List<MonsterSkill.MoneyAwokenSkill> list = data.getPotentialAwokenList();
                        int size = list.size();
                        if ( size > 0 ) {
                            if(size >= 5) {
                                size = 5;
                            }
                            mawk.setImageResource(R.drawable.maws_01+size-1);
                            mawk.setVisibility(View.VISIBLE);
                        } else {
                            mawk.setVisibility(View.INVISIBLE);
                        }
                        if(data.getAssistantNo() > 0) {
                            File f2 = new File(mCtx.getFilesDir(), String.format("%di.png", data.getAssistantNo()));
                            if ( f2.exists() ) {
                                assit.setImageURI(Uri.fromFile(f2));
                            } else {
                                assit.setImageResource(R.drawable.i000);
                            }
                            assit.setVisibility(View.VISIBLE);
                        }

                        image.setOnLongClickListener(new View.OnLongClickListener() {

                            @Override
                            public boolean onLongClick(View v) {
                                new AlertDialog.Builder(NewMonsterBoxActivity.this)
                                        .setMessage(R.string.str_confirm_delete)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                LocalDBHelper.removeMonsterFromBox(NewMonsterBoxActivity.this, data.getIndex());
                                                reloadMonsterBox();
                                            }})
                                        .setNegativeButton(android.R.string.no, null).show();
                                return false;
                            }
                        });
                    } else {
                        image.setImageResource(R.drawable.i000);
                        lv.setText("");
                        p297.setText("");
                        awk.setVisibility(View.INVISIBLE);
                        mawk.setVisibility(View.INVISIBLE);
                        assit.setVisibility(View.INVISIBLE);
                    }
                    image.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            saveTopLocation();

                            Intent intent = new Intent(NewMonsterBoxActivity.this,
                                    MonsterDetailActivity.class);
                            intent.putExtra("index", data.getIndex());
                            startActivity(intent);
                        }
                    });

                }
            }
        }

    }

    private void saveTopLocation() {
        // save index and top position
        index = mListView.getFirstVisiblePosition();
        View v = mListView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - mListView.getPaddingTop());
    }

    private void restoreTopLocation() {
        if ( index != -1 ) {
            mListView.setSelectionFromTop(index, top);
        }
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
            SharedPreferences pref = getSharedPreferences(BOX_SORT_PREF, Context.MODE_PRIVATE);
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
        Activity activity = NewMonsterBoxActivity.this;
        if ( filter != null ) {
            mData = LocalDBHelper.getMonsterBoxFilter(activity, filter);
            List<MonsterInfo> data = new ArrayList<MonsterInfo>(mData);
            sort(data);
            mAdapter.setData(data);
        } else {
            mData = LocalDBHelper.getMonsterBox(activity);
            List<MonsterInfo> data = new ArrayList<MonsterInfo>(mData);
            sort(data);
            mAdapter.setData(data);
        }

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if ( mData.size() == 0 ) {
                    Toast.makeText(NewMonsterBoxActivity.this, getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
                }
                mAdapter.notifyDataSetInvalidated();
                restoreTopLocation();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initViews();
    }

    private void initViews() {
        final Activity activity = NewMonsterBoxActivity.this;
        //View view = inflate(R.layout.layout_monster_box, null);
        setContentView(R.layout.layout_monster_box);
        mListView = findViewById(android.R.id.list);
        mAdapter = new MonsterAdapter(activity);
        mListView.setAdapter(mAdapter);

        Spinner spinner = findViewById(R.id.page);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewMonsterBoxActivity.this,
                R.layout.cm_spinner);
        final String[] teams = { "Page 1", "Page 2", "Page 3", "Page 4",
                "Page 5", "Page 6", "Page 7", "Page 8", "Page 9", "Page 10",
                "Page 11", "Page 12", "Page 13", "Page 14", "Page 15",
                "Page 16", "Page 17", "Page 18", "Page 19", "Page 20"};
        adapter.addAll(teams);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int index, long arg3) {
                mListView.setSelection(index * 6);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        Button btnSort = findViewById(R.id.btn_sort);
        btnSort.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SortDialog dialog = DialogUtil.getSortDialog(activity, BOX_SORT_PREF, new SortDialog.ISortCallback() {

                    @Override
                    public void onResult(final Map<String, String> data) {
                        runOnNonUiThread(new Runnable() {
                            public void run() {
                                List<MonsterInfo> monsterData = new ArrayList<MonsterInfo>(mData);
                                mSortData = data;
                                sort(monsterData);
                                mAdapter.setData(monsterData);


                                activity.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        mAdapter.notifyDataSetInvalidated();
                                    }
                                });
                            }
                        });

                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        reloadMonsterBox();
    }

    private void reloadMonsterBox() {
        runOnNonUiThread(new Runnable() {

            @Override
            public void run() {
                queryBox(null);
            }
        });

    }

    public void runOnNonUiThread(Runnable runnable) {
        Observable.just(runnable)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Runnable>() {
                    @Override
                    public void call(Runnable runnable) {
                        if(runnable != null) {
                            runnable.run();
                        }
                    }
                });
    }
}
