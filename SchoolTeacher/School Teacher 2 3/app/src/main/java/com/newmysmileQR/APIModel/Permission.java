package com.newmysmileQR.APIModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Permission {
    @SerializedName("teacher_id")
    @Expose
    private int teacherId;
    @SerializedName("permission_id")
    @Expose
    private int permissionId;
    @SerializedName("permissiondata")
    @Expose
    private Teacher teacherPermission;

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public Teacher getTeacher() {
        return teacherPermission;
    }

    public void setTeacher(Teacher permissiondata) {
        this.teacherPermission = permissiondata;
    }

    public static class Teacher {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
