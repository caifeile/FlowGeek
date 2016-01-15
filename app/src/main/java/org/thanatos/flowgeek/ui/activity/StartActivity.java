package org.thanatos.flowgeek.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import org.thanatos.flowgeek.R;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        new Handler().postDelayed(()->{
            finish();
            startActivity(new Intent(StartActivity.this, MainActivity.class));
        },2000);
    }


}
