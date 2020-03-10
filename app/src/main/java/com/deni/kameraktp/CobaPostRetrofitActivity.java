package com.deni.kameraktp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deni.kameraktp.model.MetaData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Deni Supriyatna on 10/03/2020.
 * Email : denisupriyatna01@gmail.com
 */

public class CobaPostRetrofitActivity extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba_post_retrofit);
        Button button = findViewById(R.id.btn_a);
        button.setOnClickListener(clickA);
        textView = findViewById(R.id.tv_a);
        Button button1 = findViewById(R.id.btn_model);
        button1.setOnClickListener(model);
    }

    View.OnClickListener model = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clModel();
        }
    };

    private void clModel(){
        Retrofit retrofit = RetrofitSingleton.getRetrofitClient(this);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<MetaData> result = apiInterface.getResult();

        result.enqueue(new Callback<MetaData>() {
            @Override
            public void onResponse(Call<MetaData> call, Response<MetaData> response) {
                boolean success = response.body().getMeta().getSuccess();
                int status = response.body().getMeta().getStatus();
                String message = response.body().getMeta().getMessage();
                String timestamp = response.body().getMeta().getTimestamp();
                textView.setText("success = "+success +"\n status = "+status +"\n message = " +message + "\n timestamp = " +timestamp);
            }

            @Override
            public void onFailure(Call<MetaData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    View.OnClickListener clickA = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            a();
        }
    };

    private void a(){
        Retrofit retrofit = RetrofitSingleton.getRetrofitClient(this);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.getResultAsJson();

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
//                    Toast.makeText(CobaPostRetrofitActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                    textView.setText(response.body().string());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
