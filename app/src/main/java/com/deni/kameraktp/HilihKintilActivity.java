package com.deni.kameraktp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.deni.kameraktp.databinding.ActivityHilihKintilBinding;

public class HilihKintilActivity extends AppCompatActivity {

    ActivityHilihKintilBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hilih_kintil);
        setTitle(R.string.hilih_kintil);
        binding.btnProsesHilih.setOnClickListener(hilihKintil);
    }

    private String strReplace(String word){
        String regexKecil = "[aueo]";
        String regexBesar = "[AUEO]";
        String output1 = "";
        String output2 = "";
        output1 = word.replaceAll(regexKecil, "i");
        output2 = output1.replaceAll(regexBesar, "I");
        return output2;
    }

    View.OnClickListener hilihKintil = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String hasil = "";
            hasil = strReplace(binding.tietInputHilih.getText().toString());
            binding.tietOutputHilih.setText(hasil);
        }
    };
}
