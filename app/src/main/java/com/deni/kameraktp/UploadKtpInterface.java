package com.deni.kameraktp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Deni Supriyatna on 05/03/2020.
 * Email : denisupriyatna01@gmail.com
 */
public interface UploadKtpInterface {
    @Multipart
    @POST("api/verifikasi_ktp")
    Call<ResponseBody> uploadKtp(@Part MultipartBody.Part file, @Part("name")RequestBody requestBody);
}
