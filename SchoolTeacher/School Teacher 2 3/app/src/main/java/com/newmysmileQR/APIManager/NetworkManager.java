package com.newmysmileQR.APIManager;


import com.newmysmileQR.APIModel.Messages;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.APIModel.SendData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class NetworkManager {

    public static void postRequest(final String url, HashMap<String, String> param, HashMap<String, String> headers, Callback<RootModel> callback) {
        Call<RootModel> call = APIClient.getClient(APIConstant.BASE_URL).create(APIInterface.class).postRequest(url, param, headers);
        call.enqueue(callback);
    }

    public static void getRequest(final String url, Callback<RootModel> callback) {
        Call<RootModel> call = APIClient.getClient(APIConstant.BASE_URL).create(APIInterface.class).getRequest(url, new HashMap<String, String>());
        call.enqueue(callback);
    }

    public static void formDataRequest(final String url, Map<String, RequestBody> param, List<MultipartBody.Part> fileList, HashMap<String, String> headers, Callback<RootModel> callback) {
        Call<RootModel> call = APIClient.getClient(APIConstant.BASE_URL).create(APIInterface.class).formDataRequest(url, param, fileList, headers);
        call.enqueue(callback);
    }

    public static void rawDataRequest(final String url, Map<String, Object> param,  HashMap<String, String> headers, Callback<RootModel> callback) {
        Call<RootModel> call = APIClient.getClient(APIConstant.BASE_URL).create(APIInterface.class).rawDataRequest(url, param, headers);
        call.enqueue(callback);
    }

    public static void send(final String url, SendData data, List<MultipartBody.Part> fileList, HashMap<String, String> headers, Callback<Messages> callback) {
        Call<Messages> call = APIClient.getClient(APIConstant.TeacherHome.SEND_OTP).create(APIInterface.class).send(url,data, headers);
        call.enqueue(callback);
    }

    public static void deleteRequest(final String url, Callback<RootModel> callback) {
        Call<RootModel> call = APIClient.getClient(APIConstant.BASE_URL).create(APIInterface.class).deleteRequest(url);
        call.enqueue(callback);
    }
}
