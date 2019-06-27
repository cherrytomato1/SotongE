package com.example.hyokwon.sotong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.hyokwon.sotong.URLS.server;

public class evalActivity extends Activity {
    Button evalfbtn, HELPbtn;
    String NAME, ID, LECT, Q_1, Q_2, Q_3, Q_4, Q_5;

    TextView Qname[]=new TextView[5];
    int QID[] = new int[]{ R.id.qnm1, R.id.qnm2, R.id.qnm3, R.id.qnm4, R.id.qnm5 };

    EditText et;
    TextView name_num, lectname, pfsrname;
    RadioGroup Q1, Q2, Q3, Q4, Q5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eval);
        evalfbtn = (Button) findViewById(R.id.evalfinish);
        name_num = (TextView) findViewById(R.id.namenum);
        HELPbtn = (Button) findViewById(R.id.help);
        lectname = (TextView) findViewById(R.id.sub);
        Q1 = (RadioGroup) findViewById(R.id.q1);
        Q2 = (RadioGroup) findViewById(R.id.q2);
        Q3 = (RadioGroup) findViewById(R.id.q3);
        Q4 = (RadioGroup) findViewById(R.id.q4);
        Q5 = (RadioGroup) findViewById(R.id.q5);
        et = (EditText) findViewById(R.id.evalEt);
        Intent intent = getIntent();
        NAME = intent.getStringExtra("NAME");
        ID = intent.getStringExtra("ID");
        LECT = intent.getStringExtra("LECTURE");
        pfsrname = (TextView) findViewById(R.id.pro);
        name_num.setText(NAME + "(" + ID + ")");
        lectname.setText(LECT);


        for(int i=0; i<5; i++)
            Qname[i] = (TextView) findViewById(QID[i]);


        INDAT ind2 = new INDAT();
        try {
            Log.d("디버그", "1단계 del");
            String res = ind2.execute(server + "/getPfsrNAME.php", LECT, null, null, null, null).get();
            Log.d("디버그", "2단계 del");
            BufferedReader reader = new BufferedReader(new StringReader(res));
            Log.d("디버그", "3단계 del");
            String line;
            while ((line = reader.readLine()) != null) {
                pfsrname.setText(line + " 교수님");
                //Toast.makeText(evalActivity.this, line, Toast.LENGTH_SHORT).show();
            }
            Log.d("디버그", "4단계 del");
        } catch (Exception e) {
            Log.e("디버그", "ERROR set-del", e);
        }

        INDAT ind3 = new INDAT();
        int i=0;

        try {
            Log.d("디버그", "1단계-접속");
            String res = ind3.execute(server + "/load_QUESTION.php", LECT, null, null, null, null).get();            //결과를 res에 받음
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
        HELPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert;
                AlertDialog.Builder builder = new AlertDialog.Builder(evalActivity.this);
                builder.setTitle("도움말");
                builder.setIcon(R.drawable.ic_launcher_sogtonge_round);
                builder.setMessage(" 선택한 강의에 대해 평가를 할 수 있습니다.\n\n" + " 각 항목에 대해 체크를 하신 뒤 교수님에게 하고싶은 말씀을 작성하면 됩니다.\n" +
                        " 모두 작성하신 뒤 완료 버튼을 누르면 평가가 완료 됩니다.");
                builder.setPositiveButton("확인", null);
                alert = builder.create();
                alert.show();
            }
        });
        evalfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                INDAT ind = new INDAT();

                //  if(rBtn1.isChecked() == false || rBtn2.isChecked() == false || rBtn3.isChecked()==false) {
                //         Log.e("디버그","선택 x ");
                //          Toast.makeText(getApplicationContext(), "모든 항목을 체크해주세요.", Toast.LENGTH_SHORT).show();
                //       }
                //      else {
                if (Q1.getCheckedRadioButtonId() == -1 || Q2.getCheckedRadioButtonId() == -1 || Q3.getCheckedRadioButtonId() == -1 || Q4.getCheckedRadioButtonId() == -1 || Q5.getCheckedRadioButtonId() == -1)
                    Toast.makeText(evalActivity.this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();

                else {

                    Q_1 = ((RadioButton) findViewById(Q1.getCheckedRadioButtonId())).getText().toString() + ".";
                    Q_2 = ((RadioButton) findViewById(Q2.getCheckedRadioButtonId())).getText().toString() + ".";
                    Q_3 = ((RadioButton) findViewById(Q3.getCheckedRadioButtonId())).getText().toString() + ".";
                    Q_4 = ((RadioButton) findViewById(Q4.getCheckedRadioButtonId())).getText().toString() + ".";
                    Q_5 = ((RadioButton) findViewById(Q5.getCheckedRadioButtonId())).getText().toString() + ".";

                    Toast.makeText(getApplicationContext(), "평가를 완료 했습니다!!", Toast.LENGTH_SHORT).show();


                    SimpleDateFormat format1 = new SimpleDateFormat("MM월 dd일 HH시 mm분");
                    Calendar time = Calendar.getInstance();
                    String now = format1.format(time.getTime());


                    ind.execute(server + "/insertFEED.php", ID, LECT, Q_1 + Q_2 + Q_3 + Q_4 + Q_5, et.getText().toString(), now);
                    finish();
                }
                //    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                //   startActivity(i);
                //    }
                //    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                //   startActivity(i);
            }
        });
    }
}