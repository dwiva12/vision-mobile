package xyz.finity.vision.libs.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dwiva on 5/13/19.
 * This code is a part of Vision project
 */
public class Web implements Parcelable {
    @SerializedName("webEntities")
    private List<WebEntity> webEntities;

    @SerializedName("fullMatchingImages")
    private List<MatchingImage> fullMatchingImages;

    @SerializedName("partialMatchingImages")
    private List<MatchingImage> partialMatchingImages;

    @SerializedName("visuallySimilarImages")
    private List<MatchingImage> visuallySimilarImages;

    @SerializedName("pages")
    private List<WebPage> webPages;

    @SerializedName("bestGuessLabels")
    private List<BestGuessLabel> bestGuessLabels;

    public Web(List<WebEntity> webEntities, List<MatchingImage> fullMatchingImages, List<MatchingImage> partialMatchingImages, List<MatchingImage> visiuallySimilarImages, List<WebPage> webPages, List<BestGuessLabel> bestGuessLabels) {
        this.webEntities = webEntities;
        this.fullMatchingImages = fullMatchingImages;
        this.partialMatchingImages = partialMatchingImages;
        this.visuallySimilarImages = visiuallySimilarImages;
        this.webPages = webPages;
        this.bestGuessLabels = bestGuessLabels;
    }

    protected Web(Parcel in) {
        webEntities = in.createTypedArrayList(WebEntity.CREATOR);
        fullMatchingImages = in.createTypedArrayList(MatchingImage.CREATOR);
        partialMatchingImages = in.createTypedArrayList(MatchingImage.CREATOR);
        visuallySimilarImages = in.createTypedArrayList(MatchingImage.CREATOR);
        webPages = in.createTypedArrayList(WebPage.CREATOR);
        bestGuessLabels = in.createTypedArrayList(BestGuessLabel.CREATOR);
    }

    public static final Creator<Web> CREATOR = new Creator<Web>() {
        @Override
        public Web createFromParcel(Parcel in) {
            return new Web(in);
        }

        @Override
        public Web[] newArray(int size) {
            return new Web[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(webEntities);
        dest.writeTypedList(fullMatchingImages);
        dest.writeTypedList(partialMatchingImages);
        dest.writeTypedList(visuallySimilarImages);
        dest.writeTypedList(webPages);
        dest.writeTypedList(bestGuessLabels);
    }

    public List<WebEntity> getWebEntities() {
        return webEntities;
    }

    public void setWebEntities(List<WebEntity> webEntities) {
        this.webEntities = webEntities;
    }

    public List<MatchingImage> getFullMatchingImages() {
        return fullMatchingImages;
    }

    public void setFullMatchingImages(List<MatchingImage> fullMatchingImages) {
        this.fullMatchingImages = fullMatchingImages;
    }

    public List<MatchingImage> getPartialMatchingImages() {
        return partialMatchingImages;
    }

    public void setPartialMatchingImages(List<MatchingImage> partialMatchingImages) {
        this.partialMatchingImages = partialMatchingImages;
    }

    public List<MatchingImage> getVisuallySimilarImages() {
        return visuallySimilarImages;
    }

    public void setVisuallySimilarImages(List<MatchingImage> visuallySimilarImages) {
        this.visuallySimilarImages = visuallySimilarImages;
    }

    public List<WebPage> getWebPages() {
        return webPages;
    }

    public void setWebPages(List<WebPage> webPages) {
        this.webPages = webPages;
    }

    public List<BestGuessLabel> getBestGuessLabels() {
        return bestGuessLabels;
    }

    public void setBestGuessLabels(List<BestGuessLabel> bestGuessLabels) {
        this.bestGuessLabels = bestGuessLabels;
    }
}
