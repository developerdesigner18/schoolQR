package com.newmysmileQR.APICall;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.newmysmileQR.APIManager.APIConstant;
import com.newmysmileQR.APIManager.NetworkManager;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.Utility.Preference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentHome {
    public static void updateProfile(final Activity context, String teacherId, String FirstName, String LastName, String FatherName, String MotherName, String birthdate, String address, Uri fileUri, final ResponseCallback callback) {

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("student_id", RequestBody.create(teacherId.getBytes()));
        param.put("first_name", RequestBody.create(FirstName.getBytes()));
        param.put("last_name", RequestBody.create(LastName.getBytes()));
        param.put("father_name", RequestBody.create(FatherName.getBytes()));
        param.put("mother_name", RequestBody.create(MotherName.getBytes()));
        param.put("address", RequestBody.create(address.getBytes()));
        param.put("birthdate", RequestBody.create(birthdate.getBytes()));

        ArrayList<MultipartBody.Part> files = new ArrayList<>();
        if (fileUri != null) {
            File file = new File(Preference.getFilePath(context, fileUri));
            RequestBody requestFile = RequestBody.create(file, MediaType.get("image/*"));
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            files.add(body);
        }

        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.ParentHome.UPDATE_PROFILE, param, files, new HashMap<String, String>(), new Callback<RootModel>() {
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

    public static void changePassword(final Activity context, String oldPassword, String newlPassword, final ResponseCallback callback) {

        String id = String.valueOf(Preference.getUser(context).getId());
        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("student_id", RequestBody.create(id.getBytes()));
        param.put("oldpassword", RequestBody.create(oldPassword.getBytes()));
        param.put("newpassword", RequestBody.create(newlPassword.getBytes()));

        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.ParentHome.CHANGEPASSWORD, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
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

    public static void notificationList(final Activity context, final ResponseCallback callback) {

        String id = String.valueOf(Preference.getUser(context).getId());
        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
            param.put("student_id", RequestBody.create(id.getBytes()));

        Log.d("student_id", "onResponse: " + id);
        Preference.showProcess(context, "", "");
        NetworkManager.formDataRequest(APIConstant.ParentHome.NOTIFICATION_LIST, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();

                String final_data = new Gson().toJson(response.body());
                Log.d("final_response1", "onResponse: " + response.raw());
                Log.d("final_response2", "onResponse: " + final_data);
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

    public static void forgetPassword(final Activity context, String email, String ICnumber, final ResponseCallback callback) {

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("email", RequestBody.create(email.getBytes()));
        param.put("ICnumber", RequestBody.create(ICnumber.getBytes()));

        Preference.showProcess(context, "", "");
        NetworkManager.formDataRequest(APIConstant.ParentHome.FORGET_PASSWORD, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
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

    public static void getSchool(final Activity context, String ICnumber, final ResponseCallback callback) {


        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("ICnumber", RequestBody.create(ICnumber.getBytes()));

        Log.d("getschooldada", "getSchool: " + ICnumber);

        Preference.showProcess(context, "", "");
        NetworkManager.formDataRequest(APIConstant.ParentHome.SCHOOL_LIST, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
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

    public static void parentsData(final Activity context, final ResponseCallback callback) {

        String code = Preference.getUser(context).getId() + "";

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("student_id", RequestBody.create(code.getBytes()));
        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.ParentHome.PARENTS_DATA, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
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

}
