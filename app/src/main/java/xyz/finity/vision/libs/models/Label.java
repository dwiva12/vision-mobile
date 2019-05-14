package xyz.finity.vision.libs.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dwiva on 4/22/19.
 * This code is a part of Vision project
 */
public class Label implements Parcelable {

    @SerializedName("description")
    private String description;

    @SerializedName("score")
    private Float score;

    public Label(String label, float confidence) {
        this.description = label;
        this.score = confidence;
    }

    protected Label(Parcel in) {
        description = in.readString();
        if (in.readByte() == 0) {
            score = null;
        } else {
            score = in.readFloat();
        }
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

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Label> CREATOR = new Creator<Label>() {
        @Override
        public Label createFromParcel(Parcel in) {
            return new Label(in);
        }

        @Override
        public Label[] newArray(int size) {
            return new Label[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
