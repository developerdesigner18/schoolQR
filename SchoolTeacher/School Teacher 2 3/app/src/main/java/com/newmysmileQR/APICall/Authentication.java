package com.newmysmileQR.APICall;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.newmysmileQR.APIManager.APIConstant;
import com.newmysmileQR.APIManager.NetworkManager;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.Utility.Preference;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Authentication {

    public static void login(final Activity context,String  type,String ICnumber, String code, String email, String password, String fcmToken, String deviceType, final ResponseCallback callback) {

       /* HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("ICnumber", RequestBody.create(MediaType.parse("text/plain"), code));
        param.put("email", RequestBody.create(MediaType.parse("text/plain"), email));
        param.put("password", RequestBody.create(MediaType.parse("text/plain"), password));
        param.put("notification_token", RequestBody.create(MediaType.parse("text/plain"),  Preference.getFCMToken(context)));
        param.put("device_type", RequestBody.create(MediaType.parse("text/plain"), "0"));*/

        HashMap<String, Object> param = new HashMap<String, Object>();


//check it parent
//        param.put("ICnumber", ICnumber);
        if(type.equalsIgnoreCase("teacher")){
            param.put("notification_token", fcmToken);
            param.put("device_type", deviceType);
            param.put("schoolcode", code);
            param.put("email", email);
            param.put("password", password);
            param.put("type",type);
        }else{
            param.put("notification_token", fcmToken);
            param.put("device_type", deviceType);
            param.put("email", email);
            param.put("password", password);
            param.put("type",type);
        }

        Preference.showProcess(context, "", "");

        Log.d("logindata", "login: " + param);

        NetworkManager.rawDataRequest(APIConstant.Auth.LOGIN, param, new HashMap<String, String>(), new Callback<RootModel>() {
            @Override
            public void onResponse(Call<RootModel> call, Response<RootModel> response) {
                Preference.hideProcess();
                try {
                    Log.d("check_logonn", "onResponse: "+response.raw());

                    switch (response.code()) {
                        case 200: {
                            if (response.isSuccessful()) {
                                RootModel responseObject = response.body();
                                if (responseObject != null && responseObject.isSuccess()) {
                                    callback.onResponse(responseObject);
                                    Log.d("check_logonn if ", "onResponse: "+responseObject);
                                } else if (responseObject != null && responseObject.getMessage() != null) {
                                    callback.onFailure(new Throwable(responseObject.getMessage()));
                                    Log.d("check_logonn if ", "onResponse: else "+responseObject);

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

    public static void teacherSignUp(final Activity context, String code, String name, String email, String phone, String password, final ResponseCallback callback) {

        HashMap<String, Object> param = new HashMap<>();
        param.put("notification_token", FirebaseInstanceId.getInstance().getToken());
        param.put("device_type", "0");
        param.put("email", email);
        param.put("code", code);
        param.put("name", name);
        param.put("phone", phone);
        param.put("password", password);

        Preference.showProcess(context, "", "");

        NetworkManager.rawDataRequest(APIConstant.Auth.TEACHER_SIGN_UP,param, new HashMap<String, String>(), new Callback<RootModel>() {
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

    public static void parentSignUp(final Activity context, String ICNumber, String code, String name, String email, String phone, String password, String birthDate, final ResponseCallback callback) {

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("ICnumber", RequestBody.create(ICNumber.getBytes()));
        param.put("email", RequestBody.create(email.getBytes()));
        param.put("code", RequestBody.create(code.getBytes()));
        param.put("name", RequestBody.create(name.getBytes()));
        param.put("phone", RequestBody.create(phone.getBytes()));
        param.put("password", RequestBody.create(password.getBytes()));
        param.put("birthdate", RequestBody.create(birthDate.getBytes()));

        Preference.showProcess(context, "", "");

        NetworkManager.formDataRequest(APIConstant.Auth.PARENT_SIGN_UP, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
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

    public static void getPermission(final Activity context, String code, String teacherId, final ResponseCallback callback) {

        HashMap<String, RequestBody> param = new HashMap<String, RequestBody>();
        param.put("code", RequestBody.create(code.getBytes()));
        param.put("teacher_id", RequestBody.create(teacherId.getBytes()));

        /*Preference.showProcess(context, "", "");*/

        NetworkManager.formDataRequest(APIConstant.Auth.TEACHER_PERMISSION, param, new ArrayList<MultipartBody.Part>(), new HashMap<String, String>(), new Callback<RootModel>() {
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
