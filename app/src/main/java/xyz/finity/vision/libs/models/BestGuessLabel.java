package xyz.finity.vision.libs.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dwiva on 5/13/19.
 * This code is a part of Vision project
 */
public class BestGuessLabel implements Parcelable {
    @SerializedName("label")
    private String label;

    @SerializedName("languageCode")
    private String languageCode;

    public BestGuessLabel(String label, String languageCode) {
        this.label = label;
        this.languageCode = languageCode;
    }

    protected BestGuessLabel(Parcel in) {
        label = in.readString();
        languageCode = in.readString();
    }

    public static final Creator<BestGuessLabel> CREATOR = new Creator<BestGuessLabel>() {
        @Override
        public BestGuessLabel createFromParcel(Parcel in) {
            return new BestGuessLabel(in);
        }

        @Override
        public BestGuessLabel[] newArray(int size) {
            return new BestGuessLabel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(languageCode);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
