package com.newmysmileQR.APIManager;


import com.newmysmileQR.APIModel.Messages;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.APIModel.SendData;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

public interface APIInterface {

    @GET
    Call<RootModel> getRequest(@Url String url, @HeaderMap Map<String, String> headers);

    @POST
    Call<RootModel> postRequest(@Url String url, @FieldMap Map<String, String> requestParam, @HeaderMap Map<String, String> headers);


    @Multipart
    @POST
    Call<RootModel> formDataRequest(@Url String url, @PartMap() Map<String, RequestBody> requestParam, @Part List<MultipartBody.Part> fileList, @HeaderMap Map<String, String> headers);


    @POST
    Call<RootModel> rawDataRequest(@Url String url, @Body Map<String, Object> requestParam, @HeaderMap Map<String, String> headers);


    @POST
    Call<Messages> send(@Url String url, @Body SendData data, @HeaderMap Map<String, String> headers);

    @DELETE
    Call<RootModel> deleteRequest(@Url String url);

}
