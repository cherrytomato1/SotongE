package com.example.hyokwon.sotong;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import static com.example.hyokwon.sotong.URLS.server;

public class CameraActivity extends Activity {
    private Activity JoinActivity = this;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    String NAME, ID, url = server + "/setENABLE.php";
    ImageView img;
    ImageButton btnBACK;
    ProgressDialog progressDialog;
    Button injengbtn, HELPbtn;
    TextView namenum;
    public void loading() {
        //로딩
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog = new ProgressDialog(CameraActivity.this);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("인증 메일 전송중...");
                        progressDialog.show();
                    }
                }, 0);
    }

    public void loadingEnd() {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 0);
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath, String filename) {
        File file = new File(strFilePath);
        // If no folders
        if (!file.exists()) {
            file.mkdirs();
            // Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }
        File fileCacheItem = new File(strFilePath + filename);
        OutputStream out = null;
        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onCreate(Bundle saveInStanceState) {
        super.onCreate(saveInStanceState);
        setContentView(R.layout.activity_camera);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
        Intent intent = getIntent();
        NAME = intent.getStringExtra("NAME");
        ID = intent.getStringExtra("ID");
        final String adr = "rlagyrnjs12@naver.com";
        injengbtn = (Button) findViewById(R.id.injeng);
        img = (ImageView) findViewById(R.id.face);
        btnBACK = (ImageButton) findViewById(R.id.camera_backbtn);
        HELPbtn = (Button) findViewById(R.id.help);
        namenum = (TextView) findViewById(R.id.namenum);
        Button btnCAMERA = (Button) findViewById(R.id.camerabtn);
        namenum.setText(NAME + "(" + ID + ")");
        btnBACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnCAMERA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionReadStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                int permissionWriteStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionReadStorage == PackageManager.PERMISSION_DENIED || permissionWriteStorage == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(JoinActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
                    Toast.makeText(getApplicationContext(), "권한을 요청 합니다. 버튼을 다시 눌러주십시오.", Toast.LENGTH_SHORT).show();
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });
        HELPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert;
                AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
                builder.setTitle("도움말");
                builder.setIcon(R.drawable.ic_launcher_sogtonge_round);
                builder.setMessage("학생증 인증을 완료해야 원할한 기능을 수행할 수 있습니다. \n\n" +" 1. 먼저 CAMERA버튼을 클릭해 자신의 학생증을 찍습니다. \n"+" 2. 학생증 사진을 찍으면 인증 버튼이 활성화 됩니다.\n"+
                " 3. 인증 버튼을 클릭하여 인증메일을 발송합니다. \n" +" 4. 인증은 1시간 이내로 완료돱니다.");
                builder.setPositiveButton("확인", null);
                alert = builder.create();
                alert.show();
            }
        });
        injengbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading();
                try {

                    GMailSender gMailSender = new GMailSender("gyrnjs1277@gmail.com", "slaustkddlek1!");
                    gMailSender.sendMail2("소통E 인증요청 : " + ID + NAME, NAME, adr, "/sdcard/DCIM/Camera/test11.jpg", "zz.jpg"); // 이미지 메일 전송
                    Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
                    loadingEnd();
                    File file = new File("/sdcard/DCIM/Camera/test11.jpg"); // 이미지 전송 후 파일 삭제
                    boolean deleted = file.delete();
                    INDAT indata = new INDAT();
                    indata.execute(url, ID, "0", "0", null, null);
                    finish();
                } catch (SendFailedException e) {
                    Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                } catch (MessagingException e) {
                    Toast.makeText(getApplicationContext(), "사진 전송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("mail", "디버그" + e.toString());
                    Log.e("mail", "디버그" + e.getMessage());
                    Log.d("mail", "Exception occured : ");
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }public Bitmap rotateImage(Bitmap src, float degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {     // 이미지뷰에 사진 올리기
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            injengbtn.setEnabled(true);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
          //  ((ImageView) findViewById(R.id.face)).setImageBitmap(rotateImage(imageBitmap, -90));
            ((ImageView) findViewById(R.id.face)).setImageBitmap(imageBitmap);
            SaveBitmapToFileCache(imageBitmap, "/sdcard/DCIM/Camera/", "test11.jpg");
        }
    }
}