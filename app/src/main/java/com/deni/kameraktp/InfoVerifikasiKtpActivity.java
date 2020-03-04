package com.deni.kameraktp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class InfoVerifikasiKtpActivity extends AppCompatActivity {
    //2
    Button btnMulai;
    Context context = InfoVerifikasiKtpActivity.this;
    int requestCode = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_verifikasi_ktp);
        btnMulai = findViewById(R.id.btn_ktp_mulai);
        btnMulai.setOnClickListener(btnOpenCamera);
    }

    View.OnClickListener btnOpenCamera = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
//                openCamera();
                Intent intent = new Intent(InfoVerifikasiKtpActivity.this, CameraVerifikasiKtpJavaActivity.class);
                startActivity(intent);
            }else {
                ActivityCompat.requestPermissions(InfoVerifikasiKtpActivity.this, new String[]{Manifest.permission.CAMERA}, requestCode);
            }
        }
    };

    // after permision
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permisions, @NonNull int[]grandResult){
        super.onRequestPermissionsResult(requestCode, permisions, grandResult);
        if(requestCode == this.requestCode){
            if(grandResult[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(context, R.string.permission_granted, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void openCamera(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }
}
