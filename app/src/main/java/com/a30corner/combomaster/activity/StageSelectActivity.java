package com.a30corner.combomaster.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.ui.DialogUtil;
import com.a30corner.combomaster.activity.ui.DialogUtil.ITeam2SelectCallback;
import com.a30corner.combomaster.activity.ui.DialogUtil.ITeamSelectCallback;
import com.a30corner.combomaster.playground.stage.EnemyData;
import com.a30corner.combomaster.playground.stage.StageData;
import com.a30corner.combomaster.utils.BitmapUtil;
import com.a30corner.combomaster.utils.Constants;
import com.a30corner.combomaster.utils.NetworkUtils;
import com.a30corner.combomaster.utils.RandomUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class StageSelectActivity extends Activity {

	InterstitialAd intersistialAd;
	ListView stageListView;
	
	ProgressDialog dialog;
	boolean isCop = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stage_layout);
		setTitle(R.string.select_stage);
		
		stageListView = findViewById(android.R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.layout_stage, android.R.id.text1);
		adapter.addAll(getResources().getStringArray(R.array.stage_list));
		
		int mode = getIntent().getIntExtra("simulator", 1);
		isCop = (mode == 2);
		
		stageListView.setAdapter(adapter);
		stageListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				String stage = getResources().getStringArray(R.array.value_list)[position];
				List<Integer> imageList = checkImages(stage);
				
				if("machinezeus".equals(stage)) {
					ComboMasterApplication.getsInstance().setDropOrbs(2);
				} else {
					ComboMasterApplication.getsInstance().setDropOrbs();
				}
				
				if(imageList.size() == 0) { // all images downloaded
					startTeamSelection(stage);
				} else {
					downloadImages(imageList, stage);
				}

			}
		});
		
		intersistialAd = new InterstitialAd(this);
		intersistialAd.setAdUnitId("ca-app-pub-9312207078900289/9414704423");
		
		intersistialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                gameStart();
            }
        });
		
		requestNewInterstitial();
	}
	
	boolean canceled = false;
	private void downloadImages(List<Integer> list, final String stage) {
		if(!NetworkUtils.isNetworkAvailable(this)) {
			Toast.makeText(this, R.string.need_network_1st, Toast.LENGTH_LONG).show();
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
			dialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					canceled = true;
				}
			});
			dialog.show();
		} catch(Exception e) {
			Toast.makeText(StageSelectActivity.this, e.toString(), Toast.LENGTH_LONG).show();
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
					Toast.makeText(StageSelectActivity.this, R.string.need_network_1st, Toast.LENGTH_LONG).show();
				}
				
				@Override
				public void onLoadingComplete(String url, View arg1, Bitmap bitmap) {
					int progress = counter.decrementAndGet();
					dialog.setProgress(size-progress);
					if(progress == 0) {
						dialog.dismiss();
						startTeamSelection(stage);
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
					BitmapUtil.saveBitmap(StageSelectActivity.this, bitmap, String.format("%di.png", id));
				}
			});
	}
	
	private void startTeamSelection(final String stage) {
		if(!isCop) {
			try {
				DialogUtil.getTeamSelectDialog(StageSelectActivity.this,
						new ITeamSelectCallback() {

							@Override
							public void onSelect(int index, Map<String, Object> data) {
								ComboMasterApplication instance = ComboMasterApplication.getsInstance();
								instance.setTargetTeam(index);
								instance.setStage(stage);
								instance.setCopMode(false);
								int type = 0; // full
								if (RandomUtil.getLuck(50)) {
									type = 1;
								}
								instance.setAdSceneType(type);

								start();
							}

							@Override
							public void onCancel() {
							}
						}, true).show();
			} catch (Throwable e) {
				ComboMasterApplication instance = ComboMasterApplication.getsInstance();
				//instance.setTargetTeam(index);
				instance.setStage(stage);
				instance.setCopMode(false);
				int type = 0; // full
				if (RandomUtil.getLuck(50)) {
					type = 1;
				}
				instance.setAdSceneType(type);

				start();
			}
		} else {
			try {
				DialogUtil.getTeam2SelectDialog(StageSelectActivity.this,
						new ITeam2SelectCallback() {

							@Override
							public void onSelect(int index, int index2, Map<String, Object> data) {
								ComboMasterApplication instance = ComboMasterApplication.getsInstance();
								instance.setTargetTeam(index);
								instance.setTargetTeam2(index2);
								instance.setStage(stage);
								instance.setGameMode(Constants.MODE_MULTIPLE);
								instance.setCopMode(true);
								int type = 0; // full
								if (RandomUtil.getLuck(50)) {
									type = 1;
								}
								instance.setAdSceneType(type);

								start();
							}

							@Override
							public void onCancel() {
							}
						}).show();
			} catch (Throwable e) {
				ComboMasterApplication instance = ComboMasterApplication.getsInstance();
				instance.setStage(stage);
				instance.setGameMode(Constants.MODE_MULTIPLE);
				instance.setCopMode(true);
				int type = 0; // full
				if (RandomUtil.getLuck(50)) {
					type = 1;
				}
				instance.setAdSceneType(type);

				start();
			}
		}
	}
	
	private List<Integer> checkImages(String stage) {
		List<Integer> imageList = new ArrayList<Integer>();
		if(stage.length() > 2) {
			List<Integer> data;
			if("tuesday".equals(stage)) {
				data = Arrays.asList(147,148,149,150,151,321,1176);
			} else if("devilsuper".equals(stage) || "devilnormal".equals(stage)) {
				data = Arrays.asList(1062,895,1324,1211,1214,1510,647,1371,1849);
			} else if("challenge32_9".equals(stage)) {
				data = Arrays.asList(1957,2740,2198,1470,918,2277);
			} else if("friday".equals(stage)) {
				data = Arrays.asList(155,156,157,158,160,246,247,248,249,250,251,1002,915,916);
			} else if("28_10".equals(stage)) {
				data = Arrays.asList(2298, 1954, 1747, 2078);
			} else if("snow_river".equals(stage)) {
				data = Arrays.asList(148,247,251,758,234,1229,1602,2277);
			} else if("hanuman".equals(stage)) {
				data = Arrays.asList(2838);
			} else if("zeus297".equals(stage)) {
				data = Arrays.asList(262, 264,266,174,175,234,192,194,196,263,265,267,187);
			} else if("hera297".equals(stage)) {
				data = Arrays.asList(98,100,102,104,108,191,192,193,194,195,196,197,198,199,200,1928,1533,1535,1534,1748,189);
			} else if("machinezeus".equals(stage)) {
				data = Arrays.asList(319,320,2582,2584,2586,2588,2590,150,151,2195,2196,2197,2198,2199,1206,1207,2335,2338,2127,2529,2532,2189,2528);
			} else if("machinehera".equals(stage)) {
				data = Arrays.asList(85,87,2195,2196,2197,2198,2199,897,899,901,903,905,2186,1272,1273,1274,2334,2337,1206,1207,2530,2531,2128,2526);
			} else if("monday".equals(stage)) {
				data = Arrays.asList(310,311,312,313,314,315,1272,1273,1274,246,247,248,249,250,2414);
			} else if("dragondestroy".equals(stage)) {
				data = Arrays.asList(253,256,682,1225,1227,1472,1631,2092,2277,1846,1847);
			} else if("dragonhell".equals(stage)) {
				data = Arrays.asList(253,256,682,1225,1227,1472,1631,2092,2277,1215);
			} else if("superfight".equals(stage)) {
				data = Arrays.asList(2716, 2717, 2718, 2719, 2720);
			} else if("challenge34_10".equals(stage)) {
				data = Arrays.asList(2127, 2294, 2296, 2298, 2528);
			} else if("machineathena".equals(stage)) {
				data = Arrays.asList(82, 1458, 2187, 2195, 2196, 2199, 2298, 2529, 2531, 2652, 2653, 2655, 2741, 2987, 3074);
			} else if("zeusdragon".equals(stage)) {
				data = Arrays.asList(2719, 2748, 2749);
			} else if("heradragon".equals(stage)) {
				data = Arrays.asList(825, 393, 2720);
			} else if ("201806_10".equals(stage)) {
				data = Arrays.asList(3541, 761, 1105, 2008, 1190, 2742, 2391);
			} else if("sopdet".equals(stage)) {
				data = Arrays.asList(1463, 1462);
			} else if ("extremepractice".equals(stage)) {
				data = Arrays.asList(46, 48, 50, 52, 54, 414, 417, 428, 4244, 3318, 3319, 3320, 3321, 3322, 3323, 3324, 3325, 3700, 3701, 3702, 3703, 694, 973, 983, 1271, 1517, 4011, 922, 475, 476, 477, 478, 479, 3877, 4995, 3638, 3640, 3642, 3311);
			} else if ("uralien".equals(stage)) {
				data = Arrays.asList(161, 162, 163, 164, 165,3318,3319, 3320, 3321, 3322, 3323, 3324, 3325,2551,3259,3208,3838,363,365,1108,2989,3017,3251,2896,3457,3327,2206,2524,1920,1921,2393,2395,3074,4417,1338,4358,908,1514,2560,3200,3903,4013,3638,3640,3642,4585,4742,2098,1885,5175,5177,5179,5181,5183,5297,3891,1547,1548,1549,1550,1551);
			} else if("ultimate".equals(stage)){
				data = new ArrayList<Integer>();
				data.add(812);
				data.add(814);
				data.add(1307);
				data.add(1945);
				data.add(823);
				data.add(826);
				data.add(1258);
				data.add(1062);
				for(int i=0; i<5; ++i) {
					data.add(1526+i);
					data.add(475+i);
					data.add(1210+i);
					data.add(1538+2*i);
					data.add(746+2*i);
					data.add(1547+i);
				}
				for(int i=0; i<3; ++i) {
					data.add(1228+i);
				}
				data.add(1466);
				data.add(1468);
				data.add(1469);
				data.add(1223);
				data.add(1425);
				data.add(1590);
				data.add(1711);
				data.add(2104);
				data.add(1648);
				for(int i=0; i<3; ++i) {
					data.add(1533+i);
				}
				data.add(1748);
				for(int i=0; i<4; ++i) {
					data.add(889+i);
				}

				data.add(1342);				
				data.add(1343);
				data.add(1645);
				data.add(914);
				data.add(1089);
				data.add(1324);
				data.add(1663);
				data.add(1463);
				data.add(1465);
				data.add(1837);
				data.add(2008);
				
				data.add(1092);
				data.add(1095);
				data.add(757);
				
				data.add(1928);
				data.add(1726);
				data.add(1954);
				data.add(1956);
				data.add(1837);
				data.add(2075);
				
				data.add(1849);
				data.add(651);
				data.add(917);
				data.add(918);
				data.add(1252);
				data.add(1532);
				
				data.add(1215);
				data.add(1472);
				data.add(1631);
				data.add(2092);
				
				data.add(1760);
				data.add(985);
				data.add(1536);
				
				data.add(1737);
				data.add(1422);
				data.add(1371);
				
				data.add(1837);
				
				data.add(1747);
				data.add(2078);
			} else if("ultimate2".equals(stage)) {
				data = new ArrayList<Integer>();
				for (int i = 0; i < 5; ++i) {
					data.add(2305 + 2 * i);
				}
				data.add(2549);
				data.add(812);
				data.add(814);
				data.add(1307);
				data.add(1945);
				data.add(823);
				data.add(826);
				data.add(1258);
				data.add(1062);
				for (int i = 0; i < 5; ++i) {
					data.add(1526 + i);
					data.add(475 + i);
					data.add(1210 + i);
					data.add(1538 + 2 * i);
					data.add(746 + 2 * i);
					data.add(1547 + i);
				}
				for (int i = 0; i < 3; ++i) {
					data.add(1228 + i);
				}
				data.add(1466);
				data.add(1468);
				data.add(1469);
				data.add(1223);
				data.add(1425);
				data.add(1590);
				data.add(1711);
				data.add(2104);
				data.add(2182);
				data.add(2184);
				data.add(2320);
				data.add(2398);
				data.add(2400);
				data.add(2402);
				for (int i = 0; i < 6; ++i) {
					data.add(2333 + i);
				}
				for (int i = 0; i < 5; ++i) {
					data.add(2299 + i);
				}
				data.add(1922);
				data.add(1648);
				for (int i = 0; i < 3; ++i) {
					data.add(1533 + i);
				}
				data.add(1748);
				for (int i = 0; i < 4; ++i) {
					data.add(889 + i);
				}

				data.add(1342);
				data.add(1343);
				data.add(1645);
				data.add(914);
				data.add(1089);
				data.add(1324);
				data.add(1663);
				data.add(1463);
				data.add(1465);
				data.add(1837);
				data.add(2008);

				data.add(1092);
				data.add(1095);
				data.add(757);

				data.add(1928);
				data.add(1726);
				data.add(1954);
				data.add(1956);
				data.add(1837);
				data.add(2075);

				data.add(1849);
				data.add(651);
				data.add(917);
				data.add(918);
				data.add(1252);
				data.add(1532);

				data.add(1215);
				data.add(1472);
				data.add(1631);
				data.add(2092);
				data.add(2277);

				data.add(1760);
				data.add(985);
				data.add(760);
				data.add(1955);
				data.add(1536);

				data.add(1737);
				data.add(1422);
				data.add(1371);

				data.add(1837);

				data.add(1747);
				data.add(2078);
				data.add(2081);
			} else if("ura".equals(stage)) {
				data = Arrays.asList(1526, 1527, 1528, 1529, 1530, 3721, 3722, 3723, 3724, 3725, 2739, 2740, 2741, 2742, 2743,
						3741, 3742, 3743, 3744, 3745, 234, 3317, 3318, 3319, 3320, 3321, 3322, 3323, 3324, 3325, 3000, 3001, 3002, 3726, 3727, 3728, 3729, 3730,
						1206, 1207, 310, 311, 312, 313, 314, 315, 1294, 1295, 1463, 1465, 1837, 2008, 2738, 2180, 2664, 2006, 2391,
						2939, 2940, 2941, 1955, 1726, 1956, 1954, 3829, 3832, 3881, 2092, 2277, 2754, 3013, 3245,
						985, 1536, 1737, 2807, 2946, 1217, 1644, 1547, 1548, 1549, 1550, 1551, 3891, 1747, 2078, 1458, 1602, 2130
				);
			} else if ("lvultimate".equals(stage)) {
				data = Arrays.asList(152,153,154,3483,3507,155,156,157,158,159,246,247,248,249,250,4638,4640,4642,4644,4646,4273,4264,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,3241,3243,797,3827,3487,3488,3489,3490,3491,4411,4413,4415,4648,4650,3700,3701,3702,3703);
			} else if ("ultimate5".equals(stage)) {
                data = Arrays.asList(2393, 2395, 1920, 1921, 2206, 2524, 3457, 3327, 3251, 2896, 2989, 3017, 161, 162, 163, 164, 165, 2551, 3259, 3208, 3838, 363, 365, 1108, 3318, 3319, 3320, 332, 2395, 3074, 4417, 1338, 4358, 908, 1514, 3318, 3319, 3320, 3321, 3322, 3323, 3324, 3325, 2560, 3200, 3903, 4013, 3638, 3640, 3642, 4585, 4742, 1547, 1548, 1549, 1550, 1551);
            } else if ("7th_lv30".equals(stage)) {
				data = Arrays.asList(4834, 1726, 2749, 1668, 2081, 2983);
			} else if ("quest_201901_lv10".equals(stage)) {
				data = Arrays.asList(761, 189, 2294, 3251, 4744);
			} else if ("quest_201903_lv10".equals(stage)) {
				data = Arrays.asList(2239, 1726, 2130, 2191, 2641, 2989, 2809);
			} else if ("quest_201901_lv9".equals(stage)) {
				data = Collections.singletonList(2718);
			} else if("ultimate3".equals(stage)){
				data = new ArrayList<Integer>();
				for(int i=0; i<5; ++i) {
					data.add(2305+2*i);
				}
				data.add(2549);
				data.add(812);
				data.add(814);
				data.add(1307);
				data.add(1945);
				data.add(823);
				data.add(826);
				data.add(1258);
				data.add(1062);
				for(int i=0; i<5; ++i) {
					data.add(1526+i);
					data.add(475+i);
					data.add(1210+i);
					data.add(1538+2*i);
					data.add(746+2*i);
					data.add(1547+i);
				}
				for(int i=0; i<3; ++i) {
					data.add(1228+i);
				}
				data.add(1466);
				data.add(1468);
				data.add(1469);
				data.add(1223);
				data.add(1425);
				data.add(1590);
				data.add(1711);
				data.add(2104);
				data.add(2182);
				data.add(2184);
				data.add(2320);
				data.add(2398);
				data.add(2400);
				data.add(2402);
				for(int i=0; i<6; ++i) {
					data.add(2333+i);
				}
				for(int i=0; i<5; ++i) {
					data.add(2299+i);
				}
				data.add(1922);
				data.add(1648);
				for(int i=0; i<3; ++i) {
					data.add(1533+i);
				}
				data.add(1748);
				for(int i=0; i<4; ++i) {
					data.add(889+i);
				}

				data.add(1342);
				data.add(1343);
				data.add(1645);
				data.add(914);
				data.add(1089);
				data.add(1324);
				data.add(1663);
				data.add(1463);
				data.add(1465);
				data.add(1837);
				data.add(2008);

				data.add(1092);
				data.add(1095);
				data.add(757);

				data.add(1928);
				data.add(1726);
				data.add(1954);
				data.add(1956);
				data.add(1837);
				data.add(2075);

				data.add(1849);
				data.add(651);
				data.add(917);
				data.add(918);
				data.add(1252);
				data.add(1532);

				data.add(1215);
				data.add(1472);
				data.add(1631);
				data.add(2092);
				data.add(2277);

				data.add(1760);
				data.add(985);
				data.add(760);
				data.add(1955);
				data.add(1536);

				data.add(1737);
				data.add(1422);
				data.add(1371);

				data.add(1837);

				data.add(1747);
				data.add(2078);
				data.add(2081);

				for(int i=0; i<5; ++i) {
					data.add(2716+i);
					data.add(2723+i);
				}
				for(int i=0; i<9; ++i) {
					data.add(3317+i);
				}
			} else {
				data = Arrays.asList(152,227,153,154,1002,1085,1086,1087);
			}
			
			for(Integer id : data) {
				File f = new File(getFilesDir(), id + "i.png");
				if(!f.exists()) {
					imageList.add(id);
				}
			}
			
		} else {
			StageData stageData = loadStage(stage);
			for(EnemyData ed : stageData.enemies) {
				File f = new File(getFilesDir(), ed.id + "i.png");
				if(!f.exists()) {
					imageList.add(ed.id);
				}
			}
		}
		return imageList;
	}
	
	private StageData loadStage(String stage) {
		Gson gson = new Gson();
		final int[] stageName = {R.string.stage, R.string.stage1};
		String json = getResources().getString(stageName[Integer.parseInt(stage)]);
		java.lang.reflect.Type type = new TypeToken<StageData>(){}.getType();
		
		return gson.fromJson(json, type);
	}

	public void start() {
		// 0 -> full
		if(ComboMasterApplication.getsInstance().getAdSceneType() == 0 && intersistialAd.isLoaded()) {
			intersistialAd.show();
		} else {
			gameStart();
		}
	}
	
	private void gameStart() {
		setResult(RESULT_OK);
		finish();
	}
	
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                  .addTestDevice("E4FF164BB1F3CA085C0CB1B708E50283")
                  .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                  .build();

        intersistialAd.loadAd(adRequest);
    }
	
}
