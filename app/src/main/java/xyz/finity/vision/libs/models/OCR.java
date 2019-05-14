package xyz.finity.vision.libs.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dwiva on 5/9/19.
 * This code is a part of Vision project
 */
public class OCR implements Parcelable {
    @SerializedName("text")
    private String text;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;
    @SerializedName("blocks")
    private List<Block> blocks;

    public OCR(String text, int width, int height, List<Block> blocks) {
        this.text = text;
        this.width = width;
        this.height = height;
        this.blocks = blocks;
    }

    protected OCR(Parcel in) {
        text = in.readString();
        width = in.readInt();
        height = in.readInt();
        blocks = in.createTypedArrayList(Block.CREATOR);
    }

    public static final Creator<OCR> CREATOR = new Creator<OCR>() {
        @Override
        public OCR createFromParcel(Parcel in) {
            return new OCR(in);
        }

        @Override
        public OCR[] newArray(int size) {
            return new OCR[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeTypedList(blocks);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }
}
