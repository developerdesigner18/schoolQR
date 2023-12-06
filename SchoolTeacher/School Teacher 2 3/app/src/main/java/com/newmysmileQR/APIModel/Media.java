package com.newmysmileQR.APIModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Media {
    @SerializedName("image")
    @Expose
    private ArrayList<Image> image = new ArrayList<>();
    @SerializedName("video")
    @Expose
    private ArrayList<Video> video = new ArrayList<>();

    public ArrayList<Image> getImage() {
        return image;
    }

    public void setImage(ArrayList<Image> image) {
        this.image = image;
    }

    public ArrayList<Video> getVideo() {
        return video;
    }

    public void setVideo(ArrayList<Video> video) {
        this.video = video;
    }
}
