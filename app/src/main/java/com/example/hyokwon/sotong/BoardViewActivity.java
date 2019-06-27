package com.example.hyokwon.sotong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.StringReader;

import static com.example.hyokwon.sotong.URLS.server;

public class BoardViewActivity extends Activity {
    TextView name, date, title, text;
    ImageButton back;
    Button DELbtn, CHKbtn;
    LinearLayout mLayout, mLayout2, mLayout3, bar;
    String STAT,NAME,LECN;                                       //이 xmlㅇ에서의 NAME은 BID임
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardview);
        name = (TextView) findViewById(R.id.name);
        date = (TextView) findViewById(R.id.date);
        title = (TextView) findViewById(R.id.title);
        DELbtn = (Button) findViewById(R.id.DELbtn);
        CHKbtn = (Button) findViewById(R.id.CHKbtn);
        text = (TextView) findViewById(R.id.text);
        back = (ImageButton) findViewById(R.id.view_backbtn) ;
        mLayout= (LinearLayout) findViewById(R.id.mLayout);
        mLayout2 = (LinearLayout) findViewById(R.id.mLayout2);
        mLayout3= (LinearLayout) findViewById(R.id.mLayout3);
        bar = (LinearLayout) findViewById(R.id.bar);
        Intent i =getIntent();
        STAT = i.getStringExtra("STAT");

        date.setText(i.getStringExtra("BDATE"));
        title.setText(i.getStringExtra("BTITLE"));
        text.setText(i.getStringExtra("BTEXT"));
        NAME=i.getStringExtra("BNAME");


        INDAT ind = new INDAT();
        try {
            Log. d("디버그","1단계");
            String res= ind.execute(server+"/getSubNAME.php",NAME,null,null,null, null).get();
            Log. d("디버그","2단계");
            BufferedReader reader = new BufferedReader(new StringReader(res));
            Log. d("디버그","3단계");
            String line;
            while((line = reader.readLine())!= null) {

                LECN=line;
                name.setText(LECN);
            }
            Log. d("디버그","4단계 : "+ line);
        }catch (Exception e){
            Log.e("디버그","ERROR setlist",e);
        }


        if(STAT.equals("PRO")){
            mLayout.setBackgroundResource(R.drawable.probackground);
            mLayout2.setBackgroundResource(R.drawable.pro_nemo);
            mLayout3.setBackgroundResource(R.drawable.pro_nemo);
            bar.setBackgroundColor(Color.rgb(255, 181, 161));
            CHKbtn.setVisibility(View.GONE);
        }
        else {
            DELbtn.setVisibility(View.GONE);
            CHKbtn.setVisibility(View.GONE);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    public void del(View v){
        AlertDialog.Builder dialog = new AlertDialog.Builder(BoardViewActivity.this);
        dialog.setTitle("삭제");
        dialog.setMessage("삭제하시겠습니까?");
        dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                INDAT ind = new INDAT();
                ind.execute(server+"/delBOARD.php",NAME,null,null,null, null);
                setResult(RESULT_OK);
                finish();
            }
        });
        dialog.setNegativeButton("아니요", null);
     /*   INDAT ind = new INDAT();
        ind.execute(server+"/delBOARD.php",NAME,null,null,null, null);
        setResult(RESULT_OK);
        finish();*/
        dialog.show();
    }
    public void fix(View v){
        Intent i = new Intent(getApplicationContext(), WriteboardActivity.class);

        i.putExtra("LECTURE", LECN);
        i.putExtra("BID",NAME);
        startActivity(i);

        INDAT ind = new INDAT();
        ind.execute(server + "/insertBOARD.php", LECN , title.getText().toString(),text.getText().toString(), "1", NAME);
        setResult(RESULT_OK);
        finish();
    }
}