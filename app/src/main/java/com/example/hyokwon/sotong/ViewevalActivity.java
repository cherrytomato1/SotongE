package com.example.hyokwon.sotong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.StringReader;

import static com.example.hyokwon.sotong.URLS.server;
import static java.lang.Boolean.FALSE;

public class ViewevalActivity extends Activity {
    Button finish, HELPbtn;
    EditText pro;
    TextView stu, proname, lectname;
    TextView[] Q= new TextView[5];
    String feedtext, subName ,NAME,fID,STATE,fBACK;

    TextView Qname[]=new TextView[5];
    int QID2[] = new int[]{ R.id.qnm1, R.id.qnm2, R.id.qnm3, R.id.qnm4, R.id.qnm5 };

    int[] QID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vieweval);

     /*   Q1 = (TextView) findViewById(R.id.Q1);
        Q2 = (TextView) findViewById(R.id.Q2);
        Q3 = (TextView) findViewById(R.id.Q3);
        Q4 = (TextView) findViewById(R.id.Q4);
        Q5 = (TextView) findViewById(R.id.Q5);
       */

        HELPbtn = (Button) findViewById(R.id.help);
        stu = (TextView) findViewById(R.id.stutext);                    //학생의 말
        proname = (TextView) findViewById(R.id.namenum);                //교수이름
        lectname = (TextView) findViewById(R.id.lectname);              //강의명
        finish = (Button) findViewById(R.id.finish);
        pro = (EditText) findViewById(R.id.protext);                    //피드백이 자나

        QID = new int[]{ R.id.Q1, R.id.Q2, R.id.Q3, R.id.Q4, R.id.Q5 };

        feedtext = pro.getText().toString();
        Intent ii = getIntent();
        subName = ii.getStringExtra("sNAME");             // 강의명
        NAME = ii.getStringExtra("NAME");
        fID = ii.getStringExtra("fID");
        proname.setText(NAME+" 교수님");


        for(int i=0; i<5; i++)
            Qname[i] = (TextView) findViewById(QID2[i]);

        INDAT ind3 = new INDAT();
        int i=0;

        try {
            Log.d("디버그", "1단계-접속");
            String res = ind3.execute(server + "/load_QUESTION.php", subName, null, null, null, null).get();            //결과를 res에 받음
            Log.d("디버그", "2단계-접완");
            BufferedReader reader = new BufferedReader(new StringReader(res));                          //받은 결과를 버퍼 리더에 얹음
            Log.d("디버그", "3단계-버퍼끝");
            String line;
            while ((line = reader.readLine()) != null) {                                                  //한 줄로 받아서 한 줄씩 어레이 리스트에 추가.
                if(i==0){}
                else if(i<6){
                    i--;
                    Log.d("디버그","점수 공간확인 "+i);

                    Qname[i].setText(line);
                    Log.d("디버그","점수 넣기 "+line);
                    i++;
                }
                i++;
            }
            Log.d("디버그", "4단계!!!!");
        } catch (Exception e) {
            Log.e("디버그", "ERROR get feed", e);
        }


        INDAT ind = new INDAT();
        i=0;

        try {
            Log.d("디버그", "1단계-접속");
            String res = ind.execute(server + "/viewFEED.php", fID, "1", null, null, null).get();            //결과를 res에 받음
            Log.d("디버그", "2단계-접완");
            BufferedReader reader = new BufferedReader(new StringReader(res));                          //받은 결과를 버퍼 리더에 얹음
            Log.d("디버그", "3단계-버퍼끝");
            String line;
            while ((line = reader.readLine()) != null) {                                                  //한 줄로 받아서 한 줄씩 어레이 리스트에 추가.
                if(i==0)
                    stu.setText("학생의 말 : " + line);
                else if(i<6){
                    i--;
                    Log.d("디버그","점수 공간확인 "+i);
                    Q[i] = (TextView) findViewById(QID[i]);
                    Q[i].setText(line+" 점");
                    Log.d("디버그","점수 넣기 "+line);
                    i++;
                }
                else if(i==6){
                    STATE=line;
                }
                else{
                    fBACK=line;
                    Log.d("디버그", "fback = "+fBACK);
                }

                i++;
            }
            Log.d("디버그", "4단계!!!!");
        } catch (Exception e) {
            Log.e("디버그", "ERROR get feed", e);
        }

        if(Integer.parseInt(STATE)==3) {
            //Toast.makeText(this, STATE, Toast.LENGTH_SHORT).show();
            pro.setText(fBACK);
            pro.setEnabled(FALSE);
            pro.setBackgroundResource(R.drawable.nemo_gray);
            stu.setBackgroundResource(R.drawable.nemo_gray);
            finish.setText("완료됨");
            finish.setEnabled(FALSE);
            finish.setBackgroundResource(R.drawable.nemo_gray);
        }
        lectname.setText(subName);


        setResult(RESULT_OK);


        HELPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert;
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewevalActivity.this);
                builder.setTitle("도움말");
                builder.setIcon(R.drawable.ic_launcher_sogtonge_round);
                builder.setMessage(" 학생들이 작성한 강의평가에 대해 피드백을 할 수 있습니다. \n\n"+" 피드백 버튼을 누르고나면 완료 상태가 되어 더 이상 피드백을 할 수 없습니다.");
                builder.setPositiveButton("확인", null);
                alert = builder.create();
                alert.show();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pro.getText().toString()=="")
                {
                    Toast.makeText(ViewevalActivity.this, "피드백을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    INDAT ind2 = new INDAT();
                    Log.d("디버그", "1단계-접속");
                    String res = ind2.execute(server + "/insertFEEDBACK.php", fID, pro.getText().toString(), STATE, null, null).get();            //결과를 res에 받음
                    Log.d("디버그", "2단계-접완");
                    BufferedReader reader = new BufferedReader(new StringReader(res));                          //받은 결과를 버퍼 리더에 얹음
                    Log.d("디버그", "3단계-버퍼끝");
                    String line;
                    Log.d("디버그", "4단계!!!!");
                } catch (Exception e) {
                    Log.e("디버그", "ERROR insert feed back", e);
                }
                Toast.makeText(ViewevalActivity.this, "피드백을 완료했습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    public void BACKbtn(View v) {
        finish();
    }
}