package com.deni.kameraktp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import com.deni.kameraktp.databinding.ActivityVerifikasiFotoKtpJavaBinding;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

/**
 * Created by Deni Supriyatna
 */

public class VerifikasiFotoKtpJavaActivity extends AppCompatActivity {

    //4
    ActivityVerifikasiFotoKtpJavaBinding binding;
    private final String ktp_bitmap = "KTPBITMAP";
    private final String ktp_text = "KTPTEXT";
    private final String ktp_base64 = "KTPBASE64";
    private Bitmap bitmapKtp;
    private byte[] byteArray;
    private TextRecognizer textRecognizer;
    private Frame frame;
    private String textKtp = "";
    private String [] ktpStringArray = null;
    private String ktpBase64String;

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
            ktpBase64String = getIntent().getStringExtra(ktp_base64);
            // mengkonversi data bitearray menjadi bitmap/gambar
            bitmapKtp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            // menampilkan gambar ke imageView
            binding.ivVkKtpHasilCapture.setImageBitmap(bitmapKtp);
        }else{
            binding.ivVkKtpHasilCapture.setImageResource(R.drawable.camera_circle);
        }
        textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        frame = new Frame.Builder()
                        .setBitmap(bitmapKtp)
                        .build();
        SparseArray<TextBlock> textBlockSparseArray = textRecognizer.detect(frame);

//        ktpStringArray = new String[textBlockSparseArray.size()];
        ktpStringArray = new String[15];
        for(int i = 0; i < textBlockSparseArray.size(); i++){
            TextBlock textBlock = textBlockSparseArray.get(textBlockSparseArray.keyAt(i));
            textKtp += textBlock.getValue() +" \n";
            ktpStringArray[i] = textBlock.getValue();
        }
        // ini untuk scroll view menampilkan semua text
        binding.tvTextKtp.setText(textKtp);

    }

    View.OnClickListener kembali = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            bitmapKtp = null;
            byteArray = null;
            ktpStringArray = null;
            finish();
        }
    };

    View.OnClickListener gunakan = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(VerifikasiFotoKtpJavaActivity.this, HasilVerifikasiKtpJavaActivity.class);
            intent.putExtra(ktp_bitmap, byteArray);
            intent.putExtra(ktp_text, ktpStringArray);
            intent.putExtra(ktp_base64, ktpBase64String);
            startActivity(intent);
        }
    };

}

