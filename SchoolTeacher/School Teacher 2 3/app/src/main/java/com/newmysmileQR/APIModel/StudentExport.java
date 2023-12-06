package com.newmysmileQR.APIModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentExport {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("image")
    @Expose
    private Object image;
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
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private Integer phone;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("birthdate")
    @Expose
    private String birthdate;
    @SerializedName("islogin")
    @Expose
    private Integer islogin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Integer getIslogin() {
        return islogin;
    }

    public void setIslogin(Integer islogin) {
        this.islogin = islogin;
    }
}
