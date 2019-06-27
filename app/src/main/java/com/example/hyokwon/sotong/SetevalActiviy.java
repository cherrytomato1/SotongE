package com.example.hyokwon.sotong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.StringReader;

import static com.example.hyokwon.sotong.URLS.server;

public class SetevalActiviy extends Activity {
    EditText set_term;
    EditText setQ[]=new EditText[5];
    int QID[] = new int[]{ R.id.set_Q1, R.id.set_Q2, R.id.set_Q3, R.id.set_Q4, R.id.set_Q5 };
    String  TERM,Question;
    String ID,LECN,NAME;
    TextView pname,lecname;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seteval);

        set_term = (EditText) findViewById(R.id.set_term);      // 평가 기간
        pname = (TextView) findViewById(R.id.namenum);          // 교수 이름
        lecname = (TextView) findViewById(R.id.lectname);


        INDAT ind = new INDAT();
        Intent itt = getIntent();
        ID = itt.getStringExtra("ID");
        LECN = itt.getStringExtra("LECTURE");
        NAME = itt.getStringExtra("NAME");
        pname.setText(NAME);
        lecname.setText(LECN);

        for(int i=0; i<5; i++)
            setQ[i] = (EditText) findViewById(QID[i]);



        INDAT ind2 = new INDAT();
        int i=0;

        try {
            Log.d("디버그", "1단계-접속");
            String res = ind2.execute(server + "/load_QUESTION.php", LECN, null, null, null, null).get();            //결과를 res에 받음
            Log.d("디버그", "2단계-접완");
            BufferedReader reader = new BufferedReader(new StringReader(res));                          //받은 결과를 버퍼 리더에 얹음
            Log.d("디버그", "3단계-버퍼끝");
            String line;
            while ((line = reader.readLine()) != null) {                                                  //한 줄로 받아서 한 줄씩 어레이 리스트에 추가.
                if(i==0)
                    set_term.setText(line);
                else if(i<6){
                    i--;
                    Log.d("디버그","점수 공간확인 "+i);

                    setQ[i].setText(line);
                    Log.d("디버그","점수 넣기 "+line);
                    i++;
                }
                i++;
            }
            Log.d("디버그", "4단계!!!!");
        } catch (Exception e) {
            Log.e("디버그", "ERROR get feed", e);
        }
    }
    public void HELPbtn(View v) {
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(SetevalActiviy.this);
        builder.setTitle("도움말");
        builder.setIcon(R.drawable.ic_launcher_sogtonge_round);
        builder.setMessage(" 이 메뉴는 교수님이 직접 강의평가 문항을 수정할 수 있는 메뉴 입니다.\n\n" +" 1~5번 문항과 평가 기간을 직접 작성한 뒤 완료 버튼을 누르면 됩니다.");
        builder.setPositiveButton("확인", null);
        alert = builder.create();
        alert.show();
    }
    public void BACKbtn(View v) {


        finish();
    }

    public void finish(View v){

        Question="";
        for(int i=0; i<5 ; i++)
        {
            Question=Question + setQ[i].getText().toString()+".";
        }

        TERM = set_term.getText().toString();

        INDAT ind =new INDAT();

        ind.execute(server + "/insertSUBoption.php",ID,LECN,Question,TERM,null);
        finish();
    }
}