package xyz.finity.vision.libs.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dwiva on 4/29/19.
 * This code is a part of Vision project
 */
public class VisionData {
    @SerializedName("objects")
    private List<Object> objects;
    @SerializedName("labels")
    private List<Label> labels;
    @SerializedName("web")
    private Web web;
    @SerializedName("textAnnotation")
    private OCR ocr;

    public VisionData(List<Object> objects, List<Label> labels, Web web, OCR ocr) {
        this.objects = objects;
        this.labels = labels;
        this.web = web;
        this.ocr = ocr;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public Web getWeb() {
        return web;
    }

    public void setWeb(Web web) {
        this.web = web;
    }

    public OCR getOcr() {
        return ocr;
    }

    public void setOcr(OCR ocr) {
        this.ocr = ocr;
    }
}
