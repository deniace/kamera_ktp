package com.deni.kameraktp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Deni Supriyatna
 */

public class MainActivity extends AppCompatActivity {

    // 1
    Button btnVerifikasi, btnTextureTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnVerifikasi = findViewById(R.id.btn_verifikasi);
        btnVerifikasi.setOnClickListener(btnVerifikasiClik);
    }

    View.OnClickListener btnVerifikasiClik = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, InfoVerifikasiKtpActivity.class);
            startActivity(intent);
        }
    };

}
