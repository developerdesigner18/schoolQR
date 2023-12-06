package com.newmysmileQR.APICall;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.newmysmileQR.APIManager.APIConstant;
import com.newmysmileQR.APIManager.NetworkManager;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.Messages;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.APIModel.SendData;
import com.newmysmileQR.Utility.Preference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherHome {


    public static void classList(final Activity context, String code, String teacherId, final ResponseCallback callback) {

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("code", RequestBody.create(code.getBytes()));
        param.put("teacher_id", RequestBody.create(teacherId.getBytes()));

        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.TeacherHome.CLASS_LIST, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();
                try {
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    if (responseObject.getClassList().size() == 0) {
                                        Preference.setClassId(context, 0);
                                    }
                                    callback.onResponse(responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));

                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("ERROR", e.getMessage());
                    Preference.setClassId(context, -1);
                }
            }

            @Override
            public void onFailure(Call<RootModel> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.toString());
                callback.onFailure(t);
                call.cancel();
                Preference.hideProcess();
            }
        });
    }

    public static void studentList(final Activity context, String code, String standardId, String pageCount, final ResponseCallback callback) {

        String teacherId = Preference.getUser(context).getId() + "";

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("code", RequestBody.create(code.getBytes()));
        param.put("standard_id", RequestBody.create(standardId.getBytes()));
        param.put("page_count", RequestBody.create(pageCount.getBytes()));
        param.put("teacher_id", RequestBody.create(teacherId.getBytes()));

//        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.TeacherHome.STUDENT_LIST, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
//                Preference.hideProcess();
                try {
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    callback.onResponse(responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RootModel> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.toString());
                callback.onFailure(t);
                call.cancel();
//                Preference.hideProcess();
            }
        });
    }

    public static void searchStudent(final Activity context, String query, String standardId, final ResponseCallback callback) {
        String teacherId = Preference.getUser(context).getId() + "";

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("standard_id", RequestBody.create(standardId.getBytes()));
        param.put("name", RequestBody.create(query.getBytes()));
        param.put("code", RequestBody.create(Preference.getUser(context).getSchool().getCode().getBytes()));
        param.put("teacher_id", RequestBody.create(teacherId.getBytes()));

        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.TeacherHome.SEARCH_STUDENT, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();
                try {
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    callback.onResponse(responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RootModel> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.toString());
                callback.onFailure(t);
                call.cancel();
                Preference.hideProcess();
            }
        });
    }

    public static void addSingleStudent(final Activity context,  String code, String standardId, String email, String birthDate, String firstName, String lastName, String fatherNme, String motherName, String phone, final ResponseCallback callback) {

        HashMap<String, RequestBody> param = new HashMap<>();
//        param.put("ICnumber", RequestBody.create(ICNumber.getBytes()));
        param.put("code", RequestBody.create(code.getBytes()));
        param.put("standard_id", RequestBody.create(standardId.getBytes()));
        param.put("email", RequestBody.create(email.getBytes()));
        param.put("birthdate", RequestBody.create(birthDate.getBytes()));
        param.put("first_name", RequestBody.create(firstName.getBytes()));
        param.put("last_name", RequestBody.create(lastName.getBytes()));
        param.put("father_name", RequestBody.create(fatherNme.getBytes()));
        param.put("mother_name", RequestBody.create(motherName.getBytes()));
        param.put("phone", RequestBody.create(phone.getBytes()));

        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.TeacherHome.ADD_SINGLE_STUDENT, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();
                try {
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    callback.onResponse(responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RootModel> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.toString());
                callback.onFailure(t);
                call.cancel();
                Preference.hideProcess();
            }
        });
    }

    public static void updateProfile(final Activity context, String code, String teacherId, String name, String address, Uri fileUri, final ResponseCallback callback) {

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("code", RequestBody.create(code.getBytes()));
        param.put("teacher_id", RequestBody.create(teacherId.getBytes()));
        param.put("name", RequestBody.create(name.getBytes()));
        param.put("address", RequestBody.create(address.getBytes()));

        ArrayList<MultipartBody.Part> files = new ArrayList<>();
        if (fileUri != null) {
            File file = new File(Preference.getFilePath(context, fileUri));
            RequestBody requestFile = RequestBody.create(file, MediaType.get("image/*"));
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            files.add(body);
        }

        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.TeacherHome.UPDATE_PROFILE, param, files, new HashMap<String, String>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();
                try {
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    callback.onResponse(responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RootModel> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.toString());
                callback.onFailure(t);
                call.cancel();
                Preference.hideProcess();
            }
        });
    }

    public static void uploadFile(final Activity context, String code, String teacherId, String studentId, String fileType, Bitmap bitmap, final ResponseCallback callback) {

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("code", RequestBody.create(code.getBytes()));
        param.put("teacher_id", RequestBody.create(teacherId.getBytes()));
        param.put("ICnumber", RequestBody.create(studentId.getBytes()));
        param.put("fileType", RequestBody.create(fileType.getBytes()));

        ArrayList<MultipartBody.Part> files = new ArrayList<>();
        if (bitmap != null) {
            try {
                //create a file to write bitmap data
                File f = new File(context.getCacheDir(), "image.png");
                f.createNewFile();

                //Convert bitmap to byte array

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();

                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
                files.add(MultipartBody.Part.createFormData("file", "image.png", reqFile));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*String filePath = Preference.getFilePath(context, fileUri);
            File file = new File(filePath);
            RequestBody requestFile = RequestBody.create(file, MediaType.get("image/*"));
            if (fileType.equalsIgnoreCase("1")) {
                requestFile = RequestBody.create(file, MediaType.get("video/*"));
            }
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            files.add(body);*/

        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.TeacherHome.UPLOAD_FILE, param, files, new HashMap<String, String>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();
                try {
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    callback.onResponse(responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                }
            }

            @Override
            public void onFailure(Call<RootModel> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.toString());
                callback.onFailure(t);
                call.cancel();
                Preference.hideProcess();
            }
        });
    }

    public static void uploadVideo(final Activity context, String code, String teacherId, String studentId, String fileType, Uri uri, final ResponseCallback callback) {

        HashMap<String, RequestBody> param = new HashMap<>();
        param.put("code", RequestBody.create(code.getBytes()));
        param.put("teacher_id", RequestBody.create(teacherId.getBytes()));
        param.put("ICnumber", RequestBody.create(studentId.getBytes()));
        param.put("fileType", RequestBody.create(fileType.getBytes()));

        String paramsdata = new Gson().toJson(param);
        Log.d("paramsdata123", "uploadVideo: " + paramsdata);

        ArrayList<MultipartBody.Part> files = new ArrayList<>();

        String filePath = Preference.getFilePath(context, uri);
        File file = null;
        if (filePath != null) {
            file = new File(filePath);
            Log.d("final_file_path1", "uploadVideo: " + file);
            Log.d("final_file_path2", "uploadVideo: " + filePath);
            Log.d("final_file_path4", "uploadVideo: " + file.getAbsolutePath());
        } else {
            Toast.makeText(context, "file is null", Toast.LENGTH_SHORT).show();
        }
        RequestBody requestFile = null;
        if (fileType.equalsIgnoreCase("1")) {
            requestFile = RequestBody.create(file, MediaType.get("video/*"));
            Log.d("final_file_path5", "uploadVideo: " + requestFile);
        }
        if (requestFile != null) {
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            Log.d("final_file_path6", "uploadVideo: " + body);
            files.add(body);
        }

        Preference.showProcess(context, "", "");

        Log.d("final_video_params", "uploadVideo: " + code + " " + teacherId + " " +
                studentId + " " + fileType + " " + files);

        NetworkManager.formDataRequest(APIConstant.TeacherHome.UPLOAD_FILE, param, files, new HashMap<>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();
                try {
                    Log.d("final_video_url", "onResponse: " + response.raw());
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    callback.onResponse(responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                    Log.d("ERROR1", "onFailure: " + responseObject);
                                }
                            } else if (response.errorBody() != null) {

                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                                Log.d("ERROR2", "onFailure: " + rootModel);
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));

                                Log.d("ERROR3", "onFailure: " + "Failed To Load Your Request Please try Again");
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                }
            }

            @Override
            public void onFailure(Call<RootModel> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.getCause());
                callback.onFailure(t);
                call.cancel();
                Preference.hideProcess();
            }
        });
    }

    public static void mediaList(final Activity context, String ICnumber, String schooltId, final ResponseCallback callback) {

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("ICnumber", RequestBody.create(ICnumber.getBytes()));
        param.put("school_id", RequestBody.create(schooltId.getBytes()));

        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.ParentHome.MEDIA_LIST, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();
                try {
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    callback.onResponse(responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RootModel> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.toString());
                callback.onFailure(t);
                call.cancel();
                Preference.hideProcess();
            }
        });
    }

    public static void addMultipleStudent(final Activity context, String code, String teacherId, Uri fileUri, final ResponseCallback callback) {

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("code", RequestBody.create(code.getBytes()));

        ArrayList<MultipartBody.Part> files = new ArrayList<>();
        if (fileUri != null) {
            File file = new File(Preference.getFilePath(context, fileUri));
            RequestBody requestFile = RequestBody.create(file, MediaType.get("application/*"));
            MultipartBody.Part body = MultipartBody.Part.createFormData("excelsheet", file.getName(), requestFile);
            files.add(body);
        }

        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.TeacherHome.ADD_MULTIPLE_STUDENT, param, files, new HashMap<String, String>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();
                try {
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    callback.onResponse(responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                }
            }

            @Override
            public void onFailure(Call<RootModel> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.toString());
                callback.onFailure(t);
                call.cancel();
                Preference.hideProcess();
            }
        });
    }

    public static void notificationList(final Activity context, String teacherId, String code, final ResponseCallback callback) {

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("teacher_id", RequestBody.create(teacherId.getBytes()));
        param.put("code", RequestBody.create(code.getBytes()));

        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.TeacherHome.NOTIFICATION_LIST, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();
                try {
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    callback.onResponse(responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RootModel> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.toString());
                callback.onFailure(t);
                call.cancel();
                Preference.hideProcess();
            }
        });
    }

    public static void studentByNotification(final Activity context, String teacherId, String standardCode, final ResponseCallback callback) {

        HashMap<String, RequestBody> param = new HashMap<>();
        param.put("teacher_id", RequestBody.create(teacherId.getBytes()));
        param.put("standard_id", RequestBody.create(standardCode.getBytes()));

        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.TeacherHome.STUDENT_BY_NOTIFICATION, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();
                String res = new Gson().toJson(response.body());
                Log.d("check_apisa", "onResponse: " + res);
                Log.d("check_apisa", "onResponse: " + response.raw());
                try {
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    callback.onResponse(responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RootModel> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.toString());
                callback.onFailure(t);
                call.cancel();
                Log.d("check_apisa", "onResponse:  failser" + t.getMessage());

                Preference.hideProcess();
            }
        });
    }

    public static void forgetPassword(final Activity context, String email, String code, final ResponseCallback callback) {

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("email", RequestBody.create(email.getBytes()));
        param.put("code", RequestBody.create(code.getBytes()));

        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.TeacherHome.FORGET_PASSWORD, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();
                try {
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    callback.onResponse(responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RootModel> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.getMessage());
                callback.onFailure(t);
                call.cancel();
                Preference.hideProcess();
            }
        });
    }

    public static void sendNotification(final Activity context, String title, String description, String standard_id, String teacher_id, String downloadble, File fileUri, final ResponseCallback callback) {

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("title", RequestBody.create(title.getBytes()));
        param.put("description", RequestBody.create(description.getBytes()));
        param.put("standard_id", RequestBody.create(standard_id.getBytes()));
        param.put("teacher_id", RequestBody.create(teacher_id.getBytes()));
        param.put("downloadable", RequestBody.create(downloadble.getBytes()));

        Log.d("final_file_data", "sendNotification: " + title + " " + description + " " + standard_id + " " + teacher_id + " " + downloadble);

        ArrayList<MultipartBody.Part> files = new ArrayList<>();
            String mediaType;
            if (fileUri.getName().endsWith(".pdf")) {
                mediaType = "application/pdf";
            } else {
                mediaType = "image/*";
            }

            RequestBody requestFile = RequestBody.create(fileUri, MediaType.parse(mediaType));
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", fileUri.getName(), requestFile);
            files.add(body);
                Log.d("chjec_dataa", "sendNotification: "+fileUri.getName());
                String dewd = new Gson().toJson(files);
                String ddde = new Gson().toJson(body);

        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.TeacherHome.SEND_NOTIFICATION, param, files, new HashMap<String, String>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();
                try {
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    callback.onResponse(responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RootModel> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.toString());
                callback.onFailure(t);
                call.cancel();
                Preference.hideProcess();
            }
        });
    }

    public static void deleteFile(final Activity context, String id, final ResponseCallback callback) {

        Preference.showProcess(context, "", "");

        NetworkManager.deleteRequest(APIConstant.TeacherHome.DETELE_FILE + id, new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();
                try {
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    callback.onResponse(responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RootModel> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.toString());
                callback.onFailure(t);
                call.cancel();
                Preference.hideProcess();
            }
        });
    }

    public static void studentExport(final Activity context, String code, String standardId, final ResponseCallback callback) {

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("code", RequestBody.create(code.getBytes()));
        param.put("standard_id", RequestBody.create(standardId.getBytes()));

        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.TeacherHome.EXPORT_STUDENT, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();
                try {
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    callback.onResponse(responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RootModel> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.toString());
                callback.onFailure(t);
                call.cancel();
                Preference.hideProcess();
            }
        });
    }

    public static void teacherData(final Activity context, final ResponseCallback callback) {

        String code = Preference.getUser(context).getId() + "";

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("teacher_id", RequestBody.create(code.getBytes()));

        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.TeacherHome.TEACHER_DATA, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();
                try {
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    callback.onResponse(responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                }
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RootModel> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.toString());
                callback.onFailure(t);
                call.cancel();
                Preference.hideProcess();
            }
        });
    }

    public static void sendOTP(final Activity context, String mobile, String otp, final ResponseCallback callback) {

        /*HashMap<String, SendData> param = new HashMap<String, SendData>();
        param.put("teacher_id", RequestBody.create("cc53630a".getBytes()));
        param.put("api_secret", RequestBody.create("7bc3935f".getBytes()));
        param.put("to", RequestBody.create(mobile.getBytes()));
        param.put("from", RequestBody.create("NEXMO".getBytes()));
        param.put("text", RequestBody.create(otp.getBytes()));*/

        SendData data = new SendData();
        data.setApi_key("cc53630a");
        data.setApi_secret("7bc3935f");
        data.setTo(mobile);
        data.setFrom("NEXMO");
        data.setText("Dear user, your registration OTP is " + otp);


        Preference.showProcess(context, "", "");

        NetworkManager.send(APIConstant.TeacherHome.SEND_OTP_METHOD, data, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<Messages>() {
            @Override
            public void onResponse(Call<Messages> call, Response<Messages> response) {
                Preference.hideProcess();
                try {
                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                Messages responseObject = response.body();
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            } else {
                                callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            }
                            break;
                        }
                        case 422: {
                            if (response.isSuccessful()) {
                                Messages responseObject = response.body();
                            } else if (response.errorBody() != null) {
                                RootModel rootModel = new Gson().fromJson(response.errorBody().string(), RootModel.class);
                                callback.onFailure(new Throwable(rootModel.getMessage()));
                            }
                            break;
                        }
                        default: {
                            callback.onFailure(new Throwable("Failed To Load Your Request Please try Again"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Messages> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.toString());
                callback.onFailure(t);
                call.cancel();
                Preference.hideProcess();
            }
        });
    }


}
