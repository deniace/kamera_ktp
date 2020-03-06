package com.deni.kameraktp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.deni.kameraktp.databinding.ActivityHasilVerifikasiKtpJavaBinding;

import java.util.Arrays;

/**
 * Created by Deni Supriyatna
 */

public class HasilVerifikasiKtpJavaActivity extends AppCompatActivity {

    //5
    private final String ktp_bitmap = "KTPBITMAP";
    private final String ktp_text = "KTPTEXT";
    ActivityHasilVerifikasiKtpJavaBinding binding;
    private Bitmap bitmapKtp;
    private byte[] byteArray;
    private String [] ktpStringArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hasil_verifikasi_ktp_java);

        if(getIntent().hasExtra(ktp_bitmap)){
            byteArray = getIntent().getByteArrayExtra(ktp_bitmap);
            ktpStringArray = new String[getIntent().getStringArrayExtra(ktp_text).length];
            ktpStringArray = getIntent().getStringArrayExtra(ktp_text);
            Log.d("array", Arrays.toString(ktpStringArray));
            // mengkonversi data bitearray menjadi bitmap/gambar
            bitmapKtp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            // menampilkan gambar ke imageView
            binding.ivHasilCaptureKtp.setImageBitmap(bitmapKtp);
        }else{
            binding.ivHasilCaptureKtp.setImageResource(R.drawable.camera_circle);
        }
        binding.btnKonfirmasiVerifikasiKtp.setOnClickListener(konfirmasi);
    }

    private Boolean isDataValid(){
        boolean valid = false;
        // mengecek apakah radio button dalam radio grup telah dipilih
        boolean isSelected = binding.rgHvKtpJenisId.getCheckedRadioButtonId() != -1;
        String namaIbuKandung = binding.tietHvKtpNamaIbu.getText().toString();
        if(!isSelected){
            Toast.makeText(this, "Pilih Salah Satu", Toast.LENGTH_SHORT).show();
            binding.rbHvKtpWna.setError("Pilih Salah Satu");
            binding.rbHvKtpWni.setError("Pilih Salah Satu");
            valid = false;
        }else if (namaIbuKandung.length() < 1){
            binding.tilHvKtpNamaIbu.setErrorEnabled(true);
            binding.tilHvKtpNamaIbu.setError("Nama Ibu Harus diisi");
            valid = false;
        }else{
            valid = true;
        }
        return valid;
    }

    // fungsi untuk mengembalikan error pada layout menjadi tidak erro
    private void nullError(){
        binding.rbHvKtpWna.setError(null);
        binding.rbHvKtpWni.setError(null);
        binding.tilHvKtpNamaIbu.setErrorEnabled(false);
        binding.tilHvKtpNamaIbu.setError(null);
    }

    View.OnClickListener konfirmasi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            nullError();
            if(isDataValid()){
                Toast.makeText(HasilVerifikasiKtpJavaActivity.this, "oke", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(HasilVerifikasiKtpJavaActivity.this, "not oke", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void ulangi(View view){
        finish();
    }
}
