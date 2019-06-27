package com.example.hyokwon.sotong;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class INDAT extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;              //대기시간을 위한 그래픽효과
    String TAG="디버그";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //progressDialog = ProgressDialog.show(pwdActivity.this,
        //       "Please Wait", null, true, true);
        // progressDialog.show();
    }


    @Override
    protected void onPostExecute(String result) {       //doinbackground 에서 넘어온 리턴값
        super.onPostExecute(result);

        //progressDialog.dismiss();
        Log.d(TAG, "POST response  - " + result);
    }


    @Override
    protected String doInBackground(String... params) {

        String urlstr = (String) params[0];
        String uid = (String) params[1];
        String upwd = (String) params[2];
        String npwd = (String) params[3];
        String pamams4 = (String) params[4];
        String pamams5 = (String) params[5];

        //      String serverURL = (String)params[0];
        String postParameters = "&u_id=" + uid + "&u_pw=" + upwd + "&param0=" + npwd + "&param1=" + pamams4 +"&param2=" + pamams5;


        try {

            URL url = new URL(urlstr);

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