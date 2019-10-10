package com.a30corner.combomaster.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.a30corner.combomaster.R;
import com.a30corner.combomaster.utils.NetworkUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.animation.drawable.AnimatedDrawable2;
import com.facebook.fresco.animation.drawable.AnimationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class EggMachineActivity extends Activity {
        DraweeController newcontroller;
        int cardId = 1;
        String rareUrl, key;

        SimpleDraweeView view;

        @SuppressLint("CheckResult")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.layout_egg_machine);

            key = getIntent().getStringExtra("machine");

            view = findViewById(R.id.gifView);
            display();
        }

        private void display() {
            Observable.just("http://188.166.227.62/rolling/draw.php?key=" + key)
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
                            if(TextUtils.isEmpty(s)) {
                                s = "{'rare':5, 'id':1}";
                            }
                            try {
                                return new JSONObject(s);
                            } catch (JSONException e) {
                                return null;
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JSONObject>() {
                        @Override
                        public void call(JSONObject jObj) {
                            boolean failed = false;
                            if(jObj != null) {
                                try {
                                    if (jObj.has("rare")) {
                                        int rare = jObj.getInt("rare");
                                        rareUrl = "http://188.166.227.62/combomaster/images/" + key + "/rare" + rare + ".webp";
                                    }
                                    if (jObj.has("id")) {
                                        cardId = jObj.getInt("id");
                                        initDraweeView(view);
                                    } else {
                                        failed = true;
                                    }
                                } catch(Exception e) {
                                    failed = true;
                                }
                            } else {
                                failed = true;
                            }
                            if(failed) {
                                Toast.makeText(EggMachineActivity.this, "Connection error!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        private void initDraweeView(final SimpleDraweeView view) {

            ControllerListener listener = new ControllerListener() {

                @Override
                public void onSubmit(String id, Object callerContext) {

                }

                @Override
                public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                    if(animatable != null) {
                        ((AnimatedDrawable2)animatable).setAnimationListener(new AnimationListener() {
                            @Override
                            public void onAnimationStart(AnimatedDrawable2 drawable) {
                                Observable.just(0)
                                        .delay(8L, TimeUnit.SECONDS)
                                        .subscribe(new Action1<Integer>() {
                                            @Override
                                            public void call(Integer integer) {
                                                displayDrawCard();
                                            }
                                        });
                            }

                            @Override
                            public void onAnimationStop(AnimatedDrawable2 drawable) {
                                displayDrawCard();
                            }

                            @Override
                            public void onAnimationReset(AnimatedDrawable2 drawable) {

                            }

                            @Override
                            public void onAnimationRepeat(AnimatedDrawable2 drawable) {

                            }

                            @Override
                            public void onAnimationFrame(AnimatedDrawable2 drawable, int frameNumber) {

                            }
                        });
                        animatable.start();
                    }
                }

                @Override
                public void onIntermediateImageSet(String id, Object imageInfo) {

                }

                @Override
                public void onIntermediateImageFailed(String id, Throwable throwable) {

                }

                @Override
                public void onFailure(String id, Throwable throwable) {

                }

                @Override
                public void onRelease(String id) {

                }

            };

            //ImageRequest rareRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(rareUrl)).build();


            newcontroller =
                    Fresco.newDraweeControllerBuilder()
                            .setControllerListener(listener)
                            .setUri(rareUrl)
                            .setAutoPlayAnimations(false)
                            .build();

            DraweeController controller =
                    Fresco.newDraweeControllerBuilder()
                            .setOldController(view.getController())
                            .setUri("http://188.166.227.62/combomaster/images/" + key + "/wait.webp")
                            .setAutoPlayAnimations(true)
                            .build();

            view.setController(controller);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.setEnabled(false);
                    view.setController(newcontroller);
                }
            });
        }

        AtomicBoolean pause = new AtomicBoolean(false);
        AtomicBoolean go = new AtomicBoolean(false);
        private void displayDrawCard() {
            if(go.get() || pause.get()) {
                return ;
            }
            go.set(true);
            Intent intent = new Intent(EggMachineActivity.this, EggResultActivity.class);
            intent.putExtra("card", cardId);
            intent.putExtra("machine", key);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }

        @Override
        protected void onResume() {
            super.onResume();

            if(pause.get() && !go.get()) {
                pause.set(false);
                displayDrawCard();
            }
        }

        @Override
        protected void onPause() {
            super.onPause();
            pause.set(true);
        }
}
