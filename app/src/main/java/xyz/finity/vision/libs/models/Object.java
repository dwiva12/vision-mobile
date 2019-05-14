package xyz.finity.vision.libs.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dwiva on 4/29/19.
 * This code is a part of Vision project
 */
public class Object implements Parcelable {
    @SerializedName("name")
    private String name;
    @SerializedName("score")
    private float score;
    @SerializedName("vertices")
    private Vertices vertices;

    public Object(String name, float score, Vertices vertices) {
        this.name = name;
        this.score = score;
        this.vertices = vertices;
    }

    protected Object(Parcel in) {
        name = in.readString();
        score = in.readFloat();
        vertices = in.readParcelable(Vertices.class.getClassLoader());
    }

    public static final Creator<Object> CREATOR = new Creator<Object>() {
        @Override
        public Object createFromParcel(Parcel in) {
            return new Object(in);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeFloat(score);
        dest.writeParcelable(vertices, flags);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Vertices getVertices() {
        return vertices;
    }

    public void setVertices(Vertices vertices) {
        this.vertices = vertices;
    }
}
