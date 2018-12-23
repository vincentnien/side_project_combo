package com.a30corner.combomaster.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;

public class SetAnalysisLocationActivity extends Activity {
    
	private ImageView mImageView;
	private ImageView mCropView;
	
	private SeekBar[] mCropRect = new SeekBar[4];
	
	private Handler mNonUiHandler;
	private Rect mAnalysisRect;
	
	private int mImageWidth, mImageHeight;
	private float mRatio = 1.0f;
	
	private Button[] mButtons = new Button[8];
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        
        setContentView(R.layout.analysis_layout);
        
        mCropView = (ImageView) findViewById(R.id.crop);
        mImageView = (ImageView) findViewById(R.id.image);
        
        final int[] seekbarId = {R.id.crop_x, R.id.crop_y, R.id.crop_w, R.id.crop_h};
        for(int i=0; i<4; ++i) {
            mCropRect[i] = (SeekBar) findViewById(seekbarId[i]);
            mCropRect[i].setEnabled(false);
        }
        initSeekBarListener();
        setupButtons();
        
        HandlerThread ht = new HandlerThread("db");
        ht.start();
        mNonUiHandler = new Handler(ht.getLooper());
        
        mNonUiHandler.post(new Runnable() {
            
            @Override
            public void run() {
                loadRect();
            }
        });
    }
    
    private void setupButtons() {
    	mButtons[0] = (Button) findViewById(R.id.x_minus);
    	mButtons[1] = (Button) findViewById(R.id.x_plus);
    	mButtons[2] = (Button) findViewById(R.id.y_minus);
    	mButtons[3] = (Button) findViewById(R.id.y_plus);
    	mButtons[4] = (Button) findViewById(R.id.w_minus);
    	mButtons[5] = (Button) findViewById(R.id.w_plus);
    	mButtons[6] = (Button) findViewById(R.id.h_minus);
    	mButtons[7] = (Button) findViewById(R.id.h_plus);
    	
    	mButtons[0].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( mCropRect[0].isEnabled() ) {
					int progress = mCropRect[0].getProgress();
					mCropRect[0].setProgress(progress-1);
				}
			}
		});
    	mButtons[1].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( mCropRect[0].isEnabled() ) {
					int progress = mCropRect[0].getProgress();
					mCropRect[0].setProgress(progress+1);
				}
			}
		});
    	mButtons[2].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( mCropRect[1].isEnabled() ) {
					int progress = mCropRect[1].getProgress();
					mCropRect[1].setProgress(progress-1);
				}
			}
		});
    	mButtons[3].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( mCropRect[1].isEnabled() ) {
					int progress = mCropRect[1].getProgress();
					mCropRect[1].setProgress(progress+1);
				}
			}
		});
    	mButtons[4].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( mCropRect[2].isEnabled() ) {
					int progress = mCropRect[2].getProgress();
					mCropRect[2].setProgress(progress-1);
				}
			}
		});
    	mButtons[5].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( mCropRect[2].isEnabled() ) {
					int progress = mCropRect[2].getProgress();
					mCropRect[2].setProgress(progress+1);
				}
			}
		});
    	mButtons[6].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( mCropRect[3].isEnabled() ) {
					int progress = mCropRect[3].getProgress();
					mCropRect[3].setProgress(progress-1);
				}
			}
		});
    	mButtons[7].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( mCropRect[3].isEnabled() ) {
					int progress = mCropRect[3].getProgress();
					mCropRect[3].setProgress(progress+1);
				}
			}
		});
    }
    
    private void loadRect() {
        SharedPreferenceUtil util = SharedPreferenceUtil.getInstance(SetAnalysisLocationActivity.this);
        mAnalysisRect = util.getAnalysisRect();
        if ( !mAnalysisRect.isEmpty() ) {
            int w = mAnalysisRect.right - mAnalysisRect.left;
            int h = mAnalysisRect.bottom - mAnalysisRect.top;
            mCropRect[0].setMax(w);
            mCropRect[1].setMax(h);
            mCropRect[2].setMax(w);
            mCropRect[3].setMax(h);
            mCropRect[0].setProgress(mAnalysisRect.left);
            mCropRect[1].setProgress(mAnalysisRect.top);
            mCropRect[2].setProgress(w);
            mCropRect[3].setProgress(h);
            
            android.view.ViewGroup.LayoutParams params = mCropView.getLayoutParams();
            params.width = w;
            params.height = h;
            mCropView.setX(mAnalysisRect.left);
            mCropView.setY(mAnalysisRect.top);
            mCropView.invalidate();
        }
    }
    
    private void initSeekBarListener() {
        mCropRect[0].setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = mCropRect[2].getProgress();
                int x = mCropRect[0].getProgress();
                if ( x + progress > mImageWidth ) {
                    mCropRect[2].setProgress(mImageWidth - x);
                }
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                mCropView.setX(progress);
                mCropView.requestLayout();
            }
        });
        mCropRect[1].setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = mCropRect[3].getProgress();
                int y = mCropRect[1].getProgress();
                if ( y + progress > mImageHeight ) {
                    mCropRect[3].setProgress(mImageHeight - y);
                }
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                mCropView.setY(progress);
                mCropView.requestLayout();
            }
        });
        mCropRect[2].setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = mCropRect[2].getProgress();
                int x = mCropRect[0].getProgress();
                if ( x + progress > mImageWidth ) {
                    mCropRect[2].setProgress(mImageWidth - x);
                }
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                mCropView.getLayoutParams().width = progress;
                mCropView.requestLayout();
            }
        });
        mCropRect[3].setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = mCropRect[3].getProgress();
                int y = mCropRect[1].getProgress();
                if ( y + progress > mImageHeight ) {
                    mCropRect[3].setProgress(mImageHeight - y);
                }
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                mCropView.getLayoutParams().height = progress;
                mCropView.requestLayout();

            }
        });
    }
    
    @Override
    protected void onDestroy() {
    	mNonUiHandler.getLooper().quit();
    	super.onDestroy();
    }

    
	private DisplayMetrics getMetrics() {
		WindowManager mgr = getWindowManager();
		Display display = mgr.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		return dm;
    }
    
	@SuppressLint("NewApi")
	private DisplayMetrics getRealMetrics() {
		WindowManager mgr = getWindowManager();
		Display display = mgr.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getRealMetrics(dm);
		return dm;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			mNonUiHandler.post(new Runnable() {
				
				@Override
				public void run() {
					final String path;
					Uri uri = data.getData();
					String[] pathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = null;
					try {
						cursor = getContentResolver().query(
								uri, pathColumn, null, null, null);
						if (cursor != null && cursor.moveToFirst()) {
							path = cursor.getString(cursor
									.getColumnIndex(pathColumn[0]));
						} else {
							path = null;
						}
					} finally {
						if (cursor != null) {
							cursor.close();
						}
					}
					if ( path != null ) {
				        final BitmapDrawable d = (BitmapDrawable) BitmapDrawable.createFromPath(path);
				        if ( d != null ) {
				        	DisplayMetrics dm = null;
				        	if ( Build.VERSION.SDK_INT < 17 ) {
				        		dm = getMetrics();
				        	} else {
				        		dm = getRealMetrics();
				        	}
				        	
				            Bitmap bitmap = d.getBitmap();
				            mImageWidth = bitmap.getWidth();
				            mImageHeight = bitmap.getHeight();
				            
				            // resolution is different to image size
				            if ( mImageWidth != dm.widthPixels ) {
				            	mRatio = dm.widthPixels / (float)mImageWidth;
				            	
				            	bitmap = Bitmap.createScaledBitmap(bitmap, dm.widthPixels, dm.heightPixels, false);
					            mImageWidth = dm.widthPixels;
					            mImageHeight = dm.heightPixels;
				            }
				            
    						runOnUiThread(new Runnable() {
    							public void run() {
    								mImageView.setImageDrawable(d);
    								
                                    for(int i=0; i<4; ++i) {
                                        mCropRect[i].setEnabled(true);
                                    }
    								
                                    int w = mImageView.getWidth();
                                    int h = mImageView.getHeight();
                                    int h_div_2 = h/2;
                                    
                                    mCropRect[0].setMax(w);
                                    mCropRect[1].setMax(h);
                                    mCropRect[2].setMax(w);
                                    mCropRect[3].setMax(h);
                                    
                                    if ( mAnalysisRect.isEmpty() ) {
        								android.view.ViewGroup.LayoutParams params = mCropView.getLayoutParams();
        								params.width = w;
        								params.height = h;
        								mCropView.setY(h);
        								
                                        mCropRect[1].setProgress(h_div_2);
                                        mCropRect[2].setProgress(w);
                                        mCropRect[3].setProgress(h_div_2);
                                    } else {
                                        float percentX = mImageView.getWidth() / (float)mImageWidth;
                                        float percentY = mImageView.getHeight() / (float)mImageHeight;
                                        mCropRect[0].setProgress((int)(mAnalysisRect.left * percentX));
                                        mCropRect[1].setProgress((int)(mAnalysisRect.top * percentY));
                                        mCropRect[2].setProgress((int)(mAnalysisRect.width() * percentX));
                                        mCropRect[3].setProgress((int)(mAnalysisRect.height() * percentY));
                                    }
    							}
    						});
				        } else {
				            LogUtil.e("can not create image from path ");
				        }
					}
				}
			});

		}
						
    }
    
    public void onLoadImage(View view) {
    	try {
			Intent pickIntent = new Intent(Intent.ACTION_PICK);
			pickIntent.setType("image/*");
			startActivityForResult(pickIntent,0);
    	} catch(Throwable e) {
    		Toast.makeText(SetAnalysisLocationActivity.this, getString(R.string.str_need_album_app), Toast.LENGTH_LONG).show();
    	}
    }
    
    public void onSaveLoc(View view) {
    	
        float percentX = mImageWidth / (float)mImageView.getWidth();
        float percentY = mImageHeight / (float)mImageView.getHeight();
        int x = (int)(mCropRect[0].getProgress() * percentX);
        int y = (int)(mCropRect[1].getProgress() * percentY);
        int w = (int)(mCropRect[2].getProgress() * percentX);
        int h = (int)(mCropRect[3].getProgress() * percentY);
        SharedPreferenceUtil util = SharedPreferenceUtil.getInstance(SetAnalysisLocationActivity.this);
        util.setAnalysisRect(x, y, w, h);
        util.apply();
        
        finish();
    }
}
