package com.example.hyokwon.sotong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class optionActivity extends Activity {
    String NAME, ID;
    TextView name_num;
    ImageButton exit, pwdchk, lectin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        name_num = (TextView) findViewById(R.id.namenum);
        exit = (ImageButton) findViewById(R.id.opt_backbtn);
        pwdchk = (ImageButton) findViewById(R.id.pwdChkbtn);
        lectin = (ImageButton) findViewById(R.id.lectbtn);

        Intent intent = getIntent();
        NAME = intent.getStringExtra("NAME");
        ID = intent.getStringExtra("ID");
        name_num.setText(NAME + "(" + ID + ")");
        pwdchk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), pwdActivity.class);
                i.putExtra("NAME", NAME);
                i.putExtra("ID", ID);
                startActivity(i);
            }
        });
       lectin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(getApplicationContext(), lectureActivity.class);
               i.putExtra("NAME",NAME);
               i.putExtra("ID",ID);
               startActivity(i);
           }
       });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
