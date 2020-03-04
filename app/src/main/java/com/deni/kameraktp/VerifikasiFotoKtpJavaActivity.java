package com.deni.kameraktp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.deni.kameraktp.databinding.ActivityVerifikasiFotoKtpJavaBinding;

public class VerifikasiFotoKtpJavaActivity extends AppCompatActivity {

    //4
    ActivityVerifikasiFotoKtpJavaBinding binding;
    private final String ktp_bitmap = "KTPBITMAP";
    private Bitmap bitmapKtp;
    private byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_verifikasi_foto_ktp_java);

        binding.btnUlangiKtp.setOnClickListener(kembali);
        binding.btnGunakanKtp.setOnClickListener(gunakan);

        // check intent bawa extra atau tidak
        if(getIntent().hasExtra(ktp_bitmap)){
//            mengambil data dari intent extra
            byteArray = getIntent().getByteArrayExtra(ktp_bitmap);
            // mengkonversi data bitearray menjadi bitmap/gambar
            bitmapKtp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            // menampilkan gambar ke imageView
            binding.ivVkKtpHasilCapture.setImageBitmap(bitmapKtp);
        }else{
            binding.ivVkKtpHasilCapture.setImageResource(R.drawable.camera_circle);
        }
    }

    View.OnClickListener kembali = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            bitmapKtp = null;
            byteArray = null;
            finish();
        }
    };

    View.OnClickListener gunakan = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(VerifikasiFotoKtpJavaActivity.this, HasilVerifikasiKtpJavaActivity.class);
            intent.putExtra(ktp_bitmap, byteArray);
            startActivity(intent);
        }
    };

}

