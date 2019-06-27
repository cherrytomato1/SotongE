package com.example.hyokwon.sotong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import static com.example.hyokwon.sotong.URLS.server;

public class Menu2Activity extends Activity {
    ImageButton feedbtn, exitbtn, boardbtn, pwdbtn, backbtn, setbtn;
    TextView nameV;
    Button HELPbtn;
    String name, lecture, ID;
    String STAT = "PRO";
    Spinner spinner;
    //String[] lect_list1 = {"모프", "자바", "씨언어"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);
        feedbtn = (ImageButton) findViewById(R.id.feedbackbtn);
        exitbtn = (ImageButton) findViewById(R.id.exitbtn);
        backbtn = (ImageButton) findViewById(R.id.menu2_back);
        boardbtn = (ImageButton) findViewById(R.id.boardbtn);
        pwdbtn = (ImageButton) findViewById(R.id.pwdChkbtn);
        setbtn = (ImageButton) findViewById(R.id.setbtn);
        HELPbtn = (Button) findViewById(R.id.help);
        nameV = (TextView) findViewById(R.id.proname);
        spinner = (Spinner) findViewById(R.id.spinner1);
        Intent ii = getIntent();
        name = ii.getStringExtra("NAME");
        ID = ii.getStringExtra("ID");
        nameV.setText(name + " 교수님");


        ArrayList arrayList = new ArrayList<String>();          //어레이 리스트 추가


        INDAT ind = new INDAT();
        try {
            Log.d("디버그", "1단계");
            String res = ind.execute(server + "/load_pfsr-subList.php", ID, null, null, null, null).get();            //결과를 res에 받음
            Log.d("디버그", "2단계");
            BufferedReader reader = new BufferedReader(new StringReader(res));                          //받은 결과를 버퍼 리더에 얹음
            Log.d("디버그", "3단계");
            String line;
            while ((line = reader.readLine()) != null) {                                                  //한 줄로 받아서 한 줄씩 어레이 리스트에 추가.

                arrayList.add(line);
            }
            Log.d("디버그", "4단계!!!!");
        } catch (Exception e) {
            Log.e("디버그", "ERROR setlist", e);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList);

        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // namenum.setText(""+parent.getItemAtPosition(position));
                lecture = "" + parent.getItemAtPosition(position);
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        pwdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), pwdActivity.class);
                i.putExtra("STAT", STAT);
                i.putExtra("NAME", name);
                startActivity(i);
            }
        });
        feedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), feedListActivity.class);
                i.putExtra("ID", ID);
                i.putExtra("LECTURE", lecture);
                i.putExtra("NAME", name);
                startActivity(i);
            }
        });
        setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SetevalActiviy.class);
                i.putExtra("ID", ID);
                i.putExtra("LECTURE", lecture);
                i.putExtra("NAME", name);
                startActivity(i);
            }
        });
        exitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
        });
        HELPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert;
                AlertDialog.Builder builder = new AlertDialog.Builder(Menu2Activity.this);
                builder.setTitle("도움말");
                builder.setIcon(R.drawable.ic_launcher_sogtonge_round);
                builder.setMessage(" 먼저 강의를 선택한 뒤 하단 메뉴를 선택합니다. \n\n" + " 1. 피드백 : 학생들이 평가한 것에 대해 피드백을 할 수 있습니다. 글을 클릭한 후 피드백을 진행합니다.\n" +
                        " 2. 게시판 : 학생들에게 보여줄 공지사항이나 강의 정보를 게시할 수 있습니다.\n" +" 3. 항목설정 : 교수님이 직접 강의평가 문항을 수정할 수 있습니다.\n" +" 4. PW재설정 : 비밀번호를 재설정 할 수 있습니다.\n" +
                        " 5. 종료 : 어플리케이션을 종료하는 기능 입니다.");
                builder.setPositiveButton("확인", null);
                alert = builder.create();
                alert.show();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        boardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), BoardActivity.class);
                i.putExtra("STAT", STAT);
                i.putExtra("LECTURE", lecture);
                i.putExtra("NAME", name);
                startActivityForResult(i, 0);
            }
        });
    }
}