package com.deni.kameraktp.retrofitinterface;

import com.deni.kameraktp.model.MetaData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Deni Supriyatna on 10/03/2020.
 * Email : denisupriyatna01@gmail.com
 */
public interface ApiInterface {
    // url end point
    @GET("api/get_events_detail?id=1")
    Call<ResponseBody> getResultAsJson(); // responsebody bawaan retrofit

    @GET("api/get_events_detail?id=1")
    Call<MetaData> getResult(); // MetaData model (pojo)
}
