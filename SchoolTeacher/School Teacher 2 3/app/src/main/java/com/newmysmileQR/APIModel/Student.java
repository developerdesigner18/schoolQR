package com.newmysmileQR.APIModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Student {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("mother_name")
    @Expose
    private String motherName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("ICnumber")
    @Expose
    private String iCnumber;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getICnumber() {
        return iCnumber;
    }

    public void setICnumber(String iCnumber) {
        this.iCnumber = iCnumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
