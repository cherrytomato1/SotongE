package com.example.hyokwon.sotong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.hyokwon.sotong.URLS.server;

public class WriteboardActivity extends Activity {
    Button write;
    EditText textboard, titleboard;
    ImageButton backbtn;
    String text, title, date, name, LECN, BID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writeboard);
        write = (Button) findViewById(R.id.make);
        textboard = (EditText) findViewById(R.id.textboard);
        backbtn = (ImageButton) findViewById(R.id.make_backbtn);
        titleboard = (EditText) findViewById(R.id.title);

        Intent ii = getIntent();
        name = ii.getStringExtra("NAME");
        LECN = ii.getStringExtra("LECTURE");
        BID = ii.getStringExtra("BID");


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Log.d("디버그", "bid : " + BID);

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat format1 = new SimpleDateFormat("MM월 dd일 HH시 mm분");
                Calendar time = Calendar.getInstance();
                Intent i = new Intent(getApplicationContext(), WriteboardActivity.class);

                setResult(RESULT_OK, i);

                INDAT ind = new INDAT();
                ind.execute(server + "/insertBOARD.php", LECN, titleboard.getText().toString(), textboard.getText().toString(), format1.format(time.getTime()), null);
                finish();

            }
        });

    }
}