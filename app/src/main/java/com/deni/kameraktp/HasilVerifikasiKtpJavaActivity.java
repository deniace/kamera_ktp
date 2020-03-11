package com.deni.kameraktp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.deni.kameraktp.databinding.ActivityHasilVerifikasiKtpJavaBinding;
import com.deni.kameraktp.model.UploadKtp;
import com.deni.kameraktp.retrofitinterface.UploadKtpInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Deni Supriyatna
 */

public class HasilVerifikasiKtpJavaActivity extends AppCompatActivity {

    //5
    private final String ktp_bitmap = "KTPBITMAP";
    private final String ktp_text = "KTPTEXT";
    private final String ktp_base64 = "KTPBASE64";
    ActivityHasilVerifikasiKtpJavaBinding binding;
    private Bitmap bitmapKtp;
    private byte[] byteArray;
    private String [] ktpStringArray = null;
    File fileKtp;
    private String ktpBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hasil_verifikasi_ktp_java);

        if(getIntent().hasExtra(ktp_bitmap)){
            byteArray = getIntent().getByteArrayExtra(ktp_bitmap);
            ktpStringArray = new String[15];
            ktpStringArray = getIntent().getStringArrayExtra(ktp_text);
            ktpBase64 = getIntent().getStringExtra(ktp_base64);
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
            Toast.makeText(this, R.string.pick_one, Toast.LENGTH_SHORT).show();
            binding.rbHvKtpWna.setError(getString(R.string.pick_one));
            binding.rbHvKtpWni.setError(getString(R.string.pick_one));
            valid = false;
        }else if (namaIbuKandung.length() < 1){
            binding.tilHvKtpNamaIbu.setErrorEnabled(true);
            binding.tilHvKtpNamaIbu.setError(getString(R.string.mother_name_must_filled));
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
//                saveFile(bitmapKtp);
//                a();
            }else{
                Toast.makeText(HasilVerifikasiKtpJavaActivity.this, "not oke", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void ulangi(View view){
        finish();
    }

    private void kirimKtp(){
//        NetworkClient networkClient = Ret
    }

    private void saveBitmap(String bitname, Bitmap bitmap){
//        File file = new File(Environment.getExternalStorageDirectory().toString()+"/"+bitname+"png");
        File file = new File(getCacheDir(), "image_ktp.png");

        try {
            file.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        }catch (IOException e){
            e.printStackTrace();
        }

        try {
            fOut.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void saveFile(Bitmap bitmap){
        File file = new File(getCacheDir(), "image_ktp.png");
        try {
            file.createNewFile();
            Log.d("taggggg", "saveFile: "+getCacheDir());
            // chache dir = /data/user/0/com.deni.kameraktp/cache
            // file dir = /data/user/0/com.deni.kameraktp/files
            Log.d("taggggg file", "saveFile: "+getFilesDir());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();
            //write the byte in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void a(){
//        File dir = new File(getCacheDir(), "image_ktp.png");
//        if (dir.exists()) {
//            for (File f : dir.listFiles()) {
//                //perform here your operation
//
//            }
//        }
        fileKtp = new File(getCacheDir(),"image_ktp.png");
        // progres bar
        binding.pbHasilUploadKtp.setVisibility(View.VISIBLE);

//        ByteArrayOutputStream bs = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bs);
//        fileKtp = bs.writeTo(fileKtp);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileKtp);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image_ktp", fileKtp.getName(), requestBody);

        int selectedId = binding.rgHvKtpJenisId.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), "3");
        RequestBody jenisIdentitas = RequestBody.create(MediaType.parse("text/plain"),radioButton.getText().toString());
        RequestBody mark1 = RequestBody.create(MediaType.parse("text/plain"), ktpStringArray[0]);
        RequestBody mark2 = RequestBody.create(MediaType.parse("text/plain"), ktpStringArray[1]);
        RequestBody mark3 = RequestBody.create(MediaType.parse("text/plain"), ktpStringArray[2]);
        RequestBody mark4 = RequestBody.create(MediaType.parse("text/plain"), ktpStringArray[3]);
        RequestBody mark5 = RequestBody.create(MediaType.parse("text/plain"), ktpStringArray[4]);
        RequestBody mark6 = RequestBody.create(MediaType.parse("text/plain"), ktpStringArray[5]);
        RequestBody mark7 = RequestBody.create(MediaType.parse("text/plain"), ktpStringArray[6]);
        RequestBody mark8 = RequestBody.create(MediaType.parse("text/plain"), ktpStringArray[7]);
        RequestBody mark9 = RequestBody.create(MediaType.parse("text/plain"), ktpStringArray[8]);
        RequestBody mark10 = RequestBody.create(MediaType.parse("text/plain"), ktpStringArray[9]);
        RequestBody mark11 = RequestBody.create(MediaType.parse("text/plain"), ktpStringArray[10]);
        RequestBody mark12 = RequestBody.create(MediaType.parse("text/plain"), ktpStringArray[11]);
        RequestBody mark13 = RequestBody.create(MediaType.parse("text/plain"), ktpStringArray[12]);
        RequestBody mark14 = RequestBody.create(MediaType.parse("text/plain"), ktpStringArray[13]);
        RequestBody mark15 = RequestBody.create(MediaType.parse("text/plain"), ktpStringArray[14]);

        Retrofit retrofit = RetrofitSingleton.getRetrofitClient(this);
        UploadKtpInterface  uploadKtpInterface = retrofit.create(UploadKtpInterface.class);

        Call<ResponseBody> call = uploadKtpInterface.uploadKtpa(
                userId,
                body,
                jenisIdentitas,
                mark1,
                mark2,
                mark3,
                mark4,
                mark5,
                mark6,
                mark7,
                mark8,
                mark9,
                mark10,
                mark11,
                mark12,
                mark13,
                mark14,
                mark15
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    binding.pbHasilUploadKtp.setVisibility(View.GONE);
                    Toast.makeText(HasilVerifikasiKtpJavaActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.pbHasilUploadKtp.setVisibility(View.GONE);
                Toast.makeText(HasilVerifikasiKtpJavaActivity.this, "gagal " +t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendKtp64(){
        // progres bar
        binding.pbHasilUploadKtp.setVisibility(View.VISIBLE);

        int selectedId = binding.rgHvKtpJenisId.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);

        String userId = "3";
        String jenisIdentitas = radioButton.getText().toString();
        String image_ktp = ktpBase64;
        String mark1 = ktpStringArray[0];
        String mark2 = ktpStringArray[1];
        String mark3 = ktpStringArray[2];
        String mark4 = ktpStringArray[3];
        String mark5 = ktpStringArray[4];
        String mark6 = ktpStringArray[5];
        String mark7 = ktpStringArray[6];
        String mark8 = ktpStringArray[7];
        String mark9 = ktpStringArray[8];
        String mark10 = ktpStringArray[9];
        String mark11 = ktpStringArray[10];
        String mark12 = ktpStringArray[11];
        String mark13 = ktpStringArray[12];
        String mark14 = ktpStringArray[13];
        String mark15 = ktpStringArray[14];

        Retrofit retrofit = RetrofitSingleton.getRetrofitClient(this);
        UploadKtpInterface  uploadKtpInterface = retrofit.create(UploadKtpInterface.class);

        Call<ResponseBody> call = uploadKtpInterface.uploadKtp(
                userId,
                image_ktp,
                jenisIdentitas,
                mark1,
                mark2,
                mark3,
                mark4,
                mark5,
                mark6,
                mark7,
                mark8,
                mark9,
                mark10,
                mark11,
                mark12,
                mark13,
                mark14,
                mark15
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    binding.pbHasilUploadKtp.setVisibility(View.GONE);
                    Toast.makeText(HasilVerifikasiKtpJavaActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.pbHasilUploadKtp.setVisibility(View.GONE);
                Toast.makeText(HasilVerifikasiKtpJavaActivity.this, "gagal " +t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
