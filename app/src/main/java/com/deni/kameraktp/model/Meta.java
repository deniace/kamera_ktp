package com.deni.kameraktp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Deni Supriyatna on 10/03/2020.
 * Email : denisupriyatna01@gmail.com
 */
public class Meta {
    @SerializedName("success")
    private Boolean success;

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("timestamp")
    private String timestamp;

    public Meta (){}

    public Meta (Boolean success, int status, String message, String timestamp){
        this.success = success;
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
