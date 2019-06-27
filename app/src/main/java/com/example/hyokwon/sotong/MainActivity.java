package com.example.hyokwon.sotong;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.hyokwon.sotong.URLS.server;

public class MainActivity extends AppCompatActivity{
    String ID, NAME, ENABLE;
    EditText etID, etPW;
    String TAG = "디버그";
    AlertDialog alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ImageButton jobtn = (ImageButton) findViewById(R.id.joinbtn);
        // ImageButton logbtn = (ImageButton) findViewById(R.id.loginbtn);
        etID = (EditText) findViewById(R.id.etLGNid);
        etPW = (EditText) findViewById(R.id.etLGNpw);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("소통e 소개");
        builder.setIcon(R.drawable.ic_launcher_sogtonge_round);
        builder.setMessage("어서오세요 소통e 어플은 교수님과 학생간의 소통을 위한 어플입니다. \n\n "+" 1. 로그인을 한 뒤 수강중인 강의를 추가한다.\n " +
                " 2. 메뉴에서 강의를 선택하고 원하는 메뉴를 수행한다.");
        builder.setPositiveButton("확인", null);
        alert = builder.create();
   //     alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE)); //배경바꾸기
    }
    public void btnHELP(View v) {
        alert.show();
    }
    public void btnLOGIN(View v) {
        Log.d(TAG,"ID/PW 확인"+etID.getText().toString()+etPW.getText().toString());
        if(etID.getText().toString().equals("") || etPW.getText().toString().equals("")) {
            Log.d(TAG,"ID/PW 공백");
            Toast.makeText(getApplicationContext(), "ID와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        InsertData inDATAlogin = new InsertData();

        try {
            String res = inDATAlogin.execute(etID.getText().toString(), etPW.getText().toString()).get();
            Log.d(TAG,"반환값 : "+res);
            if (!res.equals("ERROR!\n")) {
                Log.d(TAG,"로그인 성공");
                ID = res.split("\n")[0];
                NAME = res.split("\n")[1];
                ENABLE = res.split("\n")[2];
                if(Integer.parseInt(ENABLE)==1) {
                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                    i.putExtra("NAME",NAME);
                    i.putExtra("ID",ID);
                    startActivity(i);
                }
                else if(Integer.parseInt(ENABLE)==2) {
                    Intent i = new Intent(getApplicationContext(), waitActivity.class);
                    i.putExtra("NAME",NAME);
                    i.putExtra("ID",ID);
                    startActivity(i);
                }
                else if(Integer.parseInt(ENABLE)==4) {
                    Intent i = new Intent(getApplicationContext(), Menu2Activity.class);
                    i.putExtra("NAME", NAME);
                    i.putExtra("ID", ID);
                    startActivity(i);
                }
                else {//캐매러
                    Intent i = new Intent(getApplicationContext(), CameraActivity.class);
                    i.putExtra("NAME",NAME);
                    i.putExtra("ID",ID);
                    startActivity(i);
                }
              //  Toast.makeText(getApplicationContext(), ID + NAME+"로 로그인", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "ID와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "ID/PW ERROR :");
            }
        } catch (Exception e) {
            Log.e(TAG, "GET ERROR :", e);
        }

    }

    public void btnNEWacntPAGE(View v) {
        Intent itnt = new Intent(getApplicationContext(), JoinActivity.class);
        startActivity(itnt);
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;              //대기시간을 위한 그래픽효과


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {       //doinbackground 에서 넘어온 리턴값
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String uid = (String) params[0];
            String upwd = (String) params[1];

            //      String serverURL = (String)params[0];
            String postParameters = "u_id=" + uid + "&u_pw=" + upwd + "";


            try {

                URL url = new URL(server + "/login.php");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                Log.d(TAG, "POST 세팅");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                //    httpURLConnection.getInputStream();
                Log.d(TAG, "아웃풋 스트림 설정 성공");
                outputStream.write(postParameters.getBytes("UTF-8"));
                Log.d(TAG, "전송");
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;        //인풋스트림 설정
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {   //연결이 ok이면
                    inputStream = httpURLConnection.getInputStream();   //인풋스트림을 받아온다
                } else {
                    inputStream = httpURLConnection.getErrorStream();   //에러 스트림 받기
                }

                //인풋스트림 리더에 인풋스트림 삽입, 버퍼리더에 인풋스트림 리더 삽입
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "\n");
                }


                bufferedReader.close();


                return sb.toString();           //리턴값은 on postexecute로 간다.


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
}