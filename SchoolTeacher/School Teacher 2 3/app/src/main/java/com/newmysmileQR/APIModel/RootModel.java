package com.newmysmileQR.APIModel;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class RootModel {

    @SerializedName("data")
    private JsonElement mData;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private boolean success;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public User getUser() {
        return new Gson().fromJson(mData, User.class);
    }

    public ArrayList<ClassModel> getClassList() {
        return new Gson().fromJson(mData, new TypeToken<ArrayList<ClassModel>>() {
        }.getType());
    }

    public ArrayList<Student> getStudentList() {
        return new Gson().fromJson(mData, new TypeToken<ArrayList<Student>>() {
        }.getType());
    }

    public ArrayList<Permission> getPermissions() {
        return new Gson().fromJson(mData, new TypeToken<ArrayList<Permission>>() {
        }.getType());
    }

    public Media getMedia() {
        return new Gson().fromJson(mData, Media.class);
    }

    public Image getImage() {
        return new Gson().fromJson(mData, Image[].class)[0];
    }

    public Video getVideo() {
        return new Gson().fromJson(mData, Video[].class)[0];
    }

    public ArrayList<Notification> getNotification() {
        return new Gson().fromJson(mData, new TypeToken<ArrayList<Notification>>() {
        }.getType());
    }

    public ArrayList<StudentByNotification> getStudNotification() {
        return new Gson().fromJson(mData, new TypeToken<ArrayList<StudentByNotification>>() {
        }.getType());
    }

    public ForgetPassword getForget() {
        return new Gson().fromJson(mData, ForgetPassword.class);
    }

    public ArrayList<SchoolList> getSchoolList() {
        return new Gson().fromJson(mData, new TypeToken<ArrayList<SchoolList>>() {
        }.getType());
    }

    public ArrayList<StudentExport> getExportStudent() {
        return new Gson().fromJson(mData, new TypeToken<ArrayList<StudentExport>>() {
        }.getType());
    }
}
