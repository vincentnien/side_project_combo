package com.a30corner.combomaster.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;

public class MonsterBoxList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.box_list);

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MonsterBoxList.this, QuickSetupActivity.class));
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MonsterBoxList.this, TeamActivity.class));
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MonsterBoxList.this, NewMonsterBoxActivity.class));
            }
        });

        final SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        boolean viewpager = pref.getBoolean("viewpager", false);
        CheckBox checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.edit().putBoolean("viewpager", isChecked).apply();

                ComboMasterApplication.getsInstance().putGaAction("viewpager", "set", ""+isChecked);
            }
        });
        checkBox.setChecked(viewpager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
