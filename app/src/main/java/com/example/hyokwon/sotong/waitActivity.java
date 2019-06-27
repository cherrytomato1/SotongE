package com.example.hyokwon.sotong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class waitActivity extends Activity {
    String NAME, ID;
    Button btnREPER, btnHELP;
    ImageButton btnBACK;
    TextView name_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        btnBACK = (ImageButton) findViewById(R.id.wait_backbtn);
        btnREPER = (Button) findViewById(R.id.rePER);
        btnHELP = (Button) findViewById(R.id.help);
        name_num = (TextView) findViewById(R.id.namenum);
        Intent intent = getIntent();
        NAME = intent.getStringExtra("NAME");
        ID = intent.getStringExtra("ID");
        name_num.setText(NAME + "(" + ID + ")");
        btnBACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                ;
            }
        });
        btnHELP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert;
                AlertDialog.Builder builder = new AlertDialog.Builder(waitActivity.this);
                builder.setTitle("도움말");
                builder.setIcon(R.drawable.ic_launcher_sogtonge_round);
                builder.setMessage(" 인증메일을 전송한 뒤 인증 대기중인 화면입니다. \n"+" 메일 전송 후 1시간 이내로 인증이 완료됩니다.\n"+" 인증이 완료되면 정상적으로 어플을 이용할 수 있습니다.");
                builder.setPositiveButton("확인", null);
                alert = builder.create();
                alert.show();
            }
        });
        btnREPER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CameraActivity.class);
                i.putExtra("NAME",NAME);
                i.putExtra("ID",ID);
                startActivity(i);
            }
        });

    }
}
