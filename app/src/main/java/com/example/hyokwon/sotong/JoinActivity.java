package com.example.hyokwon.sotong;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import static com.example.hyokwon.sotong.URLS.server;

public class JoinActivity extends Activity {

    ImageView img;
    Button btnCKID, btnFINISH;
    String TAG = "디버그";
    EditText etID, etPWD, etPWDchk;

    @Override
    protected void onCreate(Bundle saveInStanceState) {
        super.onCreate(saveInStanceState);
        setContentView(R.layout.activity_join);
        ImageButton backbtn = (ImageButton) findViewById(R.id.backbtn);
        btnFINISH = (Button) findViewById(R.id.finishbtn);
        btnCKID=(Button) findViewById(R.id.chkID);
        etID= (EditText) findViewById(R.id.etId);
        etPWD= (EditText) findViewById(R.id.etPwd);
        etPWDchk= (EditText) findViewById(R.id.etPwdChk);
    }

    public void btnNEWacnt(View v) {
        InsertData insertDATA = new InsertData();
        if(btnCKID.isClickable()==false)            //아이디 중복 체크를 실행한 경우
        {
            if(etPWD.getText().toString().equals(etPWDchk.getText().toString())) {      //비밀번호두개가 일치할 경우
                insertDATA.execute(etID.getText().toString(), etPWD.getText().toString());
                Toast.makeText(getApplicationContext(), "회원가입성공", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(i);
                finish();
            }
            else
                Toast.makeText(getApplicationContext(), "비밀번호를 동일하게 작성하세요", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(), "ID 중복을 확인하세요", Toast.LENGTH_SHORT).show();


    }

    public void CHKIDL(View v){             //아이디 주복확인
        InsertData inDatCKID = new InsertData();

        try {
            String RESULT = inDatCKID.execute(etID.getText().toString(), "#").get();    //get 반환
            if(RESULT.equals("TRUE")) {
                btnCKID.setTextColor(Color.GRAY);
                Toast.makeText(getApplicationContext(), "사용 가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();
                btnCKID.setClickable(false);
            }
            else if(RESULT.equals("FALSE"))
                Toast.makeText(getApplicationContext(), "중복된 아이디 입니다.", Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            Log.e(TAG,"GET ERROR :", e);
        }

    }
    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;              //대기시간을 위한 그래픽효과

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(JoinActivity.this,
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

            String uid = (String)params[0];
            String upwd = (String)params[1];

            //      String serverURL = (String)params[0];
            String postParameters = "u_id=" + uid + "&u_pw=" + upwd+"";


            try {

                URL url = new URL(server+"/appdata.php");
                if(upwd.equals("#")==true)           //id 중복체크일경우
                    url=new URL(server+"/checkID.php");
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
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {   //연결이 ok이면
                    inputStream = httpURLConnection.getInputStream();   //인풋스트림을 받아온다
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();   //에러 스트림 받기
                }

                //인풋스트림 리더에 인풋스트림 삽입, 버퍼리더에 인풋스트림 리더 삽입
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
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