package com.deni.kameraktp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Deni Supriyatna on 10/03/2020.
 * Email : denisupriyatna01@gmail.com
 */
public class UploadKtp {
    @SerializedName("responce")
    private Boolean responce;

    @SerializedName("data")
    private String data;

    public Boolean getResponce() {
        return responce;
    }

    public void setResponce(Boolean responce) {
        this.responce = responce;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
