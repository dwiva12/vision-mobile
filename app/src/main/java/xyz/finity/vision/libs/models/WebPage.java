package xyz.finity.vision.libs.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dwiva on 5/13/19.
 * This code is a part of Vision project
 */
public class WebPage implements Parcelable {
    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    public WebPage(String title, String url) {
        this.title = title;
        this.url = url;
    }

    protected WebPage(Parcel in) {
        title = in.readString();
        url = in.readString();
    }

    public static final Creator<WebPage> CREATOR = new Creator<WebPage>() {
        @Override
        public WebPage createFromParcel(Parcel in) {
            return new WebPage(in);
        }

        @Override
        public WebPage[] newArray(int size) {
            return new WebPage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(url);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
