package xyz.finity.vision.libs.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dwiva on 5/13/19.
 * This code is a part of Vision project
 */
public class MatchingImage implements Parcelable {
    @SerializedName("url")
    private String url;

    public MatchingImage(String url) {
        this.url = url;
    }

    protected MatchingImage(Parcel in) {
        url = in.readString();
    }

    public static final Creator<MatchingImage> CREATOR = new Creator<MatchingImage>() {
        @Override
        public MatchingImage createFromParcel(Parcel in) {
            return new MatchingImage(in);
        }

        @Override
        public MatchingImage[] newArray(int size) {
            return new MatchingImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
