package com.deni.kameraktp;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Deni Supriyatna on 05/03/2020.
 * Email : denisupriyatna01@gmail.com
 */
public class RetrofitSingleton {

    private static final String BASE_URL = "http://192.168.1.107/appnews/";
    private static Retrofit retrofit = null;


    public static Retrofit getRetrofitClient(Context context){
        if(retrofit == null){
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
