package com.example.hyokwon.sotong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadingAcitivty extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 800);// 0.5초 정도 딜레이를 준 후 시작

    }
}
