package xyz.finity.vision.libs.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dwiva on 5/13/19.
 * This code is a part of Vision project
 */
public class WebEntity implements Parcelable {
    @SerializedName("description")
    private String description;

    @SerializedName("score")
    private Float score;

    public WebEntity(String description, Float score) {
        this.description = description;
        this.score = score;
    }

    protected WebEntity(Parcel in) {
        description = in.readString();
        if (in.readByte() == 0) {
            score = null;
        } else {
            score = in.readFloat();
        }
    }

    public static final Creator<WebEntity> CREATOR = new Creator<WebEntity>() {
        @Override
        public WebEntity createFromParcel(Parcel in) {
            return new WebEntity(in);
        }

        @Override
        public WebEntity[] newArray(int size) {
            return new WebEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        if (score == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(score);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }
}
