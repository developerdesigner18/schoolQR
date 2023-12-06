package com.newmysmileQR.APIModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("id")
    @Expose
    private Integer id;

    public Integer getDownloadable() {
        return downloadable;
    }

    public void setDownloadable(Integer downloadable) {
        this.downloadable = downloadable;
    }

    @SerializedName("downloadable")
    @Expose
    private Integer downloadable;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("q_rstudent_notificationsent")
    @Expose
    private QRstudentNotificationsent qRstudentNotificationsent;
    @SerializedName("teacher_id")
    @Expose
    private Integer teacherId;
    @SerializedName("studentNotification_id")
    @Expose
    private Integer studentNotificationId;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public QRstudentNotificationsent getQRstudentNotificationsent() {
        return qRstudentNotificationsent;
    }

    public void setQRstudentNotificationsent(QRstudentNotificationsent qRstudentNotificationsent) {
        this.qRstudentNotificationsent = qRstudentNotificationsent;
    }


    public class QRstudentNotificationsent {

        @SerializedName("teacher_id")
        @Expose
        private Integer teacherId;
        @SerializedName("studentNotification_id")
        @Expose
        private Integer studentNotificationId;

        public Integer getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(Integer teacherId) {
            this.teacherId = teacherId;
        }

        public Integer getStudentNotificationId() {
            return studentNotificationId;
        }

        public void setStudentNotificationId(Integer studentNotificationId) {
            this.studentNotificationId = studentNotificationId;
        }

    }

}
