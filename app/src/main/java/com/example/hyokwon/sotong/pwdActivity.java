package com.example.hyokwon.sotong;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.hyokwon.sotong.URLS.server;

public class pwdActivity extends Activity {
    ImageButton btnBACK;
    Button btnCOMPLETE;
    EditText etPW, etCHKPW, etUSEPW;
    TextView name_num;
    LinearLayout layout, bar;
    String NAME, ID, TAG = "디버그";
    String STAT;
    String url = server + "/changePW.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd);
        btnCOMPLETE = (Button) findViewById(R.id.completebtn);
        name_num = (TextView) findViewById(R.id.namenum);
        btnBACK = (ImageButton) findViewById(R.id.pwd_backbtn);
        layout = (LinearLayout) findViewById(R.id.pwdlay);
        bar = (LinearLayout) findViewById(R.id.bar);
        etPW = (EditText) findViewById(R.id.etLGNpw);
        etUSEPW = (EditText) findViewById(R.id.etUSEpw);
        etCHKPW = (EditText) findViewById(R.id.etCHKpw);

        Intent intent = getIntent();
        NAME = intent.getStringExtra("NAME");
        ID = intent.getStringExtra("ID");
        STAT = intent.getStringExtra("STAT");
        if (STAT.equals("PRO")) {
            name_num.setText(NAME + " 교수님");
            layout.setBackgroundResource(R.drawable.probackground);
            btnCOMPLETE.setBackgroundResource(R.drawable.nemo3);
            bar.setBackgroundColor(Color.rgb(255, 181, 161));
        } else
            name_num.setText(NAME + "(" + ID + ")");
        btnBACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void btnChangePW(View v) {
        Log.d(TAG, "버튼클릭, ID :" + ID);
        String PW = etPW.getText().toString();
        String CKPW = etCHKPW.getText().toString();
        String NEWPW = etUSEPW.getText().toString();

        if (CKPW.equals(NEWPW) && !NEWPW.equals("")) {
            Log.d(TAG, "비밀번호 허가");
            INDAT inDATA = new INDAT();
            try {
                String res = inDATA.execute(url, ID, PW, NEWPW, null, null).get();

                Log.d(TAG, "통신결과 :" + res);
                if (res.equals("ERROR!\n"))
                    Toast.makeText(getApplicationContext(), "현재 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getApplicationContext(), "비밀번호 변경 성공!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (Exception e) {
                Log.e(TAG, "ERROR", e);
            }
        } else {
            Log.d(TAG, "비밀번호 오류");
            Toast.makeText(getApplicationContext(), "새 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
        }
    }


}