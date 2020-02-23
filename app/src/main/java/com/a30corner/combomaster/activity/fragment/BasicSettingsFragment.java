package com.a30corner.combomaster.activity.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.activity.SetAnalysisLocationActivity;
import com.a30corner.combomaster.utils.BackupUtils;
import com.a30corner.combomaster.utils.BackupUtils.IResultCallback;
import com.a30corner.combomaster.utils.LogUtil;
import com.a30corner.combomaster.utils.SharedPreferenceUtil;
import com.a30corner.combomaster.utils.StorageUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BasicSettingsFragment extends CMBaseFragment {

	private SharedPreferenceUtil sp = null;
	private TextView mAnalysisRect;

	private ProgressDialog mProgressDialog;
	private TextView backup;
	
	private TextView bgmPath;
	private TextView soundPath;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		sp = getSharedPreference();
		ComboMasterApplication.getsInstance().setHasMusic(!TextUtils.isEmpty(sp.getBgm()),!TextUtils.isEmpty(sp.getSound()));
		
		View view = inflater.inflate(R.layout.basic_settings, null);

		Spinner dropTime = view.findViewById(R.id.drop_time);
		dropTime.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				sp.setTimeLimit(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		dropTime.setSelection(sp.getTimeLimitPos());

		Spinner orbType = (Spinner) view.findViewById(R.id.orb_type);
		orbType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				sp.setType(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		orbType.setSelection(sp.getType());

		CheckBox showResult = (CheckBox) view.findViewById(R.id.show_directly);
		showResult.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				sp.setShowResultDialog(isChecked);
			}
		});
		showResult.setChecked(sp.getShowResultDialog());

		ToggleButton colorWeakness = (ToggleButton) view
				.findViewById(R.id.color_weakness);
		colorWeakness.setChecked(sp
				.getBoolean(SharedPreferenceUtil.PREF_COLOR_WEAKNESS));
		colorWeakness.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				sp.setBoolean(SharedPreferenceUtil.PREF_COLOR_WEAKNESS,
						isChecked);
			}
		});

		mAnalysisRect = view.findViewById(R.id.analysis_rect);
		mAnalysisRect.setText("[][]");
		
		Button setupAnalysis = view.findViewById(R.id.setup_analysis);
		setupAnalysis.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                gotoAnalysisPage(v);
            }
        });
		Button resetAnalysis = view.findViewById(R.id.reset_analysis);
		resetAnalysis.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                clearAnalysisPos(v);
            }
        });
		
		CheckBox fullscreen = view.findViewById(R.id.cb_fullscreen);
		boolean checked = sp.getBoolean(SharedPreferenceUtil.PREF_FULLSCREEN, true);
		fullscreen.setChecked(checked);
		
		fullscreen.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sp.setBoolean(SharedPreferenceUtil.PREF_FULLSCREEN,
                        isChecked);
            }
        });
		CheckBox original = view.findViewById(R.id.original_style);
		try {
            final SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
            boolean orig = pref.getBoolean("viewpager", false);
            original.setChecked(orig);
            original.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pref.edit().putBoolean("viewpager", isChecked).apply();
                    ComboMasterApplication.getsInstance().putGaAction("viewpager", "set", ""+isChecked);
                }
            });
        } catch(Exception e) {
		    e.printStackTrace();
        }

		CheckBox keepRatio = view.findViewById(R.id.cb_keep_ratio);
		boolean keepIt = sp.getBoolean(SharedPreferenceUtil.PREF_KEEP_RATIO, true);
		keepRatio.setChecked(keepIt);
		keepRatio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				sp.setBoolean(SharedPreferenceUtil.PREF_KEEP_RATIO, isChecked);
			}
		});
		
		backup = view.findViewById(R.id.backup_path);
		backup.setText(getString(R.string.str_backup_path, StorageUtil.getCustomDirectory(getActivity())));
		Button backupBtn = (Button) view.findViewById(R.id.btn_backup);
		backupBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
        Button restoreBtn = (Button) view.findViewById(R.id.btn_restore);
        restoreBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                showRestoreDialog();
            }
        });
		
        Button setPathBtn = (Button) view.findViewById(R.id.btn_set_path);
        setPathBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showInputPathDialog();
			}
		});
        
        bgmPath = (TextView) view.findViewById(R.id.bgm_path);
        bgmPath.setText(getActivity().getString(R.string.str_path) + sp.getBgm());
        soundPath = (TextView) view.findViewById(R.id.sound_path);
        soundPath.setText(getActivity().getString(R.string.str_path) + sp.getSound());
        
		Button bgm = (Button) view.findViewById(R.id.btn_bgm);
		bgm.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                openFileChooser(0);
            }
        });
        Button reset = (Button) view.findViewById(R.id.btn_bgm_reset);
        reset.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                SharedPreferenceUtil.getInstance(activity).setBgm("");
                bgmPath.setText(activity.getString(R.string.str_path));
            }
        });

        Button sound = (Button) view.findViewById(R.id.btn_sound);
        sound.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                openFileChooser(1);
            }
        }); 
        Button resetSound = (Button) view.findViewById(R.id.btn_sound_reset);
        resetSound.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                SharedPreferenceUtil.getInstance(activity).setSound("");
                soundPath.setText(activity.getString(R.string.str_path));
            }
        });
        
		return view;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if ( resultCode == Activity.RESULT_OK) {
	        // FIXME: run on non ui thread
            Uri uri=data.getData();
            Activity activity = getActivity();
            
            Cursor cursor = activity.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);
            if ( cursor != null ) {
                try {
                    if ( cursor.moveToFirst() ) {
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                        SharedPreferenceUtil sp = SharedPreferenceUtil.getInstance(activity);
                        if ( requestCode == 0 ) {
	                        sp.setBgm(path);
	                        bgmPath.setText(activity.getString(R.string.str_path) + path);
                        } else if ( requestCode == 1 ) {
	                        sp.setSound(path);
	                        soundPath.setText(activity.getString(R.string.str_path) + path);                        	
                        }
                        sp.apply();
                    }
                } finally {
                    cursor.close();
                }
            }
	    }
	}

    private void openFileChooser(int id) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, id);
        } catch(Exception e) {
            Activity activity = getActivity();
            Toast.makeText(activity, activity.getString(R.string.str_need_music_app), Toast.LENGTH_LONG).show();
        }
    }
	
	private void showRestoreDialog() {
	    final Activity activity = getActivity();
	    
	    File backup = new File(Environment.getExternalStorageDirectory(), "ComboMaster");
	    List<File> allList = new ArrayList<File>();
	    
	    File backup1 = new File(StorageUtil.getCustomDirectory(activity));
	    if (!backup.getAbsolutePath().equals(backup1.getAbsolutePath())) {
		    File[] list1 = backup1.listFiles(new FilenameFilter() {
	            
	            @Override
	            public boolean accept(File dir, String filename) {
	                return filename.endsWith(".zip");
	            }
	        });
		    if ( list1 != null ) {
	            Arrays.sort(list1, new Comparator<File>() {

	                @Override
	                public int compare(File lhs, File rhs) {
	                    return Long.valueOf(rhs.lastModified()).compareTo(lhs.lastModified());
	                }
	                
	            });
		    	for(File f : list1) {
		    		allList.add(f);
		    	}
		    }
	    }
	    
	    File[] list = backup.listFiles(new FilenameFilter() {
            
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".zip");
            }
        });
	    if ( list != null ) {
            Arrays.sort(list, new Comparator<File>() {

                @Override
                public int compare(File lhs, File rhs) {
                    return Long.valueOf(rhs.lastModified()).compareTo(lhs.lastModified());
                }
                
            });
	    	for(File f : list) {
	    		allList.add(f);
	    	}
	    }
	    

	    
	    if ( allList.size() > 0 ) {
            final ArrayAdapter<String> fileAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1);

            for(File f : allList) {
                fileAdapter.add(f.getName());
            }
            
	        AlertDialog dialog = new AlertDialog.Builder(activity)
	                    .setTitle(R.string.str_restore)
	                    .setAdapter(fileAdapter, new DialogInterface.OnClickListener() {
                            
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = fileAdapter.getItem(which);
                                restoreData(name);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
	        dialog.show();
	    } else {
	    	Toast.makeText(activity, getString(R.string.str_no_backup_files), Toast.LENGTH_SHORT).show();
	    }
	}
	
	private void showInputPathDialog() {
	    final Activity activity = getActivity();
	    final EditText input = new EditText(activity);
	    input.setText(StorageUtil.getCustomDirectory(activity));
	    AlertDialog dialog = new AlertDialog.Builder(activity)
	                            .setMessage(R.string.str_input_path)
	                            .setView(input)
	                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String path = input.getText().toString();
                                        File file = new File(path);
                                        if ( !file.exists() ) {
                                        	file.mkdirs();
                                        }
                                        if ( !file.canWrite() ) {
                                        	Toast.makeText(activity, getString(R.string.str_cannot_write), Toast.LENGTH_SHORT).show();
                                        } else {
                                        	StorageUtil.setCustomDirectory(activity, path);
                                        	backup.setText(getString(R.string.str_backup_path, path));
                                        }
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
	                            .create();
	    dialog.show();
	}
	
	private void showInputDialog() {
	    final Activity activity = getActivity();
	    final EditText input = new EditText(activity);
	    AlertDialog dialog = new AlertDialog.Builder(activity)
	                            .setMessage(R.string.str_input_path)
	                            .setView(input)
	                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String filename = input.getText().toString();
                                        if ( TextUtils.isEmpty(filename) ) {
                                            Toast.makeText(activity, "Empty filename, skip saving", Toast.LENGTH_SHORT).show();
                                        } else {
                                            backupMonsterBox(filename);
                                        }
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
	                            .create();
	    dialog.show();
	}
	
	@Override
	public void onResume() {
	    Rect rect = sp.getAnalysisRect();
	    if ( !rect.isEmpty() ) {
	        mAnalysisRect.setText(rect.toShortString());
	    }
	    super.onResume();
	}
	
	private void dismissProgressDialog(final boolean success, final String message, final boolean isBackup) {
	    final Activity activity = getActivity();
	    getActivity().runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                if ( mProgressDialog != null ) {
                    mProgressDialog.dismiss();
                }
                String output = null;
                if ( isBackup ) {
                    output = (success)? activity.getString(R.string.backup_success):
                        activity.getString(R.string.backup_fail);
                    Toast.makeText(activity, output+ message, Toast.LENGTH_LONG).show();
                } else {
                    output = (success)? activity.getString(R.string.restore_success):
                        activity.getString(R.string.restore_fail);
                    Toast.makeText(activity, output, Toast.LENGTH_LONG).show();
                }
                
            }
        });
	}
	
	private void restoreData(final String filename) {
	    final Activity activity = getActivity();
	    File path = new File(Environment.getExternalStorageDirectory(), "ComboMaster");
	    File file = new File(path, filename);
	    if ( file.exists() && file.canRead() ) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.str_restoring));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
            
            runOnNonUiThread(new Runnable() {
                
                @Override
                public void run() {
                    BackupUtils.restore(activity, filename, new IResultCallback() {
                        
                        @Override
                        public void onFail(Exception e) {
                            dismissProgressDialog(false, e.getMessage(), false);
                        }
                        
                        @Override
                        public void onComplete(String output) {
                            LogUtil.d("completed");
                            ComboMasterApplication.getsInstance().init();
                            dismissProgressDialog(true, output, false);
                        }
                    });
                }
            });
	    } else {
	        Toast.makeText(activity, "Cannot open file.", Toast.LENGTH_LONG).show();
	    }
	}
	
	private void backupMonsterBox(final String filename) {
		String path = StorageUtil.getCustomDirectory(getActivity());
		File file = new File(path);
        if (file.canWrite()) {
            if (BackupUtils.isFileExists(path, filename)) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.app_name)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(R.string.str_file_exists)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startBackup(filename, true);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startBackup(filename, false);
                            dialog.dismiss();
                        }
                    })
                    .create();
                dialog.show();
            } else {
                startBackup(filename, false);
            }
        } else {
            Toast.makeText(getActivity(), "Storage is not available", Toast.LENGTH_SHORT).show();
        }

	}
	
	private void startBackup(final String filename, final boolean override) {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.str_backing_up));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        
        runOnNonUiThread(new Runnable() {
            
            @Override
            public void run() {
                BackupUtils.backup(getActivity(), filename, new IResultCallback() {
                    
                    @Override
                    public void onFail(Exception e) {
                        dismissProgressDialog(false, e.getMessage(), true);
                    }
                    
                    @Override
                    public void onComplete(String output) {
                        LogUtil.d("completed");
                        dismissProgressDialog(true, output, true);
                    }
                }, override);
            }
        });
	}
	
	public void gotoAnalysisPage(View view) {
	    Intent intent = new Intent(getActivity(), SetAnalysisLocationActivity.class);
	    startActivity(intent);
	}
	
	public void clearAnalysisPos(View view) {
	    sp.setAnalysisRect(0, 0, 0, 0);
	    sp.apply();
	    mAnalysisRect.setText("[][]");
	}
	
	// @Override
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if ( resultCode == Activity.RESULT_OK ) {
	// LogUtil.e(data.toString());
	// Bundle bundle = data.getExtras();
	// if ( bundle != null ) {
	// File file = (File) bundle.get(FileChooserActivity.OUTPUT_FILE_OBJECT);
	// mBgm.setText(file.getName());
	//
	// SharedPreferenceUtil.getInstance(getActivity()).setBgm(file.getAbsolutePath());
	// }
	// }
	// }
}
