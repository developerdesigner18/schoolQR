package com.newmysmileQR.Utility.Notification;

import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("body")
    public String body;

    @SerializedName("title")
    public String title;

    @SerializedName("url")
    public String url;

    @SerializedName("customData")
    public Data customData;

    public static class Data {

        @SerializedName("userType")
        public int userType;

    }
}
