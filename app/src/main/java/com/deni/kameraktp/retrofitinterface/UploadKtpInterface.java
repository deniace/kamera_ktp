package com.deni.kameraktp.retrofitinterface;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
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
    Call<ResponseBody> uploadKtpa(@Part("user_id") RequestBody user_id,
                                  @Part MultipartBody.Part file,
                                  @Part("jenis_identitas") RequestBody jenis_identitas,
                                  @Part("mark_1") RequestBody mark_1,
                                  @Part("mark_2") RequestBody mark_2,
                                  @Part("mark_3") RequestBody mark_3,
                                  @Part("mark_4") RequestBody mark_4,
                                  @Part("mark_5") RequestBody mark_5,
                                  @Part("mark_6") RequestBody mark_6,
                                  @Part("mark_7") RequestBody mark_7,
                                  @Part("mark_8") RequestBody mark_8,
                                  @Part("mark_9") RequestBody mark_9,
                                  @Part("mark_10") RequestBody mark_10,
                                  @Part("mark_11") RequestBody mark_11,
                                  @Part("mark_12") RequestBody mark_12,
                                  @Part("mark_13") RequestBody mark_13,
                                  @Part("mark_14") RequestBody mark_14,
                                  @Part("mark_15") RequestBody mark_15
                                  );

    @POST("api/verifikasi_ktp_id")
    Call<ResponseBody> uploadKtp(@Field("user_id") String user_id,
                                  @Field("image_ktp") String image_ktp,
                                  @Field("jenis_identitas") String jenis_identitas,
                                  @Field("mark_1") String mark_1,
                                  @Field("mark_2") String mark_2,
                                  @Field("mark_3") String mark_3,
                                  @Field("mark_4") String mark_4,
                                  @Field("mark_5") String mark_5,
                                  @Field("mark_6") String mark_6,
                                  @Field("mark_7") String mark_7,
                                  @Field("mark_8") String mark_8,
                                  @Field("mark_9") String mark_9,
                                  @Field("mark_10") String mark_10,
                                  @Field("mark_11") String mark_11,
                                  @Field("mark_12") String mark_12,
                                  @Field("mark_13") String mark_13,
                                  @Field("mark_14") String mark_14,
                                  @Field("mark_15") String mark_15
    );
}
