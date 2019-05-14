package xyz.finity.vision.libs.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dwiva on 4/29/19.
 * This code is a part of Vision project
 */
public class Vertices implements Parcelable{
    @SerializedName("left")
    private float left;
    @SerializedName("top")
    private float top;
    @SerializedName("right")
    private float right;
    @SerializedName("bottom")
    private float bottom;

    public Vertices(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    protected Vertices(Parcel in) {
        left = in.readFloat();
        top = in.readFloat();
        right = in.readFloat();
        bottom = in.readFloat();
    }

    public static final Creator<Vertices> CREATOR = new Creator<Vertices>() {
        @Override
        public Vertices createFromParcel(Parcel in) {
            return new Vertices(in);
        }

        @Override
        public Vertices[] newArray(int size) {
            return new Vertices[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(left);
        dest.writeFloat(top);
        dest.writeFloat(right);
        dest.writeFloat(bottom);
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }
}
