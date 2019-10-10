package com.a30corner.combomaster.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

public class EggResultActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.result);

        int id = getIntent().getIntExtra("card", 0);
        if(id == 0) {
            finish();
        }
        final String key = getIntent().getStringExtra("machine");
        final SimpleDraweeView view = findViewById(R.id.cardView);
        File f = new File(getFilesDir(), id + "i.png");
        Uri uri;
        if(f.exists()) {
            uri = Uri.fromFile(f);
        } else {
            uri = Uri.parse("http://188.166.227.62/combomaster/images/" + id + "i.png");
        }
        DraweeController controller =
                Fresco.newDraweeControllerBuilder()
                        .setOldController(view.getController())
                        .setUri(uri)
                        .build();
        view.setController(controller);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EggResultActivity.this, EggMachineActivity.class);
                intent.putExtra("machine", key);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                finish();
            }
        });

        ComboMasterApplication.getsInstance().putGaAction("egg", "draw", "result");
    }
}
