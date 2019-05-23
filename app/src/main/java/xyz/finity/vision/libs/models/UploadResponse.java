package xyz.finity.vision.libs.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dwiva on 4/22/19.
 * This code is a part of Vision project
 */
public class UploadResponse {

    @SerializedName("success")
    private Boolean isSuccess;

    @SerializedName("imagetoken")
    private int token;

    public UploadResponse(Boolean isSuccess, int token) {
        this.isSuccess = isSuccess;
        this.token = token;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }
}
