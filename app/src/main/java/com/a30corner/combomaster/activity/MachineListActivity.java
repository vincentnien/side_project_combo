package com.a30corner.combomaster.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.utils.BitmapUtil;
import com.a30corner.combomaster.utils.NetworkUtils;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MachineListActivity extends Activity {
    InterstitialAd intersistialAd;
    ListView listView;
    ProgressDialog dialog;
    Map<String, JSONObject> machineMaps = new HashMap<String, JSONObject>();
    Map<String, Boolean> downloadMaps = new HashMap<String, Boolean>();
    String globalkey = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, getString(R.string.need_network_1st), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        dialog = ProgressDialog.show(MachineListActivity.this, "", "Loading. Please wait...", true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setCancelable(true);

        setContentView(R.layout.machine_layout);
        //setTitle(R.string.select_egg_machine);

        listView = findViewById(android.R.id.list);

        loadEggMachines();

        ComboMasterApplication.getsInstance().putGaAction("egg", "draw", "enter");

        intersistialAd = new InterstitialAd(this);
        intersistialAd.setAdUnitId("ca-app-pub-9312207078900289/9414704423");

        intersistialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if(!TextUtils.isEmpty(globalkey)) {
                    startActivity(globalkey);
                }
            }
        });

        requestNewInterstitial();
    }
    public void start() {
        // 0 -> full
        if(ComboMasterApplication.getsInstance().getAdSceneType() == 0 && intersistialAd.isLoaded()) {
            intersistialAd.show();
        } else {
            startActivity(globalkey);
        }
    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("E4FF164BB1F3CA085C0CB1B708E50283")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        intersistialAd.loadAd(adRequest);
    }

    private void loadEggMachines() {
        Observable.just("http://188.166.227.62/rolling/list.php")
                .observeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return NetworkUtils.httpRequest(s);
                    }
                })
                .map(new Func1<String, JSONObject>() {
                    @Override
                    public JSONObject call(String s) {
                        try {
                            return new JSONObject(s);
                        } catch (JSONException e) {
                            return null;
                        }
                    }
                })
                .map(new Func1<JSONObject, ArrayAdapter>() {
                    @Override
                    public ArrayAdapter call(JSONObject jsonObject) {
                        if(jsonObject == null) {
                            return null;
                        }
                        Locale locale = Locale.getDefault();
                        String lang = locale.getLanguage();
                        if(!jsonObject.has(lang)) {
                            lang = "en";
                        } else if ("zh".equals(lang) && "cn".equalsIgnoreCase(locale.getCountry())) {
                            lang = "cn";
                        }
                        if (jsonObject.has(lang)) {
                            ArrayAdapter<Machine> adapter;
                            try {
                                JSONArray arr = jsonObject.getJSONArray(lang);
                                JSONArray key = jsonObject.getJSONArray("key");
                                int size = arr.length();
                                adapter = new ArrayAdapter<Machine>(MachineListActivity.this, R.layout.layout_stage, android.R.id.text1);
                                for (int i = 0; i < size; ++i) {
                                    String k = key.getString(i);
                                    adapter.add(new Machine(k, arr.getString(i)));
                                    if (jsonObject.has(k)) {
                                        JSONObject obj = jsonObject.getJSONObject(k);
                                        machineMaps.put(k, obj);

                                        JSONArray pet_id = obj.getJSONArray("pet_id");
                                        int len = pet_id.length();
                                        boolean downloaded = true;
                                        for(int x=0; x<len; ++x) {
                                            File f = new File(getFilesDir(), pet_id.getInt(x) + "i.png");
                                            if(!f.exists()) {
//                                                Log.e("Vincent", "not exists: " + f.getName());
                                                downloaded = false;
                                                break;
                                            }
                                        }
                                        JSONArray webp = obj.getJSONArray("images");
                                        len = webp.length();
                                        for(int x=0; x<len; ++x) {
                                            String url = "http://188.166.227.62/combomaster/images/" + k + "/" + webp.getString(x);
                                            if(!isWebpExists(url)) {
                                                downloaded = false;
                                                break;
                                            }
                                        }
                                        downloadMaps.put(k, downloaded);
                                    }
                                }


                            } catch (JSONException e) {
                                Log.e("ComboMaster", e.toString());
                                return null;
                            }
                            return adapter;
                        }

                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayAdapter>() {
                    @Override
                    public void call(final ArrayAdapter adapter) {
                        boolean failed = false;
                        dialog.dismiss();

                        if(adapter != null ) {
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    final Machine ma = (Machine) adapter.getItem(i);
                                    String key = ma.key;
                                    if(ma.key.contains("_")) {
                                        key = ma.key.split("_")[0];
                                    }
                                    if(downloadMaps.containsKey(key) && downloadMaps.get(key)) {
                                        globalkey = ma.key;
                                        start();
                                    } else {
                                        AlertDialog alertDialog = new AlertDialog.Builder(MachineListActivity.this)
                                                .setMessage(R.string.download_data)
                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        downloadEggMachineData(ma.key);
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                    }
                                                })
                                                .create();
                                        alertDialog.show();
                                    }
                                }
                            });
                        } else {
                            failed = true;
                        }
                        if (failed) {
                            Toast.makeText(MachineListActivity.this, "Load error, please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private boolean isWebpExists(String uri) {
        ImageRequest request = ImageRequest.fromUri(Uri.decode(uri));
        ImagePipeline pipeline = Fresco.getImagePipeline();

        return pipeline.isInDiskCacheSync(request);
    }

    private class PrefetchSubscriber extends BaseDataSubscriber<Void> {
        @Override
        protected void onNewResultImpl(DataSource<Void> dataSource) {
        }

        @Override
        protected void onFailureImpl(DataSource<Void> dataSource) {
        }

    }

    PrefetchSubscriber subscriber = new PrefetchSubscriber();

    private void downloadEggMachineData(String key) {
        if(!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.need_network_1st, Toast.LENGTH_LONG).show();
            return ;
        }
        String datakey = key;
        if(key.contains("_")) {
            datakey = key.split("_")[0];
        }
        JSONObject obj = machineMaps.get(datakey);

        List<Integer> petId = new ArrayList<Integer>();
        try {

            JSONArray webp = obj.getJSONArray("images");
            int size = webp.length();
            for(int i=0; i<size; ++i) {
                String url = "http://188.166.227.62/combomaster/images/" + datakey + "/" + webp.getString(i);
                downloadWebp(url);
            }

            JSONArray ids = obj.getJSONArray("pet_id");
            size = ids.length();
            for(int i=0; i<size; ++i) {
                int id = ids.getInt(i);
                File f = new File(getFilesDir(), id + "i.png");
                if(!f.exists()) {
                    petId.add(id);
                }
            }
            downloadImages(petId, key);
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void downloadWebp(String uri) {
        ImageRequest request = ImageRequest.fromUri(Uri.decode(uri));
        ImagePipeline pipeline = Fresco.getImagePipeline();

        if(pipeline.isInDiskCacheSync(request)) {
            return ;
        }
        DataSource<Void> ds = pipeline.prefetchToDiskCache(request, null);
        ds.subscribe(subscriber, UiThreadImmediateExecutorService.getInstance());
    }

    private class Machine {
        public String key;
        public String name;

        public Machine(String k, String n) {
            this.key = k;
            this.name = n;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private void startActivity(String key) {
        Intent intent = new Intent(MachineListActivity.this, EggMachineActivity.class);
        intent.putExtra("machine", key);
        startActivity(intent);
    }

    boolean canceled = false;
    private void downloadImages(List<Integer> list, final String key) {
        if(list.size() == 0) {
            globalkey = key;
            start();
            return ;
        }
        Set<Integer> set = new HashSet<Integer>(list);
        final int size = set.size();
        try {
            canceled = false;
            dialog = new ProgressDialog(this);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMessage(getString(R.string.downloading));
            dialog.setMax(size);
            dialog.setProgress(0);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    canceled = true;
                }
            });
            dialog.show();
        } catch(Exception e) {
        }

        final AtomicInteger counter = new AtomicInteger(size);
        for(Integer id : set) {
            final int imageId = id;
            String uri = "http://188.166.227.62/combomaster/images/" + id + "i.png";
            ImageLoader.getInstance().loadImage(uri, new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String arg0, View arg1) {
                }

                @Override
                public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                    Toast.makeText(MachineListActivity.this, R.string.need_network_1st, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onLoadingComplete(String url, View arg1, Bitmap bitmap) {
                    int progress = counter.decrementAndGet();
                    dialog.setProgress(size-progress);
                    if(progress == 0) {
                        downloadMaps.put(key, true);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                dialog.dismiss();

                                globalkey = key;
                                start();
                            }
                        }, 1000L);

                    }

                    saveImageAsync(bitmap, imageId);
                    if(canceled) {
                        return ;
                    }
                }

                @Override
                public void onLoadingCancelled(String arg0, View arg1) {
                }
            });
        }

    }

    private void saveImageAsync(Bitmap bitmap, final int id) {
        Observable.just(bitmap)
                .observeOn(Schedulers.immediate())
                .subscribe(new Action1<Bitmap>() {

                    @Override
                    public void call(Bitmap bitmap) {
                        BitmapUtil.saveBitmap(MachineListActivity.this, bitmap, String.format("%di.png", id));
                    }
                });
    }
}
