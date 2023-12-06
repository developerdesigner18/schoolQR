package com.newmysmileQR.APIModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchoolList {
    @SerializedName("school_id")
    @Expose
    private Integer schoolId;
    @SerializedName("school_name")
    @Expose
    private SchoolName schoolName;

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public SchoolName getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(SchoolName schoolName) {
        this.schoolName = schoolName;
    }

    public class SchoolName {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
