package com.a30corner.combomaster.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.ui.DialogUtil;
import com.a30corner.combomaster.pad.monster.MonsterSkill;
import com.a30corner.combomaster.pad.monster.MonsterVO;
import com.a30corner.combomaster.padherder.PadHerderLoader;
import com.a30corner.combomaster.padherder.vo.Monster;
import com.a30corner.combomaster.padherder.vo.PadHerder;
import com.a30corner.combomaster.provider.LocalDBHelper;
import com.a30corner.combomaster.provider.vo.MonsterDO;
import com.a30corner.combomaster.utils.BitmapUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class QuickSetupActivity extends Activity {

    private AlertDialog mUrlDialog;
    private EditText edit;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_quick_setup);
        initViews();
    }

    public void initViews() {

        AlertDialog.Builder builder = new AlertDialog.Builder(QuickSetupActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_url, null);
        builder.setTitle(R.string.title_input_url);
        builder.setView(dialogView);
        mUrlDialog = builder.create();
        edit = dialogView.findViewById(R.id.url);
        dialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mUrlDialog.dismiss();
                mProgress = new ProgressDialog(QuickSetupActivity.this);
                mProgress.setMessage(getString(R.string.str_loading));
                mProgress.setCancelable(false);
                mProgress.show();

                String url = edit.getText().toString();
                parseUrl(url);
            }
        });
        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mUrlDialog.dismiss();
            }
        });

        findViewById(R.id.load_by_padherder).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Activity activity = QuickSetupActivity.this;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.add_by_padherder);
                final EditText input = new EditText(activity);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadByPadHerder(input.getText().toString().trim());
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

                ComboMasterApplication.getsInstance().putGaAction("Settings", "addByPadHerder");
            }
        });

        findViewById(R.id.load_by_filter).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadByFilter(v);
                ComboMasterApplication.getsInstance().putGaAction("Settings", "addByFilter");
            }
        });

        findViewById(R.id.load_from_url).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mUrlDialog.show();
                ComboMasterApplication.getsInstance().putGaAction("Settings", "addByUrl");
            }
        });

        findViewById(R.id.how_to_use_it).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO: go to guideline activity
                startActivity(new Intent(QuickSetupActivity.this, GuideLineActivity.class));
                ComboMasterApplication.getsInstance().putGaAction("Settings", "howToUse");
            }
        });

    }

    private void parseUrl(final String url) {
        if (!TextUtils.isEmpty(url)) {
            runOnNonUiThread(new Runnable() {

                @Override
                public void run() {
                    if(url.length()>40) {
                        // not shorten url... parse it..
                        analysisUrl(url);
                    } else {
                        String newurl = url;
                        if(!url.contains("/")) {
                            newurl = "http://tinyurl.com/" + url;
                        }
                        String fullurl = getLongUrl(newurl);
                        analysisUrl(fullurl);
                    }
                }
            });
        } else {
            Toast.makeText(QuickSetupActivity.this, "Empty Url", Toast.LENGTH_SHORT).show();
            mProgress.dismiss();
        }
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

    private void analysisUrl(String url) {
        ArrayList<Integer> parsed = new ArrayList<Integer>();
        if(url.contains("puzzledragonx")) {
            String[] arg = url.split("=");
            if (arg.length>=2) {
                String[] petsData = arg[1].split("\\.\\.");
                for(int i=0; i<petsData.length;++i) {
                    try {
                        String[] data = petsData[i].split("\\.");
                        if (data.length>=7) {
                            parsed.add(Integer.parseInt(data[0]));
                            parsed.add(Integer.parseInt(data[1]));
                            parsed.add(Integer.parseInt(data[3]));
                            parsed.add(Integer.parseInt(data[4]));
                            parsed.add(Integer.parseInt(data[5]));
                            parsed.add(Integer.parseInt(data[6]));
                        }

                    } catch(Exception e) {
                        Log.e("ComboMaster", e.toString());
                    }
                }
            }
        } else if (url.contains("loglesslove")) {
            String[] arg = url.split("\\?");
            if (arg.length>=2) {
                String[] petsData = arg[1].split("&");
                for(int i=0; i<petsData.length;++i) {
                    try {
                        String[] m = petsData[i].split("=");
                        if (m.length>=2) {
                            String[] data = m[1].split("\\.");
                            if (data.length>=6) {
                                parsed.add(Integer.parseInt(data[0]));
                                parsed.add(Integer.parseInt(data[1]));
                                parsed.add(Integer.parseInt(data[2]));
                                parsed.add(Integer.parseInt(data[3]));
                                parsed.add(Integer.parseInt(data[4]));
                                parsed.add(Integer.parseInt(data[5]));
                            }
                        }

                    } catch(Exception e) {
                    }
                }
            }
        } else if(url.contains("skyozora")) {
            boolean loaded = false;
            String[] arg = url.split("/");
            if (arg.length>=5) {
                String[] petsData = arg[4].split(";");
                for(int i=0; i<petsData.length;++i) {
                    try {
                        String[] data = petsData[i].split(",");
                        if (data.length>=6) {
                            parsed.add(Integer.parseInt(data[0]));
                            parsed.add(Integer.parseInt(data[1]));
                            parsed.add(Integer.parseInt(data[2]));
                            parsed.add(Integer.parseInt(data[3]));
                            parsed.add(Integer.parseInt(data[4]));
                            parsed.add(Integer.parseInt(data[5]));
                            loaded = true;
                        }

                    } catch(Exception e) {
                    }
                }
            }
            if(!loaded) {
                Document document = null;
                try {
                    document = Jsoup.parse(URI.create(url).toURL(), 30000);
                    Elements elements = document.select("a[href~=(^pets*)]");
                    Iterator<Element> iter = elements.iterator();
                    int c = 0;
                    while(iter.hasNext()) {
                        Element element = iter.next();
                        String href = element.attr("href");
                        if(!TextUtils.isEmpty(href)) {
                            String id = href.replace("pets/", "");
                            try {
                                int no = Integer.parseInt(id);
                                parsed.add(no);
                                parsed.add(99);
                                parsed.add(9);
                                parsed.add(99);
                                parsed.add(99);
                                parsed.add(99);
                            } catch(Exception e) {
                            }
                        }
                        if(++c >= 6) {
                            break;
                        }
                    }
                } catch (IOException e) {
                    Log.e("ComboMaster", e.toString());
                    e.printStackTrace();
                }
            }
        }
        int size = parsed.size();
        if (size>0) {
            mProgress.dismiss();

            Intent intent = new Intent(QuickSetupActivity.this, TeamAdjustActivity.class);
            Bundle bundle = new Bundle();
            int[] d = new int[parsed.size()];
            for(int i=0; i<size; ++i) {
                d[i] = parsed.get(i);
            }
            bundle.putIntArray("data", d);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            QuickSetupActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(QuickSetupActivity.this, "parse failed, it may be a unsupported website", Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                }
            });
        }
    }

    private String getLongUrl(String url) {
        String longurl = "";
        try {
            String decodeUrl = "http://urlex.org/" + URLEncoder.encode(url, "UTF-8");
            Document document =Jsoup.parse(URI.create(decodeUrl).toURL(), 30000);
            Elements elements = document.select("a[rel~=(external.*)]");
            Iterator<Element> iter = elements.iterator();
            while(iter.hasNext()) {
                Element element = iter.next();
                longurl = element.attr("href");
                break;
            }
        } catch (Exception e) {
        }
        return longurl;
    }

    public void loadByFilter(View view) {
        startActivity(new Intent(QuickSetupActivity.this, QuickTeamSetupActivity.class));
    }

    ProgressDialog mDialog;
    public void loadByPadHerder(String username) {
        final Activity activity = QuickSetupActivity.this;
        mDialog = DialogUtil.getUpdateDialog(activity, true);
        mDialog.show();

        Observable<PadHerder> observable = PadHerderLoader.load(username, new ErrorHandler() {

            @Override
            public Throwable handleError(RetrofitError error) {
                String errMsg = error.toString();
                mDialog.dismiss();
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(activity, activity.getString(R.string.retrofit_error), Toast.LENGTH_SHORT).show();
                    }
                });
                return new IOException(errMsg);
            }
        });

        observable
                .map(new Func1<PadHerder, List<Monster>>() {

                    @Override
                    public List<Monster> call(PadHerder pad) {
                        return pad.monsters;
                    }
                })
                .subscribe(new Action1<List<Monster>>() {

                               @Override
                               public void call(List<Monster> data) {
                                   int size = data.size();
                                   int index = LocalDBHelper.getMaxIndexFromBox(activity);
                                   if(index > 0) {
                                       ++index;
                                   }
                                   int max = Math.min(LocalDBHelper.MAX_BOX-index, size);
                                   mDialog.setProgress(0);
                                   mDialog.setMax(max);

                                   if(max<0) {
                                       activity.runOnUiThread(new Runnable() {

                                           @Override
                                           public void run() {
                                               Toast.makeText(activity, activity.getString(R.string.box_full), Toast.LENGTH_SHORT).show();
                                           }
                                       });
                                       return ;
                                   }

                                   List<Integer> idList = new ArrayList<Integer>();
                                   for(Monster m : data) {
                                       idList.add(m.monster);
                                   }

                                   List<Pair<MonsterVO, Monster>> newList = new ArrayList<Pair<MonsterVO,Monster>>();
                                   for(int i=0; i<size; ++i) {
                                       Monster m = data.get(i);
                                       MonsterVO vo = LocalDBHelper.getMonsterData(activity, m.monster);
                                       if(vo != null) {
                                           newList.add(new Pair<MonsterVO, Monster>(vo, m));
                                       }
                                   }

                                   Collections.sort(newList, new Comparator<Pair<MonsterVO, Monster>>() {

                                       @Override
                                       public int compare(Pair<MonsterVO, Monster> lhs,
                                                          Pair<MonsterVO, Monster> rhs) {
                                           if(lhs.second.priority>rhs.second.priority) {
                                               return -1;
                                           } else if(lhs.second.priority<rhs.second.priority) {
                                               return 1;
                                           } else {
                                               if(lhs.first.getRare()>rhs.first.getRare()) {
                                                   return -1;
                                               } else if(lhs.first.getRare()<rhs.first.getRare()) {
                                                   return 1;
                                               }
                                           }
                                           return 0;
                                       }
                                   });
                                   int newsize = newList.size();
                                   for(int i=0; i<newsize; ++i) {
                                       Pair<MonsterVO, Monster> pair = newList.get(i);
                                       MonsterVO vo = pair.first;
                                       Monster m = pair.second;
                                       int boxindex = i + index;
                                       mDialog.setProgress(i);

                                       if(boxindex>=LocalDBHelper.MAX_BOX) {
                                           break;
                                       }

                                       File f = new File(activity.getFilesDir(), m.monster + "i.png");
                                       if(!f.exists()) {
                                           downloadImage(activity, vo);
                                       }

                                       MonsterDO mdo = MonsterDO.fromData(boxindex,
                                               m.monster,
                                               m.target_level,
                                               m.plus_hp,
                                               m.plus_atk,
                                               m.plus_rcv,
                                               m.current_awakening, vo.getAwokenList().size()==m.current_awakening,
                                               new ArrayList<MonsterSkill.MoneyAwokenSkill>(),
                                               -1,
                                               vo.slv1 - m.current_skill + 1,
                                               -1, -1);
                                       LocalDBHelper.addMonster(activity, mdo);


                                   }

                                   mDialog.dismiss();

                                   activity.runOnUiThread(new Runnable() {

                                       @Override
                                       public void run() {
                                           Toast.makeText(activity, activity.getString(R.string.successful), Toast.LENGTH_LONG).show();
                                       }
                                   });
                               }
                           },
                        new Action1<Throwable>() {

                            @Override
                            public void call(final Throwable t) {
                                activity.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        mDialog.dismiss();
                                        Toast.makeText(activity, t.toString(), Toast.LENGTH_LONG).show();
                                        Log.e("QuickSetup", t.toString(), t);
                                        t.printStackTrace();
                                    }
                                });
                            }
                        }
                );
    }

    private void saveImageAsync(final Activity activity, Bitmap bitmap, final int id) {
        Observable.just(bitmap)
                .observeOn(Schedulers.immediate())
                .subscribe(new Action1<Bitmap>() {

                    @Override
                    public void call(Bitmap bitmap) {
                        BitmapUtil.saveBitmap(activity, bitmap, String.format("%di.png", id));
                    }
                });
    }

    private void downloadImage(final Activity activity, MonsterVO vo) {
        final int imageId = vo.mNo;
        //String uri = "http://188.166.227.62/combomaster/images/" + id + "i.png";
        String uri = vo.getImageUrl();
        ImageLoader.getInstance().loadImage(uri, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(activity, R.string.need_network_1st, Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onLoadingComplete(String url, View arg1, Bitmap bitmap) {
                saveImageAsync(activity, bitmap, imageId);
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
            }
        });
    }
}
