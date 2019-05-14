package xyz.finity.vision.libs.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dwiva on 5/9/19.
 * This code is a part of Vision project
 */
public class Block implements Parcelable {
    @SerializedName("text")
    private String text;
    @SerializedName("vertices")
    private Vertices vertices;

    public Block(String text, Vertices vertices) {
        this.text = text;
        this.vertices = vertices;
    }

    protected Block(Parcel in) {
        text = in.readString();
        vertices = in.readParcelable(Vertices.class.getClassLoader());
    }

    public static final Creator<Block> CREATOR = new Creator<Block>() {
        @Override
        public Block createFromParcel(Parcel in) {
            return new Block(in);
        }

        @Override
        public Block[] newArray(int size) {
            return new Block[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeParcelable(vertices, flags);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Vertices getVertices() {
        return vertices;
    }

    public void setVertices(Vertices vertices) {
        this.vertices = vertices;
    }
}
