package com.a30corner.combomaster.activity.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.TeamInfo;
import com.a30corner.combomaster.utils.BitmapUtil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SetLvDialog extends Dialog {
    
    private WeakReference<TeamInfo> mTeamRef = null;
    private int[] mLevel = {1,1,1,1,1,1};
    
    private ImageView[] mImages = new ImageView[6];
    private Button[] mButtons = new Button[6];
    
    private EditText mLvEditor;
    
    private IResultCallback mCallback = null;
    
    public interface IResultCallback {
        public void onResult();
    }
    
    public SetLvDialog(Context context) {
        super(context);
    }
    
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        
        initView();
    }
    
    private void initView() {
        final int[] layoutID = { R.id.leader1, R.id.member1, R.id.member2,
                R.id.member3, R.id.member4, R.id.leader2 };
        
        for(int i=0; i<6; ++i) {
            View view = findViewById(layoutID[i]);
            mImages[i] = (ImageView) view.findViewById(R.id.monster);
            mButtons[i] = (Button)view.findViewById(R.id.monsterLv);
            mButtons[i].setEnabled(false);
        }
        
        mLvEditor = (EditText) findViewById(R.id.edit_lv);
        
        Button btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if ( mTeamRef == null ) {
                    return ;
                }
                TeamInfo team = mTeamRef.get();
                for(int i=0; i<6; ++i) {
                    MonsterInfo minfo = team.getMember(i);
                    if ( minfo == null ) {
                        continue;
                    }
                    minfo.setLv(mLevel[i]);
                }
                if ( mCallback != null ) {
                    mCallback.onResult();
                }
                dismiss();
            }
        });
        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        
        Button btnSetLv = (Button) findViewById(R.id.btn_edit_lv);
        btnSetLv.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if ( mTeamRef == null ) {
                    return ;
                }
                String value = mLvEditor.getText().toString();
                
                if (!TextUtils.isEmpty(value)) {
                    String[] lv = value.split(",");
                    if (lv != null) {
                        TeamInfo team = mTeamRef.get();
                        for(int i=0; i<lv.length; ++i) {
                            MonsterInfo minfo = team.getMember(i);
                            if ( minfo == null ) {
                                continue;
                            }
                            try {
                                int level = Integer.parseInt(lv[i]);
                                mLevel[i] = level;
                                minfo.setLv(level);
                                mButtons[i].setText(String.format("Lv.%02d", level));
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                
            }
        });
        
        updateUI();
    }
    
    public void setCallback(IResultCallback callback) {
        mCallback = callback;
    }

    public void setTeamInfo(TeamInfo info) {
        mTeamRef = new WeakReference<TeamInfo>(info);
        
        updateUI();
    }
    
    private void updateUI() {
        if ( mTeamRef == null ) {
            return ;
        }
        final Context context = getContext();
        ComboMasterApplication instance = ComboMasterApplication.getsInstance(); 
        TeamInfo info = mTeamRef.get();
        List<Integer> levelList = new ArrayList<Integer>();
        for(int i=0; i<6; ++i) {
            MonsterInfo minfo = info.getMember(i);
            if ( minfo == null ) {
                continue;
            }
            final int index = i;
            int no = minfo.getNo();
            Bitmap bitmap = instance.getBitmap(no);
            if (bitmap == null) {
                bitmap = BitmapUtil.getBitmap(context, no + "i.png");
            }
            mImages[i].setImageBitmap(bitmap);
            
            final int lv = minfo.getLv();
            final Button btn = mButtons[i];
            mLevel[i] = lv;
            levelList.add(lv);
            btn.setText(String.format("Lv.%02d", lv));
            btn.setEnabled(true);
            btn.setOnClickListener(new View.OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    DialogUtil.getNumberPickerDialog(context,
                            R.string.lv, lv, 1, 99,
                            new DialogUtil.ResultCallback() {

                                @Override
                                public void onResult(int value) {
                                    mLevel[index] = value;
                                    btn.setText(String.format("Lv.%02d", value));
                                }

                                @Override
                                public void onCancel() {
                                }
                            }).show();
                }
            });
        }
        mLvEditor.setText(TextUtils.join(",", levelList));
    }
}
