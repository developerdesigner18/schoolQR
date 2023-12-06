package com.newmysmileQR.APIModel;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassModel {
    @SerializedName("teacher_id")
    @Expose
    private int teacherId;
    @SerializedName("standard_id")
    @Expose
    private int standardId;
    @SerializedName("standard_data")
    @Expose
    private StandardData standardData;

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getStandardId() {
        return standardId;
    }

    public void setStandardId(int standardId) {
        this.standardId = standardId;
    }

    public StandardData getStandardData() {
        return standardData;
    }

    public void setStandardData(StandardData standardData) {
        this.standardData = standardData;
    }

    public static class StandardData{

        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("status")
        @Expose
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        @NonNull
        @Override
        public String toString() {
            return title;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return getStandardData().getTitle();
    }
}
