package xyz.finity.vision.libs.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dwiva on 4/22/19.
 * This code is a part of Vision project
 */
public class AnnotatedImage {

    @SerializedName("token")
    private int token;

    @SerializedName("created_at")
    private String createdAt;

    public AnnotatedImage(int token, String createdAt) {
        this.token = token;
        this.createdAt = createdAt;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
