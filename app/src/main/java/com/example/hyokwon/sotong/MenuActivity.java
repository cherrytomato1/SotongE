package com.example.hyokwon.sotong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.hyokwon.sotong.URLS.server;

public class MenuActivity extends Activity {
    ImageButton eval, exit, last_eval, board, back, pwdChk, lectIN;
    TextView namenum;
    Button btnHELP;
    Spinner spinner;
    String NAME, ID, LECTURE,LASTFEED="";
    Integer TERM=0;
    String STAT = "STU";

    ArrayList arrayList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        NAME = intent.getStringExtra("NAME");
        ID = intent.getStringExtra("ID");
        namenum = (TextView) findViewById(R.id.namenum);
        eval = (ImageButton) findViewById(R.id.evalbtn);
        btnHELP = (Button) findViewById(R.id.help);
        exit = (ImageButton) findViewById(R.id.exitbtn);
        last_eval = (ImageButton) findViewById(R.id.last_evalbtn);
        board = (ImageButton) findViewById(R.id.boardbtn);
        back = (ImageButton) findViewById(R.id.menu_backbtn);
        pwdChk = (ImageButton) findViewById(R.id.pwdChkbtn);
        lectIN = (ImageButton) findViewById(R.id.lectinbtn);
        spinner = (Spinner) findViewById(R.id.spinner1);
        namenum.setText(NAME + "(" + ID + ")");
        // final String[] subject = {"모바일 프로그래밍", "현대인과 기독교", "확룰과 랜덤 프로세스", "임베디드 운영체제", "임베디드 시스템", "디지털 통신 시스템"};
        arrayList = new ArrayList<String>();
        INDAT ind = new INDAT();
        try {
            Log.d("디버그", "1단계");
            String res = ind.execute(server + "/load_user-subList.php", ID, null, null, null, null).get();            //결과를 res에 받음
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
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // namenum.setText(""+parent.getItemAtPosition(position));
                LECTURE = "" + parent.getItemAtPosition(position);
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), BoardActivity.class);
                i.putExtra("NAME", NAME);
                i.putExtra("ID", ID);

                i.putExtra("LECTURE", LECTURE);
                i.putExtra("STAT", STAT);
                if (LECTURE == null)
                    Toast.makeText(getApplicationContext(), "강의를 먼저 선택해주세요.", Toast.LENGTH_SHORT).show();
                else
                    startActivity(i);
            }
        });
        eval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), evalActivity.class);
                i.putExtra("NAME", NAME);
                i.putExtra("ID", ID);
                i.putExtra("LECTURE", LECTURE);



                if (LECTURE == null)
                    Toast.makeText(getApplicationContext(), "강의를 먼저 선택해주세요.", Toast.LENGTH_SHORT).show();
                else {

                    INDAT ind = new INDAT();
                    try {
                        Log.d("디버그", "1단계");
                        String res = ind.execute(server + "/load_LASTFEED.php", ID, LECTURE, null, null, null).get();            //결과를 res에 받음
                        Log.d("디버그", "2단계");
                        BufferedReader reader = new BufferedReader(new StringReader(res));                          //받은 결과를 버퍼 리더에 얹음
                        Log.d("디버그", "3단계");
                        String line;
                        int c = 0;
                        while ((line = reader.readLine()) != null) {                                                  //한 줄로 받아서 한 줄씩 어레이 리스트에 추가.
                            if (c == 0)
                                LASTFEED = line;
                            else
                                TERM = Integer.parseInt(line);
                            c++;
                        }
                        Log.d("디버그", "LASTFEED.1" + LASTFEED);
                        if (LASTFEED.length()>3) {
                            SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일 HH시 mm분");
                            Date date = sdf.parse(LASTFEED);
                            Log.d("디버그", "4단계.2");


                            Calendar now = Calendar.getInstance();
                            Date nowdate = sdf.parse(sdf.format(now.getTime()));
                            //String temp = new SimpleDateFormat("MM월 dd일 HH시 mm분").format(date);

                            Log.d("디버그", "5단계");
                            long diff = date.getTime() - nowdate.getTime();

                            long diffDays = diff / (24 * 60 * 60 * 1000);
                            int idiff = (int) diffDays;
                            Log.d("디버그", "diffff----" + Long.toString(diffDays));


                            if (diffDays < TERM) {
                                Toast.makeText(MenuActivity.this, "최근에 평가를 하셨어요. " + (TERM - diffDays) + " 일 뒤에 다시 해주세요.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                startActivity(i);
                            }
                        }
                        else
                        {
                            Log.d("디버그", "LAST 0");
                            startActivity(i);
                        }
                    } catch (Exception e) {
                        Log.e("디버그", "ERROR setlist", e);
                    }
                }


            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
        });
        pwdChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), pwdActivity.class);
                i.putExtra("NAME", NAME);
                i.putExtra("ID", ID);
                i.putExtra("LECTURE", LECTURE);
                i.putExtra("STAT", STAT);
                startActivity(i);
            }
        });
        lectIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), lectureActivity.class);
                i.putExtra("NAME", NAME);
                i.putExtra("ID", ID);
                i.putExtra("LECTURE", LECTURE);
                i.putExtra("STAT", STAT);
                startActivityForResult(i, 0);


            }
        });
        last_eval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), feedBackListActivity.class);
                i.putExtra("NAME", NAME);
                i.putExtra("LECTURE", LECTURE);
                i.putExtra("STAT", STAT);
                i.putExtra("ID", ID);
                if (LECTURE == null)
                    Toast.makeText(getApplicationContext(), "강의를 먼저 선택해주세요.", Toast.LENGTH_SHORT).show();
                else
                    startActivity(i);
            }
        });
        btnHELP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert;
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                builder.setTitle("도움말");
                builder.setIcon(R.drawable.ic_launcher_sogtonge_round);
                builder.setMessage(" 먼저 강의를 선택한 뒤 하단 메뉴를 선택합니다. \n\n"+" 1. 평가하기 : 해당 강의에 대해 평가하는 기능 \n"+
                        " 2. 게시판 : 교수님이 올려주신 공지사항을 확인할 수 있는 기능 \n" + " 3. 지난평가 : 자신이 작성한 평가에 대해 교수님의 피드백을 확인하는 기능 \n" +
                        " 4. PW재설정 : 비밀번호 재설정 기능\n" + " 5. 강의입력 : 자신이 수강중인 강의를 추가하는 기능\n" + " 6. 종료 : 어플리케이션 종료");
                builder.setPositiveButton("확인", null);
                alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            INDAT ind = new INDAT();
            arrayList.clear();
            try {
                Log.d("디버그", "1단계");
                String res = ind.execute(server + "/load_user-subList.php", ID, null, null, null, null).get();            //결과를 res에 받음
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
            adapter.notifyDataSetChanged();
            spinner.setAdapter(adapter);

        } else {   // RESULT_CANCEL
        }
//        } else if (requestCode == REQUEST_ANOTHER) {
//            ...
    }
}