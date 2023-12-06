package com.newmysmileQR.APIManager;

public class APIConstant {
    //Url
    public static final String BASE_DOMAIN = "https://mysmileqr.com/";
    public static final String BASE_URL = BASE_DOMAIN + "SchoolQR/api/V2/";

    public static class Auth {
        public static String LOGIN = "logIn";
        public static String TEACHER_SIGN_UP = "teacher/teacherSignup";
        public static String PARENT_SIGN_UP = "parents/parentSignup";
        public static String TEACHER_PERMISSION = "teacher/permission";
    }

    public static class TeacherHome {
        public static String CLASS_LIST = "teacher/class";
        public static String STUDENT_LIST = "teacher/studentList";
        public static String SEARCH_STUDENT = "teacher/studentSearch";
        public static String ADD_SINGLE_STUDENT = "teacher/singlestudentAdd";
        public static String UPDATE_PROFILE = "teacher/profile";
        public static String UPLOAD_FILE = "teacher/uploadstudentData";
        public static String ADD_MULTIPLE_STUDENT = "teacher/studentBulkAdd";
        public static String NOTIFICATION_LIST = "teacher/notificationList";
        public static String STUDENT_BY_NOTIFICATION = "teacher/notificationByclass";
        public static String FORGET_PASSWORD = "teacher/forgotPassword";
        public static String SEND_NOTIFICATION = "teacher/sendNotification";
        public static String DETELE_FILE = "teacher/datadelete/";
        public static String EXPORT_STUDENT = "teacher/exportstudent";
        public static String TEACHER_DATA = "teacher/techerdata";
        public static String SEND_OTP = "https://rest.nexmo.com/";
        public static String SEND_OTP_METHOD = "sms/json";


    }

    public static class ParentHome {
        public static String UPDATE_PROFILE = "parents/updateParents";
        public static String PARENTS_DATA = "parents/parentsdata";
        public static String CHANGEPASSWORD = "parents/changePassword";
        public static String NOTIFICATION_LIST = "parents/notificationList";
        public static String FORGET_PASSWORD = "parents/forgotPassword";
        public static String SCHOOL_LIST = "parents/getDataList";
        public static String MEDIA_LIST = "parents/getparentDataList";
    }
}
