package xyz.finity.vision.libs.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dwiva on 4/29/19.
 * This code is a part of Vision project
 */
public class HomeData {

    @SerializedName("pageCount")
    private int pageCount;
    @SerializedName("annotatedImages")
    private List<AnnotatedImage> annotatedImages;

    public HomeData(int pageCount, List<AnnotatedImage> annotatedImages) {
        this.pageCount = pageCount;
        this.annotatedImages = annotatedImages;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<AnnotatedImage> getAnnotatedImages() {
        return annotatedImages;
    }

    public void setAnnotatedImages(List<AnnotatedImage> annotatedImages) {
        this.annotatedImages = annotatedImages;
    }
}
